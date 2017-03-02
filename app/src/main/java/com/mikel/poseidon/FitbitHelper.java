package com.mikel.poseidon;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.SignatureType;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.mikel.poseidon.utility.FitbitApi20;

import org.fuckboilerplate.rx_social_connect.RxSocialConnect;

import java.io.IOException;
import java.security.AccessController;

import static com.github.scribejava.core.model.Verb.GET;
import static java.security.AccessController.getContext;
import static org.fuckboilerplate.rx_social_connect.internal.ActivityConnect.service;

/**
 * Created by mikel on 19/01/2017.
 */

public class FitbitHelper {

    private final Object targetUI;

    public FitbitHelper(Object targetUI) {
        this.targetUI = targetUI;
    }


    OAuth20Service fitbitService() {

        final String client_id = "2283NP";
        final String client_secret = "140376d2db1c29f2e8e0bb4bda2d0714";
        final String PROTECTED_RESOURCE_URL = "https://api.fitbit.com/1/user/[user-id]/body/log/weight/date/[date].json";
        final String ACCEPTED_LANGUAGE_AND_UNITS = "es_ES"; //spain
        final String redirect_uri = "http://example.com";

        return new ServiceBuilder()
                .apiKey(client_id)
                .apiSecret(client_secret)
                .scope("profile") // replace with desired scope
                .callback(redirect_uri)//your callback URL to store and handle the authorization code sent by Fitbit
                .build(FitbitApi20.instance());


    }



    //==============================
    //            METHODS
    //==============================
    void showResponse(OAuth2AccessToken token) {
        //clearEditTexts();

        //((TextView)findViewById(R.id.tv_token)).setText(token.getToken());
        //System.out.println(token.getAccessToken());
        System.out.println(token.getAccessToken());


    }



   /* private void clearEditTexts() {
        ((TextView)findViewById(R.id.show_fitbit_weight)).setText("");
    }*/



    void closeConnection(Class<? extends BaseApi> clazz) {
        RxSocialConnect
                .closeConnection(clazz)
                .subscribe(_I -> {
                    showToast(clazz.getName() + " disconnected");

                }, error -> showError(error));
    }
    void showError(Throwable throwable) {
        showToast(throwable.getMessage());
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
    private Context getContext() {
        if (targetUI instanceof Activity) return  ((Activity) targetUI);
        else return((Fragment) targetUI).getActivity();
    }

}