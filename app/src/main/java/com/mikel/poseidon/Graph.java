package com.mikel.poseidon;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.max;
import static android.R.attr.x;
import static android.R.attr.y;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class Graph extends AppCompatActivity {

    DBHelper myDB;
    Cursor alldata;

    private LineChart chart;
    ArrayList<String> xVals;
    ArrayList<Entry> yVals;
    Date[] date_d;
    double min;
    double max;
    //java.sql.Timestamp[] ts;
    //java.sql.Date[] date_sql;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {

                Intent home_intent = new Intent(Graph.this, MainActivity.class);
                startActivity(home_intent);
            }
        });

        //full screen mode
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                //WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //instance db
        myDB = new DBHelper(this);

        //get all data from db
        alldata = myDB.getListContents();


        int count = alldata.getCount();

        double[] weights = new double[count];
        String[] dates = new String[count];
        date_d = new Date[count];
        //date_sql = new java.sql.Date[count];

        xVals = new ArrayList<>();
        yVals = new ArrayList<Entry>();


        //get dates and weight from the database and populate arrays
        for (int m = 0; m < count; m++) {
            alldata.moveToNext();
            dates[m] = alldata.getString(1);
            weights[m] = alldata.getDouble(2);

            yVals.add(new Entry(m, (float) weights[m]));
            xVals.add(dates[m]);





        }

        //Check yVals with weight values is empty

        if (yVals.size() == 0) {

            Toast.makeText(this, "No data available, please enter some data", Toast.LENGTH_LONG).show();
            this.finish();


        //should fix this somehow -- how to show only one point??

        }else if(yVals.size() == 1){
            Toast.makeText(this, "Cannot graph one point", Toast.LENGTH_LONG).show();
            this.finish();


        } else {
            //get min & max value to assign correct y axis value
            min = weights[0];
            max = weights[0];


            for (int i = 0; i < weights.length; i++) {
                if (weights[i] < min) {
                    min = weights[i];
                }

                if (weights[i] > max) {
                    max = weights[i];
                }
            }


            //create chart
            chart = (LineChart) findViewById(R.id.linechart);
            LineDataSet dataSet = new LineDataSet(yVals, "Kg");

            LineData lineData = new LineData(dataSet);

            //set x axis: year-month-day
            XAxis xAxis = chart.getXAxis(); //create X axis instance
            xAxis.setValueFormatter(new MyXAxisValueFormatter(xVals));

            ////////////chart styling///////

            //create Y axis instances
            YAxis leftAxis = chart.getAxisLeft();
            YAxis rightAxis = chart.getAxisRight();

            //set to 0 to only show bars according to each day
            xAxis.setLabelCount(0);
            xAxis.setGranularity(0f);
            dataSet.setValueTextSize(10f);

            //show x axis on the bottom of the chart
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            //assign max and min value of y axis
            leftAxis.setAxisMaximum((float) max + 5f);//left
            leftAxis.setAxisMinimum((float) min - 5f);
            rightAxis.setAxisMaximum((float) max + 5f);//right
            rightAxis.setAxisMinimum((float) min - 5f);

            //set text size of y axis
            float y_text_size = 15;
            leftAxis.setTextSize(y_text_size);
            rightAxis.setTextSize(y_text_size);

            //set text size of x axis
            float x_text_size = 15;
            xAxis.setTextSize(x_text_size);


            ////////////set chart data/////
            chart.setData(lineData);
            chart.invalidate();
        }

    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        public ArrayList mValues;

        public MyXAxisValueFormatter(ArrayList xVals) {
            this.mValues = xVals;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return (String) mValues.get((int) value);
        }

    }



}

