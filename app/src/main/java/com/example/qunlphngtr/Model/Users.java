package com.example.qunlphngtr.Model;

public class Users {
    private String UserFullName;
    private String UserPhone;
    private String UserName;
    private String UserAddress;
    private String UserPassword;
    private byte[] UserAvater;

    public String getUserFullName() {
        return UserFullName;
    }

    public void setUserFullName(String userFullName) {
        UserFullName = userFullName;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserAddress() {
        return UserAddress;
    }

    public void setUserAddress(String userAddress) {
        UserAddress = userAddress;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public byte[] getUserAvater() {
        return UserAvater;
    }

    public void setUserAvater(byte[] userAvater) {
        UserAvater = userAvater;
    }

    public Users() {
    }
}
