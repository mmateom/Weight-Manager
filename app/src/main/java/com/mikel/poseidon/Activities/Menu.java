package com.mikel.poseidon.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mikel.poseidon.Activities.activity.ChooseActivity;
import com.mikel.poseidon.Activities.intro.Tutorial;
import com.mikel.poseidon.Activities.notifications.NotifReceiver;
import com.mikel.poseidon.Activities.preferences.PreferencesList;
import com.mikel.poseidon.Activities.recordweight.ChooseManAuto;
import com.mikel.poseidon.Activities.viewstatistics.Summary;
import com.mikel.poseidon.Activities.viewstatistics.Graph;
import com.mikel.poseidon.Activities.viewstatistics.StepsRecord;
import com.mikel.poseidon.Activities.viewstatistics.WeightRecord;
import com.mikel.poseidon.Model.DBHelper;
import com.mikel.poseidon.R;


import java.util.Calendar;


import static com.mikel.poseidon.Activities.preferences.SetGraphLimits.sharedPrefs;

public class Menu extends AppCompatActivity {
    int mMin, mHour, mMilis, mSeconds;
    DBHelper myDB;
    SharedPreferences mPrefs;
    private final static String TAG = Menu.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPrefs= this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);

        //Start notification services
        Intent weatherIntent = new Intent(this, NotifReceiver.class);
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
                    Intent i = new Intent(Menu.this, Tutorial.class);
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
        //Intent intentw = new Intent(this, WeatherNService.class);
        //startService(intentw);


        //timeToWeightNotif();//only show when it has not default value


    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                Intent weightIntent = new Intent(Menu.this, ChooseManAuto.class);
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
                Intent viewSummaryIntent = new Intent(Menu.this, Summary.class);
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
                Intent viewSummaryIntent = new Intent(Menu.this, WeightRecord.class);
                startActivity(viewSummaryIntent);
            }
        });

        // preferences
        TextView preferences = (TextView) findViewById(R.id.preferences);

        preferences.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {
                Intent preferencesIntent = new Intent(Menu.this, PreferencesList.class);
                startActivity(preferencesIntent);
            }
        });

        //  graph
        TextView graph = (TextView) findViewById(R.id.graph);

        graph.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {
                Intent graphIntent = new Intent(Menu.this, Graph.class);
                startActivity(graphIntent);
            }
        });


        //  step counter
        TextView step_counter = (TextView) findViewById(R.id.steps);

        step_counter.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {
                Intent stepsIntent = new Intent(Menu.this, ChooseActivity.class);
                startActivity(stepsIntent);
            }
        });


        // step counter record
        TextView step_record = (TextView) findViewById(R.id.steps_record);

        step_record.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {
                Intent stepsRecordIntent = new Intent(Menu.this, StepsRecord.class);
                startActivity(stepsRecordIntent);
            }
        });

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
