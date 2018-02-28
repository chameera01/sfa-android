package com.example.ahmed.sfa.controllers.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Dialogs.Alert;
import com.example.ahmed.sfa.activities.InvCollections;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.CollectionNote;

import java.util.ArrayList;

/**
 * Created by Shani on 19/12/2017.
 */

public class CollectionNoteAdapter extends RecyclerView.Adapter<CollectionNoteAdapter.CollectionNoteHolder> {

    private static final String TAG = "COL";
    private static int rowcount = 0;
    ArrayList<CollectionNote> al;
    DialogInterface.OnClickListener dismissListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
    private Context context;
    private com.example.ahmed.sfa.activities.Dialogs.Alert alert;
    private int ref;
    private DBHelper helper;
    private DBAdapter dbAdapter;
    private String invoice, type;
    private double cheque;
    private DialogInterface.OnClickListener removeListener;

    public CollectionNoteAdapter(Context applicationContext, ArrayList<CollectionNote> al) {
        Log.d("COL", "inside adapter");
        this.context = applicationContext;
        this.al = al;
        this.alert = new Alert((Activity) applicationContext);
        helper = new DBHelper(context);
        dbAdapter = new DBAdapter(context);
    }

    @Override
    public CollectionNoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d("COL", "inside onCreateViewHolder");
//        rowcount++;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_collections, parent, false);
        return new CollectionNoteHolder(view);
    }

    @Override
    public void onBindViewHolder(final CollectionNoteHolder holder, final int position) {

        Log.d("COL", "inside onBindViewHolder");

//        ref = holder.getAdapterPosition();
        ref = holder.getAdapterPosition();

        invoice = al.get(position).getInvoiceNo();
        holder.invno.setText(invoice);
        Log.d("COL", al.get(position).getInvoiceNo());
        holder.crdt.setText(String.valueOf(al.get(position).getCreditAmt()));
        holder.cash.setText(String.valueOf(al.get(position).getCashAmt()));

        cheque = al.get(position).getChequeAmt();
        holder.cheq.setText(String.valueOf(cheque));
        holder.bal.setText(String.valueOf(al.get(position).getBalance()));
        holder.remain.setText(String.valueOf(al.get(position).getRemaining()));

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("COL", "ref: " + ref);
                Log.d("COL", "getAdapterPosition(): " + holder.getAdapterPosition());
                ref = holder.getAdapterPosition();
                alert.showAlert("Remove item", "Are you sure?", "Yes", "No", removeListener, dismissListener);
            }
        });

        type = "";
        if (al.get(ref).getCashAmt() != 0.0 && al.get(ref).getChequeAmt() == 0.0) {
            type = "Cash";
        } else if (al.get(ref).getChequeAmt() != 0.0 && al.get(ref).getCashAmt() == 0.0) {
            type = "Cheque";
        } else if (al.get(ref).getChequeAmt() != 0.0 && al.get(ref).getCashAmt() != 0.0) {
            type = "Cash and Cheque";
        }

        removeListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    InvCollections ic = new InvCollections();
//                    InvCollections.lastSelectedCustomer = InvCollections.customers.getSelectedItem().toString();
                    double d = 0.0;
                    Log.d(TAG, "list size before removing: " + getItemCount());
                    if (type.equals("Cash") || type.equals("Cash and Cheque")) {
                        if (dbAdapter.setLastRemaining(InvCollections.customers.getSelectedItem().toString(), al.get(ref).getCashAmt())) {
                            Log.d(TAG, "lastInvoiceRemaining: " + al.get(ref).getCashAmt());
                        }
//                        ic.setGrid(type);

                    } else {
                        if (dbAdapter.setLastRemaining(InvCollections.customers.getSelectedItem().toString(), al.get(ref).getChequeAmt())) {
                            Log.d(TAG, "lastInvoiceRemaining: " + al.get(ref).getChequeAmt());
                        }
//                        ic.setGrid(type);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "ex updating remaining: " + e.getMessage());
                }

                try {
                    removeItem(ref);
                    InvCollections.cash.setEnabled(true);
                    InvCollections.popup.setEnabled(true);
                    Log.d(TAG, "list size after removing one: " + getItemCount());
                } catch (Exception e) {
                    Log.d(TAG, "ex remove item: " + e.getLocalizedMessage());
                }

                try {
                    if (helper.removeCNPayment(context, invoice, type)) {
                        InvCollections.chequeTotal = InvCollections.chequeTotal - cheque;
                        Log.d("COL", "list size: " + al.size());
                        Toast.makeText(context, "Deleted from DB", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Failed to delete from DB", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.d("COL", "catch list size: " + al.size());
                    Log.d("COL", "Ex: " + e.getLocalizedMessage());
                    Toast.makeText(context, "Caught error: Failed to delete from DB", Toast.LENGTH_LONG).show();
                }

                if (getItemCount() == 0) {
                    InvCollections.cash.setEnabled(true);
                    InvCollections.popup.setEnabled(true);
                }

            }
        };

    }

    private void removeItem(int position) {
        Log.d(TAG, "inside remove-pos: " + position);
        Log.d(TAG, "count: " + getItemCount());
        al.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        Log.d(TAG, "count after: " + getItemCount());
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class CollectionNoteHolder extends RecyclerView.ViewHolder {

        TextView invno, crdt, cash, cheq, bal, remain;
        Button remove;

        public CollectionNoteHolder(View itemView) {
            super(itemView);

            invno = (TextView) itemView.findViewById(R.id.tvInvNo);
            crdt = (TextView) itemView.findViewById(R.id.tvCrdtAmt);
            cash = (TextView) itemView.findViewById(R.id.tvCashAmt);
            cheq = (TextView) itemView.findViewById(R.id.tvChequeAmt);
            bal = (TextView) itemView.findViewById(R.id.tvCrdtBalance);
            remain = (TextView) itemView.findViewById(R.id.tvRemaining);
            remove = (Button) itemView.findViewById(R.id.removeItem);


        }


    }
}
