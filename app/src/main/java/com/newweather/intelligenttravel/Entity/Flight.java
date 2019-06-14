package com.newweather.intelligenttravel.Entity;

public class Flight {
    private String StartTime;
    private String EndTime;
    private String StartStation;
    private String EndStation;
    private String Fare;
    private int order_flag;//1表示先，2表示后
    private int choose_flag;//0表示无，1表示有

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getStartStation() {
        return StartStation;
    }

    public void setStartStation(String startStation) {
        StartStation = startStation;
    }

    public String getEndStation() {
        return EndStation;
    }

    public void setEndStation(String endStation) {
        EndStation = endStation;
    }

    public String getFare() {
        return Fare;
    }

    public void setFare(String fare) {
        Fare = fare;
    }

    public int getOrder_flag() {
        return order_flag;
    }

    public void setOrder_flag(int order_flag) {
        this.order_flag = order_flag;
    }

    public int getChoose_flag() {
        return choose_flag;
    }

    public void setChoose_flag(int choose_flag) {
        this.choose_flag = choose_flag;
    }
}
