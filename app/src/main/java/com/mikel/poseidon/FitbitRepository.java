package com.mikel.poseidon;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.mikel.poseidon.utility.FitbitApi20;

import java.util.List;

import io.victoralbertos.rx_social_connect.OAuth2Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.Result;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

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

        @GET("1/user/-/body/log/weight/date/2016-12-31/1m.json")
        Observable<Object>getData();
    }

    OAuth20Service fitbitService() {


        final String client_id = "2283NP";
        final String client_secret = "140376d2db1c29f2e8e0bb4bda2d0714";
        final String PROTECTED_RESOURCE_URL = "https://api.fitbit.com/1/user/2283NP/body/log/weight/date/2016-01-01/1m.json";
        final String ACCEPTED_LANGUAGE_AND_UNITS = "es_ES"; //spain
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


