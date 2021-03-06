package com.newweather.intelligenttravel.util;


import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newweather.intelligenttravel.Entity.Segments;
import com.newweather.intelligenttravel.Entity.Subway;
import com.newweather.intelligenttravel.Entity.Train;
import com.newweather.intelligenttravel.Gson.LngAndLat;
import com.newweather.intelligenttravel.db.City;
import com.newweather.intelligenttravel.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 保存返回的数据
 */
public class Utility {

    //解析trainroute数据
//    在这里面进行选择

    public static List<Segments> handleTrainRouteResponse(String response){
        try{
            String cost;
            String duration;
            //response已经是获取到的内容了
            JSONObject jsonObject=new JSONObject(response);
            JSONObject jsonObject1=jsonObject.getJSONObject("route");
            JSONArray jsonArray=jsonObject1.getJSONArray("transits");
            JSONObject jsonObject2=jsonArray.getJSONObject(0);
            cost=jsonObject2.getString("cost");
            duration = jsonObject2.getString("duration");
            JSONArray jsonArray1=jsonObject2.getJSONArray("segments");
            String trainRouteContent1=jsonArray1.toString();
            Gson gson=new Gson();
            Type type=new TypeToken<List<Segments>>(){}.getType();
            List<Segments> list=gson.fromJson(trainRouteContent1,type);
            ArrayList<Segments> trueList = new ArrayList<>();
            for(Segments segments:list){
                if(segments.railway.name!=null){
                    segments.railway.arrival_stop.arrival_time=SomeUtil.TransToTime(segments.railway.arrival_stop.arrival_time);
                    segments.railway.departure_stop.departure_time=SomeUtil.TransToTime(segments.railway.departure_stop.departure_time);
                    trueList.add(segments);
                }
            }
            trueList.get(0).cost = cost;
            trueList.get(0).usetime = SomeUtil.TransToTime(duration);
            return trueList;
            //return new Gson().fromJson(trainRouteContent1, Segments.class);
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

    /**
     * 解析经纬度api
     */
    public static LngAndLat handleLngALat(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            String LaLContent=jsonObject.toString();
//            Log.d(TAG, "handleLngALat: kkk content = " + LaLContent);
            return new Gson().fromJson(LaLContent, LngAndLat.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过高德地图获取公交时间
     * 在这里面进行选择
     */
    public static int handleSubwayRouteResponse(String response){
        int subwaytime = 0;
        try{
            Log.d(TAG, "handleProvinceResponse: kkk utility xiancheng = " + Thread.currentThread().getName());
            JSONObject jsonObject=new JSONObject(response);
            JSONObject jsonObject1=jsonObject.getJSONObject("route");
            JSONArray jsonArray=jsonObject1.getJSONArray("transits");
            JSONObject jsonObject2=jsonArray.getJSONObject(0);
            JSONArray jsonArray1=jsonObject2.getJSONArray("segments");
            String trainRouteContent1=jsonArray1.toString();
            Gson gson=new Gson();
            Type type=new TypeToken<List<Segments>>(){}.getType();
//            List是返回来的所有数据
            List<Segments> list=gson.fromJson(trainRouteContent1,type);
            for(Segments segments:list){
                if(segments.walking!=null){
                    subwaytime +=segments.walking.duration;
                }
                if(segments.bus!=null){
                    subwaytime += segments.bus.buslines.get(0).busduration;
                }
            }
            //return new Gson().fromJson(trainRouteContent1, Segments.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return subwaytime;
    }

}
