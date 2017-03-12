package com.example.ahmed.sfa.activities;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.adapters.ItineraryAdapter;
import com.example.ahmed.sfa.controllers.adapters.NavigationDrawerMenuManager;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.Itinerary;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private ListView listView;//the list view element on the home ui to list the itineraries
    private SearchView searchView;//the search view element on the home ui
    private int selectedIndex;
    ItineraryAdapter adapter;
    ArrayList<Itinerary> itineraries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


         NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
         NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationDrawerMenuManager(this);
         //navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);



        selectedIndex=-1;
        listView = (ListView)findViewById(R.id.routelist);
        searchView = (SearchView)findViewById(R.id.routesearch);

        adapter = new ItineraryAdapter(this, itineraries);//create the Itinerary adapter passing this context as a parameter and itineraries array
        //so that adapter can retrieve views according to the array

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showCustomerDetails(itineraries.get(position).getCustomerNo());
                selectedIndex = position;
                adapter.notifyDataSetChanged();//unless adaper notified about a false data changed
                //message it wont refresh the views
                Toast.makeText(getApplicationContext(),"Item Clicked :"+position,Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getItinerary(query,true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getItinerary(newText,false);
                return false;
            }
        });
        getItinerary(null,false);//initially passing the search term as null so the getItineraries method will return all possible itineraries
        //listView.setAdapter(adapter);//set the adapter to the list view

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

    public int getSelectedIndex(){
        return selectedIndex;
    }

    private void getItinerary(String searchTerm,boolean matchOnlyStart){
        itineraries.clear();//clear the array so that results will be start from the beginning without appending

        DBAdapter db = new DBAdapter(this);//create the DB Adapter to open and close the db
        //db.openDB();

        Itinerary itinerary;

        /**to standardize the format of the date im using a seperate class to manage dates
         * Calendar cal  = Calendar.getInstance();
        String date="1993/12/8"; //intializing a value that doesnt exist to avoid null values
        String year = ""+cal.get(Calendar.YEAR);
        String month = ""+(cal.get(Calendar.MONTH)+1);
        String day = ""+cal.get(Calendar.DAY_OF_MONTH);
        date = year+"/"+month+"/"+day;*/
        String date = DateManager.dateToday();
        Log.w(">Current Date :",date);
        //Cursor cursor = db.retrieveItinerary(searchTerm,date,matchOnlyStart);

        db.retrieveItinerary(searchTerm,date,matchOnlyStart);

        //these two lines werer used for testing purposes
        //String count = cursor.getCount()+"";
        //Toast.makeText(getApplicationContext(),count,Toast.LENGTH_SHORT).show();

        /**create an Itinerary object for all the results in the cursor and add them to the array "itineraries"
        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String customerNo = cursor.getString(1);
            String customer = cursor.getString(2);
            String town = cursor.getString(3);
            int isInvoiced = cursor.getInt(4);

            itinerary = new Itinerary(id,customerNo,customer,town,isInvoiced);

            itineraries.add(itinerary);
        }*/

        //db.closeDB();//close the Database
        listView.setAdapter(adapter);//again set the adapter to the listview to reflect the changes made
    }

    public void showCustomerDetails(String customerNo){
        CustomerPopupFragment customerPopupFragment = (CustomerPopupFragment)getFragmentManager().findFragmentById(R.id.customermanagementfragment);
        //check whether fragment has to be replaced
        if(customerPopupFragment==null || !customerPopupFragment.getCurrentCustomer().equals(customerNo)){
            //make new fragment to replace
            customerPopupFragment = CustomerPopupFragment.newInstance(customerNo);
            Log.w("Fragment proble","here 0");
            //execute a transaction, replacing any existing fragment
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.customermanagementfragment,customerPopupFragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
    }



    //this class handles the database operations
    class DBAdapter extends BaseDBAdapter{

        public DBAdapter(Context c){
            super(c);
        }

        /**
        //open DB
        public void openDB(){
            try{
                db = helper.getWritableDatabase();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        public void closeDB(){
            try{
                helper.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }*/

        //insert method have to be implemented later
        //db.insert(TABLE_NAME,ROW,CONTENT_VALUES)


        public Cursor retrieveItinerary(String searchTerm,String date,boolean matchOnlyTheStart){
            //public Cursor retrieveItinerary(String searchTerm){ //making date as a parameter
            //String date="2017/02/03";
            //String[] columns = {"_id","customer","town"};// have to be changed to a constant
            openDB();
            Cursor cursor;

            String sql= "SELECT b.ItineraryID, a.CustomerNo,a.CustomerName,a.Town,b.IsInvoiced FROM Mst_Customermaster a "+
                    "inner join Tr_ItineraryDetails b on a.CustomerNo=b.CustomerNo WHERE"+
                    " b.ItineraryDate ='"+date+"'";
            if(searchTerm != null && searchTerm.length()>0){

                if(matchOnlyTheStart){
                    sql += " AND a.CustomerName LIKE '"+searchTerm+"%' ;";
                }else{
                    sql +=" AND a.CustomerName LIKE '%"+searchTerm+"%' ;";
                }

                Log.w("Executing sql",sql);
                cursor = db.rawQuery(sql,null);
                //return cursor;
            }
            sql += ";";
            cursor=db.rawQuery(sql,null);
            Log.w("Executing sql",sql);

            Itinerary itinerary;
            ArrayList<Itinerary> results = new ArrayList<>();
            while (cursor.moveToNext()){
                String id = cursor.getString(0);
                String customerNo = cursor.getString(1);
                String customer = cursor.getString(2);
                String town = cursor.getString(3);
                int isInvoiced = cursor.getInt(4);

                itinerary = new Itinerary(id,customerNo,customer,town,isInvoiced);

                itineraries.add(itinerary);
            }
            Log.w("Count result",itineraries.size()+"");
            closeDB();
            return cursor;
        }


    }




}
