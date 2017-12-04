package com.example.ahmed.sfa.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.activities.supportactivities.TableThread;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.adapters.NavigationDrawerMenuManager;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.Mst_ProductMaster;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.text.DecimalFormat;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//fixed landscape screan;



        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationDrawerMenuManager(this);

        //Intent intent = getIntent();

       /*add initial message com from intent starter
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("sssssssssssssss");
        //textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
        layout.addView(textView);*/

        spinner_brand = (Spinner) findViewById(R.id.spinner_brand);
        spinner_principle = (Spinner) findViewById(R.id.spinner_principle);
        searchView=(SearchView) findViewById(R.id.search_txt);
        btnviewall=(Button)findViewById(R.id.button_view);

        //onEventListenr for spinner principle
        spinner_principle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                principle = spinner_principle.getSelectedItem().toString();
                brand = spinner_brand.getSelectedItem().toString();
                keyword = searchView.getQuery().toString();
                getdata(principle, brand, keyword);
                sortBrandByPrinciple(principle);

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
                brand = spinner_brand.getSelectedItem().toString();
                principle = spinner_principle.getSelectedItem().toString();
                keyword = searchView.getQuery().toString();
                getdata(principle, brand, keyword);
            }

        });
        /*spinner_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                brand = spinner_brand.getSelectedItem().toString();
                principle = spinner_principle.getSelectedItem().toString();
                keyword = searchView.getQuery().toString();
                getdata(principle, brand, keyword);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });*/
		
		
        setSpinner();//aulto load values to dropdown boxes
        //getdata("All", "All", "cd001");//call default search;




    }
    private void sortBrandByPrinciple(String principle) {
        try{
            //set brand spinner values accordingt o principle
            DBAdapter adapter = new DBAdapter(this);

            String qry="select DISTINCT Brand from Mst_ProductMaster where Principle='"+principle+"'";
            ArrayList<String> brandList = adapter.getArryListUniMethod(qry,"Brand");
            if(brandList.isEmpty()){
                brandList.add("All");
            }

            ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,brandList);
            spinner_brand.setAdapter(adp);
            spinner_brand.setVisibility(View.VISIBLE);
        }catch (Exception e){
            Toast.makeText(this, "ee"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void setListView(){
        ListView lv= (ListView) findViewById(R.id.list_view);
        TableLayout tbl= (TableLayout) findViewById(R.id.table_product);
        TableThread tt = new TableThread(this,this,lv,tbl,"");
        tt.start();
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

    /*public void viewallbtnClick(View view){
		brand=spinner_brand.getSelectedItem().toString();
		principle=spinner_principle.getSelectedItem().toString();
		keyword=searchView.getQuery().toString();


        //String val=getdata();
        int result=dbtest();

        Snackbar.make(view, "number of result"+result+"", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        getdata(principle, brand, keyword);
    }*/


    public void selfDestruct(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    public int  dbtest() {
        String result = "ss";
        DBHelper db = new DBHelper(this);

        try {
            result = db.insertProduct("cd001", "descrp_2", "principle_1", "phama", "bran_1", "belcocid", "subid", "sub_name", 45, "unitname", 50.2, 60.0, 30.0, 1, "2017-10-10", 1);
        } catch (Exception e) {
            result = "failInsert";
        }
        btnviewall.setText(result);

        db.close();
        if (result == "success"){
            return 1;
        }else{
           // Toast.makeText(this, "here toast"+result, Toast.LENGTH_LONG).show();
            return 0;
        }

    }
    public void getdata(String ...filter){

        String principle=filter[0];
        String brand=filter[1];
        String searchword=filter[2];
		String searchQry="";
        String query="select * from Mst_ProductMaster ";

		

		
            if(principle=="All" && brand=="All"){
                if(searchword!=null){
					query+= " where Description like'%"+searchword+"%'";
				}
            }else if(principle=="All" && brand !="All"){
                query+= " where Brand='"+brand+"' ";
            }else if(principle!="All" && brand=="All"){
                query+= " where Principle='"+principle+"' ";
            }else{
                query+= " where Brand='"+brand+"'  AND Principle='"+principle+"' ";
            }
		if(searchword!=null && !(principle=="All" && brand=="All") ){
			query+=" AND Description like'%"+searchword+"%'";
		}

            //refresh the table;
			TableLayout table = (TableLayout)findViewById(R.id.table_product);
			int n=table.getChildCount();


           table.removeAllViews();
            //Thread
        ListView lv= (ListView) findViewById(R.id.list_view);
        TableLayout tbl= (TableLayout) findViewById(R.id.table_product);
        TableThread tt = new TableThread(this,this,lv,tbl,query);
        tt.start();

        /*try {
            Mst_ProductMaster product= new Mst_ProductMaster();
            DBHelper db = new DBHelper(this);
            Cursor res = db.getData(query);//return tupple id=1;



            while (res.moveToNext()) {
                row_count++;
                product.setItemCode(res.getString(res.getColumnIndex("ItemCode")));//res.getColumnIndex("itemcode")
                product.setBrand(res.getString(res.getColumnIndex("Brand")));
                product.setUnitSize(res.getInt(res.getColumnIndex("UnitSize")));
                product.setSellingPrice(res.getFloat(res.getColumnIndex("SellingPrice")) );
                product.setRetailPrice(res.getFloat(res.getColumnIndex("RetailPrice")) );

                update(product);
                //btnviewall.setText(res.getCount());
            }
            db.close();

           // setListView();
            ///Toast.makeText(DisplayProductTableActivity.this, "RowCount<1:"+row_count, Toast.LENGTH_SHORT).show();
            if(row_count<1){
                //Toast.makeText(DisplayProductTableActivity.this, "RowCount<1:"+row_count, Toast.LENGTH_SHORT).show();
                TextView tr_emty_msg=new TextView(this);
                tr_emty_msg.setText("No result to preview");
                table.addView(tr_emty_msg);
            }
            row_count=0;


           //return product;

        }catch (Exception e){
            e.printStackTrace();
            //btnviewall.setText(e.getMessage());
        }*/
    }
    int row_count=0;
    //insert data to  table

    private void update(Mst_ProductMaster pm) {

        TableLayout table = (TableLayout)findViewById(R.id.table_product);

        /*add style to table row*/
        ContextThemeWrapper wrappedContext = new ContextThemeWrapper(this, R.style.pending_customer_row);
        //add row
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        /*add style to table row*/

        if(row_count%2!=0) {
            wrappedContext = new ContextThemeWrapper(this, R.style.pending_customer_odd_row);
        }

        //tv.setLayoutParams(param);
        /*add extra cutomer method*/
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



        TableRow.LayoutParams  col_param=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,0.1f);
        TableRow.LayoutParams  col_param_wide=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,0.2f);
        col_param.weight=1f;
        col_param_wide.weight=2f;
        //col_param.width=150;
        //col_param_wide.width=350;


        col_1.setLayoutParams(col_param);
        col_2.setLayoutParams(col_param_wide);
        col_3.setLayoutParams(col_param);
        col_4.setLayoutParams(col_param);
        col_5.setLayoutParams(col_param);
        /*end addextra customer method*/

        //add coloum_item_code
        TextView tv = new TextView(wrappedContext,null,0);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv.setText(pm.getItemCode());
        tv.setGravity(Gravity.LEFT);
        //add coloum_brand
        TextView tv_brand = new TextView(wrappedContext,null,0);
        tv_brand.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_brand.setText(pm.getBrand());
        tv_brand.setGravity(Gravity.LEFT);

        TextView th_prdct=(TextView) findViewById(R.id.tv_product_col_title);
        //tv.setWidth(th_prdct.getWidth());
        //tv.setText("Entry-1");

        //add coloum_unitsize
        TextView tv_unitsize = new TextView(wrappedContext,null,0);
        tv_unitsize.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_unitsize.setText(" "+pm.getUnitSize());


        //add coloum_sellingprice
        TextView tv_sellingprice = new TextView(wrappedContext,null,0);
        tv_sellingprice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        String  sp= ""+pm.getSellingPrice();
        //String[] sellp_str=sp.split(".");
        //String[] decimal_sp=sellp_str[1].split("");
        double sellp = Double.parseDouble(new DecimalFormat("##.##").format(pm.getSellingPrice()));
        tv_sellingprice.setText(""+sellp);
        tv_sellingprice.setGravity(Gravity.RIGHT);

        //add coloum_sellingprice
        TextView tv_retailprice = new TextView(wrappedContext,null,0);
        tv_retailprice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        double retailp = Double.parseDouble(new DecimalFormat("##.##").format(pm.getRetailPrice()));
        tv_retailprice.setText(" " + retailp);
        tv_retailprice.setGravity(Gravity.RIGHT);


        /*TextView tv2 = new TextView(wrappedContext,null,0);
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv2.setText("Entry-2");*/



        col_1.addView(tv);
        col_2.addView(tv_brand);
        col_3.addView(tv_unitsize);
        col_4.addView(tv_sellingprice);
        col_5.addView(tv_retailprice);

        //add coloums to row
        tr.addView(col_1);
        tr.addView(col_2);
        tr.addView(col_3);
        tr.addView(col_4);
        tr.addView(col_5);

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
