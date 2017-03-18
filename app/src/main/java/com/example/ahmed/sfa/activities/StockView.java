package com.example.ahmed.sfa.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;


import java.util.ArrayList;

import controllers.database.DBHelper;
import com.example.ahmed.sfa.models.Tr_TabStock;

public class StockView extends AppCompatActivity {
    Spinner spinner_brand_sv;
    Spinner spinner_principle_sv;
    SearchView searchView_sv;
    Button btnView_sv;

    String brand;
    String principle;
    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_view);



        Intent intent = getIntent();

        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("SFA");

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_stock_view);
        layout.addView(textView);

        spinner_brand_sv = (Spinner) findViewById(R.id.spinner_brand_stock_view);
        spinner_principle_sv = (Spinner) findViewById(R.id.spinner_principle_stockview);
        searchView_sv =(SearchView) findViewById(R.id.search_txt_stockview);
        btnView_sv =(Button)findViewById(R.id.btn_add);

        //onEventListenr for spinner principle
        spinner_principle_sv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                brand = spinner_brand_sv.getSelectedItem().toString();
                principle = spinner_principle_sv.getSelectedItem().toString();
                keyword = searchView_sv.getQuery().toString();
                getdata(principle, brand, keyword);
                sortBrandByPrinciple(principle);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //onEventListenr for spinner brand
        spinner_brand_sv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                brand = spinner_brand_sv.getSelectedItem().toString();
                principle = spinner_principle_sv.getSelectedItem().toString();
                keyword = searchView_sv.getQuery().toString();
                getdata(principle, brand, keyword);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*btnView_sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            brand=spinner_brand_sv.getSelectedItem().toString();
			principle=spinner_principle_sv.getSelectedItem().toString();
			keyword=searchView_sv.getQuery().toString();
            }
        });*/
        //searchView_sv event
        searchView_sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                brand = spinner_brand_sv.getSelectedItem().toString();
                principle = spinner_principle_sv.getSelectedItem().toString();
                keyword = searchView_sv.getQuery().toString();
                getdata(principle, brand, keyword);

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
        /*removed ovriwitten method
        spinner_brand_sv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Your code here
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });*/



        setSpinner();//aulto load values to dropdown boxes
        //getdata("All", "All", "d");//call default search;
    }

    private void sortBrandByPrinciple(String principle) {
        //set brand spinner values accordingt o principle
        DBAdapter adapter = new DBAdapter(this);

        String qry="select DISTINCT BrandID from Tr_TabStock where PrincipleID='"+principle+"'";
        ArrayList<String> brandList = new ArrayList<String>();
        brandList=adapter.getAllbrands(qry);

        ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,brandList);
        spinner_brand_sv.setAdapter(adp);
        spinner_brand_sv.setVisibility(View.VISIBLE);

    }


    public  void setSpinner(){
        DBHelper dbbrand=new DBHelper(this);
        DBAdapter adapter = new DBAdapter(this);

        ArrayList<String> arrayList1 = new ArrayList<String>();
        arrayList1=dbbrand.getAllbrands();

        ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        spinner_brand_sv.setAdapter(adp);
        spinner_brand_sv.setVisibility(View.VISIBLE);


        //set Spinner values to principle dropdown menu;
        ArrayList<String> principleList = new ArrayList<String>();
        principleList=adapter.getAllprinciples();//adapter.getAllprinciples();


        ArrayAdapter<String> adp2 = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,principleList);
        spinner_principle_sv.setAdapter(adp2);
        spinner_principle_sv.setVisibility(View.VISIBLE);
    }

    public void btnStockView(View view){
        Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show();

        brand= spinner_brand_sv.getSelectedItem().toString();
        principle= spinner_principle_sv.getSelectedItem().toString();
        keyword= searchView_sv.getQuery().toString();


        //String val=getdata();
        int result=0;
        try{
            result=dbtest();
        }catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Snackbar.make(view, ""+result+"", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        getdata(principle, brand, keyword);

    }


    public void selfDestruct(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    public int  dbtest(){
       String result;




        try {
            DBHelper db=new DBHelper(this);
            result=db.inserToStockView("server_id","principle_id3","bra_id","cd005","bath_2","2017-10-25",45.50,60.5,895,"2017-02-25");

            //db.close();
        }catch (Exception e){
            result= e.getMessage();
        }
        //btnView_sv.setText(result);
        Toast.makeText(this,result,Toast.LENGTH_SHORT).show();

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
        String query="select * from Mst_ProductMaster INNER JOIN Tr_TabStock  ON Mst_ProductMaster.ItemCode=Tr_TabStock.ItemCode ";
        //String query="select * from Mst_ProductMaster pm INNER JOIN Tr_TabStock s ON Mst_ProductMaster.ItemCode=Tr_TabStock.ItemCode ";
        /*SELECT doctors.doctor_id,doctors.doctor_name,visits.patient_name
        FROM doctors
        INNER JOIN visits
        ON doctors.doctor_id=visits.doctor_id



             table1 : columa1 columa2 columa3
            table2 : columa1 columb2 columb3

            select a.columa1 as it, a.columa2,a.columa3, b.columa1, b.columb2,b.columb3 from table1 a inner table2 b on a.columa1= b.columa1*/




        if(principle=="All" && brand=="All"){
            if(searchword!=""){
                query+= " where Description like'"+searchword+"%'";
            }
        }else if(principle=="All" && brand !="All"){
            query+= " where  Tr_TabStock.BrandID='"+brand+"' ";
        }else if(principle!="All" && brand=="All"){
            query+= " where Tr_TabStock.PrincipleID='"+principle+"' ";
        }else{
            query+= " where Tr_TabStock.BrandID='"+brand+"'  AND Tr_TabStock.PrincipleID='"+principle+"' ";
        }
        if(searchword!="" && !(principle=="All" && brand=="All") ){
            query+=" AND Description like'"+searchword+"%'";
        }
        //query="select * from Tr_TabStock";//WARNING !<<remover this after test>>

        //refresh the table;
        TableLayout table = (TableLayout)findViewById(R.id.table_stockview);
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
            Tr_TabStock product_sv= new Tr_TabStock();

            DBHelper db = new DBHelper(this);
            Cursor res = db.getData(query);//return tupple id=1;



            while (res.moveToNext()) {
                product_sv.setPrinciple(res.getString(res.getColumnIndex("PrincipleID")));//should be modified
                product_sv.setBrand(res.getString(res.getColumnIndex("BrandID")));//should be modified
                product_sv.setItemCode(res.getString(res.getColumnIndex("ItemCode")));//res.getColumnIndex("itemcode")
                product_sv.setDescription(res.getString(res.getColumnIndex("Description")));//should be modified
                product_sv.setBatchNumber(res.getString(res.getColumnIndex("BatchNumber")));
                product_sv.setExpireyDate(res.getString(res.getColumnIndex("ExpiryDate")));

                product_sv.setSellingPrice(res.getFloat(res.getColumnIndex("SellingPrice")) );
                product_sv.setRetailPrice(res.getFloat(res.getColumnIndex("RetailPrice")) );
                product_sv.setQuantity(res.getInt(res.getColumnIndex("Qty")));
                product_sv.setLastupadateDate(res.getString(res.getColumnIndex("LastUpdateDate")));

                update(product_sv);
                //btnView_sv.setText(res.getCount());
            }


            //return product;

        }catch (Exception e){
            //btnView_sv.setText(e.getMessage());
        }
    }
    //insert data to  table
    private void update(Tr_TabStock pm) {
        TableLayout table = (TableLayout)findViewById(R.id.table_stockview);



        /*add style to table row*/
        ContextThemeWrapper wrappedContext = new ContextThemeWrapper(this, R.style.tableStockView_row);

        //add row
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        //add coloum_item_code
        TextView tv_principle = new TextView(wrappedContext,null,0);
        tv_principle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_principle.setText(pm.getPrinciple());

        //add coloum_brand
        TextView tv_brand = new TextView(wrappedContext,null,0);
        tv_brand.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_brand.setText(pm.getBrand());
        //tv.setText("Entry-1");

        //add coloum_itemcode
        TextView tv_itemcode = new TextView(wrappedContext,null,0);
        tv_itemcode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_itemcode.setText(" "+pm.getItemCode());



        //add coloum_description
        TextView tv_description = new TextView(wrappedContext,null,0);
        tv_description.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_description.setText(" "+pm.getDescription());
        //tv_description.setWidth(0);

        //add coloum_batchnumber
        TextView tv_batchnumber = new TextView(wrappedContext,null,0);
        tv_batchnumber.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_batchnumber.setText(" "+pm.getBatchNumber());

        //add coloum_exp_date
        TextView tv_exp = new TextView(wrappedContext,null,0);
        tv_exp.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_exp.setText(" "+pm.getExpireyDate());

        //add coloum_sellingprice
        TextView tv_sellingprice = new TextView(wrappedContext,null,0);
        tv_sellingprice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_sellingprice.setText(" "+pm.getSellingPrice());

        //add coloum_sellingprice
        TextView tv_retailprice = new TextView(wrappedContext,null,0);
        tv_retailprice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_retailprice.setText(" " + pm.getRetailPrice());

        //add coloum_quantity
        TextView tv_qnt = new TextView(wrappedContext,null,0);
        tv_qnt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_qnt.setText(" "+pm.getQuantity());

        //add coloum_lastupdate_date
        TextView tv_lupdate = new TextView(wrappedContext,null,0);
        tv_lupdate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv_lupdate.setText(" "+pm.getLastupadateDate());




                //add coloums to row
        //tr.addView(tv_principle);
        //tr.addView(tv_brand);
        tr.addView(tv_itemcode);
        tr.addView(tv_description);
        tr.addView(tv_batchnumber);
        tr.addView(tv_exp);
        tr.addView(tv_sellingprice);
        tr.addView(tv_retailprice);
        tr.addView(tv_qnt);
        tr.addView(tv_lupdate);




        table.addView(tr);
    }
}
