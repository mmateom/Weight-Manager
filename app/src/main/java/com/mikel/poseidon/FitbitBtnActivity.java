package com.mikel.poseidon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.Gson;
import com.mikel.poseidon.utility.FitbitApi20;

import org.fuckboilerplate.rx_social_connect.RxSocialConnect;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;
import static com.github.scribejava.core.model.Verb.GET;
import static com.mikel.poseidon.R.id.fitbitbtn;
import static com.mikel.poseidon.R.id.ok_button;
import static org.fuckboilerplate.rx_social_connect.internal.ActivityConnect.service;

public class FitbitBtnActivity extends AppCompatActivity {
    WeightLogFitbit w = new WeightLogFitbit();

    private FitbitRepository repository;

        @Override protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fitbit_btn);

            //callback to home button
            ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
            home_button.setOnClickListener(view -> {

                Intent home_intent = new Intent(FitbitBtnActivity.this, MainActivity.class);
                startActivity(home_intent);
            });


            repository = new FitbitRepository();

            setUpFitbit();

        }


    private void setUpFitbit() {
        findViewById(R.id.fitbitbtn).setOnClickListener(v ->
                RxSocialConnect.with(this, repository.fitbitService())
                        .subscribe(response -> response.targetUI().showToken(response.token()),
                                error -> showError(error))
        );

        findViewById(R.id.retrievebtn).setOnClickListener(v ->
                repository.getFitbitApi().getData()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> showUserProfile(user.toString()),
                                error -> showError(error))
        );
    }

    private void showToken(OAuth2AccessToken oAuth2AccessToken) {
        Toast.makeText(this, oAuth2AccessToken.getAccessToken(), Toast.LENGTH_SHORT).show();
    }
    private void showError(Throwable error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        System.out.println(error);
    }

    private void showUserProfile(String userProfileJSON) {

        //TODO: EXTRAER SOLAMENTE EL WEIGHT. TIENES TRES PÁGINAS ABIERTAS SOBRE JSON PARSING CON RETROFIT


        TextView tv_profile = (TextView) findViewById(R.id.tv_profile);
        tv_profile.setText(userProfileJSON);

        System.out.println(userProfileJSON);

        /////////////
        Gson gson = new Gson();

      /*  MyStatus status = gson.fromJson(userProfileJSON, MyStatus.class);
        Log.i(LOG_TAG, String.valueOf(status.weight));*/

    }
    /*class MyStatus{
        double weight;
    }*/


    /*private void setUpFitbit() throws IOException {

        RxSocialConnect.with(FitbitBtnActivity.this, helper.fitbitService())
                .subscribe(response -> response.targetUI().helper.showResponse(response.token()),
                        error -> helper.showError(error));

        RxSocialConnect.getTokenOAuth2(FacebookApi.class)
                .subscribe(token -> helper.showResponse(token),
                        error -> helper.showError(error));


    }*/



}
