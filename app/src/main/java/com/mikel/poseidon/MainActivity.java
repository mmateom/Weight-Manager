package com.mikel.poseidon;

import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mikel.poseidon.utility.ExerciseNotifService;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;

public class MainActivity extends AppCompatActivity {
    int mMin, mHour, mMilis, mSeconds;
    DBHelper myDB;
    SharedPreferences mPrefs;
    private final static String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPrefs= this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);

        //Start weather service
        Intent weatherIntent = new Intent(this, WeatherReceiver.class);
        sendBroadcast(weatherIntent);

        //Exit button
        Button btn1 = (Button) findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });



        //  Declare a new thread to do a preference check for the tutorial
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

        //Initialise intent buttons
        initialiseIntents();
        //
        myDB = new DBHelper(this);
        mPrefs = this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);

        //Add toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);






        //Remind user to weight every x days
        //TODO: CUANDO HAGA LA PANTALLA PARA EDITAR LA HORA, PASAR ESOS VALORES A LOS IF
        //TODO: EN timeToWeightNotif (abajo) Y EN EL RECEIVER
        //TODO: Para que el método timeToWeightNotif comience tiene que abrirse la aplicación y acceder al main
        //TODO: lo que no tiene sentido. Habría que poner la condición en el propio método,
        //TODO: porque ahí es donde se activa el servicio


        Calendar calendar = Calendar.getInstance();
        mMilis = calendar.get(Calendar.MILLISECOND);
        mSeconds = calendar.get(Calendar.SECOND);
        mMin = calendar.get(Calendar.MINUTE);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);

        //WeightNotification.setRepeating(getApplicationContext());

        //permanentNotification();
        //timeToWeightNotif(); //only show when it has not default value



    }

    @Override protected void onResume() {

        super.onResume();

        //Weather service
        //Intent intentw = new Intent(this, WeatherService.class);
        //startService(intentw);

        int goal = mPrefs.getInt("steps_goal", 0);
        int caloriesGoal = mPrefs.getInt("calories_goal", 0);

        Calendar calendar = Calendar.getInstance();
        mMilis = calendar.get(Calendar.MILLISECOND);
        mSeconds = calendar.get(Calendar.SECOND);
        mMin = calendar.get(Calendar.MINUTE);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        String hour = String.valueOf(mHour);
        String minutes = String.valueOf(mMin);
        String seconds = String.valueOf(mSeconds);
        String time = hour + ":" + minutes + ":" + seconds;


        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
        Date date_f = null;
        try {
            date_f = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date_final = formatter.format(date_f);

        //Log.e("TIIIIMEEEEE", date_final);


        //if((goal > 0 && date_final.equals("13:00:00")) || (goal > 0 && date_final.equals("17:00:00"))) {
        if((goal > 0 && mHour >= 17 && mHour < 18)){// || (goal > 0 && mHour >= 12 && mHour <= 13)) {
                int stepProgress = (int) ((100 * todaySteps()) / goal);
                int caloriesProgress = (int) ((100 * todayCalories()) / caloriesGoal);

                if (stepProgress < 50 || caloriesProgress < 50) {
                    // Create an Explicit Intent
                    Intent intent = new Intent(this, ExerciseNotifService.class);
                    // Set some data that the Service might require/use
                    //intent.putExtra("key", "val");
                    // Start the Service
                    startService(intent);

                }

        }else if (goal == 0 && mHour >= 17 && mHour < 18){
                // Create an Explicit Intent
                Intent intent = new Intent(this, ExerciseNotifService.class);
                // Set some data that the Service might require/use
                //intent.putExtra("key", "val");
                // Start the Service
                startService(intent);

        }

        //timeToWeightNotif();//only show when it has not default value


    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public long todaySteps(){
        Cursor alldata = myDB.getTodaySumSteps();
        long todaySteps = 0;

        if(alldata.getCount() > 0) { //if count equals zero (no record today), textView automatically displays 0

            alldata.moveToFirst();
            todaySteps = alldata.getLong(1);
        }
        //previous: see COMMENTED CODE below
        return todaySteps;
    }

    public double todayCalories(){
        Cursor alldata = myDB.getCalories();
        double todayCalories = 0;

        if(alldata.getCount() > 0) { //if count equals zero (no record today), textView automatically displays 0

            alldata.moveToFirst();
            todayCalories = alldata.getDouble(3);
        }
        //previous: see COMMENTED CODE below
        return todayCalories;
    }

    public void initialiseIntents(){
        // Find the View that shows get weight
        TextView measure_weight = (TextView) findViewById(R.id.measure_weight);

        // Set a click listener on that View
        measure_weight.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the measure weight button is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link GetWeight}
                Intent weightIntent = new Intent(MainActivity.this, ChooseManAuto.class);
                //weightIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // Start the new activity
                startActivity(weightIntent);
            }
        });

        // Current state BMI
        TextView currentState = (TextView) findViewById(R.id.current_state);

        currentState.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the view summary button is clicked on.
            @Override
            public void onClick(View view) {
                Intent viewSummaryIntent = new Intent(MainActivity.this, CurrentState.class);
                //viewSummaryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(viewSummaryIntent);
            }
        });

        // weight summary
        TextView view_summary = (TextView) findViewById(R.id.view_summary);

        view_summary.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the view summary button is clicked on.
            @Override
            public void onClick(View view) {
                Intent viewSummaryIntent = new Intent(MainActivity.this, ViewSummary.class);
                startActivity(viewSummaryIntent);
            }
        });

        // preferences
        TextView preferences = (TextView) findViewById(R.id.preferences);

        preferences.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {
                Intent preferencesIntent = new Intent(MainActivity.this, PreferencesList.class);
                startActivity(preferencesIntent);
            }
        });

        //  graph
        TextView graph = (TextView) findViewById(R.id.graph);

        graph.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {
                Intent graphIntent = new Intent(MainActivity.this, Graph.class);
                startActivity(graphIntent);
            }
        });


        //  step counter
        TextView step_counter = (TextView) findViewById(R.id.steps);

        step_counter.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {
                Intent stepsIntent = new Intent(MainActivity.this, ActivityTracker.class);
                startActivity(stepsIntent);
            }
        });


        // step counter record
        TextView step_record = (TextView) findViewById(R.id.steps_record);

        step_record.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {
                Intent stepsRecordIntent = new Intent(MainActivity.this, StepsRecord.class);
                startActivity(stepsRecordIntent);
            }
        });

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



    /* private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            WeightNotifService.WeightBinder binder = (WeightNotifService.WeightBinder) service;
            mService = binder.getService();
            mBound = true;


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            mBound = false;
        }
    };*/
