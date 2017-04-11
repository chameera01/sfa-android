package com.example.ahmed.sfa.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import java.util.ArrayList;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;


import com.example.ahmed.sfa.controllers.adapters.NavigationDrawerMenuManager;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.Tr_NewCustomer;

public class PendingCustomer extends AppCompatActivity {
    Spinner spinner_area;
    Spinner spinner_town;
    SearchView cus_name;
    Button btnView_sv;

    String area;
    String town;
    String customer_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_customer);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationDrawerMenuManager(this);

        /**/
        Intent intent = getIntent();

        TextView textView = new TextView(this);
        textView.setTextSize(40);
        //textView.setText("SFA");

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_pending_customer);
        layout.addView(textView);
        /*delete after test*/


        spinner_area = (Spinner) findViewById(R.id.spinner_area);
        spinner_town = (Spinner) findViewById(R.id.spinner_town);
        cus_name =(SearchView) findViewById(R.id.search_txt_customer);
        //btnView_sv =(Button)findViewById(R.id.btn_add);

        //onEventListenr for spinner principle
        spinner_town.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                area = spinner_area.getSelectedItem().toString();
                town = spinner_town.getSelectedItem().toString();
                customer_name = cus_name.getQuery().toString();

               getdata(town, area, customer_name);
                //sortBrandByPrinciple(town);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //onEventListenr for spinner brand
        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                area = spinner_area.getSelectedItem().toString();
                town = spinner_town.getSelectedItem().toString();
                customer_name = cus_name.getQuery().toString();
                getdata(town, area, customer_name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //searchView_sv event
        cus_name.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                area = spinner_area.getSelectedItem().toString();
                town = spinner_town.getSelectedItem().toString();
                customer_name = cus_name.getQuery().toString();
                getdata(town, area, customer_name);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              if (searchView_sv.isExpanded() && TextUtils.isEmpty(newText)) {
                callSearch(newText);
//              }
                return true;
            }

            public void callSearch(String query) {
                //Do searching
            }

        });




        setSpinner();//aulto load values to dropdown boxes
        getdata("All", "All", "d");//call default search;

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

    public  void setSpinner(){

        DBAdapter adapter = new DBAdapter(this);

        ArrayList<String> arrayList1 = new ArrayList<String>();
        arrayList1=adapter.getAllArea();

        ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        spinner_area.setAdapter(adp);
        spinner_area.setVisibility(View.VISIBLE);


        //set Spinner values to principle dropdown menu;
        ArrayList<String> principleList = new ArrayList<String>();
        principleList=adapter.getAllTown();//adapter.getAllprinciples();


        ArrayAdapter<String> adp2 = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,principleList);
        spinner_town.setAdapter(adp2);
        spinner_town.setVisibility(View.VISIBLE);
    }
    public void getdata(String ...filter){

        String town=filter[0];
        String area=filter[1];
        String cusName=filter[2];

        String query="select * from Tr_NewCustomer ";
        /*SELECT doctors.doctor_id,doctors.doctor_name,visits.patient_name
        FROM doctors
        INNER JOIN visits
        ON doctors.doctor_id=visits.doctor_id*/




        if(town=="All" && area=="All"){
            if(cusName!=""){
                query+= " where CustomerName like'"+cusName+"%'";
            }
        }else if(town=="All" && area !="All"){
            query+= " where Area='"+area+"' ";
        }else if(town!="All" && area=="All"){
            query+= " where Town='"+town+"' ";
        }else{
            query+= " where Area='"+area+"'  AND Town='"+town+"' ";
        }
        if(cusName!="" && !(town=="All" && area=="All") ){
            query+=" AND CustomerName like'"+cusName+"%'";
        }
        //query="select * from Tr_TabStock";//WARNING !<<remover this after test>>

        //refresh the table;
        TableLayout table = (TableLayout)findViewById(R.id.table_pending_customer);
        //int n=table.getChildCount();
        LinearLayout  l=(LinearLayout)findViewById(R.id.linear_layout_data_row);
        int n=l.getChildCount();



        l.removeAllViews();
       /* table.removeViews(0,1);
                for(int index=1;index < n;index++) {
                    //table.removeView(table.getChildAt(index));
                        View view = l.getChildAt(index);
                        TableRow row = (TableRow) view;
                        l.removeView(row);

                }*/


        //end refresh

        try {
            Tr_NewCustomer  pending_customer= new Tr_NewCustomer();

            DBHelper db = new DBHelper(this);
            Cursor res = db.getData(query);//return tupple id=1;



            while (res.moveToNext()) {

                pending_customer.setNewCustomerID(res.getString(res.getColumnIndex("NewCustomerID")));
                pending_customer.setCustomerName(res.getString(res.getColumnIndex("CustomerName")));
                pending_customer.setAddress(res.getString(res.getColumnIndex("Address")));
                pending_customer.setContactNo(res.getString(res.getColumnIndex("OwnerContactNo")));
                pending_customer.setUploadedStatus(res.getString(res.getColumnIndex("IsUpload")));
                pending_customer.setApprovedStatus(res.getString(res.getColumnIndex("ApproveStatus")));

               /* pending_customer.setDescription(res.getString(res.getColumnIndex("Description")));//should be modified
                pending_customer.setBatchNumber(res.getString(res.getColumnIndex("BatchNumber")));
                pending_customer.setExpireyDate(res.getString(res.getColumnIndex("ExpiryDate")));
                */


                update(pending_customer);
                //btnView_sv.setText(res.getCount());
            }

            db.close();
            //return product;

        }catch (Exception e){
            //btnView_sv.setText(e.getMessage());
        }
    }


    //insert data to  table
    private void update(Tr_NewCustomer cus) {
        TableLayout table = (TableLayout)findViewById(R.id.table_pending_customer);
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.linear_layout_data_row);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;



        LinearLayout col_1=new LinearLayout(this);
        LinearLayout col_2=new LinearLayout(this);
        LinearLayout col_3=new LinearLayout(this);
        LinearLayout col_4=new LinearLayout(this);
        LinearLayout col_5=new LinearLayout(this);


        col_1.setOrientation(LinearLayout.VERTICAL);
        col_2.setOrientation(LinearLayout.VERTICAL);
        col_3.setOrientation(LinearLayout.VERTICAL);
        col_4.setOrientation(LinearLayout.VERTICAL);
        col_5.setOrientation(LinearLayout.VERTICAL);

        /*ol_1.setWeightSum(1.0f);
        col_2.setWeightSum(2.0f);
        col_3.setWeightSum(3.0f);
        col_4.setWeightSum(4.0f);
        col_5.setWeightSum(5.0f);*/

        TableRow.LayoutParams  col_param=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        col_param.weight=1.0f;

        col_1.setLayoutParams(col_param);
        col_2.setLayoutParams(col_param);
        col_3.setLayoutParams(col_param);
        col_4.setLayoutParams(col_param);
        col_5.setLayoutParams(col_param);
