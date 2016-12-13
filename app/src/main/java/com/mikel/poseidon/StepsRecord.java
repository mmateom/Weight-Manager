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
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
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

        //populate an ArrayList<String> from the database and then view it
        //ArrayList<String> theList = new ArrayList<>();
        Cursor dataSteps = myDB.getListContentsSteps();
        /*if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_SHORT).show();
        }else{
            while(data.moveToNext()){
                theList.add(data.getString(2));
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(listAdapter);
            }
        }*/

        startManagingCursor(dataSteps);

        String[] fromFieldNames = new String[]
                {DBHelper.ACT_START, DBHelper.STEPS};
        int[] toViewIDs = new int[]
                {R.id.date_item, R.id.weight_item};//cambiar esto

        SimpleCursorAdapter myCursorAdapterSteps = new SimpleCursorAdapter(

                this,                           //Context
                R.layout.item_layout, //Row layout template
                dataSteps,                           //Cursor named 'data' gets all the content on DB
                fromFieldNames,                 //DB column names
                toViewIDs);                     //view IDs to put information in


        ListView listViewSteps = (ListView) findViewById(R.id.listView_steps); //TODO: por qué es nulo?????
        listViewSteps.setAdapter(myCursorAdapterSteps);

    }
}