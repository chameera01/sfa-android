package com.example.ahmed.sfa.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.Volley.VolleyLog;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.adapters.NavigationDrawerMenuManager;
import com.example.ahmed.sfa.models.Mst_Customermaster;
import com.example.ahmed.sfa.service.CallbackReceiver;
import com.example.ahmed.sfa.service.HttpGetRequest;
import com.example.ahmed.sfa.service.HttpGetRequestAbstract;
import com.example.ahmed.sfa.service.JsonFilter_Send;
import com.example.ahmed.sfa.service.JsonHelper;
import com.example.ahmed.sfa.service.JsonObjGenerate;
import com.example.ahmed.sfa.service.JsonRequestListerner;
import com.example.ahmed.sfa.service.SendDeviceDetails;
import com.example.ahmed.sfa.service.SyncReturn;
import com.example.ahmed.sfa.service.UploadTables;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.R.attr.name;
import static android.R.attr.resource;
import static android.R.attr.type;
import static com.example.ahmed.sfa.R.id.address;
import static com.example.ahmed.sfa.R.id.textView;


public class ManualSync extends AppCompatActivity implements JsonRequestListerner {
    ImageView ivProductSync;
    ImageView ivrepSync;
    ImageView ivSupplierSync;
    ImageView ivProductBradnSync;
    ImageView ivCustomerStatusSync;
    ImageView ivDistrictSync;
    ImageView ivTerritorySync;
    ImageView ivRouteSync;
    ImageView ivReasonSync;
    ImageView ivchechInOutPointsSync;
    ImageView ivCustomerSync;
    ImageView ivTabStockSync;
    String deviecId ="t1";
    String repId="93";
    ImageView ivItineraryDeatildSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_sync);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//fixed landscape screan;

        ivProductSync=(ImageView) findViewById(R.id.iv_productSync);
        ivrepSync=(ImageView) findViewById(R.id.iv_repSync);
        ivSupplierSync=(ImageView) findViewById(R.id.iv_SupplierSync);
        ivProductBradnSync=(ImageView) findViewById(R.id.iv_ProductBradnSync);
        ivCustomerStatusSync=(ImageView) findViewById(R.id.iv_CustomerStatusSync);
        ivDistrictSync=(ImageView) findViewById (R.id.iv_DistrictSync);
        ivTerritorySync=(ImageView) findViewById (R.id.iv_TerritorySync);
        ivRouteSync=(ImageView) findViewById (R.id.iv_RouteSync);
        ivReasonSync=(ImageView) findViewById (R.id.iv_ReasonSync);
        ivchechInOutPointsSync=(ImageView) findViewById (R.id.iv_chechInOutPointsSync);
        ivCustomerSync=(ImageView) findViewById (R.id.iv_CustomerSync);
        ivTabStockSync=(ImageView) findViewById (R.id.iv_StockSync);
        ivItineraryDeatildSync = (ImageView) findViewById(R.id.iv_ItinerarySync);


        setListeners();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationDrawerMenuManager(this);
        getRepAndDeviceId();
    }

    public void getRepAndDeviceId(){
        try {
        DBAdapter dbAdapter = new DBAdapter(this);
        Cursor deviceCursor = dbAdapter.runQuery("select * from DeviceCheckController where ACTIVESTATUS = 'YES'");
        Cursor repCursor = null;
        deviceCursor.moveToFirst();





            deviecId = repCursor.getString(repCursor.getColumnIndex("DeviceID"));
            repCursor = dbAdapter.runQuery("select * from Mst_RepTable");
            repCursor.moveToFirst();
            repId = repCursor.getString(repCursor.getColumnIndex("RepID"));
        }catch (Exception e){
            Log.e("data:",e.getMessage());
        }

    }
    public  void setListeners(){
        final LinearLayout app_layer = (LinearLayout) findViewById (R.id.product_layout);
        app_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManualSync.this,AndroidDatabaseManager.class);
                //Intent intent = new Intent(activity, AndroidDatabaseManager.class);
                ManualSync.this.startActivity(intent);
                Toast.makeText(ManualSync.this, "listenerSet", Toast.LENGTH_LONG).show();
            }
        });

        /*app_layer.setOnHoverListener( new View.OnHoverListener(){

            @Override
            public boolean onHover(View v, MotionEvent event) {
                app_layer.setBackgroundColor(241);
                return false;
            }
        });*/


        ivProductSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"clckedSync",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/DIstributorManagementSystem/productdetails/SelectProductDetails?DeviceID=T1&RepID=93",ManualSync.this);
                    jObjGen.setFilterType("productdetails");
                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });
        /**SynceRep**/
        ivrepSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"clckedSync",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/DIstributorManagementSystem/GetRepDetails/SelectGetRepDetails?DeviceID=T1&RepID=93",ManualSync.this);
                    jObjGen.setFilterType("RepDetails");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });
        /**Syncronize Supplier Details**/
        ivSupplierSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"clckedSync",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/DIstributorManagementSystem/Mst_SupplierTable/SelectProductMst_SupplierTable?DeviceID=T1&RepID=93",ManualSync.this);
                    jObjGen.setFilterType("SupplierTable");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });

        /*Syncronize Product Brand Details*/
        ivProductBradnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"clckedSync",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/DIstributorManagementSystem/ProductBrandManagement/SelectProductBrandManagement?DeviceID=T1&RepID=93",ManualSync.this);
                    jObjGen.setFilterType("ProductBrandManagement");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });

        /*Syncronize CustomerStatus*/
        ivCustomerStatusSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"clckedSync",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/DIstributorManagementSystem/Mst_CustomerStatus/SelectMst_CustomerStatus?DeviceID=T1&RepID=93",ManualSync.this);
                    jObjGen.setFilterType("CustomerStatus");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });
        //Sync Distric Details
        ivDistrictSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"Connecting...",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/DIstributorManagementSystem/Mst_District/SelectMst_District?DeviceID=T1&RepID=93",ManualSync.this);
                    jObjGen.setFilterType("district");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });
        /*sunc territory*/
        ivTerritorySync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"Connecting...",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/DIstributorManagementSystem/Mst_Territory/SelectMst_Territory?DeviceID=T1&RepID=93",ManualSync.this);
                    jObjGen.setFilterType("territory");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });
        /*sunc iv_RouteSync*/
        ivRouteSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"Connecting...",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/DIstributorManagementSystem/Mst_Route/SelectMst_Route?DeviceID=T1&RepID=93",ManualSync.this);
                    jObjGen.setFilterType("route");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });
        /*sunc iv_Reasonync*/
        ivReasonSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"Connecting...",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/DIstributorManagementSystem/Mst_Reasons/SelectMst_Reasons?DeviceID="+deviecId+"&RepID="+repId+"",ManualSync.this);
                    jObjGen.setFilterType("Reason");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });
        /*sunc chechInOutPoints*/
        ivchechInOutPointsSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"Connecting...",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/DIstributorManagementSystem/Mst_CheckInOutPoints/SelectMst_CheckInOutPoints?DeviceID=T1&RepID=93",ManualSync.this);
                    jObjGen.setFilterType("CheckInOutPoints");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });
        /*sunc Customer*/
        ivCustomerSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"Connecting...",Toast.LENGTH_LONG).show();

                //uploading data
                UploadTables uplwd = new UploadTables(ManualSync.this);
                ArrayList<String> cusArr =  uplwd.mstCustomerMaster();
                Toast.makeText(ManualSync.this, "size"+cusArr.get(0), Toast.LENGTH_SHORT).show();

               // String tmpGenUrl = cusArr.get(0);
                try {
                    for (final String url : cusArr) {
                       // Toast.makeText(ManualSync.this, "URL:"+url, Toast.LENGTH_SHORT).show();
                        //new HttpGetRequest(ManualSync.this).execute(url);
                        //////////////////////////////////////////////////////
                       /* new JsonHelper.JsonDataCallback() {
                            @Override
                            public void receiveData(Object object) {
                                String tmpData = (String) object;
                                Toast.makeText(ManualSync.this, "Result:"+tmpData, Toast.LENGTH_SHORT).show();
                                try {
                                        JSONArray jsonArray = new JSONArray(tmpData);
//                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                                    jsonObject.optString("ACTIVESTATUS");
//
//                                    if (jsonObject.optString("ACTIVESTATUS").equals("SUCCESS")) {
//                                        Toast.makeText(ManualSync.this, "Activated", Toast.LENGTH_SHORT).show();
//                                    } else {
//
//                                    }

                                } catch (JSONException e) {
                                    if(tmpData=="did not work"){

                                    }
                                    e.printStackTrace();
                                }

                            }
                        }.execute(cusArr.get(0), null, null);*/
                        //////////////////////////////////////////////////////
                        //method 2
                       // new HttpGetRequest(ManualSync.this).execute("http://www.bizmapexpert.com/DIstributorManagementSystem/Up_Tr_CustomerMaster_Pending/Tr_CustomerMaster_PendingNew?RepID=99&TabCustomerNo=382&CustomerName=CASHCUSTOMER&Address=NO221,GALLEROAD,MOUNTLAVINIA&DistrictID=0&Area=MOUNT%20LAVINIA&Town=MOUNT%20LAVINIA&RouteID=1391&CustomerStatusID=456&Telephone=0112721015&Fax=111&Email=f&BRno=r&OwnerName=66&OwnerContactNo=u&PhamacyRegNo=yy&Latitude=0.0&longitude=0.0&ImageID=default");

                        //////////////////////////////////////////////////////////

                    //method 3
                    new UploadWithCallback(){

                        //HashMap<String, String> map = new HashMap<String, String>();//put in it
                        //map.put(stts, filename[i]);
                        String urlToSplit =url ;

                        String cusNo =    urlToSplit.split("TabCustomerNo=")[1].split("&CustomerName")[0];
                        @Override
                        public void receiveData(Object result) {
                            Toast.makeText(ManualSync.this, "Ststus"+url, Toast.LENGTH_SHORT).show();
                            String response = (String) result;
                            Toast.makeText(ManualSync.this, "Response_callback:"+response, Toast.LENGTH_SHORT).show();
                            try {
                                if(response!=null) {
                                    JSONArray responseArr = new JSONArray(response);
                                    JSONObject respondObj=responseArr.getJSONObject(0);
                                    String stts = respondObj.getString("Status");
                                    if(stts.equals("Success")){
                                        DBAdapter adp = new DBAdapter(ManualSync.this);
                                        adp.updateCustomerUploadStatus(cusNo);

                                    }else{
                                        Toast.makeText(ManualSync.this, "Ststus"+stts, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute(url);
                    }
                }catch (Exception e){
                    Toast.makeText(ManualSync.this, "ArrayList:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }



               //download customer master data
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/DIstributorManagementSystem/Mst_Customermaster/SelectMst_Customermaster?DeviceID=T1&RepID=93",ManualSync.this);
                    jObjGen.setFilterType("Customer");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                   // e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }

            }
        });
        /*sunc tabStock*/
        ivTabStockSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"Connecting...",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/DIstributorManagementSystem/D_Tr_RepStock/GetRepStock?DeviceID=T1&RepID=99",ManualSync.this);
                    jObjGen.setFilterType("TabStock");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });

        //download ItinerayDetails
        ivItineraryDeatildSync.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                try{
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/DIstributorManagementSystem/Tr_ItineraryDetails/GetItineraryDetails?DeviceID=T1&RepID=99",ManualSync.this);
                    jObjGen.setFilterType("ItineraryDetails");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    Toast.makeText(ManualSync.this, "ItineraryDeatils Download:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void receiveData(String result,String filter) {
        Toast.makeText(this, "came inside recieve data", Toast.LENGTH_LONG).show();
        if(result!=null){
            String josnString=result;
            Toast.makeText(this, "result:" + josnString, Toast.LENGTH_LONG).show();
            try{
                JsonFilter_Send josnFilter= new JsonFilter_Send(ManualSync.this.getApplicationContext());
                josnFilter.filterJsonData(josnString,filter);

            }catch (Exception e) {
                Toast.makeText(this,"RecieveData:"+ e.getMessage(),Toast.LENGTH_LONG ).show();
            }
        }else{
            Toast.makeText(this,"is nulllll",Toast.LENGTH_LONG ).show();
        }
        Toast.makeText(ManualSync.this,"method_complete",Toast.LENGTH_LONG).show();
    }

    public void send_to_web() {

    }

    public void josnPost(String weblink){
        try {
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("Title", "VolleyApp Android Demo");
            jsonBody.put("Author", "BNK");
            jsonBody.put("Date", "2015/08/26");
            final String requestBody = jsonBody.toString();

            //Toast.makeText(this, "link:"+postlink, Toast.LENGTH_SHORT).show();
            String url=weblink;//"https://httpbin.org/post";

            StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //textView.setText(response);
                    Log.i("response",response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error:",error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("name", jsonBody.toString());
                    return params;
                }
                @Override
                public String getBodyContentType() {
                    return String.format("application/json; charset=utf-8");
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        com.android.volley.VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                requestBody, "utf-8");
                        return null;
                    }
                }
            };
            // MySingleton.getInstance(this).addToRequestQueue(stringRequest);
//        Volley.addToRequestQueue(stringRequest);
            Volley.newRequestQueue(this).add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
/////////////////////////////////////////////////////////////////////////////////upload private class
private static abstract  class  UploadWithCallback extends AsyncTask<String,String,String> implements CallbackReceiver {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;



    @Override
    protected String doInBackground(String... params){
        String stringUrl = params[0];
        String result;
        String inputLine;
        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            //Connect to our url
            connection.connect();
            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
        }
        catch(IOException e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }


    protected void onPostExecute(String result){
        super.onPostExecute(result);
        if(result!=null)
        {
            receiveData(result);

        }else{
            receiveData("{no data}");
        }

        //Toast.makeText(ManualSync.this, "response:"+result, Toast.LENGTH_SHORT).show();

    }
}
////////////////////////////////////////////////////////////////////////////

}
