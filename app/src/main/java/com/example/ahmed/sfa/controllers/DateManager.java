package com.example.ahmed.sfa.controllers;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ahmed on 3/6/2017.
 */

public class DateManager {
    final private static String[] DAYS = {"Sunday","Monday","Tuesday","Wednesday","Thursday"
            ,"Friday","Saturday"};
    final private static String[] MONTHS = {"January","February","March","April","May","June",
            "July","August","September","October","November","December"};
    final private static String DIVIDER = "/";

    //MM/DD/YYYY
    Calendar cal= Calendar.getInstance();

    //break the string into three integer values

    public static int[] breakApart(String date){
        int[] result = new int[3];

        int index = date.indexOf(DIVIDER);//month
        result[0] = Integer.parseInt(date.substring(0,index));
        date = date.substring(index+1);

        index = date.indexOf(DIVIDER);//day
        result[1] = Integer.parseInt(date.substring(0,index));
        date = date.substring(index+1);

        result[2] = Integer.parseInt(date);//year
        return result;

    }

    public static String getMonthName(String date){
        int[] re = breakApart(date);
        return MONTHS[re[0]-1];
    }

    public static String getMonthName(int month){
        month = month==0?1:month;
        return MONTHS[month-1];
    }

    public static String getMonthName(){
        return getMonthName(dateToday());
    }

    public static Date getJavaDate(String date){
        int[] partedDate  = breakApart(date);
        Date dateObj = new Date(partedDate[2],partedDate[0],partedDate[1]);

        return dateObj;
    }

    public static String dayBefore(String date){
        int[] data = breakApart(date);
        data[0]--;
        Calendar cal = Calendar.getInstance();
        cal.set(data[2],data[0],data[1]);
        ///Log.w("Set date",cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR));
        cal.add(Calendar.DAY_OF_MONTH,-1);
        String changedDate = getDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH));
        //Log.w("red date",cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR));
        //Log.w("red date",changedDate);
        return changedDate;

    }

    public static String monthbefore(String date){
        int[] data = breakApart(date);
        Calendar cal = Calendar.getInstance();
        cal.set(data[2],data[0]-1,data[1]);
        ///Log.w("Set date",cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR));
        cal.add(Calendar.MONTH,-1);
        String changedDate = getDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH));
        //Log.w("red date",cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR));
        //Log.w("red date",changedDate);
        return changedDate;

    }

    public static String dateToday(){

        Calendar cal  = Calendar.getInstance();
        //String date="12/8/1993"; //intializing a value that doesn't exist to avoid null values
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
        yearStr+=year;

        String monthStr = "";
        if(month<10)monthStr+="0";
        monthStr+=month;


        String dayStr = "";
        if(dayOfMonth<10)dayStr+="0";
        dayStr+=dayOfMonth;

        String date = monthStr+ DIVIDER +dayStr+ DIVIDER +yearStr;
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
        return DAYS[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1];
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

    public static String getTime(){
        String time="";
        Calendar cal = Calendar.getInstance();
        time+=cal.get(Calendar.HOUR)+" : ";
        time+=cal.get(Calendar.MINUTE) + " : ";
        time+=cal.get(Calendar.AM_PM);
        return time;
    }


        public static Date getNetMonth(int monthsfromNow) {


            Calendar cal = Calendar.getInstance(); //Get the Calendar instance
            cal.add(Calendar.MONTH, +3);//Three months from now
            Date date = cal.getTime();// Get the Date object

            return date;

        }

}
