package com.mikel.poseidon.statistical;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.mikel.poseidon.models.DBHelper;
import com.mikel.poseidon.MainActivity;
import com.mikel.poseidon.R;

import java.util.Calendar;


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


        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        TextView todayTotalSteps = (TextView) findViewById(R.id.sumaSteps);


        if (hourOfDay ==  24) //if it is midnight, set textview to 0 to start new count of next day
        {

            todayTotalSteps.setText("0");

        }else {
            todayTotalSteps.setText(String.valueOf(todaySteps()));
        }


    }




    public long todaySteps(){
        Cursor alldata = myDB.getTodaySumSteps();
        long todaySteps = 0;

        if(alldata.getCount() > 0) { //if count equals zero (no record today), textView automatically displays 0

            alldata.moveToFirst();
            todaySteps = alldata.getLong(1);
        }
        //previous: see COMMENTED CODE below
        return todaySteps;
    }

    @Override
    protected void onStop() {
        super.onStop();

        this.finish();
    }
}

//COMMENTED CODE
//this code used to work with an array that stored all the values on the cursor, but it had no sense
//because it only returns one value (today's value).

//int count = alldata.getCount();
//long[] dates = new long[count];

        /*
        //get dates and weight from the database and populate arrays
        for (int m = 0; m < count; m++) {
            alldata.moveToNext();
            dates[m] = alldata.getLong(1);

        }
        return dates[0]; //there will only be one entry for today, so it always takes the first one
        */