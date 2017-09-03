package com.example.ahmed.sfa.Activities.supportactivities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.example.ahmed.sfa.Activities.DisplayProductTableActivity;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.ListViewAdapter;
import com.example.ahmed.sfa.models.Mst_ProductMaster;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Created by DELL on 8/26/2017.
 */

public class TableThread extends Thread {
    private final String query;
    Context c;
    ListView v;
    TableLayout table ;
    static int row_count = 0;
    Activity activity;
    static LinkListProductQueue queue;
    ArrayList<Mst_ProductMaster> prdList = new ArrayList<>();

    private ArrayList<HashMap<String, String>> list;
    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    public static final String THIRD_COLUMN="Third";
    public static final String FOURTH_COLUMN="Fourth";
    public static final String Fifth_COLUMN="Fifth";

    private static  class LinkListProductQueue{

        private LinkedList<Mst_ProductMaster> data = new LinkedList<Mst_ProductMaster>();

        public void enqueue(Mst_ProductMaster item) {
            data.addLast(item);
            //System.out.println("Added: " + item);
        }

        public Mst_ProductMaster dequeue() {
            //System.out.println("Removed: " + data.getFirst());
            return data.removeFirst();

        }

        public Object peek() {
            return data.getFirst();
        }

        public int size() {
            return data.size();
        }

        public boolean isEmpty() {
            return data.isEmpty();
        }
    }



