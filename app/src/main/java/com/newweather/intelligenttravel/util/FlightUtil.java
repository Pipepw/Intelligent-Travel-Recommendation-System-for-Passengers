package com.newweather.intelligenttravel.util;

import android.util.Log;

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
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static android.content.ContentValues.TAG;

/**
 * 转换Code，获取航班信息
 */
public class FlightUtil {

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

    //    获取航班信息
    public static Flight GetFligt(final String StartCity, final String EndCity, final String StartDate, final String Time) {
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
                    //        获取具体的ip以及port
                    String AllString = GetIp();
                    Vector<String> ip_vec = new Vector<>();
                    Vector<String> port_vec = new Vector<>();
                    int k = 0;
                    int p = 0;
                    //Log.d(TAG, "call: kkk    " + AllString);
                    //返回的是所有的数据
                    String[] ip_port = AllString.split("\\s+");
                    for (int i = 0; i < ip_port.length; i++) {
                        //Log.d(TAG, "call: kkk    " + ip_port[i]);（思路是正确的，查看一下是不是ip_port越界了，但是当不是它越界的时候，为什么不想一下是不是其他的数组越界了呢？
                        //为什么i到7就停下来了，明明ip_port.length的大小是135
                        //ip_port可以执行到i+1，说明没有越界，i的值也不会改变，那么为什么会停下来呢？
                        //出不了循环，程序就终结了
                        //Log.d(TAG, "call: kkk    " + ip_port[i]);
                        //答案揭晓，是ip数组越界了，因为我不熟悉String的用法，其实和C++差不多的，所以String[] ip = new String[1]的意思就是创建一个大小为1的数组，当然就越界了
                        if (i == 0 || i % 9 == 0) {
                            ip_vec.add(ip_port[i]);
                            port_vec.add(ip_port[i+1]);
                        }
                    }
//                    设置HTTPURLConnection的请求头
                    for (int m = 0; m < ip_vec.size(); m++) {
                        try {
                            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip_vec.get(m), Integer.parseInt(port_vec.get(m))));
                            URL url = new URL("http://www.161580.com/tools/query.aspx?t=0.11355620486783125&sc="
                                    + StartCity + "&ec=" + EndCity + "&sd=" + StartDate + "&too=0");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
//                            设置必要的头部信息
                            connection.setRequestMethod("GET");
                            connection.setRequestProperty("User-agent", ua[j]);
                            connection.setConnectTimeout(100000);
                            connection.setReadTimeout(100000);
                            connection.setDoInput(true);
                            connection.setDoInput(true);
                            connection.connect();
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
                                            Log.d(TAG, "call: kkk000000");
                                            break;
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if(flight.getFare()!=null){
                                break;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
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
    //    获取代理ip
//    用来获取ip和port的整体数据，具体的获取还需要进行截取，其中ip为0+7i，port为1+7i,
//    这个部分是没有问题的
    public static String GetIp(){

        ExecutorService executor = Executors.newSingleThreadExecutor();
        final String[] result = new String[1];
        FutureTask<String> f1 = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Document doc = null;
                Elements els = null;
                try {
                    doc = Jsoup.connect("https://www.kuaidaili.com/free/").get();
                    els = doc.select("td");
                    result[0] = els.text();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result[0];
            }
        });
        executor.execute(f1);
        String rs = null;
        try {
            rs = f1.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Log.d(TAG, "GetIp: kkk   " + rs);
        return rs;
    }

//    加个对话框，在加载的时候弹出来，不然会以为是卡住了
}

