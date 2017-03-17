package com.example.ahmed.sfa.controllers.database;

import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.CheckSession;

import java.util.ArrayList;

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

    public int checkLogedInorNot(){
        openDB();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM Tr_CheckInCheckOut WHERE InPoint is not null AND OutPoint is null ;",null);

        if(cursor.moveToNext()){
            closeDB();
            Toast.makeText(context,""+cursor.getInt(0),Toast.LENGTH_LONG).show();
             return cursor.getInt(0);

        }
        closeDB();
        return 0;//if an error occurs while retriving data user will be notified as not logged in
        //to overcome this problem this method should throw an exception
    }

    public int checkIn(CheckSession session){
        int result=0;
            openDB();

            db.execSQL("INSERT INTO Tr_CheckInCheckOut(SerialCode,Date,InPoint,InTime,InComment,Latitude_CheckIn,Longitude_CheckIn,isUpload)" +
                    " VALUES('"+session.getSerialCode()+"','"+session.getDate()+"','"+session.getPointName()+"','"+session.getTime()+"','"+session.getComment()+"','"+
                    session.getLocation().getLatitude()+"','"+session.getLocation().getLongitude()+"',"+1+");");

            closeDB();
            result++;
        return result;
    }

    public void checkOut(CheckSession session){
        openDB();
            db.execSQL("UPDATE Tr_CheckInCheckOut SET OutPoint='"+session.getPointName()+"',OutTime='"+session.getTime()+
                    "',OutComment='"+session.getComment()+"',Latitude_CheckOut='"+session.getLocation().getLatitude()
                    +"',Longitude_CheckOut='"+session.getLocation().getLongitude()+"',isUpload="+1+" WHERE SerialCode in (SELECT SerialCode FROM Tr_CheckInCheckOut WHERE InPoint is not null AND OutPoint is null );");
        closeDB();
    }
}

