package com.example.ahmed.sfa.controllers.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.BaseAdapter;

/**
 * Created by Ahmed on 3/12/2017.
 */

public class BaseDBAdapter {
    protected Context context;
    protected SQLiteDatabase db;
    DBHelper dbHelper;

    protected BaseDBAdapter(Context c){
        this.context = c;
        dbHelper = new DBHelper(c);
    }

    protected boolean openDB(){
        try{
            db = dbHelper.getWritableDatabase();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    protected boolean closeDB(){
        try {
            dbHelper.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
