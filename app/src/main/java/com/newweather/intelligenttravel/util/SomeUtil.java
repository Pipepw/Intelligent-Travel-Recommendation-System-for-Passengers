package com.newweather.intelligenttravel.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.newweather.intelligenttravel.Gson.LngAndLat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class SomeUtil {
    private static String startLng;
    private static String startLat;
    private static String endLng;
    private static String endLat;
    /**
     * 获取时间的结果,如果时间小于的话，则返回"false"，否则返回花费的时间（String型）
     * 所以判断时间大小的时候，就看结果是不是“false”,如果前小于后，则返回false
     * @param EndTime
     * @param StartTime
     * @return
     */
    public static String getTime(String EndTime, String StartTime) {
        Log.d(TAG, "getTime: Endtime kkk = ??? kkk " + EndTime +" and " + StartTime);
        String[] spString1 = StartTime.split(":");
        String[] spString2 = EndTime.split(":");
        int StartHour = Integer.valueOf(spString1[0]);
        int StartMine = Integer.valueOf(spString1[1]);
        int EndHour = Integer.valueOf(spString2[0]);
        int EndMine = Integer.valueOf(spString2[1]);
        int UseHour = EndHour - StartHour;
        int UseMine = EndMine - StartMine;
        String UseTime = UseHour + ":" + UseMine;
        if(UseHour < 0 ||(UseHour == 0 && UseMine < 0)){
            return "false";
        }else{
            return UseTime;
        }
    }

    /**
     * 获取时间相加的结果
     */
    public static String AddTime(String time1, String time2){
        String[] spString1 = time1.split(":");
        String[] spString2 = time2.split(":");
        int time1_H = Integer.valueOf(spString1[0]);
        int time1_M = Integer.valueOf(spString1[1]);
        int time2_H = Integer.valueOf(spString2[0]);
        int time2_M = Integer.valueOf(spString2[1]);
        int getHour = time1_H+time2_H;
        int getMine = time1_M+time2_M;
        if(getMine>=60){
            getMine %= 60;
            getHour++;
        }
        return getHour+":"+getMine;
    }
    public static String AddTime(String time1, String time2, String time3){
        String[] spString1 = time1.split(":");
        String[] spString2 = time2.split(":");
        String[] spString3 = time3.split(":");
        int time1_H = Integer.valueOf(spString1[0]);
        int time1_M = Integer.valueOf(spString1[1]);
        int time2_H = Integer.valueOf(spString2[0]);
        int time2_M = Integer.valueOf(spString2[1]);
        int time3_H = Integer.valueOf(spString3[0]);
        int time3_M = Integer.valueOf(spString3[1]);
        int getHour = time1_H+time2_H+time3_H;
        int getMine = time1_M+time2_M+time3_M;
        if(getMine>=60){
            getMine %= 60;
            getHour++;
        }
        return new DecimalFormat("00").format(getHour)+":"+ new DecimalFormat("00").format(getMine);
    }



    /**
     * 获取城市的经纬度
     */
    public static Map<String,String> GetLaL(){
        Map<String,String> LaLMap = new HashMap<>();
        String All = "北京 39.55 116.24" +
                " 天津 39.02 117.12" +
                " 上海 31.14 121.29" +
                " 重庆 29.35 106.33" +
                " 合川 30.02 106.15" +
                " 江津 29.18 106.16" +
                " 南川 29.10 107.05" +
                " 永川 29.23 105.53" +
                " 石家庄 38.02 114.30" +
                " 安国 38.24 115.20" +
                " 保定 38.51 115.30" +
                " 霸州 39.06 116.24" +
                " 泊头 38.04 116.34" +
                " 沧州 38.18 116.52" +
                " 承德 40.59 117.57" +
                " 定州 38.30 115.00" +
                " 丰南 39.34 118.06" +
                " 高碑店 39.20 115.51" +
                " 蒿城 38.02 114.50" +
                " 邯郸 36.36 114.28" +
                " 河间 38.26 116.05" +
                " 衡水 37.44 115.42" +
                " 黄骅 38.21 117.21" +
                " 晋州 38.02 115.02" +
                " 冀州 37.34 115.33" +
                " 廓坊 39.31 116.42" +
                " 鹿泉 38.04 114.19" +
                " 南宫 37.22 115.23" +
                " 秦皇岛 39.55 119.35" +
                " 任丘 38.42 116.07" +
                " 三河 39.58 117.04" +
                " 沙河 36.51 114.30" +
                " 深州 38.01 115.32" +
                " 唐山 39.36 118.11" +
                " 武安 36.42 114.11" +
                " 邢台 37.04 114.30" +
                " 辛集 37.54 115.12" +
                " 新乐 38.20 114.41" +
                " 张家口 40.48 114.53" +
                " 涿州 39.29 115.59" +
                " 遵化 40.11 117.58" +
                " 太原 37.54 112.33" +
                " 长治 36.11 113.06" +
                " 大同 40.06 113.17" +
                " 高平 35.48 112.55" +
                " 古交 37.54 112.09" +
                " 河津 35.35 110.41" +
                " 侯马 35.37 111.21" +
                " 霍州 36.34 111.42" +
                " 介休 37.02 111.55" +
                " 晋城 35.30 112.51" +
                " 临汾 36.05 111.31" +
                " 潞城 36.21 113.14" +
                " 朔州 39.19 112.26" +
                " 孝义 37.08 111.48" +
                " 忻州 38.24 112.43" +
                " 阳泉 37.51 113.34" +
                " 永济 34.52 110.27" +
                " 原平 38.43 112.42" +
                " 榆次 37.41 112.43" +
                " 运城 35.02 110.59" +
                " 呼和浩特 40.48 111.41" +
                " 包头 40.39 109.49" +
                " 赤峰 42.17 118.58" +
                " 东胜 39.48 109.59" +
                " 二连浩特 43.38 111.58" +
                " 额尔古纳 50.13 120.11" +
                " 丰镇 40.27 113.09" +
                " 根河 50.48 121.29" +
                " 海拉尔 49.12 119.39" +
                " 霍林郭勒 45.32 119.38" +
                " 集宁 41.02 113.06" +
                " 临河 40.46 107.22" +
                " 满洲里 49.35 117.23" +
                " 通辽 43.37 122.16" +
                " 乌兰浩特 46.03 122.03" +
                " 乌海 39.40 106.48" +
                " 锡林浩特 43.57 116.03" +
                " 牙克石 49.17 120.40" +
                " 扎兰屯 48.00 122.47" +
                " 沈阳 41.48 123.25" +
                " 鞍山 41.07 123.00" +
                " 北票 41.48 120.47" +
                " 本溪 41.18 123.46" +
                " 朝阳 41.34 120.27" +
                " 大连 38.55 121.36" +
                " 丹东 40.08 124.22" +
                " 大石桥 40.37 122.31" +
                " 东港 39.53 124.08" +
                " 凤城 40.28 124.02" +
                " 抚顺 41.51 123.54" +
                " 阜新 42.01 121.39" +
                " 盖州 40.24 122.21" +
                " 海城 40.51 122.43" +
                " 葫芦岛 40.45 120.51" +
                " 锦州 41.07 121.09" +
                " 开原 42.32 124.02" +
                " 辽阳 41.16 123.12" +
                " 凌海 41.10 121.21" +
                " 凌源 41.14 119.22" +
                " 盘锦 41.07 122.03" +
                " 普兰店 39.23 121.58" +
                " 铁法 42.28 123.32" +
                " 铁岭 42.18 123.51" +
                " 瓦房店 39.37 122.00" +
                " 兴城 40.37 120.41" +
                " 新民 41.59 122.49" +
                " 营口 40.39 122.13" +
                " 庄河 39.41 122.58" +
                " 长春 43.54 125.19" +
                " 白城 45.38 122.50" +
                " 白山 41.56 126.26" +
                " 大安 45.30 124.18" +
                " 德惠 44.32 125.42" +
                " 敦化 43.22 128.13" +
                " 公主岭 43.31 124.49" +
                " 和龙 42.32 129.00" +
                " 桦甸 42.58 126.44" +
                " 珲春 42.52 130.22" +
                " 集安 41.08 126.11" +
                " 蛟河 43.42 127.21" +
                " 吉林 43.52 126.33" +
                " 九台 44.09 125.51" +
                " 辽源 42.54 125.09" +
                " 临江 41.49 126.53" +
                " 龙井 42.46 129.26" +
                " 梅河口 42.32 125.40" +
                " 舒兰 44.24 126.57" +
                " 四平 43.10 124.22" +
                " 松原 45.11 124.49" +
                " 洮南 45.20 122.47" +
                " 通化 41.43 125.56" +
                " 图们 42.57 129.51" +
                " 延吉 42.54 129.30" +
                " 愉树 44.49 126.32" +
                " 哈尔滨 45.44 126.36" +
                " 阿城 45.32 126.58" +
                " 安达 46.24 125.18" +
                " 北安 48.15 126.31" +
                " 大庆 46.36 125.01" +
                " 富锦 47.15 132.02" +
                " 海林 44.35 129.21" +
                " 海伦 47.28 126.57" +
                " 鹤岗 47.20 130.16" +
                " 黑河 50.14 127.29" +
                " 佳木斯 46.47 130.22" +
                " 鸡西 45.17 130.57" +
                " 密山 45.32 131.50" +
                " 牡丹江 44.35 129.36" +
                " 讷河 48.29 124.51" +
                " 宁安 44.21 129.28" +
                " 齐齐哈尔 47.20 123.57" +
                " 七台河 45.48 130.49" +
                " 双城 45.22 126.15" +
                " 尚志 45.14 127.55" +
                " 双鸭山 46.38 131.11" +
                " 绥芬河 44.25 131.11" +
                " 绥化 46.38 126.59" +
                " 铁力 46.59 128.01" +
                " 同江 47.39 132.30" +
                " 五常 44.55 127.11" +
                " 五大连池 48.38 126.07" +
                " 伊春 47.42 128.56" +
                " 肇东 46.04 125.58" +
                " 南京 32.03 118.46" +
                " 常熟 31.39 120.43" +
                " 常州 31.47 119.58" +
                " 丹阳 32.00 119.32" +
                " 东台 32.51 120.19" +
                " 高邮 32.47 119.27" +
                " 海门 31.53 121.09" +
                " 淮安 33.30 119.09" +
                " 淮阴 33.36 119.02" +
                " 江都 32.26 119.32" +
                " 姜堰 32.34 120.08" +
                " 江阴 31.54 120.17" +
                " 靖江 32.02 120.17" +
                " 金坛 31.46 119.33" +
                " 昆山 31.23 120.57" +
                " 连去港 34.36 119.10" +
                " 溧阳 31.26 119.29" +
                " 南通 32.01 120.51" +
                " 邳州 34.19 117.59" +
                " 启乐 31.48 121.39" +
                " 如皋 32.23 120.33" +
                " 宿迁 33.58 118.18" +
                " 苏州 31.19 120.37" +
                " 太仓 31.27 121.06" +
                " 泰兴 32.10 120.01" +
                " 泰州 32.30 119.54" +
                " 通州 32.05 121.03" +
                " 吴江 31.10 120.39" +
                " 无锡 31.34 120.18" +
                " 兴化 32.56 119.50" +
                " 新沂 34.22 118.20" +
                " 徐州 34.15 117.11" +
                " 盐在 33.22 120.08" +
                " 扬中 32.14 119.49" +
                " 扬州 32.23 119.26" +
                " 宜兴 31.21 119.49" +
                " 仪征 32.16 119.10" +
                " 张家港 31.52 120.32" +
                " 镇江 32.11 119.27" +
                " 杭州 30.16 120.10" +
                " 慈溪 30.11 121.15" +
                " 东阳 29.16 120.14" +
                " 奉化 29.39 121.24" +
                " 富阳 30.03 119.57" +
                " 海宁 30.32 120.42" +
                " 湖州 30.52 120.06" +
                " 建德 29.29 119.16" +
                " 江山 28.45 118.37" +
                " 嘉兴 30.46 120.45" +
                " 金华 29.07 119.39" +
                " 兰溪 29.12 119.28" +
                " 临海 28.51 121.08" +
                " 丽水 28.27 119.54" +
                " 龙泉 28.04 119.08" +
                " 宁波 29.52 121.33" +
                " 平湖 30.42 121.01" +
                " 衢州 28.58 118.52" +
                " 瑞安 27.48 120.38" +
                " 上虞 30.01 120.52" +
                " 绍兴 30.00 120.34" +
                " 台州 28.41 121.27" +
                " 桐乡 30.38 120.32" +
                " 温岭 28.22 121.21" +
                " 温州 28.01 120.39" +
                " 萧山 30.09 120.16" +
                " 义乌 29.18 120.04" +
                " 乐清 28.08 120.58" +
                " 余杭 30.26 120.18" +
                " 余姚 30.02 121.10" +
                " 永康 29.54 120.01" +
                " 舟山 30.01 122.06" +
                " 诸暨 29.43 120.14" +
                " 合肥 31.52 117.17" +
                " 安庆 30.31 117.02" +
                " 蚌埠 32.56 117.21" +
                " 亳州 33.52 115.47" +
                " 巢湖 31.36 117.52" +
                " 滁州 32.18 118.18" +
                " 阜阳 32.54 115.48" +
                " 贵池 30.39 117.28" +
                " 淮北 33.57 116.47" +
                " 淮南 32.37 116.58" +
                " 黄山 29.43 118.18" +
                " 界首 33.15 115.21" +
                " 六安 31.44 116.28" +
                " 马鞍山 31.43 118.28" +
                " 明光 32.47 117.58" +
                " 宿州 33.38 116.58" +
                " 天长 32.41 118.59" +
                " 铜陵 30.56 117.48" +
                " 芜湖 31.19 118.22" +
                " 宣州 30.57 118.44" +
                " 福州 26.05 119.18" +
                " 长乐 25.58 119.31" +
                " 福安 27.06 119.39" +
                " 福清 25.42 119.23" +
                " 建瓯 27.03 118.20" +
                " 建阳 27.21 118.07" +
                " 晋江 24.49 118.35" +
                " 龙海 24.26 117.48" +
                " 龙岩 25.06 117.01" +
                " 南安 24.57 118.23" +
                " 南平 26.38 118.10" +
                " 宁德 26.39 119.31" +
                " 莆田 24.26 119.01" +
                " 泉州 24.56 118.36" +
                " 三明 26.13 117.36" +
                " 邵武 27.20 117.29" +
                " 石狮 24.44 118.38" +
                " 武夷山 27.46 118.02" +
                " 厦门 24.27 118.06" +
                " 永安 25.58 117.23" +
                " 漳平 25.17 117.24" +
                " 漳州 24.31 117.39" +
                " 南昌 28.40 115.55" +
                " 德兴 28.57 117.35" +
                " 丰城 28.12 115.48" +
                " 赣州 28.52 114.56" +
                " 高安 28.25 115.22" +
                " 吉安 27.07 114.58" +
                " 景德镇 29.17 117.13" +
                " 井冈山 26.34 114.10" +
                " 九江 29.43 115.58" +
                " 乐平 28.58 117.08" +
                " 临川 27.59 116.21" +
                " 萍乡 27.37 113.50" +
                " 瑞昌 29.40 115.38" +
                " 瑞金 25.53 116.01" +
                " 上饶 25.27 117.58" +
                " 新余 27.48 114.56" +
                " 宜春 27.47 114.23" +
                " 鹰潭 28.14 117.03" +
                " 樟树 28.03 115.32" +
                " 济南 36.40 117.00" +
                " 安丘 36.25 119.12" +
                " 滨州 37.22 118.02" +
                " 昌邑 39.52 119.24" +
                " 德州 37.26 116.17" +
                " 东营 37.27 118.30" +
                " 肥城 36.14 116.46" +
                " 高密 36.22 119.44" +
                " 菏泽 35.14 115.26" +
                " 胶南 35.53 119.58" +
                " 胶州 36.17 120.00" +
                " 即墨 36.22 120.28" +
                " 济宁 35.23 116.33" +
                " 莱芜 36.12 117.40" +
                " 莱西 36.52 120.31" +
                " 莱阳 36.58 120.42" +
                " 莱州 37.10 119.57" +
                " 乐陵 37.44 117.12" +
                " 聊城 36.26 115.57" +
                " 临清 36.51 115.42" +
                " 临沂 35.03 118.20" +
                " 龙口 37.39 120.21" +
                " 蓬莱 37.48 120.45" +
                " 平度 36.47 119.58" +
                " 青岛 36.03 120.18" +
                " 青州 36.42 118.28" +
                " 曲阜 35.36 116.58" +
                " 日照 35.23 119.32" +
                " 荣成 37.10 122.25" +
                " 乳山 36.54 121.31" +
                " 寿光 36.53 118.44" +
                " 泰安 36.11 117.08" +
                " 滕州 35.06 117.09" +
                " 潍坊 36.43 119.06" +
                " 威海 37.31 122.07" +
                " 文登 37.12 122.03" +
                " 新泰 35.54 117.45" +
                " 烟台 37.32 121.24" +
                " 兖州 35.32 116.49" +
                " 禹城 36.56 116.39" +
                " 枣庄 34.52 117.33" +
                " 章丘 36.43 117.32" +
                " 招远 37.21 120.23" +
                " 诸城 35.59 119.24" +
                " 淄博 36.48 118.03" +
                " 邹城 35.24 116.58" +
                " 郑州 34.46 11340" +
                " 安阳 36.06 114.21" +
                " 长葛 34.12 113.47" +
                " 登封 34.27 113.02" +
                " 邓州 32.42 112.05" +
                " 巩义 34.46 112.58" +
                " 鹤壁 35.54 114.11" +
                " 辉县 35.27 113.47" +
                " 焦作 35.14 113.12" +
                " 济源 35.04 112.35" +
                " 开封 34.47 114.21" +
                " 灵宝 34.31 110.52" +
                " 林州 36.03 113.49" +
                " 漯河 33.33 114.02" +
                " 洛阳 34.41 112.27" +
                " 南阳 33.00 112.32" +
                " 平顶山 33.44 113.17" +
                " 濮阳 35.44 115.01" +
                " 沁阳 35.05 112.57" +
                " 汝州 34.09 112.50" +
                " 三门峡 34.47 111.12" +
                " 商丘 34.26 115.38" +
                " 卫辉 35.24 114.03" +
                " 舞钢 33.17 113.30" +
                " 项城 33.26 114.54" +
                " 荥阳 34.46 113.21" +
                " 新密 34.31 113.22" +
                " 新乡 35.18 113.52" +
                " 信阳 32.07 114.04" +
                " 新郑 34.24 113.43" +
                " 许昌 34.01 113.49" +
                " 偃师 34.43 112.47" +
                " 义马 34.43 111.55" +
                " 禹州 34.09 113.28" +
                " 周口 33.37 114.38" +
                " 驻马店 32.58 114.01" +
                " 武汉 30.35 114.17" +
                " 安陆 31.15 113.41" +
                " 当阳 30.50 111.47" +
                " 丹江口 32.33 108.30" +
                " 大冶 30.06 114.58" +
                " 恩施 30.16 109.29" +
                " 鄂州 30.23 114.52" +
                " 广水 31.37 113.48" +
                " 洪湖 29.48 113.27" +
                " 黄石 30.12 115.06" +
                " 黄州 30.27 114.52" +
                " 荆门 31.02 112.12" +
                " 荆沙 30.18 112.16" +
                " 老河口 32.23 111.40" +
                " 利川 30.18 108.56" +
                " 麻城 31.10 115.01" +
                " 浦圻 29.42 113.51" +
                " 潜江 30.26 112.53" +
                " 石首 29.43 112.24" +
                " 十堰 32.40 110.47" +
                " 随州 31.42 113.22" +
                " 天门 60.39 113.10" +
                " 武穴 29.51 115.33" +
                " 襄樊 32.02 112.08" +
                " 咸宁 29.53 114.17" +
                " 仙桃 30.22 113.27" +
                " 孝感 30.56 113.54" +
                " 宜昌 30.42 111.17" +
                " 宜城 31.42 112.15" +
                " 应城 30.57 113.33" +
                " 枣阳 32.07 112.44" +
                " 枝城 30.23 111.27" +
                " 钟祥 31.10 112.34" +
                " 长沙 28.12 112.59" +
                " 常德 29.02 111.51" +
                " 郴州 25.46 113.02" +
                " 衡阳 26.53 112.37" +
                " 洪江 27.07 109.59" +
                " 怀化 27.33 109.58" +
                " 津市 29.38 111.52" +
                " 吉首 28.18 109.43" +
                " 耒阳 26.24 112.51" +
                " 冷水江 27.42 111.26" +
                " 冷水滩 26.26 111.35" +
                " 涟源 27.41 111.41" +
                " 醴陵 27.40 113.30" +
                " 临湘 29.29 113.27" +
                " 浏阳 28.09 113.37" +
                " 娄底 27.44 111.59" +
                " 汨罗 28.49 113.03" +
                " 韶山 27.54 112.29" +
                " 邵阳 27.14 111.28" +
                " 武冈 26.43 110.37" +
                " 湘潭 27.52 112.53" +
                " 湘乡 27.44 112.31" +
                " 益阳 28.36 112.20" +
                " 永州 26.13 111.37" +
                " 沅江 28.50 112.22" +
                " 岳阳 29.22 113.06" +
                " 张家界 29.08 110.29" +
                " 株洲 27.51 113.09" +
                " 资兴 25.58 113.13" +
                " 广州 23.08 113.14" +
                " 潮阳 23.16 116.36" +
                " 潮州 23.40 116.38" +
                " 澄海 23.28 116.46" +
                " 从化 23.33 113.33" +
                " 东莞 23.02 113.45" +
                " 恩平 22.12 112.19" +
                " 佛山 23.02 113.06" +
                " 高明 22.53 112.50" +
                " 高要 23.02 112.26" +
                " 高州 21.54 110.50" +
                " 鹤山 22.46 112.57" +
                " 河源 23.43 114.41" +
                " 花都 23.23 113.12" +
                " 化州 21.39 110.37" +
                " 惠阳 22.48 114.28" +
                " 惠州 23.05 114.22" +
                " 江门 22.35 113.04" +
                " 揭阳 22.32 116.21" +
                " 开平 22.22 112.40" +
                " 乐昌 25.09 113.21" +
                " 雷州 20.54 110.04" +
                " 廉江 21.37 110.17" +
                " 连州 24.48 112.23" +
                " 罗定 22.46 111.33" +
                " 茂名 21.40 110.53" +
                " 梅州 24.19 116.07" +
                " 南海 23.01 113.09" +
                " 番禺 22.57 113.22" +
                " 普宁 23.18 116.10" +
                " 清远 23.42 113.01" +
                " 三水 23.10 112.52" +
                " 汕头 23.22 116.41" +
                " 汕尾 22.47 115.21" +
                " 韶关 24.48 113.37" +
                " 深圳 22.33 114.07" +
                " 顺德 22.50 113.15" +
                " 四会 23.21 112.41" +
                " 台山 22.15 112.48" +
                " 吴川 21.26 110.47" +
                " 新会 22.32 113.01" +
                " 兴宁 24.09 115.43" +
                " 阳春 22.10 111.48" +
                " 阳江 21.50 111.58" +
                " 英德 24.10 113.22" +
                " 云浮 22.57 112.02" +
                " 增城 23.18 113.49" +
                " 湛江 21.11 110.24" +
                " 肇庆 23.03 112.27" +
                " 中山 22.31 113.22" +
                " 珠海 22.17 113.34" +
                " 南宁 22.48 108.19" +
                " 北海 21.28 109.07" +
                " 北流 22.42 110.21" +
                " 百色 23.54 106.36" +
                " 防城港 21.37 108.20" +
                " 贵港 23.06 109.36" +
                " 桂林 25.17 110.17" +
                " 桂平 23.22 110.04" +
                " 河池 24.42 108.03" +
                " 合山 23.47 108.52" +
                " 柳州 23.19 109.24" +
                " 赁祥 22.07 106.44" +
                " 钦州 21.57 108.37" +
                " 梧州 23.29 111.20" +
                " 玉林 22.38 110.09" +
                " 宜州 24.28 108.40" +
                " 海口 20.02 110.20" +
                " 儋州 19.31 109.34" +
                " 琼海 19.14 110.28" +
                " 琼山 19.59 110.21" +
                " 三亚 18.14 109.31" +
                " 通什 18.46 109.31" +
                " 成都 30.40 104.04" +
                " 巴中 31.51 106.43" +
                " 崇州 30.39 103.40" +
                " 达川 31.14 107.29" +
                " 德阳 31.09 104.22" +
                " 都江堰 31.01 103.37" +
                " 峨眉山 29.36 103.29" +
                " 涪陵 29.42 107.22" +
                " 广汉 30.58 104.15" +
                " 广元 32.28 105.51" +
                " 华蓥 30.26 106.44" +
                " 简阳 30.24 104.32" +
                " 江油 31.48 104.42" +
                " 阆中 31.36 105.58" +
                " 乐山 29.36 103.44" +
                " 泸州 28.54 105.24" +
                " 绵阳 31.30 104.42" +
                " 南充 30.49 106.04" +
                " 内江 29.36 105.02" +
                " 攀枝花 26.34 101.43" +
                " 彭州 30.59 103.57" +
                " 邛崃 30.26 103.28" +
                " 遂宁 30.31 105.33" +
                " 万县 30.50 108.21" +
                " 万源 32.03 108.03" +
                " 西昌 27.54 102.16" +
                " 雅安 29.59 102.59" +
                " 宜宾 28.47 104.34" +
                " 自贡 29.23 104.46" +
                " 资阳 30.09 104.38" +
                " 贵阳 26.35 106.42" +
                " 安顺 26.14 105.55" +
                " 毕节 27.18 105.18" +
                " 赤水 28.34 105.42" +
                " 都匀 26.15 107.31" +
                " 凯里 26.35 107.58" +
                " 六盘水 26.35 104.50" +
                " 清镇 26.33 106.27" +
                " 铜仁 27.43 109.12" +
                " 兴义 25.05 104.53" +
                " 遵义 27.42 106.55" +
                " 昆明 25.04 102.42" +
                " 保山 25.08 99.10" +
                " 楚雄 25.01 101.32" +
                " 大理 25.34 100.13" +
                " 东川 26.06 103.12" +
                " 个旧 23.21 103.09" +
                " 景洪 22.01 100.48" +
                " 开远 23.43 103.13" +
                " 曲靖 25.30 103.48" +
                " 瑞丽 24.00 97.50" +
                " 思茅 22.48 100.58" +
                " 畹町 24.06 98.04" +
                " 宣威 26.13 104.06" +
                " 玉溪 24.22 102.32" +
                " 昭通 27.20 103.42" +
                " 拉萨 29.39 91.08" +
                " 日喀则 29.16 88.51" +
                " 西安 34.17 108.57" +
                " 安康 32.41 109.01" +
                " 宝鸡 34.22 107.09" +
                " 韩城 35.28 110.27" +
                " 汉中 33.04 107.01" +
                " 华阴 34.34 110.05" +
                " 商州 33.52 109.57" +
                " 铜川 35.06 109.07" +
                " 渭南 34.30 109.30" +
                " 咸阳 34.20 108.43" +
                " 兴平 34.18 108.29" +
                " 延安 36.35 109.28" +
                " 榆林 38.18 109.47" +
                " 兰州 36.04 103.51" +
                " 白银 36.33 104.12" +
                " 敦煌 40.08 94.41" +
                " 嘉峪关 39.48 98.14" +
                " 金昌 38.28 102.10" +
                " 酒泉 39.44 98.31" +
                " 临夏 35.37 103.12" +
                " 平凉 35.32 106.40" +
                " 天水 34.37 105.42" +
                " 武威 37.56 102.39" +
                " 西峰 35.45 107.40" +
                " 玉门 39.49 97.35" +
                " 张掖 38.56 100.26" +
                " 西宁 36.38 101.48" +
                " 德令哈 37.22 97.23" +
                " 格尔木 36.26 94.55" +
                " 银川 38.27 106.16" +
                " 青铜峡 37.56 105.59" +
                " 石嘴山 39.02 106.22" +
                " 吴忠 37.59 106.11" +
                " 乌鲁木齐 43.45 87.36" +
                " 阿克苏 41.09 80.19" +
                " 阿勒泰 47.50 88.12" +
                " 阿图什 39.42 76.08" +
                " 博乐 44.57 82.08" +
                " 昌吉 44.02 87.18" +
                " 阜康 44.09 87.58" +
                " 哈密 42.50 93.28" +
                " 和田 37.09 79.55" +
                " 克拉玛依 45.36 84.51" +
                " 喀什 39.30 75.59" +
                " 库尔勒 41.46 86.07" +
                " 奎屯 44.27 84.56" +
                " 石河子 44.18 86.00" +
                " 塔城 46.46 82.59" +
                " 吐鲁番 42.54 89.11" +
                " 伊宁 43.55 81.20" +
                " 香港 21.23 115.12" +
                " 澳门 21.33 115.07" +
                " 台北 25.03 121.30" +
                " 高雄 22.98 120.19" +
                " 台中 24.15 120.67" +
                " 大兴安岭 50.42 124.12" +
                " 鄂尔多斯 39.62 109.80" +
                " 阿左旗 38.833412 105.66629" +
                " 廊坊 39.50 116.68" +
                " 晋中 37.70 112.71" +
                " 离石 37.51 111.15";
        String[] spString = All.split("\\s+");
        for(int i=0;i<spString.length;i++){
            if((i+1)%3==0){
                LaLMap.put(spString[i-2],spString[i-1]+" "+spString[i]);
            }
        }
        return LaLMap;
    }
    public static void GetLaL(Handler LaLHandler, final String startCity, final String endCity){
        String startLaL = "http://api.map.baidu.com/geocoder/v2/?address=" + startCity+"&output=json&ak=DwmOFEfx3wu9uRHK5iRkP21Yoj7jrDvI";
        String endLaL = "http://api.map.baidu.com/geocoder/v2/?address="+endCity+"&output=json&ak=DwmOFEfx3wu9uRHK5iRkP21Yoj7jrDvI";
//        Log.d(TAG, "GetLaL: kkk Lal = " + startLaL);
//        内存中没有时才访问网络，有的话就直接读取
        HttpUtil.sendOkHttpRequest(startLaL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: get startLaL failed");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final LngAndLat lngAndLat = Utility.handleLngALat(responseText);
//                Log.d(TAG, "onResponse: kkk!!! " + lngAndLat.getStatus());
//                Log.d(TAG, "onResponse: kkk?? " + lngAndLat.getResult().getLevel());
//                Lng为经度，Lat为维度
                if(lngAndLat!=null){
                    if(lngAndLat.getResult()!=null){
                        if(lngAndLat.getResult().getLocation()!=null){
                            startLng = lngAndLat.getResult().getLocation().getLng().substring(0,10);
                            startLat = lngAndLat.getResult().getLocation().getLat().substring(0,10);
//                            Log.d(TAG, "onResponse: kkk Lng = " + startLng);
                            Message msg = new Message();
                            msg.what = 1;
                            LaLHandler.sendMessage(msg);
                        }
                    }
                }
            }
        });
        //        获取结束时的经纬度
        HttpUtil.sendOkHttpRequest(endLaL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: get endLaL failed");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();

//                Log.d(TAG, "onResponse: kkk response = " + responseText);
                final LngAndLat lngAndLat = Utility.handleLngALat(responseText);
//                Lng为经度，Lat为维度
                if(lngAndLat.getResult()!=null){
                    endLng = lngAndLat.getResult().getLocation().getLng().substring(0,10);
                    endLat = lngAndLat.getResult().getLocation().getLat().substring(0,10);
                    Message msg = new Message();
                    msg.what = 2;
                    LaLHandler.sendMessage(msg);
                }
            }
        });
    }
