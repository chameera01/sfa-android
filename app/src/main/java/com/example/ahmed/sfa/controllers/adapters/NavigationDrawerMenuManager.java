package com.example.ahmed.sfa.controllers.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.support.annotation.NonNull;

import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ahmed.sfa.activities.AddCustomer;
import com.example.ahmed.sfa.activities.Invoice;
import com.example.ahmed.sfa.activities.ManualSync;
import com.example.ahmed.sfa.R;

import com.example.ahmed.sfa.activities.AddExtraCustomer;
import com.example.ahmed.sfa.activities.AndroidDatabaseManager;


import com.example.ahmed.sfa.activities.DisplayProductTableActivity;
import com.example.ahmed.sfa.activities.Home;
import com.example.ahmed.sfa.activities.PendingCustomer;
import com.example.ahmed.sfa.activities.SalesInvoice;
import com.example.ahmed.sfa.activities.SalesReturn;
import com.example.ahmed.sfa.activities.StockView;

import com.example.ahmed.sfa.controllers.CheckInOutManager;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.PermissionManager;
import com.example.ahmed.sfa.models.CheckInCheckOutActions;
import com.example.ahmed.sfa.models.CheckSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Ahmed on 3/10/2017.
 */

public class NavigationDrawerMenuManager implements NavigationView.OnNavigationItemSelectedListener,CheckInCheckOutActions {
    private Activity activity;
    NavigationView navview;

    //String[] places ;
    DrawerLayout drawer ;

    public NavigationDrawerMenuManager(final Activity activity){

        this.activity = activity;

        init();

        ///activity.findViewById(R.id.
    }



    /**
    public void init(){
        navview =(NavigationView)activity.findViewById(R.id.nav_view);
        navview.setNavigationItemSelectedListener(NavigationDrawerMenuManager.this);
        View v = navview.getHeaderView(0);
        Button b = (Button) v.findViewById(R.id.navigation_header_actionbutton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity.getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(activity, CheckIn.class);
                activity.startActivity(intent);
            }
        });

        //retrive locations from database
        CheckingDBAdapter checkingDBAdapter = new CheckingDBAdapter(activity);
        ArrayAdapter<String> arrayAdapter = checkingDBAdapter.getCheckPointsArrayAdapter();


        //add values for the spinner
        Spinner spinner = (Spinner) v.findViewById(R.id.navigation_header_place);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity.getApplicationContext(),android.R.layout.simple_spinner_item,places);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        //set Date and time
        //Calendar cal= Calendar.getInstance();
        TextView dayofMonth =((TextView) v.findViewById(R.id.navigation_header_date));
        dayofMonth.setText(DateManager.dayToday());
        TextView day =((TextView) v.findViewById(R.id.navigation_header_day));
        day.setText(DateManager.getDayOfWeek());

        //setTime
        //String time="";
        //time+=cal.get(Calendar.HOUR)+" : ";
        //time+=cal.get(Calendar.MINUTE) + " : ";
        //time+=cal.get(Calendar.AM_PM);
        String time = DateManager.getTimeFull();

        TextView t = (TextView)v.findViewById(R.id.navigation_header_time);
        t.setText(time);
    }*/

