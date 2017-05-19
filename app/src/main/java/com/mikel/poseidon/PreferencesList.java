package com.mikel.poseidon;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;



/**
 * Created by mikel on 12/01/2017.
 */

public class PreferencesList extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.toolbar);
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle(null);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        /*SharedPreferences mSettings = this.getSharedPreferences(STRESS_PREFS, 0);

        String device = mSettings.getString("macaddress", "");

        if (device.isEmpty()) {
            mIsPaired = false;
        }*/
        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the preferences button is clicked on.
            @Override
            public void onClick(View view) {

                Intent home_intent100 = new Intent(PreferencesList.this, MainActivity.class);
                home_intent100.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home_intent100);
            }
        });

    }

    public static class MyPreferenceFragment extends PreferenceFragment{

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.act_preferences_list);



        }



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



}

