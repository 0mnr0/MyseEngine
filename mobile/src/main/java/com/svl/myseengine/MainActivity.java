package com.svl.myseengine;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<String> messages = new ArrayList<>();
    ChatAdapter adapter = new ChatAdapter(messages);
    MonetChatAdapter MonetAdapter = new MonetChatAdapter(messages);
    RecyclerView RecyclerView;
    private GoogleApiClient googleApiClient;
    private DataApi.DataListener dataListener;
    private int clicks = -1;
    private String[] DialogNames = {};
    private String[] DialogTexts = {};
    private String[] DialogImages = {};
    private String[] DialogIDS = {};
    private MediaPlayer mediaPlayer;
    private int BSLT = 0;//Buttons Spawned Last time
    boolean clicking = true;
    String last_btn_choice = "";
    String chapterID = "C1";
    boolean AutoPlaying=false;
    boolean FastScroll = false;
    int MonetColor=0;
    boolean SoundMuted=false;


    private boolean checkMusicPlaying() {
        return ((AudioManager) getSystemService(Context.AUDIO_SERVICE)).isMusicActive() && !mediaPlayer.isPlaying();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void scroller(View view) {
        adapter.notifyDataSetChanged();
        MonetAdapter.notifyDataSetChanged();
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewMain);
        RecyclerView.SmoothScroller smoothScroller = null;

        int timer = 1;
        if (!FastScroll) {
            timer = 220;
        }

        int finalTimer = timer;
        smoothScroller = new LinearSmoothScroller(getApplicationContext()) {
            @Override
            protected int calculateTimeForScrolling(int dx) {
                return finalTimer;
            }
        };


        if (!((messages.size() - 1) < 0)) {
            smoothScroller.setTargetPosition((messages.size() - 1));
            AutoScrollRunnable autoScrollRunnable = new AutoScrollRunnable(recyclerView, smoothScroller);
            recyclerView.postDelayed(autoScrollRunnable, 200);

        }
        clicking=true;


    }


    public void cleaning() {
        messages.clear();
        scroller(null);
        freeMemory();
    }

    public void wait_startercon(int ms) {
        Handler handler = new Handler();
        handler.postDelayed(() -> starter_con(null), ms);
    }


    public void show_toast(Object text) {
        Toast.makeText(this, text.toString(),
                Toast.LENGTH_LONG).show();
    }

    public void click_manadger(View view) {
        Button CButton = ((Button) view);

        String buttonText = ((Button) view).getText().toString();

        if (messages.size() != 1) {
            for (int i = 0; i < BSLT; i++) {
                int msgs = messages.size();
                messages.remove(msgs - 1);
            }
        } else {
            messages.remove(0);
        }

        messages.add(R.drawable.nothing + "|" + buttonText + "|" + "ChoiceService" + "|" + "304"+"|"+3);
        CButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.disabledbtn)));
        scroller(view);

        if (!DialogTexts[clicks].equals("None")) {


            try {
                String[] btnchoices = DialogTexts[clicks].split("\\|");
                String[] originallist = DialogImages[clicks].split("\\|");
                if (btnchoices.length != 1) {

                    List<String> list = Arrays.asList(originallist);
                    int position = list.indexOf(buttonText);
                    if (!last_btn_choice.equals("None")) {
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("AdditionalName", btnchoices[position]).apply();
                        writeArrayDialogs(false);

                    }
                }
            } catch (Exception e) {
                Log.e("ChoiceDetector:", String.valueOf(e));
            }
            BSLT = 0;
        }

        try {
            if (num_checker(clicks + 1)) {
                if (DialogNames[clicks + 1].equals("#!(play_videoobject)")) {
                    wait_startercon(120);
                }
            }
        } catch (Exception ignored) {}
        scroller(null);

    }

    void check_updates() {

    }

    public void spawnbuttons(View view, int btns_count, String txt) {
        BSLT = btns_count;
        String[] chtexts = txt.split("\\|");
        SharedPreferences prefs0 = getSharedPreferences("GameUISettings", Context.MODE_PRIVATE);
        boolean Monet=prefs0.getBoolean("Monet", false);

        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)){Monet=false;}
        if (chtexts.length > 1) {
            for (int i = 0; i < btns_count; i++) {

                int MonetState = 0;

                if (Monet){

                    if (i !=0 & i<btns_count-1) {MonetState=1;}

                    if (i == btns_count -1 ) {MonetState=2;}

                    messages.add(R.drawable.nothing + "|" + chtexts[i] + "|" + "ChoiceService" + "|" + "303" +"|" + MonetState +"|" +MonetColor);


                }
                else {
                    MonetState=R.drawable.nothing;
                    messages.add(MonetState + "|" + chtexts[i] + "|" + "ChoiceService" + "|" + "303");
                }
            }
        } else {
            if (Monet){
                messages.add(R.drawable.nothing + "|" + chtexts[0] + "|" + "ChoiceService" + "|" + "303"+"|"+3+"|"+MonetColor);
            }else{
                messages.add(R.drawable.nothing + "|" + chtexts[0] + "|" + "ChoiceService" + "|" + "303");
            }

        }
        scroller(view);
        clicking = true;

    }

    public void achivments() {
        AchivmentFragment fragment = new AchivmentFragment();
        fragment.show(getSupportFragmentManager(), "MyFragment");
        new Handler().postDelayed(fragment::dismiss, 1200);
    }


    public boolean is_command_next() {
        try {
            String item = DialogNames[clicks + 1];
            return item.equals("#!(playsound)") || item.equals("#!(stopsound)") || item.equals("#!(achievement_executor)") || item.equals("#!(makeknown_executor)") || item.equals("#!(write.album_executor)");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean num_checker(int indexToCheck) {
        return indexToCheck >= 0 && indexToCheck < DialogNames.length;
    }



    @SuppressLint("DiscouragedApi")
    public void starter_con(View view) {
        try {

            if (clicking) {
                clicks++;
                boolean cmdwas = false;
                if (clicks < DialogNames.length) {

                    if (DialogNames[clicks].equals("#!(clear_screen);")) {
                        cleaning();
                        cmdwas = true;
                    }

                    if (DialogNames[clicks].equals("#!(btn_executor)")) {
                        if (!cmdwas) {
                            //StopAutoPlay();
                            spawnbuttons(view, Integer.parseInt(DialogIDS[clicks]), DialogImages[clicks]);
                            clicking = false;
                            cmdwas = true;
                        }
                    }

                    if (DialogNames[clicks].equals("#!(achievement_executor)")) {
                        if (!cmdwas) {
                            achivments();
                            getSharedPreferences("Achievements", MODE_PRIVATE).edit().putBoolean(DialogIDS[clicks], true).apply();
                            cmdwas = true;
                        }
                    }

                    if (DialogNames[clicks].equals("#!(play_videoobject)")) {
                        if (!cmdwas) {
                            cmdwas = true;
                            SharedPreferences prefs = getSharedPreferences("GameUISettings", Context.MODE_PRIVATE);
                            boolean MiniVideos = prefs.getBoolean("MiniVideo", false);
                            boolean skip = prefs.getBoolean("Videos", false);

                            if (MiniVideos) {
                                clicking = false;

                                VideoView VideoPlayer = findViewById(R.id.BackgroundVideo);//Need to load video from DialogTexts[clicks]
                                int resId = getResources().getIdentifier(DialogTexts[clicks], "raw", getPackageName());
                                String path = "android.resource://" + getPackageName() + "/" + resId;
                                VideoPlayer.setVideoURI(Uri.parse(path));
                                VideoPlayer.start();
                                VideoPlayer.setVisibility(View.VISIBLE);

                                VideoPlayer.setOnCompletionListener(mp -> {clicking=true; VideoPlayer.setVisibility(View.GONE);});

                            } else {
                                if (!skip) {
                                    Intent intent = new Intent(this, video_player.class);
                                    intent.putExtra("VideoName", DialogTexts[clicks]);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                                }
                            }


                        }
                    }

                    if (DialogNames[clicks].equals("#!(makeknown_executor)")) {
                        if (!cmdwas) {

                            getSharedPreferences("KnownPers", MODE_PRIVATE).edit().putBoolean(DialogTexts[clicks], true).apply();
                            cmdwas = true;
                        }
                    }

                    if (DialogNames[clicks].equals("#!(write.album_executor)")) {
                        if (!cmdwas) {
                            cmdwas = true;
                            //getSharedPreferences("Albums", MODE_PRIVATE).edit().putBoolean(DialogTexts[clicks], true).apply();
                        }
                    }


                    if (DialogNames[clicks].equals("#!(FlashGameScreen)")) {
                        if (!cmdwas) {
                            SharedPreferences prefs = getSharedPreferences("GameUISettings", Context.MODE_PRIVATE);

                            boolean myValue = prefs.getBoolean("BlackSplashed", false);
                            Intent intent;

                            if (myValue) {
                                intent = new Intent(this, flash.class);
                            } else {
                                intent = new Intent(this, dark_flash.class);
                            }

                            startActivity(intent);
                            overridePendingTransition(R.anim.fast_alpha_in, R.anim.fast_alpha_out);
                            if (DialogTexts[clicks].equals("Y")) {
                                cleaning();
                            }
                            cmdwas = true;
                        }
                    }

                    if (DialogNames[clicks].equals("#!(playsound)")) {
                        if (!cmdwas) {
                            cmdwas = true;
                            try {
                                mediaPlayer.reset();
                                int resId = getResources().getIdentifier(DialogTexts[clicks], "raw", getPackageName());
                                mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + this.getPackageName() + "/" + resId));

                                PutString("LastSound", DialogTexts[clicks]);

                                mediaPlayer.prepare();
                                mediaPlayer.setLooping(true);
                                mediaPlayer.start();


                            } catch (Exception ignored) {
                            }

                        }
                    }

                    if (DialogNames[clicks].equals("#!(stopsound)")) {
                        if (!cmdwas) {
                            cmdwas = true;
                            mediaPlayer.stop();
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LastSouund", "None").apply();

                        }
                    }


                    if (DialogNames[clicks].equals("#!(goto_xml)")) {
                        if (!cmdwas) {
                            Log.i("Goto_XML", "Trying To Go: " +chapterID + "_" + DialogTexts[clicks] + " : " + DialogIDS[clicks]);
                            returnArrayDialogsToPos(DialogTexts[clicks], DialogIDS[clicks]);
                            Log.i("Goto_XML", "Sucsess migration");

                        }
                    }

                    if (!cmdwas) {
                        SharedPreferences prefs0 = getSharedPreferences("GameUISettings", Context.MODE_PRIVATE);
                        boolean Monet=prefs0.getBoolean("Monet", false);
                        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)){Monet=false;}

                        String Appender="";
                        if (Monet){Appender="|"+MonetColor;}

                        int resourceId = getResources().getIdentifier(DialogImages[clicks], "drawable", getPackageName());
                        if (DialogNames[clicks].equals("#!(rounded_video)")){
                            String videoId=Integer.toString(getResources().getIdentifier(DialogTexts[clicks], "raw", getPackageName()));
                            messages.add(videoId + "|" + DialogNames[clicks] + "|" + DialogTexts[clicks] + "|" + DialogIDS[clicks]+Appender);
                        }else{
                            messages.add(resourceId + "|" + DialogNames[clicks] + "|" + DialogTexts[clicks] + "|" + DialogIDS[clicks]+Appender);
                        }
                        scroller(view);
                        cmdwas = true;
                    }

                    if (num_checker(clicks)) {
                        if (is_command_next()) {
                            starter_con(null);
                        }
                    }

                } else {
                    cleaning();
                    clicks = -1;
                }
                SaveInt("SavedClicks", clicks - 1);
                //PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("AdditionalName", //).apply();

            }
        }catch (Exception e){
            Log.e("Iventor09142", e+"\nText:"+DialogTexts[clicks]+"\nNames:"+DialogNames[clicks]+"\nImage:"+DialogImages[clicks]+"\nID:"+DialogIDS[clicks]);
        }
    }

    public void open_album(View view){
        Intent intent = new Intent(this, Album.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
    public void open_achivments(View view){
        Intent intent = new Intent(this, Achievements.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
    public void open_knownpersnons(View view){
        Intent intent = new Intent(this, knownpersonlist.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
    public void open_timeline(View view){
        Intent intent = new Intent(this, timeline.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }



    @SuppressLint("DiscouragedApi")
    //public void returnArrayDialogsPos(Имя xml файла, номер сообщения)
    public void returnArrayDialogsToPos(String AdditionalName, String PositionNumber) {
        DialogNames = null;
        DialogTexts = null;
        DialogImages = null;
        DialogIDS = null;

        int resId;

        clicks = Integer.parseInt(PositionNumber);

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("AdditionalName", AdditionalName).apply();

        resId = getResources().getIdentifier(chapterID+"_"+AdditionalName + "_NamesOrServiceNames", "array", getPackageName());
        DialogNames = getResources().getStringArray(resId);

        resId = getResources().getIdentifier(chapterID+"_"+AdditionalName + "_DialogsOrTexts", "array", getPackageName());
        DialogTexts = getResources().getStringArray(resId);

        resId = getResources().getIdentifier(chapterID+"_"+AdditionalName + "_ImagesOrAvatars", "array", getPackageName());
        DialogImages = getResources().getStringArray(resId);

        resId = getResources().getIdentifier(chapterID+"_"+AdditionalName + "_TypedID", "array", getPackageName());
        DialogIDS = getResources().getStringArray(resId);


        Log.d("REWriterArrays", "DialogNames:" + AdditionalName + "_NamesOrServiceNames");
        Log.d("REWriterArrays", "DialogTexts:" + AdditionalName + "_DialogsOrTexts");
        Log.d("REWriterArrays", "DialogImages:" + AdditionalName + "_ImagesOrAvatars");
        Log.d("REWriterArrays", "DialogIDS:" + AdditionalName + "_TypedID");
    }

    @SuppressLint("DiscouragedApi")
    public void writeArrayDialogs(Boolean FirstLaunch) {
        DialogNames = null;
        DialogTexts = null;
        DialogImages = null;
        DialogIDS = null;

        int resId;

        String AdditionalName = PreferenceManager.getDefaultSharedPreferences(this).getString("AdditionalName", "");

        if (!AdditionalName.isEmpty()) {
            AdditionalName = "_" + AdditionalName;
        }

        Log.d("WriterArrays0", chapterID + AdditionalName + "_DialogsOrTexts");

        resId = getResources().getIdentifier(chapterID + AdditionalName + "_NamesOrServiceNames", "array", getPackageName());
        DialogNames = getResources().getStringArray(resId);
        Log.d("WriterArrays0", "DialogNames:"+ chapterID + AdditionalName + "_NamesOrServiceNames");

        resId = getResources().getIdentifier(chapterID + AdditionalName + "_DialogsOrTexts", "array", getPackageName());
        DialogTexts = getResources().getStringArray(resId);
        Log.d("WriterArrays0", "DialogTexts:"+ chapterID + AdditionalName + "_DialogsOrTexts");

        resId = getResources().getIdentifier(chapterID + AdditionalName + "_ImagesOrAvatars", "array", getPackageName());
        DialogImages = getResources().getStringArray(resId);
        Log.d("WriterArrays0", "DialogImages:"+ chapterID + AdditionalName + "_ImagesOrAvatars");

        resId = getResources().getIdentifier(chapterID + AdditionalName + "_TypedID", "array", getPackageName());
        DialogIDS = getResources().getStringArray(resId);
        Log.d("WriterArrays0", "DialogIDS:"+ chapterID + AdditionalName + "_TypedID");

        if (FirstLaunch){clicks=LoadInt();}

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

    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    void register_clicks(){
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewMain);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    starter_con(null);
                }
                return false;
            }

            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) { starter_con(null); }
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { starter_con(null); }
        });



    }
    public void SaveInt(String key, int value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public int LoadInt(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getInt("SavedClicks", -1);
    }
    public void PutString(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String LoadStr(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getString("LastSound", "None");
    }
    public boolean LoadMuting(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getBoolean("IsSoundMuted", false);
    }
    public void LoadSounds(){
        String sound = LoadStr();
        if (!sound.equals("None")) {
            try {
                Resources res = getResources();
                int resId = res.getIdentifier(sound, "raw", getPackageName());
                mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + resId));
                if (checkMusicPlaying() && !SoundMuted) {
                    mediaPlayer.setVolume(0.4F, 0.4F);
                }
                mediaPlayer.prepare();
            } catch (IOException ignored) {}

            mediaPlayer.setLooping(true);
            if (LoadMuting()){
                ImageView S = findViewById(R.id.imageView10);
                S.setImageResource(R.drawable.sound_disabled);
                mediaPlayer.setVolume(0F, 0F);
            }
            mediaPlayer.start();

        }
    }

    public void OpenFS(View view){
        FrameLayout FastSettings = findViewById(R.id.FastSettingsLayout);
        if (FastSettings.getVisibility()==View.VISIBLE){
            FastSettings.setVisibility(View.GONE);
        } else {
            FastSettings.setVisibility(View.VISIBLE);
        }
    }

    public void SoundMuteUnMute(View view){
        ImageView SoundState = findViewById(R.id.imageView10);
        if (SoundMuted) {
            SoundState.setImageResource(R.drawable.sound_enabled);
            SoundMuted=false;
            mediaPlayer.setVolume(0.4F, 0.4F);

        } else {
            SoundState.setImageResource(R.drawable.sound_disabled);
            SoundMuted=true;
            mediaPlayer.setVolume(0F, 0F);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsSoundMuted", SoundMuted).apply();

    }
    public void SetAutoPlay(View view){
        ImageView AutoPlay = findViewById(R.id.imageView11);
        if (AutoPlaying) {
            AutoPlay.setImageResource(R.drawable.pause);
            AutoPlaying=false;
        } else {
            AutoPlay.setImageResource(R.drawable.play);
            AutoPlaying=true;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsAutoPlay", AutoPlaying).apply();

    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        hideSystemUI();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);

        LoadSounds();


        writeArrayDialogs(true);


        RecyclerView = findViewById(R.id.RecyclerViewMain);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        SharedPreferences prefs = getSharedPreferences("GameUISettings", Context.MODE_PRIVATE);

        if (prefs.getBoolean("Monet", false)) {
            RecyclerView.setAdapter(MonetAdapter);
        } else {
            RecyclerView.setAdapter(adapter);
        }

        RecyclerView.setLayoutManager(layoutManager);


        register_clicks();

        check_updates();


        FastScroll = prefs.getBoolean("Anims", false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            TypedValue typedValue = new TypedValue();
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this,
                    android.R.style.Theme_DeviceDefault);
            contextThemeWrapper.getTheme().resolveAttribute(android.R.attr.colorAccent,
                    typedValue, true);
            MonetColor = typedValue.data;
        } else {
            MonetColor = Color.parseColor("#FFFFFF");
        }


        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                try {
                    if (AutoPlaying) {starter_con(null); }
                    sleep(2000);
                } catch (Exception ignored) {
                }


                run();
            }
        };
        Thread thread2 = new Thread(runnable2);
        thread2.start();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean("IsAutoPlay", false)){
            ImageView AutoPlay = findViewById(R.id.imageView11);
            AutoPlay.setImageResource(R.drawable.play);
            AutoPlaying=true;
        } else {
            ImageView AutoPlay = findViewById(R.id.imageView11);
            AutoPlay.setImageResource(R.drawable.pause);
            AutoPlaying=false;
        }





    }


    public void opensettings(View view){
        Intent intent = new Intent(this, settings.class);
        startActivity(intent);
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            if (!SoundMuted) {
                if (checkMusicPlaying()) {
                    mediaPlayer.setVolume(0.4F, 0.4F);
                } else {
                    mediaPlayer.setVolume(1F, 1F);
                }
            }
            mediaPlayer.start();
        }


        SharedPreferences prefs = getSharedPreferences("GameUISettings", Context.MODE_PRIVATE);

        FastScroll = prefs.getBoolean("Anims", false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            TypedValue typedValue = new TypedValue();
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this,
                    android.R.style.Theme_DeviceDefault);
            contextThemeWrapper.getTheme().resolveAttribute(android.R.attr.colorAccent,
                    typedValue, true);
            MonetColor = typedValue.data;
        }


    }

    protected void onDestroy() {
        super.onDestroy();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            Wearable.DataApi.removeListener(googleApiClient, dataListener);
            googleApiClient.disconnect();
        }
    }


}



