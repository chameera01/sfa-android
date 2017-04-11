package com.example.ahmed.sfa.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by DELL on 3/27/2017.
 */

public class JsonObjGenerate {
    InputStream inputStream = null;
    String result = "";
    HttpURLConnection httpUrlConnection;
    String link;
    private JsonRequestListerner mlistener;
    private String filterType;

    public JsonObjGenerate(String webLink,JsonRequestListerner mListener){
        link=webLink;
        mlistener=mListener;

    }
    //create HttpClient
    public String createConnection() {
        try {
            URL url = null;
            url = new URL(link);

            httpUrlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = httpUrlConnection.getInputStream();

            if (in != null) {
                result = convertInputStreamToString(in);

            } else {
                result = "did not work";
            }
            httpUrlConnection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public  JsonRequestListerner getRequestListener(){

       return mlistener;

    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine())!= null){
            result += line;
        }
        inputStream.close();
        return result;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }
}
