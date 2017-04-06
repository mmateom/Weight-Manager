package com.mikel.poseidon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikel.poseidon.utility.BluetoothDevice;

import java.util.Collections;
import java.util.List;

/**
 * Created by mikel on 05/04/2017.
 */

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceViewHolder> {

    private List<BluetoothDevice> mBluetoothDevices;
    private boolean mIsEmpty;
    private static DeviceClickListener mClickListener;

    public BluetoothDeviceAdapter(List<BluetoothDevice> devices, boolean empty) {
        mBluetoothDevices = devices;
        mIsEmpty = empty;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_card_layout, parent, false);

        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        BluetoothDevice device = mBluetoothDevices.get(position);

        holder.mName.setText(device.name);

        if (!mIsEmpty) {
            holder.mMac.setText(device.macaddress + " rssi: " + device.rssi);
            if (device.checked) {
                holder.mChecked.setVisibility(View.VISIBLE);
            } else {
                holder.mChecked.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setIsEmpty(boolean isEmpty) {
        mIsEmpty = isEmpty;
    }

    @Override
    public int getItemCount() {
        return mBluetoothDevices.size();
    }

    public void addItem(BluetoothDevice device) {

        if (mIsEmpty) {
            mBluetoothDevices.clear();
            mIsEmpty = false;
        }

        mBluetoothDevices.add(device);
        Collections.sort(mBluetoothDevices);
        notifyDataSetChanged();
    }

    public void alterItem(BluetoothDevice device) {

        int i = mBluetoothDevices.indexOf(device);
        BluetoothDevice d = mBluetoothDevices.get(i);
        d.rssi = device.rssi;
        d.name = device.name;
        Collections.sort(mBluetoothDevices);
        notifyDataSetChanged();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        protected TextView mName;
        protected TextView mMac;
        protected ImageButton mChecked;

        public DeviceViewHolder(View v) {
            super(v);
            mName = (TextView) v.findViewById(R.id.title);
            mMac = (TextView) v.findViewById(R.id.macaddress);
            mChecked = (ImageButton) v.findViewById(R.id.checked);
            mChecked.setVisibility(View.INVISIBLE);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(DeviceClickListener listener) {
        mClickListener = listener;
    }

    public interface DeviceClickListener {
        void onItemClick(int position, View v);
    }
}

