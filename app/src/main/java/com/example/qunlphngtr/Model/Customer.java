package com.example.qunlphngtr.Model;


public class Customer {
    private String CustomerID;
    private byte[] CustomerImage;
    private String CustomerPhone;
    private String CustomerName;
    private int CustomerCMND;
    private byte[] CustomerCMNDImgBefore;
    private byte[] CustomerCMNdImgAfter;

    @Override
    public String toString() {
        return CustomerName;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public byte[] getCustomerImage() {
        return CustomerImage;
    }

    public void setCustomerImage(byte[] customerImage) {
        CustomerImage = customerImage;
    }

    public String getCustomerPhone() {
        return CustomerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        CustomerPhone = customerPhone;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public int getCustomerCMND() {
        return CustomerCMND;
    }

    public void setCustomerCMND(int customerCMND) {
        CustomerCMND = customerCMND;
    }

    public byte[] getCustomerCMNDImgBefore() {
        return CustomerCMNDImgBefore;
    }

    public void setCustomerCMNDImgBefore(byte[] customerCMNDImgBefore) {
        CustomerCMNDImgBefore = customerCMNDImgBefore;
    }

    public byte[] getCustomerCMNdImgAfter() {
        return CustomerCMNdImgAfter;
    }

    public void setCustomerCMNdImgAfter(byte[] customerCMNdImgAfter) {
        CustomerCMNdImgAfter = customerCMNdImgAfter;
    }

    public Customer(String customerID, byte[] customerImage, String customerPhone, String customerName, int customerCMND) {
        CustomerID = customerID;
        CustomerImage = customerImage;
        CustomerPhone = customerPhone;
        CustomerName = customerName;
        CustomerCMND = customerCMND;
    }

    public Customer() {
    }

}
