package controllers.database;

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
 * Created by DELL on 3/2/2017.
 */
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

    private HashMap hp;

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
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
        db.execSQL("INSERT INTO Tr_TabStock VALUES (1,'server_id','principle_1','bran_1','cd001','bct_1','2017-10-25',45.50,60.5,895,'2017-02-25');");

        db.execSQL(
                "create table Mst_SupplierTable" +
                        "(_ID integer primary key AUTOINCREMENT , PrincipleID  text,Principle text," +
                        "Activate  integer,LastUpdateDate text)"
        );
        db.execSQL(
                "create table Mst_ProductBrandManagement" +
                        "(_ID integer primary key AUTOINCREMENT ,BrandID text, PrincipleID  text,Principle text," +
                        "MainBrand text,Activate integer,LastUpdateDate text)"
        );



        //only few columns were addesto Tr_NewCustome tables
        db.execSQL(
                "create table Tr_NewCustomer" +
                        "(_ID integer primary key AUTOINCREMENT ,NewCustomerID text,CustomerName text,Address text,Area text,Town text,OwnerContactNo text," +
                        "IsUpload text,ApproveStatus text)"
        );



        //adding datato  table 21 TrNewCustomer
        db.execSQL("INSERT INTO Tr_NewCustomer VALUES (1,'cus_001','peachnet','addre ','area_dalugama','dalugama','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer VALUES (2,'cus_002','healthycafe','addre is goes here','area_dalugama','bambalapitiya','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer VALUES (3,'cus_003','thilakawardhana','addre ','area_kiribathgoda','kiribathgoda','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer VALUES (4,'cus_004','kandy','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer VALUES (5,'cus_005','thilakawardhana','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
/*
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
                " VALUES ('cus_001','peachnet','addre','area_dalugama','dalugama','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)" +
                "VALUES ('cus_002','healthycafe','addre is goes here','area_dalugama','bambalapitiya','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
                " VALUES ('cus_003','thilakawardhana','addre ','area_kiribathgoda','kiribathgoda','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+"" +
                "VALUES ('cus_004','kandy','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
                "VALUES ('cus_005','thilakawardhana','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer(NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
                " VALUES ('cus_006','disStore','addre ','area_dalugama','dalugama','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
               " VALUES ('cus_007','ABD','addre is goes here','area_dalugama','bambalapitiya','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
                "VALUES ('cus_008','DSI','addre ','area_kiribathgoda','kiribathgoda','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
                "VALUES ('cus_009','Newkandy','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
                "VALUES ('cus_010','Newthilakawardhana','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
                "VALUES ('cus_011','niharika','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
                "VALUES ('cus_010','Newthilakawardhana','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
                "VALUES ('cus_010','Newthilakawardhana','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
                "VALUES ('cus_010','Newthilakawardhana','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
                "VALUES ('cus_010','Newthilakawardhana','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)"+
                "VALUES ('cus_010','Newthilakawardhana','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
        db.execSQL("INSERT INTO Tr_NewCustomer (NewCustomerID,CustomerName,Address,Area,Town,OwnerContactNo,IsUpload,ApproveStatus)" +
                "VALUES ('cus_010','Newthilakawardhana','addre ','area_kadawatha','kadawatha','071562895','uploaded','pending');");
*/



        //tr_new cstomer table 23
        db.execSQL(
                "create table Tr_ItineraryDetails" +
                        "(_ID integer primary key AUTOINCREMENT ,ItineraryID text,ItineraryDate text,CustomerNo text,IsPlaned integer,IsInvoiced integer," +
                        "LastUpdateDate  text)"
        );


        //db.execSQL("INSERT INTO route VALUES (2,'cd002','descrp_2','principle_2','newphama','bran_1','belcocid','subid_2','sub_name',45,'unitname',50f,60f,30f,'1','','1');");
        //creating the customer table
        db.execSQL("CREATE TABLE Mst_Customermaster (_ID integer primary key AUTOINCREMENT," +
                "CustomerNo TEXT,CustomerName TEXT,Address TEXT,DistrictID TEXT,District TEXT,AreaID TEXT," +
                "Area TEXT,Town TEXT,Telephone TEXT,Fax TEXT,Email Text, BRno TEXT,OwnerContactNo TEXT," +
                "OwnerName TEXT,PhamacyRegNo TEXT,CreditLimit TEXT,CurrentCreditAmount TEXT,CustomerStatus TEXT" +
                ",InsertDate TEXT,RouteID TEXT,RouteName TEXT,ImageID TEXT,Latitude TEXT,Longitude TEXT,CompanyCode TEXT," +
                "IsActive INTEGER,LastUpdateDate TEXT);");
        //adding data to table 17 ,Mst_Customermaster table

        db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Address,Area,Town,Telephone,RouteName,InsertDate)" +
                "VALUES ('CUS1','aksa','Dalugama Kelaniya','Kiribathgoda','Kelaniya','0715624895','route_name_kadawatha','2017-03-16');");
        db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Address,Area,Town,Telephone,RouteName,InsertDate)" +
                "VALUES ('CUS2','DiscountStore','Dalugama Kelaniya','Kiribathgoda','Dalugama','0715685495','route_name_Kiribathgoda','2017-03-17');");
        db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Address,Area,Town,Telephone,RouteName,InsertDate)" +
                "VALUES ('CUS3','peachNet','Dalugama Kelaniya','Kiribathgoda','Dalugama','0725685495','route_name_Kiribathgoda','2017-03-18');");
        db.execSQL("INSERT INTO Mst_Customermaster (CustomerNo,CustomerName,Address,Area,Town,Telephone,RouteName,InsertDate)" +
                "VALUES ('CUS4','Thilakawardhana','Dalugama Kelaniya','Kiribathgoda','Kiribathgoda','0725656235','route_name_kadawatha','2017-03-17');");
//  add data to mst_ProductMaster

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Mst_ProductMaster");
        onCreate(db);
    }

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
                result ="outer_if";

        }catch (SQLException e){
            e.printStackTrace();
            result=e.getMessage();
        }

            return result;

    }


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

    public   ArrayList<String> getAllbrands() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery("SELECT DISTINCT Brand FROM Mst_ProductMaster", null );
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
        Cursor res =  db.rawQuery("SELECT DISTINCT Principle FROM Mst_ProductMaster", null );
        res.moveToFirst();

        array_list.add("All");
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(PRODUCT_COLUMN_PRINCIPLE)));
            res.moveToNext();
        }
        return array_list;
    }

    /*this is added for test AndroidDatabaseManage.java*/
    public ArrayList<Cursor> getArryListUniMethod(String ...qry) {
        String query=qry[0];
        String columnName=qry[1];

        ArrayList<Cursor> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery(query, null );
        res.moveToFirst();


        while(res.isAfterLast() == false){
            array_list.add(res);
            res.moveToNext();
        }
        db.close();
        return array_list;

    }



}