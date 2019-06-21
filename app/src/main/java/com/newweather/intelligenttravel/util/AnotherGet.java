package com.newweather.intelligenttravel.util;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;

import com.newweather.intelligenttravel.Entity.Segments;
import com.newweather.intelligenttravel.Entity.TrueTrain;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import okhttp3.Call;
import okhttp3.Response;

import static org.litepal.LitePalBase.TAG;

/**
 * 获取火车的相关数据
 */
public class AnotherGet {
    private static TrueTrain trueTrain=new TrueTrain();
    private static int subwaytime;
    private static List<Segments> segmentsList;

    public static void getTrain(Map<String, String> LaLMap, final Handler myHandler, String startcity, String endcity, String date, final String time){
        final String[] startLng = {""};
        final String[] startLat = {""};
        final String[] endLng = {""};
        final String[] endLat = {""};
//        能够直接从map中获取经纬度时
        if(LaLMap.get(startcity)!=null&&LaLMap.get(endcity)!=null){
            String startLal = LaLMap.get(startcity);
            Log.d(TAG, "getTrain: kkk end city = " + endcity);
            Log.d(TAG, "getTrain: kkk start city = " +startcity);
            Log.d(TAG, "getTrain: kkk city = " + LaLMap.get(endcity));
            String endLal = LaLMap.get(endcity);
            String[] startsp = startLal.split("\\s+");
            String[] endsp = endLal.split("\\s+");
            startLng[0] = startsp[1];
            startLat[0] = startsp[0];
            endLng[0] = endsp[1];
            endLat[0] = endsp[0];
            if(!(startLng[0].equals(""))&&!(endLng[0].equals(""))){
                String trainUrl="https://restapi.amap.com/v3/direction/transit/integrated?key=1880efe9146aed667f188cd171df3f7c" +
                        "&origin="+ startLng[0] +","+ startLat[0] +"&destination="+ endLng[0] +","+ endLat[0] +"&city="+startcity+"&cityd="+endcity+"&strategy=0" +
                        "&date="+date+"&time="+time;
                Log.d(TAG, "handleMessage: kkk train get it? " + trainUrl);
                HttpUtil.sendOkHttpRequest(trainUrl, new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        Log.d(TAG, "onResponse: kkk xianchegn out = " + Thread.currentThread().getName());
                        final String responseText=response.body().string();
                        segmentsList= Utility.handleTrainRouteResponse(responseText);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("train", (ArrayList<? extends Parcelable>) segmentsList);
                        Message msg=new Message();
                        msg.what=1;
                        msg.setData(bundle);
                        myHandler.sendMessage(msg);

                    }
                });
            }
        }else{//不能从map中直接获取时，从api中获取
            @SuppressLint("HandlerLeak") Handler LaLHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.what==1) {
                        startLng[0] = SomeUtil.getStartLng();
                        startLat[0] = SomeUtil.getStartLat();

                    }
                    if(msg.what==2) {
                        endLng[0] = SomeUtil.getEndLng();
                        endLat[0] = SomeUtil.getEndLat();
                    }
                    //                出发城市以及目标城市的经纬度都获取了之后才开始后面的任务
                    if(!(startLng[0].equals(""))&&!(endLng[0].equals(""))){
                        String trainUrl="https://restapi.amap.com/v3/direction/transit/integrated?key=1880efe9146aed667f188cd171df3f7c" +
                                "&origin="+ startLng[0] +","+ startLat[0] +"&destination="+ endLng[0] +","+ endLat[0] +"&city="+startcity+"&cityd="+endcity+"&strategy=0" +
                                "&date="+date+"&time="+time;
                        Log.d(TAG, "handleMessage: train get it? " + trainUrl);
                        HttpUtil.sendOkHttpRequest(trainUrl, new okhttp3.Callback() {
                            @Override
                            public void onFailure(okhttp3.Call call, IOException e) {
                                e.printStackTrace();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                Log.d(TAG, "onResponse: kkk xianchegn out = " + Thread.currentThread().getName());
                                final String responseText=response.body().string();
                                segmentsList = Utility.handleTrainRouteResponse(responseText);
                                Bundle bundle = new Bundle();
                                ArrayList list = new ArrayList();
                                list.add(segmentsList);
                                bundle.putParcelableArrayList("train", (ArrayList<? extends Parcelable>) segmentsList);
                                Message msg=new Message();
                                msg.what=1;
                                msg.setData(bundle);
                                myHandler.sendMessage(msg);
                            }
                        });
                    }
                }
            };
            SomeUtil.GetLaL(LaLHandler,startcity,endcity);
        }
    }



    public static void GetSubway(Handler myHandler,String city,String start,String end){
        final String[] startLng = {""};
        final String[] startLat = {""};
        final String[] endLng = {""};
        final String[] endLat = {""};
        Log.d(TAG, "GetSubway: kkk sub start = "+ start);
        Log.d(TAG, "GetSubway: kkk sub end = "+end);
        @SuppressLint("HandlerLeak") Handler LaLHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1) {
                    startLng[0] = SomeUtil.getStartLng();
                    startLat[0] = SomeUtil.getStartLat();

                }
                if(msg.what==2) {
                    endLng[0] = SomeUtil.getEndLng();
                    endLat[0] = SomeUtil.getEndLat();
                }
                //                出发地点以及目标地点的经纬度都获取了之后才开始后面的任务
                if(!(startLng[0].equals(""))&&!(endLng[0].equals(""))){
                    String subwayUrl="https://restapi.amap.com/v3/direction/transit/integrated?key=0207fc16251cf7d3487948f8949cf2b7" +
                            "&origin="+ startLng[0] +","+ startLat[0] +"&destination="+ endLng[0] +","+ endLat[0] +"&city="+city;
                    Log.d(TAG, "handleMessage: sub get it? " + subwayUrl);
                    HttpUtil.sendOkHttpRequest(subwayUrl, new okhttp3.Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            Log.d(TAG, "onResponse: kkk Threadname = " + Thread.currentThread().getName());
                            final String responseText=response.body().string();
                            subwaytime = Utility.handleSubwayRouteResponse(responseText);
//                            Log.d(TAG, "onResponse: kkk subway = " + subwaytime);
                            Bundle bundle = new Bundle();
                            bundle.putInt("subawy",subwaytime);
                            Message msg=new Message();
                            msg.what=2;
                            msg.setData(bundle);
                            myHandler.sendMessage(msg);
                        }
                    });
                }
            }
        };
        SomeUtil.GetLaL(LaLHandler,start+"站",end);
    }

    /**
     * 将秒转化为时分
     */

    public static TrueTrain gettrain(){
        return trueTrain;
    }
    public static List<Segments> getTrainRouteList(){
        return segmentsList;
    }
    public static String GetSubwayTime(){
        return SomeUtil.Transtime(subwaytime);
    }
}
