package com.mikel.poseidon.Activities.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.mikel.poseidon.Model.DBHelper;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static com.mikel.poseidon.Activities.preferences.SetGraphLimits.sharedPrefs;

/**
 * Created by mikel on 12/05/2017.
 */

public class NotifReceiver extends BroadcastReceiver {

    private static final String TAG = NotifReceiver.class.getName();
    DBHelper myDB;
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences mPrefs= context.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        myDB = new DBHelper(context);

        // Weather service
        Intent myIntent = new Intent(context, WeatherNService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            int h = 1;
            int m = 60;
            long frequency = h * m * 60 * 1000; // in ms
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), frequency, pendingIntent);


        //Permanent notification is held in the same bootreceiver as weather
        Intent permanentN = new Intent(context, PermanentNService.class);
        context.startService(permanentN);




        int goal = mPrefs.getInt("steps_goal", 0);
        int caloriesGoal = mPrefs.getInt("calories_goal", 0);

        Calendar calendar = Calendar.getInstance();
        int mMin = calendar.get(Calendar.MINUTE);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);

        //encourage exercise based on goals

        if(goal > 0 && (mHour == 17 && mMin == 30 || mHour == 10 && mMin == 30)){
            int stepProgress = (int) ((100 * todaySteps()) / goal);
            int caloriesProgress = (int) ((100 * todayCalories()) / caloriesGoal);


            if (stepProgress == 0 || stepProgress < 50 || caloriesProgress < 50) {
                // Create an Explicit Intent
                Intent intent2 = new Intent(context, ExerciseNService.class);
                intent2.putExtra("hasGoal", true);
                // Start the Service
                context.startService(intent2);

            }

        }else if (goal == 0 && (mHour == 17 && mMin == 30 || mHour == 10 && mMin == 30)){
            // Create an Explicit Intent
            Intent intent3 = new Intent(context, ExerciseNService.class);
            intent3.putExtra("hasGoal", false);
            // Start the Service
            context.startService(intent3);

        }


    }

    public long todaySteps(){
        Cursor alldata = myDB.getTodaySumSteps();
        long todaySteps = 0;

        if(alldata.getCount() > 0) { //if count equals zero (no record today), textView automatically displays 0

            alldata.moveToFirst();
            todaySteps = alldata.getLong(1);
        }
        //previous: see COMMENTED CODE below
        return todaySteps;
    }

    public double todayCalories(){
        Cursor alldata = myDB.getCalories();
        double todayCalories = 0;

        if(alldata.getCount() > 0) { //if count equals zero (no record today), textView automatically displays 0

            alldata.moveToFirst();
            todayCalories = alldata.getDouble(3);
        }
        //previous: see COMMENTED CODE below
        return todayCalories;
    }
}
