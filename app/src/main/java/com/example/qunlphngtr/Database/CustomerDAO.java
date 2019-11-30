package com.example.qunlphngtr.Database;

import android.content.Context;

public class CustomerDAO {
    DatabaseHelper databaseHelper;
    public CustomerDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

}
