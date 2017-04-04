package com.example.ahmed.sfa.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.service.JsonFilter_Send;
import com.example.ahmed.sfa.service.JsonObjGenerate;
import com.example.ahmed.sfa.service.JsonRequestListerner;
import com.example.ahmed.sfa.service.SyncReturn;

public class ManualSync extends AppCompatActivity implements JsonRequestListerner {
    ImageView ivProductSync;
    ImageView ivrepSync;
    ImageView ivSupplierSync;
    ImageView ivProductBradnSync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_sync);

        ivProductSync=(ImageView) findViewById(R.id.iv_productSync);
        ivrepSync=(ImageView) findViewById(R.id.iv_repSync);
        ivSupplierSync=(ImageView) findViewById(R.id.iv_SupplierSync);
        ivProductBradnSync=(ImageView) findViewById(R.id.iv_ProductBradnSync);


        setListeners();
    }

    public  void setListeners(){
        final LinearLayout app_layer = (LinearLayout) findViewById (R.id.product_layout);
        app_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManualSync.this,AndroidDatabaseManager.class);
                //Intent intent = new Intent(activity, AndroidDatabaseManager.class);
                ManualSync.this.startActivity(intent);
                Toast.makeText(ManualSync.this, "listenerSet", Toast.LENGTH_LONG).show();
            }
        });

        /*app_layer.setOnHoverListener( new View.OnHoverListener(){

            @Override
            public boolean onHover(View v, MotionEvent event) {
                app_layer.setBackgroundColor(241);
                return false;
            }
        });*/


        ivProductSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"clckedSync",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/ProductDetails/SelectProductDetails?DeviceID=T1&RepID=93",ManualSync.this);
                    jObjGen.setFilterType("ProductDetails");
                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });
        /**SynceRep**/
        ivrepSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"clckedSync",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/GetRepDetails/SelectGetRepDetails?DeviceID=T1&RepID=93",ManualSync.this);
                    jObjGen.setFilterType("RepDetails");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });
        /**Syncronize Supplier Details**/
        ivSupplierSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualSync.this,"clckedSync",Toast.LENGTH_LONG).show();
                try {
                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/Mst_SupplierTable/SelectProductMst_SupplierTable?DeviceID=T1&RepID=93",ManualSync.this);
                    jObjGen.setFilterType("SupplierTable");

                    SyncReturn io = new SyncReturn();
                    io.execute(jObjGen);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ManualSync.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    @Override
    public void receiveData(String result,String filter) {
        Toast.makeText(this, "came inside recieve data", Toast.LENGTH_LONG).show();
        if(result!=null){
            String josnString=result;
            Toast.makeText(this, "result:" + josnString, Toast.LENGTH_LONG).show();
            try{
                JsonFilter_Send josnFilter= new JsonFilter_Send(ManualSync.this.getApplicationContext());
                josnFilter.filterJsonData(josnString,filter);

            }catch (Exception e) {
                Toast.makeText(this,"RecieveData:"+ e.getMessage(),Toast.LENGTH_LONG ).show();
            }
        }else{
            Toast.makeText(this,"is nulllll",Toast.LENGTH_LONG ).show();
        }
        Toast.makeText(ManualSync.this,"method_complete",Toast.LENGTH_LONG).show();
    }
}
