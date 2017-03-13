package com.mikel.poseidon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikel.poseidon.utility.Chronometer;

import static com.mikel.poseidon.R.id.measure_weight;
import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;
import static com.mikel.poseidon.Steps.CHRONO_WAS_RUNNING;

public class ActivityTracker extends AppCompatActivity {


    public static final String ACTIVITY = "ACTIVITY";

    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        //callback to home button
        ImageButton home_button4 = (ImageButton) findViewById(R.id.homebutton);
        home_button4.setOnClickListener(view -> {

            Intent home_intent4 = new Intent(ActivityTracker.this, MainActivity.class);
            startActivity(home_intent4);
        });

        //

        preferences = getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //

        // WALK
        TextView walkText = (TextView) findViewById(R.id.walk);
        walkText.setOnClickListener(view -> {
            Intent walkIntent = new Intent(ActivityTracker.this, Steps.class);
            editor.putString(ACTIVITY, "walk");
            editor.apply();
            startActivity(walkIntent);
        });

        // RUN
        TextView runText = (TextView) findViewById(R.id.run);
        runText.setOnClickListener(view -> {
            Intent runIntent = new Intent(ActivityTracker.this, Steps.class);
            editor.putString(ACTIVITY, "run");
            editor.commit();
            startActivity(runIntent);
        });

        // SWIM
        TextView swimText = (TextView) findViewById(R.id.swim);
        swimText.setOnClickListener(view -> {
            Intent swimIntent = new Intent(ActivityTracker.this, Steps.class);
            editor.putString(ACTIVITY, "swim");
            editor.commit();
            startActivity(swimIntent);
        });

        // DANCE
        TextView danceText = (TextView) findViewById(R.id.dance);
        danceText.setOnClickListener(view -> {
            Intent danceIntent = new Intent(ActivityTracker.this, Steps.class);
            editor.putString(ACTIVITY, "dance");
            editor.commit();
            startActivity(danceIntent);
        });

        // D

        TextView current = (TextView) findViewById(R.id.current_activity);
        current.setVisibility(View.INVISIBLE);





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
