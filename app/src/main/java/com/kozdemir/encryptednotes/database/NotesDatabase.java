package com.kozdemir.encryptednotes.database;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.kozdemir.encryptednotes.database.NotesDBHelper;
import com.kozdemir.encryptednotes.pojo.Crypt;

import com.kozdemir.encryptednotes.pojo.Note;
import com.kozdemir.encryptednotes.pojo.Sabitler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.*;

public class NotesDatabase {

    private SQLiteDatabase db;
    private final Context context;
    private final NotesDBHelper dbhelper;


    //constructer
    public NotesDatabase(Context c) {
        context = c;
        //Dphelper opjesiyle yeni veritabanı oluşturuluyor.
        dbhelper = new NotesDBHelper(context, Sabitler.DATABASE_NAME_NOTES, null,
                Sabitler.DATABASE_VERSION_NOTES);

    }

    /*
     * Veritabanını operasyonlara kapatmak
     * için kullandığımız method.
     */
    public void kapat() {
        db.close();
    }

    /*
     * Veritabanını yazma ve okuma için açtığımız method
     * **!**
     * ->yazmak için aç, yazma operasyonu değilse exception ver catch bloğunda okumak için aç
     */
    public void ac() throws SQLiteException {
        try {
            db = dbhelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            Log.v("db exception caught", ex.getMessage());
            db = dbhelper.getReadableDatabase();
        }
    }


    public void tumNotlariSil() {
        db.delete(Sabitler.TABLO_NOTLAR, null, null);
    }


