package com.mikel.poseidon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;
import static android.view.View.GONE;
import static com.github.mikephil.charting.charts.Chart.LOG_TAG;
import static com.mikel.poseidon.R.id.calories;
import static com.mikel.poseidon.R.id.congratulations;
import static com.mikel.poseidon.R.id.congratulations2;
import static com.mikel.poseidon.R.id.currentWeightUnit;
import static com.mikel.poseidon.R.id.unit_get_weight;
import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;
import static com.mikel.poseidon.R.drawable.bmi;
import static com.mikel.poseidon.R.id.cm;
import static com.mikel.poseidon.R.id.years;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.reflect.Array.getInt;


public class CurrentState extends AppCompatActivity {

    SharedPreferences mPrefs;
    int mAge, mPosition, goal;
    double mHeight, mWeight, mBmi, calories_goal;
    String mGender;

    DBHelper myDB;

    TextView bmitxt, currentWeight, bmiFeedback, bmiBar;

    int units;
    ProgressBar stepsPrg, caloriesPrg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_state);


        //callback to home button
        ImageButton home_butto = (ImageButton) findViewById(R.id.homebutton);
        home_butto.setOnClickListener(view -> {
            Intent home_intent5 = new Intent(CurrentState.this, MainActivity.class);
            startActivity(home_intent5);
        });

        //create DB instance
        myDB = new DBHelper(this);

        //get values from sharedprefs
        mPrefs= this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        TextView unit = (TextView)findViewById(currentWeightUnit);
        units = mPrefs.getInt("weightUnits", 0);
        if (units == 1){
            unit.setText("lbs");
        }


        mAge = mPrefs.getInt("age_key",0);
        mHeight = mPrefs.getInt("height_key",0);

        mWeight = getLastWeight(); /**gets weight from DB*/

        mGender = mPrefs.getString("gender_key","Male");
        mPosition = mPrefs.getInt("levelint", 0);


        System.out.println(String.valueOf(mPosition));

        //calculate BMI
        mBmi = calculateBmi(mHeight, mWeight);

        //show
        setTexts();

        //calculate progress bar
        setStepsProgressBars();
        setCaloriesProgressBar();



        //stepsFraction.setText(String.valueOf(todaySteps()));


        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!(mPrefs.getString("STEPS", "")).equals("")){
                stepsFraction.setText(String.valueOf(todaySteps() + Integer.parseInt(mPrefs.getString("STEPS", ""))));}
            }
        });*/






    }

    private void setStepsProgressBars(){
        goal = mPrefs.getInt("steps_goal", 0);
        if(goal > 0) {
            int stepProgress = (int) ((100 * todaySteps()) / goal);

            stepsPrg = (ProgressBar) findViewById(R.id.stepsProgressBar);
            stepsPrg.setScaleY(6f);
            stepsPrg.setProgress(stepProgress);
            stepsPrg.getProgressDrawable().setColorFilter(
                    ContextCompat.getColor(this, R.color.Good), PorterDuff.Mode.MULTIPLY);

            TextView feedback = (TextView) findViewById(congratulations);
            feedback.setVisibility(View.INVISIBLE);

            TextView stepsFraction = (TextView) findViewById(R.id.fraction);

            if (todaySteps() < goal) {
                stepsFraction.setText(String.valueOf(todaySteps()) + " of " + String.valueOf(goal));

            }

            if (todaySteps() == goal) {
                feedback.setText("Congratulations! You did it!");
                feedback.setVisibility(View.VISIBLE);
                stepsFraction.setText(String.valueOf(todaySteps()) + " of " + String.valueOf(goal));

            }

            if (todaySteps() > goal) {
                feedback.setText("Wow! Beyond your goal!");
                feedback.setVisibility(View.VISIBLE);
                stepsFraction.setText(String.valueOf(todaySteps()) + " - Goal: " + String.valueOf(goal));
            }
        }else{
            TextView stepsFraction = (TextView) findViewById(R.id.fraction);
            stepsFraction.setText("0");
            stepsPrg = (ProgressBar) findViewById(R.id.stepsProgressBar);
            stepsPrg.setScaleY(6f);
            stepsPrg.setProgress(0);
            stepsPrg.getProgressDrawable().setColorFilter(
                    ContextCompat.getColor(this, R.color.Good), PorterDuff.Mode.MULTIPLY);
            TextView feedback = (TextView) findViewById(congratulations);
            feedback.setVisibility(View.INVISIBLE);

        }


    }

    private void setCaloriesProgressBar(){
        calories_goal = mPrefs.getInt("calories_goal", 0);
        if(calories_goal > 0) {
            int caloriesProgress = (int) ((100 * todayCalories()) / calories_goal);

            caloriesPrg = (ProgressBar) findViewById(R.id.caloriesProgressBar);
            caloriesPrg.setScaleY(7f);
            caloriesPrg.setProgress(caloriesProgress);
            caloriesPrg.getProgressDrawable().setColorFilter(
                    ContextCompat.getColor(this, R.color.Good), PorterDuff.Mode.MULTIPLY);

            TextView feedback = (TextView) findViewById(congratulations2);
            feedback.setVisibility(View.INVISIBLE);

            TextView stepsFraction = (TextView) findViewById(R.id.fraction2);

            if (todayCalories() < calories_goal) {
                stepsFraction.setText(String.valueOf(todayCalories()) + " of " + String.valueOf(calories_goal));

            }

            if (todayCalories() == calories_goal) {
                feedback.setText("Congratulations! You did it!");
                feedback.setVisibility(View.VISIBLE);
                stepsFraction.setText(String.valueOf(todayCalories()) + " of " + String.valueOf(calories_goal));

            }

            if (todayCalories() > calories_goal) {
                feedback.setText("Okay! Enough for today!");
                feedback.setVisibility(View.VISIBLE);
                stepsFraction.setText(String.valueOf(todayCalories() + " - Goal: " + String.valueOf(calories_goal)));
            }
        }else{
            TextView stepsFraction = (TextView) findViewById(R.id.fraction2);
            stepsFraction.setText("0");
            caloriesPrg = (ProgressBar) findViewById(R.id.caloriesProgressBar);
            caloriesPrg.setScaleY(7f);
            caloriesPrg.setProgress(0);
            caloriesPrg.getProgressDrawable().setColorFilter(
                    ContextCompat.getColor(this, R.color.Good), PorterDuff.Mode.MULTIPLY);
            TextView feedback = (TextView) findViewById(congratulations);
            feedback.setVisibility(View.INVISIBLE);

        }

    }

    private void setTexts(){

        try{
            currentWeight = (TextView)findViewById(R.id.current_weight);
            currentWeight.setText(String.valueOf(mWeight));

            bmitxt = (TextView)findViewById(R.id.bmi_text);
            bmitxt.setText(String.valueOf(round(mBmi,2)));

            /*bmrtxt= (TextView)findViewById(R.id.bmr_text);
            bmrtxt.setText(String.valueOf(round(mBmr,2)));

            dailyIntake = (TextView)findViewById(R.id.daily_calories);
            dailyIntake.setText(String.valueOf(mDailyCalories));*/

            bmiFeedback = (TextView)findViewById(R.id.feedback);
            bmiFeedback.setText(interpretBMI(mBmi));

            bmiBar = (TextView)findViewById(R.id.bar);
            bmiBar.setX((dpToPx((int) moveBmibar(mBmi))));


        }
        catch (Exception e){
            Log.e("BMI", "No input in textviews");

        }
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    private double calculateBmi(double height, double weight){
        if (units == 1){
            weight = weight * 0.453592; //from lbs to kg
        }

        double p = weight / pow((height/100), 2);

        System.out.println(String.valueOf(p));

        return p;

    }



    private double moveBmibar(double bmi){

        double constant = 10; //300/30 ==> dp / rango bmi (45-15)

        //if the bmi exceeds max or min bmi of the progress bar, round it to the max or min
        if(bmi > 45){
            bmi = 45;
        }else if (bmi < 15){
            bmi = 15;
        }

        return (bmi-15) * constant; //minus 15 to adjust bmi to zero start of the progress bar
    }


    private String interpretBMI(double bmi) {

        if (bmi < 16) {
            return "You are Severely Underweight";
        } else if (bmi < 18.5) {

            return "You are Underweight";
        } else if (bmi < 25) {
            return "You are Normal";
        }else if (bmi < 30) {
            return "You are Overweight";
        }else if (bmi < 40) {
            return "You are Obese";
        }else if (bmi >= 40) {
            return "You are Morbidly Obese";
        }else {
            return "Enter your Details";
        }
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


    public long todaySteps(){
        Cursor alldata = myDB.getTodaySumSteps();
        long todaySteps = 0;

        if(alldata.getCount() > 0) { //if count equals zero (no record today), textView automatically displays 0

            alldata.moveToFirst();
            todaySteps = alldata.getLong(1);
        }



        return todaySteps;
    }

    public double todayCalories(){
        Cursor alldata = myDB.getCalories();
        double todayCalories = 0;

        if(alldata.getCount() > 0) { //if count equals zero (no record today), textView automatically displays 0

            alldata.moveToFirst();
            todayCalories = alldata.getDouble(2);
        }



        return todayCalories;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
