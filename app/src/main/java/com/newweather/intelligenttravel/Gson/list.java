package com.newweather.intelligenttravel.Gson;

//train中result中的list
public class list {
    //极速数据中的数组数据

    private String typename;//类型
    private String station;//起始站
    private String endstation;//到达站
    private  String departuretime;//出发时间
    private  String arrivaltime;//到达时间
    private  String costtime;//花费时间
    private  String priceyd;//一等座价
    private  String priceed;//二等座价

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
}
