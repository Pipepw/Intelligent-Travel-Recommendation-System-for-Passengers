package com.newweather.intelligenttravel.util;

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
     * 获取城市的经纬度
     * @param startCity
     */
    public static void GetLaL(Handler LaLHandler, final String startCity, final String endCity){
        String startLaL = "https://apis.map.qq.com/ws/geocoder/v1/?address="+startCity+"&key=3BYBZ-U6DKI-L5WGH-5EQXL-PJCBZ-LQFMK";
        String endLaL = "https://apis.map.qq.com/ws/geocoder/v1/?address="+endCity+"&key=3BYBZ-U6DKI-L5WGH-5EQXL-PJCBZ-LQFMK";
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
                startLng = lngAndLat.result.location.lng;
                startLat = lngAndLat.result.location.lat;
                Message msg = new Message();
                msg.what = 1;
                LaLHandler.sendMessage(msg);
            }
        });
        HttpUtil.sendOkHttpRequest(endLaL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: get endLaL failed");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final LngAndLat lngAndLat = Utility.handleLngALat(responseText);
//                Lng为经度，Lat为维度
                endLng = lngAndLat.result.location.lng;
                endLat = lngAndLat.result.location.lat;
                Message msg = new Message();
                msg.what = 2;
                LaLHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 将字符串转换为时间
     * @param time_str
     * @return
     */
    public static String TransToTime(String time_str){
        String str_hour = time_str.substring(0,2);
        String str_mine = time_str.substring(2,4);
        Log.d(TAG, "TransToTime: kkk hour = " + str_hour);
        Log.d(TAG, "TransToTime: kkk mine = " + str_mine);
        String time = str_hour + ":"+str_mine;
        Log.d(TAG, "TransToTime: kkk time = " + time);
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
