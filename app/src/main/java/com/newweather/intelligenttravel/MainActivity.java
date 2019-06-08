package com.newweather.intelligenttravel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.newweather.intelligenttravel.Entity.Subway;
import com.newweather.intelligenttravel.Entity.Train;
import com.newweather.intelligenttravel.Gson.list;
import com.newweather.intelligenttravel.util.HttpUtil;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
//Train的请求示例
//    private TextView msgText;
//    private TextView statusText;
//    //private TextView listText;
//    private TextView startText;
//    private TextView endText;
//    private TextView departuretimeText;
//    private TextView arrivaltimeText;
//    private TextView stationText;
//    private TextView endstationText;
//    private TextView costtimeText;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        msgText=(TextView)findViewById(R.id.msg);
//        statusText=(TextView)findViewById(R.id.status);
//        //listText=(TextView)findViewById(R.id.list);
//        startText=(TextView)findViewById(R.id.start);
//        endText=(TextView)findViewById(R.id.end);
//        departuretimeText=(TextView)findViewById(R.id.departuretime);
//        arrivaltimeText=(TextView)findViewById(R.id.arrivaltime);
//        stationText=(TextView)findViewById(R.id.station);
//        endstationText=(TextView)findViewById(R.id.endstation);
//        costtimeText=(TextView)findViewById(R.id.costtime);

//    train网址
//        String trainUrl="https://api.jisuapi.com/train/station2s?appkey=e74dd71c6e53e1c1&start=杭州&end=北京&ishigh=0";
//        Http.sendOkHttpRequest(trainUrl, new Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, IOException e) {
//                e.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this,"huoquxinxishibai",Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//            @Override
//            public void onResponse(okhttp3.Call call, Response response) throws IOException {
//                final String responseText=response.body().string();
//                final Train train= Util.handleTrainResponse(responseText);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(train!=null&&"ok".equals(train.msg)){
//                            show(train);
//                        }else{
//                            Toast.makeText(MainActivity.this,"获取信息失败",Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                });
//            }
//        });
//    }
//    public void show(Train train){
//        String msg=train.msg;
//        String status=train.status;
//        String start=train.result.start;
//        String end=train.result.end;
//        String list=train.result.listList.toString();
//
//        msgText.setText(msg);
//        statusText.setText(status);
//        startText.setText(start);
//        endText.setText(end);
//        //listText.setText(list);
//        for(com.newweather.intelligenttravel.Gson.list mlist :train.result.listList){
//            departuretimeText.setText(mlist.departuretime);
//            arrivaltimeText.setText(mlist.arrivaltime);
//            stationText.setText(mlist.station);
//            endstationText.setText(mlist.endstation);
//            costtimeText.setText(mlist.costtime);
//        }
//
//    }

// Subway的请求示例
//    private TextView statusText;
//    private TextView msgText;
//    private TextView totaldurationText;//总时间
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        statusText = (TextView) findViewById(R.id.status);
//        msgText = (TextView) findViewById(R.id.msg);
//        totaldurationText = (TextView) findViewById(R.id.totalduration);
//Subway网址
//        String subwayUrl = " https://api.jisuapi.com/transit/station2s?city=杭州&endcity=杭州&start=西溪竞舟苑&end=杭州汽车北站&appkey=e74dd71c6e53e1c1";
//        HttpUtil.sendOkHttpRequest(subwayUrl, new Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, IOException e) {
//                e.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this, "huoquxinxishibai", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(okhttp3.Call call, Response response) throws IOException {
//                final String responseText = response.body().string();
//                final Subway subway = Util.handleSubwayResponse(responseText);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (subway != null && "ok".equals(subway.msg)) {
//                            show(subway);
//                        } else {
//                            Toast.makeText(MainActivity.this, "获取信息失败", Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                });
//            }
//        });
//    }
//
//    public void show(Subway subway) {
//        String msg = subway.msg;
//        String status = subway.status;
//
//        msgText.setText(msg);
//        statusText.setText(status);
//
//        for(Result mresult :subway.ResultList){
//            totaldurationText.setText(mresult.totalduration);
//        }
//    }
}
