package controllers;

import java.util.Calendar;

/**
 * Created by DELL on 3/17/2017.
 */

public class DateManager {
    public static String dateToday(){
        Calendar cal  = Calendar.getInstance();
        String date="1993/12/8"; //intializing a value that doesnt exist to avoid null values
        String year = ""+cal.get(Calendar.YEAR);
        String month = ""+(cal.get(Calendar.MONTH)+1);
        String day = ""+cal.get(Calendar.DAY_OF_MONTH);
        date = year+"/"+month+"/"+day;
        return date;
    }

    public static boolean isFirstAfterSecond(String date1,String date2){
        boolean result=false;

        return false;
    }
}
