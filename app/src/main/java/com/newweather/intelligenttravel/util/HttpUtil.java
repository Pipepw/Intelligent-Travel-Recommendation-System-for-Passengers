package com.newweather.intelligenttravel.util;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static org.litepal.LitePalBase.TAG;

/**
 * 获取网页
 */
public class HttpUtil {
    //请求数据
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request
                .Builder()
                .url(address)
                .build();
        Log.d(TAG, "sendOkHttpRequest: kkk xiancheng = " + Thread.currentThread().getName());
        client.newCall(request).enqueue(callback);
    }

}
