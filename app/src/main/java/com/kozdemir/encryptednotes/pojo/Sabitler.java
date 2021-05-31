package com.kozdemir.encryptednotes.pojo;

import android.os.Parcelable;


public class Sabitler {

    public static final String DATABASE_NAME_USER = "userdb";
    public static final int DATABASE_VERSION_USER = 1;

    public static final String TABLO_KULLANICI = "kullanici_tablosu";
    public static final String KEY_USER_ID = "_id";
    public static final String ROW_USER_PASSWORD = "password";
    public static final String ROW_USER_FAKEPASSWORD = "fake_password";
    public static final String ROW_USER_GUN_DURUM = "gundurum";
    public static final String ROW_USER_GUN = "gunsayisi";
    public static final String ROW_USER_TEXTSIZE = "textsize";


    public static final String DATABASE_NAME_NOTES = "EncryptedDB";
    public static final int DATABASE_VERSION_NOTES = 1;

    public static final String TABLO_NOTLAR = "notes";
    public static final String KEY_NOT_ID = "_id";
    public static final String ROW_NOT_USER_NAME = "username";
    public static final String ROW_NOT_BASLIK = "title";
    public static final String ROW_NOT_ICERIK = "body";
    public static final String ROW_NOT_GRUP = "grup";
    public static final String ROW_NOT_TARIH = "adddate";


    public static Parcelable state=null;

    public static String loginPassword; //kullanıcının girdiği değer buraya yazılıyor
    public static String loginUserName; //kullanıcının girdiği değer buraya yazılıyor

    public static int yaziBoyutu = 18;
    public static int lastPosition =-1;


}
