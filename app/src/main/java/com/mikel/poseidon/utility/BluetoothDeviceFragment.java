/*Copyright 2016 WorkStress Experiment
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.mikel.poseidon.utility;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikel.poseidon.R;
import com.mikel.poseidon.preferences.BluetoothDevice;
import com.mikel.poseidon.preferences.BluetoothDeviceActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to pair bluetooth devices
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
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
                    Log.e("BLE", "DEVICE IS CHECKED");

                } else {
                    device.checked = true;
                    mActivity.setMacAddress(device.macaddress);
                    Log.e("BLE", "DEVICE NOOOOOOO CHECKED");
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
            try {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addItem(device);
                    }
                });
            }catch (Exception e){
                Log.e("Activity", e.toString());
            }
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
