package com.example.ahmed.sfa.controllers;

import android.widget.TextView;

import com.example.ahmed.sfa.R;

import java.util.Calendar;

/**
 * Created by Ahmed on 3/6/2017.
 */

public class DateManager {
    private static String[] days= {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private static String DEVIDER = "/";
    Calendar cal= Calendar.getInstance();



    public static String dateToday(){

        Calendar cal  = Calendar.getInstance();
        String date="1993/12/8"; //intializing a value that doesnt exist to avoid null values
        String year = ""+cal.get(Calendar.YEAR);
        String month = ""+(cal.get(Calendar.MONTH)+1);
        String day = ""+cal.get(Calendar.DAY_OF_MONTH);
        date = year+DEVIDER+month+DEVIDER+day;
        return date;
    }

    //this will be used to create a date with the format applicaton require
    //but using androids default values
    public static String getDate(int year,int month,int dayOfMonth){
         String date = year+DEVIDER+(month+1)+DEVIDER+dayOfMonth;
        return date;
    }

    public static boolean isFirstAfterSecond(String date1,String date2){
        boolean result=false;

        return false;
    }

    public static String dayToday(){
        Calendar cal= Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH)+"";
    }

    public static String getDayOfWeek(){
        return days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1];
    }

    public static String getTimeFull(){
        String time="";
        Calendar cal = Calendar.getInstance();
        time+=cal.get(Calendar.HOUR)+" : ";
        time+=cal.get(Calendar.MINUTE) + " : ";
        time+=cal.get(Calendar.AM_PM);
        return time;
    }

}
