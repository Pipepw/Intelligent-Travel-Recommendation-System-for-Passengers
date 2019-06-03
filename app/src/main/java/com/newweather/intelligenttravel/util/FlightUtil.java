package com.newweather.intelligenttravel.util;

import com.newweather.intelligenttravel.Entity.Flight;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


/**
 * 转换Code，获取航班信息
 */
public class FlightUtil {



    //    获取航班信息
    public static Flight GetFligt(final String StartCity, final String EndCity, final String StartDate, final String Time) {
        final String StartCode = TransCode(StartCity);
        final String EndCode = TransCode(EndCity);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<Flight> future = new FutureTask<>(new Callable<Flight>() {
            @Override
            public Flight call() throws Exception {
//                常用的user-agent
                Random r = new Random();
                String[] ua = {"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
                        "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
                        "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
                        "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
                        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
                        "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0"};
                int j = r.nextInt(14);
                final Flight flight = new Flight();
                try {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("118.178.124.208", 80));
                    URL url = new URL("http://www.161580.com/tools/query.aspx?t=0.11355620486783125&sc="
                            + StartCode + "&ec=" + EndCode + "&sd=" + StartDate + "&too=0");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
//                            设置必要的头部信息
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("User-agent", ua[j]);
                    connection.setRequestProperty("Host", "www.161580.com");
                    connection.setRequestProperty("Connection", "alive");
                    connection.setRequestProperty("Cookie", "ASP.NET_Sessionld=fpajva2yirzqwowrfau3nivf");
                    connection.setRequestProperty("Referer", "http://www.161580.com/flight/showfarefirst.aspx?from=SHA&to=CKG&__today__=20190603&FlightType=1&fc=%E4%B8%8A%E6%B5%B7&tc=%E9%87%8D%E5%BA%86&date=2019-06-30");
                    connection.setConnectTimeout(100000);
                    connection.setReadTimeout(100000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
//                            将获取的数据转换为输入流
                    InputStream is = connection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
                    StringBuffer bs = new StringBuffer();
                    String I = null;
                    while ((I = buffer.readLine()) != null) {
                        bs.append(I);
                    }
//                            将获取的数据转换为类网页信息
                    Document doc = Jsoup.parse(bs.toString());
                    Elements els = doc.select(".newstyledetails1");
                    String[] spString = els.text().split("\\s+");
//                            一个循环是九个数据，其中0：出发时间，1：到达时间，2.出发站点，3.到达站点,7，费用（包含非数字部分）
                    for (int i = 0; i < spString.length; i++) {
                        if (i == 0 || i % 9 == 0) {
                            DateFormat df = new SimpleDateFormat("HH:mm");
                            try {
                                Date dt1 = df.parse(Time);
                                Date dt2 = df.parse(spString[i]);
//                                比较时间大小
                                if (dt2.getTime() > dt1.getTime()) {
                                    flight.setStartTime(spString[i]);
                                    flight.setEndTime(spString[i + 1]);
                                    flight.setStartStation(spString[i + 2]);
                                    flight.setEndStation(spString[i + 3]);
                                    String fare = TransNum(spString[i + 7]);
                                    flight.setFare(fare);
                                    //Log.d(TAG, "call: kkk000000");
                                    break;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return flight;
            }
        });
        executor.execute(future);
        Flight flight = new Flight();
        try {
            flight = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return flight;
    }

    //    转化为纯数字
    private static String TransNum(String Str) {
        String Str1 = "";
        Str = Str.trim();
        for (int i = 0; i < Str.length(); i++) {
            if (Str.charAt(i) >= 48 && Str.charAt(i) <= 57) {
                Str1 += Str.charAt(i);
            }
        }
        return Str1;
    }

    //    将城市名转换为CityCode
    public static String TransCode(final String CityName) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
//        使用FutureTask开启线程，才能有返回值
        FutureTask<String> future = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                final String[] Code = new String[1];
                Document doc = null;
                Elements els = null;
                try {
                    doc = Jsoup.connect("https://airportcode.51240.com/" + CityName + "__airportcodesou/").get();
                    els = doc.select("td");
                    String[] spString = els.text().split("\\s+");
                    Code[0] = spString[6];
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return Code[0];
            }
        });
//        获取从FutureTask中返回的值，并将其返回到主界面中
        executor.execute(future);
        String Code = null;
        try {
            Code = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Code;
    }

}

