package com.kozdemir.encryptednotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kozdemir.encryptednotes.database.NotesDatabase;
import com.kozdemir.encryptednotes.pojo.Note;
import com.kozdemir.encryptednotes.custom.ExpListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class NotesActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    ExpandableListView expListView;
    HashMap<String, ArrayList<Note>> listItem;
    ArrayList<String> listGroup;
    ArrayList<Note> notlar;
    ExpListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        floatingActionButton = findViewById(R.id.floatingActionButton);

       /* SharedPreferences sharedPreferences = this.getSharedPreferences("com.kozdemir.encryptednotes", Context.MODE_PRIVATE);
        String storedUser = sharedPreferences.getString("storedUser", "");
        setTitle("Notes " + "(" + storedUser + ")");*/

        expListView = (ExpandableListView) findViewById(R.id.exp_list);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();


        notlariGetir();


    }

    private void notlariGetir() {
        //Veritabanından tüm grupları al getir
        NotesDatabase dba = new NotesDatabase(NotesActivity.this);
        dba.ac();
        try {
            listGroup = dba.tumGruplariGetir();// tekil olarak grup adları getirildi
        } catch (Exception e) {
            e.printStackTrace();
        }
        int notSay = 0;

        for (int i = 0; i < listGroup.size(); i++) {
            notlar = dba.grubunNotlariniGetir(listGroup.get(i));
            try {
                listItem.put(listGroup.get(i), notlar); //grubun notlarını set et
            } catch (Exception e) {
                e.printStackTrace();
            }
            notSay = notSay + notlar.size();
        }
        dba.kapat();


        if (notSay == 0) {
            Toast.makeText(getApplicationContext(), "Hiç notunuz yok", Toast.LENGTH_SHORT).show();
        } else {

            adapter = new ExpListAdapter(this, listGroup, listItem);
            expListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        }

    }

    public void floatingAddButton(View view) {

        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(40);
        Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_note) {
            Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
            startActivity(intent);
            finish();

        } else if (item.getItemId() == R.id.logout) {

            //Login sayfasina git
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            finish();


        }

        return super.onOptionsItemSelected(item);
    }
}