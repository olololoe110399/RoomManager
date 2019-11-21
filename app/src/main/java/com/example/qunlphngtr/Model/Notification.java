package com.example.qunlphngtr.Model;

public class Notification {
    private String Message;
    private boolean Status;

    public Notification(String message, boolean status) {
        Message = message;
        Status = status;
    }

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
