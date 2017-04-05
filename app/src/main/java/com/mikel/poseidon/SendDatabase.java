package com.mikel.poseidon;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.mikel.poseidon.DBHelper.TABLE_NAME_STEPS;

public class SendDatabase extends AppCompatActivity {

    CSVWriter csvWrite, csvWrite2;

    DBHelper myDB;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_database);

        myDB = new DBHelper(this);
        //verifyStoragePermissions(this);

        Button send = (Button) findViewById(R.id.send_data);
        send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        exportDB();
                                    }
                                }

        );


    }


    private void exportDB() {

        File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "Activity.csv");
        try {
            file.createNewFile();
            csvWrite = new CSVWriter(new FileWriter(file));
            Cursor curCSV = myDB.getListContentsSteps();
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2),
                        curCSV.getString(3), curCSV.getString(4)};
                csvWrite.writeNext(arrStr);
                System.out.println(csvWrite);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }

        File file2 = new File(exportDir, "Weight.csv");
        try {
            file2.createNewFile();
            csvWrite2 = new CSVWriter(new FileWriter(file2));
            Cursor curCSV2 = myDB.getListContents();
            csvWrite2.writeNext(curCSV2.getColumnNames());
            while (curCSV2.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV2.getString(0), curCSV2.getString(1), curCSV2.getString(2),
                        curCSV2.getString(3), curCSV2.getString(4)};
                csvWrite2.writeNext(arrStr);
                System.out.println(csvWrite2);
            }
            csvWrite2.close();
            curCSV2.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }

        //
        Uri u1;
        u1 = Uri.fromFile(file);
        Uri u2;
        u2 = Uri.fromFile(file2);

        ArrayList<Uri> uris = new ArrayList<Uri>();
        uris.add(u1);
        uris.add(u2);


        String recepientEmail = ""; // either set to destination email or leave empty
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setData(Uri.parse("mailto:" + recepientEmail));
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"mikelmateo@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Weight and activity data");
        intent.putExtra(Intent.EXTRA_TEXT, "Here I send my weight and activity data.");
        intent.putExtra(Intent.EXTRA_STREAM, uris );//Uri.parse("file://" + file.getAbsolutePath())
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /*// Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };*/

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    /*public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }*/
}
