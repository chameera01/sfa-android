package com.example.ahmed.sfa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.RowViewRecyclerAdapter;
import com.example.ahmed.sfa.models.InvoiceSummaryFromSalesDetails;
import com.example.ahmed.sfa.models.InvoiceSummaryFromSalesHeader;

import java.util.ArrayList;

public class SingleInvoiceHistoryDisplay extends AppCompatActivity {

    private ArrayList<InvoiceSummaryFromSalesDetails> salesDetails = new ArrayList<>();
    private InvoiceSummaryFromSalesHeader salesHeader;
    private InvoiceSummaryFromSalesHeader sh;
    private InvoiceSummaryFromSalesDetails sd;
    private TextView subtot, invtot, disc, free, credit, cash, cheque;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String discRate;

    private static final String TAG = "HISTORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_invoice_history_display);

        salesDetails = this.getIntent().getParcelableArrayListExtra("SalesDetails");
        salesHeader = this.getIntent().getParcelableExtra("SalesHeader");

        Log.d(TAG, salesDetails.toString());
        Log.d(TAG, salesHeader.toString());

        //details from sales header
        subtot = (TextView) findViewById(R.id.tvSubtotal);
        invtot = (TextView) findViewById(R.id.tvTotal);
        disc = (TextView) findViewById(R.id.tvDisc);
        free = (TextView) findViewById(R.id.tvFreeQty);
        credit = (TextView) findViewById(R.id.tvCredit);
        cash = (TextView) findViewById(R.id.tvCash);
        cheque = (TextView) findViewById(R.id.tvCheque);

        discRate = salesHeader.getDiscountRate();

        setInvoiceDetails(salesHeader);

        Log.d(TAG, subtot + "_" + invtot + "_" + disc + "_" + cash);

        //get a recyclerview initialized and then the adapter and get the items into it.
        recyclerView = (RecyclerView) findViewById(R.id.rowViewRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new RowViewRecyclerAdapter(salesDetails, discRate, getApplicationContext());
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        Button btnPrint = (Button) findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initiate the print method
            }
        });

    }

    private void setInvoiceDetails(InvoiceSummaryFromSalesHeader salesHeader) {

        subtot.setText(salesHeader.getSubTotal());
        invtot.setText(salesHeader.getInvoiceTotal()); //should check with the database. qty or value?
        disc.setText(salesHeader.getDiscount());
        free.setText(salesHeader.getFreeQty());
        credit.setText(salesHeader.getCredit());
        cash.setText(salesHeader.getCash());
        cheque.setText(salesHeader.getCheque());
    }
}
