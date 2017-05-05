package com.mikel.poseidon;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.data;
import static com.mikel.poseidon.R.id.currentWeightUnit;
import static com.mikel.poseidon.R.id.summary_unit;
import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;


public class ViewSummary extends AppCompatActivity {

    DBHelper myDB;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_summary);

        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(view -> {

            Intent home_intent = new Intent(ViewSummary.this, MainActivity.class);
            startActivity(home_intent);
        });

        //set unit
        mPrefs= this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        TextView unit = (TextView)findViewById(summary_unit);
        int units = mPrefs.getInt("weightUnits", 0);
        if (units == 1){
            unit.setText("Weight (lbs)");
        }

        //create db instance
        myDB = new DBHelper(this);

        //populate an ArrayList<String> from the database and then view it
        //ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        /*if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_SHORT).show();
        }else{
            while(data.moveToNext()){
                theList.add(data.getString(2));
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(listAdapter);
            }
        }*/

        startManagingCursor(data);

        String[] fromFieldNames = new String[]
                {DBHelper.DATE, DBHelper.WEIGHT};
        int[] toViewIDs = new int[]
                {R.id.date_item, R.id.weight_item};

        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(

                this,                           //Context
                R.layout.item_layout, //Row layout template
                data,                           //Cursor named 'data' gets all the content on DB
                fromFieldNames,                 //DB column names
                toViewIDs);                     //view IDs to put information in


        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(myCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewSummary.this);
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Delete item?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Delete entry from
                        myDB.deleteSingleWeight(String.valueOf(id));


                        // Refresh activity
                        Intent intent = getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "Weight entry deleted", Toast.LENGTH_SHORT).show();

                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();

            }

        });


        myCursorAdapter.notifyDataSetChanged();




    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}




