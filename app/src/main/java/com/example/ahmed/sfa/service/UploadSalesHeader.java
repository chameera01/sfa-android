package com.example.ahmed.sfa.service;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.controllers.database.DBHelper;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by frank on 12/7/2017.
 */

public class UploadSalesHeader {
    Context context;

    public UploadSalesHeader(Context c) {
        this.context = c;
    }

    public ArrayList<String> mstSalesInvoiceHeader(String repId) {
        ArrayList<String> cusArr = new ArrayList<>();
        String url_str = "";
        Cursor cusCursor = null;

        Toast.makeText(context, "Inside Upload Sales Header", Toast.LENGTH_SHORT).show();
        //upload Mst_CustomerMaster data

        try {

            String sqlQry = "SELECT * FROM Tr_SalesHeader where isUpload = 0";


            DBHelper dbConn = new DBHelper(context);
            cusCursor = dbConn.getData(sqlQry);
            Toast.makeText(context, "cursorCount: " + cusCursor.getCount(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "db conn error:" + e.getCause(), Toast.LENGTH_SHORT).show();
        }
        try {
            String TabID, ItineraryID, CustomerNo, InvoiceNo, PaymentType, InvoiceDate, SubTotal, InvoiceTotal, FullDiscountRate, DiscountAmount, DiscountType, IsOnInvoiceReturn, OnInvoiceReturnNo, OnInvoiceReturnValue, CreditAmount, CashAmount, ChequeAmount, Isprint, ProductCount, InvoiceType, Latitude, Longitude;

            if (cusCursor != null) {
                while (cusCursor.moveToNext()) {


                    TabID = cusCursor.getString(cusCursor.getColumnIndex("_id"));
                    ItineraryID = cusCursor.getString(cusCursor.getColumnIndex("ItineraryID"));
                    CustomerNo = cusCursor.getString(cusCursor.getColumnIndex("CustomerNo"));
                    InvoiceNo = cusCursor.getString(cusCursor.getColumnIndex("InvoiceNo"));
                    InvoiceDate = cusCursor.getString(cusCursor.getColumnIndex("InvoiceDate"));
                    PaymentType = cusCursor.getString(cusCursor.getColumnIndex("PaymentTime"));
                    SubTotal = cusCursor.getString(cusCursor.getColumnIndex("SubTotal"));
                    InvoiceTotal = cusCursor.getString(cusCursor.getColumnIndex("InvoiceTotal"));

                    FullDiscountRate = cusCursor.getString(cusCursor.getColumnIndex("FullDiscountRate"));
                    DiscountAmount = cusCursor.getString(cusCursor.getColumnIndex("DiscountAmount"));
                    DiscountType = cusCursor.getString(cusCursor.getColumnIndex("DiscountType"));
                    IsOnInvoiceReturn = cusCursor.getString(cusCursor.getColumnIndex("IsOnInvoiceReturn"));
                    OnInvoiceReturnNo = cusCursor.getString(cusCursor.getColumnIndex("OnInvoiceReturnNo"));
                    OnInvoiceReturnValue = cusCursor.getString(cusCursor.getColumnIndex("OnInvoiceReturnValue"));
                    CreditAmount = cusCursor.getString(cusCursor.getColumnIndex("CreditAmount"));

                    CashAmount = cusCursor.getString(cusCursor.getColumnIndex("CashAmount"));
                    ChequeAmount = cusCursor.getString(cusCursor.getColumnIndex("ChequeAmount"));
                    Isprint = cusCursor.getString(cusCursor.getColumnIndex("Isprint"));
                    ProductCount = cusCursor.getString(cusCursor.getColumnIndex("ProductCount"));
                    InvoiceType = cusCursor.getString(cusCursor.getColumnIndex("InvoiceType"));

                    Latitude = cusCursor.getString(cusCursor.getColumnIndex("Latitude"));
                    Longitude = cusCursor.getString(cusCursor.getColumnIndex("Longitude"));

                    url_str = Constants.BASE_URL + "DIstributorManagementSystem/Up_Tr_SalesHeader/Tr_SetInvoiceHeader?RepID=" + repId + "&TabID=" + TabID + "&ItineraryID=" + ItineraryID + "&CustomerNo=" + CustomerNo + "&InvoiceNo=" + InvoiceNo + "&InvoiceDate=" + InvoiceDate + "&PaymentType=" + PaymentType + "&SubTotal=" + SubTotal + "&InvoiceTotal=" + InvoiceTotal + "&FullDiscountRate=" + FullDiscountRate + "&DiscountAmount=" + DiscountAmount + "&DiscountType=" + DiscountType + "&IsOnInvoiceReturn=" + IsOnInvoiceReturn + "&OnInvoiceReturnNo=" + OnInvoiceReturnNo + "&OnInvoiceReturnValue=" + OnInvoiceReturnValue + "&CreditAmount=" + CreditAmount + "&CashAmount=" + CashAmount + "&ChequeAmount=" + ChequeAmount + "&Isprint=" + Isprint + "&ProductCount=" + ProductCount + "&InvoiceType=" + InvoiceType + "&Latitude=" + Latitude + "&Longitude=" + Longitude + "";

                    String url = "";

                    try {
                        URI uri = new URI(url_str.replace(" ", "$$"));
                        {
                            url += uri;
                        }
                    } catch (Exception e) {
                        Log.d("SYNC", e.getMessage());
                    }
                    cusArr.add(url);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "errorupload:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return cusArr;
    }

    public ArrayList<String> mstSalesInvoiceOutstandingDetails(String repId) throws Exception {
        ArrayList<String> cusArr = new ArrayList<>();
        String url_str = "";
        Cursor cusCursor = null;
        Cursor cusCursor2 = null;
        Toast.makeText(context, "Inside Upload Sales  Outstanding Details", Toast.LENGTH_SHORT).show();
        //upload Mst_CustomerMaster data

        String sqlQry = "SELECT _id,SerialCode,InvoiceDate,InvoiceNo,CustomerNo,InvoiceTotalValue,CreditValue,CurrentCreditValue,CreditDays FROM Tr_InvoiceOutstanding where IsUpload==0";


        DBHelper dbConn = new DBHelper(context);
        cusCursor = dbConn.getData(sqlQry);


        Toast.makeText(context, "cursorCount: " + cusCursor.getCount(), Toast.LENGTH_SHORT).show();
        try {
            String TabID, SerialCode, InvoiceDate, InvoiceNo, CustomerNo, InvoiceTotalValue, CreditValue, CurrentCreditValue, CreditDays;

            while (cusCursor.moveToNext()) {


                TabID = cusCursor.getString(cusCursor.getColumnIndex("_id"));
                SerialCode = cusCursor.getString(cusCursor.getColumnIndex("SerialCode"));
                InvoiceDate = cusCursor.getString(cusCursor.getColumnIndex("InvoiceDate"));
                InvoiceNo = cusCursor.getString(cusCursor.getColumnIndex("InvoiceNo"));
                CustomerNo = cusCursor.getString(cusCursor.getColumnIndex("CustomerNo"));
                InvoiceTotalValue = cusCursor.getString(cusCursor.getColumnIndex("InvoiceTotalValue"));
                CreditValue = cusCursor.getString(cusCursor.getColumnIndex("CreditValue"));
                CurrentCreditValue = cusCursor.getString(cusCursor.getColumnIndex("CurrentCreditValue"));
                CreditDays = cusCursor.getString(cusCursor.getColumnIndex("CreditDays"));

                url_str = Constants.BASE_URL + "DIstributorManagementSystem/Up_Tr_InvoiceOutstanding/Tr_InvoiceOutstanding?RepID=" + repId + "&TabID=" + TabID + "&SerialCode=" + SerialCode + "&InvoiceDate=" + InvoiceDate + "&InvoiceNo=" + InvoiceNo + "&CustomerNo=" + CustomerNo + "&InvoiceTotalValue=" + InvoiceTotalValue + "&CreditValue=" + CreditValue + "&CurrentCreditValue=" + CurrentCreditValue + "&CreditDays=" + CreditDays + "";

                String url = "";

                try {
                    URI uri = new URI(url_str.replace(" ", "$$"));
                    {
                        url += uri;
                    }
                } catch (Exception e) {
                    Log.d("SYNC", e.getMessage());
                }
                cusArr.add(url);
            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "errorupload:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return cusArr;
    }

    public ArrayList<String> mstSalesInvoiceDetails(String repId) throws Exception {
        ArrayList<String> cusArr = new ArrayList<>();
        String url_str = "";
        Cursor cusCursor = null;
        Cursor cusCursor2 = null;
        Toast.makeText(context, "Inside Upload Sales Deatils", Toast.LENGTH_SHORT).show();
        //upload Mst_CustomerMaster data

        String sqlQry = "SELECT Tr_SalesDetails._id,  Tr_SalesDetails.HeaderID,Tr_SalesHeader.InvoiceNo,Tr_SalesHeader.CustomerNo, Tr_SalesDetails.ItemCode,  Tr_SalesDetails.UnitPrice, Tr_SalesDetails.BatchNumber, Tr_SalesDetails.ExpiryDate, Tr_SalesDetails.DiscountRate, Tr_SalesDetails.DiscountAmount, Tr_SalesDetails.IssueMode, Tr_SalesDetails.ShelfQty, Tr_SalesDetails.RequestQty, Tr_SalesDetails.OrderQty, Tr_SalesDetails.FreeQty, Tr_SalesDetails.Total FROM Tr_SalesDetails inner join Tr_SalesHeader on Tr_SalesDetails.HeaderID=Tr_SalesHeader._id where Tr_SalesDetails.isUpload = 0";


        DBHelper dbConn = new DBHelper(context);
        cusCursor = dbConn.getData(sqlQry);


        Toast.makeText(context, "cursorCount: " + cusCursor.getCount(), Toast.LENGTH_SHORT).show();
        try {
            String TabID, HeaderID, CustomerNo, InvoiceNo, ItemCode, UnitPrice, BatchNumber, ExpiryDate, DiscountRate, DiscountAmount, IssueMode, ShelfQty, RequestQty, OrderQty, FreeQty, Total;

            while (cusCursor.moveToNext()) {

                TabID = cusCursor.getString(cusCursor.getColumnIndex("_id"));
                HeaderID = cusCursor.getString(cusCursor.getColumnIndex("HeaderID"));

                ItemCode = cusCursor.getString(cusCursor.getColumnIndex("ItemCode"));
                UnitPrice = cusCursor.getString(cusCursor.getColumnIndex("UnitPrice"));
                BatchNumber = cusCursor.getString(cusCursor.getColumnIndex("BatchNumber"));
                ExpiryDate = cusCursor.getString(cusCursor.getColumnIndex("ExpiryDate"));
                DiscountRate = cusCursor.getString(cusCursor.getColumnIndex("DiscountRate"));
                DiscountAmount = cusCursor.getString(cusCursor.getColumnIndex("DiscountAmount"));

                IssueMode = cusCursor.getString(cusCursor.getColumnIndex("IssueMode"));
                ShelfQty = cusCursor.getString(cusCursor.getColumnIndex("ShelfQty"));
                RequestQty = cusCursor.getString(cusCursor.getColumnIndex("RequestQty"));
                OrderQty = cusCursor.getString(cusCursor.getColumnIndex("OrderQty"));
                FreeQty = cusCursor.getString(cusCursor.getColumnIndex("FreeQty"));
                Total = cusCursor.getString(cusCursor.getColumnIndex("Total"));

                CustomerNo = cusCursor.getString(cusCursor.getColumnIndex("CustomerNo"));
                InvoiceNo = cusCursor.getString(cusCursor.getColumnIndex("InvoiceNo"));

                url_str = Constants.BASE_URL + "DIstributorManagementSystem/Up_Tr_SalesDetails/Tr_SetInvoiceDetails?RepID=" + repId + "&TabID=" + TabID + "&HeaderID=" + HeaderID + "&InvoiceNo=" + InvoiceNo + "&CustomerNo=" + CustomerNo + "&itemCode=" + ItemCode + "&unitPrice=" + UnitPrice + "&batchNumber=" + BatchNumber + "&expiryDate=" + ExpiryDate + "&discountRate=" + DiscountRate + "&discountAmount=" + DiscountAmount + "&IssueMode=" + IssueMode + "&ShelfQty=" + ShelfQty + "&RequestQty=" + RequestQty + "&OrderQty=" + OrderQty + "&FreeQty=" + FreeQty + "&total=" + Total + "";

                String url = "";

                try {
                    URI uri = new URI(url_str.replace(" ", "$$"));
                    {
                        url += uri;
                    }
                } catch (Exception e) {
                    Log.d("SYNC", e.getMessage());
                }
                cusArr.add(url);
            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "errorupload:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return cusArr;
    }

    public ArrayList<String> mstSalesInvoiceDiscountDetails(String repId) throws Exception {
        ArrayList<String> cusArr = new ArrayList<>();
        String url_str = "";
        Cursor cusCursor = null;
        Cursor cusCursor2 = null;
        Toast.makeText(context, "Inside Upload Sales  Principle Discount Details", Toast.LENGTH_SHORT).show();
        //upload Mst_CustomerMaster data

        String sqlQry = "SELECT Tr_PrincipleDiscount._id as TabID,Tr_PrincipleDiscount.InvoiceId,Tr_SalesHeader._id as InvHeaderID, Tr_PrincipleDiscount.CustomerNo, Tr_PrincipleDiscount.Date,  Tr_PrincipleDiscount.PrincipleID, Tr_PrincipleDiscount.Principle, Tr_PrincipleDiscount.Value, Tr_PrincipleDiscount.DiscountRate, Tr_PrincipleDiscount.DiscountValue FROM Tr_PrincipleDiscount inner join Tr_SalesHeader on Tr_PrincipleDiscount.InvoiceId=Tr_SalesHeader.InvoiceNo and Tr_PrincipleDiscount.CustomerNo=Tr_SalesHeader.CustomerNo";


        DBHelper dbConn = new DBHelper(context);
        cusCursor = dbConn.getData(sqlQry);


        Toast.makeText(context, "cursorCount: " + cusCursor.getCount(), Toast.LENGTH_SHORT).show();
        try {
            String TabID, InvoiceId, HeaderID, CustomerNo, Date, PrincipleID, Principle, Value, DiscountRate, DiscountValue;

            while (cusCursor.moveToNext()) {


                TabID = cusCursor.getString(cusCursor.getColumnIndex("TabID"));
                InvoiceId = cusCursor.getString(cusCursor.getColumnIndex("InvoiceId"));
                HeaderID = cusCursor.getString(cusCursor.getColumnIndex("InvHeaderID"));
                CustomerNo = cusCursor.getString(cusCursor.getColumnIndex("CustomerNo"));
                Date = cusCursor.getString(cusCursor.getColumnIndex("Date"));
                PrincipleID = cusCursor.getString(cusCursor.getColumnIndex("PrincipleID"));
                Principle = cusCursor.getString(cusCursor.getColumnIndex("Principle"));
                Value = cusCursor.getString(cusCursor.getColumnIndex("Value"));
                DiscountRate = cusCursor.getString(cusCursor.getColumnIndex("DiscountRate"));

                DiscountValue = cusCursor.getString(cusCursor.getColumnIndex("DiscountValue"));


                url_str = Constants.BASE_URL + "DIstributorManagementSystem/Up_Tr_SalesPrinsipleWiseDiscount/Tr_UploadSalesPrinsipleWiseDiscount?" +
                        "RepID=" + repId + "&TabID=" + TabID + "&HeaderID=" + HeaderID + "&InvoiceId=" + InvoiceId + "&CustomerNo=" + CustomerNo + "" +
                        "&date=" + Date + "&PrincipleID=" + PrincipleID + "&Principle=" + Principle + "&value=" + Value + "&discountRate=" + DiscountRate + "" +
                        "&discountValue=" + DiscountValue + "";


                String url = "";

                try {
                    URI uri = new URI(url_str.replace(" ", "$$"));
                    {

                        url += uri;
                    }
                } catch (Exception e) {

                }

                cusArr.add(url);


            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "errorupload:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return cusArr;
    }


    //// Returns Upload
    public ArrayList<String> SalesReturnInvoiceHeader(String repId) throws Exception {
        ArrayList<String> cusArr = new ArrayList<>();
        String url_str = "";
        Cursor cusCursor = null;
        Cursor cusCursor2 = null;
        Toast.makeText(context, "Inside Upload Sales Return Header", Toast.LENGTH_SHORT).show();
        //upload Mst_CustomerMaster data

        String sqlQry = "select  _ID as TabID, ItineraryID, CustomerNo, InvoiceNo, InvoiceDate, PaymentTime, SubTotal, InvoiceTotal, fullDiscountRate, DiscountAmount, DiscountType, Isprint, ProductCount,  InvoiceType, Latitude, longitude  from Tr_SalesReturn where IsUpload=0";


        DBHelper dbConn = new DBHelper(context);
        cusCursor = dbConn.getData(sqlQry);


        Toast.makeText(context, "cursorCount: " + cusCursor.getCount(), Toast.LENGTH_SHORT).show();
        try {
            String TabID, ItineraryID, CustomerNo, InvoiceNo, PaymentType, InvoiceDate, SubTotal, InvoiceTotal, FullDiscountRate, DiscountAmount, DiscountType, IsOnInvoiceReturn, OnInvoiceReturnNo, OnInvoiceReturnValue, CreditAmount, CashAmount, ChequeAmount, Isprint, ProductCount, InvoiceType, Latitude, Longitude;

            while (cusCursor.moveToNext()) {


                TabID = cusCursor.getString(cusCursor.getColumnIndex("TabID"));
                ItineraryID = cusCursor.getString(cusCursor.getColumnIndex("ItineraryID"));
                CustomerNo = cusCursor.getString(cusCursor.getColumnIndex("CustomerNo"));
                InvoiceNo = cusCursor.getString(cusCursor.getColumnIndex("InvoiceNo"));
                InvoiceDate = cusCursor.getString(cusCursor.getColumnIndex("InvoiceDate"));
                PaymentType = cusCursor.getString(cusCursor.getColumnIndex("PaymentTime"));
                SubTotal = cusCursor.getString(cusCursor.getColumnIndex("SubTotal"));
                InvoiceTotal = cusCursor.getString(cusCursor.getColumnIndex("InvoiceTotal"));

                FullDiscountRate = cusCursor.getString(cusCursor.getColumnIndex("FullDiscountRate"));
                DiscountAmount = cusCursor.getString(cusCursor.getColumnIndex("DiscountAmount"));
                DiscountType = cusCursor.getString(cusCursor.getColumnIndex("DiscountType"));
                Isprint = cusCursor.getString(cusCursor.getColumnIndex("Isprint"));
                ProductCount = cusCursor.getString(cusCursor.getColumnIndex("ProductCount"));
                InvoiceType = cusCursor.getString(cusCursor.getColumnIndex("InvoiceType"));

                Latitude = cusCursor.getString(cusCursor.getColumnIndex("Latitude"));
                Longitude = cusCursor.getString(cusCursor.getColumnIndex("Longitude"));


                url_str = Constants.BASE_URL + "DIstributorManagementSystem/Up_Tr_SalesReturnHeader/Tr_SetInvoiceReturnHeader?" +
                        "RepID=" + repId + "&TabID=" + TabID + "&ItineraryID=" + ItineraryID + "&CustomerNo=" + CustomerNo + "&InvoiceNo=" + InvoiceNo + "&InvoiceDate=" + InvoiceDate + "&paymentType=" + PaymentType + "&subTotal=" + SubTotal + "&" +
                        "invoiceTotal=" + InvoiceTotal + "&fullDiscountRate=" + FullDiscountRate + "&discountAmount=" + DiscountAmount + "&DiscountType=" + DiscountType + "&Isprint=" + Isprint + "&ProductCount=" + ProductCount + "&" +
                        "InvoiceType=" + InvoiceType + "&Latitude=" + Latitude + "&longitude=" + Longitude + "";


                String url = "";

                try {
                    URI uri = new URI(url_str.replace(" ", "$$"));
                    {

                        url += uri;
                    }
                } catch (Exception e) {

                }

                cusArr.add(url);


            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "errorupload:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return cusArr;
    }

    public ArrayList<String> SalesReturnInvoiceDetails(String repId) throws Exception {
        ArrayList<String> cusArr = new ArrayList<>();
        String url_str = "";
        Cursor cusCursor = null;
        Cursor cusCursor2 = null;
        Toast.makeText(context, "Inside Upload Sales Return Details", Toast.LENGTH_SHORT).show();
        //upload Mst_CustomerMaster data

        String sqlQry = "SELECT Tr_SalesReturnDetails._ID as TabID, HeaderID, InvoiceNo, CustomerNo, itemCode, unitPrice, batchNumber, ExpiryDate, discountRate, Tr_SalesReturnDetails.discountAmount, IssueMode, 0  as ShelfQty, 0 as RequestQty, OrderQty*-1 as OrderQty, FreeQty*-1 as FreeQty, total*-1 as Total   FROM Tr_SalesReturnDetails inner join Tr_SalesReturn on Tr_SalesReturnDetails.HeaderID=Tr_SalesReturn._id where Tr_SalesReturnDetails.IsUpload=0";


        DBHelper dbConn = new DBHelper(context);
        cusCursor = dbConn.getData(sqlQry);


        Toast.makeText(context, "cursorCount: " + cusCursor.getCount(), Toast.LENGTH_SHORT).show();
        try {
            String bt, ex, im, TabID, HeaderID, CustomerNo, InvoiceNo, ItemCode, UnitPrice, BatchNumber, ExpiryDate, DiscountRate, DiscountAmount, IssueMode, ShelfQty, RequestQty, OrderQty, FreeQty, Total;

            while (cusCursor.moveToNext()) {

                TabID = cusCursor.getString(cusCursor.getColumnIndex("TabID"));
                HeaderID = cusCursor.getString(cusCursor.getColumnIndex("HeaderID"));

                ItemCode = cusCursor.getString(cusCursor.getColumnIndex("ItemCode"));
                UnitPrice = cusCursor.getString(cusCursor.getColumnIndex("UnitPrice"));

                bt = cusCursor.getString(cusCursor.getColumnIndex("BatchNumber"));
                if (bt == "") {
                    BatchNumber = cusCursor.getString(cusCursor.getColumnIndex("ItemCode"));
                } else {
                    BatchNumber = cusCursor.getString(cusCursor.getColumnIndex("BatchNumber"));

                }
                ex = cusCursor.getString(cusCursor.getColumnIndex("ExpiryDate"));
                if (ex == "") {
                    ExpiryDate = "01/01/2020";
                } else {
                    ExpiryDate = cusCursor.getString(cusCursor.getColumnIndex("ExpiryDate"));
                }

                DiscountRate = cusCursor.getString(cusCursor.getColumnIndex("DiscountRate"));
                DiscountAmount = cusCursor.getString(cusCursor.getColumnIndex("DiscountAmount"));

                //im = cusCursor.getString(cusCursor.getColumnIndex("IssueMode")) ;
                //if(im == "") {
                //    IssueMode = "R";
                //}
                //else
                //{
                //   IssueMode = cusCursor.getString(cusCursor.getColumnIndex("IssueMode")) ;
                //}
                IssueMode = "R";
                ShelfQty = cusCursor.getString(cusCursor.getColumnIndex("ShelfQty"));
                RequestQty = cusCursor.getString(cusCursor.getColumnIndex("RequestQty"));

                OrderQty = cusCursor.getString(cusCursor.getColumnIndex("OrderQty"));

                FreeQty = cusCursor.getString(cusCursor.getColumnIndex("FreeQty"));
                Total = cusCursor.getString(cusCursor.getColumnIndex("Total"));

                CustomerNo = cusCursor.getString(cusCursor.getColumnIndex("CustomerNo"));
                InvoiceNo = cusCursor.getString(cusCursor.getColumnIndex("InvoiceNo"));

                url_str = Constants.BASE_URL + "DIstributorManagementSystem/Up_Tr_SalesReturnDetails/Tr_SalesReturnDetails?" +
                        "RepID=" + repId + "&TabID=" + TabID + "&HeaderID=" + HeaderID + "&InvoiceNo=" + InvoiceNo + "&CustomerNo=" + CustomerNo + "&itemCode=" + ItemCode + "&unitPrice=" + UnitPrice + "&batchNumber=" + BatchNumber + "&" +
                        "ExpiryDate=" + ExpiryDate + "&discountRate=" + DiscountRate + "&discountAmount=" + DiscountAmount + "&IssueMode=" + IssueMode + "&ShelfQty=" + ShelfQty + "&RequestQty=" + RequestQty + "&OrderQty=" + OrderQty + "&" +
                        "FreeQty=" + FreeQty + "&total=" + Total + "";

                String url = "";

                try {
                    URI uri = new URI(url_str.replace(" ", "$$"));
                    {

                        url += uri;
                    }
                } catch (Exception e) {

                }

                cusArr.add(url);


            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "errorupload:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return cusArr;
    }
}
