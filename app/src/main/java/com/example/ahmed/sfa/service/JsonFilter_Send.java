package com.example.ahmed.sfa.service;

import android.content.Context;
import android.widget.Toast;

import com.example.ahmed.sfa.activities.ServiceTest;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.models.Mst_CheckInOutPoints;
import com.example.ahmed.sfa.models.Mst_CustomerStatus;
import com.example.ahmed.sfa.models.Mst_Customermaster;
import com.example.ahmed.sfa.models.Mst_District;
import com.example.ahmed.sfa.models.Mst_ProductBrandManagement;
import com.example.ahmed.sfa.models.Mst_ProductMaster;
import com.example.ahmed.sfa.models.Mst_Reasons;
import com.example.ahmed.sfa.models.Mst_RepTable;
import com.example.ahmed.sfa.models.Mst_Route;
import com.example.ahmed.sfa.models.Mst_SupplierTable;
import com.example.ahmed.sfa.models.Mst_Territory;
import com.example.ahmed.sfa.models.Tr_ItineraryDetails;
import com.example.ahmed.sfa.models.Tr_TabStock;

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

                case "productdetails":
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
                        sup.setActive( jsonSupObject.optInt("Activate"));




                        sup_adptr.insertMst_SupplierTable(sup);
                        //jsonMyArray.add(productMst);
                        //Toast.makeText(this.context,productMst.getBrand(),Toast.LENGTH_LONG).show();
                        // result_view.setText(productMst.getBrand());
                    }
                    break;
                case "ProductBrandManagement":
                    Mst_ProductBrandManagement brand= new Mst_ProductBrandManagement();
                    DBAdapter brand_adptr=new DBAdapter(context);

                    JSONArray jsonBrandArray = new JSONArray(result);
                    for (int i = 0; i < jsonBrandArray.length(); i++) {
                        JSONObject jsonSupObject = jsonBrandArray.getJSONObject(i);

                        brand.setBrandID( jsonSupObject.optString("BrandID"));
                        brand.setPrincipleID(  jsonSupObject.optString("PrincipleID"));
                        brand.setPrinciple(jsonSupObject.optString("Principle"));
                        brand.setMainBrand(jsonSupObject.optString("MainBrand"));
                        brand.setActive( jsonSupObject.optInt("Activate"));
                        brand.setLastUpdateDate(DateManager.dateToday());




                        brand_adptr.insertMst_ProductBrandManagement(brand);
                        //jsonMyArray.add(productMst);
                        //Toast.makeText(this.context,productMst.getBrand(),Toast.LENGTH_LONG).show();
                        // result_view.setText(productMst.getBrand());
                    }
                    break;
                case "CustomerStatus":
                    Mst_CustomerStatus cusStatus= new Mst_CustomerStatus();
                    DBAdapter status_adptr=new DBAdapter(context);

                    JSONArray jsonStatusArray = new JSONArray(result);
                    for (int i = 0; i < jsonStatusArray.length(); i++) {
                        JSONObject jsonSupObject = jsonStatusArray.getJSONObject(i);

                        cusStatus.setStatusId(jsonSupObject.optString("StatusID"));
                        cusStatus.setStatus(jsonSupObject.optString("Status"));
                        cusStatus.setIsActive(jsonSupObject.optInt("IsActive"));

                        //cusStatus.setLastUpdateDate(DateManager.dateToday());

                        status_adptr.insertCustomerStatus(cusStatus);
                    }
                        break;
                case "district":

                    Mst_District district= new Mst_District();
                    DBAdapter district_adptr=new DBAdapter(context);

                    JSONArray jsonDistrictArray = new JSONArray(result);
                    for (int i = 0; i < jsonDistrictArray.length(); i++) {
                        JSONObject jsonSupObject = jsonDistrictArray.getJSONObject(i);

                        district.setDistrictId(jsonSupObject.optString("DistrictId"));
                        district.setDistrictName(jsonSupObject.optString("DistrictName"));
                        district.setIsActive(jsonSupObject.optInt("IsActive"));

                        //cusStatus.setLastUpdateDate(DateManager.dateToday());

                        district_adptr.insertDistrict(district);
                    }
                    break;
                case "territory":
                    Mst_Territory terri= new Mst_Territory();
                    DBAdapter territory_adptr=new DBAdapter(context);

                    JSONArray jsonTerriArray = new JSONArray(result);
                    for (int i = 0; i < jsonTerriArray.length(); i++) {
                        JSONObject jsonSupObject = jsonTerriArray.getJSONObject(i);
                        /*ID | TerritoryID | Territory | IsActive | LastUpdateDate 	*/
                        terri.setTerritory_id(jsonSupObject.optString("TerritoryID"));
                        terri.setTerritory(jsonSupObject.optString("Territory"));
                        terri.setIsActive(jsonSupObject.optInt("IsActive"));
                        terri.setLastUpdateDate(DateManager.dateToday());
                        territory_adptr.insertTerritory(terri);
                    }
                    break;

                case "route":

                    Mst_Route route= new Mst_Route();
                    DBAdapter route_adptr=new DBAdapter(context);

                    JSONArray jsonRouteArray = new JSONArray(result);
                    for (int i = 0; i < jsonRouteArray.length(); i++) {
                        JSONObject jsonSupObject = jsonRouteArray.getJSONObject(i);

                        route.setRouteID(jsonSupObject.optString("RouteID"));
                        route.setTerritoryID(jsonSupObject.optString("TerritoryID"));
                        route.setTerritory(jsonSupObject.optString("Territory"));
                        route.setRoute(jsonSupObject.optString("Route"));
                        route.setIsActive(jsonSupObject.optInt("isActive"));
                        route.setLastUpdateDate(DateManager.dateToday());

                        route_adptr.insertRoute(route);
                    }
                    break;
                case "Reason":

                    Mst_Reasons reason =new Mst_Reasons();
                    DBAdapter reason_adptr=new DBAdapter(context);

                    JSONArray jsonReasonArray = new JSONArray(result);
                    for (int i = 0; i < jsonReasonArray.length(); i++) {
                        JSONObject jsonSupObject = jsonReasonArray.getJSONObject(i);

                        reason.setReasonId(jsonSupObject.optString("ReasonsID"));
                        reason.setReason(jsonSupObject.optString("Reasons"));
                        reason.setIsActive(jsonSupObject.optInt("isActive"));
                        reason.setLastUpdateDate(DateManager.dateToday());

                        reason_adptr.insertReason(reason);
                    }
                    break;
                case "TabStock":
                    Tr_TabStock tabStock = new Tr_TabStock();
                    DBAdapter tabstock_adptr = new DBAdapter(context);

                    JSONArray jsontabStockArr = new JSONArray(result);
                    for(int i = 0; i< jsontabStockArr.length(); i++){
                        JSONObject jsonTabStockObj = jsontabStockArr.getJSONObject(i);


                        tabStock.setServerId(jsonTabStockObj.optString("ServerID"));
                        tabStock.setPrincipleID(jsonTabStockObj.optString("PrincipleID"));
                        tabStock.setBrandId(jsonTabStockObj.optString("BrandID"));
                        tabStock.setItemCode(jsonTabStockObj.optString("ItemCode"));
                        tabStock.setBatchNumber(jsonTabStockObj.optString("BatchNumber"));
                        tabStock.setExpireyDate(jsonTabStockObj.optString("ExpiryDate"));
                        tabStock.setSellingPrice(jsonTabStockObj.optDouble("SellingPrice"));
                        tabStock.setRetailPrice(jsonTabStockObj.optDouble("RetailPrice"));
                        tabStock.setQuantity(jsonTabStockObj.optInt("Qty"));

                        tabstock_adptr.insert_tabStock(tabStock);
                    }
                    break;

                case  "CheckInOutPoints":
                    ////ID | ServerID | Type | PointDescription | IsActive | LastUpdateDate

                    Mst_CheckInOutPoints checkInOutPoints=new Mst_CheckInOutPoints();
                    DBAdapter checkin_adptr=new DBAdapter(context);

                    JSONArray jsonCheckInArray = new JSONArray(result);
                    for (int i = 0; i < jsonCheckInArray.length(); i++) {
                        JSONObject jsonSupObject = jsonCheckInArray.getJSONObject(i);

                        checkInOutPoints.setServerId(jsonSupObject.optString("ServerID"));
                        checkInOutPoints.setType(jsonSupObject.optString("Type"));
                        checkInOutPoints.setPointDescription(jsonSupObject.optString("PointDescription"));
                        checkInOutPoints.setIsActive(jsonSupObject.optInt("isActive"));
                        checkInOutPoints.setLastUpdateDate(DateManager.dateToday());

                        checkin_adptr.insertCheckInOutPoints(checkInOutPoints);
                    }
                    break;
                case"Customer":

                    Mst_Customermaster cus=new Mst_Customermaster();
                    DBAdapter cus_adptr=new DBAdapter(context);

                    JSONArray jsonCusInArray = new JSONArray(result);
                    for (int i = 0; i < jsonCusInArray.length(); i++) {
                        JSONObject jsonSupObject = jsonCusInArray.getJSONObject(i);
/*Mst_Customermaster (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "CustomerNo TEXT,CustomerName TEXT,Address TEXT,DistrictID TEXT,District TEXT,AreaID TEXT," +
                    "Area TEXT,Town TEXT,Telephone TEXT,Fax TEXT,Email Text, BRno TEXT,OwnerContactNo TEXT," +
                    "OwnerName TEXT,PhamacyRegNo TEXT,CreditLimit TEXT,CurrentCreditAmount TEXT,CustomerStatus TEXT" +
                    ",InsertDate TEXT,RouteID TEXT,RouteName TEXT,ImageID TEXT,Latitude TEXT,Longitude TEXT,CompanyCode TEXT," +
                    "IsActive INTEGER,LastUpdateDate TEXT);*/

                        cus.setCustomerNo(jsonSupObject.optString("CustomerNo"));
                        cus.setCustomerName(jsonSupObject.optString("CustomerName"));
                        cus.setAddress(jsonSupObject.optString("Address"));
                        cus.setDistrictID(jsonSupObject.optString("DistrictID"));
                        cus.setDistrict(jsonSupObject.optString("District"));
                        cus.setAreaID(jsonSupObject.optString("AreaID"));
                        cus.setArea(jsonSupObject.optString("Area"));
                        cus.setTown(jsonSupObject.optString("Town"));
                        cus.setTelephone(jsonSupObject.optString("Telephone"));
                        cus.setFax(jsonSupObject.optString("Fax"));
                        cus.setEmail(jsonSupObject.optString("Email"));
                        cus.setBrNo(jsonSupObject.optString("BRno"));
                        cus.setOwnerContactNo(jsonSupObject.optString("OwnerContactNo"));
                        cus.setOwnerName(jsonSupObject.optString("OwnerName"));
                        cus.setPhamacyRegNo(jsonSupObject.optString("PhamacyRegNo"));
                        cus.setCreditLimit(jsonSupObject.optDouble("CreditLimit"));
                        cus.setCurrentCreditAmount(jsonSupObject.optDouble("CurrentCreditAmount"));
                        cus.setCustomerStatus(jsonSupObject.optString("CustomerStatus"));
                        cus.setInsertDate(jsonSupObject.optString("InsertDate"));
                        cus.setRouteID(jsonSupObject.optString("RouteID"));
                        cus.setRouteName(jsonSupObject.optString("RouteName"));
                        /*cus.setImageID(jsonSupObject.optString("ImageID"));
                        cus.setLatitude(jsonSupObject.optDouble("Latitude"));
                        cus.setLongitude(jsonSupObject.optDouble("Longitude"));
                        cus.setCompanyCode(jsonSupObject.optString("CompanyCode"));*/
                        cus.setIsActive(jsonSupObject.optInt("isActive"));
                        cus.setLastUpdateDate(DateManager.dateToday());

                        cus_adptr.insertCustomerData(cus);
                    }



                    break;

                case "ItineraryDetails":
                    Tr_ItineraryDetails itineraryDetails = new Tr_ItineraryDetails();
                    DBAdapter itinerary_adptr=new DBAdapter(context);

                    JSONArray jsonItineraryDetailsArray = new JSONArray(result);
                    for (int i = 0; i < jsonItineraryDetailsArray.length(); i++) {
                        JSONObject jsonSupObject = jsonItineraryDetailsArray.getJSONObject(i);

                        itineraryDetails.setCustomerNo(jsonSupObject.optString("CustomerNo"));
                        itineraryDetails.setIsInvoiced(jsonSupObject.optInt("IsInvoiced"));
                        itineraryDetails.setIsPlaned(jsonSupObject.optInt("IsPlaned"));
                        itineraryDetails.setItineraryDate(jsonSupObject.optString("ItineraryDate"));
                        itineraryDetails.setItineraryID(jsonSupObject.optString("ItineraryID"));
                        itineraryDetails.setLastUpdateDate(DateManager.dateToday());

                        itinerary_adptr.insertItineraryDetails(itineraryDetails);
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
