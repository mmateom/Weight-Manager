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
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent serviceLauncher = new Intent(context, WeightNotifService.class);

            //TODO: VER SI REALMENTE FUNCIONA ESTO. SÍ QUE RECUERDA CADA MINUTO, PERO NO SABEMOS SI LO HACE NADA MÁS ENCENDERSE O A LA HORA INDICADA
            //TODO: SI NO ES ASÍ PONER EN EL IF "&& QUE CUMPLA EL TIEMPO"

            AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, serviceLauncher, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 30);
            calendar.set(Calendar.MINUTE, 15);
            calendar.set(Calendar.HOUR_OF_DAY, 13);
            //calendar.add(Calendar.DAY_OF_YEAR, 1);
            int everyDayNotif = 1000*60*60*24;

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


            context.startService(serviceLauncher);
            Log.v("TEST", "Service loaded at start");
        }
    }



}
