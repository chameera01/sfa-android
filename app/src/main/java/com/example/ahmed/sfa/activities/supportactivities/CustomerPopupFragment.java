package com.example.ahmed.sfa.activities.supportactivities;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ahmed.sfa.Constants;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Home;
import com.example.ahmed.sfa.activities.Invoice;
import com.example.ahmed.sfa.activities.InvoiceHistory;
import com.example.ahmed.sfa.activities.Return;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.ImageManager;
import com.example.ahmed.sfa.controllers.RandomNumberGenerator;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.Customer;
import com.example.ahmed.sfa.models.Itinerary;
import com.example.ahmed.sfa.models.OutstandingInvoice;

import java.util.ArrayList;

/**
 * Created by Ahmed on 3/3/2017.
 */

public class CustomerPopupFragment extends Fragment {
    public static Itinerary itineraryforCurrentCustomer;
    private Customer currentCustomer;
    private Spinner reason;
    private String invoiceNo;
    private double credit;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    public static CustomerPopupFragment newInstance(Itinerary itinerary){
        CustomerPopupFragment customerPopupFragment = new CustomerPopupFragment();
        Bundle args = new Bundle();
        args.putString("customerNo",itinerary.getCustomerNo());
        itineraryforCurrentCustomer = itinerary;
        customerPopupFragment.setArguments(args);

        return customerPopupFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        this.onCreate(savedInstanceState);
        final String cusNo =getCurrentCustomer();

        final DBAdapter adapter = new DBAdapter(getActivity().getApplicationContext());

        currentCustomer = adapter.getCustomerDetails(cusNo);

        Log.w("Cursor Check","is Not Empty");
        //Customer customer = new Customer(,,,,,,cursor.getString(6), cursor.getString(7));
        View customerView = inflater.inflate(R.layout.customerpopupforhomeui, container, false);
        reason = (Spinner)customerView.findViewById(R.id.reason_customer_skip);
        reason.setAdapter(adapter.getReasons());

        Button generateInvBtn = (Button)customerView.findViewById(R.id.generateinvoice);
        generateInvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerPopupFragment.this.getActivity(), Invoice.class);
                intent.putExtra(Constants.CUSTOMER_NO,cusNo);//this data will be passed on to the db insert step
                intent.putExtra(Constants.ITINERARY,itineraryforCurrentCustomer);//this data will be passed on to the db insert step
                startActivity(intent);
            }
        });

        Button generateReturnBtn = (Button) customerView.findViewById(R.id.generatereturn);
        generateReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOutstanding(cusNo);
