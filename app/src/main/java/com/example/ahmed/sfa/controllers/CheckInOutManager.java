package com.example.ahmed.sfa.controllers;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.ahmed.sfa.controllers.database.CheckingDBAdapter;
import com.example.ahmed.sfa.models.CheckInCheckOutActions;
import com.example.ahmed.sfa.models.CheckSession;

/**
 * Created by Ahmed on 3/15/2017.
 */

public class CheckInOutManager {
    Context context;
    CheckingDBAdapter dbAdapter;
    CheckInCheckOutActions actions;

    public CheckInOutManager(Context c, CheckInCheckOutActions actions){
        this.context = c;
        this.actions = actions;
        dbAdapter = new CheckingDBAdapter(c);
    }

    public ArrayAdapter<String> getLocationsArrayAdapter(){
        return dbAdapter.getCheckPointsArrayAdapter();
    }

    public boolean isCheckedIn(){
        if (dbAdapter.checkLogedInorNot()>0){
            return true;
        }else{
            return false;
        }
    }

    public boolean checkIn(CheckSession session){
        if(!isCheckedIn()) {
            int results = dbAdapter.checkIn(session);
            if (results > 0) {
                actions.onCheckedIn();
            } else {
                actions.onNotCheckedIn();
                return false;
            }
            return true;
        }else{
            actions.onNotCheckedIn();
            return false;
        }
    }

    public void checkOut(CheckSession session){
        if(isCheckedIn()){
            dbAdapter.checkOut(session);
        }
        actions.onCheckOut();
    }
}
