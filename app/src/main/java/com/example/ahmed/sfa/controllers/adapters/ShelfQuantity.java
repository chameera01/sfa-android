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
 * created for invoice gen
 */
public class ShelfQuantity {
    private static final String KEY_ROW_ID = "row_id";
    private static final String KEY_INVOICE_NO = "invoice_no";
    private static final String KEY_INVOICE_DATE = "invoice_date";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_BATCH = "batch";
    private static final String KEY_AVAILABLE_STOCK = "available_stock";
    private static final String KEY_TIME_STAMP = "time_stamp";
    private static final String KEY_IS_UPLOADED = "is_uploaded";

    String[] columns = {KEY_ROW_ID, KEY_INVOICE_NO, KEY_INVOICE_DATE, KEY_CUSTOMER_ID, KEY_PRODUCT_ID, KEY_BATCH,
            KEY_AVAILABLE_STOCK, KEY_TIME_STAMP, KEY_IS_UPLOADED};

    private static final String TABLE_NAME = "shelf_quantity";
    private static final String SHELF_QUANTITY_CREATE = "CREATE TABLE " + TABLE_NAME
            + " (" + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_INVOICE_NO + " TEXT NOT NULL,"
            + KEY_INVOICE_DATE + " TEXT ,"
            + KEY_CUSTOMER_ID + " TEXT ,"
            + KEY_PRODUCT_ID + " TEXT ,"
            + KEY_BATCH + " TEXT ,"
            + KEY_AVAILABLE_STOCK + " TEXT , "
            + KEY_TIME_STAMP + " TEXT ," + KEY_IS_UPLOADED + " TEXT " + " );";
    public final Context shelfQuantityContext;
    public DBHelper databaseHelper;
    private SQLiteDatabase database;

    public ShelfQuantity(Context c) {
        shelfQuantityContext = c;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(SHELF_QUANTITY_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public ShelfQuantity openWritableDatabase() throws SQLException {
        databaseHelper = new DBHelper(shelfQuantityContext);
        database = databaseHelper.getWritableDatabase();
        return this;

    }

    public ShelfQuantity openReadableDatabase() throws SQLException {
        databaseHelper = new DBHelper(shelfQuantityContext);
        database = databaseHelper.getReadableDatabase();
        return this;

    }

    public void closeDatabase() throws SQLException {
        databaseHelper.close();
    }

    public long insertShelfQuantity(String invoiceNo, String invoiceDate, String customerId, String productId, String batch,
                                    String availableStock, String timeStamp, String isUploaded
    ) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(KEY_INVOICE_NO, invoiceNo);
        cv.put(KEY_INVOICE_DATE, invoiceDate);
        cv.put(KEY_CUSTOMER_ID, customerId);
        cv.put(KEY_PRODUCT_ID, productId);
        cv.put(KEY_BATCH, batch);
        cv.put(KEY_AVAILABLE_STOCK, availableStock);
        cv.put(KEY_TIME_STAMP, timeStamp);
        cv.put(KEY_IS_UPLOADED, isUploaded);

        Log.w("insertShelfQty ", "insertShelfQty : " + availableStock);

        return database.insert(TABLE_NAME, null, cv);

    }

    public List<String[]> getShelfQuantitiesByStatus(String status) {
        List<String[]> rtnProducts = new ArrayList<String[]>();


        Log.w("invoice size", "status : " + status);

//		Cursor cursor = database.query(TABLE_NAME,
//				columns, KEY_UPLOADED_STATUS+" = ?", new String[]{status}, null, null, null);

        Cursor cursor = database.query(TABLE_NAME,
                columns, KEY_IS_UPLOADED + " = '" + status + "'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] invoiceData = new String[9];
            invoiceData[0] = cursor.getString(0);
            invoiceData[1] = cursor.getString(1);
            invoiceData[2] = cursor.getString(2);
            invoiceData[3] = cursor.getString(3);
            invoiceData[4] = cursor.getString(4);
            invoiceData[5] = cursor.getString(5);
            invoiceData[6] = cursor.getString(6);
            invoiceData[7] = cursor.getString(7);
            invoiceData[8] = cursor.getString(8);

            rtnProducts.add(invoiceData);
            cursor.moveToNext();
        }

        cursor.close();

        Log.w("invoice size", "inside : " + rtnProducts.size());

        return rtnProducts;
    }

    public void setShelfQtyUploadedStatus(String shelfQtyProductId, String status) {

        String updateQuery = "UPDATE " + TABLE_NAME
                + " SET "
                + KEY_IS_UPLOADED
                + " = '"
                + status
                + "' WHERE "
                + KEY_ROW_ID
                + " = "
                + shelfQtyProductId;

        database.execSQL(updateQuery);
        Log.w("Upload service", "<Invoice> Set invoice uploaded status to :" + status + " of id : " + shelfQtyProductId + "");
    }
}

