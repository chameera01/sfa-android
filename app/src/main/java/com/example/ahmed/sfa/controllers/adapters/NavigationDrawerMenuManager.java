package com.example.ahmed.sfa.controllers.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.AddCustomer;
import com.example.ahmed.sfa.activities.CheckIn;
import com.example.ahmed.sfa.activities.DisplayProductTableActivity;
import com.example.ahmed.sfa.activities.Home;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Ahmed on 3/10/2017.
 */

public class NavigationDrawerMenuManager implements NavigationView.OnNavigationItemSelectedListener {
    private Activity activity;
    NavigationView navview;
    //String[] places ;
    private static String[] days= {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

    public NavigationDrawerMenuManager(final Activity activity){

        this.activity = activity;
        init();

        ///activity.findViewById(R.id.
    }

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
        Calendar cal= Calendar.getInstance();
        TextView dayofMonth =((TextView) v.findViewById(R.id.navigation_header_date));
        dayofMonth.setText(cal.get(Calendar.DAY_OF_MONTH)+"");
        TextView day =((TextView) v.findViewById(R.id.navigation_header_day));
        day.setText(days[cal.get(Calendar.DAY_OF_WEEK)-1]);

        //setTime
        String time="";
        time+=cal.get(Calendar.HOUR)+" : ";
        time+=cal.get(Calendar.MINUTE) + " : ";
        time+=cal.get(Calendar.AM_PM);

        TextView t = (TextView)v.findViewById(R.id.navigation_header_time);
        t.setText(time);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
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
                    activity.startActivity(intent);
                    return true;
                }
                break;


        }



        return false;
    }


    class DBAdapter{
        DBAdapter(){

        }
    }

}
