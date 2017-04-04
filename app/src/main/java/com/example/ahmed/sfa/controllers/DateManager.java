package com.example.ahmed.sfa.controllers;

import android.widget.TextView;

import com.example.ahmed.sfa.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ahmed on 3/6/2017.
 */

public class DateManager {
    private static String[] days= {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private static String DEVIDER = "/";

    //MM/DD/YYYY
    Calendar cal= Calendar.getInstance();

    //break the string into three integer values

    public static int[] breakApart(String date){
        int[] result = new int[3];

        int index = date.indexOf(DEVIDER);//month
        result[0] = Integer.parseInt(date.substring(0,index));
        date = date.substring(index+1);

        index = date.indexOf(DEVIDER);//day
        result[1] = Integer.parseInt(date.substring(0,index));
        date = date.substring(index+1);

        result[2] = Integer.parseInt(date);//year
        return result;

    }

    public static Date getJavaDate(String date){
        int[] partedDate  = breakApart(date);
        Date dateObj = new Date(partedDate[2],partedDate[0],partedDate[1]);
        return dateObj;
    }

    public static String dayBefore(String date){
        Date javaDate = getJavaDate(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(javaDate);
        cal.add(Calendar.DAY_OF_MONTH,-1);

        Date dt = cal.getTime();
        return getDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));

    }

    public static String dateToday(){

        Calendar cal  = Calendar.getInstance();
        //String date="12/8/1993"; //intializing a value that doesnt exist to avoid null values
        String date;

        int yearInt = cal.get(Calendar.YEAR);
        int monthInt = (cal.get(Calendar.MONTH)+1);
        int dayVal = cal.get(Calendar.DAY_OF_MONTH);

        date = getDate(yearInt,monthInt,dayVal);
        return date;
    }

    //this will be used to create a date with the format applicaton require
    //month starts from 1-12;
    public static String getDate(int year,int month,int dayOfMonth){

        String yearStr = "";
        if(year<10)yearStr+="0";
        yearStr+=year;

        String monthStr = "";
        if(month<10)monthStr+="0";
        monthStr+=month;


        String dayStr = "";
        if(dayOfMonth<10)dayStr+="0";
        dayStr+=dayOfMonth;

        String date = (month+1)+DEVIDER+monthStr+DEVIDER+yearStr;
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
        time+=cal.get(Calendar.SECOND)+" ";
        time+=cal.get(Calendar.AM_PM);
        return time;
    }

}
