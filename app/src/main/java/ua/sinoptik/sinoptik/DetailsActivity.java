package ua.sinoptik.sinoptik;

import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Админ on 14.12.2015.
 */
public class DetailsActivity  extends FragmentActivity {
    DBHelper dbHelper;
    ArrayList<String> listdate;
    ArrayList<String> listtemp;
    ArrayList<String> listhumidity;
    ArrayList<String> listspeed;
    ArrayList<String> listicon;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        Log.d("log", " " + this);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable1", null, null, null, null, null, null);
        listdate = new ArrayList<>();
        listtemp = new ArrayList<>();
        listhumidity = new ArrayList<>();
        listspeed = new ArrayList<>();
        listicon = new ArrayList<>();
        int i=0;
        if(c.moveToFirst()){
            int dateColIndex = c.getColumnIndex("date");
            int tempColIndex = c.getColumnIndex("temp");
            int humidityColIndex = c.getColumnIndex("humidity");
            int speedColIndex = c.getColumnIndex("speed");
            int iconColIndex = c.getColumnIndex("icon");
            do{

                listdate.add(c.getString(dateColIndex));
                listtemp.add("" + c.getString(tempColIndex));
                listhumidity.add("" + c.getString(humidityColIndex));
                listspeed.add("" + c.getString(speedColIndex));
                listicon.add("" + c.getString(iconColIndex));

            }while (c.moveToNext());
        }
        showDetails(Main2Activity.position);

    }
    void showDetails(int pos) {

        Fragment details = getSupportFragmentManager()
                .findFragmentById(R.id.details);
        ((TextView) details.getView()
                .findViewById(R.id.date)).
                setText(listdate.get(pos));

        ((TextView) details.getView()
                .findViewById(R.id.temp)).
                setText(listtemp.get(pos));

        ((TextView) details.getView()
                .findViewById(R.id.humidity)).
                setText(listhumidity.get(pos)+"%");
        ((TextView) details.getView()
                .findViewById(R.id.speed)).
                setText("v-" +listspeed.get(pos) );

        ((ImageView) details.getView()
                .findViewById(R.id.imageView)).
                setImageResource(Helper.showIcon(listicon.get(pos)));


    }
}
