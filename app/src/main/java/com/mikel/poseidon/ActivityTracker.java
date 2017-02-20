package com.mikel.poseidon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.mikel.poseidon.R.id.measure_weight;

public class ActivityTracker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(view -> {

            Intent home_intent = new Intent(ActivityTracker.this, MainActivity.class);
            startActivity(home_intent);
        });

        // WALK
        TextView walkText = (TextView) findViewById(R.id.walk);
        walkText.setOnClickListener(view -> {
            Intent walkIntent = new Intent(ActivityTracker.this, Steps.class);
            startActivity(walkIntent);
        });

        // RUN
        TextView runText = (TextView) findViewById(R.id.run);
        runText.setOnClickListener(view -> {
            Intent runIntent = new Intent(ActivityTracker.this, Steps.class);
            startActivity(runIntent);
        });

        // SWIM
        TextView swimText = (TextView) findViewById(R.id.swim);
        runText.setOnClickListener(view -> {
            Intent swimIntent = new Intent(ActivityTracker.this, Steps.class);
            startActivity(swimIntent);
        });

        // DANCE
        TextView danceText = (TextView) findViewById(R.id.dance);
        danceText.setOnClickListener(view -> {
            Intent danceIntent = new Intent(ActivityTracker.this, Steps.class);
            startActivity(danceIntent);
        });



    }
}
