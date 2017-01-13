package com.mikel.poseidon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by mikel on 12/01/2017.
 */

public class WeightNotifBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        int milLisec = cal.get(Calendar.MILLISECOND);

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())
            && hour == 17 && min == 11 && sec == 0 && milLisec == 0) {
            Intent serviceLauncher = new Intent(context, WeightNotifService.class);

            AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, serviceLauncher, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 11);
            calendar.set(Calendar.HOUR_OF_DAY, 17);
            //calendar.add(Calendar.DAY_OF_YEAR, 1);
            int everyDayNotif = 1000*60*60*24;

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


            context.startService(serviceLauncher);
            Log.v("TEST", "Service loaded at start");
        }
    }



}
