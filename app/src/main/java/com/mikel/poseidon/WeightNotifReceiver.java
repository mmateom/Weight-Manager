package com.mikel.poseidon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mikel on 06/02/2017.
 */

public class WeightNotifReceiver extends BroadcastReceiver {

    private final static String LOG_TAG = WeightNotifReceiver.class.getName();

    // Must return quickly so just start a Service
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "AlarmReceiver invoked.");
        context.startService(new Intent(context, WeightNotifService.class));
    }


}
