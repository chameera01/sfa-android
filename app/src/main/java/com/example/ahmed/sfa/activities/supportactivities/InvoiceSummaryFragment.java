package com.example.ahmed.sfa.activities.supportactivities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.InvoiceHistory;
import com.example.ahmed.sfa.controllers.adapters.InvoiceHistoryClickListener;
import com.example.ahmed.sfa.controllers.adapters.InvoiceSummaryAdapter;
import com.example.ahmed.sfa.models.InvoiceSummary;

import java.util.ArrayList;

public class InvoiceSummaryFragment extends Fragment implements InvoiceHistory.DataUpdateListener, InvoiceHistoryClickListener {

    private InvoiceSummary[] invoiceSummaryArrayList = {new InvoiceSummary("", "No Data Found", "", "", "")};
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView rv;

    InvoiceHistoryClickListener clickListener;

    public InvoiceSummaryFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_invoice_summary, container, false);


        rv = (RecyclerView) rootView.findViewById(R.id.invoiceSummaryRecycler);
        rv.setHasFixedSize(true);
        adapter = new InvoiceSummaryAdapter(getActivity(), invoiceSummaryArrayList, this);
        layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((InvoiceHistory) context).registerDataUpdateListener(this);
        try {
            clickListener = (InvoiceHistoryClickListener) context;
        } catch (ClassCastException e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((InvoiceHistory) getActivity()).unregisterDataUpdateListener(this);
    }

    @Override
    public void onDataUpdate(InvoiceSummary[] array) {

        adapter = new InvoiceSummaryAdapter(getActivity(), array, this);
        rv.setAdapter(adapter);
        Log.d("HISTORY", "Inside onDataUpdate");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onReturnDataUpdate(ArrayList<InvoiceSummary> list) {
        //do nothing
    }

    @Override
    public void onItemClick(String invoiceId) {
        Toast.makeText(getContext(), "Inside the onItemClick frag-" + invoiceId, Toast.LENGTH_LONG).show();
        clickListener.onItemClick(invoiceId);
    }

}
