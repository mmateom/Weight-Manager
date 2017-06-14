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
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.mikel.poseidon.Activities.activity.ChooseActivity;
import com.mikel.poseidon.Activities.preferences.Goals;
import com.mikel.poseidon.R;

/**
 * Created by mikel on 20/03/2017.
 */

public class ExerciseNService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        boolean hasGoal = intent.getBooleanExtra("hasGoal", false);
        if(hasGoal){
            hasExercisedNotif();
        }else putGoalNotif();


        return START_STICKY;


    }

    private void putGoalNotif() {
        // START YOUR TASKS
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Create notification
        NotificationCompat.Builder weightNotif = new NotificationCompat.Builder(this)
                .setContentTitle("Do you want to set a steps goal?")
                .setContentText("Weight Manager")
                .setLargeIcon(bitmapIcon())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri); //This sets the sound to play



        Intent myIntent = new Intent(this, Goals.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
        weightNotif.setContentIntent(contentIntent);
        Notification n = weightNotif.build();

        nm.notify(2, n);
    }


    private void hasExercisedNotif(){
        // START YOUR TASKS
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        //Create notification
        NotificationCompat.Builder weightNotif = new NotificationCompat.Builder(this)
                .setContentTitle("Remember to complete goals!")
                .setContentText("Weight Manager")
                .setLargeIcon(bitmapIcon())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri); //This sets the sound to play



        Intent myIntent = new Intent(this, ChooseActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
        weightNotif.setContentIntent(contentIntent);
        Notification n = weightNotif.build();

        nm.notify(1, n);
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
