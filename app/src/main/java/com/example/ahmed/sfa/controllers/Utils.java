package com.example.ahmed.sfa.controllers;

import java.text.DecimalFormat;

/**
 * Created by xelvias on 12/2/17.
 */

public class Utils {

    /**
     *
     * @param value pass the value to be converted
     * @param decimalpoints number of decimal points required
     * @return converted value for the given decimal points
     */
    public static double decimalFix(double value,int decimalpoints){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(decimalpoints);
        return Double.parseDouble(df.format(value));
    }

    /**
     * Same as the previous method but assumes <B>2</B> as the decimal point
     * @param value pass the value to be converted
     * @return
     */
    public static double decimalFix(double value){
        return decimalFix(value,2);
    }
}
