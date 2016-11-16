package com.mikel.poseidon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        // Find the View that shows the numbers category
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

        // Find the View that shows the numbers category
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

        // Find the View that shows the numbers category
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





    }


}



