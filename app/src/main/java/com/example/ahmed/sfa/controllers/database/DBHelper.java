package com.example.ahmed.sfa.controllers.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.models.CollectionChequeDetails;
import com.example.ahmed.sfa.models.CollectionNoteDetails;
import com.example.ahmed.sfa.models.CollectionNoteHeader;
import com.example.ahmed.sfa.models.InvoiceSummary;
import com.example.ahmed.sfa.models.InvoiceSummaryFromSalesDetails;
import com.example.ahmed.sfa.models.InvoiceSummaryFromSalesHeader;
import com.example.ahmed.sfa.models.InvoiceType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ahmed on 3/3/2017.
 */

//this class handles the creation and upgrade functionality
public class DBHelper extends SQLiteOpenHelper {
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
    private static final String DB_NAME = "sfa.db";//this is the name of the database created in the phone
    private static final int VERSION = 1;
    private static final String TAG = "HISTORY";
    private String iid;
    private HashMap hp;
    public DBHelper(Context ctxt) {
        super(ctxt,DB_NAME,null,VERSION);//error handler shld be implemented
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        try{
		/************Added by asanka*/
		db.execSQL("create table  Mst_Territory (_ID integer primary key AUTOINCREMENT,TerritoryID text,Territory text,IsActive integer,LastUpdateDate text)");
				/*create table to save Active Status*/
        db.execSQL("CREATE TABLE DeviceCheckController (_ID integer primary key AUTOINCREMENT ,DeviceID text,Password text,ACTIVESTATUS text)");
            //add data DeviceCheckController
          //  db.execSQL("INSERT INTO DeviceCheckController (DeviceID,Password,ACTIVESTATUS) values('T1','123456','YES')");
		/*create table for sales rep*/
        db.execSQL("CREATE TABLE Mst_RepTable " +
                    "(_ID integer primary key AUTOINCREMENT,RepID text,DeviceName text,RepName text," +
                    "Address text,ContactNo text,DealerName text,DealerAddress text,MacAddress text,AgentID text,IsActive integer,LastUpdateDate text )");


            /******************end new Add*/
            //creating the itinerarydetails table
            db.execSQL("CREATE TABLE Tr_ItineraryDetails (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ItineraryID TEXT,ItineraryDate TEXT,CustomerNo TEXT,IsPlanned INTEGER,IsInvoiced INTEGER,LastUpdateDate TEXT );");

            //creating the customer table
            db.execSQL("CREATE TABLE Mst_Customermaster (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "CustomerNo TEXT,CustomerName TEXT,Address TEXT,DistrictID TEXT,District TEXT,AreaID TEXT," +
                    "Area TEXT,Town TEXT,Telephone TEXT,Fax TEXT,Email Text, BRno TEXT,OwnerContactNo TEXT," +
                    "OwnerName TEXT,PhamacyRegNo TEXT,CreditLimit real,CurrentCreditAmount real,CustomerStatus TEXT" +
                    ",InsertDate TEXT,RouteID TEXT,RouteName TEXT,ImageID TEXT,Latitude real,Longitude real,CompanyCode TEXT," +
                    "IsActive INTEGER,LastUpdateDate TEXT,uploadStatus INTEGER DEFAULT 0,IsCashCustomer INTEGER DEFAULT 0);");

            //creating Customer status Table
            db.execSQL("CREATE TABLE Mst_CustomerStatus (_id INTEGER PRIMARY KEY AUTOINCREMENT,StatusID TEXT,Status TEXT,isActive INTEGER,LastUpdateDate TEXT);");

            //creating the master District Table
            db.execSQL("CREATE TABLE Mst_District (_id INTEGER PRIMARY KEY AUTOINCREMENT,DistrictId TEXT,DistrictName TEXT,isActive INTEGER,LastUpdateDate TEXT);");

            //creating master route table
            db.execSQL("CREATE TABLE Mst_Route (_id INTEGER PRIMARY KEY AUTOINCREMENT,RouteID TEXT,TerritoryID TEXT,Territory TEXT,Route TEXT,isActive INTEGER,LastUpdateDate TEXT);");

            //Create a table to save the images relavent to customers
            db.execSQL("CREATE TABLE Customer_Images (_id INTEGER PRIMARY KEY AUTOINCREMENT,CustomerNo TEXT,CustomerImageName TEXT);");

            //create a table for temporary customer details
            db.execSQL("CREATE TABLE Tr_NewCustomer (_id INTEGER PRIMARY KEY AUTOINCREMENT,NewCustomerID text,CustomerName TEXT,Address TEXT,District TEXT,Area TEXT," +
                    "Town TEXT,Telephone TEXT,Fax TEXT,Email TEXT,BRno TEXT,OwnerContactNo TEXT,OwnerName TEXT,PharmacyRegNo TEXT," +
                    "CreditLimit INTEGER,CustomerStatusID TEXT,CustomerStatus TEXT,InsertDate TEXT,RouteID TEXT,RouteName TEXT," +
                    "ImageID TEXT,Latitude REAL,Longitude REAL,isUpload INTEGER,UploadDate TEXT,ApproveStatus INTEGER,LastUpdateDate TEXT);");

            //insert into Tr_NewCustomer
           // db.execSQL("INSERT INTO Tr_NewCustomer(NewCustomerID,CustomerName,Address,Town,Area,isUpload,ApproveStatus) values('tmp_cus_1','tmp_cusname','myadd','mytown','area_51',0,0);");


            //create Tr_DailyRouteDetails
            db.execSQL("CREATE TABLE Tr_DailyRouteDetails (_id INTEGER PRIMARY KEY AUTOINCREMENT,SerialCode TEXT,Date TEXT,ItineraryID TEXT,CustomerNo TEXT,IsPlanned INTEGER,IsInvoiced INTEGER,InvoiceNo INTEGER,Reasons TEXT,Comment TEXT,IsUpload TEXT,UploadDate TEXT)");

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
                    ",SubTotal REAL,InvoiceTotal REAL,FullDiscountRate REAL,DiscountAmount REAL,DiscountType REAL, IsOnInvoiceReturn INTEGER,OnInvoiceReturnNo TEXT" +
                    ",OnInvoiceReturnValue REAL,CreditAmount REAL,CashAmount REAL,ChequeAmount REAL,Isprint INTEGER,ProductCount INTEGER" +
                    ",InvoiceType TEXT,Latitude REAL,Longitude REAL,IsUpload INTEGER,UploadDate TEXT,CashDiscount REAL)");


            db.execSQL("CREATE TABLE Tr_SalesReturn(_id INTEGER PRIMARY KEY AUTOINCREMENT, ItineraryID TEXT," +
                    "CustomerNo TEXT,InvoiceNo INTEGER,InvoiceDate TEXT,PaymentTime TEXT,SubTotal REAL," +
                    "InvoiceTotal REAL,FullDiscountRate REAL,DiscountAmount REAL,DiscountType TEXT," +
                    "IsOnInvoiceReturn INTEGER,Isprint INTEGER,ProductCount INTEGER,InvoiceType TEXT,Latitude REAL,Longitude " +
                    "REAL,IsUpload INTEGER,UploadDate TEXT)");


            //insert data into reasons table
           /*db.execSQL("INSERT INTO Mst_Reasons(ReasonsID,Reason,isActive) VALUES('RSN1','Reason 1',1);");
            db.execSQL("INSERT INTO Mst_Reasons(ReasonsID,Reason,isActive) VALUES('RSN2','Reason 2',0);");
            db.execSQL("INSERT INTO Mst_Reasons(ReasonsID,Reason,isActive) VALUES('RSN3','Reason 3',0);");
            */


            //insert data into sales header
//            db.execSQL("INSERT INTO Tr_SalesHeader(CustomerNo,InvoiceNo,InvoiceDate,InvoiceTotal,CreditAmount) VALUES ('38249','INV1','04/08/2017',12000.00,500.00);");
//            db.execSQL("INSERT INTO Tr_SalesHeader(CustomerNo,InvoiceNo,InvoiceDate,InvoiceTotal,CreditAmount) VALUES ('38326','INV2','04/08/2017',12020.02,500.01);");
//            db.execSQL("INSERT INTO Tr_SalesHeader(CustomerNo,InvoiceNo,InvoiceDate,InvoiceTotal,CreditAmount) VALUES ('38327','INV3','04/08/2017',12030.02,530.01);");
//            db.execSQL("INSERT INTO Tr_SalesHeader(CustomerNo,InvoiceNo,InvoiceDate,InvoiceTotal,CreditAmount) VALUES ('CUS3','INV3','04/08/2017',12030.02,530.01);");
//            db.execSQL("INSERT INTO Tr_SalesHeader(CustomerNo,InvoiceNo,InvoiceDate,InvoiceTotal,CreditAmount) VALUES ('CUS3','INV3','04/07/2017',12030.02,530.01);");
//            db.execSQL("INSERT INTO Tr_SalesHeader(CustomerNo,InvoiceNo,InvoiceDate,InvoiceTotal,CreditAmount) VALUES ('CUS1','INV3','04/07/2017',12030.02,530.01);");
//            db.execSQL("INSERT INTO Tr_SalesHeader(CustomerNo,InvoiceNo,InvoiceDate,InvoiceTotal,CreditAmount) VALUES ('CUS2','INV3','04/07/2017',12030.02,530.01);");
//            db.execSQL("INSERT INTO Tr_SalesHeader(CustomerNo,InvoiceNo,InvoiceDate,InvoiceTotal,CreditAmount) VALUES ('CUS4','INV3','04/07/2017',12030.02,530.01);");
//

           /*
            //insert data into mst_checkinoutpoints
            db.execSQL("INSERT INTO Mst_CheckInOutPoints (ServerID,Type,PointDescription,isActive) VALUES ('p1','IN','WareHouse 1',0)");
            db.execSQL("INSERT INTO Mst_CheckInOutPoints (ServerID,Type,PointDescription,isActive) VALUES ('p2','IN','WareHouse 2',0)");
            db.execSQL("INSERT INTO Mst_CheckInOutPoints (ServerID,Type,PointDescription,isActive) VALUES ('p3','IN','WareHouse 3',1)");
            db.execSQL("INSERT INTO Mst_CheckInOutPoints (ServerID,Type,PointDescription,isActive) VALUES ('p4','OUT','WareHouse 4',0)");
            db.execSQL("INSERT INTO Mst_CheckInOutPoints (ServerID,Type,PointDescription,isActive) VALUES ('p4','OUT','WareHouse 5',0)");
            */

            //add data to the DailyRouteDetails
            //db.execSQL("INSERT INTO Tr_DailyRouteDetails (SerialCode ,Date,ItineraryID ,CustomerNo ,IsPlanned ,IsInvoiced ,InvoiceNo ,Reasons ,Comment ,IsUpload ,UploadDate) VALUES ('SER1','2017/3/10','IT1','CUS1',0,1) ");

            /*
            //adding data to District table
            db.execSQL("INSERT INTO Mst_District(DistrictId,DistrictName,isActive,LastUpdateDate) VALUES('DST1','Matale',1,'2017/3/2');");
            db.execSQL("INSERT INTO Mst_District(DistrictId,DistrictName,isActive,LastUpdateDate) VALUES('DST2','Colombo',1,'2017/3/2');");
            db.execSQL("INSERT INTO Mst_District(DistrictId,DistrictName,isActive,LastUpdateDate) VALUES('DST3','Kandy',1,'2017/3/2');");
            */
            /*
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
           /* db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +

                    ",IsPlanned,IsInvoiced) VALUES ('IT1','04/25/2017','CUS1',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT2','04/25/2017','CUS2',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT3','04/25/2017','CUS3',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT4','04/25/2017','CUS4',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT5','04/25/2017','CUS5',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT6','04/25/2017','CUS6',1,2);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT7','04/25/2017','CUS7',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT8','04/25/2017','CUS8',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT9','04/25/2017','CUS9',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT10','04/25/2017','CUS10',1,0);");


            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT2','04/08/2017','CUS2',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT3','04/08/2017','CUS3',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT4','04/08/2017','CUS4',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT5','04/08/2017','CUS5',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT6','04/09/2017','CUS6',1,2);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT7','04/09/2017','CUS7',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT8','04/09/2017','CUS8',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT9','04/09/2017','CUS9',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT10','04/09/2017','CUS10',1,0);");
*/


            /*
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
                    "VALUES ('CUS11','rose','Town5');");
            db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Town)" +
                    "VALUES ('CUS12','rose','Town5');");

            */

            //Testing customer data
            db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Town)" +
                    "VALUES ('CUS25','BB Retailers','Town5');");


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
            /*
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
                            "SellingPrice real,BuyingPrice real,Active integer,LastUpdateDate text,TargetAllow  integer,SortOrder real DEFAULT 0.0)"
            );


            db.execSQL(
                    "create table Tr_TabStock" +
                            "(_ID integer primary key AUTOINCREMENT ,ServerID  text,PrincipleID text,BrandID text,ItemCode text,BatchNumber text," +
                            "ExpiryDate text,SellingPrice real,RetailPrice real," +
                            "Qty  integer,LastUpdateDate text)");
            //db.execSQL("INSERT INTO Tr_TabStock VALUES (1,'server_id','principle_1','bran_1','cd001','bct_1','2017-10-25',45.50,60.5,895,'2017-02-25');");

            db.execSQL("CREATE TABLE Mst_SupplierTable(_id INTEGER PRIMARY KEY AUTOINCREMENT,PrincipleID TEXT, Principle TEXT, Activate INTEGER, LastUpdateDate TEXT)");

            /*
            db.execSQL("INSERT INTO Mst_SupplierTable VALUES(1,'PRN1','Principle1',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_SupplierTable VALUES(2,'PRN2','Principle2',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_SupplierTable VALUES(3,'PRN3','Principle3',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_SupplierTable VALUES(4,'PRN4','Principle4',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_SupplierTable VALUES(5,'PRN5','Principle5',0,'2017/1/1')");
            */

            db.execSQL(
                    "create table Mst_ProductBrandManagement" +
                            "(_ID integer primary key AUTOINCREMENT ,BrandID text, PrincipleID  text,Principle text," +
                            "MainBrand text,Activate integer,LastUpdateDate text)"
            );

            /*

            db.execSQL("INSERT INTO Mst_ProductBrandManagement VALUES(1,'BRND1','PRN1','Principle1','Brand1',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_ProductBrandManagement VALUES(2,'BRND2','PRN1','Principle1','Brand2',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_ProductBrandManagement VALUES(3,'BRND3','PRN2','Principle2','Brand3',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_ProductBrandManagement VALUES(4,'BRND4','PRN2','Principle2','Brand4',0,'2017/1/1')");
            db.execSQL("INSERT INTO Mst_ProductBrandManagement VALUES(5,'BRND5','PRN3','Principle3','Brand5',1,'2017/1/1')");
            */




            /*

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
            */
            //db.execSQL("INSERT INTO ");

            db.execSQL("CREATE TABLE Mst_Banks (_id INTEGER PRIMARY KEY AUTOINCREMENT,BankNameID TEXT,BankName TEXT,IsActive INTEGER,LastUpdateDate TEXT)");

            /*
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
            db.execSQL("CREATE TABLE Tr_SalesDetails (_id INTEGER PRIMARY KEY AUTOINCREMENT,HeaderID TEXT,ItemCode TEXT,UnitPrice REAL" +
                    ",BatchNumber TEXT,ExpiryDate TEXT,DiscountRate REAL,DiscountAmount REAL,IssueMode INTEGER,ShelfQty INTEGER" +
                    ",RequestQty INTEGER,OrderQty INTEGER,FreeQty INTEGER,Total REAL,IsUpload INTEGER,UploadDate TEXT)");

            db.execSQL("CREATE TABLE Tr_InvoiceOutstanding (_id INTEGER PRIMARY KEY AUTOINCREMENT,SerialCode TEXT,InvoiceDate TEXT," +
                    "InvoiceNo TEXT,CustomerNo TEXT,InvoiceTotalValue REAL,CreditValue REAL,CurrentCreditValue REAL,CreditDays INTEGER,LastUpdateDate TEXT," +
                    "LastUpdateAmount REAL,LastUpdateType INTEGER,InsertDate TEXT,IsUpload TEXT,IsUpdate TEXT);");

            db.execSQL("CREATE TABLE ChequeDetails (_id INTEGER PRIMARY KEY AUTOINCREMENT,SerialCode TEXT,InvoiceDate TEXT,InvoiceNo TEXT,CustomerNo TEXT" +
                    ",InvoiceTotalValue REAL,ChequeAmount REAL,ChequeNumber TEXT,BankName TEXT,CollectedDate TEXT,RealizedDate TEXT,IsUpload INTEGER" +
                    ",IsUpdate INTEGER,Status TEXT,StatusUpdateDate TEXT)");

            db.execSQL("CREATE TABLE Mst_InvoiceNumbers_Management(_id INTEGER PRIMARY KEY AUTOINCREMENT,InvoiceNo INTEGER,InvoiceReturnNo INTEGER,CollectionNoteNo INTEGER,LastUpdateDate TEXT)");

            db.execSQL("CREATE TABLE Collections (_id INTEGER PRIMARY KEY AUTOINCREMENT, CreditDays INTEGER,CreditValue REAL, CurrentCreditValue REAL, CustomerName TEXT, CustomerNo TEXT, InvoiceDate TEXT, InvoiceNo TEXT, InvoiceTotalValue REAL, RepID TEXT, RepName TEXT)");

            //Move
            db.execSQL("CREATE TABLE Mst_DeviceRepDetails(_id INTEGER PRIMARY KEY AUTOINCREMENT,DeviceID TEXT,RepID TEXT, RepName TEXT)");
//            db.execSQL("INSERT INTO Mst_DeviceRepDetails(_id,DeviceID ,RepID, RepName) VALUES(1,'c1','93','c1')");
//            db.execSQL("INSERT INTO Mst_DeviceRepDetails(_id,DeviceID ,RepID, RepName) VALUES(2,'c1','99','t1')");
//            db.execSQL("INSERT INTO Mst_DeviceRepDetails(_id,DeviceID ,RepID, RepName) VALUES(3,'t1','99','t1')");

            db.execSQL("CREATE TABLE Collection_Header(_id INTEGER PRIMARY KEY AUTOINCREMENT,CollectionNoteNo TEXT, CollectedDate TEXT, Type TEXT, CustomerNo TEXT, IsUpload INTEGER)");
            db.execSQL("CREATE TABLE Cash_Collection_Details (_id INTEGER PRIMARY KEY AUTOINCREMENT, HeaderID TEXT, CollectionNoteNo TEXT,RepID TEXT, Route TEXT, InvoiceNo TEXT, CreditAmount REAL, Cash REAL, Cheque REAl,Balance REAL, CreditRemaining REAL, IsUpload INTEGER )");
            db.execSQL("CREATE TABLE Cheque_Collection_Details (_id INTEGER PRIMARY KEY AUTOINCREMENT, HeaderID TEXT, CollectionNoteNo TEXT,RepID TEXT, InvoiceNo TEXT, ChequeAmount REAL, ChequeNo TEXT,Bank TEXT, Branch TEXT, RealizeDate TEXT, IsUpload INTEGER)");


            /**Shani created this on 13/12/2017**/

            db.execSQL("CREATE TABLE Tr_PrincipleDiscount(_ID INTEGER  PRIMARY KEY AUTOINCREMENT,InvoiceId TEXT, CustomerNo TEXT,Date TEXT,PrincipleID TEXT,Principle TEXT,Value REAL,DiscountRate REAL,DiscountValue REAL, IsUpload INTEGER Default 0)");

            db.execSQL("CREATE TABLE Tr_TargetData (_id INTEGER PRIMARY KEY AUTOINCREMENT,ServerID TEXT,Date TEXT,Month TEXT,TargetValue INTEGER)");

            //Table for saving temporary collection note payment types

