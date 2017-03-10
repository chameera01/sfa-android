package com.example.ahmed.sfa.controllers.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.AddCustomer;
import com.example.ahmed.sfa.activities.Home;

/**
 * Created by Ahmed on 3/10/2017.
 */

public class NavigationDrawerMenuManager implements NavigationView.OnNavigationItemSelectedListener {
    private Activity activity;

    public NavigationDrawerMenuManager(Activity activity){
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id== R.id.Home && !(activity instanceof Home)){
            Intent intent = new Intent(activity, Home.class);
            activity.startActivity(intent);
            //activity.overridePendingTransition(R.animator.fadein,R.animator.fadeout);
            return true;
        }else if(id== R.id.add_customer && !(activity instanceof AddCustomer)){
            Intent intent = new Intent(activity, AddCustomer.class);
            activity.startActivity(intent);
            return true;
        }
        return false;
    }



}
