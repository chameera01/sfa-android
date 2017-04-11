package com.example.ahmed.sfa.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;

import controllers.DateManager;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
//import controllers.database.DBHelper;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.Mst_Customermaster;
import com.example.ahmed.sfa.models.Tr_ItineraryDetails;

public class AddExtraCustomer extends AppCompatActivity {
    Spinner spinner_area;
    Spinner spinner_town;
    SearchView cus_name;
    String area;
    String town;
    String customer_name;
    Button btn_add_itinerary;
    String qry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_extra_customer);

        try {
            spinner_area = (Spinner) findViewById(R.id.spinner_area_ec);
            spinner_town = (Spinner) findViewById(R.id.spinner_town_ec);
            cus_name = (SearchView) findViewById(R.id.search_txt_customer_ec);
            btn_add_itinerary = (Button) findViewById(R.id.btn_add_itineraray);
        }catch (Exception e){
            Toast.makeText(this, "setSpinners", Toast.LENGTH_SHORT).show();
        }

        spinner_town.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    area = spinner_area.getSelectedItem().toString();
                    town = spinner_town.getSelectedItem().toString();
                    customer_name = cus_name.getQuery().toString();

                    getdata(town, area, customer_name);
                }catch(Exception e){
                    Toast.makeText(AddExtraCustomer.this, "town_spinner_click"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
                try {
                    area = spinner_area.getSelectedItem().toString();
                    town = spinner_town.getSelectedItem().toString();
                    customer_name = cus_name.getQuery().toString();
                    getdata(town, area, customer_name);
                }catch (Exception e){
                    Toast.makeText(AddExtraCustomer.this, "Area_spinner_clcik"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //searchView_sv event
        cus_name.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getdata("ALL", "ALL", "");
                try {
                    area = spinner_area.getSelectedItem().toString();
                    town = spinner_town.getSelectedItem().toString();
                    customer_name = cus_name.getQuery().toString();

                }catch(Exception e){
                    Toast.makeText(AddExtraCustomer.this, "getDataFromSpinners:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //getdata(town, area, customer_name);
                //testing
               // getdata("ALL", "ALL", customer_name);

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
        //set event listenfer for btn add_itinerary
        btn_add_itinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSelectedRows();
            }
        });




        //setSpinner();//aulto load values to dropdown boxes
        //getdata("All", "All", "d");//call default search;
    }

    public  void setSpinner(){
        try {

            DBAdapter adapter = new DBAdapter(this);

            ArrayList<String> arrayList1 = new ArrayList<String>();

            try {
                arrayList1 = adapter.getArryListUniMethod("select DISTINCT Area from Mst_Customermaster", "Area");//.getArryListUniMethod("select DISTINCT Area from Mst_Customermaster","Area");//getArea List
            }catch (Exception e){
                Toast.makeText(this, "Area fetch from db:"+e.getMessage()   , Toast.LENGTH_SHORT).show();
            }
            arrayList1.add("ALL");/*newly addded for spinner bug fix*/
            ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList1);
            spinner_area.setAdapter(adp);
            spinner_area.setVisibility(View.VISIBLE);


            //set Spinner values to principle dropdown menu;
            ArrayList<String> principleList = new ArrayList<String>();
            try {
                principleList = adapter.getArryListUniMethod("select DISTINCT Town from Mst_Customermaster", "Town");//adapter.getAllprinciples();
            }catch (Exception e){
                Toast.makeText(this, "fetch Town datafrom db:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            principleList.add("ALL");/*newly added fro spinner fug fix*/
            ArrayAdapter<String> adp2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, principleList);
            spinner_town.setAdapter(adp2);
            spinner_town.setVisibility(View.VISIBLE);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    public void getdata(String ...filter){
        Toast.makeText(this, "came inside gedData method", Toast.LENGTH_SHORT).show();
try {
    String town = filter[0];
    String area = filter[1];
    String cusName = filter[2];

    String query = "select * from Mst_Customermaster ";
        /*SELECT doctors.doctor_id,doctors.doctor_name,visits.patient_name
        FROM doctors
        INNER JOIN visits
        ON doctors.doctor_id=visits.doctor_id*/


    if (town == "All" && area == "All") {
        if (cusName != "") {
            query += " where CustomerName like'" + cusName + "%'";
        }
    } else if (town == "All" && area != "All") {
        query += " where Area='" + area + "' ";
    } else if (town != "All" && area == "All") {
        query += " where Town='" + town + "' ";
    } else {
        query += " where Area='" + area + "'  AND Town='" + town + "' ";
    }
    if (cusName != "" && !(town == "All" && area == "All")) {
        query += " AND CustomerName like'" + cusName + "%'";
    }
        /*filter data which are not insert within one day*/
//testing
    //query += " AND strftime('%d',strftime('%d'," + DateManager.dateToday() + ")-strftime('%d',InsertDate)) NOT IN('0','1')";

        /*set Global varible to takle qury to search customerNo in add Itinerary function;*/

    qry = query.substring(9, query.length());


    //refresh the table;
    TableLayout table = (TableLayout) findViewById(R.id.table_add_extra_customer);
    //int n=table.getChildCount();
    LinearLayout l = (LinearLayout) findViewById(R.id.linear_layout_add_ec_data_row);
    int n = l.getChildCount();


    try {
        l.removeAllViews();
    } catch (Exception e) {
        Toast.makeText(this, "remove_tables:" + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
       /* table.removeViews(0,1);
                for(int index=1;index < n;index++) {
                    //table.removeView(table.getChildAt(index));
                        View view = l.getChildAt(index);
                        TableRow row = (TableRow) view;
                        l.removeView(row);

                }*/


    //end refresh

    try {
        Mst_Customermaster mst_customer = new Mst_Customermaster();

        DBHelper db = new DBHelper(this);
        Cursor res = db.getData(query);//return tupple id=1;


        while (res.moveToNext()) {

            mst_customer.setCustomerNo(res.getString(res.getColumnIndex("CustomerNo")));
            mst_customer.setCustomerName(res.getString(res.getColumnIndex("CustomerName")));
            mst_customer.setAddress(res.getString(res.getColumnIndex("Address")));
            mst_customer.setTown(res.getString(res.getColumnIndex("Town")));
            mst_customer.setTelephone(res.getString(res.getColumnIndex("Telephone")));


               /* pending_customer.setDescription(res.getString(res.getColumnIndex("Description")));//should be modified
                pending_customer.setBatchNumber(res.getString(res.getColumnIndex("BatchNumber")));
                pending_customer.setExpireyDate(res.getString(res.getColumnIndex("ExpiryDate")));
                */


            update(mst_customer);
            //testing
            //btnView_sv.setText(res.getCount());
        }

        db.close();
        //return product;

    } catch (Exception e) {
        Toast.makeText(this, "Exception in getdata:" + e.getMessage(), Toast.LENGTH_LONG).show();
    }
}catch(Exception e){
    Toast.makeText(this, "GetDataMethod_error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
}
    }


    private void update(Mst_Customermaster cus) {
        try{
        TableLayout table = (TableLayout)findViewById(R.id.table_add_extra_customer);
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.linear_layout_add_ec_data_row);

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

        /*col_1.setWeightSum(1.0f);
        col_2.setWeightSum(4.0f);
        col_3.setWeightSum(8.0f);
        col_4.setWeightSum(11.0f);
        col_5.setWeightSum(14.0f);*/

        TableRow.LayoutParams  col_param=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        col_param.weight=1.0f;
        TableRow.LayoutParams  col_param2=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        col_param2.weight=1.0f;

        col_1.setLayoutParams(col_param);
        col_2.setLayoutParams(col_param);
        col_3.setLayoutParams(col_param);
        col_4.setLayoutParams(col_param);
        col_5.setLayoutParams(col_param2);

        //new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        /*add style to table row*/
        ContextThemeWrapper wrappedContext = new ContextThemeWrapper(this, R.style.pending_customer_row);

        //add row
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));


        //add coloum_select
         final CheckBox cb_select = new CheckBox(wrappedContext);
        cb_select.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        /*set ClickEvent Listener for CheckBox;
        cb_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(cb_select.isChecked()){
                  getSelectedRows();//getParent().getParentForAccessibility());

                //}
            }
        });*/


        //add coloum_cusname
        TextView tv_cusName = new TextView(wrappedContext,null,0);
        tv_cusName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_cusName.setText(cus.getCustomerName());



        //add coloum_adress
        TextView tv_address = new TextView(wrappedContext,null,0);
        tv_address.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_address.setText(cus.getAddress()+"....");
        //tv_description.setWidth(0);

        //add coloum_contact
        TextView tv_contact = new TextView(wrappedContext,null,0);
        tv_contact.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_contact.setText(cus.getTelephone()+",,,,");

        //add coloum_town
        TextView tv_town = new TextView(wrappedContext,null,0);
        tv_town.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_town.setText(cus.getTown());










        //add coloums to row
        col_1.addView(tv_cusName);
        col_2.addView(tv_address);
        col_3.addView(tv_town);
        col_4.addView(tv_contact);
        col_5.addView(cb_select);



        tr.addView(col_5);
        tr.addView(col_1);
        tr.addView(col_2);
        tr.addView(col_3);
        tr.addView(col_4);



        // table.addView(tr);
        linearLayout.addView(tr);
        }catch (Exception e){
            Toast.makeText(this, "TableUIerror", Toast.LENGTH_SHORT).show();
        }
    }



    public  void getSelectedRows() {

        Intent db=new Intent(this,AndroidDatabaseManager.class);
        this.startActivity(db);

        TableLayout table = (TableLayout) findViewById(R.id.table_add_extra_customer);
        LinearLayout data_LinearLayout=(LinearLayout) findViewById(R.id.linear_layout_add_ec_data_row);
        DBAdapter adp=new DBAdapter(this);

        /*display table row count*/
        Toast.makeText(this,"row_count:"+data_LinearLayout.getChildCount(),Toast.LENGTH_LONG).show();

        /*load customer id list*/
        ArrayList<String> cusId=new ArrayList<String>();
        try {
            cusId = adp.getCusId("select CustomerNo " + qry);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage()+"db_data_reading error",Toast.LENGTH_LONG).show();
        }

        //get selected row;
        try {
            int count = 0;
            for (int i = 0; i < data_LinearLayout.getChildCount(); i++) {



                View parentRow = data_LinearLayout.getChildAt(i);//getTable rows
                if (parentRow instanceof TableRow) {

                    for (int j = 0; j <2/*((TableRow) parentRow).getChildCount()*/; j++) {//get columns
                        count++;
                        View innerLinear= ((TableRow) parentRow).getChildAt(j);//access to column_1
                        LinearLayout ll=(LinearLayout) innerLinear;//access to column_1 linearLayout

                        View cbox=  ll.getChildAt(0);//access to column_1 checkbox inside LinearLayout
                        View tb=ll.getChildAt(0);//acess to column 2 cusName textBox;

                        Toast.makeText(this,"line:_1",Toast.LENGTH_LONG).show();
                        if (cbox instanceof CheckBox) {
                            Toast.makeText(this,"line:_2",Toast.LENGTH_LONG).show();
                            if(((CheckBox) cbox).isChecked()) {
                                ((CheckBox) cbox).setText("Added");

                                Toast.makeText(this,"line:_3",Toast.LENGTH_LONG).show();
                                try {
                                    //cusId.get(i);
                                    addToItinerary(cusId.get(i));
                                    Toast.makeText(this, "id:"+cusId.get(i), Toast.LENGTH_SHORT).show();

                                }catch (Exception  e){
                                    Toast.makeText(this, "cusId error-"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                TableRow nametblrow=null;
                                try {
                                     nametblrow = (TableRow) data_LinearLayout.getChildAt(i);/*get  checked row*/
                                }catch (Exception e ){
                                    Toast.makeText(this, "converting_error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                LinearLayout namell= (LinearLayout) nametblrow.getChildAt(1);/*get second col in row which is checked*/
                                TextView nametv= (TextView) namell.getChildAt(0);/*enter to the textview in second colum which is selected*/

                                //add data to itirnararyDetails;nametv.setText(cusId.get(i));
                                //addToItinerary(cusId.get(i));


                            }else
                                ((CheckBox) cbox).setText("");
                        }
                       /* if(tb instanceof  TextView)
                            Toast.makeText(this,((TextView) tb).getText(),Toast.LENGTH_LONG).show();*/


                    }
                }
            }
           // Toast.makeText(this, "count:"+count, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(this,"Error:"+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    private void addToItinerary(String id){

        try {

            Tr_ItineraryDetails itinerary = new Tr_ItineraryDetails();
            DBAdapter adp = new DBAdapter(this);

            Toast.makeText(this, "came inside addtoIternerary_method", Toast.LENGTH_LONG).show();

            itinerary.setCustomerNo(id);
            itinerary.setItineraryID("ITRY" + id);
            itinerary.setItineraryDate(DateManager.dateToday());
            itinerary.setIsInvoiced(0);/*default value*/
            itinerary.setIsPlaned(0);/**default val*/
            itinerary.setLastUpdateDate(DateManager.dateToday());

            Toast.makeText(this, "added:" + id, Toast.LENGTH_LONG).show();
            String output = adp.itineraryDetails(itinerary);
            Toast.makeText(this, "output" + output, Toast.LENGTH_SHORT).show();
        }catch (Exception  e){
            Toast.makeText(this, "addToImethod:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}
