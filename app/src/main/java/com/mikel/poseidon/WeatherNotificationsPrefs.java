package com.mikel.poseidon;

import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;

public class WeatherNotificationsPrefs extends AppCompatActivity {

    EditText minT;
    EditText maxT;
    SharedPreferences mPrefs;
    Switch simpleSwitch;
    RadioGroup radio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_notifications_prefs);


        mPrefs = this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        mPrefs.getInt("minT", -1);
        mPrefs.getInt("maxT", -1);

        // initiate a Switch
        simpleSwitch = (Switch) findViewById(R.id.weatherSwitch);

        simpleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // do something, the isChecked will be
            // true if the switch is in the On position
            //Toast.makeText(getApplicationContext(), String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean("wn", isChecked);
            editor.apply();

            if (isChecked) {
                simpleSwitch.setText("Weather notifications: ON");
            } else simpleSwitch.setText("Weather notifications: OFF");

        });


        radio = (RadioGroup) findViewById(R.id.toggle);
        radio.setOnCheckedChangeListener((group, checkedId) -> {
            // checkedId is the RadioButton selected
            //Toast.makeText(getApplicationContext(), String.valueOf(checkedId), Toast.LENGTH_SHORT).show();


            View radioButton = radio.findViewById(checkedId);
            int index = radio.indexOfChild(radioButton);


            switch (index) {
                case 0: // first button

                    celsius();

                    break;
                case 1: // secondbutton

                    faren();

                    break;
            }

        });


        minT = (EditText)findViewById(R.id.minTemp);
        maxT = (EditText)findViewById(R.id.maxTemp);


        Button set = (Button)findViewById(R.id.setTemp);
        set.setOnClickListener(v -> {
            if(!"".equals(minT.getText().toString())) {
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putInt("minT", Integer.parseInt(minT.getText().toString()));
                editor.apply();
            }
            if(!"".equals(maxT.getText().toString())){
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putInt("maxT", Integer.parseInt(maxT.getText().toString()));
                editor.apply();
            }

            Toast.makeText(getApplicationContext(), "SET", Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    protected void onResume() {
        super.onResume();


        if(mPrefs.getString("tempUnit", "").equals("celsius")){
            celsius();
        }else faren();

        int min = mPrefs.getInt("minT", 0);
        int max = mPrefs.getInt("maxT", 0);
        if(min != 0) {
            minT.setText(String.valueOf(min));
            maxT.setText(String.valueOf(max));
//            maxT.setText(mPrefs.getInt("maxT", 0));
        }




    }

    private void celsius(){
        RadioButton rb = (RadioButton)findViewById(R.id.celsius);
        rb.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ToolbarTextColor));
        rb.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.HeaderColor));

        RadioButton far = (RadioButton)findViewById(R.id.faren);
        far.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.enter_weight));
        far.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.StatusBarColor));

        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("tempUnit", "celsius" );
        editor.apply();

    }

    private void faren(){
        RadioButton rb2 = (RadioButton)findViewById(R.id.celsius);
        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.enter_weight));
        rb2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.StatusBarColor));

        RadioButton far2 = (RadioButton)findViewById(R.id.faren);
        far2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ToolbarTextColor));
        far2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.HeaderColor));

        SharedPreferences.Editor editor2 = mPrefs.edit();
        editor2.putString("tempUnit", "faren" );
        editor2.apply();
    }

}


