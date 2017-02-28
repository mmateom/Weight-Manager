package com.mikel.poseidon;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikel.poseidon.utility.FitbitApi20;

import org.fuckboilerplate.rx_social_connect.RxSocialConnect;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.R.string.no;
import static com.mikel.poseidon.R.id.displayWeight;
import static com.mikel.poseidon.R.id.tv_profile;

public class GetWeightFitbit extends AppCompatActivity {

    DBHelper myDB;
    double nowWeight;
    String nowDate;
    FitbitRepository repository;


    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_weight_fitbit);

        //callback to home button
        ImageButton home_button = (ImageButton) findViewById(R.id.homebutton);
        home_button.setOnClickListener(view -> {

            Intent home_intent = new Intent(GetWeightFitbit.this, MainActivity.class);
            startActivity(home_intent);
        });

        //create db
        myDB = new DBHelper(this);

        //create Fitbit repository
        repository = new FitbitRepository();

        getWeightFromFitbit();


    }



    public void getWeightFromFitbit(){
        

        findViewById(R.id.getWeightFitbit).setOnClickListener(v -> {

                    Call<WeightArray> call = repository.getFitbitApi().getData();
                    call.enqueue(new Callback<WeightArray>() {
                        @Override
                        public void onResponse(Call<WeightArray> call, retrofit2.Response<WeightArray> response) {

                            List<BodyWeight> list = response.body().getBodyWeight();

                            for (BodyWeight todayWeight : list) {

                                //Log messages
                                Log.e("Weight:", todayWeight.getValue().toString());
                                Log.e("Weight:", todayWeight.getDateTime().toString());
                                Log.v("RESPONSE_CALLED", "ON_RESPONSE_CALLED");
                                String didItWork = String.valueOf(response.isSuccessful());
                                Log.v("SUCCESS?", didItWork);

                                nowWeight = Double.parseDouble(todayWeight.getValue());
                                nowDate = todayWeight.getDateTime().toString();

                                //Display weight
                                TextView profile = (TextView)findViewById(displayWeight);
                                profile.setText(String.valueOf(nowWeight));


                                //Add weight to DB
                                myDB.addData(nowWeight, String.valueOf(nowDate));


                                // Find last weight entered to compare to the new one
                                double theLastWeight = getLastWeight();
                                double weightDiference = nowWeight - theLastWeight;


                                //Display feedback
                                weightFeedback(weightDiference);


                            }


                        }

                        @Override
                        public void onFailure(Call<WeightArray> call, Throwable t) {
                            Log.d("Error",t.getMessage());
                        }
                    });



                }

        );



    }

    public double getLastWeight() {

        Cursor alldata;
        ArrayList<Double> yVals;
        alldata= myDB.getListContents();

        int count = alldata.getCount();
        double[] weights = new double[count];

        yVals = new ArrayList<Double>();

        //get weight from the database and populate array
        for (int m = 0; m < count; m++) {
            alldata.moveToNext();
            weights[m] = alldata.getDouble(2);

            yVals.add(weights[m]);


        }

        double lastWeight = yVals.get(yVals.size() - 1);

        return lastWeight;

    }


    //===============================================
    //                   Give feedback
    //===============================================
    public void weightFeedback(double substraction){

        if (substraction > -1 && substraction <1){

            //more or less the same pop up window
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup, null);
            relativeLayout = (RelativeLayout) findViewById(R.id.activity_get_weight_fitbit);

            popupWindow = new PopupWindow(container, 500, 500, true); //true allows us to close window by tapping outside
            popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 110, 306);

            //shut popup outside window
            container.setOnTouchListener((view, motionEvent) -> {
                popupWindow.dismiss();
                return false;
            });

            //System.out.println("more or less the same pop up window");

        }else if (substraction >= 1 && substraction < 2 ){

            //increased between 1 and 2Kg
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup_1to2, null);
            relativeLayout = (RelativeLayout) findViewById(R.id.activity_get_weight_fitbit);

            popupWindow = new PopupWindow(container, 500, 500, true); //true allows us to close window by tapping outside
            popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 110, 306);

            //shut popup outside window
            container.setOnTouchListener((view, motionEvent) -> {
                popupWindow.dismiss();
                return false;
            });
            //System.out.println("increased between 1 and 2Kg");

        }else if (substraction >= 2){

            //increased in more than 2Kg
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup_2plus, null);
            relativeLayout = (RelativeLayout) findViewById(R.id.activity_get_weight_fitbit);

            popupWindow = new PopupWindow(container, 500, 500, true); //true allows us to close window by tapping outside
            popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 110, 306);

            //shut popup outside window
            container.setOnTouchListener((view, motionEvent) -> {
                popupWindow.dismiss();
                return false;
            });
            //System.out.println("increased in more than 2Kg");

        }else if (substraction < -1){

            //reduced in more than 1Kg -- Good
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup_good, null);
            relativeLayout = (RelativeLayout) findViewById(R.id.activity_get_weight_fitbit);

            popupWindow = new PopupWindow(container, 500, 500, true); //true allows us to close window by tapping outside
            popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 110, 306);

            //shut popup outside window
            container.setOnTouchListener((view, motionEvent) -> {
                popupWindow.dismiss();
                return false;
            });
            //System.out.println("reduced in more than 1Kg");

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        myDB.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
