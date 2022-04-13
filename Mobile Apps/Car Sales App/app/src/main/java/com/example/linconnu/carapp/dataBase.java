package com.example.linconnu.carapp;

/*
 *Name: Hermann Yepdjio
 *SID: 40917845
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by linconnu on 24/10/17.
 */

public class    dataBase extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "cars.db";
    public static final int DATABASE_VERSION = 4;
   /*public static final String TABLE_NAME = "car_table";
    public static final String column_1 = "name";
    public static final String column_2 = "year";
    public static final String column_3 = "price";*/

    String CREATE_QUERY = "CREATE TABLE "+ newCar.info.TABLE_NAME + "("
            + newCar.info.column_1 + " INTEGER PRIMARY KEY,"
            + newCar.info.column_2 + " TEXT,"
            + newCar.info.column_3 + " INTEGER,"
            + newCar.info.column_4 + " DOUBLE" + ");";

    public dataBase(Context context)

    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("DATABASE OPERATIONS", "Database created / Opened ...");
    }

    @Override
    public void onCreate(SQLiteDatabase db)

    {
        db.execSQL(CREATE_QUERY);
        Log.e("DATABASE OPERATIONS", "Database created ...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+ newCar.info.TABLE_NAME);
        onCreate(db);
    }
    public boolean addData(String name, String year, String price)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentData = new ContentValues();
        contentData.put(newCar.info.column_2,name);
        contentData.put(newCar.info.column_3,year);
        contentData.put(newCar.info.column_4,price);

        long error = db.insert(newCar.info.TABLE_NAME, null, contentData);
        if(error == -1)
        {
            Log.e("DATABASE OPERATIONS", "TABLE INSERT ERROR ...");
            return false;  //insert didnt succeed
        }
        else
        {
            Log.e("DATABASE OPERATIONS", "TABLE INSERT SUCCESS ...");
            return true;
        }
    }

    public Cursor showCars()
    {
        SQLiteDatabase db  = this.getWritableDatabase();
        Cursor show = db.rawQuery("select * from " + newCar.info.TABLE_NAME, null);
        return show;
    }
}

