package com.example.qunlphngtr.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qunlphngtr.Model.Customer;

import java.util.ArrayList;
import java.util.List;


public class CustomerDAO {
    DatabaseHelper databaseHelper;
    public CustomerDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }
    public int addCustomer(Customer customer) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("customerID", customer.getCustomerID());
        values.put("customerImage", customer.getCustomerImage());
        values.put("customerPhone", customer.getCustomerPhone());
        values.put("customerName", customer.getCustomerName());
        values.put("customerCMND", customer.getCustomerCMND());
        values.put("customerCMNDImgBefore", customer.getCustomerCMNDImgBefore());
        values.put("customerCMNdImgAfter", customer.getCustomerCMNdImgAfter());
        if (db.insert(databaseHelper.TABLE_CUSTOMER, null, values) == -1) {
            return -1;
        }
        return 1;
    }
    public List<Customer> getAllCustomer(){
        List<Customer> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM customer", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Customer customer = new Customer();
            customer.setCustomerID(cursor.getString(0));
            customer.setCustomerImage(cursor.getBlob(1));
            customer.setCustomerPhone(cursor.getString(2));
            customer.setCustomerName(cursor.getString(3));
            customer.setCustomerCMND(cursor.getInt(4));
            customer.setCustomerCMNDImgBefore(cursor.getBlob(5));
            customer.setCustomerCMNdImgAfter(cursor.getBlob(6));
            list.add(customer);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
