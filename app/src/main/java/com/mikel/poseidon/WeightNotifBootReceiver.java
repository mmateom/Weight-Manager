package com.mikel.poseidon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static com.mikel.poseidon.WeightNotification.setRepeating;

/**
 * Created by mikel on 12/01/2017.
 */

public class WeightNotifBootReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){

           //WeightNotification.setRepeating(context);
            Intent service1 = new Intent(context, WeightNotifService.class);
            context.startService(service1);
            Log.v("TEST", "Service loaded at start");
        }
    }



}
