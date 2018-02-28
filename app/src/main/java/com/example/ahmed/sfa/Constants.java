package com.example.ahmed.sfa;

/**
 * Created by Ahmed on 3/25/2017.
 */

public class Constants {
    public static final String GRAPH_COLOR_ONE = "#E65100";
    public static final String GRAPH_COLOR_TWO = "#795548";

    public static final int GRAPH_NUMBEROFMONTHS = 2;
    public static final int GRAPH_NUMBEROFDAYS = 5;

    public static final int RETURN_REQUEST_RESULT = 147;
    public static final int PRINCIPLE_DISCOUNT_REQUEST_RESULT = 143;

    public static final int ACTIVE = 0; //default value for active
    public static final int INACTIVE = 1;

    public static  final String DATA_ARRAY_NAME = "DATAARRAY";//this will be used as array to pass SalesInvoiceModel Parcels
    //between activities
    public static  final String SUMMARY_OBJECT_NAME = "SUMMARY";//Sales invoice summary object passig tag
    public static  final String CHEQUE= "CHEQUE";//cheque object passing tag
    public static  final String SALES_PAYMENT_SUMMARY= "SALESPAYMENTSUMMARY";//sales payment object passing tag
    public static final String ITINERARY ="ITINERARY"; //itinerary passing tag
    public static final String CUSTOMER_NO ="CUSTOMER_NO";// custoemr num passing tag
    public static final String SALES_RETURN_SUMMARY = "SALES_RETURN";

    public static final int INVOICED = 1;
    public static final int RETURNED = 1;
    public static final int DISMISSED = 2;
    public static final int DEFAULT_VAL = 0;

    public static final String BASE_URL = "http://124.43.19.123:8082/BizMapExpertHesperus_UATTest/";

    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = "http://192.168.1.114/AndroidFileUpload/fileUpload.php";


    public static final String[] CHEQUE_TABLE_COLUMNS ={"SerialCode","InvoiceDate","InvoiceNo","CustomerNo",
            "InvoiceTotalValue","ChequeAmount","ChequeNumber","BankName" ,"CollectedDate","RealizedDate","IsUpload",
                    "IsUpdate","Status","StatusUpdateDate"};//columns of table ChequeDetails to be used with content values
    public static final String CHEQUE_DETAILS_TABLE_NAME ="ChequeDetails";


    public static final String SALES_HEADER_TABLE = "Tr_SalesHeader";
    public static final String[] SALES_HEADER_TABLE_COLUMNS = {"ItineraryID","CustomerNo","InvoiceNo","InvoiceDate","PaymentTime",
            "SubTotal","InvoiceTotal","FullDiscountRate","DiscountAmount","DiscountType","IsOnInvoiceReturn","OnInvoiceReturnNo",
            "OnInvoiceReturnValue","CreditAmount","CashAmount","ChequeAmount","Isprint","ProductCount",
            "InvoiceType", "Latitude", "Longitude", "IsUpload", "UploadDate", "CashDiscount"};

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


    public static final String SALES_RETURN_TABLE = "Tr_SalesReturn";
    public static final String[] SALES_RETURN_TABLE_COLUMNS={"ItineraryID","CustomerNo","InvoiceNo","InvoiceDate",
            "PaymentTime","SubTotal","InvoiceTotal","FullDiscountRate","DiscountAmount","DiscountType",
            "IsOnInvoiceReturn","Isprint","ProductCount","InvoiceType","Latitude","Longitude"
            ,"IsUpload","UploadDate"};

    public static final String SALES_RETURN_DETAILS_TABLE = "Tr_SalesReturnDetails";
    public static final String[] SALES_RETURN_DETAILS_TABLE_COLUMNS ={"HeaderID","ItemCode","UnitPrice","BatchNumber","ExpiryDate","DiscountRate","DiscountAmount","IssueMode","OrderQty","FreeQty","Total","IsUpload","UploadDate"};

    public static final String PRINCIPLE_DISCOUNT_TABLE = "Tr_PrincipleDiscount";
    public static final String[] PRINCIPLE_DISCOUNT_TABLE_COLUMNS = {"InvoiceId","CustomerNo","Date","PrincipleID",
            "Principle", "Value", "DiscountRate", "DiscountValue", "IsUpload"};
}
