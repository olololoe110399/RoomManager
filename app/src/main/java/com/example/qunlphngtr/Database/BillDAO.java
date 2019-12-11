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
        values.put("billPaymentDate", new SimpleDateFormat("yyyy/MM/dd").format(bill.getBillPaymentDate()));
        values.put("billTotal", bill.getBIllTotal());
        values.put("billDebtsToPay", bill.getBillDebtsToPay());
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
            s.setContractID(c.getInt(5));
            s.setBillElectricNumber(c.getInt(6));
            s.setBillWaterNumber(c.getInt(7));
            try {
                s.setBillPaymentDate(new SimpleDateFormat("dd/MM/yyyy").parse(c.getString(8)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            s.setBIllTotal(c.getDouble(9));
            s.setBillDebtsToPay(c.getDouble(10));
            list.add(s);
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    public List<Bill> getAllBill() {
        List<Bill> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String sSQL = "SELECT * FROM bill ";
        Bill s = null;
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            s = new Bill();
            s.setBillID(c.getInt(0));
            s.setBillCustomerName(c.getString(2));
            s.setBillDateBegin(c.getString(3));
            s.setBillDateEnd(c.getString(4));
            s.setContractID(c.getInt(5));
            s.setBillElectricNumber(c.getInt(6));
            s.setBillWaterNumber(c.getInt(7));
            try {
                s.setBillPaymentDate(new SimpleDateFormat("dd/MM/yyyy").parse(c.getString(8)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            s.setBIllTotal(c.getDouble(9));
            s.setBillDebtsToPay(c.getDouble(10));
            list.add(s);
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    public List<Bill> getBillByContractID(int contractID) {
        List<Bill> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String sSQL = "SELECT * FROM bill WHERE bill.contractID='" + contractID + "'";
        Bill s = null;
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            s = new Bill();
            s.setBillID(c.getInt(0));
            s.setBillCustomerName(c.getString(2));
            s.setBillDateBegin(c.getString(3));
            s.setBillDateEnd(c.getString(4));
            s.setContractID(c.getInt(5));
            s.setBillElectricNumber(c.getInt(6));
            s.setBillWaterNumber(c.getInt(7));
            try {
                s.setBillPaymentDate(new SimpleDateFormat("dd/MM/yyyy").parse(c.getString(8)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            s.setBIllTotal(c.getDouble(9));
            s.setBillDebtsToPay(c.getDouble(10));
            list.add(s);
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    public List<Bill> getBillDebtsByContractID(int contractID) {
        List<Bill> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String sSQL = "SELECT * FROM bill WHERE bill.contractID='" + contractID + "' AND bill.billDebtsToPay > 0";
        Bill s = null;
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            s = new Bill();
            s.setBillID(c.getInt(0));
            s.setBillCustomerName(c.getString(2));
            s.setBillDateBegin(c.getString(3));
            s.setBillDateEnd(c.getString(4));
            s.setContractID(c.getInt(5));
            s.setBillElectricNumber(c.getInt(6));
            s.setBillWaterNumber(c.getInt(7));
            try {
                s.setBillPaymentDate(new SimpleDateFormat("dd/MM/yyyy").parse(c.getString(8)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            s.setBIllTotal(c.getDouble(9));
            s.setBillDebtsToPay(c.getDouble(10));
            list.add(s);
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    public void updateBill(Bill bill) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("billTotal", bill.getBIllTotal());
        values.put("billDebtsToPay", bill.getBillDebtsToPay());
        values.put("billPaymentDate", new SimpleDateFormat("yyyy/MM/dd").format(bill.getBillPaymentDate()));
        db.update(databaseHelper.TABLE_BILL, values, "billID" + "=?", new String[]{String.valueOf(bill.getBillID())});
        db.close();
    }

    public double getAllSumbillTotal() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        double SumTotal = 0;
        String sSQL = "SELECT SUM(billTotal) AS SumTotal FROM bill";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumTotal = c.getDouble(c.getColumnIndex("SumTotal"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumTotal;
    }


    public double getAllSumbillTotalbyDate(String datebegin, String dateend) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        double SumTotal = 0;
        String sSQL = "SELECT SUM(billTotal) AS SumTotal FROM bill WHERE bill.billPaymentDate BETWEEN  \"" + datebegin + "\" AND \"" + dateend + "\"";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumTotal = c.getDouble(c.getColumnIndex("SumTotal"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumTotal;
    }

    public double getAllSumbillTotalbyDateandRoomID(String datebegin, String dateend, int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        double SumTotal = 0;
        String sSQL = "SELECT SUM(billTotal) AS SumTotal FROM bill WHERE bill.billPaymentDate BETWEEN  \"" + datebegin + "\" AND \"" + dateend + "\" AND bill.roomID='" + id + "'";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumTotal = c.getDouble(c.getColumnIndex("SumTotal"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumTotal;
    }

    public double getAllSumbillTotalbyRoomID(int ID) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        double SumTotal = 0;
        String sSQL = "SELECT SUM(billTotal) AS SumTotal FROM bill WHERE bill.roomID='" + ID + "'";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumTotal = c.getDouble(c.getColumnIndex("SumTotal"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumTotal;
    }

    public double getAllSumbillDebtsToPay() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        double SumDebtsToPay = 0;
        String sSQL = "SELECT SUM(billDebtsToPay) AS SumDebtsToPay FROM bill";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumDebtsToPay = c.getDouble(c.getColumnIndex("SumDebtsToPay"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumDebtsToPay;
    }

    public double getAllSumbillDebtsToPaybyDate(String datebegin, String dateend) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        double SumDebtsToPay = 0;
        String sSQL = "SELECT SUM(billDebtsToPay) AS SumDebtsToPay FROM bill WHERE bill.billPaymentDate BETWEEN  \"" + datebegin + "\" AND \"" + dateend + "\"";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumDebtsToPay = c.getDouble(c.getColumnIndex("SumDebtsToPay"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumDebtsToPay;
    }

    public double getAllSumbillDebtsToPaybyDateandRoomID(String datebegin, String dateend, int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        double SumDebtsToPay = 0;
        String sSQL = "SELECT SUM(billDebtsToPay) AS SumDebtsToPay FROM bill WHERE bill.billPaymentDate BETWEEN  \"" + datebegin + "\" AND \"" + dateend + "\" AND bill.roomID='" + id + "'";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumDebtsToPay = c.getDouble(c.getColumnIndex("SumDebtsToPay"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumDebtsToPay;
    }

    public double getAllSumbillDebtsToPaybyRoomID(int ID) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        double SumDebtsToPay = 0;
        String sSQL = "SELECT SUM(billDebtsToPay) AS SumDebtsToPay FROM bill WHERE bill.roomID='" + ID + "'";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumDebtsToPay = c.getDouble(c.getColumnIndex("SumDebtsToPay"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumDebtsToPay;
    }

    public int getSumNumberElectric() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int SumElectricNumber = 0;
        String sSQL = "SELECT SUM(billElectricNumber) AS SumElectricNumber FROM bill";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumElectricNumber = c.getInt(c.getColumnIndex("SumElectricNumber"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumElectricNumber;
    }

    public int getSumNumberElectricbyroomID(int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int SumElectricNumber = 0;
        String sSQL = "SELECT SUM(billElectricNumber) AS SumElectricNumber FROM bill WHERE bill.roomID='" + id + "'";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumElectricNumber = c.getInt(c.getColumnIndex("SumElectricNumber"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumElectricNumber;
    }

    public int getSumNumberElectricbyDate(String datebegin, String dateend) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int SumElectricNumber = 0;
        String sSQL = "SELECT SUM(billElectricNumber) AS SumElectricNumber FROM bill WHERE bill.billPaymentDate BETWEEN  \"" + datebegin + "\" AND \"" + dateend + "\"";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumElectricNumber = c.getInt(c.getColumnIndex("SumElectricNumber"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumElectricNumber;
    }

    public int getSumNumberElectricbyDateandRoomID(String datebegin, String dateend, int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int SumElectricNumber = 0;
        String sSQL = "SELECT SUM(billElectricNumber) AS SumElectricNumber FROM bill WHERE bill.billPaymentDate BETWEEN  \"" + datebegin + "\" AND \"" + dateend + "\" AND bill.roomID='" + id + "'";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumElectricNumber = c.getInt(c.getColumnIndex("SumElectricNumber"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumElectricNumber;
    }

    public int getSumNumberWater() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int SumElectricNumber = 0;
        String sSQL = "SELECT SUM(billWaterNumber) AS SumElectricNumber FROM bill";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumElectricNumber = c.getInt(c.getColumnIndex("SumElectricNumber"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumElectricNumber;
    }

    public int getSumNumberWaterbyroomID(int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int SumElectricNumber = 0;
        String sSQL = "SELECT SUM(billWaterNumber) AS SumElectricNumber FROM bill WHERE bill.roomID='" + id + "'";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumElectricNumber = c.getInt(c.getColumnIndex("SumElectricNumber"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumElectricNumber;
    }

    public int getSumNumberWatericbyDate(String datebegin, String dateend) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int SumElectricNumber = 0;
        String sSQL = "SELECT SUM(billWaterNumber) AS SumElectricNumber FROM bill WHERE bill.billPaymentDate BETWEEN  \"" + datebegin + "\" AND \"" + dateend + "\"";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumElectricNumber = c.getInt(c.getColumnIndex("SumElectricNumber"));
            c.moveToNext();
        }
        c.close();
        db.close();
        return SumElectricNumber;
    }

    public int getSumNumberWaterbyDateandRoomID(String datebegin, String dateend, int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int SumElectricNumber = 0;
        String sSQL = "SELECT SUM(billWaterNumber) AS SumElectricNumber FROM bill WHERE bill.billPaymentDate BETWEEN  \"" + datebegin + "\" AND \"" + dateend + "\" AND bill.roomID='" + id + "'";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            SumElectricNumber = c.getInt(c.getColumnIndex("SumElectricNumber"));
            c.moveToNext();
        }
        c.close();
        db.close();

        return SumElectricNumber;
    }
}
