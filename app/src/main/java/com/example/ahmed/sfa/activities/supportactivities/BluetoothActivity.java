package com.example.ahmed.sfa.activities.supportactivities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Dialogs.Alert;
import com.example.ahmed.sfa.activities.Home;
import com.example.ahmed.sfa.controllers.adapters.DeviceListAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "NUT";
    Thread workerThread;
    Thread printThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    private ArrayList<BluetoothDevice> btDevices;
    private DeviceListAdapter deviceAdapter;
    private ListView lvDevices;
    private String BILL;
    private Alert alert;
    private BluetoothAdapter btAdapter;
    private Dialog dialogProgress;
    private BluetoothSocket mBTSocket = null;
    private BluetoothDevice mDevice;
    private OutputStream os;
    private InputStream is;
    private String string = "-------BB's first print-------\n-------BB's second print-------";

    private DialogInterface.OnClickListener pos = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                Log.d(TAG, "inside dialog onclick try");
                closeConnection();
                Intent intent = new Intent(BluetoothActivity.this, Home.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.d(TAG, "Ex: in intent " + e.getMessage());
            }
        }
    };
    private DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            //direct back to invoice
        }
    };
    private BroadcastReceiver receiver3 = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.ECLAIR)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onreceive: ACTION_FOUND");

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btDevices.add(device);

                Log.d(TAG, "onreceive: device name-" + device.getName() + " device address- " + device.getAddress());
                deviceAdapter = new DeviceListAdapter(context, R.layout.device_listview, btDevices);
                lvDevices.setAdapter(deviceAdapter);
            }

        }
    };
    private BroadcastReceiver receiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //paired already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "receiver4: BOND_BONDED");
                    //send data to print
                    alert.showAlert("Success", "Device paired. Print?", "OK", "Cancel", successful, cancel);

                }
                //creating a pair
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "receiver4: BOND_BONDING");
                }
                //breaking a pair
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "receiver4: BOND_NONE");
                }
            }
        }
    };
    private NativeBluetoothSocket socket;
    private DialogInterface.OnClickListener successful = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            try {
                Log.d(TAG, "starting print");
                printBillToDevice(mDevice.getAddress());
//                try {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothActivity.this);
//                    builder.setTitle("Success")
//                            .setMessage("Successfully printed!")
//                            .setCancelable(false)
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    try {
//                                        Log.d(TAG, "inside dialog onclick try");
//                                        closeConnection();
//                                        Intent intent = new Intent(BluetoothActivity.this, Home.class);
//                                        startActivity(intent);
//                                    }catch (Exception e){
//                                        Log.d(TAG, "Ex: in intent "+e.getMessage());
//                                    }
//                                }
//                            });
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                }catch (Exception e){
//                    Log.d(TAG, "Ex: in alert "+e.getMessage());
//                }
            } catch (Exception e) {
                Toast.makeText(BluetoothActivity.this, "Ex: printing " + e.getMessage(), Toast.LENGTH_LONG).show();
                //again display the print button
            }

        }
    };

    private void closeConnection() throws IOException {
        try {
            stopWorker = true;
            os.close();
            is.close();
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "Disabling Bluetooth..");
                btAdapter.disable();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver3);
        unregisterReceiver(receiver4);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = 500;
        params.width = 800;
        this.getWindow().setAttributes(params);
        setFinishOnTouchOutside(false);

        alert = new Alert(this);

        lvDevices = (ListView) findViewById(R.id.devicelistview);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btDevices = new ArrayList<>();

        BILL = getIntent().getStringExtra("BILL");

        dialogProgress = new Dialog(BluetoothActivity.this);

        discover();

        //Broadcasts when bonding state changes ie: pairing
        IntentFilter pairIntent = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(receiver4, pairIntent);

        lvDevices.setOnItemClickListener(BluetoothActivity.this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void discover() {
        Log.d(TAG, "discover: Looking for unpaired devices..");

        if (btAdapter.isDiscovering()) {
            btAdapter.cancelDiscovery();
            Log.d(TAG, "discover: Canceling discovery..");

            //check BT permissions in Manifest
            checkBTPermissions();

            btAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver3, discoverDevicesIntent);

        } else {
            checkBTPermissions();

            btAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver3, discoverDevicesIntent);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission(Manifest.permission.BLUETOOTH);
            permissionCheck += this.checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN);
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, 1);
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check BT permissions. SDK version < Lollipop");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d(TAG, "onItemClick: Clicked on a device");

        String dname = btDevices.get(position).getName();
        String daddress = btDevices.get(position).getAddress();

        Log.d(TAG, "onItemClick: device name-" + dname + " device address- " + daddress);

        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices != null) {
            Log.d(TAG, "list size: " + pairedDevices.size());
            for (BluetoothDevice paired : pairedDevices) {
                if (paired.getAddress().equals(daddress)) {
                    Log.d(TAG, "bond status: " + btDevices.get(position).getBondState());
                    if (btDevices.get(position).getBondState() == 12) {
                        mDevice = btDevices.get(position);
                        alert.showAlert("Success", "Device is already paired. Print?", "OK", "Cancel", successful, cancel);
                    }

                } else {
                    //creates the pair
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        Log.d(TAG, "onItemClick: Trying to pair with " + dname);
                        btDevices.get(position).createBond();
                    }
                }
            }
        } else {
            Log.d(TAG, "list size: null");
            //creates the pair
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                Log.d(TAG, "onItemClick: Trying to pair with " + dname);
                btDevices.get(position).createBond();
            }
        }
    }


    public Boolean printBillToDevice(final String address) {

        try {
            printThread = new Thread(new Runnable() {
                public void run() {

                    runOnUiThread(new Runnable() {

                        public void run() {
                            dialogProgress.setTitle("Connecting...");
                            dialogProgress.show();
                        }

                    });

                    btAdapter.cancelDiscovery();
                    Log.d(TAG, "after cancelDiscovery");

                    try {
                        socket = new NativeBluetoothSocket(mBTSocket);

                        BluetoothDevice mdevice = btAdapter.getRemoteDevice(address);
                        Log.d(TAG, "address: " + address);
                        try {

//                      mBTSocket = mdevice.createRfcommSocketToServiceRecord(uuid);

                            Method m = mdevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                            Log.d(TAG, "after get method");
                            mBTSocket = (BluetoothSocket) m.invoke(mdevice, 1);
                            btAdapter.cancelDiscovery();

                        } catch (Exception e) {
                            Log.d(TAG, "Ex: creating socket" + e.getMessage());
                        }
                        try {
                            Log.d(TAG, "connecting to bt socket");
                            mBTSocket.connect();
                        } catch (Exception e) {
                            Log.d(TAG, "Ex: connecting to socket-" + e.getMessage());

                            //try the fallback
                            try {
                                socket = new FallbackBluetoothSocket(socket.getUnderlyingSocket());
                                Thread.sleep(500);
                                Log.d(TAG, "connecting to fallback socket");
                                socket.connect();

                            } catch (FallbackException e1) {
                                Toast.makeText(BluetoothActivity.this, "Could not initialize FallbackBluetoothSocket classes.", Toast.LENGTH_LONG).show();
                                Log.w("BT", "Could not initialize FallbackBluetoothSocket classes.", e);
                            } catch (InterruptedException e1) {
                                Toast.makeText(BluetoothActivity.this, e1.getMessage(), Toast.LENGTH_LONG).show();
                                Log.w("BT", e1.getMessage(), e1);
                            } catch (IOException e1) {
                                Toast.makeText(BluetoothActivity.this, "Fallback failed. Cancelling.", Toast.LENGTH_LONG).show();
                                Log.w("BT", "Fallback failed. Cancelling.", e1);
                            }

                        }
//                        if (mBTSocket.isConnected()) {
//                            Log.d(TAG, "-----inside connectToDevice: after connecting to socket-------");
//                        } else {
//                            Log.d(TAG, "-----not connected-------");
//                        }
                        os = mBTSocket.getOutputStream();
                        is = mBTSocket.getInputStream();


                        Log.d(TAG, "-----Before beginListenForData-------");
                        beginListenForData();
                        Log.d(TAG, "-----After beginListenForData-------");


                        Log.d(TAG, "before write to print");
                        try {
                            os.write(BILL.getBytes());
                        } catch (Exception e) {
                            Log.d(TAG, "Ex: in writing to device " + e.getMessage());
                            Toast.makeText(BluetoothActivity.this, "Ex: in writing to device " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        Log.d(TAG, "after writing");
                        try {
                            os.flush();
                        } catch (Exception e) {
                            Log.d(TAG, "Ex: in flush " + e.getMessage());
                            Toast.makeText(BluetoothActivity.this, "Ex: in flush " + e.getMessage(), Toast.LENGTH_LONG).show();
                        } finally {
                            if (btAdapter != null) {
                                btAdapter.cancelDiscovery();
                            }
                            Log.d(TAG, "before closing socket");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        mBTSocket.close();
                                    } catch (IOException e) {
                                        Log.d(TAG, "Ex: closing socket-" + e.getLocalizedMessage());
                                        e.printStackTrace();
                                    }
                                    Log.d(TAG, "after closing socket");
                                }
                            }, 100);

                        }

                        setResult(RESULT_OK);
                        Log.d(TAG, "result: " + RESULT_OK);
                        //finish();

                    } catch (Exception e) {

                        e.printStackTrace();
                        Log.d(TAG, "Ex: " + e.getMessage());
                        setResult(RESULT_CANCELED);
                        //finish();
                    }
                    runOnUiThread(new Runnable() {

                        public void run() {
                            try {
                                dialogProgress.dismiss();

                                try {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothActivity.this);
                                    builder.setTitle("Success")
                                            .setMessage("Successfully printed!")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    try {
                                                        Log.d(TAG, "inside dialog onclick try");
                                                        closeConnection();
                                                        Intent intent = new Intent(BluetoothActivity.this, Home.class);
                                                        startActivity(intent);
                                                    } catch (Exception e) {
                                                        Log.d(TAG, "Ex: in intent " + e.getMessage());
                                                    }
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();


                                } catch (Exception e) {
                                    Log.d(TAG, "Ex: in alert " + e.getMessage());
                                }
                            } catch (Exception e) {
                                Log.e("Class ", "My Exe ", e);
                            }
                        }
                    });
                }

            });
            printThread.start();
            Log.d(TAG, "returning true");
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Ex: in executing thread " + e.getMessage());
            return false;
        }
    }


    /*
 * after opening a connection to bluetooth printer device,
 * we have to listen and check if a data were sent to be printed.
 */
    private Boolean beginListenForData() {

        Log.d(TAG, "----inside beginListenForData");
        try {
//            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            Log.d(TAG, "initiating worker thread");
            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {
                            Log.d(TAG, "inside while thread");
                            int bytesAvailable = 0;
                            try {
                                bytesAvailable = is.available();
//                                Log.d(TAG, "no of bytes: " + is.available());
                            } catch (IOException e) {
//                                Log.d(TAG, "no of bytes: " + is.available());
                                Log.d(TAG, "Ex: is is null or failed");
                                e.printStackTrace();
                            }

                            if (bytesAvailable > 0) {

                                Log.d(TAG, "bytesAvailable > 0");
                                byte[] packetBytes = new byte[bytesAvailable];
                                is.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        Log.d(TAG, "inside run: data" + data);

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            Log.d(TAG, "EX: trying to read bytes");
                            stopWorker = true;
                        }

                    }
                }
            });
            Log.d(TAG, "starting worker thread");
            workerThread.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Ex: listening for connection: " + e.getMessage());
            return false;
        }
    }

    public static interface BluetoothSocketWrapper {

        InputStream getInputStream() throws IOException;

        OutputStream getOutputStream() throws IOException;

        String getRemoteDeviceName();

        void connect() throws IOException;

        String getRemoteDeviceAddress();

        void close() throws IOException;

        BluetoothSocket getUnderlyingSocket();

    }


    public static class NativeBluetoothSocket implements BluetoothSocketWrapper {

        private BluetoothSocket socket;

        public NativeBluetoothSocket(BluetoothSocket tmp) {
            this.socket = tmp;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return socket.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return socket.getOutputStream();
        }

        @Override
        public String getRemoteDeviceName() {
            return socket.getRemoteDevice().getName();
        }

        @Override
        public void connect() throws IOException {
            socket.connect();
        }

        @Override
        public String getRemoteDeviceAddress() {
            return socket.getRemoteDevice().getAddress();
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }

        @Override
        public BluetoothSocket getUnderlyingSocket() {
            return socket;
        }

    }

    public static class FallbackException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public FallbackException(Exception e) {
            super(e);
        }

    }

    public class FallbackBluetoothSocket extends NativeBluetoothSocket {

        private BluetoothSocket fallbackSocket;

        public FallbackBluetoothSocket(BluetoothSocket tmp) throws FallbackException {
            super(tmp);
            try {
                Class<?> clazz = tmp.getRemoteDevice().getClass();
                Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};
                Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                Object[] params = new Object[]{Integer.valueOf(1)};
                fallbackSocket = (BluetoothSocket) m.invoke(tmp.getRemoteDevice(), params);
            } catch (Exception e) {
                throw new FallbackException(e);
            }
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return fallbackSocket.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return fallbackSocket.getOutputStream();
        }


        @Override
        public void connect() throws IOException {
            fallbackSocket.connect();
        }


        @Override
        public void close() throws IOException {
            fallbackSocket.close();
        }

    }
}
