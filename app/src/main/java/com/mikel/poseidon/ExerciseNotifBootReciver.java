package com.mikel.poseidon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by mikel on 20/03/2017.
 */

public class ExerciseNotifBootReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent serviceIntent = new Intent("com.mikel.poseidon.utility.ExerciseNotifService");
            context.startService(serviceIntent);
        }
    }
}

