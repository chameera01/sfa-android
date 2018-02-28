package com.example.ahmed.sfa.controllers.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.RandomNumberGenerator;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.CollectionsOutStanding;
import com.example.ahmed.sfa.models.DeviceCheckController;
import com.example.ahmed.sfa.models.Mst_CheckInOutPoints;
import com.example.ahmed.sfa.models.Mst_CustomerStatus;
import com.example.ahmed.sfa.models.Mst_Customermaster;
import com.example.ahmed.sfa.models.Mst_DeviceRepDetails;
import com.example.ahmed.sfa.models.Mst_District;
import com.example.ahmed.sfa.models.Mst_InvoiceNos_Mgt;
import com.example.ahmed.sfa.models.Mst_ProductBrandManagement;
import com.example.ahmed.sfa.models.Mst_ProductMaster;
import com.example.ahmed.sfa.models.Mst_Reasons;
import com.example.ahmed.sfa.models.Mst_RepTable;
import com.example.ahmed.sfa.models.Mst_Route;
import com.example.ahmed.sfa.models.Mst_SupplierTable;
import com.example.ahmed.sfa.models.Mst_Territory;
import com.example.ahmed.sfa.models.Tr_ItineraryDetails;
import com.example.ahmed.sfa.models.Tr_TabStock;

import java.util.ArrayList;

/*for testing purpose link:http://www.sqlitetutorial.net/tryit/query/sqlite-replace-statement/#1*/

/**
 * Created by DELL on 3/10/2017.
 */

public class DBAdapter{
    Context context;
    DBHelper dbHelper;
    SQLiteDatabase db;

