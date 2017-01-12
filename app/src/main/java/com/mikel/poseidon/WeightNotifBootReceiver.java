package com.mikel.poseidon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mikel on 12/01/2017.
 */

public class WeightNotifBootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent serviceLauncher = new Intent(context, WeightNotifService.class);
            context.startService(serviceLauncher);
            Log.v("TEST", "Service loaded at start");
        }
    }



}
