package ua.sinoptik.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        TitlesFragment.onItemClickListener {
    static int position = 0;
    static Context context;
    static boolean isNetwork;
    DBHelper dbHelper;
    ArrayList<String> listdate;
    ArrayList<String> listtemp;
    ArrayList<String> listhumidity;
    ArrayList<String> listspeed;
    ArrayList<String> listicon;
    static  ExpandableListAdapter listAdapter;
    SQLiteDatabase db;
    HashMap<String, List<String>> listDataChild;
    List<String> listDataHeader;
    static  ArrayList<String> day1;
    boolean withDetails = true;
    Cursor c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              withDetails = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        Log.d("orientation", "orientation " + withDetails);
        setContentView(R.layout.activity_main);
        context = this;
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        isNetwork = isNetworkAvailable();
        if (savedInstanceState != null) position = savedInstanceState.getInt("position");
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        c = db.query("mytable1", null, null, null, null, null, null);
           titleDate();
            if(withDetails){
        showDetails(position);}



    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    void showDetails(int pos) {

       Fragment details = getSupportFragmentManager()
                .findFragmentById(R.id.details);
                 ((TextView) details.getView()
                    .findViewById(R.id.date)).
                    setText(listdate.get(pos));

        ((TextView) details.getView()
                .findViewById(R.id.temp)).
                setText("t°-" +listtemp.get(pos));
        Log.d("log", "1 " + listtemp.get(pos));
        ((TextView) details.getView()
                .findViewById(R.id.humidity)).
                setText("f-" +listhumidity.get(pos)+"%");
        ((TextView) details.getView()
                .findViewById(R.id.speed)).
                setText("v-" +listspeed.get(pos) );

        Picasso.with(MainActivity.context).load("http://openweathermap.org/img/w/" + listicon.get(pos) + ".png").into(((ImageView) details.getView()
                .findViewById(R.id.imageView)));

               
        }
    void titleDate(){

        Log.d("log", " " + this);
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

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild,listicon);
        Fragment frag1 = getSupportFragmentManager().findFragmentById(R.id.titles);
        Log.d("log", "frag " + frag1);
        ((ExpandableListView) frag1.getView().findViewById(R.id.listView)).setAdapter(listAdapter);

    }

    @Override
    public void itemClick(int position, int groupPosition) {
        if(groupPosition==0)this.position = position;
        else this.position = position + (groupPosition-1)*8 + day1.size();
        Log.d("log", "Абсолютная позиция  - " + this.position);
        if(withDetails){showDetails(this.position);}else{
        startActivity(new Intent(this, DetailsActivity.class).putExtra("position", position));}

}
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList<Integer> grup = new ArrayList<>();
        day1 = new ArrayList<>();
        ArrayList<String> day2 = new ArrayList<>();
        ArrayList<String> day3 = new ArrayList<>();
        ArrayList<String> day4 = new ArrayList<>();
        ArrayList<String> day5 = new ArrayList<>();
        ArrayList<String> day6 = new ArrayList<>();
        ArrayList <Integer>month = new ArrayList<>();
        ArrayList year = new ArrayList<>();

        try {
            for(int i = 0;i<listdate.size();i++){
            String dateStr  = listdate.get(i);
            Date date = sdf.parse(dateStr);
                if(!grup.contains(date.getDate())){
                    month.add(date.getMonth()+1);
                    year.add(date.getYear()+1900);
                    grup.add(date.getDate());
                }
             }

            for(int i=0;i<listdate.size();i++) {
                String dateStr  = listdate.get(i);
                Date date = sdf.parse(dateStr);

                if(grup.get(0)==date.getDate()){
                    if(date.getHours()<10)day1.add("0"+String.valueOf(date.getHours())+ ".00 часов");else day1.add(String.valueOf(date.getHours())+ ".00 часов");

                }else {
                    if(grup.get(1)==date.getDate()) if(date.getHours()<10)day2.add("0"+String.valueOf(date.getHours())+ ".00 часов");else day2.add(String.valueOf(date.getHours())+ ".00 часов");
                    else{
                        if(grup.get(2)==date.getDate())if(date.getHours()<10)day3.add("0"+String.valueOf(date.getHours())+ ".00 часов");else day3.add(String.valueOf(date.getHours())+ ".00 часов");
                        else{
                            if(grup.get(3)==date.getDate())if(date.getHours()<10)day4.add("0"+String.valueOf(date.getHours())+ ".00 часов");else day4.add(String.valueOf(date.getHours())+ ".00 часов");

                            else{
                                if(grup.get(4)==date.getDate())if(date.getHours()<10)day5.add("0"+String.valueOf(date.getHours())+ ".00 часов");else day5.add(String.valueOf(date.getHours())+ ".00 часов");
                                else{
                                    if(grup.get(5)==date.getDate())if(date.getHours()<10)day6.add("0"+String.valueOf(date.getHours())+ ".00 часов");else day6.add(String.valueOf(date.getHours())+ ".00 часов");
                                }}}}}}



        } catch (ParseException e) {
            e.printStackTrace();
        }



String m = "";
for(int i = 0;i<grup.size();i++){
    switch(month.get(i)) {
        case 1:
            m ="Января";
            break;
             case 2:
            m ="Февраля";
            break;
        case 3:
            m ="Марта";
            break;
        case 4:
            m ="Апреля";
            break;
        case 5:
            m ="Мая";
            break;
        case 6:
            m ="Июня";
            break;
        case 7:
            m ="Июля";
            break;
        case 8:
            m ="Августа";
            break;
        case 9:
            m ="Сентября";
            break;
        case 10:
            m ="Октября";
            break;
        case 11:
            m ="Ноября";
            break;
        case 12:
            m ="Декабря";
            break;
    }


    if(grup.get(i)<10){
    listDataHeader.add("Датта - 0"+ grup.get(i) +" " + m + " " + year.get(i)); }
    else{
        listDataHeader.add("Датта - "+ grup.get(i)+" " + m+ " " + year.get(i));
    }
}



        if (grup.size()==1){
            listDataChild.put(listDataHeader.get(0), day1);

        }
        if (grup.size()==2){
            listDataChild.put(listDataHeader.get(0), day1);
            listDataChild.put(listDataHeader.get(1), day2);
        }
        if (grup.size()==3){
            listDataChild.put(listDataHeader.get(0), day1);
            listDataChild.put(listDataHeader.get(1), day2);
            listDataChild.put(listDataHeader.get(2), day3);
        }
        if (grup.size()==4){
            listDataChild.put(listDataHeader.get(0), day1);
            listDataChild.put(listDataHeader.get(1), day2);
            listDataChild.put(listDataHeader.get(2), day3);
            listDataChild.put(listDataHeader.get(3), day4);
        }
        if (grup.size()==5){
            listDataChild.put(listDataHeader.get(0), day1);
            listDataChild.put(listDataHeader.get(1), day2);
            listDataChild.put(listDataHeader.get(2), day3);
            listDataChild.put(listDataHeader.get(3), day4);
            listDataChild.put(listDataHeader.get(4), day5);
        }

if (grup.size()==6){
    listDataChild.put(listDataHeader.get(0), day1);
    listDataChild.put(listDataHeader.get(1), day2);
    listDataChild.put(listDataHeader.get(2), day3);
    listDataChild.put(listDataHeader.get(3), day4);
    listDataChild.put(listDataHeader.get(4), day5);
    listDataChild.put(listDataHeader.get(5), day6);
}
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", position);

    }



}
