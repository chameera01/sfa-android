package com.example.ahmed.sfa.controllers.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.models.AddExtraCusListViewAdp;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DELL on 9/5/2017.
 */

public class Stockview_listview_adp extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    public  static int rowCount = 0;
    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    public static final String THIRD_COLUMN="Third";
    public static final String FOURTH_COLUMN="Fourth";
    public static final String FIFTH_COLUMN="Fifth";
    public static final String  SIXTH_COLUMN="six";
    public static final String SEVENTH_COLUMN="seven";
    public static final String EIGHT_COLUMN="eight";

    public Stockview_listview_adp(Activity activity, ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder{
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
        TextView txtFourth;
        TextView txtFifth;
        TextView txtSixth;
        TextView txtSeventh;
        TextView txtEighth;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        rowCount++;
        ViewHolder holder;

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.stockview_lv_row, null);
            holder=new ViewHolder();

            holder.txtFirst=(TextView) convertView.findViewById(R.id.stock_view$itemcode);
            holder.txtSecond=(TextView) convertView.findViewById(R.id.stock_view$itemcodeprinciple);
            holder.txtThird=(TextView) convertView.findViewById(R.id.stock_view$batch_number);
            holder.txtFourth=(TextView) convertView.findViewById(R.id.stock_view$expdate);
            holder.txtFifth=(TextView) convertView.findViewById(R.id.stock_view$sellp);
            holder.txtSixth=(TextView) convertView.findViewById(R.id.stock_view$retailp);
            holder.txtSeventh=(TextView) convertView.findViewById(R.id.stock_view$qnt);
            //holder.txtEighth=(TextView) convertView.findViewById(R.id.stock_view$last_update);

            convertView.setTag(holder);
        }else{

            holder=(ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map=list.get(position);

            holder.txtFirst.setText(map.get(FIRST_COLUMN));
            holder.txtSecond.setText(map.get(SECOND_COLUMN));
            holder.txtThird.setText(map.get(THIRD_COLUMN));
            holder.txtFourth.setText(map.get(FOURTH_COLUMN));
            holder.txtFifth.setText(map.get(FIFTH_COLUMN));
            holder.txtSixth.setText(map.get(SIXTH_COLUMN));
            holder.txtSeventh.setText(map.get(SEVENTH_COLUMN));
            //holder.txtEighth.setText(map.get(EIGHT_COLUMN));


        if (rowCount%2==0) {
            holder.txtFirst.setBackgroundColor(Color.LTGRAY);
            holder.txtSecond.setBackgroundColor(Color.LTGRAY);
            holder.txtThird.setBackgroundColor(Color.LTGRAY);
            holder.txtFourth.setBackgroundColor(Color.LTGRAY);
            holder.txtFifth.setBackgroundColor(Color.LTGRAY);
            holder.txtSixth.setBackgroundColor(Color.LTGRAY);
            holder.txtSeventh.setBackgroundColor(Color.LTGRAY);
           // holder.txtEighth.setBackgroundColor(Color.LTGRAY);
        }else{
            holder.txtFirst.setBackgroundColor(Color.WHITE);
            holder.txtSecond.setBackgroundColor(Color.WHITE);
            holder.txtThird.setBackgroundColor(Color.WHITE);
            holder.txtFourth.setBackgroundColor(Color.WHITE);
            holder.txtFifth.setBackgroundColor(Color.WHITE);
            holder.txtSixth.setBackgroundColor(Color.WHITE);
            holder.txtSeventh.setBackgroundColor(Color.WHITE);
           // holder.txtEighth.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }

}
