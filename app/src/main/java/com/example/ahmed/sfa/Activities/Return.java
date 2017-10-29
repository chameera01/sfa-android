package com.example.ahmed.sfa.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.Activities.Dialogs.Alert;
import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.PermissionManager;
import com.example.ahmed.sfa.controllers.RandomNumberGenerator;
import com.example.ahmed.sfa.controllers.adapters.NavigationDrawerMenuManager;
import com.example.ahmed.sfa.controllers.adapters.ReturnRecyclerAdapter;
import com.example.ahmed.sfa.controllers.adapters.SummaryUpdateListner;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.Brand;
import com.example.ahmed.sfa.models.Itinerary;
import com.example.ahmed.sfa.models.Principle;
import com.example.ahmed.sfa.models.SalesInvoiceModel;
import com.example.ahmed.sfa.models.SalesInvoiceSummary;
import com.example.ahmed.sfa.models.SalesPayment;
import com.example.ahmed.sfa.models.SalesReturnSummary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ahmed on 9/16/2017.
 */

public class Return extends AppCompatActivity implements SummaryUpdateListner{
    private Spinner principleSpinner;
    private Spinner subBrandSpinner;

    String customerNo;
    Itinerary itinerary;

    Location lastKnownLocation;
    SalesPayment payment;

    private List<SalesInvoiceModel> invoiceModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReturnRecyclerAdapter adapter;

    TextView subTotal, returnQty,discount,total;

    SearchView searchView;

    DBAdapter dbAdapter;

