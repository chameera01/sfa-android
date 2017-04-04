package com.example.ahmed.sfa.activities.supportactivities;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ahmed on 4/3/2017.
 */

public class DailyGraphFragment extends Fragment {
    GraphView gv;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        View view =layoutInflater.inflate(R.layout.graph,container,false);
        gv = (GraphView)view.findViewById(R.id.weeklytarget);

        Viewport viewport = gv.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(50000.0);


        /*
        BarGraphSeries<DataPoint> series =  new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(1,35000),
                new DataPoint(2,40000),
                new DataPoint(5,25000),
                new DataPoint(6,10000)
        });*/


        /** worked well on 4/4/2017 but changing into a loop

        DBAdapter dbAdapter = new DBAdapter(this.getActivity());
        DataPoint[] arra = new DataPoint[2];
        arra[0] = new DataPoint(1,dbAdapter.getSales("2017/1/1"));
        arra[1] = new DataPoint(2,dbAdapter.getTarger("2017/04/01"));
        */
        DataPoint[] arra = new DataPoint[10];
        DBAdapter dbAdapter = new DBAdapter(this.getActivity());

        String date = DateManager.dateToday();
        int j=1;
        for(int i=0;i<10;i++){
            arra[i] = new DataPoint(j,dbAdapter.getSales(date));
            arra[++i] = new DataPoint(++j,dbAdapter.getTarger(date));
            j+=3;
            date = DateManager.dayBefore(date);
        }


        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(arra);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint dataPoint) {
                if(dataPoint.getX()%2 ==1){
                    return Color.BLUE;
                }else{
                    return Color.GREEN;
                }

            }
        });
        series.setSpacing(1);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
        gv.addSeries(series);


        return view;
    }

    class DBAdapter extends BaseDBAdapter {

        protected DBAdapter(Context c) {
            super(c);
        }

        public double getTarger(String date){
            double result = 0.0;
            openDB();
            Cursor cursor = db.rawQuery("SELECT TargetValue FROM Tr_TargetData WHERE Date='"+date+"';",null);
            if(cursor.moveToNext())
                result = cursor.getDouble(0);
            closeDB();
            return result;

        }
        public double getSales(String date){
            double result = 0.0;
            openDB();
            Cursor cursor = db.rawQuery("SELECT SUM(InvoiceTotal) FROM Tr_SalesHeader WHERE InvoiceDate='"+date+"';",null);
            if(cursor.moveToNext())result = cursor.getDouble(0);
            closeDB();
            return result;
        }
    }


}
