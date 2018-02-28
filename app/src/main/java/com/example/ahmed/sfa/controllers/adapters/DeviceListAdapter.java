package com.example.ahmed.sfa.controllers.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ahmed.sfa.R;

import java.util.ArrayList;

/**
 * Created by Shani on 07/02/2018.
 */

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private ArrayList<BluetoothDevice> devices;
    private LayoutInflater inflater;
    private int resourceId;


    public DeviceListAdapter(Context context, int resourceId, ArrayList<BluetoothDevice> btDevices) {
        super(context, resourceId, btDevices);

        this.devices = btDevices;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resourceId = resourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(this.resourceId, null);

        BluetoothDevice device = devices.get(position);
        if (devices != null) {
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView macAddress = (TextView) convertView.findViewById(R.id.macAddress);

            if (name != null) {
                name.setText(device.getName());
            }
            if (macAddress != null) {
                macAddress.setText(device.getAddress());
            }

        }
        return convertView;
    }
}
