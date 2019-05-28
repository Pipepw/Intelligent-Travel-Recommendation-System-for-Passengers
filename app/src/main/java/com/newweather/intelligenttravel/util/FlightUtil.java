package com.newweather.intelligenttravel.util;

import com.newweather.intelligenttravel.Entity.Flight;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 转换Code，获取航班信息
 */
public class FlightUtil {

    //    将城市名转换为CityCode
    public static String TransCode(final String CityName) {
        final String[] Code = new String[1];
        new Thread(new Runnable() {
            Document doc = null;
            Elements els = null;

            @Override
            public void run() {
                try {
                    doc = Jsoup.connect("https://airportcode.51240.com/" + CityName + "__airportcodesou/").get();
                    els = doc.select("td");
                    String[] spString = els.text().split("\\s+");
                    Code[0] = spString[6];
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return Code[0];
    }

    //    获取航班信息
    public static Flight GetFligt(final String StartCity, final String EndCity, final String StartDate, final String Time) {
        final Flight flight = new Flight();
        final int[] Flag = {0};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("http://www.161580.com/tools/query.aspx?t=0.11355620486783125&sc="
                            + StartCity + "&ec=" + EndCity + "&sd=" + StartDate + "&too=0").get();
                    Elements els = doc.select(".newstyledetails1");
                    String[] spString = els.text().split("\\s+");
//                    一个循环是九个数据，其中0：出发时间，1：到达时间，2.出发站点，3.到达站点,7，费用（包含非数字部分）
                    for (int i = 0; i < spString.length; i++) {
                        if (i == 0 || i % 9 == 0) {
                            DateFormat df = new SimpleDateFormat("HH:mm");
                            try {
                                Date dt1 = df.parse(Time);
                                Date dt2 = df.parse(spString[i]);
                                if (dt2.getTime() > dt1.getTime()) {
                                    Flag[0] = 1;
                                    flight.setStartTime(spString[i]);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String str = TransNum(spString[i+7]);
                            flight.setEndTime(spString[i + 1]);
                            flight.setStartStation(spString[i + 2]);
                            flight.setEndStation(spString[i + 3]);
                            flight.setFare(str);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return flight;
    }

//    转化为纯数字
    public static String TransNum(String Str){
        String Str1="";
        Str = Str.trim();
        for(int i=0;i<Str.length();i++){
            if(Str.charAt(i)>=48&&Str.charAt(i)<=57){
                Str1+=Str.charAt(i);
            }
        }
        return Str1;
    }
}

