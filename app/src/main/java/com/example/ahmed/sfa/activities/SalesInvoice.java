package com.example.ahmed.sfa.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.PermissionManager;
import com.example.ahmed.sfa.controllers.adapters.NavigationDrawerMenuManager;
import com.example.ahmed.sfa.controllers.adapters.SalesInvoiceAddedChoicesAdapter;
import com.example.ahmed.sfa.controllers.adapters.SalesInvoiceProductsTableAdapter;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.Brand;
import com.example.ahmed.sfa.models.Itinerary;
import com.example.ahmed.sfa.models.Principle;
import com.example.ahmed.sfa.models.SalesInvoiceModel;
import com.example.ahmed.sfa.models.SalesInvoiceSummary;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 3/19/2017.
 */

public class SalesInvoice extends AppCompatActivity {


    DBAdapter dbAdapter;
    SalesInvoiceProductsTableAdapter productSearchTableAdapter;
    SalesInvoiceAddedChoicesAdapter addedContentTableAdapter;
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

    private boolean showInvoiced;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private void showInvoiced(boolean com) {
        showInvoiced = com;
    }

    private boolean isShowingInvoiced() {
        return showInvoiced;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout )findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
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
                setContentView(R.layout.sales_invoice_layout);

                customerNo = getIntent().getStringExtra(Constants.CUSTOMER_NO);
                itinerary = getIntent().getParcelableExtra(Constants.ITINERARY);

                //Toast.makeText(getApplicationContext(),"taken +"+customerNo+" -- "+itinerary.getId(),Toast.LENGTH_SHORT).show();
                showInvoiced(false);


                principleSpinner = (Spinner) findViewById(R.id.principleSpinner_si);
                subBrandSpinner = (Spinner) findViewById(R.id.si_subbrand_spinner);

                dbAdapter = new DBAdapter(this);

                initPrincipleSpinner();


                addedListLayout = (TableLayout) findViewById(R.id.added_content_si);
                addedContentTableAdapter = new SalesInvoiceAddedChoicesAdapter(addedListLayout, this);

                showHideInvoicedBtn = (Button) findViewById(R.id.show_returned_sr);
                showHideInvoicedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showHideInvoiced();
                    }
                });


                TableLayout prdctsTable = (TableLayout) findViewById(R.id.product_details_si);
                productSearchTableAdapter = new SalesInvoiceProductsTableAdapter(prdctsTable, this);

                searchView = (SearchView) findViewById(R.id.search_query_si);
                //ImageView searchIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
                //Drawable icon = searchIcon.getDrawable();
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                  //  icon.setTint(Color.RED);
                //}
                //searchIcon.setImageDrawable(icon);

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

                Button nextBtn = (Button) findViewById(R.id.next_si);
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent intent = new Intent(SalesInvoice.this, SalesInvoicePayment.class);
                        //startActivity(intent);
                        showSalesInvoiceSummary();
                    }
                });
            }else{
                Toast.makeText(this,"Location unavailable",Toast.LENGTH_SHORT).show();
                setContentView(R.layout.location_not_found_error_layout);
            }

        }else{
            Toast.makeText(this,"Permission Unavailable",Toast.LENGTH_SHORT).show();
            setContentView(R.layout.location_not_found_error_layout);

        }
    }

    private void showHideInvoiced() {
        Boolean isShowing = isShowingInvoiced();
        if (isShowing) {
            addedListLayout.setVisibility(View.GONE);
            showInvoiced(false);
            showHideInvoicedBtn.setText("Show Invoiced");
        } else {
            addedListLayout.setVisibility(View.VISIBLE);
            showInvoiced(true);
            showHideInvoicedBtn.setText("Hide Invoiced");
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
        if (principle == null || principle.equals("ALL")) {
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

    public void showSalesInvoiceSummary(){
        Intent intent = new Intent(this,SalesInvoicePayment.class);
        intent.putParcelableArrayListExtra(Constants.DATA_ARRAY_NAME,addedContentTableAdapter.getDataArray());
        intent.putExtra(Constants.SUMMARY_OBJECT_NAME,addedContentTableAdapter.getSalesInvoiceSummary());
        intent.putExtra(Constants.CUSTOMER_NO,customerNo);
        intent.putExtra(Constants.ITINERARY,itinerary);
        startActivity(intent);
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


    }


}
