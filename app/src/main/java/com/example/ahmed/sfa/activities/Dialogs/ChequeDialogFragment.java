package com.example.ahmed.sfa.activities.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.Cheque;

import java.util.ArrayList;

/**
 * Created by Ahmed on 3/26/2017.
 */

public class ChequeDialogFragment extends DialogFragment {
    Cheque chq;
    NoticeDialogListener mListener;
    DBAdapter dbAdapter;

    EditText chqVal;
    EditText chqNum;
    Spinner banks;
    TextView colDate;
    TextView realDate;


    public interface NoticeDialogListener{
        public void onDialogPositiveClick(Cheque chq);
        public void onDialogNegativeClick();
    }



    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (NoticeDialogListener)activity;
            dbAdapter = new DBAdapter(activity);
        }catch (ClassCastException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        chq = new Cheque();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_cheque_details,null);

        chqVal = (EditText)v.findViewById(R.id.chq_dlg_val);
        chqNum = (EditText)v.findViewById(R.id.chq_dlg_num);

                colDate = (TextView) v.findViewById(R.id.chq_dlg_coldate);
        colDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePicker(colDate).show(getFragmentManager(),"ColDatePicker");
            }
        });

        realDate = (TextView)v.findViewById(R.id.chq_dlg_realDate);
        realDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePicker(realDate).show(getFragmentManager(),"RealDatePicker");
            }
        });

        banks = (Spinner)v.findViewById(R.id.chq_dlg_banks);
        banks.setAdapter(dbAdapter.getAllBanks());

        builder.setView(v)

                .setPositiveButton("Add Cheque", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initChq();
                        mListener.onDialogPositiveClick(chq);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogNegativeClick();
                    }
                });

        return builder.create();
    }

    private void initChq(){
        String val = chqVal.getText().toString();
        if(val!=null && !val.equals("")){
            chq.setChequeVal(Integer.parseInt(val));
        }

        val = chqNum.getText().toString();
        if(val!=null && !val.equals("")){
            chq.setChequeNum(val);
        }

        chq.setBank(banks.getSelectedItem().toString());

        val = colDate.getText().toString();
        if(val!=null && !val.equals("") && !val.equals("Click to Select")){
            chq.setCollectionDate(val);
        }

        val = realDate.getText().toString();
        if(val!=null && !val.equals("") && !val.equals("Click to Select")){
            chq.setRealizedDate(val);
        }
    }

    class DBAdapter extends BaseDBAdapter{
        protected DBAdapter(Context c) {
            super(c);
        }

        public ArrayAdapter<String> getAllBanks(){
            ArrayList<String> banks = new ArrayList<>();
            openDB();
            Cursor cursor = db.rawQuery("SELECT BankName FROM Mst_Banks WHERE IsActive = 0",null);
            while (cursor.moveToNext()){
                banks.add(cursor.getString(0));
            }
            closeDB();
            ArrayAdapter<String> banksSpinnerAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,banks);
            banksSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            return banksSpinnerAdapter;
        }
    }
}
