package com.example.toshiba.virtualdt;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    private ArrayList<Location> lieux = new ArrayList<Location>();
    private ArrayList<Marker> markers=new ArrayList<Marker>();
    MyPhoneStateListener mPhoneStatelistener;
    List<CellInfo> cellInfos;

    TelephonyManager manager;
    private GoogleMap mMap;
    private Marker mMarker;
    private ArrayList<Circle> circles = new ArrayList<Circle>();
    private ArrayList<Polyline> polylines = new ArrayList<Polyline>();
    Cell cell = new Cell();
    GoogleMap.OnMyLocationButtonClickListener mylocationbtn = new GoogleMap.OnMyLocationButtonClickListener() {
        @Override
        public boolean onMyLocationButtonClick() {
            //Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if(!markers.isEmpty()){
                for(Marker m:markers){
                    m.remove();
                }
            }
            lieux.add(location);
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if(!lieux.isEmpty()) {
                //Toast.makeText(getApplicationContext(),lieux.get(lieux.size()-1).toString(),Toast.LENGTH_SHORT).show();
               if(cell.getCellSignalStrengthDbm() >= -90){
                   mMap.addPolyline(new PolylineOptions()
                           .add(loc, loc)
                           .width(10)
                           .color(Color.GREEN)
                           .geodesic(true));

               }
                else if(cell.getCellSignalStrengthDbm() < -90 && cell.getCellSignalStrengthDbm() >= -97){
                   mMap.addPolyline(new PolylineOptions()
                           .add(loc, loc)
                           .width(10)
                           .color(Color.YELLOW)
                           .geodesic(true));
               }
                else if (cell.getCellSignalStrengthDbm() < -97 && cell.getCellSignalStrengthDbm() >= -103) {
                    mMap.addPolyline(new PolylineOptions()
                            .add(loc, loc)
                            .width(10)
                            .color(Color.argb(100, 255, 127, 80))
                            .geodesic(true));
               }
                else {
                   mMap.addPolyline(new PolylineOptions()
                           .add(loc, loc)
                           .width(10)
                           .color(Color.RED)
                           .geodesic(true));

                }

            }


            BackgroundAsyncTask bgasyn = null;
            try {
                bgasyn = new BackgroundAsyncTask();
            } catch (Exception e) {
                e.printStackTrace();
            }
            bgasyn.execute();

            if (mMap != null) {

                //mobile position draw data cell info
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                // Instantiates a new CircleOptions object and defines the center and radius
                if (!circles.isEmpty()) {
                    for (int i = 0; i < circles.size(); i++) {
                        circles.get(i).remove();
                    }
                }
                if (!polylines.isEmpty()) {
                    for (int j = 0; j < polylines.size(); j++) {
                        polylines.get(j).remove();
                    }
                }

                int height = 50;
                int width = 50;
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.mobile);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap mobileMarker = Bitmap.createScaledBitmap(b, width, height, false);
                double dist=0;
                if(cell.getLon()!=null && cell.getLat()!=null){
                     dist = CalculationByDistance(loc, new LatLng(Double.valueOf(cell.getLat()), Double.valueOf(cell.getLon())));

                }
                MarkerOptions mobileoption=new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(mobileMarker)).position(loc).title(String.valueOf("LTE ,distance :" + dist + " km ," + "lat :" + loc.latitude + ",log :" + loc.longitude));
                Marker mobile=mMap.addMarker(mobileoption);
                markers.add(mobile);




                if (cell.getLat() != null && cell.getLon() != null) {

                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(loc, new LatLng(Double.parseDouble(cell.getLat()), Double.parseDouble(cell.getLon())))
                            .width(10)
                            .color(Color.BLUE)
                            .geodesic(true));
                    polylines.add(line);

                    //bts connect to the mobile data chart cell info
                    int heightbts = 50;
                    int widthbts = 50;
                    BitmapDrawable bitmapdrawbts = (BitmapDrawable) getResources().getDrawable(R.drawable.bts2);
                    Bitmap bts = bitmapdrawbts.getBitmap();
                    Bitmap btsMarker = Bitmap.createScaledBitmap(bts, widthbts, heightbts, false);
                    // Add a marker in Sydney and move the camera
                    LatLng btspos = new LatLng(Double.parseDouble(cell.getLat()), Double.parseDouble(cell.getLon()));
                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(btsMarker)).position(btspos)
                            .title(String.valueOf(
                                    " cell Lat : " + cell.getLat()
                                            + " cell Lon : " + cell.getLon()
                                            + " cell Pci : " + cell.getPci()
                                            + " cell AverageSignalStrength : " + cell.getAverageSignalStrength()
                                            + " cell Radio : " + cell.getRadio()
                                            + " cell Range : " + cell.getRange()
                                            + " cell Changeabe : " + cell.getChangeable()

                            )));
                    mMap.setOnMyLocationButtonClickListener(mylocationbtn);

                    //access method icon 2g,3g,4g
                    if (cell.getMethod_access().equals("LTE")) {
                        int heightgen = 20;
                        int widthgen = 20;
                        BitmapDrawable bitmapdrawgen = (BitmapDrawable) getResources().getDrawable(R.drawable.gen4);
                        Bitmap gen = bitmapdrawgen.getBitmap();
                        Bitmap genMarker = Bitmap.createScaledBitmap(gen, widthgen, heightgen, false);
                        LatLng genpos = new LatLng(Double.parseDouble(cell.getLat()) + 0.00100, Double.parseDouble(cell.getLon()) );
                        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(genMarker)).position(genpos));
                    } else if (cell.getMethod_access().equals("UMTS")) {
                        int heightgen = 20;
                        int widthgen = 20;
                        BitmapDrawable bitmapdrawgen = (BitmapDrawable) getResources().getDrawable(R.drawable.gen3);
                        Bitmap gen = bitmapdrawgen.getBitmap();
                        Bitmap genMarker = Bitmap.createScaledBitmap(gen, widthgen, heightgen, false);
                        LatLng genpos = new LatLng(Double.parseDouble(cell.getLat()) , Double.parseDouble(cell.getLon()) - 0.00100);
                        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(genMarker)).position(genpos));

                    } else if (cell.getMethod_access().equals("GSM")) {
                        int heightgen = 20;
                        int widthgen = 20;
                        BitmapDrawable bitmapdrawgen = (BitmapDrawable) getResources().getDrawable(R.drawable.gen2);
                        Bitmap gen = bitmapdrawgen.getBitmap();
                        Bitmap genMarker = Bitmap.createScaledBitmap(gen, widthgen, heightgen, false);
                        LatLng genpos = new LatLng(Double.parseDouble(cell.getLat()) + 0.00400, Double.parseDouble(cell.getLon()) - 0.00100);
                        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(genMarker)).position(genpos));
                    }

                    CircleOptions circleOptions=new CircleOptions()
                            .center(loc)
                            .fillColor(Color.argb(100, 255, 255, 0))
                            .strokeColor(Color.argb(20, 250, 250, 205))
                            .radius(100);

                    CircleOptions circleOptionsbts = new CircleOptions()
                            .center(btspos)
                            .fillColor(Color.argb(20, 255, 255, 0))
                            .strokeColor(Color.argb(20, 250, 250, 205))
                            .radius(600);

                    if (cell.getCellSignalStrengthDbm() >= -90) {
                        circleOptionsbts.strokeColor(Color.argb(20, 0, 255, 0)); // green
                        circleOptionsbts.fillColor(Color.argb(40, 0, 255, 0));   //green

                         circleOptions = new CircleOptions()
                                .center(loc)
                                .fillColor(Color.argb(40, 0, 255, 0))
                                .strokeColor(Color.argb(20, 0, 255, 0))
                                .radius(100);

                    } else if (cell.getCellSignalStrengthDbm() < -90 && cell.getCellSignalStrengthDbm() >= -97) {
                        circleOptionsbts.strokeColor(Color.argb(20, 250, 250, 205)); //yellow
                        circleOptionsbts.fillColor(Color.argb(100, 255, 255, 0));  //yelow

                         circleOptions = new CircleOptions()
                                .center(loc)
                                .fillColor(Color.argb(100, 255, 255, 0))
                                .strokeColor(Color.argb(20, 250, 250, 205))
                                .radius(100);

                    } else if (cell.getCellSignalStrengthDbm() < -97 && cell.getCellSignalStrengthDbm() >= -103) {
                        circleOptionsbts.strokeColor(Color.argb(20, 255, 127, 80)); //orange
                        circleOptionsbts.fillColor(Color.argb(100, 255, 127, 80));    //orange

                        circleOptions = new CircleOptions()
                                .center(loc)
                                .fillColor(Color.argb(100, 255, 127, 80))
                                .strokeColor(Color.argb(20, 255, 127, 80))
                                .radius(100);
                    } else {
                        circleOptionsbts.strokeColor(Color.argb(20, 250, 0, 0)); //red
                        circleOptionsbts.fillColor(Color.argb(100, 255, 0, 0));    //red
                         circleOptions = new CircleOptions()
                                .center(loc)
                                .fillColor(Color.argb(100, 255, 0, 0))
                                .strokeColor(Color.argb(20, 250, 0, 0))
                                .radius(100);
                    }

                    Circle circlebts = mMap.addCircle(circleOptionsbts);
                    circles.add(circlebts);
                    Circle circle = mMap.addCircle(circleOptions);
                    circles.add(circle);
                } else {
                    Toast.makeText(getApplicationContext(), "BTS introuvable ,attendez s'il vous plait", Toast.LENGTH_SHORT).show();
                }


                //Toast.makeText(getApplicationContext(), cell.toString() + "generation" + cell.getMethod_access() + "power in dbm is : " + cell.getCellSignalStrengthDbm(), Toast.LENGTH_SHORT).show();



            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        manager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStatelistener = new MyPhoneStateListener();
        manager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);


    }


    @Override
    protected void onStart() {
        super.onStart();
        BackgroundAsyncTask bgasyn = null;
        try {
            bgasyn = new BackgroundAsyncTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bgasyn.execute();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        mMap.setOnCameraChangeListener(getCameraChangeListener());



    }

    public GoogleMap.OnCameraChangeListener getCameraChangeListener(){
        return  new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position)
            {
                int mCameraTilt = (position.zoom < 15) ? 0 : 60;
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                                .target(position.target)
                                .tilt(mCameraTilt)
                                .zoom(position.zoom)
                                .build()));
            }
        };
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius*c;
    }



    @Override
    protected void onResume() {
        super.onResume();

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
            cell.setMcc(Integer.valueOf(manager.getNetworkOperator().substring(0, 3)));
            cell.setMnc(Integer.valueOf((manager.getNetworkOperator().substring(3))));
            if(manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_LTE){
                GsmCellLocation cellLocation = (GsmCellLocation)manager.getCellLocation();
                cell.setId(Integer.valueOf(String.valueOf(cellLocation.getCid())));
                cell.setLac(Integer.valueOf(String.valueOf(cellLocation.getLac())));
                cell.setMethod_access("LTE");
                cell.setCellSignalStrengthDbm(cell.getCellSignalStrengthDbm());

            }
            else if(manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_CDMA
                    ||manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_HSDPA
                    ||manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_HSPA
                    ||manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_HSPAP
                    ||manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_HSUPA
                    ||manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_UMTS
                    ||manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_EVDO_0
                    ||manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_EVDO_A
                    ||manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_EVDO_B
                    ||manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_EHRPD
                    ||manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_1xRTT
                    ){
                cellInfos=manager.getAllCellInfo();
                cell.setMethod_access("UMTS");
                CellIdentityWcdma cellIdentityWcdma=((CellInfoWcdma)cellInfos.get(0)).getCellIdentity();
                cell.setLac(cellIdentityWcdma.getLac());
                cell.setId(cellIdentityWcdma.getCid());
                cell.setMcc(cellIdentityWcdma.getMcc());
                cell.setMnc(cellIdentityWcdma.getMnc());
            }
            else if (manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_GPRS ||manager.getNetworkType()==TelephonyManager.NETWORK_TYPE_EDGE){
                cell.setMethod_access("GSM");

                GsmCellLocation cellLocation =(GsmCellLocation)manager.getCellLocation();
                cell.setLac(cellLocation.getLac());
                cell.setId(cellLocation.getCid());
                cell.setCellSignalStrengthDbm(-1*cell.getCellSignalStrengthDbm());

            }


            URL url = null;
            String returnedResult = "";
            try {
                url = new URL("http://opencellid.org/cell/get?mcc="+cell.getMcc()+"&mnc="+cell.getMnc()+"&cellid="+cell.getId()+"&lac="+cell.getLac()+"&key=3216e3b6-f212-413e-83f8-b05adb8e04cc");
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


    class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            int mSignalStrength=0;
            mSignalStrength = signalStrength.getGsmSignalStrength();
            //Toast.makeText(getApplicationContext()," level"+,Toast.LENGTH_LONG).show();
            mSignalStrength = ((2 * mSignalStrength) - 113); // -> dBm
            cell.setCellSignalStrengthDbm(mSignalStrength);
            cell.setCellSignalStrengthwWatt(10^(mSignalStrength/10));
        }


    }
}
