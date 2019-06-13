package com.newweather.intelligenttravel.Gson;

//subway中的result

public class Result1 {

    public String totaldistance;//总距离
    public String totalduration;//总用时
    public String totalprice;//总价格
    public String tiptype;//方案类型
    public String arrivetime;//到达时间

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalduration(String totalduration) {
        this.totalduration = totalduration;
    }

    public String getTotalduration() {
        return totalduration;
    }

    public void setTotaldistance(String totaldistance) {
        this.totaldistance = totaldistance;
    }

    public String getTotaldistance() {
        return totaldistance;
    }

    // 解析出现错误
//    @SerializedName("vehicles")
//    public List<String>VehiclesList;//换乘车列表
//
//    @SerializedName("steps")
//    public List<String >StepsList;//步骤


}
