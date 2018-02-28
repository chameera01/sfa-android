package com.example.ahmed.sfa.activities.supportactivities;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Dialogs.Alert;
import com.example.ahmed.sfa.activities.ManualSyncFromOtherActivities;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.service.JsonFilter_Send;
import com.example.ahmed.sfa.service.JsonRequestListerner;


public class UploadFragment extends Fragment implements JsonRequestListerner {


    ImageView ivNewCustomer;
    ImageView ivSalesHeaderSync;
    ImageView ivCollections;
    String deviecId;
    String repId;
    LinearLayout app_layer;

    public UploadFragment() {
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
        View view = inflater.inflate(R.layout.fragment_upload, container, false);


        ivNewCustomer = (ImageView) view.findViewById(R.id.iv_New_Customer);
//        ivCollections = (ImageView) view.findViewById(R.id.iv_Collections);
        ivSalesHeaderSync = (ImageView) view.findViewById(R.id.iv_SalesDetails);
//        app_layer = (LinearLayout) view.findViewById(R.id.product_layout);


        setListeners();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getRepAndDeviceId();
            }
        }, 1000);

        // Inflate the layout for this fragment
        return view;
    }

    public void getRepAndDeviceId() {
        try {
            Log.d("MGT", "inside getRepID");
            DBAdapter dbAdapter = new DBAdapter(UploadFragment.this.getActivity());
            Log.d("MGT", dbAdapter.toString());
            dbAdapter.openDB();
            Cursor deviceCursor = dbAdapter.runQuery("select * from DeviceCheckController where ACTIVESTATUS = 'YES'");
            Log.d("MGT", deviceCursor.toString());
            Cursor repCursor = null;

            if (deviceCursor.getCount() == 0) {
                Log.d("MGT", "dcursor is empty");
            } else {
                Log.d("MGT", "dcursor is not empty");
            }

            if (deviceCursor.moveToFirst()) {
                Log.d("MGT", "cursor moved to first_device");
                deviecId = deviceCursor.getString(deviceCursor.getColumnIndex("DeviceID"));
                Log.d("MGT", "Inside getRepId_" + deviecId);
                deviceCursor.moveToNext();
                Log.d("MGT", "cursor moved to next_device");
            }

            repCursor = dbAdapter.runQuery("select * from Mst_RepTable");

            if (repCursor.getCount() == 0) {
                Log.d("MGT", "rcursor is empty");
            } else {
                Log.d("MGT", "rcursor is not empty");
                Log.d("MGT", String.valueOf(repCursor.getCount()));
            }

            if (repCursor.moveToFirst()) {
                Log.d("MGT", "cursor moved to first_rep");
                repId = repCursor.getString(repCursor.getColumnIndex("RepID"));
                Log.d("MGT", "Inside getRepId_" + repId);
                repCursor.moveToNext();
                Log.d("MGT", "cursor moved to next_rep");
            }

            dbAdapter.closeDB();
        } catch (Exception e) {
            String msg = (e.getMessage() == null) ? "getrepid failed!" : e.getMessage();
            Log.e("data:", msg);
        }

    }

    public void setListeners() {


        /**Upload New Customer**/
        ivNewCustomer.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                Toast.makeText(UploadFragment.this.getActivity(), "Connecting...", Toast.LENGTH_SHORT).show();
                ManualSyncFromOtherActivities msfoa = new ManualSyncFromOtherActivities(UploadFragment.this.getActivity());
                if (msfoa.uploadNewCustomers(repId)) {
                    Alert alert = new Alert(UploadFragment.this.getActivity());
                    alert.showAlert("Success", "Updated successfully!", null, null);
                } else {
                    Alert alert = new Alert(UploadFragment.this.getActivity());
                    alert.showAlert("Error", "Failed to update!", null, null);
                }

            }
        });

        /*FRANK Jayawardana */
        ivSalesHeaderSync.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                ManualSyncFromOtherActivities msfa = new ManualSyncFromOtherActivities(UploadFragment.this.getActivity());
                if (msfa.uploadSalesDetails(repId)) {
                    Alert alert = new Alert(UploadFragment.this.getActivity());
                    alert.showAlert("Success", "Updated successfully!", null, null);
                } else {
                    Alert alert = new Alert(UploadFragment.this.getActivity());
                    alert.showAlert("Error", "Failed to update!", null, null);
                }

            }
        });
        /*FRANK Jayawardana */
    }

    @Override
    public void receiveData(String result, String filter) {
//        Toast.makeText(this, "came inside recieve data", Toast.LENGTH_LONG).show();
        if (result != null) {
            String josnString = result;
//            Toast.makeText(this, "result:" + josnString, Toast.LENGTH_LONG).show();
            try {
                JsonFilter_Send josnFilter = new JsonFilter_Send(UploadFragment.this.getActivity().getApplicationContext());
                josnFilter.filterJsonData(josnString, filter);
//                Alert alert = new Alert(UploadFragment.this.getActivity());
//                alert.showAlert("Success", "Updated successfully!", null, null);

            } catch (Exception e) {
//                Alert alert = new Alert(UploadFragment.this.getActivity());
//                alert.showAlert("Error", "Failed to update!", null, null);

            }
        } else {
//            Alert alert = new Alert(UploadFragment.this.getActivity());
//            alert.showAlert("Error", "Failed to update!", null, null);

        }
//        Toast.makeText(UploadFragment.this.getActivity(), "method_complete", Toast.LENGTH_LONG).show();
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }


}
