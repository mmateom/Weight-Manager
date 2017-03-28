package com.mikel.poseidon;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.R.attr.value;
import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;

public class Reminders extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    TextView remindTime, mFrequency;
    Button saveBtn;
    SharedPreferences mSharedPrefs;

    String time_key = "time_key";
    static String minutes_key ="minutes_key";
    static String hours_key ="hours_key";
    String frequency_key = "frequency_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(view -> {
            Intent home_intent = new Intent(Reminders.this, MainActivity.class);
            startActivity(home_intent);
        });



        remindTime = (TextView)findViewById(R.id.remind_time);
        remindTime.setOnClickListener(view -> showTimePicker());

        mFrequency = (TextView)findViewById(R.id.frequency);
        mFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reminders.this.showFrequency();
            }
        });

        saveBtn = (Button)findViewById(R.id.save2);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reminders.this.save();
            }
        });

        mSharedPrefs = this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        mSharedPrefs.getString(time_key, "");
        mSharedPrefs.getInt(frequency_key,-1);
        mSharedPrefs.getInt(minutes_key,-1);
        mSharedPrefs.getInt(hours_key,-1);




    }



    private void showFrequency() {

        final Dialog d = new Dialog(Reminders.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.np_dialog);


        Button set = (Button) d.findViewById(R.id.set_action);
        Button cancel = (Button) d.findViewById(R.id.cancel_action);

        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100); // max value 100
        np.setMinValue(1); // min value 0
        np.setValue(14); //default value 14 days
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

        set.setOnClickListener(v -> {
            mFrequency.setText(String.valueOf(np.getValue())); //set the value to textview

            //save frequency on sharedpreferences
            SharedPreferences.Editor timeEditor = mSharedPrefs.edit();
            timeEditor.putInt(frequency_key, np.getValue());
            timeEditor.apply();

            d.dismiss();

        });
        cancel.setOnClickListener(v -> {
            d.dismiss(); // dismiss the dialog
        });
        d.show();

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is",""+newVal);
//       age.setText(newVal);

    }


    //
    public void showTimePicker(){
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;


            mTimePicker = new TimePickerDialog(Reminders.this, (view, hourOfDay, minute1) -> {

                //String format = "";

                //save on sharepreferences
                SharedPreferences.Editor timeEditor = mSharedPrefs.edit();
                timeEditor.putInt(minutes_key, hourOfDay);
                timeEditor.putInt(hours_key, minute1);
                timeEditor.apply();

          /*  if (selectedHour == 0) {
                selectedHour += 12;
                format = "AM";
            } else if (selectedHour == 12) {
                format = "PM";
            } else if (selectedHour > 12) {
                selectedHour -= 12;
                format = "PM";
            } else {
                format = "AM";
            }*/

                String am_pm = checkHourFormat(hourOfDay);

                //set on textview
                String output = String.format("%02d:%02d " + am_pm /*format*/, hourOfDay, minute1); //this does zero padding on minutes
                remindTime.setText(output);
                //remindTime.setText(new StringBuilder().append(selectedHour).append(" : ").append(selectedMinute)
                // .append(" ").append(format));

            }, hour, minute, false);//24 hour time disabled
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();


    }

    public String checkHourFormat(int pickerHour){

        String format = "";

        if ( pickerHour == 0) {
            pickerHour += 12;
            return format = "AM";
        } else if (pickerHour == 12) {
            return format = "PM";
        } else if (pickerHour> 12) {
            pickerHour -= 12;
            return format = "PM";
        } else {
            return format = "AM";
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("onResume!!!!!!!!!!!!!!!!!");

        mSharedPrefs = this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        int mMinute= mSharedPrefs.getInt(minutes_key,0);
        int mHour = mSharedPrefs.getInt(hours_key,0);
        int frequency = mSharedPrefs.getInt(frequency_key,0);
        String amORpm = "";

        checkHourFormat(mHour);

        //set on textview
        String output = String.format("%02d:%02d " + amORpm /*format*/, mHour, mMinute); //this does zero padding on minutes
        remindTime.setText(output);
        mFrequency.setText(String.valueOf(frequency));



    }

    public void save(){

        Intent mainIntent = new Intent(Reminders.this, MainActivity.class);
        startActivity(mainIntent);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
