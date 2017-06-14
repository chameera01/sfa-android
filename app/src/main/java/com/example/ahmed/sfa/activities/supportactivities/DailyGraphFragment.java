package com.example.ahmed.sfa.Activities.supportactivities;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import static com.example.ahmed.sfa.Constants.GRAPH_NUMBEROFDAYS;

/**
 * Created by Ahmed on 4/3/2017.
 */

public class DailyGraphFragment extends Fragment {
    GraphView gv;




    /**
    public View onCreateView(LayoutInflater layoutInflater,ViewGroup container,Bundle savedInstanceState){
        View view = layoutInflater.inflate(R.layout.graph,container,false);
        BarChart chart = (BarChart)view.findViewById(R.id.chart);

        ArrayList<String> dates = getDates(DateManager.dateToday());
        ArrayList<BarDataSet> barDataSets = getDataSet(dates);


        BarData data = new BarData(barDataSets.get(0),barDataSets.get(1));
        chart.setData(data);
        return view;
    }*/



    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        String[] dates = new String[GRAPH_NUMBEROFDAYS +2];
        View view =layoutInflater.inflate(R.layout.graph,container,false);
        gv = (GraphView)view.findViewById(R.id.chart);
        gv.setTitle("Daily Sales Vs Target");
        dates[0] = dates[GRAPH_NUMBEROFDAYS +1] = "";
        /**
        gv.getGridLabelRenderer().setHumanRounding(false);
        gv.getLegendRenderer().setVisible(true);

        gv.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        gv.getGridLabelRenderer().setNumHorizontalLabels(3);

        Viewport viewport = gv.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(50000.0);



        viewport.setScalable(true);
        viewport.setScalableY(true);


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
        arra[1] = new DataPoint(2,dbAdapter.getTarget("2017/04/01"));
        */
        DataPoint[] arra = new DataPoint[GRAPH_NUMBEROFDAYS];
        DataPoint[] arra2 = new DataPoint[GRAPH_NUMBEROFDAYS];
        DBAdapter dbAdapter = new DBAdapter(this.getActivity());

        String date = DateManager.dateToday();

        /**
        Log.w("Date today",date);
        int j=1;
        for(int i=0;i<10;i++){
            arra[i] = new DataPoint(j,dbAdapter.getSales(date));
            arra[++i] = new DataPoint(++j,dbAdapter.getTarget(date));
            j+=3;
            date = DateManager.dayBefore(date);
            Log.w("changed Date",date);
        }*/
        //int j=1;
        for(int i = 0; i< GRAPH_NUMBEROFDAYS; i++){
            arra[i]=new DataPoint(i+1,dbAdapter.getSales(date));
            arra2[i]=new DataPoint(i+1,dbAdapter.getTarget(date));
            dates[i+1]=date;
            date = DateManager.dayBefore(date);
            Log.w("qryDate",date);
        }



        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(arra);
        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(arra2);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        /*series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint dataPoint) {
                return Color.BLUE;

            }
        });*/
        series.setColor(Color.parseColor(Constants.GRAPH_COLOR_ONE));
        series.setSpacing(20);
        series.setTitle("Sales");
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPointInterface) {

                Toast.makeText(DailyGraphFragment.this.getActivity(),Math.round(dataPointInterface.getY())+"",Toast.LENGTH_SHORT).show();;
            }
        });
        gv.addSeries(series);


        series2.setColor(Color.parseColor(Constants.GRAPH_COLOR_TWO));
        series2.setSpacing(20);
        series2.setTitle("Target");
        series2.setDrawValuesOnTop(true);
        series2.setValuesOnTopColor(Color.BLACK);
        series2.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPointInterface) {
                Toast.makeText(DailyGraphFragment.this.getActivity(),Math.round(dataPointInterface.getY())+"",Toast.LENGTH_SHORT).show();;
            }
        });
        gv.addSeries(series2);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(gv);
        staticLabelsFormatter.setHorizontalLabels(dates);

        gv.getViewport().setMinX(0);
        gv.getViewport().setMaxX(GRAPH_NUMBEROFDAYS +1);
        gv.getViewport().setXAxisBoundsManual(true);
        gv.getGridLabelRenderer().setNumHorizontalLabels(GRAPH_NUMBEROFDAYS);
        gv.getGridLabelRenderer().setTextSize(14f);
        gv.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        gv.getGridLabelRenderer().setHumanRounding(true);
        gv.getViewport().setScalable(true);
        gv.getViewport().setScrollable(true);

        gv.getLegendRenderer().setVisible(true);
        gv.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        gv.getGridLabelRenderer().setVerticalAxisTitle("Sales (Rs)");

        return view;
    }

    class DBAdapter extends BaseDBAdapter {

        protected DBAdapter(Context c) {
            super(c);
        }

        public double getTarget(String date){
            double result = 0.0;
            openDB();
            //Log.w("date tar",date);
            Cursor cursor = db.rawQuery("SELECT TargetValue FROM Tr_TargetData WHERE Date='"+date+"';",null);
            if(cursor.moveToNext())
                result = cursor.getDouble(0);
            closeDB();
            //Log.w("tar",result+"");
            return result;

        }
        public float getSales(String date){
            float result = 0.0f;
            openDB();
            //Log.w("Date",date);
            Cursor cursor = db.rawQuery("SELECT SUM(InvoiceTotal) FROM Tr_SalesHeader WHERE InvoiceDate='"+date+"';",null);
            if(cursor.moveToNext())result = cursor.getFloat(0);
            closeDB();
            //Log.w("sales",result+"");
            return result;
        }
    }


}
