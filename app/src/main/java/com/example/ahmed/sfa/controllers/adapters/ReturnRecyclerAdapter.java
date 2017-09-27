package com.example.ahmed.sfa.controllers.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.SalesInvoiceModel;

import java.util.List;

/**
 * Created by Ahmed on 9/17/2017.
 */

public class ReturnRecyclerAdapter extends RecyclerView.Adapter<ReturnRecyclerAdapter.MyViewHolder> implements SummaryUpdater{
    private List<SalesInvoiceModel> salesInvoice;
    boolean onBind;

    public final String SHELF = "SHLEF",REQUEST="REQUEST", RETURN ="RETURN",FREE="FREE",DISCOUNT="DISCOUNT",
            LINEVAL = "LINEVAL",UNITPRICE="UNITPRICE";
    DBAdapter dbAdapter;

    SummaryUpdateListner listner;

    @Override
    public void notifyUpdate() {
        listner.updateSummary();
    }

    @Override
    public void addListener(SummaryUpdateListner listner) {
        this.listner = listner;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView code,product,expiry,stock,lineval,batchNum;
        EditText shelf,request,unitprice, returnQty,free,discount;
        int ref;

        View view;
        public MyViewHolder(View view){
            super(view);
            this.view = view;
            dbAdapter = new ReturnRecyclerAdapter.DBAdapter(view.getContext());
            code = (TextView) view.findViewById(R.id.code_e);
            product =(TextView) view.findViewById(R.id.product_e);
            batchNum = (TextView) view.findViewById(R.id.batch_e);
            expiry = (TextView)view.findViewById(R.id.expiry_e);

            stock = (TextView)view.findViewById(R.id.stock_e);
            lineval = (TextView)view.findViewById(R.id.line_val_e);

            shelf = (EditText)view.findViewById(R.id.shelf_e);
            request =(EditText)view.findViewById(R.id.request_e);
            returnQty = (EditText)view.findViewById(R.id.order_e);
            free = (EditText)view.findViewById(R.id.free_e);
            discount = (EditText)view.findViewById(R.id.dsc_e);
            unitprice = (EditText) view.findViewById(R.id.unit_price);

        }

        public void setCursor(String view){
            switch (view){
                case SHELF:
                    shelf.setSelection(shelf.getText().length());
                    break;
                case REQUEST:
                    request.setSelection(request.getText().length());
                    break;

                case RETURN:
                    returnQty.setSelection(returnQty.getText().length());
                    break;

                case FREE:
                    free.setSelection(free.getText().length());
                    break;
                case DISCOUNT:
                    discount.setSelection(discount.getText().length());
                    break;



            }
        }

        public void setColor(int color){
            view.setBackgroundColor(color);
        }

    }

    public ReturnRecyclerAdapter(List<SalesInvoiceModel> list){
        this.salesInvoice = list;
    }

