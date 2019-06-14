package com.newweather.intelligenttravel.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.newweather.intelligenttravel.Entity.Flight;
import com.newweather.intelligenttravel.Entity.TrueTrain;
import com.newweather.intelligenttravel.db.City;
import com.newweather.intelligenttravel.db.Province;


import org.litepal.LitePal;

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
    private TrueTrain time_train = new TrueTrain();
    private TrueTrain fare_train = new TrueTrain();
    private List<Province> provinceList;
    private List<City> cityList;
    //AirportMap对应所有的机场，key是城市名，list为该城市所有的机场，所以调用的时候，要用循环的方式，AirportMap(CityName).size()
    private Map<String,List<String>> AirportMap = FlightUtil.GetAllAirport();
    private String use_time_min = "12:22";
    private String fare_min = "10000";
    private Handler myHandler;

    /**
     * 总的调用方法
     */
    public void GetPlan(Handler mHanlder, String StartCity, String EndCity, String Date, String Time){
        provinceList = LitePal.findAll(Province.class);
        AllFlight(StartCity,EndCity,Date,Time);
        AllTrain(mHanlder,StartCity,EndCity,Date,Time);
    }
    /**
     * 全程飞机的情况，只考虑初末城市
     * 先获取交通工具的实例，然后获取其花费的时间以及费用，然后分别于最小的进行比较
     */
    public void AllFlight(String StartCity, String EndCity, String Date, String Time){
        Flight flight = FlightUtil.GetFligt(StartCity, EndCity, Date, Time);
        if(flight!=null){
            String useTime = SomeUtil.getTime(flight.getEndTime(),flight.getStartTime());
            String fare = flight.getFare();
            Log.d(TAG, "handleMessage: kkk flight  fare = " + fare);
            Log.d(TAG, "handleMessage: kkk flight  usetime = " + useTime);
//        getTime方法，前小后大返回false
            if(SomeUtil.getTime(useTime,getUsetime_min()).equals("false")){
//            在这里面将flight的信息获取下来，最后通过set来将全局的flight设置为该flight
                flight.setChoose_flag(1);
                flight.setOrder_flag(1);
                time_train.setChoose_flag(0);
                time_flight = flight;
                setUsetime_min(useTime);
            }
            if(Float.parseFloat(flight.getFare())<Float.parseFloat(fare_min)){
                flight.setChoose_flag(1);
                flight.setOrder_flag(1);
                fare_train.setChoose_flag(0);
                fare_flight = flight;
                setFare_min(fare);
            }
        }

    }

    /**
     * 全程火车的情况
     * @param StartCity
     * @param EndCity
     * @param Date
     * @param Time
     */
    @SuppressLint("HandlerLeak")
    public void AllTrain(Handler mHanlder,String StartCity, String EndCity, String Date, String Time){
        myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1){
                    TrueTrain train =  AnotherGet.gettrain();
                    String useTime = SomeUtil.getTime(train.getArrivaltime(),train.getDeparturetime());
                    String fare = train.getPriceed();
                    Log.d(TAG, "handleMessage: kkk train  fare = " + fare);
                    Log.d(TAG, "handleMessage: kkk train  usetime = " + useTime);
                    if(SomeUtil.getTime(useTime,getUsetime_min()).equals("false")){
                        train.setChoose_flag(1);
                        train.setOrder_flag(1);
                        time_flight.setChoose_flag(0);
                        time_train = train;
                        setUsetime_min(useTime);
                    }
                    if(Float.parseFloat(fare)<Float.parseFloat(fare_min)){
                        train.setChoose_flag(1);
                        train.setOrder_flag(1);
                        fare_flight.setChoose_flag(0);
                        fare_train = train;
                        setFare_min(fare);
                    }
                    Message message = new Message();
                    message.what = 1;
                    mHanlder.sendMessage(message);
                }
            }
        };
        AnotherGet.getTrain(myHandler, StartCity, EndCity, Date, Time);
    }

    /**
     * 先火车后飞机的情况
     */
    public void TrainToFlight(Handler mHandler,String StartCity, String EndCity, String Date, String Time){

    }

    /**
     * 先飞机后火车的情况
     * @return
     */
    public void FlightToTrain(Handler mHandler,String StartCity, String EndCity, String Date, String Time){
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
                                    String useTime = SomeUtil.getTime(train.getArrivaltime(),Time);
                                    String fare = flight.getFare();
                                }
                            }
                        };
                        AnotherGet.getTrain(myHandler,city.getCityName(),EndCity,Date,flight.getEndTime());
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

    public TrueTrain getTime_train() {
        return time_train;
    }

    public void setTime_train(TrueTrain time_train) {
        this.time_train = time_train;
    }

    public TrueTrain getFare_train() {
        return fare_train;
    }

    public void setFare_train(TrueTrain fare_train) {
        this.fare_train = fare_train;
    }
}
