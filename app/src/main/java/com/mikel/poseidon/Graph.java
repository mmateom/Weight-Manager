package com.mikel.poseidon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.max;
import static android.R.attr.packageNames;
import static android.R.attr.value;
import static android.R.attr.x;
import static android.R.attr.y;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.mikel.poseidon.DBHelper.WEIGHT;
import static com.mikel.poseidon.Preferences.sharedPrefs;
import static java.lang.reflect.Array.getFloat;


public class Graph extends AppCompatActivity {

    DBHelper myDB;
    Cursor alldata_a;

    private LineChart chart;
    ArrayList<String> xVals;
    ArrayList<Entry> yVals;
    Date[] date_d;
    double min;
    double max;
    XAxis xAxis;
    YAxis yAxis;

    float min_limit_risk, max_limit_risk;
    float min_limit_becareful, max_limit_becareful;
    float min_limit_good, max_limit_good;

    LineData lineData;
    LineDataSet dataSet;

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
        alldata_a= myDB.getListContents();

        //generate graph



        createGraph(alldata_a);



        }

    private void createGraph(Cursor alldata) {

        int count = alldata.getCount();
        double[] weights = new double[count];
        String[] dates = new String[count];
        date_d = new Date[count];
        //date_sql = new java.sql.Date[count];

        //initialise data containing arrays
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

        //Check yVals (weight values) is empty

        if (yVals.size() == 0) {

            Toast.makeText(this, "No data available, please enter some data", Toast.LENGTH_LONG).show();
            this.finish(); //in case is empty, shut down activity to prevent shutting down whole app


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
            dataSet = new LineDataSet(yVals, "Kg");

            lineData = new LineData(dataSet);

            //set x axis: year-month-day
            xAxis = chart.getXAxis(); //create X axis instance
            xAxis.setValueFormatter(new MyXAxisValueFormatter(xVals));
            chartStyle();


            setLimitLines();

            setGraphData(lineData);
        }
    }

    SharedPreferences mPrefs;
    private void setLimitLines() {

        //===================================================================
        //  Set limit lines
        //===================================================================



        mPrefs= this.getSharedPreferences(sharedPrefs, MODE_PRIVATE);

        min_limit_risk= mPrefs.getFloat("min_risk", (float) 92.1);
        max_limit_risk = mPrefs.getFloat("max_risk", 97);

        min_limit_becareful= mPrefs.getFloat("min_becareful", (float) 88.1);
        max_limit_becareful = mPrefs.getFloat("max_becareful", (float) 92);

        min_limit_good= mPrefs.getFloat("min_good", 84);
        max_limit_good = mPrefs.getFloat("max_good", 88);

        //risk

        //getResources().getColor(R.color.LightRed, null);

        //where is the limit line
        LimitLine z0 = new LimitLine(min_limit_risk);
        z0.setLineColor(getResources().getColor(R.color.LightRed, null));
        z0.setLineWidth(4f);
        yAxis.addLimitLine(z0);

        LimitLine z1 = new LimitLine(max_limit_risk);//make this editable by user(+0.5)
        z1.setLineColor(getResources().getColor(R.color.LightRed, null));
        z1.setLineWidth(2f);
        yAxis.addLimitLine(z1);



        LimitLine z2 = new LimitLine(min_limit_becareful);//make this editable by user(-0.5)
        z2.setLineColor(getResources().getColor(R.color.BeCareful, null));
        z2.setLineWidth(4f);
        yAxis.addLimitLine(z2);

        LimitLine z3 = new LimitLine(max_limit_becareful);//make this editable by user(-0.75)
        z3.setLineColor(getResources().getColor(R.color.BeCareful, null));
        z3.setLineWidth(2f);
        yAxis.addLimitLine(z3);



        LimitLine z4 = new LimitLine(min_limit_good);//make this editable by user(+0.75)
        z4.setLineColor(getResources().getColor(R.color.Good, null));
        z4.setLineWidth(4f);
        yAxis.addLimitLine(z4);

        LimitLine z5 = new LimitLine(max_limit_good);//make this editable by user(-0.75)
        z5.setLineColor(getResources().getColor(R.color.Good, null));
        z5.setLineWidth(2f);
        yAxis.addLimitLine(z5);




    }



    private void setGraphData(LineData lineData_1) {
        //===================================================================
        // Set data
        //===================================================================
        chart.setData(lineData_1);
        chart.invalidate();
    }

    private void chartStyle() {
        //===================================================================
        //  Chart styling
        //===================================================================

        //create Y axis instance
        yAxis = chart.getAxisLeft();

        //hide right y axis
        chart.getAxisRight().setDrawLabels(false);

        //set to 0 to only show bars according to each day
        xAxis.setLabelCount(0);
        xAxis.setGranularity(0f);
        dataSet.setValueTextSize(10f);

        //set line + circle color & width
        dataSet.setColor(Color.BLUE);
        dataSet.setLineWidth(6f);
        dataSet.setCircleColor(Color.BLUE);

        //show x axis on the bottom of the chart
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //assign max and min value of y axis
        yAxis.setAxisMaximum((float) max + 5f);//left
        yAxis.setAxisMinimum((float) min - 5f);
        yAxis.setLabelCount(12);

        //set text size of y axis
        float y_text_size = 15;
        yAxis.setTextSize(y_text_size);

        //set text size of x axis
        float x_text_size = 15;
        xAxis.setTextSize(x_text_size);

        //disable scrolling on Y axis, only scrollable con X axis
        chart.setScaleYEnabled(false);

        //enable limitline to be on the background on the chart line
        yAxis.setDrawLimitLinesBehindData(true);


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


    @Override
    public void onResume(){
        super.onResume();

       // chart.setData(lineData);




    }





}





