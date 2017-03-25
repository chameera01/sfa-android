package com.example.ahmed.sfa.models;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.Activities.Login;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by DELL on 3/18/2017.
 */
public class JsonHelper {

    ArrayList<Object> jsonMyArray=new ArrayList<Object>();

    TextView result_view;
    private  boolean isLonding=true;
    private  boolean isRequesting=false;
    private  String filterType="deviceid_pass";
    private  String  activeStatus="no";
    private Context context;

    public JsonHelper(TextView tv){
        result_view=tv;
    }

    /*initial login y sending device Id n password to server*/
    public  String initialLoging(String devideId,String pass){

        final String[] recieveData = new String[1];

        new JsonDataCallback() {
            @Override
            public void receiveData(Object object) {
                String tmpData = (String)object;
                result_view.setText(tmpData);
                /*universal metho to filter Json Data from Json Array*/
                filterType="deviceid_pass";

                /*end unit methos*/
                try {
                    JSONArray jsonArray = new JSONArray(tmpData);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    recieveData[0] =jsonObject.optString("ACTIVESTATUS");
                    result_view.setText(jsonObject.optString("ACTIVESTATUS"));
                    setLonding(false);
                    filterJsonData(tmpData) ;
                    getMstProductData("devideId","pass");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute("http://www.bizmapexpert.com/api/DeviceCheck/DeviceCheckController?DeviceID="+devideId+"&Password="+pass+"",null,null);


         return recieveData[0];
    }
        /*
        * Get data from webservice on Mst_Product * */
        public  String getMstProductData(String devideId,String pass){

            final String[] recieveData = new String[1];

            new JsonDataCallback() {
                @Override
                public void receiveData(Object object) {
                    String tmpData = (String)object;
                    result_view.setText(tmpData);
                /*universal metho to filter Json Data from Json Array*/
                    filterType="ProductDetails";

                /*end unit methos*/
                    try {
                        /*JSONArray jsonArray = new JSONArray(tmpData);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        recieveData[0] =jsonObject.optString("ACTIVESTATUS");
                        result_view.setText(jsonObject.optString("ACTIVESTATUS"));
                        */
                        setLonding(false);
                        filterJsonData(tmpData) ;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }.execute("http://www.bizmapexpert.com/api/ProductDetails/SelectProductDetails?DeviceID=T1&RepID=93",null,null);


            return recieveData[0];
        }

        /**/
    public  void sendInitialData(String devideId,String pass){
        new HttpAsyncTask().execute("http://www.bizmapexpert.com/api/ProductBrandManagement/SelectProductBrandManagement?DeviceID=T1&RepID=93");
        filterType="salesProductBrand";
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

    private void insertToTable() {
        DBAdapter adptr=new DBAdapter(context);
        adptr.setMst_ProductBrandManagement();
        adptr.setMst_ProductMaster();

    }

    public   ArrayList<Object> getJsonMyArray(){

        return  jsonMyArray;
    }

    public String GET(String txtUrl){
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection httpUrlConnection;
        try{
            //create HttpClient

            URL url = null;
            try {
                url = new URL(txtUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = httpUrlConnection.getInputStream();

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
            Toast.makeText(null, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }catch (IOException ex){
            Toast.makeText(null, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    private void filterJsonData(String result) {
        try {
            switch (filterType){
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
                    String status=jsonObject.optString("ACTIVESTATUS");
                    activeStatus=status;
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
                         productMst.setGetSubBrand( jsonProductObject.optString("SubBrand"));
                         productMst.setUnitSize( jsonProductObject.optInt("UnitSize"));
                         productMst.setUnitName(  jsonProductObject.optString("UnitName"));
                         productMst.setRetailPrice(jsonProductObject.optDouble("RetailPrice"));
                         productMst.setBuyingPrice(jsonProductObject.optDouble("BuyingPrice"));
                         productMst.setActive( jsonProductObject.optInt("Active"));
                         productMst.setTargetAllow( jsonProductObject.optInt("TargetAllow"));

                        jsonMyArray.add(productMst);
                         //Toast.makeText(this.context,productMst.getBrand(),Toast.LENGTH_LONG).show();
                         result_view.setText(productMst.getBrand());
                    }
                    break;
                default:
                    break;
            }



        }catch (Exception e){
            e.printStackTrace();
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
    public abstract class JsonDataCallback extends AsyncTask<String, String, String> implements CallbackReceiver {
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

        public abstract void receiveData(Object object);
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