//new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        /*add style to table row*/
        ContextThemeWrapper wrappedContext = new ContextThemeWrapper(this, R.style.pending_customer_row);

        //add row
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));



        //add coloum_cusname
        TextView tv_cusName = new TextView(wrappedContext,null,0);
        tv_cusName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_cusName.setText(cus.getCustomerName());



        //add coloum_adress
        TextView tv_address = new TextView(wrappedContext,null,0);
        tv_address.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_address.setText(cus.getAddress());
        //tv_description.setWidth(0);

        //add coloum_contact
        TextView tv_contact = new TextView(wrappedContext,null,0);
        tv_contact.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_contact.setText(cus.getContactNo());

        //add coloum_uploadstatus
        TextView tv_uploadStatus = new TextView(wrappedContext,null,0);
        tv_uploadStatus.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_uploadStatus.setText(cus.getUploadedStatus());

        //add coloum_aprovedstatus
        TextView tv_approvedStatus = new TextView(wrappedContext,null,0);
        tv_approvedStatus.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_approvedStatus.setText(cus.getApprovedStatus());







        //add coloums to row
        col_1.addView(tv_cusName);
        col_2.addView(tv_address);
        col_3.addView(tv_contact);
        col_4.addView(tv_uploadStatus);
        col_5.addView(tv_approvedStatus);


        tr.addView(col_1);
        tr.addView(col_2);
        tr.addView(col_3);
        tr.addView(col_4);
        tr.addView(col_5);


       // table.addView(tr);
        linearLayout.addView(tr);
    }
}
