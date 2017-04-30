package com.example.ahmed.sfa.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.sfa.Activities.Login;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by DELL on 4/8/2017.
 */

public class SendDeviceDetails  extends AsyncTask<String, Void, String> {
    Context contxt;
    public SendDeviceDetails(Context c) {
        contxt=c;
    }

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


    public void setContext(Context c) {
        contxt=c;
    }
}
