package com.mikel.poseidon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikel.poseidon.utility.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikel on 05/04/2017.
 */

public class BluetoothDeviceFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private BluetoothDeviceAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<BluetoothDevice> mDevices;
    private BluetoothDeviceActivity mActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceData) {

        View rootView = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDevices = getDevices(null);
        mAdapter = new BluetoothDeviceAdapter(mDevices, true);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public void onResume() {
        super.onResume();
        mActivity = (BluetoothDeviceActivity) getActivity();
        mAdapter.setOnItemClickListener(new BluetoothDeviceAdapter.DeviceClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                BluetoothDevice device = mDevices.get(position);
                int size = mDevices.size();

                for (int i = 0; i < size; i++) {
                    if (i != position) {
                        BluetoothDevice d = mDevices.get(i);
                        d.checked = false;

                    }
                }

                if (device.checked) {
                    device.checked = false;
                    mActivity.setMacAddress("");
                } else {
                    device.checked = true;
                    mActivity.setMacAddress(device.macaddress);
                }

                mAdapter.notifyDataSetChanged();
            }
        });

        mActivity.startScanning(true);
    }

    private List<BluetoothDevice> getDevices(List<BluetoothDevice> devices) {

        if (devices == null) {
            devices = new ArrayList<>();
        }

        BluetoothDevice device = new BluetoothDevice();
        device.name = "No devices found...";
        device.macaddress = "";
        devices.add(device);

        return devices;
    }

    public void foundDevice(final BluetoothDevice device) {

        if (!mDevices.contains(device)) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.addItem(device);
                }
            });
        } else {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.alterItem(device);
                }
            });
        }
    }

    public void clearDevices() {
        mAdapter.setIsEmpty(true);
        mDevices.clear();
        mDevices = getDevices(mDevices);
        mAdapter.notifyDataSetChanged();
    }
}