    //grubun tüm notları al getir
    public ArrayList<Note> grubunNotlariniGetir(String grupAdi) {
        ArrayList<Note> notlar = new ArrayList<Note>();
        Cursor c;
        try {
            // new String[]{Sabitler.KEY_NOT_ID, Sabitler.ROW_NOT_BASLIK, Sabitler.ROW_NOT_TARIH, Sabitler.ROW_NOT_GRUP}
            c = db.query(Sabitler.TABLO_NOTLAR, new String[]{Sabitler.KEY_NOT_ID, Sabitler.ROW_NOT_BASLIK,
                            Sabitler.ROW_NOT_TARIH, Sabitler.ROW_NOT_GRUP},
                    Sabitler.ROW_NOT_GRUP + "=? ",
                    new String[]{grupAdi},
                    null, null, Sabitler.ROW_NOT_TARIH + " desc");

            //   c = db.query(Sabitler.TABLO_NOTLAR, null, null, null, null, null,null);

        } catch (Exception ex) {
            ex.printStackTrace();
            c = null;
        }

//Curson tipinde gelen notları teker teker dolaşıyoruz
        if (c != null) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndex(Sabitler.KEY_NOT_ID));
                String baslik = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_BASLIK));
                String grup = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_GRUP));
                //  String icerik = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_ICERIK));

                DateFormat dateFormat = DateFormat.getDateTimeInstance();
                long trhfark = new Date().getTime() - c.getLong(c.getColumnIndex(Sabitler.ROW_NOT_TARIH));
                String tarih = null;
                long birgun = 24 * 60 * 60 * 1000;
                if (trhfark <= 0) {
                    trhfark = trhfark * (-1);
                }

                if (trhfark < birgun) {
                    SimpleDateFormat bicimAyniGun = new SimpleDateFormat("hh:mm");
                    tarih = bicimAyniGun.format(new Date(c.getLong(c
                            .getColumnIndex(Sabitler.ROW_NOT_TARIH))).getTime());
                } else {
                    SimpleDateFormat bicim = new SimpleDateFormat("dd MMM yy");
                    tarih = bicim.format(new Date(c.getLong(c
                            .getColumnIndex(Sabitler.ROW_NOT_TARIH))).getTime());
                }
                Note not = new Note();
                not.set_id(id);
                not.setTitle(baslik);
                not.setGroup(grup);
                not.setDate(tarih);
                notlar.add(not);
            }
        }
        return notlar;
    }


    //tüm gruplari al getir, kriptolu metin
    public ArrayList<String> tumGruplariGetir() throws Exception {
        ArrayList<String> gruplar = new ArrayList<>();
        Cursor c = tumGruplariAlGetir();

//Curson tipinde gelen notları teker teker dolaşıyoruz
        if (c != null) {
            while (c.moveToNext()) {
                String grup = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_GRUP));
                gruplar.add(grup);
            }
        }
        Collections.sort(gruplar);
        return gruplar;
    }

    public Cursor tumGruplariAlGetir() {
        try {
            Cursor c = db.query(true, Sabitler.TABLO_NOTLAR,
                    new String[]{Sabitler.ROW_NOT_GRUP}, null, null,
                    null, null, null, null);
            return c;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //başlangıçta tüm notların id ve tarihlerini al getir
    public List<Note> tumKayitlar() {
        List<Note> notlar = new ArrayList<Note>();
        Cursor c = tumNotlar();

        //Curson tipinde gelen notları teker teker dolaşıyoruz
        if (c != null) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndex(Sabitler.KEY_NOT_ID));

                String date = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_TARIH));
                Note gecici = new Note(id, date);

                //Veritabanındaki tüm notları birer birer ArrayList’e kaydediyoruz.
                notlar.add(gecici);

            }
        }


        return notlar;

    }

    public Cursor tumNotlar() {
        try {
            //Sabitler.ROW_USER_ID + " = ? ", new String[]{String.valueOf(idKullanici)}
            Cursor c = db.query(Sabitler.TABLO_NOTLAR, null, null, null, null, null
                    , null, null);
            return c;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }


    }

    /*
     * Veritabanına not eklediğimiz method.
     * insert Yapısı:
     * —-db.insert(String table, String nullColumnHack, ContentValues icerikDegerleri)
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long notEkle(String baslik, String icerik, String grup) throws Exception {
        try {
            //get Date
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);

            ContentValues yeniDegerler = new ContentValues();

            Crypt crypt = new Crypt();

            yeniDegerler.put(Sabitler.ROW_NOT_USER_NAME, Sabitler.loginUserName);
            yeniDegerler.put(Sabitler.ROW_NOT_BASLIK, crypt.encrypt(baslik, Sabitler.loginPassword));
            yeniDegerler.put(Sabitler.ROW_NOT_ICERIK, crypt.encrypt(icerik, Sabitler.loginPassword));
            yeniDegerler.put(Sabitler.ROW_NOT_GRUP, crypt.encrypt(grup, Sabitler.loginPassword));
            yeniDegerler.put(Sabitler.ROW_NOT_TARIH, date);

            return db.insert(Sabitler.TABLO_NOTLAR, null, yeniDegerler);

        } catch (SQLiteException ex) {

            Log.v("ekleme isleminde hata !",
                    ex.getMessage());

            return -1;
        }
    }

    public void notGuncelle(int id, String konu, String icerik, String grup) {

        ContentValues guncelDegerler = new ContentValues();
        String[] idArray = {String.valueOf(id)};

        guncelDegerler.put(Sabitler.ROW_NOT_BASLIK, konu);
        guncelDegerler.put(Sabitler.ROW_NOT_ICERIK, icerik);
        guncelDegerler.put(Sabitler.ROW_NOT_GRUP, grup);
        guncelDegerler.put(Sabitler.ROW_NOT_TARIH, System.currentTimeMillis());
        db.update(Sabitler.TABLO_NOTLAR, guncelDegerler, Sabitler.KEY_NOT_ID + " =? ", idArray);


    }

    public int tumTarihGuncelle(long tarih) {
        ContentValues guncelDegerler = new ContentValues();
        guncelDegerler.put(Sabitler.ROW_NOT_TARIH, tarih);
        try {
            db.update(Sabitler.TABLO_NOTLAR, guncelDegerler, null, null);
            return 1;
        } catch (Exception ex) {
            return -1;
        }

    }

    public void idIleNotSil(int id) {
        db.delete(Sabitler.TABLO_NOTLAR, Sabitler.KEY_NOT_ID + " =" + id, null);
    }

    public void eskileriSil(List<Integer> idler) {
        for (int i = 0; i < idler.size(); i++) {
            String[] idArray = {String.valueOf(idler.get(i))};
            db.delete(Sabitler.TABLO_NOTLAR, Sabitler.KEY_NOT_ID + " = ?", idArray);
        }
    }

    //id ile not getir
    public Note notGetir(String id) {

        Cursor c;
        try {
            //Sabitler.ROW_USER_ID + " = ? ", new String[]{String.valueOf(idKullanici)}
            c = db.query(Sabitler.TABLO_NOTLAR, null, Sabitler.KEY_NOT_ID + " = ? ",
                    new String[]{id}, null
                    , null, null);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

//Curson tipinde gelen notları teker teker dolaşıyoruz

        if (c.moveToNext()) {
            int idx = c.getInt(c.getColumnIndex(Sabitler.KEY_NOT_ID));
            String baslik = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_BASLIK));
            String icerik = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_ICERIK));
            String grup = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_GRUP));
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            long trhfark = new Date().getTime() - c.getLong(c.getColumnIndex(Sabitler.ROW_NOT_TARIH));
            String tarih;
            if (trhfark < 24 * 60 * 60 * 1000) {
                SimpleDateFormat bicimAyniGun = new SimpleDateFormat("hh:mm:ss");
                tarih = bicimAyniGun.format(new Date(c.getLong(c
                        .getColumnIndex(Sabitler.ROW_NOT_TARIH))).getTime());
            } else {
                SimpleDateFormat bicim = new SimpleDateFormat("dd-MM-yyyy");
                tarih = bicim.format(new Date(c.getLong(c
                        .getColumnIndex(Sabitler.ROW_NOT_TARIH))).getTime());
            }

            Note not = new Note(idx, baslik, icerik, tarih, grup);

            return not;

        } else {
            makeText(null, "hata oluştu", LENGTH_SHORT).show();
            return null;
        }


    }

    //parola değiştiriken tüm notların şifrelemesinin değişmesi için tüm notları al getir
    public List<Note> butunNotlar() {
        List<Note> notlar = new ArrayList<Note>();
        Cursor c = tumNotlar2();

        //Curson tipinde gelen notları teker teker dolaşıyoruz
        if (c != null) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndex(Sabitler.KEY_NOT_ID));
                String grup = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_GRUP));
                String baslik = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_BASLIK));
                String icerik = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_ICERIK));
                String date = c.getString(c.getColumnIndex(Sabitler.ROW_NOT_TARIH));

                Note gecici = new Note(id, grup, baslik, icerik, date);

                //Veritabanındaki tüm notları birer birer ArrayList’e kaydediyoruz.
                notlar.add(gecici);
            }
        }

        return notlar;

    }

    public Cursor tumNotlar2() {
        try {
            //Sabitler.ROW_USER_ID + " = ? ", new String[]{String.valueOf(idKullanici)}
            Cursor c = db.query(Sabitler.TABLO_NOTLAR, null, null, null, null, null
                    , null, null);
            return c;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void notlariYenidenYaz(Note note) {
        ContentValues yeniDegerler = new ContentValues();

        String[] idArray = {String.valueOf(note.get_id())};
        yeniDegerler.put(Sabitler.ROW_NOT_GRUP, note.getGroup());
        yeniDegerler.put(Sabitler.ROW_NOT_BASLIK, note.getTitle());
        yeniDegerler.put(Sabitler.ROW_NOT_ICERIK, note.getBody());
        yeniDegerler.put(Sabitler.ROW_NOT_TARIH, note.getDate());
        db.update(Sabitler.TABLO_NOTLAR, yeniDegerler, Sabitler.KEY_NOT_ID + " =?", idArray);
    }

}