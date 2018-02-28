package com.example.ahmed.sfa.controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.ahmed.sfa.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Ahmed on 3/4/2017.
 */

public class ImageManager {
    /**public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    */
    public static final int CAMERA_REQUEST = 1888;
    public static File root, image;
    Activity activity;
    private PermissionManager permissionManager;


    public ImageManager(Activity activity){
        this.activity = activity;
        permissionManager = new PermissionManager(activity);
    }


    public void capture(){
        if(permissionManager.checkForCameraPermission()){
            openCamera();
        }
    }

    /**
    public void capture(){
        if (ContextCompat.checkSelfPermission(c, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (getFromPref(c, ALLOW_KEY)) {
                showSettingsAlert("App needs to access the Camera.");
            } else if (ContextCompat.checkSelfPermission(c,
                    Manifest.permission.CAMERA)

                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(c,
                        Manifest.permission.CAMERA)) {

                        showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    showSettingsAlert("App needs to access the Camera.");

                }
            }
        } else {
           openCamera();
        }

    }*/

    /**
    public static void saveToPreferences(Context context, String key, Boolean allowed) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, allowed);
        prefsEditor.commit();
    }

    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(c).create();
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
                        ActivityCompat.requestPermissions(c,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                });
        alertDialog.show();
    }

    private void showSettingsAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(c).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);

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
                        startInstalledAppDetailsActivity(c);
                    }
                });

        alertDialog.show();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];

                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean
                                showRationale =
                                ActivityCompat.shouldShowRequestPermissionRationale(
                                        c, permission);

                        if (showRationale) {
                            showAlert();
                        } else if (!showRationale) {
                            // user denied flagging NEVER ASK AGAIN
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app setting
                            saveToPreferences(c, ALLOW_KEY, true);
                        }
                    }
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
    */
    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        activity.startActivityForResult(intent,CAMERA_REQUEST);
    }

    /**
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {

        if (requestCode == CAMERA_REQUEST) {
            Toast.makeText(getApplicationContext(),"Captured",Toast.LENGTH_SHORT).show();
            if(resultCode==Activity.RESULT_OK) {
                Bitmap photo = (Bitmap)data.getExtras().get("data");
                imgView.setImageBitmap(photo);
                Toast.makeText(getApplicationContext(),"Captured",Toast.LENGTH_SHORT).show();
            }
        } else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    //this method will save the image in the appication private space
    public boolean saveImage(Bitmap bitmap,String filename) {
        if (permissionManager.checkPermissionToExternalStorage()){
            //File root = c.getFilesDir();this line is for internel storage
            File root = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"sfaimages");
            if (!root.exists()) root.mkdir();


            File image = new File(root, filename);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(image);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }else{
            return false;
        }

    }

    //this method will retrieve the image from the appication private space
    //returns null if the image location is empty
    public Bitmap retrieveImage(String filename){
        Bitmap bitmap;
        if(filename==null){
            ImageView temp = new ImageView(activity);
            temp.setBackgroundResource(R.drawable.photo_placeholder);
            return  temp.getDrawingCache();
        }
        if (permissionManager.checkPermissionToExternalStorage()){
            root = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "sfaimages");
            if (!root.exists()) root.mkdir();

            image = new File(root, filename);

            try {
                FileInputStream fileInputStream = new FileInputStream(image);
                bitmap = BitmapFactory.decodeStream(fileInputStream);

                return bitmap;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }else{
            return null;
        }



        /**Bitmap bitmap=null;

        File root =  c.getFilesDir();
        if(!root.exists()) root.mkdir();
        File imagesFolder = new File(root,"images");
        if(!imagesFolder.exists())imagesFolder.mkdir();

        File image = new File(imagesFolder,filename);
        try {
            FileInputStream is = new FileInputStream(image);
            bitmap = BitmapFactory.decodeStream(is);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;*/
    }


}
