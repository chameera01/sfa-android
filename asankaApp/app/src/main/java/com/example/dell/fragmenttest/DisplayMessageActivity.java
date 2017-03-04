package com.example.dell.fragmenttest;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import DBhandle.DBhandle;

public class DisplayMessageActivity extends AppCompatActivity {
    Button btnviewall;
    Spinner spinner_brand;
    EditText et_fn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
        layout.addView(textView);

        spinner_brand = (Spinner) findViewById(R.id.spinner_brand);

        btnviewall=(Button)findViewById(R.id.button_view);
        et_fn=(EditText)findViewById(R.id.editetext_fn);



        /*btnviewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // dbtest();
               // et_fn.setText(getdata());
            }
        });*/
        setSpinner();

    }

    public  void setSpinner(){
        ArrayList<String> arrayList1 = new ArrayList<String>();

        arrayList1.add("Bangalore");
        arrayList1.add("Delhi");
        arrayList1.add("Mumbai");
        ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        spinner_brand.setAdapter(adp);

        spinner_brand.setVisibility(View.VISIBLE);
    }

    public void viewallbtnClick(View view){

        String val=getdata();
        Toast.makeText(this,val, Toast.LENGTH_LONG).show();
    }


    public void selfDestruct(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        update();
        /*TableLayout table = (TableLayout)findViewById(R.id.table_product);
        for(ResourceBalance b : xmlDoc.balance_info)
        {
            // Inflate your row "template" and fill out the fields.
            TableRow row = (TableRow)LayoutInflater.from(CheckBalanceActivity.this).inflate(R.layout.attrib_row, null);
            ((TextView)row.findViewById(R.id.attrib_name)).setText(b.NAME);
            ((TextView)row.findViewById(R.id.attrib_value)).setText(b.VALUE);
            table.addView(row);
        }
        table.requestLayout();     // Not sure if this is needed.
*/


    }
    public boolean  dbtest(){
        DBhandle db=new DBhandle(this);
        boolean result=db.insertContact("name_1", "phone_1", "email@gmail.com", "streat", "place");


        return   result;
    }
    public String getdata(){
        try {
            DBhandle db = new DBhandle(this);
            Cursor res = db.getData();//return tupple id=1;

            if(res.moveToNext()){
                return res.getString(1);
            }else {

                return null;
            }
            /* StringBuffer buffer=new StringBuffer();
                while(res.moveToNext()){
                res.getString(0);//get coloumn value
            }*/

        }catch (Exception e){
            return e.toString();
        }
    }

    private void update() {//insert data to  table
        TableLayout table = (TableLayout)findViewById(R.id.table_product);

        ContextThemeWrapper wrappedContext = new ContextThemeWrapper(this, R.style.tableRow);
        /* ContextThemeWrapper wrappedContext = new ContextThemeWrapper(yourContext, R.style.your_style);
        /* TextView textView = new TextView(wrappedContext, null, 0);
        * */

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        tv.setText("as");

        TextView tv2 = new TextView(wrappedContext,null,0);
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv2.setText("Entry-2");

        tr.addView(tv);
        tr.addView(tv2);

        table.addView(tr);
    }
   /* private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }*/
    /*
    //getselcted row;


     row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                 Toast.makeText(getApplicationContext(), "value was "+value,
                  Toast.LENGTH_LONG).show();
                    view.setBackgroundColor(Color.DKGRAY);
                }
                });
     //second method to get selected row ;
      private OnClickListener tablerowOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            //GET TEXT HERE
            String currenttext = ((TextView)v).getText().toString());
        }
    };
    //3rd method;
    tr.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO:
        }
    });
     */

}
