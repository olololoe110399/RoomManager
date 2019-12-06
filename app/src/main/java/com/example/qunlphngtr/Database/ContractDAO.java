package com.example.qunlphngtr.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qunlphngtr.Model.Contract;
import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.Model.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ContractDAO {
    DatabaseHelper databaseHelper;

    public ContractDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public int addContract(Contract contract) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("contractDateBegin", contract.getContractDateBegin());
        values.put("contractDateEnd", contract.getContractDateEnd());
        values.put("contractPeopleNumber", contract.getContractPeopleNumber());
        values.put("contractVehicleNumber", contract.getContractVehicleNumber());
        values.put("roomID", contract.getRoom().getRoomID());
        values.put("customerID", contract.getCustomer().getCustomerID());
        values.put("contractMonthPeriodic", contract.getContractMonthPeriodic());
        values.put("contractWaterNumberBegin", contract.getContracNumberWaterBegin());
        values.put("contractElectricNumberBegin", contract.getContracNumberElectricBegin());
        values.put("contractDateTerm", contract.getContractDateTerm());
        values.put("contractStatus", contract.getContractstatus());
        values.put("contractDeposits", contract.getContractDeposits());
        if (db.insert(databaseHelper.TABLE_CONTRACT, null, values) == -1) {
            return -1;
        }
        return 1;
    }

    public int updateContractStatus(int contractID) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("contractStatus", 1);
        if (db.update(databaseHelper.TABLE_CONTRACT, values, "contractID=?", new String[]{String.valueOf(contractID)}) == 0) {
            return -1;
        }
        return 1;
    }

    public int deleteContractByID(int contractID) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int result = db.delete(databaseHelper.TABLE_CONTRACT, "contractID=?", new String[]{String.valueOf(contractID)});
        if (result == 0)
            return -1;
        return 1;
    }

    public List<Contract> getAllContract(int roomID) {
        List<Contract> contractList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sSQL = "SELECT contractID,contractDateBegin,contractDateEnd,contractPeopleNumber,contractVehicleNumber," +
                "room.roomID,room.roomName,room.roomPrice,room.roomAcreage,room.roomWaterPrice,room.roomElectricPrice,room.roomImage," +
                "customer.customerID,customer.customerImage,customer.customerPhone,customer.customerName,customer.customerCMND,customer.customerCMNDImgBefore,customer.customerCMNdImgAfter," +
                "contractMonthPeriodic,contractWaterNumberBegin,contractElectricNumberBegin,contractDateTerm,contractStatus,contractDeposits" +
                " FROM contract INNER JOIN room ON contract.roomID = room.roomID INNER JOIN customer on contract.customerID=customer.customerID WHERE contract.roomID ='" +
                roomID + "'";
        Cursor cursor = db.rawQuery(sSQL, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contract contract = new Contract();
            contract.setContractID(cursor.getInt(0));
            contract.setContractDateBegin(cursor.getString(1));
            contract.setContractDateEnd(cursor.getString(2));
            contract.setContractPeopleNumber(cursor.getInt(3));
            contract.setContractVehicleNumber(cursor.getInt(4));
            contract.setRoom(new Room(cursor.getInt(5), cursor.getString(6), cursor.getInt(7), cursor.getInt(8), cursor.getInt(9), cursor.getInt(10), cursor.getBlob(11)));
            contract.setCustomer(new Customer(cursor.getInt(12), cursor.getBlob(13), cursor.getString(14), cursor.getString(15), cursor.getInt(16), cursor.getBlob(17), cursor.getBlob(18)));
            contract.setContractMonthPeriodic(cursor.getInt(19));
            contract.setContracNumberElectricBegin(cursor.getInt(20));
            contract.setContracNumberWaterBegin(cursor.getInt(21));
            contract.setContractDateTerm(cursor.getInt(22));
            contract.setContractstatus(cursor.getInt(23));
            contract.setContractDeposits(cursor.getInt(24));
            contractList.add(contract);
            cursor.moveToNext();
        }
        cursor.close();
        return contractList;
    }

    public int getContractIDByStatus() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//WHERE clause
        int contractID = -1;
        String sSQL = "SELECT contractID FROM contract WHERE contractStatus= 0";
//WHERE clause arguments
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            if (c.getInt(0) == 0) {
                contractID = c.getInt(0);
                break;
            }
            c.moveToNext();
        }
        c.close();
        return contractID;
    }

    public int getStatusRoom(int roomID) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//WHERE clause
        int status = -1;
        String sSQL = "SELECT * FROM contract WHERE roomID = '" + roomID + "'";
//WHERE clause arguments
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            if (c.getInt(11) == 0) {
                status = 1;
                break;
            }
            c.moveToNext();
        }
        c.close();
        return status;
    }

    public int getpeopleNumberRoom(int roomID) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//WHERE clause
        int peopleNumberRoom = 0;
        String sSQL = "SELECT * FROM contract WHERE roomID = '" + roomID + "'";
//WHERE clause arguments
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            if (c.getInt(11) == 0) {
                peopleNumberRoom = c.getInt(3);
                break;
            }
            c.moveToNext();
        }
        c.close();
        return peopleNumberRoom;
    }

    public int getvehicleNumberRoom(int roomID) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//WHERE clause
        int vehicleNumberRoom = 0;
        String sSQL = "SELECT * FROM contract WHERE roomID = '" + roomID + "'";
//WHERE clause arguments
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            if (c.getInt(11) == 0) {
                vehicleNumberRoom = c.getInt(4);
                break;
            }
            c.moveToNext();
        }
        c.close();
        return vehicleNumberRoom;
    }

    public int getallPeopleNumberRoom() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//WHERE clause
        int peopleNumberRoom = 0;
        String sSQL = "SELECT * FROM contract";
//WHERE clause arguments
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            if (c.getInt(11) == 0) {
                peopleNumberRoom = peopleNumberRoom + c.getInt(3);
            }
            c.moveToNext();
        }
        c.close();
        return peopleNumberRoom;
    }

    public int getallNumberRoomNotNull() {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//WHERE clause
        int status = 0;
        String sSQL = "SELECT * FROM contract ";
//WHERE clause arguments
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            if (c.getInt(11) == 0) {
                status = status + 1;
            }
            c.moveToNext();
        }
        c.close();
        return status;
    }

}
