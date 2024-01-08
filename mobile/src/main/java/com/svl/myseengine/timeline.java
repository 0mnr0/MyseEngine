package com.svl.myseengine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class timeline extends AppCompatActivity {


    public void open_album(View view){
        Intent intent = new Intent(this, Album.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
    public void open_achivments(View view){
        Intent intent = new Intent(this, Achievements.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
    public void open_knownpersnons(View view){
        Intent intent = new Intent(this, knownpersonlist.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    public void open_timeline(View view){
    }

    public void close(View view){
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}