    public DBAdapter(Context context){
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public void openDB() {
        try {
            db = dbHelper.getWritableDatabase();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void closeDB() {
        try{
            dbHelper.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public Cursor runQuery(String qry){
//        openDB();
        Cursor res = null;
        try {
            Log.d("STOCK", "inside runQuery");
            res = db.rawQuery(qry, null);
            if (res.getCount() != 0) {
                Log.d("STOCK", String.valueOf(res.getCount()) + "not empty");
            } else {
                Log.d("STOCK", "empty");
            }
        } catch (Exception e) {
            Log.d("STOCK", e.getMessage());
        }

//        closeDB();
        return  res;

    }
    public ArrayList<String> getAllprinciples() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        openDB();
        Cursor res = db.rawQuery("SELECT DISTINCT Mst_ProductMaster.Principle FROM Mst_ProductMaster INNER JOIN Tr_TabStock ON Mst_ProductMaster.PrincipleID = Tr_TabStock.PrincipleID", null);
        res.moveToFirst();

        array_list.add("All");
        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("Principle")));
            res.moveToNext();
        }
        closeDB();
        return array_list;

    }

    public String getBrand(String qry) {

        Log.d("STOCK", "inside getBrand in DBAdapter");
        Log.d("STOCK", qry);
        String br = "";
        Cursor res;
        openDB();
        try {
            res = db.rawQuery(qry, null);
            if (res.getCount() != 0) {
                Log.d("STOCK", String.valueOf(res.getCount()) + "not empty");
                if (res.moveToFirst()) {
                    Log.d("STOCK", "cursor moved to first");
                    while (!res.isAfterLast()) {
                        br = (res.getString(res.getColumnIndex("Brand")));
                        Log.d("STOCK", br);
//                    Toast.makeText(context, br, Toast.LENGTH_SHORT).show();
                        res.moveToNext();
                    }
                }
            } else {
                Log.d("STOCK", "empty");
                Log.d("STOCK", "next to empty");
            }

        } catch (Exception e) {
            Log.d("STOCK", e.getMessage());
        }
        closeDB();
        return br;
    }

    //get brands
//    public ArrayList<String> getAllbrands(String qry, ArrayList<String> bid) {
//
//
//        ArrayList<String> array_list = new ArrayList<String>();
//        ArrayList<String> bidList = new ArrayList<>();
//        bidList.addAll(bid);
//        array_list.add("All");
//        openDB();
//        Log.d("STOCK","opened DB");
//        for(int i = 0;i<bidList.size();i++){
//
//            String currentBrand = bidList.get(i);
//            Log.d("STOCK",currentBrand);
//            qry = qry+ currentBrand;
//            Cursor res =  db.rawQuery( qry, null );
//            res.moveToFirst();
//            Log.d("STOCK","moved to first");
//            while(!res.isAfterLast()){
//                array_list.add(res.getString(res.getColumnIndex("Brand")));
//                Log.d("STOCK",array_list.get(i));
//                Toast.makeText(context,res.getString(res.getColumnIndex("Brand")),Toast.LENGTH_SHORT).show();
//                res.moveToNext();
//            }
//        }
//
//        closeDB();
//        return array_list;
//
//    }

    public boolean insertIntoCustomerImage(String imageCode,String customerID){
        openDB();
        db.execSQL("INSERT INTO Customer_Images(CustomerNo,CustomerImageName) VALUES ('"+customerID+"','"+imageCode+"')");
        closeDB();
        return true;
    }

    public ArrayList<String> getAllArea() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        openDB();
        Cursor res =  db.rawQuery("select DISTINCT Area from Tr_NewCustomer", null );
        res.moveToFirst();

        array_list.add("All");
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("Area")));
            res.moveToNext();
        }
        closeDB();
        return array_list;
    }

    public ArrayList<String> getAllTown() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        openDB();
        Cursor res =  db.rawQuery("select DISTINCT Town from Tr_NewCustomer", null );
        res.moveToFirst();

        array_list.add("All");
        while(res.isAfterLast() == false){
            String town=res.getString(res.getColumnIndex("Town"));
            if(town!=null) {
                array_list.add(town);
            }
            res.moveToNext();
        }
        closeDB();
        return array_list;
    }
    public ArrayList<String> getCusId(String qry) {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        openDB();
        Cursor res =  db.rawQuery(qry,null);
        res.moveToFirst();


        while(res.isAfterLast() == false){
            String cusNo=res.getString(res.getColumnIndex("CustomerNo"));
            if(cusNo!=null){
                array_list.add(cusNo);
            }
            res.moveToNext();
        }
        closeDB();
        return array_list;
    }
    //universal method to get data from db

    public ArrayList<String> getArryListUniMethod(String ...qry) {
        ArrayList<String> array_list = new ArrayList<String>();




        try {
            String query = qry[0];
            String columnName = qry[1];



            openDB();
            Cursor res = db.rawQuery(query, null);
            res.moveToFirst();

            //array_list.add("All");/*removed for spinner bug fix*/
            while (res.isAfterLast() == false) {
                //handle null values;
                String val=res.getString(res.getColumnIndex(columnName));
                if(val!=null){
                    array_list.add(val);
                }
                res.moveToNext();
            }
            closeDB();
            return array_list;
        }catch (Exception e){
            Toast.makeText(context, "DBAdptr_unimethod"+e.getMessage(), Toast.LENGTH_SHORT).show();
            //array_list.add("All");/*removed for spinner bug fix test */
            return null;

        }

    }

    public  String itineraryDetails(Tr_ItineraryDetails it_d){
        openDB();

        String result="failDBclass";
        try{


            ContentValues contentValues = new ContentValues();
            contentValues.put("ItineraryID", it_d.getItineraryID());
            contentValues.put("ItineraryDate",it_d.getItineraryDate());
            contentValues.put("CustomerNo",it_d.getCustomerNo());
            contentValues.put("IsPlanned",it_d.getIsPlaned());
            contentValues.put("IsInvoiced",it_d.getIsInvoiced());
            contentValues.put("LastUpdateDate",it_d.getLastUpdateDate());


            if(db.insert("Tr_ItineraryDetails", null, contentValues)>0) {
                result = "success";

                //Toast.makeText(context,"Succees",Toast.LENGTH_SHORT).show();
            }else {
                result = "outer_if";
                Toast.makeText(context,"failed",Toast.LENGTH_SHORT).show();

            }
        }catch (SQLException e){
            Toast.makeText(context,"exception",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            result=e.getMessage();
        }
        closeDB();
        return result;


    }



    public void setMst_ProductMaster(Mst_ProductMaster pro) {

        openDB();


        db.execSQL("INSERT OR REPLACE INTO Mst_ProductMaster (_ID,ItemCode, Description, PrincipleID, Principle, BrandID,Brand,SubBrandID," +
                "SubBrand,UnitSize,UnitName,RetailPrice,SellingPrice,BuyingPrice,Active,LastUpdateDate,TargetAllow,SortOrder) values (" +
                "   (select _ID from Mst_ProductMaster where ItemCode = \""+pro.getItemCode()+"\")," +
                "   \"" + pro.getItemCode() + "\",\"" + pro.getDescription() + "\",\"" + pro.getPrincipleId() + "\",\"" + pro.getPrinciple() + "\"," +
                "\"" + pro.getBrandId() + "\",  \"" + pro.getBrand() + "\",  \"" + pro.getSubBrandId() + "\",\"" + pro.getSubBrand() + "\",\"" + pro.getUnitSize() + "\"   ,         " +
                "   \"" + pro.getUnitName() + "\",\"" + pro.getRetailPrice() + "\",\"" + pro.getSellingPrice() + "\",\"" + pro.getBuyingPrice() + "\",\"" + pro.getActive() + "\", \"" + DateManager.dateToday() + "\"  ,\"" + pro.getTargetAllow() + "\",\"" + pro.getSortOrder() + "\"" +
                " );");

        closeDB();
    }

    public void setMst_RepTable(Mst_RepTable rep) {

        Log.d("MGT", "inside adapter_" + rep.toString());
        openDB();

            try {
                db.execSQL("INSERT OR REPLACE INTO Mst_RepTable (_ID,RepID,DeviceName,RepName,Address,ContactNo,DealerName,DealerAddress,MacAddress,AgentID,IsActive,LastUpdateDate) values ((select _ID from Mst_RepTable where RepID = '" + rep.getRepId() + "'),'" + rep.getRepId() + "','" + rep.getDeviceName() + "','" + rep.getRepName() + "','" + rep.getAddress() + "'," + "'" + rep.getContactNo() + "','" + rep.getDealerName() + "','" + rep.getDealerAdress() + "','" + rep.getMacAdress() + "','" + rep.getAgentId() + "'," + rep.getIsActive() + ",'" + DateManager.dateToday() + "' );");
                Log.d("MGT", "inserted to rep table");
            }catch (Exception e){
                Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        closeDB();
    }

    public void setCollections(CollectionsOutStanding co) {

        Log.d("COL", "inside col_adapter_" + co.toString());
        openDB();

        try {
            db.execSQL("INSERT OR REPLACE INTO Collections (_id,CreditDays,CreditValue,CurrentCreditValue,CustomerName,CustomerNo,InvoiceDate,InvoiceNo,InvoiceTotalValue,RepID,RepName) values ((select _id from Collections where InvoiceNo = '" + co.getInvoiceNo() + "'),'" + co.getCreditDays() + "','" + co.getCreditValue() + "','" + co.getCurrentCreditValue() + "','" + co.getCustomerName() + "'," + "'" + co.getCustomerNo() + "','" + co.getInvoiceDate() + "','" + co.getInvoiceNo() + "','" + co.getInvoiceTotal() + "','" + co.getRepId() + "','" + co.getRepName() + "' );");
            Log.d("COL", "inserted to collections table");
        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("COL", e.getMessage());
        }
        closeDB();

    }

    public void setMst_InvNos_Mgt(Mst_InvoiceNos_Mgt inm) {

        Log.d("MGT", "inside DBAdapter saving in DB");
        openDB();

        try {
            db.execSQL("INSERT OR REPLACE INTO Mst_InvoiceNumbers_Management (_id,InvoiceNo,InvoiceReturnNo,CollectionNoteNo,LastUpdateDate) VALUES ((select _id from Mst_InvoiceNumbers_Management where InvoiceNo = '" + inm.getInvoiceNo() + "'),'" + inm.getInvoiceNo() + "','" + inm.getInvoiceReturnNo() + "','" + inm.getCollectionNoteNo() + "','" + inm.getlUpdateDate() + "');");
        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        closeDB();
    }


    public void insertMst_SupplierTable(Mst_SupplierTable sup){
        openDB();
        db.execSQL(
                "INSERT OR REPLACE INTO Mst_SupplierTable (_id,PrincipleID,Principle,Activate," +
                        "LastUpdateDate) values (" +
                        "(select _id from Mst_SupplierTable where PrincipleID ='"+sup.getPrincipleID()+"')," +
                        "'"+sup.getPrincipleID()+"','"+sup.getPrinciple()+"','"+sup.getActive()+"','"+DateManager.dateToday()+"'" +
                        " );"
                    );
        closeDB();

    }
    public void insertMst_ProductBrandManagement(Mst_ProductBrandManagement proBrand) {
        openDB();

        Log.w("line","came inside dbadapter");
        db.execSQL("INSERT OR REPLACE INTO Mst_ProductBrandManagement(_ID,BrandID ,PrincipleID,Principle," +
                " MainBrand,Activate,LastUpdateDate)  values(" +
                "(select _ID from Mst_ProductBrandManagement where BrandID='"+proBrand.getBrandID()+"'),'"+proBrand.getBrandID()+"','"+proBrand.getPrincipleID()+"','"+proBrand.getPrinciple()+"'," +
                "'"+proBrand.getMainBrand()+"',"+proBrand.getActive()+",'"+DateManager.dateToday()+"');");
        closeDB();
    }
    public void insertCustomerStatus(Mst_CustomerStatus cusStatus){
        openDB();
        try{
            //_id INTEGER PRIMARY KEY AUTOINCREMENT,StatusID TEXT,Status TEXT,isActive INTEGER,LastUpdateDate TEXT
            db.execSQL(
                    "INSERT OR REPLACE INTO Mst_CustomerStatus (_id ,StatusID,Status,isActive," +
                            "LastUpdateDate) values ((select _id from Mst_CustomerStatus where StatusID ='"+cusStatus.getStatusId()+"'  )," +
                            "'"+cusStatus.getStatusId()+"','"+cusStatus.getStatus()+"',"+cusStatus.getIsActive()+",'"+DateManager.dateToday()+"'" +
                            " );"
            );
        }catch (Exception e){
            Toast.makeText(context, cusStatus.getStatusId()+"dnload Error:"+e.getMessage(), Toast.LENGTH_LONG).show();
            e.getMessage();
        }
        closeDB();

    }

    public void insertDeviceCheckController(DeviceCheckController initialDetails) {
        openDB();
        try{//_ID integer primary key ,DeviceID text,Password text,ACTIVESTATUS text
            db.execSQL(
                   /* "INSERT OR REPLACE INTO DeviceCheckController (_ID,DeviceID,Password,ACTIVESTATUS)" +
                            " values (" +
                            "(select _ID from DeviceCheckController where DeviceID='"+initialDetails.getDevice_id()+"')," +
                            "'"+initialDetails.getDevice_id()+"',"+initialDetails.getPass()+",'"+initialDetails.getStatus()+"'" +
                            " );*/
                    "INSERT OR REPLACE INTO DeviceCheckController (_ID,DeviceID,Password,ACTIVESTATUS) VALUES((select _ID from DeviceCheckController where DeviceID='"+initialDetails.getDevice_id()+"')" +
                            ",'"+initialDetails.getDevice_id()+"','"+initialDetails.getPass()+"','"+initialDetails.getStatus()+"');"
            );
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            Toast.makeText(context,initialDetails.getDevice_id(),Toast.LENGTH_LONG).show();

        }
        closeDB();
    }

    public void insertDistrict(Mst_District district) {
        openDB();
        try{//_id INTEGER PRIMARY KEY AUTOINCREMENT,DistrictId TEXT,DistrictName TEXT,isActive INTEGER,LastUpdateDate TEXT);");
            db.execSQL( "INSERT OR REPLACE INTO Mst_District (_id,DistrictId ,DistrictName ,isActive ,LastUpdateDate) VALUES((select _id from Mst_District where DistrictId='"+district.getDistrictId()+"')" +
                    ",'"+district.getDistrictId()+"','"+district.getDistrictName()+"',"+district.getIsActive()+",'"+DateManager.dateToday()+"');");
        }catch (Exception e){
            Toast.makeText(context, "insert_error"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }

    public void insertTerritory(Mst_Territory terri) {
        openDB();
        //db.execSQL("CREATE TABLE  Mst_Territory (_ID integer PRIMARY KEY AUTOINCREMENT, TerritoryID text, Territory text, IsActive integer, LastUpdateDate text) ");
        try{
            db.execSQL("INSERT OR REPLACE INTO Mst_Territory (_ID,TerritoryID ,Territory,IsActive,LastUpdateDate) VALUES((select _id from Mst_Territory where TerritoryID='"+terri.getTerritory_id()+"')" +
                    ",'"+terri.getTerritory_id()+"','"+terri.getTerritory()+"',"+terri.getIsActive()+",'"+terri.getLastUpdateDate()+"');");
        }catch (Exception e){
            Toast.makeText(context, "territoery:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }

    public void insertRoute(Mst_Route route) {
        openDB();
        try{
            ////db.execSQL("CREATE TABLE Mst_Route (_id INTEGER PRIMARY KEY AUTOINCREMENT,RouteID TEXT,TerritoryID TEXT,Territory TEXT,Route TEXT,isActive INTEGER,LastUpdateDate TEXT);");
            db.execSQL("INSERT OR REPLACE INTO Mst_Route (_id,RouteID ,TerritoryID ,Territory ,Route ,isActive ,LastUpdateDate) VALUES((select _id from Mst_Route where RouteID='"+route.getRouteID()+"')" +
                    ",'"+route.getRouteID()+"','"+route.getTerritoryID()+"','"+route.getTerritory()+"','"+route.getRoute()+"',"+route.getIsActive()+",'"+route.getLastUpdateDate()+"');");
        }catch (Exception e){
            Toast.makeText(context, "Route_insert_"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }

    public void insertReason(Mst_Reasons reason) {
        openDB();
        try{//ID | ReasonsID | Reasons | IsActive | LastUpdateDate
            db.execSQL("INSERT OR REPLACE INTO Mst_Reasons (_ID,ReasonsID,Reason,IsActive,LastUpdateDate) VALUES((select _id from Mst_Reasons where ReasonsID='"+reason.getReasonId()+"')" +
                    ",'"+reason.getReasonId()+"','"+reason.getReason()+"',"+reason.getIsActive()+",'"+reason.getLastUpdateDate()+"');");
        }catch (Exception e){
            Toast.makeText(context, "Reson_insert_:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }

    public void insertCheckInOutPoints(Mst_CheckInOutPoints checkInOutPoints) {
        openDB();
        try{
            /**Mst_CheckInOutPoints *ID | ServerID | Type | PointDescription | IsActive | LastUpdateDate */
            db.execSQL("INSERT OR REPLACE INTO Mst_CheckInOutPoints  (_id,ServerID,Type,PointDescription,IsActive,LastUpdateDate) VALUES((select _id from Mst_CheckInOutPoints where ServerID='"+checkInOutPoints.getServerId()+"')" +
                    ",'"+checkInOutPoints.getServerId()+"','"+checkInOutPoints.getType()+"','"+checkInOutPoints.getPointDescription()+"',"+checkInOutPoints.getIsActive()+",'"+checkInOutPoints.getLastUpdateDate()+"');");
        }catch (Exception e){
            Toast.makeText(context, "CheckINoutPonits:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }

    public void insertCustomerData(Mst_Customermaster cus) {
        openDB();
        try{
            db.execSQL("INSERT OR REPLACE INTO Mst_Customermaster (_id," +
                    "CustomerNo,CustomerName ,Address,DistrictID ,District ,AreaID," +
                    "Area,Town,Telephone,Fax,Email,BRno,OwnerContactNo," +
                    "OwnerName,PhamacyRegNo ,CreditLimit,CurrentCreditAmount ,CustomerStatus" +
                    ",InsertDate,RouteID ,RouteName,ImageID,Latitude ,Longitude ,CompanyCode ," +
                    "IsActive ,LastUpdateDate,IsCashCustomer) VALUES((select _id from  Mst_Customermaster where CustomerNo='" + cus.getCustomerNo() + "')" +
                    ",'"+cus.getCustomerNo()+"','"+cus.getCustomerName()+"','"+cus.getAddress()+"','"+cus.getDistrictID()+"','"+cus.getDistrict()+"'," +
                    "'"+cus.getAreaID()+"','"+cus.getArea()+"','"+cus.getTown()+"','"+cus.getTelephone()+"','"+cus.getFax()+"','"+cus.getEmail()+"'" +
                    ",'"+cus.getBrNo()+"','"+cus.getOwnerContactNo()+"','"+cus.getOwnerName()+"','"+cus.getPhamacyRegNo()+"',"+cus.getCreditLimit()+","+cus.getCurrentCreditAmount()+"" +
                    ",'"+cus.getCustomerStatus()+"','"+cus.getInsertDate()+"','"+cus.getRouteID()+"','"+cus.getRouteName()+"','"+cus.getImageID()+"',"+cus.getLatitude()+"" +
                    "," + cus.getLongitude() + ",'" + cus.getCompanyCode() + "'," + cus.getIsActive() + ",'" + cus.getLastUpdateDate() + "','" + cus.getIsCashCustomer() + "');");
        }catch (Exception e){
            //Toast.makeText(context, "Customermaster_insert:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("MGT", "Customermaster_insert:" + e.getMessage());
        }
        closeDB();

    }

    public void insert_tabStock(Tr_TabStock tabStock) {
        openDB();
        try{
            /*
              {
        "ServerID": 8,
        "PrincipleID": 2021,
        "BrandID": 2035,
        "ItemCode": "GEP80-G",
        "BatchNumber": "GEP80-G",
        "ExpiryDate": "2050-01-01T00:00:00",
        "SellingPrice": 772,
        "RetailPrice": 950,
        "Qty": 100
    }
             */
            db.execSQL("INSERT OR REPLACE INTO Tr_TabStock (_ID," +
                    "ServerID,PrincipleID ,BrandID,ItemCode ,BatchNumber ,ExpiryDate," +
                    "SellingPrice,RetailPrice,Qty) VALUES((select _ID from  Tr_TabStock where ServerID='"+tabStock.getServerId()+"')" +
                    ",'"+tabStock.getServerId()+"','"+tabStock.getPrincipleID()+"','"+tabStock.getBrandId()+"','"+tabStock.getItemCode()+"','"+tabStock.getBatchNumber()+"'," +
                    "'"+tabStock.getExpireyDate()+"',"+tabStock.getSellingPrice()+","+tabStock.getRetailPrice()+","+tabStock.getQuantity()+");");
        }catch (Exception e){
            Toast.makeText(context, "tabstock_insert:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }

    public void updateCustomerUploadStatus(String cusNo) {
        openDB();
        try{
            db.execSQL("UPDATE Tr_NewCustomer" +
                    " SET isUpload = 1 " +
                    " WHERE NewCustomerID = '"+cusNo+"' ;");
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "updae customer upload status:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }

    public void insertItineraryDetails(Tr_ItineraryDetails itineraryDetails) {
        openDB();
        try{
            RandomNumberGenerator rg = new RandomNumberGenerator();
            String itineraryId = rg.generateRandomCode(RandomNumberGenerator.GENERATE_ALPHABANUMERIC,"ITRY_",15);
            String[] dateConvert = itineraryDetails.getItineraryDate().split("-");
            String date = dateConvert[1]+"/"+dateConvert[2].substring(0,2)+"/"+dateConvert[0];
            /*_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ItineraryID TEXT,ItineraryDate TEXT,CustomerNo TEXT,IsPlanned INTEGER,IsInvoiced INTEGER,LastUpdateDate TEXT );");*/
            db.execSQL("INSERT OR REPLACE INTO Tr_ItineraryDetails (_id," +
                    "ItineraryID,ItineraryDate ,CustomerNo,IsPlanned ,IsInvoiced ,LastUpdateDate" +
                    ") VALUES((select _id from  Tr_ItineraryDetails where CustomerNo='"+itineraryDetails.getCustomerNo()+"' AND ItineraryDate = '"+date+"')" +
                    ",'"+itineraryId+"','"+date+"','"+itineraryDetails.getCustomerNo()+"',"+itineraryDetails.getIsPlaned()+","+itineraryDetails.getIsInvoiced()+"," +
                    "'"+DateManager.dateToday()+"');");
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "updae customer upload status:" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }

    public void updateSalesHeaderUploadStatus(String cusNo) {
        openDB();
        try {
            db.execSQL("UPDATE Tr_SalesHeader" +
                    " SET isUpload = 1 " +
                    " WHERE CustomerNo = '" + cusNo + "' ;");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "update Sales Header upload status:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }

    /**
     * -----------Get the Header ID and pass it-----------
     **/
    public void updateSalesDetailsUploadStatus(String cusNo) {
        openDB();
        try {
            db.execSQL("UPDATE Tr_SalesDetails SET IsUpload = 0 WHERE _id = '" + cusNo + "' ;");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Update Sales Details upload status:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }

    public void updateSalesoutstandingUploadStatuscusNo(String cusno) {
        openDB();
        try {
            db.execSQL("UPDATE Tr_InvoiceOutstanding SET IsUpload = 0 WHERE _id = '" + cusno + "' ;");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Update InvoiceOutstanding upload status:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }


    public void updateSalesReturnDetails(String cusNo) {

        openDB();
        try {
            db.execSQL("UPDATE Tr_SalesReturnDetails SET IsUpload = 0 WHERE _id = '" + cusNo + "' ;");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Update SalesReturnDetails upload status:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }

    public void updateSalesReturnHeader(String cusno) {
        openDB();
        try {
            db.execSQL("UPDATE Tr_SalesReturn SET IsUpload = 1 WHERE _id = '" + cusno + "' ;");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Update Sales Header upload status:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }

    //manual sync
    public Boolean updateaudit() {
        openDB();
        try {
            db.execSQL("UPDATE Tr_SalesHeader SET IsUpload = 0;");
            db.execSQL("UPDATE Tr_SalesDetails SET IsUpload = 0;");
            db.execSQL("UPDATE Tr_InvoiceOutstanding SET IsUpload = 0;");
            db.execSQL("UPDATE Tr_NewCustomer SET IsUpload = 0;");
            db.execSQL("UPDATE Tr_SalesReturn SET IsUpload = 0;");
            db.execSQL("UPDATE Tr_SalesReturnDetails SET IsUpload = 0;");
            closeDB();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Update Invoice Outstanding upload status:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public double getLastRemaining(String customer) {
        Log.d("COL", "inside getLastRemaining customer: " + customer);
        double remain = -1.0;
        openDB();
        Cursor c = runQuery("SELECT LastRemaining FROM Temp_LastSelected_Collection WHERE LastSelectedCustomer = '" + customer + "'");
        if (c.getCount() != 0 && c.moveToLast()) {
            remain = c.getInt(0);
        }
        Log.d("COL", "last remaining: " + remain);
        closeDB();
        return remain;
    }

    public Boolean setLastRemaining(String cust, double remaining) {

        try {
            openDB();
            db.execSQL("INSERT INTO Temp_LastSelected_Collection(LastSelectedCustomer,LastRemaining) VALUES('" + cust + "','" + remaining + "') ");
            closeDB();
            return true;
        } catch (Exception e) {
            Log.d("CASH", "Ex: " + e.getMessage());
            return false;
        }
    }

    public void setDeviceRepDetails(Mst_DeviceRepDetails drd) {

        Log.d("MGT", "inside DBAdapter saving in DB");
        openDB();

        try {
            db.execSQL("INSERT OR REPLACE INTO Mst_DeviceRepDetails (DeviceID,RepID, RepName) VALUES ('" + drd.getDeviceId() + "','" + drd.getRepId() + "','" + drd.getRepName() + "');");
        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        closeDB();

    }
}
