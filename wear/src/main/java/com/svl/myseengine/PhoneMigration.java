package com.svl.myseengine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class PhoneMigration extends AppCompatActivity {


    private Button sendButton;

    private void sendMessageToPhone(String message) {
        message="Backed Text";
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/message");
        putDataMapRequest.getDataMap().putString("message", message);
        PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();

        Task<DataItem> putDataTask = Wearable.getDataClient(this).putDataItem(putDataRequest);
        putDataTask.addOnSuccessListener(new OnSuccessListener<DataItem>() {
            @Override
            public void onSuccess(DataItem dataItem) {
                // Успешно отправлено
                Toast.makeText(PhoneMigration.this, "Message sent", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Ошибка при отправке
                Toast.makeText(PhoneMigration.this, "Failed to send message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void send_to_phone(View view){
        String txtmessage="asdasdasd";

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
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}