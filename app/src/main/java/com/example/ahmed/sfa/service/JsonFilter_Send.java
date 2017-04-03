package com.example.ahmed.sfa.service;

import android.content.Context;
import android.widget.Toast;

import com.example.ahmed.sfa.Activities.ServiceTest;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.models.Mst_ProductMaster;
import com.example.ahmed.sfa.models.Mst_RepTable;
import com.example.ahmed.sfa.models.Mst_SupplierTable;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by DELL on 3/27/2017.
 */

public class JsonFilter_Send {
    private  Context context;
    public  JsonFilter_Send(Context c){


        context=c;
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
                    DBAdapter adptr=new DBAdapter(context);

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


                        adptr.setMst_ProductMaster(productMst);
                        //jsonMyArray.add(productMst);
                        //Toast.makeText(this.context,productMst.getBrand(),Toast.LENGTH_LONG).show();
                       // result_view.setText(productMst.getBrand());
                    }
                    break;
                case "RepDetails":
                    Mst_RepTable rep= new Mst_RepTable();
                    DBAdapter rep_adptr=new DBAdapter(context);

                    JSONArray jsonRepArray = new JSONArray(result);
                    for (int i = 0; i < jsonRepArray.length(); i++) {
                        JSONObject jsonRepObject = jsonRepArray.getJSONObject(i);

                        rep.setRepId( jsonRepObject.optString("RepID"));
                        rep.setDeviceName(  jsonRepObject.optString("DeviceName"));
                        rep.setRepName( jsonRepObject.optString("RepName"));
                        rep.setAddress( jsonRepObject.optString("Address"));
                        rep.setContactNo(  jsonRepObject.optString("ContactNo"));
                        rep.setDealerName( jsonRepObject.optString("DealerName"));
                        rep.setDealerAdress( jsonRepObject.optString("DealerAddress"));
                        rep.setMacAdress( jsonRepObject.optString("MacAddress"));
                        rep.setAgentId( jsonRepObject.optString("AgentID"));
                        rep.setIsActive(  jsonRepObject.optInt("IsActive"));


                        rep_adptr.setMst_RepTable(rep);
                        //jsonMyArray.add(productMst);
                        //Toast.makeText(this.context,productMst.getBrand(),Toast.LENGTH_LONG).show();
                        // result_view.setText(productMst.getBrand());
                    }

                    break;

                case  "SupplierTable":
                    Mst_SupplierTable sup= new Mst_SupplierTable();
                    DBAdapter sup_adptr=new DBAdapter(context);

                    JSONArray jsonSupplierArray = new JSONArray(result);
                    for (int i = 0; i < jsonSupplierArray.length(); i++) {
                        JSONObject jsonSupObject = jsonSupplierArray.getJSONObject(i);

                        sup.setPrincipleID( jsonSupObject.optString("PrincipleID"));
                        sup.setPrinciple(  jsonSupObject.optString("Principle"));
                        sup.setActive( jsonSupObject.optString("Activate"));



                        sup_adptr.insertMst_SupplierTable(sup);
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
