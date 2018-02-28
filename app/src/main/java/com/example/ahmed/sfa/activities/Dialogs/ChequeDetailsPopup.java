package com.example.ahmed.sfa.activities.Dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.models.Cheque;

import java.util.Calendar;

public class ChequeDetailsPopup extends AppCompatActivity {

    private EditText chequeET, chequeNoET, bankET, branchET;
    private TextView realizeDateET;
    private Button cancel, done, selectdate;
    private String chequeNo, bank, branch, rdate;
    private double chequeVal, chequeTotal;
    private DatePickerDialog.OnDateSetListener date;
    private Calendar myCalendar;
    private int yearY, monthY, dayY;
    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
            yearY = year;
            monthY = month + 1;
            dayY = dayOfMonth;

            realizeDateET.setText(String.valueOf(monthY) + "/" + String.valueOf(dayY) + "/" + String.valueOf(yearY));
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cheque_details_popup);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = 500;
        params.width = 800;
        this.getWindow().setAttributes(params);
        setFinishOnTouchOutside(false);

        chequeET = (EditText) findViewById(R.id.etChequeAmount);
        chequeNoET = (EditText) findViewById(R.id.etChequeNumber);
        bankET = (EditText) findViewById(R.id.etBank);
        branchET = (EditText) findViewById(R.id.etBranch);
        realizeDateET = (TextView) findViewById(R.id.etRealizeDate);

        cancel = (Button) findViewById(R.id.btnCancel);
        done = (Button) findViewById(R.id.btnDone);
        selectdate = (Button) findViewById(R.id.btnDate);

        //DatePicker
        setCurrentDates();

        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v.getId());
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmpty()) {
                    chequeVal = Double.parseDouble(chequeET.getText().toString());
                    chequeNo = chequeNoET.getText().toString();
                    bank = bankET.getText().toString();
                    branch = branchET.getText().toString();
                    rdate = realizeDateET.getText().toString();

                    final Cheque cd = new Cheque();
                    cd.setChequeVal(chequeVal);
                    cd.setChequeNum(chequeNo);
                    cd.setBank(bank);
                    cd.setBranch(branch);
                    cd.setRealizedDate(rdate);

                    Log.d("COL", "" + chequeVal + " " + chequeNo + " " + bank + " " + branch + " " + rdate);

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChequeDetailsPopup.this);
                    alertDialog.setTitle("Confirm");
                    alertDialog.setMessage("Are you sure you want to add?");

                    alertDialog.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent();
                                    intent.putExtra("CHEQUE_DETAILS", cd);
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                }
                            });

                    alertDialog.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();
                } else {

                }
            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == R.id.btnDate) {
            return new DatePickerDialog(this, dateListener, yearY, monthY, dayY);
        }
        return null;
    }

    private void setCurrentDates() {

        final Calendar cal = Calendar.getInstance();

        yearY = cal.get(Calendar.YEAR);
        monthY = cal.get(Calendar.MONTH);
        dayY = cal.get(Calendar.DAY_OF_MONTH);

    }


    private Boolean checkEmpty() {

        if (TextUtils.isEmpty(chequeET.getText()) || TextUtils.isEmpty(chequeNoET.getText()) || TextUtils.isEmpty(bankET.getText()) || TextUtils.isEmpty(branchET.getText()) || TextUtils.isEmpty(realizeDateET.getText())) {
            return false;
        }
        return true;

    }


}
