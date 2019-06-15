package com.newweather.intelligenttravel.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.newweather.intelligenttravel.Entity.Flight;
import com.newweather.intelligenttravel.Entity.Segments;
import com.newweather.intelligenttravel.Entity.TrueSubway;
import com.newweather.intelligenttravel.Entity.TrueTrain;
import com.newweather.intelligenttravel.db.City;
import com.newweather.intelligenttravel.db.Province;


import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.litepal.LitePalBase.TAG;

/**
 * 获取最终的规划方法
 */
public class PlanUtil {
//    分别对应时间和费用最优解
    private Flight time_flight = new Flight();
    private Flight fare_flight = new Flight();
    private List<Segments> time_train;
    private List<Segments> fare_train;
    private List<Province> provinceList;
    private List<City> cityList;
    //AirportMap对应所有的机场，key是城市名，list为该城市所有的机场，所以调用的时候，要用循环的方式，AirportMap.get(CityName).size()
    private Map<String,List<String>> AirportMap = FlightUtil.GetAllAirport();
    private String use_time_min = "24:22";
    private String fare_min = "100000";
    private Handler myHandler;

    /**
     * 总的调用方法
     */
    public void GetPlan(Context context, Handler mHandler, String StartCity, String EndCity, String Date, String Time){
        provinceList = LitePal.findAll(Province.class);
        AllFlight(StartCity,EndCity,Date,Time);
        AllTrain(context,mHandler,StartCity,EndCity,Date,Time);
        TrainToFlight(context,mHandler,StartCity,EndCity,Date,Time);
    }
    /**
     * 全程飞机的情况，只考虑初末城市
     * 先获取交通工具的实例，然后获取其花费的时间以及费用，然后分别于最小的进行比较
     */
    public void AllFlight(String StartCity, String EndCity, String Date, String Time){
        Flight flight = FlightUtil.GetFligt(StartCity, EndCity, Date, SomeUtil.AddTime(Time,"01:30"));
        if(flight.getStartTime()!=null){
            String useTime = SomeUtil.getTime(flight.getEndTime(),flight.getStartTime());
            String fare = flight.getFare();
//        getTime方法，前小后大返回false
            if(SomeUtil.getTime(useTime,use_time_min).equals("false")){
//            在这里面将flight的信息获取下来，最后通过set来将全局的flight设置为该flight
                flight.setChoose_flag(1);
                if(time_train!=null){
                    time_train.get(0).setChoose_flag(0);
                }
                flight.setOrder_flag(1);
                time_flight = flight;
                setUsetime_min(useTime);
            }
            if(Float.parseFloat(flight.getFare())<Float.parseFloat(fare_min)){
                flight.setChoose_flag(1);
                if(fare_train!=null){
                    fare_train.get(0).setChoose_flag(0);
                }
                flight.setOrder_flag(1);
                fare_flight = flight;
                fare_min = fare;
            }
        }

    }

