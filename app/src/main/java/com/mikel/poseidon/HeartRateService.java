package com.mikel.poseidon;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.poseidon_project.context.IContextReasoner;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import uk.ac.mdx.cs.ie.acontextlib.IContextReceiver;
import uk.ac.mdx.cs.ie.acontextlib.personal.HeartRateMonitor;

import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;

/**
 * Created by mikel on 25/04/2017.
 */

public class HeartRateService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private boolean mBound;
    private boolean mCollecting = false;
    HeartRateMonitor mHeartrateMonitor;
    private PowerManager.WakeLock mWakeLock;
    private long hr;
    private static final int INTERVAL = 60000;
    private static final int HR_INTERVAL = 20000;
    public static final int HEARTMONITOR_STATUS = 1;
    private SharedPreferences mSettings;
    private boolean mHeartMonitorBonded = false;
    //String device = "DD:05:50:CE:DA:95";
    Context mContext;
    public static final String BROADCAST_INTENT = "com.mikel.poseidon.BPM";



    @Override
    public void onCreate() {

        mContext = getApplicationContext();


    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        HeartRateService getService() {
            // Return this instance of LocalService so clients can call public methods

            return HeartRateService.this;
        }
    }

    public void getHR() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mContext = getApplicationContext();
            mHeartrateMonitor = new HeartRateMonitor(mContext);

            startCollecting();

            mHeartrateMonitor.addContextReceiver(new IContextReceiver() {
                @Override
                public void newContextValue(String name, long value) {
                    hr = value;
                    broadcastBPM();
                }

                @Override
                public void newContextValue(String name, double value) {

                }

                @Override
                public void newContextValue(String name, boolean value) {
               /* if (value == false) {
                    mService.showConnectionTroubleNotification();
                } else {
                    mService.dismissConnectionNotification();
                }*/
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
    }



    public void startCollecting() {
        int interval = INTERVAL;


        if (VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            mSettings = mContext.getSharedPreferences("com.mikel.poseidon", MODE_PRIVATE);
            String device = mSettings.getString("macaddress", "");

            if (!device.equals("")) {
                mHeartMonitorBonded = true;
                mHeartrateMonitor.setDeviceID(device);
                mHeartrateMonitor.setConnectRetry(true);
                mHeartrateMonitor.start();
                interval = HR_INTERVAL;
            }

        }

        mCollecting = true;

    }

    public void stopCollection() {
        if (mCollecting) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (mHeartMonitorBonded) {
                    mHeartrateMonitor.stop();
                }
            }

            mCollecting = false;

            if (mBound) {
                unbindService(mConnection);
                mBound = false;
            }

        }
    }

    public void broadcastBPM() {

        Intent intent = new Intent();
        try {
            intent.setAction(BROADCAST_INTENT);
            intent.putExtra("bpm", hr);
            mContext.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("StopService", e.getMessage());
        }

    }







    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };





}
