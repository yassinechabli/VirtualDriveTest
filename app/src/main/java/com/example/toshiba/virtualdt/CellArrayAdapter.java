package com.example.toshiba.virtualdt;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by TOSHIBA on 06/01/2017.
 */

public class CellArrayAdapter extends ArrayAdapter<Cell>
{
    ArrayList<Cell> cells;
    private int generation; // 2=gsm ,3=wcdma , 4=lte

    public CellArrayAdapter(Context context, ArrayList<Cell> listcells, int gen) {
        super(context, 0,listcells);
        this.generation=gen;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cell c=getItem(position);

        if(this.generation==2){

        View v= View.inflate(super.getContext(),R.layout.item_listview,null);
        TextView mnc=(TextView)v.findViewById(R.id.mnc);
        TextView mcc=(TextView)v.findViewById(R.id.mcc);
        TextView rssi=(TextView)v.findViewById(R.id.rssi);
        TextView asu=(TextView)v.findViewById(R.id.asu);
        TextView cid=(TextView)v.findViewById(R.id.cid);
        TextView lac=(TextView)v.findViewById(R.id.lac);
        TextView power=(TextView)v.findViewById(R.id.power);
        TextView sigle=(TextView)v.findViewById(R.id.sigle);
        TextView networktypesigle=(TextView)v.findViewById(R.id.networkTypeSigle);
        sigle.setText(sigle.getText()+" "+c.getSigle());
        networktypesigle.setText(networktypesigle.getText()+" "+c.getMethod_access_type()+"("+c.getMethod_access()+")");
        mnc.setText(mnc.getText()+" "+c.getMnc());
        mcc.setText(mcc.getText()+" "+c.getMcc());
        rssi.setText(rssi.getText()+" "+c.getCellSignalStrengthDbm()+" dbm");
        asu.setText(asu.getText()+" "+c.getAsuLevel());
        cid.setText(cid.getText()+" "+c.getId());
        lac.setText(lac.getText()+" "+c.getLac());
        power.setText(power.getText()+" "+ Math.pow(10,(c.getCellSignalStrengthDbm()-30)/10)+"watt");
        return v;
        }
        else if(this.generation==3){
            View v= View.inflate(super.getContext(),R.layout.item_listview_cdma,null);
            TextView mnc=(TextView)v.findViewById(R.id.mnc);
            TextView mcc=(TextView)v.findViewById(R.id.mcc);
            TextView rssi=(TextView)v.findViewById(R.id.rssi);
            TextView asu=(TextView)v.findViewById(R.id.asu);
            TextView cid=(TextView)v.findViewById(R.id.cid);
            TextView lac=(TextView)v.findViewById(R.id.lac);
            TextView power=(TextView)v.findViewById(R.id.power);
            TextView sigle=(TextView)v.findViewById(R.id.sigle);
            TextView networktypesigle=(TextView)v.findViewById(R.id.networkTypeSigle);
            TextView psc=(TextView)v.findViewById(R.id.psc);
            TextView rnc=(TextView)v.findViewById(R.id.rnc);
            TextView ucid=(TextView)v.findViewById(R.id.ucid);

            sigle.setText(sigle.getText()+" "+c.getSigle());
            networktypesigle.setText(networktypesigle.getText()+" "+c.getMethod_access_type()+"("+c.getMethod_access()+")");
            mnc.setText(mnc.getText()+" "+c.getMnc());
            mcc.setText(mcc.getText()+" "+c.getMcc());
            rssi.setText(rssi.getText()+" "+c.getCellSignalStrengthDbm()+" dbm");
            asu.setText(asu.getText()+" "+c.getAsuLevel());
            cid.setText(cid.getText()+" "+c.getId());
            lac.setText(lac.getText()+" "+c.getLac());
            power.setText(power.getText()+" "+1.0* Math.pow(10,(c.getCellSignalStrengthDbm()-30)/10)+"watt");
            psc.setText(psc.getText()+" "+c.getPsc());
            rnc.setText(rnc.getText()+" "+c.getRnc());
            ucid.setText(ucid.getText()+" "+c.getUcid());
            return v;
        }

        else if (this.generation==4){

            View v= View.inflate(super.getContext(),R.layout.item_listview_lte,null);
            TextView mnc=(TextView)v.findViewById(R.id.mnc);
            TextView mcc=(TextView)v.findViewById(R.id.mcc);
            TextView asu=(TextView)v.findViewById(R.id.asu);
            TextView lac=(TextView)v.findViewById(R.id.lac);
            TextView power=(TextView)v.findViewById(R.id.power);
            TextView sigle=(TextView)v.findViewById(R.id.sigle);
            TextView networktypesigle=(TextView)v.findViewById(R.id.networkTypeSigle);
            TextView pci=(TextView)v.findViewById(R.id.pci);
            TextView eci=(TextView)v.findViewById(R.id.eci);
            TextView rssnr=(TextView)v.findViewById(R.id.rssnr);
            TextView rsrq=(TextView)v.findViewById(R.id.rsrq);
            TextView rsrp=(TextView)v.findViewById(R.id.rsrp);
            sigle.setText(sigle.getText()+" "+c.getSigle());
            TextView tac=(TextView)v.findViewById(R.id.tac);

            networktypesigle.setText(networktypesigle.getText()+" "+c.getMethod_access_type()+"("+c.getMethod_access()+")");
            mnc.setText(mnc.getText()+" "+c.getMnc());
            mcc.setText(mcc.getText()+" "+c.getMcc());
            asu.setText(asu.getText()+" "+c.getAsuLevel());
            lac.setText(lac.getText()+" "+c.getLac());
            power.setText(power.getText()+" "+1.0* Math.pow(10,(c.getCellSignalStrengthDbm()-30)/10)+"watt");
            pci.setText(pci.getText()+" "+c.getPci());
            rssnr.setText(rssnr.getText()+" "+c.getRssnr());
            rsrq.setText(rsrq.getText()+" "+c.getRsrq());
            rsrp.setText(rsrp.getText()+" "+c.getRsrp()+" dbm");
            eci.setText(eci.getText()+" "+c.getEci());
            tac.setText(tac.getText()+" "+c.getTac());

            return v;

        }
        return null;
    }
}
