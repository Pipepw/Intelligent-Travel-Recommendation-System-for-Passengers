package com.newweather.intelligenttravel.Entity;

import com.newweather.intelligenttravel.Gson.Result;

public class Train {

    private  String status;
    private  String msg;
    private  Result result;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}