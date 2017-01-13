package com.mikel.poseidon;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by mikel on 12/01/2017.
 */

public class MyPreferenceFragment extends PreferenceFragment{

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.act_preferences_list);
        }

}
