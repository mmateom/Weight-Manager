package com.mikel.poseidon.Activities.recordweight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mikel.poseidon.Activities.Menu;
import com.mikel.poseidon.R;

import static com.mikel.poseidon.Activities.preferences.UserProfile.dpToPx;

public class ChooseManAuto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_man_auto);

        //callback to home button
        ImageButton home_butto = (ImageButton) findViewById(R.id.homebutton);
        home_butto.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent home_intent5 = new Intent(ChooseManAuto.this, Menu.class);
                                              //home_intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                              home_intent5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                              startActivity(home_intent5);
                                          }
                                      }


        );

        // Manually
        Button getManually = (Button) findViewById(R.id.manually);

        getManually.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent stepsRecordIntent = new Intent(ChooseManAuto.this, GetWeight.class);
                                               //stepsRecordIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                               startActivity(stepsRecordIntent);
                                           }


        });

        // Automatically
        Button getAutomatically= (Button) findViewById(R.id.automatically);

        getAutomatically.setOnClickListener(view -> {
            try{
            Intent stepsRecordIntent = new Intent(ChooseManAuto.this, GetWeightFitbit.class);
            startActivity(stepsRecordIntent);
            }
            catch (Exception e){
                Log.e("Fitbit: ", "Not connected to Fitbit");
                Toast.makeText(this, "No connected to Bitbit", Toast.LENGTH_SHORT).show();
            }

        });





    }

    public void showGetAutomaticallyInfo(View view){

        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.get_auto_info, null);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_choose_man_auto);

        PopupWindow popupWindow = new PopupWindow(container, dpToPx(360), dpToPx(400), true); //true allows us to close window by tapping outside
        popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, dpToPx(0), dpToPx(120));

        //shut popup outside window
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
