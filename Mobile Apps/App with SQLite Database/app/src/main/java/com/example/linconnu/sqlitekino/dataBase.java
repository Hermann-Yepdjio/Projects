package com.example.linconnu.sqlitekino;

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
    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 4;
   /*public static final String TABLE_NAME = "movie_table";
    public static final String column_1 = "name";
    public static final String column_2 = "genre";
    public static final String column_3 = "price";*/

    String CREATE_QUERY = "CREATE TABLE "+ newMovie.info.TABLE_NAME + "("
           + newMovie.info.column_1 + " INTEGER PRIMARY KEY,"
           + newMovie.info.column_2 + " TEXT,"
           + newMovie.info.column_3 + " TEXT,"
           + newMovie.info.column_4 + " DOUBLE" + ");";

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
        db.execSQL("DROP TABLE IF EXISTS "+ newMovie.info.TABLE_NAME);
        onCreate(db);
    }
    public boolean addData(String name, String genre, String price)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentData = new ContentValues();
        contentData.put(newMovie.info.column_2,name);
        contentData.put(newMovie.info.column_3,genre);
        contentData.put(newMovie.info.column_4,price);

        long error = db.insert(newMovie.info.TABLE_NAME, null, contentData);
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

    public Cursor showMovies()
    {
        SQLiteDatabase db  = this.getWritableDatabase();
        Cursor show = db.rawQuery("select * from " + newMovie.info.TABLE_NAME, null);
        return show;
    }
}
