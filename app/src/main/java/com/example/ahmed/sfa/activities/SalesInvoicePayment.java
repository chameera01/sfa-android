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

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Dialogs.ChequeDialogFragment;
import com.example.ahmed.sfa.activities.Dialogs.PricipleDiscountDialog;
import com.example.ahmed.sfa.controllers.Utils;
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

public class SalesInvoicePayment extends AppCompatActivity implements ChequeDialogFragment.NoticeDialogListener {
    //Cash discount
    private static final double CASH_DISCOUNT_RATE = 0.03;
    //private enum FocusedField {FULDISCOUNT,CASH};
    boolean cashFocused;
    ArrayList<SalesInvoiceModel> data;
    SalesPayment payment;
    EditText fullInvDisc;
    TextView credit, cheque, cash, subTot, invTot, discount, freeQty, returnTot, returnQty, total;
    TextView cashDiscount;
    double cashDisc;
    Cheque chequeModel;
    String customerNo;
    Itinerary itinerary;
    Spinner creditDays;
    EditText principleDiscount;
    int selectCreditDatePosition;
    private Boolean lineDiscounted = false;
    private boolean fullDiscounted = false;
    private boolean brandDiscounted = false;
    private double finalTot;
    private SalesReturnSummary returnSummary;
    private boolean isCashCustomer;
    private ArrayList<SalesInvoiceModel> returnedList;
    private SalesPayment returnHeaderSummary;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {

        super.onResume();
        returnSummary = getIntent().getParcelableExtra(Constants.SALES_RETURN_SUMMARY);
        returnHeaderSummary = getIntent().getParcelableExtra("RETURN_HEADER_SUMMARY");
        returnedList = getIntent().getParcelableArrayListExtra("RETURNED_LIST");
        Log.d("SUM", "inside onResume");
        if (returnSummary != null) {
            Log.d("SUM", "inside ret sum not null_returnQty: " + payment.getReturnQty() + " retTot: " + payment.getReturnTot());
            payment.setReturnTot(returnSummary.getReturnTot());
            payment.setReturnQty(returnSummary.getReturnQty());
            init(false);
        } else {
            Log.d("SUM", "returnSummary null");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_payment);

        cashFocused = true;
        returnedList = new ArrayList<>();

        chequeModel = new Cheque();

        credit = (TextView) findViewById(R.id.si_pay_totcredit);
        cash = (TextView) findViewById(R.id.si_pay_cash);
        fullInvDisc = (EditText) findViewById(R.id.full_invoice_discount);
        cheque = (TextView) findViewById(R.id.si_pay_cheque);
        subTot = (TextView) findViewById(R.id.subtotal);
        invTot = (TextView) findViewById(R.id.invoice_qty);
        discount = (TextView) findViewById(R.id.payment_discount);
        freeQty = (TextView) findViewById(R.id.free_qty);
        returnTot = (TextView) findViewById(R.id.return_total);
        returnQty = (TextView) findViewById(R.id.return_qty);
        total = (TextView) findViewById(R.id.invoice_total);
        cashDiscount = (TextView) findViewById(R.id.cash_discount);
        creditDays = (Spinner) findViewById(R.id.pay_si_credit_dates);

        isCashCustomer = getIntent().getBooleanExtra("IS_CASH_CUSTOMER", false);
        customerNo = getIntent().getStringExtra(Constants.CUSTOMER_NO);
        itinerary = getIntent().getParcelableExtra(Constants.ITINERARY);
        //returnSummary = getIntent().getParcelableExtra(Constants.SALES_RETURN_SUMMARY);

        //Button chqBtn = (Button)findViewById(R.id.si_pay_cheque_popup);
        cheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChequeDialog();
            }
        });


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

        final SalesInvoiceSummary sum = this.getIntent().getParcelableExtra(Constants.SUMMARY_OBJECT_NAME);
        payment = new SalesPayment(sum);

        Button back = (Button) findViewById(R.id.si_pay_return_to_si);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesInvoicePayment.this, Return.class);
                intent.putExtra(Constants.CUSTOMER_NO, customerNo);
                intent.putExtra(Constants.ITINERARY, itinerary);
                intent.putExtra("CALLING_ACT", true);
                intent.putExtra("INV_TOTAL", Double.parseDouble(total.getText().toString()));
                // intent.putExtra(Constants.SALES_PAYMENT_SUMMARY,payment);//send this so the activity can return the object adding more data to it
                startActivityForResult(intent, Constants.RETURN_REQUEST_RESULT);

            }
        });


        Button generateInvoice = (Button) findViewById(R.id.si_pay_gen_inv);
        generateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("SCROLL", "inside payment_line discount: " + payment.getDiscount());
                Log.d("SCROLL", "inside payment_full discount: " + payment.getFullInvDisc());
                Log.d("SCROLL", "inside payment_brand discount: " + payment.getTotalPrincipleDiscounts());

                if (payment.getDiscount() > 0.0) {
                    Log.d("SCROLL", "inside payment_line discount: " + payment.getDiscount());
                    lineDiscounted = true;
                }
                if (payment.getFullInvDisc() > 0.0) {
                    Log.d("SCROLL", "inside payment_full discount: " + payment.getFullInvDisc());
                    fullDiscounted = true;
                }
                if (payment.getTotalPrincipleDiscounts() > 0.0) {
                    Log.d("SCROLL", "inside payment_brand discount: " + payment.getTotalPrincipleDiscounts());
                    brandDiscounted = true;
                }

                if (isCashCustomer) {
                    payment.setCash(finalTot);
                    payment.setCheque(0.0);
                    payment.setCredit(0.0);
                    payment.setTotal(finalTot);


                    Log.d("CASH", "inside next button click_if(isCashCustomer)->getTotal(): " + payment.getTotal());
                    Log.d("CASH", "inside next button click_if(isCashCustomer)->getCash(): " + payment.getCash());
                    Log.d("CASH", "inside next button click_if(isCashCustomer)->getCredit(): " + payment.getCredit());
                }

                Intent intent = new Intent(SalesInvoicePayment.this, SalesSummaryActivity.class);
                intent.putParcelableArrayListExtra(Constants.DATA_ARRAY_NAME, data);
                intent.putExtra(Constants.SALES_PAYMENT_SUMMARY, payment);
                Log.d("CASH", "inside next button click_getTotal(): " + payment.getTotal());
                if (returnedList != null) {
                    Log.d("BLUE", "inside payment-ret size: " + returnedList.size());
                    intent.putExtra("RETURNED_LIST", returnedList);
                }
                intent.putExtra("RETURN_HEADER_SUMMARY", returnHeaderSummary);
                intent.putExtra(Constants.SALES_RETURN_SUMMARY, returnSummary);
                intent.putExtra(Constants.CHEQUE, chequeModel);
                intent.putExtra(Constants.ITINERARY, itinerary);
                intent.putExtra(Constants.CUSTOMER_NO, customerNo);
                intent.putExtra("CASH_DISCOUNT", cashDisc);
                intent.putExtra("DISCOUNT_TYPE_LINE", lineDiscounted);
                intent.putExtra("DISCOUNT_TYPE_FULL", fullDiscounted);
                intent.putExtra("DISCOUNT_TYPE_BRAND", brandDiscounted);
                startActivity(intent);
            }
        });


        fullInvDisc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals("")) {

                    for (SalesInvoiceModel model : data) {
                        model.setCalculateFromRetail(true);
                    }
                    payment = new SalesPayment(SalesInvoiceSummary.createSalesInvoiceSummary(data));
                    payment.setFullInvDisc(Double.parseDouble(s.toString()));
                } else {

                    for (SalesInvoiceModel model : data) {
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

        if (!isCashCustomer) {
            cash.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().equals("")) {
                        payment.setCash(Double.parseDouble(cash.getText().toString()));
                        init(true);
                    } else {
                        payment.setCash(0.0);
                    }
                    init(true);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        Log.d("SUM", "inside oncreate");
        if (returnSummary != null) {
            Log.d("SUM", "inside ret sum not null_returnQty: " + payment.getReturnQty() + " retTot: " + payment.getReturnTot());
            payment.setReturnTot(returnSummary.getReturnTot());
            payment.setReturnQty(returnSummary.getReturnQty());
            init(false);
        } else {
            Log.d("SUM", "returnSummary null");
        }


        Button btn = (Button) findViewById(R.id.btn_principle_discount);
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
            //if only one discount is allowed, disable the brand discount button as well
        }

//        credit.setText(payment.getCredit() + "");
//        cheque.setText(chequeModel.getChequeVal() + "");

        subTot.setText(payment.getSubTotal() + "");
        invTot.setText(payment.getInvQty() + "");
        discount.setText(payment.getTotalDiscount() + "");
        freeQty.setText(payment.getFreeQty() + "");
        returnTot.setText(payment.getReturnTot() + "");
        returnQty.setText(payment.getReturnQty() + "");

        finalTot = 0.0;
        if (isCashCustomer) {
            cashDisc = (payment.getTotal() * CASH_DISCOUNT_RATE);
            Log.d("CASH", "cashDisc: " + cashDisc);

            cashDiscount.setText(Utils.decimalFix(cashDisc) + "");
            finalTot = payment.getTotal() - cashDisc;
            Log.d("CASH", "finalTot: " + finalTot);

            payment.setCash(finalTot);
            payment.setCheque(0.0);
            payment.setCredit(0.0);
            payment.setTotal(finalTot);

            Log.d("CASH", "payment.Total: " + payment.getTotal());
            Log.d("CASH", "payment.Cheque: " + payment.getCheque());
            Log.d("CASH", "payment.Credit: " + payment.getCredit());

            total.setText(Utils.decimalFix(finalTot) + "");
            credit.setText(String.valueOf(0.0));
            cheque.setText(String.valueOf(0.0));
            cash.setText(Utils.decimalFix(finalTot) + "");
            cash.setEnabled(false);
            credit.setEnabled(false);
            cheque.setEnabled(false);
            creditDays.setEnabled(false);

            Log.d("CASH", "payment.Cash: " + payment.getCash());
            Log.d("CASH", "payment.Cheque: " + payment.getCheque());
            Log.d("CASH", "payment.Credit: " + payment.getCredit());

        } else {
            cashDiscount.setText(String.valueOf(0.0));
            total.setText(Utils.decimalFix(payment.getTotal()) + "");
            credit.setText(Utils.decimalFix(payment.getCredit()) + "");
            cheque.setText(Utils.decimalFix(chequeModel.getChequeVal()) + "");
            if (!cashFocused) cash.setText(Utils.decimalFix(payment.getCash()) + "");
            if (!(payment.getCreditDays() == 0)) creditDays.setSelection(selectCreditDatePosition);
        }
        payment.setCheque(Utils.decimalFix(chequeModel.getChequeVal()));

//        if (!(payment.getCreditDays() == 0)) creditDays.setSelection(selectCreditDatePosition);
    }

    private void onPrincipleDiscountUpdate(List<PrincipleDiscountModel> models) {

        for (SalesInvoiceModel salesInvoiceModel : data) {
            for (PrincipleDiscountModel principleDiscountModel : models) {
                if (principleDiscountModel.getPrincipleID().equals(salesInvoiceModel.getPrincipleID())) {
                    if (principleDiscountModel.getDiscount() > 0)
                        salesInvoiceModel.setCalculateFromRetail(true);
                    else salesInvoiceModel.setCalculateFromRetail(false);
                    break;
                }
            }
        }
        updateSummary();
        payment.setTotalPrincipleDiscounts(getTotalPrincipleDiscount(models));

    }

    private void showPrincipleDiscountDialog() {
        Intent intent = new Intent(this, PricipleDiscountDialog.class);
        startActivityForResult(intent, Constants.PRINCIPLE_DISCOUNT_REQUEST_RESULT);
    }


    public double getTotalPrincipleDiscount(List<PrincipleDiscountModel> models) {
        double total = 0.0;
        for (PrincipleDiscountModel model : models) {
            total += model.getDisountValue();
        }

        return total;
    }

    private void showChequeDialog() {
        DialogFragment dialog = new ChequeDialogFragment();
        dialog.show(getFragmentManager(), "ChequeDialogFragment");

    }

    @Override
    public void onDialogPositiveClick(Cheque chq) {
        this.chequeModel = chq;
        init(false);
    }

    @Override
    public void onDialogNegativeClick() {

    }

    //after return activity


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.w("got","result");
//        if(requestCode == Constants.RETURN_REQUEST_RESULT){//check for which request we got result
//            Log.w("got","return request");
//            if(resultCode==RESULT_OK){//check whether we got result ok response
//                Log.w("got","return request ok");
//                SalesReturnSummary returnSummary = data.getParcelableExtra(Constants.SALES_RETURN_SUMMARY);
//                Log.w("return qty",returnSummary.getReturnQty()+"");
//                Log.w("return tot",returnSummary.getReturnTot()+"");
//                payment.setReturnTot(returnSummary.getReturnTot());
//                payment.setReturnQty(returnSummary.getReturnQty());
//                init(false);
//            }
//        }else
        if (requestCode == PRINCIPLE_DISCOUNT_REQUEST_RESULT) {
            if (resultCode == RESULT_OK) {
                List<PrincipleDiscountModel> list = data.getExtras().getParcelableArrayList("MODELS");

                onPrincipleDiscountUpdate(list);
                //updateSummary();
                init(false);
            }
        }
    }

    public void updateSummary() {
        SalesInvoiceSummary salesInvoiceSummary = SalesInvoiceSummary.createSalesInvoiceSummary(data);
        payment = new SalesPayment(salesInvoiceSummary);
    }

    class DBAdapter extends BaseDBAdapter {
        public DBAdapter(Context c) {
            super(c);
        }

        public ArrayAdapter<String> getCreditDewDates() {
            ArrayList<String> dates = new ArrayList<>();
            openDB();
            Cursor cursor = db.rawQuery("SELECT CreditDays FROM Mst_CreditDays WHERE IsActive=0; ", null);
            while (cursor.moveToNext()) {
                dates.add(cursor.getInt(0) + "");
            }
            closeDB();
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, dates);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            return spinnerAdapter;
        }
    }
}
