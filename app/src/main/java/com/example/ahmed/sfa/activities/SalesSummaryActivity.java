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
import com.example.ahmed.sfa.models.Cheque;
import com.example.ahmed.sfa.models.Itinerary;
import com.example.ahmed.sfa.models.PrincipleDiscountModel;
import com.example.ahmed.sfa.models.SalesInvoiceModel;
import com.example.ahmed.sfa.models.SalesPayment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 3/27/2017.
 */

public class SalesSummaryActivity extends AppCompatActivity {
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

    String customerNo;
    Itinerary itinerary;

    private Alert alert;

    Location lastKnownLocation;
    DBAdapter dbAdapter;

    int assignedReturned;

    String deviceId, repId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = new DBAdapter(SalesSummaryActivity.this);
        init();
        getRepAndDeviceId();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        //Toast.makeText(this," permission Received",Toast.LENGTH_SHORT).show();
        switch (requestCode){
            case PermissionManager.MY_PERMISSIONS_REQUEST_LOCATION:
                //Toast.makeText(this," Location",Toast.LENGTH_SHORT).show();
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    init();
                    //Toast.makeText(this,"Location permission Received",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    private void insertReturnData(){
        boolean isError = false;
        Return.DBAdapter dbAdapter = new Return(). new DBAdapter(this);
        ArrayList<SalesInvoiceModel> list = dbAdapter.getInvoicedItems();
        int invoNum  = dbAdapter.getInvoiceReturnNo();//this method will return the invoice number we should use to enter the data
        if(invoNum!=-1){
            //insert data into sales return header
            if(dbAdapter.insertIntoSalesReturnHeader(payment,lastKnownLocation,itinerary.getId(),customerNo,invoNum)){
                int lastSalesReturnHeaderID = dbAdapter.getLastSalesReturnHeaderID();
                if (lastSalesReturnHeaderID!=-1){
                    ArrayList<SalesInvoiceModel> data = list;
                    int val = dbAdapter.insertDataToSalesReturnDetails(data,lastSalesReturnHeaderID);
                    assignedReturned = lastSalesReturnHeaderID;
                    if(val == data.size()){
                        int updatedStockCount = dbAdapter.updateStock(data);
                        if(updatedStockCount == data.size()){
                            if(dbAdapter.insertToDailyRouteDetails(itinerary,customerNo,invoNum)){
                                if(dbAdapter.increaseInvoiceReturnNo()){
                                    alert.showAlert("Success","Invoice completed",null,successfull);
                                }else{
                                    Log.w("Error > ","increment on sales return num error");
                                    isError = true;
                                }
                            }else{
                                Log.w("Error > ","updating itinerary details error");
                                isError = true;
                            }
                        }else {
                            Log.w("Error > ","updating stock error");
                            isError = true;
                        }
                    }else{
                        Log.w("Error > ","not all sales return details entered to db");
                        isError = true;
                    }
                }else{
                    Log.w("Error > ","retrieving data from sales header error");
                    isError = true;
                }
            }else{
                //inserting to sales return error
                isError = true;
                Log.w("Error >","Inserting data into sales return header");
            }
        }else{
            //invoice number is -1 show the corresponding error
            Log.w("Error >","Getting invoice return no");
            isError = true;
        }

        if(isError)alert.showAlert("Error","Invoicing error try again",null,null);
    }

