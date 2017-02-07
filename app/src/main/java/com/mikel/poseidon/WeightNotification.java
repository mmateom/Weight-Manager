package com.mikel.poseidon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static com.mikel.poseidon.Preferences.sharedPrefs;

/**
 * Created by mikel on 06/02/2017.
 */

public class WeightNotification {

    private int minutes;
    private int hours;
    private static int frequency=1;


    public WeightNotification(){

    }


    public static void setRepeating(Context context){

        Intent intent = new Intent(context, WeightNotifReceiver.class);

        PendingIntent pending = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);




        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        //calendar.add(Calendar.DAY_OF_YEAR, 1);

        int interval = 1000*60*frequency;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pending);


    }

    public void setMinutes(int minutes){

        this.minutes = minutes;
    }

    public void setHours(int hours){

        this.hours = hours;
    }


    public void setFrequency(int frequency){

        this.frequency = frequency;
    }




}
