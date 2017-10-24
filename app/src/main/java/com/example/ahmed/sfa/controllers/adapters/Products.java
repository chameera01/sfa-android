package com.example.ahmed.sfa.controllers.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.Mst_ProductMaster;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 10/25/2017.
 * created ADAPTER CLASS for invoice gen
 */
public class Products {

    private static final String KEY_ROW_ID = "row_id";
    private static final String KEY_ID = "id";
    private static final String KEY_CODE = "code";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_UNIT_NAME = "unit_name";
    private static final String KEY_UNIT_SIZE = "unit_size";
    private static final String KEY_GEN_NAME = "generic_name";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_PRO_DES = "pro_des";
    private static final String KEY_INTR_DATE = "introduced_date";
    private static final String KEY_COUN_OF_ORIGIN = "count_of_origin";
    private static final String KEY_PRINCIPLE = "principle";
    private static final String KEY_PURCHASE_PRICE = "purchase_price";
    private static final String KEY_SELLING_PRICE = "selling_price"; // the unit
    // price
    // thats
    // being
    // used
    private static final String KEY_RETAIL_PRICE = "retail_price";
    private static final String KEY_FORCE = "force";
    private static final String KEY_INACTIVE = "inactive";
    private static final String KEY_VAT = "vat";
    private static final String KEY_TT = "tt";
    private static final String KEY_TIME_STAMP = "time_stamp";
    private static final String KEY_PROD_ROW_ID = "prod_row_id";

    String[] columns = new String[]{KEY_ROW_ID, KEY_ID, KEY_CODE, KEY_BRAND,
            KEY_UNIT_NAME, KEY_UNIT_SIZE, KEY_GEN_NAME, KEY_CATEGORY,
            KEY_PRO_DES, KEY_INTR_DATE, KEY_COUN_OF_ORIGIN, KEY_PRINCIPLE,
            KEY_PURCHASE_PRICE, KEY_SELLING_PRICE, KEY_RETAIL_PRICE, KEY_FORCE,
            KEY_INACTIVE, KEY_VAT, KEY_TT, KEY_TIME_STAMP, KEY_PROD_ROW_ID};

    private static final String TABLE_NAME = "products";
    private static final String PRODUCTS_CREATE = "CREATE TABLE " + TABLE_NAME
            + " (" + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ID + " INTEGER NOT NULL ," + KEY_CODE + " TEXT NOT NULL ,"
            + KEY_BRAND + " TEXT NOT NULL ," + KEY_UNIT_NAME
            + " TEXT NOT NULL ," + KEY_UNIT_SIZE + " TEXT ," + KEY_GEN_NAME
            + " TEXT NOT NULL ," + KEY_CATEGORY + " TEXT ," + KEY_PRO_DES
            + " TEXT NOT NULL ," + KEY_INTR_DATE + " TEXT ,"
            + KEY_COUN_OF_ORIGIN + " TEXT ," + KEY_PRINCIPLE + " TEXT ,"
            + KEY_PURCHASE_PRICE + " REAL NOT NULL ," + KEY_SELLING_PRICE
            + " REAL NOT NULL ," + KEY_RETAIL_PRICE + " REAL NOT NULL ,"
            + KEY_FORCE + " TEXT NOT NULL ," + KEY_INACTIVE + " TEXT ,"
            + KEY_VAT + " TEXT ," + KEY_TT + " TEXT ," + KEY_TIME_STAMP
            + " TEXT NOT NULL , " + KEY_PROD_ROW_ID + " TEXT " + " );";
    public final Context productContext;
    public DBHelper databaseHelper;
    private SQLiteDatabase database;

