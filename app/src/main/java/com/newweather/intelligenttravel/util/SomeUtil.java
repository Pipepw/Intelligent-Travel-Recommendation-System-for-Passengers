package com.newweather.intelligenttravel.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.newweather.intelligenttravel.Gson.LngAndLat;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class SomeUtil {
    private static String startLng;
    private static String startLat;
    private static String endLng;
    private static String endLat;
    /**
     * 获取时间的结果,如果时间小于的话，则返回"false"，否则返回花费的时间（String型）
     * 所以判断时间大小的时候，就看结果是不是“false”,如果前小于后，则返回false
     * @param EndTime
     * @param StartTime
     * @return
     */
    public static String getTime(String EndTime, String StartTime) {
        String[] spString1 = StartTime.split(":");
        String[] spString2 = EndTime.split(":");
        int StartHour = Integer.valueOf(spString1[0]);
        int StartMine = Integer.valueOf(spString1[1]);
        int EndHour = Integer.valueOf(spString2[0]);
        int EndMine = Integer.valueOf(spString2[1]);
        int UseHour = EndHour - StartHour;
        int UseMine = EndMine - StartMine;
        String UseTime = UseHour + ":" + UseMine;
        if(UseHour < 0 ||(UseHour == 0 && UseMine < 0)){
            return "false";
        }else{
            return UseTime;
        }
    }

    /**
     * 获取时间相加的结果
     */
    public static String AddTime(String time1, String time2){
        String[] spString1 = time1.split(":");
        String[] spString2 = time2.split(":");
        int time1_H = Integer.valueOf(spString1[0]);
        int time1_M = Integer.valueOf(spString1[1]);
        int time2_H = Integer.valueOf(spString2[0]);
        int time2_M = Integer.valueOf(spString2[1]);
        int getHour = time1_H+time2_H;
        int getMine = time1_M+time2_M;
        if(getMine>=60){
            getMine %= 60;
            getHour++;
        }
        return getHour+":"+getMine;
    }
    public static String AddTime(String time1, String time2, String time3){
        String[] spString1 = time1.split(":");
        String[] spString2 = time2.split(":");
        String[] spString3 = time3.split(":");
        int time1_H = Integer.valueOf(spString1[0]);
        int time1_M = Integer.valueOf(spString1[1]);
        int time2_H = Integer.valueOf(spString2[0]);
        int time2_M = Integer.valueOf(spString2[1]);
        int time3_H = Integer.valueOf(spString3[0]);
        int time3_M = Integer.valueOf(spString3[1]);
        int getHour = time1_H+time2_H+time3_H;
        int getMine = time1_M+time2_M+time3_M;
        if(getMine>=60){
            getMine %= 60;
            getHour++;
        }
        return getHour+":"+getMine;
    }



    /**
     * 获取城市的经纬度
     * @param startCity
     */
    public static void GetLaL(Context context, Handler LaLHandler, final String startCity, final String endCity){
        String startLaL = "https://apis.map.qq.com/ws/geocoder/v1/?address="+startCity+"&key=3BYBZ-U6DKI-L5WGH-5EQXL-PJCBZ-LQFMK";
        String endLaL = "https://apis.map.qq.com/ws/geocoder/v1/?address="+endCity+"&key=3BYBZ-U6DKI-L5WGH-5EQXL-PJCBZ-LQFMK";
        SharedPreferences pref = context.getSharedPreferences("LaL",Context.MODE_PRIVATE);
//        内存中没有时才访问网络，有的话就直接读取
        if(pref.getString(startCity+"Lat","").equals("")){
            for(int i=0;i<10000;i++);
            HttpUtil.sendOkHttpRequest(startLaL, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "onFailure: get startLaL failed");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final LngAndLat lngAndLat = Utility.handleLngALat(responseText);
//                Lng为经度，Lat为维度
                    if(lngAndLat!=null){
                        startLng = LngAndLat.result.location.lng;
                        startLat = LngAndLat.result.location.lat;
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = context.getSharedPreferences("LaL",Context.MODE_PRIVATE).edit();
                        editor.putString(startCity+"Lat",LngAndLat.result.location.lat);
                        editor.putString(startCity+"Lng",LngAndLat.result.location.lng);
                        Message msg = new Message();
                        msg.what = 1;
                        LaLHandler.sendMessage(msg);
                    }
                }
            });
        }else {
            startLng = pref.getString(startCity+"Lng","");
            startLat = pref.getString(startCity+"Lat","");
            Message msg = new Message();
            msg.what = 1;
            LaLHandler.sendMessage(msg);
        }
        if(pref.getString(endCity+"Lat","").equals("")){
            for(int i=0;i<10000;i++);
            //        获取结束时的经纬度
            HttpUtil.sendOkHttpRequest(endLaL, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "onFailure: get endLaL failed");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();

                    Log.d(TAG, "onResponse: kkk response = " + responseText);
                    final LngAndLat lngAndLat = Utility.handleLngALat(responseText);
//                Lng为经度，Lat为维度
                    assert lngAndLat != null;
                    endLng = LngAndLat.result.location.lng;
                    endLat = LngAndLat.result.location.lat;
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = context.getSharedPreferences("LaL",Context.MODE_PRIVATE).edit();
                    editor.putString(endCity+"Lat",LngAndLat.result.location.lat);
                    editor.putString(endCity+"Lng",LngAndLat.result.location.lng);
                    Message msg = new Message();
                    msg.what = 2;
                    LaLHandler.sendMessage(msg);
                }
            });
        }else{
            endLng = pref.getString(endCity+"Lng","");
            endLat = pref.getString(endCity+"Lat","");
            Message msg = new Message();
            msg.what = 2;
            LaLHandler.sendMessage(msg);
        }
    }

    /**
     * 将字符串1234转换为时间12:34
     * @param time_str
     * @return
     */
    public static String TransToTime(String time_str){
        String str_hour = time_str.substring(0,2);
        String str_mine = time_str.substring(2,4);
//        Log.d(TAG, "TransToTime: kkk hour = " + str_hour);
//        Log.d(TAG, "TransToTime: kkk mine = " + str_mine);
        String time = str_hour + ":"+str_mine;
//        Log.d(TAG, "TransToTime: kkk time = " + time);
        return time;
    }

    public static String getStartLng() {
        return startLng;
    }

    public static void setStartLng(String startLng) {
        SomeUtil.startLng = startLng;
    }

    public static String getStartLat() {
        return startLat;
    }

    public static void setStartLat(String startLat) {
        SomeUtil.startLat = startLat;
    }

    public static String getEndLng() {
        return endLng;
    }

    public static void setEndLng(String endLng) {
        SomeUtil.endLng = endLng;
    }

    public static String getEndLat() {
        return endLat;
    }

    public static void setEndLat(String endLat) {
        SomeUtil.endLat = endLat;
    }
}
