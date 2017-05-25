package com.example.ahmed.sfa.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.service.JsonObjGenerate;
import com.example.ahmed.sfa.service.JsonRequestListerner;
import com.example.ahmed.sfa.service.SendDeviceDetails;
import com.example.ahmed.sfa.service.SyncReturn;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements JsonRequestListerner {
    AppCompatButton logBtn;
    AppCompatButton upload_btn;
    EditText pass;
    EditText repID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_login);

        logBtn=(AppCompatButton) findViewById(R.id.btn_login);
        upload_btn=(AppCompatButton) findViewById(R.id.btn_upload);

        pass=(EditText) findViewById(R.id.input_password);
        repID=(EditText) findViewById(R.id.input_repID);
        setListeners();
    }


    private void setListeners(){
        /*upload_btn listener*/
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                jsonSendToWeb();
            }
         });
        /*end listenr*/

        /*loginbutton listener*/
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
               /**/
                Toast.makeText(Login.this, "OnCLick detected", Toast.LENGTH_SHORT).show();
                String passWord;
                passWord=pass.getText().toString();
                String rep_id=repID.getText().toString();
                Toast.makeText(Login.this,">"+passWord,Toast.LENGTH_LONG).show();

                Toast.makeText(Login.this,"not null cursor",Toast.LENGTH_LONG).show();
                DBHelper db= new DBHelper(Login.this);
                Cursor res=db.getData("select * from DeviceCheckController where Password='"+passWord+"'");
                String isActive="NO";
                String isPassCorrect="notcorrect";
                if(res!=null) {
                    Toast.makeText(Login.this,":"+res.getString(res.getColumnIndex("DeviceID")),Toast.LENGTH_LONG).show();

                    while (res.moveToNext()) {
                        isActive=res.getString(res.getColumnIndex("ACTIVESTATUS"));
                        isPassCorrect=res.getString(res.getColumnIndex("Password"));

                        if(isPassCorrect.toString()==passWord.toString()) {
                           /* Intent ui=new Intent(Login.this,ManualSync.class );
                            Login.this.startActivity(ui);*/
                            Toast.makeText(Login.this,"isActive:"+isActive+"-password:"+isPassCorrect,Toast.LENGTH_LONG).show();

                        }
                    }

                    Toast.makeText(Login.this,":"+isActive,Toast.LENGTH_LONG).show();
                    Toast.makeText(Login.this,":"+isPassCorrect,Toast.LENGTH_LONG).show();

                    if(isActive!="NO"  && isPassCorrect==passWord) {
                        Intent ui=new Intent(Login.this,ManualSync.class );
                        Login.this.startActivity(ui);

                    }
                }else{
                    Toast.makeText(Login.this,"null cursor",Toast.LENGTH_LONG).show();
                }



            }
        });
    }



    private  void checkLogin(){



        }

    @Override
    public void receiveData(String result,String filter) {

    }

    public void jsonSendToWeb(){


        try {
            JSONArray jArray =new JSONArray();
            JSONObject postData = new JSONObject();

            postData.put("BrandID","13");
            postData.put("PrincipleID", "12");
            postData.put("Principle", "VASMOL");
            postData.put("MainBrand", "VASMOL");
            postData.put("Activate", "0");

            jArray.put(postData);

         /*postData.put("manufacturer", manufacturer.getText().toString());
         postData.put("location", location.getText().toString());
         postData.put("type", type.getText().toString());
         postData.put("deviceID", deviceID.getText().toString());*/

            SendDeviceDetails sendDev = new SendDeviceDetails();
            //sendDev.setContext(this);
            sendDev.execute("http://192.168.56.1:8080/myserver/json_webservice.php", jArray.toString());
        }catch (Exception e){
            Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
}
