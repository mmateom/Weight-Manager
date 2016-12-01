package com.mikel.poseidon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Steps extends AppCompatActivity {

    TextView textView;
    String steps_c;
    MyBroadcastReceiver getStepCounterData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

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

        /*Bundle get_intent = getIntent().getExtras();
        steps_c = get_intent.getString("Steps");

        textView = (TextView)findViewById(R.id.steps_counting);

        textView.setText(steps_c);*/
        registerReceiver();
    }

    public void startService(View view){
        Intent intent = new Intent(Steps.this, StepIntentService.class);
        startService(intent);
    }


    public void stopService(View view){
        Intent intent = new Intent(Steps.this, StepIntentService.class);
        stopService(intent);
    }



    public class MyBroadcastReceiver extends BroadcastReceiver {
        public final static String ACTION_PASS_DATA =  "com.mikel.poseidon.intent.action.ACTION_TEXT_CAPITALIZED";


        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(StepIntentService.EXTRA_KEY_OUT);
            textView = (TextView)findViewById(R.id.steps_counting);
            textView.setText(result);
        }
    }


    private void registerReceiver() {
        IntentFilter intentFilter =new IntentFilter(MyBroadcastReceiver.ACTION_PASS_DATA);
		//create new broadcast receiver
        getStepCounterData = new MyBroadcastReceiver();
		//registering our Broadcast receiver to listen action
        registerReceiver(getStepCounterData, intentFilter);
    }
    @Override
    protected void onPause() {
        Log.i("sadf","onPause()asñodifjasñdfjasdf");
		//we should unregister BroadcastReceiver here
        unregisterReceiver(getStepCounterData);
        super.onPause();
    }

   /*@Override
    protected void onResume() {
        Log.i("asdfgasdfasf","onResume()");
		// we register BroadcastReceiver here
        registerReceiver();
        super.onResume();
    }*/




}
