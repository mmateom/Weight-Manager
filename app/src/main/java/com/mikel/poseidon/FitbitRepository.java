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

    String currentDate = getCurrentTime();

    interface FitbitApiRest {
        String URL_BASE = "https://api.fitbit.com";



        @GET("1/user/-/body/weight/date/today/1d.json")
        Call<WeightArray> getData();
    }

    OAuth20Service fitbitService() {


        final String client_id = "2283NP";
        final String client_secret = "140376d2db1c29f2e8e0bb4bda2d0714";
        final String redirect_uri = "http://example.com";
        final String permissions = "weight";

        return new ServiceBuilder()
                .apiKey(client_id)
                .apiSecret(client_secret)
                .callback(redirect_uri)
                .scope(permissions)
                .build(FitbitApi20.instance());
    }



    //=========================================
    //             GET CURRENT TIME
    //=========================================
   public String getCurrentTime() {

        Calendar cal = Calendar.getInstance();

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) +1; //plus 1, because months star in 0 in java
        int year = cal.get(Calendar.YEAR);

        String date_final = null;


        String current_start_time = year + "-" + month + "-" + day;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {

            Date date_f = formatter.parse(current_start_time);
            date_final = formatter.format(date_f);



            System.out.println(date_f);
            System.out.println(date_final);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date_final;
   }

}


