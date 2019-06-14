package com.newweather.intelligenttravel.Entity;

import com.google.gson.annotations.SerializedName;

//trainroute
public class Segments {
    public Railway railway;
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