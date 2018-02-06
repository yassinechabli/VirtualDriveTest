package com.example.toshiba.virtualdt;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FullInfoActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    TelephonyManager manager;
    List<CellInfo> cellInfos;
    Cell cell;
    TextView operatorName,IMEINumber,subscriberID,softwareVersion;
    TextView mcc,mnc,networktype,lac,tac,cellid,lontlat,simserialnumber;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        operatorName=(TextView)findViewById(R.id.OperatorName);
        mcc=(TextView)findViewById(R.id.mcc);
        networktype=(TextView)findViewById(R.id.cellnotworkType);
        mnc=(TextView)findViewById(R.id.mcc);
        lac=(TextView)findViewById(R.id.lac);
        tac=(TextView)findViewById(R.id.tac);
        cellid=(TextView)findViewById(R.id.cellid);
        lontlat=(TextView)findViewById(R.id.latlan);
        simserialnumber=(TextView)findViewById(R.id.simserialnumber);
        IMEINumber=(TextView)findViewById(R.id.imei);
        subscriberID=(TextView)findViewById(R.id.subscriberId);
        softwareVersion=(TextView)findViewById(R.id.softwareVersion);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        cell=new Cell();
        manager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        cellInfos=manager.getAllCellInfo();
        simserialnumber.setText(manager.getSimSerialNumber());
        IMEINumber.setText(manager.getDeviceId());
        subscriberID.setText(manager.getSubscriberId());
        softwareVersion.setText(manager.getDeviceSoftwareVersion());



        if(manager.getNetworkOperatorName().equals("IAM")){
            Drawable drawable= getResources().getDrawable(R.drawable.iam);
            collapsingToolbarLayout.setBackground(drawable);
        }
        else if(manager.getNetworkOperatorName().equals("Orange")){
            Drawable drawable= getResources().getDrawable(R.drawable.orange);
            collapsingToolbarLayout.setBackground(drawable);        }
        else if (manager.getNetworkOperatorName().equals("inwi")){
            Drawable drawable= getResources().getDrawable(R.drawable.inwi);
            collapsingToolbarLayout.setBackground(drawable);
        }

        for(CellInfo cellInfo:cellInfos){

        if(cellInfo instanceof CellInfoLte){
            CellIdentityLte cellInfoLte=((CellInfoLte) cellInfo).getCellIdentity();
            cellid.setText( String.valueOf(((CellInfoLte) cellInfo).getCellIdentity().getCi()));
            mnc.setText(String.valueOf(((CellInfoLte) cellInfo).getCellIdentity().getMnc()));
            mcc.setText(String.valueOf(((CellInfoLte) cellInfo).getCellIdentity().getMcc()));
            tac.setText(String.valueOf(((CellInfoLte) cellInfo).getCellIdentity().getTac()));
            lac.setText("cet element n'existe pas pour le reseau LTE");
        }
        else if(cellInfo instanceof CellInfoWcdma ){
            CellIdentityWcdma cellIdentityWcdma= ((CellInfoWcdma) cellInfo).getCellIdentity();
            cellid.setText(String.valueOf(((CellInfoWcdma) cellInfo).getCellIdentity().getCid()));
            mnc.setText(String.valueOf(((CellInfoWcdma) cellInfo).getCellIdentity().getMnc()));
            mcc.setText(String.valueOf(((CellInfoWcdma) cellInfo).getCellIdentity().getMcc()));
            lac.setText(String.valueOf(((CellInfoWcdma) cellInfo).getCellIdentity().getLac()));
            tac.setText("cet element n'existe pas pour le reseau UMTS/GSM");

        }
         else if(cellInfo instanceof CellInfoGsm){
            CellIdentityGsm cellidentitygsm=((CellInfoGsm) cellInfo).getCellIdentity();
            lac.setText(String.valueOf(cellidentitygsm.getLac()));
            mnc.setText(String.valueOf(cellidentitygsm.getMnc()));
            mcc.setText(String.valueOf(cellidentitygsm.getMcc()));
            cellid.setText(String.valueOf(cellidentitygsm.getCid()));
            tac.setText("cet element n'existe pas pour le reseau UMTS/GSM");
        }
            break;
        }


        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //static data
        operatorName.setText(manager.getNetworkOperatorName().toString()+","+manager.getSimOperatorName());
