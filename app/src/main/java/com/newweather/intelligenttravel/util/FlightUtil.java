package com.newweather.intelligenttravel.util;

import android.content.SharedPreferences;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static android.content.ContentValues.TAG;


/**
 * 转换Code，获取航班信息
 */
public class FlightUtil {




    //    获取航班信息
    public static Flight GetFligt(final String StartCity, final String EndCity, final String StartDate, final String Time) {
        final String StartCode = TransCode(StartCity);
        final String EndCode = TransCode(EndCity);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<Flight> future = new FutureTask<>(() -> {
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
                Elements els_fuck = doc.getElementsByTag("strong");
                String[] spFare = els_fuck.text().split("\\s+");
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
                                for(int m = 0;m<spFare.length;m++){
                                    if(spFare[m].equals(spString[i])){
                                        String fare = spFare[m+1];
                                        flight.setFare(fare);
                                    }
                                }
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

    //    通过城市名与机场对应的map
    public static Map<String,List<String>> GetAllAirport(){
        Map<String, List<String>> AirportMap = new HashMap<>();
        String All = "AKA 安康 五里铺机场 陕西 AKU 阿克苏 温宿机场 新疆 AQG 安庆 大龙山机场 安徽 AYN 安阳 安阳机场 河南 BAV 包头 海兰泡机场 内蒙古 BHY 北海 福成机场 广西 BPX 昌都 昌都马草机场 西藏 BSD 保山 保山机场 云南 CAN 广州 白云国际机场 广东 CGD 常德 桃花机场 湖南 CGO 郑州 新郑国际机场 河南 CHG 朝阳 大房身机场 吉林 CHW 酒泉 酒泉机场 甘肃 CIF 赤峰 玉龙国际机场 内蒙古 CIH 长治 玉村机场 山西 CKG 重庆 江北国际机场 重庆 CNI 长海 大长山岛机场 辽宁 CSX 长沙 黄花国际机场 湖南 CTU 成都 双流国际机场 四川 CZX 常州 奔牛机场 江苏 DAT 大同 怀仁机场 山西 DAX 达州 河市机场 四川 DDG 丹东 浪头机场 辽宁 DIG 香格里拉 迪庆机场 云南 DLC 大连 周水子国际机场 辽宁 DLU 大理 大理机场 云南 DNH 敦煌 敦煌机场 甘肃 DOY 东营 东营机场 山东 DYG 张家界 荷花机场 湖南 ENH 恩施 许家坪机场 湖北 ENY 延安 二十里铺机场 陕西 FIG 阜阳 西关机场 安徽 FOC 福州 长乐国际机场 福建 FYN 富蕴 可可托托海机场 新疆 GHN 广汉 广汉机场 四川 GOQ 格尔木 格尔木机场 青海 HAK 海口 美兰国际机场 海南 HEK 黑河 黑河机场 黑龙江 HET 呼和浩特 白塔机场 内蒙古 HFE 合肥 骆岗机场 安徽 HGH 杭州 萧山国际机场 浙江 HJJ 怀化 芷江机场 湖南 HLD 海拉尔 东山机场 内蒙古 HLH 乌兰浩特 乌兰浩特机场 内蒙古 HMI 哈密 哈密机场 新疆 HNY 衡阳 衡阳机场 湖南 HRB 哈尔滨 闫家岗国际机场 黑龙江 HSN 舟山 普陀山机场 浙江 HTN 和田 和田机场 新疆 HYN 黄岩 路桥机场 浙江 HZG 汉中 西关机场 陕西 INC 银川 河东机场 宁夏 IQM 且末 且末机场 新疆 IQN 庆阳 西峰镇机场 甘肃 JDZ 景德镇 罗家机场 江西 JGN 嘉峪关 嘉峪关机场 甘肃 JGS 井冈山 井冈山机场 江西 JHG 西双版纳 景洪机场 云南 JIL 吉林 二台子机场 吉林 JIU 九江 庐山机场 江西 JJN 泉州 晋江机场 福建 JMU 佳木斯 东郊机场 黑龙江 JNZ 锦州 小岭子机场 辽宁 JUZ 衢州 衢州机场 浙江 JZH 九寨沟 黄龙机场 四川 KCA 库车 库车机场 新疆 KHG 喀什 喀什机场 新疆 KHN 南昌 昌北机场 江西 KMG 昆明 巫家坝国际机场 云南 KOW 赣州 黄金机场 江西 KRL 库尔勒 库尔勒机场 新疆 KRY 克拉玛依 克拉玛依机场 新疆 KWE 贵阳 龙洞堡机场 贵州 KWL 桂林 两江国际机场 广西 LCX 连城 连城机场 福建 LHW 兰州 中川机场 甘肃 LJG 丽江 丽江机场 云南 LNJ 临沧 临沧机场 云南 LUM 潞西 芒市机场 云南 LXA 拉萨 贡嘎机场 西藏 LYA 洛阳 北郊机场 河南 LYG 连云港 白塔埠机场 江苏 LYI 临沂 临沂机场 山东 LZH 柳州 白莲机场 广西 LZO 泸州 萱田机场 四川 MDG 牡丹江 海浪机场 黑龙江 MIG 绵阳 南郊机场 四川 MXZ 梅州 梅县机场 广东 NAO 南充 高坪机场 四川 NAY 北京 南苑机场 北京 NDG 齐齐哈尔 三家子机场 黑龙江 NGB 宁波 栎社机场 浙江 NKG 南京 禄口国际机场 江苏 NNG 南宁 吴墟机场 广西 NNY 南阳 姜营机场 河南 NTG 南通 兴东机场 江苏 PEK 北京 首都国际机场 北京 PVG 上海 浦江国际机场 上海 PZI 攀枝花 保安营机场 四川 SHA 上海 虹桥机场 上海 SHE 沈阳 桃仙机场 辽宁 SHP 山海关 秦皇岛机场 河北 SHS 荆州 沙市机场 湖北 SJW 石家庄 正定机场 河北 SWA 汕头 外砂机场 广东 SYM 思茅 思茅机场 云南 SYX 三亚 凤凰国际机场 海南 SZX 深圳 宝安国际机场 深圳�\n";
        String[] spString = All.split("\\s+");

        for(int i=0;i<spString.length;i++){
            Log.d(TAG, "GetAllAirport:  all kkk " + spString[i]);
            if((i+1)%4==0){
//                如果不存在，则增加一个新的list，存在则直接向其中添加
                if(AirportMap.get(spString[i-2])==null){
                    ArrayList<String> list = new ArrayList<>();
                    list.add(spString[i-1]);
                    AirportMap.put(spString[i-2],list);
                }else{
                    AirportMap.get(spString[i-2]).add(spString[i-1]);
                }
            }
        }
        return AirportMap;
    }

}

