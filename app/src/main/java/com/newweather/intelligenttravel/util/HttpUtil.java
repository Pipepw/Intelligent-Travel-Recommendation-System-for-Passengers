package com.newweather.intelligenttravel.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 获取网页
 */
public class HttpUtil {
    //请求数据
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