//        mcc.setText(Integer.parseInt(manager.getNetworkOperator().substring(0, 3)));
//        mnc.setText(Integer.parseInt(manager.getNetworkOperator().substring(3)));

        //dynamique data
        int networktypevalue=manager.getNetworkType();
        switch (networktypevalue) {
            case TelephonyManager.NETWORK_TYPE_1xRTT: networktype.setText("1xRTT") ;break;
            case TelephonyManager.NETWORK_TYPE_CDMA: networktype.setText("CDMA") ;break;
            case TelephonyManager.NETWORK_TYPE_EDGE: networktype.setText("EDGE") ;break;
            case TelephonyManager.NETWORK_TYPE_EHRPD: networktype.setText("eHRPD") ;break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0: networktype.setText("EVDO rev. 0") ;break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A: networktype.setText("EVDO rev. A") ;break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B: networktype.setText("EVDO rev. B") ;break;
            case TelephonyManager.NETWORK_TYPE_GPRS: networktype.setText("GPRS") ;break;
            case TelephonyManager.NETWORK_TYPE_HSDPA: networktype.setText("HSDPA") ;break;
            case TelephonyManager.NETWORK_TYPE_HSPA: networktype.setText("HSPA") ;break;
            case TelephonyManager.NETWORK_TYPE_HSPAP: networktype.setText("HSPA+") ;break;
            case TelephonyManager.NETWORK_TYPE_HSUPA: networktype.setText("HSUPA") ;break;
            case TelephonyManager.NETWORK_TYPE_IDEN: networktype.setText("iDen") ;break;
            case TelephonyManager.NETWORK_TYPE_LTE: networktype.setText("LTE") ;break;
            case TelephonyManager.NETWORK_TYPE_UMTS: networktype.setText("UMTS") ;break;
            default:
            case TelephonyManager.NETWORK_TYPE_UNKNOWN: networktype.setText(" Unknow") ;break;
        }
        lontlat.setText(cell.getLon()+","+cell.getLat());
        mHandler.postDelayed(mRunnable, 1000);
    }



    @Override
    protected void onStart() {
        super.onStart();
        BackgroundAsyncTask bgasyn=null;
        try {
            bgasyn=new BackgroundAsyncTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bgasyn.execute();
    }



    class BackgroundAsyncTask extends AsyncTask<Void, Void, Void> {
        protected XmlPullParserFactory xmlPullParserFactory;
        protected XmlPullParser parser;

        BackgroundAsyncTask() throws Exception {
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(false);
            parser = xmlPullParserFactory.newPullParser();
        }

        @Override
        protected Void doInBackground(Void... params) {
            TelephonyManager manager ;
            manager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

            int networktypevalue=manager.getNetworkType();
            switch (networktypevalue) {
                case TelephonyManager.NETWORK_TYPE_1xRTT: cell.setMethod_access("1xRTT") ;break;
                case TelephonyManager.NETWORK_TYPE_CDMA: cell.setMethod_access("CDMA") ;break;
                case TelephonyManager.NETWORK_TYPE_EDGE: cell.setMethod_access("EDGE") ;break;
                case TelephonyManager.NETWORK_TYPE_EHRPD: cell.setMethod_access("eHRPD") ;break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0: cell.setMethod_access("EVDO rev. 0") ;break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A: cell.setMethod_access("EVDO rev. A") ;break;
                case TelephonyManager.NETWORK_TYPE_EVDO_B: cell.setMethod_access("EVDO rev. B") ;break;
                case TelephonyManager.NETWORK_TYPE_GPRS: cell.setMethod_access("GPRS") ;break;
                case TelephonyManager.NETWORK_TYPE_HSDPA: cell.setMethod_access("HSDPA") ;break;
                case TelephonyManager.NETWORK_TYPE_HSPA: cell.setMethod_access("HSPA") ;break;
                case TelephonyManager.NETWORK_TYPE_HSPAP: cell.setMethod_access("HSPA+") ;break;
                case TelephonyManager.NETWORK_TYPE_HSUPA: cell.setMethod_access("HSUPA") ;break;
                case TelephonyManager.NETWORK_TYPE_IDEN: cell.setMethod_access("iDen") ;break;
                case TelephonyManager.NETWORK_TYPE_LTE: cell.setMethod_access("LTE") ;break;
                case TelephonyManager.NETWORK_TYPE_UMTS: cell.setMethod_access("UMTS") ;break;
                default:
                case TelephonyManager.NETWORK_TYPE_UNKNOWN: cell.setMethod_access(" Unknow") ;break;
            }
            for(CellInfo cellInfo:cellInfos){

                if(cellInfo instanceof CellInfoLte){
                    cell.setMethod_access("LTE");
                    
                    CellIdentityLte cellInfoLte=((CellInfoLte) cellInfo).getCellIdentity();
                    cell.setId(((CellInfoLte) cellInfo).getCellIdentity().getCi());
                    cell.setMnc(((CellInfoLte) cellInfo).getCellIdentity().getMnc());
                    cell.setMcc(((CellInfoLte) cellInfo).getCellIdentity().getMcc());
                    cell.setTac(((CellInfoLte) cellInfo).getCellIdentity().getTac());
                    cell.setLac(cell.getTac());
                }
                else if(cellInfo instanceof CellInfoWcdma ){
                    cell.setMethod_access("UMTS");
                    CellIdentityWcdma cellIdentityWcdma= ((CellInfoWcdma) cellInfo).getCellIdentity();
                    cell.setId(((CellInfoWcdma) cellInfo).getCellIdentity().getCid());
                    cell.setMnc(((CellInfoWcdma) cellInfo).getCellIdentity().getMnc());
                    cell.setMcc(((CellInfoWcdma) cellInfo).getCellIdentity().getMcc());
                    cell.setLac(((CellInfoWcdma) cellInfo).getCellIdentity().getLac());

                }
                else if(cellInfo instanceof CellInfoGsm){
                    cell.setMethod_access("GSM");
                    CellIdentityGsm cellidentitygsm=((CellInfoGsm) cellInfo).getCellIdentity();
                    cell.setLac(cellidentitygsm.getLac());
                    cell.setMnc(cellidentitygsm.getMnc());
                    cell.setMcc(cellidentitygsm.getMcc());
                    cell.setId(cellidentitygsm.getCid());
                }
                break;
            }


            URL url = null;
            String returnedResult = "";
            try {
                url = new URL("http://opencellid.org/cell/get?mcc="+cell.getMcc()+"&mnc="+cell.getMnc()+"&cellid="+cell.getId()+"&lac="+cell.getTac()+"&key=3216e3b6-f212-413e-83f8-b05adb8e04cc");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(2000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                parser.setInput(is, null);
                returnedResult = getLoadedXmlValues(parser);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(cell!=null){
                //displayXmlContent.setText(cell.getLat()+" ; "+cell.getLon());
            }
        }

        private String getLoadedXmlValues(XmlPullParser parser) throws XmlPullParserException, IOException {
            int eventType = parser.getEventType();
            String name = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    name = parser.getName();
                    cell.setLat(parser.getAttributeValue(null, "lat"));
                    cell.setLon(parser.getAttributeValue(null, "lon"));
                    cell.setAverageSignalStrength(parser.getAttributeValue(null, "averageSignalStrength"));
                    cell.setRadio(parser.getAttributeValue(null,"radio"));
                    //cell.setPci(parser.getAttributeValue(null,"pci"));
                    cell.setChangeable(parser.getAttributeValue(null,"radio"));
                    cell.setRange(parser.getAttributeValue(null,"range"));

                }
                eventType = parser.next();
            }
            return cell.getLat() + " ;" + cell.getLon();
        }

    }



    private final Runnable mRunnable = new Runnable() {
        public void run() {
            BackgroundAsyncTask bgasyn=null;
            try {
                bgasyn=new BackgroundAsyncTask();
            } catch (Exception e) {
                e.printStackTrace();
            }
            bgasyn.execute();
            lontlat.setText(cell.getLon()+","+cell.getLat());
            mHandler.postDelayed(mRunnable, 1000);
        }
    };
}

