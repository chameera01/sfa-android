package com.example.ahmed.sfa.controllers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Ahmed on 3/5/2017.
 */

public class PermissionManager {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION=101;
    public static final int CAMERA_REQUEST = 1888;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";

    private  Activity activity;
    public PermissionManager(Activity activity){
        this.activity = activity;
    }


    public boolean checkForLocationPermission(){
        if(ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}
                    , MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        }else{
            return true;
        }
    }

    public boolean checkForCameraPermission(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
            return false;
            /** Should we show an explanation? no so commenting it
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CAMERA)) {

                showAlert();
                return false;
            } else {
                // No explanation needed, we can request the permission.
                showSettingsAlert("App needs to access the Camera.");
                return false;
            }*/
        }else{
            return true;
        }
    }

    public boolean checkPermissionToExternalStorage(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return  false;
    }


    private void showSettingsAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);

        /**this a our apps dialog box to request permission but since android also request the same we comment it
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(activity);
                    }
                });

        alertDialog.show();*/
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }

        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                });
        alertDialog.show();
    }
}