//                Intent intent = new Intent(CustomerPopupFragment.this.getActivity(), Return.class);
//                intent.putExtra(Constants.CUSTOMER_NO,cusNo);//this data will be passed on to the db insert step
//                intent.putExtra(Constants.ITINERARY,itineraryforCurrentCustomer);//this data will be passed on to the db insert step
//                startActivity(intent);
            }
        });

        Button dismissBtn = (Button) customerView.findViewById(R.id.close_popup);
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeViewAt(0);
            }
        });

        Button skipBtn = (Button) customerView.findViewById(R.id.skipp_customer);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.skipCustomer(currentCustomer,itineraryforCurrentCustomer);
                ((Home)getActivity()).refresh();
            }
        });


        Button history = (Button) customerView.findViewById(R.id.invoicehistory);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InvoiceHistory.class);
                intent.putExtra("CUSTOMER", currentCustomer.getName());
                startActivity(intent);
            }
        });

        ((TextView) customerView.findViewById(R.id.customername)).setText(currentCustomer.getName());
        ((TextView) customerView.findViewById(R.id.address)).setText( currentCustomer.getAddress());
        ((TextView) customerView.findViewById(R.id.town)).setText(currentCustomer.getTown());
        ((TextView) customerView.findViewById(R.id.tel)).setText(currentCustomer.getTel());
        String captionforLastInvoice = "No : "+currentCustomer.getLastInvoiceNo()+"\n"+"Credit(Rs) : "+currentCustomer.getLastInvoiceCredit()
                +"\n"+"Tot : "+currentCustomer.getLastInvoiceTotal();
        ((TextView) customerView.findViewById(R.id.lastinvoice)).setText(captionforLastInvoice);
        ((TextView) customerView.findViewById(R.id.lastvisit)).setText(currentCustomer.getLastInvoiceDate());

        ImageManager im = new ImageManager(this.getActivity());
        Bitmap bitmap = im.retrieveImage(currentCustomer.getImagePath());
        if (bitmap != null)
            ((ImageView) customerView.findViewById(R.id.customer_image)).setImageBitmap(bitmap);

        return customerView;

    }

    private void checkOutstanding(final String cusNo) {

        DBHelper db = new DBHelper(getActivity());

        Log.d("COL", "inside checkOutstanding");

        final ArrayList<OutstandingInvoice> oiarray = new ArrayList<>();

        com.example.ahmed.sfa.controllers.adapters.DBAdapter adapter = new com.example.ahmed.sfa.controllers.adapters.DBAdapter(getActivity());

        adapter.openDB();

        Cursor c = adapter.runQuery("SELECT InvoiceNo, CurrentCreditValue FROM Collections WHERE CustomerNo ='" + cusNo + "'");
        if (c.getCount() != 0) {
            Log.d("COL", "inside DB helper_not empty_" + c.getCount());
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    OutstandingInvoice oi = new OutstandingInvoice();
                    oi.setInvoiceNo(c.getString(c.getColumnIndex("InvoiceNo")));
                    Log.d("COL", oi.getInvoiceNo());
                    oi.setCurrentCredit(c.getDouble(c.getColumnIndex("CurrentCreditValue")));
                    Log.d("COL", String.valueOf(oi.getCurrentCredit()));
                    oiarray.add(oi);
                    c.moveToNext();
                    Log.d("COL", "moved to next");
                }
            }

            ArrayList<String> dis = setString(oiarray);
            String[] values = dis.toArray(new String[0]);
            for (int i = 0; i < values.length; i++) {
                Log.d("COL", values[i]);
            }


            ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, values);

            ListView lv = new ListView(getActivity());
            lv.setAdapter(adp);

            //write onclicklistener

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position,
                                        long arg3) {
//                    String value = (String)adapter.getItemAtPosition(position);
                    invoiceNo = oiarray.get(position).getInvoiceNo();
                    credit = oiarray.get(position).getCurrentCredit();

                    Intent intent = new Intent(CustomerPopupFragment.this.getActivity(), Return.class);
                    intent.putExtra(Constants.CUSTOMER_NO, cusNo);//this data will be passed on to the db insert step
                    intent.putExtra(Constants.ITINERARY, itineraryforCurrentCustomer);//this data will be passed on to the db insert step
                    intent.putExtra("INVOICE_NO", invoiceNo);
                    intent.putExtra("CURRENT_CREDIT", credit);
                    intent.putExtra("CALLING_ACT", false);

                    startActivity(intent);
                    dialog.dismiss();

                }
            });

            builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);

            builder.setTitle("Outstanding Invoices - Select one");
            builder.setView(lv);
            dialog = builder.create();
            dialog.show();

        } else {
            Log.d("COL", "inside DB helper_empty");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);

            builder.setTitle("There are no outstanding invoices!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }

        adapter.closeDB();

    }

    private ArrayList<String> setString(ArrayList<OutstandingInvoice> oiarray) {

        ArrayList<String> displays = new ArrayList<>();

        for (OutstandingInvoice oi : oiarray) {
            String display = "Invoice No: " + oi.getInvoiceNo() + "\t\t\t\t\t" + "Current Credit: " + oi.getCurrentCredit();
            displays.add(display);
        }

        return displays;
    }


    public String getCurrentCustomer(){
        return getArguments().getString("customerNo");
    }

    class DBAdapter  extends BaseDBAdapter{


        public DBAdapter(Context context){
           super(context);
        }

        public Customer getCustomerDetails(String customerNo){
            openDB();
            //Cursor c = db.rawQuery("SELECT CustomerNo,CustomerName,Town,Address,Telephone,OwnerName,LastUpdateDate FROM Mst_Customermaster WHERE CustomerNo='"+customerNo+"';",null);
            String qry = "SELECT a.CustomerNo,a.CustomerName,a.Town,a.Address,a.Telephone,c.InvoiceNo" +
                    ",c.CreditAmount,c.InvoiceTotal,c.InvoiceDate,b.CustomerImageName" +
                    " FROM Mst_Customermaster a left join Tr_SalesHeader c on a.CustomerNo=c.CustomerNo left join Customer_Images b on c.CustomerNo=b.CustomerNo"+
                    //" FROM Mst_Customermaster a inner join Customer_Images b on a.CustomerNo = b.CustomerNo inner join Tr_SalesHeader c on a.CustomerNo=c.CustomerNo " +
                    " WHERE a.CustomerNo='"+customerNo+"' Order BY c.InvoiceDate DESC;";
            Cursor c = db.rawQuery(qry,null);
            Customer customer =  new Customer(c);
            closeDB();
            return customer;
        }

        public ArrayAdapter<String> getReasons(){
            openDB();
            String qry = "SELECT Reason FROM Mst_Reasons WHERE isActive=0 ";
            Cursor cursor = db.rawQuery(qry,null);
            ArrayList<String> results = new ArrayList<>();
            while(cursor.moveToNext()){
                results.add(cursor.getString(0));
            }
            closeDB();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CustomerPopupFragment.this.getActivity().getApplicationContext(), R.layout.custom_spinner, results);
            arrayAdapter.setDropDownViewResource(R.layout.custom_spinner);
            return arrayAdapter;
        }

        public void skipCustomer(Customer customer,Itinerary itinerary){
            openDB();
            String serial = RandomNumberGenerator.generateRandomCode(RandomNumberGenerator.GENERATE_ALPHABANUMERIC,"SKPCUS_",16);
            String date = DateManager.dateToday();
            String isPlanned = (itinerary.isPlanned())?"0":"1";
                String qry = "INSERT INTO Tr_DailyRouteDetails (SerialCode,Date,ItineraryID,CustomerNo,IsPlanned,IsInvoiced,Reasons," +
                        "isUpload) VALUES ('"+serial+"','"+date+"','"+itinerary.getId()+"','"+customer.getCustomerNo()
                        +"',"+isPlanned+",2,'"+reason.getSelectedItem().toString()+"',1);";
            db.execSQL(qry);
                qry = "UPDATE Tr_ItineraryDetails SET IsInvoiced=2 WHERE ItineraryID='"+itinerary.getId()+"';";
            db.execSQL(qry);
            Log.w("Action","Customer Dismissed");
            closeDB();

        }
    }



    /**
     @Override
     public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
     this.onCreate(savedInstanceState);


     String cusNo =getCurrentCustomer();

     DBAdapter adapter = new DBAdapter(getActivity().getApplicationContext());



     Cursor cursor = adapter.getCustomerDetails(cusNo);
     if(cursor.moveToNext()) {
     Log.w("Cursor Check","is Not Empty");
     //Customer customer = new Customer(,,,,,,cursor.getString(6), cursor.getString(7));
     ;;
     View customerView = inflater.inflate(R.layout.customerpopupforhomeui, container, false);
     Spinner spinner = (Spinner)customerView.findViewById(R.id.reason_customer_skip);
     spinner.setAdapter(adapter.getReasons());
     Button dismissBtn = (Button) customerView.findViewById(R.id.close_popup);
     dismissBtn.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
     container.removeViewAt(0);
     }
     });

     ((TextView) customerView.findViewById(R.id.customername)).setText(cursor.getString(1));
     ((TextView) customerView.findViewById(R.id.address)).setText( cursor.getString(3));
     ((TextView) customerView.findViewById(R.id.town)).setText( cursor.getString(2));
     ((TextView) customerView.findViewById(R.id.tel)).setText(cursor.getString(4));
     String captionforLastInvoice = "No : "+cursor.getString(5)+"\n"+"Credit(Rs) : "+cursor.getString(6)+"\n"+"Tot : "+cursor.getString(7);
     ((TextView) customerView.findViewById(R.id.lastinvoice)).setText(captionforLastInvoice);
     ((TextView) customerView.findViewById(R.id.lastvisit)).setText(cursor.getString(8));

     ImageManager im = new ImageManager(this.getActivity());
     Bitmap bitmap = im.retrieveImage(cursor.getString(9));
     if (bitmap != null)
     ((ImageView) customerView.findViewById(R.id.customer_image)).setImageBitmap(bitmap);

     return customerView;
     }
     Log.w("Cursor Check","is Empty");
     return null;
     //Customer  customer = new Customer("1","Ifhaam","Matale","124/d3 Kuriwela Ukuwela","0776699609","2017/3/1","2017/3/1");

     }*/


    /**
     public Cursor getCustomerDetails(String customerNo){
     openDB();
     //Cursor c = db.rawQuery("SELECT CustomerNo,CustomerName,Town,Address,Telephone,OwnerName,LastUpdateDate FROM Mst_Customermaster WHERE CustomerNo='"+customerNo+"';",null);
     String qry = "SELECT a.CustomerNo,a.CustomerName,a.Town,a.Address,a.Telephone,c.InvoiceNo" +
     ",c.CreditAmount,c.InvoiceTotal,c.InvoiceDate,b.CustomerImageName" +
     " FROM Mst_Customermaster a left join Tr_SalesHeader c on a.CustomerNo=c.CustomerNo left join Customer_Images b on c.CustomerNo=b.CustomerNo"+
     //" FROM Mst_Customermaster a inner join Customer_Images b on a.CustomerNo = b.CustomerNo inner join Tr_SalesHeader c on a.CustomerNo=c.CustomerNo " +
     " WHERE a.CustomerNo='"+customerNo+"' Order BY c.InvoiceDate DESC;";
     Cursor c = db.rawQuery(qry,null);
     Log.w("Entered here","executed "+qry);
     //here owner name and LastupdateDate should be changed to lastinvoice and last visit after joinning it with the tables

     return c;
     }*/
}
