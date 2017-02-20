package com.mikel.poseidon;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.mikel.poseidon.utility.FitbitApi20;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.victoralbertos.rx_social_connect.OAuth2Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by mikel on 20/01/2017.
 */

public class FitbitRepository {


    private final FitbitApiRest fitbitApi;

    public FitbitRepository() {
        fitbitApi = initFitbitApiRest();
    }

    private FitbitApiRest initFitbitApiRest() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new OAuth2Interceptor(FitbitApi20.class))
                .build();

        return new Retrofit.Builder()
                .baseUrl(FitbitApiRest.URL_BASE)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(FitbitApiRest.class);
    }



    FitbitApiRest getFitbitApi() {
        return fitbitApi;
    }


    interface FitbitApiRest {
        String URL_BASE = "https://api.fitbit.com";

        @GET("1/user/-/body/weight/date/today/1d.json")
        Call<WeightArray> getData();
    }

    OAuth20Service fitbitService() {


        final String client_id = "2283NP";
        final String client_secret = "1d730957a1af3011d339e00db5dfe929";
        final String redirect_uri = "http://example.com";
        final String permissions = "weight";

        return new ServiceBuilder()
                .apiKey(client_id)
                .apiSecret(client_secret)
                .callback(redirect_uri)
                .scope(permissions)
                .build(FitbitApi20.instance());
    }





}