    private void insertInvoiceData(){
        boolean isError = false;
        if(pman.checkForLocationPermission()){
            LocationManager locMan = (LocationManager)getSystemService(LOCATION_SERVICE);
            Location loc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }else {
            Toast.makeText(getApplicationContext(),"Location Permission Not available",Toast.LENGTH_SHORT).show();
        }


        String serialCode = RandomNumberGenerator.generateRandomCode(RandomNumberGenerator.GENERATE_ALPHABANUMERIC,"SRL",14);
        //String invNum = RandomNumberGenerator.generateRandomCode(RandomNumberGenerator.GENERATE_ALPHABANUMERIC,"INV",14);

        //get invoice number from table -1 will be returned on error
        int invNo = dbAdapter.getInvoiceNo();
        Log.d("TABLE", "Inv NO_" + String.valueOf(invNo));
        if (invNo!=-1){
            //insert into sales header and get sales header id

            int i = invNo + 1;
            String invNum = deviceId + String.valueOf(i); //deviceID + invoiceNo added to sales header
            Log.d("TABLE", invNum);
            if (dbAdapter.insertIntoSalesHeader(payment, new Location(LocationManager.GPS_PROVIDER), itinerary.getId(), customerNo, invNum, assignedReturned, data.size())) {
                Log.d("TABLE", itinerary.getId());
                int salesHeaderID = dbAdapter.getLastSalesHeaderID();
                if (salesHeaderID!=-1){
                    int val = dbAdapter.insertDataToSalesDetails(data,salesHeaderID);
                    if(val==data.size()){
                        int r =dbAdapter.updateStock(data);
                        Log.w("Error check :",r+"");
                        Log.w("Error check :",data.size()+"");
                        if(r==data.size()){
                            if (dbAdapter.insertToInvoiceOutStanding(payment, invNum)) {
                                Log.d("TABLE", "inserted to insertToInvoiceOutStanding");
                                if (dbAdapter.insertToDailyRouteDetails(itinerary, customerNo, invNum)) {
                                    Log.d("TABLE", "inserted to insertToDailyRouteDetails");
                                    if(dbAdapter.updateItineraryDetailsTable(itinerary.getId(),customerNo)){
                                        Log.d("TABLE", "inserted to updateItineraryDetailsTable");
                                        if (dbAdapter.insertChequeDetails(chequeModel, serialCode, invNum, customerNo, payment.getTotal())) {
                                            Log.d("TABLE", "inserted to insertChequeDetails");
                                            if(dbAdapter.increaseInvoiceNo()){
                                                Log.d("TABLE", "increased invoice No.");
                                                dbAdapter.insertPrincipleDiscountDetails(dbAdapter.getPrincipleDiscountList(), invNum, customerNo);
                                                alert.showAlert("Success","Invoice completed",null,successfull);
                                            }else{
                                                Log.w("Eror : >","incr inv no");
                                                isError = true;//increasing invoice number error
                                            }
                                        }else {
                                            Log.w("Eror : >","cheque");
                                            isError = true;//inserting cheque details error
                                        }
                                    }else{
                                        Log.w("Eror : >","itinerary");
                                        isError=true;//updating itinerary table error
                                    }
                                }else{
                                    Log.w("Eror : >","daily route");
                                    isError= true;//inserting data into daily route details error
                                }
                            }else{
                                Log.w("Eror : >","sales outstanding");
                                isError = true;//inserting data into sales outstanding error;
                            }

                        }else {
                            Log.w("Eror : >","stock values");
                            isError = true;//not all product's Stock values are updated;
                        }
                    }else {
                        Log.w("Eror : >","sales details");
                        isError =true;//Not all data inserted to the sales details table
                    }
                }else {
                    Log.w("Eror : >","sales header");
                    isError = true; //retreiving id from sales header error;
                }
            }else{
                Log.w("Eror : >","sales headr");
                isError = true;//inserting into sales header error
            }
        }else{
            Log.w("Eror : >","invoice num ret");
            isError = true;//invoice number retreiving error
        }

        if (isError) alert.showAlert("Error", "Invoicing error, try again!", null, null);

        //this value has to be taken from the home ui but hard coded here for convenience
        /**if(dbAdapter.insertChequeDetails(chequeModel,serialCode,invNum,customerNo,payment.getTotal()))
         Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
         else Toast.makeText(getApplicationContext(),"Un Successfull",Toast.LENGTH_SHORT).show();*/

    }

