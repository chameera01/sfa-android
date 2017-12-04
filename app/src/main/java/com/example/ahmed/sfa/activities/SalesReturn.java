package com.example.ahmed.sfa.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Dialogs.Alert;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.PermissionManager;
import com.example.ahmed.sfa.controllers.RandomNumberGenerator;
import com.example.ahmed.sfa.controllers.adapters.NavigationDrawerMenuManager;
import com.example.ahmed.sfa.controllers.adapters.SalesReturnAddedChoicesAdapter;
import com.example.ahmed.sfa.controllers.adapters.SalesReturnProductsTableAdapter;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.Brand;
import com.example.ahmed.sfa.models.Itinerary;
import com.example.ahmed.sfa.models.Principle;
import com.example.ahmed.sfa.models.SalesInvoiceModel;
import com.example.ahmed.sfa.models.SalesInvoiceSummary;
import com.example.ahmed.sfa.models.SalesPayment;
import com.example.ahmed.sfa.models.SalesReturnSummary;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 3/19/2017.
 */

public class SalesReturn extends AppCompatActivity {


    DBAdapter dbAdapter;
    SalesReturnProductsTableAdapter productSearchTableAdapter;
    SalesReturnAddedChoicesAdapter addedContentTableAdapter;
    Spinner principleSpinner;
    Spinner subBrandSpinner;
    SearchView searchView;

    TableLayout addedListLayout;
    Button showHideInvoicedBtn;

    TextView discount;
    TextView subtotal;
    TextView invTot;
    TextView tot;

    String customerNo;
    Itinerary itinerary;

    Location lastKnownLocation;
    SalesPayment payment;

    private boolean launchedForResult;

    private Alert alert;
    private boolean showReturned;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private void showReturned(boolean com) {
        showReturned = com;
    }

