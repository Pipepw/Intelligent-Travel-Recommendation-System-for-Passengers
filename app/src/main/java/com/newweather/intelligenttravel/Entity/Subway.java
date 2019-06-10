package com.newweather.intelligenttravel.Entity;

import com.google.gson.annotations.SerializedName;
import com.newweather.intelligenttravel.Gson.Result1;

import java.util.List;

public class Subway {

    public String status;
    public String msg;

    @SerializedName("result")
    public List<Result1> ResultList;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}