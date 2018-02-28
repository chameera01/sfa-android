package com.example.ahmed.sfa.activities;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.ahmed.sfa.activities.supportactivities.BluetoothActivity;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.PermissionManager;
import com.example.ahmed.sfa.controllers.RandomNumberGenerator;
import com.example.ahmed.sfa.controllers.adapters.DeviceListAdapter;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.Cheque;
import com.example.ahmed.sfa.models.Itinerary;
import com.example.ahmed.sfa.models.PrincipleDiscountModel;
import com.example.ahmed.sfa.models.SalesInvoiceModel;
import com.example.ahmed.sfa.models.SalesPayment;
import com.example.ahmed.sfa.models.SalesReturnSummary;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 3/27/2017.
 */

public class SalesSummaryActivity extends AppCompatActivity {
    private static final String TAG = "BLUE";
    private static final String TYPE_INVOICE = "Invoice";
    private final BroadcastReceiver receiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
                switch (mode) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "receiver2: Discoverability enabled");
                        break;

                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "onReceive: Discoverability enabled. Able to receive connections.");
                        break;

                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "onReceive: Discoverability disabled. Not able to receive connections.");
                        break;

                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "onReceive: Connecting..");
                        break;

                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "onReceive: Connected");
                        break;

                }
            }
        }
    };
    PermissionManager pman;
    ArrayList<SalesInvoiceModel> data;
    SalesPayment payment;
    Cheque chequeModel;
    TableLayout table;
    TextView subtotal;
    TextView discount;
    TextView returntot;
    TextView invoiceQty;
    TextView freeQty;
    TextView returnQty;
    TextView credit;
    TextView cheque;
    TextView cash;
    Button saveBtn, printBtn;
    String customerNo;
    Itinerary itinerary;
    Location lastKnownLocation;
    Location loc;
    DBAdapter dbAdapter;

    int assignedReturned;
    String deviceId, repId;
    Return.DBAdapter dba = new Return().new DBAdapter(this);
    ArrayList<SalesInvoiceModel> datalist;
    DecimalFormat formatter;
    Dialog.OnClickListener dismissListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
    private Alert alert;
    private int onInvoiceReturnNo;
    private int invoiceNo;
    private String invNum;
    private boolean lineDisc = false;
    private String discType;
    private double discAmount;
    private double fd;
    private boolean fullDisc = false;
    private boolean brandDisc = false;
    private double cashDisc = 0.0;
    private BluetoothAdapter btAdapter;
    private DeviceListAdapter deviceAdapter;
    private Boolean status = false;
    private String bill;
    private final BroadcastReceiver receiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(btAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, btAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE_OFF");
                        break;

                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "onReceive: STATE_TURNING_OFF");
                        break;

                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "onReceive: STATE_ON");
                        status = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent actIntent = new Intent(SalesSummaryActivity.this, BluetoothActivity.class);
                                actIntent.putExtra("BILL", bill);
                                startActivity(actIntent);
                            }
                        }, 2500);

                        break;

                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "onReceive: STATE_TURNING_ON");
                        break;

                }
            }
        }
    };
    private int size;
    private boolean workDone = false;
    private boolean reg1;
    private boolean reg2;
    private double brandDiscount = 0.0;
    private SalesReturnSummary returnSummary;
    private SalesPayment returnHeaderSummary;
    private Button homeBtn;
    private DialogInterface.OnClickListener successful = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            setInvoiceBill(payment, customerNo, invNum, data);
            Log.d(TAG, "Bill: " + bill);
            printBtn.setEnabled(true);
            homeBtn.setEnabled(true);
            saveBtn.setEnabled(false);
            dialog.dismiss();

            workDone = true;

            //run manual sync
            ManualSyncFromOtherActivities msfa = new ManualSyncFromOtherActivities(SalesSummaryActivity.this);
            msfa.uploadSalesDetails(repId);


        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reg1 && reg2) {
            unregisterReceiver(receiver1);
            unregisterReceiver(receiver2);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        dbAdapter = new DBAdapter(SalesSummaryActivity.this);
        init();
        getRepAndDeviceId();
    }

    public void enableDisableBT() {
        if (btAdapter == null) {
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if (!btAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: Enabling Bluetooth..");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);

            IntentFilter btIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(receiver1, btIntent);
        }
        if (btAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: Disabling Bluetooth..");
            btAdapter.disable();

            IntentFilter btIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(receiver1, btIntent);
            reg1 = true;

        }
    }

    public Boolean enableDisableDiscoverable() {

        try {
            Log.d(TAG, "enableDisableDiscoverable: Making device discoverable for 300 seconds");
            Intent discoverIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverIntent);

            IntentFilter btIntent = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
            registerReceiver(receiver2, btIntent);
            reg2 = true;
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Ex: " + e.getLocalizedMessage());
            return false;
        }

    }

    public void setInvoiceBill(SalesPayment payment, String customerNo, String invoiceNo, ArrayList<SalesInvoiceModel> data) {

        DBAdapter adapter = new DBAdapter(SalesSummaryActivity.this);

        String customerName = adapter.getCustomerNameforBill(customerNo);
        String customerTel = adapter.getCustomerPhoneforBill(customerNo);
        String repName = adapter.getRepNameforBill(repId);

        //unchanged
        String header =
                "\tHesperus Marketing (Pvt) Ltd.\n" +
                        "\tNo.100, 5th Lane, Colombo 03.\n" +
                        "     Tel: +94 11 2576736, +94 11 2576737\n" +
                        "\t     Fax: +94 11 2576746\n" +
                        "\t\tTax Invoice\n" +
                        "---------------------------------------------\n\n";

        if (customerName.length() > 11) {
            customerName = customerName.substring(0, 11);
        } else if (customerName.length() < 11) {
            customerName = String.format("%-11s", customerName);
        }

        if (customerTel.length() > 11) {
            customerTel = customerTel.substring(0, 11);
        } else if (customerTel.length() < 11) {
            customerTel = String.format("%-11s", customerTel);
        }

        String invoiceTo =
                "Invoice to:\tRep. Code: " + repId + "\n" +
                        "" + customerName + "\tRep.: " + repName + "\n" +
                        "" + customerTel + "\tInvoice No.: " + invoiceNo + "\n" +
                        "\t\tDate: " + DateManager.dateWithTimeToday() + "\n\n";

        //unchanged
        String billHeader = "Description\t\tQty\tPrice\tValue\n" +
                "---------------------------------------------\n" +
                "";


        String billBody = "";
        for (int i = 0; i < data.size(); i++) {

            String word = (data.get(i).getProduct());
            String prod = "";
            String prod2 = "";
            String dash = "-";

            //before 30
            if (word.length() > 21) {
                prod = word.substring(0, 21);
                prod2 = word.substring(21, word.length());

                billBody += "" + prod + dash + "\t" + data.get(i).getInvoiceQuantity() + "\t" + data.get(i).getUnitPrice() + "\t" + data.get(i).getLineValue() + "\n" + prod2 + "\n";
            } else if (word.length() < 21) {
                prod = String.format("%-21s", word);

                billBody += "" + prod + "\t" + data.get(i).getInvoiceQuantity() + "\t" + data.get(i).getUnitPrice() + "\t" + data.get(i).getLineValue() + "\n";
            } else {
                billBody += "" + word + "\t" + data.get(i).getInvoiceQuantity() + "\t" + data.get(i).getUnitPrice() + "\t" + data.get(i).getLineValue() + "\n";
            }
        }

        String billFooter = "---------------------------------------------\n" +
                String.format("%-11s", "Total") + "\t\t" + payment.getInvQty() + "\t\t" + Double.parseDouble(formatter.format(payment.getSubTotal())) + "\n" +
                "---------------------------------------------\n\n\n";

        //unchanged
        String brandHeader =
                //"---------------------------------------------\n" +
                "Brand\tTot.Qty\tF.Qty\tDi.V\tDi.R\tValue\n" +
                        "---------------------------------------------\n";

        DBAdapter brandAdapter = new DBAdapter(SalesSummaryActivity.this);
        ArrayList<BrandDetails> list = brandAdapter.getBrandDetails();


        String brandBody = "";
        int totQty = 0;
        int freeQty = 0;
        double totDisc = 0.0;
        double totVal = 0.0;

        for (int x = 0; x < size; x++) {

            String br = (list.get(x).getBrandName());

            if (br.length() > 7) {
                br = br.substring(0, 7);
            } else if (br.length() < 7) {
                br = String.format("%-7s", br);
            }

            brandBody += "" + br + "\t" + list.get(x).getTotalQty() + "\t" + list.get(x).getFreeQty() + "\t" + brandAdapter.getDiscountValue(list.get(x).getBrandID()) + "\t" + brandAdapter.getDiscountRate(list.get(x).getBrandID()) + "\t" + list.get(x).getTotalValue() + "\n";

            Log.d("BRAND", brandBody);

            totQty += list.get(x).getTotalQty();
            freeQty += list.get(x).getFreeQty();
            totDisc += brandDiscount;
            totVal += list.get(x).getTotalValue();

        }

        String brandFooter = "---------------------------------------------\n" +
                "Total\t" + totQty + "\t" + freeQty + "\t" + totDisc + "\t\t" + Double.parseDouble(formatter.format(totVal)) + "\n" +
                "---------------------------------------------\n\n\n";

        Log.d("BRAND", brandFooter);

        bill = header + invoiceTo + billHeader + billBody + billFooter + brandHeader + brandBody + brandFooter;

        double returnTot = 0.0;
        int returnQty = 0;

        if (onInvoiceReturnNo != 0) {

            returnTot = payment.getReturnTot();
            returnQty = payment.getReturnQty();

            String returnHeader =
                    //"---------------------------------------------\n" +
                    "Description\t\tQty\tPrice\tValue\t\n" +
                            "---------------------------------------------\n";

            String returnBody = "";

            Log.d(TAG, "ret size: " + datalist.size());
            if (datalist != null && datalist.size() > 0) {
                for (int y = 0; y < datalist.size(); y++) {

                    String item = (datalist.get(y).getProduct());
                    String part1, part2;
                    String dash = "-";

                    if (item.length() > 21) {
                        part1 = item.substring(0, 21);
                        part2 = item.substring(21, item.length());

                        returnBody += "" + part1 + dash + "\t" + datalist.get(y).getOrder() + "\t" + datalist.get(y).getUnitPrice() + "\t" + datalist.get(y).getLineValue() + "\n" + part2 + "\n";

                    } else if (item.length() < 21) {
                        item = String.format("%-21s", item);
                        returnBody += "" + item + "\t" + datalist.get(y).getOrder() + "\t" + datalist.get(y).getUnitPrice() + "\t" + datalist.get(y).getLineValue() + "\n";
                    } else {
                        returnBody += "" + item + "\t" + datalist.get(y).getOrder() + "\t" + datalist.get(y).getUnitPrice() + "\t" + datalist.get(y).getLineValue() + "\n";

                    }

                }
            }

            String returnFooter = "---------------------------------------------\n" +
                    String.format("%-11s", "Total") + "\t\t" + returnQty + "\t\t" + returnTot + "\n" +
                    "---------------------------------------------\n\n";

            bill += returnHeader + returnBody + returnFooter;
        }

        double finTot = 0.0;
        int cdays = 0;
        if (cashDisc != 0.0) {
            finTot = payment.getCash();
        } else {
            finTot = payment.getCredit();
            cdays = payment.getCreditDays();
        }

        String summary =
                String.format("%-22s", "Gross Value:") + "\t" + Double.parseDouble(formatter.format(payment.getSubTotal())) + "\n" +
                        String.format("%-22s", "Return Value:") + "\t" + Double.parseDouble(formatter.format(returnTot)) + "\n" +
                        "Full Invoice Discount:\t" + Double.parseDouble(formatter.format(fd)) + "\n" +
                        String.format("%-22s", "VAT%:") + "\t" + "--" + "\n" +
                        String.format("%-22s", "Cash Discount:") + "\t" + cashDisc + "\n" +
                        String.format("%-22s", "Need to pay:") + "\t" + Double.parseDouble(formatter.format(finTot)) + "\n" +
                        String.format("%-22s", "Credit Days:") + "\t" + cdays + "\n\n\n";

        String signature = "------------------------\t" + DateManager.dateWithTimeToday() + "\n" +
                "Customer's Sign. and Seal\tDate and Time\n\n";

        bill += summary + signature;

    }

    public void getRepAndDeviceId() {
        try {
            com.example.ahmed.sfa.controllers.adapters.DBAdapter dbAdapter = new com.example.ahmed.sfa.controllers.adapters.DBAdapter(this);
            dbAdapter.openDB();
            Cursor deviceCursor = dbAdapter.runQuery("select * from DeviceCheckController where ACTIVESTATUS = 'YES'");
            Cursor repCursor = null;
            deviceCursor.moveToFirst();

            deviceId = deviceCursor.getString(deviceCursor.getColumnIndex("DeviceID"));
            repCursor = dbAdapter.runQuery("select * from Mst_RepTable");
            repCursor.moveToFirst();
            repId = repCursor.getString(repCursor.getColumnIndex("RepID"));
            dbAdapter.closeDB();
        } catch (Exception e) {
            Log.e("data:", e.getMessage());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //Toast.makeText(this," permission Received",Toast.LENGTH_SHORT).show();
        switch (requestCode) {
            case PermissionManager.MY_PERMISSIONS_REQUEST_LOCATION:
                //Toast.makeText(this," Location",Toast.LENGTH_SHORT).show();
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                    //Toast.makeText(this,"Location permission Received",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void insertReturnData() {
        boolean isError = false;
        Return.DBAdapter dbAdapter = new Return().new DBAdapter(this);
        ArrayList<SalesInvoiceModel> list = dbAdapter.getInvoicedItems();

        //onInvoiceReturnNo = dbAdapter.getInvoiceReturnNo();

        if (invoiceNo != -1) {
            //insert data into sales return header
            if (dbAdapter.insertIntoSalesReturnHeader(returnHeaderSummary, lastKnownLocation, itinerary.getId(), customerNo, onInvoiceReturnNo, true)) {

                int lastSalesReturnHeaderID = dbAdapter.getLastSalesReturnHeaderID();
                if (lastSalesReturnHeaderID != -1) {
                    ArrayList<SalesInvoiceModel> data = datalist;
                    int val = dbAdapter.insertDataToSalesReturnDetails(data, lastSalesReturnHeaderID);
                    assignedReturned = lastSalesReturnHeaderID;
                    if (val == data.size()) {
                        int updatedStockCount = dbAdapter.updateStock(data);
                        if (updatedStockCount == data.size()) {
                            if (dbAdapter.insertToDailyRouteDetails(itinerary, customerNo, invNum)) {
//                                if (dbAdapter.increaseInvoiceReturnNo()) {

                                alert.showAlert("Success", "Invoice Completed!", "OK", null, successful, null);

//                                } else {
//                                    Log.w("Error > ", "increment on sales return num error");
//                                    isError = true;
//                                }
                            } else {
                                Log.w("Error > ", "updating itinerary details error");
                                isError = true;
                            }
                        } else {
                            Log.w("Error > ", "updating stock error");
                            isError = true;
                        }
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

    private void insertInvoiceData() {

        boolean isError = false;
        if (pman.checkForLocationPermission()) {
            LocationManager locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
            loc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            Toast.makeText(getApplicationContext(), "Location Permission Not available", Toast.LENGTH_SHORT).show();
        }

        //Return.DBAdapter adapter = new Return().new DBAdapter(this);

        String serialCode = RandomNumberGenerator.generateRandomCode(RandomNumberGenerator.GENERATE_ALPHABANUMERIC, "SRL", 14);

        invoiceNo = dbAdapter.getInvoiceNo();

//        if (payment.getReturnQty() > 0) {
//            onInvoiceReturnNo = adapter.getInvoiceReturnNo();
//        } else {
//            onInvoiceReturnNo = 0;
//        }


        if (invoiceNo != -1) {

            //insert into sales header and get sales header id
            int i = invoiceNo + 1;
            invNum = deviceId + String.valueOf(i); //deviceID + invoiceNo added to sales header

            if (dbAdapter.insertIntoSalesHeader(payment, lastKnownLocation, itinerary.getId(), customerNo, invNum, onInvoiceReturnNo, data.size())) {

                int salesHeaderID = dbAdapter.getLastSalesHeaderID();
                if (salesHeaderID != -1) {
                    int val = dbAdapter.insertDataToSalesDetails(data, salesHeaderID);
                    if (val == data.size()) {
                        int r = dbAdapter.updateStock(data);

                        if (r == data.size()) {
                            if (dbAdapter.insertToInvoiceOutStanding(payment, invNum)) {

                                if (dbAdapter.insertToDailyRouteDetails(itinerary, customerNo, invNum)) {

                                    if (dbAdapter.updateItineraryDetailsTable(itinerary.getId(), customerNo)) {

                                        if (dbAdapter.insertChequeDetails(chequeModel, serialCode, invNum, customerNo, payment.getTotal())) {

                                            if (dbAdapter.increaseInvoiceNo()) {
                                                if (payment.getReturnQty() > 0) {
                                                    dbAdapter.increaseInvoiceReturnNo();
                                                    Log.d("NUM", "increased invoice No.");
                                                }

                                                dbAdapter.insertPrincipleDiscountDetails(dbAdapter.getPrincipleDiscountList(), invNum, customerNo);

                                                if (payment.getReturnQty() <= 0) {
                                                    alert.showAlert("Success", "Invoice Completed!", "OK", null, successful, null);
                                                }
                                                //run manual sync
//                                                ManualSyncFromOtherActivities msfa = new ManualSyncFromOtherActivities(SalesSummaryActivity.this);
//                                                Boolean status = msfa.uploadSalesDetails(repId);

                                            } else {
                                                Log.d("CASH", "incr inv no");
                                                isError = true;//increasing invoice number error
                                            }
                                        } else {
                                            Log.d("CASH", "cheque");
                                            isError = true;//inserting cheque details error
                                        }
                                    } else {
                                        Log.d("CASH", "itinerary");
                                        isError = true;//updating itinerary table error
                                    }
                                } else {
                                    Log.d("CASH", "daily route");
                                    isError = true;//inserting data into daily route details error
                                }
                            } else {
                                Log.d("CASH", "sales outstanding");
                                isError = true;//inserting data into sales outstanding error;
                            }

                        } else {
                            Log.d("CASH", "stock values");
                            isError = true;//not all product's Stock values are updated;
                        }
                    } else {
                        Log.d("CASH", "sales details");
                        isError = true;//Not all data inserted to the sales details table
                    }
                } else {
                    Log.d("CASH", "sales header");
                    isError = true; //retreiving id from sales header error;
                }
            } else {
                Log.d("CASH", "sales headr");
                isError = true;//inserting into sales header error
            }
        } else {
            Log.d("CASH", "invoice num ret");
            isError = true;//invoice number retreiving error
        }

        if (isError) alert.showAlert("Error", "Invoicing error, try again!", null, null);

        //this value has to be taken from the home ui but hard coded here for convenience
        /**if(dbAdapter.insertChequeDetails(chequeModel,serialCode,invNum,customerNo,payment.getTotal()))
         Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
         else Toast.makeText(getApplicationContext(),"Un Successfull",Toast.LENGTH_SHORT).show();*/

    }

//    private boolean printInvoice(String customerNo) {
//
//        try {
//            String printData = "Customer: " + customerNo;
//            Bundle bundleToView = new Bundle();
//            bundleToView.putString("PrintData", printData);
//
//            // Print invoice
//
//            Intent activityIntent = new Intent(SalesSummaryActivity.this, PrintUtility.class);
//            activityIntent.putExtras(bundleToView);
//            startActivityForResult(activityIntent, 0);
//            return true;
//
//        }catch (Exception e){
//            Toast.makeText(SalesSummaryActivity.this,"Ex: "+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
//            return false;
//        }
//    }

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
                setContentView(R.layout.sales_invoice_summary_activity);
                alert = new Alert(this);

                returnSummary = null;

                itinerary = getIntent().getParcelableExtra(Constants.ITINERARY);

                customerNo = getIntent().getStringExtra(Constants.CUSTOMER_NO);
                Log.d("Error", customerNo);

                pman = new PermissionManager(this);

                data = getIntent().getParcelableArrayListExtra(Constants.DATA_ARRAY_NAME);
                payment = getIntent().getParcelableExtra(Constants.SALES_PAYMENT_SUMMARY);
                returnSummary = getIntent().getParcelableExtra(Constants.SALES_RETURN_SUMMARY);
                returnHeaderSummary = getIntent().getParcelableExtra("RETURN_HEADER_SUMMARY");

                Log.d("CASH", "inside last ui payment.Total: " + payment.getTotal());
                Log.d("CASH", "payment.Cash: " + payment.getCash());
                Log.d("CASH", "payment.Cheque: " + payment.getCheque());
                Log.d("CASH", "inside last ui payment.Credit: " + payment.getCredit());

                chequeModel = getIntent().getParcelableExtra(Constants.CHEQUE);

                lineDisc = getIntent().getBooleanExtra("DISCOUNT_TYPE_LINE", false);
                fullDisc = getIntent().getBooleanExtra("DISCOUNT_TYPE_FULL", false);
                brandDisc = getIntent().getBooleanExtra("DISCOUNT_TYPE_BRAND", false);

                cashDisc = getIntent().getDoubleExtra("CASH_DISCOUNT", 0.0);

                formatter = new DecimalFormat("0.00");
                cashDisc = Double.parseDouble(formatter.format(cashDisc));

                datalist = new ArrayList<>();

                datalist = getIntent().getParcelableArrayListExtra("RETURNED_LIST");
                //Log.d("BLUE","inside summary act-list size: "+datalist.size());

                getDiscountType();

                table = (TableLayout) findViewById(R.id.si_pay_sum_data);


                subtotal = (TextView) findViewById(R.id.si_pay_sum_sub_tot);
                discount = (TextView) findViewById(R.id.si_pay_sum_disc);
                returntot = (TextView) findViewById(R.id.si_pay_sum_return_tot);
                invoiceQty = (TextView) findViewById(R.id.si_pay_sum_inv_qty);
                freeQty = (TextView) findViewById(R.id.si_pay_sum_free_qty);
                returnQty = (TextView) findViewById(R.id.si_pay_sum__return_qty);

                cash = (TextView) findViewById(R.id.si_pay_sum_cash);
                cheque = (TextView) findViewById(R.id.si_pay_sum_chq);
                credit = (TextView) findViewById(R.id.si_pay_sum_crdt);

                homeBtn = (Button) findViewById(R.id.home_button_ss);
                homeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SalesSummaryActivity.this, Home.class);
                        startActivity(intent);
                    }
                });

                printBtn = (Button) findViewById(R.id.print_button_ss);
                printBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(SalesSummaryActivity.this);
                        builder.setTitle("Confirm ")
                                .setMessage("To use Bluetooth Services, you must first turn on Bluetooth. Turn on Bluetooth now?")
                                .setIcon(null)
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //continue
                                        enableDisableBT();
                                        enableDisableDiscoverable();
                                        Log.d(TAG, "Status: " + status);

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                });

                saveBtn = (Button) findViewById(R.id.save_button_ss);
                saveBtn.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SalesSummaryActivity.this);
                                builder.setTitle("Confirm ")
                                        .setMessage("Are you sure ?")
                                        .setIcon(null)
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (payment.getReturnQty() > 0) {
                                                    onInvoiceReturnNo = dba.getInvoiceReturnNo();
                                                    Log.d("NUM", "no: " + onInvoiceReturnNo);
                                                    insertInvoiceData();
                                                    insertReturnData();
                                                } else {
                                                    onInvoiceReturnNo = 0;
                                                    insertInvoiceData();
                                                }
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
                        });

                for (int i = 0; i < data.size(); i++) {
                    table.addView(getView(i));

                }

                subtotal.setText(payment.getSubTotal() + "");
                discount.setText(payment.getTotalDiscount() + "");
                returntot.setText(payment.getReturnTot() + "");
                returnQty.setText(payment.getReturnQty() + "");
                invoiceQty.setText(payment.getInvQty() + "");
                freeQty.setText(payment.getFreeQty() + "");
                returnQty.setText(payment.getReturnQty() + "");


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

    private void getDiscountType() {

        fd = payment.getSubTotal() * (payment.getFullInvDisc() / 100);

        if (lineDisc && !brandDisc && !fullDisc) {
            discType = "PD";
            discAmount = payment.getDiscount();
        } else if (brandDisc && !lineDisc && !fullDisc) {
            discType = "BD";
            discAmount = payment.getTotalPrincipleDiscounts();
        } else if (fullDisc && !lineDisc && !brandDisc) {
            discType = "FD";
            discAmount = fd;
        } else if (lineDisc && brandDisc && !fullDisc) {
            discType = "LBD";
            discAmount = payment.getDiscount() + payment.getTotalPrincipleDiscounts();
        } else if (lineDisc && fullDisc && !brandDisc) {
            discType = "LFD";
            discAmount = payment.getDiscount() + fd;
        } else if (brandDisc && fullDisc && !lineDisc) {
            discType = "BFD";
            discAmount = payment.getTotalPrincipleDiscounts() + fd;
        } else if (lineDisc && brandDisc && fullDisc) {
            discType = "LBFD";
            discAmount = payment.getDiscount() + payment.getTotalPrincipleDiscounts();
        } else {
            discType = "N/A";
            discAmount = 0.0;
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

    public String getPaymentType(SalesPayment payment) {

        String toReturn = "";
        if (cashDisc > 0.0) {
            toReturn = "CA";
        } else {
            toReturn += payment.getCredit() > 0 ? "CR" : "";
            toReturn += payment.getCheque() > 0 ? "CH" : "";
            toReturn += payment.getCash() > 0 ? "CA" : "";

            toReturn += payment.getCash() > 0 && payment.getCredit() > 0 ? "CACR" : "";
            toReturn += payment.getCash() > 0 && payment.getCheque() > 0 ? "CACH" : "";
            toReturn += payment.getCheque() > 0 && payment.getCredit() > 0 ? "CRCH" : "";
            //what if customer pays with all 3 methods?
        }

        return toReturn;

    }

    class DBAdapter extends BaseDBAdapter {

        public DBAdapter(Context c) {
            super(c);
        }

        public boolean insertChequeDetails(Cheque chq, String serialCode, String invoiceNumber, String customerNo, double invTotal) {
            openDB();
            ContentValues cv = new ContentValues();

            cv.put(Constants.CHEQUE_TABLE_COLUMNS[0], serialCode);
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[1], DateManager.dateToday());
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[2], invoiceNumber);
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[3], customerNo);
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[4], invTotal);
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[5], chq.getChequeVal());
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[6], chq.getChequeNum());
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[7], chq.getBank());
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[8], chq.getCollectionDate());
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[9], chq.getRealizedDate());
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[10], 1);//not uploaded
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[11], 0);//isupdate yes
            //status unknows so ignoring that field
            //status update date is also unknown
            long val = db.insert(Constants.CHEQUE_DETAILS_TABLE_NAME, null, cv);
            closeDB();

            if (val > 0) return true;
            else return false;
        }

        public boolean insertIntoSalesHeader(SalesPayment payment, Location loc, String itineraryId, String customerNO, String invNo, int returnNo, int totalProductCount) {
            openDB();

            Log.d("CASH", "return: " + payment.getReturnQty());

            ContentValues cv = new ContentValues();
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[0], itineraryId);
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[1], customerNO);
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[2], invNo);
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[3], DateManager.dateWithTimeToday());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[4], getPaymentType(payment));//done ///TODO: change for payment type
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[5], payment.getSubTotal());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[6], payment.getTotal());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[7], payment.getFullInvDisc());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[8], discAmount);
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[9], discType);
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[10], payment.getReturnQty() > 0 ? Constants.INACTIVE : Constants.ACTIVE);//for now we dont include is on return or not so its one
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[11], returnNo == -1 ? null : returnNo);//leaving un touched will insert null value;
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[12], payment.getReturnTot());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[13], payment.getCredit());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[14], payment.getCash());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[15], payment.getCheque());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[16], Constants.ACTIVE);//is print is default 0 and after printing it shld be changed to 1
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[17], totalProductCount);//product count changed to assign the value from paramter
            // on 02/12/2017// product count is assumed as total invoiced qty
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[18], TYPE_INVOICE);
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[19], loc.getLatitude());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[20], loc.getLongitude());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[21], Constants.ACTIVE); //isUpload default is 0
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[22], DateManager.dateWithTimeToday());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[23], cashDisc);

            long result = db.insert(Constants.SALES_HEADER_TABLE, null, cv);
            closeDB();
            if (result > 0) return true;
            else return false;
        }


        //loop through the invoice details and add each item one by one by calling insertRowToSalesDetails method
        public int insertDataToSalesDetails(ArrayList<SalesInvoiceModel> data, int id) {
            int completed = 0;
            for (SalesInvoiceModel model : data) {
                if (insertRowToSalesDetails(model, id)) completed++;
                else break;
            }
            return completed;
        }

        //insert a single item details to the sales details
        private boolean insertRowToSalesDetails(SalesInvoiceModel rowModel, int id) {

            openDB();
            ContentValues cv = new ContentValues();
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[0], id);//get the id
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[1], rowModel.getCode());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[2], rowModel.getUnitPrice());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[3], rowModel.getBatchNumber());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[4], rowModel.getExpiryDate());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[5], rowModel.getDiscountRate());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[6], rowModel.getDiscount());
            //cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[7],null);//ignoring would enter null value
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[8], rowModel.getShelf());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[9], rowModel.getRequest());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[10], rowModel.getOrder());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[11], rowModel.getFree());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[12], rowModel.getLineValue());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[13], Constants.ACTIVE);
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[14], DateManager.dateWithTimeToday());

            long result = db.insert(Constants.SALES_DETAILS_TABLE, null, cv);

            closeDB();
            if (result > 0) return true;
            else return false;
        }


        //on Database connection errro sytem will return -1 else will return the current invoice No
        //on the system
        private int getInvoiceNo() {
            openDB();
            Cursor cursor = db.rawQuery("SELECT InvoiceNo FROM Mst_InvoiceNumbers_Management", null);
            if (cursor.moveToNext()) {
                int val = cursor.getInt(0);
                closeDB();
                return val;
            } else {
                Log.w("catch", "here 0");
                return -1;
            }
        }


        //this will incerase one value of the invoice num in the db
        private boolean increaseInvoiceNo() {
            int curVal = getInvoiceNo();
            openDB();
            int newVal = curVal + 1;
//            String invNo = deviceId+String.valueOf(newVal); //deviceID + invNo from Mst_InvNos Table increased by one
            db.execSQL("UPDATE Mst_InvoiceNumbers_Management SET InvoiceNo=" + newVal + " Where InvoiceNo=" + curVal + ";");
//            db.execSQL("INSERT INTO Mst_InvoiceNumbers_Management VALUES()");
            closeDB();
            return true;
        }

        public boolean increaseInvoiceReturnNo() {

            Return.DBAdapter rdb = new Return().new DBAdapter(SalesSummaryActivity.this);
            int oldValue = rdb.getInvoiceReturnNo();

            openDB();
            int newValue = oldValue + 1;
            db.execSQL("UPDATE Mst_InvoiceNumbers_Management SET InvoiceReturnNo=" + newValue + " Where InvoiceReturnNo=" + oldValue + ";");

            closeDB();
            return true;
        }

        public int getLastSalesHeaderID() {
            int result = -1;

            openDB();
            Cursor cursor = db.rawQuery("SELECT _id FROM " + Constants.SALES_HEADER_TABLE, null);
            if (cursor.moveToLast()) {
                result = cursor.getInt(0);
            }
            closeDB();

            return result;

        }

        //this method is to reduce the invoiced quanity from the Tr_TabStock
        //we have assumed tht item code is unique here
        private boolean updateStockRow(SalesInvoiceModel model) {
            boolean result = false;
            int valToAssign = model.getStock() - model.getInvoiceQuantity();
            openDB();
            ContentValues cv = new ContentValues();
            cv.put(Constants.TAB_STOCK_COLUMNS[8], valToAssign);
            String whereClauseArgs[] = {model.getCode(), model.getBatchNumber(), model.getUnitPrice() + ""};
            Log.w("id", model.getId() + "");
            int updatedCount = db.update(Constants.TAB_STOCK, cv, Constants.TAB_STOCK_COLUMNS[3] + "=? AND " +
                    Constants.TAB_STOCK_COLUMNS[4] + "=? AND " +
                    Constants.TAB_STOCK_COLUMNS[6] + "=?", whereClauseArgs);
            closeDB();
            Log.w("UpdateCount :", "" + updatedCount);
            if (updatedCount > 0) return true;
            else return false;

        }

        //reduce the stock from for each item in the db
        public int updateStock(ArrayList<SalesInvoiceModel> data) {
            int val = 0;
            for (SalesInvoiceModel model : data) {
                if (updateStockRow(model)) val++;
                else return val;
            }
            return val;
        }

        //this table details is for the transacetion payment details
        public boolean insertToInvoiceOutStanding(SalesPayment payment, String invNo) {

            openDB();
            ContentValues cv = new ContentValues();
            String serialCode = RandomNumberGenerator.generateRandomCode(RandomNumberGenerator.GENERATE_ALPHABANUMERIC, "SER", 13);
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[0], serialCode);//auto genarated serialCode passed from caller
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[1], DateManager.dateToday());//Todays date created from DateManager
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[2], invNo);//invoice num passed from cller functions
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[3], customerNo);//customerNo of the selcted custoemr
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[4], payment.getTotal());//from sales payment
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[5], payment.getCredit());//from the sales payment
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[6], payment.getCredit());//from the asles payment
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[7], payment.getCreditDays());//from the sales payment
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[8], DateManager.dateToday());//Date created wiht date manager
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[9], 0);//for this release this has been fixed as a 0
            //cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[10],);//last update type is unknown for now have to clarifiy
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[11], DateManager.dateToday());//date today
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[12], Constants.ACTIVE);//making the column as inactive or negative
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[13], Constants.ACTIVE);//marking the column as active or positive

            long val = db.insert(Constants.INVOICE_OUTSTANDING, null, cv);
            closeDB();

            if (val > 0) return true;
            else return false;
        }

        //this is kind of a log table where we enter details about the
        //itinerary and its type
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

        //should update the itinereary details table about this customer
        // about the transaction type
        public boolean updateItineraryDetailsTable(String itineraryId, String CustomerNo) {
            openDB();
            ContentValues cv = new ContentValues();
            cv.put(Constants.ITINERARY_DETAILS_TABLE_COLUMNS[4], Constants.INVOICED);

            int val = db.update(Constants.ITINERARY_DETAILS_TABLE, cv, Constants.ITINERARY_DETAILS_TABLE_COLUMNS[0] + "=? AND "
                    + Constants.ITINERARY_DETAILS_TABLE_COLUMNS[2] + "=?", new String[]{itineraryId, customerNo});
            closeDB();
            Log.w("Updated Itineraries", ">> " + val);
            if (val == 1) return true;
            else return false;
        }

        public void insertPrincipleDiscountDetails(List<PrincipleDiscountModel> list, String invoiceId, String customerId) {
            openDB();
            Log.d("TABLE", "inside insertPrincipleDiscountDetails");
            for (PrincipleDiscountModel model : list) {
                ContentValues cv = new ContentValues();
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[0], invoiceId);
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[1], customerId);
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[2], DateManager.dateToday());
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[3], model.getPrincipleID());
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[4], model.getPrinciple());
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[5], model.getAmount());
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[6], model.getDiscount());
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[7], model.getDisountValue());
                //isupload
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[8], Constants.ACTIVE);
                db.insert(Constants.PRINCIPLE_DISCOUNT_TABLE, null, cv);

                Log.d("TABLE", "inside insertPrincipleDiscountDetails_after inserting");
            }


            closeDB();
        }

        public List<PrincipleDiscountModel> getPrincipleDiscountList() {
            openDB();
            ArrayList<PrincipleDiscountModel> list = new ArrayList<>();
            String sql = "SELECT * FROM temp_discount_rate WHERE DiscountRate>0";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                PrincipleDiscountModel model = new PrincipleDiscountModel();
                model.setPrincipleID(cursor.getString(1));
                model.setPrinciple(cursor.getString(2));
                model.setDiscount(cursor.getDouble(3));
                model.setDisountValue(cursor.getDouble(4));
                model.setAmount(cursor.getDouble(5));
                list.add(model);
            }
            closeDB();

            return list;
        }


        public String getCustomerNameforBill(String customerNo) {

            String name = "";
            openDB();
            String sql = "SELECT CustomerName FROM Mst_Customermaster WHERE CustomerNo = '" + customerNo + "'";
            Cursor c1 = db.rawQuery(sql, null);

            if (c1.moveToFirst()) {
                name = c1.getString(0);
            }
            closeDB();
            return name;
        }

        public String getCustomerPhoneforBill(String customerNo) {

            String phone = "";
            openDB();
            String sql = "SELECT Telephone FROM Mst_Customermaster WHERE CustomerNo = '" + customerNo + "'";
            Cursor c2 = db.rawQuery(sql, null);

            if (c2.moveToFirst()) {
                phone = c2.getString(0);
            }
            closeDB();
            return phone;
        }

        public String getRepNameforBill(String repId) {

            String repName = "";
            openDB();
            String sql = "SELECT RepName FROM Mst_RepTable WHERE RepID = '" + repId + "'";
            Cursor c3 = db.rawQuery(sql, null);

            if (c3.moveToFirst()) {
                repName = c3.getString(0);
            }
            closeDB();
            return repName;
        }

        public ArrayList<BrandDetails> getBrandDetails() {

            ArrayList<BrandDetails> bdList = new ArrayList<>();

            openDB();

            String sql = "SELECT PrincipleID,SUM(LineVal),SUM(OrderQty),SUM(Disc),SUM(Free) FROM temp_invoice WHERE (OrderQty > 0 OR Free > 0) GROUP BY PrincipleID";
            Cursor cursor = db.rawQuery(sql, null);

            size = cursor.getCount();

            while (cursor.moveToNext()) {
                BrandDetails bd = new BrandDetails();
                bd.setBrandID(cursor.getString(0));
                bd.setBrandName(getBrandName(cursor.getString(0)));
                bd.setTotalQty(cursor.getInt(2));
                bd.setFreeQty(cursor.getInt(4));
                bd.setDiscountValue(cursor.getDouble(3));
                bd.setTotalValue(cursor.getDouble(1));

                bdList.add(bd);
            }

            closeDB();

            return bdList;
        }

        private String getBrandName(String brandId) {

            String brandName = "";
            openDB();
            String sql = "SELECT Principle FROM Mst_SupplierTable WHERE PrincipleID = '" + brandId + "'";
            Cursor c2 = db.rawQuery(sql, null);

            if (c2.moveToFirst()) {
                brandName = c2.getString(0);
            }
            closeDB();
            return brandName;
        }

        public String getDiscountValue(String principleId) {

            double discVal = 0.0;
            openDB();
            String sql = "SELECT DiscountValue FROM temp_discount_rate WHERE PrincipleID = '" + principleId + "'";
            Cursor c2 = db.rawQuery(sql, null);

            if (c2.moveToFirst()) {
                discVal = c2.getDouble(0);
            }
            brandDiscount = discVal;
            closeDB();
            return String.valueOf(discVal);
        }

        public String getDiscountRate(String principleId) {

            double discRate = 0.0;
            openDB();
            String sql = "SELECT DiscountRate FROM temp_discount_rate WHERE PrincipleID = '" + principleId + "'";
            Cursor c2 = db.rawQuery(sql, null);

            if (c2.moveToFirst()) {
                discRate = c2.getDouble(0);
            }
            closeDB();
            return String.valueOf(discRate);
        }
    }

    private class BrandDetails {
        private String brandID;
        private String brandName;
        private int totalQty;
        private int freeQty;
        private double discountValue;
        private double discountRate;
        private double totalValue;


        public String getBrandID() {
            return brandID;
        }

        public void setBrandID(String brandID) {
            this.brandID = brandID;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public int getTotalQty() {
            return totalQty;
        }

        public void setTotalQty(int totalQty) {
            this.totalQty = totalQty;
        }

        public int getFreeQty() {
            return freeQty;
        }

        public void setFreeQty(int freeQty) {
            this.freeQty = freeQty;
        }

        public double getDiscountValue() {
            return discountValue;
        }

        public void setDiscountValue(double discountValue) {
            this.discountValue = discountValue;
        }

        public double getDiscountRate() {
            return discountRate;
        }

        public void setDiscountRate(double discountRate) {
            this.discountRate = discountRate;
        }

        public double getTotalValue() {
            return totalValue;
        }

        public void setTotalValue(double totalValue) {
            this.totalValue = totalValue;
        }
    }

}
