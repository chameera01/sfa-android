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
 */
public class ItineraryAdp {
    //
    private static final String KEY_ROW_ID = "row_id";
    private static final String KEY_ITN_ID = "itn_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_VISIT_NO = "visit_no";
    private static final String KEY_GLB_PHARMACY_ID = "glb_pharmacy_id";
    private static final String KEY_GLB_PHARMACY_CODE = "glb_pharmacy_code";
    private static final String KEY_NAME = "name";
    private static final String KEY_TARGET = "target";
    private static final String KEY_ITN_REP_ID = "itn_rep_id";
    private static final String KEY_IS_ACTIVE = "is_active";
    private static final String KEY_TIME_STAMP = "time_stamp";
    private static final String KEY_IS_TEMPORARY_CUSTOMER = "is_temporary_customer";
    private static final String KEY_IS_INVOICED = "is_invoiced";
    private static final String KEY_ITN_ROW_ID = "itn_row_id";
    String[] columns = new String[]{KEY_ROW_ID, KEY_ITN_ID, KEY_DATE,
            KEY_VISIT_NO, KEY_GLB_PHARMACY_ID, KEY_GLB_PHARMACY_CODE, KEY_NAME,
            KEY_TARGET, KEY_ITN_REP_ID, KEY_IS_ACTIVE, KEY_TIME_STAMP,
            KEY_IS_TEMPORARY_CUSTOMER, KEY_IS_INVOICED, KEY_ITN_ROW_ID};
    private static final String TABLE_NAME = "itinerary";
    private static final String ITINERARY_CREATE = "CREATE TABLE " + TABLE_NAME
            + " (" + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ITN_ID + " TEXT NOT NULL, " + KEY_DATE + " TEXT, "
            + KEY_VISIT_NO + " TEXT, " + KEY_GLB_PHARMACY_ID + " TEXT, "
            + KEY_GLB_PHARMACY_CODE + " TEXT, " + KEY_NAME + " TEXT, "
            + KEY_TARGET + " TEXT, " + KEY_ITN_REP_ID + " TEXT, "
            + KEY_IS_ACTIVE + " TEXT, " + KEY_TIME_STAMP + " TEXT, "
            + KEY_IS_TEMPORARY_CUSTOMER + " TEXT, " + KEY_IS_INVOICED
            + " TEXT," + KEY_ITN_ROW_ID + " TEXT NOT NULL " + ");";
    public final Context itineraryContext;
    public DBHelper databaseHelper;
    private SQLiteDatabase database;

    public ItineraryAdp(Context c) {
        itineraryContext = c;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(ITINERARY_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public ItineraryAdp openWritableDatabase() throws SQLException {
        databaseHelper = new DBHelper(itineraryContext);
        database = databaseHelper.getWritableDatabase();
        return this;

    }

    public ItineraryAdp openReadableDatabase() throws SQLException {
        databaseHelper = new DBHelper(itineraryContext);
        database = databaseHelper.getReadableDatabase();
        return this;

    }

    public void closeDatabase() throws SQLException {
        databaseHelper.close();
    }

    public long insertItinerary(String itnRowId, String itnId, String date, String visitNo,
                                String pharmacyId, String pharmacyCode, String name, String target,
                                String repId, String timeStamp, String isTemporaryCustomer,
                                String isInvoiced, String isActive) throws SQLException {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();

        cv.put(KEY_ITN_ID, itnId);
        cv.put(KEY_DATE, date);
        cv.put(KEY_VISIT_NO, visitNo);
        cv.put(KEY_GLB_PHARMACY_ID, pharmacyId);
        cv.put(KEY_GLB_PHARMACY_CODE, pharmacyCode);
        cv.put(KEY_NAME, name);
        cv.put(KEY_TARGET, target);
        cv.put(KEY_ITN_REP_ID, repId);
        cv.put(KEY_IS_ACTIVE, isActive);
        cv.put(KEY_TIME_STAMP, timeStamp);
        cv.put(KEY_IS_TEMPORARY_CUSTOMER, isTemporaryCustomer);
        cv.put(KEY_IS_INVOICED, isInvoiced);
        cv.put(KEY_ITN_ROW_ID, itnRowId);

        return database.insert(TABLE_NAME, null, cv);
    }

    public List<String[]> getAllItineraries() {

        Log.w("Log", " in getAllItineraries");
        List<String[]> itineraryList = new ArrayList<String[]>();

        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null,
                null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] data = new String[1];
            data[0] = cursor.getString(0);
            data[1] = cursor.getString(1);
            data[2] = cursor.getString(2);
            data[3] = cursor.getString(3);
            data[4] = cursor.getString(4);
            data[5] = cursor.getString(5);
            data[6] = cursor.getString(6);
            data[7] = cursor.getString(7);
            data[8] = cursor.getString(8);
            data[9] = cursor.getString(9);

            itineraryList.add(data);
            cursor.moveToNext();
        }

        cursor.close();
        Log.w("Log", "userLoginList size : " + itineraryList.size());

        return itineraryList;
    }

