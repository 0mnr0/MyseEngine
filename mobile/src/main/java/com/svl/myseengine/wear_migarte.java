package com.svl.myseengine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

public class wear_migarte extends AppCompatActivity {


    public void SendData(String command, String DataInCommand){
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();

        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Wearable.NodeApi.getConnectedNodes(googleApiClient).setResultCallback(nodes -> {
                    Node node = nodes.getNodes().get(0);
                    String nodeId = node.getId();
                    byte[] messageBytes = command.getBytes();
                    Wearable.MessageApi.sendMessage(googleApiClient, nodeId, DataInCommand, messageBytes);
                });
            }

            @Override
            public void onConnectionSuspended(int i) {}
        });

    }

    public void upd(View view) {
        ProgressBar pb = findViewById(R.id.progressBar2);
        pb.setVisibility(View.VISIBLE);
        SendData("#!(get_save_file)", "null");
        pb.setVisibility(View.INVISIBLE);
    }

    @SuppressLint({"VisibleForTests", "UseSwitchCompatOrMaterialCode"})
    public void wear_listener(View view) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();

         DataApi.DataListener dataListener = dataEventBuffer -> {
            for (DataEvent event : dataEventBuffer) {
                if (event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath().equals("/message")) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    String message = dataMapItem.getDataMap().getString("message");
                    assert message != null;
                    String[] keys = message.split("\\|");
                    Log.d("keys[0]", keys[0]);
                    Log.d("keys[1]", keys[1]);
                    if (keys[0].equals("setting_rounded_videos")) {
                        boolean isChecked = Boolean.parseBoolean(keys[1]);
                        Switch switch1 = findViewById(R.id.switch1);
                        switch1.setChecked(isChecked);
                        switch1.setEnabled(true);
                    }


                    Toast.makeText(wear_migarte.this, message,
                            Toast.LENGTH_LONG).show();

                }
            }
        };

        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                Wearable.DataApi.addListener(googleApiClient, dataListener);
            }

            @Override
            public void onConnectionSuspended(int i) {
                Toast.makeText(wear_migarte.this, "Ошибка подключения", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void RoundedVideo (View v){
        Switch switch2 = (Switch) v;
        boolean isChecked = switch2.isChecked();
        SendData("setting_rounded_videos", String.valueOf(isChecked));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_migarte);
        wear_listener(null);
        SendData("setting_rounded_videos_get","null");
    }

}