package com.joe.payp;

import java.io.Serializable;

public class ListObject implements Serializable{

    private Integer paymentID;
    private String date;
    private String cost;
    private String startTime;
    private String endTime;
    private String paid;

    public Integer getPaymentID() {
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

    public String getPaid() {
        return paid;
    }

    public ListObject(Integer paymentID, String date, String cost, String startTime, String endTime, String paid) {
        this.paymentID = paymentID;
        this.date = date;
        this.cost = cost;
        this.startTime = startTime;
        this.endTime = endTime;
        this.paid = paid;
    }
}