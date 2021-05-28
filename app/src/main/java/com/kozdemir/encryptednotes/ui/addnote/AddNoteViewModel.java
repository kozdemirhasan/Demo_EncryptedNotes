package com.kozdemir.encryptednotes.ui.addnote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddNoteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddNoteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Add Note Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}