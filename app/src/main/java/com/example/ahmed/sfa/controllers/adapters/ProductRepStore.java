package com.example.ahmed.sfa.controllers.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.Mst_ProductMaster;
import com.example.ahmed.sfa.models.RepStock;

import java.util.ArrayList;

/**
 * Created by DELL on 10/22/2017.
 */
public class ProductRepStore {

    private static final String KEY_ROW_ID = "row_id";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_PRODUCT_CODE = "product_code";
    private static final String KEY_BATCH_NO = "batch_number";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_EXPIRY_DATE = "expiry_date";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_PURCHASE_PRICE = "price_purchase";
    private static final String KEY_SELLING_PRICE = "price_selling";
    private static final String KEY_RETAIL_PRICE = "price_retail";


    String[] columns = {KEY_ROW_ID, KEY_PRODUCT_ID, KEY_PRODUCT_CODE, KEY_BATCH_NO, KEY_QUANTITY, KEY_EXPIRY_DATE,KEY_PURCHASE_PRICE,KEY_SELLING_PRICE,KEY_RETAIL_PRICE,KEY_TIMESTAMP};
    // String[] columns = {KEY_ROW_ID, KEY_PRODUCT_ID, KEY_PRODUCT_CODE, KEY_BATCH_NO, KEY_QUANTITY, KEY_EXPIRY_DATE,KEY_TIMESTAMP};
    private static final String TABLE_NAME = "productRepStore";
    private static final String PRODUCT_REP_STORE_CREATE = "CREATE TABLE " + TABLE_NAME
            + " (" + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_PRODUCT_ID + " INTEGER NOT NULL ,"
            + KEY_PRODUCT_CODE + " TEXT NOT NULL ,"
            + KEY_BATCH_NO + " TEXT NOT NULL ,"
            + KEY_QUANTITY + " TEXT NOT NULL ,"
            + KEY_EXPIRY_DATE + " TEXT NOT NULL ,"
            + KEY_PURCHASE_PRICE  + " TEXT,"
            + KEY_SELLING_PRICE  + " TEXT,"
            + KEY_RETAIL_PRICE  + " TEXT,"
            + KEY_TIMESTAMP + " TEXT NOT NULL);";
    public final Context productRepStoretContext;
    public DBHelper databaseHelper;
    private SQLiteDatabase database;

    public ProductRepStore(Context c) {
        productRepStoretContext = c;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(PRODUCT_REP_STORE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public ProductRepStore openWritableDatabase() throws SQLException {
        databaseHelper = new DBHelper(productRepStoretContext);
        database = databaseHelper.getWritableDatabase();
        return this;

    }

    public ProductRepStore openReadableDatabase() throws SQLException {
        databaseHelper = new DBHelper(productRepStoretContext);
        database = databaseHelper.getReadableDatabase();
        return this;

    }

    public void closeDatabase() throws SQLException {
        databaseHelper.close();
    }

    public long insertProductRepStore(String productId, String productCode, String batchNo, String quantity, String expiryDate,String pPrice,String sellPrice,String retailPrice, String timestamp) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(KEY_PRODUCT_ID, productId);
        cv.put(KEY_PRODUCT_CODE, productCode);
        cv.put(KEY_BATCH_NO, batchNo);
        cv.put(KEY_QUANTITY, quantity);
        cv.put(KEY_EXPIRY_DATE, expiryDate);
        cv.put(KEY_PURCHASE_PRICE,pPrice);
        cv.put(KEY_SELLING_PRICE,sellPrice);
        cv.put(KEY_RETAIL_PRICE,retailPrice);
        cv.put(KEY_TIMESTAMP, timestamp);

        return database.insert(TABLE_NAME, null, cv);

    }

    /**
     * should move to this code
     * @return
     */



//    public long insertProductRepStore(String productId, String productCode, String batchNo, String quantity, String expiryDate, String timestamp,String purchase,String selling,String retial) throws SQLException {
//
//        ContentValues cv = new ContentValues();
//
//        cv.put(KEY_PRODUCT_ID, productId);
//        cv.put(KEY_PRODUCT_CODE, productCode);
//        cv.put(KEY_BATCH_NO, batchNo);
//        cv.put(KEY_QUANTITY, quantity);
//        cv.put(KEY_EXPIRY_DATE, expiryDate);
//        cv.put(KEY_PURCHASE_PRICE,purchase);
//        cv.put(KEY_SELLING_PRICE,selling);
//        cv.put(KEY_RETAIL_PRICE,retial);
//        cv.put(KEY_TIMESTAMP, timestamp);
//
//        return database.insert(TABLE_NAME, null, cv);
//
//    }

    public ArrayList<String[]> getAllProductRepstore() {
        ArrayList<String[]> productsStore = new ArrayList<String[]>();

        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null,
                null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] productData = new String[7];
            productData[0] = cursor.getString(0);// rowId
            productData[1] = cursor.getString(1);// productId
            productData[2] = cursor.getString(2);// productCode
            productData[3] = cursor.getString(3);// batch
            productData[4] = cursor.getString(4);// quantity
            productData[5] = cursor.getString(5);// expDate
            productData[6] = cursor.getString(6);// timestamp

            Log.w("productRepStoreData 0:", productData[0]);
            Log.w("productRepStoreData 1:", productData[1]);
            Log.w("productRepStoreData 2:", productData[2]);
            Log.w("productRepStoreData 3:", productData[3]);
            Log.w("productRepStoreData 4:", productData[4]);
            Log.w("productRepStoreData 5:", productData[5]);
            Log.w("productRepStoreData 6:", productData[6]);

            productsStore.add(productData);
            cursor.moveToNext();
        }

