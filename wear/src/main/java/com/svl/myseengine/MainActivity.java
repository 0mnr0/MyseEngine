package com.svl.myseengine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.svl.myseengine.databinding.ActivityMainBinding;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    View ReClick = null;

    boolean AllowClicking = true;

    String chapterID="C1";
    int clicks = -3;
    String last_btn_choice = "";
    String DialogNames = "";
    String DialogTexts = "";
    String DialogImages = "";
    String DialogIDS = "";


    private String[] OrigDialogNames = {};
    private String[] OrigDialogTexts = {};
    private String[] OrigDialogImages = {};
    private String[] OrigDialogIDS = {};

    private int DialogNamesL=0;
    private int CBI = 0;//CurButtonId
    private int MAX_btns_count = 0;//Max buttons counts
    String[] chtexts;
    ImageView avatarka;
    TextView Name;
    TextView Text;


    public void cleaning() {
        freeMemory();
    }

    public void wait_startercon(int ms) {
        Handler handler = new Handler();
        handler.postDelayed(() -> prepare_texts(null), ms);
    }


    public void show_toast(Object text) {
        Toast.makeText(this, text.toString(),
                Toast.LENGTH_LONG).show();
    }

    public void click_manadger(View view) {
        ImageView keydown = findViewById(R.id.KeyDOWN);
        keydown.setVisibility(View.GONE);
        ImageView keyup = findViewById(R.id.KeyUP);
        keyup.setVisibility(View.GONE);
        Button CurChoiceButton=findViewById(R.id.ChoiceButton);
        CurChoiceButton.setVisibility(View.GONE);



        String buttonText = ((Button) view).getText().toString();
        SetOnScreen(R.drawable.nothing,"",buttonText,"0");

        if (DialogTexts.equals("None")) {


            try {
                String[] btnchoices = DialogTexts.split("\\|");
                String[] originallist = DialogImages.split("\\|");
                Log.w("BCDStep 1:", Arrays.toString(btnchoices) + " | " + btnchoices.length);

                if (btnchoices.length != 1) {

                    List<String> list = Arrays.asList(originallist);
                    int position = list.indexOf(buttonText);
                    Log.w("BCDLog1:", list + " | " + buttonText + " || " + btnchoices[position]);
                    Log.w("BCDLog2:", String.valueOf(position));
                    Log.w("BtnChoiceDetector:", "Step 2 Success");


                    if (!last_btn_choice.equals("None")) {
                        Log.d("Choiced", btnchoices[position] + " : " + buttonText);
                        Log.w("BCDLog3:", btnchoices[position]);
                        Log.w("BCDLog4:", buttonText);
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("AdditionalName", btnchoices[position]).apply();
                        writeArrayDialogs(false);

                    }
                }
            } catch (Exception e) {
                Log.e("ChoiceDetector:", String.valueOf(e));
            }
            CBI = 0;
        }

        try {
            if (num_checker(clicks + 1)) {
                if (DialogNames.equals("#!(play_videoobject)")) {
                    wait_startercon(120);

                }
            }
        } catch (Exception ignored) {}

        AllowClicking=true;

    }


    public void spawnbuttons(int btns_count, String txt) {
        AllowClicking=false;

        boolean onebutton=false;
        MAX_btns_count=btns_count-1;

        chtexts = txt.split("\\|");

        if (chtexts.length==1){
            onebutton=true;
        }

        if (!onebutton) {
            ImageView keydown = findViewById(R.id.KeyDOWN);
            keydown.setVisibility(View.VISIBLE);
        }
        Button CurChoiceButton=findViewById(R.id.ChoiceButton);
        CurChoiceButton.setVisibility(View.VISIBLE);

        CBI=0;

        SetOnScreen(R.drawable.nothing,"","","");
        CurChoiceButton.setText(chtexts[0]);


    }

    public void KeyDownEvent(View view){
        Log.d("KeyDownEvent", Arrays.toString(chtexts));
        if (CBI <= MAX_btns_count) {//Если это не макисмум - то:

            CBI++;
            if (CBI + 1 >= MAX_btns_count) {
                ImageView keydown = findViewById(R.id.KeyDOWN);
                keydown.setVisibility(View.INVISIBLE);
                ImageView keyup = findViewById(R.id.KeyUP);
                keyup.setVisibility(View.VISIBLE);
            }
            Button CurChoiceButton=findViewById(R.id.ChoiceButton);
            CurChoiceButton.setText(chtexts[CBI]);

        }
    }

    public void KeyUpEvent(View view){
        Log.d("KeyUpEvent","if ("+(CBI)+" > 0) {"+(CBI > 0)+"}");

        if (CBI > 0) {//Если это не минимум - то:

            CBI--;

            Button CurChoiceButton=findViewById(R.id.ChoiceButton);
            CurChoiceButton.setText(chtexts[CBI]);


            if (CBI >= 0){
                ImageView keyup = findViewById(R.id.KeyUP);
                keyup.setVisibility(View.INVISIBLE);
                ImageView keydown = findViewById(R.id.KeyDOWN);
                keydown.setVisibility(View.VISIBLE);
            }



        }
    }

    public void achivments() {}


    public boolean is_command_next() {
        try {
            String item = OrigDialogNames[clicks];
            return item.equals("#!(play_videoobject)") || item.equals("#!(playsound)") || item.equals("#!(stopsound)") || item.equals("#!(achievement_executor)") || item.equals("#!(makeknown_executor)") || item.equals("#!(write.album_executor)");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean num_checker(int indexToCheck) {
        return indexToCheck >= 0 && indexToCheck < DialogNamesL;
    }

    //SetOnScreen()
    public void SetOnScreen(int ImageID, String Avatarname, String text, String ID) {
        Log.d("ScreenChanger", "SetOnScreen: " + ImageID + " | " + Avatarname + " | " + text);
        CardView RoundedCardID = findViewById(R.id.RoundedCardID);
        ConstraintLayout CardID = findViewById(R.id.MainCardID);
        boolean showName;
        boolean showText = true;

        ImageView avatarka = findViewById(R.id.imageView);
        TextView Text = findViewById(R.id.Text);
        TextView Name = findViewById(R.id.name);


        if (ID.equals("202")) {
            showText = false;
            avatarka.setAlpha(1.0F);
        } else {
            avatarka.setAlpha(0.25F);
        }


        Log.d("ScreenChangerNONE", "Is Avatarname NONE: " + (!Avatarname.equals("None")));
        showName = !Avatarname.equals("None") && !Avatarname.equals("nothing");

        if (showName){
            Name.setText(Avatarname);
            Name.setVisibility(View.VISIBLE);
        } else {
            Name.setText("");
            Name.setVisibility(View.GONE);
        }

        if (showText) {
            Text.setText(text);
        } else {
            Text.setText("");
        }


        try {
            avatarka.setImageResource(ImageID);
        } catch (Exception ignored) {
        }

        if (ID.equals("1001")){
            RoundedCardID.setVisibility(View.VISIBLE);
            CardID.setVisibility(View.GONE);
            VideoView VideoPlayer = findViewById(R.id.RoundedVideoView);
            VideoPlayer.setVideoURI(Uri.parse("android.resource://com.svl.myseengine/"+ImageID));
            VideoPlayer.start();
        } else {
            RoundedCardID.setVisibility(View.GONE);
            CardID.setVisibility(View.VISIBLE);

        }




    }





    @SuppressLint("DiscouragedApi")
    public void starter_con(View view) {
        try {
            if (AllowClicking) {
                boolean cmdwas = false;
                if (clicks < DialogNamesL) {


                    Log.d("Executing: ", DialogNames);
                    if (DialogNames.equals("#!(clear_screen);")) {
                        cleaning();
                        SetOnScreen(R.drawable.nothing,"","","");
                        cmdwas = true;
                    }

                    if (DialogNames.equals("#!(btn_executor)")) {
                        if (!cmdwas) {
                            spawnbuttons(Integer.parseInt(DialogIDS), DialogImages);
                            AllowClicking = false;
                            cmdwas = true;
                        }
                    }

                    if (DialogNames.equals("ImagerService")) {
                        if (!cmdwas) {
                            int resourceId = getResources().getIdentifier(DialogImages, "drawable", getPackageName());
                            SetOnScreen(resourceId,"None", "None", "202");
                            cmdwas = true;
                        }
                    }

                    if (DialogNames.equals("#!(achievement_executor)")) {
                        if (!cmdwas) {
                            achivments();
                            getSharedPreferences("Achievements", MODE_PRIVATE).edit().putBoolean(DialogIDS, true).apply();
                            cmdwas = true;
                        }
                    }

                    if (DialogNames.equals("#!(play_videoobject)")) {
                        if (!cmdwas) {
                            cmdwas = true;

                        }
                    }

                    if (DialogNames.equals("#!(makeknown_executor)")) {
                        if (!cmdwas) {

                            getSharedPreferences("KnownPers", MODE_PRIVATE).edit().putBoolean(DialogTexts, true).apply();
                            cmdwas = true;
                        }
                    }

                    if (DialogNames.equals("#!(write.album_executor)")) {
                        if (!cmdwas) {
                            cmdwas = true;
                            //getSharedPreferences("Albums", MODE_PRIVATE).edit().putBoolean(DialogTexts[clicks], true).apply();
                        }
                    }


                    if (DialogNames.equals("#!(FlashGameScreen)")) {
                        if (!cmdwas) {
                            cmdwas = true;
                        }
                    }

                    if (DialogNames.equals("#!(playsound)")) {
                        if (!cmdwas) {
                            cmdwas = true;
                        }
                    }

                    if (DialogNames.equals("#!(stopsound)")) {
                        if (!cmdwas) {
                            cmdwas = true;
                        }
                    }


                    if (DialogNames.equals("#!(rounded_video)")) {
                        if (!cmdwas) {
                            if (GetBool("setting_rounded_videos", false)) {
                                int videoId = getResources().getIdentifier(DialogTexts, "raw", getPackageName());
                                SetOnScreen(videoId, "None", "None", "1001");
                            } else {prepare_texts(view);}
                            cmdwas = true;
                        }
                    }


                    if (DialogNames.equals("#!(goto_xml)")) {
                        if (!cmdwas) {
                            Log.i("Goto_XML", "Trying To Go: " +chapterID + "_" + DialogTexts + " : " + DialogIDS);
                            returnArrayDialogsToPos(DialogTexts, DialogIDS, false);
                            Log.i("Goto_XML", "Sucsess migration");

                        }
                    }

                    if (!cmdwas) {
                        int resourceId = getResources().getIdentifier(DialogImages, "drawable", getPackageName());
                        SetOnScreen(resourceId,DialogNames, DialogTexts, DialogIDS);
                        cmdwas = true;
                    }

                    if (num_checker(clicks)) {
                        Log.d("NumChekcer", String.valueOf((num_checker(clicks))));
                        Log.d("IsCommandNext", String.valueOf(is_command_next()));
                        if (is_command_next()) {
                            prepare_texts(null);
                        }
                    }


                } else {
                    cleaning();
                    clicks = -1;
                }

                //saving
                Log.d("ADDName", String.valueOf(chapterID));
                SaveInt("SavedClicks", clicks - 1);
                //PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("AdditionalName", //).apply();

            }
        }catch (Exception e){Log.e("Iventor09142", e+"\nText:"+DialogTexts+"\nNames:"+DialogNames+"\nImage:"+DialogImages+"\nID:"+DialogIDS);}
    }


    @SuppressLint("DiscouragedApi")
    //public void returnArrayDialogsPos(Имя xml файла, номер сообщения)
    public void returnArrayDialogsToPos(String AdditionalName, String PositionNumber, Boolean IsSaveFromPhone) {
        DialogNames = null;
        DialogTexts = null;
        DialogImages = null;
        DialogIDS = null;

        int resId;

        clicks = Integer.parseInt(PositionNumber);
        SaveInt("SavedClicks", clicks - 1);
        String NeededChapter = chapterID;
        if (!IsSaveFromPhone) {NeededChapter=NeededChapter+"_";}

        Log.d("WriterArrays", chapterID+AdditionalName + "_DialogsOrTexts");

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("AdditionalName", AdditionalName).apply();

        resId = getResources().getIdentifier(NeededChapter+AdditionalName + "_NamesOrServiceNames", "array", getPackageName());
        OrigDialogNames = getResources().getStringArray(resId);

        resId = getResources().getIdentifier(NeededChapter+AdditionalName + "_DialogsOrTexts", "array", getPackageName());
        OrigDialogTexts = getResources().getStringArray(resId);

        resId = getResources().getIdentifier(NeededChapter+AdditionalName + "_ImagesOrAvatars", "array", getPackageName());
        OrigDialogImages = getResources().getStringArray(resId);

        resId = getResources().getIdentifier(NeededChapter+AdditionalName + "_TypedID", "array", getPackageName());
        OrigDialogIDS = getResources().getStringArray(resId);


        Log.d("REWriterArrays", "DialogNames:" + AdditionalName + "_NamesOrServiceNames");
        Log.d("REWriterArrays", "DialogTexts:" + AdditionalName + "_DialogsOrTexts");
        Log.d("REWriterArrays", "DialogImages:" + AdditionalName + "_ImagesOrAvatars");
        Log.d("REWriterArrays", "DialogIDS:" + AdditionalName + "_TypedID");
        Log.d("REWriterArrays", "SetClicks:" + clicks + ". NeedToSet:"+PositionNumber);
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

        OrigDialogNames = getResources().getStringArray(resId);
        Log.d("WriterArrays0", "DialogNames:"+ chapterID + AdditionalName + "_NamesOrServiceNames");

        resId = getResources().getIdentifier(chapterID + AdditionalName + "_DialogsOrTexts", "array", getPackageName());
        OrigDialogTexts = getResources().getStringArray(resId);
        Log.d("WriterArrays0", "DialogTexts:"+ chapterID + AdditionalName + "_DialogsOrTexts");

        resId = getResources().getIdentifier(chapterID + AdditionalName + "_ImagesOrAvatars", "array", getPackageName());
        OrigDialogImages = getResources().getStringArray(resId);
        Log.d("WriterArrays0", "DialogImages:"+ chapterID + AdditionalName + "_ImagesOrAvatars");

        resId = getResources().getIdentifier(chapterID + AdditionalName + "_TypedID", "array", getPackageName());
        OrigDialogIDS = getResources().getStringArray(resId);
        Log.d("WriterArrays0", "DialogIDS:"+ chapterID + AdditionalName + "_TypedID");

        if (FirstLaunch){clicks=LoadInt();}

    }

    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public void SaveInt(String key, int value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value-1);
        editor.apply();
    }

    public void SaveString(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void SaveBoolean(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, Boolean.parseBoolean(value));
        editor.apply();
    }

    public int LoadInt(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getInt("SavedClicks", 0);
    }

    public boolean GetBool(String key, Boolean DefaultValue){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getBoolean(key, DefaultValue);
    }

    public void PutString(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void prepare_texts(View view){
        if (ReClick == null){ReClick = view;}
        if (AllowClicking) {
            int resId;

            if (OrigDialogNames.length-1 == clicks){
                clicks = 0;
            } else {
                clicks++;
            }


            String AdditionalName = "NewHistory_TheStart";


            AdditionalName = "_" + AdditionalName;


            resId = getResources().getIdentifier(chapterID + AdditionalName + "_NamesOrServiceNames", "array", getPackageName());
            OrigDialogNames = getResources().getStringArray(resId);

            resId = getResources().getIdentifier(chapterID + AdditionalName + "_DialogsOrTexts", "array", getPackageName());
            OrigDialogTexts = getResources().getStringArray(resId);

            resId = getResources().getIdentifier(chapterID + AdditionalName + "_ImagesOrAvatars", "array", getPackageName());
            OrigDialogImages = getResources().getStringArray(resId);

            resId = getResources().getIdentifier(chapterID + AdditionalName + "_TypedID", "array", getPackageName());
            OrigDialogIDS = getResources().getStringArray(resId);

            DialogNamesL = OrigDialogTexts.length;


            if (clicks==-1){clicks++;}
            DialogNames = OrigDialogNames[clicks];
            DialogTexts = OrigDialogTexts[clicks];
            DialogImages = OrigDialogImages[clicks];
            DialogIDS = OrigDialogIDS[clicks];

            starter_con(null);
        }

    }



    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {

            boolean FLaunch = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("FirstLaunch", true);

            if (FLaunch) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("SavedChapterName", "C1").apply();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("SavedChapterMsg", 0).apply();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("AdditionalName", "NewHistory_TheStart").apply();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("FirstLaunch", false).apply();
            }



            writeArrayDialogs(true);
        }catch (Exception ignored){}

        avatarka=findViewById(R.id.imageView);
        Name=findViewById(R.id.name);
        Text=findViewById(R.id.Text);

        clicks++;


        get_messages_from_phone();



    }

    public void get_messages_from_phone(){
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();

        Wearable.MessageApi.addListener(googleApiClient, messageEvent -> {
            byte[] data = messageEvent.getData();
            String message = messageEvent.getPath();
            String command = new String(data, StandardCharsets.UTF_8);
            Log.d("GetData:", message+", "+ Arrays.toString(data) + ", "+ Arrays.toString(data));

            if (command.equals("setting_rounded_videos"))
                {SaveBoolean("setting_rounded_videos", message); show_toast("Круглые видео: " + message);}
            if (command.equals("setting_rounded_videos_get")) {
                boolean rounded_value = GetBool("setting_rounded_videos", false);
                send_to_phone("setting_rounded_videos|"+rounded_value);
            }
            if (command.equals("push_save_file")) {
                String[] keys = message.split(",");
                boolean OriginalClickingAllowed = AllowClicking;
                AllowClicking = true;
                returnArrayDialogsToPos(keys[1], keys[0], true);
                prepare_texts(null);
                AllowClicking = OriginalClickingAllowed;
            }
        });
    }

    @SuppressLint("VisibleForTests")
    public void send_to_phone(Object txtmessage) {
        PutDataMapRequest dataMap = PutDataMapRequest.create("/message");
        dataMap.getDataMap().putString("message", txtmessage.toString());
        dataMap.getDataMap().putLong("timestamp", System.currentTimeMillis());
        PutDataRequest request = dataMap.asPutDataRequest();
        request.setUrgent();


    }

}