package com.example.qunlphngtr.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qunlphngtr.Model.Bill;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.Model.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    DatabaseHelper databaseHelper;

    public BillDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public int addBill(Bill bill) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("roomID", bill.getRoom().getRoomID());
        values.put("billCustomerName", bill.getBillCustomerName());
        values.put("billDateBegin", bill.getBillDateBegin());
        values.put("billDateEnd", bill.getBillDateEnd());
        values.put("contractID", bill.getContractID());
        values.put("billElectricNumber", bill.getBillElectricNumber());
        values.put("billWaterNumber", bill.getBillWaterNumber());
        values.put("billPaymentDate", new SimpleDateFormat("dd/MM/yyyy").format(bill.getBillPaymentDate()));
        values.put("billTotal", bill.getBIllTotal());
        if (db.insert(databaseHelper.TABLE_BILL, null, values) == -1) {
            return -1;
        }
        return 1;
    }

    public List<Bill> getBillByID(int roomID) {
        List<Bill> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String sSQL = "SELECT * FROM bill WHERE bill.roomID='" + roomID + "'";
        Bill s = null;
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            s = new Bill();
            s.setBillID(c.getInt(0));
            s.setBillCustomerName(c.getString(2));
            s.setBillDateBegin(c.getString(3));
            s.setBillDateEnd(c.getString(4));
            s.setContractID(c.getString(5));
            s.setBillElectricNumber(c.getInt(6));
            s.setBillWaterNumber(c.getInt(7));
            try {
                s.setBillPaymentDate(new SimpleDateFormat("dd/MM/yyyy").parse(c.getString(8)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            s.setBIllTotal(c.getInt(9));
            list.add(s);
            c.moveToNext();
        }
        c.close();
        return list;
    }
}
