package com.mikel.poseidon;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;

import static java.awt.font.TextAttribute.WEIGHT;


/**
 * Created by com.mikel on 15/11/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "WeightDatabase_2";
    public static final int DATABASE_VERSION = 1;

    Intent intent;
    String date;


    //==========================
    //Create tables
    //==========================


    //WEIGHT table parameters
    public static final String TABLE_NAME = "Weight_Summary";
    public static final String WEIGHT_ID = "_id";
    public static final String DATE = "Date";
    public static final String WEIGHT = "Weight";

    //parameters of the WEIGHT table
    public static final String CREATE_TABLE = "CREATE TABLE "
            +TABLE_NAME + " ("
            +WEIGHT_ID + " integer primary key autoincrement,"
            +DATE + " TEXT,"
            +WEIGHT + " REAL);";



    //STEPS parameters
    public static final String TABLE_NAME_STEPS = "Steps_Summary";
    public static final String STEPS_ID = "_id";
    public static final String ACT_START = "Act_Start";
    public static final String ACT_STOP = "Act_Stop";
    public static final String STEPS = "Steps";

    //parameters of the STEPS table
    public static final String CREATE_TABLE_STEPS = "CREATE TABLE "
            +TABLE_NAME + " ("
            +STEPS_ID + " integer primary key autoincrement,"
            +ACT_START + " INT,"
            +ACT_STOP + " INT,"
            +STEPS + " INT);";





    //Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
//        db.execSQL(CREATE_TABLE_STEPS);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME_STEPS);
        onCreate(db);
    }

   /* public ContentValues generateContentValues(String weight) {
        ContentValues values = new ContentValues();
        values.put(WEIGHT, weight);
        return values;
    }*/

    /*public String getDate(){

        date = getIntent().getExtras().getString("Date");
        return date;


    }*/

    //==========================
    //METHODS FOR STEPS TABLE
    //==========================

    public boolean addDataSteps(int act_start, int act_stop, int steps){
        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues values = new ContentValues();


        values.put(ACT_START, act_start);
        values.put(ACT_STOP, act_stop);
        values.put(STEPS, steps);
        long result = db.insert(TABLE_NAME_STEPS,null ,values);
        if(result == -1)
            return false;
        else
            return true;

    }

    public void deleteDataSteps(String TABLE_NAME_STEPS){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME_STEPS);
    }


    //method to get contents from the database
    public Cursor getListContentsSteps(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_STEPS, null);
        return data;
    }

    //==========================
    //METHODS FOR WEIGHT TABLE
    //==========================

    public boolean addData(double weight, String date_i){
        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues values = new ContentValues();

        //date_i = getDate();

        values.put(WEIGHT, weight);
        values.put(DATE, date_i);
        long result = db.insert(TABLE_NAME,null ,values);
        if(result == -1)
            return false;
        else
            return true;

    }

    public void deleteData (String TABLE_NAME){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }



    //method to get contents from the database
    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }



}

