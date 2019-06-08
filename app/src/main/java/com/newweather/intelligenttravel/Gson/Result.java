package com.newweather.intelligenttravel.Gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//train中的result
public class Result {

    //极速数据中的result
    public String start;
    public String end;
    public String ishigh;

    @SerializedName("list")
    public List<list> listList;
}
