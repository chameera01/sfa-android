package com.example.ahmed.sfa.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import android.os.Process;

import com.example.ahmed.sfa.activities.ServiceTest;


public class SyncService extends Service implements JsonRequestListerner{
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private Context contxt;


    @Override
    public void receiveData(String result,String filter) {
        if(result!=null){
            String josnString=result;
            Toast.makeText(this, "result" + josnString, Toast.LENGTH_LONG).show();
            try{
                JsonFilter_Send josnFilter= new JsonFilter_Send(SyncService.this.getApplicationContext());
                josnFilter.filterJsonData(josnString,"productdetails");
            }catch (Exception e) {
                Toast.makeText(this,"RecieveData:"+ e.getMessage(),Toast.LENGTH_LONG ).show();
            }
        }else{
            Toast.makeText(this,"is nulllll",Toast.LENGTH_LONG ).show();
        }
    }


    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {

            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(5000);

                /*****************/
                while (0<1){

                    JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/productdetails/SelectProductDetails?DeviceID=T1&RepID=93", SyncService.this);
                SyncReturn io = new SyncReturn();
                io.execute(jObjGen);
                Toast.makeText(SyncService.this.contxt, "SERVICE_runiing_10sec", Toast.LENGTH_LONG).show();
                    Thread.sleep(10000);
                    }
                /*****************/


            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
/**************************************comment by me********************************************/
            //stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        contxt=this.getApplicationContext();
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {

        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
