package com.example.ahmed.sfa.activities.supportactivities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Home;
import com.example.ahmed.sfa.controllers.CheckInOutManager;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.PermissionManager;
import com.example.ahmed.sfa.models.CheckInCheckOutActions;
import com.example.ahmed.sfa.models.CheckSession;

import java.util.List;


/**
 * Created by Ahmed on 3/12/2017.
 */

public class CheckIn extends AppCompatActivity implements CheckInCheckOutActions {

    public void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        final CheckInOutManager man = new CheckInOutManager(this,this);
        if(man.isCheckedIn()){
            onCheckedIn();
        }else{
          init();
        }

    }
    private void init(){
        final CheckInOutManager man = new CheckInOutManager(this,this);
        Button checkInBtn = (Button) findViewById(R.id.navigation_header_actionbutton);
        Button skipBtn = (Button) findViewById(R.id.skipp_checkin);
        final Spinner loc = (Spinner) findViewById(R.id.navigation_header_place);
        final TextView comment = (TextView) findViewById(R.id.navigation_header_comment);
        //CheckingDBAdapter man = new CheckingDBAdapter(getApplicationContext());
        TextView date = (TextView) findViewById(R.id.navigation_header_date);
        date.setText(DateManager.dayToday());
        TextView day = (TextView) findViewById(R.id.navigation_header_day);
        day.setText(DateManager.getDayOfWeek());
        TextView time = (TextView)findViewById(R.id.navigation_header_time);
        String time_checkIn = DateManager.getTime();
        String[] time_arr = time_checkIn.split(":");
        Toast.makeText(this, ""+time_checkIn, Toast.LENGTH_SHORT).show();
        time.setText(time_arr[0]+":"+time_arr[1]);

        TextView month = (TextView)findViewById(R.id.month);
        month.setText(DateManager.getMonthName());
        loc.setAdapter(man.getLocationsArrayAdapter(man.isCheckedIn()));
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });
        skipBtn.setVisibility(View.VISIBLE);
        checkInBtn.setText("Check In");
        checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionManager pm = new PermissionManager(CheckIn.this);
                Location lastKnownLocation = null;
                if (pm.checkForLocationPermission()) {
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
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



    @Override
    public void onCheckedIn() {
        Intent intent=new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
    }

    @Override
    public void onNotCheckedIn() {

    }

    @Override
    public void onException() {

    }

    @Override
    public void onCheckOut() {

    }

}
