package com.mikel.poseidon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;

import static com.mikel.poseidon.ActivityTracker.ACTIVITY;

import static com.mikel.poseidon.R.id.steps_counting;
import static com.mikel.poseidon.R.id.tv_chr;
import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;
import static com.mikel.poseidon.UserProfile.dpToPx;

public class Steps extends AppCompatActivity {

    long ko = 0;

    StepService mService = new StepService();
    DBHelper myDB;
    SharedPreferences mPrefs;


    boolean mBound = false;


    Cursor allsteps;
    private TextView mStepsTextView, caloriesText, tvChron, mHeartRateTv;

    long number;
    private BroadcastReceiver mStepsReceiver;
    private Context mContext;

    private Context context;

    private String LOG_TAG = "Which activity";
    private boolean isConnected;
    private int hr;



    //Instance of Chronometer
    private com.mikel.poseidon.utility.Chronometer mChrono;

    //private Chronometer mChrono;
    long timeWhenStopped = 0;

    //Thread for chronometer
    private Thread mThreadChrono;

    /**
     * Key for getting saved start time of Chronometer class
     * this is used for onResume/onPause/etc.
     */
    public static final String START_TIME = "START_TIME";
    /**
     * Same story, but to tell whether the Chronometer was running or not
     */
    public static final String  CHRONO_WAS_RUNNING = "CHRONO_WAS_RUNNING";
    /**
     * Same story, but if chronometer was stopped, we dont want to lose the stop time shows in
     * the tv_timer
     */
    public static final String TV_TIMER_TEXT = "TV_TIMER_TEXT";

    public static final String STEPS = "STEPS";
    private int mHeight;
    private String mGender;
    private int mAge;

    //
    String activity;
    int units;

    SharedPreferences preferences;
    NotificationManager nm;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        mPrefs= this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        units = mPrefs.getInt("weightUnits", 0);

        mContext = getApplicationContext();
        context = this;

        preferences = getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        activity = preferences.getString(ACTIVITY, "");
        mGender = preferences.getString("gender_key", "");
        mAge = preferences.getInt("age_key", 0);


        nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //create db
        myDB = new DBHelper(this);

        allsteps= myDB.getListContentsSteps();

        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {

                Intent home_intent = new Intent(Steps.this, MainActivity.class);
                startActivity(home_intent);
            }
        });

        mStepsTextView = (TextView) findViewById(steps_counting);

        caloriesText = (TextView) findViewById(R.id.calories);

        tvChron = (TextView)findViewById(tv_chr);

        mHeartRateTv =  (TextView) findViewById(R.id.heart_rate);

        FloatingActionButton infobtn = (FloatingActionButton) findViewById(R.id.fab);
        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.get_steps_info, null);
                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_steps);

                    PopupWindow popupWindow = new PopupWindow(container, dpToPx(360), dpToPx(450), true); //true allows us to close window by tapping outside
                    popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, dpToPx(0), dpToPx(120));

                    //shut popup outside window
                    container.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            popupWindow.dismiss();
                            return false;
                        }
                    });


            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();
        loadInstance(); //load chrono instance

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBReceiver();
        //Toast.makeText(this, "ON RESUME", Toast.LENGTH_SHORT).show();
        if(mChrono == null){
            tvChron.setText("00:00:00");
            ko = number = 0;
            caloriesText.setText("0");
            mStepsTextView.setText("0");
        }


