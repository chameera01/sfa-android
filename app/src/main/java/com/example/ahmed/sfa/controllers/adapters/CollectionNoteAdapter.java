package com.example.ahmed.sfa.controllers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.models.CollectionNote;

import java.util.ArrayList;

/**
 * Created by Shani on 19/12/2017.
 */

public class CollectionNoteAdapter extends RecyclerView.Adapter<CollectionNoteAdapter.CollectionNoteHolder> {

    private Context context;
    ArrayList<CollectionNote> al;
    private static int rowcount = 0;

    public CollectionNoteAdapter(Context applicationContext, ArrayList<CollectionNote> al) {
        Log.d("COL", "inside adapter");
        this.context = applicationContext;
        this.al = al;
    }

    @Override
    public CollectionNoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d("COL", "inside onCreateViewHolder");
//        rowcount++;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_collections, parent, false);
        return new CollectionNoteHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectionNoteHolder holder, int position) {

        Log.d("COL", "inside onBindViewHolder");

        holder.invno.setText(al.get(position).getInvoiceNo());
        Log.d("COL", al.get(position).getInvoiceNo());
        holder.crdt.setText(String.valueOf(al.get(position).getCreditAmt()));
        holder.cash.setText(String.valueOf(al.get(position).getCashAmt()));
        holder.cheq.setText(String.valueOf(al.get(position).getChequeAmt()));
        holder.bal.setText(String.valueOf(al.get(position).getBalance()));
        holder.remain.setText(String.valueOf(al.get(position).getRemaining()));
    }

    @Override
    public int getItemCount() {
//        Log.d("COL","inside getItemCount");
        return al.size();
    }

    class CollectionNoteHolder extends RecyclerView.ViewHolder {

        TextView invno, crdt, cash, cheq, bal, remain;

        public CollectionNoteHolder(View itemView) {
            super(itemView);

            invno = (TextView) itemView.findViewById(R.id.tvInvNo);
            crdt = (TextView) itemView.findViewById(R.id.tvCrdtAmt);
            cash = (TextView) itemView.findViewById(R.id.tvCashAmt);
            cheq = (TextView) itemView.findViewById(R.id.tvChequeAmt);
            bal = (TextView) itemView.findViewById(R.id.tvCrdtBalance);
            remain = (TextView) itemView.findViewById(R.id.tvRemaining);
        }


    }
}
