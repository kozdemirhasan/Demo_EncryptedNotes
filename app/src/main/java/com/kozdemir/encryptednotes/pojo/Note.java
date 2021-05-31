package com.kozdemir.encryptednotes.pojo;

import java.util.ArrayList;

public class Note {
    public int _id;
    public String title;
    public String body;
    public String group;
    public String date;

    public Note() {
    }

    public Note(int _id,String group, String title, String body,  String date) {
        this._id = _id;
        this.title = title;
        this.body = body;
        this.group = group;
        this.date = date;
    }

    public Note(int _id, String date) {
        this._id = _id;
        this.date = date;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}