package com.mikel.poseidon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.mikel.poseidon.R.id.delete_2;
import static com.mikel.poseidon.R.id.delete_steps;
import static com.mikel.poseidon.R.id.setbtn_becareful;
import static com.mikel.poseidon.R.id.setbtn_good;
import static com.mikel.poseidon.R.id.setbtn_risk;

public class SetGraphLimits extends AppCompatActivity {


    Button deletebtn, deletebtn_steps, set_risk,set_becareful,set_good;
    EditText min_risk_edit, max_risk_edit;
    EditText min_becareful_edit, max_becareful_edit;
    EditText min_good_edit, max_good_edit;

    float new_Risk_Min, new_Risk_Max, riskMin, riskMax;
    float new_Becareful_Min, new_Becareful_Max, becarefulMin, becarefulMax;
    float new_Good_Min, new_Good_Max, goodMin, goodMax;

    public static String sharedPrefs = "com.mikel.poseidon";
    public String min_key_risk = "min_risk";
    public String max_key_risk = "max_risk";

    public String min_key_becareful = "min_becareful";
    public String max_key_becareful = "max_becareful";

    public String min_key_good = "min_good";
    public String max_key_good = "max_good";


    private SharedPreferences mSharedPrefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_graph_limits);


        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {

                Intent home_intent = new Intent(SetGraphLimits.this, MainActivity.class);
                home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home_intent);
            }
        });




        min_risk_edit = (EditText) findViewById(R.id.min_risk);
        max_risk_edit = (EditText) findViewById(R.id.max_risk);

        min_becareful_edit = (EditText) findViewById(R.id.min_becareful);
        max_becareful_edit = (EditText) findViewById(R.id.max_becareful);

        min_good_edit = (EditText) findViewById(R.id.min_good);
        max_good_edit = (EditText) findViewById(R.id.max_good);

        ////initialize limit sharedpreferences instances

        mSharedPrefs = this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);

        //Risk

        mSharedPrefs.getFloat(min_key_risk, 0);
        mSharedPrefs.getFloat(max_key_risk, 0);


        //Be careful
        mSharedPrefs.getFloat(min_key_becareful, 0);
        mSharedPrefs.getFloat(max_key_becareful, 0);

        //Good
        mSharedPrefs.getFloat(min_key_good, 0);
        mSharedPrefs.getFloat(max_key_good, 0);


        set_risk = (Button) findViewById(setbtn_risk);
        set_becareful = (Button) findViewById(setbtn_becareful);
        set_good = (Button) findViewById(setbtn_good);


        //when click set button store values to retrieve from Graph.java
        set_risk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                riskMin = Float.parseFloat(min_risk_edit.getText().toString());
                riskMax = Float.parseFloat(max_risk_edit.getText().toString());


                //When saving
                SharedPreferences.Editor editor_risk_min = mSharedPrefs.edit();
                editor_risk_min.putFloat(min_key_risk, riskMin);
                editor_risk_min.apply();

                SharedPreferences.Editor editor_risk_max = mSharedPrefs.edit();
                editor_risk_max.putFloat(max_key_risk, riskMax);
                editor_risk_max.apply();


                Toast.makeText(SetGraphLimits.this, "Changes applied", Toast.LENGTH_SHORT).show();
            }
        });


        //Be careful
        set_becareful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                becarefulMin = Float.parseFloat(min_becareful_edit.getText().toString());
                becarefulMax = Float.parseFloat(max_becareful_edit.getText().toString());


                //When saving
                SharedPreferences.Editor editor_becareful_min = mSharedPrefs.edit();
                editor_becareful_min.putFloat(min_key_becareful, becarefulMin);
                editor_becareful_min.apply();

                SharedPreferences.Editor editor_becareful_max = mSharedPrefs.edit();
                editor_becareful_max.putFloat(max_key_becareful, becarefulMax);
                editor_becareful_max.apply();


                Toast.makeText(SetGraphLimits.this, "Changes applied", Toast.LENGTH_SHORT).show();
            }
        });

        //Good
        set_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goodMin = Float.parseFloat(min_good_edit.getText().toString());
                goodMax = Float.parseFloat(max_good_edit.getText().toString());


                //When saving
                SharedPreferences.Editor editor_good_min = mSharedPrefs.edit();
                editor_good_min.putFloat(min_key_good, goodMin);
                editor_good_min.apply();

                SharedPreferences.Editor editor_good_max = mSharedPrefs.edit();
                editor_good_max.putFloat(max_key_good, goodMax);
                editor_good_max.apply();


                Toast.makeText(SetGraphLimits.this, "Changes applied", Toast.LENGTH_SHORT).show();
            }
        });








    }


    //============================
    //          Methods
    //============================


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



}


