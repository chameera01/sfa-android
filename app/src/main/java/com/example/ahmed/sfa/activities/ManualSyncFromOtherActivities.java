package com.example.ahmed.sfa.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.service.UploadSalesHeader;
import com.example.ahmed.sfa.service.UploadTables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shani on 12/01/2018.
 */

public class ManualSyncFromOtherActivities {

    private Context context;

    public ManualSyncFromOtherActivities(Context context) {
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    public Boolean uploadSalesDetails(String repId) {

        Toast.makeText(context, "Connecting...", Toast.LENGTH_LONG).show();
        try {
            //uploading data
            UploadSalesHeader uplwd = new UploadSalesHeader(context);
            ArrayList<String> cusArr = uplwd.mstSalesInvoiceHeader(repId);
//            Toast.makeText(context, "size" + cusArr.get(0), Toast.LENGTH_SHORT).show();

            try {
                for (final String url : cusArr) {

                    //method 3
                    new Sync.UploadWithCallback() {


                        String urlToSplit = url;

                        String cusNo = urlToSplit.split("TabID=")[1].split("&ItineraryID")[0];


                        @Override
                        public void receiveData(Object result) {
//                            Toast.makeText(context, "Ststus" + url, Toast.LENGTH_SHORT).show();
                            String response = (String) result;
//                            Toast.makeText(context, "Response_callback:" + response, Toast.LENGTH_SHORT).show();
                            try {
                                if (response != null) {
                                    JSONArray responseArr = new JSONArray(response);
                                    JSONObject respondObj = responseArr.getJSONObject(0);
                                    String stts = respondObj.getString("Status");
                                    if (stts.equals("Success")) {
                                        DBAdapter adp = new DBAdapter(context);
                                        adp.updateSalesHeaderUploadStatus(cusNo);

                                    } else {
                                        Toast.makeText(context, "Ststus" + stts, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute(url);
                }
            } catch (Exception e) {
//                Toast.makeText(context, "ArrayList:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        //sales details
        try {
            //uploading data
            UploadSalesHeader uplwd = new UploadSalesHeader(context);
            ArrayList<String> cusArr = uplwd.mstSalesInvoiceDetails(repId);
            Toast.makeText(context, "size" + cusArr.get(0), Toast.LENGTH_SHORT).show();

            try {
                for (final String url : cusArr) {

                    //method 3
                    new Sync.UploadWithCallback() {


                        String urlToSplit = url;


                        String[] domains = urlToSplit.split("&");

                        String cusNo = domains[1];


                        @Override
                        public void receiveData(Object result) {
//                            Toast.makeText(context, "Ststus" + url, Toast.LENGTH_SHORT).show();
                            String response = (String) result;
//                            Toast.makeText(context, "Response_callback:" + response, Toast.LENGTH_SHORT).show();
                            try {
                                if (response != null) {
                                    JSONArray responseArr = new JSONArray(response);
                                    JSONObject respondObj = responseArr.getJSONObject(0);
                                    String stts = respondObj.getString("Status");
                                    if (stts.equals("Success")) {
                                        DBAdapter adp = new DBAdapter(context);
                                        adp.updateSalesDetailsUploadStatus(cusNo);

                                    } else {
//                                        Toast.makeText(context, "Ststus" + stts, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute(url);
                }
            } catch (Exception e) {
//                Toast.makeText(context, "ArrayList:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        /// discounts
        try {
            //uploading data
            UploadSalesHeader uplwd = new UploadSalesHeader(context);
            ArrayList<String> cusArr = uplwd.mstSalesInvoiceDiscountDetails(repId);
//            Toast.makeText(context, "size" + cusArr.get(0), Toast.LENGTH_SHORT).show();

            try {
                for (final String url : cusArr) {

                    //method 3
                    new Sync.UploadWithCallback() {


                        String urlToSplit = url;


                        String[] domains = urlToSplit.split("&");

                        String cusNo = domains[1];


                        @Override
                        public void receiveData(Object result) {
//                            Toast.makeText(context, "Ststus" + url, Toast.LENGTH_SHORT).show();
                            String response = (String) result;
//                            Toast.makeText(context, "Response_callback:" + response, Toast.LENGTH_SHORT).show();
                            try {
                                if (response != null) {
                                    JSONArray responseArr = new JSONArray(response);
                                    JSONObject respondObj = responseArr.getJSONObject(0);
                                    String stts = respondObj.getString("Status");
                                    if (stts.equals("Success")) {
                                        DBAdapter adp = new DBAdapter(context);
                                        //adp.updateSalesDetailsUploadStatus(cusNo);

                                    } else {
//                                        Toast.makeText(context, "Ststus" + stts, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute(url);
                }
            } catch (Exception e) {
//                Toast.makeText(context, "ArrayList:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //uploading data - Outstanding Upload
            UploadSalesHeader uplwd = new UploadSalesHeader(context);
            ArrayList<String> cusArr = uplwd.mstSalesInvoiceOutstandingDetails(repId);
//            Toast.makeText(context, "size" + cusArr.get(0), Toast.LENGTH_SHORT).show();

            try {
                for (final String url : cusArr) {

                    //method 3
                    new Sync.UploadWithCallback() {


                        String urlToSplit = url;


                        String[] domains = urlToSplit.split("&");

                        String cusNo = domains[1];


                        @Override
                        public void receiveData(Object result) {
//                            Toast.makeText(context, "Ststus" + url, Toast.LENGTH_SHORT).show();
                            String response = (String) result;
//                            Toast.makeText(context, "Response_callback:" + response, Toast.LENGTH_SHORT).show();
                            try {
                                if (response != null) {
                                    JSONArray responseArr = new JSONArray(response);
                                    JSONObject respondObj = responseArr.getJSONObject(0);
                                    String stts = respondObj.getString("Status");
                                    if (stts.equals("Success")) {
                                        DBAdapter adp = new DBAdapter(context);
                                        adp.updateSalesoutstandingUploadStatuscusNo(cusNo);

                                    } else {
//                                        Toast.makeText(context, "Ststus" + stts, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute(url);
                }
            } catch (Exception e) {
//                Toast.makeText(context, "ArrayList:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //uploading data - Sales Return Header
            UploadSalesHeader uplwd = new UploadSalesHeader(context);
            ArrayList<String> cusArr = uplwd.SalesReturnInvoiceHeader(repId);
//            Toast.makeText(context, "size" + cusArr.get(0), Toast.LENGTH_SHORT).show();

            try {
                for (final String url : cusArr) {

                    //method 3
                    new Sync.UploadWithCallback() {


                        String urlToSplit = url;


                        String[] domains = urlToSplit.split("&");

                        String cusNo1 = domains[1];
                        String[] domainss = cusNo1.split("=");
                        String cusNo = domainss[1];


                        public void receiveData(Object result) {
//                            Toast.makeText(context, "Ststus" + url, Toast.LENGTH_SHORT).show();
                            String response = (String) result;
//                            Toast.makeText(context, "Response_callback:" + response, Toast.LENGTH_SHORT).show();
                            try {
                                if (response != null) {
                                    JSONArray responseArr = new JSONArray(response);
                                    JSONObject respondObj = responseArr.getJSONObject(0);
                                    String stts = respondObj.getString("Status");
                                    if (stts.equals("Success")) {
                                        DBAdapter adp = new DBAdapter(context);
                                        adp.updateSalesReturnHeader(cusNo);

                                    } else {
//                                        Toast.makeText(context, "Ststus" + stts, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute(url);
                }
            } catch (Exception e) {
//                Toast.makeText(context, "ArrayList:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            //uploading data - Sales Return Details
            UploadSalesHeader uplwd = new UploadSalesHeader(context);
            ArrayList<String> cusArr = uplwd.SalesReturnInvoiceDetails(repId);
//            Toast.makeText(context, "size" + cusArr.get(0), Toast.LENGTH_SHORT).show();

            try {
                for (final String url : cusArr) {

                    //method 3
                    new Sync.UploadWithCallback() {


                        String urlToSplit = url;


                        String[] domains = urlToSplit.split("&");

                        String cusNo1 = domains[1];
                        String[] domainss = cusNo1.split("=");
                        String cusNo = domainss[1];


                        @Override
                        public void receiveData(Object result) {
//                            Toast.makeText(context, "Ststus" + url, Toast.LENGTH_SHORT).show();
                            String response = (String) result;
//                            Toast.makeText(context, "Response_callback:" + response, Toast.LENGTH_SHORT).show();
                            try {
                                if (response != null) {
                                    JSONArray responseArr = new JSONArray(response);
                                    JSONObject respondObj = responseArr.getJSONObject(0);
                                    String stts = respondObj.getString("Status");
                                    if (stts.equals("Success")) {
                                        DBAdapter adp = new DBAdapter(context);
                                        adp.updateSalesReturnDetails(cusNo);

                                    } else {
//                                        Toast.makeText(context, "Ststus" + stts, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute(url);
                }

            } catch (Exception e) {
//                Toast.makeText(context, "ArrayList:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @SuppressLint("StaticFieldLeak")
    public Boolean uploadNewCustomers(String repId) {
        try {
            //uploading data
            UploadTables uplwd = new UploadTables(context);
            ArrayList<String> cusArr = uplwd.mstCustomerMaster(repId);

            Toast.makeText(context, "size" + cusArr.get(0), Toast.LENGTH_SHORT).show();

            try {
                for (final String url : cusArr) {


                    //method 3
                    new Sync.UploadWithCallback() {

                        //HashMap<String, String> map = new HashMap<String, String>();//put in it
                        //map.put(stts, filename[i]);
                        String urlToSplit = url;

                        String cusNo = urlToSplit.split("TabCustomerNo=")[1].split("&CustomerName")[0];

                        @Override
                        public void receiveData(Object result) {
                            Toast.makeText(context, "Ststus" + url, Toast.LENGTH_SHORT).show();
                            String response = (String) result;
                            Toast.makeText(context, "Response_callback:" + response, Toast.LENGTH_SHORT).show();
                            try {
                                if (response != null) {
                                    JSONArray responseArr = new JSONArray(response);
                                    JSONObject respondObj = responseArr.getJSONObject(0);
                                    String stts = respondObj.getString("Status");
                                    if (stts.equals("Success")) {
                                        DBAdapter adp = new DBAdapter(context);
                                        adp.updateCustomerUploadStatus(cusNo);

                                    } else {
                                        Toast.makeText(context, "Ststus" + stts, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute(url);
                }
            } catch (Exception e) {
                Toast.makeText(context, "ArrayList:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
