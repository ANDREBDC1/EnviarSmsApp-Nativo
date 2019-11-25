package com.example.enviarsmsapp.status_device;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import static android.content.Context.TELEPHONY_SERVICE;

public class ServiceDeviceInfo {

    private  Context context;

    public ServiceDeviceInfo(Context context) {
        this.context = context;
    }

    public DeviceInfo getInfo() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
            }
        }
        String number = tm.getSimOperator();
        String imei = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
           imei = tm.getImei();
        }

        DeviceInfo deviceInfo = new DeviceInfo(number, imei);

        return deviceInfo;

    }
}
