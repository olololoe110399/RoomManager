package com.example.qunlphngtr.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qunlphngtr.Model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    DatabaseHelper databaseHelper;
    public RoomDAO(Context context){
        databaseHelper = new DatabaseHelper(context);
    }
    public int addRoom(Room room){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("roomName", room.getRoomName());
        values.put("roomPrice", room.getRoomPrice());
        values.put("roomAcreage", room.getRoomAcreage());
        values.put("roomWaterPrice", room.getRoomWaterPrice());
        values.put("roomElectricPrice", room.getRoomElectricPrice());
        values.put("roomImage", room.getRoomImage());
        if(db.insert(databaseHelper.TABLE_ROOM, null, values) == -1){
            return -1;
        }
        return 1;
    }
    public List<Room> getAllRoom(){
        List<Room> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM room", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Room room = new Room();
            room.setRoomID(cursor.getInt(0));
            room.setRoomName(cursor.getString(1));
            room.setRoomPrice(cursor.getInt(2));
            room.setRoomAcreage(cursor.getInt(3));
            room.setRoomWaterPrice(cursor.getInt(4));
            room.setRoomElectricPrice(cursor.getInt(5));
            room.setRoomImage(cursor.getBlob(6));
            list.add(room);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public int deleteRoomByID(int room){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int result = db.delete(databaseHelper.TABLE_ROOM, "roomID" + "=?", new String[]{String.valueOf(room)});
        if (result == 0) {
            return -1;
        }
        return 1;
    }
    public void updateRoomByID(Room room){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("roomName", room.getRoomName());
        values.put("roomPrice", room.getRoomPrice());
        values.put("roomAcreage", room.getRoomAcreage());
        values.put("roomWaterPrice", room.getRoomWaterPrice());
        values.put("roomElectricPrice", room.getRoomElectricPrice());
        values.put("roomImage", room.getRoomImage());
        db.update(databaseHelper.TABLE_ROOM,values, "roomID" + "=?", new String[]{String.valueOf(room.getRoomID())});
        db.close();
    }
}
