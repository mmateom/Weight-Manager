package com.mikel.poseidon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


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










    }

    @Override protected void onResume() {
        super.onResume();
    }


}



