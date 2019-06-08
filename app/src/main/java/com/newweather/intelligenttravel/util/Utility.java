package com.newweather.intelligenttravel.util;


import com.google.gson.Gson;
import com.newweather.intelligenttravel.Entity.Subway;
import com.newweather.intelligenttravel.Entity.Train;

import org.json.JSONObject;

/**
 * 保存返回的数据
 * 计算时间，获取附近地铁位置
 */
public class Utility {
    public static Train handleTrainResponse(String response){
        //解析train数据
        try {
            JSONObject jsonObject=new JSONObject(response);
            String trainContent=jsonObject.toString();
            //Log.d("ac",trainContent);
            return new Gson().fromJson(trainContent,Train.class);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Subway handleSubwayResponse(String response){
        //解析subway数据
        try {
            JSONObject jsonObject=new JSONObject(response);
            String subwayContent=jsonObject.toString();
            //Log.d("Util",subwayContent);

            return new Gson().fromJson(subwayContent,Subway.class);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
