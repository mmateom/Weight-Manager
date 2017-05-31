package com.mikel.poseidon.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikel.poseidon.models.DBHelper;
import com.mikel.poseidon.MainActivity;
import com.mikel.poseidon.R;

import java.text.DecimalFormat;

import static com.mikel.poseidon.R.id.goodlimits;
import static com.mikel.poseidon.R.id.info;
import static com.mikel.poseidon.R.id.obese;
import static com.mikel.poseidon.R.id.overweight;
import static com.mikel.poseidon.R.id.setbtn_becareful;
import static com.mikel.poseidon.R.id.setbtn_good;
import static com.mikel.poseidon.R.id.setbtn_risk;
import static java.lang.Math.pow;

public class SetGraphLimits extends AppCompatActivity {


    Button  set_risk,set_becareful,set_good;
    EditText min_risk_edit, max_risk_edit;
    EditText min_becareful_edit, max_becareful_edit;
    EditText min_good_edit, max_good_edit;
    TextView ob, ov, g;

    float riskMin, riskMax;
    float becarefulMin, becarefulMax;
    float goodMin, goodMax;

    public static String sharedPrefs = "com.mikel.poseidon";
    public String min_key_risk = "min_risk";
    public String max_key_risk = "max_risk";

    public String min_key_becareful = "min_becareful";
    public String max_key_becareful = "max_becareful";

    public String min_key_good = "min_good";
    public String max_key_good = "max_good";
    int height;
    int p;
    DBHelper myDB;


    private SharedPreferences mSharedPrefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_graph_limits);

        myDB = new DBHelper(this);

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

        p = mSharedPrefs.getInt("weightUnits", 0);

        height = mSharedPrefs.getInt("height_key", 0);


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


        ob = (TextView) findViewById(obese);
        ov = (TextView) findViewById(overweight);
        g = (TextView) findViewById(goodlimits);


        double obeseLimitUpper = 40d * pow((height / 100d), 2);
        double obeseLimitLower = 30.1d * pow((height / 100d), 2);
        double overweightLimitUpper = 30d * pow((height / 100d), 2);
        double overweightLimitLower = 25.1d * pow((height / 100d), 2);
        double goodLimitUpper = 25d * pow((height / 100d), 2);
        double goodLimitLower = 18.5d * pow((height / 100d), 2);

        String units = "kg";

        if(p == 1){
            obeseLimitUpper = obeseLimitUpper * 2.20462;
            obeseLimitLower = obeseLimitLower * 2.20462;
            overweightLimitUpper = overweightLimitUpper * 2.20462;
            overweightLimitLower = overweightLimitLower * 2.20462;
            goodLimitUpper = goodLimitUpper * 2.20462;
            goodLimitLower = goodLimitLower * 2.20462;

            units = "lbs";

        }




        ob.setText("Recommended - " + "Max: " + new DecimalFormat("##").format(obeseLimitUpper)+ units + " - Min: " + new DecimalFormat("##").format(obeseLimitLower) + units);
        ov.setText("Recommended - " + "Max: " + new DecimalFormat("##").format(overweightLimitUpper) + units + " - Min: " + new DecimalFormat("##").format(overweightLimitLower) + units);
        g.setText("Recommended - " + "Max: " + new DecimalFormat("##").format(goodLimitUpper) + units + " - Min: " + new DecimalFormat("##").format(goodLimitLower) + units);

        Button information = (Button) findViewById(info);
        double finalOverweightLimitUpper = overweightLimitUpper;
        double finalObeseLimitUpper = obeseLimitUpper;
        double finalGoodLimitUpper = goodLimitUpper;
        double finalOverweightLimitLower = overweightLimitLower;
        double finalObeseLimitLower = obeseLimitLower;
        double finalGoodLimitLower = goodLimitLower;
        String finalUnits = units;
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showLimitsInformation(obeseLimitUpper, obeseLimitLower, overweightLimitUpper, overweightLimitLower, goodLimitUpper, goodLimitLower);
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.limit_level_info, null);
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_set_limits);
                /*TextView allLimits = (TextView)findViewById(R.id.allthelimits);
                allLimits.setText("Obese - " + "Max: " + new DecimalFormat("##").format(obeseLimitUpper)+ " - Min: " + new DecimalFormat("##").format(obeseLimitLower) + "\n"
                        + "Overweight - " + "Max: " + new DecimalFormat("##").format(overweightLimitUpper) + " - Min: " + new DecimalFormat("##").format(overweightLimitLower) + "\n"
                        +"Good weight - " + "Max: " + new DecimalFormat("##").format(goodLimitUpper) + " - Min: " + new DecimalFormat("##").format(goodLimitLower));*/

                PopupWindow popupWindow = new PopupWindow(container, dpToPx(360), dpToPx(470), true); //true allows us to close window by tapping outside
                ((TextView)popupWindow.getContentView().findViewById(R.id.allthelimits))
                        .setText("Obese - " + "Max: " + new DecimalFormat("##").format(finalObeseLimitUpper)+ finalUnits +  " - Min: " + new DecimalFormat("##").format(finalObeseLimitLower) + finalUnits + "\n"
                        + "Overweight - " + "Max: " + new DecimalFormat("##").format(finalOverweightLimitUpper) + finalUnits + " - Min: " + new DecimalFormat("##").format(finalOverweightLimitLower) + finalUnits + "\n"
                        +"Good weight - " + "Max: " + new DecimalFormat("##").format(finalGoodLimitUpper) + finalUnits + " - Min: " + new DecimalFormat("##").format(finalGoodLimitLower) + finalUnits);
                popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, dpToPx(0), dpToPx(120));

                //shut popup outside window
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popupWindow.dismiss();
                        return false;
                    }
                });
            }
        });
    }


    /*public void showLimitsInformation(double obeseLimitUpper, double obeseLimitLower, double overweightLimitUpper, double overweightLimitLower, double goodLimitUpper, double goodLimitLower){



    }*/

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }




    //============================
    //          Methods
    //============================


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



}


