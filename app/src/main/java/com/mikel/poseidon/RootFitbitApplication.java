package com.mikel.poseidon;

import android.app.Application;

import org.fuckboilerplate.rx_social_connect.RxSocialConnect;

import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Created by mikel on 19/01/2017.
 */

public class RootFitbitApplication extends Application {


    @Override public void onCreate() {
        super.onCreate();

        RxSocialConnect.register(this, "myEncryptionKey")
                .using(new GsonSpeaker());
    }
}

