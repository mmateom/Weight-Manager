package com.mikel.poseidon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.mikel.poseidon.R.id.delete_2;
import static com.mikel.poseidon.R.id.delete_steps;

public class DeleteData extends AppCompatActivity {


    DBHelper myDB;
    Button deletebtn, deletebtn_steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_data);




        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {

                Intent home_intent = new Intent(DeleteData.this, MainActivity.class);
                startActivity(home_intent);
            }
        });

        myDB = new DBHelper(this);


        deletebtn = (Button) findViewById(delete_2);


        //Delete weight data
        deletebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myDB.deleteData("Weight_Summary");

                Toast.makeText(DeleteData.this,"Data deleted",Toast.LENGTH_LONG).show();

            }

        });


        deletebtn_steps = (Button) findViewById(delete_steps);


        //Delete steps data
        deletebtn_steps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myDB.deleteDataSteps("Steps_Summary");

                Toast.makeText(DeleteData.this,"Data deleted",Toast.LENGTH_LONG).show();

            }

        });


    }
}
