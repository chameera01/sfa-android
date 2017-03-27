package com.example.ahmed.sfa.service;

import android.os.AsyncTask;

/**
 * Created by DELL on 3/27/2017.
 */

public class SyncReturn extends AsyncTask<JsonObjGenerate ,Void,String> {

    //JsonObjGenerate jObj =new JsonObjGenerate();
    JsonRequestListerner mlistener;

    @Override
    public String doInBackground(JsonObjGenerate...jObj) {
         String inStream=jObj[0].createConnection();
         mlistener =jObj[0].getRequestListener();
        return  inStream;
    }
    @Override
    public void onPostExecute(String obj){
        //return  null;
        mlistener.receiveData(obj);

    }
}