    public ReturnRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.returntable_layout,parent,false);

        return new ReturnRecyclerAdapter.MyViewHolder(itemView);

    }

    public void onBindViewHolder(final MyViewHolder holder, final int position, List<Object> payload){
        //onBindViewHolder(holder,position);

        onBind = true;
        holder.ref = position;
        SalesInvoiceModel siModel = salesInvoice.get(position);
        holder.code.setText(siModel.getCode());
        holder.product.setText(siModel.getProduct());

        holder.batchNum.setText(siModel.getBatchNumber());
        if(siModel.getBatchNumber() == null||siModel.getBatchNumber().equalsIgnoreCase("")){
            holder.batchNum.setText(siModel.getCode());
        }
        holder.expiry.setText(siModel.getExpiryDate());
        if(siModel.getExpiryDate()==null||siModel.getExpiryDate().equalsIgnoreCase("")){
            holder.expiry.setText("01-05-2025");
        }
        holder.stock.setText(siModel.getStock()+"");


        if(payload==null || payload.size()==0) {
            holder.unitprice.setText(siModel.getUnitPrice()+"");
            holder.shelf.setText(siModel.getShelf() + "");
            holder.request.setText(siModel.getRequest() + "");
            holder.returnQty.setText(siModel.getOrder() + "");
            holder.free.setText(siModel.getFree() + "");
            holder.discount.setText(siModel.getDiscountRate() + "");

            holder.lineval.setText(siModel.getLineValue()+"");
        }else{

            for(Object editText :payload) {
                //String val = (String)payload.get(0);
                String val = (String) editText;
                switch (val) {
                    case SHELF:
                        holder.shelf.setText(salesInvoice.get(position).getShelf() + "");
                        break;
                    case REQUEST:
                        holder.request.setText(salesInvoice.get(position).getRequest() + "");
                        break;
                    case RETURN:
                        holder.returnQty.setText(salesInvoice.get(position).getOrder() + "");
                        break;
                    case FREE:
                        holder.free.setText(salesInvoice.get(position).getFree() + "");
                        break;
                    case DISCOUNT:
                        holder.discount.setText(salesInvoice.get(position).getDiscountRate() + "");
                        break;

                    case LINEVAL:
                        holder.lineval.setText(salesInvoice.get(position).getLineValue()+"");
                        break;

                    case UNITPRICE:
                        holder.unitprice.setText(siModel.getUnitPrice()+"");
                        break;


                }
            }

        }

        //ADD TEXT WATCHERS

        holder.unitprice.setOnFocusChangeListener(new FocusChangeListener());
        holder.unitprice.addTextChangedListener(new GenericTextWatcher(){
            public void afterTextChanged(String s ){
                int pos = holder.ref;
                if(!s.toString().equals("")) {

                    salesInvoice.get(pos).setUnitPrice(Double.parseDouble(s+""));

                    //initTable();
                    // if(!onBind)notifyItemChanged(position);


                }else{
                    salesInvoice.get(pos).setUnitPrice(0.0);
                }
                dbAdapter.updateInvoiceData(salesInvoice.get(pos));
                if(!onBind)notifyItemChanged(pos,LINEVAL);
            }
        });
        /*
        holder.unitprice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText editText = (EditText)v;
                String s = editText.getText().toString();
                if(!s.toString().equals("")) {

                    salesInvoice.get(position).setUnitPrice(Double.parseDouble(s+""));

                    //initTable();
                    // if(!onBind)notifyItemChanged(position);


                }else{
                    salesInvoice.get(position).setUnitPrice(0.0);
                }
                dbAdapter.updateInvoiceData(salesInvoice.get(position));
                if(!onBind)notifyItemChanged(position,LINEVAL);

                return false;

            }
        });*/



        holder.shelf.setOnFocusChangeListener(new FocusChangeListener());
        holder.shelf.addTextChangedListener(new GenericTextWatcher(){
            public void afterTextChanged(String s){
                int pos = holder.ref;
                if(!s.toString().equals("")) {

                    salesInvoice.get(pos).setShelf(Integer.parseInt(s+""));

                    //initTable();
                    // if(!onBind)notifyItemChanged(position);

                }else{
                    salesInvoice.get(pos).setShelf(0);
                }
                dbAdapter.updateInvoiceData(salesInvoice.get(pos));
            }
        });
        /*
        holder.shelf.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText editText = (EditText)v;
                String s = editText.getText().toString();
                if(!s.toString().equals("")) {

                    salesInvoice.get(position).setShelf(Integer.parseInt(s+""));

                    //initTable();
                    // if(!onBind)notifyItemChanged(position);

                }else{
                    salesInvoice.get(position).setShelf(0);
                }
                dbAdapter.updateInvoiceData(salesInvoice.get(position));
                return false;
            }
        });*/

        holder.request.setOnFocusChangeListener(new FocusChangeListener());
        holder.request.addTextChangedListener(new GenericTextWatcher(){
            public void afterTextChanged(String s){
                int pos = holder.ref;
                if(!(s.equals(""))){

                    int val = Integer.parseInt(s);
                    salesInvoice.get(pos).setRequest(val);
                    salesInvoice.get(pos).setOrder(val);
                }else{
                    salesInvoice.get(pos).setRequest(0);
                }

                if(!onBind){

                    notifyItemChanged(position, RETURN);
                    notifyItemChanged(position,LINEVAL);
                }
                dbAdapter.updateInvoiceData(salesInvoice.get(position));
                notifyUpdate();

            }
        });
        /*
        holder.request.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(!((EditText)v).getText().toString().equals("")){

                    int val = Integer.parseInt(((EditText)v).getText().toString());
                    salesInvoice.get(position).setRequest(val);
                    salesInvoice.get(position).setOrder(val);
                }else{
                    salesInvoice.get(position).setRequest(0);
                }



                if(!onBind){

                    notifyItemChanged(position, RETURN);
                    notifyItemChanged(position,LINEVAL);
                }
                dbAdapter.updateInvoiceData(salesInvoice.get(position));
                notifyUpdate();

                return false;
            }
        });*/


        holder.returnQty.setOnFocusChangeListener(new FocusChangeListener());
        holder.returnQty.addTextChangedListener(new GenericTextWatcher(){
            public void afterTextChanged(String s){

                int pos = holder.ref;
                if(!(s.toString().equals(""))){

                    int val = Integer.parseInt(s.toString());
                    salesInvoice.get(pos).setOrder(val);//make sure we set returnQty before

                }else{
                    salesInvoice.get(pos).setOrder(0);
                }
                if(!onBind)notifyItemChanged(pos,LINEVAL);
                dbAdapter.updateInvoiceData(salesInvoice.get(pos)); //update the value in the database
                notifyUpdate();
            }
        });
        /*
        holder.returnQty.setOnKeyListener(new View.OnKeyListener() {
            boolean valHasChanged = false;
            boolean freeHasChanged = false;
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                valHasChanged = false;
                freeHasChanged = false;
                EditText editText = (EditText)v;
                String s = editText.getText().toString();
                if(!(s.toString().equals(""))){

                    int val = Integer.parseInt(s.toString());
                    salesInvoice.get(position).setOrder(val);//make sure we set returnQty before

                }else{
                    salesInvoice.get(position).setOrder(0);
                }
                if(!onBind)notifyItemChanged(position,LINEVAL);
                dbAdapter.updateInvoiceData(salesInvoice.get(position)); //update the value in the database
                notifyUpdate();
                return false;
            }
        });*/



        holder.free.setOnFocusChangeListener(new FocusChangeListener());
        holder.free.addTextChangedListener(new GenericTextWatcher(){
            public void afterTextChanged(String s){
                int pos = holder.ref;
                if (!s.toString().equals("")) {

                    int val = Integer.parseInt(s.toString());
                    salesInvoice.get(pos).setFree(val);
                }
                if(!onBind){
                    //notifyItemChanged(position);
                    notifyItemChanged(pos,LINEVAL);

                }
                holder.setCursor(FREE);
                dbAdapter.updateInvoiceData(salesInvoice.get(pos));
                notifyUpdate();
            }
        });
        /*
        holder.free.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText editText = (EditText)v;
                String s = editText.getText().toString();
                if (!s.toString().equals("")) {

                    int val = Integer.parseInt(s.toString());
                    salesInvoice.get(position).setFree(val);
                }
                if(!onBind){
                    //notifyItemChanged(position);
                    notifyItemChanged(position,LINEVAL);

                }
                holder.setCursor(FREE);
                dbAdapter.updateInvoiceData(salesInvoice.get(position));
                notifyUpdate();
                return false;
            }
        });*/

        holder.discount.setOnFocusChangeListener(new FocusChangeListener());
        holder.discount.addTextChangedListener(new GenericTextWatcher(){
            public void afterTextChanged(String s){
                int pos = holder.ref;
                if(!(s.equals(salesInvoice.get(pos).getDiscountRate()+""))){
                    if(!s.toString().equals("")){
                        Double rate = Double.parseDouble(s.toString().trim());

                        salesInvoice.get(pos).setDiscountRate(rate);
                        //notifyItemChanged(position);
                        if(!onBind)notifyItemChanged(pos,LINEVAL);
                        dbAdapter.updateInvoiceData(salesInvoice.get(pos));
                        notifyUpdate();
                    }
                }
            }
        });
        /*
        holder.discount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText editText = (EditText)v;
                String s = editText.getText().toString();
                if(!(s.equals(salesInvoice.get(position).getDiscountRate()+""))){
                    if(!s.toString().equals("")){
                        Double rate = Double.parseDouble(s.toString().trim());

                        salesInvoice.get(position).setDiscountRate(rate);
                        //notifyItemChanged(position);
                        if(!onBind)notifyItemChanged(position,LINEVAL);
                        dbAdapter.updateInvoiceData(salesInvoice.get(position));
                        notifyUpdate();
                    }
                }
                return false;
            }
        });*/


        if(position%2==0){
            holder.setColor(Color.LTGRAY);
        }else{
            holder.setColor(Color.GRAY);
        }

        onBind=false;

    }

    public void onBindViewHolder(MyViewHolder holder, int position){
        onBindViewHolder(holder,position,null);
    }

    @Override
    public int getItemCount(){
        return salesInvoice.size();
    }


    class GenericTextWatcher implements TextWatcher {

        public void afterTextChanged(String s){

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            afterTextChanged(s.toString());
        }
    }

    class DBAdapter extends BaseDBAdapter {

        protected DBAdapter(Context c) {
            super(c);

        }

        public void updateInvoiceData(final SalesInvoiceModel model){
            openDB();


            String sql = "UPDATE temp_return SET SellingPrice="+model.getUnitPrice() +
                    ", Shelf="+model.getShelf()+" , Request="+model.getRequest()
                    +" , returnQty="+model.getOrder()+" , Free="+model.getFree()
                    +" , Disc="+model.getDiscountRate()+" , LineVal="+model.getLineValue()
                    +" WHERE _id="+model.getId();
            db.execSQL(sql);

            closeDB();

        }


    }


    class FocusChangeListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            EditText view = (EditText)v;
            if(hasFocus){
                view.setBackgroundColor(Color.BLACK);
                (view).setTextColor(Color.WHITE);
                if(view.getText().toString().equals("0") || view.getText().toString().equals("0.0")){
                    view.setText("");
                }else{
                    view.setSelection(view.getText().length());
                }
            }else{
                v.setBackgroundColor(Color.TRANSPARENT);
                (view).setTextColor(Color.BLACK);
                if(view.getText().toString().equalsIgnoreCase("") ){
                    view.setText("0");
                }
            }
        }


    }

}