//    将时间从秒转化为时分
    public static String Transtime(int second){
//        Log.d(TAG, "Transtime: second kkk = " + second);
        int Mine = second/60;
        int Hour = Mine/60;
        Mine %= 60;
        String TheHour = new DecimalFormat("00").format(Hour);
        String TheMine = new DecimalFormat("00").format(Mine);
//        Log.d(TAG, "Transtime: kkk TheHour = " + TheHour);
        return TheHour+":"+TheMine;
    }

    /**
     * 将字符串1234转换为时间12:34
     * @param time_str
     * @return
     */
    public static String TransToTime(String time_str){
        String str_hour = time_str.substring(0,2);
        String str_mine = time_str.substring(2,4);
//        Log.d(TAG, "TransToTime: kkk hour = " + str_hour);
//        Log.d(TAG, "TransToTime: kkk mine = " + str_mine);
        String time = str_hour + ":"+str_mine;
//        Log.d(TAG, "TransToTime: kkk time = " + time);
        return time;
    }

    /**
     * 用于将飞机的时间调正确
     */
    public static String FlightTime(String time){
        Log.d(TAG, "FlightTime: zhege time kkk = " + time);
        String[] spString = time.split(":");
        int Hour = Integer.parseInt(spString[0]);
        int Mine = Integer.parseInt(spString[1]);
        String theHour = new DecimalFormat("00").format(Hour);
        String theMine = new DecimalFormat("00").format(Mine);
        return theHour+":"+theMine;
    }


    /**
     * 获取三字码
     * @return
     */

    public static Map<String,String> getCodeMap(){
        Map<String,String> CodeMap = new HashMap<>();
        String All = "AKA 安康 五里铺机场 陕西 AKU 阿克苏 温宿机场 新疆 AQG 安庆 大龙山机场 安徽 AYN 安阳 安阳机场 河南 BAV 包头 海兰泡机场 内蒙古 BHY 北海 福成机场 广西 BPX 昌都 昌都马草机场 西藏 BSD 保山 保山机场 云南 CAN 广州 白云国际机场 广东 CGD 常德 桃花机场 湖南 CGO 郑州 新郑国际机场 河南 CHG 朝阳 大房身机场 吉林 CHW 酒泉 酒泉机场 甘肃 CIF 赤峰 玉龙国际机场 内蒙古 CIH 长治 玉村机场 山西 CKG 重庆 江北国际机场 重庆 CNI 长海 大长山岛机场 辽宁 CSX 长沙 黄花国际机场 湖南 CTU 成都 双流国际机场 四川 CZX 常州 奔牛机场 江苏 DAT 大同 怀仁机场 山西 DAX 达州 河市机场 四川 DDG 丹东 浪头机场 辽宁 DIG 香格里拉 迪庆机场 云南 DLC 大连 周水子国际机场 辽宁 DLU 大理 大理机场 云南 DNH 敦煌 敦煌机场 甘肃 DOY 东营 东营机场 山东 DYG 张家界 荷花机场 湖南 ENH 恩施 许家坪机场 湖北 ENY 延安 二十里铺机场 陕西 FIG 阜阳 西关机场 安徽 FOC 福州 长乐国际机场 福建 FYN 富蕴 可可托托海机场 新疆 GHN 广汉 广汉机场 四川 GOQ 格尔木 格尔木机场 青海 HAK 海口 美兰国际机场 海南 HEK 黑河 黑河机场 黑龙江 HET 呼和浩特 白塔机场 内蒙古 HFE 合肥 骆岗机场 安徽 HGH 杭州 萧山国际机场 浙江 HJJ 怀化 芷江机场 湖南 HLD 海拉尔 东山机场 内蒙古 HLH 乌兰浩特 乌兰浩特机场 内蒙古 HMI 哈密 哈密机场 新疆 HNY 衡阳 衡阳机场 湖南 HRB 哈尔滨 闫家岗国际机场 黑龙江 HSN 舟山 普陀山机场 浙江 HTN 和田 和田机场 新疆 HYN 黄岩 路桥机场 浙江 HZG 汉中 西关机场 陕西 INC 银川 河东机场 宁夏 IQM 且末 且末机场 新疆 IQN 庆阳 西峰镇机场 甘肃 JDZ 景德镇 罗家机场 江西 JGN 嘉峪关 嘉峪关机场 甘肃 JGS 井冈山 井冈山机场 江西 JHG 西双版纳 景洪机场 云南 JIL 吉林 二台子机场 吉林 JIU 九江 庐山机场 江西 JJN 泉州 晋江机场 福建 JMU 佳木斯 东郊机场 黑龙江 JNZ 锦州 小岭子机场 辽宁 JUZ 衢州 衢州机场 浙江 JZH 九寨沟 黄龙机场 四川 KCA 库车 库车机场 新疆 KHG 喀什 喀什机场 新疆 KHN 南昌 昌北机场 江西 KMG 昆明 巫家坝国际机场 云南 KOW 赣州 黄金机场 江西 KRL 库尔勒 库尔勒机场 新疆 KRY 克拉玛依 克拉玛依机场 新疆 KWE 贵阳 龙洞堡机场 贵州 KWL 桂林 两江国际机场 广西 LCX 连城 连城机场 福建 LHW 兰州 中川机场 甘肃 LJG 丽江 丽江机场 云南 LNJ 临沧 临沧机场 云南 LUM 潞西 芒市机场 云南 LXA 拉萨 贡嘎机场 西藏 LYA 洛阳 北郊机场 河南 LYG 连云港 白塔埠机场 江苏 LYI 临沂 临沂机场 山东 LZH 柳州 白莲机场 广西 LZO 泸州 萱田机场 四川 MDG 牡丹江 海浪机场 黑龙江 MIG 绵阳 南郊机场 四川 MXZ 梅州 梅县机场 广东 NAO 南充 高坪机场 四川 NAY 北京 南苑机场 北京 NDG 齐齐哈尔 三家子机场 黑龙江 NGB 宁波 栎社机场 浙江 NKG 南京 禄口国际机场 江苏 NNG 南宁 吴墟机场 广西 NNY 南阳 姜营机场 河南 NTG 南通 兴东机场 江苏 PEK 北京 首都国际机场 北京 PVG 上海 浦江国际机场 上海 PZI 攀枝花 保安营机场 四川 SHA 上海 虹桥机场 上海 SHE 沈阳 桃仙机场 辽宁 SHP 山海关 秦皇岛机场 河北 SHS 荆州 沙市机场 湖北 SJW 石家庄 正定机场 河北 SWA 汕头 外砂机场 广东 SYM 思茅 思茅机场 云南 SYX 三亚 凤凰国际机场 海南 SZX 深圳 宝安国际机场 深圳�\n";
        String[] spString = All.split("\\s+");
        for(int i=0;i<spString.length;i++){
//            Log.d(TAG, "GetAllAirport:  all kkk " + spString[i]);
            if((i+1)%4==0){
                CodeMap.put(spString[i-2],spString[i-3]);
//                Log.d(TAG, "getCodeMap: kkk codemap = " + CodeMap.get(spString[i-2]));
            }
        }
        return CodeMap;
    }
    /**
     *
     */

    /**
     * URL编码
     * @return
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String URLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String getStartLng() {
        return startLng;
    }

    public static void setStartLng(String startLng) {
        SomeUtil.startLng = startLng;
    }

    public static String getStartLat() {
        return startLat;
    }

    public static void setStartLat(String startLat) {
        SomeUtil.startLat = startLat;
    }

    public static String getEndLng() {
        return endLng;
    }

    public static void setEndLng(String endLng) {
        SomeUtil.endLng = endLng;
    }

    public static String getEndLat() {
        return endLat;
    }

    public static void setEndLat(String endLat) {
        SomeUtil.endLat = endLat;
    }
}
