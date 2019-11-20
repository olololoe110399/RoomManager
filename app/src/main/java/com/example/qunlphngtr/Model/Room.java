package com.example.qunlphngtr.Model;

public class Room {
    private int RoomID;
    private String RoomName;
    private double RoomPrice;
    private int Acreage;
    private int RoomWaterPrice;
    private int RoomElectricPrice;

    public Room(int roomID, String roomName, double roomPrice, int acreage, int roomWaterPrice, int roomElectricPrice) {
        RoomID = roomID;
        RoomName = roomName;
        RoomPrice = roomPrice;
        Acreage = acreage;
        RoomWaterPrice = roomWaterPrice;
        RoomElectricPrice = roomElectricPrice;
    }

    public Room() {
    }

    public int getRoomID() {
        return RoomID;
    }

    public void setRoomID(int roomID) {
        RoomID = roomID;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public double getRoomPrice() {
        return RoomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        RoomPrice = roomPrice;
    }

    public int getAcreage() {
        return Acreage;
    }

    public void setAcreage(int acreage) {
        Acreage = acreage;
    }

    public int getRoomWaterPrice() {
        return RoomWaterPrice;
    }

    public void setRoomWaterPrice(int roomWaterPrice) {
        RoomWaterPrice = roomWaterPrice;
    }

    public int getRoomElectricPrice() {
        return RoomElectricPrice;
    }

    public void setRoomElectricPrice(int roomElectricPrice) {
        RoomElectricPrice = roomElectricPrice;
    }

    @Override
    public String toString() {
        return RoomName ;
    }
}