    private DialogInterface.OnClickListener successfull= new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(SalesSummaryActivity.this,Home.class);
            startActivity(intent);
        }

    };

    private void init(){

        PermissionManager pm = new PermissionManager(this);
        if(pm.checkForLocationPermission()) {
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
            lastKnownLocation = new Location(LocationManager.GPS_PROVIDER);//this line has to be removed
            /* -----------------------------------------------------------------------------------------------*/
            //this has been added since emmulator failed to send gps points

            if (lastKnownLocation != null) {
                setContentView(R.layout.sales_invoice_summary_activity);
                alert = new Alert(this);

                itinerary = getIntent().getParcelableExtra(Constants.ITINERARY);
                Log.d("Error", itinerary.getId());
                customerNo = getIntent().getStringExtra(Constants.CUSTOMER_NO);
                Log.d("Error", customerNo);

                pman = new PermissionManager(this);

                data = getIntent().getParcelableArrayListExtra(Constants.DATA_ARRAY_NAME);
                payment = getIntent().getParcelableExtra(Constants.SALES_PAYMENT_SUMMARY);
                chequeModel = getIntent().getParcelableExtra(Constants.CHEQUE);

                table = (TableLayout)findViewById(R.id.si_pay_sum_data);


                subtotal = (TextView) findViewById(R.id.si_pay_sum_sub_tot);
                discount =(TextView)findViewById(R.id.si_pay_sum_disc);
                returntot = (TextView)findViewById(R.id.si_pay_sum_return_tot);
                invoiceQty = (TextView)findViewById(R.id.si_pay_sum_inv_qty);
                freeQty = (TextView)findViewById(R.id.si_pay_sum_free_qty);
                returnQty = (TextView)findViewById(R.id.si_pay_sum__return_qty);

                cash = (TextView)findViewById(R.id.si_pay_sum_cash);
                cheque = (TextView)findViewById(R.id.si_pay_sum_chq);
                credit = (TextView)findViewById(R.id.si_pay_sum_crdt);

                Button printBtn = (Button)findViewById(R.id.print_button_ss);
                printBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                Button saveBtn = (Button)findViewById(R.id.save_button_ss);
                saveBtn.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SalesSummaryActivity.this);
                                builder
                                        .setTitle("Confirm ")
                                        .setMessage("Are you sure ?")
                                        .setIcon(null)
                                        .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                insertInvoiceData();
                                                if(payment.getReturnQty()>0){
                                                    insertReturnData();

                                                }
                                            }
                                        })
                                        .setNegativeButton("No",new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();


                            }
                        });
                //table.removeAllViews();
                for(int i=0;i<data.size();i++){
                    table.addView(getView(i));

                }

                subtotal.setText(payment.getSubTotal()+"");
                discount.setText(payment.getTotalDiscount()+"");
                returntot.setText(payment.getReturnTot()+"");
                returnQty.setText(payment.getReturnQty()+"");
                invoiceQty.setText(payment.getInvQty()+"");
                freeQty.setText(payment.getFreeQty()+"");
                returnQty.setText(payment.getReturnQty()+"");


                cash.setText(payment.getCash()+"");
                credit.setText(payment.getCredit()+"");
                cheque.setText(payment.getCheque()+"");
            }else{
                Toast.makeText(this,"Location unavailable",Toast.LENGTH_SHORT).show();
                setContentView(R.layout.location_not_found_error_layout);
            }

        }else{
            Toast.makeText(this,"Permission Unavailable",Toast.LENGTH_SHORT).show();
            setContentView(R.layout.location_not_found_error_layout);

        }


    }

    public TableRow getView(int i){
        SalesInvoiceModel salesrow = data.get(i);
        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.sales_summary_row,table,false);
        if(i%2==0){
            row.setBackgroundColor(Color.LTGRAY);
        }else{
            row.setBackgroundColor(Color.GRAY);
        }
        TextView code = (TextView)row.findViewById(R.id.code_e);
        TextView product = (TextView)row.findViewById(R.id.product_e);
        TextView batch = (TextView)row.findViewById(R.id.batch_e);
        TextView expiry = (TextView)row.findViewById(R.id.expiry_e);
        TextView unitprice = (TextView)row.findViewById(R.id.unit_price);
        TextView stock = (TextView)row.findViewById(R.id.stock_e);
        TextView shelf = (TextView) row.findViewById(R.id.shelf_e);
        TextView request = (TextView)row.findViewById(R.id.request_e);
        TextView order = (TextView)row.findViewById(R.id.order_e);
        TextView free = (TextView)row.findViewById(R.id.free_e);
        TextView disc = (TextView)row.findViewById(R.id.dsc_e);
        TextView linVal = (TextView)row.findViewById(R.id.line_val_e);


        code.setText(salesrow.getCode());
        product.setText(salesrow.getProduct());
        batch.setText(salesrow.getBatchNumber());
        expiry.setText(salesrow.getExpiryDate());
        unitprice.setText(salesrow.getUnitPrice()+"");
        stock.setText(salesrow.getStock()+"");
        shelf.setText(salesrow.getShelf()+"");
        request.setText(salesrow.getRequest()+"");
        order.setText(salesrow.getOrder()+"");
        free.setText(salesrow.getFree()+"");
        disc.setText(salesrow.getDiscountRate()+"");
        linVal.setText(salesrow.getLineValue()+"");

        Toast.makeText(getApplicationContext(),"val "+salesrow.getOrder(),Toast.LENGTH_SHORT).show();
        return row;
    }

    public String getPaymentType(SalesPayment payment){
        String toReturn = "";
        toReturn += payment.getCredit()>0?"CR":"";
        toReturn += payment.getCheque()>0?"CH":"";
        toReturn += payment.getCash()  >0?"CA":"";

        toReturn += payment.getCash() > 0 && payment.getCredit() > 0 ? "CACR" : "";
        toReturn += payment.getCash() > 0 && payment.getCheque() > 0 ? "CACH" : "";
        toReturn += payment.getCheque() > 0 && payment.getCredit() > 0 ? "CRCH" : "";

        return toReturn;

    }

    class DBAdapter extends BaseDBAdapter{

        public DBAdapter(Context c){
            super(c);
        }

        public boolean insertChequeDetails(Cheque chq, String serialCode, String invoiceNumber, String customerNo, double invTotal) {
            openDB();
            ContentValues cv =new ContentValues();

            cv.put(Constants.CHEQUE_TABLE_COLUMNS[0],serialCode);
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[1],DateManager.dateToday());
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[2],invoiceNumber);
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[3],customerNo);
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[4],invTotal);
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[5],chq.getChequeVal());
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[6],chq.getChequeNum());
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[7],chq.getBank());
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[8],chq.getCollectionDate());
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[9],chq.getRealizedDate());
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[10],1);//not uploaded
            cv.put(Constants.CHEQUE_TABLE_COLUMNS[11],0);//isupdate yes
            //status unknows so ignoring that field
            //status update date is also unknown
            long val = db.insert(Constants.CHEQUE_DETAILS_TABLE_NAME,null,cv);
            closeDB();

            if(val>0)return true;
            else return false;
        }

        public boolean insertIntoSalesHeader(SalesPayment payment, Location loc, String itineraryId, String customerNO, String invNo, int returnNo, int totalProductCount) {
            openDB();

            ContentValues cv = new ContentValues();
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[0],itineraryId);
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[1],customerNO);
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[2],invNo);
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[3],DateManager.dateWithTimeToday());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[4],getPaymentType(payment));//done ///TODO: change for payment type
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[5],payment.getSubTotal());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[6],payment.getTotal());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[7],payment.getFullInvDisc());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[8],payment.getDiscount());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[9],"UNKNOWN");//for now discount type is unknown
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[10], payment.getReturnQty() > 0 ? Constants.INACTIVE : Constants.ACTIVE);//for now we dont include is on return or not so its one
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[11],returnNo==-1?null:returnNo);//leaving un touched will insert null value;
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[12],payment.getReturnTot());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[13],payment.getCredit());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[14],payment.getCash());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[15],payment.getCheque());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[16], Constants.ACTIVE);//is print is default 0 and after printing it shld be changed to 1
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[17],totalProductCount);//product count changed to assign the value from paramter
            // on 02/12/2017// product count is assumed as total invoiced qty
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[18],"NOTYPE");//invoice type is yet to be clarrified
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[19],loc.getLatitude());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[20],loc.getLongitude());
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[21], Constants.ACTIVE); //isUpload default is 0
            cv.put(Constants.SALES_HEADER_TABLE_COLUMNS[22],DateManager.dateWithTimeToday());

            long result = db.insert(Constants.SALES_HEADER_TABLE,null,cv);
            closeDB();
            if(result>0)return true;
            else return false;
        }


        //loop through the invoice details and add each item one by one by calling insertRowToSalesDetails method
        public int insertDataToSalesDetails(ArrayList<SalesInvoiceModel> data,int id){
            int completed = 0;
            for (SalesInvoiceModel model:data) {
                if(insertRowToSalesDetails(model,id)) completed++;
                else break;
            }
            return completed;
        }

        //insert a single item details to the sales details
        private boolean insertRowToSalesDetails(SalesInvoiceModel rowModel,int id){

            openDB();
            ContentValues cv = new ContentValues();
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[0],id);//get the id
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[1], rowModel.getCode());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[2],rowModel.getUnitPrice());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[3],rowModel.getBatchNumber());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[4],rowModel.getExpiryDate());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[5],rowModel.getDiscountRate());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[6],rowModel.getDiscount());
            //cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[7],null);//ignoring would enter null value
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[8],rowModel.getShelf());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[9],rowModel.getRequest());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[10],rowModel.getOrder());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[11],rowModel.getFree());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[12],rowModel.getLineValue());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[13], Constants.ACTIVE);
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[14],DateManager.dateWithTimeToday());

            long result = db.insert(Constants.SALES_DETAILS_TABLE, null, cv);

            closeDB();
            if(result>0) return true;
            else return false;
        }


        //on Database connection errro sytem will return -1 else will return the current invoice No
        //on the system
        private int getInvoiceNo(){
            openDB();
            Cursor cursor = db.rawQuery("SELECT InvoiceNo FROM Mst_InvoiceNumbers_Management",null);
            if(cursor.moveToNext()){
                int val=cursor.getInt(0);
                closeDB();
                return val;
            }else{
                Log.w("catch","here 0");
                return -1;
            }
        }


        //this will incerase one value of the invoice num in the db
        private boolean increaseInvoiceNo(){
            int curVal = getInvoiceNo();
            openDB();
            int newVal = curVal+1;
//            String invNo = deviceId+String.valueOf(newVal); //deviceID + invNo from Mst_InvNos Table increased by one
            db.execSQL("UPDATE Mst_InvoiceNumbers_Management SET InvoiceNo="+newVal+" Where InvoiceNo="+curVal+";");
//            db.execSQL("INSERT INTO Mst_InvoiceNumbers_Management VALUES()");
            closeDB();
            return true;
        }

        public  int getLastSalesHeaderID(){
            int result = -1;

            openDB();
            Cursor cursor = db.rawQuery("SELECT _id FROM "+Constants.SALES_HEADER_TABLE,null);
            if(cursor.moveToLast()){
                result = cursor.getInt(0);
            }
            closeDB();

            return result;

        }

        //this method is to reduce the invoiced quanity from the Tr_TabStock
        //we have assumed tht item code is unique here
        private boolean updateStockRow(SalesInvoiceModel model){
            boolean result = false;
            int valToAssign = model.getStock()-model.getInvoiceQuantity();
            openDB();
            ContentValues cv = new ContentValues();
            cv.put(Constants.TAB_STOCK_COLUMNS[8],valToAssign);
            String whereClauseArgs[] = {model.getCode(), model.getBatchNumber(),model.getUnitPrice()+""};
            Log.w("id",model.getId()+"");
            int updatedCount = db.update(Constants.TAB_STOCK, cv, Constants.TAB_STOCK_COLUMNS[3]+"=? AND "+
                    Constants.TAB_STOCK_COLUMNS[4]+"=? AND "+
                    Constants.TAB_STOCK_COLUMNS[6]+"=?", whereClauseArgs);
            closeDB();
            Log.w("UpdateCount :",""+updatedCount);
            if(updatedCount>0) return true;
            else return false;

        }

        //reduce the stock from for each item in the db
        public int updateStock(ArrayList<SalesInvoiceModel> data){
            int val = 0;
            for (SalesInvoiceModel model :data) {
                if(updateStockRow(model)) val++;
                else return val;
            }
            return val;
        }

        //this table details is for the transacetion payment details
        public boolean insertToInvoiceOutStanding(SalesPayment payment, String invNo) {

            openDB();
            ContentValues cv = new ContentValues();
            String serialCode = RandomNumberGenerator.generateRandomCode(RandomNumberGenerator.GENERATE_ALPHABANUMERIC,"SER",13);
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[0],serialCode);//auto genarated serialCode passed from caller
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[1],DateManager.dateToday());//Todays date created from DateManager
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[2],invNo);//invoice num passed from cller functions
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[3],customerNo);//customerNo of the selcted custoemr
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[4],payment.getTotal());//from sales payment
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[5],payment.getCredit());//from the sales payment
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[6],payment.getCredit());//from the asles payment
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[7],payment.getCreditDays());//from the sales payment
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[8],DateManager.dateToday());//Date created wiht date manager
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[9],0);//for this release this has been fixed as a 0
            //cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[10],);//last update type is unknown for now have to clarifiy
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[11],DateManager.dateToday());//date today
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[12],Constants.INACTIVE);//making the column as inactive or negative
            cv.put(Constants.INVOICE_OUTSTANDING_COLUMNS[13],Constants.ACTIVE);//marking the column as active or positive

            long val = db.insert(Constants.INVOICE_OUTSTANDING,null,cv);
            closeDB();

            if (val>0)return true;
            else return false;
        }

        //this is kind of a log table where we enter details about the
        //itinerary and its type
        public boolean insertToDailyRouteDetails(Itinerary itinerary, String customrNo, String invNo) {
            openDB();
            ContentValues cv = new ContentValues();
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[0],RandomNumberGenerator.generateRandomCode(RandomNumberGenerator.GENERATE_ALPHABANUMERIC,10));
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[1],DateManager.dateToday());
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[2],itinerary.getId());
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[3],customrNo);
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[4],itinerary.isPlanned()?Constants.ACTIVE:Constants.INACTIVE);
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[5],Constants.ACTIVE);
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[6],invNo);
            //cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[7],null);//reasons will be null since we invoice this user
            //cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[8],null);//comment will also be null since we are invoicing
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[9],Constants.INACTIVE);
            //cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[10],"");//

            long val = db.insert(Constants.DAILY_ROUTES_TABLE,null,cv);
            closeDB();

            if(val>0) return true;
            else return false;
        }

        //should update the itinereary details table about this customer
        // about the transaction type
        public boolean updateItineraryDetailsTable(String itineraryId,String CustomerNo){
            openDB();
            ContentValues cv = new ContentValues();
            cv.put(Constants.ITINERARY_DETAILS_TABLE_COLUMNS[4],Constants.INVOICED);

            int val = db.update(Constants.ITINERARY_DETAILS_TABLE, cv, Constants.ITINERARY_DETAILS_TABLE_COLUMNS[0] + "=? AND "
                    + Constants.ITINERARY_DETAILS_TABLE_COLUMNS[2] + "=?", new String[]{itineraryId, customerNo});
            closeDB();
            Log.w("Updated Itineraries",">> "+val);
            if(val==1) return true;
            else return false;
        }

        public void insertPrincipleDiscountDetails(List<PrincipleDiscountModel> list, String invoiceId, String customerId) {
            openDB();
            Log.d("TABLE", "inside insertPrincipleDiscountDetails");
            for(PrincipleDiscountModel model:list){
                ContentValues cv = new ContentValues();
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[0],invoiceId);
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[1],customerId);
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[2],DateManager.dateToday());
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[3],model.getPrincipleID());
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[4],model.getPrinciple());
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[5],model.getAmount());
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[6],model.getDiscount());
                cv.put(Constants.PRINCIPLE_DISCOUNT_TABLE_COLUMNS[7],model.getDisountValue());
                db.insert(Constants.PRINCIPLE_DISCOUNT_TABLE,null,cv);

                Log.d("TABLE", "inside insertPrincipleDiscountDetails_after inserting");
            }


            closeDB();
        }

        public List<PrincipleDiscountModel> getPrincipleDiscountList(){
            openDB();
            ArrayList<PrincipleDiscountModel> list = new ArrayList<>();
            String sql = "SELECT * FROM temp_discount_rate WHERE DiscountRate>0";
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
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

    }
}
