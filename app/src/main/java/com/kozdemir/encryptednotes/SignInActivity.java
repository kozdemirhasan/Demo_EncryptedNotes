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
        String userName = userNameSignInEditText.getText().toString();
        String password = passwordSignInEditText.getText().toString();

        String databaseUserName = "";
        String databasePassword = "";

        database = this.openOrCreateDatabase("EncryptedDB", MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE username =? AND password =?",
                new String[]{userName, password});

        //  new String[]{userNameSignInEditText.getText().toString()});

        int userNameIndex = cursor.getColumnIndex("username");
        int passwordIndex = cursor.getColumnIndex("password");
        // int dateIndex = cursor.getColumnIndex("date");


        while (cursor.moveToNext()) {

            databaseUserName = cursor.getString(userNameIndex);
            databasePassword = cursor.getString(passwordIndex);

            if (userName.equals(databaseUserName) && password.equals(databasePassword)) {
             //giris yapan kullnaiciyi local db kayit ediyoruz
                SharedPreferences sharedPreferences = this.getSharedPreferences("com.kozdemir.encryptednotes", Context.MODE_PRIVATE);
              //  String storedUser = sharedPreferences.getString("storedUser", "");
                sharedPreferences.edit().putString("storedUser", userName).apply();

                //kullanici kayitli ise NotesListActivity e git
                Intent intent = new Intent(SignInActivity.this, NotesMainActivity.class);
                startActivity(intent);
            }


        }
    }
}