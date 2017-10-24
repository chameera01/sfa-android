package com.example.ahmed.sfa.controllers.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ahmed.sfa.controllers.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 10/25/2017.
 * for invoice gen
 */
public class ExpireWarning {

    private static final String KEY_ROW_ID = "row_id";
    private static final String KEY_COSTOMER_NO= "COSTOMER_NO";
    private static final String KEY_DEALER_ID= "DEALER_ID";
    private static final String KEY_REP_ID = "REP_ID";
    private static final String KEY_DATE = "DATE";
    private static final String KEY_PRODUCT_CODE = "PRODUCT_CODE";
    private static final String KEY_SHELF_STOCK = "SHELF_STOCK";
    private static final String KEY_EXPIRE_DATE = "EXPIRE_DATE";
    private static final String KEY_BATCH_CODE= "BATCH_CODE";
    private static final String KEY_CONTACT_NO= "CONTACT_NO";
    private static final String KEY_IS_UPLOAD= "IS_UPLOAD";

    String[] columns = new String[]{KEY_ROW_ID, KEY_COSTOMER_NO, KEY_DEALER_ID, KEY_REP_ID,KEY_DATE ,KEY_PRODUCT_CODE,KEY_SHELF_STOCK ,KEY_EXPIRE_DATE,
            KEY_BATCH_CODE,KEY_CONTACT_NO,KEY_IS_UPLOAD};

    private static final String TABLE_NAME = "ExpireWarning";
    private static final String COLLECTION_NOTE_CREATE = "CREATE TABLE " + TABLE_NAME
            + " (" + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_COSTOMER_NO + " TEXT NOT NULL,"
            + KEY_DEALER_ID + " TEXT ,"
            + KEY_REP_ID + " TEXT ,"
            + KEY_DATE + " TEXT ,"
            + KEY_PRODUCT_CODE + " TEXT ,"
            + KEY_SHELF_STOCK + " TEXT ,"
            + KEY_EXPIRE_DATE + " TEXT ,"
            + KEY_BATCH_CODE + " TEXT ,"
            + KEY_CONTACT_NO + " TEXT ,"
            + KEY_IS_UPLOAD + " TEXT " + " );";


    public final Context customerContext;
    public DBHelper databaseHelper;
    private SQLiteDatabase database;


    public ExpireWarning(Context c) {
        customerContext = c;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(COLLECTION_NOTE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public ExpireWarning openWritableDatabase() throws SQLException {
        databaseHelper = new DBHelper(customerContext);
        database = databaseHelper.getWritableDatabase();
        return this;

    }

    public ExpireWarning openReadableDatabase() throws SQLException {
        databaseHelper = new DBHelper(customerContext);
        database = databaseHelper.getReadableDatabase();
        return this;

    }

    public void closeDatabase() throws SQLException {
        databaseHelper.close();
    }

    public long insert_Branch(String KEY_COSTOMER_NO1, String KEY_DEALER_ID1, String KEY_REP_ID1, String KEY_DATE1,
                              String KEY_PRODUCT_CODE1, String KEY_SHELF_STOCK1,  String KEY_EXPIRE_DATE1,
                              String KEY_BATCH_CODE1 ,String KEY_CONTACT_NO1,String KEY_IS_UPLOAD1) throws SQLException {

        ContentValues cv = new ContentValues();
        cv.put(KEY_COSTOMER_NO, KEY_COSTOMER_NO1);
        cv.put(KEY_DEALER_ID, KEY_DEALER_ID1);
        cv.put(KEY_REP_ID, KEY_REP_ID1);
        cv.put(KEY_DATE, KEY_DATE1);
        cv.put(KEY_PRODUCT_CODE, KEY_PRODUCT_CODE1);
        cv.put(KEY_SHELF_STOCK, KEY_SHELF_STOCK1);
        cv.put(KEY_EXPIRE_DATE, KEY_EXPIRE_DATE1);
        cv.put(KEY_BATCH_CODE, KEY_BATCH_CODE1);
        cv.put(KEY_CONTACT_NO, KEY_CONTACT_NO1);
        cv.put(KEY_IS_UPLOAD, KEY_IS_UPLOAD1);
        return database.insert(TABLE_NAME, null, cv);

    }

    public List<String[]> getSendWarningByUploadStatus(String status) {
        List<String[]> invoice = new ArrayList<String[]>();

        Cursor cursor = database.query(TABLE_NAME,
                columns, KEY_IS_UPLOAD + " = '" + status + "'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] invoiceData = new String[10];
            invoiceData[0] = cursor.getString(0);
            invoiceData[1] = cursor.getString(1);
            invoiceData[2] = cursor.getString(2);
            invoiceData[3] = cursor.getString(3);
            invoiceData[4] = cursor.getString(4);
            invoiceData[5] = cursor.getString(5);
            invoiceData[6] = cursor.getString(6);
            invoiceData[7] = cursor.getString(7);
            invoiceData[8] = cursor.getString(8);
            invoiceData[9] = cursor.getString(9);

            invoice.add(invoiceData);
            cursor.moveToNext();
        }

        cursor.close();

        Log.w("invoice size", "inside : " + invoice.size());

        return invoice;
    }
    public void setrUploadedStatus(String custId, String status) {

        String updateQuery = "UPDATE " + TABLE_NAME + " SET "
                + KEY_IS_UPLOAD + " = '" + status + "' WHERE " + KEY_ROW_ID
                + " = " + custId;

        database.execSQL(updateQuery);
        Log.w("Upload service", "<Invoice> Set invoice uploaded status to :"
                + status + " of id : " + custId + "");
    }




}