//        t.start();
        Intent intent = new Intent(this, StepService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        if(!isConnected || !mBound){
            hrmonitorDialog();
        }//todo fix this
    }

    @Override
    protected void onPause(){
        super.onPause();
        unbindService(mConnection);
        mContext.unregisterReceiver(mStepsReceiver);
        //Toast.makeText(this, "ON PAUSE", Toast.LENGTH_SHORT).show();
        saveInstance(); //save chrono instance



    }





    /// METHODS
    public void setupBReceiver(){
        mStepsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();

                if (StepService.BROADCAST_INTENT_STEPS.equals(action) ) {
                    final long steps = intent.getLongExtra("steps", 0);
                    number = steps;

                    ko = number - ko;


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mStepsTextView.setText(String.valueOf(steps));


                        }
                    });
                } else if (StepService.BROADCAST_INTENT_HEART_RATE.equals(action)) {
                    hr = intent.getIntExtra("heartrate", 0);

                    //check heart rate
                    detectHighHR(activity, hr);

                    //get boolean, check if HR monitor connected
                    isConnected = intent.getBooleanExtra("isConnected", true);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mHeartRateTv.setText(String.valueOf(hr));


                        }
                    });
                }

            }
        };

        mContext.registerReceiver(mStepsReceiver, makeStepServiceIntentFilter());
        //mContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }


    //===================
    //          Service
    //===================


    public void onStartService(View v) {
        super.onStart();

        // Bind to LocalService
        mService.getSteps();
        mService.getHR();
        createNotification();
        //start chronometer
        startChrono();

        //TODO: POR QUÉ CREO UN NUEVO THREAD???
        t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /*if (activity.equals("walk")){
                                    calories = getWalkingCalories(getLastWeight(), ko);
                                    Log.e(LOG_TAG, "Walking");
                                    caloriesText.setText(String.valueOf(calories));
                                }else {
                                calories = getCalories(getLastWeight(), activity);
                                }*/
                                double calories;
                                if (!isConnected){
                                    calories = getCalories(getLastWeight(), activity);
                                    caloriesText.setText(String.valueOf(calories));
                                    Log.e("BLE monitor ON", String.valueOf(isConnected));
                                }else {
                                    calories = getCaloriesHR(getLastWeight(), mAge, hr, mGender);
                                    caloriesText.setText(String.valueOf(calories));
                                    Log.e("BLE monitor ON", String.valueOf(isConnected));
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Log.e(LOG_TAG, String.valueOf(e));
                }
            }
        };

        t.start();


        Toast.makeText(this, "TRACKING ACTIVITY", Toast.LENGTH_LONG).show();
    }

    public void hrmonitorDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Steps.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Do you want to use a Hear Rate monitor?");
        alertDialog.setIcon(R.drawable.android);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Refresh activity
                Intent intent = new Intent(getApplicationContext(), BluetoothDeviceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);


            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void onStopService(View v) {

        // Unbind from the service
        if (mBound) {
            //String currentStartTime = getCurrentStartTime();
            //CON LA DATE EN TIPO STRING METIÉNDOLO DESDE getCurrentTime(); myDB.addDataSteps(currentStartTime, number);

            //mContext.unregisterReceiver(mStepsReceiver);
            mService.stopCounting();
            mService.stopCollection();
            cancelNotification();
            //stop chronometer
  //          t.interrupt();
            stopChrono();
            //myDB.addDataSteps(tvChron.getText().toString(), getCalories(getLastWeight(), activity),  ko);
            myDB.addDataSteps(tvChron.getText().toString(), Double.parseDouble(caloriesText.getText().toString()),  ko);
            myDB.close();


            /**Maybe create another button with: tvChron.setText("00:00:00");**/
            //caloriesText.setText(String.valueOf(getCalories(getLastWeight(), activity)));

            Toast.makeText(this, "TRACKING STOPPED", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(this, "Currently stopped", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            StepService.LocalBinder binder = (StepService.LocalBinder) service;
            mService = binder.getService();

            mBound = true;


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            mBound = false;
        }
    };

    //==============================
    // Calories calculation methods
    //==============================

    /*private double getWalkingCalories(double weight, long stepsCount){

        mHeight = mPrefs.getInt("height",0);

        double walkingFactor = 0.57;
        double CaloriesBurnedPerMile;
        double strip;
        double stepCountMile; // step/mile
        double conversationFactor;
        double CaloriesBurned;

        int units = mPrefs.getInt("weightUnits", 0);

        if (units == 1){
            weight = weight * 0.453592; //from lbs to kg
        }
         CaloriesBurnedPerMile = walkingFactor * (weight * 2.2);
         strip = mHeight * 0.415;
         stepCountMile = 160934.4 / strip;
         conversationFactor = CaloriesBurnedPerMile / stepCountMile;
         CaloriesBurned = stepsCount * conversationFactor;

        return CaloriesBurned;
        /*1.) Calories burned per mile = 0.57 x 175 lbs.(your weight) = 99.75 calories per mile.

            2.)Your_strip = height * 0,415.

            3.) steps_in_1_mile = 160934.4(mile in cm) / strip.

            4.) "conversationFactor" = stepsCount (what the pedometer provides) / step_in_1_mile;

            5.) CaloriesBurned = stepsCount * conversationFactor;

    }*/

    //private double getCalories (int walkTime, double weight){
    private double getCalories (double weight, String activity){

        if (units == 1){
            weight = weight * 0.453592; //from lbs to kg
        }

        double walkTime = getMinutes(tvChron.getText().toString());

        double met = 0;

        switch (activity) {
            case "walk":
                met = 3.5;
                Log.e(LOG_TAG, "Walking");
                break;
            case "run":
                met = 7;
                Log.e(LOG_TAG, "Running");
                break;
            case "swim":
                met = 5.8;
                Log.e(LOG_TAG, "Swimming");
                break;
            case "dance":
                met = 5;
                Log.e(LOG_TAG, "Dancing");
                break;
        }

        double caloriesMin = (met * 3.5 * weight)/200;  //[(MET value) x 3.5 x (weight in kg)]/200

        return round(caloriesMin * walkTime, 0);
    }

    private double getCaloriesHR (double W, int A, int HR, String gender){

        //W = weight
        //A = age
        //T = time in HOURS
        //HR = heart rate =  beats/minute

        double caloriesHR = 0;
        if (units == 1){
            W = W * 0.453592; //from lbs to kg
        }

        double T = getMinutes(tvChron.getText().toString());

        switch (gender){
            case "Male":
                caloriesHR = ((-55.0969 + (0.6309 * HR) + (0.1988 * W) + (0.2017 * A))/4.184) * T;
                return round(caloriesHR, 0);

            case "Female":
                caloriesHR += ((-20.4022 + (0.4472 * HR) - (0.1263 * W) + (0.074 * A))/4.184) * 60 * T;
                return round(caloriesHR, 0);

            default: return -1;

        }


    }

    private double getLastWeight() {

        Cursor alldata;
        ArrayList<Double> yVals;
        alldata= myDB.getListContents();
        double lastWeight;

        int count = alldata.getCount();
        double[] weights = new double[count];

        yVals = new ArrayList<Double>();

        //get dates and weight from the database and populate arrays
        for (int m = 0; m < count; m++) {
            alldata.moveToNext();
            weights[m] = alldata.getDouble(4);

            yVals.add(weights[m]);


        }


        if(yVals.size() == 0){

            lastWeight = 0;

        }else lastWeight = yVals.get(yVals.size() - 1);

        //lastWeight = yVals.get(yVals.size() - 1);

        return lastWeight;

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    //=================
    //   CHRONOMETER
    //=================


    private void startChrono(){
        if(mChrono == null) {
            //instantiate the chronometer
            mChrono = new com.mikel.poseidon.utility.Chronometer(context);
            //run the chronometer on a separate thread
            mThreadChrono = new Thread((Runnable) mChrono);
            mThreadChrono.start();

            //start the chronometer!
            mChrono.start();

        }
    }

    private void stopChrono(){
        //if the chronometer had been instantiated before...
        if(mChrono != null) {
            //stop the chronometer
            mChrono.stop();
            //stop the thread
            mThreadChrono.interrupt();
            mThreadChrono = null;
            //kill the chrono class
            mChrono = null;
        }

    }

    public void updateTimerText(final String timeAsText) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvChron.setText(timeAsText);
            }
        });
    }



    /**
     * If the application goes to background or orientation change or any other possibility that
     * will pause the application, we save some instance values, to resume back from last state
     */
    private void saveInstance() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //TODO move tags to a static class
        if(mChrono != null && mChrono.isRunning()) {
            editor.putBoolean(CHRONO_WAS_RUNNING, mChrono.isRunning());
            editor.putLong(START_TIME, mChrono.getStartTime());
        } else {
            editor.putBoolean(CHRONO_WAS_RUNNING, false);
            editor.putLong(START_TIME, 0); //0 means chronometer was not active! a redundant check!
        }


        //Same story for timer text
        editor.putString(TV_TIMER_TEXT, tvChron.getText().toString());

        editor.commit();
    }


    /**
     * Load the shared preferences to resume last known state of the application
     */
    private void loadInstance() {

        SharedPreferences pref = getPreferences(MODE_PRIVATE);

        //if chronometer was running
        if(pref.getBoolean(CHRONO_WAS_RUNNING, false)) {
            //get the last start time from the bundle
            long lastStartTime = pref.getLong(START_TIME, 0);
            //if the last start time is not 0
            if(lastStartTime != 0) { //because 0 means value was not saved correctly!

                if(mChrono == null) { //make sure we dont create new instance and thread!

                    if(mThreadChrono != null) { //if thread exists...first interrupt and nullify it!
                        mThreadChrono.interrupt();
                        mThreadChrono = null;
                    }

                    //start chronometer with old saved time
                    mChrono = new com.mikel.poseidon.utility.Chronometer(context, lastStartTime);
                    mThreadChrono = new Thread(mChrono);
                    mThreadChrono.start();
                    mChrono.start();
                }
            }
        }



        String oldTvTimerText = pref.getString(TV_TIMER_TEXT, "");
        if(!oldTvTimerText.isEmpty()){
            tvChron.setText(oldTvTimerText);
        }
    }


    /**
     * Turns a period of time into the number of minutes represented
     * (eg. 06:30:15 returns 360.25)
     * @param hourFormat The string containing the hour format "HH:MM:SS"
     * @return The number of minutes represented, or -1 if the date could not be processed.
     */
    public static double getMinutes(String hourFormat) {

        double minutes = 0;
        String[] split = hourFormat.split(":");

        try {

            minutes += Double.parseDouble(split[0])*60;
            minutes += Double.parseDouble(split[1]);
            minutes += Double.parseDouble(split[2])/60;

            Log.e("MINUTES", String.valueOf(minutes));
            return minutes;

        } catch (Exception e) {
            return -1;
        }

    }

    public static double getHours(String hourFormat) {

        double hours = 0;
        String[] split = hourFormat.split(":");

        try {

            hours += Double.parseDouble(split[0]);
            hours += Double.parseDouble(split[1])/60;
            hours += Double.parseDouble(split[2])/3600;

            Log.e("MINUTES", String.valueOf(hours));
            return hours;

        } catch (Exception e) {
            return -1;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //stopChrono();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //=======================================================
    //        CREATE NOTIFICATION - when start is pushed
    //=======================================================

    public void createNotification() {

        SharedPreferences preferences;
        preferences = getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        String activity = preferences.getString(ACTIVITY, "");



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("Monitoring Activity - POSEIDON")
                .setContentText("Activity: " +activity)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setAutoCancel(false);

        Intent resultIntent = new Intent(this, Steps.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, 0);
        builder.setContentIntent(resultPendingIntent);

        //startForeground(1, builder.build());

        builder.setContentIntent(resultPendingIntent);
        Notification n = builder.build();

        nm.notify(1, n);

    }

    public void cancelNotification(){

        nm.cancel(1);
    }


    //=======================================================
    //                CREATE WARNING NOTIFICATION
    //=======================================================
    public void warningNotification() {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("BE CAREFUL! - HIGH HEART RATE")
                .setContentText("STOP OR REDUCE SPEED")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(false)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri);

        Intent resultIntent = new Intent(this, Steps.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, 0);
        builder.setContentIntent(resultPendingIntent);

        //startForeground(1, builder.build());

        builder.setContentIntent(resultPendingIntent);
        Notification n = builder.build();

        nm.notify(1, n);

    }

    private void detectHighHR(String activity, int bpm){
        switch (activity) {
            case "walk":
                if(bpm > 120){
                    warningNotification();
                }
                break;
            case "run":
                if(bpm > 140){
                    warningNotification();
                }
                break;

        }

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    //// Intent filter for StepService broadcast intents

    private static IntentFilter makeStepServiceIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StepService.BROADCAST_INTENT_STEPS);
        intentFilter.addAction(StepService.BROADCAST_INTENT_HEART_RATE);
        return intentFilter;
    }

}










    /**public void pauseCounting(View v){

        // Unbind from the service
        if (mBound) {
            isPaused = true;
            //String currentStartTime = getCurrentStartTime();
            //CON LA DATE EN TIPO STRING METIÉNDOLO DESDE getCurrentTime(); myDB.addDataSteps(currentStartTime, number);
            myDB.addDataSteps(ko);
            //mContext.unregisterReceiver(mStepsReceiver);
            mService.stopCounting();

            //stop chronometer
            mChrono.stop();
            saveInstance();
            ///Maybe create another button with: tvChron.setText("00:00:00");
            caloriesText.setText(String.valueOf(getCalories(getLastWeight(), activity)));

            Toast.makeText(this, "PAUSE", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(this, "Currently stopped", Toast.LENGTH_SHORT).show();
        }


    }*/





