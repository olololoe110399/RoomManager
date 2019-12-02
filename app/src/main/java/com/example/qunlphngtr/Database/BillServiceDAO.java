package com.example.qunlphngtr.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qunlphngtr.Model.Service;

import java.util.ArrayList;
import java.util.List;

public class BillServiceDAO {
    DatabaseHelper databaseHelper;

    public BillServiceDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }
    public int addServiceBill(Service service){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("serviceBillName", service.getServiceName());
        values.put("serviceBillPrice", service.getServicePrice());
        values.put("contractID", service.getContracID());
        if(db.insert(databaseHelper.TABLE_SERVICE_BILL, null, values) == -1){
            return -1;
        }
        return 1;
    }
    public int deleteBillServiceByID(int billServiceID) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int result = db.delete(databaseHelper.TABLE_SERVICE_BILL, "serviceBillID=?", new String[]{String.valueOf(billServiceID)});
        if (result == 0)
            return -1;
        return 1;
    }
    public List<Service> getsServiceBillByID(int contractID) {
        List<Service> list=new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//WHERE clause
        Service s=null;
        String selection = "contractID=?";
//WHERE clause arguments
        String[] selectionArgs = {String.valueOf(contractID)};
        Cursor c = db.query(databaseHelper.TABLE_SERVICE_BILL, null, selection, selectionArgs, null, null, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            s=new Service();
            s.setServiceID(c.getInt(0));
            s.setServiceName(c.getString(1));
            s.setServicePrice(c.getInt(2));
            s.setContracID(c.getInt(3));
            list.add(s);
            c.moveToNext();
        }
        c.close();
        return list;
    }
}
