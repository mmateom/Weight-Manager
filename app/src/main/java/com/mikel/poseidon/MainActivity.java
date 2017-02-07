package com.mikel.poseidon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mikel.poseidon.utility.FitbitApi20;

import org.fuckboilerplate.rx_social_connect.RxSocialConnect;

import java.util.Calendar;

import static android.R.attr.x;
import static android.content.Context.ALARM_SERVICE;
import static com.mikel.poseidon.Preferences.sharedPrefs;
import static com.mikel.poseidon.R.id.frequency;


public class MainActivity extends AppCompatActivity {


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
                Intent weightIntent = new Intent(MainActivity.this, ChooseManAuto.class);

                // Start the new activity
                startActivity(weightIntent);
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
                Intent stepsIntent = new Intent(MainActivity.this, Steps.class);
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


        //Remind user to weight every x days
        //TODO: CUANDO HAGA LA PANTALLA PARA EDITAR LA HORA, PASAR ESOS VALORES A LOS IF
        //TODO: EN timeToWeightNotif (abajo) Y EN EL RECEIVER
        //TODO: Para que el método timeToWeightNotif comience tiene que abrirse la aplicación y acceder al main
        //TODO: lo que no tiene sentido. Habría que poner la condición en el propio método,
        //TODO: porque ahí es donde se activa el servicio




        //WeightNotification.setRepeating(getApplicationContext());
        SharedPreferences mPrefs= this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        int min = mPrefs.getInt("minute_key",-1);

        if (min != -1) {timeToWeightNotif();} //only show when it has not default value


    }

    @Override protected void onResume() {

        super.onResume();

        SharedPreferences mPrefs= this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        int min = mPrefs.getInt("minute_key",-1);
        if (min != -1) {timeToWeightNotif();} //only show when it has not default value

    }



    public void timeToWeightNotif() {

        Intent myIntent = new Intent(this , WeightNotifReceiver.class); //intent to BroadcastReveicer

        //bindService(myIntent, mConnection, Context.BIND_AUTO_CREATE);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0); //wrap intent in a PendingIntent

        //Get hour, minute and frequency from Reminders class
        SharedPreferences mPrefs= this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        int min = mPrefs.getInt("minute_key",-1);
        int hour = mPrefs.getInt("hour_key",-1);
        int frequency = mPrefs.getInt("frequency_key",-1);

        //Schedule alarm
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        //calendar.add(Calendar.DAY_OF_YEAR, 1);
        //int interval = 1000*60*10;


        //Alarm fires pendingIntent to BroadcastReceiver
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * frequency, pendingIntent);


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

}



