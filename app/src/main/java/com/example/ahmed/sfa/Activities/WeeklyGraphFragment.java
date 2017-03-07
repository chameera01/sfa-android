package com.example.ahmed.sfa.activities;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.sfa.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by Ahmed on 3/1/2017.
 */

public class WeeklyGraphFragment extends Fragment{
    GraphView gv ;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,Bundle savedInstanceState){
        //inflating the layout for the fragement

        View view =layoutInflater.inflate(R.layout.graph,container,false);


        gv = (GraphView)view.findViewById(R.id.weeklytarget);

        gv.getViewport().setYAxisBoundsManual(true);
        gv.getViewport().setMaxY(20);
        gv.getViewport().setMinY(0);
        gv.getViewport().setXAxisBoundsManual(true);
        gv.getViewport().setMaxX(10);
        gv.getViewport().setMinX(0);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(1,2),
                new DataPoint(2,5),
                new DataPoint(5,5),
                new DataPoint(6,3)

        });

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint dataPoint) {
                if(dataPoint.getX()%2==1){
                    return Color.BLUE;
                }else{
                    return Color.GREEN;
                }
            }
        });
        series.setSpacing(1);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        gv.addSeries(series);


        return view;
    }


    /**
    public void onCreate(Bundle savedBundleState){
        super.onCreate(savedBundleState);
        setContentView(R.layout.graph);
        gv = (GraphView)findViewById(R.id.weeklytarget);

        gv.getViewport().setYAxisBoundsManual(true);
        gv.getViewport().setMaxY(20);
        gv.getViewport().setMinY(0);
        gv.getViewport().setXAxisBoundsManual(true);
        gv.getViewport().setMaxX(10);
        gv.getViewport().setMinX(0);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0,2),
                new DataPoint(1,5),
                new DataPoint(4,5),
                new DataPoint(5,3)

        });

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint dataPoint) {
                if(dataPoint.getX()%2==1){
                    return Color.BLUE;
                }else{
                    return Color.GREEN;
                }
            }
        });
        series.setSpacing(1);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        gv.addSeries(series);



    }*/
}
