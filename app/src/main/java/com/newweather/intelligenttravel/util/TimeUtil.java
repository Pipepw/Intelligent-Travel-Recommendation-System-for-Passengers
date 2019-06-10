package com.newweather.intelligenttravel.util;

public class TimeUtil {
    /**
     * 获取时间的结果,如果时间小于的话，则返回"false"，否则返回花费的时间（String型）
     * 所以判断时间大小的时候，就看结果是不是“false”,如果前小于后，则返回false
     * @param EndTime
     * @param StartTime
     * @return
     */
    public static String getTime(String EndTime, String StartTime) {
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

}
