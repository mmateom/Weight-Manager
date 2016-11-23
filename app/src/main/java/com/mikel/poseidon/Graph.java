package com.mikel.poseidon;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static com.mikel.poseidon.DBHelper.DATE;
import static com.mikel.poseidon.DBHelper.TABLE_NAME;
import static com.mikel.poseidon.DBHelper.WEIGHT;
import static com.mikel.poseidon.R.id.listView;

public class Graph extends AppCompatActivity {

    DBHelper myDB;
    Cursor alldata;

    private LineChart mChart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        myDB = new DBHelper(this);

        alldata = myDB.getListContents();

        int count = alldata.getCount();

        double[] weights = new double[count];
        String[] dates = new String[count];

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        //get dates and weight from the database and populate arrays
        for (int m = 0; m < count; m++) {
            alldata.moveToNext();
            dates[m] = alldata.getString(1);
            weights[m] = alldata.getDouble(2);

            yVals.add(new Entry((float) weights[m], m));
            xVals.add(dates[m]);




        }

        //System.out.println(xVals);
        //System.out.println(yVals);
        //System.out.println(weights.length);

        //mChart = (LineChart) findViewById(R.id.linechart);

    }



}

