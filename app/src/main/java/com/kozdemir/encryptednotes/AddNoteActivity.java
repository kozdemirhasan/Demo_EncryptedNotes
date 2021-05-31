package com.kozdemir.encryptednotes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.kozdemir.encryptednotes.database.NotesDatabase;
import com.kozdemir.encryptednotes.pojo.Sabitler;
import com.kozdemir.encryptednotes.pojo.Crypt;


import java.util.ArrayList;


public class AddNoteActivity extends Activity {
    Vibrator vib;
    NotesDatabase dba;
    EditText titleEditText, bodyEdittext;
    private String m_Text = "";
    String secilenGrup = "DEFAULT";
    TextView twGruplar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        twGruplar = findViewById(R.id.twGruplar);
        titleEditText = findViewById(R.id.titleEditText);
        bodyEdittext = findViewById(R.id.bodyEditText);
    }


    //add Note butonuna tiklandiginda
    public void addNoteButton(View view) {
        vib.vibrate(40);

        try {
            dba = new NotesDatabase(AddNoteActivity.this);
            dba.ac();

            /*
             * Note ekleme alanları boş ise uyarı ver değilse notu ekle*/

            if (titleEditText.getText().length() != 0 && bodyEdittext.getText().length() != 0) {

                new IslemTask().execute(); //multi thread işlem başladı... not kayıt ediliyor

            } else {
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Başlık ve içerik giriniz",
                        duration);
                toast.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class IslemTask extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog;

        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(AddNoteActivity.this,
                    "Note kaydediliyor",
                    "Lütfen bekleyiniz...");
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(String... params) {

            notEkle();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //not kaydedildikten sonra konu ve içerik alanını temizle
            //ve Notlar sayfasına git

            titleEditText.setText("");
            bodyEdittext.setText("");

            Intent i = new Intent(AddNoteActivity.this, NotesActivity.class);
            startActivity(i);
            finish();

            progressDialog.dismiss();
        }

    }

    /*
     * EditTextlerden aldığı verileri notEkle methoduna gönderen method
     */


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notEkle() {

        try {
            long x = dba.notEkle(titleEditText.getText().toString(), bodyEdittext.getText().toString(), secilenGrup);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dba.kapat();
        }


    }

    public void groupButton(View view) {
        vib.vibrate(30);

        grupSec(view, grupElemanlari());
    }

    private void grupSec(final View anchor, ArrayList<String> grupItems) {
        Crypt crypt = new Crypt();
        PopupMenu popupMenu = new PopupMenu(anchor.getContext(), anchor);
        popupMenu.getMenu().add("DEFAULT");
        for (int i = 0; i < grupItems.size(); i++) {
            try {
                String grupElemani = crypt.decrypt(grupItems.get(i), Sabitler.loginPassword);
                if (!grupElemani.equals("DEFAULT")) {
                    popupMenu.getMenu().add(grupElemani);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        popupMenu.getMenu().add("(+) YENİ GRUP");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                secilenGrup = item.getTitle().toString().trim().toUpperCase();
                if (secilenGrup.toString().equals("(+) YENİ GRUP")) {
                    //yeni grup ekleme ekranı gelsin

                    yeniGrupAl();

                } else {
                    twGruplar.setText(secilenGrup);
                }


                return true;
            }
        });

    }

    public ArrayList<String> grupElemanlari() {
        //Veritabanından tüm grupları al getir
        NotesDatabase dba = new NotesDatabase(AddNoteActivity.this);
        dba.ac();
        ArrayList<String> grupItems = null;
        try {
            grupItems = dba.tumGruplariGetir();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dba.kapat();
        return grupItems;

    }

    public void yeniGrupAl() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
        builder.setTitle("Yeni grup ekle");

// Set up the input
        final EditText input = new EditText(AddNoteActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString().toUpperCase();
                if (!m_Text.toString().equals("")) {
                    secilenGrup = m_Text;
                    twGruplar.setText(m_Text);
                } else {
                    secilenGrup = "DEFAULT";
                    twGruplar.setText(secilenGrup);
                }

            }
        });
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                secilenGrup = "DEFAULT";
                twGruplar.setText(secilenGrup);
                dialog.cancel();
            }
        });

        builder.show();
    }

}