package com.mikel.poseidon;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;

import java.util.Date;
import java.text.SimpleDateFormat;

import static android.R.attr.data;
import static android.R.attr.value;
import static android.icu.text.MessagePattern.ArgType.SELECT;
import static android.icu.text.RelativeDateTimeFormatter.AbsoluteUnit.NOW;
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
    //     Create tables
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
    //public static final String ACT_STOP = "Act_Stop";
    public static final String STEPS = "Steps";
    public static final String CALORIES = "Calories";
    public static final String EXERCISE_TIME = "Exercise_Time";
    public static final String ACTIVITY = "Activity";

    //parameters of the STEPS table
    public static final String CREATE_TABLE_STEPS = "CREATE TABLE "
            +TABLE_NAME_STEPS + " ("
            +STEPS_ID + " integer primary key autoincrement,"
            +ACT_START + " date,"
            +EXERCISE_TIME + " TEXT,"
            +CALORIES + " REAL,"
            //+ACT_STOP + " INT,"
            +STEPS + " REAL);";





    //Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_STEPS);
        insertExampleData(db); /** inserts example data. Comment this method in the future */


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

    public boolean addDataSteps(String exercise_time, double calories, long steps){
        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues values = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        values.put(ACT_START, date);
        //values.put(ACT_STOP, act_stop);
        values.put(STEPS, steps);
        values.put(CALORIES, calories);
        values.put(EXERCISE_TIME, exercise_time);
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

    //today steps textview
    public Cursor getTodaySumSteps(){ //previous: getStepsSumByDate
        SQLiteDatabase db = this.getWritableDatabase();

        //String query = "SELECT Act_Start, SUM(Steps) FROM Steps_Summary GROUP BY Act_Start";
        //Previous query only sums and groups by date to then go to the latest entry.
        //With the new query it automatically gets today's entry
        String query = "SELECT Act_Start, SUM(Steps) FROM Steps_Summary WHERE DATE(Act_Start) = DATE('now')";

        Cursor dates = db.rawQuery(query, null);

        return dates;
    }


    //for listview
    public Cursor getStepsByDate(){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT _id, Act_Start, SUM(Steps) as Steps FROM Steps_Summary GROUP BY Act_Start";

        Cursor dates = db.rawQuery(query, null);

        return dates;
    }

    public Cursor getCalories(){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT _id, Act_Start, SUM(Calories) FROM Steps_Summary WHERE DATE(Act_Start) = DATE('now')";

        Cursor dates = db.rawQuery(query, null);

        return dates;
    }

    public Cursor getExerciseTime(){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT _id, Act_Start, SUM(Exercise_Time) FROM Steps_Summary WHERE DATE(Act_Start) = DATE('now')";

        Cursor dates = db.rawQuery(query, null);

        return dates;
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


    private void insertExampleData(SQLiteDatabase db){


        db.execSQL("insert into Weight_Summary values (1, '2016-12-05' , 89)");
        db.execSQL("insert into Weight_Summary values (2, '2016-12-12' , 90)");
        db.execSQL("insert into Weight_Summary values (3, '2016-12-19' , 91)");
        db.execSQL("insert into Weight_Summary values (4, '2016-12-26' , 91.5)");
        db.execSQL("insert into Weight_Summary values (5, '2017-01-02' , 92)");
        db.execSQL("insert into Weight_Summary values (6, '2017-01-09' , 91.3)");
        db.execSQL("insert into Weight_Summary values (7, '2017-01-16' , 90.8)");
        /*db.execSQL("insert into Weight_Summary values (8, '19-01-17' , 90)");
        db.execSQL("insert into Weight_Summary values (9, '26-01-17' , 89.7)");
        db.execSQL("insert into Weight_Summary values (10, '02-01-17' , 89.2)");*/
        db.execSQL("insert into Steps_Summary values (1, '2016-12-19' , '13:13:13', 230, 7823)");
        db.execSQL("insert into Steps_Summary values (2, '2016-12-20' ,'13:13:13', 230, 6384)");
        db.execSQL("insert into Steps_Summary values (3, '2016-12-21' ,'13:13:13', 230, 8387)");
        db.execSQL("insert into Steps_Summary values (4, '2016-12-22' ,'13:13:13', 230, 9387)");

    }
}