    public Products(Context c) {
        productContext = c;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(PRODUCTS_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public Products openWritableDatabase() throws SQLException {
        databaseHelper = new DBHelper(productContext);
        database = databaseHelper.getWritableDatabase();
        return this;

    }

    public Products openReadableDatabase() throws SQLException {
        databaseHelper = new DBHelper(productContext);
        database = databaseHelper.getReadableDatabase();
        return this;

    }

    public void closeDatabase() throws SQLException {
        databaseHelper.close();
    }

    public long insertProduct(String id, String brand, String code,
                              String unitName, String unitSize, String genericName,
                              String category, String productDescription, String introducedDate,
                              String countryOfOrigin, String principle, String purchasePrice,
                              String sellingPrice, String retailPrice, String force,
                              String inactive, String vat, String tt, String timeStamp,
                              String prodRowId) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(KEY_ID, id);
        cv.put(KEY_CODE, code);
        cv.put(KEY_UNIT_NAME, unitName);
        cv.put(KEY_BRAND, brand);
        cv.put(KEY_GEN_NAME, genericName);
        cv.put(KEY_CATEGORY, category);
        cv.put(KEY_INTR_DATE, introducedDate);
        cv.put(KEY_UNIT_SIZE, unitSize);
        cv.put(KEY_COUN_OF_ORIGIN, countryOfOrigin);
        cv.put(KEY_PRINCIPLE, principle);
        cv.put(KEY_PURCHASE_PRICE, purchasePrice);
        cv.put(KEY_RETAIL_PRICE, retailPrice);
        cv.put(KEY_FORCE, force);
        cv.put(KEY_INACTIVE, inactive);
        cv.put(KEY_SELLING_PRICE, sellingPrice);
        cv.put(KEY_VAT, vat);
        cv.put(KEY_PRO_DES, productDescription);
        cv.put(KEY_TT, tt);
        cv.put(KEY_TIME_STAMP, timeStamp);
        cv.put(KEY_PROD_ROW_ID, prodRowId);

        return database.insert(TABLE_NAME, null, cv);

    }

    public List<String[]> getAllProducts() {
        List<String[]> products = new ArrayList<String[]>();

        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null,
                null, KEY_FORCE + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] productData = new String[21];
            productData[0] = cursor.getString(0);// rowId
            productData[1] = cursor.getString(1);// id
            productData[2] = cursor.getString(2);// Code
            productData[3] = cursor.getString(3);// brand
            productData[4] = cursor.getString(4);// unit name
            productData[5] = cursor.getString(5);// size
            productData[6] = cursor.getString(6);// generic name
            productData[7] = cursor.getString(7);// category
            productData[8] = cursor.getString(8);// proDes
            productData[9] = cursor.getString(9);// introduced date
            productData[10] = cursor.getString(10);// country of origin
            productData[11] = cursor.getString(11);// principle
            productData[12] = cursor.getString(12);// purchase Price
            productData[13] = cursor.getString(13);// selling Price----
            productData[14] = cursor.getString(14);// retail Price
            productData[15] = cursor.getString(15);// force
            productData[16] = cursor.getString(16);// inactiv
            productData[17] = cursor.getString(17);// vat
            productData[18] = cursor.getString(18);// tt
            productData[19] = cursor.getString(19);// tt
            productData[20] = cursor.getString(20);// tt

            // Log.w("productData 0:", cursor.getString(0));
            // Log.w("productData 1:", cursor.getString(1));
            // Log.w("productData 2:", cursor.getString(2));
            // Log.w("productData 3:", cursor.getString(3));
            // Log.w("productData 4:", cursor.getString(4));
            // Log.w("productData 5:", cursor.getString(5));
            // Log.w("productData 6:", cursor.getString(6));
            // Log.w("productData 7:", cursor.getString(7));
            // Log.w("productData 8:", cursor.getString(8));
            // Log.w("productData 9:", cursor.getString(9));
            // Log.w("productData 10:", cursor.getString(10));
            // Log.w("productData 11:", cursor.getString(11));
            // Log.w("productData 12:", cursor.getString(12));
            // Log.w("productData 13:", cursor.getString(13));
            // Log.w("productData 14:", cursor.getString(14));
            // Log.w("productData 15:", cursor.getString(15));
            // Log.w("productData 16:", cursor.getString(16));
            // Log.w("productData 17:", cursor.getString(17));
            // Log.w("productData 18:", cursor.getString(18));
            // Log.w("productData 19:", cursor.getString(19));
            // Log.w("productData 20:", cursor.getString(20));

            products.add(productData);
            cursor.moveToNext();
        }

        cursor.close();