    private boolean isShowingInvoiced() {
        return showReturned;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alert = new Alert(this);
        init();//we set the layout in this method to make sure user dont
        //get to proceed without neccessory data

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        //NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationDrawerMenuManager(this);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout )findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
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
                setContentView(R.layout.sales_return_activity);

                customerNo = getIntent().getStringExtra(Constants.CUSTOMER_NO);
                itinerary = getIntent().getParcelableExtra(Constants.ITINERARY);

                //Toast.makeText(getApplicationContext(),"taken +"+customerNo+" -- "+itinerary.getId(),Toast.LENGTH_SHORT).show();
                showReturned(false);


                principleSpinner = (Spinner) findViewById(R.id.principleSpinner_si);
                subBrandSpinner = (Spinner) findViewById(R.id.si_subbrand_spinner);

                dbAdapter = new DBAdapter(this);

                initPrincipleSpinner();


                addedListLayout = (TableLayout) findViewById(R.id.added_content_si);
                addedContentTableAdapter = new SalesReturnAddedChoicesAdapter(addedListLayout, this);

                showHideInvoicedBtn = (Button) findViewById(R.id.show_returned_sr);
                showHideInvoicedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showHideInvoiced();
                    }
                });


                TableLayout prdctsTable = (TableLayout) findViewById(R.id.product_details_si);
                productSearchTableAdapter = new SalesReturnProductsTableAdapter(prdctsTable, this);


                searchView = (SearchView) findViewById(R.id.search_query_si);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        productSearchTableAdapter.updateData(principleSpinner.getSelectedItem().toString()
                                , subBrandSpinner.getSelectedItem().toString(), query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });


                subtotal = (TextView) findViewById(R.id.sub_tot_si);
                invTot = (TextView) findViewById(R.id.inv_qty_si);
                discount = (TextView) findViewById(R.id.discount_si);
                tot = (TextView) findViewById(R.id.tot_si);



                Button doneBtn = (Button) findViewById(R.id.next_si);
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    submit();

                    }
                });

                launchedForResult = false;
                if(getCallingActivity()!=null){
                    launchedForResult = true;
                    Toast.makeText(getApplicationContext(),"Launced for result",Toast.LENGTH_SHORT).show();
                }

                //payment =
                //salesInvoiceModel = new SalesInvoiceModel();
            }else{
                Toast.makeText(this,"Location unavailable",Toast.LENGTH_SHORT).show();
                setContentView(R.layout.location_not_found_error_layout);
            }

        }else{
            Toast.makeText(this,"Permission Unavailable",Toast.LENGTH_SHORT).show();
            setContentView(R.layout.location_not_found_error_layout);

        }
    }


    private void submit(){
        payment = new SalesPayment(addedContentTableAdapter.getSalesInvoiceSummary());
        boolean isError = false;
        if(launchedForResult){
            setResult();
            //finish();
        }
        int invoNum  = dbAdapter.getInvoiceReturnNo();//this method will return the invoice number we should use to enter the data
        if(invoNum!=-1){
            //insert data into sales return header
            if(dbAdapter.insertIntoSalesReturnHeader(payment,lastKnownLocation,itinerary.getId(),customerNo,invoNum)){
                int lastSalesReturnHeaderID = dbAdapter.getLastSalesReturnHeaderID();
                if (lastSalesReturnHeaderID!=-1){
                    ArrayList<SalesInvoiceModel> data = addedContentTableAdapter.getDataArray();
                    int val = dbAdapter.insertDataToSalesReturnDetails(data,lastSalesReturnHeaderID);
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

    private DialogInterface.OnClickListener successfull= new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }

    };

   private void setResult(){
       Intent result = new Intent();
       SalesReturnSummary returnSummary = new SalesReturnSummary(addedContentTableAdapter.getSalesInvoiceSummary());
       result.putExtra(Constants.SALES_RETURN_SUMMARY,returnSummary);
       setResult(RESULT_OK,result);
       finish();
   }

    private void showHideInvoiced() {
        Boolean isShowing = isShowingInvoiced();
        if (isShowing) {
            addedListLayout.setVisibility(View.GONE);
            showReturned(false);
            showHideInvoicedBtn.setText("Show Returned");
        } else {
            addedListLayout.setVisibility(View.VISIBLE);
            showReturned(true);
            showHideInvoicedBtn.setText("Hide Returned");
        }

    }

    public void setSummary(){
        SalesInvoiceSummary summary = addedContentTableAdapter.getSalesInvoiceSummary();
        tot.setText(summary.getTotal()+"");
        discount.setText(summary.getDiscount()+"");
        subtotal.setText(summary.getSubtotal()+"");
        invTot.setText(summary.getInvoicedQty()+"");
    }

    public void selectItem(SalesInvoiceModel model) {
        addedContentTableAdapter.addToList(model);
        setSummary();
        //productSearchTableAdapter.refreshTable();
    }

    public boolean isAlreadySelected(SalesInvoiceModel model) {
        return addedContentTableAdapter.isAlreadyAdded(model);
    }

    private void searchWithoutQuery() {
        productSearchTableAdapter.updateData(((Principle) principleSpinner.getSelectedItem()).getId(),
                ((Brand) subBrandSpinner.getSelectedItem()).getBrandID(), "");
    }

    private void initPrincipleSpinner() {

        principleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Principle selectedPrinciple = (Principle) principleSpinner.getSelectedItem();
                initSubBrandsSpinner(selectedPrinciple.getId());
                searchWithoutQuery();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                initSubBrandsSpinner("");
            }
        });

        principleSpinner.setAdapter(dbAdapter.getAllPriciples());
    }

    private void initSubBrandsSpinner(String principle) {
        if (principle == null || principle == "" || principle.equals("ALL")) {
            principle = "";
        }

        subBrandSpinner.setAdapter(dbAdapter.getALLSubBrands(principle));
        subBrandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchWithoutQuery();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SalesInvoice Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }





    class DBAdapter extends BaseDBAdapter {

        protected DBAdapter(Context c) {
            super(c);
        }

        public ArrayAdapter<Principle> getAllPriciples() {
            openDB();
            ArrayList<Principle> principles = new ArrayList<>();
            principles.add(new Principle("ALL","ALL"));
            Cursor cursor = db.rawQuery("SELECT PrincipleID,Principle FROM Mst_SupplierTable WHERE Activate="+ Constants.ACTIVE, null);
            while (cursor.moveToNext()) {
                principles.add(new Principle(cursor.getString(0), cursor.getString(1)));
            }
            closeDB();

            ArrayAdapter<Principle> spinnerAdapter = new ArrayAdapter<Principle>(context, android.R.layout.simple_spinner_item, principles);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            return spinnerAdapter;
        }

        public ArrayAdapter<Brand> getALLSubBrands(String principles) {
            openDB();

            String sql = "SELECT  PrincipleID, BrandID, MainBrand FROM " +
                    "Mst_ProductBrandManagement WHERE Activate="+Constants.ACTIVE;

            if (!principles.equals("")) sql += " AND PrincipleID='" + principles + "';";

            ArrayList<Brand> data = new ArrayList<>();

            data.add(new Brand("", "ALL", "ALL"));

            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                data.add(new Brand(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }

            closeDB();

            ArrayAdapter<Brand> arrayAdapter = new ArrayAdapter<Brand>(context, android.R.layout.simple_spinner_item, data);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            return arrayAdapter;
        }




        public boolean insertIntoSalesReturnHeader(SalesPayment payment, Location loc, String itineraryId, String customerNO, int invNo){
            openDB();

            ContentValues cv = new ContentValues();
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[0],itineraryId);
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[1],customerNO);
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[2],invNo);
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[3],DateManager.dateToday());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[4],DateManager.getTimeFull());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[5],payment.getSubTotal());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[6],payment.getTotal());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[7],payment.getFullInvDisc());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[8],payment.getDiscount());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[9],"UNKNOWN");//for now discount type is unknown

            //if the activity launched from invoice activity this will be positive, negative otherwise
                cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[10],Constants.INACTIVE);//for now we dont include is on return or not so its one

            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[11],Constants.INACTIVE);//is print is default 1 and after printing it shld be changed to 0
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[12],payment.getInvQty());//product count is assumed as total invoiced qty
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[13],"NOTYPE");//invoice type is yet to be clarrified
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[14],loc.getLatitude());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[15],loc.getLongitude());
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[16],Constants.INACTIVE);
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[17],DateManager.dateToday());

            long result = db.insert(Constants.SALES_RETURN_TABLE,null,cv);
            closeDB();
            if(result>0)return true;
            else return false;
        }


        //loop through the sales return details and add each item one by one by calling insertRowToSalesDetails method
        public int insertDataToSalesReturnDetails(ArrayList<SalesInvoiceModel> data,int salesReturnHeaderID){
            int completed = 0;
            for (SalesInvoiceModel model:data) {
                if(insertRowToSalesReturnDetails(model,salesReturnHeaderID)) completed++;
                else break;
            }
            return completed;
        }

        //insert a single item details to the sales details
        private boolean insertRowToSalesReturnDetails(SalesInvoiceModel rowModel,int salesReturnHeaderID){

            openDB();
            ContentValues cv = new ContentValues();
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[0],salesReturnHeaderID);//get the id
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[1], rowModel.getCode());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[2],rowModel.getUnitPrice());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[3],rowModel.getBatchNumber());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[4],rowModel.getExpiryDate());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[5],rowModel.getDiscountRate());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[6],rowModel.getDiscount());
            //cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[7],null);//ignoring would enter null value
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[8],rowModel.getOrder());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[9],rowModel.getFree());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[10],rowModel.getLineValue());
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[11],Constants.INACTIVE);
            cv.put(Constants.SALES_DETAILS_TABLE_COLUMNS[12],DateManager.dateToday());

            long result = db.insert(Constants.SALES_RETURN_DETAILS_TABLE, null, cv);

            closeDB();
            if(result>0) return true;
            else return false;
        }


        //on Database connection errro sytem will return -1 else will return the current invoice No
        //on the system
        private int getInvoiceReturnNo(){
            openDB();
            Cursor cursor = db.rawQuery("SELECT InvoiceReturnNo FROM Mst_InvoiceNumbers_Management",null);
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
        private boolean increaseInvoiceReturnNo(){
            int curVal = getInvoiceReturnNo();
            openDB();
            int newVal = curVal+1;
            db.execSQL("UPDATE Mst_InvoiceNumbers_Management SET InvoiceNo="+newVal+" Where InvoiceNo="+curVal+";");
            closeDB();
            return true;
        }

        public  int getLastSalesReturnHeaderID(){
            int result = -1;

            openDB();
            Cursor cursor = db.rawQuery("SELECT _id FROM "+Constants.SALES_RETURN_TABLE,null);
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
            int valToAssign = model.getStock()+model.getInvoiceQuantity();
            openDB();
            ContentValues cv = new ContentValues();
            cv.put(Constants.TAB_STOCK_COLUMNS[8],valToAssign);
            String whereClauseArgs[] = {model.getId()};
            Log.w("id",model.getId()+"");
            int updatedCount = db.update(Constants.TAB_STOCK, cv, Constants.TAB_STOCK_COLUMNS[0]+"=?", whereClauseArgs);
            closeDB();
            Log.w("UpdateCount :",""+updatedCount);
            if(updatedCount>0) return true;
            else return false;

        }

        public int updateStock(ArrayList<SalesInvoiceModel> data){
            int val = 0;
            for (SalesInvoiceModel model :data) {
                if(updateStockRow(model)) val++;
                else return val;
            }
            return val;
        }

        public boolean insertToDailyRouteDetails(Itinerary itinerary,String customrNo,int invNo){
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

    }


}
