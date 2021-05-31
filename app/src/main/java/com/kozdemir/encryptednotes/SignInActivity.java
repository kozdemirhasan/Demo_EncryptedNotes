package com.kozdemir.encryptednotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kozdemir.encryptednotes.pojo.Crypt;
import com.kozdemir.encryptednotes.pojo.Sabitler;

public class SignInActivity extends AppCompatActivity {
    EditText userNameSignInEditText;
    EditText passwordSignInEditText;
    SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userNameSignInEditText = findViewById(R.id.userNameSignInEditText);
        passwordSignInEditText = findViewById(R.id.passwordSignInEditText);
    }

    public void creatNewUser(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    public void signInButton(View view) {
        try {

            Crypt crypt = new Crypt();
            final String userName = userNameSignInEditText.getText().toString();
            final String password = passwordSignInEditText.getText().toString();
            final String userNameEncrypt = crypt.encrypt(userName, password);
            final String passwordEncrypt = crypt.encrypt(password, password);


            database = this.openOrCreateDatabase("EncryptedDB", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM users WHERE username =? AND password =?",
                    new String[]{userName, passwordEncrypt});


            int userNameIndex = cursor.getColumnIndex("username");
            int passwordIndex = cursor.getColumnIndex("password");
            // int dateIndex = cursor.getColumnIndex("date");


            while (cursor.moveToNext()) {
                if (userName.equals(cursor.getString(userNameIndex)) &&
                        passwordEncrypt.equals(cursor.getString(passwordIndex))) {

                //giris yapan kullnaiciyi local db kayit ediyoruz
                    SharedPreferences sharedPreferences = this.getSharedPreferences("com.kozdemir.encryptednotes", Context.MODE_PRIVATE);
                    //  String storedUser = sharedPreferences.getString("storedUser", "");
                    sharedPreferences.edit().putString("storedUser", userName).apply();

                    //giris yapilan sifre ve kullanici adini sabitlere kaydediyoruz
                    Sabitler.loginPassword = password;
                    Sabitler.loginUserName = userName;

                    //kullanici kayitli ise NotesActivity e git
                    Intent intent = new Intent(SignInActivity.this, NotesActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "Falsch user name und/oder password", Toast.LENGTH_LONG).show();
                }


            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}