    private void init(){
        final CheckInOutManager man = new CheckInOutManager(activity.getApplicationContext(),this);//create an instance of the manager
        //to manage database operations

        drawer = (DrawerLayout)activity.findViewById(R.id.drawer_layout);

        //find the navigation view from the passed activities layout
        navview =(NavigationView)activity.findViewById(R.id.nav_view);
        navview.setNavigationItemSelectedListener(NavigationDrawerMenuManager.this);//assign this as the navigation item clicked listener
        View v = navview.getHeaderView(0);//get the header from the navigation drawer
        Button checkInBtn = (Button) v.findViewById(R.id.navigation_header_actionbutton);
        final EditText comment = (EditText)v.findViewById(R.id.navigation_header_comment);
        final Spinner loc = (Spinner) v.findViewById(R.id.navigation_header_place);
        loc.setAdapter(man.getLocationsArrayAdapter(man.isCheckedIn()));//get locations from the databse
        TextView date = (TextView) v.findViewById(R.id.navigation_header_date);
        date.setText(DateManager.dayToday());
        TextView day = (TextView) v.findViewById(R.id.navigation_header_day);
        day.setText(DateManager.getDayOfWeek());
        TextView time = (TextView)v.findViewById(R.id.navigation_header_time);
        time.setText(DateManager.getTimeFull());
        if(man.isCheckedIn()) {//cheack whether the user already checked in


            checkInBtn.setText("Check Out");
            //text has to be check out
            checkInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PermissionManager pm = new PermissionManager(activity);
                    Location lastKnownLocation = null;
                    if (pm.checkForLocationPermission()) {
                        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
                        List<String> providers = locationManager.getProviders(true);
                        for (String provider : providers) {
                            Location tempLoc = locationManager.getLastKnownLocation(provider);
                            if (tempLoc == null) continue;
                            if (lastKnownLocation == null || tempLoc.getAccuracy() > lastKnownLocation.getAccuracy()) {
                                lastKnownLocation = tempLoc;
                            }
                        }
                    }
                    lastKnownLocation = new Location(LocationManager.GPS_PROVIDER);
                    CheckSession session = new CheckSession(DateManager.dateToday(), DateManager.getTimeFull(), lastKnownLocation, loc.getSelectedItem().toString(), comment.getText().toString(), false);
                    man.checkOut(session);
                    //onCheckedIn();
                }
            });
        }else{
            checkInBtn.setText("Check In");
            checkInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PermissionManager pm = new PermissionManager(activity);
                    Location lastKnownLocation = null;
                    if (pm.checkForLocationPermission()) {
                        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
                        List<String> providers = locationManager.getProviders(true);
                        for (String provider : providers) {
                            Location tempLoc = locationManager.getLastKnownLocation(provider);
                            if (tempLoc == null) continue;
                            if (lastKnownLocation == null || tempLoc.getAccuracy() > lastKnownLocation.getAccuracy()) {
                                lastKnownLocation = tempLoc;
                            }
                        }
                    }
                    lastKnownLocation = new Location(LocationManager.GPS_PROVIDER);
                    CheckSession session = new CheckSession(DateManager.dateToday(), DateManager.getTimeFull(), lastKnownLocation, loc.getSelectedItem().toString(), comment.getText().toString(), false);
                    man.checkIn(session);
                    //onCheckedIn();
                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        switch (id){
            case R.id.Home:
                if(!(activity instanceof Home)){
                    Intent intent = new Intent(activity, Home.class);
                    activity.startActivity(intent);

                    //activity.overridePendingTransition(R.animator.fadein,R.animator.fadeout);
                    return true;
                }
                break;

            case R.id.add_customer:

                if( !(activity instanceof AddCustomer)){
                    Intent intent = new Intent(activity, AddCustomer.class);
                    activity.startActivity(intent);
                    return true;
                }
                break;

            case R.id.product_details:
                if(!(activity instanceof DisplayProductTableActivity)){
                    Intent intent = new Intent(activity,DisplayProductTableActivity.class);
                    //Intent intent = new Intent(activity, AndroidDatabaseManager.class);
                    activity.startActivity(intent);
                    return true;
                }
                break;

            case R.id.add_extra_customer:
                if(!(activity instanceof AddExtraCustomer)){
                    Intent intent = new Intent(activity,AddExtraCustomer.class);
                    //Intent intent = new Intent(activity, AndroidDatabaseManager.class);
                    activity.startActivity(intent);
                    return true;
                }
                break;

            case R.id.pending_Customer:
                if(!(activity instanceof PendingCustomer)){
                    Intent intent = new Intent(activity,PendingCustomer.class);
                    //Intent intent = new Intent(activity, AndroidDatabaseManager.class);
                    activity.startActivity(intent);
                    return true;
                }
                break;

            case R.id.stock_view:
                if(!(activity instanceof StockView)){
                    Intent intent = new Intent(activity,StockView.class);
                    //Intent intent = new Intent(activity, AndroidDatabaseManager.class);
                    activity.startActivity(intent);
                    return true;
                }
                break;

            case R.id.db_manage:
                if(!(activity instanceof AndroidDatabaseManager)){
                    //Intent intent = new Intent(activity,DisplayProductTableActivity.class);
                    Intent intent = new Intent(activity, AndroidDatabaseManager.class);
                    activity.startActivity(intent);
                    return true;
                }
                break;

            case R.id.sales_invoice:
                if(!(activity instanceof Invoice)){
                    //Intent intent = new Intent(activity,DisplayProductTableActivity.class);
                    Intent intent = new Intent(activity, Invoice.class);
                    activity.startActivity(intent);
                    return true;
                }
                break;
            case R.id.activity_manual_sync:
                if(!(activity instanceof SalesInvoice)){
                    //Intent intent = new Intent(activity,DisplayProductTableActivity.class);
                    Intent intent = new Intent(activity, ManualSync.class);
                    activity.startActivity(intent);
                    return true;
                }
                break;

            case R.id.nav_share:
                try {
                    //File file = activity.getBaseContext().getDatabasePath("sfa"); //"/data/data/com.marina.channelbridge/databases/channel_bridge_db.db";
                    File file = activity.getBaseContext().getDatabasePath("sfa.db");
//				    File dbFile = new File(inFileName);
                    FileInputStream fis;

                    fis = new FileInputStream(file);

                    String version = "0";

                    PackageInfo pInfo;
                    try {
                        pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
                        version = pInfo.versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Integer databaseVersion = 0;
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());

                    String outFileName = Environment.getExternalStorageDirectory() + "/sfa.db";

                    // Open the empty db as the output stream
                    OutputStream output = new FileOutputStream(outFileName);

                    // Transfer bytes from the inputfile to the outputfile
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }

                    // Close the streams
                    output.flush();
                    output.close();
                    fis.close();


                    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                    alertDialog.setTitle("Successful");
                    alertDialog
                            .setMessage("File Saved to the location : " + outFileName);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    alertDialog.show();

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;

        }



        return false;
    }


    @Override
    public void onCheckedIn() {
        init();
    }

    @Override
    public void onNotCheckedIn() {

    }

    @Override
    public void onException() {

    }

    @Override
    public void onCheckOut() {
        init();
    }
}
