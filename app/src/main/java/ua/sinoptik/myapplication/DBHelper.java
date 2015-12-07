package ua.sinoptik.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Админ on 30.11.2015.
 */
class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(MainActivity.context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // создаем таблицу с полями
        db.execSQL("create table mytable1 ("
                + "id integer primary key autoincrement,"
                + "date text,"
                + "temp text,"
                + "humidity text,"
                + "speed text,"
                + "icon text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

