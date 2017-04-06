package com.mikel.poseidon;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mikel.poseidon.utility.BluetoothDevice;

public class BluetoothDeviceActivity extends AppCompatActivity {


    private BluetoothDeviceFragment mFragment;
    private Context mContext;
    private FragmentManager mFragManager;
    private SharedPreferences mSettings;
    private static final String STRESS_PREFS = "StressPrefs";
    private Handler mHandler = new Handler();
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private static final long SCAN_PERIOD = 10000;
    private String mPairedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mSettings = mContext.getSharedPreferences(STRESS_PREFS, 0);
        mFragManager = getSupportFragmentManager();
        setContentView(R.layout.activity_bluetooth_device);

        mFragment = (BluetoothDeviceFragment) mFragManager.findFragmentById(R.id.bluetoothfragment);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            this.finish();

        }

        Button rescan = (Button)findViewById(R.id.resc);
        rescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.clearDevices();
                startScanning(true);
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }

        mPairedDevice = mSettings.getString("macaddress", "");

        if (!mPairedDevice.isEmpty()) {
            BluetoothDevice bluetoothDevice = new BluetoothDevice();
            bluetoothDevice.macaddress = mPairedDevice;
            bluetoothDevice.name = "Currently not found";
            bluetoothDevice.checked = true;
            bluetoothDevice.rssi = -1000;
            mFragment.foundDevice(bluetoothDevice);
        }
    }

    public void setMacAddress(String mac) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("macaddress", mac);
        editor.commit();

        mPairedDevice = mac;
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pair_bluetooth_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == R.id.action_rescan) {
            mFragment.clearDevices();
            startScanning(true);
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void startScanning(final boolean enable) {
        if (enable) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }

                }
            }, SCAN_PERIOD);

            mScanning = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        } else {
            mScanning = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(android.bluetooth.BluetoothDevice device, int rssi, byte[] scanRecord) {
                    final BluetoothDevice bluetoothDevice = new BluetoothDevice();
                    bluetoothDevice.name = device.getName();
                    bluetoothDevice.macaddress = device.getAddress();
                    bluetoothDevice.rssi = rssi;

                    if (bluetoothDevice.macaddress.equalsIgnoreCase(mPairedDevice)) {
                        bluetoothDevice.checked = true;
                    }

                    mFragment.foundDevice(bluetoothDevice);

                }
            };
}


