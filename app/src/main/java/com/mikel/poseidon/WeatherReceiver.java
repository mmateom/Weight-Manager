package com.mikel.poseidon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.mikel.poseidon.R.id.c;
import static com.mikel.poseidon.R.id.frequency;
import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;

/**
 * Created by mikel on 12/05/2017.
 */

public class WeatherReceiver extends BroadcastReceiver {

    private static final String TAG = WeatherReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {

            Intent myIntent = new Intent(context, WeatherService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            int h = 1;
            long frequency = h * 60 * 60 * 1000; // in ms
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), frequency, pendingIntent);


            //Permanent notification is held in the same bootreceiver as weather
            Intent permanentN = new Intent(context, PermanentNService.class);
            context.startService(permanentN);



    }
}
