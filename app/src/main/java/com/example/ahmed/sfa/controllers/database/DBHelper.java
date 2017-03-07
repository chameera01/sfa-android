package com.example.ahmed.sfa.controllers.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                    ",IsPlanned,IsInvoiced) VALUES ('IT1','2017/3/7','CUS1',1,1);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT2','2017/3/7','CUS2',1,1);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT3','2017/3/7','CUS3',1,0);");
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT4','2017/3/7','CUS4',1,0);");//this one is planned but invoiced
            db.execSQL("INSERT INTO Tr_ItineraryDetails(ItineraryID ,ItineraryDate ,CustomerNo " +
                    ",IsPlanned,IsInvoiced) VALUES ('IT5','2017/3/5','CUS5',1,0);");//this one is for next day


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
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        //no second schema yet so left empty
    }

}