        cursor.close();

        return productsStore;
    }

    public ArrayList<String[]> getProductDetailsByProductCode(String id) {

        ArrayList<String[]> productInformation = new ArrayList<String[]>();
        Log.w("repstore :", "getProductDetailsByProductCode " + id);
        Cursor cursor = database.query(TABLE_NAME, columns, KEY_PRODUCT_CODE + " = '" + id + "'",
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] productDetails = new String[7];

            productDetails[0] = cursor.getString(0);
            productDetails[1] = cursor.getString(1);
            productDetails[2] = cursor.getString(2);
            productDetails[3] = cursor.getString(3);
            productDetails[4] = cursor.getString(4);
            productDetails[5] = cursor.getString(5);
            productDetails[6] = cursor.getString(6);

            productInformation.add(productDetails);


            cursor.moveToNext();
        }

        return productInformation;

    }

    public String[] getProductDetailsByProductBatchAndProductCode(String batch, String productCode) {

        //ArrayList<String[]> productInformation = new ArrayList<String[]>();
        Log.w("repstore :", "getProductDetailsByProductBatch " + batch);
        Cursor cursor = database.query(TABLE_NAME, columns, KEY_BATCH_NO + " = '" + batch + "' AND " + KEY_PRODUCT_CODE + "='" + productCode + "'",
                null, null, null, null);
        String[] productDetails = new String[14];

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            productDetails[0] = cursor.getString(0);
            productDetails[1] = cursor.getString(1);
            productDetails[2] = cursor.getString(2);
            productDetails[3] = cursor.getString(3);
            productDetails[4] = cursor.getString(4);
            productDetails[5] = cursor.getString(5);
            productDetails[6] = cursor.getString(6);
            productDetails[7] = "0";
            productDetails[8] = "0";
            productDetails[9] = "0";
            productDetails[10] = "0";
            productDetails[13] = "0";

            Log.w("productRepStoreData 0:", productDetails[0]);
            Log.w("productRepStoreData 1:", productDetails[1]);
            Log.w("productRepStoreData 2:", productDetails[2]);
            Log.w("productRepStoreData 3:", productDetails[3]);
            Log.w("productRepStoreData 4:", productDetails[4]);
            Log.w("productRepStoreData 5:", productDetails[5]);
            Log.w("productRepStoreData 6:", productDetails[6]);

            cursor.moveToNext();
        }