    /**
     * 全程火车的情况
     */
    @SuppressLint("HandlerLeak")
    public void AllTrain(Context context, Handler mHandler,String StartCity, String EndCity, String Date, String Time){
        myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1){
                    List<Segments> train =  AnotherGet.getTrainRouteList();
                    String startime = train.get(0).railway.departure_stop.departure_time;
                    String endtime = train.get(train.size()-1).railway.arrival_stop.arrival_time;
                    String usetime = SomeUtil.getTime(endtime,startime);
                    String fare = train.get(0).cost;
                    if(SomeUtil.getTime(usetime,use_time_min).equals("false")){
                        use_time_min = usetime;
                        train.get(0).setChoose_flag(1);
                        time_flight.setChoose_flag(0);
                        train.get(0).setOrder_flag(1);
                        time_train = train;
                    }
                    if(Float.parseFloat(fare)<Float.parseFloat(fare_min)){
                        fare_min = fare;
                        train.get(0).setChoose_flag(1);
                        fare_flight.setChoose_flag(0);
                        train.get(0).setOrder_flag(1);
                        fare_train = train;
                    }
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);
                }
            }
        };
        AnotherGet.getTrain(context, myHandler, StartCity, EndCity, Date, Time);
    }

    /**
     * 先火车后飞机的情况
     */
    @SuppressLint("HandlerLeak")
    public void TrainToFlight(Context context, Handler mHandler, String StartCity, String EndCity, String Date, String Time){

//        到达城市有飞机时，才能满足条件
        if(AirportMap.get(EndCity)!=null){
            for(Province province:provinceList){
                cityList = LitePal.where("provinceId = ?",String
                        .valueOf(province.getId())).find(City.class);
                for(City city : cityList){
                    if(city.getCityName().equals(StartCity)||city.getCityName().equals(EndCity)){
                        continue;
                    }
//                    接收火车发送的消息
                    myHandler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            if(msg.what == 1){
                                List<Segments> train =  AnotherGet.getTrainRouteList();
//                                接收地铁发送的消息
                                Handler subHandler = new Handler(){
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
//                                    获取地铁之后再继续往下执行
                                        if(msg.what == 2){
//                                        地铁花费的时间
                                            String subwaytime = AnotherGet.getsubwayy().totalduration;
//                                        飞机出发的时间，没有考虑超过一天的情况，多加1:30用于候机
                                            String flightstarttime = SomeUtil.AddTime(subwaytime,train.get(train.size()-1).railway.arrival_stop.arrival_time,"01:30");
                                            Flight flight = FlightUtil.GetFligt(StartCity,EndCity,Date,flightstarttime);
//                                        最初的出发的时间
                                            String startime = train.get(0).railway.departure_stop.departure_time;
                                            String endtime = flight.getEndTime();//飞机到达的时间
                                            String usetime = SomeUtil.getTime(endtime,startime);
                                            Float fare = Float.parseFloat(train.get(0).cost)+Float.parseFloat(flight.getFare());
                                            if(SomeUtil.getTime(usetime,use_time_min).equals("false")){
                                                use_time_min = usetime;
                                                train.get(0).setChoose_flag(1);
                                                train.get(0).setOrder_flag(1);
                                                flight.setChoose_flag(1);
                                                flight.setOrder_flag(2);
                                                time_train = train;
                                                time_flight = flight;
                                            }
                                            if(fare<Float.parseFloat(fare_min)){
                                                fare_min = String.valueOf(fare);
                                                train.get(0).setChoose_flag(1);
                                                train.get(0).setOrder_flag(1);
                                                flight.setChoose_flag(1);
                                                flight.setOrder_flag(2);
                                                fare_train = train;
                                                fare_flight = flight;
                                            }
//                                            Message message = new Message();
//                                            message.what = 1;
//                                            mHandler.sendMessage(message);
                                        }
                                    }
                                };
                                if(AirportMap.get(city.getCityName()).size()==1) {
                                    AnotherGet.getSubway(subHandler,city.getCityName(), city.getCityName(), train.get(train.size() - 1)
                                            .railway.arrival_stop.arrival_name, AirportMap.get(city.getCityName()).get(0));
                                }
                            }
                        }
                    };
//                从出发点到一个中转城市的火车
                    AnotherGet.getTrain(context,myHandler, StartCity, city.getCityName(), Date, Time);
                }
            }
        }

    }

    /**
     * 先飞机后火车的情况
     * @return
     */
    public void FlightToTrain(Context context, Handler mHandler,String StartCity, String EndCity, String Date, String Time){
        for(Province province:provinceList){
            cityList = LitePal.where("provinceId = ?",String.valueOf(province.getId())).find(City.class);
//            遍历所有城市，找出所有情况
            for(City city:cityList){
                if(!(city.getCityName().equals(StartCity)||city.getCityName().equals(EndCity))){
                    Flight flight = FlightUtil.GetFligt(StartCity,city.getCityName(),Date,Time);
                    if(flight!=null){
//                        获取火车的实例
                        @SuppressLint("HandlerLeak") Handler myHandler = new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                if(msg.what == 1){
                                    TrueTrain train = AnotherGet.gettrain();
//                                    String useTime = SomeUtil.getTime(train.getArrivaltime(),Time);
                                    String fare = flight.getFare();
                                }
                            }
                        };
                        AnotherGet.getTrain(context,myHandler,city.getCityName(),EndCity,Date,flight.getEndTime());
                    }
                }
            }
        }
    }
    



//    一系列与全局变量匹配的方法
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

    public List<Segments> getTime_train(){
        return time_train;
    }

    public void setTime_train(List<Segments> list){
        time_train = list;
    }

    public List<Segments> getFare_train(){
        return fare_train;
    }

}
