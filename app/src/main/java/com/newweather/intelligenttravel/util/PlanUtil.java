package com.newweather.intelligenttravel.util;

import com.newweather.intelligenttravel.Entity.Flight;
import com.newweather.intelligenttravel.Entity.Train;
import com.newweather.intelligenttravel.db.City;
import com.newweather.intelligenttravel.db.Province;

import org.litepal.LitePal;

import java.util.List;

/**
 * 获取最终的规划方法
 */
public class PlanUtil {
//    分别对应时间和费用最优解
    private Flight time_flight;
    private Flight fare_flight;
    private Train time_train;
    private Train fare_train;
    private List<Province> provinceList;
    private List<City> cityList;
    private String use_time_min;
    private String fare_min;

    public PlanUtil() {
    }

    /**
     * 总的调用方法
     */
    public void GetPlan(String StartCity, String EndCity, String Date, String Time){

    }
    /**
     * 全程飞机的情况
     */
    public void AllFlight(String StartCity, String EndCity, String Date, String Time){
        Flight flight = FlightUtil.GetFligt(StartCity, EndCity, Date, Time);
        String useTime = TimeUtil.getTime(flight.getEndTime(),flight.getStartTime());
        String fare = flight.getFare();
//        getTime方法，前小后大返回false
        if(TimeUtil.getTime(useTime,getUsetime_min()).equals("false")){
            flight.setChoose_flag(1);
            flight.setOrder_flag(1);
//            这里需补充：   train.set....
            setUsetime_min(useTime);
            setTime_flight(flight);
        }

    }

    public Flight getTime_flight() {
        return time_flight;
    }

    public void setTime_flight(Flight time_flight) {
        this.time_flight = time_flight;
    }

    public Flight getFare_flight() {
        return fare_flight;
    }

    public void setFare_flight(Flight fare_flight) {
        this.fare_flight = fare_flight;
    }

    public Train getTime_train() {
        return time_train;
    }

    public void setTime_train(Train time_train) {
        this.time_train = time_train;
    }

    public Train getFare_train() {
        return fare_train;
    }

    public void setFare_train(Train fare_train) {
        this.fare_train = fare_train;
    }

    public List<Province> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public String getUsetime_min() {
        return use_time_min;
    }

    public void setUsetime_min(String usetime_min) {
        this.use_time_min = usetime_min;
    }

    public String getFare_min() {
        return fare_min;
    }

    public void setFare_min(String fare_min) {
        this.fare_min = fare_min;
    }
}
