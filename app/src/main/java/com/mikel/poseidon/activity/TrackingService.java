package com.mikel.poseidon.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mikel.poseidon.utility.ExplicitIntentGenerator;

import org.poseidon_project.context.IContextReasoner;

import java.util.Map;

import uk.ac.mdx.cs.ie.acontextlib.IContextReceiver;
import uk.ac.mdx.cs.ie.acontextlib.hardware.StepCounter;
import uk.ac.mdx.cs.ie.acontextlib.personal.HeartRateMonitor;

import static com.mikel.poseidon.preferences.SetGraphLimits.sharedPrefs;

/**
 * Created by mikel on 02/12/2016.
 */

public class TrackingService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    StepCounter sCounter;
    private boolean mBound;
    private boolean mCollecting = false;
    private boolean mCollectingHeartRate = false;

    //TextView textViewSteps;
    public static final String BROADCAST_INTENT_STEPS = "com.mikel.poseidon.TOTAL_STEPS";
    public static final String BROADCAST_INTENT_HEART_RATE = "com.mikel.poseidon.HEART_RATE";
    private Context mContext;
    private PowerManager.WakeLock mWakeLock;
    private IContextReasoner mContextService;


    //Heart rate variables
    private HeartRateMonitor mHeartrateMonitor;
    private int heart_rate;
    private static final int INTERVAL = 60000;
    private boolean mHeartMonitorBonded = false;
    private SharedPreferences mSettings;
    private static final int HR_INTERVAL = 20000;
    long step;

    @Override
    public void onCreate() {
        super.onCreate();

        step = 0;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        TrackingService getService() {
            // Return this instance of LocalService so clients can call public methods

            return TrackingService.this;
        }
    }



    public long getSteps(){

        // We want to avoid people trying to start the "service" more than once
        if (mCollecting) {
            return step;
        }

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
        mWakeLock.acquire();

        mContext = getApplicationContext();
        sCounter = new StepCounter(mContext);
        sCounter.start();

        sCounter.addContextReceiver(new IContextReceiver() {
            @Override
            public void newContextValue(String name, long value) {
                step += value;
                //Value.add(step);
                //Value.add(step+1);*/
                broadcastSteps();


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

        Intent serviceIntent = new Intent(IContextReasoner.class.getName());

        serviceIntent = ExplicitIntentGenerator
                .createExplicitFromImplicitIntent(mContext, serviceIntent);

        if (serviceIntent != null) {
            bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        } else {
            Log.e("POSEIDON", "Context Reasoner not installed!");
        }

        //createNotification();

        return 0;

    }

    public void getHR() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            mHeartrateMonitor = new HeartRateMonitor(mContext);
            startCollecting();
            mHeartrateMonitor.addContextReceiver(new IContextReceiver() {
                @Override
                public void newContextValue(String name, long value) {
                    heart_rate = ((int) value);
                    broadcastHeartRate();
                }

                @Override
                public void newContextValue(String name, double value) {

                }

                @Override
                public void newContextValue(String name, boolean value) {
                    if (value == false) {
                        Log.e("TrackingService", "not getting data");
                    }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            mSettings = mContext.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
            String device = mSettings.getString("macaddress", "");

            if (!device.equals("")) {
                mHeartMonitorBonded = true;
                mHeartrateMonitor.setDeviceID(device);
                mHeartrateMonitor.setConnectRetry(true);

                mHeartrateMonitor.start();
                interval = HR_INTERVAL;
                mCollectingHeartRate = true;
            }else mHeartMonitorBonded =  false;

        }



    }

    public void stopCollection() {
        if (mCollectingHeartRate) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (mHeartMonitorBonded) {
                    mHeartrateMonitor.stop();
                }
            }

            mCollectingHeartRate = false;

        }
    }




    /** method for clients */


    public void broadcastSteps() {

        Intent intent = new Intent();
        try {
            intent.setAction(BROADCAST_INTENT_STEPS);
            intent.putExtra("steps",step);
            mContext.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("StopService", e.getMessage());
        }

    }

    public void broadcastHeartRate(){
        Intent intent_hr = new Intent();
        try {
            intent_hr.setAction(BROADCAST_INTENT_HEART_RATE);
            intent_hr.putExtra("heartrate", heart_rate);
            intent_hr.putExtra("isConnected", mHeartMonitorBonded);
            mContext.sendBroadcast(intent_hr);
        } catch (Exception e) {
            Log.e("StopService", e.getMessage());
        }

    }

   /* public long setSteps(){

        if (Value.size() > 0) {

            for (int i = 0; i < Value.size(); i++) {

                step = step + Value.get(i);


            }
        } else {
            step = 0;
        }

        return step;


    }*/

   public void stopCounting (){


       //Only allow stopping if its collecting.
       if (mCollecting) {
           //sCounter.stop(); this somehow stops counting but crushes the counter
           return;
       }

       try {
           if (sCounter.isRunning()) {
               sCounter.stop();
           }
       }catch (Exception e){
           Log.e("StepCounter", e.getMessage());
       }

       Intent intent = new Intent();
       try {
           intent.setAction("org.poseidon_project.context.EXTERNAL_CONTEXT_UPDATE");
           intent.putExtra("context_name", "Activity");
           intent.putExtra("context_value_type","long");
           intent.putExtra("context_value",step);
           mContext.sendBroadcast(intent);
       } catch (Exception e) {
           Log.e("StopService", e.getMessage());
       }

       if (mBound) {
           unbindService(mConnection);
           mBound = false;
       }


       //Autodismiss the notification
       //stopForeground(true);


       try{mWakeLock.release();}
       catch(Exception e){Log.e("StepCounter", e.getMessage());}

   }

    /*@Override
    public void onDestroy() {


        super.onDestroy();
    }*/

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mContextService  = IContextReasoner.Stub.asInterface(iBinder);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mContextService = null;
            mBound = false;
        }
    };








}
