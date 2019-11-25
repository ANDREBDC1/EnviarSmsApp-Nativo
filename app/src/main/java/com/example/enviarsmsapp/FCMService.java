package com.example.enviarsmsapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        Intent intent = remoteMessage.toIntent();
        super.onMessageReceived(remoteMessage);
        Log.i("Debug", remoteMessage.getMessageId());
        Log.i("Teste", remoteMessage.getData().get("DestinationNumber"));
        Log.i("Teste", remoteMessage.getData().get("Message"));


        String destinationNumber = remoteMessage.getData().get("DestinationNumber");
        String message = remoteMessage.getData().get("Message");



//        Intent intent1 = new Intent(getBaseContext(), SmsSentReceiver.class);
//        intent1.setType("vnd.android-dir/mms-sms");
//
//        Intent intent2 = new Intent(getBaseContext(), SmsDeliveredReceiver.class);
//        intent2.setType("vnd.android-dir/mms-sms");


        PendingIntent sentPI = PendingIntent.getBroadcast(getBaseContext(), 0, intent
               , 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getBaseContext(), 0,
                intent, 0);



        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destinationNumber, null, message, sentPI, deliveredPI);
        startActivity(new Intent(FCMService.this, MainActivity.class));

//        Intent intent = new Intent(this, ServiceSendSms.class);
//        intent.putExtra("DestinationNumber", destinationNumber);
//        intent.putExtra("Message", message);
//
//        Log.i("Teste", "Chegou aqui!");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(intent);
//        }else{
//            startService(intent);
//        }


    }


}
