package com.example.ahmed.sfa.activities.supportactivities;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Dialogs.Alert;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.service.JsonFilter_Send;
import com.example.ahmed.sfa.service.JsonObjGenerate;
import com.example.ahmed.sfa.service.JsonRequestListerner;
import com.example.ahmed.sfa.service.SyncReturn;

public class DownloadFragment extends Fragment implements JsonRequestListerner {

    private static final String TAG = "COL";
    ImageView ivProductSync, ivrepSync, ivSupplierSync, ivProductBradnSync, ivCustomerStatusSync, ivDistrictSync, ivTerritorySync, ivRouteSync, ivReasonSync;
    ImageView ivchechInOutPointsSync, ivCustomerSync, ivNewCustomer, ivTabStockSync, ivSalesHeaderSync, ivCollections, ivAudit, ivDRDetails;
    String deviecId;
    String repId;
    ImageView ivItineraryDeatildSync;
    Boolean check = false;

    public DownloadFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_download, container, false);

        ivProductSync = (ImageView) view.findViewById(R.id.iv_productSync);
        ivrepSync = (ImageView) view.findViewById(R.id.iv_repSync);
        ivSupplierSync = (ImageView) view.findViewById(R.id.iv_SupplierSync);
        ivProductBradnSync = (ImageView) view.findViewById(R.id.iv_ProductBradnSync);
        ivCustomerStatusSync = (ImageView) view.findViewById(R.id.iv_CustomerStatusSync);
        ivDistrictSync = (ImageView) view.findViewById(R.id.iv_DistrictSync);
        ivTerritorySync = (ImageView) view.findViewById(R.id.iv_TerritorySync);
        ivRouteSync = (ImageView) view.findViewById(R.id.iv_RouteSync);
        ivReasonSync = (ImageView) view.findViewById(R.id.iv_ReasonSync);
        ivchechInOutPointsSync = (ImageView) view.findViewById(R.id.iv_chechInOutPointsSync);
        ivCustomerSync = (ImageView) view.findViewById(R.id.iv_CustomerSync);
        ivTabStockSync = (ImageView) view.findViewById(R.id.iv_StockSync);
        ivItineraryDeatildSync = (ImageView) view.findViewById(R.id.iv_ItinerarySync);
        ivNewCustomer = (ImageView) view.findViewById(R.id.iv_New_Customer);
        ivCollections = (ImageView) view.findViewById(R.id.iv_Collections);

        ivSalesHeaderSync = (ImageView) view.findViewById(R.id.iv_SalesDetails);
        ivAudit = (ImageView) view.findViewById(R.id.iv_audit);
        ivDRDetails = (ImageView) view.findViewById(R.id.iv_drDetails);

        setListeners();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getRepAndDeviceId();
            }
        }, 1000);
        Toast.makeText(DownloadFragment.this.getActivity(), DownloadFragment.this.getActivity().getLocalClassName(), Toast.LENGTH_LONG).show();


        // Inflate the layout for this fragment
        return view;
    }

    public void getRepAndDeviceId() {
        try {
            Log.d("MGT", "inside getRepID");
            DBAdapter dbAdapter = new DBAdapter(DownloadFragment.this.getActivity());
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

        ivDRDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting..", Toast.LENGTH_SHORT).show();
                try {
                    Log.d(TAG, "inside onclick");
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/Mst_deviceRepDetails/GetdeviceRepDetails?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    Log.d(TAG, "inside onclick after jobjgen");
                    jObjGen.setFilterType("DeviceRepDetails");
                    Log.d(TAG, "inside onclick after setting filter type");
                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });

        ivCollections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting..", Toast.LENGTH_SHORT).show();
                try {
                    Log.d(TAG, "inside onclick");
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/Tr_OutstandingForCollections/Tr_OutstandingForCollections?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    Log.d(TAG, "inside onclick after jobjgen");
                    jObjGen.setFilterType("Collections");
                    Log.d(TAG, "inside onclick after setting filter type");
                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });


        ivProductSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting..", Toast.LENGTH_SHORT).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/productdetails/SelectProductDetails?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    jObjGen.setFilterType("productdetails");
                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });
        /**Sync_Rep**/
        ivrepSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting..", Toast.LENGTH_SHORT).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/GetRepDetails/SelectGetRepDetails?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    jObjGen.setFilterType("RepDetails");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);
