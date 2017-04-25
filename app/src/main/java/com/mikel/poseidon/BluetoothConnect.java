package com.mikel.poseidon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BluetoothConnect extends AppCompatActivity {

    SharedPreferences mSettings;
    private static final String STRESS_PREFS = "StressPrefs";
    public boolean mIsPaired = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);


        mSettings = this.getSharedPreferences(STRESS_PREFS, 0);

        String device = mSettings.getString("macaddress", "");

        if (device.isEmpty()) {
            mIsPaired = false;
        }

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BluetoothDeviceActivity.class);
                startActivity(i);

            }
        });
    }
}

