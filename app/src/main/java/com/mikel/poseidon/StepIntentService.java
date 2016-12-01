package com.mikel.poseidon;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import uk.ac.mdx.cs.ie.acontextlib.IContextReceiver;
import uk.ac.mdx.cs.ie.acontextlib.hardware.LightContext;
import uk.ac.mdx.cs.ie.acontextlib.hardware.StepCounter;

import static android.R.attr.name;
import static android.os.Build.VERSION_CODES.M;
import static com.mikel.poseidon.R.id.textView;

/**
 * Created by mikel on 30/11/2016.
 */

public class StepIntentService extends IntentService {


    public static final String EXTRA_KEY_OUT = "Steps";
    private PowerManager.WakeLock mWakeLock;
    String strValue;

    public StepIntentService() {
        super("StepIntentService");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service stoped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
//        mWakeLock.release();

    }




    @Override
    protected void onHandleIntent(Intent intent) {
        //This is what the service does



        createNotification();
        getSteps();


    }


    public void getSteps() {

        StepCounter stepCounter;

        stepCounter = new StepCounter(getApplicationContext());
        Log.e("Service ", "Service stops at this point");
        stepCounter.addContextReceiver(new IContextReceiver() {


            @Override
            public void newContextValue(String name, long value) {
                strValue = String.valueOf(value);

                        Intent intent = new Intent();
                        intent.setAction(Steps.MyBroadcastReceiver.ACTION_PASS_DATA);
                        intent.putExtra(EXTRA_KEY_OUT, strValue);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        sendBroadcast(intent);




            }

            @Override
            public void newContextValue(String name, double value) {

            }

            @Override
            public void newContextValue(String name, boolean value) {

            }

            @Override
            public void newContextValue(String name, String value) {

            }

            @Override
            public void newContextValue(String name, Object value) {

            }

            @Override
            public void newContextValues(Map<String, String> values) {

            }
        });

    }


    public void createNotification(){
        //PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
        //mWakeLock.acquire();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(getText(R.string.app_name));
        builder.setContentText("Step counter");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Intent resultIntent = new Intent(this, Steps.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, builder.build());

        //startForeground(1, builder.build());


    }





}
