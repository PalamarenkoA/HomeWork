package ua.sinoptik.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class MyService extends Service {
    private Intent intentDownload;
    private Intent intentApp;
    static android.support.v4.app.NotificationCompat.Builder builder;
    static NotificationManager mNotificationManager;
    static String date ;
    String tem;
    String humidity;
    String speed;
    String icon;
    Reader reader = null;
    String resultJson = "";
    InputStream is = null;
    DBHelper dbHelper;
    Context context;
    public MyService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("servise", "onStartCommand");
        context = this;
         Thread thread = new Thread(){
            @Override
            public void run() {
                while (true) {
                    doInBackground();
                    intentDownload = new Intent(context, MyService.class);
                    intentApp = new Intent(context, MainActivity.class);
                    PendingIntent pendingIntentApp = PendingIntent.getActivity(context, 0, intentApp, PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent pendingIntentDownload = PendingIntent.getActivity(context, 0, intentDownload, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setContentText("Погода синхронизирована")
                            .setContentTitle("Синоптик")
                            .setContentIntent(pendingIntentApp)
                            .addAction(R.drawable.autorenew,"обновить",pendingIntentDownload);

                    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, builder.build());



                    }
                }
            };

        thread.start();



        return super.onStartCommand(intent, flags, startId);
    }
    protected void doInBackground() {
        Log.d("tred", " та работает " );
                try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=Cherkassy,ua&units=metric&mode=json&appid=2de143494c0b295cca9337e1e96b00e0");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                is = conn.getInputStream();

                // Convert the InputStream into a string
                resultJson = readIt(is);

                try {
                    if(MainActivity.isNetwork){
                        JSONObject dataJsonObj = new JSONObject(resultJson);
                        JSONArray weather = dataJsonObj.getJSONArray("list");
                        dbHelper = new DBHelper(MainActivity.context);
                        ContentValues cv = new ContentValues();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        db.delete("mytable1", null, null);
                        for(int i=0;i<40;i++){
                            JSONObject item = weather.getJSONObject(i);
                            date = item.getString("dt_txt");
                            tem = item.getJSONObject("main").getString("temp");
                            humidity = item.getJSONObject("main").getString("humidity");
                            speed = item.getJSONObject("wind").getString("speed");
                            icon = item.getJSONArray("weather").getJSONObject(0).getString("icon");
                            cv.put("date", date);
                            cv.put("temp", tem);

                            cv.put("humidity", humidity);
                            cv.put("speed", speed);
                            cv.put("icon", icon);
                            db.insert("mytable1", null, cv);}

                    }
                    MainActivity.listAdapter.notifyDataSetChanged();
                    try {
                        TimeUnit.SECONDS.sleep(60);

                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

    }
    private String readIt(InputStream stream) throws IOException {

        reader = new InputStreamReader(stream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        String s = bufferedReader.readLine();
        return s;
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
