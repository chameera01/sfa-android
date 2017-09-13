package com.example.ahmed.sfa.Activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.adapters.InvoiceRecyclerAdapter;
import com.example.ahmed.sfa.controllers.adapters.NavigationDrawerMenuManager;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.models.Brand;
import com.example.ahmed.sfa.models.Principle;
import com.example.ahmed.sfa.models.SalesInvoiceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 9/8/2017.
 */

public class Invoice extends AppCompatActivity {
    private Spinner principleSpinner;
    private Spinner subBrandSpinner;

    private SearchView searchView;


    //for table
    private List<SalesInvoiceModel> invoiceModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private InvoiceRecyclerAdapter adapter;

    //To handle database operations
    DBAdapter dbAdapter;




    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_invoice);

        //assign views
        principleSpinner = (Spinner)findViewById(R.id.principleSpinner);
        subBrandSpinner = (Spinner)findViewById(R.id.subBrandSpinner);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContent((Principle) principleSpinner.getSelectedItem()
                        ,(Brand)subBrandSpinner.getSelectedItem(),newText);
                return true;
            }
        });

        //set navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationDrawerMenuManager(this);

        dbAdapter = new DBAdapter(this);
        dbAdapter.createTempTable();
        invoiceModelList = dbAdapter.getAllData();
        recyclerView = (RecyclerView)findViewById(R.id.invoiceRecycler);
        adapter = new InvoiceRecyclerAdapter(invoiceModelList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //intialize the spinnners
        initPrincipleSpinner();//inside this subrands spinner initializer will be called
    }


    private void initPrincipleSpinner(){
        principleSpinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Principle selectedPrinciple = (Principle) principleSpinner.getSelectedItem();
                initSubBrandsSpinner(selectedPrinciple.getId());
                // old model : searchWithoutQuery();
                filterContent((Principle) principleSpinner.getSelectedItem()
                        ,(Brand)subBrandSpinner.getSelectedItem(),searchView.getQuery ()+"");

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
                filterContent((Principle) principleSpinner.getSelectedItem()
                        ,(Brand)subBrandSpinner.getSelectedItem(),searchView.getQuery ()+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void filterContent(Principle principle,Brand subbrand,String product ){
        invoiceModelList.clear();
        invoiceModelList.addAll(dbAdapter.getAllData(principle.getId(),subbrand.getBrandID(),product));
        //invoiceModelList =

        adapter.notifyDataSetChanged();
    }


    class DBAdapter extends BaseDBAdapter {

        public DBAdapter(Context c) {
            super(c);
        }

        public void createTempTable(){
            openDB();
            String sql = "DROP TABLE IF EXISTS temp_invoice";
            db.execSQL(sql);//drops the temprary invoice table if it
            //exists already
            sql="CREATE TABLE temp_invoice(ServerID TEXT,ItemCode TEXT,Description TEXT,BatchNumber" +
                    " TEXT,ExpiryDate TEXT,SellingPrice REAL,Qty INTEGER" +
                    ",Shelf INTEGER DEFAULT 0,Request INTEGER DEFAULT 0,OrderQty INTEGER DEFAULT 0" +
                    ",Free INTEGER DEFAULT 0,Disc Real DEFAULT 0.0,LineVal Real DEFAULT 0.0,PrincipleID TEXT,BrandID TEXT);";
            db.execSQL(sql); //create the table again;

            //fill in data
            sql = "INSERT INTO temp_invoice(ServerID,ItemCode,Description,BatchNumber" +
                    ",ExpiryDate,SellingPrice,Qty,PrincipleID,BrandID) SELECT b.ServerID,a.ItemCode,a.Description," +
                    "b.BatchNumber,b.ExpiryDate,b.SellingPrice,b.Qty,a.PrincipleID,a.BrandID" +
                    " FROM Mst_ProductMaster a inner join Tr_TabStock b " +
                    "on a.ItemCode  = b.ItemCode";
            db.execSQL(sql);
            closeDB();
        }

        public void updateInvoiceData(SalesInvoiceModel model){
            openDB();

            String sql = "UPDATE temp_invoice SET" +
                    "Shelf="+model.getShelf()+" , Request="+model.getRequest()
                    +" , OrderQty="+model.getOrder()+" , Free="+model.getFree()
                    +" , Disc="+model.getDiscount()+" , LineVal="+model.getLineValue();
            db.execSQL(sql);

            closeDB();
        }

        public ArrayList<SalesInvoiceModel> getAllData(){
            ArrayList<SalesInvoiceModel> data = new ArrayList<>();
            openDB();
            //used before when we were directly took data inner joining tables
            //Cursor cursor = db.rawQuery("SELECT b.ServerID,a.ItemCode,a.Description,b.BatchNumber,b.ExpiryDate,b.SellingPrice,b.Qty" +
                    //" FROM Mst_ProductMaster a inner join Tr_TabStock b " +
                    //"on a.ItemCode  = b.ItemCode ;",null);
            String sql = "SELECT * from temp_invoice";
            Cursor cursor = db.rawQuery(sql,null);

            while(cursor.moveToNext()){
                SalesInvoiceModel salesInvoiceModel =new SalesInvoiceModel(cursor.getString(0),
                        cursor.getString(1),cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),cursor.getDouble(5),cursor.getInt(6)) ;
                salesInvoiceModel.setShelf(Integer.parseInt(cursor.getString(7)));
                salesInvoiceModel.setRequest(Integer.parseInt(cursor.getString(8)));
                salesInvoiceModel.setOrder(Integer.parseInt(cursor.getString(9)));
                salesInvoiceModel.setFree(Integer.parseInt(cursor.getString(10)));
                salesInvoiceModel.setDiscountRate(Integer.parseInt(cursor.getString(11)));
                salesInvoiceModel.setLineValue(Integer.parseInt(cursor.getString(12)));
                data.add(salesInvoiceModel);

            }
            closeDB();

            return data;
        }

        //
        public ArrayList<SalesInvoiceModel> getAllData(String principle,String subbrand,String product){
            ArrayList<SalesInvoiceModel> data = new ArrayList<>();
            openDB();

            /*String sql = "SELECT b.ServerID, a.ItemCode,a.Description,b.BatchNumber,b.ExpiryDate,b.SellingPrice,b.Qty" +
                    " FROM Mst_ProductMaster a inner join Tr_TabStock b " +
                    "on a.ItemCode = b.ItemCode WHERE ";
            used in older version*/
            String sql = "SELECT * from temp_invoice WHERE";

            if (!(principle.equals("ALL")|| principle == null) ){
                sql+=" PrincipleID ='"+principle+"'";
                //principle = "";
                if(!(subbrand.equals("ALL") || subbrand == null)){
                    sql+=" AND BrandID ='"+subbrand+"'";
                    //subbrand = "";
                }
                sql+=" AND ";
            }

            if(product.equals("ALL")|| product==null){
                product = "";

            }
            sql+=" Description Like '"+product+"%'";

            Cursor cursor = db.rawQuery(sql,null);

            while(cursor.moveToNext()){
                SalesInvoiceModel salesInvoiceModel =new SalesInvoiceModel(cursor.getString(0),
                        cursor.getString(1),cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),cursor.getDouble(5),cursor.getInt(6)) ;
                salesInvoiceModel.setShelf(Integer.parseInt(cursor.getString(7)));
                salesInvoiceModel.setRequest(Integer.parseInt(cursor.getString(8)));
                salesInvoiceModel.setOrder(Integer.parseInt(cursor.getString(9)));
                salesInvoiceModel.setFree(Integer.parseInt(cursor.getString(10)));
                salesInvoiceModel.setDiscountRate(Integer.parseInt(cursor.getString(11)));
                salesInvoiceModel.setLineValue(Integer.parseInt(cursor.getString(12)));
                data.add(salesInvoiceModel);
            }
            closeDB();

            return data;

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
