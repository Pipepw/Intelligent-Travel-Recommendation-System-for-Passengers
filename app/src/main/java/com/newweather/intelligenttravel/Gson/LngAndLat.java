package com.newweather.intelligenttravel.Gson;

//用Gson解析多层嵌套Json的方法：
//将子类声明在里面
public class LngAndLat {
    private int status;
    private LalResult result;
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setResult(LalResult result) {
        this.result = result;
    }
    public LalResult getResult() {
        return result;
    }
}


