package com.example.ahmed.sfa.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.supportactivities.CollectionsSummaryFragment;
import com.example.ahmed.sfa.activities.supportactivities.InvoiceSummaryFragment;
import com.example.ahmed.sfa.activities.supportactivities.ReturnsSummaryFragment;
import com.example.ahmed.sfa.controllers.adapters.InvoiceHistoryClickListener;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.InvoiceSummary;
import com.example.ahmed.sfa.models.InvoiceSummaryFromSalesDetails;
import com.example.ahmed.sfa.models.InvoiceSummaryFromSalesHeader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class InvoiceHistory extends AppCompatActivity implements InvoiceHistoryClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Spinner selectCustomer;
    private DBAdapter adapter;
    private DBHelper helper;
    private String atvCusName;
    private String customerName;
    private ArrayList<String> cusNames;
    private InvoiceSummary[] invoiceDetails;
    private AutoCompleteTextView atv;
    private static final String TAG = "HISTORY";
    private List<DataUpdateListener> mListeners = new ArrayList<>();
    private LinearLayout container;
    private String customerHome;

    private InvoiceSummaryFromSalesHeader salesHeader;
    private ArrayList<InvoiceSummaryFromSalesDetails> salesDetails = new ArrayList<>();
    private ArrayList<InvoiceSummary> returnsDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        selectCustomer = (Spinner) findViewById(R.id.spSelectCustomer);
        atv = (AutoCompleteTextView) findViewById(R.id.atvSelectCustomer);

        adapter = new DBAdapter(this);
        helper = new DBHelper(this);

        setSpinner();

        //activity called from home with a selected customer
        customerHome = getIntent().getStringExtra("CUSTOMER");
        if (customerHome != null) {
            customerName = customerHome;
            ArrayAdapter b = (ArrayAdapter) selectCustomer.getAdapter();
            int cposition = b.getPosition(customerName);
            selectCustomer.setSelection(cposition);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cusNames);
        atv.setAdapter(arrayAdapter);

        selectCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                customerName = selectCustomer.getSelectedItem().toString();
                Log.d(TAG, customerName);
                requestDetails(customerName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                atvCusName = parent.getItemAtPosition(position).toString();
                requestDetails(atvCusName);

                ArrayAdapter a = (ArrayAdapter) selectCustomer.getAdapter(); //cast to an ArrayAdapter
                int pos = a.getPosition(atvCusName);
                selectCustomer.setSelection(pos);
                atv.setText("");
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpagerInvoiceHistory);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InvoiceSummaryFragment(), "Invoices");
        adapter.addFragment(new ReturnsSummaryFragment(), "Returns");
        adapter.addFragment(new CollectionsSummaryFragment(), "InvCollections");
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(2);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutInvoiceHistory);
//        container = (LinearLayout)findViewById(R.id.fragment_container);

        tabLayout.setupWithViewPager(viewPager);

        replaceFragment(new InvoiceSummaryFragment());

    }

    private void setSpinner() {

        cusNames = helper.getAllCustomers();

        Collections.sort(cusNames);
        cusNames.add("--Select customer--");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cusNames);
        selectCustomer.setAdapter(arrayAdapter);
        selectCustomer.setVisibility(View.VISIBLE);

        //setting the default value on top
        ArrayAdapter aa = (ArrayAdapter) selectCustomer.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = aa.getPosition("--Select customer--");
        selectCustomer.setSelection(spinnerPosition);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
    }

    private void requestDetails(String customerName) {

        //send the details to db to retrieve invoice details of selected customer
        invoiceDetails = helper.getInvoiceDetails(customerName);
        Log.d(TAG, customerName);

        //send the details to db to retrieve return details of selected customer
        returnsDetails = helper.getReturnDetails(customerName);

        dataUpdated(invoiceDetails, returnsDetails);
        Log.d(TAG, "returned data");
    }


    public synchronized void dataUpdated(InvoiceSummary[] array, ArrayList<InvoiceSummary> list) {
        for (DataUpdateListener listener : mListeners) {
            Log.d("HISTORY", "inside dataUpdated ");
            listener.onDataUpdate(array);
            listener.onReturnDataUpdate(list);
            Log.d(TAG, list.toString());

        }
    }

    public synchronized void registerDataUpdateListener(DataUpdateListener listener) {
        mListeners.add(listener);
    }

    public synchronized void unregisterDataUpdateListener(DataUpdateListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void onItemClick(String id) {
        Toast.makeText(getApplicationContext(), "inside the activity-" + id, Toast.LENGTH_LONG).show();
        salesHeader = helper.getSavedInvoiceHeaderDetails(id);
        salesDetails = helper.getSavedInvoiceDetails(id);

        Log.d(TAG, salesDetails.toString());
        Log.d(TAG, salesHeader.toString());

        Toast.makeText(getApplicationContext(), salesDetails.toString(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), salesHeader.toString(), Toast.LENGTH_LONG).show();

        if (salesHeader != null && salesDetails != null) {
            Intent intent = new Intent(this, SingleInvoiceHistoryDisplay.class);
            intent.putParcelableArrayListExtra("SalesDetails", salesDetails);
            intent.putExtra("SalesHeader", salesHeader);
            startActivity(intent);
            //pop up the interface of summary activity and load the details into it
        }

    }


    class DBAdapter extends BaseDBAdapter {

        protected DBAdapter(Context c) {
            super(c);
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public interface DataUpdateListener {
        void onDataUpdate(InvoiceSummary[] array);

        void onReturnDataUpdate(ArrayList<InvoiceSummary> list);
    }

}
