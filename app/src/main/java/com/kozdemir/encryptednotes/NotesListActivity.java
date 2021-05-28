package com.kozdemir.encryptednotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class NotesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        TextView textView = findViewById(R.id.textView2);

        SharedPreferences sharedPreferences2 = this.getSharedPreferences("com.kozdemir.encryptednotes", Context.MODE_PRIVATE);
        String storedUser = sharedPreferences2.getString("storedUser", "");

        textView.setText(storedUser);

    }

    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_note) {


        } else if (item.getItemId() == R.id.logout) {

           SharedPreferences sharedPreferences = this.getSharedPreferences("com.kozdemir.encryptednotes", Context.MODE_PRIVATE);
            String storedUser = sharedPreferences.getString("storedUser", "");
            sharedPreferences.edit().putString("storedUser","").apply();

            //Login sayfasina git
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            finish();


        }

        return super.onOptionsItemSelected(item);
    }
}