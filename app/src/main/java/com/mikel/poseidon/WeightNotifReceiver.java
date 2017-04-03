package com.mikel.poseidon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by mikel on 06/02/2017.
 */

public class WeightNotifReceiver extends BroadcastReceiver {

    private final static String LOG_TAG = WeightNotifReceiver.class.getName();

    // Must return quickly so just start a Service
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "AlarmReceiver invoked.");
        //context.startService(new Intent(context, WeightNotifService.class));

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Create notification
        NotificationCompat.Builder weightNotif = new NotificationCompat.Builder(context)
                .setContentTitle("Time to Weight!")
                .setContentText("Weight Manager")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri); //This sets the sound to play



        Intent myIntent = new Intent(context, ChooseManAuto.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myIntent, 0);
        weightNotif.setContentIntent(contentIntent);
        Notification n = weightNotif.build();

        nm.notify(1, n);
    }


}
