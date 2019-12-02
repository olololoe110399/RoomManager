package com.example.qunlphngtr.Model;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Service {

    private int ServiceID;
    private String ServiceName;
    private int ServicePrice;
    private int ContracID;

    public int getContracID() {
        return ContracID;
    }

    public void setContracID(int contracID) {
        ContracID = contracID;
    }


    public int getServiceID() {
        return ServiceID;
    }

    public void setServiceID(int serviceID) {
        ServiceID = serviceID;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public int getServicePrice() {
        return ServicePrice;
    }

    public void setServicePrice(int servicePrice) {
        ServicePrice = servicePrice;
    }

    public Service(int serviceID, String serviceName, int servicePrice) {
        ServiceID = serviceID;
        ServiceName = serviceName;
        ServicePrice = servicePrice;
    }



    public Service() {
    }

    @Override
    public String toString() {
        NumberFormat formatter = new DecimalFormat("#,###");
        return ServiceName+" - "+formatter.format(ServicePrice)+" VND";
    }
}
