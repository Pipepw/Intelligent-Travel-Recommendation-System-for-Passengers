package com.newweather.intelligenttravel.Entity;

public class TrueSubway {
    public String totaldistance;//总距离
    public String totalduration;//总用时
    public String totalprice;//总价格
    public String tiptype;//方案类型
    public String arrivetime;//到达时间

//    @SerializedName("vehicles")
//    public List<String>VehiclesList;//换乘车列表
//
//    @SerializedName("steps")
//    public List<String >StepsList;//步骤


    public void setArrivetime(String arrivetime) {
        this.arrivetime = arrivetime;
    }

    public String getArrivetime() {
        return arrivetime;
    }

    public void setTiptype(String tiptype) {
        this.tiptype = tiptype;
    }

    public String getTiptype() {
        return tiptype;
    }

    public void setTotaldistance(String totaldistance) {
        this.totaldistance = totaldistance;
    }

    public String getTotaldistance() {
        return totaldistance;
    }

    public void setTotalduration(String totalduration) {
        this.totalduration = totalduration;
    }

    public String getTotalduration() {
        return totalduration;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getTotalprice() {
        return totalprice;
    }
}
