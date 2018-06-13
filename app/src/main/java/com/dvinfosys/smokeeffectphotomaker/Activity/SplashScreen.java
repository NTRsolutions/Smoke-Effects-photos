package com.dvinfosys.smokeeffectphotomaker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.dvinfosys.smokeeffectphotomaker.R;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        initialize();
    }

    /*
       Initialization of all view element from xml file.
        - stsrt thread for 3 seconds
      */
    public void initialize() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(SplashScreen.this,MainActivity.class));
                SplashScreen.this.finish();
            }
        };
        thread.start();
    }
}
