package com.example.ahmed.sfa;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private ListView lv;
    private SearchView sv;

    CustomAdapter adapter;
    ArrayList<Route> routes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lv = (ListView)findViewById(R.id.routelist);
        sv = (SearchView)findViewById(R.id.routesearch);

        adapter = new CustomAdapter(this,routes);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getRoutes(newText);
                return false;
            }
        });

        lv.setAdapter(adapter);

    }

    private void getRoutes(String searchTerm){
        routes.clear();

        DBAdapter db = new DBAdapter(this);
        db.openDB();
        Route route ;
        Cursor cursor = db.retrieve(searchTerm);
        String count = cursor.getCount()+"";
        Toast.makeText(getApplicationContext(),count,Toast.LENGTH_SHORT).show();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String customer = cursor.getString(1);
            String town = cursor.getString(2);

            route = new Route(id,customer,town);

            routes.add(route);
        }

        db.closeDB();
        lv.setAdapter(adapter);
    }

    //this class handles the creation and upgrade functionality
    class DBHelper extends SQLiteOpenHelper{
        private static final String DB_NAME = "sfa.db";
        private static final int VERSION = 1;

        public DBHelper (Context ctxt){
            super(ctxt,DB_NAME,null,VERSION);//error handler shld be implemented
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            try{

                db.execSQL("CREATE TABLE route (_id INTEGER PRIMARY KEY AUTOINCREMENT, customer TEXT,town TEXT);");
                db.execSQL("INSERT INTO route VALUES (1,'a','z');");
                db.execSQL("INSERT INTO route VALUES (2,'b','a');");
                db.execSQL("INSERT INTO route VALUES (3,'c','z');");
                db.execSQL("INSERT INTO route VALUES (4,'d','a');");
                db.execSQL("INSERT INTO route VALUES (5,'e','z');");
                db.execSQL("INSERT INTO route VALUES (6,'f','a');");

            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
            //no second schema yet so left empty
        }

    }

    //this class handles the database operations
    class DBAdapter {
        Context c;
        SQLiteDatabase db;
        DBHelper helper;

        public DBAdapter(Context c){
            this.c = c;
            helper = new DBHelper(c);
        }

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
        }

        //insert method have to be implemented later
        //db.insert(TABLE_NAME,ROW,CONTENT_VALUES)


        public Cursor retrieve(String searchTerm){
            String[] columns = {"_id","customer","town"};// have to be changed to a constant
            Cursor c;

            if(searchTerm != null && searchTerm.length()>0){
                String sql = "SELECT * FROM route WHERE customer = '"+searchTerm+"';";
                Log.w("Executing sql",sql);
                c = db.rawQuery(sql,null);
                return c;
            }

            c=db.rawQuery("SELECT * FROM route",null);
            return c;
        }


    }

    //to bind and create new view for the filtered list we use this class
    class CustomAdapter extends BaseAdapter{
        Context c;
        ArrayList<Route> routes;
        LayoutInflater inflater;

        public CustomAdapter(Context c, ArrayList<Route> routes){
            this.c=c;
            this.routes=routes;
        }
        @Override
        public long getItemId(int a){
            return a;
        }

        @Override
        public int getCount(){
            return routes.size();
        }

        public Object getItem(int position){
            return routes.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(inflater==null){
                inflater = (LayoutInflater)getLayoutInflater();

            }
            if(convertView==null){
                convertView = inflater.inflate(R.layout.homesearchresultrow,parent,false);
            }

            TextView customer = (TextView)convertView.findViewById(R.id.customer);
            TextView town = (TextView)convertView.findViewById(R.id.town);

            customer.setText(routes.get(position).getCustomer());
            town.setText(routes.get(position).getTown());

            final int pos = position;

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"Item Clicked",Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }


    }

    public class Route{
        private int id;
        private String customer;
        private String town;

        public Route(int id,String customer,String town){
            this.id = id;
            this.customer = customer;
            this.town = town;
        }

        public int getId(){
            return id;

        }

        public String getCustomer(){
            return customer;
        }

        public String getTown(){
            return town;
        }
    }
}
