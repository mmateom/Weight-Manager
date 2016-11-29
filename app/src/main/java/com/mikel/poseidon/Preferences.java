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
import static com.mikel.poseidon.R.id.min;
import static com.mikel.poseidon.R.id.setbtn;
import static java.lang.Float.parseFloat;

public class Preferences extends AppCompatActivity {


    DBHelper myDB;
    Button deletebtn, set;
    EditText min, max;

    float newMin, newMax;
    public static String RISK_MIN = "weight_prefs_risk_min";
    public static String RISK_MAX = "weight_prefs_risk_max";

    SharedPreferences settings_risk_min, settings_risk_max;

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


        //When I press OK button get newWeight and newDate
        deletebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myDB.deleteData("Weight_Summary");

                Toast.makeText(Preferences.this,"Data deleted",Toast.LENGTH_LONG).show();

            }

        });


        min = (EditText) findViewById(R.id.min);
        max = (EditText) findViewById(R.id.max);

        //initialize risk limit sharedpreferences instances
        settings_risk_min = this.getSharedPreferences(RISK_MIN, 0);
        settings_risk_min.getFloat(RISK_MIN, 85);

        settings_risk_max = this.getSharedPreferences(RISK_MAX, 0);
        settings_risk_max.getFloat(RISK_MAX, 85);


        set = (Button) findViewById(setbtn);

        //when click set button store values to retrieve from Graph.java
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMin = Float.parseFloat(min.getText().toString());
                newMax = Float.parseFloat(max.getText().toString());

                //When saving
                SharedPreferences.Editor editor_risk_min = settings_risk_min.edit();
                editor_risk_min.putFloat(RISK_MIN, newMin );
                editor_risk_min.commit();

                SharedPreferences.Editor editor_risk_max = settings_risk_max.edit();
                editor_risk_max.putFloat(RISK_MAX, newMax );
                editor_risk_max.commit();

                Toast.makeText(Preferences.this, "Changes applied", Toast.LENGTH_SHORT).show();



            }
        });






    }


}
