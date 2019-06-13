package com.newweather.intelligenttravel.util;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.newweather.intelligenttravel.Entity.Subway;
import com.newweather.intelligenttravel.Entity.Train;
import com.newweather.intelligenttravel.Entity.TrueSubway;
import com.newweather.intelligenttravel.Entity.TrueTrain;
import com.newweather.intelligenttravel.Gson.Result1;
import com.newweather.intelligenttravel.Gson.list;
import com.newweather.intelligenttravel.MainActivity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AnotherGet {
    private static Train train;
    private static TrueTrain trueTrain=new TrueTrain();
    private static Subway subway;
    private static TrueSubway trueSubway=new TrueSubway();
    public static void getTrain(final Handler myHandler, String startcity, String endcity, String date, final String time){
        String trainUrl="https://api.jisuapi.com/train/station2s?appkey=e74dd71c6e53e1c1&start="+startcity+"&end="+endcity+"&date="+date;
        HttpUtil.sendOkHttpRequest(trainUrl, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                train= Utility.handleTrainResponse(responseText);
                Bundle bundle=new Bundle();
                for(list List:train.getResult().listList){
                    if(compare_date(List.getDeparturetime(),time)>0) {
                        trueTrain.setDeparturetime(List.getDeparturetime());
                        trueTrain.setArrivaltime(List.getArrivaltime());
                        trueTrain.setStation(List.getStation());
                        trueTrain.setEndstation(List.getEndstation());
                        trueTrain.setCosttime(List.getCosttime());
                        trueTrain.setTypename(List.getTypename());
                        trueTrain.setPriceyd(List.getPriceyd());
                        trueTrain.setPriceed(List.getPriceed());
                        Message msg = new Message();
                        msg.what = 1;
                        msg.setData(bundle);
                        myHandler.sendMessage(msg);
                        break;
                    }
                }
            }
        });
    }
    public static void getSubway(Handler myHandler,String city,String endcity,String start,String end){
        String subwayUrl = " https://api.jisuapi.com/transit/station2s?city="+city+"&endcity="+endcity+"&start="+start+"&end="+end+"&appkey=e74dd71c6e53e1c1";
        HttpUtil.sendOkHttpRequest(subwayUrl, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                subway = Utility.handleSubwayResponse(responseText);
                Bundle bundle=new Bundle();
                for(Result1 mresult:subway.ResultList){
                    trueSubway.setTotalduration(mresult.getTotalduration());
                    trueSubway.setTotalprice(mresult.getTotalprice());
                    Message msg=new Message();
                    msg.what=2;
                    msg.setData(bundle);
                    myHandler.sendMessage(msg);
                    break;
                }

            }
        });
    }
    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    public static TrueTrain gettraim(){
        return trueTrain;
    }
    public static TrueSubway getsubwayy(){
        return trueSubway;
    }
}