//        Log.w("repstore :", "productDetails " + productDetails[1]);

        return productDetails;

    }

    public String[] getProductDetailsByProductBatch11(String batch, String pharmacyId) {

        final String ITINERARY_TABLE = "itinerary";
        final String INVOICE_TABLE = "invoice";
        final String ITINERARY_ID = "itinerary_id";
        final String PHARMACY_ID = "glb_pharmacy_id";
        final String KEY_CODE = "code";
        final String PRODUCT_TABLE = "products";
        final String KEY_INVOICE_ID = "invoice_id";
        final String INVOICED_PRODUCTS_TABLE = "invoiced_product";
        final String QUERY = "SELECT * FROM "
                + TABLE_NAME
                + " INNER JOIN " + ITINERARY_TABLE + " ON " + ITINERARY_TABLE + "." + KEY_ROW_ID + "=" + INVOICE_TABLE + "." + ITINERARY_ID
                + " INNER JOIN " + INVOICE_TABLE + " ON " + INVOICE_TABLE + "." + KEY_ROW_ID + "=" + INVOICED_PRODUCTS_TABLE + "." + KEY_INVOICE_ID
                + " INNER JOIN " + INVOICED_PRODUCTS_TABLE + " ON " + TABLE_NAME + "." + KEY_PRODUCT_CODE + "=" + PRODUCT_TABLE + "." + KEY_CODE
                + " WHERE " + ITINERARY_TABLE + "." + PHARMACY_ID + "='" + pharmacyId + "' AND " + TABLE_NAME + "." + KEY_BATCH_NO + "='" + batch + "'";
        Log.w("repstore :", "getProductDetailsByProductBatch " + QUERY);
//        Cursor cursor = database.query(TABLE_NAME, columns, KEY_BATCH_NO + " = '" + batch + "'",
//                null, null, null, null);

        Cursor cursor = database.rawQuery(QUERY, null);
        String[] productDetails = new String[30];
        Log.w("repstore :", "cursor size " + cursor.getCount());

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            productDetails[0] = cursor.getString(0);
            productDetails[1] = cursor.getString(1);
            productDetails[2] = cursor.getString(2);
            productDetails[3] = cursor.getString(3);
            productDetails[4] = cursor.getString(4);
            productDetails[5] = cursor.getString(5);
            productDetails[6] = cursor.getString(6);

            Log.w("productRepStoreData 0:", productDetails[0]);
            Log.w("productRepStoreData 1:", productDetails[1]);
            Log.w("productRepStoreData 2:", productDetails[2]);
            Log.w("productRepStoreData 3:", productDetails[3]);
            Log.w("productRepStoreData 4:", productDetails[4]);
            Log.w("productRepStoreData 5:", productDetails[5]);
            Log.w("productRepStoreData 6:", productDetails[6]);

            cursor.moveToNext();
        }

        Log.w("repstore :", "productDetails " + productDetails[1]);

        return productDetails;

    }

    //need to change thunder
    public long updateRepStoreData(String batchNo, int quantity, String productCode) throws SQLException {
        long result = 0;
        String stock = getQuantityByProductCodeAndBatch(productCode,batchNo);
        int remain = Integer.parseInt(stock) - quantity;
        if (remain < 0 ){
            remain = 0;
        }
        final String query = "UPDATE " + TABLE_NAME + " SET " + KEY_QUANTITY + "="  + remain + " WHERE " + KEY_BATCH_NO + "='" + batchNo + "' AND " + KEY_PRODUCT_CODE + "='" + productCode + "'";

        database.execSQL(query);
        Log.w("ProductRepstoreUpdate", query);
        return result;
    }

    public String getTotalCurrentStockForPoductByProductId(String productId) {

        String QUERY = "SELECT SUM(" + KEY_QUANTITY + ")"
                + " FROM "
                + TABLE_NAME
                + " WHERE "
                + KEY_PRODUCT_CODE + "= ?";
        Cursor cursor = database.rawQuery(QUERY, new String[]{productId});

        String totalCurrentStock = "0";
        cursor.moveToFirst();
        if (!cursor.isNull(0)) {
            totalCurrentStock = cursor.getString(1);
        }

        return totalCurrentStock;
    }

    public ArrayList<String[]> getAllProductRepStoreWithDetails() {
        ArrayList<String[]> productsWithDetails = new ArrayList<String[]>();

        String PRODUCT_TABLE = "products";
        String KEY_CODE = "code";
        String KEY_UNIT_NAME = "unit_name";
        String KEY_UNIT_SIZE = "unit_size";
        String KEY_GEN_NAME = "generic_name";
        String KEY_CATEGORY = "category";
        String KEY_PRO_DES = "pro_des";
        String KEY_INTR_DATE = "introduced_date";
        String KEY_COUN_OF_ORIGIN = "count_of_origin";
        String KEY_PRINCIPLE = "principle";
        String KEY_PURCHASE_PRICE = "purchase_price";
        String KEY_SELLING_PRICE = "selling_price";
        String KEY_RETAIL_PRICE = "retail_price";
        String query = "SELECT "
                + PRODUCT_TABLE + "." + KEY_CODE + ","
                + PRODUCT_TABLE + "." + KEY_UNIT_NAME + ","
                + PRODUCT_TABLE + "." + KEY_UNIT_SIZE + ","
                + PRODUCT_TABLE + "." + KEY_GEN_NAME + ","
                + PRODUCT_TABLE + "." + KEY_CATEGORY + ","
                + PRODUCT_TABLE + "." + KEY_PRO_DES + ","
                + PRODUCT_TABLE + "." + KEY_INTR_DATE + ","
                + PRODUCT_TABLE + "." + KEY_COUN_OF_ORIGIN + ","
                + PRODUCT_TABLE + "." + KEY_PRINCIPLE + ","
                + PRODUCT_TABLE + "." + KEY_PURCHASE_PRICE + ","
                + PRODUCT_TABLE + "." + KEY_SELLING_PRICE + ","
                + PRODUCT_TABLE + "." + KEY_RETAIL_PRICE + ","
                + TABLE_NAME + "." + KEY_PRODUCT_CODE + ","
                + TABLE_NAME + "." + KEY_BATCH_NO + ","
                + TABLE_NAME + "." + KEY_QUANTITY + ","
                + TABLE_NAME + "." + KEY_EXPIRY_DATE
                + " FROM " + TABLE_NAME
                + " INNER JOIN " + PRODUCT_TABLE + " ON " + TABLE_NAME + "." + KEY_PRODUCT_CODE + "=" + PRODUCT_TABLE + "." + KEY_CODE;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String[] productData = new String[16];

            productData[0] = cursor.getString(0);
            productData[1] = cursor.getString(1);
            productData[2] = cursor.getString(2);
            productData[3] = cursor.getString(3);
            productData[4] = cursor.getString(4);
            productData[5] = cursor.getString(5);
            productData[6] = cursor.getString(6);
            productData[7] = cursor.getString(7);
            productData[8] = cursor.getString(8);
            productData[9] = cursor.getString(9);
            productData[10] = cursor.getString(10);
            productData[11] = cursor.getString(11);
            productData[12] = cursor.getString(12);
            productData[13] = cursor.getString(13);
            productData[14] = cursor.getString(14);
            productData[15] = cursor.getString(15);

            productsWithDetails.add(productData);

//    		Log.w("productData 0:", cursor.getString(0));
//			Log.w("productData 1:", cursor.getString(1));
//			Log.w("productData 2:", cursor.getString(2));
//			Log.w("productData 3:", cursor.getString(3));
//			Log.w("productData 4:", cursor.getString(4));
//			Log.w("productData 5:", cursor.getString(5));
//			Log.w("productData 6:", cursor.getString(6));
//			Log.w("productData 7:", cursor.getString(7));
//			Log.w("productData 8:", cursor.getString(8));
//			Log.w("productData 9:", cursor.getString(9));
//			Log.w("productData 10:", cursor.getString(10));
//			Log.w("productData 11:", cursor.getString(11));
//			Log.w("productData 12:", cursor.getString(12));
//			Log.w("productData 13:", cursor.getString(13));
//			Log.w("productData 14:", cursor.getString(14));
//			Log.w("productData 15:", cursor.getString(15));

            cursor.moveToNext();
        }


        return productsWithDetails;
    }

    public ArrayList<String[]> SearchProductRepStoreWithDetails(String searchString) {
        ArrayList<String[]> productsWithDetails = new ArrayList<String[]>();

        String PRODUCT_TABLE = "products";
        String KEY_CODE = "code";
        String KEY_UNIT_NAME = "unit_name";
        String KEY_UNIT_SIZE = "unit_size";
        String KEY_GEN_NAME = "generic_name";
        String KEY_CATEGORY = "category";
        String KEY_PRO_DES = "pro_des";
        String KEY_INTR_DATE = "introduced_date";
        String KEY_COUN_OF_ORIGIN = "count_of_origin";
        String KEY_PRINCIPLE = "principle";
        String KEY_PURCHASE_PRICE = "purchase_price";
        String KEY_SELLING_PRICE = "selling_price";
        String KEY_RETAIL_PRICE = "retail_price";
        String query = "SELECT "
                + PRODUCT_TABLE + "." + KEY_CODE + ","
                + PRODUCT_TABLE + "." + KEY_UNIT_NAME + ","
                + PRODUCT_TABLE + "." + KEY_UNIT_SIZE + ","
                + PRODUCT_TABLE + "." + KEY_GEN_NAME + ","
                + PRODUCT_TABLE + "." + KEY_CATEGORY + ","
                + PRODUCT_TABLE + "." + KEY_PRO_DES + ","
                + PRODUCT_TABLE + "." + KEY_INTR_DATE + ","
                + PRODUCT_TABLE + "." + KEY_COUN_OF_ORIGIN + ","
                + PRODUCT_TABLE + "." + KEY_PRINCIPLE + ","
                + PRODUCT_TABLE + "." + KEY_PURCHASE_PRICE + ","
                + PRODUCT_TABLE + "." + KEY_SELLING_PRICE + ","
                + PRODUCT_TABLE + "." + KEY_RETAIL_PRICE + ","
                + TABLE_NAME + "." + KEY_PRODUCT_CODE + ","
                + TABLE_NAME + "." + KEY_BATCH_NO + ","
                + TABLE_NAME + "." + KEY_QUANTITY + ","
                + TABLE_NAME + "." + KEY_EXPIRY_DATE
                + " FROM " + TABLE_NAME
                + " INNER JOIN " + PRODUCT_TABLE + " ON " + TABLE_NAME + "." + KEY_PRODUCT_CODE + "=" + PRODUCT_TABLE + "." + KEY_CODE
                + " WHERE "
                + PRODUCT_TABLE + "." + KEY_PRO_DES + " LIKE ?";
        Cursor cursor = database.rawQuery(query, new String[]{"%" + searchString + "%"});
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String[] productData = new String[16];

            productData[0] = cursor.getString(0);
            productData[1] = cursor.getString(1);
            productData[2] = cursor.getString(2);
            productData[3] = cursor.getString(3);
            productData[4] = cursor.getString(4);
            productData[5] = cursor.getString(5);
            productData[6] = cursor.getString(6);
            productData[7] = cursor.getString(7);
            productData[8] = cursor.getString(8);
            productData[9] = cursor.getString(9);
            productData[10] = cursor.getString(10);
            productData[11] = cursor.getString(11);
            productData[12] = cursor.getString(12);
            productData[13] = cursor.getString(13);
            productData[14] = cursor.getString(14);
            productData[15] = cursor.getString(15);

            productsWithDetails.add(productData);

//    		Log.w("productData 0:", cursor.getString(0));
//			Log.w("productData 1:", cursor.getString(1));
//			Log.w("productData 2:", cursor.getString(2));
//			Log.w("productData 3:", cursor.getString(3));
//			Log.w("productData 4:", cursor.getString(4));
//			Log.w("productData 5:", cursor.getString(5));
//			Log.w("productData 6:", cursor.getString(6));
//			Log.w("productData 7:", cursor.getString(7));
//			Log.w("productData 8:", cursor.getString(8));
//			Log.w("productData 9:", cursor.getString(9));
//			Log.w("productData 10:", cursor.getString(10));
//			Log.w("productData 11:", cursor.getString(11));
//			Log.w("productData 12:", cursor.getString(12));
//			Log.w("productData 13:", cursor.getString(13));
//			Log.w("productData 14:", cursor.getString(14));
//			Log.w("productData 15:", cursor.getString(15));

            cursor.moveToNext();
        }

        return productsWithDetails;
    }

    public ArrayList<String> getAllProductRepStoreNames() {
        ArrayList<String> productsNames = new ArrayList<String>();

        String PRODUCT_TABLE = "products";
        String KEY_PRO_DES = "pro_des";
        String KEY_CODE = "code";
        String query = "SELECT DISTINCT "
                + PRODUCT_TABLE + "." + KEY_PRO_DES
                + " FROM " + TABLE_NAME
                + " INNER JOIN " + PRODUCT_TABLE + " ON " + TABLE_NAME + "." + KEY_PRODUCT_CODE + "=" + PRODUCT_TABLE + "." + KEY_CODE;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            productsNames.add(cursor.getString(0));
            Log.w("productData 0:", cursor.getString(0));
            cursor.moveToNext();
        }
        return productsNames;
    }

    public void updateProductRepStoreReturns(String batchNo, String quantity) {
        final String query = "UPDATE " + TABLE_NAME + " SET " + KEY_QUANTITY + "=(" + KEY_QUANTITY + "+" + quantity + ") WHERE " + KEY_BATCH_NO + "='" + batchNo + "'";
        database.execSQL(query);
        Log.w("ProductRepstoreUpdate", query);
    }

    public String getProductIdByRowId(String rowId) {
        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_PRODUCT_ID}, KEY_ROW_ID + "='" + rowId + "'", null, null, null, null, null);
        cursor.moveToFirst();
        String productId = "0";
        if (cursor.getCount() == 0) {
            productId = "0";
        } else {
            productId = cursor.getString(0);
        }

        return productId;
    }

    public String getExpiryByProductCodeAndBatch(String productCode, String batch) {

        final String MY_QUERY = "SELECT " + KEY_EXPIRY_DATE + " FROM " + TABLE_NAME + " WHERE " + KEY_BATCH_NO
                + " = '" + batch + "' AND " + KEY_PRODUCT_CODE + " = '" + productCode + "'";

        Log.w("MY_QUERY", " : " + MY_QUERY);
        Cursor cursor = database.rawQuery(MY_QUERY, null);

        cursor.moveToFirst();
        String expiry = cursor.getString(0);
        Log.w("expiry", " : " + expiry);

        return expiry;
    }

    public ArrayList<String> getBatchesByProductCode(String productCode) {
        Log.w("product id recieved to get batches: ", productCode + "");
        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_BATCH_NO}, KEY_PRODUCT_CODE + "=?", new String[]{productCode}, null, null, null);
        cursor.moveToFirst();
        ArrayList<String> batchList = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            batchList.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return batchList;
    }

    public String getQuantitySumByProductCode(String productCode) {
        String query = "SELECT SUM(" + KEY_QUANTITY + ") FROM " + TABLE_NAME + " WHERE `" + KEY_PRODUCT_CODE + "`='" + productCode + "'";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        String qty = "0";
        if (cursor.getCount() != 0) {
            qty = cursor.getString(0);
            if (qty.contentEquals("null")) {
                qty = "0";
            }
        }

        return qty;
    }

    public long updateProductRepstore(String batchNo, String quantity, String expiryDate, String timestamp, String productId) throws SQLException {
        ContentValues cv = new ContentValues();
        cv.put(KEY_PRODUCT_ID, productId.trim());
        cv.put(KEY_QUANTITY, quantity);
        cv.put(KEY_EXPIRY_DATE, expiryDate);
        cv.put(KEY_TIMESTAMP, timestamp);

        return database.update(TABLE_NAME, cv, KEY_BATCH_NO + "=?", new String[]{batchNo});

    }

    public boolean isBatchAvailable(String batch, String prodId) {
        Cursor cursor = database.query(TABLE_NAME, columns, KEY_BATCH_NO + "=? AND " + KEY_PRODUCT_CODE + "=?", new String[]{batch, prodId}, null, null, null);
        cursor.moveToFirst();
        boolean available = false;
        if (cursor.getCount() == 0) {
            available = false;
        } else {
            available = true;
        }
        return available;
    }

    public String getCurrentStockByBatch(String batch) {
        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_QUANTITY}, KEY_BATCH_NO + "=?", new String[]{batch}, null, null, null);
        cursor.moveToFirst();
        String qty = "0";
        if (cursor.getCount() == 0) {
            qty = "0";
        } else {
            qty = cursor.getString(0);
        }
        return qty;
    }

