package com.mikel.poseidon.preferences;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.mikel.poseidon.MainActivity;
import com.mikel.poseidon.R;
import com.mikel.poseidon.notifications.WeightNotifReceiver;

import java.util.Calendar;

import static com.mikel.poseidon.preferences.SetGraphLimits.sharedPrefs;

public class Reminders extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    TextView remindTime, mFrequency;
    Button saveBtn;
    SharedPreferences mSharedPrefs;

    String time_key = "time_key";
    static String minutes_key = "minutes_key";
    static String hours_key = "hours_key";
    String frequency_key = "frequency_key";

    private final static String TAG = Reminders.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(view -> {
            Intent home_intent = new Intent(Reminders.this, MainActivity.class);
            home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home_intent);
        });


        remindTime = (TextView) findViewById(R.id.remind_time);
        remindTime.setOnClickListener(view -> showTimePicker());

        mFrequency = (TextView) findViewById(R.id.frequency);
        mFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reminders.this.showFrequency();
            }
        });

        saveBtn = (Button) findViewById(R.id.save2);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reminders.this.save();
            }
        });

        mSharedPrefs = this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        mSharedPrefs.getString(time_key, "");
        mSharedPrefs.getInt(frequency_key, -1);
        mSharedPrefs.getInt(minutes_key, -1);
        mSharedPrefs.getInt(hours_key, -1);


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

        Log.i("value is", "" + newVal);
//       age.setText(newVal);

    }


    //
    public void showTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;


        mTimePicker = new TimePickerDialog(Reminders.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String format = "";

                //save on sharepreferences
                SharedPreferences.Editor timeEditor = mSharedPrefs.edit();
                timeEditor.putInt(minutes_key, minute);
                timeEditor.putInt(hours_key, hourOfDay);
                timeEditor.apply();

                if (hourOfDay == 0) {
                    hourOfDay += 12;
                    format = "AM";
                } else if (hourOfDay == 12) {
                    format = "PM";
                } else if (hourOfDay > 12) {
                    hourOfDay -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }


                //set on textview
                String output = String.format("%02d:%02d " + format, hourOfDay, minute); //this does zero padding on minutes
                remindTime.setText(output);
                //remindTime.setText(new StringBuilder().append(selectedHour).append(" : ").append(selectedMinute)
                // .append(" ").append(format));

            }
        }, hour, minute, false);//24 hour time disabled
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();


    }




    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("onResume!!!!!!!!!!!!!!!!!");

        mSharedPrefs = this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        int mMinute = mSharedPrefs.getInt(minutes_key, 0);
        int mHour = mSharedPrefs.getInt(hours_key, 0);
        int frequency = mSharedPrefs.getInt(frequency_key, 0);
        String amORpm;


        if (mHour == 0) {
            mHour += 12;
            amORpm = "AM";
        } else if (mHour == 12) {
            amORpm = "PM";
        } else if (mHour > 12) {
            mHour -= 12;
            amORpm = "PM";
        } else {
            amORpm = "AM";
        }

        //set on textview
        String output = String.format("%02d:%02d " + amORpm /*format*/, mHour, mMinute); //this does zero padding on minutes
        remindTime.setText(output);
        mFrequency.setText(String.valueOf(frequency));


    }

    public void save() {

        Context context = getApplicationContext();

        SharedPreferences mPrefs = context.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        int min = mPrefs.getInt("minutes_key", -1);
        int hour = mPrefs.getInt("hours_key", -1);
        int frequency = mPrefs.getInt("frequency_key", -1);


        if (min != -1) {

            Log.e(TAG, hour + ":" + min);
            Intent myIntent = new Intent(context, WeightNotifReceiver.class); //intent to BroadcastReveicer

            //bindService(myIntent, mConnection, Context.BIND_AUTO_CREATE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0); //wrap intent in a PendingIntent


            //Schedule alarm
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            //calendar.add(Calendar.DAY_OF_YEAR, 1); //para que salga al día siguiente
            //int interval = 1000*60*frequency;


            //Alarm fires pendingIntent to BroadcastReceiver
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * frequency, pendingIntent);

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
