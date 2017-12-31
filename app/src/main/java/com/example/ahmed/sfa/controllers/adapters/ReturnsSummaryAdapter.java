package com.example.ahmed.sfa.controllers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.models.InvoiceSummary;

import java.util.ArrayList;

/**
 * Created by Shani on 13/11/2017.
 */

public class ReturnsSummaryAdapter extends RecyclerView.Adapter<ReturnsSummaryAdapter.ReturnsSummaryHolder> {

    private ArrayList<InvoiceSummary> dataset;
    private Context context;

    private static final String TAG = "HISTORY";

    public ReturnsSummaryAdapter(ArrayList<InvoiceSummary> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    class ReturnsSummaryHolder extends RecyclerView.ViewHolder {

        TextView rId, rDate, rValue;

        public ReturnsSummaryHolder(View itemView) {
            super(itemView);

            rId = (TextView) itemView.findViewById(R.id.tvReturnNo);
            rDate = (TextView) itemView.findViewById(R.id.tvReturnDate);
            rValue = (TextView) itemView.findViewById(R.id.tvReturnValue);
        }
    }

    @Override
    public ReturnsSummaryHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.returns_summary_row, parent, false);
        return new ReturnsSummaryHolder(root);
    }

    @Override
    public void onBindViewHolder(ReturnsSummaryHolder holder, int position) {

        holder.rId.setText(dataset.get(position).getReturnNo());
        holder.rDate.setText(dataset.get(position).getInvoiceDate());
        holder.rValue.setText(dataset.get(position).getReturnValue());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
