package com.example.ahmed.sfa.controllers;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.CheckInCheckOutActions;
import com.example.ahmed.sfa.models.CheckSession;

import java.util.ArrayList;

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

    public ArrayAdapter<String> getLocationsArrayAdapter(boolean isAlreadyCheckedIn){
        if(isAlreadyCheckedIn)
            return dbAdapter.getCheckPointsArrayAdapter(SELECTION.OUT);
        else
            return dbAdapter.getCheckPointsArrayAdapter(SELECTION.IN);
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

    enum SELECTION {IN, OUT}

    class CheckingDBAdapter extends BaseDBAdapter {

        public CheckingDBAdapter(Context context){
            super(context);
        }

        public ArrayAdapter<String> getCheckPointsArrayAdapter(SELECTION selection){
            ArrayList<String> result = new ArrayList<>();
            openDB();
            String sql = "SELECT PointDescription FROM Mst_CheckInOutPoints WHERE isActive=0 AND Type='";
            if(selection.equals(SELECTION.IN))
                sql +="IN';";
            else
                sql +="OUT';";
            Log.w("Check qry",sql);
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                result.add(cursor.getString(0));
            }
            Log.d("CHECKIIN", "list size: " + result.size());
            String[] list = result.toArray(new String[result.size()]);


            ArrayAdapter<String> out = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
//            out.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            out.setDropDownViewResource(R.layout.custom_spinner);
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
}
