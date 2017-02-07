package com.mikel.poseidon;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.id.edit;
import static com.github.mikephil.charting.charts.Chart.LOG_TAG;
import static com.mikel.poseidon.Preferences.sharedPrefs;
import static com.mikel.poseidon.R.id.textView;

public class UserProfile extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    private TextView age,height,gender;
    private EditText weight;
    private Button savebtn;

    SharedPreferences mSharedPrefs;
    public String age_key = "age_key";
    public String height_key = "height_key";
    public String gender_key = "gender_key";

    //numberpicker


    //TODO: NO VOY A GUARDAR EL PESO EN SHAREDPrefs porque para hacer cálculos siempre necesitaré
    //el último peso

    DBHelper myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(view -> {
            Intent home_intent = new Intent(UserProfile.this, MainActivity.class);
            startActivity(home_intent);
        });



        //text views
        age = (TextView)findViewById(R.id.profile_age);
        height = (TextView)findViewById(R.id.profile_height);
        gender = (TextView)findViewById(R.id.profile_gender);
        weight = (EditText) findViewById(R.id.profile_weight);
        savebtn = (Button)findViewById(R.id.save);

        //initialise sharedprefs instances
        mSharedPrefs = this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        mSharedPrefs.getInt(age_key, 0);
        mSharedPrefs.getInt(height_key,0);
        mSharedPrefs.getString(gender_key, "Male");

        //set listeners
        age.setOnClickListener(view -> showAgeDialog());
        height.setOnClickListener(view -> showHeightDialog());
        gender.setOnClickListener(view -> showGenderDialog());
        savebtn.setOnClickListener(view -> onSaveButtonClick());

        //create DB instance
        myDB = new DBHelper(this);


    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is",""+newVal);
//        age.setText(newVal);

    }



    public void showAgeDialog()
    {
        final Dialog d = new Dialog(UserProfile.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.np_dialog);

        Button set = (Button) d.findViewById(R.id.set_action);
        Button cancel = (Button) d.findViewById(R.id.cancel_action);

        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100); // max value 100
        np.setMinValue(5); // min value 0
        np.setValue(20);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

       // v=np.getValue();

        set.setOnClickListener(v -> {
            age.setText(String.valueOf(np.getValue())); //set the value to textview
            d.dismiss();
        });
        cancel.setOnClickListener(v -> {
            d.dismiss(); // dismiss the dialog
        });
        d.show();


    }
    //
    public void showHeightDialog()
    {

        final Dialog d = new Dialog(UserProfile.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.np_dialog);
        Button set = (Button) d.findViewById(R.id.set_action);
        Button cancel = (Button) d.findViewById(R.id.cancel_action);
        final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np2.setMaxValue(210); // max value 100
        np2.setMinValue(90); // min value 0
        np2.setValue(170);
        np2.setWrapSelectorWheel(false);
        np2.setOnValueChangedListener(this);

        set.setOnClickListener(v -> {
            height.setText(String.valueOf(np2.getValue())); //set the value to textview
            d.dismiss();
        });
        cancel.setOnClickListener(v -> {
            d.dismiss(); // dismiss the dialog
        });
        d.show();


    }
    //
    public void showGenderDialog()
    {

        final Dialog d = new Dialog(UserProfile.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.np_dialog);
        Button set = (Button) d.findViewById(R.id.set_action);
        Button cancel = (Button) d.findViewById(R.id.cancel_action);
        final NumberPicker np3 = (NumberPicker) d.findViewById(R.id.numberPicker1);

        final String[] values= {"Male", "Female"};

        //Populate NumberPicker values from String array values
        //Set the minimum value of NumberPicker

        np3.setMinValue(0); //from array first value

        //Specify the maximum value/number of NumberPicker
        np3.setMaxValue(values.length-1); //to array last value

        //Specify the NumberPicker data source as array elements
        np3.setDisplayedValues(values);

        np3.setWrapSelectorWheel(false);
        np3.setOnValueChangedListener(this);

        set.setOnClickListener(v -> {
            gender.setText(String.valueOf(values[np3.getValue()])); //set the value to textview
            d.dismiss();
        });
        cancel.setOnClickListener(v -> {
            d.dismiss(); // dismiss the dialog
        });
        d.show();


    }


    public void onSaveButtonClick()
    {

        ////Saving values in sharedprefs

        //if x.getText is not empty then save it in sharedprefs
        if (!"".equals(age.getText().toString())) {

            SharedPreferences.Editor ageEditor = mSharedPrefs.edit();
            ageEditor.putInt(age_key, Integer.parseInt(age.getText().toString()));
            ageEditor.apply();
        }

        if (!"".equals(height.getText().toString())) {

            SharedPreferences.Editor heightEditor = mSharedPrefs.edit();
            heightEditor.putInt(height_key, Integer.parseInt(height.getText().toString()));
            heightEditor.apply();
        }

        if (!"".equals(gender.getText().toString())) {
            SharedPreferences.Editor genderEditor = mSharedPrefs.edit();
            genderEditor.putString(gender_key, gender.getText().toString());
            genderEditor.apply();
        }



        //TextView textView = (TextView)findViewById(R.id.textView15);
        //textView.setText(gender.getText().toString());


        //Add weight to DB

        //---------------------------------------------
        //if weight is not null, parse it to double
        if (!"".equals(weight.getText().toString())) {
            double yourWeight = Double.parseDouble(weight.getText().toString());

            //if yourWeight is not empty (has not default value), save to DB
            if (yourWeight != 0.0d) {

                myDB.addData(yourWeight, getCurrentDate());
            }
            else { Log.e(LOG_TAG, String.valueOf(yourWeight));}

        }
        //---------------------------------------------


    }


    //=========================================
    //             GET CURRENT TIME
    //=========================================
   public String getCurrentDate() {

        Calendar cal = Calendar.getInstance();

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) +1; //plus 1, because months start on 0 in java
        int year = cal.get(Calendar.YEAR);

        String date_final = null;


        String current_start_time = year + "-" + month + "-" + day;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {

            Date date_f = formatter.parse(current_start_time);
            date_final = formatter.format(date_f);

            //System.out.println(date_f);
            //System.out.println(date_final);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date_final;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSharedPrefs = this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);

        int mAge = mSharedPrefs.getInt(age_key,0);
        age.setText(String.valueOf(mAge));

        int mHeight = mSharedPrefs.getInt(height_key,0);
        height.setText(String.valueOf(mHeight));

        String mGender = mSharedPrefs.getString(gender_key,"Male");
        gender.setText(String.valueOf(mGender));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSharedPrefs = this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);

        int mAge = mSharedPrefs.getInt(age_key,0);
        age.setText(String.valueOf(mAge));

        int mHeight = mSharedPrefs.getInt(height_key,0);
        height.setText(String.valueOf(mHeight));

        String mGender = mSharedPrefs.getString(gender_key,"Male");
        gender.setText(String.valueOf(mGender));
    }
}
