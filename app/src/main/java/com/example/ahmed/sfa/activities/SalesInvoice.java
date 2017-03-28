package com.example.ahmed.sfa.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.NavigationDrawerMenuManager;
import com.example.ahmed.sfa.controllers.adapters.SalesInvoiceAddedChoicesAdapter;
import com.example.ahmed.sfa.controllers.adapters.SalesInvoiceProductsTableAdapter;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.Brand;
import com.example.ahmed.sfa.models.Principle;
import com.example.ahmed.sfa.models.SalesInvoiceModel;
import com.example.ahmed.sfa.models.SalesInvoiceSummary;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

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
        setContentView(R.layout.sales_invoice_layout);

        showInvoiced(false);


        principleSpinner = (Spinner) findViewById(R.id.principleSpinner_si);
        subBrandSpinner = (Spinner) findViewById(R.id.si_subbrand_spinner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        //NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationDrawerMenuManager(this);

        dbAdapter = new DBAdapter(this);

        initPrincipleSpinner();


        addedListLayout = (TableLayout) findViewById(R.id.added_content_si);
        addedContentTableAdapter = new SalesInvoiceAddedChoicesAdapter(addedListLayout, this);

        showHideInvoicedBtn = (Button) findViewById(R.id.show_invoiced_si);
        showHideInvoicedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideInvoiced();
            }
        });


        TableLayout prdctsTable = (TableLayout) findViewById(R.id.product_details_si);
        productSearchTableAdapter = new SalesInvoiceProductsTableAdapter(prdctsTable, this);


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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        subtotal = (TextView)findViewById(R.id.sub_tot_si);
        invTot = (TextView)findViewById(R.id.inv_qty_si);
        discount = (TextView)findViewById(R.id.discount_si);
        tot = (TextView)findViewById(R.id.tot_si);
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

        Button nextBtn = (Button) findViewById(R.id.next_si);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(SalesInvoice.this, SalesInvoicePayment.class);
                //startActivity(intent);
                showSalesInvoiceSummary();
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
        intent.putParcelableArrayListExtra(Constants.DATAARRAYNAME,addedContentTableAdapter.getDataArray());
        intent.putExtra(Constants.SUMMARYOBJECTNAME,addedContentTableAdapter.getSalesInvoiceSummary());
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
