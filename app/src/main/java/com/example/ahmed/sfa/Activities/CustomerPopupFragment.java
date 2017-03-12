package com.example.ahmed.sfa.activities;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.ImageManager;
import com.example.ahmed.sfa.controllers.database.DBHelper;

/**
 * Created by Ahmed on 3/3/2017.
 */

public class CustomerPopupFragment extends Fragment {

    public static CustomerPopupFragment newInstance(String customerNo){
        CustomerPopupFragment customerPopupFragment = new CustomerPopupFragment();
        Bundle args = new Bundle();
        args.putString("customerNo",customerNo);
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

        DBAdapter adapter = new DBAdapter(getActivity().getApplicationContext());



        Cursor cursor = adapter.getCustomerDetails(cusNo);
        if(cursor.moveToNext()) {
            //Customer customer = new Customer(,,,,,,cursor.getString(6), cursor.getString(7));
            View customerView = inflater.inflate(R.layout.customerpopupforhomeui, container, false);

            Button dismissBtn = (Button) customerView.findViewById(R.id.close_popup);
            dismissBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container.removeViewAt(0);
                }
            });

            ((TextView) customerView.findViewById(R.id.customername)).setText(cursor.getString(1));
            //((TextView) customerView.findViewById(R.id.address)).setText( cursor.getString(3));
            ((TextView) customerView.findViewById(R.id.town)).setText( cursor.getString(2));
            //((TextView) customerView.findViewById(R.id.tel)).setText(cursor.getString(4));
            //((TextView) customerView.findViewById(R.id.lastinvoice)).setText(cursor.getString(5));
            //((TextView) customerView.findViewById(R.id.lastvisit)).setText(cursor.getString(6));

            ImageManager im = new ImageManager(this.getActivity());
            Bitmap bitmap = im.retrieveImage(cursor.getString(7));
            if (bitmap != null)
                ((ImageView) customerView.findViewById(R.id.customer_image)).setImageBitmap(bitmap);

            return customerView;
        }
        return container;
        //Customer  customer = new Customer("1","Ifhaam","Matale","124/d3 Kuriwela Ukuwela","0776699609","2017/3/1","2017/3/1");

    }

    public String getCurrentCustomer(){
        return getArguments().getString("customerNo");
    }

    class DBAdapter {
        Context c;
        DBHelper dbHelper;
        SQLiteDatabase db;

        public DBAdapter(Context context){
            this.c = context;
            dbHelper = new DBHelper(c);
        }

        public void openDB(){
            try{
                db = dbHelper.getWritableDatabase();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        public void closeDB(){
            try{
                dbHelper.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        public Cursor getCustomerDetails(String customerNo){
            openDB();
            //Cursor c = db.rawQuery("SELECT CustomerNo,CustomerName,Town,Address,Telephone,OwnerName,LastUpdateDate FROM Mst_Customermaster WHERE CustomerNo='"+customerNo+"';",null);
            Cursor c = db.rawQuery("SELECT a.CustomerNo,a.CustomerName,a.Town,a.Address,a.Telephone,a.OwnerName,a.LastUpdateDate,b.CustomerImageName" +
                    " FROM Mst_Customermaster a inner join Customer_Images b on a.CustomerNo = b.CustomerNo WHERE a.CustomerNo='"+customerNo+"';",null);
            //here owner name and LastupdateDate should be changed to lastinvoice and last visit after joinning it with the tables

            return c;
        }
    }
}
