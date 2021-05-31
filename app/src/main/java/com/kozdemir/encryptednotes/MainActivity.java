package com.kozdemir.encryptednotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //login olan kullanici kontrolü yapiyoruz, eger login olan kullanici varsa direkt notlar sayfasina gönderiyoruz
        SharedPreferences sharedPreferences2 = this.getSharedPreferences("com.kozdemir.encryptednotes", Context.MODE_PRIVATE);
        String storedUser = sharedPreferences2.getString("storedUser", "");

        if (!storedUser.equals("")) {
            Intent intent = new Intent(getApplicationContext(), NotesActivity.class);
            startActivity(intent);
            finish();

        } else {
            //login olan kullanici yoksa Login (sign in sayfasina gönderiyoruz)
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            finish();

        }


    }


}