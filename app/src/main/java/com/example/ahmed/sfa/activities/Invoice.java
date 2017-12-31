package com.example.ahmed.sfa.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Dialogs.Alert;
import com.example.ahmed.sfa.controllers.PermissionManager;
import com.example.ahmed.sfa.controllers.adapters.InvoiceRecyclerAdapter;
import com.example.ahmed.sfa.controllers.adapters.InvoiceSummaryChangeReceiver;
import com.example.ahmed.sfa.controllers.adapters.NavigationDrawerMenuManager;
import com.example.ahmed.sfa.controllers.adapters.SummaryUpdateListner;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.Brand;
import com.example.ahmed.sfa.models.Itinerary;
import com.example.ahmed.sfa.models.Principle;
import com.example.ahmed.sfa.models.SalesInvoiceModel;
import com.example.ahmed.sfa.models.SalesInvoiceSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 9/8/2017.
 **/

public class Invoice extends AppCompatActivity implements SummaryUpdateListner {
    private Spinner principleSpinner;
    private Spinner subBrandSpinner;
    private String selectedPrincipleString;


    private SearchView searchView;

    Location lastKnownLocation;
    String customerNo;
    Itinerary itinerary;

    //for table
    private List<SalesInvoiceModel> invoiceModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private InvoiceRecyclerAdapter adapter;

    //To handle database operations
    DBAdapter dbAdapter;

    private static final String TAG = "INVOICE";

    //data fields for summary
    TextView subTotal, invoicedQty, discount, total, shelf, order, free;
    private InvoiceSummaryChangeReceiver receiver;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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

