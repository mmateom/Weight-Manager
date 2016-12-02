package com.mikel.poseidon;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.icu.util.Calendar;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.onClick;
import static com.mikel.poseidon.R.id.steps_counting;

public class Steps extends AppCompatActivity {

    TextView textView;
    String steps_c;
    StepService mService = new StepService();
    DBHelper myDB;

    boolean mBound = false;
    Calendar cal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        //create db
        myDB = new DBHelper(this);

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

    }


    public void onStartService(View v) {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, StepService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        //mService.createNotification();
        mBound = true;
        Toast.makeText(this, "Service binded successfully", Toast.LENGTH_SHORT).show();
    }


    public void onStopService(View v) {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
            Toast.makeText(this, "Service unbinded", Toast.LENGTH_SHORT).show();
        }else {

            Toast.makeText(this, "Service already unbinded", Toast.LENGTH_SHORT).show();
        }
    }

    /** Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute) */
    public void onBtnClick(View v) {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.

            long number = mService.getSteps();
            Toast.makeText(this, "Steps: " + number, Toast.LENGTH_SHORT).show();
            String currentStartTime = getCurrentStartTime();
            myDB.addDataSteps(currentStartTime, number);

            textView = (TextView)findViewById(steps_counting);
            textView.setText(String.valueOf(number));
        }else {
            Toast.makeText(this, "Start service first", Toast.LENGTH_SHORT).show();

        }
    }



    /** Defines callbacks for service binding, passed to bindService() */
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

    public String getCurrentStartTime (){

        cal = Calendar.getInstance();

        int millisecond = cal.get(Calendar.MILLISECOND);
        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        //24 hour format
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);

        String mlseconds = String.valueOf(millisecond);
        String seconds = String.valueOf(second);
        String minutes = String.valueOf(minute);
        String hours= String.valueOf(hourofday);

        String current_start_time = hours + ":" + minutes + ":" + seconds + ":" + mlseconds;

        return current_start_time;
    }

}





