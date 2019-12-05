package com.example.qunlphngtr.Model;

import java.util.Date;

public class Bill {
    private int BillID;
    private Room Room;
    private String BillCustomerName;
    private String BillDateBegin;
    private String BillDateEnd;
    private int ContractID;
    private int BillElectricNumber;
    private int BillWaterNumber;
    private Date BillPaymentDate;
    private double BillTotal;
    private double BillDebtsToPay;

    public double getBillDebtsToPay() {
        return BillDebtsToPay;
    }

    public void setBillDebtsToPay(double billDebtsToPay) {
        BillDebtsToPay = billDebtsToPay;
    }

    public String getBillCustomerName() {
        return BillCustomerName;
    }

    public void setBillCustomerName(String billCustomerName) {
        BillCustomerName = billCustomerName;
    }

    public int getBillID() {
        return BillID;
    }

    public void setBillID(int billID) {
        BillID = billID;
    }
    public com.example.qunlphngtr.Model.Room getRoom() {
        return Room;
    }

    public void setRoom(com.example.qunlphngtr.Model.Room room) {
        Room = room;
    }


    public String getBillDateBegin() {
        return BillDateBegin;
    }

    public void setBillDateBegin(String billDateBegin) {
        BillDateBegin = billDateBegin;
    }

    public String getBillDateEnd() {
        return BillDateEnd;
    }

    public void setBillDateEnd(String billDateEnd) {
        BillDateEnd = billDateEnd;
    }


    public int getContractID() {
        return ContractID;
    }

    public void setContractID(int contractID) {
        ContractID = contractID;
    }

    public int getBillElectricNumber() {
        return BillElectricNumber;
    }

    public void setBillElectricNumber(int billElectricNumber) {
        BillElectricNumber = billElectricNumber;
    }

    public int getBillWaterNumber() {
        return BillWaterNumber;
    }

    public void setBillWaterNumber(int billWaterNumber) {
        BillWaterNumber = billWaterNumber;
    }

    public Date getBillPaymentDate() {
        return BillPaymentDate;
    }

    public void setBillPaymentDate(Date billPaymentDate) {
        BillPaymentDate = billPaymentDate;
    }

    public double getBIllTotal() {
        return BillTotal;
    }

    public void setBIllTotal(double BillTotal) {
        this.BillTotal = BillTotal;
    }

    public Bill( com.example.qunlphngtr.Model.Room room, String billCustomerName, String billDateBegin,String billDateEnd, int contractID, int billElectricNumber, int billWaterNumber, Date billPaymentDate, double billTotal) {
        Room = room;
        BillCustomerName = billCustomerName;
        BillDateBegin = billDateBegin;
        BillDateEnd = billDateEnd;
        ContractID = contractID;
        BillElectricNumber = billElectricNumber;
        BillWaterNumber = billWaterNumber;
        BillPaymentDate = billPaymentDate;
        BillTotal = billTotal;
    }

    public Bill() {
    }
}
