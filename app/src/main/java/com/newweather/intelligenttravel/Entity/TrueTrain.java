package com.newweather.intelligenttravel.Entity;

public class TrueTrain {
    private String typename;//类型
    private String station;//起始站
    private String endstation;//到达站
    private  String departuretime;//出发时间
    private  String arrivaltime;//到达时间
    private  String costtime;//花费时间
    private  String priceyd;//一等座价
    private  String priceed;//二等座价
    private int order_flag;
    private int choose_flag;

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getTypename() {
        return typename;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStation() {
        return station;
    }

    public void setEndstation(String endstation) {
        this.endstation = endstation;
    }

    public String getEndstation() {
        return endstation;
    }

    public void setDeparturetime(String departuretime) {
        this.departuretime = departuretime;
    }

    public String getDeparturetime() {
        return departuretime;
    }

    public void setArrivaltime(String arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    public String getArrivaltime() {
        return arrivaltime;
    }

    public void setCosttime(String costtime) {
        this.costtime = costtime;
    }

    public String getCosttime() {
        return costtime;
    }

    public void setPriceyd(String priceyd) {
        this.priceyd = priceyd;
    }

    public String getPriceed() {
        return priceyd;
    }

    public void setPriceed(String priceed) {
        this.priceed = priceed;
    }

    public String getPriceyd() {
        return priceed;
    }

    public void setOrder_flag(int order_flag) {
        this.order_flag = order_flag;
    }

    public int getOrder_flag() {
        return order_flag;
    }

    public void setChoose_flag(int choose_flag) {
        this.choose_flag = choose_flag;
    }

    public int getChoose_flag() {
        return choose_flag;
    }
}
