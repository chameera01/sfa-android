package com.example.ahmed.sfa.controllers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.models.PrincipleDiscountModel;

import java.util.List;

/**
 * Created by Ahmed on 10/1/2017.
 */

public class PrincipleDiscountAdapter extends RecyclerView.Adapter<PrincipleDiscountAdapter.MyViewHolder> {
    List<PrincipleDiscountModel> list;
    TextView tv_total_discount;

    public PrincipleDiscountAdapter(List<PrincipleDiscountModel> list,TextView totalTextView){
        this.list = list;
        this.tv_total_discount  = totalTextView;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position, List<Object> payload){
        holder.href = position;
        holder.value.setText(list.get(holder.href).getDisountValue()+"");
        holder.amount.setText(list.get(holder.href).getAmount()+"");
        if(payload.size()==0)holder.discount.setText(list.get(holder.href).getDiscount()+"");
        holder.brand.setText(list.get(holder.href).getPrinciple());


        holder.discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double rate;
                try {
                    rate = Double.parseDouble(s.toString());
                }catch (Exception ex){
                    rate = 0.0;
                }

                list.get(holder.href).setDiscount(rate);
                notifyItemChanged(holder.href,new Object());
            }
        });
        holder.discount.setSelection(holder.discount.getText().length());
        setTotalPrincipleDiscount();
    }

    public void onBindViewHolder( final MyViewHolder holder, int position){
        holder.href = position;
        holder.value.setText(list.get(holder.href).getDisountValue()+"");
        holder.amount.setText(list.get(holder.href).getAmount()+"");
        holder.discount.setText(list.get(holder.href).getDiscount()+"");
        holder.brand.setText(list.get(holder.href).getPrinciple());

        holder.discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double rate;
                try {
                    rate = Double.parseDouble(s.toString());
                }catch (Exception ex){
                    rate = 0.0;
                }

                list.get(holder.href).setDiscount(rate);

                notifyItemChanged(holder.href);

            }
        });

        holder.discount.setSelection(holder.discount.getText().length());
        //showSoftKeyboard(holder.discount);
        setTotalPrincipleDiscount();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.principle_discount_row,parent,false);
        return new MyViewHolder(itemView);
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void setTotalPrincipleDiscount(){
        double totalPrincipleDiscount =0.0;
        for(PrincipleDiscountModel model:list){
            totalPrincipleDiscount+=model.getDisountValue();
        }
        tv_total_discount.setText(totalPrincipleDiscount+"");
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        EditText discount;
        TextView brand,amount,value;
        int href;

        public MyViewHolder(View itemView) {
            super(itemView);

            discount = (EditText)itemView.findViewById(R.id.et_discount);
            brand = (TextView)itemView.findViewById(R.id.tv_brand);
            amount = (TextView)itemView.findViewById(R.id.tv_amount);
            value =(TextView)itemView.findViewById(R.id.tv_discount_value);
        }
    }
}
