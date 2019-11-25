package com.example.enviarsmsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enviarsmsapp.status_device.DeviceInfo;
import com.example.enviarsmsapp.status_device.ServiceDeviceInfo;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

public class SplashScreenActivity extends AppCompatActivity {

    private DeviceInfo deviceInfo;
    private String idUser;
    private String PREF_NAME = "SplashScreenActivity";
    private String TOKEN = "Token";
    private Intent intent;
    private Button btnRegister;
    private EditText txtDDD, txtNumberPhone;
    private TextView txtRegistering;
    private String numberPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        btnRegister = findViewById(R.id.btn_register);
        txtDDD = findViewById(R.id.txt_ddd);
        txtNumberPhone = findViewById(R.id.txt_number_phone);
        txtRegistering =findViewById(R.id.txt_registering);

        txtRegistering.setVisibility(View.INVISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN, "");
        intent = new Intent(this, MainActivity.class);

        if(!token.isEmpty()){
            startActivity(intent);
            finish();
            return;
        }

        // Mascara Telefone
        SimpleMaskFormatter maskFormatter = new SimpleMaskFormatter("NNNNN-NNNN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(txtNumberPhone, maskFormatter);
        txtNumberPhone.addTextChangedListener(maskTextWatcher);


        // Mascara DDD
        SimpleMaskFormatter maskFormatterDDD = new SimpleMaskFormatter("NN");
        MaskTextWatcher maskTextWatcherDDD = new MaskTextWatcher(txtDDD, maskFormatterDDD);
        txtDDD.addTextChangedListener(maskTextWatcherDDD);

        //btnRegister.setVisibility(View.INVISIBLE);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberPhoneMask = txtNumberPhone.getText().toString();
                String dddMask = txtDDD.getText().toString();
                if(dddMask.trim().isEmpty() || numberPhoneMask.trim().isEmpty()){
                    Toast.makeText(SplashScreenActivity.this, "Infome um número de telefone", Toast.LENGTH_LONG).show();
                    return;
                }

                numberPhone = removeMaskDDD(dddMask) + removeMaskNumberPhone(numberPhoneMask);

                registerDevice();
            }
        });


    }

    private String removeMaskNumberPhone(String numberPhoneMask) {

        return  numberPhoneMask.replace("-", "");
    }

    private String removeMaskDDD(String dddMask) {

        return  dddMask.replace("(", "").replace(")", "");
    }


    private void registerDevice() {

        txtRegistering.setVisibility(View.VISIBLE);



        ServiceDeviceInfo serviceDeviceInfo = new ServiceDeviceInfo(this);
        deviceInfo = serviceDeviceInfo.getInfo();
        String email = numberPhone+"@sms.com.br";
        String senha = numberPhone;


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
                    //btnRegister.setVisibility(View.VISIBLE);
                txtRegistering.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void registerUserFirebase() {

        final String token = FirebaseInstanceId.getInstance().getToken();
        User user = new User(idUser, token, numberPhone);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //String keyUser = Base64.getEncoder().encodeToString(deviceInfo.getNumberPhone().getBytes());

            FirebaseFirestore.getInstance().collection("Users").document(numberPhone)
                    .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("Debug", "Success register");
                    SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(TOKEN, token);
                    editor.commit();
                    startActivity(intent);
                    finish();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
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
