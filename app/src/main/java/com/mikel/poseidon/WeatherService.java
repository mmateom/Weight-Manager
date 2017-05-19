package com.mikel.poseidon;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.Calendar;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import java.util.Arrays;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;

/**
 * Created by mikel on 12/05/2017.
 */

public class WeatherService extends Service {


    private static final String TAG = WeatherService.class.getName();

    int[] conditions;
    Context context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "started");
        context = getApplicationContext();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return START_STICKY;
        }

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        Boolean weathenNotifOn = preferences.getBoolean("wn", true);
        String tempUnit = preferences.getString("tempUnit", "celsius");
        int minT = preferences.getInt("minT", -1);
        int maxT = preferences.getInt("maxT", -1);

        GoogleApiClient client = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build();

        client.connect();

        Awareness.SnapshotApi.getWeather(client)
                .setResultCallback(new ResultCallback<WeatherResult>() {
                    @Override
                    public void onResult(@NonNull WeatherResult weatherResult) {
                        if (!weatherResult.getStatus().isSuccess()) {
                            Log.e("WeatherActivity", "Could not get weather.");
                            return;
                        }
                        Weather weather = weatherResult.getWeather();
                        Log.i("WeatherActivity", "Weather: " + weather);

                        conditions = weather.getConditions();
                        StringBuilder stringBuilder = new StringBuilder();
                        if (conditions.length > 0) {
                            for (int i = 0; i < conditions.length; i++) {
                                if (i > 0) {
                                    stringBuilder.append(", ");
                                }
                                stringBuilder.append(retrieveConditionString(conditions[i]));

                            }
                        }


                        float temp = 0;
                        String unit = "";
                        switch (tempUnit){
                            case "celsius":
                                temp = weather.getTemperature(Weather.CELSIUS);
                                unit = "ºC";
                                break;
                            case "faren":
                                temp = weather.getTemperature(Weather.FAHRENHEIT);
                                unit = "ºF";
                                break;
                        }

                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        calendar.set(java.util.Calendar.MILLISECOND, 0);
                        calendar.set(java.util.Calendar.SECOND, 0);
                        int min = calendar.get(java.util.Calendar.MINUTE);
                        int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
                        String w = stringBuilder.toString();

                        if(weathenNotifOn) {
                            if ((hour == 9) || (hour == 12) || (hour == 18)) {
                                if ((w.equals("Cloudy") || w.equals("Clear")) && temp > minT ) {
                                    goodWeatherNotif(w, temp, unit);
                                } else if ((w.equals("Cloudy") || w.equals("Clear") && temp < minT)){
                                    badWeatherNotif(w, temp, unit);

                                }else if ((w.equals("Cloudy") || w.equals("Clear")) && temp < maxT ){
                                    hotWeatherNotif(w, temp, unit);
                                }

                                } else if (w.equals("Icy") || w.equals("Stormy") || w.equals("Snowy") || w.equals("Rainy") || w.equals("Windy")) {
                                    Log.e(TAG + "NO", String.valueOf(stringBuilder));
                                    badWeatherNotif(w, temp, unit);
                                } else if (w.equals("Hazy") || w.equals("Foggy")) {
                                    foggyNotif();



                            }
                        }


                        //Toast.makeText(getApplicationContext(), "Temp" + tempCelsius, Toast.LENGTH_LONG).show();


                    }
                });





        return START_STICKY;
    }

    private void foggyNotif() {
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Create notification
        NotificationCompat.Builder weightNotif = new NotificationCompat.Builder(context)
                .setContentTitle("Foogy weather. Be careful if exercising")
                .setContentText("Weight Manager")
                .setLargeIcon(bitmapIcon())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri); //This sets the sound to play



        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myIntent, 0);
        weightNotif.setContentIntent(contentIntent);
        Notification n = weightNotif.build();

        nm.notify(5, n);
    }

    private void badWeatherNotif(String w, float temp, String unit) {
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Create notification
        NotificationCompat.Builder weightNotif = new NotificationCompat.Builder(context)
                .setContentTitle("Bad weather today to exercise")
                .setContentText(w + " -" + String.format("%.0f", temp) + unit)
                .setLargeIcon(bitmapIcon())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri); //This sets the sound to play



        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myIntent, 0);
        weightNotif.setContentIntent(contentIntent);
        Notification n = weightNotif.build();

        nm.notify(4, n);

    }

    private String retrieveConditionString(int condition) {
        switch (condition) {
            case Weather.CONDITION_CLEAR:
                return "Clear";
            case Weather.CONDITION_CLOUDY:
                return "Cloudy";
            case Weather.CONDITION_FOGGY:
                return "Foggy";
            case Weather.CONDITION_HAZY:
                return "Hazy";
            case Weather.CONDITION_ICY:
                return "Icy";
            case Weather.CONDITION_RAINY:
                return "Rainy";
            case Weather.CONDITION_SNOWY:
                return "Snowy";
            case Weather.CONDITION_STORMY:
                return "Stormy";
            case Weather.CONDITION_WINDY:
                return "Windy";
            default:
                return "Unknown";
        }
    }
    private void goodWeatherNotif(String w, float temp, String unit){

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Create notification
        NotificationCompat.Builder weightNotif = new NotificationCompat.Builder(context)
                .setContentTitle("Good weather today to exercise!")
                .setContentText(w + " -" + String.format("%.0f", temp) + unit)
                .setLargeIcon(bitmapIcon())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri); //This sets the sound to play



        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myIntent, 0);
        weightNotif.setContentIntent(contentIntent);
        Notification n = weightNotif.build();

        nm.notify(3, n);
    }

    private void hotWeatherNotif(String w, float temp, String unit) {
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Create notification
        NotificationCompat.Builder weightNotif = new NotificationCompat.Builder(context)
                .setContentTitle("Very hot today to exercise")
                .setContentText(w + " -" + String.format("%.0f", temp) + unit)
                .setLargeIcon(bitmapIcon())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri); //This sets the sound to play



        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myIntent, 0);
        weightNotif.setContentIntent(contentIntent);
        Notification n = weightNotif.build();

        nm.notify(10, n);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    public Bitmap bitmapIcon(){
        BitmapDrawable PicDrawable = (BitmapDrawable) getDrawable(R.mipmap.ic_launcher);
        Bitmap contactPic = PicDrawable.getBitmap();

        Resources res = context.getResources();
        int height = (int) res.getDimension(android.R.dimen.notification_large_icon_height);
        int width = (int) res.getDimension(android.R.dimen.notification_large_icon_width);
        contactPic = Bitmap.createScaledBitmap(contactPic, width, height, false);
        return contactPic;
    }
}