package com.mikel.poseidon;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.mikel.poseidon.utility.FitbitApi20;

import org.fuckboilerplate.rx_social_connect.RxSocialConnect;

import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Created by mikel on 19/01/2017.
 */

public class RootFitbitApplication extends Application {


    @Override public void onCreate() {
        super.onCreate();

        RxSocialConnect.register(this, "holacaracola")
                .using(new GsonSpeaker());



       /* NotificationManager nm = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);


        //Create notification
        NotificationCompat.Builder weightNotif = new NotificationCompat.Builder(this)
                .setContentTitle("POSEIDON")
                .setContentText("Healthy Living")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setOngoing(true);
                //This sets the sound to play



        Intent myIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
        weightNotif.setContentIntent(contentIntent);
        Notification n = weightNotif.build();

        nm.notify(1, n);*/

    }


}

