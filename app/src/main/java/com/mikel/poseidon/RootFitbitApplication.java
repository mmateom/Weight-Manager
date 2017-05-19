package com.mikel.poseidon;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


import org.fuckboilerplate.rx_social_connect.RxSocialConnect;

import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Created by mikel on 19/01/2017.
 */


public class RootFitbitApplication extends MultiDexApplication {


    @Override public void onCreate() {
        super.onCreate();

        RxSocialConnect.register(this, "holacaracola")
                .using(new GsonSpeaker());



    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}

