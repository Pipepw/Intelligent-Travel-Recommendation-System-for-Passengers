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
    private static String Lng;
    private static String Lat;
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
     * @param City
     */
    public static void GetLaL(Handler LaLHandler, final String City){
        String lalUrl = "https://apis.map.qq.com/ws/geocoder/v1/?address="+City+"&key=3BYBZ-U6DKI-L5WGH-5EQXL-PJCBZ-LQFMK";
        HttpUtil.sendOkHttpRequest(lalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: kkk have it failed?");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: kkk have it ? ? ?");
                final String responseText = response.body().string();
                final LngAndLat lngAndLat = Utility.handleLngALat(responseText);
//                Lng为经度，Lat为维度
                Lng = lngAndLat.result.location.lng;
                Lat = lngAndLat.result.location.lat;
                Message msg = new Message();
                msg.what = 1;
                LaLHandler.sendMessage(msg);
            }
        });
    }

    public static String getLng() {
        return Lng;
    }

    public static void setLng(String lng) {
        Lng = lng;
    }

    public static String getLat() {
        return Lat;
    }

    public static void setLat(String lat) {
        Lat = lat;
    }
}
