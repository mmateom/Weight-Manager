package com.mikel.poseidon;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import static com.mikel.poseidon.R.layout.toolbar;


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

    }





}

