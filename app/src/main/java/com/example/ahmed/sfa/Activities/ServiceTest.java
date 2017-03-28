package com.example.ahmed.sfa.Activities;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.service.JsonFilter_Send;
import com.example.ahmed.sfa.service.JsonHelper;
import com.example.ahmed.sfa.service.JsonObjGenerate;
import com.example.ahmed.sfa.service.JsonRequestListerner;
import com.example.ahmed.sfa.service.SyncReturn;


public class ServiceTest extends AppCompatActivity implements JsonRequestListerner {
TextView result_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);
        result_view=(TextView) findViewById(R.id.tv_service_result);
        runJsonCLass();
    }

    public void runJsonCLass(){
        /*try {
            JsonHelper jh = new JsonHelper(result_view);
            jh.sendInitialData("deviceid", "password");

        }catch (Exception e){
            Toast.makeText(this,"Error_found:"+e.getMessage(),Toast.LENGTH_LONG).show();
        }*/

        try {
            JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/ProductDetails/SelectProductDetails?DeviceID=T1&RepID=93", this);
            SyncReturn io = new SyncReturn();
            io.execute(jObjGen);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
/**/
/**********************/
    @Override
    public void receiveData(Object result) {
        if(result!=null){
            String josnString=result.toString();
            Toast.makeText(this, "result" + josnString, Toast.LENGTH_LONG).show();
            try{
                JsonFilter_Send josnFilter= new JsonFilter_Send(ServiceTest.this.getApplicationContext());
                josnFilter.filterJsonData(josnString,"ProductDetails");
            }catch (Exception e) {
                Toast.makeText(this,"RecieveData:"+ e.getMessage(),Toast.LENGTH_LONG ).show();
            }
        }else{
            Toast.makeText(this,"is nulllll",Toast.LENGTH_LONG ).show();
        }
        /**
        String josnString=result.toString();
        try {
            Toast.makeText(this, "result" + josnString, Toast.LENGTH_LONG).show();
        }catch (Exception t) {
            Toast.makeText(this,"toast:"+ t.getMessage(),Toast.LENGTH_LONG ).show();
        }
        try{
            JsonFilter_Send josnFilter= new JsonFilter_Send(ServiceTest.this.getApplicationContext());
           // josnFilter.filterJsonData(josnString,"ProductDetails");
        }catch (Exception e) {
            Toast.makeText(this,"RecieveData:"+ e.getMessage(),Toast.LENGTH_LONG ).show();
        }*/


    }
/*
    public  void volleyJsonArrayRequest(){
        String url = "http://www.bizmapexpert.com/api/ProductBrandManagement/SelectProductBrandManagement?DeviceID=T1&RepID=93";

        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // the response is already constructed as a JSONObject!
                        int i=0;
                        String tmp="";

                        try {
                            //response = response.getJSONObject("args");
                            //String site = response.getString("site"),
                            // network = response.getString("network");
                            //System.out.println("Site: "+site+"\nNetwork: "+network);
                            for(i=0;i<response.length();i++ ) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String  repId = jsonObject.optString("Principle").toString();
                                String deviceName = jsonObject.optString("MainBrand").toString();
                                tmp+="repId: "+repId+"\ndeviceName: "+deviceName+"\n";
                            }

                            text.setText(tmp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            text.setText("error:"+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);
    }*/


}
