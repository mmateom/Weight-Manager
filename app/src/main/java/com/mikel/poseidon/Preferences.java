package com.mikel.poseidon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import static android.R.attr.value;
import static com.mikel.poseidon.R.id.delete_2;

import static com.mikel.poseidon.R.id.delete_steps;
import static com.mikel.poseidon.R.id.setbtn_becareful;
import static com.mikel.poseidon.R.id.setbtn_good;
import static com.mikel.poseidon.R.id.setbtn_risk;
import static java.lang.Float.parseFloat;

public class Preferences extends AppCompatActivity {


    DBHelper myDB;
    Button deletebtn, deletebtn_steps, set_risk,set_becareful,set_good;
    EditText min_risk_edit, max_risk_edit;
    EditText min_becareful_edit, max_becareful_edit;
    EditText min_good_edit, max_good_edit;

    float new_Risk_Min, new_Risk_Max;
    float new_Becareful_Min, new_Becareful_Max;
    float new_Good_Min, new_Good_Max;

    public static String RISK_MIN = "weight_prefs_risk_min";
    public static String RISK_MAX = "weight_prefs_risk_max";

    public static String BE_CAREFUL_MIN = "weight_prefs_risk_min";
    public static String BE_CAREFUL_MAX = "weight_prefs_risk_max";

    public static String GOOD_MIN = "weight_prefs_risk_min";
    public static String GOOD_MAX = "weight_prefs_risk_max";

    SharedPreferences settings_risk_min, settings_risk_max;
    SharedPreferences settings_becareful_min, settings_becareful_max;
    SharedPreferences settings_good_min, settings_good_max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {

                Intent home_intent = new Intent(Preferences.this, MainActivity.class);
                startActivity(home_intent);
            }
        });

        myDB = new DBHelper(this);


        deletebtn = (Button) findViewById(delete_2);


        //Delete weight data
        deletebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myDB.deleteData("Weight_Summary");

                Toast.makeText(Preferences.this,"Data deleted",Toast.LENGTH_LONG).show();

            }

        });


        deletebtn_steps = (Button) findViewById(delete_steps);


        //Delete steps data
        deletebtn_steps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myDB.deleteDataSteps("Steps_Summary");

                Toast.makeText(Preferences.this,"Data deleted",Toast.LENGTH_LONG).show();

            }

        });


        min_risk_edit = (EditText) findViewById(R.id.min_risk);
        max_risk_edit = (EditText) findViewById(R.id.max_risk);

        min_becareful_edit = (EditText) findViewById(R.id.min_becareful);
        max_becareful_edit = (EditText) findViewById(R.id.max_becareful);

        min_good_edit = (EditText) findViewById(R.id.min_good);
        max_good_edit = (EditText) findViewById(R.id.max_good);

        ////initialize risk limit sharedpreferences instances

        //Risk
        settings_risk_min = this.getSharedPreferences(RISK_MIN, 0);
        settings_risk_min.getFloat(RISK_MIN, 85);

        settings_risk_max = this.getSharedPreferences(RISK_MAX, 0);
        settings_risk_max.getFloat(RISK_MAX, 85);

        //Be careful
        settings_becareful_min = this.getSharedPreferences(BE_CAREFUL_MIN, 0);
        settings_becareful_min.getFloat(BE_CAREFUL_MIN, 85);

        settings_becareful_max = this.getSharedPreferences(BE_CAREFUL_MAX, 0);
        settings_becareful_max.getFloat(BE_CAREFUL_MAX, 85);

        //Good
        settings_good_min = this.getSharedPreferences(GOOD_MIN, 0);
        settings_good_min.getFloat(BE_CAREFUL_MIN, 85);

        settings_good_max = this.getSharedPreferences(GOOD_MAX, 0);
        settings_good_max.getFloat(GOOD_MAX, 85);


        set_risk = (Button) findViewById(setbtn_risk);
        set_becareful = (Button) findViewById(setbtn_becareful);
        set_good = (Button) findViewById(setbtn_good);


        //when click set button store values to retrieve from Graph.java
        set_risk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_Risk_Min = Float.parseFloat(min_risk_edit.getText().toString());
                new_Risk_Max = Float.parseFloat(max_risk_edit.getText().toString());

                //When saving
                SharedPreferences.Editor editor_risk_min = settings_risk_min.edit();
                editor_risk_min.putFloat(RISK_MIN, new_Risk_Min);
                editor_risk_min.apply();

                SharedPreferences.Editor editor_risk_max = settings_risk_max.edit();
                editor_risk_max.putFloat(RISK_MAX, new_Risk_Max);
                editor_risk_max.apply();


                /*new_Good_Min = Float.parseFloat(min_good_edit.getText().toString());
                new_Good_Max = Float.parseFloat(max_good_edit.getText().toString());

                SharedPreferences.Editor editor_good_min = settings_good_min.edit();
                editor_good_min.putFloat(GOOD_MIN, new_Good_Min );
                editor_good_min.commit();

                SharedPreferences.Editor editor_good_max = settings_good_max.edit();
                editor_good_max.putFloat(GOOD_MAX, new_Good_Max );
                editor_good_max.commit();

                new_Becareful_Min = Float.parseFloat(min_becareful_edit.getText().toString());
                new_Becareful_Max = Float.parseFloat(max_becareful_edit.getText().toString());


                SharedPreferences.Editor editor_becareful_min = settings_becareful_min.edit();
                editor_becareful_min.putFloat(BE_CAREFUL_MIN, new_Becareful_Min );
                editor_becareful_min.apply();

                SharedPreferences.Editor editor_becareful_max = settings_becareful_max.edit();
                editor_becareful_max.putFloat(BE_CAREFUL_MAX, new_Becareful_Max );
                editor_becareful_max.apply();*/





                Toast.makeText(Preferences.this, "Changes applied", Toast.LENGTH_SHORT).show();
            }
        });




    }


    //============================
    //          Methods
    //============================





    }












