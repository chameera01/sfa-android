package com.example.ahmed.sfa.Activities.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.adapters.PrincipleDiscountAdapter;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.PrincipleDiscountModel;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 9/30/2017.
 */

public class PricipleDiscountDialog extends DialogFragment {
    Context activityLaunched;

    PrincipleDiscountAdapter adapter;
    DBAdapter dbAdapter;

    List<PrincipleDiscountModel> models;
    TextView tvTotalDiscount;
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        models =dbAdapter.getModels();

        adapter = new PrincipleDiscountAdapter(models);



        LayoutInflater inflater = ((Activity)activityLaunched).getLayoutInflater();
        View v = inflater.inflate(R.layout.principle_discount_dialog,null);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv_data);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activityLaunched);
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        builder.setView(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
            }
        });
        builder.setPositiveButton("Add ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doPositiveClick(models);
            }
        });

        tvTotalDiscount = (TextView)v.findViewById(R.id.tv_total);

        Dialog dialog = builder.create();
        adapter.notifyDataSetChanged();
        return dialog;

    }

    private void setTotalPrincipleDiscount(){
        double totalPrincipleDiscount =0.0;
        for(PrincipleDiscountModel model:models){
            totalPrincipleDiscount+=model.getDisountValue();
        }
        tvTotalDiscount.setText(totalPrincipleDiscount+"");
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        activityLaunched = activity;
        dbAdapter = new DBAdapter(((Activity)activityLaunched).getApplicationContext());
    }

    public void doPositiveClick(List<PrincipleDiscountModel> models){
        this.models = models;
        dbAdapter.updateDB();
        setTotalPrincipleDiscount();
    }

    public void notifyDataSetChanged(){
        //adapter.notifyDataSetChanged();
    }

    class DBAdapter extends BaseDBAdapter{

        protected DBAdapter(Context c) {
            super(c);
        }

        public List<PrincipleDiscountModel> getModels(){
            List<PrincipleDiscountModel> models = new ArrayList<>();
            //get datafrom tempInvoice
            openDB();

            String sql = "SELECT PrincipleID,SUM(LineVal),SUM(Disc) FROM temp_invoice GROUP BY PrincipleID";
            Cursor cursor = db.rawQuery(sql,null);

            String selectionList = "(";//make the string for retrieving discount rate and principle
            while (cursor.moveToNext()){
                double disc = Double.parseDouble(cursor.getString(2));
                double tot = Double.parseDouble(cursor.getString(1));
                if(disc==0 && tot>0) {
                    PrincipleDiscountModel model = new PrincipleDiscountModel();
                    model.setPrincipleID(cursor.getString(0));
                    model.setAmount(tot);
                    models.add(model);
                    selectionList += cursor.getString(0) + ",";
                }
            }

            selectionList = selectionList.substring(0,selectionList.length()-1)+")";
            sql = "SELECT PrincipleID,Principle,DiscountRate FROM temp_discount_rate " +
                    "WHERE PrincipleID IN "+selectionList;

            cursor = db.rawQuery(sql,null);

            while (cursor.moveToNext()){
                for(PrincipleDiscountModel model :models){
                    if(model.getPrincipleID().equals(cursor.getString(0))){
                        model.setPrinciple(cursor.getString(1));
                        model.setDiscount(Double.parseDouble(cursor.getString(2)));
                        break;
                    }
                }
            }

            closeDB();

            return models;
        }

        public void updateDB(){
            openDB();

            for(PrincipleDiscountModel model : models){
                String sql = "UPDATE temp_discount_rate SET  DiscountRate="+model.getDiscount()+
                        " WHERE PrincipleID="+model.getPrincipleID();
                db.execSQL(sql);
            }

            closeDB();
        }
    }
}
