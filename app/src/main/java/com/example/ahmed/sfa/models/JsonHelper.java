package com.example.ahmed.sfa.models;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by DELL on 3/18/2017.
 */
public class JsonHelper {

    ArrayList<String> jsonMyArray=new ArrayList<String>();

    TextView result_view;
    private  boolean isLonding=true;
    private  boolean isRequesting=false;
    private  String filterType="deviceid_pass";
    private  String  activeStatus="no";

    public JsonHelper(TextView tv){
        result_view=tv;
    }

    public  String initialLoging(String devideId,String pass){

        final String[] recieveData = new String[1];
        new JsonDataCallback() {
            @Override
            public void receiveData(Object object) {
                String tmpData = (String)object;
                result_view.setText(tmpData);

                try {
                    JSONArray jsonArray = new JSONArray(tmpData);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    recieveData[0] =jsonObject.optString("ACTIVESTATUS");
                    result_view.setText(jsonObject.optString("ACTIVESTATUS"));
                    setLonding(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute("http://www.bizmapexpert.com/api/DeviceCheck/DeviceCheckController?DeviceID="+devideId+"&Password="+pass+"",null,null);
        filterType="deviceid_pass";






        return recieveData[0];
    }





    public  void sendInitialData(String devideId,String pass){


        new HttpAsyncTask().execute("http://www.bizmapexpert.com/api/ProductBrandManagement/SelectProductBrandManagement?DeviceID=T1&RepID=93");
        filterType="salesProductBrand";
    }

    public boolean getisLonding() {
        return isLonding;
    }

    public void setLonding(boolean londing) {
        isLonding = londing;
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
            result_view.setText(jsonMyArray.get(0));


        }
    }
    public   ArrayList<String> getJsonMyArray(){

        return  jsonMyArray;
    }

    public /*static*/ String GET(String txtUrl){
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
                filterJsonData(result) ;


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

            if(filterType=="salesProductBrand") {
                JSONArray jsonArray = new JSONArray(result);

                //Iterate the jsonArray and print the info of JSONObjects


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String principle = jsonObject.optString("Principle").toString();
                    String mainBrand = jsonObject.optString("MainBrand").toString();
                    jsonMyArray.add(mainBrand);
                }
            }else if(filterType=="deviceid_pass"){
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String status=jsonObject.optString("ACTIVESTATUS");
                activeStatus=status;
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
    public  class Mst_RepTable{

        private  String repId;
        private String deviceName;
        private  String repName;
        private  String address;
        private  String contactNo;
        private  String dealerName;
        private String dealerAdress;
        private String macAdress;
        private int isActive;
        private String lastUpdateDae;

        public String getRepId() {
            return repId;
        }

        public void setRepId(String repId) {
            this.repId = repId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getRepName() {
            return repName;
        }

        public void setRepName(String repName) {
            this.repName = repName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getDealerName() {
            return dealerName;
        }

        public void setDealerName(String dealerName) {
            this.dealerName = dealerName;
        }

        public String getDealerAdress() {
            return dealerAdress;
        }

        public void setDealerAdress(String dealerAdress) {
            this.dealerAdress = dealerAdress;
        }

        public String getMacAdress() {
            return macAdress;
        }

        public void setMacAdress(String macAdress) {
            this.macAdress = macAdress;
        }

        public int getIsActive() {
            return isActive;
        }

        public void setIsActive(int isActive) {
            this.isActive = isActive;
        }

        public String getLastUpdateDae() {
            return lastUpdateDae;
        }

        public void setLastUpdateDae(String lastUpdateDae) {
            this.lastUpdateDae = lastUpdateDae;
        }
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