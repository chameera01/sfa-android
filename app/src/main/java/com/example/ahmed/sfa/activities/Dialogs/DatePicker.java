package com.example.ahmed.sfa.activities.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ahmed.sfa.controllers.DateManager;

import java.util.Calendar;

/**
 * Created by Ahmed on 3/26/2017.
 */

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    TextView v;
    public DatePicker(TextView v){
        super();
        this.v = v;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c= Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        String date = DateManager.getDate(year,month,dayOfMonth);
        v.setText(date);
    }
}
