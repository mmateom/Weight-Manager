package com.mikel.poseidon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;

/**
 * Created by mikel on 12/01/2017.
 */

public class WeightNotifBootReceiver extends BroadcastReceiver {

    private final static String TAG = WeightNotifReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            //Get hour, minute and frequency from Reminders class
            SharedPreferences mPrefs = context.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
            int min = mPrefs.getInt("minutes_key", -1);
            int hour = mPrefs.getInt("hours_key", -1);
            int frequency = mPrefs.getInt("frequency_key", -1);




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




        }
    }



}
