package com.example.toshiba.virtualdt;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthLte;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.widget.ListView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    TelephonyManager manager ;
    List<CellInfo> cellInfos;
    Cells cells;
    MyPhoneStateListener mPhoneStatelistener;

    ListView listViewcells;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        listViewcells=(ListView)findViewById(R.id.listview);
        manager=(TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        cellInfos=manager.getAllCellInfo();
        mPhoneStatelistener =new MyPhoneStateListener();
        manager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS );

    }

    class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            cellInfos=manager.getAllCellInfo();
            cells=new Cells();

            // Cells.cells=null;
            for (CellInfo cellInfo : cellInfos)
            {
                if (cellInfo instanceof CellInfoGsm)
                {
                    Cell cell=new Cell();
                    cell.setMethod_access("GSM");
                    cell.setMethod_access_type(manager.getNetworkType());
                    // cast to CellInfoLte and call all the CellInfoGSM methods you need
                    CellIdentityGsm cellidentitygsm=((CellInfoGsm) cellInfo).getCellIdentity();
                    cell.setLac(cellidentitygsm.getLac());
                    cell.setMnc(cellidentitygsm.getMnc());
                    cell.setMcc(cellidentitygsm.getMcc());
                    cell.setId(cellidentitygsm.getCid());
                    cell.setCellSignalStrengthDbm(((CellInfoGsm) cellInfo).getCellSignalStrength().getDbm());
                    cell.setCellSignalStrengthLevel(((CellInfoGsm) cellInfo).getCellSignalStrength().getLevel());
                    cell.setAsuLevel(((CellInfoGsm) cellInfo).getCellSignalStrength().getAsuLevel());
                    cell.setCellSignalStrengthwWatt((Double.valueOf(Math.pow(10,cell.getCellSignalStrengthDbm()/10))));// not working
                    cells.addCell(cell);
                    cells.filterSigleCell();
                    final CellArrayAdapter cellArrayAdapter=new CellArrayAdapter(ListActivity.this,cells.getAllCells(),2);
                    listViewcells.setAdapter(cellArrayAdapter);
                }

                else if(cellInfo instanceof CellInfoWcdma){
                    Cell cell=new Cell();
                    cell.setMethod_access("UMTS");
                    cell.setMethod_access_type(manager.getNetworkType());
                    //WcdmaCellLocation cdmaCellLocation=(CdmaCellLocation) manager.getCellLocation();
                    CellIdentityWcdma cellIdentityWcdma=((CellInfoWcdma)cellInfo).getCellIdentity();
                    cell.setLac(cellIdentityWcdma.getLac());
                    cell.setMnc(cellIdentityWcdma.getMnc());
                    cell.setMcc(cellIdentityWcdma.getMcc());
                    cell.setUcid(cellIdentityWcdma.getCid());
                    cell.setId(-1);
                    cell.setPsc(cellIdentityWcdma.getPsc());
                    cell.setRnc(cell.getLac());
                    int mSignalStrength=0;
                    mSignalStrength = signalStrength.getGsmSignalStrength();
                    mSignalStrength = ((2 * mSignalStrength) - 113); // -> dBm
                    cell.setCellSignalStrengthDbm(mSignalStrength);
                    cell.setAsuLevel((cell.getCellSignalStrengthDbm()+113)/2);
                    //Toast.makeText(getApplicationContext()," "+cdmaCellLocation.toString(),Toast.LENGTH_SHORT).show();
                    cell.setCellSignalStrengthwWatt((Double.valueOf(Math.pow(10,cell.getCellSignalStrengthDbm()/10))));
                    cells.addCell(cell);
                    cells.filterSigleCell();
                    final CellArrayAdapter cellArrayAdapter=new CellArrayAdapter(ListActivity.this,cells.getAllCells(),3);
                    listViewcells.setAdapter(cellArrayAdapter);
                }

                else if(cellInfo instanceof CellInfoLte){
                    CellInfoLte cellInfoLte=(CellInfoLte) cellInfo;
                    CellIdentityLte cellIdentityLte=cellInfoLte.getCellIdentity();
                    CellSignalStrengthLte cellSignalStrengthLte=cellInfoLte.getCellSignalStrength();
                    //Toast.makeText(getApplicationContext()," "+cellSignalStrengthLte.toString(),Toast.LENGTH_LONG).show();
                    Cell cell=new Cell();
                    cell.setMethod_access("LTE");
                    cell.setMethod_access_type(manager.getNetworkType());
                    cell.setTac(((CellInfoLte) cellInfo).getCellIdentity().getTac());
                    cell.setPci(cellIdentityLte.getPci());
                    cell.setMcc(cellIdentityLte.getMcc());
                    cell.setMnc(cellIdentityLte.getMnc());
                    cell.setEci(cellIdentityLte.getCi());
                    cell.setId(cell.getEci());// for 2g and 3g
                    cell.setLac(cell.getTac()); // for 2g and 3g ; not exist in lte  ,replace with tac
                    //int mSignalStrength = signalStrength.getGsmSignalStrength();
                   // mSignalStrength = -1*((2 * mSignalStrength) - 113); // -> dBm
                    //cell.setRsrp(signalStrength.get);
                    cell.setRsrq(-1);
                    cell.setRssnr(-1);
                    try {
                        Method[] methods = android.telephony.SignalStrength.class
                                .getMethods();
                        for (Method mthd : methods) {
                           // Toast.makeText(getApplicationContext(),mthd.toString() ,Toast.LENGTH_SHORT).show();

                            if (mthd.getName().equals("getLteRsrp")
                                    ) {
                                int i= Integer.valueOf(mthd.invoke(signalStrength).toString());
                                cell.setRsrp(i);
                               // Toast.makeText(getApplicationContext(),"onSignalStrengthsChanged: " + mthd.getName() + " "
                                  //      +i ,Toast.LENGTH_LONG).show();

                            }
                            else if (mthd.getName().equals("getLteRsrq")
                                    ) {
                                int i= Integer.valueOf(mthd.invoke(signalStrength).toString());
                                cell.setRsrq(i);
                            }

                        }
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    cell.setAsuLevel(cell.getRsrp()+116);
                    cell.setCellSignalStrengthDbm(cell.getRsrp());
                    cells.addCell(cell);
                    cells.filterSigleCell();
                    final CellArrayAdapter cellArrayAdapter=new CellArrayAdapter(ListActivity.this,cells.getAllCells(),4);
                    listViewcells.setAdapter(cellArrayAdapter);
                }
            }

        }
    }
}