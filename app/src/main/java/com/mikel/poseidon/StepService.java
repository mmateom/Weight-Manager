package com.mikel.poseidon;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import uk.ac.mdx.cs.ie.acontextlib.IContextReceiver;
import uk.ac.mdx.cs.ie.acontextlib.hardware.StepCounter;

import static android.R.attr.name;
import static android.R.attr.value;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.mikel.poseidon.R.id.steps_counting;
import static com.mikel.poseidon.R.id.textView;

/**
 * Created by mikel on 02/12/2016.
 */

public class StepService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    StepCounter sCounter;
    //private final Random mGenerator = new Random();
    DBHelper myDB = new DBHelper(this);
    Calendar cal;
    //TextView textViewSteps;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public class LocalBinder extends Binder {
        StepService getService() {
            // Return this instance of LocalService so clients can call public methods
            return StepService.this;
        }
    }

    ArrayList<Long> Value = new ArrayList<Long>();
    long step;

    public long getSteps(){

        sCounter = new StepCounter(getApplicationContext());
        sCounter.start();

        sCounter.addContextReceiver(new IContextReceiver() {
            @Override
            public void newContextValue(String name, long value) {
                step = value;
                Value.add(step);
                //Value.add(step+1);*/



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

        long steps_counted = setSteps();

       /* System.out.println("step "+steps_counted);
        System.out.println("Array size "+ Value.size());
        System.out.println("Array size "+ Value);*/
        return steps_counted;

    }
    /** method for clients */


    public long setSteps(){

        if (Value.size() > 0) {

            for (int i = 0; i < Value.size(); i++) {

                step = step + Value.get(i);


            }
        } else {
            step = 0;
        }

        return step;


    }

    public void stopCounting (){
        sCounter.stop();

    }













}
