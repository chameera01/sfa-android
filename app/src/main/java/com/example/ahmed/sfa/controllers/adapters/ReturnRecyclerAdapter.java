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

import com.example.ahmed.sfa.Activities.Return;
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

    public final String SHELF = "SHLEF",REQUEST="REQUEST",ORDER="ORDER",FREE="FREE",DISCOUNT="DISCOUNT",
            LINEVAL = "LINEVAL";
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
        TextView code,product,expiry,unitprice,stock,lineval,batchNum;
        EditText shelf,request,order,free,discount;


        View view;
        public MyViewHolder(View view){
            super(view);
            this.view = view;
            dbAdapter = new ReturnRecyclerAdapter.DBAdapter(view.getContext());
            code = (TextView) view.findViewById(R.id.code_e);
            product =(TextView) view.findViewById(R.id.product_e);
            batchNum = (TextView) view.findViewById(R.id.batch_e);
            expiry = (TextView)view.findViewById(R.id.expiry_e);
            unitprice = (TextView) view.findViewById(R.id.unit_price_e);
            stock = (TextView)view.findViewById(R.id.stock_e);
            lineval = (TextView)view.findViewById(R.id.line_val_e);

            shelf = (EditText)view.findViewById(R.id.shelf_e);
            request =(EditText)view.findViewById(R.id.request_e);
            order = (EditText)view.findViewById(R.id.order_e);
            free = (EditText)view.findViewById(R.id.free_e);
            discount = (EditText)view.findViewById(R.id.dsc_e);


        }

        public void setCursor(String view){
            switch (view){
                case SHELF:
                    shelf.setSelection(shelf.getText().length());
                    break;
                case REQUEST:
                    request.setSelection(request.getText().length());
                    break;

                case ORDER:
                    order.setSelection(order.getText().length());
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
                .inflate(R.layout.sales_row,parent,false);

        return new ReturnRecyclerAdapter.MyViewHolder(itemView);

    }

    public void onBindViewHolder(final MyViewHolder holder, final int position, List<Object> payload){
        //onBindViewHolder(holder,position);


        onBind = true;
        SalesInvoiceModel siModel = salesInvoice.get(position);
        holder.code.setText(siModel.getCode());
        holder.product.setText(siModel.getProduct());
        holder.batchNum.setText(siModel.getBatchNumber());
        holder.expiry.setText(siModel.getExpiryDate());
        holder.unitprice.setText(siModel.getUnitPrice()+"");
        holder.stock.setText(siModel.getStock()+"");
        holder.lineval.setText(siModel.getLineValue()+"");

        if(payload==null || payload.size()==0) {
            holder.shelf.setText(siModel.getShelf() + "");
            holder.request.setText(siModel.getRequest() + "");
            holder.order.setText(siModel.getOrder() + "");
            holder.free.setText(siModel.getFree() + "");
            holder.discount.setText(siModel.getDiscountRate() + "");
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
                    case ORDER:
                        holder.order.setText(salesInvoice.get(position).getOrder() + "");
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
                    default:
                        onBindViewHolder(holder, position);
                        break;
                }
            }

        }

        //ADD TEXT WATCHERS


        holder.shelf.setOnFocusChangeListener(new FocusChangeListener());

        holder.shelf.addTextChangedListener(new GenericTextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {




            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {

                    salesInvoice.get(position).setShelf(Integer.parseInt(s+""));
                    dbAdapter.updateInvoiceData(salesInvoice.get(position));
                    //initTable();
                    // if(!onBind)notifyItemChanged(position);

                }

            }


        });

        holder.request.setOnFocusChangeListener(new FocusChangeListener());

        holder.request.setOnKeyListener(new View.OnKeyListener() {
            boolean valHasChanged = false;
            boolean freeHasChanged = false;
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                valHasChanged = false;
                freeHasChanged = false;
                if(!((EditText)v).getText().toString().equals("")){

                    int val = Integer.parseInt(((EditText)v).getText().toString());
                    int stock =salesInvoice.get(position).getStock();
                    if(val>stock){
                        //Toast.makeText(null,"Enter a valid Qty",Toast.LENGTH_SHORT).show();
                        val=stock;
                        valHasChanged = true;
                    }
                    salesInvoice.get(position).setRequest(val);
                    salesInvoice.get(position).setOrder(val);

                    if(salesInvoice.get(position).getFree()+salesInvoice.get(position).getOrder()>salesInvoice.get(position).getStock()){
                        salesInvoice.get(position).setFree(0);
                        freeHasChanged = true;

                    }
                    //initTable();

//                        notifyItemChanged(position,new String[]{ORDER});
                }else{
                    salesInvoice.get(position).setRequest(0);
                }



                if(!onBind){
                    //List<String> item = new ArrayList<>();
                    //item.add(ORDER);
                    notifyItemChanged(position,ORDER);
                    if(valHasChanged){
                        notifyItemChanged(position,REQUEST);
                        //holder.setCursor(REQUEST);
                    }
                    if(freeHasChanged){
                        notifyItemChanged(position,FREE);
                    }
                }
                dbAdapter.updateInvoiceData(salesInvoice.get(position));
                notifyUpdate();

                return false;
            }
        });
        holder.request.addTextChangedListener(new GenericTextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        holder.order.setOnFocusChangeListener(new FocusChangeListener());
        holder.order.addTextChangedListener(new GenericTextWatcher(){
            boolean valHasChanged = false;
            boolean freeHasChanged = false;

            @Override
            public void onTextChanged(CharSequence s,int start,int before,int count){

                //siModel.setOrder(Integer.parseInt(s+""));
            }

            @Override
            public void afterTextChanged(Editable s) {
                valHasChanged = false;
                freeHasChanged = false;
                if(!(s.toString().equals(""))){

                    int val = Integer.parseInt(s.toString());
                    int stock =salesInvoice.get(position).getStock();

                    if(val>stock){
                        //Toast.makeText(null,"Order is more than the stock",Toast.LENGTH_SHORT).show();
                        val=stock;
                        valHasChanged = true;

                    }
                    salesInvoice.get(position).setOrder(val);//make sure we set order before
                    //chekcing the total of order and free against stock

                    if(salesInvoice.get(position).getOrder()+salesInvoice.get(position).getFree()>salesInvoice.get(position).getStock()){
                        salesInvoice.get(position).setFree(0);
                        freeHasChanged = true;
                    }

                    //notifyItemChanged(position);

                }else{
                    salesInvoice.get(position).setOrder(0);
                }

                if(!onBind){
                    //notifyItemChanged(position);
                    if(valHasChanged){
                        notifyItemChanged(position,ORDER);//notify the adapter that value changed and refresh the view mentioned by the string
                        //holder.setCursor(ORDER);
                    }
                    if(freeHasChanged) notifyItemChanged(position,FREE);
                }

                dbAdapter.updateInvoiceData(salesInvoice.get(position)); //update the value in the database
                notifyUpdate();

            }

        });


        holder.free.setOnFocusChangeListener(new FocusChangeListener());
        holder.free.addTextChangedListener(new GenericTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s,int start,int before,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean valChanged = false;
                if (!s.toString().equals("")) {

                    int val = Integer.parseInt(s.toString());
                    salesInvoice.get(position).setFree(val);
                    if (salesInvoice.get(position).getOrder() + salesInvoice.get(position).getFree() > salesInvoice.get(position).getStock()) {
                        salesInvoice.get(position).setFree(0);
                        valChanged = true;
                    }
                    //initTable();
                    //notifyItemChanged(position);
                }
                if(!onBind){
                    //notifyItemChanged(position);
                    notifyItemChanged(position,LINEVAL);
                    if(valChanged)notifyItemChanged(position,FREE);
                }
                holder.setCursor(FREE);
                dbAdapter.updateInvoiceData(salesInvoice.get(position));
                notifyUpdate();
            }
        });


        holder.discount.setOnFocusChangeListener(new FocusChangeListener());
        holder.discount.addTextChangedListener(new GenericTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!(s.equals(salesInvoice.get(position).getDiscountRate()+""))){
                    if(!s.toString().equals("")){
                        Double rate = Double.parseDouble(s.toString().trim());
                        Log.i(" RAte ",rate+"");
                        salesInvoice.get(position).setDiscountRate(rate);
                        //notifyItemChanged(position);
                        if(!onBind)notifyItemChanged(position,LINEVAL);
                        dbAdapter.updateInvoiceData(salesInvoice.get(position));
                        notifyUpdate();
                    }
                }
                //if(!onBind)notifyItemChanged(position);

            }
        });

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

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class DBAdapter extends BaseDBAdapter {

        protected DBAdapter(Context c) {
            super(c);

        }

        public void updateInvoiceData(final SalesInvoiceModel model){
            openDB();


            String sql = "UPDATE temp_return SET" +
                    " Shelf="+model.getShelf()+" , Request="+model.getRequest()
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
            //if(hasFocus){
            if(view.getText().equals("0") || view.getText().equals("0.0")){
                view.setText("");
            }else{
                view.setSelection(view.getText().length());
            }
            //}
        }
    }

}
