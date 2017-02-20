package com.mikel.poseidon;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.mikel.poseidon.Preferences.sharedPrefs;

/**
 * Created by mikel on 12/01/2017.
 */

public class WeightNotifBootReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){

            Calendar calendar = Calendar.getInstance();
            int mMin = calendar.get(Calendar.MINUTE);
            int mHour = calendar.get(Calendar.HOUR_OF_DAY);

            SharedPreferences mPrefs= context.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
            int min = mPrefs.getInt("minutes_key",-1);
            int hour = mPrefs.getInt("hours_key",-1);
            if (min != -1 && min == mMin && hour == mHour) { //only show when it has not default value

                //WeightNotification.setRepeating(context);
                /*Intent service1 = new Intent(context, WeightNotifService.class);
                context.startService(service1);
                Log.v("TEST", "Service loaded at start");*/

                Intent service1 = new Intent(context, WeightNotifReceiver.class);
                context.sendBroadcast(service1);

            }
        }
    }



}
