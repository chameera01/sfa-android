package com.example.ahmed.sfa.controllers.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.SalesInvoice;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.SalesInvoiceModel;

import java.util.ArrayList;

/**
 * Created by Ahmed on 3/19/2017.
 */

public class SalesInvoiceProductsTableAdapter {
    private ArrayList<SalesInvoiceModel> data;
    private TableLayout table;
    private Activity activity;
    DBAdapter dbAdapter;
    int selectedRow;
    SalesInvoiceModel selectedModel;
    boolean focused=false;

    public SalesInvoiceModel getSelectedModel(){
        return selectedModel;
    }

    public SalesInvoiceProductsTableAdapter(ArrayList<SalesInvoiceModel> data, TableLayout table, Activity activity) {
        dbAdapter = new DBAdapter(activity);
        this.data = data;
        this.table = table;
        this.activity = activity;
        initTable();
    }

    public SalesInvoiceProductsTableAdapter(TableLayout table, Activity activity) {
        selectedRow =-1;
        dbAdapter = new DBAdapter(activity);
        this.data = dbAdapter.getAllData();
        this.table = table;
        this.activity = activity;
        initTable();
   }

    private void initTable(){

        table.removeAllViews();
        for (int i =0;i<data.size();i++) {
            SalesInvoiceModel model = data.get(i);
            if(!((SalesInvoice)activity).isAlreadySelected(model)){
                TableRow row;
                Log.w("Selected Row",selectedRow+"");
                if(selectedRow==i){
                    row = getEditView(model);

                }else{
                    row = getView(model,i);

                }
                row.setTag(i);
                table.addView(row);
            }
        }

    }

    public void refreshTable(){
        initTable();
    }

