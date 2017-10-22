package com.example.ahmed.sfa.Activities.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.adapters.PrincipleDiscountAdapter;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.PrincipleDiscountModel;
import com.example.ahmed.sfa.models.SalesPayment;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.example.ahmed.sfa.Constants.PRINCIPLE_DISCOUNT_REQUEST_RESULT;

/**
 * Created by Ahmed on 9/30/2017.
 */

public class PricipleDiscountDialog extends Activity {
    Context activityLaunched;

    PrincipleDiscountAdapter adapter;
    DBAdapter dbAdapter;

    ArrayList<PrincipleDiscountModel> models;
    TextView tvTotalDiscount;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.principle_discount_dialog);
        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dbAdapter = new DBAdapter(getApplicationContext());
        models =dbAdapter.getModels();

        tvTotalDiscount = (TextView)/*v.*/findViewById(R.id.tv_total);

        adapter = new PrincipleDiscountAdapter(models,tvTotalDiscount);
        setFinishOnTouchOutside(false);


        //LayoutInflater inflater = getLayoutInflater();
        //View v = inflater.inflate(R.layout.principle_discount_dialog,null);
        //showSoftKeyboard(v);
        RecyclerView rv = (RecyclerView) /*v.*/findViewById(R.id.rv_data);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        //rv.getLayoutManager().generateDefaultLayoutParams();
        //builder.setView(v);
        /*v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
            }
        });*/

        Button btnOK = (Button)findViewById(R.id.btn_ok);
        Button btnCancel = (Button)findViewById(R.id.btn_Cancel);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPositiveClick(models);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*builder.setPositiveButton("Add ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doPositiveClick(models);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });*/


        //Dialog dialog = builder.create();
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        adapter.notifyDataSetChanged();
        getWindow().setLayout(600,500);
        //return dialog;

    }




   /* @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        activityLaunched = activity;
        dbAdapter = new DBAdapter(((Activity)activityLaunched).getApplicationContext());
    }*/

    private void onCancelClick(){

    }

    public void doPositiveClick(ArrayList<PrincipleDiscountModel> models){
        this.models = models;
        dbAdapter.updateDB();
        Intent intent = new Intent();

        intent.putParcelableArrayListExtra("MODELS",models);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void notifyDataSetChanged(){
        //adapter.notifyDataSetChanged();
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    activityLaunched.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    class DBAdapter extends BaseDBAdapter{

        protected DBAdapter(Context c) {
            super(c);
        }

        public ArrayList<PrincipleDiscountModel> getModels(){
            ArrayList<PrincipleDiscountModel> models = new ArrayList<>();
            //get datafrom tempInvoice
            openDB();

            String sql = "SELECT PrincipleID,SUM(LineVal),SUM(Disc),SUM(RetailPriceLineVal) FROM temp_invoice GROUP BY PrincipleID";
            Cursor cursor = db.rawQuery(sql,null);

            String selectionList = "(";//make the string for retrieving discount rate and principle
            while (cursor.moveToNext()){
                double disc = Double.parseDouble(cursor.getString(2));
                double tot = Double.parseDouble(cursor.getString(1));

                if(disc==0 && tot>0) {
                    PrincipleDiscountModel model = new PrincipleDiscountModel();
                    model.setPrincipleID(cursor.getString(0));
                    model.setAmount(cursor.getDouble(3));
                    models.add(model);
                    selectionList += cursor.getString(0) + ",";
                }
            }


            sql = "SELECT PrincipleID,Principle,DiscountRate FROM temp_discount_rate ";
            if (models.size() >0){
                selectionList = selectionList.substring(0,selectionList.length()-1)+")";
                sql+="WHERE PrincipleID IN "+selectionList;

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
            }



            closeDB();

            return models;
        }

        public void updateDB(){
            openDB();

            for(PrincipleDiscountModel model : models){

                String sql = "UPDATE temp_discount_rate SET  DiscountRate="+model.getDiscount()+
                        " , DiscountValue="+model.getDisountValue()+" , LineVal="+model.getAmount()+
                        " WHERE PrincipleID="+model.getPrincipleID();
                db.execSQL(sql);
            }

            closeDB();
        }
    }
}
