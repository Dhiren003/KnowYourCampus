package com.smartmoles.knowyourcampus;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                if(!Prefs.with(SplashActivity.this).readBoolean("firstboot",false)) {
                    startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, SelectionActivity.class));
                }
                finish();
            }
        },1200);
    }
}