//   public long deleteRepStoreByBatch(String batch) {
//	   return database.delete(TABLE_NAME, KEY_BATCH_NO+"=?", new String[] {batch});
//   }

    public String getMaxRepstoreId() {

//	   String queryStr = "SELECT Max("+KEY_PROD_ROW_ID+") from "+TABLE_NAME;
//
//	   Cursor cursor = database.rawQuery(queryStr, null);

        Cursor cursor = database.query(TABLE_NAME, new String[]{"Max(" + KEY_PRODUCT_ID + ")"}, null, null, null, null, null, null);


        String productId = "0";
        if (cursor.getCount() == 0) {

        } else {
            cursor.moveToFirst();
            productId = cursor.getString(0);
        }

        return productId;
    }

    public boolean isBatchAvailableWithoutProdCode(String batch) {
        Cursor cursor = database.query(TABLE_NAME, columns, KEY_BATCH_NO + "=?", new String[]{batch}, null, null, null);
        cursor.moveToFirst();
        boolean available = false;
        if (cursor.getCount() > 0) {
            available = true;
        }
        return available;
    }


    public long updateRepStoreQtyAdd(String batchNo, int quantity, String productCode) throws SQLException {
        long result = 0;

        final String query = "UPDATE " + TABLE_NAME + " SET " + KEY_QUANTITY + "=(" + KEY_QUANTITY + "+" + quantity + ") WHERE " + KEY_BATCH_NO + "='" + batchNo + "' AND " + KEY_PRODUCT_CODE + "='" + productCode + "'";

        database.execSQL(query);
        Log.w("ProductRepstoreUpdate", query);
        return result;
    }


    /**
     * Author Amila
     * @return
     */
    public ArrayList<RepStock> getAllRepstores() {
        ArrayList<RepStock> productsStore = new ArrayList<>();


        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null,
                null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                RepStock productData = new RepStock();
                // productData. = cursor.getString(0);// rowId
                // productData. = cursor.getString(1);// productId
                productData.setProductCode(cursor.getString(2));// productCode
                productData.setBatchCode(cursor.getString(3));// batch
                productData.setQuantity(Integer.parseInt(cursor.getString(4)));// quantity
                // productData. = cursor.getString(5);// expDate
                // productData. = cursor.getString(6);// timestamp


                productsStore.add(productData);
                cursor.moveToNext();
            }
        }catch (SQLException e){
            Log.e("Rep store error",e.toString());
        }catch (Exception e){
            Log.e("Rep store error",e.toString());
        }finally {
            cursor.close();
        }



        return productsStore;
    }


    public ArrayList<Mst_ProductMaster> getAllRepAtoreDetails() {
        ArrayList<Mst_ProductMaster> products = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from products  inner join productRepStore  on products.code = productRepStore.product_code ", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Mst_ProductMaster productData = new Mst_ProductMaster();
//            by me productData.setRowId(Integer.parseInt(cursor.getString(21)));// rowId of repstore
            productData.setId(cursor.getString(22));// product id from rep stores
//            byme productData.setCode( cursor.getString(2));// Code
            productData.setBrand( cursor.getString(3));// brand
            productData.setUnitName( cursor.getString(4));// unit name
//            productData.setUnitSize(cursor.getString(5));// size
//            productData.setGenericName(cursor.getString(6));// generic name
//            productData.setCategory(cursor.getString(7));// category
//            productData.setProDes(cursor.getString(8));// proDes
//            productData.setIntroducedDate(cursor.getString(9));// introduced date
            // productData.setUnitSize(); cursor.getString(10);// country of origin
            productData.setPrinciple(cursor.getString(11));// principle
//            productData.setPurchasePrice(Double.parseDouble(cursor.getString(27)));// purchase Price
//            productData.setSellingPrice(Double.parseDouble(cursor.getString(28)));// selling Price----
            productData.setRetailPrice(Double.parseDouble(cursor.getString(29)));// retail Price
//            productData.setBatchNumber(cursor.getString(24));
//            productData.setQuantity(Integer.parseInt(cursor.getString(25)));//stock repstore
            Log.i("stock ->",cursor.getString(25));
//            productData.setTimeStamp(cursor.getString(30));// timestamp
//            productData.setExpiryDate(cursor.getString(26));
//            productData.setUnitSize(); cursor.getString(16);// inactiv
//            productData.setUnitSize(); cursor.getString(17);// vat
//            productData.setUnitSize(); cursor.getString(18);// tt
//            productData.setUnitSize(); cursor.getString(19);// tt
//            productData.setUnitSize(); cursor.getString(20);// tt



            products.add(productData);
            cursor.moveToNext();
        }

        cursor.close();

        return products;
    }

    public String getQuantityByProductCodeAndBatch(String productCode, String batch) {

        final String MY_QUERY = "SELECT " + KEY_QUANTITY + " FROM " + TABLE_NAME + " WHERE " + KEY_BATCH_NO
                + " = '" + batch + "' AND " + KEY_PRODUCT_CODE + " = '" + productCode + "'";

        Log.w("MY_QUERY", " : " + MY_QUERY);
        Cursor cursor = database.rawQuery(MY_QUERY, null);

        cursor.moveToFirst();
        String expiry = cursor.getString(0);
        Log.w("expiry", " : " + expiry);

        return expiry;
    }

}
