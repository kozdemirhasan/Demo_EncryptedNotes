package com.kozdemir.encryptednotes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SignUpActivity extends AppCompatActivity {

    EditText signUpUserNameEditText;
    EditText signUpPasswordEdittext;
    EditText signUpPasswordRepeatEditText;
    SQLiteDatabase database;
    SharedPreferences sharedPreferences, sharedPreferences2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpUserNameEditText = findViewById(R.id.signUpUserNameEditText);
        signUpPasswordEdittext = findViewById(R.id.passwordSignInEditText);
        signUpPasswordRepeatEditText = findViewById(R.id.signUpPasswordRepeatEditText);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setSignUpButton(View view) {
        String userName = signUpUserNameEditText.getText().toString();
        String password = signUpPasswordEdittext.getText().toString();
        String passwordRepeat = signUpPasswordRepeatEditText.getText().toString();

        if (password.matches("") || userName.matches("")) {
            Toast.makeText(getApplicationContext(), "kullanici adi veya password bos olamaz", Toast.LENGTH_LONG).show();

        } else if (!password.equals(passwordRepeat)) {
            Toast.makeText(getApplicationContext(), "Password  ve PasswordRepait esit degil", Toast.LENGTH_LONG).show();
        } else if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password en az 6 karakter olmali", Toast.LENGTH_LONG).show();

        } else {

            userCreat(userName, password);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void userCreat(String userName, String password) {

        try {
            //get Date
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);

            database = this.openOrCreateDatabase("EncryptedDB", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY, " +
                    "username VARCHAR UNIQUE, " +
                    "password VARCHAR, " +
                    "date VARCHAR)");

            String sqlString = "INSERT INTO users (username, password, date) VALUES (?, ?, ?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
            sqLiteStatement.bindString(1, userName);
            sqLiteStatement.bindString(2, password);
            sqLiteStatement.bindString(3, date);
            sqLiteStatement.execute();


            //kullanici basarili bir sekilde olusturuldu ise:
            Toast.makeText(getApplicationContext(), userName + " created", Toast.LENGTH_LONG).show();

            //son kullanici bilgilerini kalici hafizaya kaydet
            sharedPreferences = this.getSharedPreferences("com.kozdemir.encryptednotes", Context.MODE_PRIVATE);
            String storedUser = sharedPreferences.getString("storedUser", "");

            sharedPreferences.edit().putString("storedUser",userName).apply();


            if (!storedUser.equals("")) {
              //  Toast.makeText(getApplicationContext(), "Son kullanici1111: " + storedUser, Toast.LENGTH_LONG).show();

            }

            getAlleUser();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), userName + " daha önce olusturulmus", Toast.LENGTH_LONG).show();
        }


    }


    public void getAlleUser() {
        try {
            database = this.openOrCreateDatabase("EncryptedDB", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM users ", null);

            int userNameIndex = cursor.getColumnIndex("username");
            int passwordIndex = cursor.getColumnIndex("password");
            int dateIndex = cursor.getColumnIndex("date");


            while (cursor.moveToNext()) {
            /*    artNameText.setText(cursor.getString(artNameIx));
                paintNameText.setText(cursor.getString(painterNameIx));
                yearText.setText(cursor.getString(yearIx));

                byte[] bytes = cursor.getBlob(imageIx);
                //byte[] dizisini image e dönüstürüyoruz
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
                */

                System.out.println("user name: " + cursor.getString(userNameIndex));
                System.out.println("pasword: " + cursor.getString(passwordIndex));
                System.out.println("date: " + cursor.getString(dateIndex));
            }

            cursor.close();

            //WICHTIG !!!!!
            //Intent ile tüm sayfalari önce kapatip sonra istedigimiz Sayfaya gidiyoruz
            Intent intent = new Intent(SignUpActivity.this, NotesActivity.class);
            //bu araya bayrak ekliyerek önce calisan tüm sayfalari kapatiyoruz,
            //daha sonra ManinActivity i yeniden aciyoruz (onCreat metodunun calisabilmesi icin)
            //eger MainActivity de onCreat calismaz ise son eknelenen kaydi göremayiz
            startActivity(intent);

            //aktivitiy i kapatiyoruz, ancak buradan sonra önceki aktivitiy bastan baslamadigi icin hata verecektir,
            //bu hatali bir kullanim
            //finish();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}