//            db.execSQL("CREATE TABLE Temp_CNote_Payment_Types(_id INTEGER  PRIMARY KEY AUTOINCREMENT, InvoiceNo TEXT, Type TEXT)");

            db.execSQL("CREATE TABLE Temp_CNote_Payment_Types(_id INTEGER  PRIMARY KEY AUTOINCREMENT, InvoiceNo TEXT, Type TEXT, CollectionNoteNo TEXT)");


            db.execSQL("CREATE TABLE Temp_LastSelected_Collection(_id INTEGER  PRIMARY KEY AUTOINCREMENT,LastSelectedCustomer TEXT, LastRemaining REAL)");

            /*
            db.execSQL("INSERT INTO Tr_TargetData (ServerID,Date,Month,TargetValue) VALUES('TG1','04/09/2017','April',35000)");
            db.execSQL("INSERT INTO Tr_TargetData (ServerID,Date,Month,TargetValue) VALUES('TG2','04/08/2017','April',20000)");
            db.execSQL("INSERT INTO Tr_TargetData (ServerID,Date,Month,TargetValue) VALUES('TG3','04/07/2017','April',15000)");
            db.execSQL("INSERT INTO Tr_TargetData (ServerID,Date,Month,TargetValue) VALUES('TG3','04/06/2017','April',15000)");
            db.execSQL("INSERT INTO Tr_TargetData (ServerID,Date,Month,TargetValue) VALUES('TG3','04/05/2017','April',15000)");
            db.execSQL("INSERT INTO Tr_TargetData (ServerID,Date,Month,TargetValue) VALUES('TG3','04/04/2017','April',15000)");
            db.execSQL("INSERT INTO Tr_TargetData (ServerID,Date,Month,TargetValue) VALUES('TG4','03/05/2017','March',50000)");
            db.execSQL("INSERT INTO Tr_TargetData (ServerID,Date,Month,TargetValue) VALUES('TG5','03/34/2017','March',30000)");
            */

            db.execSQL("CREATE TABLE Tr_SalesReturnDetails(_ID INTEGER  PRIMARY KEY AUTOINCREMENT,HeaderID INTEGER,ItemCode TEXT," +
                    "UnitPrice REAL,BatchNumber REAL,ExpiryDate TEXT,DiscountRate REAL,DiscountAmount REAL," +
                    "IssueMode TEXT,OrderQty INTEGER,FreeQty INTEGER,Total REAL,IsUpload INTEGER,UploadDate TEXT)");
            db.execSQL("CREATE TABLE Mst_CreditDays (_ID INTEGER PRIMARY KEY AUTOINCREMENT,CreditDaysID TEXT,CreditDays INTEGER," +
                    "IsActive INTEGER DEFAULT 0,LastUpdateDate TEXT)");
            db.execSQL("INSERT INTO Mst_CreditDays(CreditDaysID,CreditDays,IsActive,LastUpdateDate)" +
                    "VALUES ('ID1',20,0,'19/09/2017')");
            db.execSQL("INSERT INTO Mst_CreditDays(CreditDaysID,CreditDays,IsActive,LastUpdateDate)" +
                    "VALUES ('ID2',30,0,'19/09/2017')");
            db.execSQL("INSERT INTO Mst_CreditDays(CreditDaysID,CreditDays,IsActive,LastUpdateDate)" +
                    "VALUES ('ID3',10,0,'19/09/2017')");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        //no second schema yet so left empty
    }

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
            Log.d("CUSTOMER", "inside get data_ex: " + e.getLocalizedMessage());
            e.printStackTrace();
            return null ;
        }

    }

    //Move
    public ArrayList<String> getCollectionReps(Context context) {

        ArrayList<String> reps = new ArrayList<>();
        DBAdapter db2 = new DBAdapter(context);

        db2.openDB();
        Cursor rep = db2.runQuery("SELECT RepName FROM Mst_DeviceRepDetails WHERE lower(DeviceID) =(SELECT DeviceID FROM DeviceCheckController)");

        if (rep.getCount() != 0 && rep.moveToFirst()) {
            while (!rep.isAfterLast()) {
                reps.add(rep.getString(rep.getColumnIndex("RepName")));
                Log.d("COL", rep.getString(rep.getColumnIndex("RepName")));
                rep.moveToNext();
            }
        }
        db2.closeDB();

        return reps;
    }


    //Move
    public ArrayList<CollectionCustomer> getCollectionCustomers(String rep, Context context) {

        Log.d("COL", "inside dbhelper getCollectionCustomers");
        ArrayList<CollectionCustomer> cus = new ArrayList<>();
        DBAdapter db = new DBAdapter(context);
        Log.d("COL", "rep_" + rep);

        db.openDB(); //no invoice outstanding table yet-therefore temp from collections
        Cursor cursor = db.runQuery("SELECT DISTINCT CustomerName, CustomerNo FROM Collections WHERE RepID = (SELECT RepID FROM Mst_RepTable WHERE LOWER(RepName) = LOWER('" + rep + "'))"); //get this correct

        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                CollectionCustomer cc = new CollectionCustomer();
                cc.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                cc.setCustomerNo(cursor.getString(cursor.getColumnIndex("CustomerNo")));
                Log.d("COL", "Inside getcus_" + cursor.getString(cursor.getColumnIndex("CustomerName")));
                cus.add(cc);
                cursor.moveToNext();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

            builder.setTitle("No outstanding customers found!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .create().show();
        }
        db.closeDB();
        return cus;
    }

    public ArrayList<CollectionInvoice> getCollectionInvoiceNos(String cusName, String rep, Context context) {

        ArrayList<CollectionInvoice> cinvoices = new ArrayList<>();
        DBAdapter db3 = new DBAdapter(context);

        db3.openDB();
        Cursor c3 = db3.runQuery("SELECT InvoiceNo, CurrentCreditValue FROM Collections WHERE CustomerName = '" + cusName + "' "); //add rep filter too after getting the case sensitivie issue right
        if (c3.getCount() != 0 && c3.moveToFirst()) {
            while (!c3.isAfterLast()) {
                CollectionInvoice ci = new CollectionInvoice();
                ci.setInvNo(c3.getString(c3.getColumnIndex("InvoiceNo")));
                ci.setCcredit(c3.getDouble(c3.getColumnIndex("CurrentCreditValue")));
                cinvoices.add(ci);
                c3.moveToNext();
            }
        }

        db3.closeDB();
        return cinvoices;
    }

    public void insertCollectionHeader(CollectionNoteHeader cn, Context context) {

        Log.d("COL", "inside insertCollectionHeader");
        DBAdapter adapter = new DBAdapter(context);
        SQLiteDatabase db = getWritableDatabase();
        adapter.openDB();
        db.execSQL("INSERT INTO Collection_Header(CollectionNoteNo, CollectedDate, Type, CustomerNo,IsUpload) VALUES ('" + cn.getCollectionNoteNo() + "','" + cn.getCollectedDate() + "','" + cn.getType() + "','" + cn.getCustomerNo() + "','" + cn.getIsUpload() + "')");
        adapter.closeDB();

    }

    public int getLastCollectionHeaderID(Context context) {

        DBAdapter db = new DBAdapter(context);
        int id = -1;
        db.openDB();
        Cursor c = db.runQuery("SELECT _id FROM Collection_Header");
        if (c.getCount() != 0 && c.moveToLast()) {
            id = c.getInt(0);
        }
        Log.d("COL", "last header ID: " + id);
        return id;
    }

    public int getCollectionNoteNo(Context context) {
        DBAdapter db = new DBAdapter(context);
        db.openDB();
        Cursor cursor = db.runQuery("SELECT CollectionNoteNo FROM Mst_InvoiceNumbers_Management");
        if (cursor.moveToNext()) {
            int val = cursor.getInt(0);
            db.closeDB();
            return val;
        } else {
            Log.w("COL", "db error");
            return -1;
        }
    }

    public boolean updateCollectionNoteNo(Context context) {
        int ccno = getCollectionNoteNo(context);
        DBAdapter db = new DBAdapter(context);
        SQLiteDatabase sqldb = getWritableDatabase();
        int newccno = ccno + 1;
        db.openDB();
        sqldb.execSQL("UPDATE Mst_InvoiceNumbers_Management SET CollectionNoteNo = '" + newccno + "' WHERE CollectionNoteNo = '" + ccno + "'");
        db.closeDB();
        return true;

    }

    public String getCNoteNofromHeader(int hid, Context context) {

        DBAdapter db = new DBAdapter(context);
        Toast.makeText(context, "inside getNO: hid: " + hid, Toast.LENGTH_LONG).show();
        String cnno = "";
        db.openDB();
        Cursor c = db.runQuery("SELECT CollectionNoteNo FROM Collection_Header WHERE _id = '" + hid + "'");
        if (c.getCount() != 0 && c.moveToFirst()) {
            cnno = c.getString(0);
        }
        Toast.makeText(context, "inside getNO: cnno: " + cnno, Toast.LENGTH_LONG).show();
        return cnno;

    }

    public String getSelectedRepId(String selectedRep, Context context) {

        DBAdapter db = new DBAdapter(context);
        String repid = "";
        db.openDB();
        Cursor cr = db.runQuery("SELECT RepID FROM Mst_RepTable WHERE LOWER(RepName) = LOWER('" + selectedRep + "')");
        if (cr.getCount() != 0 && cr.moveToFirst()) {
            repid = cr.getString(0);
        }

        return repid;

    }

    public Boolean insertCollectionDetails(ArrayList<CollectionNoteDetails> cndlist, int headerId, String colNo, Context context) {

        Log.d("COL", "inside insertCollectionDetails");
        DBAdapter dbAdapter = new DBAdapter(context);
        SQLiteDatabase db = getWritableDatabase();
        dbAdapter.openDB();

        try {
            for (CollectionNoteDetails cnd : cndlist) {
                db.execSQL("INSERT INTO Cash_Collection_Details (HeaderID , CollectionNoteNo ,RepID , Route , InvoiceNo , CreditAmount , Cash , Cheque ,Balance , CreditRemaining , IsUpload) VALUES ('" + headerId + "','" + colNo + "','" + cnd.getRepId() + "','" + cnd.getRoute() + "','" + cnd.getInvoiceNo() + "','" + cnd.getCredit() + "','" + cnd.getCash() + "','" + cnd.getCheque() + "','" + cnd.getBalance() + "','" + cnd.getRemaining() + "','" + cnd.getIsUpload() + "')");
            }
            dbAdapter.closeDB();
            return true;
        } catch (Exception e) {
            Log.d("COL", "ex: " + e.getLocalizedMessage());
            return false;
        }
    }

    public Boolean insertCollectionChequeDetails(ArrayList<CollectionChequeDetails> ccdlist, int headerId, String colNo, Context context) {

        DBAdapter dbAdapter = new DBAdapter(context);
        SQLiteDatabase db = getWritableDatabase();
        dbAdapter.openDB();

        try {
            for (CollectionChequeDetails ccd : ccdlist) {
                db.execSQL("INSERT INTO Cheque_Collection_Details (HeaderID, CollectionNoteNo ,RepID , InvoiceNo , ChequeAmount , ChequeNo,Bank , Branch, RealizeDate , IsUpload) VALUES ('" + headerId + "','" + colNo + "','" + ccd.getRepId() + "','" + ccd.getInvoiceNo() + "','" + ccd.getChequeAmt() + "','" + ccd.getChequeNo() + "','" + ccd.getBank() + "','" + ccd.getBranch() + "','" + ccd.getRealizeDate() + "','" + ccd.getIsUpload() + "')");
            }
            dbAdapter.closeDB();
            return true;
        } catch (Exception e) {
            Log.d("COL", "ex: " + e.getLocalizedMessage());
            return false;
        }
    }

    public void updateCreditBalance(String selectedInvoice, double balance, Context context) {

        DBAdapter adapter = new DBAdapter(context);
        SQLiteDatabase db = getWritableDatabase();

        Log.d("COL", "inside update balance_" + balance);

        adapter.openDB();
        db.execSQL("UPDATE Collections SET CurrentCreditValue = '" + balance + "' WHERE InvoiceNo = '" + selectedInvoice + "'");
    }

    public String checkLastCNNo() {

        String lastCnno = "";

        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery("SELECT CollectionNoteNo FROM Temp_CNote_Payment_Types", null);

        if (cursor != null && cursor.moveToLast()) {
            lastCnno = cursor.getString(cursor.getColumnIndex("CollectionNoteNo"));
        }

        return lastCnno;

    }

    public void removeData(Context context) {

        DBAdapter adapter = new DBAdapter(context);
        SQLiteDatabase db = getWritableDatabase();

        Log.d("COL", "inside removeData");

        adapter.openDB();
        db.execSQL("DELETE FROM Temp_CNote_Payment_Types");
        adapter.closeDB();
    }

    public ArrayList<String> getAllbrands() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery("SELECT DISTINCT Brand FROM Mst_ProductMaster", null );
        res.moveToFirst();

        array_list.add("All");
        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(PRODUCT_COLUMN_BRAND)));
            res.moveToNext();
        }
        return array_list;
    }

    public   ArrayList<String> getAllprinciples() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT DISTINCT Principle FROM Mst_ProductMaster", null );
        res.moveToFirst();

        array_list.add("All");
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(PRODUCT_COLUMN_PRINCIPLE)));
            res.moveToNext();
        }
        return array_list;
    }

    //method for accessing all customers
    public ArrayList<String> getAllCustomers() {
        ArrayList<String> allCustomers = new ArrayList<String>();

        SQLiteDatabase sqldb = this.getReadableDatabase();
        Cursor cursor = sqldb.rawQuery("SELECT CustomerName FROM Mst_Customermaster ", null);
        cursor.moveToFirst();

        //allCustomers.add("All");
        while (cursor.isAfterLast() == false) {
            allCustomers.add(cursor.getString(cursor.getColumnIndex("CustomerName")));
            cursor.moveToNext();
        }
        return allCustomers;
    }

    public InvoiceSummary[] getInvoiceDetails(String customerName) {

        Log.d(TAG, customerName);
        ArrayList<InvoiceSummary> iDetails = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT Tr_SalesHeader.InvoiceNo, Tr_SalesHeader.InvoiceDate, Tr_SalesHeader.InvoiceTotal  FROM Tr_SalesHeader INNER JOIN Mst_Customermaster ON Tr_SalesHeader.CustomerNo = Mst_Customermaster.CustomerNo WHERE Mst_Customermaster.CustomerName = '" + customerName + "'", null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            // The Cursor is now set to the right position
            String id, date, value;
            id = c.getString(c.getColumnIndex("InvoiceNo"));
            date = c.getString(c.getColumnIndex("InvoiceDate"));
            value = c.getString(c.getColumnIndex("InvoiceTotal"));
            Log.d(TAG, id + "_" + date + "_" + value);
            iDetails.add(new InvoiceSummary(id, date, value, null, null));

        }

        InvoiceSummary[] array = iDetails.toArray(new InvoiceSummary[iDetails.size()]);

        Log.d(TAG, customerName);
        return array;
    }

    public ArrayList<InvoiceSummary> getReturnDetails(String customerName) {

        ArrayList<InvoiceSummary> returnDetails = new ArrayList<>();
        InvoiceSummary is = new InvoiceSummary("", "", "", "", "");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT Tr_SalesHeader.OnInvoiceReturnNo, Tr_SalesHeader.InvoiceDate, Tr_SalesHeader.OnInvoiceReturnValue  FROM Tr_SalesHeader INNER JOIN Mst_Customermaster ON Tr_SalesHeader.CustomerNo = Mst_Customermaster.CustomerNo WHERE Mst_Customermaster.CustomerName = '" + customerName + "' AND Tr_SalesHeader.IsOnInvoiceReturn = 1", null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            String retNo, retDate, retValue;
            retNo = c.getString(c.getColumnIndex("OnInvoiceReturnNo"));
            retDate = c.getString(c.getColumnIndex("InvoiceDate"));
            retValue = c.getString(c.getColumnIndex("OnInvoiceReturnValue"));
            Log.d(TAG, retNo + "_" + retDate + "_" + retValue);
            is = new InvoiceSummary(null, retDate, null, retNo, retValue);
            returnDetails.add(is);

        }
        Log.d(TAG, is.getReturnNo() + "_" + is.getReturnValue());
        return returnDetails;

    }

    public ArrayList<InvoiceSummaryFromSalesDetails> getSavedInvoiceDetails(String invoiceNo) {

        Log.d(TAG, "inside the sales details method");

        ArrayList<InvoiceSummaryFromSalesDetails> fromSalesDetails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query2 = "SELECT Tr_SalesDetails.ItemCode, \n" +
                "        Mst_ProductMaster.Description, \n" +
                "        Tr_SalesDetails.UnitPrice, \n" +
                "        Tr_SalesDetails.BatchNumber, \n" +
                "        Tr_SalesDetails.ShelfQty, \n" +
                "        Tr_SalesDetails.RequestQty, \n" +
                "        Tr_SalesDetails.OrderQty, \n" +
                "        Tr_SalesDetails.FreeQty, \n" +
                "        Tr_SalesDetails.Total \n" +
                "\n" +
                "FROM Tr_SalesDetails    INNER JOIN Tr_SalesHeader ON Tr_SalesHeader._id = Tr_SalesDetails.HeaderID \n" +
                "                        INNER JOIN Mst_ProductMaster ON Mst_ProductMaster.ItemCode = Tr_SalesDetails.ItemCode \n" +
                "WHERE Tr_SalesHeader.InvoiceNo = '" + invoiceNo + "'";

        Cursor cursor2 = db.rawQuery(query2, null);

        for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) {

            Log.d(TAG, "inside the cursor for loop");
            String code, product, batchNo, uPrice, shelf, request, order, freeLine, lineVal;

            code = cursor2.getString(cursor2.getColumnIndex("ItemCode"));
            product = cursor2.getString(cursor2.getColumnIndex("Description"));
            batchNo = cursor2.getString(cursor2.getColumnIndex("BatchNumber"));
            uPrice = cursor2.getString(cursor2.getColumnIndex("UnitPrice"));
            //add stock
            shelf = cursor2.getString(cursor2.getColumnIndex("ShelfQty"));
            request = cursor2.getString(cursor2.getColumnIndex("ItemCode"));
            order = cursor2.getString(cursor2.getColumnIndex("OrderQty"));
            freeLine = cursor2.getString(cursor2.getColumnIndex("FreeQty"));
            lineVal = cursor2.getString(cursor2.getColumnIndex("Total"));

            Log.d("HISTORY", code + "_" + product + "_" + uPrice + "_" + lineVal);

            fromSalesDetails.add(new InvoiceSummaryFromSalesDetails(code, product, batchNo, uPrice, shelf, request, order, freeLine, lineVal));

        }

        return fromSalesDetails;
    }

    public InvoiceSummaryFromSalesHeader getSavedInvoiceHeaderDetails(String invoiceNo) {

        Log.d(TAG, "inside the sales header method");
        InvoiceSummaryFromSalesHeader isflh = new InvoiceSummaryFromSalesHeader("", "", "", "", "", "", "", "");

        SQLiteDatabase db = this.getReadableDatabase();

        String query1 = "SELECT  Tr_SalesHeader.SubTotal,\n" +
                "        Tr_SalesHeader.InvoiceTotal, \n" +
                "        Tr_SalesHeader.FullDiscountRate, \n" +
                "        Tr_SalesHeader.DiscountAmount,\n" +
                "        Tr_SalesHeader.CreditAmount,\n" +
                "        Tr_SalesHeader.CashAmount,\n" +
                "        Tr_SalesHeader.ChequeAmount\n" +
                "\n" +
                "FROM Tr_SalesHeader WHERE Tr_SalesHeader.InvoiceNo = '" + invoiceNo + "'";

        Cursor cursor1 = db.rawQuery(query1, null);

        for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

            // The Cursor is now set to the right position
            String subTot, invoiceTot, discRate, disc, free, credit, cash, cheque;

            subTot = cursor1.getString(cursor1.getColumnIndex("SubTotal"));
            invoiceTot = cursor1.getString(cursor1.getColumnIndex("InvoiceTotal"));
            discRate = cursor1.getString(cursor1.getColumnIndex("FullDiscountRate"));
            disc = cursor1.getString(cursor1.getColumnIndex("DiscountAmount"));
            //add free
            credit = cursor1.getString(cursor1.getColumnIndex("CreditAmount"));
            cash = cursor1.getString(cursor1.getColumnIndex("CashAmount"));
            cheque = cursor1.getString(cursor1.getColumnIndex("ChequeAmount"));

            Log.d(TAG, subTot + "_" + invoiceTot + "_" + discRate + "_" + disc);

            isflh = new InvoiceSummaryFromSalesHeader(subTot, invoiceTot, discRate, disc, null, credit, cash, cheque);
        }

        return isflh;
    }

    public ArrayList<String> getCollectionRoutes() {

        ArrayList<String> colroutes = new ArrayList<String>();

        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery("SELECT Route FROM Mst_Route ", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            colroutes.add(cursor.getString(cursor.getColumnIndex("Route")));
            cursor.moveToNext();
        }
        return colroutes;
    }

    public ArrayList<String> getCollectionPaymentType(String invno) {
        ArrayList<String> typelist = new ArrayList<>();

        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery("SELECT Type FROM Temp_CNote_Payment_Types WHERE InvoiceNo = '" + invno + "' ", null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            typelist.add(cursor.getString(cursor.getColumnIndex("Type")));
        }
        return typelist;
    }

    public boolean setCollectionPaymentType(ArrayList<InvoiceType> list, String cnno, Context context) {
        DBAdapter adapter = new DBAdapter(context);
        SQLiteDatabase db = getWritableDatabase();

        Log.d("COL", "inside setCollectionPaymentType");

        try {
            adapter.openDB();
            for (InvoiceType it : list) {
                db.execSQL("INSERT INTO Temp_CNote_Payment_Types(InvoiceNo, Type,CollectionNoteNo) VALUES('" + it.getInvoice() + "','" + it.getType() + "','" + cnno + "') ");
            }
            adapter.closeDB();
            return true;
        } catch (Exception e) {
            Log.d("COL", "Inside inserting_: " + e.getLocalizedMessage());
            return false;
        }

    }

    public Boolean removeCNPayment(Context context, String invoice, String type) {
        DBAdapter adapter = new DBAdapter(context);
        SQLiteDatabase db = getWritableDatabase();

        Log.d("COL", "inside removeCNPayment: " + invoice);

        try {
            adapter.openDB();
            db.execSQL("DELETE FROM Temp_CNote_Payment_Types WHERE InvoiceNo = '" + invoice + "' AND Type = '" + type + "'");
            adapter.closeDB();
            return true;
        } catch (Exception e) {
            Log.d("COL", "Inside inserting_: " + e.getLocalizedMessage());
            return false;
        }

    }

    public Boolean checkCustomer(String cusno) {

        int customerType = 0;

        try {
            SQLiteDatabase db1 = this.getReadableDatabase();
            Cursor cursor = db1.rawQuery("SELECT IsCashCustomer FROM Mst_Customermaster WHERE CustomerNo = '" + cusno + "' ", null);

            if (cursor.getCount() != 0 && cursor.moveToFirst()) {
                customerType = cursor.getInt(0);
            }
            if (customerType == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("INVOICE", "Ex: " + e.getLocalizedMessage());
            return false;
        }
    }

    public void tempUpdate(Context context) {

        Toast.makeText(context, "inside tempUpdate", Toast.LENGTH_LONG).show();
        SQLiteDatabase db = getWritableDatabase();

        int temp = 250;
        for (int i = 1; i <= temp; i++) {
            String sql = "UPDATE Mst_ProductMaster SET SortOrder = '" + (temp - i) + "' WHERE _ID = '" + i + "'";
            db.execSQL(sql);
        }

        Toast.makeText(context, "inside tempUpdate---finished", Toast.LENGTH_LONG).show();
    }

    public class CollectionInvoice {

        private String invNo;
        private double ccredit;

        public String getInvNo() {
            return invNo;
        }

        public void setInvNo(String invNo) {
            this.invNo = invNo;
        }

        public double getCcredit() {
            return ccredit;
        }

        public void setCcredit(double ccredit) {
            this.ccredit = ccredit;
        }
    }

    public class CollectionCustomer {

        private String customerName;
        private String customerNo;

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerNo() {
            return customerNo;
        }

        public void setCustomerNo(String customerNo) {
            this.customerNo = customerNo;
        }
    }


}