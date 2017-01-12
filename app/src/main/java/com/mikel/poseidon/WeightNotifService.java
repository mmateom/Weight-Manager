package com.mikel.poseidon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by mikel on 11/01/2017.
 */

public class WeightNotifService extends Service {

    private final IBinder mBinder = new WeightBinder();


    @Override
    public void onCreate() {



    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class WeightBinder extends Binder {
        WeightNotifService getService() {
            // Return this instance of LocalService so clients can call public methods

            return WeightNotifService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationManager nm = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Create notification
        NotificationCompat.Builder weightNotif = new NotificationCompat.Builder(this)
                .setContentTitle(getText(R.string.app_name))
                .setContentText("Time to weight!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri); //This sets the sound to play



        Intent myIntent = new Intent(this , GetWeight.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
        weightNotif.setContentIntent(contentIntent);
        Notification n = weightNotif.build();

        nm.notify(1, n);


        //startForeground(1, weightNotif.build());
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
