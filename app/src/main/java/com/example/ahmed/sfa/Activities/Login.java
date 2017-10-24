package com.example.ahmed.sfa.Activities;
 
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ahmed.sfa.Activities.supportactivities.CheckIn;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//fixed landscape screan;

        logBtn=(AppCompatButton) findViewById(R.id.btn_login);
        upload_btn=(AppCompatButton) findViewById(R.id.btn_upload);

        pass=(EditText) findViewById(R.id.input_password);
        repID=(EditText) findViewById(R.id.input_repID);

        //hide login form
        final LinearLayout LoginBox = (LinearLayout) findViewById(R.id.LoginBox);
        LoginBox.setVisibility(View.GONE);

        checkInitial_login();
        setListeners();
        sleepthread();
    }


    //wait 5 seconds before animation
    private void sleepthread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //pic.get(0).setImageDrawable(getResources().getDrawable(R.drawable.coin));
                        animation();
                    }
                });
            }
        }).start();
    }
    //animation method
    public  void animation(){
        final LinearLayout LoginBox = (LinearLayout) findViewById(R.id.LoginBox);
        try {

        }catch (Exception e){
            Toast.makeText(this, "ani_exception"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Animation animTranslate  = AnimationUtils.loadAnimation(Login.this, R.anim.translate);
        animTranslate.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                ImageView img = (ImageView) findViewById(R.id.imageView);
                img.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation arg0) { }

            @Override
            public void onAnimationEnd(Animation arg0) {
                LoginBox.setVisibility(View.VISIBLE);
                Animation animFade  = AnimationUtils.loadAnimation(Login.this, R.anim.fade);
                LoginBox.startAnimation(animFade);
            }
        });
        ImageView imgLogo = (ImageView) findViewById(R.id.imageView);
        imgLogo.setVisibility(View.VISIBLE);
        imgLogo.startAnimation(animTranslate);
        //end animation method;
    }

    private void checkInitial_login() {
        int active=0;
        try{
            DBHelper db = new DBHelper(Login.this);
            Cursor res = db.getData("select * from DeviceCheckController ");




            while (res.moveToNext()) {
                String isActive = res.getString(res.getColumnIndex("ACTIVESTATUS"));
                if (isActive.equals("YES")){
                    active=1;
                }

            }
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(active<1){
            Intent initiallogin=new Intent(Login.this,InitialLogin.class);
            Login.this.startActivity(initiallogin);
        }
    }

    private void setListeners(){
        /*upload_btn listener*/
        /*logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                jsonSendToWeb();
            }
         });*/
        /*end listenr*/

        /*loginbutton listener*/
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

               // Toast.makeText(Login.this, "OnCLick detected", Toast.LENGTH_SHORT).show();
                String passWord;
                passWord=pass.getText().toString();
                String rep_id=repID.getText().toString();
                //Toast.makeText(Login.this,">"+passWord,Toast.LENGTH_LONG).show();


                try {
                    DBHelper db = new DBHelper(Login.this);
                    Cursor res = db.getData("select * from DeviceCheckController where Password='"+passWord+"'");
                    String isActive = "NO";
                    String isPassCorrect = "notcorrect";


                        while (res.moveToNext()) {
                            isActive = res.getString(res.getColumnIndex("ACTIVESTATUS"));
                            isPassCorrect = res.getString(res.getColumnIndex("Password"));

                        }

//                        Toast.makeText(Login.this, ":" + isActive, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Login.this, ":" + isPassCorrect, Toast.LENGTH_LONG).show();

                        if (isActive !="NO") {
                                Intent ui=new Intent(Login.this,CheckIn.class);
                                Login.this.startActivity(ui);
                                //Toast.makeText(Login.this, "<>"+isActive+"??"+isPassCorrect, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Login.this, "User Not Yet Activated", Toast.LENGTH_SHORT).show();
                        }
                    if(isPassCorrect.equals(passWord)){
                        //Toast.makeText(Login.this, "password check", Toast.LENGTH_SHORT).show();
                    }


                }catch (Exception e){
                    Intent ui=new Intent(Login.this,InitialLogin.class);
                    Login.this.startActivity(ui);
                    Toast.makeText(Login.this, "err:"+e.getMessage(), Toast.LENGTH_SHORT).show();
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
