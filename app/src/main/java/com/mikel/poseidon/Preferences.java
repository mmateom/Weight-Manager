package com.mikel.poseidon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.mikel.poseidon.R.id.delete_2;

public class Preferences extends AppCompatActivity {

    DBHelper myDB;
    Button deletebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {

                Intent home_intent = new Intent(Preferences.this, MainActivity.class);
                startActivity(home_intent);
            }
        });

        myDB = new DBHelper(this);


        deletebtn = (Button) findViewById(delete_2);


        //When I press OK button get newWeight and newDate
        deletebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myDB.deleteData("Weight_Summary");

                Toast.makeText(Preferences.this,"Data deleted",Toast.LENGTH_LONG).show();

            }

        });


    }
}
