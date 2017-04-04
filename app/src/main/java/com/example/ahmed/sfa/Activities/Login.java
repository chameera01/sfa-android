package com.example.ahmed.sfa.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.service.JsonRequestListerner;

public class Login extends AppCompatActivity implements JsonRequestListerner {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_login);
    }


    public void checkLogin(){

    }

    @Override
    public void receiveData(String result,String filter) {

    }
}
