package com.mikel.poseidon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by mikel on 06/02/2017.
 */

public class WeightNotifReceiver extends BroadcastReceiver {

    private final static String LOG_TAG = WeightNotifReceiver.class.getName();


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "AlarmReceiver invoked.");
        //context.startService(new Intent(context, WeightNotifService.class));


        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        //
        BitmapDrawable PicDrawable = (BitmapDrawable)context.getDrawable(R.mipmap.ic_launcher);
        Bitmap contactPic = PicDrawable.getBitmap();

        Resources res = context.getResources();
        int height = (int) res.getDimension(android.R.dimen.notification_large_icon_height);
        int width = (int) res.getDimension(android.R.dimen.notification_large_icon_width);
        contactPic = Bitmap.createScaledBitmap(contactPic, width, height, false);


        //Create notification
        NotificationCompat.Builder weightNotif = new NotificationCompat.Builder(context)
                .setContentTitle("Time to Weigh!")
                .setContentText("Weight Manager")
                .setLargeIcon(contactPic)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri); //This sets the sound to play




        Intent myIntent = new Intent(context, ChooseManAuto.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myIntent, 0);
        weightNotif.setContentIntent(contentIntent);
        Notification n = weightNotif.build();

        nm.notify(101, n);


    }

}
