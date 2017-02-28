package com.mikel.poseidon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.mikel.poseidon.R.id.a;
import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;

public class Goals extends AppCompatActivity {

    SharedPreferences preferences;
    EditText goaltxt;

    public String STEPS_GOAL = "steps_goal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(view -> {

            Intent home_intent = new Intent(Goals.this, MainActivity.class);
            startActivity(home_intent);
        });

        preferences = getSharedPreferences(sharedPrefs, MODE_PRIVATE);

        goaltxt = (EditText)findViewById(R.id.steps_per_day);

        Button setStepsGoal = (Button)findViewById(R.id.set_steps_goal);

        setStepsGoal.setOnClickListener(view -> {
            if (!"".equals(goaltxt.getText().toString())) {

                int stepsGoal = Integer.parseInt(goaltxt.getText().toString());
                SharedPreferences.Editor goalEditor = preferences.edit();
                goalEditor.putInt(STEPS_GOAL, stepsGoal);
                goalEditor.commit();

                Toast.makeText(this, "GOAL SET", Toast.LENGTH_SHORT).show();
            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        preferences = getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        goaltxt.setText(String.valueOf(preferences.getInt(STEPS_GOAL, 0)));



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



