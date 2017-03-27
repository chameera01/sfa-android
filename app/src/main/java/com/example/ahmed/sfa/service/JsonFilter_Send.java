package com.example.ahmed.sfa.service;

import android.content.Context;
import android.widget.Toast;

import com.example.ahmed.sfa.Activities.ServiceTest;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.models.Mst_ProductMaster;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by DELL on 3/27/2017.
 */

public class JsonFilter_Send {
    private  Context context;
    public  JsonFilter_Send(Context context){
        context=context;
    }

    public   void filterJsonData(String result,String filter) {


        try {

            switch (filter){
                case "salesProductBrand":
                    JSONArray jsonArray = new JSONArray(result);

                    //Iterate the jsonArray and print the info of JSONObjects


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String principle = jsonObject.optString("Principle").toString();
                        String mainBrand = jsonObject.optString("MainBrand").toString();
                        //jsonMyArray.add(mainBrand);
                    }

                    break;

                case   "deviceid_pass":

                    JSONArray jsonArray2 = new JSONArray(result);
                    JSONObject jsonObject = jsonArray2.getJSONObject(0);
                    String status=jsonObject.optString("ACTIVESTATUS");
                    //activeStatus=status;
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
                       // result_view.setText(productMst.getBrand());
                    }
                    break;
                default:
                    break;
            }



        }catch (Exception e){
            e.printStackTrace();
           Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
