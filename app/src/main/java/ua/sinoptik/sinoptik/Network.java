package ua.sinoptik.sinoptik;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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

/**
 * Created by Админ on 14.12.2015.
 */
public class Network extends AsyncTask<Void, Void, Boolean> {

    static  String date ;
    private String tem;
    private String humidity;
    private String speed;
    private String icon;
    Reader reader = null;
    String resultJson = "";
    InputStream is = null;
    DBHelper dbHelper;
    Boolean connect;
    @Override
    protected Boolean doInBackground(Void... params) {
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
                    if(start.isNetwork){
                        connect = true;
                        JSONObject dataJsonObj = new JSONObject(resultJson);
                        JSONArray weather = dataJsonObj.getJSONArray("list");
                        Log.d("tred", " та работает " );
                        dbHelper = new DBHelper(Main2Activity.context);
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


                }
                catch (JSONException e) {
                    e.printStackTrace();
                    connect = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                 connect = false;

            }

        return connect;
    }
    @Override
    protected void onPostExecute(Boolean connect){
        Log.d("tred", " мы тоже работает " + connect);
        if(connect){
            Helper.showToast("Соединение установлено", start.context);

            start.context.startActivity(new Intent(start.context, Main2Activity.class).addFlags(
                    Intent.FLAG_ACTIVITY_TASK_ON_HOME));
            start start = new start();
            start.finish();

        }else{

        }
    }
    public String readIt(InputStream stream) throws IOException {

        reader = new InputStreamReader(stream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        String s = bufferedReader.readLine();
        return s;
    }



}
