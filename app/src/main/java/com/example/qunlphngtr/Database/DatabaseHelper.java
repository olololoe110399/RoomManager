package com.example.qunlphngtr.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dbStoredemo";
    public static final String TABLE_ROOM = "room";
    public static final String TABLE_CONTRACT = "contract";
    public static final String TABLE_CUSTOMER = "customer";
    public static final String TABLE_SERVICE = "service";
    public static final String TABLE_SERVICE_BILL = "serviceBill";
    public static final String TABLE_BILL = "bill";
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
                "\t\"roomAcreage\"\tINTEGER,\n" +
                "\t\"roomWaterPrice\"\tINTEGER,\n" +
                "\t\"roomElectricPrice\"\tINTEGER\n," +
                "\t\"roomImage\"\tBLOB\n" +
                ");");
        db.execSQL("CREATE TABLE \"contract\" (\n" +
                "\t\"contractID\"\tINTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t\"contractDateBegin\"\tDATE,\n" +
                "\t\"contractDateEnd\"\tDATE,\n" +
                "\t\"contractPeopleNumber\"\tINTEGER,\n" +
                "\t\"contractVehicleNumber\"\tINTEGER,\n" +
                "\t\"roomID\"\tINTEGER,\n" +
                "\t\"customerID\"\tTEXT,\n" +
                "\t\"contractMonthPeriodic\"\tINTEGER,\n" +
                "\t\"contractWaterNumberBegin\"\tINTEGER,\n" +
                "\t\"contracElectricNumberBegin\"\tINTEGER,\n" +
                "\t\"contractDateTerm\"\tINTEGER,\n" +
                "\t\"contractStatus\"\tINTEGER,\n" +
                "\t\"contractDeposits\"\tINTEGER\n" +
                ");");
        db.execSQL("CREATE TABLE \"customer\" (\n" +
                "\t\"customerID\"\tINTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t\"customerImage\"\tBLOB,\n" +
                "\t\"customerPhone\"\tTEXT,\n" +
                "\t\"customerName\"\tTEXT,\n" +
                "\t\"customerCMND\"\tINTEGER,\n" +
                "\t\"customerCMNDImgBefore\"\tBLOB,\n" +
                "\t\"customerCMNdImgAfter\"\tBLOB\n" +
                ");");
        db.execSQL("CREATE TABLE \"service\" (\n" +
                "\t\"serviceID\"\tINTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t\"serviceName\"\tTEXT,\n" +
                "\t\"servicePrice\"\tINTEGER\n" +
                ");");
        db.execSQL("CREATE TABLE \"serviceBill\" (\n" +
                "\t\"serviceBillID\"\tINTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t\"serviceBillName\"\tTEXT,\n" +
                "\t\"serviceBillPrice\"\tINTEGER,\n" +
                "\t\"contractID\"\tINTEGER\n" +
                ");");
        db.execSQL("CREATE TABLE \"bill\" (\n" +
                "\t\"billID\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"roomID\"\tINTEGER,\n" +
                "\t\"billCustomerName\"\tTEXT,\n" +
                "\t\"billDateBegin\"\tTEXT,\n" +
                "\t\"billDateEnd\"\tTEXT,\n" +
                "\t\"contractID\"\tINTEGER,\n" +
                "\t\"billElectricNumber\"\tINTEGER,\n" +
                "\t\"billWaterNumber\"\tINTEGER,\n" +
                "\t\"billPaymentDate\"\tDATE,\n" +
                "\t\"billTotal\"\tDOUBLE\n" +
                ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + ManagerUsers.TABLE_NAME);
        db.execSQL("Drop table if exists " + TABLE_ROOM);
        db.execSQL("Drop table if exists " + TABLE_CUSTOMER);
    }
}
