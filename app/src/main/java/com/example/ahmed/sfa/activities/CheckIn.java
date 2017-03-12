package com.example.ahmed.sfa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.CheckingDBAdapter;


/**
 * Created by Ahmed on 3/12/2017.
 */

public class CheckIn extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_layout);

        Button checkInBtn = (Button)findViewById(R.id.navigation_header_actionbutton);
        Button skipBtn = (Button)findViewById(R.id.skipp_checkin);
        Spinner loc  = (Spinner)findViewById(R.id.navigation_header_place);
        CheckingDBAdapter man = new CheckingDBAdapter(getApplicationContext());
        loc.setAdapter(man.getCheckPointsArrayAdapter());
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
            }
        });
        skipBtn.setVisibility(View.VISIBLE);

        checkInBtn.setText("Check In");
        checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
            }
        });
    }
}
