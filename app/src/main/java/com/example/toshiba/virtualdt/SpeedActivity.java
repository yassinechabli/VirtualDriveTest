package com.example.toshiba.virtualdt;

import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class SpeedActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    int up=0,down=0;
    TextView packetRx,packetTx,byteTx,byteRx,totalPacketTx,totalPacketRx,totalByteTx,totalByteRx,dup,ddown;
    private long packetRxv,packetTxv,byteTxv,byteRxv,totalPacketTxv,totalPacketRxv,totalByteTxv,totalByteRxv;
    private long bytesuplink[]=new long[2];
    private long bytesdownlink[]=new long[2];
    private float debituplink=0,debitdonwlink=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        packetRx=(TextView)findViewById(R.id.packetRx);
        packetTx=(TextView)findViewById(R.id.packetTx);
        totalPacketTx=(TextView)findViewById(R.id.totalPacketTx);
        totalPacketRx=(TextView)findViewById(R.id.totalPacketRx);
        byteTx=(TextView)findViewById(R.id.byteTx);
        byteRx=(TextView)findViewById(R.id.byteRx);
        totalByteTx=(TextView)findViewById(R.id.totalByteTx);
        totalByteRx=(TextView)findViewById(R.id.totalByteRx);

        dup=(TextView)findViewById(R.id.dup);
        ddown=(TextView)findViewById(R.id.ddown);

        //==========================

        packetRxv=TrafficStats.getMobileRxPackets();
        packetTxv=TrafficStats.getMobileTxPackets();
        totalPacketRxv=TrafficStats.getTotalRxPackets();
        totalPacketTxv=TrafficStats.getTotalTxPackets();
        byteRxv=TrafficStats.getMobileRxBytes();
        byteTxv=TrafficStats.getMobileTxBytes();
        bytesdownlink[down%2]=byteRxv;down++;
        bytesuplink[up%2]=byteTxv;up++;
        totalByteRxv=TrafficStats.getTotalRxBytes();
        totalByteTxv=TrafficStats.getTotalTxBytes();
            //--------------------------
        packetRx.setText(String.valueOf(packetRxv));
        packetTx.setText(String.valueOf(packetTxv));
        totalPacketRx.setText(String.valueOf(totalPacketRxv));
        totalPacketTx.setText(String.valueOf(totalPacketTxv));
        byteRx.setText(String.valueOf(byteRxv));
        byteTx.setText(String.valueOf(byteTxv));
        totalByteRx.setText(String.valueOf(totalByteRxv));
        totalByteTx.setText(String.valueOf(totalByteTxv));



        if (totalByteRxv == TrafficStats.UNSUPPORTED || totalByteTxv == TrafficStats.UNSUPPORTED) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Uh Oh!");
            alert.setMessage("Your device does not support traffic stat monitoring.");
            alert.show();
        } else {
            mHandler.postDelayed(mRunnable, 2000);
        }

    }

    private final Runnable mRunnable = new Runnable() {
        public void run() {

            packetRxv=TrafficStats.getMobileRxPackets();
            packetTxv=TrafficStats.getMobileTxPackets();
            totalPacketRxv=TrafficStats.getTotalRxPackets();
            totalPacketTxv=TrafficStats.getTotalTxPackets();
            byteRxv=TrafficStats.getMobileRxBytes();
            byteTxv=TrafficStats.getMobileTxBytes();
            //totalByteRxv=TrafficStats.getTotalRxBytes();
            totalByteTxv=TrafficStats.getTotalTxBytes();
            //--------------------------
            packetRx.setText(String.valueOf(packetRxv));
            packetTx.setText(String.valueOf(packetTxv));
            totalPacketRx.setText(String.valueOf(totalPacketRxv));
            totalPacketTx.setText(String.valueOf(totalPacketTxv));
            byteRx.setText(String.valueOf(byteRxv));
            byteTx.setText(String.valueOf(byteTxv));
            totalByteRx.setText(String.valueOf(totalByteRxv));
            totalByteTx.setText(String.valueOf(totalByteTxv));
            bytesdownlink[down%2]=byteRxv;down++;
            bytesuplink[up%2]=byteTxv;up++;
            debitdonwlink=Math.abs(bytesdownlink[0]-bytesdownlink[1]);
            debituplink=Math.abs(bytesuplink[0]-bytesuplink[1]);
            dup.setText(String.valueOf(debituplink)+"bytes/s"+" , "+String.valueOf((debituplink*8)/1000) +"Kbit/s");
            ddown.setText(String.valueOf(debitdonwlink)+"bytes/s"+" , "+String.valueOf((debitdonwlink*8)/1000) +"Kbit/s");
            mHandler.postDelayed(mRunnable, 2000);
        }
    };
}
