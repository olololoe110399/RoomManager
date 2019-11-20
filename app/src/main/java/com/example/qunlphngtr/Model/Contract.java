package com.example.qunlphngtr.Model;

import java.util.Date;

public class Contract {
    private String ContractID;
    private String ContractDateBegin;
    private String ContractDateEnd;
    private int ContractPeopleNumber;
    private int ContractVehicleNumber;
    private Room Room;
    private Customer Customer;
    private int ContractMonthPeriodic;
    private int ContracNumberWaterBegin;
    private int ContracNumberElectricBegin;
    private int ContractDateTerm;
    private int Contractstatus;
    private double ContractDeposits;

    public double getContractDeposits() {
        return ContractDeposits;
    }

    public void setContractDeposits(double contractDeposits) {
        ContractDeposits = contractDeposits;
    }

    public int getContractstatus() {
        return Contractstatus;
    }

    public void setContractstatus(int contractstatus) {
        Contractstatus = contractstatus;
    }

    public int getContractDateTerm() {
        return ContractDateTerm;
    }

    public void setContractDateTerm(int contractDateTerm) {
        ContractDateTerm = contractDateTerm;
    }

    public Contract(String contractID, String contractDateBegin, String contractDateEnd, int contractPeopleNumber, int contractVehicleNumber, com.example.qunlphngtr.Model.Room room, Customer customer, int contractMonthPeriodic, int contracNumberWaterBegin, int contracNumberElectricBegin, int contractDateTerm, int contractstatus,double contractDeposits) {
        ContractID = contractID;
        ContractDateBegin = contractDateBegin;
        ContractDateEnd = contractDateEnd;
        ContractPeopleNumber = contractPeopleNumber;
        ContractVehicleNumber = contractVehicleNumber;
        Room = room;
        Customer = customer;
        ContractMonthPeriodic = contractMonthPeriodic;
        ContracNumberWaterBegin = contracNumberWaterBegin;
        ContracNumberElectricBegin = contracNumberElectricBegin;
        ContractDateTerm = contractDateTerm;
        Contractstatus=contractstatus;
        ContractDeposits=contractDeposits;
    }

    public String getContractID() {
        return ContractID;
    }

    public void setContractID(String contractID) {
        ContractID = contractID;
    }

    public String getContractDateBegin() {
        return ContractDateBegin;
    }

    public void setContractDateBegin(String contractDateBegin) {
        ContractDateBegin = contractDateBegin;
    }

    public String getContractDateEnd() {
        return ContractDateEnd;
    }

    public void setContractDateEnd(String contractDateEnd) {
        ContractDateEnd = contractDateEnd;
    }

    public int getContractPeopleNumber() {
        return ContractPeopleNumber;
    }

    public void setContractPeopleNumber(int contractPeopleNumber) {
        ContractPeopleNumber = contractPeopleNumber;
    }

    public int getContractVehicleNumber() {
        return ContractVehicleNumber;
    }

    public void setContractVehicleNumber(int contractVehicleNumber) {
        ContractVehicleNumber = contractVehicleNumber;
    }

    public Room getRoom() {
        return Room;
    }

    public void setRoom(Room room) {
        Room = room;
    }

    public Customer getCustomer() {
        return Customer;
    }

    public void setCustomer(Customer customer) {
        Customer = customer;
    }

    public int getContractMonthPeriodic() {
        return ContractMonthPeriodic;
    }

    public void setContractMonthPeriodic(int contractMonthPeriodic) {
        ContractMonthPeriodic = contractMonthPeriodic;
    }

    public int getContracNumberWaterBegin() {
        return ContracNumberWaterBegin;
    }

    public void setContracNumberWaterBegin(int contracNumberWaterBegin) {
        ContracNumberWaterBegin = contracNumberWaterBegin;
    }

    public int getContracNumberElectricBegin() {
        return ContracNumberElectricBegin;
    }

    public void setContracNumberElectricBegin(int contracNumberElectricBegin) {
        ContracNumberElectricBegin = contracNumberElectricBegin;
    }


    public Contract() {
    }
}
