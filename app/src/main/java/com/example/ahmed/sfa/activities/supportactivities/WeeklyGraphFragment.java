package com.example.ahmed.sfa.activities.supportactivities;

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
import com.example.ahmed.sfa.Constants;

import static com.example.ahmed.sfa.Constants.GRAPH_NUMBEROFMONTHS;

/**
 * Created by Ahmed on 3/1/2017.
 */

public class WeeklyGraphFragment extends Fragment{
    GraphView gv ;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        String[] XAxisLabels = new String[GRAPH_NUMBEROFMONTHS +2];
        View view =layoutInflater.inflate(R.layout.graph,container,false);
        gv = (GraphView)view.findViewById(R.id.chart);
        gv.setTitle("Monthly Sales Vs Target");
        XAxisLabels[0] = XAxisLabels[GRAPH_NUMBEROFMONTHS +1] = "";

        DataPoint[] arra = new DataPoint[GRAPH_NUMBEROFMONTHS];
        DataPoint[] arra2 = new DataPoint[GRAPH_NUMBEROFMONTHS];
        DBAdapter dbAdapter = new DBAdapter(this.getActivity());

        String date = DateManager.dateToday();

        for(int i = 0; i< GRAPH_NUMBEROFMONTHS; i++){
            Log.w("w qryDate",date);
            arra[i]=new DataPoint(i+1,dbAdapter.getSales(date));
            arra2[i]=new DataPoint(i+1,dbAdapter.getTarget(date));
            XAxisLabels[i+1]=DateManager.getMonthName(date);
            date = DateManager.monthbefore(date);

        }



        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(arra);
        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(arra2);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        series.setColor(Color.parseColor(Constants.GRAPH_COLOR_ONE));
        series.setSpacing(20);
        series.setTitle("Sales");
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPointInterface) {

                Toast.makeText(WeeklyGraphFragment.this.getActivity(),Math.round(dataPointInterface.getY())+"",Toast.LENGTH_SHORT).show();;
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
                Toast.makeText(WeeklyGraphFragment.this.getActivity(),Math.round(dataPointInterface.getY())+"",Toast.LENGTH_SHORT).show();;
            }
        });
        gv.addSeries(series2);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(gv);
        staticLabelsFormatter.setHorizontalLabels(XAxisLabels);

        gv.getViewport().setMinX(0);
        gv.getViewport().setMaxX(GRAPH_NUMBEROFMONTHS +1);
        gv.getViewport().setXAxisBoundsManual(true);
        gv.getGridLabelRenderer().setNumHorizontalLabels(GRAPH_NUMBEROFMONTHS);
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
    /** dummy value
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,Bundle savedInstanceState){
        //inflating the layout for the fragement

        View view =layoutInflater.inflate(R.layout.graph,container,false);


        gv = (GraphView)view.findViewById(R.id.chart);

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

    class DBAdapter extends BaseDBAdapter {

        protected DBAdapter(Context c) {
            super(c);
        }

        public double getTarget(String date){
            double result = 0.0;
            openDB();
            int[] dateBroken  = DateManager.breakApart(date);
            String month = dateBroken[0]<10?"0"+dateBroken[0]:dateBroken[0]+"";
            String year = dateBroken[2]+"";
            Cursor cursor = db.rawQuery("SELECT sum(TargetValue) FROM Tr_TargetData WHERE Date Like '"+month+"%' AND Date Like '%"+year+"';",null);
            if(cursor.moveToNext())
                result = cursor.getDouble(0);
            closeDB();
            Log.w("tar",result+"");
            return result;
            //date like 'xx%' and date like '%xxxx'
        }
        public float getSales(String date){
            float result = 0.0f;
            openDB();
            //Log.w("Date",date);
            int[] dateBroken  = DateManager.breakApart(date);
            String month = dateBroken[0]<10?"0"+dateBroken[0]:dateBroken[0]+"";
            String year = dateBroken[2]+"";
            Cursor cursor = db.rawQuery("SELECT SUM(InvoiceTotal) FROM Tr_SalesHeader WHERE InvoiceDate Like '"+month+"%' AND InvoiceDate Like '%"+year+"';",null);
            if(cursor.moveToNext())result = cursor.getFloat(0);
            closeDB();
            Log.w("sales",result+"");
            return result;
        }
    }
}
