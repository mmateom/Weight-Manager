package com.mikel.poseidon;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by com.mikel on 15/11/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WeightDatabase";
    private static final int DATABASE_VERSION = 1;

    //Table parameters
    public static final String TABLE_NAME = "Weight_Summary";
    private static final String WEIGHT_ID = "id";
    private static final String DATE = "Date";
    private static final String WEIGHT = "Weight";

    //parameters of the table
    public static final String CREATE_TABLE = "CREATE TABLE "
            +TABLE_NAME + " ("
            +WEIGHT_ID + " integer primary key autoincrement,"
            +DATE + " TIMESTAMP DEFAULT CURRENT_DATE,"
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

    public ContentValues generateContentValues(String weight) {
        ContentValues values = new ContentValues();
        values.put(WEIGHT, weight);
        return values;
    }

    public boolean insertData(String weight){
        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues values = new ContentValues();
        values.put(WEIGHT, weight);
        long result = db.insert(TABLE_NAME,null ,values);
        if(result == -1)
            return false;
        else
            return true;

    }

}

