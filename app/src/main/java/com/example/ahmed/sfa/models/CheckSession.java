package com.example.ahmed.sfa.models;

import android.location.Location;

import com.example.ahmed.sfa.controllers.RandomNumberGenerator;

/**
 * Created by Ahmed on 3/15/2017.
 */

public class CheckSession {
    //values for checking in
    private String SerialCode;
    private String Date;
    private String Time;
    private Location location;
    private String PointName;
    private String Comment;



    private boolean upload;



    public CheckSession(String date,String time,Location loc,String pointName,String comment,boolean upload){
        this.Date = date;
        this.Time = time;
        this.location = loc;
        this.PointName = pointName;
        this.Comment = comment;
        this.upload = upload;

        //generate reandom sereial code and asign it
        String code = RandomNumberGenerator.generateRandomCode(RandomNumberGenerator.GENERATE_ALPHABANUMERIC,"CHK",14);

        this.SerialCode = code;
    }


    private CheckSession(String serialCode, String date, String time, Location location, String pointName, String comment, String outTime, Location outLocation, String outPointName, String outComment, boolean upload) {
        SerialCode = serialCode;
        Date = date;
        Time = time;
        this.location = location;
        PointName = pointName;
        Comment = comment;

        this.upload = upload;
    }

    public String getSerialCode() {
        return SerialCode;
    }

    public void setSerialCode(String serialCode) {
        SerialCode = serialCode;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPointName() {
        return PointName;
    }

    public void setPointName(String pointName) {
        PointName = pointName;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }



    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }
}
