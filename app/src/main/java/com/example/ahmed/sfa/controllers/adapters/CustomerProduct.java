package com.example.ahmed.sfa.controllers.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ahmed.sfa.controllers.database.DBHelper;

/**
 * Created by DELL on 10/25/2017.
 * CREATED FOR INVOICE GEN
 */
public class CustomerProduct {
    private static final String KEY_ROW_ID = "row_id";
    private static final String KEY_PHARMACY_ID = "pharmacy_id";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_BATCH = "batch";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_TIMESTAMP = "timestamp";

    private String[] columns = {KEY_ROW_ID, KEY_PHARMACY_ID, KEY_PRODUCT_ID, KEY_BATCH, KEY_QUANTITY, KEY_TIMESTAMP};

    private static final String TABLE_NAME = "customer_product";
    private static final String CUSTOMER_PRODUCT_CREATE = createCustomerProductCreateStatement();
    private Context customerProductContext;
    private DBHelper databaseHelper;
    private SQLiteDatabase database;

    public CustomerProduct(Context c) {
        customerProductContext = c;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CUSTOMER_PRODUCT_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    private static String createCustomerProductCreateStatement() {
        StringBuilder createStatement = new StringBuilder();
        createStatement.append("CREATE TABLE " + TABLE_NAME + "(");
        createStatement.append(KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
        createStatement.append(KEY_PHARMACY_ID + " TEXT ,");
        createStatement.append(KEY_PRODUCT_ID + " TEXT ,");
        createStatement.append(KEY_BATCH + " TEXT ,");
        createStatement.append(KEY_QUANTITY + " TEXT ,");
        createStatement.append(KEY_TIMESTAMP + " TEXT )");
        return createStatement.toString();
    }

    public CustomerProduct openWritableDatabase() throws SQLException {
        databaseHelper = new DBHelper(customerProductContext);
        database = databaseHelper.getWritableDatabase();
        return this;

    }

    public CustomerProduct openReadableDatabase() throws SQLException {
        databaseHelper = new DBHelper(customerProductContext);
        database = databaseHelper.getReadableDatabase();
        return this;

    }

    public void closeDatabase() throws SQLException {
        databaseHelper.close();
    }

//    public boolean hasCustomerBoughtProductBatch(String pharmacyId, String productId, String batch) {
//    	Cursor cursor = database.query(TABLE_NAME, new String[] {KEY_QUANTITY}, KEY_PHARMACY_ID+"=? AND "+ KEY_PRODUCT_ID+"=? AND " + KEY_BATCH+"=?", new String[] {pharmacyId, productId, batch}, null, null, null);
//    	boolean purchased = false;
//    	if (cursor != null) {
//    		if (cursor.getCount() != 0) {
//    			purchased = true;
//    		} else {
//    			Log.e("Customer Product", "cursor size is 0");
//    		}
//    	}
//    	return purchased;
//    }

    public long insertCustomerProduct(String pharmacyId, String productId, String quantity, String timestamp) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_PHARMACY_ID, pharmacyId);
        cv.put(KEY_PRODUCT_ID, productId);
        cv.put(KEY_BATCH, "");
        cv.put(KEY_QUANTITY, quantity);
        cv.put(KEY_TIMESTAMP, timestamp);
        return database.insert(TABLE_NAME, null, cv);
    }

//    public int getInvoicedQuantityForCustomer(String pharmacyId, String productId, String batch) {
////    	Cursor cursor = database.query(TABLE_NAME, new String[] {KEY_QUANTITY}, KEY_PHARMACY_ID+"=? AND "+ KEY_PRODUCT_ID+"=? AND " + KEY_BATCH+"=?", new String[] {pharmacyId, productId, batch}, null, null, null);
//    	StringBuilder selectStringBuilder = new StringBuilder();
//    	selectStringBuilder.append("SELECT " + KEY_QUANTITY + " ");
//    	selectStringBuilder.append("FROM " + TABLE_NAME + " ");
//    	selectStringBuilder.append("WHERE ");
//    	selectStringBuilder.append(KEY_PHARMACY_ID + "='" + pharmacyId + "' AND ");
//    	selectStringBuilder.append(KEY_PRODUCT_ID + "='" + productId + "' AND ");
//    	selectStringBuilder.append(KEY_BATCH + "='" + batch+ "'");
//
//    	Cursor cursor = database.rawQuery(selectStringBuilder.toString(), null);
//    	Log.e("CustomerProduct", selectStringBuilder.toString());
//
//    	Log.e("CustomerProduct", " " + pharmacyId + " " + productId + " " + batch);
//    	int quantity = 0;
//    	if (cursor != null) {
//    		if (cursor.getCount() != 0) {
//    			cursor.moveToFirst();
//    			quantity = Integer.parseInt(cursor.getString(0));
//    			Log.e("CustomerProduct", "cursor is not null " + pharmacyId + " " + productId + " " + batch + " " + quantity);
//    		} else {
//    			//debugging purposes
//    			Log.e("Customer Products", "cursor is empty");
//    		}
//    	} else {
//    		Log.e("CustomerProduct", "cursor is null " + pharmacyId + " " + productId + " " + batch);
//
//    	}
//
//    	Cursor c = database.query(TABLE_NAME, columns, null, null,  null, null, null);
//    	if (c != null) {
//    		c.moveToFirst();
//    		while (!c.isAfterLast()) {
//    			Log.e("CustomerProduct", c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3)+" " + c.getString(4)+ "");
//    			c.moveToNext();
//    		}
//    	}
//
//    	return quantity;
//    }

