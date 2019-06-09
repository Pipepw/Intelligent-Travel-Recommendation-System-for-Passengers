package com.newweather.intelligenttravel.util;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.newweather.intelligenttravel.Entity.Subway;
import com.newweather.intelligenttravel.Entity.Train;
import com.newweather.intelligenttravel.db.City;
import com.newweather.intelligenttravel.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
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
            return new Gson().fromJson(trainContent, Train.class);

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

            return new Gson().fromJson(subwayContent, Subway.class);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取省份和城市
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces = new JSONArray(response);
                for(int i = 0;i<allProvinces.length();i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCityResponse(String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for(int i = 0;i<allCities.length();i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
