package com.example.ahmed.sfa.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.service.JsonObjGenerate;
import com.example.ahmed.sfa.service.JsonRequestListerner;
import com.example.ahmed.sfa.service.SendDeviceDetails;
import com.example.ahmed.sfa.service.SyncReturn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements JsonRequestListerner {
    AppCompatButton logBtn;
    AppCompatButton upload_btn;
    EditText pass;
    EditText repID;
    TextView result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_login);

        logBtn=(AppCompatButton) findViewById(R.id.btn_login);
        upload_btn=(AppCompatButton) findViewById(R.id.btn_upload);

        pass=(EditText) findViewById(R.id.input_password);
        repID=(EditText) findViewById(R.id.input_repID);
        result=(TextView) findViewById(R.id.result_tv);

        setListeners();
    }


    private void setListeners(){
        /*upload_btn listener*/
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                //jsonSendToWeb();
                josnPost("https://httpbin.org/post");
            }
         });
        /*end listenr*/

        /*loginbutton listener*/
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
               /**/
                final String passWord=pass.getText().toString();
                String rep_id=repID.getText().toString();
                Toast.makeText(Login.this,">"+passWord,Toast.LENGTH_LONG).show();

                Toast.makeText(Login.this,"not null cursor",Toast.LENGTH_LONG).show();
                DBHelper db= new DBHelper(Login.this);
                Cursor res=db.getData("select * from DeviceCheckController where Password='"+passWord+"'");
                String isActive="NO";
                String isPassCorrect="notcorrect";
                if(res!=null) {
                    Toast.makeText(Login.this,":"+res.toString(),Toast.LENGTH_LONG).show();

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
        Toast.makeText(this, "method_jsonSendTOweb_called", Toast.LENGTH_SHORT).show();

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

            //SendDeviceDetails sendDev = new SendDeviceDetails(Login.this);
            //sendDev.setContext(this);
            //sendDev.execute("http://192.168.56.1:8080/myserver/json_webservice.php", jArray.toString());

/*            SendJsonToServer sendJson=new  SendJsonToServer();
            sendJson.execute("http://httpbin.org/post",jArray.toString());*/

        }catch (Exception e){
            Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
    public void josnPost(String weblink){
    /*try{
        AsyncTaskmy asyncT = new AsyncTaskmy();
        asyncT.execute();

    }catch (Exception e){
        Toast.makeText(this, "Async Class:"+e.getMessage(), Toast.LENGTH_SHORT).show();
    }*/

        try {
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("Title", "VolleyApp Android Demo");
            jsonBody.put("Author", "BNK");
            jsonBody.put("Date", "2015/08/26");
            final String requestBody = jsonBody.toString();

            Toast.makeText(this, "link:"+weblink, Toast.LENGTH_SHORT).show();
            String url=weblink;//"https://httpbin.org/post";

            StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    result.setText(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    result.setText(error.toString());
                }
            }) {
                @Override
                public Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("name", jsonBody.toString());
                    return params;
                }
                @Override
                public String getBodyContentType() {
                    return String.format("application/json; charset=utf-8");
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                requestBody, "utf-8");
                        return null;
                    }
                }
            };
            // MySingleton.getInstance(this).addToRequestQueue(stringRequest);
//        Volley.addToRequestQueue(stringRequest);
            Volley.newRequestQueue(this).add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*private class SendJsonToServer extends AsyncTask<String, Void, String> {
        Context contxt= Login.this;
        @Override
        protected String doInBackground(String... params) {
            String data = "";
            try {
                Toast.makeText(contxt, "line1", Toast.LENGTH_LONG).show();
            }catch(Exception e){
                e.printStackTrace();
            }
            HttpURLConnection httpURLConnection = null;
            try {
                Toast.makeText(contxt, "Came Inside doInBackGround", Toast.LENGTH_SHORT).show();

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                //wr.writeBytes(params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                Toast.makeText(contxt, "AsyncTask:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                // e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            Toast.makeText(contxt,result,Toast.LENGTH_LONG).show();

        }

    }*/

}
