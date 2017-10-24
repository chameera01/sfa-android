package com.example.ahmed.sfa.controllers.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ahmed.sfa.controllers.database.DBHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by DELL on 10/22/2017.
 */
public class CustomerProductAvg {
    public static final String TABLE_NAME = "customer_product_average";
    private static final String KEY_ID = "id";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_TOTAL_QTY = "total_qty";
    private static final String KEY_INVOICE_COUNT = "invoice_count";
    private static final String KEY_TIMESTAMP = "time_stamp";
    private final String[] columns = {KEY_ID,
            KEY_CUSTOMER_ID,
            KEY_PRODUCT_ID,
            KEY_TOTAL_QTY,
            KEY_INVOICE_COUNT,
            KEY_TIMESTAMP};
    private static String CUSTOM_PRODUCT_AVG_CREATE = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
            .append(TABLE_NAME).append(" (")
            .append(KEY_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
            .append(KEY_CUSTOMER_ID).append(" TEXT,")
            .append(KEY_PRODUCT_ID).append(" TEXT,")
            .append(KEY_TOTAL_QTY).append(" INTEGER,")
            .append(KEY_INVOICE_COUNT).append(" INTEGER,")
            .append(KEY_TIMESTAMP).append(" TEXT").append(")").toString();
    private Context customerProductAvgContext;
    private DBHelper databaseHelper;
    private SQLiteDatabase database;

    public CustomerProductAvg(Context c) {
        customerProductAvgContext = c;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CUSTOM_PRODUCT_AVG_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public CustomerProductAvg openWritableDatabase() throws SQLException {
        databaseHelper = new DBHelper(customerProductAvgContext);
        database = databaseHelper.getWritableDatabase();
        return this;

    }

    public CustomerProductAvg openReadableDatabase() throws SQLException {
        databaseHelper = new DBHelper(customerProductAvgContext);
        database = databaseHelper.getReadableDatabase();
        return this;

    }

    public void closeDatabase() throws SQLException {
        databaseHelper.close();
    }

    public long insertCustomerProductAvg(String customerId, String productId, int totalQty, int invoiceCount) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_CUSTOMER_ID, customerId);
        cv.put(KEY_PRODUCT_ID, productId);
        cv.put(KEY_TOTAL_QTY, totalQty);
        cv.put(KEY_INVOICE_COUNT, invoiceCount);
        cv.put(KEY_TIMESTAMP, new Date().toString());

        return database.insert(TABLE_NAME, null, cv);
    }

