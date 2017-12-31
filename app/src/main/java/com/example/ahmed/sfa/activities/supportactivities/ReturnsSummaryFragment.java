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

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.InvoiceHistory;
import com.example.ahmed.sfa.controllers.adapters.ReturnsSummaryAdapter;
import com.example.ahmed.sfa.models.InvoiceSummary;

import java.util.ArrayList;

public class ReturnsSummaryFragment extends Fragment implements InvoiceHistory.DataUpdateListener {

    private ArrayList<InvoiceSummary> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;

    public ReturnsSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.add(new InvoiceSummary("", "no data found", "", "", ""));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_returns_summary, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.returnsSummaryRecycler);
        recyclerView.setHasFixedSize(true);
        adapter = new ReturnsSummaryAdapter(list, getActivity());
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onDataUpdate(InvoiceSummary[] array) {
        //do nothing
    }

    @Override
    public void onReturnDataUpdate(ArrayList<InvoiceSummary> arrayList) {

        Log.d("HISTORY", "Inside onReturnDataUpdate");
        //call the adapter and pass the dataset returned
        adapter = new ReturnsSummaryAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    //    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((InvoiceHistory) context).registerDataUpdateListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((InvoiceHistory) getActivity()).unregisterDataUpdateListener(this);
    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
