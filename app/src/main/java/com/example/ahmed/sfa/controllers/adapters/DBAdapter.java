package com.example.ahmed.sfa.controllers.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
/*for testing purpose link:http://www.sqlitetutorial.net/tryit/query/sqlite-replace-statement/#1*/
//import controllers.database.DBHelper;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.database.DBHelper;

import com.example.ahmed.sfa.models.Mst_ProductMaster;
import com.example.ahmed.sfa.models.Mst_RepTable;
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
            array_list.add(res.getString(res.getColumnIndex("Town")));
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
        Cursor res =  db.rawQuery(qry, null );
        res.moveToFirst();


        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("CustomerNo")));
            res.moveToNext();
        }
        closeDB();
        return array_list;
    }
    //universal method to get data from db

    public ArrayList<String> getArryListUniMethod(String ...qry) {
        String query=qry[0];
        String columnName=qry[1];

        ArrayList<String> array_list = new ArrayList<String>();

        openDB();
        Cursor res =  db.rawQuery(query, null );
        res.moveToFirst();

        array_list.add("All");
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(columnName)));
            res.moveToNext();
        }
        closeDB();
        return array_list;

    }

    public  String itineraryDetails(Tr_ItineraryDetails it_d){
        openDB();

        String result="failDBclass";
        try{


            ContentValues contentValues = new ContentValues();
            contentValues.put("ItineraryID", it_d.getItineraryID());
            contentValues.put("ItineraryDate",it_d.getItineraryDate());
            contentValues.put("CustomerNo",it_d.getCustomerNo());
            contentValues.put("IsPlaned",it_d.getIsPlaned());
            contentValues.put("IsInvoiced",it_d.getIsInvoiced());
            contentValues.put("LastUpdateDate",it_d.getLastUpdateDate());


            if(db.insert("Tr_ItineraryDetails", null, contentValues)>0) {
                result = "success";
            }else
                result ="outer_if";

        }catch (SQLException e){
            e.printStackTrace();
            result=e.getMessage();
        }
        closeDB();
        return result;


    }

    public void setMst_ProductBrandManagement() {
        openDB();
        db.execSQL("INSERT OR REPLACE INTO Tr_NewCustomer (ID,NewCustomerID ,CustomerName ,Address ,Area ,Town,OwnerContactNo," +
                " IsUpload ,ApproveStatus ) VALUES ((SELECT ID from Tr_NewCustomer where NewCustomerID='cus_007'),'cus_007','test2DBApt','testAdp ','BDAdp','kadawatha','071562895','uploaded','pending');");
        closeDB();
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


        db.execSQL("INSERT OR REPLACE INTO Mst_RepTable (_ID,RepID,DeviceName,RepName," +
                "Address ,ContactNo,DealerName,DealerAddress ,MacAddress ,AgentID,IsActive,LastUpdateDate) values (" +
                "   (select _ID from Mst_RepTable where RepID = \""+rep.getRepId()+"\")," +
                "   \""+rep.getRepId()+"\",\""+rep.getDeviceName()+"\",\""+rep.getRepName()+"\",\" "+rep.getAddress()+"\"," +

                "   \""+rep.getContactNo()+" \"  , \" "+rep.getDealerName()+" \" , \" "+rep.getDealerAdress()+" \"  , \" "+rep.getMacAdress()+" \"                       "+
                "   \""+rep.getAgentId()+"\" ,  "+rep.getIsActive() +" , \" "+DateManager.dateToday()+"\"    "+
                " );");

        closeDB();
    }
}
