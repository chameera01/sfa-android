package com.example.ahmed.sfa.activities;

import android.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Dialogs.ChequeDialogFragment;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.Cheque;
import com.example.ahmed.sfa.models.SalesInvoiceModel;
import com.example.ahmed.sfa.models.SalesInvoiceSummary;
import com.example.ahmed.sfa.models.SalesPayment;

import java.util.ArrayList;

/**
 * Created by Ahmed on 3/23/2017.
 */

public class SalesInvoicePayment extends AppCompatActivity implements ChequeDialogFragment.NoticeDialogListener{
    //private enum FocusedField {FULDISCOUNT,CASH};
    boolean cashFocused;
    ArrayList<SalesInvoiceModel> data;
    SalesPayment payment;

    TextView credit;
    EditText fullInvDisc;
    TextView cheque;
    TextView cash;


    TextView subTot;
    TextView invTot;
    TextView discount;
    TextView freeQty;
    TextView returnTot;
    TextView returnQty;

    TextView total;

    Cheque chequeModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_ui);
        cashFocused =true;

        chequeModel = new Cheque();

        credit = (TextView) findViewById(R.id.si_pay_totcredit);
        cash = (TextView)findViewById(R.id.si_pay_cash);
        fullInvDisc = (EditText)findViewById(R.id.si_pay_full_inv_disc);
        cheque = (TextView) findViewById(R.id.si_pay_cheque);

        Button chqBtn = (Button)findViewById(R.id.si_pay_cheque_popup);
        chqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChequeDialog();
            }
        });

        subTot = (TextView) findViewById(R.id.si_pay_sub_tot);
        invTot = (TextView) findViewById(R.id.si_pay_inv_qty);
        discount = (TextView)findViewById(R.id.si_pay_disc);
        freeQty = (TextView)findViewById(R.id.si_pay_free_qty);
        returnTot =(TextView) findViewById(R.id.si_pay_return_tot);
        returnQty = (TextView)findViewById(R.id.si_pay_return_qty);

        total = (TextView)findViewById(R.id.si_pay_tot);

        Spinner creditDate = (Spinner)findViewById(R.id.pay_si_credit_dates);
        creditDate.setAdapter(new DBAdapter(this).getCreditDewDates());

        data = this.getIntent().getParcelableArrayListExtra(Constants.DATAARRAYNAME);
        final SalesInvoiceSummary sum = this.getIntent().getParcelableExtra(Constants.SUMMARYOBJECTNAME);
        payment = new SalesPayment(sum);

        Button back = (Button)findViewById(R.id.si_pay_return_to_si);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button generateInvoice = (Button)findViewById(R.id.si_pay_gen_inv);
        generateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesInvoicePayment.this,SalesSummaryActivity.class);
                intent.putParcelableArrayListExtra(Constants.DATAARRAYNAME,data);
                intent.putExtra(Constants.SALESPAYMENTSUMMARY,payment);
                intent.putExtra(Constants.CHEQUE,chequeModel);
                startActivity(intent);
            }
        });
        fullInvDisc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(""))payment.setFullInvDisc(Double.parseDouble(s.toString()));
                else payment.setFullInvDisc(0.0);
                init(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**fullInvDisc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    payment.setFullInvDisc(Double.parseDouble(fullInvDisc.getText().toString()));
                    init();
                }
            }
        });

        cash.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    payment.setCash(Double.parseDouble(cash.getText().toString()));
                    init();
                }
            }
        });*/
        cash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    payment.setCash(Double.parseDouble(cash.getText().toString()));
                    init(true);
                }else{
                    payment.setCash(0.0);
                }
                init(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Toast.makeText(getApplicationContext(),"Size of the array"+data.size(),Toast.LENGTH_LONG).show();

        init(true);
    }

    private void init(boolean cashFocused){
        if(!(payment.getDiscount()>0)){
            fullInvDisc.setEnabled(true);
        }

        credit.setText(payment.getCredit()+"");

        cheque.setText(chequeModel.getChequeVal()+"");
        payment.setCheque(chequeModel.getChequeVal());

        if(!cashFocused)cash.setText(payment.getCash()+"");

        subTot.setText(payment.getSubTotal()+"");
        invTot.setText(payment.getInvQty()+"");
        discount.setText(payment.getTotalDiscount()+"");
        freeQty.setText(payment.getFreeQty()+"");
        returnTot.setText(payment.getReturnTot()+"");
        returnQty.setText(payment.getReturnQty()+"");

        total.setText(payment.getTotal()+"");
    }

    private void showChequeDialog(){
        DialogFragment dialog = new ChequeDialogFragment();
        dialog.show(getFragmentManager(),"ChequeDialogFragment");

    }

    @Override
    public void onDialogPositiveClick(Cheque chq) {
       this.chequeModel = chq;
        init(false);
    }

    @Override
    public void onDialogNegativeClick() {

    }

    class DBAdapter extends BaseDBAdapter{
        public DBAdapter(Context c){
            super(c);
        }

        public ArrayAdapter<String> getCreditDewDates(){
            ArrayList<String> dates = new ArrayList<>();
            openDB();
            Cursor cursor = db.rawQuery("SELECT CreditDays FROM Mst_CreditDays WHERE IsActive=0; ",null);
            while(cursor.moveToNext()){
                dates.add(cursor.getInt(0)+"");
            }
            closeDB();
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,dates);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            return spinnerAdapter;
        }
    }
}
