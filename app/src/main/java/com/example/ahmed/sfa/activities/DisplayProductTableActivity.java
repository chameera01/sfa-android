package com.example.ahmed.sfa.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.example.ahmed.sfa.R;
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

    TextView col_1_lbl;
    TextView col_2_lbl;
    TextView col_3_lbl;
    TextView col_4_lbl;
    TextView col_5_lbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

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


        col_1_lbl =(TextView) findViewById(R.id.lbl_1);
        col_2_lbl=(TextView) findViewById(R.id.lbl_2);
        col_3_lbl=(TextView) findViewById(R.id.lbl_3);
        col_4_lbl=(TextView) findViewById(R.id.lbl_4);
        col_5_lbl=(TextView) findViewById(R.id.lbl_5);

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
    public int  dbtest() {
        String result = "ss";
        DBHelper db = new DBHelper(this);

        try {
            result = db.insertProduct("cd001", "descrp_2", "principle_1", "phama", "bran_1", "belcocid", "subid", "sub_name", 45, "unitname", 50.2, 60.0, 30.0, 1, "2017-10-10", 1);
        } catch (Exception e) {
            result = "failInsert";
        }
        btnviewall.setText(result);

        if (result == "success"){
            return 1;
        }else{
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
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
					query+= " where Description like'"+searchword+"%'";
				}
            }else if(principle=="All" && brand !="All"){
                query+= " where Brand='"+brand+"' ";
            }else if(principle!="All" && brand=="All"){
                query+= " where Principle='"+principle+"' ";
            }else{
                query+= " where Brand='"+brand+"'  AND Principle='"+principle+"' ";
            }
		if(searchword!=null && !(principle=="All" && brand=="All") ){
			query+=" AND Description like'"+searchword+"%'";
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

                product.setItemCode(res.getString(res.getColumnIndex("ItemCode")));//res.getColumnIndex("itemcode")
                product.setBrand(res.getString(res.getColumnIndex("Brand")));
                product.setUnitSize(res.getInt(res.getColumnIndex("UnitSize")));
                product.setSellingPrice(res.getFloat(res.getColumnIndex("SellingPrice")) );
                product.setRetailPrice(res.getFloat(res.getColumnIndex("RetailPrice")) );
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
        //Layput parametre

        TableRow.LayoutParams  col_param=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        col_param.weight=1.0f;


        /*add layout */
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

        col_1.setLayoutParams(col_param);
        col_2.setLayoutParams(col_param);
        col_3.setLayoutParams(col_param);
        col_4.setLayoutParams(col_param);
        col_5.setLayoutParams(col_param);
        /**/

        //add row
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

        //add coloum_item_code
        TextView tv = new TextView(wrappedContext,null,0);
        tv.setLayoutParams(col_param/*new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)*/);
        tv.setText(pm.getItemCode());
        Toast.makeText(DisplayProductTableActivity.this, "pm_:"+pm.getItemCode(), Toast.LENGTH_SHORT).show();

        //add coloum_brand
        TextView tv_brand = new TextView(wrappedContext,null,0);
        tv_brand.setLayoutParams(col_param/*new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)*/);
        tv_brand.setText(pm.getBrand());


        //add coloum_unitsize
        TextView tv_unitsize = new TextView(wrappedContext,null,0);
        tv_unitsize.setLayoutParams(col_param/*-new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)*/);
        tv_unitsize.setText(""+pm.getUnitSize());

        //add coloum_sellingprice
        TextView tv_sellingprice = new TextView(wrappedContext,null,0);
        tv_sellingprice.setLayoutParams(col_param/*new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)*/);
        tv_sellingprice.setText(""+pm.getSellingPrice());

        //add coloum_sellingprice
        TextView tv_retailprice = new TextView(wrappedContext,null,0);
        tv_retailprice.setLayoutParams(col_param/*new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)*/);
        tv_retailprice.setText(" " + pm.getRetailPrice());




        //
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

        /*set table colum width to cloum width in data rows*//*
        col_1_lbl.setWidth(tv.getWidth());
        col_2_lbl.setWidth(tv_brand.getWidth());
        col_3_lbl.setWidth(tv_unitsize.getWidth());
        col_4_lbl.setWidth(tv_retailprice.getWidth());
        col_5_lbl.setWidth(tv_retailprice.getWidth());
*/

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
