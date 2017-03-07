package com.example.ahmed.sfa.controllers.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.Activities.CustomerPopupFragment;
import com.example.ahmed.sfa.Activities.Home;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.models.Itinerary;

import java.util.ArrayList;

/**
 * Created by Ahmed on 3/3/2017.
 */

//to bind and create new view for the filtered list we use this class
public class ItineraryAdapter extends BaseAdapter {
    Context c;
    ArrayList<Itinerary> itineraries;
    LayoutInflater inflater;
    //Home home;
    public ItineraryAdapter(Context c, ArrayList<Itinerary> itineraries){//,Home home){
        this.c=c;
        this.itineraries = itineraries;
        //this.home = home;
    }
    @Override
    public long getItemId(int a){
        return a;
    }

    @Override
    public int getCount(){
        return itineraries.size();
    }

    public Object getItem(int position){
        return itineraries.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Itinerary itinerary = itineraries.get(position);
        if(inflater==null){
            //inflater = (LayoutInflater)getLayoutInflater();//this line had to be changed for taking this class as a
            //seperate class as follows
            inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        /** because of three color changes we allways have to inflate so no use of these block
        if(convertView==null){
            if(itinerary.getIsInvoiced())
                convertView = inflater.inflate(R.layout.homesearchresultrowdone,parent,false);
            else
                convertView = inflater.inflate(R.layout.homesearchresultrow,parent,false);

        }*/

        if(itinerary.getIsInvoiced())
            convertView = inflater.inflate(R.layout.homesearchresultrowdone,parent,false);
        else
            convertView = inflater.inflate(R.layout.homesearchresultrow,parent,false);

        int selected = ((Home)c).getSelectedIndex();
        String sl =selected+"";
        if(selected==position){
            //Log.w("position in Home",sl);
            convertView = inflater.inflate(R.layout.homesearchresultrowselected,parent,false);
        }
        //Log.w("position Home",sl);

        TextView customer = (TextView)convertView.findViewById(R.id.customer);
        TextView town = (TextView)convertView.findViewById(R.id.town);

        customer.setText(itinerary.getCustomer());
        town.setText(itinerary.getTown());



        final View finalConvertView = convertView;
        /**convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c.getApplicationContext(),"Item Clicked",Toast.LENGTH_SHORT).show();


            }
        });*/
        return convertView;
    }


}