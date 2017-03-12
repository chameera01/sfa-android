package com.example.ahmed.sfa.controllers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
            db.execSQL("INSERT INTO Customer_Images(CustomerNo,CustomerImageName) VALUES ('CUS1','CUS_IMG_1')");
            db.execSQL("INSERT INTO Customer_Images(CustomerNo,CustomerImageName) VALUES ('CUS2','CUS_IMG_2')");
            db.execSQL("INSERT INTO Customer_Images(CustomerNo,CustomerImageName) VALUES ('CUS3','CUS_IMG_3')");
            db.execSQL("INSERT INTO Customer_Images(CustomerNo,CustomerImageName) VALUES ('CUS4','CUS_IMG_4')");
            db.execSQL("INSERT INTO Customer_Images(CustomerNo,CustomerImageName) VALUES ('CUS5','CUS_IMG_5')");

            //INSERT VALUES TO THE ITINERARY DETAILS TABLE
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT1','2017/3/12','CUS1',1,1);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT2','2017/3/12','CUS2',1,1);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT3','2017/3/12','CUS3',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT4','2017/3/12','CUS4',1,0);");//this one is planned but invoiced
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT5','2017/3/11','CUS5',1,0);");//this one is for next day


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

            db.execSQL(
                    "create table mst_productmaster " +
                            "(_id integer primary key AUTOINCREMENT , itemcode text,description text,principleid text, principle text," +
                            "brandid text,brand text,subbrandid text,subbrand text,unitsize integer,unitname text,retailprice real," +
                            "sellingprice real,buyingprice real,active integer,lastupdatedate text,targetallow integer)"
            );
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        //no second schema yet so left empty
    }


    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String PRODUCT_TABLE_NAME = "mst_productmaster";
    public static final String PRODUCT_COLUMN_ID = "id";
    public static final String PRODUCT_COLUMN_ITEMCODE = "itemcode";
    public static final String PRODUCT_COLUMN_DESCRIPTION = "description";
    public static final String PRODUCT_COLUMN_PRINCIPLEID = "principleid";
    public static final String PRODUCT_COLUMN_PRINCIPLE = "principle";
    public static final String PRODUCT_COLUMN_BRANDID = "brandid";
    public static final String PRODUCT_COLUMN_BRAND = "brand";
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
        Cursor res =  db.rawQuery( "SELECT DISTINCT brand FROM mst_productmaster", null );
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