package com.newweather.intelligenttravel.Gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//train中的result
public class Result {

    //极速数据中的result
    private  String start;//起始城市
    private  String end;//结束城市
    private  String ishigh;

    @SerializedName("list")
    public List<list> listList;

    public void setStart(String start) {
        this.start = start;
    }

    public String getStart() {
        return start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEnd() {
        return end;
    }

    public void setListList(List<list> listList) {
        this.listList = listList;
    }

    public List<list> getListList() {
        return listList;
    }
}
