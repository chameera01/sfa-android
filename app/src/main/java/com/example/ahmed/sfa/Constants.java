package com.example.ahmed.sfa;

import java.util.HashMap;

/**
 * Created by Ahmed on 3/25/2017.
 */

public class Constants {
    public static final int ACTIVE = 0; //default value for active
    public static final int INACTIVE = 1;

    public static  final String DATA_ARRAY_NAME = "DATAARRAY";//this will be used as array to pass SalesInvoiceModel Parcels
    //between activities
    public static  final String SUMMARY_OBJECT_NAME = "SUMMARY";//Sales invoice summary object passig tag
    public static  final String CHEQUE= "CHEQUE";//cheque object passing tag
    public static  final String SALES_PAYMENT_SUMMARY= "SALESPAYMENTSUMMARY";//sales payment object passing tag
    public static final String ITINERARY ="ITINERARY"; //itinerary passing tag
    public static final String CUSTOMER_NO ="CUSTOMER_NO";// custoemr num passing tag

    public static final int INVOICED = 1;
    public static final int RETURNED = 1;
    public static final int DISMISSED = 2;
    public static final int DEFAULT_VAL = 0;


    public static final String[] CHEQUE_TABLE_COLUMNS ={"SerialCode","InvoiceDate","InvoiceNo","CustomerNo",
            "InvoiceTotalValue","ChequeAmount","ChequeNumber","BankName" ,"CollectedDate","RealizedDate","IsUpload",
                    "IsUpdate","Status","StatusUpdateDate"};//columns of table ChequeDetails to be used with content values
    public static final String CHEQUE_DETAILS_TABLE_NAME ="ChequeDetails";


    public static final String SALES_HEADER_TABLE = "Tr_SalesHeader";
    public static final String[] SALES_HEADER_TABLE_COLUMNS = {"ItineraryID","CustomerNo","InvoiceNo","InvoiceDate","PaymentTime",
            "SubTotal","InvoiceTotal","FullDiscountRate","DiscountAmount","DiscountType","IsOnInvoiceReturn","OnInvoiceReturnNo",
            "OnInvoiceReturnValue","CreditAmount","CashAmount","ChequeAmount","Isprint","ProductCount",
            "InvoiceType","Latitude","Longitude","IsUpload","UploadDate"};

    public static final String SALES_DETAILS_TABLE ="Tr_SalesDetails";
    public static final String[] SALES_DETAILS_TABLE_COLUMNS={"HeaderID","ItemCode","UnitPrice"
            ,"BatchNumber","ExpiryDate","DiscountRate","DiscountAmount","IssueMode","ShelfQty"
                    ,"RequestQty","OrderQty","FreeQty","Total","IsUpload","UploadDate"};


    public static final String TAB_STOCK = "Tr_TabStock";
    public static final String[] TAB_STOCK_COLUMNS = { "ServerID"  ,"PrincipleID","BrandID","ItemCode","BatchNumber",
                    "ExpiryDate","SellingPrice","RetailPrice","Qty","LastUpdateDate"};

    public static final String INVOICE_OUTSTANDING = "Tr_InvoiceOutstanding";
    public static final String[] INVOICE_OUTSTANDING_COLUMNS = {"SerialCode","InvoiceDate",
            "InvoiceNo","CustomerNo","InvoiceTotalValue","CreditValue","CurrentCreditValue","CreditDays","LastUpdateDate",
                    "LastUpdateAmount","LastUpdateType","InsertDate","IsUpload","IsUpdate"};

    public static final String DAILY_ROUTES_TABLE = "Tr_DailyRouteDetails ";
    public static final String[] DAILY_ROUTES_TABLE_COLUMNS={"SerialCode","Date","ItineraryID","CustomerNo","IsPlanned","IsInvoiced"
            ,"InvoiceNo","Reasons","Comment","IsUpload","UploadDate"};

    public static final String ITINERARY_DETAILS_TABLE = "Tr_ItineraryDetails";
    public static final String[] ITINERARY_DETAILS_TABLE_COLUMNS =  {"ItineraryID","ItineraryDate","CustomerNo","IsPlanned","IsInvoiced","LastUpdateDate"};

}