        return products;
    }

    public String[] getProductDetailsById(String id) {

        String[] productDetails = new String[19];

        Cursor cursor = database.query(TABLE_NAME, columns, KEY_CODE + " = ?",
                new String[]{id}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            productDetails[0] = cursor.getString(0);
            productDetails[1] = cursor.getString(1);
            productDetails[2] = cursor.getString(2);
            productDetails[3] = cursor.getString(3);
            productDetails[4] = cursor.getString(4);
            productDetails[5] = cursor.getString(5);
            productDetails[6] = cursor.getString(6);
            productDetails[7] = cursor.getString(7);
            productDetails[8] = cursor.getString(8);
            productDetails[9] = cursor.getString(9);
            productDetails[10] = cursor.getString(10);
            productDetails[11] = cursor.getString(11);
            productDetails[12] = cursor.getString(12);
            productDetails[13] = cursor.getString(13);
            productDetails[14] = cursor.getString(14);
            productDetails[15] = cursor.getString(15);
            productDetails[16] = cursor.getString(16);
            productDetails[17] = cursor.getString(17);
            productDetails[18] = cursor.getString(18);

            Log.w("Log", "data[0] sisze : " + productDetails[0]);
            cursor.moveToNext();
        }

        return productDetails;

    }

    public String[] getProductNames() {

        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_ROW_ID,
                KEY_PRO_DES}, null, null, null, null, null);
        String[] productNameList = new String[cursor.getCount()];
        cursor.moveToFirst();

        int count = 0;
        while (!cursor.isAfterLast()) {
            // String[] customerNames = new String[2];
            // customerNames[0] = cursor.getString(0);//id
            productNameList[count] = cursor.getString(1);// name

            // Log.w("Product Name: ", productNameList[count] + "");
            count++;
            // customerNameList.add(customerNames);
            cursor.moveToNext();

        }

        return productNameList;

    }

    public ArrayList<String> getProductNameArray() {

        ArrayList<String> prodList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_ROW_ID,
                KEY_PRO_DES}, null, null, null, null, null);
        String[] productNameList = new String[cursor.getCount()];
        cursor.moveToFirst();

        int count = 0;
        while (!cursor.isAfterLast()) {
            // String[] customerNames = new String[2];
            // customerNames[0] = cursor.getString(0);//id
            prodList.add(cursor.getString(1));// name

            // Log.w("Product Name: ", productNameList[count] + "");
            count++;
            // customerNameList.add(customerNames);
            cursor.moveToNext();

        }

        return prodList;

    }

    public List<String[]> searchProducts(String productName) {
        List<String[]> products = new ArrayList<String[]>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME
                + " WHERE " + KEY_PRO_DES + " LIKE ?", new String[]{"%"
                + productName + "%"});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] productData = new String[19];
            productData[0] = cursor.getString(0);
            productData[1] = cursor.getString(1);
            productData[2] = cursor.getString(2);
            productData[3] = cursor.getString(3);
            productData[4] = cursor.getString(4);
            productData[5] = cursor.getString(5);//
            productData[6] = cursor.getString(6);
            productData[7] = cursor.getString(7);
            productData[8] = cursor.getString(8);//
            productData[9] = cursor.getString(9);
            productData[10] = cursor.getString(10);//
            productData[11] = cursor.getString(11);
            productData[12] = cursor.getString(12);
            productData[13] = cursor.getString(13);//
            productData[14] = cursor.getString(14);
            productData[15] = cursor.getString(15);
            productData[16] = cursor.getString(16);
            productData[17] = cursor.getString(17);
            productData[18] = cursor.getString(18);
            // Log.w("productData 0:", cursor.getString(0));
            // Log.w("productData 1:", cursor.getString(1));
            // Log.w("productData 2:", cursor.getString(2));
            // Log.w("productData 3:", cursor.getString(3));
            // Log.w("productData 4:", cursor.getString(4));
            // Log.w("productData 5:", cursor.getString(5));
            // Log.w("productData 6:", cursor.getString(6));
            // Log.w("productData 7:", cursor.getString(7));
            // Log.w("productData 8:", cursor.getString(8));
            // Log.w("productData 9:", cursor.getString(9));
            // Log.w("productData 10:", cursor.getString(10));
            // Log.w("productData 11:", cursor.getString(11));
            // Log.w("productData 12:", cursor.getString(12));
            // Log.w("productData 13:", cursor.getString(13));
            // Log.w("productData 14:", cursor.getString(14));
            // Log.w("productData 15:", cursor.getString(15));
            // Log.w("productData 16:", cursor.getString(16));
            // Log.w("productData 17:", cursor.getString(17));
            // Log.w("productData 18:", cursor.getString(18));

            products.add(productData);
            cursor.moveToNext();
        }

        cursor.close();

        return products;
    }

    public String[] getProductDetailsByProductCode(String code) {

        String[] productDetails = new String[19];

        Cursor cursor = database.query(TABLE_NAME, columns, KEY_CODE + " = ?",
                new String[]{code}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            productDetails[0] = cursor.getString(0);
            productDetails[1] = cursor.getString(1);
            productDetails[2] = cursor.getString(2);
            productDetails[3] = cursor.getString(3);
            productDetails[4] = cursor.getString(4);
            productDetails[5] = cursor.getString(5);
            productDetails[6] = cursor.getString(6);
            productDetails[7] = cursor.getString(7);
            productDetails[8] = cursor.getString(8);
            productDetails[9] = cursor.getString(9);
            productDetails[10] = cursor.getString(10);
            productDetails[11] = cursor.getString(11);
            productDetails[12] = cursor.getString(12);
            productDetails[13] = cursor.getString(13);
            productDetails[14] = cursor.getString(14);
            productDetails[15] = cursor.getString(15);
            productDetails[16] = cursor.getString(16);
            productDetails[17] = cursor.getString(17);
            productDetails[18] = cursor.getString(18);

            Log.w("Log", "data[0] sisze : " + productDetails[0]);
            cursor.moveToNext();
        }

        return productDetails;

    }

    public ArrayList<String[]> getInvoicedProductsForCustomer(String pharmacyId) {
        final String ITINERARY_TABLE = "itinerary";
        final String INVOICE_TABLE = "invoice";
        final String ITINERARY_ID = "itinerary_id";
        final String PHARMACY_ID = "glb_pharmacy_id";
        final String INVOICED_PRODUCT_TABLE = "invoiced_product";
        final String KEY_PRODUCT_CODE = "product_code";
        final String KEY_INVOICE_ID = "invoice_id";
        final String QUERY = "SELECT DISTINCT " + KEY_CODE + "," + KEY_PRO_DES
                + " FROM " + TABLE_NAME + " INNER JOIN " + ITINERARY_TABLE
                + " ON " + ITINERARY_TABLE + "." + KEY_ROW_ID + "="
                + INVOICE_TABLE + "." + ITINERARY_ID + " INNER JOIN "
                + INVOICE_TABLE + " ON " + INVOICE_TABLE + "." + KEY_ROW_ID
                + "=" + INVOICED_PRODUCT_TABLE + "." + KEY_INVOICE_ID
                + " INNER JOIN " + INVOICED_PRODUCT_TABLE + " ON "
                + INVOICED_PRODUCT_TABLE + "." + KEY_PRODUCT_CODE + "="
                + TABLE_NAME + "." + KEY_CODE + " WHERE " + ITINERARY_TABLE
                + "." + PHARMACY_ID + "='" + pharmacyId + "'";
        Log.w("Product: getInvoicedProductsForCustomer", QUERY);

        Cursor cursor = database.rawQuery(QUERY, null);

        cursor.moveToFirst();
        ArrayList<String[]> productsByPharmacyId = new ArrayList<String[]>();
        while (!cursor.isAfterLast()) {
            String[] productData = new String[19];
            productData[0] = cursor.getString(0);// rowId
            productData[1] = cursor.getString(1);// id
            Log.w("productsByPharmacyId 0:", cursor.getString(0));
            Log.w("productsByPharmacyId 1:", cursor.getString(1));
            productsByPharmacyId.add(productData);

            cursor.moveToNext();
        }
        cursor.close();
        return productsByPharmacyId;

    }

    public ArrayList<String[]> getProductListData() throws SQLException {

        // ArrayList<String[]> products = new ArrayList<String[]>();
        String PRODUCT_REP_STORE = "productRepStore";
        String KEY_QUANTITY = "quantity";
        String KEY_NORMAL = "normal";
        String KEY_PRODUCT_ID = "product_id";
        String KEY_PRODUCT_CODE = "product_code";
        String INVOICED_PRODUCT_TABLE = "invoiced_product";

        String query = "SELECT " + TABLE_NAME + "." + KEY_ROW_ID + ","
                + TABLE_NAME + "." + KEY_ID + "," + TABLE_NAME + "." + KEY_CODE
                + "," + TABLE_NAME + "." + KEY_BRAND + "," + TABLE_NAME + "."
                + KEY_UNIT_NAME + "," + TABLE_NAME + "." + KEY_UNIT_SIZE + ","
                + TABLE_NAME + "." + KEY_GEN_NAME + "," + TABLE_NAME + "."
                + KEY_CATEGORY + "," + TABLE_NAME + "." + KEY_PRO_DES + ","
                + TABLE_NAME + "." + KEY_INTR_DATE + "," + TABLE_NAME + "."
                + KEY_COUN_OF_ORIGIN + "," + TABLE_NAME + "." + KEY_PRINCIPLE
                + ","
                + TABLE_NAME
                + "."
                + KEY_PURCHASE_PRICE
                + ","
                + TABLE_NAME
                + "."
                + KEY_SELLING_PRICE
                + ","
                + TABLE_NAME
                + "."
                + KEY_RETAIL_PRICE
                + ","//
                + TABLE_NAME + "." + KEY_FORCE + "," + TABLE_NAME + "."
                + KEY_INACTIVE + "," + TABLE_NAME + "." + KEY_VAT + ","
                + TABLE_NAME + "." + KEY_TT + "," + TABLE_NAME + "."
                + KEY_TIME_STAMP + "," + " SUM(" + PRODUCT_REP_STORE + "."
                + KEY_QUANTITY + ")," + " SUM(" + INVOICED_PRODUCT_TABLE + "."
                + KEY_NORMAL + ")" + " FROM " + TABLE_NAME + " INNER JOIN "
                + PRODUCT_REP_STORE + " ON " + PRODUCT_REP_STORE + "."
                + KEY_PRODUCT_ID + "=" + TABLE_NAME + "." + KEY_CODE
                + " INNER JOIN " + INVOICED_PRODUCT_TABLE + " ON "
                + INVOICED_PRODUCT_TABLE + "." + KEY_PRODUCT_CODE + "="
                + TABLE_NAME + "." + KEY_CODE;

        Log.w("product QUER", query);

        Cursor cursor = database.rawQuery(query, null);
        Log.w("Cursor Rows", "" + cursor.getCount());
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            // Log.w("Products", "RowId: " + cursor.getString(0));
            // Log.w("Products", "Id: " + cursor.getString(1));
            // Log.w("Products", "Code: " + cursor.getString(2));
            // Log.w("Products", "Brand: " + cursor.getString(3));
            // Log.w("Products", "UnitName: " + cursor.getString(4));
            // Log.w("Products", "UnitSize: " + cursor.getString(5));
            // Log.w("Products", "GenName: " + cursor.getString(6));
            // Log.w("Products", "Category: " + cursor.getString(7));
            // Log.w("Products", "Description: " + cursor.getString(8));
            // Log.w("Products", "IntroDate: " + cursor.getString(9));
            // Log.w("Products", "OriginCountry: " + cursor.getString(10));
            // Log.w("Products", "Principle Id" + cursor.getString(11));
            // Log.w("Products", "P Price: " + cursor.getString(12));
            // Log.w("Products", "S Price: " + cursor.getString(13));
            // Log.w("Products", "R Price: " + cursor.getString(14));
            // Log.w("Products", "Force: " + cursor.getString(15));
            // Log.w("Products", "Inactive: " + cursor.getString(16));
            // Log.w("Products", "Vat: " + cursor.getString(17));
            // Log.w("Products", "TT: " + cursor.getString(18));
            // Log.w("Products", "TimeStamp: " + cursor.getString(19));
            // Log.w("Products", "TotalStock: " + cursor.getString(20));
            // Log.w("Products", "LastInvoice: " + cursor.getString(21));

            cursor.moveToNext();
        }

        cursor.close();
        return null;
    }

    public String getPriceByProductCode(String productCode) {

        Cursor cursor = database.query(TABLE_NAME,
                new String[]{KEY_SELLING_PRICE}, KEY_CODE + "=?",
                new String[]{productCode}, null, null, null);

        cursor.moveToFirst();

        return cursor.getString(0);
    }

    public String getRetalPriceByProductCode(String productCode) {

        Cursor cursor = database.query(TABLE_NAME,
                new String[]{KEY_RETAIL_PRICE}, KEY_CODE + "=?",
                new String[]{productCode}, null, null, null);

        cursor.moveToFirst();

        return cursor.getString(0);
    }

    public ArrayList<String> getAllProductPrinciple() {
        ArrayList<String> productPrinciples = new ArrayList<String>();

        String query = "SELECT DISTINCT " + KEY_PRINCIPLE + " FROM "
                + TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            productPrinciples.add(cursor.getString(0));
            cursor.moveToNext();
        }

        return productPrinciples;
    }

    public List<String[]> searchPrinciple(String principle) {
        List<String[]> products = new ArrayList<String[]>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME
                + " WHERE " + KEY_PRINCIPLE + " LIKE ?", new String[]{"%"
                + principle + "%"});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] productData = new String[19];
            productData[0] = cursor.getString(0);
            productData[1] = cursor.getString(1);
            productData[2] = cursor.getString(2);
            productData[3] = cursor.getString(3);
            productData[4] = cursor.getString(4);
            productData[5] = cursor.getString(5);//
            productData[6] = cursor.getString(6);
            productData[7] = cursor.getString(7);
            productData[8] = cursor.getString(8);//
            productData[9] = cursor.getString(9);
            productData[10] = cursor.getString(10);//
            productData[11] = cursor.getString(11);
            productData[12] = cursor.getString(12);
            productData[13] = cursor.getString(13);//
            productData[14] = cursor.getString(14);
            productData[15] = cursor.getString(15);
            productData[16] = cursor.getString(16);
            productData[17] = cursor.getString(17);
            productData[18] = cursor.getString(18);
            // Log.w("productData 0:", cursor.getString(0));
            // Log.w("productData 1:", cursor.getString(1));
            // Log.w("productData 2:", cursor.getString(2));
            // Log.w("productData 3:", cursor.getString(3));
            // Log.w("productData 4:", cursor.getString(4));
            // Log.w("productData 5:", cursor.getString(5));
            // Log.w("productData 6:", cursor.getString(6));
            // Log.w("productData 7:", cursor.getString(7));
            // Log.w("productData 8:", cursor.getString(8));
            // Log.w("productData 9:", cursor.getString(9));
            // Log.w("productData 10:", cursor.getString(10));
            // Log.w("productData 11:", cursor.getString(11));
            // Log.w("productData 12:", cursor.getString(12));
            // Log.w("productData 13:", cursor.getString(13));
            // Log.w("productData 14:", cursor.getString(14));
            // Log.w("productData 15:", cursor.getString(15));
            // Log.w("productData 16:", cursor.getString(16));
            // Log.w("productData 17:", cursor.getString(17));
            // Log.w("productData 18:", cursor.getString(18));

            products.add(productData);
            cursor.moveToNext();
        }

        cursor.close();

        return products;
    }

    public String getProductIdByRowId(String rowId) {
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{KEY_PROD_ROW_ID}, KEY_ROW_ID + "='" + rowId
                        + "'", null, null, null, null, null);
        cursor.moveToFirst();

        String productId = cursor.getString(0);
        return productId;
    }

    public boolean isProductAvailable(String code) {
        Cursor cursor = database.query(TABLE_NAME, columns, KEY_CODE + "=?",
                new String[]{code}, null, null, null);
        // cursor.moveToFirst();

//		Cursor cursor = database.rawQuery("SELECT * from "+TABLE_NAME+" where "+KEY_CODE+" ='"+code+"' ", null);

        boolean available = false;
        Log.w("Log", "cursor.getCount() : " + cursor.getCount());
        if (cursor.getCount() > 0) {
            available = true;
        } else {
            available = false;

        }
        return available;
    }

    public long updateProduct(String id, String brand, String code,
                              String unitName, String unitSize, String genericName,
                              String category, String productDescription, String introducedDate,
                              String countryOfOrigin, String principle, String purchasePrice,
                              String sellingPrice, String retailPrice, String force,
                              String inactive, String vat, String tt, String timeStamp,
                              String prodRowId) throws SQLException {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, id);
