package com.mikel.poseidon;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import static android.os.Build.VERSION_CODES.M;


/**
 * Created by com.mikel on 15/11/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WeightDatabase";
    private static final int DATABASE_VERSION = 1;

    Intent intent;
    String date;

    //Table parameters
    public static final String TABLE_NAME = "Weight_Summary";
    private static final String WEIGHT_ID = "id";
    private static final String DATE = "Date";
    private static final String WEIGHT = "Weight";

    //parameters of the table
    public static final String CREATE_TABLE = "CREATE TABLE "
            +TABLE_NAME + " ("
            +WEIGHT_ID + " integer primary key autoincrement,"
            +DATE + " TEXT,"
            +WEIGHT + " REAL);";




    //Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBHelper.CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" +TABLE_NAME);
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

    public boolean addData(String weight, String date_i){
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

