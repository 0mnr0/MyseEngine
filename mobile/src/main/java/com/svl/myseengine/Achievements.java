package com.svl.myseengine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Achievements extends AppCompatActivity {

    public void open_album(View view){
        Intent intent = new Intent(this, Album.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
    public void open_achivments(View view){

    }
    public void open_knownpersnons(View view){
        Intent intent = new Intent(this, knownpersonlist.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    public void open_timeline(View view){
        Intent intent = new Intent(this, timeline.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    public void close(View view){
        finish();
        overridePendingTransition(0,0);
    }

    private int getImageResIdForName(String imageName) {
        Context context = getApplicationContext();
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        return resources.getIdentifier(imageName, "drawable", packageName);
    }


    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

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




        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences prefs = getSharedPreferences("Achievements", Context.MODE_PRIVATE);
        List<DataItem> dataList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            boolean Anims = prefs.getBoolean(String.valueOf(i), false);
            if (Anims) {
                String text = String.valueOf(i);
                String additionalText = "Здесь будет описание этого достижения  {" + i + "}";
                String imageName = "a" + i;
                int imageResId = getImageResIdForName(imageName);
                DataItem item = new DataItem(text, additionalText, imageResId);
                dataList.add(item);
            }
        }

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(dataList);
        recyclerView.setAdapter(adapter);















    }






}