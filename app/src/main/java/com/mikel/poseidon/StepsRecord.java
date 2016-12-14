package com.mikel.poseidon;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.mikel.poseidon.DBHelper.TABLE_NAME_STEPS;
import static com.mikel.poseidon.R.id.listView;


public class StepsRecord extends AppCompatActivity {

    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_steps_record);

        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {

                Intent home_intent = new Intent(StepsRecord.this, MainActivity.class);
                startActivity(home_intent);
            }
        });

        myDB = new DBHelper(this);

        Cursor dataSteps = myDB.getStepsByDate();

        startManagingCursor(dataSteps);

        String[] fromFieldNames = new String[]
                {DBHelper.ACT_START, DBHelper.STEPS};
        int[] toViewIDs = new int[]
                {R.id.date_item, R.id.weight_item};//puede que haya que cambiar esto en un futuro

        SimpleCursorAdapter myCursorAdapterSteps = new SimpleCursorAdapter(

                this,                           //Context
                R.layout.item_layout, //Row layout template
                dataSteps,                           //Cursor named 'data' gets all the content on DB
                fromFieldNames,                 //DB column names
                toViewIDs);                     //view IDs to put information in


        ListView listViewSteps = (ListView) findViewById(R.id.listView_steps);
        listViewSteps.setAdapter(myCursorAdapterSteps);


        TextView todayTotalSteps  = (TextView) findViewById(R.id.sumaSteps);
        todayTotalSteps.setText(String.valueOf(todaySteps()));

    }

    //TODO: crear una DB para solo los pasos por día




    public long todaySteps(){
        Cursor alldata = myDB.getStepsSumByDate();
        int count = alldata.getCount();
        long[] dates = new long[count];



        //get dates and weight from the database and populate arrays
        for (int m = 0; m < count; m++) {
            alldata.moveToNext();
            dates[m] = alldata.getLong(1);


        }

        return dates[0]; //TODO: que cuando sea la fecha de hoy me returnee la suma

    }
}