    private TableRow getView(SalesInvoiceModel salesrow,int tag){

        //SalesInvoiceModel salesrow = data.get(position);
        TableRow row = (TableRow)activity.getLayoutInflater().inflate(R.layout.si_viewrow_layout,table,false);
        row.setTag(tag);
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


        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //((SalesInvoice)activity).selectItem(data.get(((int)v.getTag())));
                selectedRow = (int)v.getTag();
                initTable();
                Log.w("Selected Row",selectedRow+"");
                return true;
            }


        });
        //Log.w("row view",salesrow.getCode());

        for(int i=0;i<row.getChildCount();i++){
            row.getChildAt(i).setFocusable(false);
        }
        return row;
    }

    private TableRow getEditView(SalesInvoiceModel salesrow){
        TableRow row = (TableRow)activity.getLayoutInflater().inflate(R.layout.si_editview_row,table,false);
        salesrow = data.get(selectedRow);

        TextView code = (TextView)row.findViewById(R.id.code_e_si);
        TextView product = (TextView)row.findViewById(R.id.product_e_si);
        code.setText(salesrow.getCode());
        product.setText(salesrow.getProduct());

        final Button addBtn = (Button)row.findViewById(R.id.si_row_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("Action","Clicked");
                int dataPos = selectedRow;

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                v.requestFocus();

                ((SalesInvoice)activity).selectItem(data.get(dataPos));
                selectedRow = -1;

                initTable();

                //data.get(dataPos)



            }
        });

        TextView batch = (TextView)row.findViewById(R.id.batch_e_si);
        batch.setText(salesrow.getBatchNumber());

        TextView expiry = (TextView)row.findViewById(R.id.expiry_e_si);
        expiry.setText(salesrow.getExpiryDate());

        TextView unitprice = (TextView) row.findViewById(R.id.unit_price_e_si);
        unitprice.setText(salesrow.getUnitPrice()+"");

        TextView stock = (TextView)row.findViewById(R.id.stock_e_si);
        stock.setText(salesrow.getStock()+"");

        EditText shelf = (EditText)row.findViewById(R.id.shelf_e_si);
        shelf.setText(salesrow.getShelf()+"");
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
                }else{
                    focused = true;
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
                    initTable();
                }
            }
        });

        EditText request = (EditText)row.findViewById(R.id.request_e_si);
        request.setText(salesrow.getRequest()+"");
        request.addTextChangedListener(new TextWatcher() {
            CharSequence old;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!(s.equals(data.get(selectedRow).getRequest()+""))){
                    if(!s.toString().equals("")){
                        SalesInvoiceModel selectedModel = data.get(selectedRow);
                        int val = Integer.parseInt(s.toString());
                        int stock =selectedModel.getStock();
                        if(val>stock){
                            Toast.makeText(activity.getApplicationContext(),"Enter a valid Qty",Toast.LENGTH_SHORT).show();
                            val=stock;
                        }
                        selectedModel.setRequest(val);
                        selectedModel.setOrder(val);

                        if(selectedModel.getFree()+selectedModel.getOrder()>selectedModel.getStock()){
                            selectedModel.setFree(0);
                        }
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
                    Log.w("Action","Lost Focus");

                    initTable();



                }
            }
        });


        final EditText order = (EditText)row.findViewById(R.id.order_e_si);
        order.setText(salesrow.getOrder()+"");
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
                        SalesInvoiceModel selectedModel = data.get(selectedRow);
                        int val = Integer.parseInt(s.toString());
                        int stock =selectedModel.getStock();

                        if(val>stock){
                            Toast.makeText(activity.getApplicationContext(),"Order is more than the stock",Toast.LENGTH_SHORT).show();
                            val=stock;
                        }
                        selectedModel.setOrder(val);//make sure we set order before
                        //chekcing the total of order and free against stock

                        if(selectedModel.getOrder()+selectedModel.getFree()>selectedModel.getStock()){
                            selectedModel.setFree(0);

                        }



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
                    initTable();
                    Toast.makeText(activity.getApplicationContext(),"lost",Toast.LENGTH_SHORT).show();
                }
            }
        });

        EditText free = (EditText)row.findViewById(R.id.free_e_si);
        free.setText(salesrow.getFree()+"");
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
                        SalesInvoiceModel selectedModel = data.get(selectedRow);
                        int val = Integer.parseInt(s.toString());
                        selectedModel.setFree(val);
                        if(selectedModel.getOrder()+selectedModel.getFree()>selectedModel.getStock()){
                            selectedModel.setFree(0);

                        }
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
                    initTable();
                }
            }
        });


        EditText disc = (EditText)row.findViewById(R.id.dsc_e_asi);
        disc.setText(salesrow.getDiscountRate()+"");
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
                    initTable();
                }
            }
        });


        TextView linVal = (TextView)row.findViewById(R.id.line_val_e_si);
        linVal.setText(salesrow.getLineValue()+"");


        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //((SalesInvoice)activity).selectItem(data.get(((int)v.getTag())));

                initTable();
                return true;
            }


        });


        //diable order,free and discount if stock is 0
        if(!(salesrow.getStock()>0)){
            free.setEnabled(false);
            order.setEnabled(false);
            disc.setEnabled(false);
        }

        return row;
    }




    public TableLayout getTable(){
        return table;
    }

    public TableLayout updateData(String principle,String subBrand,String product ){
        data = dbAdapter.getAllData(principle,subBrand,product);
        initTable();
        return table;

    }



    class DBAdapter extends BaseDBAdapter{

        public DBAdapter(Context c) {
            super(c);
        }

        public ArrayList<SalesInvoiceModel> getAllData(){
            ArrayList<SalesInvoiceModel> data = new ArrayList<>();
            openDB();
            Cursor cursor = db.rawQuery("SELECT b._id,a.ItemCode,a.Description,b.BatchNumber,b.ExpiryDate,b.SellingPrice,b.Qty" +
                    " FROM Mst_ProductMaster a inner join Tr_TabStock b " +
                    "on a.ItemCode  = b.ItemCode ;",null);

            while(cursor.moveToNext()){
                data.add(new SalesInvoiceModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getDouble(5),cursor.getInt(6)));
            }
            closeDB();

            return data;
        }

        public ArrayList<SalesInvoiceModel> getAllData(String principle,String subbrand,String product){
            ArrayList<SalesInvoiceModel> data = new ArrayList<>();
            openDB();

            String sql = "SELECT b._id, a.ItemCode,a.Description,b.BatchNumber,b.ExpiryDate,b.SellingPrice,b.Qty" +
                    " FROM Mst_ProductMaster a inner join Tr_TabStock b " +
                    "on a.ItemCode = b.ItemCode WHERE ";

            if (!(principle.equals("ALL")|| principle == null) ){
                sql+="a.PrincipleID ='"+principle+"'";
                //principle = "";
                if(!(subbrand.equals("ALL") || subbrand == null)){
                    sql+=" AND a.BrandID ='"+subbrand+"'";
                    //subbrand = "";
                }
                sql+=" AND ";
            }

            if(product.equals("ALL")|| product==null){
                product = "";

            }
            sql+="a.Description Like '"+product+"%'";

            Cursor cursor = db.rawQuery(sql,null);

            while(cursor.moveToNext()){
                data.add(new SalesInvoiceModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getDouble(5),cursor.getInt(6)));
            }
            closeDB();

            return data;

        }
    }
}
