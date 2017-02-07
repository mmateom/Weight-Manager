package com.mikel.poseidon;

import android.app.Application;
import android.widget.Toast;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.mikel.poseidon.utility.FitbitApi20;

import org.fuckboilerplate.rx_social_connect.RxSocialConnect;

import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Created by mikel on 19/01/2017.
 */

public class RootFitbitApplication extends Application {


    @Override public void onCreate() {
        super.onCreate();

        RxSocialConnect.register(this, "holacaracola")
                .using(new GsonSpeaker());
        

    }


}

