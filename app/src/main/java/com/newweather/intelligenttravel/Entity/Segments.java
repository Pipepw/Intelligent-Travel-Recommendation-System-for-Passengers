package com.newweather.intelligenttravel.Entity;

import com.google.gson.annotations.SerializedName;

//trainroute
public class Segments {
    private int choose_flag = 0;//0表示无
    private int order_flag = 0;//1表示先

    public String cost;
    public Railway railway;

    public int getChoose_flag() {
        return choose_flag;
    }

    public void setChoose_flag(int choose_flag) {
        this.choose_flag = choose_flag;
    }

    public int getOrder_flag() {
        return order_flag;
    }

    public void setOrder_flag(int order_flag) {
        this.order_flag = order_flag;
    }


    public class Railway{
        public String name;
        public Departure_stop departure_stop;
        public Arrival_stop arrival_stop;

        public class Departure_stop{
            @SerializedName("name")
            public String departure_name;

            @SerializedName("time")
            public String departure_time;
        }


        public class Arrival_stop{
            @SerializedName("name")
            public String arrival_name;

            @SerializedName("time")
            public String arrival_time;
        }

    }
}