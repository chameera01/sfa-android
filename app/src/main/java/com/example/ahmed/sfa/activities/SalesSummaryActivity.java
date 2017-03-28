package com.example.ahmed.sfa.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.test.suitebuilder.TestMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.Cheque;
import com.example.ahmed.sfa.models.SalesInvoiceModel;
import com.example.ahmed.sfa.models.SalesPayment;

import java.util.ArrayList;

/**
 * Created by Ahmed on 3/27/2017.
 */

public class SalesSummaryActivity extends AppCompatActivity {
    ArrayList<SalesInvoiceModel> data;
    SalesPayment payment;
    Cheque chequeModel;
    TableLayout table;

    TextView subtotal;
    TextView discount;
    TextView returntot;
    TextView invoiceQty;
    TextView freeQty;
    TextView returnQty;

    TextView credit;
    TextView cheque;
    TextView cash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_invoice_summary_activity);

        data = getIntent().getParcelableArrayListExtra(Constants.DATAARRAYNAME);
        payment = getIntent().getParcelableExtra(Constants.SALESPAYMENTSUMMARY);
        chequeModel = getIntent().getParcelableExtra(Constants.CHEQUE);

        table = (TableLayout)findViewById(R.id.si_pay_sum_data);


        subtotal = (TextView) findViewById(R.id.si_pay_sum_sub_tot);
        discount =(TextView)findViewById(R.id.si_pay_sum_disc);
        returntot = (TextView)findViewById(R.id.si_pay_sum_return_tot);
        invoiceQty = (TextView)findViewById(R.id.si_pay_sum_inv_qty);
        freeQty = (TextView)findViewById(R.id.si_pay_sum_free_qty);
        returnQty = (TextView)findViewById(R.id.si_pay_sum__return_qty);

        cash = (TextView)findViewById(R.id.si_pay_sum_cash);
        cheque = (TextView)findViewById(R.id.si_pay_sum_chq);
        credit = (TextView)findViewById(R.id.si_pay_sum_crdt);

        Button printBtn = (Button)findViewById(R.id.print_button_ss);
        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button saveBtn = (Button)findViewById(R.id.save_button_ss);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        init();
    }

    private void init(){
        //table.removeAllViews();
        for(int i=0;i<data.size();i++){
            table.addView(getView(i));

        }

        subtotal.setText(payment.getSubTotal()+"");
        discount.setText(payment.getTotalDiscount()+"");
        returntot.setText(payment.getReturnTot()+"");
        returnQty.setText(payment.getReturnQty()+"");
        invoiceQty.setText(payment.getInvQty()+"");
        freeQty.setText(payment.getFreeQty()+"");
        returnQty.setText(payment.getReturnQty()+"");


        cash.setText(payment.getCash()+"");
        credit.setText(payment.getCredit()+"");
        cheque.setText(payment.getCheque()+"");
    }

    public TableRow getView(int i){
        SalesInvoiceModel salesrow = data.get(i);
        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.si_viewrow_layout,table,false);

        TextView code = (TextView)row.findViewById(R.id.code_v_si);
        TextView product = (TextView)row.findViewById(R.id.product_v_si);
        TextView batch = (TextView)row.findViewById(R.id.batch_v_si);
        TextView expiry = (TextView)row.findViewById(R.id.expiry_v_si);
        TextView unitprice = (TextView)row.findViewById(R.id.unit_price_v_si);
        TextView stock = (TextView)row.findViewById(R.id.stock_v_si);
        TextView shelf = (TextView)row.findViewById(R.id.shelf_v_si);
        TextView request = (TextView)row.findViewById(R.id.request_v_si);
        TextView order = (TextView)row.findViewById(R.id.order_v_si);
        TextView free = (TextView)row.findViewById(R.id.free_v_si);
        TextView disc = (TextView)row.findViewById(R.id.dsc_v_asi);
        TextView linVal = (TextView)row.findViewById(R.id.line_val_v_si);

        code.setText(salesrow.getCode());
        product.setText(salesrow.getProduct());
        batch.setText(salesrow.getBatchNumber());
        expiry.setText(salesrow.getExpiryDate());
        unitprice.setText(salesrow.getUnitPrice()+"");
        stock.setText(salesrow.getStock()+"");
        shelf.setText(salesrow.getShelf()+"");
        request.setText(salesrow.getRequest()+"");
        order.setText(salesrow.getOrder()+"");
        free.setText(salesrow.getFree()+"");
        disc.setText(salesrow.getDiscountRate()+"");
        linVal.setText(salesrow.getLineValue()+"");

        Toast.makeText(getApplicationContext(),"val "+salesrow.getOrder(),Toast.LENGTH_SHORT).show();
        return row;
    }

    class DBAdapter extends BaseDBAdapter{
        public DBAdapter(Context c){
            super(c);
        }


    }
}
