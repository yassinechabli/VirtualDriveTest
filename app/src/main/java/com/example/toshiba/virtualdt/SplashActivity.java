package com.example.toshiba.virtualdt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;


public class SplashActivity extends AppCompatActivity {
    private int splashTime = 5000;
    private Thread thread;
    private ProgressBar mSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        mSpinner = (ProgressBar) findViewById(R.id.Splash_ProgressBar);
        mSpinner.setIndeterminate(true);
        thread = new Thread(runable);
        thread.start();
    }
    public Runnable runable = new Runnable() {
        public void run() {
            try {
                Thread.sleep(splashTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                startActivity(new Intent(SplashActivity.this,MenuActivity.class));
                finish();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };
}
