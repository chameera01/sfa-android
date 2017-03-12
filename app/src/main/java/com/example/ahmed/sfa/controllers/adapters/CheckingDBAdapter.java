package com.example.ahmed.sfa.controllers.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.widget.ArrayAdapter;

import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.controllers.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 3/12/2017.
 */

public class CheckingDBAdapter extends BaseDBAdapter{
    public CheckingDBAdapter(Context context){
        super(context);
    }

    public ArrayAdapter<String> getCheckPointsArrayAdapter(){
        ArrayList<String> result = new ArrayList<>();
        openDB();
        Cursor cursor = db.rawQuery("SELECT PointDescription FROM Mst_CheckInOutPoints WHERE isActive=0;",null);
        while(cursor.moveToNext()){
            result.add(cursor.getString(0));
        }

        ArrayAdapter<String> out = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,result);
        out.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        closeDB();
        return out;

    }
}

