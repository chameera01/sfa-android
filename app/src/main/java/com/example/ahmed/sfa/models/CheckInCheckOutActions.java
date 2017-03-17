package com.example.ahmed.sfa.models;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ahmed on 3/15/2017.
 */

public interface CheckInCheckOutActions{
    public void onCheckedIn();
    public void onNotCheckedIn();
    public void onException();
    public void onCheckOut();
}
