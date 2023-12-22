package com.svl.myseengine;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;


public class Launcher extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    DisplayMetrics displayMetrics;
    Context context=this;
    private AudioManager audioManager;


    public void show_toast(Object text) {
        Toast.makeText(this, text.toString(),
                Toast.LENGTH_LONG).show();
    }

    private boolean checkMusicPlaying() {
        boolean isMusicPlaying = audioManager.isMusicActive();
        return isMusicPlaying;
    }

    public void launch_game(View view){

        boolean FLaunch = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("FirstLaunch", true);
        Log.d("GameLaucher", String.valueOf(FLaunch));
        if (!FLaunch) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
            mediaPlayer.stop();
            finish();
        }
    }


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
        setContentView(R.layout.activity_launcher);


        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        displayMetrics = this.getResources().getDisplayMetrics();

        mediaPlayer = MediaPlayer.create(context, R.raw.s2);
        show_toast(checkMusicPlaying());
        if (checkMusicPlaying()) {

            mediaPlayer.setVolume(0.4F, 0.4F);
        }


        mediaPlayer.start();


        Thread thread = new Thread(new MyRunnable());
        thread.start();


    }


    private class MyRunnable implements Runnable {
        @Override
        public void run() {




            hideSystemUI();

            Group FLD=findViewById(R.id.FirstLoaderGroup);
            Group RTG=findViewById(R.id.GroupPresent);

            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); // getActivity() for Fragment

            boolean FLaunch = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("FirstLaunch", true);

            if (FLaunch){
                RTG.setVisibility(View.GONE);
                FLD.setVisibility(View.VISIBLE);


                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("SavedChapterName", "C1").apply();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("SavedChapterMsg", 0).apply();

                FLD.setVisibility(View.GONE);
                RTG.setVisibility(View.VISIBLE);

                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("AdditionalName", "NewHistory_TheStart").apply();


                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("FirstLaunch", false).apply();

            }



            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Скрываем диалоговое окно загрузки
                }
            });
        }


    }

    @Override
    protected void onPause(){
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

}