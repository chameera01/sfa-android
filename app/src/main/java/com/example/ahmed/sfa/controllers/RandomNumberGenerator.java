package com.example.ahmed.sfa.controllers;

import android.widget.Switch;

import java.util.Random;

/**
 * Created by Ahmed on 3/7/2017.
 */

public class RandomNumberGenerator {
    private static final String ALPHABAT = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALPHANUMERIC = "0ABCDE1FGHI2JKLMNO3PQRST4UVWXYZ5abcdefghij6klmnopqrst7uvwx89yz";
    private static final String NUMERIC = "0123456789";
    public static final int GENERATE_ALPHABANUMERIC= 0;
    public static final int GENERATE_ALPHABATONLY = 1;
    public static final int GENERATE_NUMERICONLY =2;

    private static Random random;

    public RandomNumberGenerator(){
        random = new Random();
    }

    public static String generateRandomCode(int type,String beginwith,String endwith,int length){
        random = new Random();
        String togenfrom="";
        switch (type){
            case GENERATE_ALPHABANUMERIC:
                 togenfrom  = ALPHANUMERIC;
                 break;
            case GENERATE_ALPHABATONLY:
                togenfrom = ALPHABAT;
                break;

            case GENERATE_NUMERICONLY:
                togenfrom = ALPHANUMERIC;
                break;

        }
        String genCode="";
        int requiredLength= length-beginwith.length()-endwith.length();

        for (int i=0;i<requiredLength;i++){
            genCode+=togenfrom.charAt(random.nextInt(togenfrom.length()));
        }

        return beginwith+genCode+endwith;
    }


    public static String generateRandomCode(int type,String beginWith,int length){
        return generateRandomCode(type,beginWith,"",length);


    }

    public static String generateRandomCode(int type,int length){
        return generateRandomCode(type,"","",length);
    }
}