package com.example.enviarsmsapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ServiceSendSms extends Service {

    NotificationCompat.Builder builder;
    NotificationManagerCompat notificationManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        builder = new NotificationCompat.Builder(this, "0");
        startForeground(0, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        Log.i("Debug", "Inicio o ServiceSendSms.");
        String destinationNumber = intent.getStringExtra("DestinationNumber");
        String message = intent.getStringExtra("Message");
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destinationNumber, null, message, null, null);
        return super.onStartCommand(intent, flags, startId);
    }




}
