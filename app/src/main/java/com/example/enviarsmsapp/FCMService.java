package com.example.enviarsmsapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        String destinationNumber = remoteMessage.getData().get("DestinationNumber");
        String message = remoteMessage.getData().get("Message");

        Intent intent1 = new Intent(getBaseContext(), SmsSentReceiver.class);
        intent1.setType("vnd.android-dir/mms-sms");

        Intent intent2 = new Intent(getBaseContext(), SmsDeliveredReceiver.class);
        intent2.setType("vnd.android-dir/mms-sms");


        PendingIntent sentPI = PendingIntent.getBroadcast(getBaseContext(), 0, intent1
                , 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(getBaseContext(), 0,
                intent2, 0);

        ArrayList<PendingIntent> sents = new ArrayList<>();
        ArrayList<PendingIntent> delivereds = new ArrayList<>();

        sents.add(sentPI);
        delivereds.add(deliveredPI);



        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendMultipartTextMessage(destinationNumber, null, smsManager.divideMessage(message), sents, delivereds);
    }
}
