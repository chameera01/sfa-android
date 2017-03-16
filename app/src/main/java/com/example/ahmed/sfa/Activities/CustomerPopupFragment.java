package com.example.ahmed.sfa.activities;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.ImageManager;
import com.example.ahmed.sfa.controllers.RandomNumberGenerator;
import com.example.ahmed.sfa.controllers.database.BaseDBAdapter;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.Customer;
import com.example.ahmed.sfa.models.Itinerary;

import java.util.ArrayList;

/**
 * Created by Ahmed on 3/3/2017.
 */

public class CustomerPopupFragment extends Fragment {
    public static Itinerary itineraryforCurrentCustomer;
    private Customer currentCustomer;
    private Spinner reason;

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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        this.onCreate(savedInstanceState);
        String cusNo =getCurrentCustomer();

        final DBAdapter adapter = new DBAdapter(getActivity().getApplicationContext());

        currentCustomer = adapter.getCustomerDetails(cusNo);

        Log.w("Cursor Check","is Not Empty");
        //Customer customer = new Customer(,,,,,,cursor.getString(6), cursor.getString(7));
        ;;
        View customerView = inflater.inflate(R.layout.customerpopupforhomeui, container, false);
        reason = (Spinner)customerView.findViewById(R.id.reason_customer_skip);
        reason.setAdapter(adapter.getReasons());
        Button dismissBtn = (Button) customerView.findViewById(R.id.close_popup);
        Button skipBtn = (Button) customerView.findViewById(R.id.skipp_customer);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.skipCustomer(currentCustomer,itineraryforCurrentCustomer);
                ((Home)getActivity()).refresh();
            }
        });
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeViewAt(0);
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

        //Log.w("Cursor Check","is Empty");

        //Customer  customer = new Customer("1","Ifhaam","Matale","124/d3 Kuriwela Ukuwela","0776699609","2017/3/1","2017/3/1");

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
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CustomerPopupFragment.this.getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,results);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
