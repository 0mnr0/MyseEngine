package com.svl.myseengine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity {

    boolean MonetChanged=false;

    public void show_toast(String text) {
        Toast.makeText(this, text,
                Toast.LENGTH_LONG).show();
    }

    public void finishme(View view){
        if(MonetChanged) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        finish();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    public void Monet(View view) {
        Switch switch22 = (Switch) view;
        boolean isChecked = switch22.isChecked();
        SharedPreferences.Editor editor = getSharedPreferences("GameUISettings", Context.MODE_PRIVATE).edit();
        editor.putBoolean("Monet", isChecked).apply();
        MonetChanged= !MonetChanged;
        if (MonetChanged) {show_toast("Окно игры перезапустится, все данные сохранены");}
        else {show_toast("Изменение отменено, окно не перезапустится");}


    }

    public void wear_migrator(View view){
        Intent intent = new Intent(this, wear_migarte.class);
        startActivity(intent);
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public void animations(View view) {
        Switch switch1 = (Switch) view;

        boolean isChecked = switch1.isChecked();

        SharedPreferences.Editor editor = getSharedPreferences("GameUISettings", Context.MODE_PRIVATE).edit();
        editor.putBoolean("Anims", isChecked).apply();
    }

    public void MiniVideos(View view) {
        Switch switch6 = (Switch) view;

        boolean isChecked = switch6.isChecked();

        SharedPreferences.Editor editor = getSharedPreferences("GameUISettings", Context.MODE_PRIVATE).edit();
        editor.putBoolean("MiniVideo", isChecked).apply();
    }

    public void NightCare(View view) {
        Switch switch2 = (Switch) view;

        boolean isChecked = switch2.isChecked();

        SharedPreferences.Editor editor = getSharedPreferences("GameUISettings", Context.MODE_PRIVATE).edit();
        editor.putBoolean("BlackSplashed", isChecked).apply();
    }

    public void videos(View view) {
        Switch switch0 = (Switch) view;

        boolean isChecked = switch0.isChecked();

        SharedPreferences.Editor editor = getSharedPreferences("GameUISettings", Context.MODE_PRIVATE).edit();
        editor.putBoolean("Videos", isChecked).apply();
    }

    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        hideSystemUI();


        SharedPreferences prefs = getSharedPreferences("GameUISettings", Context.MODE_PRIVATE);

        boolean BlackSplash = prefs.getBoolean("BlackSplashed", false);
        if (BlackSplash) {Switch bs = findViewById(R.id.switch3); bs.setChecked(BlackSplash);}

        boolean Anims = prefs.getBoolean("Anims", false);
        if (Anims) {Switch an = findViewById(R.id.switch1); an.setChecked(Anims);}

        boolean MiniVideos = prefs.getBoolean("MiniVideo", false);
        if (MiniVideos) {Switch mv = findViewById(R.id.switch6); mv.setChecked(MiniVideos);}



        boolean Monet = prefs.getBoolean("Monet", false);
        if (Build.VERSION.SDK_INT >= 31) {
            Switch mn = findViewById(R.id.switch4);
            mn.setChecked(Monet);
        } else {
            Switch mn = findViewById(R.id.switch4);
            mn.setEnabled(false);
            mn.setText("Monet\nИспользование Monet тем недоступно на Android, верия которого ниже 12");
        }

        boolean Videos = prefs.getBoolean("Videos", false);
        if (Videos) {
             Switch vd = findViewById(R.id.switch5); vd.setChecked(Videos);}

    }











    protected void onDestroy() {
        finish();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        if (MonetChanged){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        super.onDestroy();

    }
}