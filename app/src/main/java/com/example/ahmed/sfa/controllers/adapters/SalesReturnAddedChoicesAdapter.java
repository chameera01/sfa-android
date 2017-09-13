package com.example.ahmed.sfa.controllers.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.SalesInvoiceModel;
import com.example.ahmed.sfa.models.SalesInvoiceSummary;

import java.util.ArrayList;

/**
 * Created by Ahmed on 3/19/2017.
 */

public class SalesReturnAddedChoicesAdapter {
    private ArrayList<SalesInvoiceModel> data;
    private TableLayout table;
    private Activity activity;
    DBAdapter dbAdapter;
    //SalesInvoiceModel selectedModel;
    int selectedRow;

    public SalesReturnAddedChoicesAdapter(TableLayout table, Activity activity){
        selectedRow = -1;
        dbAdapter=new DBAdapter(activity);
        data= new ArrayList<>();
        this.table = table;
        this.activity = activity;
        initTable();
    }

    public SalesInvoiceSummary getSalesInvoiceSummary(){
        double discount=0.0;
        double subtotal=0.0;
        double total=0.0;
        int invoicedQty=0;
        int freeQty=0;

        for(int i=0;i<data.size();i++){
            discount +=data.get(i).getDiscount();
            subtotal += data.get(i).getSubtotalVal();
            total += data.get(i).getLineValue();
            invoicedQty += data.get(i).getInvoiceQuantity();
            freeQty += data.get(i).getFree();
        }

        return new SalesInvoiceSummary(discount,subtotal,total,invoicedQty,freeQty);
    }
    private  void initTable(){
        table.removeAllViews();
        for(int i =0;data.size()>i;i++){
            if(i==selectedRow){
                table.addView(getEditView(data.get(selectedRow)));
            }else {
                table.addView(getView(data.get(i), i));
            }
        }
    }

    public boolean isAlreadyAdded(SalesInvoiceModel model){
        for (SalesInvoiceModel m : data) {
            if(m.equals(model)) return true;
        }
        return false;
    }

    public TableRow getView(SalesInvoiceModel salesrow,int tag){
        TableRow row = (TableRow)activity.getLayoutInflater().inflate(R.layout.si_viewrow_layout,table,false);
        row.setTag(tag);
        TextView code = (TextView)row.findViewById(R.id.code_v_si);
        TextView product = (TextView)row.findViewById(R.id.product_v_si);
        TextView batch = (TextView)row.findViewById(R.id.batch_v_si);
        TextView expiry = (TextView)row.findViewById(R.id.expiry_v_si);
        TextView unitprice = (TextView)row.findViewById(R.id.unit_price_v_si);
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
        shelf.setText(salesrow.getShelf()+"");
        request.setText(salesrow.getRequest()+"");
        order.setText(salesrow.getOrder()+"");
        free.setText(salesrow.getFree()+"");
        disc.setText(salesrow.getDiscountRate()+"");
        linVal.setText(salesrow.getLineValue()+"");
        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedRow = (int)v.getTag();
                initTable();
                return true;
            }
        });
        return row;
    }

    public TableRow getEditView(SalesInvoiceModel model){
        TableRow row = (TableRow) activity.getLayoutInflater().inflate(R.layout.si_editview_row,table,false);
        SalesInvoiceModel salesrow = model;



        TextView code = (TextView)row.findViewById(R.id.code_e_si);
        TextView product = (TextView)row.findViewById(R.id.product_e_si);
        TextView batch = (TextView)row.findViewById(R.id.batch_e_si);
        TextView expiry = (TextView)row.findViewById(R.id.expiry_e_si);
        TextView unitprice = (TextView) row.findViewById(R.id.unit_price_e_si);
        EditText shelf = (EditText) row.findViewById(R.id.shelf_e_si);

        EditText request = (EditText) row.findViewById(R.id.request_e_si);
        EditText order = (EditText) row.findViewById(R.id.order_e_si);
        EditText free = (EditText) row.findViewById(R.id.free_e_si);
        EditText disc = (EditText) row.findViewById(R.id.dsc_e_si);
        TextView linVal = (TextView)row.findViewById(R.id.line_val_e_si);

        code.setText(salesrow.getCode());
        product.setText(salesrow.getProduct());
        batch.setText(salesrow.getBatchNumber());
        expiry.setText(salesrow.getExpiryDate());
        unitprice.setText(salesrow.getUnitPrice()+"");
        shelf.setText(salesrow.getShelf()+"");
        request.setText(salesrow.getRequest()+"");
        order.setText(salesrow.getOrder()+"");
        free.setText(salesrow.getFree()+"");
        disc.setText(salesrow.getDiscountRate()+"");
        linVal.setText(salesrow.getLineValue()+"");




        Button btnremove = (Button)row.findViewById(R.id.si_row_add_btn);
        btnremove.setText("Remove");


        shelf.addTextChangedListener(new TextWatcher() {
            CharSequence old;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //old=s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!(s.equals(data.get(selectedRow).getShelf()+""))){
                    if(!s.toString().equals("")) {
                        data.get(selectedRow).setShelf(Integer.parseInt(s.toString()));
                        //initTable();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        shelf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    selectedRow=-1;
                    initTable();
                }
            }
        });

        request.addTextChangedListener(new TextWatcher() {
            CharSequence old;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!(s.equals(data.get(selectedRow).getRequest()+""))){
                    if(!s.toString().equals("")){
                        data.get(selectedRow).setRequest(Integer.parseInt(s.toString()));
                        //initTable();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        request.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    selectedRow=-1;
                    initTable();
                }
            }
        });


        order.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //old=s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //old=s;


                if(!(s.equals(data.get(selectedRow).getOrder()+""))){
                    if(!(s.toString().equals(""))){
                        data.get(selectedRow).setOrder(Integer.parseInt(s.toString()));

                    }


                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //initTable();
            }
        });
        order.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    selectedRow=-1;
                    initTable();
                }
            }
        });


        free.addTextChangedListener(new TextWatcher() {
            CharSequence old;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //old=s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!(s.equals(data.get(selectedRow).getFree()+""))){
                    if(!s.toString().equals("")){
                        data.get(selectedRow).setFree(Integer.parseInt(s.toString()));
                        //initTable();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        free.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    selectedRow=-1;
                    initTable();
                }
            }
        });


        disc.addTextChangedListener(new TextWatcher() {
            CharSequence old;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //old=s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!(s.equals(data.get(selectedRow).getDiscountRate()+""))){
                    if(!s.toString().equals("")){
                        data.get(selectedRow).setDiscountRate(Double.parseDouble(s.toString()));


                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //initTable();
            }
        });
        disc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    selectedRow=-1;
                    initTable();
                }
            }
        });



        return row;

    }


    public void addToList(SalesInvoiceModel model){
        Log.w("adding model",model.getId());
        data.add(model);
        initTable();
    }

    public ArrayList<SalesInvoiceModel> getDataArray(){
        return data;
    }

    class DBAdapter extends BaseDBAdapter{

        protected DBAdapter(Context c) {
            super(c);
        }

    }
}
