package com.example.ahmed.sfa.Activities.supportactivities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import com.example.ahmed.sfa.Activities.DisplayProductTableActivity;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.models.Mst_ProductMaster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Created by DELL on 8/26/2017.
 */

public class TableThread extends Thread {
    Context c;
    ListView v;
    TableLayout table ;


    private class LinkListProductQueue{

        private LinkedList<Object> data = new LinkedList<Object>();

        public void enqueue(Object item) {
            data.addLast(item);
            //System.out.println("Added: " + item);
        }

        public Object dequeue() {
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



    public  TableThread(Context context, ListView listview,TableLayout table){
        this.c  = context;
        this.v  = listview ;
        this.table = table;
    }
    public void run(){
        ListView listView;
        int count = 0 ;
        listView = v;

        ArrayList<String> values = new ArrayList<>() ;
        LinkListProductQueue queue = new LinkListProductQueue();


        for (int i=0;i<1000;i++){
            values.add( i+"add");
            queue.enqueue(i+"-queue");
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);

        table.removeAllViews();

        ContextThemeWrapper wrappedContext = new ContextThemeWrapper(c, R.style.pending_customer_row);




        try {
           while (!queue.isEmpty()){
               TextView tv = new TextView(wrappedContext,null,0);
               tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

               tv.setText(""+queue.dequeue());
                table.addView(tv);
            }
            //table.addView(tv);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
