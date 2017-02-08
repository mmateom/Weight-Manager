package com.mikel.poseidon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static com.mikel.poseidon.Preferences.sharedPrefs;
import static com.mikel.poseidon.R.id.cm;
import static com.mikel.poseidon.R.id.years;
import static java.lang.Math.pow;
import static java.lang.Math.round;


public class CurrentState extends AppCompatActivity {

    SharedPreferences mPrefs;
    int mAge, mPosition;
    double mHeight, mWeight, mBmi, mBmr, mDailyCalories;
    String mGender;

    DBHelper myDB;

    TextView bmitxt, bmrtxt, dailyIntake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_state);

        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(view -> {
            Intent home_intent = new Intent(CurrentState.this, MainActivity.class);
            startActivity(home_intent);
        });

        //create DB instance
        myDB = new DBHelper(this);

        //get values from sharedprefs
        mPrefs= this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);


        mAge = mPrefs.getInt("age_key",0);
        mHeight = mPrefs.getInt("height_key",0);

        mWeight = getLastWeight(); /**gets weight from DB*/

        mGender = mPrefs.getString("gender_key","Male");
        mPosition = mPrefs.getInt("levelint", 0);

        System.out.println(String.valueOf(mPosition));

        //calculate BMI
        mBmi = calculateBmi(mHeight, mWeight);

        //calculate bmr
        mBmr = calculateBmr(mHeight, mWeight, mAge, mGender);

        //calculate daily calories
        mDailyCalories = calculateDailyCaloryIntake(mBmr, mPosition);

        //show
        bmitxt = (TextView)findViewById(R.id.bmi_text);
        bmitxt.setText(String.valueOf(round(mBmi,2)));

        bmrtxt= (TextView)findViewById(R.id.bmr_text);
        bmrtxt.setText(String.valueOf(round(mBmr,2)));

        dailyIntake = (TextView)findViewById(R.id.daily_calories);
        dailyIntake.setText(String.valueOf(mDailyCalories));





    }


    private double calculateBmi(double height, double weight){

        double p = weight / pow((height/100), 2);

        System.out.println(String.valueOf(p));

        return p;

    }

    private double calculateBmr(double height, double weight, int age, String gender){
        double bmr =0;

        if(gender.equals("Male")){
            bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        }else if (gender.equals("Female")){
            bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;

        }

        return bmr;



    }

    private double calculateDailyCaloryIntake(double bmr, int activityLevel) {
        double dailyCalories = 0;

        if (activityLevel == 0){
            dailyCalories =  bmr*1.2;
        }
        else if (activityLevel == 1){
            dailyCalories =  bmr*1.375;
        }else if (activityLevel == 2){
            dailyCalories = bmr *1.55;
        }else if (activityLevel == 3) {
            dailyCalories = bmr * 1.725;
        }

        return dailyCalories;

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private double getLastWeight(){
        Cursor alldata;
        ArrayList<Double> yVals;
        alldata= myDB.getListContents();

        int count = alldata.getCount();
        double[] weights = new double[count];

        yVals = new ArrayList<Double>();

        //get dates and weight from the database and populate arrays
        for (int m = 0; m < count; m++) {
            alldata.moveToNext();
            weights[m] = alldata.getDouble(2);

            yVals.add(weights[m]);

        }

        double lastWeight = yVals.get(yVals.size() - 1);

        return lastWeight;
    }
}
