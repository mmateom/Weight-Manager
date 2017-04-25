package com.mikel.poseidon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class ChooseManAuto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_man_auto);

        //callback to home button
        ImageButton home_butto = (ImageButton) findViewById(R.id.homebutton);
        home_butto.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent home_intent5 = new Intent(ChooseManAuto.this, MainActivity.class);
                                              //home_intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                              home_intent5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                              startActivity(home_intent5);
                                          }
                                      }


        );

        // Manually
        Button getManually = (Button) findViewById(R.id.manually);

        getManually.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent stepsRecordIntent = new Intent(ChooseManAuto.this, GetWeight.class);
                                               //stepsRecordIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                               startActivity(stepsRecordIntent);
                                           }


        });

        // Automatically
        Button getAutomatically= (Button) findViewById(R.id.automatically);

        getAutomatically.setOnClickListener(view -> {
            try{
            Intent stepsRecordIntent = new Intent(ChooseManAuto.this, GetWeightFitbit.class);
            startActivity(stepsRecordIntent);
            }
            catch (Exception e){
                Log.e("Fitbit: ", "Not connected to Fitbit");
                Toast.makeText(this, "No connected to Bitbit", Toast.LENGTH_SHORT).show();
            }

        });





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
