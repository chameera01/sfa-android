package com.example.ahmed.sfa.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Dialogs.Alert;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.PermissionManager;
import com.example.ahmed.sfa.controllers.RandomNumberGenerator;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.Itinerary;
import com.example.ahmed.sfa.models.SalesInvoiceModel;
import com.example.ahmed.sfa.models.SalesInvoiceSummary;
import com.example.ahmed.sfa.models.SalesPayment;
import com.example.ahmed.sfa.models.SalesReturnSummary;

import java.util.ArrayList;
import java.util.List;

public class ReturnSummary extends AppCompatActivity {

    private static final String TYPE_RETURN = "Return";
    TextView subtotal, discount, returntot, invoiceQty, freeQty, returnQty, total, credit, cheque, cash, shelf, order, free;
    Button save;
    String customerNo;
    Itinerary itinerary;
    Location lastKnownLocation;
    SalesPayment payment;

    ReturnSummary.DBAdapter dbAdapter;

    private boolean launchedForResult;
    private Alert alert;
    private double invTotal;
    private SalesInvoiceSummary sim;
    private String repId;
    private String deviecId;
    private ArrayList<SalesInvoiceModel> data;
    private TableLayout table;
    private ArrayList<SalesInvoiceModel> list;
    private DialogInterface.OnClickListener success = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_return_summary);

        alert = new Alert(this);

        init();
        getRepAndDeviceId();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void init() {
        PermissionManager pm = new PermissionManager(this);
        if (pm.checkForLocationPermission()) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            List<String> providers = locationManager.getProviders(true);
            for (String provider : providers) {
                Location tempLoc = locationManager.getLastKnownLocation(provider);
                if (tempLoc == null) continue;
                if (lastKnownLocation == null || tempLoc.getAccuracy() > lastKnownLocation.getAccuracy()) {
                    lastKnownLocation = tempLoc;
                }
            }
            //lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            /* -----------------------------------------------------------------------------------------------*/
            //lastKnownLocation = new Location(LocationManager.GPS_PROVIDER);//this line has to be removed
            /* -----------------------------------------------------------------------------------------------*/
            //this has been added since emmulator failed to send gps points

            if (lastKnownLocation != null) {
                setContentView(R.layout.activity_return_summary);

                sim = new SalesInvoiceSummary();

                customerNo = getIntent().getStringExtra(Constants.CUSTOMER_NO);
                itinerary = getIntent().getParcelableExtra(Constants.ITINERARY);
                invTotal = getIntent().getDoubleExtra("INV_TOTAL", 0.00);
                payment = getIntent().getParcelableExtra("RETURN_SUMMARY");
                //list = getIntent().getParcelableArrayListExtra("SIM_ARRAYLIST");
                data = getIntent().getParcelableArrayListExtra("SIM_ARRAYLIST");
                launchedForResult = getIntent().getBooleanExtra("LAUNCHED_FOR_RESULT", false);
                Log.d("BLUE", "inside ret sum_lfr: " + launchedForResult);

                Log.d("BLUE", "list size: " + data.size());

                dbAdapter = new ReturnSummary.DBAdapter(this);
                dbAdapter.createTempTable();

                table = (TableLayout) findViewById(R.id.return_summary_table);

                subtotal = (TextView) findViewById(R.id.rs_subtotal);
                returnQty = (TextView) findViewById(R.id.rs_return_qty);
                discount = (TextView) findViewById(R.id.rs_discount);
                total = (TextView) findViewById(R.id.rs_total);
                returntot = (TextView) findViewById(R.id.rs_return_total);
                invoiceQty = (TextView) findViewById(R.id.rs_invoice_qty);
                freeQty = (TextView) findViewById(R.id.rs_free_qty);
                save = (Button) findViewById(R.id.rs_save);
                credit = (TextView) findViewById(R.id.rs_credit);
                cash = (TextView) findViewById(R.id.rs_cash);
                cheque = (TextView) findViewById(R.id.rs_cheque);

                save.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ReturnSummary.this);
                                builder
                                        .setTitle("Confirm ")
                                        .setMessage("Are you sure?")
                                        .setIcon(null)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.d("RET", "inside positive btn click");
                                                submit(payment);
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                );

