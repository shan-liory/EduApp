package com.example.eduapp.ui.profile;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eduapp.RegisterActivity;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private Button logout_btn;


    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profile fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }

}