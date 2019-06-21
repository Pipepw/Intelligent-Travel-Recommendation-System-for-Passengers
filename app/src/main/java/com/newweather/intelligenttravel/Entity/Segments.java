package com.newweather.intelligenttravel.Entity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

//trainroute
public class Segments implements Parcelable {
    private int choose_flag = 0;//0表示无
    private int order_flag = 0;//1表示先

    public String cost;
    public String usetime;
    public Walking walking;
    public Bus bus;
    public Railway railway;


    //序列化
    protected Segments(Parcel in) {
        choose_flag = in.readInt();
        order_flag = in.readInt();
        cost = in.readString();
        usetime = in.readString();
        walking = in.readParcelable(Walking.class.getClassLoader());
        bus = in.readParcelable(Bus.class.getClassLoader());
        railway = in.readParcelable(Railway.class.getClassLoader());

        //获取类加载器的几种方式
//        getClass().getClassLoader();
//        Thread.currentThread().getContextClassLoader();
//        Walking.class.getClassLoader();
    }

    public static final Creator<Segments> CREATOR = new Creator<Segments>() {
        @Override
        public Segments createFromParcel(Parcel in) {
            return new Segments(in);
        }

        @Override
        public Segments[] newArray(int size) {
            return new Segments[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //负责反序列化
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(choose_flag);
        dest.writeInt(order_flag);
        dest.writeString(cost);
        dest.writeString(usetime);
        dest.writeParcelable(walking,0);
        dest.writeParcelable(bus,0);
        dest.writeParcelable(railway,0);
    }


    public class Railway implements Parcelable{
        public String name;
        public Departure_stop departure_stop;
        public Arrival_stop arrival_stop;

        protected Railway(Parcel in) {
            name = in.readString();
            departure_stop = in.readParcelable(Departure_stop.class.getClassLoader());
            arrival_stop = in.readParcelable(Arrival_stop.class.getClassLoader());
        }
        //负责反序列化（就是在获取的地方使用）
        public final Creator<Railway> CREATOR = new Creator<Railway>() {
            @Override
            public Railway createFromParcel(Parcel in) {
                return new Railway(in);
            }

            @Override
            public Railway[] newArray(int size) {
                return new Railway[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeParcelable(departure_stop,0);
            dest.writeParcelable(arrival_stop,0);
        }

        public class Departure_stop implements Parcelable{
            @SerializedName("name")
            public String departure_name;

            @SerializedName("time")
            public String departure_time;

            protected Departure_stop(Parcel in) {
                departure_name = in.readString();
                departure_time = in.readString();
            }

            public final Creator<Departure_stop> CREATOR = new Creator<Departure_stop>() {
                @Override
                public Departure_stop createFromParcel(Parcel in) {
                    return new Departure_stop(in);
                }

                @Override
                public Departure_stop[] newArray(int size) {
                    return new Departure_stop[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(departure_name);
                dest.writeString(departure_time);
            }
        }


        public class Arrival_stop implements Parcelable{
            @SerializedName("name")
            public String arrival_name;

            @SerializedName("time")
            public String arrival_time;

            protected Arrival_stop(Parcel in) {
                arrival_name = in.readString();
                arrival_time = in.readString();
            }

            public final Creator<Arrival_stop> CREATOR = new Creator<Arrival_stop>() {
                @Override
                public Arrival_stop createFromParcel(Parcel in) {
                    return new Arrival_stop(in);
                }

                @Override
                public Arrival_stop[] newArray(int size) {
                    return new Arrival_stop[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(arrival_name);
                dest.writeString(arrival_time);
            }
        }

    }
    public class Walking implements Parcelable{
        public int duration;

        protected Walking(Parcel in) {
            duration = in.readInt();
        }

        public final Creator<Walking> CREATOR = new Creator<Walking>() {
            @Override
            public Walking createFromParcel(Parcel in) {
                return new Walking(in);
            }

            @Override
            public Walking[] newArray(int size) {
                return new Walking[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(duration);
        }
    }
    public static class Bus implements Parcelable{

        public List<Buslines>  buslines;

        protected Bus(Parcel in) {
            in.readList(buslines,Buslines.class.getClassLoader());
        }

        public static final Creator<Bus> CREATOR = new Creator<Bus>() {
            @Override
            public Bus createFromParcel(Parcel in) {
                return new Bus(in);
            }

            @Override
            public Bus[] newArray(int size) {
                return new Bus[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(buslines);
        }

        public static class Buslines implements Parcelable{

            @SerializedName("duration")
            public int busduration;

            protected Buslines(Parcel in) {
                busduration = in.readInt();
            }

            public static final Creator<Buslines> CREATOR = new Creator<Buslines>() {
                @Override
                public Buslines createFromParcel(Parcel in) {
                    return new Buslines(in);
                }

                @Override
                public Buslines[] newArray(int size) {
                    return new Buslines[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(busduration);
            }
        }
    }

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

}