    public boolean hasCustomerBoughtProduct(String pharmacyId, String productId) {
        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_QUANTITY}, KEY_PHARMACY_ID + "=? AND " + KEY_PRODUCT_ID + "=?", new String[]{pharmacyId, productId}, null, null, null);
        boolean purchased = false;
        if (cursor != null) {
            if (cursor.getCount() != 0) {
                purchased = true;
            } else {
                Log.e("Customer Product", "cursor size is 0");
            }
        }
        return purchased;
    }

    public int getInvoicedQuantityForCustomer(String pharmacyId, String productId) {
        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_QUANTITY}, KEY_PHARMACY_ID + "=? AND " + KEY_PRODUCT_ID + "=?", new String[]{pharmacyId, productId}, null, null, null);
//    	StringBuilder selectStringBuilder = new StringBuilder();
//    	selectStringBuilder.append("SELECT " + KEY_QUANTITY + " ");
//    	selectStringBuilder.append("FROM " + TABLE_NAME + " ");
//    	selectStringBuilder.append("WHERE ");
//    	selectStringBuilder.append(KEY_PHARMACY_ID + "='" + pharmacyId + "' AND ");
//    	selectStringBuilder.append(KEY_PRODUCT_ID + "='" + productId + "' AND ");
//    	selectStringBuilder.append(KEY_BATCH + "='" + batch+ "'");
//
//    	Cursor cursor = database.rawQuery(selectStringBuilder.toString(), null);
//    	Log.e("CustomerProduct", selectStringBuilder.toString());
//
//    	Log.e("CustomerProduct", " " + pharmacyId + " " + productId + " " + batch);
        int quantity = 0;
        if (cursor != null) {
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                quantity = Integer.parseInt(cursor.getString(0));
                Log.e("CustomerProduct", "cursor is not null " + pharmacyId + " " + productId + " " + quantity);
            }
        }

        return quantity;
    }

    public long updateCustomerProduct(String pharmacyId, String productId, String quantity, String timestamp) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_PHARMACY_ID, pharmacyId);
        cv.put(KEY_PRODUCT_ID, productId);
        cv.put(KEY_BATCH, "");
        cv.put(KEY_QUANTITY, quantity);
        cv.put(KEY_TIMESTAMP, timestamp);
        return database.update(TABLE_NAME, cv, KEY_PHARMACY_ID + "=? AND " + KEY_PRODUCT_ID + "=?", new String[]{pharmacyId, productId});
    }
}

