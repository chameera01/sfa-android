package com.example.ahmed.sfa.activities;

import android.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ahmed.sfa.activities.Dialogs.PricipleDiscountDialog;
import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Dialogs.ChequeDialogFragment;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.Cheque;
import com.example.ahmed.sfa.models.Itinerary;
import com.example.ahmed.sfa.models.PrincipleDiscountModel;
import com.example.ahmed.sfa.models.SalesInvoiceModel;
import com.example.ahmed.sfa.models.SalesInvoiceSummary;
import com.example.ahmed.sfa.models.SalesPayment;
import com.example.ahmed.sfa.models.SalesReturnSummary;

import java.util.ArrayList;
import java.util.List;

import static com.example.ahmed.sfa.Constants.PRINCIPLE_DISCOUNT_REQUEST_RESULT;

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

    String customerNo;
    Itinerary itinerary;

    Spinner creditDays;

    EditText principleDiscount;

    int selectCreditDatePosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_payment);
        cashFocused =true;

        chequeModel = new Cheque();

        credit = (TextView) findViewById(R.id.si_pay_totcredit);
        cash = (TextView)findViewById(R.id.si_pay_cash);
        fullInvDisc = (EditText)findViewById(R.id.full_invoice_discount);
        cheque = (TextView) findViewById(R.id.si_pay_cheque);

        //Button chqBtn = (Button)findViewById(R.id.si_pay_cheque_popup);
        cheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChequeDialog();
            }
        });

        subTot = (TextView) findViewById(R.id.subtotal);
        invTot = (TextView) findViewById(R.id.invoice_qty);
        discount = (TextView)findViewById(R.id.payment_discount);
        freeQty = (TextView)findViewById(R.id.free_qty);
        returnTot =(TextView) findViewById(R.id.return_total);
        returnQty = (TextView)findViewById(R.id.return_qty);

        total = (TextView)findViewById(R.id.invoice_total);

        creditDays = (Spinner)findViewById(R.id.pay_si_credit_dates);
        creditDays.setAdapter(new DBAdapter(this).getCreditDewDates());
        creditDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCreditDatePosition = position;
                payment.setCreditDays(Integer.parseInt(creditDays.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        data = this.getIntent().getParcelableArrayListExtra(Constants.DATA_ARRAY_NAME);
        for(SalesInvoiceModel d :data){
            Log.w("here id",d.getId());
        }
        final SalesInvoiceSummary sum = this.getIntent().getParcelableExtra(Constants.SUMMARY_OBJECT_NAME);
        payment = new SalesPayment(sum);

        Button back = (Button)findViewById(R.id.si_pay_return_to_si);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesInvoicePayment.this,Return.class);
                intent.putExtra(Constants.CUSTOMER_NO,customerNo);
                intent.putExtra(Constants.ITINERARY,itinerary);
               // intent.putExtra(Constants.SALES_PAYMENT_SUMMARY,payment);//send this so the activity can return the object adding more data to it
                startActivityForResult(intent,Constants.RETURN_REQUEST_RESULT);

            }
        });

        Button generateInvoice = (Button)findViewById(R.id.si_pay_gen_inv);
        generateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesInvoicePayment.this,SalesSummaryActivity.class);
                intent.putParcelableArrayListExtra(Constants.DATA_ARRAY_NAME,data);
                intent.putExtra(Constants.SALES_PAYMENT_SUMMARY,payment);
                intent.putExtra(Constants.CHEQUE,chequeModel);
                intent.putExtra(Constants.ITINERARY,itinerary);
                intent.putExtra(Constants.CUSTOMER_NO,customerNo);
                startActivity(intent);
            }
        });
        fullInvDisc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equals("")){

                    for(SalesInvoiceModel model:data){
                        model.setCalculateFromRetail(true);
                    }
                    payment = new SalesPayment(SalesInvoiceSummary.createSalesInvoiceSummary(data));
                    payment.setFullInvDisc(Double.parseDouble(s.toString()));
                }
                else{

                    for(SalesInvoiceModel model:data){
                        model.setCalculateFromRetail(false);
                    }
                    payment = new SalesPayment(SalesInvoiceSummary.createSalesInvoiceSummary(data));
                    payment.setFullInvDisc(0.0);
                }
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
        customerNo = getIntent().getStringExtra(Constants.CUSTOMER_NO);
        itinerary = getIntent().getParcelableExtra(Constants.ITINERARY);

        principleDiscount = (EditText)findViewById(R.id.principle_discount);
        principleDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrincipleDiscountDialog();
            }
        });

        Button btn = (Button)findViewById(R.id.btn_principle_discount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrincipleDiscountDialog();
            }
        });
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

        if(!(payment.getCreditDays()==0))creditDays.setSelection(selectCreditDatePosition);
    }

    private void onPrincipleDiscountUpdate(List<PrincipleDiscountModel> models){

        for(SalesInvoiceModel salesInvoiceModel : data){
            for(PrincipleDiscountModel principleDiscountModel :models){
                if(principleDiscountModel.getPrincipleID().equals(salesInvoiceModel.getPrincipleID())){
                    if(principleDiscountModel.getDiscount()>0)salesInvoiceModel.setCalculateFromRetail(true);
                    else salesInvoiceModel.setCalculateFromRetail(false);
                    break;
                }
            }
        }
        updateSummary();
        payment.setTotalPrincipleDiscounts(getTotalPrincipleDiscount(models));

    }

    private void showPrincipleDiscountDialog(){
        Intent intent = new Intent(this,PricipleDiscountDialog.class);

        startActivityForResult(intent,Constants.PRINCIPLE_DISCOUNT_REQUEST_RESULT);
    }


    public double getTotalPrincipleDiscount(List<PrincipleDiscountModel> models){
        double total = 0.0;
        for(PrincipleDiscountModel model:models){
            total+=model.getDisountValue();
        }

        return  total;
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

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        Log.w("got","result");
        if(requestCode == Constants.RETURN_REQUEST_RESULT){//check for which request we got result
            Log.w("got","return request");
            if(resultCode==RESULT_OK){//check whether we got result ok response
                Log.w("got","return request ok");
                SalesReturnSummary returnSummary =data.getParcelableExtra(Constants.SALES_RETURN_SUMMARY);
                Log.w("return qty",returnSummary.getReturnQty()+"");
                Log.w("return tot",returnSummary.getReturnTot()+"");
                payment.setReturnTot(returnSummary.getReturnTot());
                payment.setReturnQty(returnSummary.getReturnQty());
                init(false);
            }
        }else if(requestCode==PRINCIPLE_DISCOUNT_REQUEST_RESULT){
            if(resultCode==RESULT_OK){
                List<PrincipleDiscountModel> list = data.getExtras().getParcelableArrayList("MODELS");
                onPrincipleDiscountUpdate(list);
                //updateSummary();
                init(false);
            }
        }
    }

    public void updateSummary(){
        SalesInvoiceSummary salesInvoiceSummary = SalesInvoiceSummary.createSalesInvoiceSummary(data);
        payment = new SalesPayment(salesInvoiceSummary);
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
