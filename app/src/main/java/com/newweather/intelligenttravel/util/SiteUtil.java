package com.newweather.intelligenttravel.util;


import android.util.Log;

import com.newweather.intelligenttravel.Gson.LngAndLat;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class SiteUtil {
    /**
     * 获取城市的经纬度
     * @param City
     */
    public static void requestLaL(final String City){
        String lalUrl = "http://api.map.baidu.com/geocoder?address=" + City + "&output=json&key" +
                "=37492c0ee6f924cb5e934fa08c6b1676&city=%E5%8C%97%E4%BA%AC%E5%B8%82";
        HttpUtil.sendOkHttpRequest(lalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: failure!!!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final LngAndLat lngAndLat = Utility.handleLngALat(responseText);
                final String Lng = lngAndLat.result.location.lng;
                final String Lat = lngAndLat.result.location.lat;
                Log.d(TAG, "onResponse: kkk lng = " + Lng);
                Log.d(TAG, "onResponse: kkk lat = " + Lat);
            }
        });
    }
}