//                launchedForResult = false;
//                if (getCallingActivity() != null) {
//                    launchedForResult = true;
//                    Toast.makeText(getApplicationContext(), "Launced for result", Toast.LENGTH_SHORT).show();
//                }

                for (int i = 0; i < data.size(); i++) {
                    table.addView(getView(i));

                }

                subtotal.setText(payment.getSubTotal() + "");
                discount.setText(payment.getTotalDiscount() + "");
                returntot.setText(payment.getSubTotal() + "");
                returnQty.setText(payment.getInvQty() + "");
                invoiceQty.setText(payment.getInvQty() + "");
                freeQty.setText(payment.getFreeQty() + "");
//                returnQty.setText(payment.getReturnQty()+"");


                cash.setText(payment.getCash() + "");
                credit.setText(payment.getCredit() + "");
                cheque.setText(payment.getCheque() + "");

            } else {
                Toast.makeText(this, "Location unavailable", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.location_not_found_error_layout);
            }

        } else {
            Toast.makeText(this, "Permission Unavailable", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.location_not_found_error_layout);

        }
    }

    public TableRow getView(int i) {
        SalesInvoiceModel salesrow = data.get(i);
        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.sales_summary_row, table, false);
        if (i % 2 == 0) {
            row.setBackgroundColor(Color.LTGRAY);
        } else {
            row.setBackgroundColor(Color.GRAY);
        }
        TextView code = (TextView) row.findViewById(R.id.code_e);
        TextView product = (TextView) row.findViewById(R.id.product_e);
        TextView batch = (TextView) row.findViewById(R.id.batch_e);
        TextView expiry = (TextView) row.findViewById(R.id.expiry_e);
        TextView unitprice = (TextView) row.findViewById(R.id.unit_price);
        TextView stock = (TextView) row.findViewById(R.id.stock_e);
        TextView shelf = (TextView) row.findViewById(R.id.shelf_e);
        TextView request = (TextView) row.findViewById(R.id.request_e);
        TextView order = (TextView) row.findViewById(R.id.order_e);
        TextView free = (TextView) row.findViewById(R.id.free_e);
        TextView disc = (TextView) row.findViewById(R.id.dsc_e);
        TextView linVal = (TextView) row.findViewById(R.id.line_val_e);


        code.setText(salesrow.getCode());
        product.setText(salesrow.getProduct());
        batch.setText(salesrow.getBatchNumber());
        expiry.setText(salesrow.getExpiryDate());
        unitprice.setText(salesrow.getUnitPrice() + "");
        stock.setText(salesrow.getStock() + "");
        shelf.setText(salesrow.getShelf() + "");
        request.setText(salesrow.getRequest() + "");
        order.setText(salesrow.getOrder() + "");
        free.setText(salesrow.getFree() + "");
        disc.setText(salesrow.getDiscountRate() + "");
        linVal.setText(salesrow.getLineValue() + "");

        Toast.makeText(getApplicationContext(), "val " + salesrow.getOrder(), Toast.LENGTH_SHORT).show();
        return row;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //Toast.makeText(this," permission Received",Toast.LENGTH_SHORT).show();
        switch (requestCode) {
            case PermissionManager.MY_PERMISSIONS_REQUEST_LOCATION:
                //Toast.makeText(this," Location",Toast.LENGTH_SHORT).show();
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                }
                break;
        }
    }

    private void setResult() {

        Log.d("SUM", "inside setResult ");
        Intent result = new Intent(ReturnSummary.this, SalesInvoicePayment.class);
        SalesReturnSummary returnSummary = new SalesReturnSummary(getSalesInvoiceSummary(data));
        Log.d("SUM", "inside setResult_returnQty: " + returnSummary.getReturnQty() + " retTot: " + returnSummary.getReturnTot());
        result.putExtra("RETURNED_LIST", data);
        result.putExtra(Constants.SALES_RETURN_SUMMARY, returnSummary);
        result.putExtra("RETURN_HEADER_SUMMARY", payment);
        result.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(result);

    }

    public SalesInvoiceSummary getSalesInvoiceSummary(List<SalesInvoiceModel> data) {
        double discount = 0.0;
        double subtotal = 0.0;
        double total = 0.0;
        double returnTotal = 0.0;

        int invoicedQty = 0;
        int freeQty = 0;

        int shelfQty = 0;
        int orderQty = 0;

        for (int i = 0; i < data.size(); i++) {
            discount += data.get(i).getDiscount();
            subtotal += data.get(i).getSubtotalVal();
            total += data.get(i).getLineValue();
            invoicedQty += data.get(i).getInvoiceQuantity();
            freeQty += data.get(i).getFree();


            shelfQty += data.get(i).getShelf();
            orderQty += data.get(i).getOrder();
        }
        Log.d("INVOICE", "invoicedQty: " + invoicedQty);
        Log.d("SUM", "inside getSalesInvoiceSummary_returnQty: " + orderQty + " retTot: " + total);

        return new SalesInvoiceSummary(discount, subtotal, total, invoicedQty, shelfQty, orderQty, freeQty);
    }

    public void getRepAndDeviceId() {
        try {
            Log.d("MGT", "inside getRepID");
            com.example.ahmed.sfa.controllers.adapters.DBAdapter dbAdapter = new com.example.ahmed.sfa.controllers.adapters.DBAdapter(this);
            Log.d("MGT", dbAdapter.toString());
            dbAdapter.openDB();
            Cursor deviceCursor = dbAdapter.runQuery("select * from DeviceCheckController where ACTIVESTATUS = 'YES'");
            Log.d("MGT", deviceCursor.toString());
            Cursor repCursor = null;

            if (deviceCursor.getCount() == 0) {
                Log.d("MGT", "dcursor is empty");
            } else {
                Log.d("MGT", "dcursor is not empty");
            }

            if (deviceCursor.moveToFirst()) {
                Log.d("MGT", "cursor moved to first_device");
                deviecId = deviceCursor.getString(deviceCursor.getColumnIndex("DeviceID"));
                Log.d("MGT", "Inside getRepId_" + deviecId);
                deviceCursor.moveToNext();
                Log.d("MGT", "cursor moved to next_device");
            }

            repCursor = dbAdapter.runQuery("select * from Mst_RepTable");

            if (repCursor.getCount() == 0) {
                Log.d("MGT", "rcursor is empty");
            } else {
                Log.d("MGT", "rcursor is not empty");
                Log.d("MGT", String.valueOf(repCursor.getCount()));
            }

            if (repCursor.moveToFirst()) {
                Log.d("MGT", "cursor moved to first_rep");
                repId = repCursor.getString(repCursor.getColumnIndex("RepID"));
                Log.d("MGT", "Inside getRepId_" + repId);
                repCursor.moveToNext();
                Log.d("MGT", "cursor moved to next_rep");
            }

            dbAdapter.closeDB();
        } catch (Exception e) {
            String msg = (e.getMessage() == null) ? "getrepid failed!" : e.getMessage();
            Log.e("data:", msg);
        }

    }

    private void submit(SalesPayment payment) {
//        ArrayList<SalesInvoiceModel> list = dbAdapter.getInvoicedItems();
//        payment = new SalesPayment(getSalesInvoiceSummary(list));

        Log.d("RET", "inside submit");
        Log.d("RET", "inside submit_returnQty: " + payment.getReturnQty() + " retTot: " + payment.getReturnTot());
        Log.d("RET", "inside submit_subtot: " + payment.getSubTotal() + " tot: " + payment.getTotal());
        Log.d("RET", "inside submit- invoicedQty: " + payment.getInvQty());
        Log.d("RET", "inside submit- ReturnQty: " + payment.getReturnQty());

        boolean isError = false;
        if (launchedForResult) {
            Log.d("RET", "inside lfr true: " + launchedForResult);
            setResult();

        } else {

            Log.d("NUM", "inside return summary submit method: this should not be invoked in OnInvoiceReturn");

            int i = dbAdapter.getInvoiceReturnNo();//this method will return the invoice number we should use to enter the data
            String invoNum = String.valueOf(i);
            if (i != -1) {
                //insert data into sales return header
                if (dbAdapter.insertIntoSalesReturnHeader(payment, lastKnownLocation, itinerary.getId(), customerNo, invoNum, data.size())) {
                    int lastSalesReturnHeaderID = dbAdapter.getLastSalesReturnHeaderID();

                    if (lastSalesReturnHeaderID != -1) {

                        int val = dbAdapter.insertDataToSalesReturnDetails(data, lastSalesReturnHeaderID);
                        if (val == data.size()) {
//                            int updatedStockCount = dbAdapter.updateStock(data);
//                            if (updatedStockCount == data.size()) {
                            if (dbAdapter.insertToDailyRouteDetails(itinerary, customerNo, invoNum)) {
                                dbAdapter.increaseInvoiceReturnNo();

                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setMessage("Successfully saved!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(ReturnSummary.this, Home.class);
                                                startActivity(intent);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                                //run manual sync
                                ManualSyncFromOtherActivities msfa = new ManualSyncFromOtherActivities(ReturnSummary.this);
                                Boolean status = msfa.uploadSalesDetails(repId);

                                Log.w("Success > ", "increased return no");
                            } else {
                                Log.w("Error > ", "updating itinerary details error");
                                isError = true;
                            }
//                            } else {
//                                Log.w("Error > ", "updating stock error");
//                                isError = true;
//                            }
                        } else {
                            Log.w("Error > ", "not all sales return details entered to db");
                            isError = true;
                        }
                    } else {
                        Log.w("Error > ", "retrieving data from sales header error");
                        isError = true;
                    }
                } else {
                    //inserting to sales return error
                    isError = true;
                    Log.w("Error >", "Inserting data into sales return header");
                }
            } else {
                //invoice number is -1 show the corresponding error
                Log.w("Error >", "Getting invoice return no");
                isError = true;
            }

            if (isError) alert.showAlert("Error", "Invoicing error try again", null, null);
        }

    }

    class DBAdapter extends BaseDBAdapter {

        public DBAdapter(Context c) {
            super(c);
        }

        public void createTempTable() {
            openDB();
            String sql = "DROP TABLE IF EXISTS temp_return";
            db.execSQL(sql);//drops the temprary invoice table if it
            //exists already
            sql = "CREATE TABLE temp_return(_id INTEGER PRIMARY KEY AUTOINCREMENT,ItemCode TEXT,Description TEXT,BatchNumber" +
                    " TEXT,ExpiryDate TEXT,SellingPrice REAL,Qty INTEGER DEFAULT 0" +
                    ",Shelf INTEGER DEFAULT 0,Request INTEGER DEFAULT 0,returnQty INTEGER DEFAULT 0" +
                    ",Free INTEGER DEFAULT 0,Disc Real DEFAULT 0.0,LineVal Real DEFAULT 0.0,PrincipleID TEXT,BrandID TEXT,ServerID TEXT,RetailPrice Real);";
            db.execSQL(sql); //create the table again;

            //fill in data
            sql = "INSERT INTO temp_return(ItemCode,Description,BatchNumber" +
                    ",ExpiryDate,SellingPrice,Qty,PrincipleID,BrandID,ServerID,RetailPrice) SELECT a.ItemCode,a.Description," +
                    "b.BatchNumber,b.ExpiryDate,a.RetailPrice,b.Qty,a.PrincipleID,a.BrandID,b.ServerID, a.RetailPrice" +
                    " FROM Mst_ProductMaster a Left join Tr_TabStock b " +
                    "on a.ItemCode  = b.ItemCode";
            db.execSQL(sql);
            closeDB();
        }


        public String getPaymentType(SalesPayment payment) {
            String toReturn = "";
            toReturn += payment.getCredit() > 0 ? "CR" : "";
            toReturn += payment.getCheque() > 0 ? "CH" : "";
            toReturn += payment.getCash() > 0 ? "CA" : "";

            toReturn += payment.getCash() > 0 && payment.getCredit() > 0 ? "CACR" : "";
            toReturn += payment.getCash() > 0 && payment.getCheque() > 0 ? "CACH" : "";
            toReturn += payment.getCheque() > 0 && payment.getCredit() > 0 ? "CRCH" : "";

            return toReturn;

        }

        public boolean insertIntoSalesReturnHeader(SalesPayment payment, Location loc, String itineraryId, String customerNO, String invNo, int prodCount) {

            Log.d("NUM", "inside return summary insert method");
            openDB();

            ContentValues cv = new ContentValues();
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[0], itineraryId);
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[1], customerNO);
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[2], invNo);
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[3], DateManager.dateWithTimeToday());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[4], getPaymentType(payment));///TODO: change this to requested way 02/12/2017
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[5], payment.getSubTotal());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[6], payment.getTotal());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[7], payment.getFullInvDisc());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[8], payment.getDiscount());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[9], "NO TYPE"); //discount type

            //if the activity launched from invoice activity this will be positive, negative otherwise
            //edit
            if (getCallingActivity() != null) {
                String act = getCallingActivity().getClassName();
                Log.d("INVOICE", "act: " + act);
                switch (act) {
                    case "com.example.ahmed.sfa.activities.SalesInvoicePayment":
                        cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[10], Constants.INACTIVE);
                        Log.d("INVOICE", "from SalesInvoicePayment");
                        break;

                    default:
                        cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[10], Constants.ACTIVE);
                        break;
                }
            } else {
                Log.d("INVOICE", "calling activity is null");
            }

            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[11], Constants.ACTIVE);
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[12], prodCount);
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[13], TYPE_RETURN);
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[14], loc.getLatitude());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[15], loc.getLongitude());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[16], Constants.ACTIVE);
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[17], DateManager.dateToday());

            long result = db.insert(Constants.SALES_RETURN_TABLE, null, cv);
            closeDB();
            if (result > 0) return true;
            else return false;
        }

        public int getInvoiceReturnNo() {
            openDB();
            Cursor cursor = db.rawQuery("SELECT InvoiceReturnNo FROM Mst_InvoiceNumbers_Management", null);
            if (cursor.moveToNext()) {
                int val = cursor.getInt(0);
                closeDB();
                return val;
            } else {
                closeDB();
                Log.w("catch", "here 0");
                return -1;
            }
        }


        public int getLastSalesReturnHeaderID() {
            int result = -1;

            openDB();
            Cursor cursor = db.rawQuery("SELECT _id FROM " + Constants.SALES_RETURN_TABLE, null);
            if (cursor.moveToLast()) {
                result = cursor.getInt(0);
                Log.d("RET", "inside getLastID: " + result);
            }
            closeDB();

            return result;

        }

        public ArrayList<SalesInvoiceModel> getInvoicedItems() {

            Log.d("RET", "inside ReturnSummary: getInvoicedItems");

            ArrayList<SalesInvoiceModel> data = new ArrayList<>();
            openDB();

            String sql = "SELECT * from temp_return WHERE returnQty !=0";
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                try {
                    SalesInvoiceModel salesInvoiceModel = new SalesInvoiceModel(cursor.getString(0),
                            cursor.getString(1), cursor.getString(2), cursor.getString(3),
                            cursor.getString(4), cursor.getDouble(5), cursor.getInt(6));
                    salesInvoiceModel.setShelf(cursor.getInt(7));
                    salesInvoiceModel.setRequest(cursor.getInt(8));
                    salesInvoiceModel.setOrder(cursor.getInt(9));
                    Log.d("RET", "inside getInvoicedItems setOrder: " + cursor.getInt(9));
                    salesInvoiceModel.setFree(cursor.getInt(10));
                    salesInvoiceModel.setDiscountRate(cursor.getDouble(11));
                    salesInvoiceModel.setLineValue(cursor.getDouble(12));
                    salesInvoiceModel.setPrincipleID(cursor.getString(13));
                    salesInvoiceModel.setBrandID(cursor.getString(14));
                    salesInvoiceModel.setServerID(cursor.getString(15));
                    salesInvoiceModel.setRetailPrice(cursor.getDouble(16));

                    data.add(salesInvoiceModel);
                } catch (Exception ex) {
                    Log.d("RET", "ex: " + ex.getLocalizedMessage());
                    Toast.makeText(getApplicationContext(), "Wrong Value ", Toast.LENGTH_SHORT).show();
                }


            }
            closeDB();

            Log.d("RET", "inside getInvoicedItems size: " + data.size());

            return data;
        }


        public int insertDataToSalesReturnDetails(ArrayList<SalesInvoiceModel> data, int salesReturnHeaderID) {
            Log.d("RET", "inside insertDataToSalesReturnDetails_list size: " + data.size());
            int completed = 0;
            for (SalesInvoiceModel model : data) {
                if (insertRowToSalesReturnDetails(model, salesReturnHeaderID)) {
                    completed++;
                    Log.d("RET", "count: " + completed);
                } else break;
            }
            return completed;
        }

        //insert a single item details to the sales details
        private boolean insertRowToSalesReturnDetails(SalesInvoiceModel rowModel, int salesReturnHeaderID) {

            Log.d("RET", "inside insertRowToSalesReturnDetails");
            openDB();
            ContentValues cv = new ContentValues();
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[0], salesReturnHeaderID);//get the id
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[1], rowModel.getCode());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[2], rowModel.getUnitPrice());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[3], rowModel.getBatchNumber());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[4], rowModel.getExpiryDate());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[5], rowModel.getDiscountRate());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[6], rowModel.getDiscount());
            //cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[7],null);//ignoring would enter null value
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[8], rowModel.getOrder());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[9], rowModel.getFree());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[10], rowModel.getLineValue());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[11], Constants.ACTIVE);
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[12], DateManager.dateWithTimeToday());

            long result = db.insert(Constants.SALES_RETURN_DETAILS_TABLE, null, cv);

            closeDB();
            if (result > 0) return true;
            else return false;
        }

        public int updateStock(ArrayList<SalesInvoiceModel> data) {
            int val = 0;
            for (SalesInvoiceModel model : data) {
                if (updateStockRow(model)) val++;
                else return val;
            }
            return val;
        }

        public boolean insertToDailyRouteDetails(Itinerary itinerary, String customrNo, String invNo) {
            openDB();
            ContentValues cv = new ContentValues();
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[0], RandomNumberGenerator.generateRandomCode(RandomNumberGenerator.GENERATE_ALPHABANUMERIC, 10));
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[1], DateManager.dateToday());
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[2], itinerary.getId());
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[3], customrNo);
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[4], itinerary.isPlanned() ? Constants.ACTIVE : Constants.INACTIVE);
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[5], Constants.ACTIVE);
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[6], invNo);
            //cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[7],null);//reasons will be null since we invoice this user
            //cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[8],null);//comment will also be null since we are invoicing
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[9], Constants.INACTIVE);
            //cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[10],"");//

            long val = db.insert(Constants.DAILY_ROUTES_TABLE, null, cv);
            closeDB();

            if (val > 0) return true;
            else return false;
        }

        //this method is to reduce the invoiced quanity from the Tr_TabStock
        //we have assumed tht item code is unique here
        private boolean updateStockRow(SalesInvoiceModel model) {
            boolean result = false;
            int valToAssign = model.getStock() + model.getInvoiceQuantity();
            openDB();
            ContentValues cv = new ContentValues();
            cv.put(Constants.TAB_STOCK_COLUMNS[8], valToAssign);
            //String whereClauseArgs[] = {model.getServerID()};
            String whereClauseArgs[] = {model.getCode(), model.getBatchNumber(), model.getUnitPrice() + ""};

            Log.w("id", model.getServerID() + "");

            int updatedCount = 0;
            if (!(model.getBatchNumber() == null || model.getBatchNumber().equalsIgnoreCase(""))) {
                updatedCount = db.update(Constants.TAB_STOCK, cv, Constants.TAB_STOCK_COLUMNS[3] + "=? AND " +
                        Constants.TAB_STOCK_COLUMNS[4] + "=? AND " + Constants.TAB_STOCK_COLUMNS[6] + "=?", whereClauseArgs);
            }

            if (updatedCount <= 0) {
                cv.put(Constants.TAB_STOCK_COLUMNS[0], model.getServerID());
                cv.put(Constants.TAB_STOCK_COLUMNS[1], model.getId());
                cv.put(Constants.TAB_STOCK_COLUMNS[2], model.getBrandID());
                cv.put(Constants.TAB_STOCK_COLUMNS[3], model.getCode());
                cv.put(Constants.TAB_STOCK_COLUMNS[4], model.getCode());
                cv.put(Constants.TAB_STOCK_COLUMNS[5], "01-01-2025");
                cv.put(Constants.TAB_STOCK_COLUMNS[6], model.getUnitPrice());
                cv.put(Constants.TAB_STOCK_COLUMNS[7], model.getRetailPrice());
                cv.put(Constants.TAB_STOCK_COLUMNS[8], valToAssign);
                cv.put(Constants.TAB_STOCK_COLUMNS[9], DateManager.dateToday());
                long a = db.insert(Constants.TAB_STOCK, null, cv);
                if (a != -1) {
                    updatedCount = 1;
                }
            }
            closeDB();
            Log.w("UpdateCount :", "" + updatedCount);
            if (updatedCount > 0) return true;
            else return false;

        }

        //this will increase one value of the invoice num in the db
        public boolean increaseInvoiceReturnNo() {

            Log.d("NUM", "inside return summary increase");
            int curVal = getInvoiceReturnNo();
            openDB();
            int newVal = curVal + 1;
            db.execSQL("UPDATE Mst_InvoiceNumbers_Management SET InvoiceReturnNo=" + newVal + " Where InvoiceReturnNo=" + curVal + ";");
            closeDB();
            return true;
        }


    }

}
