package com.example.ahmed.sfa.controllers.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.widget.Toast;

import java.util.ArrayList;


import com.example.ahmed.sfa.controllers.database.DBHelper;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
/*for testing purpose link:http://www.sqlitetutorial.net/tryit/query/sqlite-replace-statement/#1*/

import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.database.DBHelper;

import com.example.ahmed.sfa.models.DeviceCheckController;
import com.example.ahmed.sfa.models.Mst_CheckInOutPoints;
import com.example.ahmed.sfa.models.Mst_CustomerStatus;
import com.example.ahmed.sfa.models.Mst_Customermaster;
import com.example.ahmed.sfa.models.Mst_District;
import com.example.ahmed.sfa.models.Mst_ProductBrandManagement;
import com.example.ahmed.sfa.models.Mst_ProductMaster;
import com.example.ahmed.sfa.models.Mst_Reasons;
import com.example.ahmed.sfa.models.Mst_RepTable;
import com.example.ahmed.sfa.models.Mst_Route;
import com.example.ahmed.sfa.models.Mst_SupplierTable;
import com.example.ahmed.sfa.models.Mst_Territory;

import com.example.ahmed.sfa.models.Tr_ItineraryDetails;

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

    private void openDB(){
        try {
            db = dbHelper.getWritableDatabase();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void closeDB(){
        try{
            dbHelper.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public Cursor runQuery(String qry){
        openDB();
        Cursor res;
        res=db.rawQuery(qry,null);
        closeDB();
        return  res;

    }
    public ArrayList<String> getAllprinciples() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        openDB();
        Cursor res =  db.rawQuery( "SELECT DISTINCT PrincipleID FROM Tr_TabStock", null );
        res.moveToFirst();

        array_list.add("All");
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("PrincipleID")));
            res.moveToNext();
        }
        closeDB();
        return array_list;

    }
    //get brands
    public ArrayList<String> getAllbrands(String qry) {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        openDB();
        Cursor res =  db.rawQuery( qry, null );
        res.moveToFirst();

        array_list.add("All");
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("BrandID")));
            res.moveToNext();
        }
        closeDB();
        return array_list;

    }

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
                "SubBrand,UnitSize,UnitName,RetailPrice,BuyingPrice,Active,LastUpdateDate,TargetAllow) values (" +
                "   (select _ID from Mst_ProductMaster where ItemCode = \""+pro.getItemCode()+"\")," +
                "   \""+pro.getItemCode()+"\",\""+pro.getDescription()+"\",\""+pro.getPrincipleId()+"\",\" "+pro.getPrinciple()+"\"," +
                "\" "+ pro.getBrandId()+" \",  \" "+ pro.getBrand()+" \",  \""+pro.getSubBrandId()+"\",\""+pro.getSubBrand()+"\",\""+pro.getUnitSize()+"\"   ,         "+
                "   \""+pro.getUnitName()+"\",\""+pro.getRetailPrice()+"\",\""+pro.getBuyingPrice()+"\"    ,\""+pro.getActive()+"\", \" "+ DateManager.dateToday()+" \"  ,\""+pro.getTargetAllow()+"\"       "+
                " );");

        closeDB();
    }

    public void setMst_RepTable(Mst_RepTable rep) {

        openDB();

            try {
                db.execSQL("INSERT OR REPLACE INTO Mst_RepTable (_ID,RepID,DeviceName,RepName," +
                        "Address,ContactNo,DealerName,DealerAddress,MacAddress,AgentID,IsActive,LastUpdateDate) values (" +
                        "(select _ID from Mst_RepTable where RepID = '" + rep.getRepId() + "')," +
                        "'" + rep.getRepId() + "','" + rep.getDeviceName() + "','" + rep.getRepName() + "','" + rep.getAddress() + "'," +
                        "'" + rep.getContactNo() + "','" + rep.getDealerName() + "','" + rep.getDealerAdress() + "','" + rep.getMacAdress() + "'," +
                        "'" + rep.getAgentId() + "'," + rep.getIsActive() + ",'" + DateManager.dateToday() + "'" +
                        " );");
            }catch (Exception e){
                Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_LONG).show();
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
                    "IsActive ,LastUpdateDate) VALUES((select _id from  Mst_Customermaster where CustomerNo='"+cus.getCustomerNo()+"')" +
                    ",'"+cus.getCustomerNo()+"','"+cus.getCustomerName()+"','"+cus.getAddress()+"','"+cus.getDistrictID()+"','"+cus.getDistrict()+"'," +
                    "'"+cus.getAreaID()+"','"+cus.getArea()+"','"+cus.getTown()+"','"+cus.getTelephone()+"','"+cus.getFax()+"','"+cus.getEmail()+"'" +
                    ",'"+cus.getBrNo()+"','"+cus.getOwnerContactNo()+"','"+cus.getOwnerName()+"','"+cus.getPhamacyRegNo()+"',"+cus.getCreditLimit()+","+cus.getCurrentCreditAmount()+"" +
                    ",'"+cus.getCustomerStatus()+"','"+cus.getInsertDate()+"','"+cus.getRouteID()+"','"+cus.getRouteName()+"','"+cus.getImageID()+"',"+cus.getLatitude()+"" +
                    ","+cus.getLongitude()+",'"+cus.getCompanyCode()+"',"+cus.getIsActive()+",'"+cus.getLastUpdateDate()+"');");
        }catch (Exception e){
            Toast.makeText(context, "Customermaster_insert:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        closeDB();

    }
}