//		cv.put(KEY_CODE, code);
        cv.put(KEY_UNIT_NAME, unitName);
        cv.put(KEY_BRAND, brand);
        cv.put(KEY_GEN_NAME, genericName);
        cv.put(KEY_CATEGORY, category);
        cv.put(KEY_INTR_DATE, introducedDate);
        cv.put(KEY_UNIT_SIZE, unitSize);
        cv.put(KEY_COUN_OF_ORIGIN, countryOfOrigin);
        cv.put(KEY_PRINCIPLE, principle);
        cv.put(KEY_PURCHASE_PRICE, purchasePrice);
        cv.put(KEY_RETAIL_PRICE, retailPrice);
        cv.put(KEY_FORCE, force);
        cv.put(KEY_INACTIVE, inactive);
        cv.put(KEY_SELLING_PRICE, sellingPrice);
        cv.put(KEY_VAT, vat);
        cv.put(KEY_PRO_DES, productDescription);
        cv.put(KEY_TT, tt);
        cv.put(KEY_TIME_STAMP, timeStamp);
        cv.put(KEY_PROD_ROW_ID, prodRowId);

        Log.w("Product: updateProduct ", prodRowId);

        return database.update(TABLE_NAME, cv, KEY_CODE + "=?",
                new String[]{code});

    }

    public String getMaxProductId() {

        // String queryStr = "SELECT Max(" + KEY_PROD_ROW_ID + ") from "
        // + TABLE_NAME;
        //
        // Cursor cursor = database.rawQuery(queryStr, null);

        ArrayList<String> productRowIds = new ArrayList<String>();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{KEY_PROD_ROW_ID}, null, null, null, null, null,
                null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            productRowIds.add(cursor.getString(0));
            cursor.moveToNext();
        }

        int maxId = 0;
        for (String id : productRowIds) {
            int prodId = Integer.parseInt(id);

            if (prodId > maxId) {
                maxId = prodId;
            }

        }
        // Cursor cursor = database.query(TABLE_NAME, new String[] {"Max("+
        // KEY_PROD_ROW_ID + ")"}, null, null, null, null, null, null);

        // String productId = "0";
        // if (cursor.getCount() == 0) {
        //
        // } else {
        // cursor.moveToFirst();
        // productId = cursor.getString(0);
        // }

        return String.valueOf(maxId);

    }

    public long getTotalInvoicedQuantityForProduct(String pharmacyId,
                                                   String productId) {
        final String ITINERARY_TABLE = "itinerary";
        final String INVOICE_TABLE = "invoice";
        final String ITINERARY_ID = "itinerary_id";
        final String PHARMACY_ID = "glb_pharmacy_id";
        final String INVOICED_PRODUCT_TABLE = "invoiced_product";
        final String KEY_PRODUCT_CODE = "product_code";
        final String KEY_INVOICE_ID = "invoice_id";
        final String KEY_NORMAL = "normal";
        final String QUERY = "SELECT SUM(" + INVOICED_PRODUCT_TABLE + "."
                + KEY_NORMAL + ")" + " FROM " + TABLE_NAME + " INNER JOIN "
                + ITINERARY_TABLE + " ON " + ITINERARY_TABLE + "." + KEY_ROW_ID
                + "=" + INVOICE_TABLE + "." + ITINERARY_ID + " INNER JOIN "
                + INVOICE_TABLE + " ON " + INVOICE_TABLE + "." + KEY_ROW_ID
                + "=" + INVOICED_PRODUCT_TABLE + "." + KEY_INVOICE_ID
                + " INNER JOIN " + INVOICED_PRODUCT_TABLE + " ON "
                + INVOICED_PRODUCT_TABLE + "." + KEY_PRODUCT_CODE + "="
                + TABLE_NAME + "." + KEY_CODE + " WHERE " + ITINERARY_TABLE
                + "." + PHARMACY_ID + "='" + pharmacyId + "' AND " + TABLE_NAME
                + "." + KEY_CODE + "='" + productId + "'";
        Log.w("Product: getTotalInvoicedQuantityForProduct", QUERY);

        Cursor cursor = database.rawQuery(QUERY, null);
        cursor.moveToFirst();
        long total = 0;

        if (cursor.getCount() == 0) {
            total = 0;
        } else {
            total = cursor.getLong(0);
        }
        cursor.close();

        return total;
    }

    /**
     * return principle names as array list from table products
     * (poor db structure of previous developers)
     *
     * @return
     */

    public ArrayList<String> getPrincipleList(){

        ArrayList<String> principleList = new  ArrayList<String>();

        Cursor cursor = database.rawQuery("select distinct principle from products ",null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String principleName = cursor.getString(0);
            principleList.add(principleName);
            cursor.moveToNext();
        }

        cursor.close();

        return principleList;
    }

    /**
     * return category as array list from table products
     *
     * @param principle
     * @return
     */
    public ArrayList<String> getCategoryListForPriciple(String principle){

        ArrayList<String> principleList = new  ArrayList<String>();

        Cursor cursor = database.rawQuery("select category from products where principle = ?  group by category",new String[]{principle});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String principleName = cursor.getString(0);
            principleList.add(principleName);
            cursor.moveToNext();
        }

        cursor.close();

        return principleList;
    }



    public ArrayList<Mst_ProductMaster> getProductsByPricipleAndCategory(String principle,String category) {
        ArrayList<Mst_ProductMaster> products = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from products  inner join productRepStore  on products.code = productRepStore.product_code  And principle = ? and category = ? ", new String[]{principle, category});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Mst_ProductMaster productData = new Mst_ProductMaster();
// STOP           productData.setRowId(Integer.parseInt(cursor.getString(21)));// rowId of repstore
            productData.setId(cursor.getString(22));// product id from rep stores
// STOP           productData.setCode( cursor.getString(2));// Code
            productData.setBrand( cursor.getString(3));// brand
            productData.setUnitName( cursor.getString(4));// unit name
            productData.setUnitSize(cursor.getInt(5));// size
// STOP           productData.setGenericName(cursor.getString(6));// generic name
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
            Log.i("hj->",""+Integer.parseInt(cursor.getString(25)));
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



    public ArrayList<String> getBatchForProdcuctAndPharma(String code){

        ArrayList<String> principleList = new  ArrayList<String>();

        Cursor cursor = database.rawQuery("select force from products where code = ?",new String[]{code});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String principleName = cursor.getString(0);
            principleList.add(principleName);
            cursor.moveToNext();
        }

        cursor.close();

        return principleList;
    }

    public String getProductCodeByName(String name){
        String code = "";
        openReadableDatabase();
        Cursor cursor = database.rawQuery("select code from products where pro_des = ?", new String[]{name});
        // ArrayList<String> customerNames = new ArrayList<String>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            code = cursor.getString(0);
            cursor.moveToNext();
        }

        closeDatabase();

        return code;
    }

    public ArrayList<String> getProductForceByCode(String code){
        ArrayList<String> forceList = new ArrayList<>();
        String force = "";
        openReadableDatabase();
        Cursor cursor = database.rawQuery("select force from products where code = ?", new String[]{code});
        // ArrayList<String> customerNames = new ArrayList<String>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            force = cursor.getString(0);
            forceList.add(force);
            cursor.moveToNext();
        }

        closeDatabase();

        return forceList;
    }

    public int getRowCount(){
        openReadableDatabase();
        String count = "SELECT count(*) FROM products";
        Cursor mcursor = database.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        closeDatabase();

        return  icount;
    }
}
