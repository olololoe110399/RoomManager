package com.example.qunlphngtr.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.qunlphngtr.Model.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    DatabaseHelper databaseHelper;

    public ServiceDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }
    public int addService(Service service){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("serviceName", service.getServiceName());
        values.put("servicePrice", service.getServicePrice());
        if(db.insert(databaseHelper.TABLE_SERVICE, null, values) == -1){
            return -1;
        }
        return 1;
    }
    public List<Service> getAllService(){
        List<Service> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM service", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Service service = new Service();
            service.setServiceID(cursor.getInt(0));
            service.setServiceName(cursor.getString(1));
            service.setServicePrice(cursor.getInt(2));
            list.add(service);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public int deleteServiceByID(int service){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int result = db.delete(databaseHelper.TABLE_SERVICE, "serviceID" + "=?", new String[]{String.valueOf(service)});
        if (result == 0) {
            return -1;
        }
        return 1;
    }
}
