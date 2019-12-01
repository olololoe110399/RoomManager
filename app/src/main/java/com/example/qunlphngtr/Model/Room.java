package com.example.qunlphngtr.Model;

public class Room {
    private int RoomID;

    public Room(int roomID) {
        RoomID = roomID;
    }

    private String RoomName;
    private int RoomPrice;
    private int RoomAcreage;
    private int RoomWaterPrice;
    private int RoomElectricPrice;
    private byte[] RoomImage;

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

    public int getRoomPrice() {
        return RoomPrice;
    }

    public void setRoomPrice(int roomPrice) {
        RoomPrice = roomPrice;
    }

    public int getRoomAcreage() {
        return RoomAcreage;
    }

    public void setRoomAcreage(int roomAcreage) {
        RoomAcreage = roomAcreage;
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

    public byte[] getRoomImage() {
        return RoomImage;
    }

    public void setRoomImage(byte[] roomImage) {
        RoomImage = roomImage;
    }

    public Room(int roomID, String roomName, int roomPrice, int roomAcreage, int roomWaterPrice, int roomElectricPrice, byte[] roomImage) {
        RoomID = roomID;
        RoomName = roomName;
        RoomPrice = roomPrice;
        RoomAcreage = roomAcreage;
        RoomWaterPrice = roomWaterPrice;
        RoomElectricPrice = roomElectricPrice;
        RoomImage = roomImage;
    }

    @Override
    public String toString() {
        return RoomName ;
    }
}
