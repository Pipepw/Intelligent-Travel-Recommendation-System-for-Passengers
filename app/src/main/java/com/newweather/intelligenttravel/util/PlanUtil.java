package com.newweather.intelligenttravel.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.newweather.intelligenttravel.Entity.Flight;
import com.newweather.intelligenttravel.Entity.Segments;
import com.newweather.intelligenttravel.Entity.TrueTrain;
import com.newweather.intelligenttravel.db.City;
import com.newweather.intelligenttravel.db.Province;


import org.litepal.LitePal;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
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
    private Map<String,List<String>> startcity = new HashMap<>();
    private Map<String,List<String>> endcity = new HashMap<>();
    //AirportMap对应所有的机场，key是城市名，list为该城市所有的机场，所以调用的时候，要用循环的方式，AirportMap.get(CityName).size()
    private Map<String,String> AirportMap = FlightUtil.GetAllAirport();
    private Map<String,String> LalMap = SomeUtil.GetLaL();
    private Map<String,String> CodeMap = SomeUtil.getCodeMap();
    private String use_time_min = "224:22";
    private String fare_min = "1000000";
    final int[] endflag = {0};

    /**
     * 总的调用方法
     */
    public void GetPlan(Handler mHandler, String StartCity, String EndCity, String Date, String Time){
        getPlaneMap();
        @SuppressLint("HandlerLeak") Handler Ahandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    time_train = AnotherGet.getTrainRouteList();
                    fare_train = AnotherGet.getTrainRouteList();
                    Log.d(TAG, "handleMessage: kkk fare = " + time_train.get(0).cost);
                    Log.d(TAG, "handleMessage: kkk time = " + fare_train.get(0).usetime);
                    time_train.get(0).setOrder_flag(0);
                    time_train.get(0).setChoose_flag(0);
                    fare_train.get(0).setOrder_flag(0);
                    fare_train.get(0).setChoose_flag(0);
                    provinceList = LitePal.findAll(Province.class);
//        AllTrain(mHandler,StartCity,EndCity,Date,Time);
//                    for(int i=0;i<1;i++){
//                        AllFlight(StartCity,EndCity,Date,Time);
//                    }
                    TrainToFlight(mHandler,StartCity,EndCity,Date,Time);
                }
            }
        };
        AnotherGet.getTrain(LalMap,Ahandler,StartCity,EndCity,Date,Time);
    }
    /**
     * 全程飞机的情况，只考虑初末城市
     * 先获取交通工具的实例，然后获取其花费的时间以及费用，然后分别于最小的进行比较
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void AllFlight(String StartCity, String EndCity, String Date, String Time){
//        这里传入的Handler也是没用用的
        Handler unuseHandler = new Handler();
        Flight flight = FlightUtil.GetFlight(CodeMap,unuseHandler,StartCity, EndCity, Date, SomeUtil.AddTime(Time,"01:30"));
        if(flight.getStartTime()!=null){
            String useTime = SomeUtil.getTime(flight.getEndTime(),flight.getStartTime());
            if(useTime.equals("false")){
                useTime = SomeUtil.getTime(SomeUtil.AddTime(flight.getEndTime(),"24:00"),flight.getStartTime());
            }
            String fare = flight.getFare();
//        getTime方法，前小后大返回false
            if(SomeUtil.getTime(useTime,use_time_min).equals("false")){
//            在这里面将flight的信息获取下来，最后通过set来将全局的flight设置为该flight
                Log.d(TAG, "AllFlight: kkk flight usetime = " + useTime);
                Log.d(TAG, "AllFlight: kkk flight usetime_min = " + use_time_min);
                flight.setChoose_flag(1);
                time_train.get(0).setChoose_flag(0);
                flight.setOrder_flag(1);
                time_flight = flight;
                setUsetime_min(useTime);
            }
            if(Float.parseFloat(flight.getFare())<Float.parseFloat(fare_min)){
                Log.d(TAG, "AllFlight: kkk flight fare = " + flight.getFare());
                Log.d(TAG, "AllFlight: kkk flight fare_min = " + fare_min);
                flight.setChoose_flag(1);
                fare_train.get(0).setChoose_flag(0);
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
    public void AllTrain(Handler mHandler,String StartCity, String EndCity, String Date, String Time){
//        接收来自火车的信息
        Handler AllTrainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1){
//                    有可能没有火车，直接坐公交就到了
                    List<Segments> train =  AnotherGet.getTrainRouteList();
                    if(train!=null){
//                        这里需要修改
                        String usetime = train.get(0).usetime;
                        String fare = train.get(0).cost;
                        if(SomeUtil.getTime(usetime,use_time_min).equals("false")){
                            Log.d(TAG, "handleMessage: kkk train usetime = " + usetime);
                            Log.d(TAG, "handleMessage: kkk train usetime_min = " + use_time_min);
                            use_time_min = usetime;
                            train.get(0).setChoose_flag(1);
                            time_flight.setChoose_flag(0);
                            train.get(0).setOrder_flag(1);
                            time_train = train;
                        }
                        if(Float.parseFloat(fare)<Float.parseFloat(fare_min)){
                            Log.d(TAG, "handleMessage: kkk train fare = " + fare);
                            Log.d(TAG, "handleMessage: kkk train fare_min = " + fare_min);
                            fare_min = fare;
                            train.get(0).setChoose_flag(1);
                            fare_flight.setChoose_flag(0);
                            train.get(0).setOrder_flag(1);
                            fare_train = train;
                        }
                    }
                }
            }
        };
        AnotherGet.getTrain(LalMap, AllTrainHandler, StartCity, EndCity, Date, Time);
    }

    /**
     * 先火车后飞机的情况
     */
    @SuppressLint("HandlerLeak")
    public void TrainToFlight(Handler mHandler, String StartCity, String EndCity, String Date, String Time){


        final Handler[] TrainFlightHandler = new Handler[1];
        final Handler[] subHandler = new Handler[1];
        final Handler[] flightHandler = new Handler[1];
        //用于查看有多少个
        final int[] numflag = {0};
//        到达城市有飞机时，才能满足条件
        if(AirportMap.get(EndCity)!=null){
            for(Province province:provinceList){
                cityList = LitePal.where("provinceId = ?",String
                        .valueOf(province.getId())).find(City.class);
                for(City city : cityList){
//                    从这里开始，就是单次的比较
                    endflag[0]=0;
                    if(isPlane(city.getCityName(),EndCity)){
                        if(AirportMap.get(city.getCityName())!=null){
                            final int[] day = new int[1];
                            if(city.getCityName().equals(StartCity)||city.getCityName().equals(EndCity)){
                                continue;
                            }
//                    接收火车发送的消息，收到消息之后才继续往下执行
                            TrainFlightHandler[0] = new Handler(){
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    if(msg.what == 1){
                                        Log.d(TAG, "handleMessage: kkk  Get it");
                                        Bundle trainbundle = msg.getData();
                                        ArrayList<Segments> train = trainbundle.getParcelableArrayList("train");
                                        if(train!=null){
                                            Log.d(TAG, "handleMessage: kkk size = " + train.size());
                                            Log.d(TAG, "handleMessage: kkk train out " + train.get(0).cost);
//                                  接收地铁发送的消息
                                            subHandler[0] = new Handler(){
                                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                                @Override
                                                public void handleMessage(Message msg) {
                                                    super.handleMessage(msg);
//                                                  获取地铁之后再继续往下执行
                                                    if(msg.what == 2){
                                                        numflag[0] += 1;
                                                        Log.d(TAG, "handleMessage: kkk num = " + numflag[0]);
//                                          \           地铁花费的时间
                                                        Bundle subbundle = msg.getData();
                                                        String subwaytime = SomeUtil.Transtime(subbundle.getInt("subway"));
//                                                         飞机出发的时间，多加1:30用于候机，这两个时间是不一样的，那么每个城市的输出结果应该不同
                                                        Log.d(TAG, "handleMessage: kkk suabwaytime = " + subwaytime);
                                                        Log.d(TAG, "handleMessage: kkk train time = " + train.get(train.size()-1).railway.arrival_stop.arrival_time);
                                                        String flightstarttime = SomeUtil.AddTime(subwaytime,train.get(train.size()-1).railway.arrival_stop.arrival_time,"01:30");
//                                                  如果最后的时间超过24小时的话
                                                        if(SomeUtil.getTime("24:00",flightstarttime).equals("false")){
                                                            String hour = flightstarttime.substring(0,2);
                                                            day[0] = Integer.parseInt(hour)/24;
                                                            String tempTime = day[0]*24+":"+"00";
                                                            Log.d(TAG, "handleMessage: kkktempTime = " +tempTime);
//                                                      用末减的方式，得到减去多余时间的结果
                                                            flightstarttime = SomeUtil.getTime(flightstarttime,tempTime);
                                                            Log.d(TAG, "handleMessage: kkkflightstarttime " + flightstarttime);
                                                        }
                                                        Log.d(TAG, "handleMessage: kkk StartCity = " + city.getCityName());
                                                        Log.d(TAG, "handleMessage: kkk EndCity = " + EndCity);
                                                        Log.d(TAG, "handleMessage: kkk Date = " + Date);
                                                        Log.d(TAG, "handleMessage: kkk Time = " + flightstarttime);
//                                                        用来接收最后一个城市传来的消息，之后发消息到主线程执行任务，
//                                                        需要接收消息的地方都没执行，说明确实是没有发送
                                                        flightHandler[0] = new Handler(){
                                                            @Override
                                                            public void handleMessage(Message msg) {
                                                                super.handleMessage(msg);
                                                                if(msg.what == 1){
                                                                    Log.d(TAG, "handleMessage: out the flight kkk");
                                                                    Bundle flightbundle = msg.getData();
                                                                    Flight flight = (Flight)flightbundle.getSerializable("flight");
                                                                    Log.d(TAG, "handleMessage: kkk out the flight StartCity = " + city.getCityName());
                                                                    Log.d(TAG, "handleMessage: kkk endstation is empty? = " + flight.getEndStation());
                                                                    //为什么没有执行后面的，用上面那个log进行测试，是否为空
                                                                    if(flight.getEndStation()!=null){
                                                                        Log.d(TAG, "handleMessage: kkk station start = " + flight.getStartStation());
                                                                        Log.d(TAG, "handleMessage: kkk station end = " + flight.getEndStation());
                                                                        Log.d(TAG, "handleMessage: end time flight = "+ flight.getStartTime());
                                                                        String flightUseTime = SomeUtil.getTime(flight.getEndTime(),flight.getStartTime());
                                                                        if(flightUseTime.equals("false")){
                                                                            flightUseTime = SomeUtil.getTime(SomeUtil.AddTime(flight.getEndTime(),"24:00")
                                                                                    ,flight.getStartTime());
                                                                        }
                                                                        String usetime = SomeUtil.AddTime(flightUseTime,subwaytime,train.get(0).usetime);
                                                                        Float fare = Float.parseFloat(train.get(0).cost)+Float.parseFloat(flight.getFare());
                                                                        if(SomeUtil.getTime(usetime,use_time_min).equals("false")){
                                                                            Log.d(TAG, "handleMessage: kkk train flight usetime = " + usetime);
                                                                            Log.d(TAG, "handleMessage: kkk train flight usetime_min = " + use_time_min);
                                                                            use_time_min = usetime;
                                                                            train.get(0).setChoose_flag(1);
                                                                            train.get(0).setOrder_flag(1);
                                                                            flight.setChoose_flag(1);
                                                                            flight.setOrder_flag(2);
                                                                            time_train = train;
                                                                            time_flight = flight;
                                                                        }
                                                                        if(fare<Float.parseFloat(fare_min)){
                                                                            Log.d(TAG, "handleMessage: kkk train flight fare = " + fare);
                                                                            Log.d(TAG, "handleMessage: kkk train flight fare_min = " + fare_min);
                                                                            fare_min = String.valueOf(fare);
                                                                            train.get(0).setChoose_flag(1);
                                                                            train.get(0).setOrder_flag(1);
                                                                            flight.setChoose_flag(1);
                                                                            flight.setOrder_flag(2);
                                                                            fare_train = train;
                                                                            fare_flight = flight;
                                                                        }

                                                                    }
                                                                }
                                                                //当线程全部结束之后，发送消息到主线程
                                                                if(msg.what == 2){
                                                                    Message message = new Message();
                                                                    message.what = 1;
                                                                    mHandler.sendMessage(message);
                                                                }
                                                            }
                                                        };
                                                        //这里获取飞机
                                                        try {
                                                            Thread.sleep(3000);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                        FlightUtil.GetFlight(CodeMap,flightHandler[0],city.getCityName(),EndCity,Date,flightstarttime);
                                                    }
                                                }
                                            };
//                                      获取地铁的时间
                                            AnotherGet.GetSubway(subHandler[0],city.getCityName(), train.get(train.size() - 1)
                                                    .railway.arrival_stop.arrival_name, AirportMap.get(city.getCityName()));
                                        }
                                    }
                                    if(msg.what == 3){
                                        endflag[0] = 1;
                                    }
                                }
                            };
                            //从出发点到一个中转城市的火车，飞机返回之后再获取下一个火车
                            AnotherGet.getTrain(LalMap,TrainFlightHandler[0], StartCity, city.getCityName(), Date, Time);
                        }
                    }
                }
            }
        }
    }

    /**
     * 先飞机后火车的情况
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void FlightToTrain(Context context, Handler mHandler, String StartCity, String EndCity, String Date, String Time){
        for(Province province:provinceList){
            cityList = LitePal.where("provinceId = ?",String.valueOf(province.getId())).find(City.class);
//            遍历所有城市，找出所有情况
            for(City city:cityList){
                if(!(city.getCityName().equals(StartCity)||city.getCityName().equals(EndCity))){
//                    这里传入Handler是没有用的，想办法让他没用
                    Handler unuseHandler = new Handler();
                    Flight flight = FlightUtil.GetFlight(CodeMap,unuseHandler,StartCity,city.getCityName(),Date,Time);
                    if(flight!=null){
//                        获取火车的实例
                        @SuppressLint("HandlerLeak") Handler FlightTrainHandler = new Handler(){
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
                        AnotherGet.getTrain(LalMap,FlightTrainHandler,city.getCityName(),EndCity,Date,flight.getEndTime());
                    }
                }
            }
        }
    }


    /**
     * 判断两个城市之间有没有航班
     * @return
     */
    public boolean isPlane(String Start,String End){
        List<String> allToCity = startcity.get(Start);
        if(allToCity!=null){
            for(int i=0;i<allToCity.size();i++){
                if(allToCity.get(i).equals(End))
                    return true;
            }
        }
        return false;
    }

    /**
     * 获取城市之间的航班信息
     * @return
     */
    public void getPlaneMap(){
        String all = "北京-长春   北京-长沙   北京-常州   北京-成都   北京-重庆   北京-大连   北京-大理   北京-广州   北京-桂林   北京-贵阳   北京-哈尔滨   北京-海口   北京-杭州   北京-合肥   北京-香港   北京-黄山   北京-呼和浩特   北京-吉林   北京-济南   北京-泉州   北京-福州   北京-昆明   北京-兰州   北京-拉萨   北京-连云港   北京-丽江   北京-洛阳   北京-澳门   北京-南昌   北京-南京   北京-南宁   北京-宁波   北京-青岛   北京-秦皇岛   北京-三亚   北京-上海   北京-深圳   北京-沈阳   北京-石家庄   北京-苏州   北京-太原   北京-天津   北京-温州   北京-武汉   北京-乌鲁木齐   北京-厦门   北京-西安   北京-西宁   北京-徐州   北京-烟台   北京-银川   北京-义乌   北京-郑州   北京-珠海   北京-台北   北京-桃园   北京-无锡   北京-烟台   北京-包头   " +
                " 长春-北京   长春-长沙   长春-常州   长春-成都   长春-重庆   长春-大连   长春-大理   长春-广州   长春-桂林   长春-贵阳   长春-哈尔滨   长春-海口   长春-杭州   长春-合肥   长春-香港   长春-黄山   长春-呼和浩特   长春-吉林   长春-济南   长春-泉州   长春-福州   长春-昆明   长春-兰州   长春-拉萨   长春-连云港   长春-丽江   长春-洛阳   长春-澳门   长春-南昌   长春-南京   长春-南宁   长春-宁波   长春-青岛   长春-秦皇岛   长春-三亚   长春-上海   长春-深圳   长春-沈阳   长春-石家庄   长春-苏州   长春-太原   长春-天津   长春-温州   长春-武汉   长春-乌鲁木齐   长春-厦门   长春-西安   长春-西宁   长春-徐州   长春-烟台   长春-银川   长春-义乌   长春-郑州   长春-珠海   长春-台北   长春-桃园   长春-无锡   长春-烟台   长春-包头   " +
                " 长沙-北京   长沙-长春   长沙-常州   长沙-成都   长沙-重庆   长沙-大连   长沙-大理   长沙-广州   长沙-桂林   长沙-贵阳   长沙-哈尔滨   长沙-海口   长沙-杭州   长沙-合肥   长沙-香港   长沙-黄山   长沙-呼和浩特   长沙-吉林   长沙-济南   长沙-泉州   长沙-福州   长沙-昆明   长沙-兰州   长沙-拉萨   长沙-连云港   长沙-丽江   长沙-洛阳   长沙-澳门   长沙-南昌   长沙-南京   长沙-南宁   长沙-宁波   长沙-青岛   长沙-秦皇岛   长沙-三亚   长沙-上海   长沙-深圳   长沙-沈阳   长沙-石家庄   长沙-苏州   长沙-太原   长沙-天津   长沙-温州   长沙-武汉   长沙-乌鲁木齐   长沙-厦门   长沙-西安   长沙-西宁   长沙-徐州   长沙-烟台   长沙-银川   长沙-义乌   长沙-郑州   长沙-珠海   长沙-台北   长沙-桃园   长沙-无锡   长沙-烟台   长沙-包头   " +
                " 常州-北京   常州-长春   常州-长沙   常州-成都   常州-重庆   常州-大连   常州-大理   常州-广州   常州-桂林   常州-贵阳   常州-哈尔滨   常州-海口   常州-杭州   常州-合肥   常州-香港   常州-黄山   常州-呼和浩特   常州-吉林   常州-济南   常州-泉州   常州-福州   常州-昆明   常州-兰州   常州-拉萨   常州-连云港   常州-丽江   常州-洛阳   常州-澳门   常州-南昌   常州-南京   常州-南宁   常州-宁波   常州-青岛   常州-秦皇岛   常州-三亚   常州-上海   常州-深圳   常州-沈阳   常州-石家庄   常州-苏州   常州-太原   常州-天津   常州-温州   常州-武汉   常州-乌鲁木齐   常州-厦门   常州-西安   常州-西宁   常州-徐州   常州-烟台   常州-银川   常州-义乌   常州-郑州   常州-珠海   常州-台北   常州-桃园   常州-无锡   常州-烟台   常州-包头   " +
                " 成都-北京   成都-长春   成都-长沙   成都-常州   成都-重庆   成都-大连   成都-大理   成都-广州   成都-桂林   成都-贵阳   成都-哈尔滨   成都-海口   成都-杭州   成都-合肥   成都-香港   成都-黄山   成都-呼和浩特   成都-吉林   成都-济南   成都-泉州   成都-福州   成都-昆明   成都-兰州   成都-拉萨   成都-连云港   成都-丽江   成都-洛阳   成都-澳门   成都-南昌   成都-南京   成都-南宁   成都-宁波   成都-青岛   成都-秦皇岛   成都-三亚   成都-上海   成都-深圳   成都-沈阳   成都-石家庄   成都-苏州   成都-太原   成都-天津   成都-温州   成都-武汉   成都-乌鲁木齐   成都-厦门   成都-西安   成都-西宁   成都-徐州   成都-烟台   成都-银川   成都-义乌   成都-郑州   成都-珠海   成都-台北   成都-桃园   成都-无锡   成都-烟台   成都-包头   " +
                " 重庆-北京   重庆-长春   重庆-长沙   重庆-常州   重庆-成都   重庆-大连   重庆-大理   重庆-广州   重庆-桂林   重庆-贵阳   重庆-哈尔滨   重庆-海口   重庆-杭州   重庆-合肥   重庆-香港   重庆-黄山   重庆-呼和浩特   重庆-吉林   重庆-济南   重庆-泉州   重庆-福州   重庆-昆明   重庆-兰州   重庆-拉萨   重庆-连云港   重庆-丽江   重庆-洛阳   重庆-澳门   重庆-南昌   重庆-南京   重庆-南宁   重庆-宁波   重庆-青岛   重庆-秦皇岛   重庆-三亚   重庆-上海   重庆-深圳   重庆-沈阳   重庆-石家庄   重庆-苏州   重庆-太原   重庆-天津   重庆-温州   重庆-武汉   重庆-乌鲁木齐   重庆-厦门   重庆-西安   重庆-西宁   重庆-徐州   重庆-烟台   重庆-银川   重庆-义乌   重庆-郑州   重庆-珠海   重庆-台北   重庆-桃园   重庆-无锡   重庆-烟台   重庆-包头   " +
                " 大连-北京   大连-长春   大连-长沙   大连-常州   大连-成都   大连-重庆   大连-大理   大连-广州   大连-桂林   大连-贵阳   大连-哈尔滨   大连-海口   大连-杭州   大连-合肥   大连-香港   大连-黄山   大连-呼和浩特   大连-吉林   大连-济南   大连-泉州   大连-福州   大连-昆明   大连-兰州   大连-拉萨   大连-连云港   大连-丽江   大连-洛阳   大连-澳门   大连-南昌   大连-南京   大连-南宁   大连-宁波   大连-青岛   大连-秦皇岛   大连-三亚   大连-上海   大连-深圳   大连-沈阳   大连-石家庄   大连-苏州   大连-太原   大连-天津   大连-温州   大连-武汉   大连-乌鲁木齐   大连-厦门   大连-西安   大连-西宁   大连-徐州   大连-烟台   大连-银川   大连-义乌   大连-郑州   大连-珠海   大连-台北   大连-桃园   大连-无锡   大连-烟台   大连-包头   " +
                " 大理-北京   大理-长春   大理-长沙   大理-常州   大理-成都   大理-重庆   大理-大连   大理-广州   大理-桂林   大理-贵阳   大理-哈尔滨   大理-海口   大理-杭州   大理-合肥   大理-香港   大理-黄山   大理-呼和浩特   大理-吉林   大理-济南   大理-泉州   大理-福州   大理-昆明   大理-兰州   大理-拉萨   大理-连云港   大理-丽江   大理-洛阳   大理-澳门   大理-南昌   大理-南京   大理-南宁   大理-宁波   大理-青岛   大理-秦皇岛   大理-三亚   大理-上海   大理-深圳   大理-沈阳   大理-石家庄   大理-苏州   大理-太原   大理-天津   大理-温州   大理-武汉   大理-乌鲁木齐   大理-厦门   大理-西安   大理-西宁   大理-徐州   大理-烟台   大理-银川   大理-义乌   大理-郑州   大理-珠海   大理-台北   大理-桃园   大理-无锡   大理-烟台   大理-包头   " +
                " 广州-北京   广州-长春   广州-长沙   广州-常州   广州-成都   广州-重庆   广州-大连   广州-大理   广州-桂林   广州-贵阳   广州-哈尔滨   广州-海口   广州-杭州   广州-合肥   广州-香港   广州-黄山   广州-呼和浩特   广州-吉林   广州-济南   广州-泉州   广州-福州   广州-昆明   广州-兰州   广州-拉萨   广州-连云港   广州-丽江   广州-洛阳   广州-澳门   广州-南昌   广州-南京   广州-南宁   广州-宁波   广州-青岛   广州-秦皇岛   广州-三亚   广州-上海   广州-深圳   广州-沈阳   广州-石家庄   广州-苏州   广州-太原   广州-天津   广州-温州   广州-武汉   广州-乌鲁木齐   广州-厦门   广州-西安   广州-西宁   广州-徐州   广州-烟台   广州-银川   广州-义乌   广州-郑州   广州-珠海   广州-台北   广州-桃园   广州-无锡   广州-烟台   广州-包头   " +
                " 桂林-北京   桂林-长春   桂林-长沙   桂林-常州   桂林-成都   桂林-重庆   桂林-大连   桂林-大理   桂林-广州   桂林-贵阳   桂林-哈尔滨   桂林-海口   桂林-杭州   桂林-合肥   桂林-香港   桂林-黄山   桂林-呼和浩特   桂林-吉林   桂林-济南   桂林-泉州   桂林-福州   桂林-昆明   桂林-兰州   桂林-拉萨   桂林-连云港   桂林-丽江   桂林-洛阳   桂林-澳门   桂林-南昌   桂林-南京   桂林-南宁   桂林-宁波   桂林-青岛   桂林-秦皇岛   桂林-三亚   桂林-上海   桂林-深圳   桂林-沈阳   桂林-石家庄   桂林-苏州   桂林-太原   桂林-天津   桂林-温州   桂林-武汉   桂林-乌鲁木齐   桂林-厦门   桂林-西安   桂林-西宁   桂林-徐州   桂林-烟台   桂林-银川   桂林-义乌   桂林-郑州   桂林-珠海   桂林-台北   桂林-桃园   桂林-无锡   桂林-烟台   桂林-包头   " +
                " 贵阳-北京   贵阳-长春   贵阳-长沙   贵阳-常州   贵阳-成都   贵阳-重庆   贵阳-大连   贵阳-大理   贵阳-广州   贵阳-桂林   贵阳-哈尔滨   贵阳-海口   贵阳-杭州   贵阳-合肥   贵阳-香港   贵阳-黄山   贵阳-呼和浩特   贵阳-吉林   贵阳-济南   贵阳-泉州   贵阳-福州   贵阳-昆明   贵阳-兰州   贵阳-拉萨   贵阳-连云港   贵阳-丽江   贵阳-洛阳   贵阳-澳门   贵阳-南昌   贵阳-南京   贵阳-南宁   贵阳-宁波   贵阳-青岛   贵阳-秦皇岛   贵阳-三亚   贵阳-上海   贵阳-深圳   贵阳-沈阳   贵阳-石家庄   贵阳-苏州   贵阳-太原   贵阳-天津   贵阳-温州   贵阳-武汉   贵阳-乌鲁木齐   贵阳-厦门   贵阳-西安   贵阳-西宁   贵阳-徐州   贵阳-烟台   贵阳-银川   贵阳-义乌   贵阳-郑州   贵阳-珠海   贵阳-台北   贵阳-桃园   贵阳-无锡   贵阳-烟台   贵阳-包头   " +
                " 哈尔滨-北京   哈尔滨-长春   哈尔滨-长沙   哈尔滨-常州   哈尔滨-成都   哈尔滨-重庆   哈尔滨-大连   哈尔滨-大理   哈尔滨-广州   哈尔滨-桂林   哈尔滨-贵阳   哈尔滨-海口   哈尔滨-杭州   哈尔滨-合肥   哈尔滨-香港   哈尔滨-黄山   哈尔滨-呼和浩特   哈尔滨-吉林   哈尔滨-济南   哈尔滨-泉州   哈尔滨-福州   哈尔滨-昆明   哈尔滨-兰州   哈尔滨-拉萨   哈尔滨-连云港   哈尔滨-丽江   哈尔滨-洛阳   哈尔滨-澳门   哈尔滨-南昌   哈尔滨-南京   哈尔滨-南宁   哈尔滨-宁波   哈尔滨-青岛   哈尔滨-秦皇岛   哈尔滨-三亚   哈尔滨-上海   哈尔滨-深圳   哈尔滨-沈阳   哈尔滨-石家庄   哈尔滨-苏州   哈尔滨-太原   哈尔滨-天津   哈尔滨-温州   哈尔滨-武汉   哈尔滨-乌鲁木齐   哈尔滨-厦门   哈尔滨-西安   哈尔滨-西宁   哈尔滨-徐州   哈尔滨-烟台   哈尔滨-银川   哈尔滨-义乌   哈尔滨-郑州   哈尔滨-珠海   哈尔滨-台北   哈尔滨-桃园   哈尔滨-无锡   哈尔滨-烟台   哈尔滨-包头   " +
                " 海口-北京   海口-长春   海口-长沙   海口-常州   海口-成都   海口-重庆   海口-大连   海口-大理   海口-广州   海口-桂林   海口-贵阳   海口-哈尔滨   海口-杭州   海口-合肥   海口-香港   海口-黄山   海口-呼和浩特   海口-吉林   海口-济南   海口-泉州   海口-福州   海口-昆明   海口-兰州   海口-拉萨   海口-连云港   海口-丽江   海口-洛阳   海口-澳门   海口-南昌   海口-南京   海口-南宁   海口-宁波   海口-青岛   海口-秦皇岛   海口-三亚   海口-上海   海口-深圳   海口-沈阳   海口-石家庄   海口-苏州   海口-太原   海口-天津   海口-温州   海口-武汉   海口-乌鲁木齐   海口-厦门   海口-西安   海口-西宁   海口-徐州   海口-烟台   海口-银川   海口-义乌   海口-郑州   海口-珠海   海口-台北   海口-桃园   海口-无锡   海口-烟台   海口-包头   " +
                " 杭州-北京   杭州-长春   杭州-长沙   杭州-常州   杭州-成都   杭州-重庆   杭州-大连   杭州-大理   杭州-广州   杭州-桂林   杭州-贵阳   杭州-哈尔滨   杭州-海口   杭州-合肥   杭州-香港   杭州-黄山   杭州-呼和浩特   杭州-吉林   杭州-济南   杭州-泉州   杭州-福州   杭州-昆明   杭州-兰州   杭州-拉萨   杭州-连云港   杭州-丽江   杭州-洛阳   杭州-澳门   杭州-南昌   杭州-南京   杭州-南宁   杭州-宁波   杭州-青岛   杭州-秦皇岛   杭州-三亚   杭州-上海   杭州-深圳   杭州-沈阳   杭州-石家庄   杭州-苏州   杭州-太原   杭州-天津   杭州-温州   杭州-武汉   杭州-乌鲁木齐   杭州-厦门   杭州-西安   杭州-西宁   杭州-徐州   杭州-烟台   杭州-银川   杭州-义乌   杭州-郑州   杭州-珠海   杭州-台北   杭州-桃园   杭州-无锡   杭州-烟台   杭州-包头   " +
                " 合肥-北京   合肥-长春   合肥-长沙   合肥-常州   合肥-成都   合肥-重庆   合肥-大连   合肥-大理   合肥-广州   合肥-桂林   合肥-贵阳   合肥-哈尔滨   合肥-海口   合肥-杭州   合肥-香港   合肥-黄山   合肥-呼和浩特   合肥-吉林   合肥-济南   合肥-泉州   合肥-福州   合肥-昆明   合肥-兰州   合肥-拉萨   合肥-连云港   合肥-丽江   合肥-洛阳   合肥-澳门   合肥-南昌   合肥-南京   合肥-南宁   合肥-宁波   合肥-青岛   合肥-秦皇岛   合肥-三亚   合肥-上海   合肥-深圳   合肥-沈阳   合肥-石家庄   合肥-苏州   合肥-太原   合肥-天津   合肥-温州   合肥-武汉   合肥-乌鲁木齐   合肥-厦门   合肥-西安   合肥-西宁   合肥-徐州   合肥-烟台   合肥-银川   合肥-义乌   合肥-郑州   合肥-珠海   合肥-台北   合肥-桃园   合肥-无锡   合肥-烟台   合肥-包头   " +
                " 香港-北京   香港-长春   香港-长沙   香港-常州   香港-成都   香港-重庆   香港-大连   香港-大理   香港-广州   香港-桂林   香港-贵阳   香港-哈尔滨   香港-海口   香港-杭州   香港-合肥   香港-黄山   香港-呼和浩特   香港-吉林   香港-济南   香港-泉州   香港-福州   香港-昆明   香港-兰州   香港-拉萨   香港-连云港   香港-丽江   香港-洛阳   香港-澳门   香港-南昌   香港-南京   香港-南宁   香港-宁波   香港-青岛   香港-秦皇岛   香港-三亚   香港-上海   香港-深圳   香港-沈阳   香港-石家庄   香港-苏州   香港-太原   香港-天津   香港-温州   香港-武汉   香港-乌鲁木齐   香港-厦门   香港-西安   香港-西宁   香港-徐州   香港-烟台   香港-银川   香港-义乌   香港-郑州   香港-珠海   香港-台北   香港-桃园   香港-无锡   香港-烟台   香港-包头   " +
                " 黄山-北京   黄山-长春   黄山-长沙   黄山-常州   黄山-成都   黄山-重庆   黄山-大连   黄山-大理   黄山-广州   黄山-桂林   黄山-贵阳   黄山-哈尔滨   黄山-海口   黄山-杭州   黄山-合肥   黄山-香港   黄山-呼和浩特   黄山-吉林   黄山-济南   黄山-泉州   黄山-福州   黄山-昆明   黄山-兰州   黄山-拉萨   黄山-连云港   黄山-丽江   黄山-洛阳   黄山-澳门   黄山-南昌   黄山-南京   黄山-南宁   黄山-宁波   黄山-青岛   黄山-秦皇岛   黄山-三亚   黄山-上海   黄山-深圳   黄山-沈阳   黄山-石家庄   黄山-苏州   黄山-太原   黄山-天津   黄山-温州   黄山-武汉   黄山-乌鲁木齐   黄山-厦门   黄山-西安   黄山-西宁   黄山-徐州   黄山-烟台   黄山-银川   黄山-义乌   黄山-郑州   黄山-珠海   黄山-台北   黄山-桃园   黄山-无锡   黄山-烟台   黄山-包头   " +
                " 呼和浩特-北京   呼和浩特-长春   呼和浩特-长沙   呼和浩特-常州   呼和浩特-成都   呼和浩特-重庆   呼和浩特-大连   呼和浩特-大理   呼和浩特-广州   呼和浩特-桂林   呼和浩特-贵阳   呼和浩特-哈尔滨   呼和浩特-海口   呼和浩特-杭州   呼和浩特-合肥   呼和浩特-香港   呼和浩特-黄山   呼和浩特-吉林   呼和浩特-济南   呼和浩特-泉州   呼和浩特-福州   呼和浩特-昆明   呼和浩特-兰州   呼和浩特-拉萨   呼和浩特-连云港   呼和浩特-丽江   呼和浩特-洛阳   呼和浩特-澳门   呼和浩特-南昌   呼和浩特-南京   呼和浩特-南宁   呼和浩特-宁波   呼和浩特-青岛   呼和浩特-秦皇岛   呼和浩特-三亚   呼和浩特-上海   呼和浩特-深圳   呼和浩特-沈阳   呼和浩特-石家庄   呼和浩特-苏州   呼和浩特-太原   呼和浩特-天津   呼和浩特-温州   呼和浩特-武汉   呼和浩特-乌鲁木齐   呼和浩特-厦门   呼和浩特-西安   呼和浩特-西宁   呼和浩特-徐州   呼和浩特-烟台   呼和浩特-银川   呼和浩特-义乌   呼和浩特-郑州   呼和浩特-珠海   呼和浩特-台北   呼和浩特-桃园   呼和浩特-无锡   呼和浩特-烟台   呼和浩特-包头   " +
                " 吉林-北京   吉林-长春   吉林-长沙   吉林-常州   吉林-成都   吉林-重庆   吉林-大连   吉林-大理   吉林-广州   吉林-桂林   吉林-贵阳   吉林-哈尔滨   吉林-海口   吉林-杭州   吉林-合肥   吉林-香港   吉林-黄山   吉林-呼和浩特   吉林-济南   吉林-泉州   吉林-福州   吉林-昆明   吉林-兰州   吉林-拉萨   吉林-连云港   吉林-丽江   吉林-洛阳   吉林-澳门   吉林-南昌   吉林-南京   吉林-南宁   吉林-宁波   吉林-青岛   吉林-秦皇岛   吉林-三亚   吉林-上海   吉林-深圳   吉林-沈阳   吉林-石家庄   吉林-苏州   吉林-太原   吉林-天津   吉林-温州   吉林-武汉   吉林-乌鲁木齐   吉林-厦门   吉林-西安   吉林-西宁   吉林-徐州   吉林-烟台   吉林-银川   吉林-义乌   吉林-郑州   吉林-珠海   吉林-台北   吉林-桃园   吉林-无锡   吉林-烟台   吉林-包头   " +
                " 济南-北京   济南-长春   济南-长沙   济南-常州   济南-成都   济南-重庆   济南-大连   济南-大理   济南-广州   济南-桂林   济南-贵阳   济南-哈尔滨   济南-海口   济南-杭州   济南-合肥   济南-香港   济南-黄山   济南-呼和浩特   济南-吉林   济南-泉州   济南-福州   济南-昆明   济南-兰州   济南-拉萨   济南-连云港   济南-丽江   济南-洛阳   济南-澳门   济南-南昌   济南-南京   济南-南宁   济南-宁波   济南-青岛   济南-秦皇岛   济南-三亚   济南-上海   济南-深圳   济南-沈阳   济南-石家庄   济南-苏州   济南-太原   济南-天津   济南-温州   济南-武汉   济南-乌鲁木齐   济南-厦门   济南-西安   济南-西宁   济南-徐州   济南-烟台   济南-银川   济南-义乌   济南-郑州   济南-珠海   济南-台北   济南-桃园   济南-无锡   济南-烟台   济南-包头   " +
                " 泉州-北京   泉州-长春   泉州-长沙   泉州-常州   泉州-成都   泉州-重庆   泉州-大连   泉州-大理   泉州-广州   泉州-桂林   泉州-贵阳   泉州-哈尔滨   泉州-海口   泉州-杭州   泉州-合肥   泉州-香港   泉州-黄山   泉州-呼和浩特   泉州-吉林   泉州-济南   泉州-福州   泉州-昆明   泉州-兰州   泉州-拉萨   泉州-连云港   泉州-丽江   泉州-洛阳   泉州-澳门   泉州-南昌   泉州-南京   泉州-南宁   泉州-宁波   泉州-青岛   泉州-秦皇岛   泉州-三亚   泉州-上海   泉州-深圳   泉州-沈阳   泉州-石家庄   泉州-苏州   泉州-太原   泉州-天津   泉州-温州   泉州-武汉   泉州-乌鲁木齐   泉州-厦门   泉州-西安   泉州-西宁   泉州-徐州   泉州-烟台   泉州-银川   泉州-义乌   泉州-郑州   泉州-珠海   泉州-台北   泉州-桃园   泉州-无锡   泉州-烟台   泉州-包头   " +
                " 福州-北京   福州-长春   福州-长沙   福州-常州   福州-成都   福州-重庆   福州-大连   福州-大理   福州-广州   福州-桂林   福州-贵阳   福州-哈尔滨   福州-海口   福州-杭州   福州-合肥   福州-香港   福州-黄山   福州-呼和浩特   福州-吉林   福州-济南   福州-泉州   福州-昆明   福州-兰州   福州-拉萨   福州-连云港   福州-丽江   福州-洛阳   福州-澳门   福州-南昌   福州-南京   福州-南宁   福州-宁波   福州-青岛   福州-秦皇岛   福州-三亚   福州-上海   福州-深圳   福州-沈阳   福州-石家庄   福州-苏州   福州-太原   福州-天津   福州-温州   福州-武汉   福州-乌鲁木齐   福州-厦门   福州-西安   福州-西宁   福州-徐州   福州-烟台   福州-银川   福州-义乌   福州-郑州   福州-珠海   福州-台北   福州-桃园   福州-无锡   福州-烟台   福州-包头   " +
                " 昆明-北京   昆明-长春   昆明-长沙   昆明-常州   昆明-成都   昆明-重庆   昆明-大连   昆明-大理   昆明-广州   昆明-桂林   昆明-贵阳   昆明-哈尔滨   昆明-海口   昆明-杭州   昆明-合肥   昆明-香港   昆明-黄山   昆明-呼和浩特   昆明-吉林   昆明-济南   昆明-泉州   昆明-福州   昆明-兰州   昆明-拉萨   昆明-连云港   昆明-丽江   昆明-洛阳   昆明-澳门   昆明-南昌   昆明-南京   昆明-南宁   昆明-宁波   昆明-青岛   昆明-秦皇岛   昆明-三亚   昆明-上海   昆明-深圳   昆明-沈阳   昆明-石家庄   昆明-苏州   昆明-太原   昆明-天津   昆明-温州   昆明-武汉   昆明-乌鲁木齐   昆明-厦门   昆明-西安   昆明-西宁   昆明-徐州   昆明-烟台   昆明-银川   昆明-义乌   昆明-郑州   昆明-珠海   昆明-台北   昆明-桃园   昆明-无锡   昆明-烟台   昆明-包头   " +
                " 兰州-北京   兰州-长春   兰州-长沙   兰州-常州   兰州-成都   兰州-重庆   兰州-大连   兰州-大理   兰州-广州   兰州-桂林   兰州-贵阳   兰州-哈尔滨   兰州-海口   兰州-杭州   兰州-合肥   兰州-香港   兰州-黄山   兰州-呼和浩特   兰州-吉林   兰州-济南   兰州-泉州   兰州-福州   兰州-昆明   兰州-拉萨   兰州-连云港   兰州-丽江   兰州-洛阳   兰州-澳门   兰州-南昌   兰州-南京   兰州-南宁   兰州-宁波   兰州-青岛   兰州-秦皇岛   兰州-三亚   兰州-上海   兰州-深圳   兰州-沈阳   兰州-石家庄   兰州-苏州   兰州-太原   兰州-天津   兰州-温州   兰州-武汉   兰州-乌鲁木齐   兰州-厦门   兰州-西安   兰州-西宁   兰州-徐州   兰州-烟台   兰州-银川   兰州-义乌   兰州-郑州   兰州-珠海   兰州-台北   兰州-桃园   兰州-无锡   兰州-烟台   兰州-包头   " +
                " 拉萨-北京   拉萨-长春   拉萨-长沙   拉萨-常州   拉萨-成都   拉萨-重庆   拉萨-大连   拉萨-大理   拉萨-广州   拉萨-桂林   拉萨-贵阳   拉萨-哈尔滨   拉萨-海口   拉萨-杭州   拉萨-合肥   拉萨-香港   拉萨-黄山   拉萨-呼和浩特   拉萨-吉林   拉萨-济南   拉萨-泉州   拉萨-福州   拉萨-昆明   拉萨-兰州   拉萨-连云港   拉萨-丽江   拉萨-洛阳   拉萨-澳门   拉萨-南昌   拉萨-南京   拉萨-南宁   拉萨-宁波   拉萨-青岛   拉萨-秦皇岛   拉萨-三亚   拉萨-上海   拉萨-深圳   拉萨-沈阳   拉萨-石家庄   拉萨-苏州   拉萨-太原   拉萨-天津   拉萨-温州   拉萨-武汉   拉萨-乌鲁木齐   拉萨-厦门   拉萨-西安   拉萨-西宁   拉萨-徐州   拉萨-烟台   拉萨-银川   拉萨-义乌   拉萨-郑州   拉萨-珠海   拉萨-台北   拉萨-桃园   拉萨-无锡   拉萨-烟台   拉萨-包头   " +
                " 连云港-北京   连云港-长春   连云港-长沙   连云港-常州   连云港-成都   连云港-重庆   连云港-大连   连云港-大理   连云港-广州   连云港-桂林   连云港-贵阳   连云港-哈尔滨   连云港-海口   连云港-杭州   连云港-合肥   连云港-香港   连云港-黄山   连云港-呼和浩特   连云港-吉林   连云港-济南   连云港-泉州   连云港-福州   连云港-昆明   连云港-兰州   连云港-拉萨   连云港-丽江   连云港-洛阳   连云港-澳门   连云港-南昌   连云港-南京   连云港-南宁   连云港-宁波   连云港-青岛   连云港-秦皇岛   连云港-三亚   连云港-上海   连云港-深圳   连云港-沈阳   连云港-石家庄   连云港-苏州   连云港-太原   连云港-天津   连云港-温州   连云港-武汉   连云港-乌鲁木齐   连云港-厦门   连云港-西安   连云港-西宁   连云港-徐州   连云港-烟台   连云港-银川   连云港-义乌   连云港-郑州   连云港-珠海   连云港-台北   连云港-桃园   连云港-无锡   连云港-烟台   连云港-包头   " +
                " 丽江-北京   丽江-长春   丽江-长沙   丽江-常州   丽江-成都   丽江-重庆   丽江-大连   丽江-大理   丽江-广州   丽江-桂林   丽江-贵阳   丽江-哈尔滨   丽江-海口   丽江-杭州   丽江-合肥   丽江-香港   丽江-黄山   丽江-呼和浩特   丽江-吉林   丽江-济南   丽江-泉州   丽江-福州   丽江-昆明   丽江-兰州   丽江-拉萨   丽江-连云港   丽江-洛阳   丽江-澳门   丽江-南昌   丽江-南京   丽江-南宁   丽江-宁波   丽江-青岛   丽江-秦皇岛   丽江-三亚   丽江-上海   丽江-深圳   丽江-沈阳   丽江-石家庄   丽江-苏州   丽江-太原   丽江-天津   丽江-温州   丽江-武汉   丽江-乌鲁木齐   丽江-厦门   丽江-西安   丽江-西宁   丽江-徐州   丽江-烟台   丽江-银川   丽江-义乌   丽江-郑州   丽江-珠海   丽江-台北   丽江-桃园   丽江-无锡   丽江-烟台   丽江-包头   " +
                " 洛阳-北京   洛阳-长春   洛阳-长沙   洛阳-常州   洛阳-成都   洛阳-重庆   洛阳-大连   洛阳-大理   洛阳-广州   洛阳-桂林   洛阳-贵阳   洛阳-哈尔滨   洛阳-海口   洛阳-杭州   洛阳-合肥   洛阳-香港   洛阳-黄山   洛阳-呼和浩特   洛阳-吉林   洛阳-济南   洛阳-泉州   洛阳-福州   洛阳-昆明   洛阳-兰州   洛阳-拉萨   洛阳-连云港   洛阳-丽江   洛阳-澳门   洛阳-南昌   洛阳-南京   洛阳-南宁   洛阳-宁波   洛阳-青岛   洛阳-秦皇岛   洛阳-三亚   洛阳-上海   洛阳-深圳   洛阳-沈阳   洛阳-石家庄   洛阳-苏州   洛阳-太原   洛阳-天津   洛阳-温州   洛阳-武汉   洛阳-乌鲁木齐   洛阳-厦门   洛阳-西安   洛阳-西宁   洛阳-徐州   洛阳-烟台   洛阳-银川   洛阳-义乌   洛阳-郑州   洛阳-珠海   洛阳-台北   洛阳-桃园   洛阳-无锡   洛阳-烟台   洛阳-包头   " +
                " 澳门-北京   澳门-长春   澳门-长沙   澳门-常州   澳门-成都   澳门-重庆   澳门-大连   澳门-大理   澳门-广州   澳门-桂林   澳门-贵阳   澳门-哈尔滨   澳门-海口   澳门-杭州   澳门-合肥   澳门-香港   澳门-黄山   澳门-呼和浩特   澳门-吉林   澳门-济南   澳门-泉州   澳门-福州   澳门-昆明   澳门-兰州   澳门-拉萨   澳门-连云港   澳门-丽江   澳门-洛阳   澳门-南昌   澳门-南京   澳门-南宁   澳门-宁波   澳门-青岛   澳门-秦皇岛   澳门-三亚   澳门-上海   澳门-深圳   澳门-沈阳   澳门-石家庄   澳门-苏州   澳门-太原   澳门-天津   澳门-温州   澳门-武汉   澳门-乌鲁木齐   澳门-厦门   澳门-西安   澳门-西宁   澳门-徐州   澳门-烟台   澳门-银川   澳门-义乌   澳门-郑州   澳门-珠海   澳门-台北   澳门-桃园   澳门-无锡   澳门-烟台   澳门-包头   " +
                " 南昌-北京   南昌-长春   南昌-长沙   南昌-常州   南昌-成都   南昌-重庆   南昌-大连   南昌-大理   南昌-广州   南昌-桂林   南昌-贵阳   南昌-哈尔滨   南昌-海口   南昌-杭州   南昌-合肥   南昌-香港   南昌-黄山   南昌-呼和浩特   南昌-吉林   南昌-济南   南昌-泉州   南昌-福州   南昌-昆明   南昌-兰州   南昌-拉萨   南昌-连云港   南昌-丽江   南昌-洛阳   南昌-澳门   南昌-南京   南昌-南宁   南昌-宁波   南昌-青岛   南昌-秦皇岛   南昌-三亚   南昌-上海   南昌-深圳   南昌-沈阳   南昌-石家庄   南昌-苏州   南昌-太原   南昌-天津   南昌-温州   南昌-武汉   南昌-乌鲁木齐   南昌-厦门   南昌-西安   南昌-西宁   南昌-徐州   南昌-烟台   南昌-银川   南昌-义乌   南昌-郑州   南昌-珠海   南昌-台北   南昌-桃园   南昌-无锡   南昌-烟台   南昌-包头   " +
                " 南京-北京   南京-长春   南京-长沙   南京-常州   南京-成都   南京-重庆   南京-大连   南京-大理   南京-广州   南京-桂林   南京-贵阳   南京-哈尔滨   南京-海口   南京-杭州   南京-合肥   南京-香港   南京-黄山   南京-呼和浩特   南京-吉林   南京-济南   南京-泉州   南京-福州   南京-昆明   南京-兰州   南京-拉萨   南京-连云港   南京-丽江   南京-洛阳   南京-澳门   南京-南昌   南京-南宁   南京-宁波   南京-青岛   南京-秦皇岛   南京-三亚   南京-上海   南京-深圳   南京-沈阳   南京-石家庄   南京-苏州   南京-太原   南京-天津   南京-温州   南京-武汉   南京-乌鲁木齐   南京-厦门   南京-西安   南京-西宁   南京-徐州   南京-烟台   南京-银川   南京-义乌   南京-郑州   南京-珠海   南京-台北   南京-桃园   南京-无锡   南京-烟台   南京-包头   " +
                " 南宁-北京   南宁-长春   南宁-长沙   南宁-常州   南宁-成都   南宁-重庆   南宁-大连   南宁-大理   南宁-广州   南宁-桂林   南宁-贵阳   南宁-哈尔滨   南宁-海口   南宁-杭州   南宁-合肥   南宁-香港   南宁-黄山   南宁-呼和浩特   南宁-吉林   南宁-济南   南宁-泉州   南宁-福州   南宁-昆明   南宁-兰州   南宁-拉萨   南宁-连云港   南宁-丽江   南宁-洛阳   南宁-澳门   南宁-南昌   南宁-南京   南宁-宁波   南宁-青岛   南宁-秦皇岛   南宁-三亚   南宁-上海   南宁-深圳   南宁-沈阳   南宁-石家庄   南宁-苏州   南宁-太原   南宁-天津   南宁-温州   南宁-武汉   南宁-乌鲁木齐   南宁-厦门   南宁-西安   南宁-西宁   南宁-徐州   南宁-烟台   南宁-银川   南宁-义乌   南宁-郑州   南宁-珠海   南宁-台北   南宁-桃园   南宁-无锡   南宁-烟台   南宁-包头   " +
                " 宁波-北京   宁波-长春   宁波-长沙   宁波-常州   宁波-成都   宁波-重庆   宁波-大连   宁波-大理   宁波-广州   宁波-桂林   宁波-贵阳   宁波-哈尔滨   宁波-海口   宁波-杭州   宁波-合肥   宁波-香港   宁波-黄山   宁波-呼和浩特   宁波-吉林   宁波-济南   宁波-泉州   宁波-福州   宁波-昆明   宁波-兰州   宁波-拉萨   宁波-连云港   宁波-丽江   宁波-洛阳   宁波-澳门   宁波-南昌   宁波-南京   宁波-南宁   宁波-青岛   宁波-秦皇岛   宁波-三亚   宁波-上海   宁波-深圳   宁波-沈阳   宁波-石家庄   宁波-苏州   宁波-太原   宁波-天津   宁波-温州   宁波-武汉   宁波-乌鲁木齐   宁波-厦门   宁波-西安   宁波-西宁   宁波-徐州   宁波-烟台   宁波-银川   宁波-义乌   宁波-郑州   宁波-珠海   宁波-台北   宁波-桃园   宁波-无锡   宁波-烟台   宁波-包头   " +
                " 青岛-北京   青岛-长春   青岛-长沙   青岛-常州   青岛-成都   青岛-重庆   青岛-大连   青岛-大理   青岛-广州   青岛-桂林   青岛-贵阳   青岛-哈尔滨   青岛-海口   青岛-杭州   青岛-合肥   青岛-香港   青岛-黄山   青岛-呼和浩特   青岛-吉林   青岛-济南   青岛-泉州   青岛-福州   青岛-昆明   青岛-兰州   青岛-拉萨   青岛-连云港   青岛-丽江   青岛-洛阳   青岛-澳门   青岛-南昌   青岛-南京   青岛-南宁   青岛-宁波   青岛-秦皇岛   青岛-三亚   青岛-上海   青岛-深圳   青岛-沈阳   青岛-石家庄   青岛-苏州   青岛-太原   青岛-天津   青岛-温州   青岛-武汉   青岛-乌鲁木齐   青岛-厦门   青岛-西安   青岛-西宁   青岛-徐州   青岛-烟台   青岛-银川   青岛-义乌   青岛-郑州   青岛-珠海   青岛-台北   青岛-桃园   青岛-无锡   青岛-烟台   青岛-包头   " +
                " 秦皇岛-北京   秦皇岛-长春   秦皇岛-长沙   秦皇岛-常州   秦皇岛-成都   秦皇岛-重庆   秦皇岛-大连   秦皇岛-大理   秦皇岛-广州   秦皇岛-桂林   秦皇岛-贵阳   秦皇岛-哈尔滨   秦皇岛-海口   秦皇岛-杭州   秦皇岛-合肥   秦皇岛-香港   秦皇岛-黄山   秦皇岛-呼和浩特   秦皇岛-吉林   秦皇岛-济南   秦皇岛-泉州   秦皇岛-福州   秦皇岛-昆明   秦皇岛-兰州   秦皇岛-拉萨   秦皇岛-连云港   秦皇岛-丽江   秦皇岛-洛阳   秦皇岛-澳门   秦皇岛-南昌   秦皇岛-南京   秦皇岛-南宁   秦皇岛-宁波   秦皇岛-青岛   秦皇岛-三亚   秦皇岛-上海   秦皇岛-深圳   秦皇岛-沈阳   秦皇岛-石家庄   秦皇岛-苏州   秦皇岛-太原   秦皇岛-天津   秦皇岛-温州   秦皇岛-武汉   秦皇岛-乌鲁木齐   秦皇岛-厦门   秦皇岛-西安   秦皇岛-西宁   秦皇岛-徐州   秦皇岛-烟台   秦皇岛-银川   秦皇岛-义乌   秦皇岛-郑州   秦皇岛-珠海   秦皇岛-台北   秦皇岛-桃园   秦皇岛-无锡   秦皇岛-烟台   秦皇岛-包头   " +
                " 三亚-北京   三亚-长春   三亚-长沙   三亚-常州   三亚-成都   三亚-重庆   三亚-大连   三亚-大理   三亚-广州   三亚-桂林   三亚-贵阳   三亚-哈尔滨   三亚-海口   三亚-杭州   三亚-合肥   三亚-香港   三亚-黄山   三亚-呼和浩特   三亚-吉林   三亚-济南   三亚-泉州   三亚-福州   三亚-昆明   三亚-兰州   三亚-拉萨   三亚-连云港   三亚-丽江   三亚-洛阳   三亚-澳门   三亚-南昌   三亚-南京   三亚-南宁   三亚-宁波   三亚-青岛   三亚-秦皇岛   三亚-上海   三亚-深圳   三亚-沈阳   三亚-石家庄   三亚-苏州   三亚-太原   三亚-天津   三亚-温州   三亚-武汉   三亚-乌鲁木齐   三亚-厦门   三亚-西安   三亚-西宁   三亚-徐州   三亚-烟台   三亚-银川   三亚-义乌   三亚-郑州   三亚-珠海   三亚-台北   三亚-桃园   三亚-无锡   三亚-烟台   三亚-包头   " +
                " 上海-北京   上海-长春   上海-长沙   上海-常州   上海-成都   上海-重庆   上海-大连   上海-大理   上海-广州   上海-桂林   上海-贵阳   上海-哈尔滨   上海-海口   上海-杭州   上海-合肥   上海-香港   上海-黄山   上海-呼和浩特   上海-吉林   上海-济南   上海-泉州   上海-福州   上海-昆明   上海-兰州   上海-拉萨   上海-连云港   上海-丽江   上海-洛阳   上海-澳门   上海-南昌   上海-南京   上海-南宁   上海-宁波   上海-青岛   上海-秦皇岛   上海-三亚   上海-深圳   上海-沈阳   上海-石家庄   上海-苏州   上海-太原   上海-天津   上海-温州   上海-武汉   上海-乌鲁木齐   上海-厦门   上海-西安   上海-西宁   上海-徐州   上海-烟台   上海-银川   上海-义乌   上海-郑州   上海-珠海   上海-台北   上海-桃园   上海-无锡   上海-烟台   上海-包头   " +
                " 深圳-北京   深圳-长春   深圳-长沙   深圳-常州   深圳-成都   深圳-重庆   深圳-大连   深圳-大理   深圳-广州   深圳-桂林   深圳-贵阳   深圳-哈尔滨   深圳-海口   深圳-杭州   深圳-合肥   深圳-香港   深圳-黄山   深圳-呼和浩特   深圳-吉林   深圳-济南   深圳-泉州   深圳-福州   深圳-昆明   深圳-兰州   深圳-拉萨   深圳-连云港   深圳-丽江   深圳-洛阳   深圳-澳门   深圳-南昌   深圳-南京   深圳-南宁   深圳-宁波   深圳-青岛   深圳-秦皇岛   深圳-三亚   深圳-上海   深圳-沈阳   深圳-石家庄   深圳-苏州   深圳-太原   深圳-天津   深圳-温州   深圳-武汉   深圳-乌鲁木齐   深圳-厦门   深圳-西安   深圳-西宁   深圳-徐州   深圳-烟台   深圳-银川   深圳-义乌   深圳-郑州   深圳-珠海   深圳-台北   深圳-桃园   深圳-无锡   深圳-烟台   深圳-包头   " +
                " 沈阳-北京   沈阳-长春   沈阳-长沙   沈阳-常州   沈阳-成都   沈阳-重庆   沈阳-大连   沈阳-大理   沈阳-广州   沈阳-桂林   沈阳-贵阳   沈阳-哈尔滨   沈阳-海口   沈阳-杭州   沈阳-合肥   沈阳-香港   沈阳-黄山   沈阳-呼和浩特   沈阳-吉林   沈阳-济南   沈阳-泉州   沈阳-福州   沈阳-昆明   沈阳-兰州   沈阳-拉萨   沈阳-连云港   沈阳-丽江   沈阳-洛阳   沈阳-澳门   沈阳-南昌   沈阳-南京   沈阳-南宁   沈阳-宁波   沈阳-青岛   沈阳-秦皇岛   沈阳-三亚   沈阳-上海   沈阳-深圳   沈阳-石家庄   沈阳-苏州   沈阳-太原   沈阳-天津   沈阳-温州   沈阳-武汉   沈阳-乌鲁木齐   沈阳-厦门   沈阳-西安   沈阳-西宁   沈阳-徐州   沈阳-烟台   沈阳-银川   沈阳-义乌   沈阳-郑州   沈阳-珠海   沈阳-台北   沈阳-桃园   沈阳-无锡   沈阳-烟台   沈阳-包头   " +
                " 石家庄-北京   石家庄-长春   石家庄-长沙   石家庄-常州   石家庄-成都   石家庄-重庆   石家庄-大连   石家庄-大理   石家庄-广州   石家庄-桂林   石家庄-贵阳   石家庄-哈尔滨   石家庄-海口   石家庄-杭州   石家庄-合肥   石家庄-香港   石家庄-黄山   石家庄-呼和浩特   石家庄-吉林   石家庄-济南   石家庄-泉州   石家庄-福州   石家庄-昆明   石家庄-兰州   石家庄-拉萨   石家庄-连云港   石家庄-丽江   石家庄-洛阳   石家庄-澳门   石家庄-南昌   石家庄-南京   石家庄-南宁   石家庄-宁波   石家庄-青岛   石家庄-秦皇岛   石家庄-三亚   石家庄-上海   石家庄-深圳   石家庄-沈阳   石家庄-苏州   石家庄-太原   石家庄-天津   石家庄-温州   石家庄-武汉   石家庄-乌鲁木齐   石家庄-厦门   石家庄-西安   石家庄-西宁   石家庄-徐州   石家庄-烟台   石家庄-银川   石家庄-义乌   石家庄-郑州   石家庄-珠海   石家庄-台北   石家庄-桃园   石家庄-无锡   石家庄-烟台   石家庄-包头   " +
                " 苏州-北京   苏州-长春   苏州-长沙   苏州-常州   苏州-成都   苏州-重庆   苏州-大连   苏州-大理   苏州-广州   苏州-桂林   苏州-贵阳   苏州-哈尔滨   苏州-海口   苏州-杭州   苏州-合肥   苏州-香港   苏州-黄山   苏州-呼和浩特   苏州-吉林   苏州-济南   苏州-泉州   苏州-福州   苏州-昆明   苏州-兰州   苏州-拉萨   苏州-连云港   苏州-丽江   苏州-洛阳   苏州-澳门   苏州-南昌   苏州-南京   苏州-南宁   苏州-宁波   苏州-青岛   苏州-秦皇岛   苏州-三亚   苏州-上海   苏州-深圳   苏州-沈阳   苏州-石家庄   苏州-太原   苏州-天津   苏州-温州   苏州-武汉   苏州-乌鲁木齐   苏州-厦门   苏州-西安   苏州-西宁   苏州-徐州   苏州-烟台   苏州-银川   苏州-义乌   苏州-郑州   苏州-珠海   苏州-台北   苏州-桃园   苏州-无锡   苏州-烟台   苏州-包头   " +
                " 太原-北京   太原-长春   太原-长沙   太原-常州   太原-成都   太原-重庆   太原-大连   太原-大理   太原-广州   太原-桂林   太原-贵阳   太原-哈尔滨   太原-海口   太原-杭州   太原-合肥   太原-香港   太原-黄山   太原-呼和浩特   太原-吉林   太原-济南   太原-泉州   太原-福州   太原-昆明   太原-兰州   太原-拉萨   太原-连云港   太原-丽江   太原-洛阳   太原-澳门   太原-南昌   太原-南京   太原-南宁   太原-宁波   太原-青岛   太原-秦皇岛   太原-三亚   太原-上海   太原-深圳   太原-沈阳   太原-石家庄   太原-苏州   太原-天津   太原-温州   太原-武汉   太原-乌鲁木齐   太原-厦门   太原-西安   太原-西宁   太原-徐州   太原-烟台   太原-银川   太原-义乌   太原-郑州   太原-珠海   太原-台北   太原-桃园   太原-无锡   太原-烟台   太原-包头   " +
                " 天津-北京   天津-长春   天津-长沙   天津-常州   天津-成都   天津-重庆   天津-大连   天津-大理   天津-广州   天津-桂林   天津-贵阳   天津-哈尔滨   天津-海口   天津-杭州   天津-合肥   天津-香港   天津-黄山   天津-呼和浩特   天津-吉林   天津-济南   天津-泉州   天津-福州   天津-昆明   天津-兰州   天津-拉萨   天津-连云港   天津-丽江   天津-洛阳   天津-澳门   天津-南昌   天津-南京   天津-南宁   天津-宁波   天津-青岛   天津-秦皇岛   天津-三亚   天津-上海   天津-深圳   天津-沈阳   天津-石家庄   天津-苏州   天津-太原   天津-温州   天津-武汉   天津-乌鲁木齐   天津-厦门   天津-西安   天津-西宁   天津-徐州   天津-烟台   天津-银川   天津-义乌   天津-郑州   天津-珠海   天津-台北   天津-桃园   天津-无锡   天津-烟台   天津-包头   " +
                " 温州-北京   温州-长春   温州-长沙   温州-常州   温州-成都   温州-重庆   温州-大连   温州-大理   温州-广州   温州-桂林   温州-贵阳   温州-哈尔滨   温州-海口   温州-杭州   温州-合肥   温州-香港   温州-黄山   温州-呼和浩特   温州-吉林   温州-济南   温州-泉州   温州-福州   温州-昆明   温州-兰州   温州-拉萨   温州-连云港   温州-丽江   温州-洛阳   温州-澳门   温州-南昌   温州-南京   温州-南宁   温州-宁波   温州-青岛   温州-秦皇岛   温州-三亚   温州-上海   温州-深圳   温州-沈阳   温州-石家庄   温州-苏州   温州-太原   温州-天津   温州-武汉   温州-乌鲁木齐   温州-厦门   温州-西安   温州-西宁   温州-徐州   温州-烟台   温州-银川   温州-义乌   温州-郑州   温州-珠海   温州-台北   温州-桃园   温州-无锡   温州-烟台   温州-包头   " +
                " 武汉-北京   武汉-长春   武汉-长沙   武汉-常州   武汉-成都   武汉-重庆   武汉-大连   武汉-大理   武汉-广州   武汉-桂林   武汉-贵阳   武汉-哈尔滨   武汉-海口   武汉-杭州   武汉-合肥   武汉-香港   武汉-黄山   武汉-呼和浩特   武汉-吉林   武汉-济南   武汉-泉州   武汉-福州   武汉-昆明   武汉-兰州   武汉-拉萨   武汉-连云港   武汉-丽江   武汉-洛阳   武汉-澳门   武汉-南昌   武汉-南京   武汉-南宁   武汉-宁波   武汉-青岛   武汉-秦皇岛   武汉-三亚   武汉-上海   武汉-深圳   武汉-沈阳   武汉-石家庄   武汉-苏州   武汉-太原   武汉-天津   武汉-温州   武汉-乌鲁木齐   武汉-厦门   武汉-西安   武汉-西宁   武汉-徐州   武汉-烟台   武汉-银川   武汉-义乌   武汉-郑州   武汉-珠海   武汉-台北   武汉-桃园   武汉-无锡   武汉-烟台   武汉-包头   " +
                " 乌鲁木齐-北京   乌鲁木齐-长春   乌鲁木齐-长沙   乌鲁木齐-常州   乌鲁木齐-成都   乌鲁木齐-重庆   乌鲁木齐-大连   乌鲁木齐-大理   乌鲁木齐-广州   乌鲁木齐-桂林   乌鲁木齐-贵阳   乌鲁木齐-哈尔滨   乌鲁木齐-海口   乌鲁木齐-杭州   乌鲁木齐-合肥   乌鲁木齐-香港   乌鲁木齐-黄山   乌鲁木齐-呼和浩特   乌鲁木齐-吉林   乌鲁木齐-济南   乌鲁木齐-泉州   乌鲁木齐-福州   乌鲁木齐-昆明   乌鲁木齐-兰州   乌鲁木齐-拉萨   乌鲁木齐-连云港   乌鲁木齐-丽江   乌鲁木齐-洛阳   乌鲁木齐-澳门   乌鲁木齐-南昌   乌鲁木齐-南京   乌鲁木齐-南宁   乌鲁木齐-宁波   乌鲁木齐-青岛   乌鲁木齐-秦皇岛   乌鲁木齐-三亚   乌鲁木齐-上海   乌鲁木齐-深圳   乌鲁木齐-沈阳   乌鲁木齐-石家庄   乌鲁木齐-苏州   乌鲁木齐-太原   乌鲁木齐-天津   乌鲁木齐-温州   乌鲁木齐-武汉   乌鲁木齐-厦门   乌鲁木齐-西安   乌鲁木齐-西宁   乌鲁木齐-徐州   乌鲁木齐-烟台   乌鲁木齐-银川   乌鲁木齐-义乌   乌鲁木齐-郑州   乌鲁木齐-珠海   乌鲁木齐-台北   乌鲁木齐-桃园   乌鲁木齐-无锡   乌鲁木齐-烟台   乌鲁木齐-包头   " +
                " 厦门-北京   厦门-长春   厦门-长沙   厦门-常州   厦门-成都   厦门-重庆   厦门-大连   厦门-大理   厦门-广州   厦门-桂林   厦门-贵阳   厦门-哈尔滨   厦门-海口   厦门-杭州   厦门-合肥   厦门-香港   厦门-黄山   厦门-呼和浩特   厦门-吉林   厦门-济南   厦门-泉州   厦门-福州   厦门-昆明   厦门-兰州   厦门-拉萨   厦门-连云港   厦门-丽江   厦门-洛阳   厦门-澳门   厦门-南昌   厦门-南京   厦门-南宁   厦门-宁波   厦门-青岛   厦门-秦皇岛   厦门-三亚   厦门-上海   厦门-深圳   厦门-沈阳   厦门-石家庄   厦门-苏州   厦门-太原   厦门-天津   厦门-温州   厦门-武汉   厦门-乌鲁木齐   厦门-西安   厦门-西宁   厦门-徐州   厦门-烟台   厦门-银川   厦门-义乌   厦门-郑州   厦门-珠海   厦门-台北   厦门-桃园   厦门-无锡   厦门-烟台   厦门-包头   " +
                " 西安-北京   西安-长春   西安-长沙   西安-常州   西安-成都   西安-重庆   西安-大连   西安-大理   西安-广州   西安-桂林   西安-贵阳   西安-哈尔滨   西安-海口   西安-杭州   西安-合肥   西安-香港   西安-黄山   西安-呼和浩特   西安-吉林   西安-济南   西安-泉州   西安-福州   西安-昆明   西安-兰州   西安-拉萨   西安-连云港   西安-丽江   西安-洛阳   西安-澳门   西安-南昌   西安-南京   西安-南宁   西安-宁波   西安-青岛   西安-秦皇岛   西安-三亚   西安-上海   西安-深圳   西安-沈阳   西安-石家庄   西安-苏州   西安-太原   西安-天津   西安-温州   西安-武汉   西安-乌鲁木齐   西安-厦门   西安-西宁   西安-徐州   西安-烟台   西安-银川   西安-义乌   西安-郑州   西安-珠海   西安-台北   西安-桃园   西安-无锡   西安-烟台   西安-包头   " +
                " 西宁-北京   西宁-长春   西宁-长沙   西宁-常州   西宁-成都   西宁-重庆   西宁-大连   西宁-大理   西宁-广州   西宁-桂林   西宁-贵阳   西宁-哈尔滨   西宁-海口   西宁-杭州   西宁-合肥   西宁-香港   西宁-黄山   西宁-呼和浩特   西宁-吉林   西宁-济南   西宁-泉州   西宁-福州   西宁-昆明   西宁-兰州   西宁-拉萨   西宁-连云港   西宁-丽江   西宁-洛阳   西宁-澳门   西宁-南昌   西宁-南京   西宁-南宁   西宁-宁波   西宁-青岛   西宁-秦皇岛   西宁-三亚   西宁-上海   西宁-深圳   西宁-沈阳   西宁-石家庄   西宁-苏州   西宁-太原   西宁-天津   西宁-温州   西宁-武汉   西宁-乌鲁木齐   西宁-厦门   西宁-西安   西宁-徐州   西宁-烟台   西宁-银川   西宁-义乌   西宁-郑州   西宁-珠海   西宁-台北   西宁-桃园   西宁-无锡   西宁-烟台   西宁-包头   " +
                " 徐州-北京   徐州-长春   徐州-长沙   徐州-常州   徐州-成都   徐州-重庆   徐州-大连   徐州-大理   徐州-广州   徐州-桂林   徐州-贵阳   徐州-哈尔滨   徐州-海口   徐州-杭州   徐州-合肥   徐州-香港   徐州-黄山   徐州-呼和浩特   徐州-吉林   徐州-济南   徐州-泉州   徐州-福州   徐州-昆明   徐州-兰州   徐州-拉萨   徐州-连云港   徐州-丽江   徐州-洛阳   徐州-澳门   徐州-南昌   徐州-南京   徐州-南宁   徐州-宁波   徐州-青岛   徐州-秦皇岛   徐州-三亚   徐州-上海   徐州-深圳   徐州-沈阳   徐州-石家庄   徐州-苏州   徐州-太原   徐州-天津   徐州-温州   徐州-武汉   徐州-乌鲁木齐   徐州-厦门   徐州-西安   徐州-西宁   徐州-烟台   徐州-银川   徐州-义乌   徐州-郑州   徐州-珠海   徐州-台北   徐州-桃园   徐州-无锡   徐州-烟台   徐州-包头   " +
                " 烟台-北京   烟台-长春   烟台-长沙   烟台-常州   烟台-成都   烟台-重庆   烟台-大连   烟台-大理   烟台-广州   烟台-桂林   烟台-贵阳   烟台-哈尔滨   烟台-海口   烟台-杭州   烟台-合肥   烟台-香港   烟台-黄山   烟台-呼和浩特   烟台-吉林   烟台-济南   烟台-泉州   烟台-福州   烟台-昆明   烟台-兰州   烟台-拉萨   烟台-连云港   烟台-丽江   烟台-洛阳   烟台-澳门   烟台-南昌   烟台-南京   烟台-南宁   烟台-宁波   烟台-青岛   烟台-秦皇岛   烟台-三亚   烟台-上海   烟台-深圳   烟台-沈阳   烟台-石家庄   烟台-苏州   烟台-太原   烟台-天津   烟台-温州   烟台-武汉   烟台-乌鲁木齐   烟台-厦门   烟台-西安   烟台-西宁   烟台-徐州   烟台-银川   烟台-义乌   烟台-郑州   烟台-珠海   烟台-台北   烟台-桃园   烟台-无锡   烟台-烟台   烟台-包头   " +
                " 银川-北京   银川-长春   银川-长沙   银川-常州   银川-成都   银川-重庆   银川-大连   银川-大理   银川-广州   银川-桂林   银川-贵阳   银川-哈尔滨   银川-海口   银川-杭州   银川-合肥   银川-香港   银川-黄山   银川-呼和浩特   银川-吉林   银川-济南   银川-泉州   银川-福州   银川-昆明   银川-兰州   银川-拉萨   银川-连云港   银川-丽江   银川-洛阳   银川-澳门   银川-南昌   银川-南京   银川-南宁   银川-宁波   银川-青岛   银川-秦皇岛   银川-三亚   银川-上海   银川-深圳   银川-沈阳   银川-石家庄   银川-苏州   银川-太原   银川-天津   银川-温州   银川-武汉   银川-乌鲁木齐   银川-厦门   银川-西安   银川-西宁   银川-徐州   银川-烟台   银川-义乌   银川-郑州   银川-珠海   银川-台北   银川-桃园   银川-无锡   银川-烟台   银川-包头   " +
                " 义乌-北京   义乌-长春   义乌-长沙   义乌-常州   义乌-成都   义乌-重庆   义乌-大连   义乌-大理   义乌-广州   义乌-桂林   义乌-贵阳   义乌-哈尔滨   义乌-海口   义乌-杭州   义乌-合肥   义乌-香港   义乌-黄山   义乌-呼和浩特   义乌-吉林   义乌-济南   义乌-泉州   义乌-福州   义乌-昆明   义乌-兰州   义乌-拉萨   义乌-连云港   义乌-丽江   义乌-洛阳   义乌-澳门   义乌-南昌   义乌-南京   义乌-南宁   义乌-宁波   义乌-青岛   义乌-秦皇岛   义乌-三亚   义乌-上海   义乌-深圳   义乌-沈阳   义乌-石家庄   义乌-苏州   义乌-太原   义乌-天津   义乌-温州   义乌-武汉   义乌-乌鲁木齐   义乌-厦门   义乌-西安   义乌-西宁   义乌-徐州   义乌-烟台   义乌-银川   义乌-郑州   义乌-珠海   义乌-台北   义乌-桃园   义乌-无锡   义乌-烟台   义乌-包头   " +
                " 郑州-北京   郑州-长春   郑州-长沙   郑州-常州   郑州-成都   郑州-重庆   郑州-大连   郑州-大理   郑州-广州   郑州-桂林   郑州-贵阳   郑州-哈尔滨   郑州-海口   郑州-杭州   郑州-合肥   郑州-香港   郑州-黄山   郑州-呼和浩特   郑州-吉林   郑州-济南   郑州-泉州   郑州-福州   郑州-昆明   郑州-兰州   郑州-拉萨   郑州-连云港   郑州-丽江   郑州-洛阳   郑州-澳门   郑州-南昌   郑州-南京   郑州-南宁   郑州-宁波   郑州-青岛   郑州-秦皇岛   郑州-三亚   郑州-上海   郑州-深圳   郑州-沈阳   郑州-石家庄   郑州-苏州   郑州-太原   郑州-天津   郑州-温州   郑州-武汉   郑州-乌鲁木齐   郑州-厦门   郑州-西安   郑州-西宁   郑州-徐州   郑州-烟台   郑州-银川   郑州-义乌   郑州-珠海   郑州-台北   郑州-桃园   郑州-无锡   郑州-烟台   郑州-包头   " +
                " 珠海-北京   珠海-长春   珠海-长沙   珠海-常州   珠海-成都   珠海-重庆   珠海-大连   珠海-大理   珠海-广州   珠海-桂林   珠海-贵阳   珠海-哈尔滨   珠海-海口   珠海-杭州   珠海-合肥   珠海-香港   珠海-黄山   珠海-呼和浩特   珠海-吉林   珠海-济南   珠海-泉州   珠海-福州   珠海-昆明   珠海-兰州   珠海-拉萨   珠海-连云港   珠海-丽江   珠海-洛阳   珠海-澳门   珠海-南昌   珠海-南京   珠海-南宁   珠海-宁波   珠海-青岛   珠海-秦皇岛   珠海-三亚   珠海-上海   珠海-深圳   珠海-沈阳   珠海-石家庄   珠海-苏州   珠海-太原   珠海-天津   珠海-温州   珠海-武汉   珠海-乌鲁木齐   珠海-厦门   珠海-西安   珠海-西宁   珠海-徐州   珠海-烟台   珠海-银川   珠海-义乌   珠海-郑州   珠海-台北   珠海-桃园   珠海-无锡   珠海-烟台   珠海-包头   " +
                " 台北-北京   台北-长春   台北-长沙   台北-常州   台北-成都   台北-重庆   台北-大连   台北-大理   台北-广州   台北-桂林   台北-贵阳   台北-哈尔滨   台北-海口   台北-杭州   台北-合肥   台北-香港   台北-黄山   台北-呼和浩特   台北-吉林   台北-济南   台北-泉州   台北-福州   台北-昆明   台北-兰州   台北-拉萨   台北-连云港   台北-丽江   台北-洛阳   台北-澳门   台北-南昌   台北-南京   台北-南宁   台北-宁波   台北-青岛   台北-秦皇岛   台北-三亚   台北-上海   台北-深圳   台北-沈阳   台北-石家庄   台北-苏州   台北-太原   台北-天津   台北-温州   台北-武汉   台北-乌鲁木齐   台北-厦门   台北-西安   台北-西宁   台北-徐州   台北-烟台   台北-银川   台北-义乌   台北-郑州   台北-珠海   台北-桃园   台北-无锡   台北-烟台   台北-包头   " +
                " 桃园-北京   桃园-长春   桃园-长沙   桃园-常州   桃园-成都   桃园-重庆   桃园-大连   桃园-大理   桃园-广州   桃园-桂林   桃园-贵阳   桃园-哈尔滨   桃园-海口   桃园-杭州   桃园-合肥   桃园-香港   桃园-黄山   桃园-呼和浩特   桃园-吉林   桃园-济南   桃园-泉州   桃园-福州   桃园-昆明   桃园-兰州   桃园-拉萨   桃园-连云港   桃园-丽江   桃园-洛阳   桃园-澳门   桃园-南昌   桃园-南京   桃园-南宁   桃园-宁波   桃园-青岛   桃园-秦皇岛   桃园-三亚   桃园-上海   桃园-深圳   桃园-沈阳   桃园-石家庄   桃园-苏州   桃园-太原   桃园-天津   桃园-温州   桃园-武汉   桃园-乌鲁木齐   桃园-厦门   桃园-西安   桃园-西宁   桃园-徐州   桃园-烟台   桃园-银川   桃园-义乌   桃园-郑州   桃园-珠海   桃园-台北   桃园-无锡   桃园-烟台   桃园-包头   " +
                " 无锡-北京   无锡-长春   无锡-长沙   无锡-常州   无锡-成都   无锡-重庆   无锡-大连   无锡-大理   无锡-广州   无锡-桂林   无锡-贵阳   无锡-哈尔滨   无锡-海口   无锡-杭州   无锡-合肥   无锡-香港   无锡-黄山   无锡-呼和浩特   无锡-吉林   无锡-济南   无锡-泉州   无锡-福州   无锡-昆明   无锡-兰州   无锡-拉萨   无锡-连云港   无锡-丽江   无锡-洛阳   无锡-澳门   无锡-南昌   无锡-南京   无锡-南宁   无锡-宁波   无锡-青岛   无锡-秦皇岛   无锡-三亚   无锡-上海   无锡-深圳   无锡-沈阳   无锡-石家庄   无锡-苏州   无锡-太原   无锡-天津   无锡-温州   无锡-武汉   无锡-乌鲁木齐   无锡-厦门   无锡-西安   无锡-西宁   无锡-徐州   无锡-烟台   无锡-银川   无锡-义乌   无锡-郑州   无锡-珠海   无锡-台北   无锡-桃园   无锡-烟台   无锡-包头   " +
                " 烟台-北京   烟台-长春   烟台-长沙   烟台-常州   烟台-成都   烟台-重庆   烟台-大连   烟台-大理   烟台-广州   烟台-桂林   烟台-贵阳   烟台-哈尔滨   烟台-海口   烟台-杭州   烟台-合肥   烟台-香港   烟台-黄山   烟台-呼和浩特   烟台-吉林   烟台-济南   烟台-泉州   烟台-福州   烟台-昆明   烟台-兰州   烟台-拉萨   烟台-连云港   烟台-丽江   烟台-洛阳   烟台-澳门   烟台-南昌   烟台-南京   烟台-南宁   烟台-宁波   烟台-青岛   烟台-秦皇岛   烟台-三亚   烟台-上海   烟台-深圳   烟台-沈阳   烟台-石家庄   烟台-苏州   烟台-太原   烟台-天津   烟台-温州   烟台-武汉   烟台-乌鲁木齐   烟台-厦门   烟台-西安   烟台-西宁   烟台-徐州   烟台-烟台   烟台-银川   烟台-义乌   烟台-郑州   烟台-珠海   烟台-台北   烟台-桃园   烟台-无锡   烟台-包头   " +
                " 包头-北京   包头-长春   包头-长沙   包头-常州   包头-成都   包头-重庆   包头-大连   包头-大理   包头-广州   包头-桂林   包头-贵阳   包头-哈尔滨   包头-海口   包头-杭州   包头-合肥   包头-香港   包头-黄山   包头-呼和浩特   包头-吉林   包头-济南   包头-泉州   包头-福州   包头-昆明   包头-兰州   包头-拉萨   包头-连云港   包头-丽江   包头-洛阳   包头-澳门   包头-南昌   包头-南京   包头-南宁   包头-宁波   包头-青岛   包头-秦皇岛   包头-三亚   包头-上海   包头-深圳   包头-沈阳   包头-石家庄   包头-苏州   包头-太原   包头-天津   包头-温州   包头-武汉   包头-乌鲁木齐   包头-厦门   包头-西安   包头-西宁   包头-徐州   包头-烟台   包头-银川   包头-义乌   包头-郑州   包头-珠海   包头-台北   包头-桃园   包头-无锡   包头-烟台   ";
        String[] spString = all.split("\\s+");
        for(int i=0;i<spString.length;i++){
            String[] the = spString[i].split("-");
            if(startcity.get(the[0])!=null){
                ArrayList xin = (ArrayList) startcity.get(the[0]);
                xin.add(the[1]);
                startcity.put(the[0],xin);
            }else {
                ArrayList xin = new ArrayList();
                xin.add(the[1]);
                startcity.put(the[0],xin);
            }
            if(endcity.get(the[1])!=null){
                ArrayList xin = (ArrayList) endcity.get(the[1]);
                xin.add(the[0]);
                endcity.put(the[1],xin);
            }else{
                ArrayList xin = new ArrayList();
                xin.add(the[0]);
                endcity.put(the[1],xin);
            }
        }
    }

//    一系列与全局变量匹配的方法
    public int getEndflag(){
        Log.d(TAG, "getEndflag: kkkkaaaaa");
        return endflag[0];
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
