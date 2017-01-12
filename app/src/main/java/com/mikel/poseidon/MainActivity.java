package com.mikel.poseidon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static android.R.attr.x;


public class MainActivity extends AppCompatActivity {


    //WeightNotifService mNotif= new WeightNotifService();

    WeightNotifService mService = new WeightNotifService();

    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        // Find the View that shows get weight
        TextView measure_weight = (TextView) findViewById(R.id.measure_weight);

        // Set a click listener on that View
        measure_weight.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the measure weight button is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link GetWeight}
                Intent weightIntent = new Intent(MainActivity.this, GetWeight.class);

                // Start the new activity
                startActivity(weightIntent);
            }
        });

        // Find the View that shows the summary
        TextView view_summary = (TextView) findViewById(R.id.view_summary);

        // Set a click listener on that View
        view_summary.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the view summary button is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link ViewSummary}
                Intent viewSummaryIntent = new Intent(MainActivity.this, ViewSummary.class);

                // Start the new activity
                startActivity(viewSummaryIntent);
            }
        });

        // Find the View that shows preferences
        TextView preferences = (TextView) findViewById(R.id.preferences);

        // Set a click listener on that View
        preferences.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link preferences}
                Intent preferencesIntent = new Intent(MainActivity.this, Preferences.class);

                // Start the new activity
                startActivity(preferencesIntent);
            }
        });

        // Find the View that shows graph
        TextView graph = (TextView) findViewById(R.id.graph);

        // Set a click listener on that View
        graph.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link graph}
                Intent graphIntent = new Intent(MainActivity.this, Graph.class);

                // Start the new activity
                startActivity(graphIntent);
            }
        });


        // Find the View that shows step counter
        TextView step_counter = (TextView) findViewById(R.id.steps);

        // Set a click listener on that View
        step_counter.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link stepcounter}
                Intent stepsIntent = new Intent(MainActivity.this, Steps.class);

                // Start the new activity
                startActivity(stepsIntent);
            }
        });


        // Find the View that shows step counter
        TextView step_record = (TextView) findViewById(R.id.steps_record);

        // Set a click listener on that View
        step_record.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link steprecord}
                Intent stepsRecordIntent = new Intent(MainActivity.this, StepsRecord.class);
                // Start the new activity
                startActivity(stepsRecordIntent);
            }
        });


        //Remind user to weight every x days



        timeToWeightNotif();



    }

    @Override protected void onResume() {
        super.onResume();
    }



    public void timeToWeightNotif() {

        Intent myIntent = new Intent(this , WeightNotifService.class);
        //bindService(myIntent, mConnection, Context.BIND_AUTO_CREATE);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 30);
        calendar.set(Calendar.MINUTE, 23);
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        int everyDayNotif = 1000*60*60*24;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


    }


    private ServiceConnection mConnection = new ServiceConnection() {

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
    };

}



