package com.example.qunlphngtr.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dbStoredemo";
    public static final String TABLE_ROOM = "room";
    public static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ManagerUsers.SQL_USER);
        db.execSQL("CREATE TABLE \"room\" (\n" +
                "\t\"roomID\"\tINTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t\"roomName\"\tTEXT,\n" +
                "\t\"roomPrice\"\tINTEGER,\n" +
                "\t\"acreage\"\tINTEGER,\n" +
                "\t\"roomWaterPrice\"\tINTEGER,\n" +
                "\t\"roomElectricPrice\"\tINTEGER\n," +
                "\t\"roomImage\"\tBLOB\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + ManagerUsers.TABLE_NAME);
        db.execSQL("Drop table if exists " + TABLE_ROOM);

    }
}
