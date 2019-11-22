package com.example.enviarsmsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.enviarsmsapp.status_device.DeviceInfo;
import com.example.enviarsmsapp.status_device.ServiceDeviceInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

public class SplashScreenActivity extends AppCompatActivity {

    private DeviceInfo deviceInfo;
    private String idUser;
    private String PREF_NAME = "SplashScreenActivity";
    private String TOKEN = "Token";
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        registerDevice();
    }

    private void registerDevice() {

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN, "");
        intent = new Intent(this, MainActivity.class);

        if(!token.isEmpty()){
            startActivity(intent);
            return;
        }

        ServiceDeviceInfo serviceDeviceInfo = new ServiceDeviceInfo(this);
        deviceInfo = serviceDeviceInfo.getInfo();
        String email = deviceInfo.getNumberPhone()+"@sms.com.br";
        String senha = deviceInfo.getNumberPhone();


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            idUser = task.getResult().getUser().getUid();
                            registerUserFirebase();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SplashScreenActivity.this, "Erro na autenticação!" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registerUserFirebase() {

        final String token = FirebaseInstanceId.getInstance().getToken();
        User user = new User(idUser, token, deviceInfo.getNumberPhone());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //String keyUser = Base64.getEncoder().encodeToString(deviceInfo.getNumberPhone().getBytes());

            FirebaseFirestore.getInstance().collection("Users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.i("Debug", "Success register");
                    SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(TOKEN, token);
                    editor.commit();
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Debug", "Error register");
                }
            });
        }else{
            Toast.makeText(SplashScreenActivity.this, "Versão do Android não compativél!", Toast.LENGTH_LONG).show();
        }

    }
}
