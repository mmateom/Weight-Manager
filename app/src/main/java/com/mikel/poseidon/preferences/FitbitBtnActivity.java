package com.mikel.poseidon.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.mikel.poseidon.models.DBHelper;
import com.mikel.poseidon.MainActivity;
import com.mikel.poseidon.R;
import com.mikel.poseidon.recordweight.FitbitRepository;
import com.mikel.poseidon.utility.FitbitApi20;

import org.fuckboilerplate.rx_social_connect.RxSocialConnect;


public class FitbitBtnActivity extends AppCompatActivity {

    DBHelper myDB;
    double nowWeight;
    String nowDate;
    SharedPreferences mPrefs;
    int mAge;
    String mGender;
    private static final String TAG = FitbitBtnActivity.class.getName();

    public FitbitRepository repository;

        @Override protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fitbit_btn);

            //callback to home button
            ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
            home_button.setOnClickListener(view -> {

                Intent home_intent = new Intent(FitbitBtnActivity.this, MainActivity.class);
                home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home_intent);
            });

            //create db
            myDB = new DBHelper(this);

            //create Fitbit repository
            repository = new FitbitRepository();

            mPrefs= this.getSharedPreferences(SetGraphLimits.sharedPrefs, MODE_PRIVATE);
            //Get age and gender
            mAge = mPrefs.getInt("age_key",0);
            mGender = mPrefs.getString("gender_key","Male");



            setUpFitbit();

            findViewById(R.id.disconnect).setOnClickListener(view -> closeConnection(FitbitApi20.class));


        }


    public void setUpFitbit() {

        findViewById(R.id.fitbitbtn).setOnClickListener(v ->
                RxSocialConnect.with(this, repository.fitbitService())
                        .subscribe(response -> response.targetUI().showToken(response.token()),
                                error -> showError(error))

        );

       /*RxSocialConnect.getTokenOAuth2(FitbitApi20.class)
                .subscribe(token -> showToken(token),
                        error -> showError(error));


       findViewById(R.id.retrievebtn).setOnClickListener(v -> {
        Call<WeightArray>call = repository.getFitbitApi().getData();
                   call.enqueue(new Callback<WeightArray>() {
                       @Override
                       public void onResponse(Call<WeightArray> call, retrofit2.Response<WeightArray> response) {

                           List<BodyWeight> list = response.body().getBodyWeight();

                           for (BodyWeight todayWeight : list) {
                               Log.e("Weight:", todayWeight.getValue().toString());
                               Log.e("Weight:", todayWeight.getDateTime().toString());

                               Log.v("RESPONSE_CALLED", "ON_RESPONSE_CALLED");
                               String didItWork = String.valueOf(response.isSuccessful());
                               Log.v("SUCCESS?", didItWork);

                               nowWeight = Double.parseDouble(todayWeight.getValue());
                               nowDate = todayWeight.getDateTime().toString();




                               TextView profile = (TextView)findViewById(tv_profile);
                               profile.setText(String.valueOf(nowWeight));

                               //myDB.addData(String.valueOf(mAge), mGender, nowWeight, String.valueOf(nowDate));

                           }




                       }

                       @Override
                       public void onFailure(Call<WeightArray> call, Throwable t) {
                           Log.d("Error",t.getMessage());
                       }
                   });
                    //Original code from example in RxSocialConnect

                           /*.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                                   @Override
                                   public void call(Object user) {
                                       FitbitBtnActivity.this.showUserProfile(user.toString());
                                   }
                               },
                            error -> FitbitBtnActivity.this.showError(error));





                }



        );*/
    }



    private void showToken(OAuth2AccessToken oAuth2AccessToken) {
        //Toast.makeText(this, oAuth2AccessToken.getExpiresIn().toString(), Toast.LENGTH_SHORT).show();
        Log.i(TAG, oAuth2AccessToken.getExpiresIn().toString());
    }
    private void showError(Throwable error) {
        //Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        //System.out.println(error);
        Log.e(TAG, String.valueOf(error));
    }

    void closeConnection(Class<? extends BaseApi> clazz) {
        RxSocialConnect
                .closeConnection(clazz)
                .subscribe(_I -> {
                    showToast(clazz.getName() + " disconnected");

                }, error -> showError(error));
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        RxSocialConnect.getTokenOAuth2(FitbitApi20.class).subscribe(token -> showToken(token),
                error -> showError(error));
        Log.e(TAG, "on resume");
        //setUpFitbit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




    private void saveInstance(OAuth2AccessToken oAuth2AccessToken) {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();



        //Same story for timer text
        editor.putString("TOKEN", oAuth2AccessToken.getAccessToken());

        editor.commit();
    }

    private void loadInstance(){

        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        pref.getString("TOKEN", "");


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
