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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikel.poseidon.R;
import com.mikel.poseidon.preferences.BluetoothDevice;

import java.util.Collections;
import java.util.List;

/**
 * Adapter for the cardview of different visible bluetooth devices
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
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