//

                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });

        /**Synchronize Supplier Details**/
        ivSupplierSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting..", Toast.LENGTH_SHORT).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/Mst_SupplierTable/SelectProductMst_SupplierTable?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    jObjGen.setFilterType("SupplierTable");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });

        /**Synchronize Product Brand Details**/
        ivProductBradnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting..", Toast.LENGTH_SHORT).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/ProductBrandManagement/SelectProductBrandManagement?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    jObjGen.setFilterType("ProductBrandManagement");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);


                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });

        /**Synchronize Customer Status**/
        ivCustomerStatusSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting..", Toast.LENGTH_SHORT).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/Mst_CustomerStatus/SelectMst_CustomerStatus?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    jObjGen.setFilterType("CustomerStatus");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);


                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });
        /**Sync District Details**/
        ivDistrictSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting...", Toast.LENGTH_SHORT).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/Mst_District/SelectMst_District?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    jObjGen.setFilterType("district");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);


                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });
        /**Sync territory**/
        ivTerritorySync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting...", Toast.LENGTH_SHORT).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/Mst_Territory/SelectMst_Territory?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    jObjGen.setFilterType("territory");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });
        /**Sync routes**/
        ivRouteSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting...", Toast.LENGTH_SHORT).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/Mst_Route/SelectMst_Route?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    jObjGen.setFilterType("route");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);


                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });
        /**Sync Reasons**/
        ivReasonSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting...", Toast.LENGTH_SHORT).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/Mst_Reasons/SelectMst_Reasons?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    jObjGen.setFilterType("Reason");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);


                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });
        /**Sync checkInOutPoints**/
        ivchechInOutPointsSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting...", Toast.LENGTH_SHORT).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/Mst_CheckInOutPoints/SelectMst_CheckInOutPoints?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    jObjGen.setFilterType("CheckInOutPoints");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);


                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });

        /*sync Customer*/
        ivCustomerSync.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting...", Toast.LENGTH_SHORT).show();
                //download customer master data
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/Mst_Customermaster/SelectMst_Customermaster?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    jObjGen.setFilterType("Customer");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);


                } catch (Exception e) {

                }

            }
        });
        /*sync tabStock*/
        ivTabStockSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting...", Toast.LENGTH_SHORT).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/D_Tr_RepStock/GetRepStock?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    jObjGen.setFilterType("TabStock");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);


                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });

        //download ItinerayDetails
        ivItineraryDeatildSync.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting...", Toast.LENGTH_SHORT).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate(Constants.BASE_URL + "DIstributorManagementSystem/Tr_ItineraryDetails/GetItineraryDetails?DeviceID=" + deviecId + "&RepID=" + repId + "", (JsonRequestListerner) DownloadFragment.this);
                    jObjGen.setFilterType("ItineraryDetails");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);


                } catch (Exception e) {

                }
            }
        });


        ivAudit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(DownloadFragment.this.getActivity(), "Connecting...", Toast.LENGTH_SHORT).show();
                DBAdapter dbAdapter = new DBAdapter(DownloadFragment.this.getActivity());
                if (dbAdapter.updateaudit()) {
                    Alert alert = new Alert(DownloadFragment.this.getActivity());
                    alert.showAlert("Success", "Updated successfully!", null, null);
                } else {
                    Alert alert = new Alert(DownloadFragment.this.getActivity());
                    alert.showAlert("Error", "Failed to update!", null, null);
                }

            }
        });

    }


    @Override
    public void receiveData(String result, String filter) {
        Toast.makeText(DownloadFragment.this.getActivity(), "came inside recieve data", Toast.LENGTH_LONG).show();
        Log.d(TAG, "inside receive data ");
        if (result != null) {
            String josnString = result;
            Log.d(TAG, "inside receive data: json string:  " + josnString);
            Toast.makeText(DownloadFragment.this.getActivity(), "result:" + josnString, Toast.LENGTH_LONG).show();

            JsonFilter_Send josnFilter = new JsonFilter_Send(DownloadFragment.this.getActivity());
            if (josnFilter.filterJsonData(josnString, filter)) {
                setAlert(true);
            } else {
                setAlert(false);
            }

        } else {
            Toast.makeText(DownloadFragment.this.getActivity(), "result null", Toast.LENGTH_LONG).show();
            setAlert(false);

        }
        Toast.makeText(DownloadFragment.this.getActivity(), "method_complete", Toast.LENGTH_SHORT).show();
    }

    private void setAlert(Boolean check) {

        if (check) {
            Alert alert = new Alert(DownloadFragment.this.getActivity());
            alert.showAlert("Success", "Updated successfully!", null, null);
        } else {
            Alert alert = new Alert(DownloadFragment.this.getActivity());
            alert.showAlert("Error", "Failed to update!", null, null);
        }

    }


}
