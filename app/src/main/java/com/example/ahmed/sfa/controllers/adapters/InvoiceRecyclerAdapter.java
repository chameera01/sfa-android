package com.example.ahmed.sfa.controllers.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.SalesInvoiceModel;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 9/9/2017.
 */

public class InvoiceRecyclerAdapter extends RecyclerView.Adapter<InvoiceRecyclerAdapter.MyViewHolder> {

    private List<SalesInvoiceModel> salesInvoice;
    boolean onBind;
    public final String SHELF = "SHLEF",REQUEST="REQUEST",ORDER="ORDER",FREE="FREE",DISCOUNT="DISCOUNT";
    DBAdapter dbAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView code,product,expiry,unitprice,stock,lineval,batchNum;
        EditText shelf,request,order,free,discount;


        View view;
        public MyViewHolder(View view){
            super(view);
            this.view = view;
            dbAdapter = new DBAdapter(view.getContext());
            code = (TextView) view.findViewById(R.id.code_e_si);
            product =(TextView) view.findViewById(R.id.product_e_si);
            batchNum = (TextView) view.findViewById(R.id.batch_e_si);
            expiry = (TextView)view.findViewById(R.id.expiry_e_si);
            unitprice = (TextView) view.findViewById(R.id.unit_price_e_si);
            stock = (TextView)view.findViewById(R.id.stock_e_si);
            lineval = (TextView)view.findViewById(R.id.line_val_e_si);

            shelf = (EditText)view.findViewById(R.id.shelf_e_si);
            request =(EditText)view.findViewById(R.id.request_e_si);
            order = (EditText)view.findViewById(R.id.order_e_si);
            free = (EditText)view.findViewById(R.id.free_e_si);
            discount = (EditText)view.findViewById(R.id.dsc_e_si);


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



    public InvoiceRecyclerAdapter(List<SalesInvoiceModel> list){
        this.salesInvoice = list;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_row,parent,false);

        return new MyViewHolder(itemView);

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
            holder.discount.setText(siModel.getDiscount() + "");
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
                        holder.discount.setText(salesInvoice.get(position).getDiscount() + "");
                        break;
                    default:
                        onBindViewHolder(holder, position);
                        break;
                }
            }

        }

        //ADD TEXT WATCHERS


        holder.shelf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

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


        holder.request.addTextChangedListener(new GenericTextWatcher() {
            boolean valHasChanged = false;
            boolean freeHasChanged = false;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {
                valHasChanged = false;
                freeHasChanged = false;
                if(!s.toString().equals("")){

                    int val = Integer.parseInt(s.toString());
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
                }



                if(!onBind){
                    //List<String> item = new ArrayList<>();
                    //item.add(ORDER);
                    notifyItemChanged(position,ORDER);
                    if(valHasChanged){
                        notifyItemChanged(position,REQUEST);
                        holder.setCursor(REQUEST);
                    }
                    if(freeHasChanged){
                        notifyItemChanged(position,FREE);
                    }
                }
                dbAdapter.updateInvoiceData(salesInvoice.get(position));
            }
        });

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

                }

                if(!onBind){
                    //notifyItemChanged(position);
                    if(valHasChanged){
                        notifyItemChanged(position,ORDER);//notify the adapter that value changed and refresh the view mentioned by the string
                        holder.setCursor(ORDER);
                    }
                    if(freeHasChanged) notifyItemChanged(position,FREE);
                }

                dbAdapter.updateInvoiceData(salesInvoice.get(position)); //update the value in the database


            }

        });

        holder.free.addTextChangedListener(new GenericTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s,int start,int before,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {

                    int val = Integer.parseInt(s.toString());
                    salesInvoice.get(position).setFree(val);
                    if (salesInvoice.get(position).getOrder() + salesInvoice.get(position).getFree() > salesInvoice.get(position).getStock()) {
                        salesInvoice.get(position).setFree(0);

                    }
                    //initTable();
                    //notifyItemChanged(position);
                }
                if(!onBind){
                    notifyItemChanged(position);
                }
                holder.setCursor(FREE);
                dbAdapter.updateInvoiceData(salesInvoice.get(position));
            }
        });


        holder.discount.addTextChangedListener(new GenericTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!(s.equals(salesInvoice.get(position).getDiscountRate()+""))){
                    if(!s.toString().equals("")){
                        salesInvoice.get(position).setDiscountRate(Double.parseDouble(s.toString()));
                        //notifyItemChanged(position);
                    }
                }
                if(!onBind)notifyItemChanged(position);
                dbAdapter.updateInvoiceData(salesInvoice.get(position));
            }
        });

        if(position%2==0){
            holder.setColor(Color.DKGRAY);
        }else{
            holder.setColor(Color.GRAY);
        }

        onBind=false;

    }

    public void onBindViewHolder(MyViewHolder holder,int position){
        onBindViewHolder(holder,position,null);
    }


    public void notifyDataItemChanged(int position ){
        super.notifyDataSetChanged();
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

    class DBAdapter extends BaseDBAdapter{

        protected DBAdapter(Context c) {
            super(c);

        }

        public void updateInvoiceData(SalesInvoiceModel model){
            openDB();

            String sql = "UPDATE temp_invoice SET" +
                    " Shelf="+model.getShelf()+" , Request="+model.getRequest()
                    +" , OrderQty="+model.getOrder()+" , Free="+model.getFree()
                    +" , Disc="+model.getDiscount()+" , LineVal="+model.getLineValue()
                    +" WHERE ";
            db.execSQL(sql);

            closeDB();
        }


    }
}
