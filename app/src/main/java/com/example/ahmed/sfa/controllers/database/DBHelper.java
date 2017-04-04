package com.example.ahmed.sfa.controllers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ahmed on 3/3/2017.
 */

//this class handles the creation and upgrade functionality
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "sfa.db";//this is the name of the database created in the phone
    private static final int VERSION = 1;

    public DBHelper (Context ctxt){
        super(ctxt,DB_NAME,null,VERSION);//error handler shld be implemented
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        try{
            /*Cretae table Tr_TabStock*/
            db.execSQL(
                    "create table Tr_TabStock" +
                            "(_ID integer primary key AUTOINCREMENT ,ServerID  text,PrincipleID text,BrandID text,ItemCode text,BatchNumber text," +
                            "ExpiryDate text,SellingPrice real,RetailPrice real," +
                            "Qty  integer,LastUpdateDate text)"
            );
            /*create tanle to save Active Status*/
            db.execSQL("CREATE TABLE DeviceCheckController (_ID integer primary key ,DeviceID text,Password text,ACTIVESTATUS text)");

            /*create table for sales rep*/
            db.execSQL("CREATE TABLE Mst_RepTable " +
                    "(_ID integer primary key AUTOINCREMENT,RepID text,DeviceName text,RepName text," +
                    "Address text,ContactNo text,DealerName text,DealerAddress text,MacAddress text,AgentID text,IsActive integer,LastUpdateDate text )");

            /*insert data into Mst_RepTable*/
            db.execSQL("INSERT INTO  Mst_RepTable (_ID,RepID,RepName) VALUES(1,'r1','malitnha'); ");
            /*create table ProductMAster*/
            db.execSQL(
                    "CREATE TABLE Mst_ProductMaster" +
                            "(_ID integer primary key AUTOINCREMENT ,ItemCode text,Description text,PrincipleID text,Principle text," +
                            "BrandID text,Brand text,SubBrandID text,SubBrand text,UnitSize integer,UnitName text,RetailPrice real," +
                            "SellingPrice real,BuyingPrice real,Active integer,LastUpdateDate text,TargetAllow  integer)"
            );
            //insert data to Mst_ProductMaster
            db.execSQL("INSERT INTO Mst_ProductMaster (ItemCode, Description, PrincipleID, Principle, BrandID,Brand,SubBrandID," +
                    " SubBrand,UnitSize,UnitName,RetailPrice,SellingPrice,BuyingPrice,Active,LastUpdateDate,TargetAllow) " +
                    "VALUES ('cd003','desc','prinid1','newphama','brandid','belcosid','subb','subbname',456,'uname',123.4,124.2,562.3,1,'2017-03-26',0);");

            /*create MSt_suppler table*/
            db.execSQL(
                    "create table Mst_SupplierTable" +
                            "(_ID integer primary key AUTOINCREMENT , PrincipleID  text,Principle text," +
                            "Activate  integer,LastUpdateDate text)"
            );
            /**Insert data into Mst_supplierTable**/
            //db.execSQL("INSERT INTO Mst_SupplierTable (PrincipleID,Principle) VALUES('Pid','PRINCIPLEnAME')");

            //creating the itinerarydetails table
            db.execSQL("CREATE TABLE Tr_ItineraryDetails (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ItineraryID TEXT,ItineraryDate TEXT,CustomerNo TEXT,IsPlanned INTEGER,IsInvoiced INTEGER,LastUpdateDate TEXT );");


            //creating the customer table
            db.execSQL("CREATE TABLE Mst_Customermaster (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "CustomerNo TEXT,CustomerName TEXT,Address TEXT,DistrictID TEXT,District TEXT,AreaID TEXT," +
                    "Area TEXT,Town TEXT,Telephone TEXT,Fax TEXT,Email Text, BRno TEXT,OwnerContactNo TEXT," +
                    "OwnerName TEXT,PhamacyRegNo TEXT,CreditLimit TEXT,CurrentCreditAmount TEXT,CustomerStatus TEXT" +
                    ",InsertDate TEXT,RouteID TEXT,RouteName TEXT,ImageID TEXT,Latitude TEXT,Longitude TEXT,CompanyCode TEXT," +
                    "IsActive INTEGER,LastUpdateDate TEXT);");

            //creating Customer status Table
            db.execSQL("CREATE TABLE Mst_CustomerStatus (_id INTEGER PRIMARY KEY AUTOINCREMENT,StatusID TEXT,Status TEXT,isActive INTEGER,LastUpdateDate TEXT);");

            //creating the master District Table
            db.execSQL("CREATE TABLE Mst_District (_id INTEGER PRIMARY KEY AUTOINCREMENT,DistrictId TEXT,DistrictName TEXT,isActive INTEGER,LastUpdateDate TEXT);");

            //creating master route table
            db.execSQL("CREATE TABLE Mst_Route (_id INTEGER PRIMARY KEY AUTOINCREMENT,RouteID TEXT,TerritoryID TEXT,Territory TEXT,Route TEXT,isActive INTEGER,LastUpdateDate TEXT);");

            //Create a table to save the images relavent to customers
            db.execSQL("CREATE TABLE Customer_Images (_id INTEGER PRIMARY KEY AUTOINCREMENT,CustomerNo TEXT,CustomerImageName TEXT);");

            //create a table for temporary customer details
            db.execSQL("CREATE TABLE Tr_NewCustomer (_id INTEGER PRIMARY KEY AUTOINCREMENT,NewCustomerID,CustomerName TEXT,Address TEXT,District TEXT,Area TEXT," +
                    "Town TEXT,Telephone TEXT,Fax TEXT,Email TEXT,BRno TEXT,OwnerContactNo TEXT,OwnerName TEXT,PharmacyRegNo TEXT," +
                    "CreditLimit INTEGER,CustomerStatusID TEXT,CustomerStatus TEXT,InsertDate TEXT,RouteID TEXT,RouteName TEXT," +
                    "ImageID TEXT,Latitude REAL,Longitude REAL,isUpload INTEGER,UploadDate TEXT,ApproveStatus INTEGER,LastUpdateDate TEXT);");

            //create Tr_DailyRouteDetails
            db.execSQL("CREATE TABLE Tr_DailyRouteDetails (_id INTEGER PRIMARY KEY AUTOINCREMENT,SerialCode TEXT,Date TEXT,ItineraryID TEXT,CustomerNo TEXT,IsPlanned INTEGER,IsInvoiced INTEGER,InvoiceNo TEXT,Reasons TEXT,Comment TEXT,IsUpload TEXT,UploadDate TEXT)");

            //create table for check in check out points
            db.execSQL("CREATE TABLE Mst_CheckInOutPoints (_id INTEGER PRIMARY KEY AUTOINCREMENT,ServerID TEXT, Type TEXT," +
                    "PointDescription TEXT,isActive INTEGER,LastUpdateDate)");

            //create check in and check out table
            db.execSQL("CREATE TABLE Tr_CheckInCheckOut (_id INTEGER PRIMARY KEY AUTOINCREMENT,SerialCode TEXT,Date TEXT,InPoint TEXT DEFAULT NULL,InTime TEXT,InComment TEXT,OutPoint TEXT DEFAULT NULL,OutTime TEXT,OutComment TEXT," +
                    "isUpload INTEGER,LastUpdateDate TEXT, Latitude_CheckIn TEXT,Longitude_CheckIn TEXT,Latitude_CheckOut TEXT,Longitude_CheckOut TEXT);");

            //create reasons table for customer pop up
            db.execSQL("CREATE TABLE Mst_Reasons (_id INTEGER PRIMARY KEY AUTOINCREMENT,ReasonsID TEXT,Reason TEXT,isActive INTEGER,LastUpdateDate TEXT);");



            //create  Tr_SalesHeader table
            db.execSQL("CREATE TABLE Tr_SalesHeader(_id INTEGER PRIMARY KEY AUTOINCREMENT,ItineraryID TEXT,CustomerNo TEXT,InvoiceNo TEXT,InvoiceDate TEXT,PaymentTime TEXT" +
                    "SubTotal REAL,InvoiceTotal REAL,FullDiscountRate REAL,DiscountAmount REAL,DiscountType REAL,IsOnInvoiceReturn INTEGER,OnInvoiceReturnNo TEXT" +
                    "OnInvoiceReturnValue REAL,CreditAmount REAL,CashAmount REAL,ChequeAmount REAL,Isprint INTEGER,ProductCount INTEGER" +
                    "InvoiceType TEXT,Latitude REAL,Longitude REAL,IsUpload INTEGER,UploadDate TEXT)");

            //insert data into reasons table
            db.execSQL("INSERT INTO Mst_Reasons(ReasonsID,Reason,isActive) VALUES('RSN1','Reason 1',1);");
            db.execSQL("INSERT INTO Mst_Reasons(ReasonsID,Reason,isActive) VALUES('RSN2','Reason 2',0);");
            db.execSQL("INSERT INTO Mst_Reasons(ReasonsID,Reason,isActive) VALUES('RSN3','Reason 3',0);");

            //insert data into sales header
            db.execSQL("INSERT INTO Tr_SalesHeader(CustomerNo,InvoiceNo,InvoiceDate,InvoiceTotal,CreditAmount) VALUES ('CUS1','INV1','2017/1/6',1200.00,500.00);");
            db.execSQL("INSERT INTO Tr_SalesHeader(CustomerNo,InvoiceNo,InvoiceDate,InvoiceTotal,CreditAmount) VALUES ('CUS2','INV2','2017/1/10',1202.02,500.01);");
            db.execSQL("INSERT INTO Tr_SalesHeader(CustomerNo,InvoiceNo,InvoiceDate,InvoiceTotal,CreditAmount) VALUES ('CUS3','INV3','2017/1/1',1203.02,530.01);");

            //insert data into mst_checkinoutpoints
            db.execSQL("INSERT INTO Mst_CheckInOutPoints (ServerID,Type,PointDescription,isActive) VALUES ('p1','DistributorPoint','WareHouse 1',0)");
            db.execSQL("INSERT INTO Mst_CheckInOutPoints (ServerID,Type,PointDescription,isActive) VALUES ('p2','DistributorPoint','WareHouse 2',0)");
            db.execSQL("INSERT INTO Mst_CheckInOutPoints (ServerID,Type,PointDescription,isActive) VALUES ('p3','DistributorPoint','WareHouse 1',1)");

            //add data to the DailyRouteDetails
            //db.execSQL("INSERT INTO Tr_DailyRouteDetails (SerialCode ,Date,ItineraryID ,CustomerNo ,IsPlanned ,IsInvoiced ,InvoiceNo ,Reasons ,Comment ,IsUpload ,UploadDate) VALUES ('SER1','2017/3/10','IT1','CUS1',0,1) ");

            //adding data to District table
            db.execSQL("INSERT INTO Mst_District(DistrictId,DistrictName,isActive,LastUpdateDate) VALUES('DST1','Matale',1,'2017/3/2');");
            db.execSQL("INSERT INTO Mst_District(DistrictId,DistrictName,isActive,LastUpdateDate) VALUES('DST2','Colombo',1,'2017/3/2');");
            db.execSQL("INSERT INTO Mst_District(DistrictId,DistrictName,isActive,LastUpdateDate) VALUES('DST3','Kandy',1,'2017/3/2');");

            //adding data to customer status table
            db.execSQL("INSERT INTO Mst_CustomerStatus (StatusID,Status,isActive,LastUpdateDate)" +
                    "VALUES ('STS1','ACTIVE',1,'2017/3/2');");
            db.execSQL("INSERT INTO Mst_CustomerStatus (StatusID,Status,isActive,LastUpdateDate)" +
                    "VALUES ('STS2','STATUS2',1,'2017/3/2');");
            db.execSQL("INSERT INTO Mst_CustomerStatus (StatusID,Status,isActive,LastUpdateDate)" +
                    "VALUES ('STS3','STATUS3',1,'2017/3/2');");

            //adding data to routes table
            db.execSQL("INSERT INTO Mst_Route(RouteID,TerritoryID,Territory,Route,isActive,LastUpdateDate) " +
                    "VALUES('RUT1','TER1','terri1','route1',1,'2017/3/2');");
            db.execSQL("INSERT INTO Mst_Route(RouteID,TerritoryID,Territory,Route,isActive,LastUpdateDate) " +
                    "VALUES('RUT2','TER2','terri2','route2',1,'2017/3/2');");
            db.execSQL("INSERT INTO Mst_Route(RouteID,TerritoryID,Territory,Route,isActive,LastUpdateDate) " +
                    "VALUES('RUT2','TER2','terri2','route2',1,'2017/3/2');");

            //add data to the customer_images table
            //db.execSQL("INSERT INTO Customer_Images(CustomerNo,CustomerImageName) VALUES ('CUS1','CUS_IMG_1')");
            //db.execSQL("INSERT INTO Customer_Images(CustomerNo,CustomerImageName) VALUES ('CUS2','CUS_IMG_2')");
            //db.execSQL("INSERT INTO Customer_Images(CustomerNo,CustomerImageName) VALUES ('CUS3','CUS_IMG_3')");
            //db.execSQL("INSERT INTO Customer_Images(CustomerNo,CustomerImageName) VALUES ('CUS4','CUS_IMG_4')");
            //db.execSQL("INSERT INTO Customer_Images(CustomerNo,CustomerImageName) VALUES ('CUS5','CUS_IMG_5')");

            //INSERT VALUES TO THE ITINERARY DETAILS TABLE
            /*db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT1','2017/3/17','CUS1',1,1);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT2','2017/3/17','CUS2',1,1);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT3','2017/3/17','CUS3',1,1);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT4','2017/3/17','CUS4',1,1);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT5','2017/3/17','CUS5',1,1);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT6','2017/3/17','CUS6',1,2);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT7','2017/3/17','CUS7',1,1);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT8','2017/3/17','CUS8',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT9','2017/3/17','CUS9',1,1);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT10','2017/3/17','CUS10',1,0);");*/


            //adding data to customer table
            /*db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Town)" +
                    "VALUES ('CUS1','aksa','Town1');");
            db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Town)" +
                    "VALUES ('CUS2','barca','Town2');");
            db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Town)" +
                    "VALUES ('CUS3','madrid','Town3');");
            db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Town)" +
                    "VALUES ('CUS4','palva','Town4');");
            db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Town)" +
                    "VALUES ('CUS6','rose','Town5');");
            db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Town)" +
                    "VALUES ('CUS7','aksa','Town1');");
            db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Town)" +
                    "VALUES ('CUS8','barca','Town2');");
            db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Town)" +
                    "VALUES ('CUS9','madrid','Town3');");
            db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Town)" +
                    "VALUES ('CUS10','palva','Town4');");
            db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Town)" +
                    "VALUES ('CUS5','rose','Town5');");*/




            /**
             db.execSQL("CREATE TABLE route (_id INTEGER PRIMARY KEY AUTOINCREMENT, customer TEXT,town TEXT);");
             db.execSQL("INSERT INTO route VALUES (1,'a','z');");
             db.execSQL("INSERT INTO route VALUES (2,'b','a');");
             db.execSQL("INSERT INTO route VALUES (3,'c','z');");
             db.execSQL("INSERT INTO route VALUES (4,'d','a');");
             db.execSQL("INSERT INTO route VALUES (5,'e','z');");
             db.execSQL("INSERT INTO route VALUES (6,'f','a');");
             */

            /*db.execSQL(
                    "create table mst_productmaster " +
                            "(_id integer primary key AUTOINCREMENT , itemcode text,description text,principleid text, principle text," +
                            "brandid text,brand text,subbrandid text,subbrand text,unitsize integer,unitname text,retailprice real," +
                            "sellingprice real,buyingprice real,active integer,lastupdatedate text,targetallow integer)"
            );*/


            //only few columns were addesto Tr_NewCustome tables
            /*db.execSQL(
                    "create table Tr_NewCustomer" +
                            "(_ID integer primary key AUTOINCREMENT ,NewCustomerID text,CustomerName text,Address text,Area text,Town text,OwnerContactNo text," +
                            "IsUpload text,ApproveStatus text)"
            );*/



            //adding datato  table 21 TrNewCustomer
           /* db.execSQL("INSERT INTO Tr_NewCustomer VALUES (1,'cus_001','peachnet','addre ','area_dalugama','dalugama','071562895','uploaded','pending');");
            db.execSQL("INSERT INTO Tr_NewCustomer VALUES (2,'cus_002','healthycafe','addre is goes here','area_dalugama','bambalapitiya','071562895','uploaded','pending');");
            db.execSQL("INSERT INTO Tr_NewCustomer VALUES (3,'cus_003','thilakawardhana','addre ','area_kiribathgoda','kiribathgoda','071562895','uploaded','pending');");
            db.execSQL("INSERT INTO Tr_NewCustomer VALUES (4,'cus_004','kandy','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
            db.execSQL("INSERT INTO Tr_NewCustomer VALUES (5,'cus_005','thilakawardhana','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");*/



            /*db.execSQL("INSERT INTO Tr_TabStock VALUES (1,'server_id','principle_1','bran_1','cd001','bct_1','2017-10-25',45.50,60.5,895,'2017-02-25');");*/



            db.execSQL(
                    "create table Mst_ProductBrandManagement" +
                            "(_ID integer primary key AUTOINCREMENT ,BrandID text, PrincipleID  text,Principle text," +
                            "MainBrand text,Activate integer,LastUpdateDate text)"
            );
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        //no second schema yet so left empty
    }



    public static final String PRODUCT_TABLE_NAME = "Mst_ProductMaster";
    public static final String PRODUCT_COLUMN_PRINCIPLE = "Principle";
    public static final String PRODUCT_COLUMN_BRAND = "Brand";


    private HashMap hp;







    public String insertProduct (String itemcode, String description, String principleid, String principle,String brandid,
                                 String brand,String subbrandid,String subbrand,int unitsize, String unitname,double retailprice,
                                 double sellingprice,double buyingprice,int active,String lastupDate, int targetallow) {
        String result="failDBclass";
        try{
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("ItemCode", itemcode);
            contentValues.put("Description", description);
            contentValues.put("PrincipleID", principleid);
            contentValues.put("Principle", principle);
            contentValues.put("BrandID", brandid);
            contentValues.put("Brand", brand);
            contentValues.put("SubBrandID", subbrandid);
            contentValues.put("SubBrand", subbrand);
            contentValues.put("UnitSize", unitsize);
            contentValues.put("UnitName", unitname);
            contentValues.put("RetailPrice", retailprice);
            contentValues.put("SellingPrice",sellingprice);
            contentValues.put("BuyingPrice", buyingprice);
            contentValues.put("Active", active);
            contentValues.put("LastUpdateDate", lastupDate);
            contentValues.put("TargetAllow", targetallow);

            if(db.insert("Mst_ProductMaster", null, contentValues)>0) {
                result = "success";
            }else
                result ="outer_fail";

        }catch (SQLException e){
            e.printStackTrace();
            result=e.getMessage();
        }

        return result;

    }

    //asankas method
    public  String inserToStockView(String serverid,String principleid,String brand_id,String itemcode,
                                    String batch,String exp,double sellingprice,double retailprice,int qty,String lupdate){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("ServerID", serverid);
            contentValues.put("PrincipleID", principleid);
            contentValues.put("BrandID", brand_id);
            contentValues.put("ItemCode", itemcode);
            contentValues.put("BatchNumber", batch);
            contentValues.put("ExpiryDate", exp);
            contentValues.put("SellingPrice", sellingprice);
            contentValues.put("RetailPrice",retailprice);
            contentValues.put("Qty", qty);
            contentValues.put("LastUpdateDate", lupdate);

            if(db.insert("Tr_TabStock", null, contentValues)>0) {
                return "success";
            }else
                return "fail";


        }catch (SQLException e){
            return e.getMessage();

        }

    }

    //this method will be used only for testing purposes with android database manager testing actibity
    public ArrayList<Cursor> getData2(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }

    public Cursor getData(String qry) {
        //String query=qry;

        try{

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery(qry, null);
            return res;

        }catch (SQLException e){
            e.printStackTrace();
            return null ;
        }

    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PRODUCT_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("Mst_ProductMaster", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Mst_ProductMaster",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllbrands() {

        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("SELECT DISTINCT Brand FROM Mst_ProductMaster", null);
            res.moveToFirst();

            array_list.add("All");
            while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex(PRODUCT_COLUMN_BRAND)));
                res.moveToNext();
            }
            db.close();
        }catch (Exception e){
            array_list.add(e.getMessage());
        }

        return array_list;
    }
    public   ArrayList<String> getAllprinciples() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("SELECT DISTINCT Principle FROM Mst_ProductMaster", null);
            res.moveToFirst();

            array_list.add("All");
            while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex(PRODUCT_COLUMN_PRINCIPLE)));
                res.moveToNext();
            }
            db.close();
        }catch (Exception e){
            array_list.add(e.getMessage());
        }
        return array_list;
    }


}