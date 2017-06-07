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

package com.mikel.poseidon.Activities.preferences;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.mikel.poseidon.Activities.utility.BluetoothDevice;
import com.mikel.poseidon.Activities.utility.BluetoothDeviceFragment;
import com.mikel.poseidon.Activities.Menu;
import com.mikel.poseidon.R;

import static com.mikel.poseidon.Activities.preferences.SetGraphLimits.sharedPrefs;


/**
 * Activity to pair bluetooth devices
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
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
        mSettings = mContext.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        mFragManager = getSupportFragmentManager();
        setContentView(R.layout.activity_bluetooth);

        //callback to home button
        ImageButton home_buttod = (ImageButton) findViewById(R.id.homebutton);
        home_buttod.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent home_inten = new Intent(BluetoothDeviceActivity.this, Menu.class);
                                              //home_intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                              home_inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                              startActivity(home_inten);
                                          }
                                      }


        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to ACCESS_FINE_LOCATION - requesting it");
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

                requestPermissions(permissions, 1);

            }
        }
        mFragment = (BluetoothDeviceFragment) mFragManager.findFragmentById(R.id.bluetoothfragment);
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

    /*@Override
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
