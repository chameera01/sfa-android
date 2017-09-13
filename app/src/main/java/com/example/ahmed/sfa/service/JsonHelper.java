package com.example.ahmed.sfa.service;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.Activities.InitialLogin;
import com.example.ahmed.sfa.Activities.Login;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.DeviceCheckController;
import com.example.ahmed.sfa.models.Mst_ProductMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by DELL on 3/18/2017.
 */
public class JsonHelper {

    ArrayList<Object> jsonMyArray=new ArrayList<Object>();

    TextView result_view;
    private  boolean isLonding=true;
    private  boolean isRequesting=false;
    //private  String filterType="deviceid_pass";
    private  String  activeStatus="no";
    static Context context;

    public JsonHelper(TextView tv){
        result_view=tv;

    }
    public JsonHelper(Context c, TextView txtv){
        context=c;
        result_view=txtv;
    }


    /*initial login y sending device Id n password to server*/
    public  DeviceCheckController initialLoging(final String deviceId, final String pass){

        final  DeviceCheckController  recieveData = new DeviceCheckController();

    new JsonDataCallback() {
        @Override
        public void receiveData(Object object) {
            String tmpData = (String) object;
            //result_view.setText(tmpData);
                /*universal metho to filter Json Data from Json Array*/
            ///filterType="deviceid_pass";

                /*end unit methos*/
            try {
                JSONArray jsonArray = new JSONArray(tmpData);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                    /*recieveData.setStatus(jsonObject.optString("ACTIVESTATUS"));
                    recieveData.setDevice_id(jsonObject.optString("DeviceID"));
                    recieveData.setPass(jsonObject.optString("Password"));*/
                DeviceCheckController devCheck = new DeviceCheckController();
                devCheck.setDevice_id(deviceId);
                devCheck.setPass(pass);
                devCheck.setStatus(jsonObject.optString("ACTIVESTATUS"));

                //Toast.makeText(context, jsonObject.toString() + "*", Toast.LENGTH_LONG).show();
                //result_view.setText(jsonObject.optString("ACTIVESTATUS"));

                DBAdapter adp = new DBAdapter(context);
                adp.insertDeviceCheckController(devCheck);


                if (jsonObject.optString("ACTIVESTATUS").equals("YES")) {
                    Toast.makeText(context, "Activated", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Device ID or password is incorrect. Please check and tryagain", Toast.LENGTH_SHORT).show();
                }
                setLonding(false);
                //filterJsonData(tmpData,"deviceid_pass") ;

                //getMstProductData("devideId","pass");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }.execute("http://www.bizmapexpert.com/api/DeviceCheck/DeviceCheckController?DeviceID=" + deviceId + "&Password=" + pass + "", null, null);



       /* DBAdapter adp=new DBAdapter(context);
        adp.insertDeviceCheckController(recieveData);

                    // launch login UI if Status is active
        if(recieveData.getStatus()=="YES"){
            Intent ui=new Intent(context,Login.class );
            context.startActivity(ui);

        }*/
         return recieveData;
    }













        /*
        * Get data from webservice on Mst_Product * */
        public  String getMstProductData(String devideId,String pass){

            final String[] recieveData = new String[1];

            new JsonDataCallback() {
                @Override
                public void receiveData(Object object) {
                    String tmpData = (String)object;
                    //result_view.setText("inside Method getMstProductData");
                /*universal metho to filter Json Data from Json Array*/
                    //filterType="ProductDetails";

                /*end unit methos*/
                    try {
                        /*JSONArray jsonArray = new JSONArray(tmpData);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        recieveData[0] =jsonObject.optString("ACTIVESTATUS");
                        result_view.setText(jsonObject.optString("ACTIVESTATUS"));
                        */
                        //result_view.setText("inside try catch");
                        setLonding(false);
                        filterJsonData(tmpData,"ProductDetails") ;
                    } catch (Exception e) {
                        e.printStackTrace();
                        //result_view.setText("exception on trycatch "+e.getMessage());
                    }

                }
            }.execute("http://www.bizmapexpert.com/api/ProductDetails/SelectProductDetails?DeviceID=T1&RepID=93",null,null);


            return recieveData[0];
        }


        /**/
    public  void sendInitialData(String devideId,String pass){
        HttpAsyncTask a = new HttpAsyncTask();

        new HttpAsyncTask().execute("http://www.bizmapexpert.com/api/ProductBrandManagement/SelectProductBrandManagement?DeviceID=T1&RepID=93");
       // filterType="salesProductBrand";
    }




    private class HttpAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls){
            setLonding(true);
            return GET(urls[0]);
        }

        //onPostExecute displays the result of the asyncTask
        @Override
        protected void onPostExecute(String result){
            setLonding(false);
            insertToTable();
            result_view.setText(jsonMyArray.get(0).toString());


        }
    }
/*---------------------------------------------------------------------------------------------
* syncronize method 2;
* **********************************************************************************************/
    /*
    private  class SyncReturn extends AsyncTask<JsonObjGenerate ,Void,Object>{

        //JsonObjGenerate jObj =new JsonObjGenerate();
        @Override
        protected Object doInBackground(JsonObjGenerate...jObj) {
            jObj[0].createConnection();
            return jObj[0];
        }
        @Override
        protected void onPostExecute(Object obj){
            //return  null;
        }
    }

    private class JsonObjGenerate {
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection httpUrlConnection;
        String link;

        public JsonObjGenerate(String webLink,Object mlistener){
            link=webLink;
        }
        //create HttpClient
       public void createConnection() {
           try {
               URL url = null;
               url = new URL(link);

               httpUrlConnection = (HttpURLConnection) url.openConnection();
               InputStream in = httpUrlConnection.getInputStream();

               if (in != null) {
                   result = convertInputStreamToString(in);

               } else {
                   result = "{did not work}";
               }
               httpUrlConnection.disconnect();
           }catch (Exception e){
               e.printStackTrace();
           }
        }

        public  String  getRequestListener(){

            return  result;
        }

    }*/
/*****************************************************************************************************************/






    private void insertToTable() {
       // DBAdapter adptr=new DBAdapter(context);
       // adptr.setMst_ProductBrandManagement();
       // adptr.setMst_ProductMaster();

    }

    public   ArrayList<Object> getJsonMyArray(){

        return  jsonMyArray;
    }

    public static String GET(String txtUrl){
        InputStream in = null;
        String result = "";
        HttpURLConnection httpUrlConnection = null;
        try{
            //create HttpClient

            URL url = null;
            try {
                url = new URL(txtUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                in = httpUrlConnection.getInputStream();
            }catch (Exception e){
                e.printStackTrace();
            }


            if(in!=null) {
                result = convertInputStreamToString(in);
                /**/
                JSONObject jsonRootObject = null;
                //filterJsonData(result) ;


                /**/

            }else{
                result="did not work";
            }
            httpUrlConnection.disconnect();
        }catch (MalformedURLException ex){
            //Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }catch (IOException ex){
           // Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }

        return result;
    }

    private  void filterJsonData(String result,String filter) {


        try {

            switch (filter){
                case "salesProductBrand":
                    JSONArray jsonArray = new JSONArray(result);

                    //Iterate the jsonArray and print the info of JSONObjects


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String principle = jsonObject.optString("Principle").toString();
                        String mainBrand = jsonObject.optString("MainBrand").toString();
                        jsonMyArray.add(mainBrand);
                    }

                    break;

                case   "deviceid_pass":

                    JSONArray jsonArray2 = new JSONArray(result);
                    JSONObject jsonObject = jsonArray2.getJSONObject(0);

                    DeviceCheckController devCheck= new DeviceCheckController();

                    devCheck.setDevice_id(jsonObject.optString("DeviceID"));
                    devCheck.setPass(jsonObject.optString("Password"));
                    devCheck.setStatus(jsonObject.optString("ACTIVESTATUS"));

                    Toast.makeText(context,"JsonHelper"+jsonObject.optString("DeviceID"),Toast.LENGTH_LONG).show();
                    DBAdapter adp=new DBAdapter(context);
                    adp.insertDeviceCheckController(devCheck);

                    break;

                case "ProductDetails":
                    Mst_ProductMaster productMst= new Mst_ProductMaster();

                    JSONArray jsonProductArray = new JSONArray(result);
                     for (int i = 0; i < jsonProductArray.length(); i++) {
                        JSONObject jsonProductObject = jsonProductArray.getJSONObject(i);

                         productMst.setItemCode( jsonProductObject.optString("ItemCode"));
                         productMst.setDescription(  jsonProductObject.optString("Description"));
                         productMst.setPrincipleId( jsonProductObject.optString("PrincipleID"));
                         productMst.setPrinciple( jsonProductObject.optString("Principle"));
                         productMst.setBrandId(  jsonProductObject.optString("BrandID"));
                         productMst.setBrand( jsonProductObject.optString("Brand"));
                         productMst.setSubBrandId( jsonProductObject.optString("SubBrandID"));
                         productMst.setSubBrand( jsonProductObject.optString("SubBrand"));
                         productMst.setUnitSize( jsonProductObject.optInt("UnitSize"));
                         productMst.setUnitName(  jsonProductObject.optString("UnitName"));
                         productMst.setRetailPrice(jsonProductObject.optDouble("RetailPrice"));
                         productMst.setBuyingPrice(jsonProductObject.optDouble("BuyingPrice"));
                         productMst.setActive( jsonProductObject.optInt("Active"));
                         productMst.setTargetAllow( jsonProductObject.optInt("TargetAllow"));

                         DBAdapter adptr=new DBAdapter(context);
                         adptr.setMst_ProductMaster(productMst);
                        //jsonMyArray.add(productMst);
                         //Toast.makeText(this.context,productMst.getBrand(),Toast.LENGTH_LONG).show();
                         result_view.setText(productMst.getBrand());
                    }
                    break;
                default:
                    break;
            }



        }catch (Exception e){
            e.printStackTrace();
            result_view.setText("ex:filter:"+e.getMessage());
        }
    }

    //
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine())!= null){
            result += line;
        }
        inputStream.close();
        return result;
    }
    //
    /*public void filterLoction(ArrayList<String> locationList) {


        JSONObject reader = null;
        ArrayList<String> list =new ArrayList();
        ArrayList<Location> loc_array=null;

        try {

            for (String myPoint : locationList) {

                reader = new JSONObject(myPoint);
                // JSONObject point = reader.getJSONObject("location");
                String latitude = reader.getString("latitude");
                String longitude = reader.getString("longitude");

                Location l=null;
                l.setLatitude(Double.parseDouble(latitude));
                l.setLongitude(Double.parseDouble(longitude));
                loc_array.add(l);

                list.add(latitude + "|" + longitude);

            }

            for(int i=0;i<loc_array.size();i++){
                float tmp= distanceReturn(((float)curentGPS.getLatitude()),((float)curentGPS.getLongitude()), (float)loc_array.get(i).getLatitude(),(float)loc_array.get(i).getLongitude());
                if(minimum_distance>tmp||i==0){
                    minimum_distance=tmp;
                }
            }

        }catch (JSONException e) {
            // outputText.setText(e.toString());

        }


    }*/

    public boolean getisLonding() {
        return isLonding;
    }
    public void setLonding(boolean londing) {
        isLonding = londing;
    }

    //abstract class to check dara return
    public static abstract class JsonDataCallback extends AsyncTask<String, String, String> implements CallbackReceiver {
        /*private ProgressDialog mProgressDialog;
        Handler handler;
        Runnable callback;
        Activity activity;*/


       /* public JsonDataCallback(Activity activity)
        {
            this.activity=activity;
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage("Loading Please Wait.");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
        }*/

        //public abstract void receiveData(Object object);
        @Override
        protected void onPreExecute() {
            //mProgressDialog =ProgressDialog.show(activity, "", "Please Wait",true,false);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
            // do stuff

        }


        @Override
        protected void onPostExecute(String jsonData) {
            /*if (mProgressDialog != null || mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }*/
            if(jsonData!=null)
            {
                receiveData(jsonData);
            }
        }
    }
}