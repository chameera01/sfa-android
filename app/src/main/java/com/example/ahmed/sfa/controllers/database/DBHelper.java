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
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
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
                    ",IsPlanned,IsInvoiced) VALUES ('IT10','2017/3/17','CUS10',1,0);");


            //adding data to customer table
            db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Town)" +
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
                    "VALUES ('CUS5','rose','Town5');");




            /**
             db.execSQL("CREATE TABLE route (_id INTEGER PRIMARY KEY AUTOINCREMENT, customer TEXT,town TEXT);");
             db.execSQL("INSERT INTO route VALUES (1,'a','z');");
             db.execSQL("INSERT INTO route VALUES (2,'b','a');");
             db.execSQL("INSERT INTO route VALUES (3,'c','z');");
             db.execSQL("INSERT INTO route VALUES (4,'d','a');");
             db.execSQL("INSERT INTO route VALUES (5,'e','z');");
             db.execSQL("INSERT INTO route VALUES (6,'f','a');");
             */

            //only few columns were addesto Tr_NewCustome tables
            /**db.execSQL(
                    "create table Tr_NewCustomer" +
                            "(_ID integer primary key AUTOINCREMENT ,NewCustomerID text,CustomerName text,Address text,Area text,Town text,OwnerContactNo text," +
                            "IsUpload text,ApproveStatus text)"
            );*/



            //adding datato  table 21 TrNewCustomer
            /**
            db.execSQL("INSERT INTO Tr_NewCustomer VALUES (1,'cus_001','peachnet','addre ','area_dalugama','dalugama','071562895','uploaded','pending');");
            db.execSQL("INSERT INTO Tr_NewCustomer VALUES (2,'cus_002','healthycafe','addre is goes here','area_dalugama','bambalapitiya','071562895','uploaded','pending');");
            db.execSQL("INSERT INTO Tr_NewCustomer VALUES (3,'cus_003','thilakawardhana','addre ','area_kiribathgoda','kiribathgoda','071562895','uploaded','pending');");
            db.execSQL("INSERT INTO Tr_NewCustomer VALUES (4,'cus_004','kandy','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
            db.execSQL("INSERT INTO Tr_NewCustomer VALUES (5,'cus_005','thilakawardhana','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
                */

            db.execSQL(
                    "create table Mst_ProductMaster" +
                            "(_ID integer primary key AUTOINCREMENT ,ItemCode text,Description text,PrincipleID text,Principle text," +
                            "BrandID text,Brand text,SubBrandID text,SubBrand text,UnitSize integer,UnitName text,RetailPrice real," +
                            "SellingPrice real,BuyingPrice real,Active integer,LastUpdateDate text,TargetAllow  integer)"
            );
            db.execSQL(
                    "create table Tr_TabStock" +
                            "(_ID integer primary key AUTOINCREMENT ,ServerID  text,PrincipleID text,BrandID text,ItemCode text,BatchNumber text," +
                            "ExpiryDate text,SellingPrice real,RetailPrice real," +
                            "Qty  integer,LastUpdateDate text)"
            );
            //db.execSQL("INSERT INTO Tr_TabStock VALUES (1,'server_id','principle_1','bran_1','cd001','bct_1','2017-10-25',45.50,60.5,895,'2017-02-25');");

            db.execSQL("CREATE TABLE Mst_SupplierTable(_id INTEGER PRIMARY KEY AUTOINCREMENT,PrincipleID TEXT, Principle TEXT, Activate INTEGER, LastUpdateDate TEXT)");

            db.execSQL("INSERT INTO Mst_SupplierTable VALUES(1,'PRN1','Principle1',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_SupplierTable VALUES(2,'PRN2','Principle2',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_SupplierTable VALUES(3,'PRN3','Principle3',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_SupplierTable VALUES(4,'PRN4','Principle4',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_SupplierTable VALUES(5,'PRN5','Principle5',0,'2017/1/1')");

            db.execSQL(
                    "create table Mst_ProductBrandManagement" +
                            "(_ID integer primary key AUTOINCREMENT ,BrandID text, PrincipleID  text,Principle text," +
                            "MainBrand text,Activate integer,LastUpdateDate text)"
            );

            db.execSQL("INSERT INTO Mst_ProductBrandManagement VALUES(1,'BRND1','PRN1','Principle1','Brand1',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_ProductBrandManagement VALUES(2,'BRND2','PRN1','Principle1','Brand2',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_ProductBrandManagement VALUES(3,'BRND3','PRN2','Principle2','Brand3',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_ProductBrandManagement VALUES(4,'BRND4','PRN2','Principle2','Brand4',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_ProductBrandManagement VALUES(5,'BRND5','PRN3','Principle3','Brand5',1,'2017/1/1')");






            db.execSQL("INSERT INTO Mst_ProductMaster(ItemCode,Description,PrincipleID,Principle," +
                    "BrandID,Brand ,SubBrandID,SubBrand,UnitSize,UnitName,RetailPrice," +
                    "SellingPrice,BuyingPrice,Active,LastUpdateDate,TargetAllow) values ('ITM1','pro1','PRN1','Principle1','BRND1','brand','','',2,'sdas',10.2,8.4,6.2,0,'2017/06/13',15);");

            db.execSQL("INSERT INTO Mst_ProductMaster(ItemCode,Description,PrincipleID,Principle," +
                    "BrandID,Brand ,SubBrandID,SubBrand,UnitSize,UnitName,RetailPrice," +
                    "SellingPrice,BuyingPrice,Active,LastUpdateDate,TargetAllow) values ('ITM2','pro2','PRN2','Principle2','BRND2','brand2','','',2,'sdas',10.2,8.4,6.2,0,'2017/06/13',15);");
            db.execSQL("INSERT INTO Mst_ProductMaster(ItemCode,Description,PrincipleID,Principle," +
                    "BrandID,Brand ,SubBrandID,SubBrand,UnitSize,UnitName,RetailPrice," +
                    "SellingPrice,BuyingPrice,Active,LastUpdateDate,TargetAllow) values ('ITM2','pro3','PRN3','Principle3','BRND3','brand3','','',2,'sdas',10.2,8.4,6.2,0,'2017/06/13',15);");
            db.execSQL("INSERT INTO Mst_ProductMaster(ItemCode,Description,PrincipleID,Principle," +
                    "BrandID,Brand ,SubBrandID,SubBrand,UnitSize,UnitName,RetailPrice," +
                    "SellingPrice,BuyingPrice,Active,LastUpdateDate,TargetAllow) values ('ITM4','pro4','PRN3','Principle3','BRND4','brand5','','',2,'sdas',10.2,8.4,6.2,0,'2017/06/13',15);");

            db.execSQL("INSERT INTO Tr_TabStock values(1,'S1','PRN1','BRND1','ITM1','batch1','2017/1/16',10.2,8.2,20,'2016/7/1')");
            db.execSQL("INSERT INTO Tr_TabStock values(2,'S2','PRN1','BRND2','ITM1','batch1','2017/1/16',10.2,8.2,20,'2016/7/1')");
            db.execSQL("INSERT INTO Tr_TabStock values(3,'S3','PRN2','BRND3','ITM2','batch1','2017/1/16',10.2,8.2,20,'2016/7/1')");
            db.execSQL("INSERT INTO Tr_TabStock values(4,'S4','PRN2','BRND4','ITM3','batch1','2017/1/16',10.2,8.2,20,'2016/7/1')");
            db.execSQL("INSERT INTO Tr_TabStock values(5,'S5','PRN3','BRND5','ITM4','batch1','2017/1/16',10.2,8.2,20,'2016/7/1')");

            //db.execSQL("INSERT INTO ");

            db.execSQL("CREATE TABLE Mst_Banks (_id INTEGER PRIMARY KEY AUTOINCREMENT,BankNameID TEXT,BankName TEXT,IsActive INTEGER,LastUpdateDate TEXT)");

            db.execSQL("INSERT INTO Mst_Banks (BankNameID,BankName ,IsActive ,LastUpdateDate)" +
                    "VALUES ('BNK1','Sampath',0,'2017/3/16')");
            db.execSQL("INSERT INTO Mst_Banks (BankNameID,BankName ,IsActive ,LastUpdateDate)" +
                    "VALUES ('BNK2','commercial',0,'2017/3/16')");

            db.execSQL("CREATE TABLE Mst_CreditDays (_id INTEGER PRIMARY KEY AUTOINCREMENT,CreditDaysID TEXT,CreditDays INTEGER,IsActive INTEGER,LastUpdateDate TEXT)");

            db.execSQL("INSERT INTO Mst_CreditDays(CreditDaysID,CreditDays,IsActive,LastUpdateDate) " +
                    "VALUES('CRDT1',30,0,'2017/03/26')");
            db.execSQL("INSERT INTO Mst_CreditDays(CreditDaysID,CreditDays,IsActive,LastUpdateDate) " +
                    "VALUES('CRDT2',15,0,'2017/03/26')");
            db.execSQL("INSERT INTO Mst_CreditDays(CreditDaysID,CreditDays,IsActive,LastUpdateDate) " +
                    "VALUES('CRDT3',5,0,'2017/03/26')");

            /**
            insertProduct("ITM1","pro1","PRN1","PRN1","brandid","brand","","",2,"sdas",10.2,8.4,6.2,0,"2017/06/13",15);
            insertProduct("ITM2","pro2","PRN2","PRN2","brandid","brand","","",2,"sdas",10.2,8.4,6.2,0,"2017/06/13",15);
            insertProduct("ITM3","pro3","PRN3","PRN3","brandid","brand","","",2,"sdas",10.2,8.4,6.2,0,"2017/06/13",15);
            insertProduct("ITM4","pro4","PRN3","PRN4","brandid","brand","","",2,"sdas",10.2,8.4,6.2,0,"2017/06/13",15);


            inserToStockView("S1","PRN1","brandid","ITM1","batch1","2017/1/16",10.2,8.2,20,"2016/7/1");
            inserToStockView("S2","PRN1","brandid","ITM1","batch2","2017/1/17",10.2,8.2,20,"2016/7/1");
            inserToStockView("S3","PRN2","brandid","ITM2","batch1","2017/1/16",10.2,8.2,20,"2016/7/1");
            inserToStockView("S4","PRN3","brandid","ITM3","batch1","2017/1/16",10.2,8.2,20,"2016/7/1");
            inserToStockView("S5","PRN4","brandid","ITM4","batch1","2017/1/16",10.2,8.2,20,"2016/7/1");
             */
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        //no second schema yet so left empty
    }


    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String PRODUCT_TABLE_NAME = "Mst_ProductMaster";
    public static final String PRODUCT_COLUMN_ID = "id";
    public static final String PRODUCT_COLUMN_ITEMCODE = "itemcode";
    public static final String PRODUCT_COLUMN_DESCRIPTION = "description";
    public static final String PRODUCT_COLUMN_PRINCIPLEID = "principleid";
    public static final String PRODUCT_COLUMN_PRINCIPLE = "Principle";
    public static final String PRODUCT_COLUMN_BRANDID = "brandid";
    public static final String PRODUCT_COLUMN_BRAND = "Brand";
    public static final String PRODUCT_COLUMN_SUBBRAND_ID = "subbrandid";
    public static final String PRODUCT_COLUMN_SUBBRAND = "subbrand";
    public static final String PRODUCT_COLUMN_UNITSZIE = "unitsize";
    public static final String PRODUCT_COLUMN_UNITNAME = "unitname";
    public static final String PRODUCT_COLUMN_RETAILPRICE = "retailprice";
    public static final String PRODUCT_COLUMN_SELLINGPRICE = "sellingprice";
    public static final String PRODUCT_COLUMN_BUYING_PRICE = "buyingprice";
    public static final String PRODUCT_COLUMN_ACTIVE = "active";
    public static final String PRODUCT_COLUMN_LAST_UPDATE_DATE = "lastupdatedate";
    public static final String PRODUCT_COLUMN_TARGET_ALLOW = "targetallow";

    private HashMap hp;







    public String insertProduct (String itemcode, String description, String principleid, String principle,String brandid,
                                 String brand,String subbrandid,String subbrand,int unitsize, String unitname,double retailprice,
                                 double sellingprice,double buyingprice,int active,String lastupDate, int targetallow) {
        String result="failDBclass";
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("itemcode", itemcode);
            contentValues.put("description", description);
            contentValues.put("principleid", principleid);
            contentValues.put("principle", principle);
            contentValues.put("brandid", brandid);
            contentValues.put("brand", brand);
            contentValues.put("subbrandid", subbrandid);
            contentValues.put("subbrand", subbrand);
            contentValues.put("unitsize", unitsize);
            contentValues.put("unitname", unitname);
            contentValues.put("retailprice", retailprice);
            contentValues.put("sellingprice",sellingprice);
            contentValues.put("buyingprice", buyingprice);
            contentValues.put("active", active);
            contentValues.put("lastupdatedate", lastupDate);
            contentValues.put("targetallow", targetallow);

            if(db.insert("mst_productmaster", null, contentValues)>0) {
                result = "success";
            }else
                result ="outer_if";

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
        db.update("mst_productmaster", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("mst_productmaster",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllbrands() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT DISTINCT brand FROM Mst_ProductMaster", null );
        res.moveToFirst();

        array_list.add("All");
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(PRODUCT_COLUMN_BRAND)));
            res.moveToNext();
        }
        return array_list;
    }
    public   ArrayList<String> getAllprinciples() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT DISTINCT principle FROM mst_productmaster", null );
        res.moveToFirst();

        array_list.add("All");
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(PRODUCT_COLUMN_PRINCIPLE)));
            res.moveToNext();
        }
        return array_list;
    }


}