package com.svl.myseengine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class wear_migarte extends AppCompatActivity {

    public void upd(View view) {
        ProgressBar pb = findViewById(R.id.progressBar2);
        pb.setVisibility(View.VISIBLE);
        //String SaveFile=("");
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();

        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Wearable.NodeApi.getConnectedNodes(googleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(@NonNull NodeApi.GetConnectedNodesResult nodes) {
                        Node node = nodes.getNodes().get(0);
                        String nodeId = node.getId();
                        String message = "#!(get_save_file)";
                        byte[] messageBytes = message.getBytes();
                        Wearable.MessageApi.sendMessage(googleApiClient, nodeId, "Msg1", messageBytes);
                    }
                });
            }

            @Override
            public void onConnectionSuspended(int i) {
            }
        });
        pb.setVisibility(View.INVISIBLE);
    }


    public void wear_listener(View view) {
// Подключение к GoogleApiClient
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();

        // Реализация слушателя
        DataApi.DataListener dataListener = new DataApi.DataListener() {
            @Override
            public void onDataChanged(DataEventBuffer dataEventBuffer) {
                for (DataEvent event : dataEventBuffer) {
                    if (event.getType() == DataEvent.TYPE_CHANGED &&
                            event.getDataItem().getUri().getPath().equals("/message")) {
                        DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                        String message = dataMapItem.getDataMap().getString("message");
                        // Обработка полученного сообщения


                        Toast.makeText(wear_migarte.this, message,
                                Toast.LENGTH_LONG).show();


                    }
                }
            }
        };

// Регистрация слушателя
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                Wearable.DataApi.addListener(googleApiClient, dataListener);
            }

            @Override
            public void onConnectionSuspended(int i) {
                Toast.makeText(wear_migarte.this, "Ошибка подключения",
                        Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_migarte);

        wear_listener(null);

    }
}