    public  TableThread(Activity activity, Context context, ListView listview, TableLayout table, String q){
        this.c  = context;
        this.v  = listview ;
        this.table = table;
        this.query = q;
        this.activity = activity;
    }
    public void run(){

        activity.runOnUiThread(new Runnable() {
                          public void run() {

                              ArrayList<String> values = new ArrayList<>() ;
                              values.add("No Results To Preview");

                              //for (int i=0;i<1000;i++){
                                 // values.add( i+"add");
                                 //queue.enqueue(i+"-queue");
                              //}


                              /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                                      android.R.layout.simple_list_item_1, android.R.id.text1, values);*/
                              //v.setAdapter(adapter);
                              // table.removeAllViews();
                              //ContextThemeWrapper wrappedContext = new ContextThemeWrapper(c, R.style.pending_customer_row);
                             /*try {
                                  while (!queue.isEmpty()){
                                      TextView tv = new TextView(wrappedContext,null,0);
                                      tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

                                      tv.setText(""+queue.dequeue());
                                      table.addView(tv);

                                  }
                                  //table.addView(tv);
                              }catch (Exception e){
                                  e.printStackTrace();
                              }*/

                              try {
                                  queue = new LinkListProductQueue();
                                  Mst_ProductMaster product= new Mst_ProductMaster();
                                  DBHelper db = new DBHelper(activity.getBaseContext());
                                  Cursor res = db.getData(query);//return tupple id=1;


                                  list = new ArrayList<HashMap<String, String>>();
                                  while (res.moveToNext()) {
                                      row_count++;
                                      product.setItemCode(res.getString(res.getColumnIndex("ItemCode")));//res.getColumnIndex("itemcode")
                                      product.setBrand(res.getString(res.getColumnIndex("Brand")));
                                      product.setUnitSize(res.getInt(res.getColumnIndex("UnitSize")));
                                      product.setSellingPrice(res.getFloat(res.getColumnIndex("SellingPrice")) );
                                      product.setRetailPrice(res.getFloat(res.getColumnIndex("RetailPrice")) );

                                      //populateList(product);
                                      double sellp = Double.parseDouble(new DecimalFormat("##.##").format(product.getSellingPrice()));
                                      double retailp = Double.parseDouble(new DecimalFormat("##.##").format(product.getRetailPrice()));

                                      HashMap<String, String> hashmap = new HashMap<String, String>();
                                      hashmap.put(FIRST_COLUMN, product.getItemCode());
                                      hashmap.put(SECOND_COLUMN, product.getBrand());
                                      hashmap.put(THIRD_COLUMN, ""+product.getUnitSize());
                                      hashmap.put(FOURTH_COLUMN, ""+sellp);
                                      hashmap.put(Fifth_COLUMN, ""+retailp);
                                      list.add(hashmap);


                                      //queue.enqueue(product);
                                      //prdList.add(product);
                                     // if(row_count<200)
                                      //update(product,v);


                                  }
                                  db.close();

                                  ListView listView=v;
                                  //populateList();
                                  ListViewAdapter adapter=new ListViewAdapter(activity,list);
                                  listView.setAdapter(adapter);
                                  //listView.getAdapter().getView(1,null,null).setBackgroundColor(Color.GRAY);

                                 /* Toast.makeText(c, "Size"+queue.size(), Toast.LENGTH_SHORT).show();
                                  String sb = null;
                                    while (!prdList.isEmpty()){
                                        sb+=prdList.get(0);
                                    }
                                  Toast.makeText(c, "SB"+sb, Toast.LENGTH_SHORT).show();*/
                                  // setListView();
                                  ///Toast.makeText(DisplayProductTableActivity.this, "RowCount<1:"+row_count, Toast.LENGTH_SHORT).show();
                                  if(row_count<1){
                                      //Toast.makeText(DisplayProductTableActivity.this, "RowCount<1:"+row_count, Toast.LENGTH_SHORT).show();

                                      TextView tr_emty_msg=new TextView(c);
                                      tr_emty_msg.setText("No result to preview");

                                      /*ArrayList<HashMap<String, String>> msgList = null;
                                      HashMap hm = new HashMap<String, String>();
                                      hm.put("Msg","No Result To View");
                                      msgList.add(hm);
                                      ListViewAdapter msgAdapter=new ListViewAdapter(activity,msgList);*/
                                      ArrayAdapter<String> msgAdapter = new ArrayAdapter<String>(c,
                                              android.R.layout.simple_list_item_1, android.R.id.text1, values);
                                      listView.setAdapter(msgAdapter);
                                  }
                                  row_count=0;


                                  //return product;
                              }catch (Exception e){
                                  e.printStackTrace();
                                  //btnviewall.setText(e.getMessage());
                              }

                              //final ScrollView sv = (ScrollView) activity.findViewById(R.id.scrollView);
                             /*sv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                                 @Override
                                  public void onScrollChanged() {
                                     // Toast.makeText(c,"ProdiclistSize"+prdList.size(), Toast.LENGTH_SHORT).show();


                                  }
                              });*/
                          }


                      });



    }
    private void populateList(Mst_ProductMaster pm) {

        list = new ArrayList<HashMap<String, String>>();

        Toast.makeText(c, "QueueSize"+queue.dequeue().getItemCode(), Toast.LENGTH_SHORT).show();
        // while(!queue.isEmpty()) {


        HashMap<String, String> hashmap = new HashMap<String, String>();
        hashmap.put(FIRST_COLUMN, "" + pm.getItemCode());
        hashmap.put(SECOND_COLUMN, pm.getBrand());
        hashmap.put(THIRD_COLUMN, pm.getUnitName());
        hashmap.put(FOURTH_COLUMN, "" + pm.getSellingPrice());
        hashmap.put(Fifth_COLUMN, "" + pm.getRetailPrice());
        list.add(hashmap);
        //}

    }

    private  void  update(Mst_ProductMaster pm,ListView v) {
        ListView listView =  v;
       // TableLayout table = (TableLayout) v.findViewById(R.id.table_product);

        /*add style to table row*/
        ContextThemeWrapper wrappedContext = new ContextThemeWrapper(c, R.style.pending_customer_row);
        //add row
        TableRow tr = new TableRow(c);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        /*add style to table row*/

        if(row_count%2!=0) {
            wrappedContext = new ContextThemeWrapper(c, R.style.pending_customer_odd_row);
        }

        //tv.setLayoutParams(param);
        /*add extra cutomer method*/
        LinearLayout col_1=new LinearLayout(c);
        LinearLayout col_2=new LinearLayout(c);
        LinearLayout col_3=new LinearLayout(c);
        LinearLayout col_4=new LinearLayout(c);
        LinearLayout col_5=new LinearLayout(c);

        col_1.setOrientation(LinearLayout.VERTICAL);
        col_2.setOrientation(LinearLayout.VERTICAL);
        col_3.setOrientation(LinearLayout.VERTICAL);
        col_4.setOrientation(LinearLayout.VERTICAL);
        col_5.setOrientation(LinearLayout.VERTICAL);



        TableRow.LayoutParams  col_param=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,0.1f);
        TableRow.LayoutParams  col_param_wide=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,0.2f);
        col_param.weight=1f;
        col_param_wide.weight=2f;
        //col_param.width=150;
        //col_param_wide.width=350;


        col_1.setLayoutParams(col_param);
        col_2.setLayoutParams(col_param_wide);
        col_3.setLayoutParams(col_param);
        col_4.setLayoutParams(col_param);
        col_5.setLayoutParams(col_param);
        /*end addextra customer method*/

        //add coloum_item_code
        TextView tv = new TextView(wrappedContext,null,0);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv.setText(pm.getItemCode());
        tv.setGravity(Gravity.LEFT);
        //add coloum_brand
        TextView tv_brand = new TextView(wrappedContext,null,0);
        tv_brand.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_brand.setText(pm.getBrand());
        tv_brand.setGravity(Gravity.LEFT);

        //TextView th_prdct=(TextView) findViewById(R.id.tv_product_col_title);
        //tv.setWidth(th_prdct.getWidth());
        //tv.setText("Entry-1");

        //add coloum_unitsize
        TextView tv_unitsize = new TextView(wrappedContext,null,0);
        tv_unitsize.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_unitsize.setText(" "+pm.getUnitSize());


        //add coloum_sellingprice
        TextView tv_sellingprice = new TextView(wrappedContext,null,0);
        tv_sellingprice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        String  sp= ""+pm.getSellingPrice();
        //String[] sellp_str=sp.split(".");
        //String[] decimal_sp=sellp_str[1].split("");
        double sellp = Double.parseDouble(new DecimalFormat("##.##").format(pm.getSellingPrice()));
        tv_sellingprice.setText(""+sellp);
        tv_sellingprice.setGravity(Gravity.RIGHT);

        //add coloum_sellingprice
        TextView tv_retailprice = new TextView(wrappedContext,null,0);
        tv_retailprice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        double retailp = Double.parseDouble(new DecimalFormat("##.##").format(pm.getRetailPrice()));
        tv_retailprice.setText(" " + retailp);
        tv_retailprice.setGravity(Gravity.RIGHT);


        /*TextView tv2 = new TextView(wrappedContext,null,0);
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv2.setText("Entry-2");*/



        col_1.addView(tv);
        col_2.addView(tv_brand);
        col_3.addView(tv_unitsize);
        col_4.addView(tv_sellingprice);
        col_5.addView(tv_retailprice);

        //add coloums to row
        tr.addView(col_1);
        tr.addView(col_2);
        tr.addView(col_3);
        tr.addView(col_4);
        tr.addView(col_5);

        table.addView(tr);
      // listView.addView(tr);


    }
}
