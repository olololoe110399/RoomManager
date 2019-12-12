package com.example.qunlphngtr.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qunlphngtr.Model.Users;

import java.util.ArrayList;
import java.util.List;


public class ManagerUsers {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    public static final String UserFullName = "UserFullName";
    public static final String UserPhone = "UserPhone";
    public static final String UserName = "UserName";
    public static final String UserAddress = "UserAddress";
    public static final String UserPassword = "UserPassword";
    public static final String UserAvater = "UserAvater";
    public static final String TABLE_NAME = "User";
    public static final String SQL_USER = "CREATE TABLE " + TABLE_NAME + "("
            + UserName + " text primary key, "
            + UserPassword + " text, "
            + UserPhone + " text, "
            + UserFullName + " text,"
            + UserAddress + " text,"
            + UserAvater + " BLOB);";
    public static final String TAG = "NguoiDungDAO";

    public ManagerUsers(Context context) {

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    //insert
    public int inserUser(Users nd) {
        ContentValues values = new ContentValues();
        values.put(UserName, nd.getUserName());
        values.put(UserPassword, nd.getUserPassword());
        try {
            if (db.insert(TABLE_NAME, null, values) == -1) {
                return -1;
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return 1;
    }

    //getAll
    public List<Users> getAllUser() {
        List<Users> dsNguoiDung = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            Users ee = new Users();
            ee.setUserName(c.getString(0));
            ee.setUserPassword(c.getString(1));
            ee.setUserPhone(c.getString(2));
            ee.setUserFullName(c.getString(3));
            ee.setUserAddress(c.getString(4));
            ee.setUserAvater(c.getBlob(5));
            dsNguoiDung.add(ee);
            Log.d("//=====", ee.toString());
            c.moveToNext();
        }
        c.close();
        return dsNguoiDung;
    }

    public int checkInformatioNull(String user) {
        int id = -1;
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+UserName+"= '" + user + "'", null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    if (c.isNull(3)) {
                        id = 1;
                    }

                } while (c.moveToNext());
            }
            c.close();
        }
        return id;
    }

    public Users getUserById(String user) {
        Users ee = null;
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE username = '" + user + "'", null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    ee = new Users();
                    ee.setUserName(c.getString(0));
                    ee.setUserPassword(c.getString(1));
                    ee.setUserPhone(c.getString(2));
                    ee.setUserFullName(c.getString(3));
                    ee.setUserAddress(c.getString(4));
                    ee.setUserAvater(c.getBlob(5));
                } while (c.moveToNext());
            }
            c.close();
        }
        return ee;
    }

    public int changePasswordUser(Users nd) {
        ContentValues values = new ContentValues();
        values.put(UserPassword, nd.getUserPassword());
        int result = db.update(TABLE_NAME, values, UserName+"=?", new String[]{nd.getUserName()});
        if (result == 0) {
            return -1;
        }
        return 1;
    }


    public int updateUser(Users users) {
        ContentValues values = new ContentValues();
        values.put(UserPhone, users.getUserPhone());
        values.put(UserFullName, users.getUserFullName());
        values.put(UserAddress, users.getUserAddress());
        values.put(UserAvater, users.getUserAvater());
        int result = db.update(TABLE_NAME, values, UserName+"=?", new String[]{users.getUserName()});
        if (result == 0) {
            return -1;
        }
        return 1;
    }

    public int addimage(String username, byte[] hinh) {
        ContentValues values = new ContentValues();
        values.put("image", hinh);
        int result = db.update(TABLE_NAME, values, UserName+"=?", new String[]{username});
        if (result == 0) {
            return -1;
        }
        return 1;
    }

    //delete
    public int deleteUserByID(String username) {
        int result = db.delete(TABLE_NAME, UserName+"=?", new String[]{username});
        if (result == 0)
            return -1;
        return 1;
    }


    //check login
    public int checkLogin(String username, String password) {
        int id = -1;
        Cursor cursor = db.rawQuery("SELECT "+UserName+" FROM "+TABLE_NAME+" WHERE "+UserName+"=? AND "+UserPassword+"=?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = 1;
            cursor.close();
        }
        return id;
    }

    public int checkEmail(String username) {
        int id = -1;
        Cursor cursor = db.rawQuery("SELECT "+UserName+" FROM "+TABLE_NAME+" WHERE "+UserName+"=?", new String[]{username});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = 1;
            cursor.close();
        }
        return id;
    }

    public int checkPhone(String phone) {
        int id = -1;
        Cursor cursor = db.rawQuery("SELECT "+UserName+" FROM "+TABLE_NAME+" WHERE "+UserPhone+"=?", new String[]{phone});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = 1;
            cursor.close();
        }
        return id;
    }


}

