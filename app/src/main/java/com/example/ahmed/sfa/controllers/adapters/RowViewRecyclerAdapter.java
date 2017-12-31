package com.example.ahmed.sfa.controllers.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.models.InvoiceSummaryFromSalesDetails;

import java.util.ArrayList;

/**
 * Created by Shani on 12/11/2017.
 */

public class RowViewRecyclerAdapter extends RecyclerView.Adapter<RowViewRecyclerAdapter.RowViewHolder> {

    private ArrayList<InvoiceSummaryFromSalesDetails> isfSalesDetails;
    private String discountRate;
    private Context context;
    private static int rowcount = 0;

    private static final String TAG = "HISTORY";


    public RowViewRecyclerAdapter(ArrayList<InvoiceSummaryFromSalesDetails> isfSalesDetails, String discountRate, Context context) {
        this.isfSalesDetails = isfSalesDetails;
        this.discountRate = discountRate;
        this.context = context;

        Log.d(TAG, isfSalesDetails.toString());
//        Log.d(TAG,discountRate);
    }

    @Override
    public RowViewRecyclerAdapter.RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        rowcount++;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview, parent, false);
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RowViewRecyclerAdapter.RowViewHolder holder, int position) {

        holder.prodCode.setText(isfSalesDetails.get(position).getCode());
        holder.productDescr.setText(isfSalesDetails.get(position).getProduct());
        holder.batchNo.setText(isfSalesDetails.get(position).getBatchNo());
        holder.unitPrice.setText(isfSalesDetails.get(position).getUnitPrice());
        //stock missing
        holder.shelf.setText(isfSalesDetails.get(position).getShelf());
        holder.request.setText(isfSalesDetails.get(position).getRequest());
        holder.order.setText(isfSalesDetails.get(position).getOrder());
        holder.free.setText(isfSalesDetails.get(position).getFree());
        holder.discRate.setText(discountRate);
        holder.lineVal.setText(isfSalesDetails.get(position).getLineValue());

        Log.d(TAG, holder.prodCode.getText().toString());
        Log.d(TAG, holder.lineVal.getText().toString());

        if (rowcount % 2 == 0) {
            holder.prodCode.setBackgroundColor(Color.LTGRAY);
            holder.productDescr.setBackgroundColor(Color.LTGRAY);
            holder.batchNo.setBackgroundColor(Color.LTGRAY);
            holder.unitPrice.setBackgroundColor(Color.LTGRAY);
            //stock missing
            holder.shelf.setBackgroundColor(Color.LTGRAY);
            holder.request.setBackgroundColor(Color.LTGRAY);
            holder.order.setBackgroundColor(Color.LTGRAY);
            holder.free.setBackgroundColor(Color.LTGRAY);
            holder.discRate.setBackgroundColor(Color.LTGRAY);
            holder.lineVal.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.prodCode.setBackgroundColor(Color.WHITE);
            holder.productDescr.setBackgroundColor(Color.WHITE);
            holder.batchNo.setBackgroundColor(Color.WHITE);
            holder.unitPrice.setBackgroundColor(Color.WHITE);
            //stock missing
            holder.shelf.setBackgroundColor(Color.WHITE);
            holder.request.setBackgroundColor(Color.WHITE);
            holder.order.setBackgroundColor(Color.WHITE);
            holder.free.setBackgroundColor(Color.WHITE);
            holder.discRate.setBackgroundColor(Color.WHITE);
            holder.lineVal.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, String.valueOf(isfSalesDetails.size()));
        return isfSalesDetails.size();
    }


    class RowViewHolder extends RecyclerView.ViewHolder {

        TextView prodCode, productDescr, batchNo, unitPrice, stock, shelf, request, order, free, discRate, lineVal;

        public RowViewHolder(View itemView) {
            super(itemView);

            prodCode = (TextView) itemView.findViewById(R.id.tvRowCode);
            productDescr = (TextView) itemView.findViewById(R.id.tvRowProduct);
            batchNo = (TextView) itemView.findViewById(R.id.tvRowBatchNo);
            unitPrice = (TextView) itemView.findViewById(R.id.tvRowUnitPrice);
            stock = (TextView) itemView.findViewById(R.id.tvRowStock);
            shelf = (TextView) itemView.findViewById(R.id.tvRowShelf);
            request = (TextView) itemView.findViewById(R.id.tvRowRequest);
            order = (TextView) itemView.findViewById(R.id.tvRowOrder);
            free = (TextView) itemView.findViewById(R.id.tvRowFree);
            discRate = (TextView) itemView.findViewById(R.id.tvRowDiscRate);
            lineVal = (TextView) itemView.findViewById(R.id.tvRowLineValue);
        }
    }


}
