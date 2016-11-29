package com.mikel.poseidon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Map;

import uk.ac.mdx.cs.ie.acontextlib.IContextReceiver;
import uk.ac.mdx.cs.ie.acontextlib.hardware.StepCounter;

import static com.mikel.poseidon.R.id.textView;

public class Steps extends AppCompatActivity {

    StepCounter steps;
    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        final String HOLA = "hola";

        //Log.e("LightContext", HOLA);

        steps = new StepCounter(getApplicationContext());

        steps.addContextReceiver(new IContextReceiver() {
            @Override
            public void newContextValue(String name, long value) {

                String strValue = String.valueOf(steps);
                int step_value = Integer.parseInt(strValue);
                view.setText(step_value);

                if (strValue == null){
                    Log.e("LightContext", HOLA);
                }



            }

            @Override
            public void newContextValue(String name, double value) {

            }

            @Override
            public void newContextValue(String name, boolean value) {

            }

            @Override
            public void newContextValue(String name, String value) {

            }

            @Override
            public void newContextValue(String name, Object value) {

            }

            @Override
            public void newContextValues(Map<String, String> values) {

            }
        });


    }
}