    public List<String[]> getAllItinerariesForADay(String requiredDate) {

        Log.w("Log", " in getAllItineraries");
        List<String[]> itineraryList = new ArrayList<String[]>();

        Cursor cursor = database.query(TABLE_NAME, columns, KEY_DATE
                        + " LIKE ?", new String[]{requiredDate + "%"}, null, null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] data = new String[14];
            data[0] = cursor.getString(0);
            data[1] = cursor.getString(1);
            data[2] = cursor.getString(2);
            data[3] = cursor.getString(3);
            data[4] = cursor.getString(4);
            data[5] = cursor.getString(5);
            data[6] = cursor.getString(6);
            data[7] = cursor.getString(7);
            data[8] = cursor.getString(8);
            data[9] = cursor.getString(9);
            data[10] = cursor.getString(10);
            data[11] = cursor.getString(11);
            data[12] = cursor.getString(12);
            data[13] = cursor.getString(13);

            Log.w("Log", "data[0] sisze : " + data[0]);
            itineraryList.add(data);
            cursor.moveToNext();
        }

        cursor.close();
        Log.w("Log", "userLoginList size : " + itineraryList.size());

        return itineraryList;
    }

    public String[] getItineraryDetailsById(String id) {

        Log.w("Log", " in getAllItineraryById :" + id);

        final String CUSTOMER_TABLE_NAME = "customers";
        final String KEY_PHARMACY_CODE = "pharmacy_code";
        final String KEY_ADDRESS = "address";
        final String KEY_TELEPHONE = "telephone";
        final String IMAGE_ID = "image_id";

        final String MY_QUERY = "SELECT itn." + KEY_NAME + ", itn."
                + KEY_TARGET + ", cust." + KEY_ADDRESS + ", cust."
                + KEY_TELEPHONE + ", itn." + KEY_GLB_PHARMACY_ID + ", itn."
                + KEY_IS_ACTIVE + ", itn." + KEY_IS_TEMPORARY_CUSTOMER + ", "
                + "cust." + IMAGE_ID+ ", "
                +"itn."+KEY_ITN_ID
                + " FROM " + TABLE_NAME + " itn INNER JOIN "
                + CUSTOMER_TABLE_NAME + " cust ON itn." + KEY_GLB_PHARMACY_ID
                + "=cust." + KEY_PHARMACY_CODE + " WHERE itn." + KEY_ROW_ID
                + "=" + id;


        Cursor cursor = database.rawQuery(MY_QUERY, null);

        Log.w("Itenarary", "Query: " + MY_QUERY);

        cursor.moveToFirst();
        String[] data = new String[9];

        while (!cursor.isAfterLast()) {

            data[0] = cursor.getString(0);
            data[1] = cursor.getString(1);
            data[2] = cursor.getString(2);
            data[3] = cursor.getString(3);
            data[4] = cursor.getString(4);
            data[5] = cursor.getString(5);
            data[6] = cursor.getString(6);
            data[7] = cursor.getString(7);
            data[8] = cursor.getString(8);

            Log.w("Log", "Cust Name : " + data[2]);
            cursor.moveToNext();
        }

        cursor.close();
        Log.w("Log", "userLoginList size : " + data.length);

        return data;
    }

    public String[] getItineraryDetailsForTemporaryCustomer(String id) {

        Log.w("Log", " in getAllItineraryById");

        Log.w("Log", " in getAllItineraryById :" + id);

        final String CUSTOMERS_PENDING_APPROVAL = "customers_pending_approval";
        final String ADDRESS = "address";
        final String TELEPHONE = "telephone";
        final String AREA = "area";
        final String TOWN = "town";
        final String DISTRICT = "district";

        final String MY_QUERY = "SELECT "
                + TABLE_NAME + "." + KEY_NAME + ", "
                + TABLE_NAME + "." + KEY_TARGET + ", "
                + CUSTOMERS_PENDING_APPROVAL + "." + ADDRESS + ", "
                + CUSTOMERS_PENDING_APPROVAL + "." + AREA + ", "
                + CUSTOMERS_PENDING_APPROVAL + "." + TOWN + ", "
                + CUSTOMERS_PENDING_APPROVAL + "." + DISTRICT + ", "
                + CUSTOMERS_PENDING_APPROVAL + "." + TELEPHONE + ", "
                + CUSTOMERS_PENDING_APPROVAL + "." + KEY_ROW_ID + ", "
                + CUSTOMERS_PENDING_APPROVAL + "." + KEY_GLB_PHARMACY_CODE
                + " FROM "
                + TABLE_NAME + " INNER JOIN " + CUSTOMERS_PENDING_APPROVAL
                + " ON " + TABLE_NAME + "." + KEY_GLB_PHARMACY_CODE + "="
                + CUSTOMERS_PENDING_APPROVAL + "." + KEY_GLB_PHARMACY_CODE + " WHERE "
                + TABLE_NAME + "." + KEY_ROW_ID + "=?";

        Log.w("Itenarary", "Query: " + MY_QUERY);

        Cursor cursor = database.rawQuery(MY_QUERY,
                new String[]{String.valueOf(id)});

        cursor.moveToFirst();
        String[] data = new String[9];

        while (!cursor.isAfterLast()) {

            data[0] = cursor.getString(0);
            data[1] = cursor.getString(1);
            data[2] = cursor.getString(2);
            data[3] = cursor.getString(3);
            data[4] = cursor.getString(4);
            data[5] = cursor.getString(5);
            data[6] = cursor.getString(6);
            data[7] = cursor.getString(7);
            data[8] = cursor.getString(8);

            Log.w("Log", "Cust Name : " + data[2]);
            cursor.moveToNext();
        }

        cursor.close();
        Log.w("Log", "userLoginList size : " + data.length);

        return data;
    }

    public String getItineraryDayTarget(String requiredDate) {

        Log.w("Log", "requiredDate : " + requiredDate);
        int result = 0;
        Cursor cursor = database.rawQuery("SELECT SUM(" + KEY_TARGET
                        + ") FROM " + TABLE_NAME + " WHERE " + KEY_DATE + " LIKE ?",
                new String[]{requiredDate + "%"});

        if (cursor.moveToFirst()) {
            result = cursor.getInt(0);
        }

        Log.w("Log", "Sum : " + Integer.toString(result));

        return Integer.toString(result);
    }

    public String getLastInvoicedItinerary() {

        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_ROW_ID},
                KEY_IS_ACTIVE + "='true'", null, null, null, null);
        cursor.moveToFirst();
        String rowId = "null";
        while (!cursor.isAfterLast()) {
            rowId = cursor.getString(0);
            cursor.moveToNext();
        }
        return rowId;
    }

    public String getItineraryStatus(String id) {

        Cursor cursor = database.query(TABLE_NAME,
                new String[]{KEY_IS_TEMPORARY_CUSTOMER}, KEY_ROW_ID + "='"
                        + id + "'", null, null, null, null);
        cursor.moveToFirst();
        String rowId = "null";
        while (!cursor.isAfterLast()) {
            rowId = cursor.getString(0);
            cursor.moveToNext();
        }
        return rowId;
    }

    public void setIsActiveTrue(String id) {

        String updateQuery = "UPDATE " + TABLE_NAME + " SET " + KEY_IS_ACTIVE
                + "='true' WHERE " + KEY_ROW_ID + "='" + id + "'";
        database.execSQL(updateQuery);
        Log.w("<Itinerary>Set active is true: ", "done!");
    }

    public void setIsActiveFalse(String id) {

        String updateQuery = "UPDATE " + TABLE_NAME + " SET " + KEY_IS_ACTIVE
                + "='false', " + KEY_IS_INVOICED + "='true' WHERE "
                + KEY_ROW_ID + "='" + id + "'";
        database.execSQL(updateQuery);

    }

    public String getCustomerTargetById(String Id) throws SQLException {

        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_ROW_ID,
                KEY_TARGET}, KEY_ROW_ID + " = ?", new String[]{Id}, null, null, null);
        cursor.moveToFirst();

        String target = cursor.getString(1);
        return target;
    }

    public String[] getAllItinerariesForPharmacyId(String pharmacyId) {

        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_ITN_ID},
                KEY_GLB_PHARMACY_ID + " = ?", new String[]{pharmacyId},
                null, null, null);
        String[] itinerariesByPharmacyId = new String[cursor.getCount()];
        cursor.moveToFirst();
        int i = 0;
        while (!cursor.isAfterLast()) {
            itinerariesByPharmacyId[i] = cursor.getString(0);
            Log.w("Iiinerary DBItineraryies by pharmacy Id", pharmacyId + ""
                    + cursor.getString(0));
            i++;
            cursor.moveToNext();
        }

        return itinerariesByPharmacyId;
    }

    public String[] getAllItinerayRowIdsForPharmacyId(String pharmacyId) {

        Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_ROW_ID},
                KEY_GLB_PHARMACY_ID + " = ?", new String[]{pharmacyId},
                null, null, null);
        String[] itinerariesByPharmacyId = new String[cursor.getCount()];
        cursor.moveToFirst();
        int i = 0;
        while (!cursor.isAfterLast()) {
            itinerariesByPharmacyId[i] = cursor.getString(0);
            Log.w("Iiinerary DBItineraryies by pharmacy Id", pharmacyId + " " + cursor.getString(0));
            i++;
            cursor.moveToNext();
        }

        return itinerariesByPharmacyId;
    }

    public String getLastUpdatedItineraryId() {


        final String MY_QUERY = "SELECT " + KEY_ITN_ROW_ID + " FROM " + TABLE_NAME + " WHERE " + KEY_ITN_ID + " not like '%C%'";

        Log.w("Itenarary", "Query: " + MY_QUERY);

        Cursor cursor = database.rawQuery(MY_QUERY, null);

        String productId = "";

        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            productId = cursor.getString(0);
        }


        cursor.close();

        Log.w("Iiinerary  Id", "Iiinerary  Id : "
                + productId);

        return productId;
    }

    public String getMaxItnId() {

        ArrayList<String> productRowIds = new ArrayList<String>();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{KEY_ITN_ROW_ID}, null, null, null, null, null,
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


        return String.valueOf(maxId);

    }


    public String getDateforSelectedROWID( String id){


        String itinararyDate = "";
        final String query = "SELECT  " + KEY_DATE + " FROM "
                + TABLE_NAME+" where " + KEY_ROW_ID + " ='" + id + "' ";
        Cursor cursor = database.rawQuery(query, null);
        // ArrayList<String> districtList = new ArrayList<String>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            itinararyDate =cursor.getString(0);
            Log.w("customer getlists", cursor.getString(0));
            cursor.moveToNext();
        }

        return itinararyDate;
    }


    public String getGlobalPharmaIDForRowID( String id){


        String pharmaId = "";
        final String query = "SELECT  " + KEY_GLB_PHARMACY_ID+ " FROM "
                + TABLE_NAME+" where " + KEY_ROW_ID + " ='" + id + "' ";
        Cursor cursor = database.rawQuery(query, null);
        // ArrayList<String> districtList = new ArrayList<String>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            pharmaId =cursor.getString(0);
            Log.w("customer pharma id", cursor.getString(0));
            cursor.moveToNext();
        }

        return pharmaId;
    }
}

