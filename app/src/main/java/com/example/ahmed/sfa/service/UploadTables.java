package com.example.ahmed.sfa.service;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.example.ahmed.sfa.controllers.database.DBHelper;

import java.net.URI;
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
            String sqlQry = "SELECT * FROM Tr_NewCustomer where isUpload = 0";
  //          cusCursor= adp_customermaster.runQuery(sqlQry);

            DBHelper dbConn = new DBHelper(context);
            cusCursor = dbConn.getData(sqlQry);
            Toast.makeText(context, "cursorCount: "+cusCursor.getCount(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "db conn error:"+e.getCause(), Toast.LENGTH_SHORT).show();
        }
        try {
            String cusno,cusname,addrss,area,twn,route,tel="deaultvalue",fax="deaultvalue",email="deaultvalue",ownername="deaultvalue",ownerCon="deaultvalue",regno="deaultvalue",brno="deaultvalue",img = "deaultvalue";
            Double  lat =0.0 ,lng = 0.0;
            int dist=0,status = 0;


            while (cusCursor.moveToNext()) {



                cusno =  cusCursor.getString(cusCursor.getColumnIndex("NewCustomerID"));
                cusname =  cusCursor.getString(cusCursor.getColumnIndex("CustomerName"));
                addrss = cusCursor.getString(cusCursor.getColumnIndex("Address"));
                dist = 12;//cusCursor.getString(cusCursor.getColumnIndex("District"));
                area = cusCursor.getString(cusCursor.getColumnIndex("Area"));
                twn = cusCursor.getString(cusCursor.getColumnIndex("Town"));
                route = cusCursor.getString(cusCursor.getColumnIndex("RouteID"));
                status = cusCursor.getInt(cusCursor.getColumnIndex("CustomerStatusID"));//CustomerStatusId instead

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

    /**
     * upload Cheque Details data
     **/
    public ArrayList<String> chequeDetails() {

        ArrayList<String> chequeArray = new ArrayList<>();
        String urlString = "";
        Cursor chequeCursor = null;

        Toast.makeText(context, "inside upload cheque", Toast.LENGTH_SHORT).show();

        try {

            String query = "SELECT * FROM ChequeDetails where isUpload = 0";

            DBHelper con = new DBHelper(context);
            chequeCursor = con.getData(query);

            Toast.makeText(context, "cursorCount: " + chequeCursor.getCount(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "db conn error:" + e.getCause(), Toast.LENGTH_SHORT).show();
        }

        try {
            String serialCode, invoiceDate, invoiceNo, customerNo, chequeNumber, bankName, collectedDate, realizedDate;
            double chequeAmount, invoiceTotalValue;

            while (chequeCursor.moveToNext()) {

                serialCode = chequeCursor.getString(chequeCursor.getColumnIndex("SerialCode"));
                invoiceDate = chequeCursor.getString(chequeCursor.getColumnIndex("InvoiceDate"));
                invoiceNo = chequeCursor.getString(chequeCursor.getColumnIndex("InvoiceNo"));
                customerNo = chequeCursor.getString(chequeCursor.getColumnIndex("CustomerNo"));
                invoiceTotalValue = chequeCursor.getDouble(chequeCursor.getColumnIndex("InvoiceTotalValue"));
                chequeAmount = chequeCursor.getDouble(chequeCursor.getColumnIndex("ChequeAmount"));
                chequeNumber = chequeCursor.getString(chequeCursor.getColumnIndex("ChequNumber"));
                bankName = chequeCursor.getString(chequeCursor.getColumnIndex("BankName"));
                collectedDate = chequeCursor.getString(chequeCursor.getColumnIndex("CollectedDate"));
                realizedDate = chequeCursor.getString(chequeCursor.getColumnIndex("RealizedDate"));

                //replace the link
                urlString = "http://www.bizmapexpert.com/DIstributorManagementSystem/Up_Tr_CustomerMaster_Pending/Tr_CustomerMaster_PendingNew?" +
                        "RepID=99&TabSerialCode=" + serialCode + "&InvoiceDate=" + invoiceDate + "&InvoiceNo=" + invoiceNo + "&CustomerNo=" + customerNo + "&" +
                        "InvoiceTotalValue=" + invoiceTotalValue + "&ChequeAmount=" + chequeAmount + "&ChequNumber=" + chequeNumber + "&BankName=" + bankName + "&CollectedDate=" + collectedDate + "&RealizedDate=" + realizedDate + "";

                String url = "";
                try {
                    URI uri = new URI(urlString.replace(" ", "$$"));
                    {
                        url += uri;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "error building url:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                chequeArray.add(url);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "errorupload:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return chequeArray;
    }
    
}
