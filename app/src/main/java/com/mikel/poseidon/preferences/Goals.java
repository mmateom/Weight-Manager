package com.mikel.poseidon.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mikel.poseidon.models.Profile;
import com.mikel.poseidon.models.DBHelper;
import com.mikel.poseidon.MainActivity;
import com.mikel.poseidon.R;

import java.util.ArrayList;


import static com.mikel.poseidon.R.id.bmr_text;
import static com.mikel.poseidon.R.id.set_calories_goal;
import static com.mikel.poseidon.R.id.uni;

public class Goals extends AppCompatActivity {

    SharedPreferences preferences;
    EditText goaltxt, caloriesGoaltxt, weightGoaltxt;

    int mAge, mPosition, units;
    double mHeight, mWeight, mBmr, mDailyCalories;
    String mGender;

    DBHelper myDB;
    Profile profile;

    public String STEPS_GOAL = "steps_goal";
    public String CALORIES_GOAL = "calories_goal";
    public String WEIGHT_GOAL = "weight_goal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        //callback to home button
        ImageButton home_button9 = (ImageButton) findViewById(R.id.homebutton);
        home_button9.setOnClickListener(view -> {

            Intent home_intent9 = new Intent(Goals.this, MainActivity.class);
            home_intent9.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home_intent9);
        });

        preferences = getSharedPreferences(SetGraphLimits.sharedPrefs, MODE_PRIVATE);


        //create DB instance
        myDB = new DBHelper(this);
        profile = new Profile(this);

        mAge = profile.getAge();
        mHeight = profile.getHeight();

        mWeight = getLastWeight(); /**gets weight from DB*/

        mGender = profile.getGender();
        mPosition = profile.getLevel();

        //calculate bmr
        mBmr = calculateBmr(mHeight, mWeight, mAge, mGender);

        //calculate daily calories
        mDailyCalories = calculateDailyCaloryIntake(mBmr, mPosition);
        //


        goaltxt = (EditText)findViewById(R.id.steps_per_day);
        caloriesGoaltxt= (EditText)findViewById(R.id.calories_per_day);
        weightGoaltxt= (EditText)findViewById(R.id.weightGoal);
        //get values from sharedprefs
        units = profile.getUnits();


        setStepsGoal();
        setCaloriesGoal();
        setWeightGoal();

        TextView unitstxt = (TextView)findViewById(uni);

        if(units == 1){

            unitstxt.setText("lbs");
        }




    }

    private void setStepsGoal(){
        Button setStepsGoal = (Button)findViewById(R.id.set_steps_goal);
        setStepsGoal.setOnClickListener(view -> {
            if (!"".equals(goaltxt.getText().toString())) {

                int stepsGoal = Integer.parseInt(goaltxt.getText().toString());
                SharedPreferences.Editor goalEditor = preferences.edit();
                goalEditor.putInt(STEPS_GOAL, stepsGoal);
                goalEditor.commit();

                Toast.makeText(this, "GOAL SET", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void setCaloriesGoal(){

        Button setCaloriesGoal = (Button)findViewById(set_calories_goal);


        setCaloriesGoal.setOnClickListener(view -> {
            if (!"".equals(caloriesGoaltxt.getText().toString())) {

                int caloriesGoal = Integer.parseInt(caloriesGoaltxt.getText().toString());
                SharedPreferences.Editor goalEditor = preferences.edit();
                goalEditor.putInt(CALORIES_GOAL, caloriesGoal);
                goalEditor.commit();

                Toast.makeText(this, "GOAL SET", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void setWeightGoal(){
        Button setWeightGoal = (Button)findViewById(R.id.set_weight_goal);
        setWeightGoal.setOnClickListener(view -> {
            if (!"".equals(weightGoaltxt.getText().toString())) {

                float weightGoal = Integer.parseInt(weightGoaltxt.getText().toString());
                SharedPreferences.Editor goalEditor = preferences.edit();
                goalEditor.putFloat(WEIGHT_GOAL, weightGoal);
                goalEditor.commit();

                Toast.makeText(this, "GOAL SET", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private double calculateBmr(double height, double weight, int age, String gender){
        double bmr =0;

        if(units == 1){

                weight = weight * 0.453592; //from lbs to kg
        }


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

        //set value on textview
        TextView bmrtxt = (TextView)findViewById(bmr_text);
        bmrtxt.setText(String.format( "Recommended calorie intake:\n%.2f calories per day" , dailyCalories )); //"Recomended calorie intake: " + String.valueOf(dailyCalories) + " calories per day");


        return dailyCalories;

    }

    private double getLastWeight(){
        Cursor alldata;
        ArrayList<Double> yVals;
        alldata= myDB.getListContents();
        double lastWeight = 0;

        int count = alldata.getCount();
        double[] weights = new double[count];

        yVals = new ArrayList<Double>();

        //get dates and weight from the database and populate arrays
        for (int m = 0; m < count; m++) {
            alldata.moveToNext();
            weights[m] = alldata.getDouble(4);

            yVals.add(weights[m]);

        }

        if(yVals.size() == 0){

            lastWeight = 0;

        }else lastWeight = yVals.get(yVals.size() - 1);

        return lastWeight;
    }


    @Override
    protected void onResume() {
        super.onResume();

        preferences = getSharedPreferences(SetGraphLimits.sharedPrefs, MODE_PRIVATE);
        goaltxt.setText(String.valueOf(preferences.getInt(STEPS_GOAL, 0)));
        caloriesGoaltxt.setText(String.valueOf(preferences.getInt(CALORIES_GOAL, 0)));
        weightGoaltxt.setText(String.valueOf(preferences.getFloat(WEIGHT_GOAL, 0)));



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