    private boolean launchedForResult;
    private Alert alert;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_return);

        alert = new Alert(this);

        init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        //NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationDrawerMenuManager(this);


    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Going back will erase all changes, are you sure?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();


            //super.onBackPressed();
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
                setContentView(R.layout.activity_return);


                customerNo = getIntent().getStringExtra(Constants.CUSTOMER_NO);
                itinerary = getIntent().getParcelableExtra(Constants.ITINERARY);

                principleSpinner = (Spinner) findViewById(R.id.principleSpinner);
                subBrandSpinner = (Spinner) findViewById(R.id.subBrandSpinner);

                dbAdapter = new Return.DBAdapter(this);
                dbAdapter.createTempTable();

                invoiceModelList = dbAdapter.getAllData();
                recyclerView = (RecyclerView)findViewById(R.id.invoiceRecycler);
                adapter = new ReturnRecyclerAdapter(invoiceModelList);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.addListener(this);

                initPrincipleSpinner();

                searchView = (SearchView) findViewById(R.id.search);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filterContent((Principle) principleSpinner.getSelectedItem()
                                ,(Brand)subBrandSpinner.getSelectedItem(),newText);
                        return true;
                    }
                });


                subTotal = (TextView) findViewById(R.id.sub_tot_ri);
                returnQty = (TextView) findViewById(R.id.inv_qty_ri);
                discount = (TextView) findViewById(R.id.discount_ri);
                total = (TextView) findViewById(R.id.tot_ri);



                Button doneBtn = (Button) findViewById(R.id.next_ri);
                /*
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();

                    }
                });*/

                doneBtn.setOnClickListener(
                        new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               AlertDialog.Builder builder = new AlertDialog.Builder(Return.this);
                               builder
                                       .setTitle("Confirm ")
                                       .setMessage("Are you sure ?")
                                       .setIcon(null)
                                       .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               submit();
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
                       }
                );

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





    private void submit(){
        ArrayList<SalesInvoiceModel> list = dbAdapter.getInvoicedItems();
        payment = new SalesPayment(getSalesInvoiceSummary(list));
        boolean isError = false;
        if(launchedForResult){
            setResult();
            //finish();
        }else{
            int invoNum  = dbAdapter.getInvoiceReturnNo();//this method will return the invoice number we should use to enter the data
            if(invoNum!=-1){
                //insert data into sales return header
                if(dbAdapter.insertIntoSalesReturnHeader(payment,lastKnownLocation,itinerary.getId(),customerNo,invoNum)){
                    int lastSalesReturnHeaderID = dbAdapter.getLastSalesReturnHeaderID();
                    if (lastSalesReturnHeaderID!=-1){
                        ArrayList<SalesInvoiceModel> data = list;
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

    }

    private DialogInterface.OnClickListener successfull= new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }

    };

    public SalesInvoiceSummary getSalesInvoiceSummary(List<SalesInvoiceModel> data){
        double discount=0.0;
        double subtotal=0.0;
        double total=0.0;
        int invoicedQty=0;
        int freeQty=0;

        for(int i=0;i<data.size();i++){
            discount +=data.get(i).getDiscount();
            subtotal += data.get(i).getSubtotalVal();
            total += data.get(i).getLineValue();
            invoicedQty += data.get(i).getInvoiceQuantity();
            freeQty += data.get(i).getFree();
        }

        return new SalesInvoiceSummary(discount,subtotal,total,invoicedQty,freeQty);
    }


    private void initPrincipleSpinner(){
        principleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Principle selectedPrinciple = (Principle)principleSpinner.getSelectedItem();
                initSubBrandsSpinner(selectedPrinciple.getId());
                filterContent((Principle) principleSpinner.getSelectedItem()
                        ,(Brand)subBrandSpinner.getSelectedItem(),searchView.getQuery ()+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                initSubBrandsSpinner("");
            }
        });
        principleSpinner.setAdapter(dbAdapter.getAllPriciples());
    }

    private void initSubBrandsSpinner(String principle) {
        if (principle == null || principle.equals("ALL")) {
            principle = "";
        }

        subBrandSpinner.setAdapter(dbAdapter.getALLSubBrands(principle));
        subBrandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //searchWithoutQuery();
                filterContent((Principle) principleSpinner.getSelectedItem()
                        ,(Brand)subBrandSpinner.getSelectedItem(),searchView.getQuery ()+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void filterContent(Principle principle,Brand subbrand,String product ){
        invoiceModelList.clear();
        invoiceModelList.addAll(dbAdapter.getAllData(principle.getId(),subbrand.getBrandID(),product));
        //invoiceModelList =

        adapter.notifyDataSetChanged();
    }

    private void setResult(){
        Intent result = new Intent();
        SalesReturnSummary returnSummary = new SalesReturnSummary(getSalesInvoiceSummary(dbAdapter.getInvoicedItems()));
        result.putExtra(Constants.SALES_RETURN_SUMMARY,returnSummary);
        setResult(RESULT_OK,result);
        finish();
    }

    private void updateSummaryView(SalesInvoiceSummary salesInvoiceSummary){
        subTotal.setText(salesInvoiceSummary.getSubtotal()+"");
        returnQty.setText(salesInvoiceSummary.getInvoicedQty()+"");
        discount.setText(salesInvoiceSummary.getDiscount()+"");
        total.setText(salesInvoiceSummary.getTotal()+"");

    }

    @Override
    public void updateSummary() {
        updateSummaryView(
                getSalesInvoiceSummary(
                        dbAdapter.getInvoicedItems()
                )
        );
        Toast.makeText(getApplicationContext(),"summary",Toast.LENGTH_LONG).show();
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

        public ArrayList<SalesInvoiceModel> getAllData(){
            ArrayList<SalesInvoiceModel> data = new ArrayList<>();
            openDB();
            //used before when we were directly took data inner joining tables
            //Cursor cursor = db.rawQuery("SELECT b.ServerID,a.ItemCode,a.Description,b.BatchNumber,b.ExpiryDate,b.SellingPrice,b.Qty" +
            //" FROM Mst_ProductMaster a inner join Tr_TabStock b " +
            //"on a.ItemCode  = b.ItemCode ;",null);
            String sql = "SELECT * from temp_return";
            Cursor cursor = db.rawQuery(sql,null);

            while(cursor.moveToNext()){
                try{
                    SalesInvoiceModel salesInvoiceModel =new SalesInvoiceModel(cursor.getString(0),
                            cursor.getString(1),cursor.getString(2),cursor.getString(3),
                            cursor.getString(4),cursor.getDouble(5),cursor.getInt(6)) ;

                    salesInvoiceModel.setShelf(cursor.getInt(7));
                    salesInvoiceModel.setRequest(cursor.getInt(8));
                    salesInvoiceModel.setOrder(cursor.getInt(9));
                    salesInvoiceModel.setFree(cursor.getInt(10));
                    salesInvoiceModel.setDiscountRate(cursor.getDouble(11));
                    salesInvoiceModel.setLineValue(cursor.getDouble(12));
                    salesInvoiceModel.setPrincipleID(cursor.getString(13));
                    salesInvoiceModel.setBrandID(cursor.getString(14));
                    salesInvoiceModel.setServerID(cursor.getString(15));
                    salesInvoiceModel.setRetailPrice(cursor.getDouble(16));

                    data.add(salesInvoiceModel);
                }catch (Exception ex){

                    Toast.makeText(getApplicationContext(), "Wrong Value ", Toast.LENGTH_SHORT).show();
                }


            }
            closeDB();

            return data;
        }

        public ArrayList<SalesInvoiceModel> getAllData(String principle,String subbrand,String product){
            ArrayList<SalesInvoiceModel> data = new ArrayList<>();
            openDB();

            /*String sql = "SELECT b.ServerID, a.ItemCode,a.Description,b.BatchNumber,b.ExpiryDate,b.SellingPrice,b.Qty" +
                    " FROM Mst_ProductMaster a inner join Tr_TabStock b " +
                    "on a.ItemCode = b.ItemCode WHERE ";
            used in older version*/
            String sql = "SELECT * from temp_return WHERE";

            if (!(principle.equals("ALL")|| principle == null) ){
                sql+=" trim(PrincipleID) = '"+principle+"'";
                //principle = "";
                if(!(subbrand.equals("ALL") || subbrand == null)){
                    sql+=" AND trim(BrandID) = '"+subbrand+"'";
                    //subbrand = "";
                }
                sql+=" AND ";
            }else{
                if(!(subbrand.equals("ALL") || subbrand == null)){
                    sql+=" trim(BrandID) = '"+subbrand+"'";
                    //subbrand = "";
                    sql+=" AND ";
                }

            }
            if(product.equals("ALL")|| product==null){
                product = "";

            }
            sql+=" Description Like '"+product+"%'";

            Cursor cursor = db.rawQuery(sql,null);

            while(cursor.moveToNext()){
                SalesInvoiceModel salesInvoiceModel =new SalesInvoiceModel(cursor.getString(0),
                        cursor.getString(1),cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),cursor.getDouble(5),cursor.getInt(6)) ;
                salesInvoiceModel.setShelf(cursor.getInt(7));
                salesInvoiceModel.setRequest(cursor.getInt(8));
                salesInvoiceModel.setOrder(cursor.getInt(9));
                salesInvoiceModel.setFree(cursor.getInt(10));
                salesInvoiceModel.setDiscountRate(cursor.getDouble(11));
                salesInvoiceModel.setLineValue(cursor.getDouble(12));
                salesInvoiceModel.setPrincipleID(cursor.getString(13));
                salesInvoiceModel.setBrandID(cursor.getString(14));
                salesInvoiceModel.setServerID(cursor.getString(15));
                salesInvoiceModel.setRetailPrice(cursor.getDouble(16));

                data.add(salesInvoiceModel);
            }
            closeDB();

            return data;

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
            cv.put(Constants.SALES_RETURN_TABLE_COLUMNS[3], DateManager.dateToday());
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

        public int getInvoiceReturnNo(){
            openDB();
            Cursor cursor = db.rawQuery("SELECT InvoiceReturnNo FROM Mst_InvoiceNumbers_Management",null);
            if(cursor.moveToNext()){
                int val=cursor.getInt(0);
                closeDB();
                return val;
            }else{
                closeDB();
                Log.w("catch","here 0");
                return -1;
            }
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

        public ArrayList<SalesInvoiceModel> getInvoicedItems(){
            ArrayList<SalesInvoiceModel> data = new ArrayList<>();
            openDB();
            //used before when we were directly took data inner joining tables
            //Cursor cursor = db.rawQuery("SELECT b.ServerID,a.ItemCode,a.Description,b.BatchNumber,b.ExpiryDate,b.SellingPrice,b.Qty" +
            //" FROM Mst_ProductMaster a inner join Tr_TabStock b " +
            //"on a.ItemCode  = b.ItemCode ;",null);
            String sql = "SELECT * from temp_return WHERE returnQty !=0";
            Cursor cursor = db.rawQuery(sql,null);

            while(cursor.moveToNext()){
                try{
                    SalesInvoiceModel salesInvoiceModel =new SalesInvoiceModel(cursor.getString(0),
                            cursor.getString(1),cursor.getString(2),cursor.getString(3),
                            cursor.getString(4),cursor.getDouble(5),cursor.getInt(6)) ;
                    salesInvoiceModel.setShelf(cursor.getInt(7));
                    salesInvoiceModel.setRequest(cursor.getInt(8));
                    salesInvoiceModel.setOrder(cursor.getInt(9));
                    salesInvoiceModel.setFree(cursor.getInt(10));
                    salesInvoiceModel.setDiscountRate(cursor.getDouble(11));
                    salesInvoiceModel.setLineValue(cursor.getDouble(12));
                    salesInvoiceModel.setPrincipleID(cursor.getString(13));
                    salesInvoiceModel.setBrandID(cursor.getString(14));
                    salesInvoiceModel.setServerID(cursor.getString(15));
                    salesInvoiceModel.setRetailPrice(cursor.getDouble(16));

                    data.add(salesInvoiceModel);
                }catch (Exception ex){

                    Toast.makeText(getApplicationContext(), "Wrong Value ", Toast.LENGTH_SHORT).show();
                }


            }
            closeDB();

            return data;
        }


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
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[8],rowModel.getOrder());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[9],rowModel.getFree());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[10],rowModel.getLineValue());
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[11],Constants.INACTIVE);
            cv.put(Constants.SALES_RETURN_DETAILS_TABLE_COLUMNS[12],DateManager.dateToday());

            long result = db.insert(Constants.SALES_RETURN_DETAILS_TABLE, null, cv);

            closeDB();
            if(result>0) return true;
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
            cv.put(Constants.DAILY_ROUTES_TABLE_COLUMNS[0], RandomNumberGenerator.generateRandomCode(RandomNumberGenerator.GENERATE_ALPHABANUMERIC,10));
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
            if(! (model.getBatchNumber() == null|| model.getBatchNumber().equalsIgnoreCase("") )){
                updatedCount = db.update(Constants.TAB_STOCK, cv, Constants.TAB_STOCK_COLUMNS[3]+"=? AND " +
                        Constants.TAB_STOCK_COLUMNS[4]+"=? AND "+Constants.TAB_STOCK_COLUMNS[6]+"=?" , whereClauseArgs);
            }

            if(updatedCount<=0){
                cv.put(Constants.TAB_STOCK_COLUMNS[0],model.getServerID());
                cv.put(Constants.TAB_STOCK_COLUMNS[1],model.getId());
                cv.put(Constants.TAB_STOCK_COLUMNS[2],model.getBrandID());
                cv.put(Constants.TAB_STOCK_COLUMNS[3],model.getCode());
                cv.put(Constants.TAB_STOCK_COLUMNS[4],model.getCode());
                cv.put(Constants.TAB_STOCK_COLUMNS[5],"01-01-2025");
                cv.put(Constants.TAB_STOCK_COLUMNS[6],model.getUnitPrice());
                cv.put(Constants.TAB_STOCK_COLUMNS[7],model.getRetailPrice());
                cv.put(Constants.TAB_STOCK_COLUMNS[8],valToAssign);
                cv.put(Constants.TAB_STOCK_COLUMNS[9], DateManager.dateToday());
                long a = db.insert(Constants.TAB_STOCK,null,cv);
                if(a!=-1){
                    updatedCount = 1;
                }
            }
            closeDB();
            Log.w("UpdateCount :",""+updatedCount);
            if(updatedCount>0) return true;
            else return false;

        }

        //this will incerase one value of the invoice num in the db
        public boolean increaseInvoiceReturnNo(){
            int curVal = getInvoiceReturnNo();
            openDB();
            int newVal = curVal+1;
            db.execSQL("UPDATE Mst_InvoiceNumbers_Management SET InvoiceNo="+newVal+" Where InvoiceNo="+curVal+";");
            closeDB();
            return true;
        }
    }
}
