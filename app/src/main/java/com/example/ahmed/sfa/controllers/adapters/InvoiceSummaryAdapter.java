package com.example.ahmed.sfa.controllers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.models.InvoiceSummary;

/**
 * Created by Shani on 02/11/2017.
 */

public class InvoiceSummaryAdapter extends RecyclerView.Adapter<InvoiceSummaryAdapter.InvoiceSummaryHolder> {

    private InvoiceSummary[] dataset;
    private Context context;
    private static InvoiceHistoryClickListener listener;
    private static final String TAG = "HISTORY";

    public InvoiceSummaryAdapter(Context context, InvoiceSummary[] dataset, InvoiceHistoryClickListener l) {
        this.context = context;
        this.dataset = dataset;
        this.setOnItemClickListener(l);
    }

    class InvoiceSummaryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView iId, iDate, iValue;

        public InvoiceSummaryHolder(View itemView) {
            super(itemView);

            iId = (TextView) itemView.findViewById(R.id.tvInvoiceId);
            iDate = (TextView) itemView.findViewById(R.id.tvInvoiceDate);
            iValue = (TextView) itemView.findViewById(R.id.tvInvoiceValue);

            itemView.setOnClickListener(this);
            //Toast.makeText(context,iId.getText().toString(),Toast.LENGTH_LONG).show();

        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(iId.getText().toString());
            Log.d(TAG, "Inside the onclick-" + iId.getText().toString());
        }
    }

    public void setOnItemClickListener(InvoiceHistoryClickListener ihcListener) {
        listener = ihcListener;
    }

    @Override
    public InvoiceSummaryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_summary_row, parent, false);

        return new InvoiceSummaryHolder(view);
    }

    @Override
    public void onBindViewHolder(InvoiceSummaryHolder holder, int position) {
        holder.iId.setText(dataset[position].getInvoiceId());
        holder.iDate.setText(dataset[position].getInvoiceDate());
        holder.iValue.setText(dataset[position].getInvoiceValue());

    }


    @Override
    public int getItemCount() {
        return dataset.length;
    }

}
