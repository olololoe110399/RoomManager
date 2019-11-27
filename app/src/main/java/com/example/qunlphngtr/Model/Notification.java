package com.example.qunlphngtr.Model;

public class Notification {
    private String Message;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Notification(String message, String date, boolean status) {
        Message = message;
        this.date = date;
        Status = status;
    }

    private boolean Status;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public Notification() {
    }
}
