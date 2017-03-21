package com.mikel.poseidon.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.mikel.poseidon.ActivityTracker;
import com.mikel.poseidon.ChooseManAuto;
import com.mikel.poseidon.MainActivity;
import com.mikel.poseidon.R;

/**
 * Created by mikel on 20/03/2017.
 */

public class ExerciseNotifService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // START YOUR TASKS
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Create notification
        NotificationCompat.Builder weightNotif = new NotificationCompat.Builder(this)
                .setContentTitle("Have you exercised today?")
                .setContentText("WeightManager")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri); //This sets the sound to play



        Intent myIntent = new Intent(this, ActivityTracker.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
        weightNotif.setContentIntent(contentIntent);
        Notification n = weightNotif.build();

        nm.notify(1, n);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // STOP YOUR TASKS
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
