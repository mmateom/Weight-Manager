package com.mikel.poseidon.Activities.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.mikel.poseidon.Activities.Menu;
import com.mikel.poseidon.R;

/**
 * Created by mikel on 19/05/2017.
 */

public class PermanentNService extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        permanentNotification();


        return START_STICKY;
    }

    public void permanentNotification(){
        NotificationManager nm = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);


        //Create notification
        NotificationCompat.Builder weightNotif = new NotificationCompat.Builder(this)
                .setContentTitle("Let's get active!")
                .setContentText("Weight Manger")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmapIcon())
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setVibrate(new long[]{0L});
        //.setOngoing(true);



        Intent myIntent = new Intent(this, Menu.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
        weightNotif.setContentIntent(contentIntent);
        Notification n = weightNotif.build();
        n.flags|= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        nm.notify(100, n);
    }

    public Bitmap bitmapIcon(){
        BitmapDrawable PicDrawable = (BitmapDrawable) getDrawable(R.mipmap.ic_launcher);
        Bitmap contactPic = PicDrawable.getBitmap();

        Resources res = getResources();
        int height = (int) res.getDimension(android.R.dimen.notification_large_icon_height);
        int width = (int) res.getDimension(android.R.dimen.notification_large_icon_width);
        contactPic = Bitmap.createScaledBitmap(contactPic, width, height, false);
        return contactPic;
    }

}