            // super.onBackPressed();
        }
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_invoice);
        init();
        //assign views for summary


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
            lastKnownLocation = new Location(LocationManager.GPS_PROVIDER);//this line has to be removed
            /* -----------------------------------------------------------------------------------------------*/
            //this has been added since emmulator failed to send gps points

            if (lastKnownLocation != null) {
                setContentView(R.layout.activity_invoice);

                receiver = new InvoiceSummaryChangeReceiver();

                customerNo = getIntent().getStringExtra(Constants.CUSTOMER_NO);
                itinerary = getIntent().getParcelableExtra(Constants.ITINERARY);

                shelf = (TextView) findViewById(R.id.shelf_si);
                order = (TextView) findViewById(R.id.order_si);
                free = (TextView) findViewById(R.id.free_si);
                subTotal = (TextView) findViewById(R.id.sub_tot_si);
                invoicedQty = (TextView) findViewById(R.id.inv_qty_si);
                discount = (TextView) findViewById(R.id.discount_si);
                total = (TextView) findViewById(R.id.tot_si);

                //assign views
                principleSpinner = (Spinner) findViewById(R.id.principleSpinner);
                subBrandSpinner = (Spinner) findViewById(R.id.subBrandSpinner);
                searchView = (SearchView) findViewById(R.id.search);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
//                        Log.d("TABLE",principleSpinner.getSelectedItem().toString()+"__"+subBrandSpinner.getSelectedItem().toString());
                        filterContent((Principle) principleSpinner.getSelectedItem(), (Brand) subBrandSpinner.getSelectedItem(), newText);
                        return true;
                    }
                });

                //set navigation drawer
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationDrawerMenuManager(this);

                dbAdapter = new DBAdapter(this);
                dbAdapter.createTempTable();
                invoiceModelList = dbAdapter.runThread();

                initPrincipleSpinner();

                recyclerView = (RecyclerView) findViewById(R.id.invoiceRecycler);
                adapter = new InvoiceRecyclerAdapter(invoiceModelList, selectedPrincipleString, this);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.addListener(this);


                Button nextBtn = (Button) findViewById(R.id.next_si);
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToPayment();
                    }
                });
            } else {
                Toast.makeText(this, "Location unavailable", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.location_not_found_error_layout);
            }
        } else {
            Toast.makeText(this, "Permission Unavailable", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.location_not_found_error_layout);

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

    private void moveToPayment() {
        dbAdapter.createTempDiscountTable();
        Intent intent = new Intent(this,
                SalesInvoicePayment.class);
        ArrayList<SalesInvoiceModel> data = dbAdapter.getInvoicedItems();
        if (data.size() > 0) {
            intent.putParcelableArrayListExtra(Constants.DATA_ARRAY_NAME, data);
            intent.putExtra(Constants.SUMMARY_OBJECT_NAME, SalesInvoiceSummary.createSalesInvoiceSummary(data));
            intent.putExtra(Constants.CUSTOMER_NO, customerNo);
            intent.putExtra(Constants.ITINERARY, itinerary);
            startActivity(intent);
        } else {
            Alert alert = new Alert(this);
            alert.showAlert("Empty", "Select at least one item to invoice ", "OK", null);
        }
    }


    private void initPrincipleSpinner() {
        principleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPrincipleString = principleSpinner.getSelectedItem().toString();
                Log.d(TAG, "principle_" + selectedPrincipleString);
                Principle selectedPrinciple = (Principle) principleSpinner.getSelectedItem();
                initSubBrandsSpinner(selectedPrinciple.getId());
                // old model : searchWithoutQuery();
                Log.d(TAG, "inside initSpinner");
                filterContent((Principle) principleSpinner.getSelectedItem(), (Brand) subBrandSpinner.getSelectedItem(), searchView.getQuery() + "");
                Log.d(TAG, "after filter content");
//                adapter.notifyDataSetChanged(); //inserted just to check
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
                filterContent((Principle) principleSpinner.getSelectedItem(), (Brand) subBrandSpinner.getSelectedItem(), searchView.getQuery() + "");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void filterContent(Principle principle, Brand subbrand, String product) {
        invoiceModelList.clear();
//        Log.d("Error",subbrand.getBrand()+"__"+principle.getPrinciple()+"__"+product);
        invoiceModelList.addAll(dbAdapter.getAllData(principle.getId(), subbrand.getBrandID(), product));

        //adapter.notifyDataSetChanged();
        adapter.customNotifyDataSetChanged();
    }

    @Override
    public void updateSummary() {

        Log.d(TAG, "inside update summary");
        Log.d(TAG, "principle_" + selectedPrincipleString);
        try {
            if (selectedPrincipleString.equals("ALL")) {
                updateSummaryView(SalesInvoiceSummary.createSalesInvoiceSummary(dbAdapter.getInvoicedItems()));
            } else {
                updateSummaryView(SalesInvoiceSummary.createSalesInvoiceSummary(dbAdapter.getPrincipleQty(selectedPrincipleString)));
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception in summary_" + e.getLocalizedMessage());
        }
        for (int i = 0; i < dbAdapter.getInvoicedItems().size(); i++) {
            Log.d(TAG, "shelf_" + String.valueOf(dbAdapter.getInvoicedItems().get(i).getShelf()));
            Log.d(TAG, "order_" + String.valueOf(dbAdapter.getInvoicedItems().get(i).getOrder()));
        }
        Toast.makeText(getApplicationContext(), "summary", Toast.LENGTH_LONG).show();

    }

    private void updateSummaryView(SalesInvoiceSummary salesInvoiceSummary) {
        Log.d(TAG, "inside updateSummaryView");

        subTotal.setText(salesInvoiceSummary.getSubtotal() + "");
        invoicedQty.setText(salesInvoiceSummary.getInvoicedQty() + "");
        discount.setText(salesInvoiceSummary.getDiscount() + "");
        total.setText(salesInvoiceSummary.getTotal() + "");

        shelf.setText(salesInvoiceSummary.getShelfQty() + "");
        Log.d(TAG, "shelf_" + salesInvoiceSummary.getShelfQty() + "");
        order.setText(salesInvoiceSummary.getOrderQty() + "");
        Log.d(TAG, "order_" + salesInvoiceSummary.getOrderQty() + "");
        free.setText(salesInvoiceSummary.getFreeQty() + "");
        Log.d(TAG, "free_" + salesInvoiceSummary.getFreeQty() + "");

    }


//    @Override
//    public void updatePrincipleSummary(String principle) {
//        updatePrincipleQty(PrincipleQtySummary.createPrincipleQtySummary(dbAdapter.getPrincipleQty(principle)));
//    }
//
//    private void updatePrincipleQty(PrincipleQtySummary principleQtySummary) {
//
//        shelf.setText(principleQtySummary.getShelf() + "");
//        order.setText(principleQtySummary.getOrder() + "");
//        free.setText(principleQtySummary.getFree() + "");
//    }


//    @Override
//    protected void onPause() {
//        super.onPause();
//        ((InvoiceRecyclerAdapter)recyclerView.getAdapter()).customOnPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        ((InvoiceRecyclerAdapter)recyclerView.getAdapter()).customOnResume();
//    }

    class DBAdapter extends BaseDBAdapter {

        public DBAdapter(Context c) {
            super(c);
        }

        public void createTempTable() {
            openDB();
            String sql = "DROP TABLE IF EXISTS temp_invoice";
            db.execSQL(sql);//drops the temprary invoice table if it
            //exists already
            sql = "CREATE TABLE temp_invoice(_id INTEGER PRIMARY KEY AUTOINCREMENT,ItemCode TEXT,Description TEXT,BatchNumber" +
                    " TEXT,ExpiryDate TEXT,SellingPrice REAL,Qty INTEGER DEFAULT 0" +
                    ",Shelf INTEGER DEFAULT 0,Request INTEGER DEFAULT 0,OrderQty INTEGER DEFAULT 0" +
                    ",Free INTEGER DEFAULT 0,Disc Real DEFAULT 0.0,LineVal Real DEFAULT 0.0,PrincipleID TEXT,BrandID TEXT,ServerID TEXT,RetailPrice REAL DEFAULT 0.0,RetailPriceLineVal REAL DEFAULT 0.0);";
            db.execSQL(sql); //create the table again;

            //fill in data
            sql = "INSERT INTO temp_invoice(ItemCode,Description,BatchNumber" +
                    ",ExpiryDate,SellingPrice,Qty,PrincipleID,BrandID,ServerID,RetailPrice) SELECT a.ItemCode,a.Description," +
                    "b.BatchNumber,b.ExpiryDate,b.SellingPrice,b.Qty,a.PrincipleID,a.BrandID,b.ServerID,b.RetailPrice" +
                    " FROM Mst_ProductMaster a inner join Tr_TabStock b " +
                    "on a.ItemCode  = b.ItemCode";
            db.execSQL(sql);

            closeDB();
        }

        public void createTempDiscountTable() {
            openDB();
            String sql = "DROP TABLE IF EXISTS temp_discount_rate";
            db.execSQL(sql);

            sql = "CREATE TABLE temp_discount_rate(_id INTEGER PRIMARY KEY AUTOINCREMENT,PrincipleID TEXT,Principle TEXT" +
                    ",DiscountRate REAL DEFAULT 0.0,DiscountValue REAL DEFAULT 0.0,LineVal REAL DEFAULT 0.0)";
            db.execSQL(sql);
            sql = "INSERT INTO temp_discount_rate(PrincipleID,Principle) SELECT PrincipleID,Principle " +
                    "FROM Mst_SupplierTable ";
            db.execSQL(sql);
        }


        private ArrayList<SalesInvoiceModel> runThread() {

            final ArrayList<SalesInvoiceModel> data = new ArrayList<>();
            new Thread() {
                public void run() {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
//                                    public ArrayList<SalesInvoiceModel> getAllData() {

//                                        ArrayList<SalesInvoiceModel> data = new ArrayList<>();
                                openDB();

                                String sql = "SELECT * from temp_invoice";
                                Cursor cursor = db.rawQuery(sql, null);

                                while (cursor.moveToNext()) {
                                    try {
                                        SalesInvoiceModel salesInvoiceModel = new SalesInvoiceModel(cursor.getString(0),
                                                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                                                cursor.getString(4), cursor.getDouble(5), cursor.getInt(6));
                                        salesInvoiceModel.setShelf(cursor.getInt(7));
                                        salesInvoiceModel.setRequest(cursor.getInt(8));
                                        salesInvoiceModel.setOrder(cursor.getInt(9));
                                        salesInvoiceModel.setFree(cursor.getInt(10));
                                        salesInvoiceModel.setDiscountRate(cursor.getDouble(11));
                                        salesInvoiceModel.setLineValue(cursor.getDouble(12));
                                        salesInvoiceModel.setPrincipleID(cursor.getString(13));
                                        salesInvoiceModel.setServerID(cursor.getString(15));
                                        salesInvoiceModel.setRetailPrice(cursor.getDouble(16));
                                        salesInvoiceModel.setRetailLineVal(cursor.getDouble(17));
                                        data.add(salesInvoiceModel);
                                    } catch (Exception ex) {

                                        Toast.makeText(getApplicationContext(), "Wrong Value ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                cursor.close();
                                closeDB();


//                                    }
                            }
                        });
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            return data;
        }

//        public ArrayList<SalesInvoiceModel> getAllData() {
//
//            ArrayList<SalesInvoiceModel> data = new ArrayList<>();
//            openDB();
//
//            String sql = "SELECT * from temp_invoice";
//            Cursor cursor = db.rawQuery(sql, null);
//
//            while (cursor.moveToNext()) {
//                try {
//                    SalesInvoiceModel salesInvoiceModel = new SalesInvoiceModel(cursor.getString(0),
//                            cursor.getString(1), cursor.getString(2), cursor.getString(3),
//                            cursor.getString(4), cursor.getDouble(5), cursor.getInt(6));
//                    salesInvoiceModel.setShelf(cursor.getInt(7));
//                    salesInvoiceModel.setRequest(cursor.getInt(8));
//                    salesInvoiceModel.setOrder(cursor.getInt(9));
//                    salesInvoiceModel.setFree(cursor.getInt(10));
//                    salesInvoiceModel.setDiscountRate(cursor.getDouble(11));
//                    salesInvoiceModel.setLineValue(cursor.getDouble(12));
//                    salesInvoiceModel.setPrincipleID(cursor.getString(13));
//                    salesInvoiceModel.setServerID(cursor.getString(15));
//                    salesInvoiceModel.setRetailPrice(cursor.getDouble(16));
//                    salesInvoiceModel.setRetailLineVal(cursor.getDouble(17));
//                    data.add(salesInvoiceModel);
//                } catch (Exception ex) {
//
//                    Toast.makeText(getApplicationContext(), "Wrong Value ", Toast.LENGTH_SHORT).show();
//                }
//            }
//            cursor.close();
//            closeDB();
//
//            return data;
//        }

        //
        public ArrayList<SalesInvoiceModel> getAllData(String principle, String subbrand, String product) {
            ArrayList<SalesInvoiceModel> data = new ArrayList<>();
            openDB();

            String sql = "SELECT * from temp_invoice WHERE";

            if (!(principle.equals("ALL") || principle == null)) {
                sql += " trim(PrincipleID) = '" + principle + "'";
                //principle = "";
                if (!(subbrand.equals("ALL") || subbrand == null)) {
                    sql += " AND trim(BrandID) = '" + subbrand + "'";
                    //subbrand = "";
                }
                sql += " AND ";
            } else {
                if (!(subbrand.equals("ALL") || subbrand == null)) {
                    sql += " trim(BrandID) = '" + subbrand + "'";
                    //subbrand = "";
                    sql += " AND ";
                }

            }

            if (product.equals("ALL") || product == null) {
                product = "";

            }
            sql += " Description Like '" + product + "%'";

            Cursor cursor = db.rawQuery(sql, null);
            Log.i(" INVOICE ", sql);
            while (cursor.moveToNext()) {
                SalesInvoiceModel salesInvoiceModel = new SalesInvoiceModel(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getDouble(5), cursor.getInt(6));
                salesInvoiceModel.setShelf(Integer.parseInt(cursor.getString(7)));
                salesInvoiceModel.setRequest(Integer.parseInt(cursor.getString(8)));
                salesInvoiceModel.setOrder(Integer.parseInt(cursor.getString(9)));
                salesInvoiceModel.setFree(Integer.parseInt(cursor.getString(10)));

                salesInvoiceModel.setDiscountRate(cursor.getDouble(11));
                salesInvoiceModel.setLineValue(cursor.getDouble(12));
                salesInvoiceModel.setPrincipleID(cursor.getString(13));
                salesInvoiceModel.setServerID(cursor.getString(15));
                salesInvoiceModel.setRetailPrice(cursor.getDouble(16));
                salesInvoiceModel.setRetailLineVal(cursor.getDouble(17));
                data.add(salesInvoiceModel);
            }
            cursor.close();
            closeDB();

            return data;

        }

        public ArrayAdapter<Principle> getAllPriciples() {
            openDB();
            ArrayList<Principle> principles = new ArrayList<>();
            principles.add(new Principle("ALL", "ALL"));
            Cursor cursor = db.rawQuery("SELECT PrincipleID,Principle FROM Mst_SupplierTable WHERE Activate=" + Constants.ACTIVE, null);
            while (cursor.moveToNext()) {
                principles.add(new Principle(cursor.getString(0), cursor.getString(1)));
            }
            cursor.close();
            closeDB();

            ArrayAdapter<Principle> spinnerAdapter = new ArrayAdapter<Principle>(context, android.R.layout.simple_spinner_item, principles);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            return spinnerAdapter;
        }

        public ArrayAdapter<Brand> getALLSubBrands(String principles) {
            openDB();

            String sql = "SELECT  PrincipleID, BrandID, MainBrand FROM " +
                    "Mst_ProductBrandManagement WHERE Activate=" + Constants.ACTIVE;

            if (!principles.equals("")) sql += " AND PrincipleID='" + principles + "';";

            ArrayList<Brand> data = new ArrayList<>();

            data.add(new Brand("", "ALL", "ALL"));

            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                data.add(new Brand(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
            cursor.close();
            closeDB();

            ArrayAdapter<Brand> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, data);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            return arrayAdapter;
        }

        public ArrayList<SalesInvoiceModel> getInvoicedItems() {

            Log.d(TAG, "inside getInvoicedItems");
            ArrayList<SalesInvoiceModel> data = new ArrayList<>();
            openDB();
            //used before when we directly took data inner joining tables
            //Cursor cursor = db.rawQuery("SELECT b.ServerID,a.ItemCode,a.Description,b.BatchNumber,b.ExpiryDate,b.SellingPrice,b.Qty" +
            //" FROM Mst_ProductMaster a inner join Tr_TabStock b " +
            //"on a.ItemCode  = b.ItemCode ;",null);
            String sql = "SELECT * from temp_invoice WHERE OrderQty !=0 OR Shelf!=0 OR Free!=0";
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                try {
                    SalesInvoiceModel salesInvoiceModel = new SalesInvoiceModel(cursor.getString(0),
                            cursor.getString(1), cursor.getString(2), cursor.getString(3),
                            cursor.getString(4), cursor.getDouble(5), cursor.getInt(6));
                    salesInvoiceModel.setShelf(cursor.getInt(7));
                    salesInvoiceModel.setRequest(cursor.getInt(8));
                    salesInvoiceModel.setOrder(cursor.getInt(9));
                    salesInvoiceModel.setFree(cursor.getInt(10));
                    salesInvoiceModel.setDiscountRate(cursor.getDouble(11));
                    salesInvoiceModel.setLineValue(cursor.getDouble(12));
                    salesInvoiceModel.setPrincipleID(cursor.getString(13));
                    salesInvoiceModel.setServerID(cursor.getString(15));
                    salesInvoiceModel.setRetailPrice(cursor.getDouble(16));
                    salesInvoiceModel.setRetailLineVal(cursor.getDouble(17));
                    data.add(salesInvoiceModel);
                } catch (Exception ex) {

                    Toast.makeText(getApplicationContext(), "Wrong Value ", Toast.LENGTH_SHORT).show();
                }
            }
            cursor.close();
//            closeDB();

            Log.d(TAG, "inside getInvoiced Items-data size_" + data.size());
            return data;
        }


        public ArrayList<SalesInvoiceModel> getPrincipleQty(String principle) {

            Log.d(TAG, "inside getPrincipleQty");
            ArrayList<SalesInvoiceModel> data = new ArrayList<>();
            openDB();
            String sql = "SELECT * from temp_invoice WHERE (OrderQty !=0 OR Shelf!=0 OR Free!=0) AND PrincipleID = (SELECT PrincipleID FROM Mst_SupplierTable WHERE Principle = '" + principle + "')";

            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                try {
                    SalesInvoiceModel salesInvoiceModel = new SalesInvoiceModel(cursor.getString(0),
                            cursor.getString(1), cursor.getString(2), cursor.getString(3),
                            cursor.getString(4), cursor.getDouble(5), cursor.getInt(6));
                    salesInvoiceModel.setShelf(cursor.getInt(7));
                    salesInvoiceModel.setRequest(cursor.getInt(8));
                    salesInvoiceModel.setOrder(cursor.getInt(9));
                    salesInvoiceModel.setFree(cursor.getInt(10));
                    salesInvoiceModel.setDiscountRate(cursor.getDouble(11));
                    salesInvoiceModel.setLineValue(cursor.getDouble(12));
                    salesInvoiceModel.setPrincipleID(cursor.getString(13));
                    salesInvoiceModel.setServerID(cursor.getString(15));
                    salesInvoiceModel.setRetailPrice(cursor.getDouble(16));
                    salesInvoiceModel.setRetailLineVal(cursor.getDouble(17));
                    data.add(salesInvoiceModel);
                } catch (Exception ex) {

                    Toast.makeText(getApplicationContext(), "Wrong Value ", Toast.LENGTH_SHORT).show();
                }
            }
            cursor.close();
            closeDB();

            return data;
        }


    }
}
