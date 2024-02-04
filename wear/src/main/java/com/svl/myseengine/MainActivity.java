package com.svl.myseengine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.svl.myseengine.databinding.ActivityMainBinding;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

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
    String ID;






    public void cleaning() {
        freeMemory();
    }

    public void wait_startercon(int ms) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                prepare_texts(null);
            }
        }, ms);
    }


    public void show_toast(String text) {
        Toast.makeText(this, text,
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
                    Log.w("BCDLog1:", String.valueOf(list) + " | " + buttonText + " || " + btnchoices[position]);
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
        } catch (Exception e) {
        }

        AllowClicking=true;

    }


    public void spawnbuttons(View view, int btns_count, String txt) {
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

    public void achivments() {

    }


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

    public void SetOnScreen(int ImageID, String name, String text, String ID) {
        Log.d("ScreenChanger", "SetOnScreen: " + ImageID + " | " + name + " | " + text);
        boolean showImage = true;
        boolean showName = true;
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


        if (Name.equals("None") || Name.equals("nothing")) {
            Name.setText(name);
            Name.setVisibility(View.VISIBLE);
        } else {
            Name.setText("");
            Name.setVisibility(View.GONE);
        }


        if (showText) {
            if (!text.equals("None") || !text.equals("nothing") || text.equals("None")) {
                Text.setText(text);
            } else {
                Text.setText("");
            }
        } else {
            Text.setText("");
        }


        try {
            avatarka.setImageResource(ImageID);
        } catch (Exception ignored) {
        }


    }





    @SuppressLint("DiscouragedApi")
    public void starter_con(View view) {
        try {
            Log.i("Allow,Clicking",String.valueOf(AllowClicking));
            if (AllowClicking) {
                boolean cmdwas = false;
                if (clicks < DialogNamesL) {


                    Log.w("CurFileNames: ", "'" + DialogNames + "'");
                    Log.w("CurFileTexts: ", "'" + DialogTexts + "'");
                    Log.w("CurFileIDS: ", "'" + DialogIDS + "'");
                    Log.w("CurFileImages: ", "'" + DialogImages + "'");

                    if (DialogNames.equals("#!(clear_screen);")) {
                        if (!cmdwas) {
                            cleaning();
                            SetOnScreen(R.drawable.nothing,"","","");
                            cmdwas = true;
                        }
                    }

                    if (DialogNames.equals("#!(btn_executor)")) {
                        if (!cmdwas) {
                            spawnbuttons(view, Integer.parseInt(DialogIDS), DialogImages);
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


                    if (DialogNames.equals("#!(goto_xml)")) {
                        if (!cmdwas) {
                            Log.i("Goto_XML", "Trying To Go: " +chapterID + "_" + DialogTexts + " : " + DialogIDS);
                            returnArrayDialogsToPos(DialogTexts, DialogIDS);
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




    public void setChapterName(String Name){
        chapterID = Name;
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("SavedChapterName", Name).apply();
    }



    @SuppressLint("DiscouragedApi")
    //public void returnArrayDialogsPos(Имя xml файла, номер сообщения)
    public void returnArrayDialogsToPos(String AdditionalName, String PositionNumber) {
        DialogNames = null;
        DialogTexts = null;
        DialogImages = null;
        DialogIDS = null;

        int resId;

        clicks = Integer.valueOf(PositionNumber);

        Log.d("WriterArrays", AdditionalName + "_DialogsOrTexts");

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("AdditionalName", AdditionalName).apply();

        resId = getResources().getIdentifier(chapterID+"_"+AdditionalName + "_NamesOrServiceNames", "array", getPackageName());
        OrigDialogNames = getResources().getStringArray(resId);

        resId = getResources().getIdentifier(chapterID+"_"+AdditionalName + "_DialogsOrTexts", "array", getPackageName());
        OrigDialogTexts = getResources().getStringArray(resId);

        resId = getResources().getIdentifier(chapterID+"_"+AdditionalName + "_ImagesOrAvatars", "array", getPackageName());
        OrigDialogImages = getResources().getStringArray(resId);

        resId = getResources().getIdentifier(chapterID+"_"+AdditionalName + "_TypedID", "array", getPackageName());
        OrigDialogIDS = getResources().getStringArray(resId);


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

        if (AdditionalName != "") {
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
        editor.commit();
    }
    public int LoadInt(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getInt("SavedClicks", 0);
    }
    public void PutString(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void prepare_texts(View view){
        if (AllowClicking) {
            int resId;
            Log.d("LoaderClicks", String.valueOf(clicks));
            clicks = clicks + 1;
            Log.d("LoadedClicks", String.valueOf(clicks));


            SharedPreferences prefs = getSharedPreferences("AdditionalName", Context.MODE_PRIVATE);
            String AdditionalName = "NewHistory_TheStart";


            if (AdditionalName != "") {
                AdditionalName = "_" + AdditionalName;
            }

            Log.d("WriterArrays02", chapterID + AdditionalName);

            resId = getResources().getIdentifier(chapterID + AdditionalName + "_NamesOrServiceNames", "array", getPackageName());
            OrigDialogNames = getResources().getStringArray(resId);
            Log.d("WriterArrays01", "DialogNames:" + chapterID + AdditionalName + "_NamesOrServiceNames");

            resId = getResources().getIdentifier(chapterID + AdditionalName + "_DialogsOrTexts", "array", getPackageName());
            OrigDialogTexts = getResources().getStringArray(resId);
            Log.d("WriterArrays01", "DialogTexts:" + chapterID + AdditionalName + "_DialogsOrTexts");

            resId = getResources().getIdentifier(chapterID + AdditionalName + "_ImagesOrAvatars", "array", getPackageName());
            OrigDialogImages = getResources().getStringArray(resId);
            Log.d("WriterArrays01", "DialogTexts:" + chapterID + AdditionalName + "_ImagesOrAvatars");


            resId = getResources().getIdentifier(chapterID + AdditionalName + "_TypedID", "array", getPackageName());
            OrigDialogIDS = getResources().getStringArray(resId);
            Log.d("WriterArrays01", "DialogTexts:" + chapterID + AdditionalName + "_TypedID");

            DialogNamesL = OrigDialogTexts.length;

            Log.d("prepare_texts0", OrigDialogNames[1]);
            Log.d("prepare_texts0", OrigDialogTexts[1]);
            Log.d("prepare_texts0", OrigDialogImages[1]);
            Log.d("prepare_texts0", OrigDialogIDS[1]);
            Log.d("prepare_clicks", String.valueOf(clicks));

            DialogNames = OrigDialogNames[clicks];
            DialogTexts = OrigDialogTexts[clicks];
            DialogImages = OrigDialogImages[clicks];
            DialogIDS = OrigDialogIDS[clicks];

            Log.d("prepare_texts", DialogNames);
            Log.d("prepare_texts", DialogTexts);
            Log.d("prepare_texts", DialogImages);
            Log.d("prepare_texts", DialogIDS);

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

        clicks=clicks+1;

        get_messages_from_phone();



    }

    public void get_messages_from_phone(){
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();

        Wearable.MessageApi.addListener(googleApiClient, new MessageApi.MessageListener() {
            @Override
            public void onMessageReceived(MessageEvent messageEvent) {
                String path = messageEvent.getPath();
                byte[] data = messageEvent.getData();
                String message = new String(data, StandardCharsets.UTF_8);
                show_toast(message);
                if (message.equals("#!(get_save_file)"))
                    {send_to_phone(null);}
            }
        });
    }

    public void send_to_phone(View view){
        String txtmessage="This is backed text from wear";

        PutDataMapRequest dataMap = PutDataMapRequest.create("/message");
        dataMap.getDataMap().putString("message", txtmessage);
        dataMap.getDataMap().putLong("timestamp", System.currentTimeMillis());
        PutDataRequest request = dataMap.asPutDataRequest();
        request.setUrgent();


        Task<DataItem> dataItemTask = Wearable.getDataClient(this).putDataItem(request);
        dataItemTask.addOnSuccessListener(new OnSuccessListener<DataItem>() {
            @Override
            public void onSuccess(DataItem dataItem) {
                // Действия при успешной отправке данных
            }
        });

    }

}