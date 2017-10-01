package com.example.ahmed.sfa.service;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.example.ahmed.sfa.Activities.ManualSync;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.Mst_Customermaster;

import java.util.ArrayList;

/**
 * Created by DELL on 9/30/2017.
 */

public class UploadTables {
    Context context;
    
    public UploadTables(Context c){
        this.context = c;
    }
    public  ArrayList<String> mstCustomerMaster() {
        ArrayList<String> cusArr = new ArrayList<>();
        String url_str = "";
        Cursor cusCursor = null;

        Toast.makeText(context, "inside upload customer", Toast.LENGTH_SHORT).show();
        //upload Mst_CustomerMaster data

        try{
//            DBAdapter adp_customermaster = new DBAdapter(context);
            String sqlQry = "SELECT * FROM Mst_Customermaster where uploadStatus = 0";
  //          cusCursor= adp_customermaster.runQuery(sqlQry);

            DBHelper dbConn = new DBHelper(context);
            cusCursor = dbConn.getData(sqlQry);
            Toast.makeText(context, "cursorCount: "+cusCursor.getCount(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "db conn error:"+e.getCause(), Toast.LENGTH_SHORT).show();
        }
        try {
            String cusno,cusname,addrss,dist,area,twn,route,tel="deaultvalue",fax="deaultvalue",email="deaultvalue",ownername="deaultvalue",ownerCon="deaultvalue",regno="deaultvalue",brno="deaultvalue",img = "deaultvalue";
            Double  lat =0.0 ,lng = 0.0;
            int status = 0;

            while (cusCursor.moveToNext()) {



                cusno =  cusCursor.getString(cusCursor.getColumnIndex("CustomerNo"));
                cusname =  cusCursor.getString(cusCursor.getColumnIndex("CustomerName"));
                addrss = cusCursor.getString(cusCursor.getColumnIndex("Address"));
                //String cusno = cusCursor.getString(cusCursor.getColumnIndex("Address"));
                dist = cusCursor.getString(cusCursor.getColumnIndex("DistrictID"));
                area = cusCursor.getString(cusCursor.getColumnIndex("Area"));
                twn = cusCursor.getString(cusCursor.getColumnIndex("Town"));
                route = cusCursor.getString(cusCursor.getColumnIndex("RouteID"));
                status = 486;//this should be integer//cusCursor.getString(cusCursor.getColumnIndex("CustomerStatus"));//CustomerStatusId instead

               /* tel = cusCursor.getString(cusCursor.getColumnIndex("Telephone")) ;
                fax = cusCursor.getString(cusCursor.getColumnIndex("Fax"));
                email = cusCursor.getString(cusCursor.getColumnIndex("Email"));
                ownername = cusCursor.getString(cusCursor.getColumnIndex("OwnerName"));
                ownerCon = cusCursor.getString(cusCursor.getColumnIndex("OwnerContactNo"));
                regno = cusCursor.getString(cusCursor.getColumnIndex("PhamacyRegNo"));
                //String cusno = cusCursor.getString(cusCursor.getColumnIndex("Email"));

                 lat = cusCursor.getDouble(cusCursor.getColumnIndex("Latitude"));
                 lng = cusCursor.getDouble(cusCursor.getColumnIndex("Longitude"));
                 img = cusCursor.getString(cusCursor.getColumnIndex("ImageID"));
                 brno = cusCursor.getString(cusCursor.getColumnIndex("BRno"));*/

                url_str= "http://www.bizmapexpert.com/DIstributorManagementSystem/Up_Tr_CustomerMaster_Pending/Tr_CustomerMaster_PendingNew?" +
                        "RepID=99&TabCustomerNo="+cusno+"&CustomerName="+cusname+"&Address="+addrss+"&DistrictID="+dist+"&" +
                        "Area="+area+"&Town="+twn+"&RouteID="+route+"&CustomerStatusID="+status+"&Telephone="+tel+"&Fax="+fax+"&" +
                        "Email="+email+"&BRno="+brno+"&OwnerName="+ownername+"&OwnerContactNo="+ownerCon+"&PhamacyRegNo="+regno+"&" +
                        "Latitude="+lat+"&longitude="+lng+"&ImageID="+img+"";

                String url ="";
                try{
                    String[] parts = url_str.split(" ");
                    for(String tmp : parts){
                        url += tmp;
                    }
                }catch (Exception e ){

                }

                cusArr.add(url);

                
            }


//            try {
//
//            }catch (Exception e){
//                Toast.makeText(context, "httpgetREq:"+e.getMessage(), Toast.LENGTH_LONG).show();
//            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "errorupload:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        
       return  cusArr;
    }
    
}
