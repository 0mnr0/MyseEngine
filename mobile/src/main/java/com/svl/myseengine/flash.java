package com.svl.myseengine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class flash extends AppCompatActivity {

    public void hideSystemUI(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flash);
        hideSystemUI();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Timer timer = new Timer();
        String nc=getIntent().getStringExtra("NeedCleaner");

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
            }
        }, 310);
    }
}