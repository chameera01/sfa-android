package com.example.ahmed.sfa.service;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.controllers.database.DBHelper;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by DELL on 9/30/2017.
 */

public class UploadTables {
    Context context;

    public UploadTables(Context c) {
        this.context = c;
    }

    public ArrayList<String> mstCustomerMaster(String repId) {
        ArrayList<String> cusArr = new ArrayList<>();
        String url_str = "";
        Cursor cusCursor = null;

        Toast.makeText(context, "inside upload customer", Toast.LENGTH_SHORT).show();


        try {

            String sqlQry = "select   Tr_NewCustomer.NewCustomerID as TabCustomerNo, CustomerName, Address, Mst_District.DistrictId as DistrictID, area, town, RouteID, CustomerStatusID, Telephone, Fax, Email, bRno, OwnerName, OwnerContactNo, PharmacyRegNo, Latitude, longitude, ImageID from  Tr_NewCustomer inner join Mst_District on Tr_NewCustomer.District=Mst_District.DistrictName  where isUpload = 0";

            DBHelper dbConn = new DBHelper(context);
            cusCursor = dbConn.getData(sqlQry);
            Toast.makeText(context, "cursorCount: " + cusCursor.getCount(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "db conn error:" + e.getCause(), Toast.LENGTH_LONG).show();
        }
        try {
            String TabCustomerNo, CustomerName, DistrictID, Address, Area, Town, RouteID, Telephone, Fax, Email, OwnerName, OwnerContactNo, PhamacyRegNo, BRno, ImageID;
            Double Latitude, longitude;
            int dist = 0, CustomerStatusID = 0;

            if (cusCursor.getCount() != 0) {
                for (cusCursor.moveToFirst(); !cusCursor.isAfterLast(); cusCursor.moveToNext()) {
                    Log.d("CUSTOMER", "inside upload tables_cursor moving");

                    TabCustomerNo = cusCursor.getString(cusCursor.getColumnIndex("TabCustomerNo"));
                    CustomerName = cusCursor.getString(cusCursor.getColumnIndex("CustomerName"));
                    Address = cusCursor.getString(cusCursor.getColumnIndex("Address"));
                    DistrictID = cusCursor.getString(cusCursor.getColumnIndex("DistrictID"));


                    Area = cusCursor.getString(cusCursor.getColumnIndex("Area"));
                    Town = cusCursor.getString(cusCursor.getColumnIndex("Town"));
                    RouteID = cusCursor.getString(cusCursor.getColumnIndex("RouteID"));
                    CustomerStatusID = cusCursor.getInt(cusCursor.getColumnIndex("CustomerStatusID"));//CustomerStatusId instead
                    Telephone = cusCursor.getString(cusCursor.getColumnIndex("Telephone"));

                    Fax = cusCursor.getString(cusCursor.getColumnIndex("Fax"));
                    Email = cusCursor.getString(cusCursor.getColumnIndex("Email"));
                    OwnerName = cusCursor.getString(cusCursor.getColumnIndex("OwnerName"));
                    OwnerContactNo = cusCursor.getString(cusCursor.getColumnIndex("OwnerContactNo"));
                    // PhamacyRegNo = cusCursor.getString(cusCursor.getColumnIndex("PharmacyRegNo"));
                    PhamacyRegNo = cusCursor.getString(cusCursor.getColumnIndex("PharmacyRegNo"));
                    //String cusno = cusCursor.getString(cusCursor.getColumnIndex("Email"));

                    Latitude = cusCursor.getDouble(cusCursor.getColumnIndex("Latitude"));
                    longitude = cusCursor.getDouble(cusCursor.getColumnIndex("Longitude"));
                    ImageID = cusCursor.getString(cusCursor.getColumnIndex("ImageID"));
                    BRno = cusCursor.getString(cusCursor.getColumnIndex("BRno"));

                    url_str = Constants.BASE_URL + "DIstributorManagementSystem/Up_Tr_CustomerMaster_Pending/Tr_CustomerMaster_PendingNew?" +
                            "RepID=93&TabCustomerNo=" + TabCustomerNo + "&CustomerName=" + CustomerName + "&Address=" + Address + "&DistrictID=" + DistrictID + "&" +
                            "Area=" + Area + "&Town=" + Town + "&RouteID=" + RouteID + "&CustomerStatusID=" + CustomerStatusID + "&Telephone=" + Telephone + "&Fax=" + Fax + "&" +
                            "Email=" + Email + "&BRno=" + BRno + "&OwnerName=" + OwnerName + "&OwnerContactNo=" + OwnerContactNo + "&PhamacyRegNo=" + PhamacyRegNo + "&" +
                            "Latitude=" + Latitude + "&longitude=" + longitude + "&ImageID=" + ImageID + "";

                    Log.d("CUSTOMER", url_str);
                    String url = "";

                    try {
                        URI uri = new URI(url_str.replace(" ", "$$"));
                        {
                            url += uri;
                        }
                    } catch (Exception e) {
                        Log.d("SYNC", e.getMessage());
                    }

                    cusArr.add(url);


                }
            } else {
                Toast.makeText(context, "cursor error: cursor empty", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "errorupload:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return cusArr;
    }

}
