package com.newweather.intelligenttravel.util;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.newweather.intelligenttravel.Entity.Segments;
import com.newweather.intelligenttravel.Entity.Subway;
import com.newweather.intelligenttravel.Entity.Train;
import com.newweather.intelligenttravel.Entity.TrueSubway;
import com.newweather.intelligenttravel.Entity.TrueTrain;
import com.newweather.intelligenttravel.Gson.Result1;
import com.newweather.intelligenttravel.Gson.list;
import com.newweather.intelligenttravel.MainActivity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static org.litepal.LitePalBase.TAG;

/**
 * 获取火车的相关数据
 */
public class AnotherGet {
    private static Train train;
    private static TrueTrain trueTrain=new TrueTrain();
    private static Subway subway;
    private static TrueSubway trueSubway=new TrueSubway();
    private static String startLng = "";
    private static String startLat = "";
    private static String endLng = "";
    private static String endLat = "";
    private static List<Segments> segmentsList;
    public static void getTrain(final Handler myHandler, String startcity, String endcity, String date, final String time){
        @SuppressLint("HandlerLeak") Handler GetHanlder = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                if(msg.what==1){
                    startLng = SomeUtil.getStartLng();
                    startLat = SomeUtil.getStartLat();
                }
                if(msg.what==2){
                    endLng = SomeUtil.getEndLng();
                    endLat = SomeUtil.getEndLat();
                }
//                出发城市以及目标城市的经纬度都获取了之后才开始后面的任务
                if(!(startLng.equals(""))&&!(endLng.equals(""))){
                    String trainUrl="https://restapi.amap.com/v3/direction/transit/integrated?key=0207fc16251cf7d3487948f8949cf2b7" +
                            "&origin="+ startLng +","+startLat+"&destination="+endLng+","+endLat+"&city="+startcity+"&cityd="+endcity+"&strategy=0" +
                            "&date="+date+"&time="+time;
                    HttpUtil.sendOkHttpRequest(trainUrl, new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        final String responseText=response.body().string();
                        //Bundle bundle=new Bundle();
                        //Log.d(TAG, "onResponse: aaabbkkk"+responseText);
                        segmentsList= Utility.handleTrainRouteResponse(responseText);
                        Log.d(TAG, "onResponse: kkk cost = " + Utility.getCost());
                        Message msg=new Message();
                        msg.what=1;
                        myHandler.sendMessage(msg);

                    }
                });
                }
            }
        };
//        获取经纬度
        SomeUtil.GetLaL(GetHanlder,startcity,endcity);
    }

//    获取地铁
    public static void getSubway(Handler myHandler,String city,String endcity,String start,String end){
        String subwayUrl = " https://api.jisuapi.com/transit/station2s?city="+city+"&endcity="+endcity+"&start="+start+"&end="+end+"&appkey=e74dd71c6e53e1c1";
        HttpUtil.sendOkHttpRequest(subwayUrl, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                subway = Utility.handleSubwayResponse(responseText);
                Bundle bundle=new Bundle();
                for(Result1 mresult:subway.ResultList){
                    trueSubway.setTotalduration(mresult.getTotalduration());
                    trueSubway.setTotalprice(mresult.getTotalprice());
                    Message msg=new Message();
                    msg.what=2;
                    msg.setData(bundle);
                    myHandler.sendMessage(msg);
                    break;
                }

            }
        });
    }
    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    public static TrueTrain gettrain(){
        return trueTrain;
    }
    public static TrueSubway getsubwayy(){
        return trueSubway;
    }
    public static List<Segments> getTrainRouteList(){
        return segmentsList;
    }
}
