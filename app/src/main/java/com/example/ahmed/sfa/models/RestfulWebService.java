package com.example.ahmed.sfa.models;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by DELL on 3/20/2017.
 */

public class RestfulWebService {
    Context context;
    String result;

    public RestfulWebService(Context context){
        this.context = context;

    }

    public  String volleyJsonArrayRequest(){
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
                            result=tmp;

                            //text.setText(tmp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //text.setText("error:"+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });


        Volley.newRequestQueue(context).add(jsonRequest);
        return result;
    }
}
