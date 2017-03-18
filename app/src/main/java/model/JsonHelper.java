package model;

import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by DELL on 3/18/2017.
 */

public class JsonHelper {

    ArrayList<String> jsonMyArray=new ArrayList();


    public  void sendPointList(ArrayList<Cursor> curList){
        String pointSet="";

        for(Cursor res:curList  ){
            pointSet=pointSet.toString()+res.toString()+","+res.toString();
        }
        new HttpAsyncTask().execute("https://roads.googleapis.com/v1/snapToRoads?path="+pointSet+"&interpolate=true&key=%20AIzaSyDTP80tr9oUgSA6Zv8ImeLj2wnsWj0MviI");

    }


    private class HttpAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls){
            return GET(urls[0]);
        }

        //onPostExecute displays the result of the asyncTask
        @Override
        protected  void onPostExecute(String result){

            //filterLoction(jsonMyArray);

        }
    }

    public /*static*/ String GET(String txtUrl){
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection httpUrlConnection;
        try{
            //create HttpClient

            URL url = null;
            try {
                url = new URL(txtUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = httpUrlConnection.getInputStream();

            if(in!=null) {
                result = convertInputStreamToString(in);
                /**/
                JSONObject jsonRootObject = null;

                try {
                    jsonRootObject = new JSONObject(result);

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray = jsonRootObject.optJSONArray("snappedPoints");

                    //Iterate the jsonArray and print the info of JSONObjects
                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String  location = jsonObject.optString("location").toString();
                        String index = jsonObject.optString("originalIndex").toString();
                        jsonMyArray.add(location);
                    }

                } catch (JSONException e) {
                    // e.printStackTrace();
                }
                /**/

            }else{
                result="did not work";
            }
            httpUrlConnection.disconnect();
        }catch (MalformedURLException ex){
            Toast.makeText(null, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }catch (IOException ex){
            Toast.makeText(null, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return result;
    }
    //
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
    //
    /*public void filterLoction(ArrayList<String> locationList) {


        JSONObject reader = null;
        ArrayList<String> list =new ArrayList();
        ArrayList<Location> loc_array=null;

        try {

            for (String myPoint : locationList) {

                reader = new JSONObject(myPoint);
                // JSONObject point = reader.getJSONObject("location");
                String latitude = reader.getString("latitude");
                String longitude = reader.getString("longitude");

                Location l=null;
                l.setLatitude(Double.parseDouble(latitude));
                l.setLongitude(Double.parseDouble(longitude));
                loc_array.add(l);

                list.add(latitude + "|" + longitude);

            }

            for(int i=0;i<loc_array.size();i++){
                float tmp= distanceReturn(((float)curentGPS.getLatitude()),((float)curentGPS.getLongitude()), (float)loc_array.get(i).getLatitude(),(float)loc_array.get(i).getLongitude());
                if(minimum_distance>tmp||i==0){
                    minimum_distance=tmp;
                }
            }

        }catch (JSONException e) {
            // outputText.setText(e.toString());

        }


    }*/
}
