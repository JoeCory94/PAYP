package com.joe.payp;

import java.io.Serializable;

public class ListObject implements Serializable{

    private String paymentID;
    private String date;
    private String cost;
    private String startTime;
    private String endTime;

    public String getPaymentID() {
        return paymentID;
    }

    public String getDate() {
        return date;
    }

    public String getCost() {
        return cost;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public ListObject(String paymentID, String date, String cost, String startTime, String endTime) {
        this.paymentID = paymentID;
        this.date = date;
        this.cost = cost;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}