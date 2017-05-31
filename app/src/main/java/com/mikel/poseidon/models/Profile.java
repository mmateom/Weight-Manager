package com.mikel.poseidon.models;


import android.content.Context;
import android.content.SharedPreferences;

import static com.mikel.poseidon.preferences.SetGraphLimits.sharedPrefs;

/**
 * Created by mikel on 24/05/2017.
 */

public class Profile {

    Context mContext;
    int Age;
    int Height;
    int Level;
    int Units;
    String Gender;
    String ActLevel;
    SharedPreferences mSharedPrefs;

    public String age_key = "age_key";
    public String height_key = "height_key";
    public String gender_key = "gender_key";
    public String actLevelKey = "actLevelKey";
    public String levelint = "levelint";
    public String weightUnits = "weightUnits";
    SharedPreferences.Editor editor;



    public Profile(Context context) {
        mContext = context;
        mSharedPrefs = mContext.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE);


    }


    public int getAge() {
       Age =  mSharedPrefs.getInt(age_key, 0);
        return Age;
    }

    public void setAge(int age) {
        editor = mSharedPrefs.edit();
        editor.putInt(age_key, age);
        editor.apply();

    }

    public int getHeight() {
        Height = mSharedPrefs.getInt(height_key,0);
        return Height;
    }

    public void setHeight(int height) {
        editor = mSharedPrefs.edit();
        editor.putInt(height_key, height);
        editor.apply();

    }

    public int getLevel() {
        Level = mSharedPrefs.getInt(levelint, 0);
        return Level;
    }

    public void setLevel(int level) {
        editor = mSharedPrefs.edit();
        editor.putInt(actLevelKey, level);
        editor.apply();

    }

    public int getUnits() {
        Units = mSharedPrefs.getInt(weightUnits, 0);
        return Units;
    }

    public void setUnits(int units) {
        editor = mSharedPrefs.edit();
        editor.putInt(weightUnits, units);
        editor.apply();

    }

    public String getGender() {
        Gender = mSharedPrefs.getString(gender_key, "Male");
        return Gender;
    }

    public void setGender(String gender) {
        editor = mSharedPrefs.edit();
        editor.putString(gender_key, gender);
        editor.apply();

    }

}
