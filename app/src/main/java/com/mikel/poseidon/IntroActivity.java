package com.mikel.poseidon;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by mikel on 23/03/2017.
 */

public class IntroActivity extends AppIntro {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

       /* String title = "Weight Manager";
        String description = "description";
        Image image*/

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.

        addSlide(SampleSlide.newInstance(R.layout.slide_1));
        addSlide(SampleSlide.newInstance(R.layout.slide_2));
        addSlide(SampleSlide.newInstance(R.layout.slide_3));
        addSlide(SampleSlide.newInstance(R.layout.slide_4));
        addSlide(SampleSlide.newInstance(R.layout.slide_permision));
        addSlide(SampleSlide.newInstance(R.layout.slide_5));

        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
        //askForPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#5e7974"));
        setSeparatorColor(Color.parseColor("#5e7974"));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);

        setFadeAnimation();

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        //setVibrate(true);
        //setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        this.finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        this.finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}