    public int updateCustomerProductAverage(String customerId, String productId, int totalQty, int invoiceCount) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TOTAL_QTY, totalQty);
        cv.put(KEY_INVOICE_COUNT, invoiceCount);

        return database.update(TABLE_NAME, cv, KEY_CUSTOMER_ID + "=? AND " + KEY_PRODUCT_ID + "=?", new String[]{customerId, productId});
    }

    public int getAverage(String customerId, String productId) {
        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_TOTAL_QTY, KEY_INVOICE_COUNT}, KEY_CUSTOMER_ID + "=? AND " + KEY_PRODUCT_ID + "=?", new String[]{customerId, productId}, null, null, null);
        int average = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                int totalQty = cursor.getInt(0);
                int invoiceCount = cursor.getInt(1);
                average = Math.round(totalQty / invoiceCount);
            }
        }
        return average;
    }

    public ArrayList<Integer> getInvoiceCount(String customerId, String productId) {
        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_TOTAL_QTY, KEY_INVOICE_COUNT}, KEY_PRODUCT_ID + "=? AND " + KEY_CUSTOMER_ID + "=?", new String[]{productId, customerId}, null, null, null);
        ArrayList<Integer> list = new ArrayList<Integer>();

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                int totalQty = cursor.getInt(0);
                int invoiceCount = cursor.getInt(1);
                list.add(totalQty);
                list.add(invoiceCount);
            }
        }

        return list;
    }

    public boolean hasCustomerBoughtProduct(String customerId, String productId) {
        Cursor cursor = database.query(TABLE_NAME, columns, KEY_CUSTOMER_ID + "=? AND " + KEY_PRODUCT_ID + "=?", new String[]{customerId, productId}, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                return true;
            }
        }
        return false;
    }

    public List<String[]> getProductList(String pharmacyId) {
        List<String[]> productList = new ArrayList<String[]>();
        String query = new StringBuilder()
                .append("SELECT ")
                .append(TABLE_NAME).append(".").append(KEY_ID).append(", ")
                .append("products.pro_des, ")
                .append(TABLE_NAME).append(".").append(KEY_TOTAL_QTY).append(", ")
                .append(TABLE_NAME).append(".").append(KEY_INVOICE_COUNT).append(", ")
                .append(TABLE_NAME).append(".").append(KEY_PRODUCT_ID).append(" ")
                .append("FROM ").append(TABLE_NAME).append(" ")
                .append("INNER JOIN products ON products.code").append("=").append(TABLE_NAME).append(".").append(KEY_PRODUCT_ID)
                .append(" WHERE ").append(KEY_CUSTOMER_ID).append("=?")
                .append(" ORDER BY ").append("products.force").toString();


        Log.e(CustomerProductAvg.class.getSimpleName(), "query : " + query);

        Cursor cursor = database.rawQuery(query, new String[]{pharmacyId});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String[] productDetails = new String[5];
                    productDetails[0] = String.valueOf(cursor.getInt(0));
                    productDetails[1] = cursor.getString(1);
                    productDetails[2] = String.valueOf(cursor.getInt(2));
                    productDetails[3] = String.valueOf(cursor.getInt(3));
                    productDetails[4] = cursor.getString(4);
                    productList.add(productDetails);
                    cursor.moveToNext();
                }
            } else {
                Log.e(CustomerProductAvg.class.getSimpleName(), "Cursor size 0");
            }
        } else {
            Log.e(CustomerProductAvg.class.getSimpleName(), "Cursor null");
        }
        return productList;
    }


    public List<String[]> getSearchedProductList(String pharmacyId, String searchStr) {
        List<String[]> productList = new ArrayList<String[]>();
        String query = new StringBuilder()
                .append("SELECT ")
                .append(TABLE_NAME).append(".").append(KEY_ID).append(", ")
                .append("products.pro_des, ")
                .append(TABLE_NAME).append(".").append(KEY_TOTAL_QTY).append(", ")
                .append(TABLE_NAME).append(".").append(KEY_INVOICE_COUNT).append(", ")
                .append(TABLE_NAME).append(".").append(KEY_PRODUCT_ID).append(" ")
                .append("FROM ").append(TABLE_NAME).append(" ")
                .append("INNER JOIN products ON products.code").append("=").append(TABLE_NAME).append(".").append(KEY_PRODUCT_ID)
                .append(" WHERE ").append(KEY_CUSTOMER_ID).append("=").append("'").append(pharmacyId).append("'")
                .append(" AND ").append("products.pro_des ").append("LIKE ").append("'%").append(searchStr).append("%'")
                .append(" ORDER BY ").append("products.force").toString();


        Log.e(CustomerProductAvg.class.getSimpleName(), "query : " + query);

        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String[] productDetails = new String[5];
                    productDetails[0] = String.valueOf(cursor.getInt(0));
                    productDetails[1] = cursor.getString(1);
                    productDetails[2] = String.valueOf(cursor.getInt(2));
                    productDetails[3] = String.valueOf(cursor.getInt(3));
                    productDetails[4] = cursor.getString(4);
                    productList.add(productDetails);
                    cursor.moveToNext();
                }
            } else {
                Log.e(CustomerProductAvg.class.getSimpleName(), "Cursor size 0");
            }
        } else {
            Log.e(CustomerProductAvg.class.getSimpleName(), "Cursor null");
        }
        return productList;
    }

    public List<String> getInvoicedProductIds(String pharmacyId) {
        List<String> productList = new ArrayList<String>();
        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_PRODUCT_ID}, KEY_TOTAL_QTY + "> 0 AND " + KEY_CUSTOMER_ID + "=?", new String[]{pharmacyId}, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    productList.add(cursor.getString(0));
                    cursor.moveToNext();
                }
            } else {
                Log.e(CustomerProductAvg.class.getSimpleName(), "Cursor size 0");
            }
        } else {
            Log.e(CustomerProductAvg.class.getSimpleName(), "Cursor null");
        }
        return productList;
    }


}
