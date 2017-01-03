package com.mikel.poseidon;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static com.mikel.poseidon.R.id.steps_counting;

public class Steps extends AppCompatActivity {

    StepService mService = new StepService();
    DBHelper myDB;

    boolean mBound = false;
    Calendar cal;

    ArrayList<Long> stepArray;
    Cursor allsteps;
    private TextView mStepsTextView;

    long number;
    private BroadcastReceiver mStepsReceiver;
    private Context mContext;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        mContext = getApplicationContext();

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


    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBReceiver();

        Intent intent = new Intent(this, StepService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause(){
        super.onPause();
        unbindService(mConnection);
        mContext.unregisterReceiver(mStepsReceiver);
    }

    public void setupBReceiver(){
        mStepsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final long steps = intent.getLongExtra("steps", 0);
                number = steps;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mStepsTextView.setText(String.valueOf(steps));
                    }
                });
            }
        };

        mContext.registerReceiver(mStepsReceiver, new IntentFilter(StepService.BROADCAST_INTENT));

    }


    public void onStartService(View v) {
        super.onStart();
        // Bind to LocalService
        mService.getSteps();
        Toast.makeText(this, "COUNTING YOUR STEPS", Toast.LENGTH_LONG).show();
    }


    public void onStopService(View v) {

        // Unbind from the service
        if (mBound) {
            //String currentStartTime = getCurrentStartTime();
            //CON LA DATE EN TIPO STRING METIÉNDOLO DESDE getCurrentTime(); myDB.addDataSteps(currentStartTime, number);
            myDB.addDataSteps(number);
            mService.stopCounting();
            Toast.makeText(this, "Step counter STOPPED", Toast.LENGTH_LONG).show();
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



    //=========================================
    //             GET CURRENT TIME
    //=========================================
   /* public String getCurrentStartTime() {

        cal = Calendar.getInstance();


        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);//24 hour format
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) +1; //plus 1, because months star in 0 in java
        int year = cal.get(Calendar.YEAR);

        String date_final = null;


        String current_start_time = day + "-" + month + "-" + year + " " + hourofday + ":" + minute + ":" +second;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        try {

            Date date_f = formatter.parse(current_start_time);
            date_final = formatter.format(date_f);



            System.out.println(date_f);
            System.out.println(date_final);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date_final;
    }*/

}





