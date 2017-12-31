package com.example.ahmed.sfa.controllers.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ahmed.sfa.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DELL on 9/5/2017.
 */

public class Customer_pennding_listview_adptr extends BaseAdapter {
    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    public  static int rowCount = 0;
    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    public static final String THIRD_COLUMN="Third";
    public static final String FOURTH_COLUMN="Fourth";
    public static final String FIFTH_COLUMN="Fifth";

public Customer_pennding_listview_adptr(Activity activity, ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder{
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
        TextView txtFourth;
        TextView txtFifth;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        rowCount++;
        ViewHolder holder;

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.customer_pending_listview_row, null);
            holder=new ViewHolder();

            holder.txtFirst = (TextView) convertView.findViewById(R.id.customer_pending_lv_name);
            holder.txtSecond = (TextView) convertView.findViewById(R.id.customer_pending_lv_address);
            holder.txtThird = (TextView) convertView.findViewById(R.id.customer_pending_lv_contact);
            holder.txtFourth = (TextView) convertView.findViewById(R.id.customer_pending_lv_uploadstatus);
            holder.txtFifth = (TextView) convertView.findViewById(R.id.customer_pending_lv_approvalstatus);

            convertView.setTag(holder);
        }else{

            holder=(ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map=list.get(i);
        holder.txtFirst.setText(map.get(FIRST_COLUMN));
        holder.txtSecond.setText(map.get(SECOND_COLUMN));
        holder.txtThird.setText(map.get(THIRD_COLUMN));
        holder.txtFourth.setText(map.get(FOURTH_COLUMN));
        holder.txtFifth.setText(map.get(FIFTH_COLUMN));

        if (rowCount%2==0) {
            holder.txtFirst.setBackgroundColor(Color.LTGRAY);
            holder.txtSecond.setBackgroundColor(Color.LTGRAY);
            holder.txtThird.setBackgroundColor(Color.LTGRAY);
            holder.txtFourth.setBackgroundColor(Color.LTGRAY);
            holder.txtFifth.setBackgroundColor(Color.LTGRAY);
        }else{
            holder.txtFirst.setBackgroundColor(Color.WHITE);
            holder.txtSecond.setBackgroundColor(Color.WHITE);
            holder.txtThird.setBackgroundColor(Color.WHITE);
            holder.txtFourth.setBackgroundColor(Color.WHITE);
            holder.txtFifth.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }

}

