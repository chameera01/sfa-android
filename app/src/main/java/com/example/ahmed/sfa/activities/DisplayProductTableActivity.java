package com.example.ahmed.sfa.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.NavigationDrawerMenuManager;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.Mst_ProductMaster;


import java.util.ArrayList;

public class DisplayProductTableActivity extends AppCompatActivity {
    Button btnviewall;
    Spinner spinner_brand;
    Spinner spinner_principle;
    SearchView searchView;


    String brand;
    String principle;
    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationDrawerMenuManager(this);

        Intent intent = getIntent();

        TextView textView = new TextView(this);
        textView.setTextSize(40);
        //textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
        layout.addView(textView);

        spinner_brand = (Spinner) findViewById(R.id.spinner_brand);
        spinner_principle = (Spinner) findViewById(R.id.spinner_principle);
        searchView=(SearchView) findViewById(R.id.search_txt);
        btnviewall=(Button)findViewById(R.id.button_view);

        //onEventListenr for spinner principle
        spinner_principle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                brand = spinner_brand.getSelectedItem().toString();
                principle = spinner_principle.getSelectedItem().toString();
                keyword = searchView.getQuery().toString();
                getdata(principle, brand, keyword);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //onEventListenr for spinner brand
        spinner_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                brand = spinner_brand.getSelectedItem().toString();
                principle = spinner_principle.getSelectedItem().toString();
                keyword = searchView.getQuery().toString();
                getdata(principle, brand, keyword);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        /*btnviewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            brand=spinner_brand.getSelectedItem().toString();
			principle=spinner_principle.getSelectedItem().toString();
			keyword=searchView.getQuery().toString();
            }
        });*/
		//searchView event
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                brand = spinner_brand.getSelectedItem().toString();
                principle = spinner_principle.getSelectedItem().toString();
                keyword = searchView.getQuery().toString();
                getdata(principle, brand, keyword);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
                callSearch(newText);
//              }
                return true;
            }

            public void callSearch(String query) {
                //Do searching
            }

        });
        spinner_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Your code here
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
		
		
        setSpinner();//aulto load values to dropdown boxes
        getdata("All", "All", "cd001");//call default search;




    }

    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout )findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    public  void setSpinner(){
        DBHelper dbbrand=new DBHelper(this);

        ArrayList<String> arrayList1 = new ArrayList<String>();
        arrayList1=dbbrand.getAllbrands();
        ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        spinner_brand.setAdapter(adp);

        spinner_brand.setVisibility(View.VISIBLE);


        //set Spinner values to principle dropdown menu;
        ArrayList<String> principleList = new ArrayList<String>();
        principleList=dbbrand.getAllprinciples();

        ArrayAdapter<String> adp2 = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,principleList);
        spinner_principle.setAdapter(adp2);

        spinner_principle.setVisibility(View.VISIBLE);
    }

    public void viewallbtnClick(View view){
		brand=spinner_brand.getSelectedItem().toString();
		principle=spinner_principle.getSelectedItem().toString();
		keyword=searchView.getQuery().toString();


        //String val=getdata();
        int result=dbtest();

        Snackbar.make(view, ""+result+"", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        getdata(principle, brand, keyword);
    }


    public void selfDestruct(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    public int  dbtest(){
        String result="ss";
        DBHelper db=new DBHelper(this);

        try {
           result= db.insertProduct("cd001", "descrp_2", "principle_1", "phama", "bran_1", "belcocid", "subid", "sub_name", 45, "unitname", 50.2, 60.0, 30.0, 1, "2017-10-10", 1);
        }catch (Exception e){
            result ="failInsert";
        }
        btnviewall.setText(result);

        if(result=="success")
            return   1;
        else
            return 0;
    }
    public void getdata(String ...filter){

        String principle=filter[0];
        String brand=filter[1];
        String searchword=filter[2];
		String searchQry="";
        String query="select * from mst_productmaster ";

		

		
            if(principle=="All" && brand=="All"){
                if(searchword!=null){
					query+= " where description like'"+searchword+"%'";
				}
            }else if(principle=="All" && brand !="All"){
                query+= " where brand='"+brand+"' ";
            }else if(principle!="All" && brand=="All"){
                query+= " where principle='"+principle+"' ";
            }else{
                query+= " where brand='"+brand+"'  AND principle='"+principle+"' ";
            }
		if(searchword!=null && !(principle=="All" && brand=="All") ){
			query+=" AND description like'"+searchword+"%'";
		}

            //refresh the table;
			TableLayout table = (TableLayout)findViewById(R.id.table_product);
			int n=table.getChildCount();


            table.removeAllViews();
                /*for(int index=1;index < n;index++) {
                    table.removeView(table.getChildAt(index));
                        /*View view = table.getChildAt(index);
                        TableRow row = (TableRow) view;
                        table.removeView(row);

                }*/


            //end refresh
				
        try {
            Mst_ProductMaster product= new Mst_ProductMaster();
            DBHelper db = new DBHelper(this);
            Cursor res = db.getData(query);//return tupple id=1;

			

            while (res.moveToNext()) {

                product.setItemCode(res.getString(res.getColumnIndex("itemcode")) + "" + res.getCount());//res.getColumnIndex("itemcode")
                product.setBrand(res.getString(1) + "" + res.getCount());
                product.setUnitSize(res.getInt(res.getColumnIndex("unitsize")));
                product.setSellingPrice(res.getFloat(res.getColumnIndex("sellingprice")) );
                product.setRetailPrice(res.getFloat(res.getColumnIndex("retailprice")) );
                update(product);
                //btnviewall.setText(res.getCount());
            }


           //return product;

        }catch (Exception e){
            //btnviewall.setText(e.getMessage());
        }
    }
    //insert data to  table
    private void update(Mst_ProductMaster pm) {
        TableLayout table = (TableLayout)findViewById(R.id.table_product);

        

        /*add style to table row*/
        ContextThemeWrapper wrappedContext = new ContextThemeWrapper(this, R.style.tableRow);

        //add row
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        //add coloum_item_code
        TextView tv = new TextView(wrappedContext,null,0);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv.setText(pm.getItemCode());

        //add coloum_brand
        TextView tv_brand = new TextView(wrappedContext,null,0);
        tv_brand.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_brand.setText(pm.getBrand());
        //tv.setText("Entry-1");

        //add coloum_unitsize
        TextView tv_unitsize = new TextView(wrappedContext,null,0);
        tv_unitsize.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_unitsize.setText(" "+pm.getUnitSize());

        //add coloum_sellingprice
        TextView tv_sellingprice = new TextView(wrappedContext,null,0);
        tv_sellingprice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_sellingprice.setText(" "+pm.getSellingPrice());

        //add coloum_sellingprice
        TextView tv_retailprice = new TextView(wrappedContext,null,0);
        tv_retailprice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_retailprice.setText(" " + pm.getRetailPrice());


        TextView tv2 = new TextView(wrappedContext,null,0);
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv2.setText("Entry-2");


        //add coloums to row
        tr.addView(tv);
        tr.addView(tv2);
        tr.addView(tv_brand);
        tr.addView(tv_unitsize);
        tr.addView(tv_sellingprice);
        tr.addView(tv_retailprice);



        table.addView(tr);
    }
   /* private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }*/
    /*
    //getselcted row;


     row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                 Toast.makeText(getApplicationContext(), "value was "+value,
                  Toast.LENGTH_LONG).show();
                    view.setBackgroundColor(Color.DKGRAY);
                }
                });
     //second method to get selected row ;
      private OnClickListener tablerowOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            //GET TEXT HERE
            String currenttext = ((TextView)v).getText().toString());
        }
    };
    //3rd method;
    tr.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO:
        }
    });
     */

}
