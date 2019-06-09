package com.newweather.intelligenttravel;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.newweather.intelligenttravel.Entity.Subway;
import com.newweather.intelligenttravel.Entity.Train;
import com.newweather.intelligenttravel.Gson.list;
import com.newweather.intelligenttravel.util.HttpUtil;
import com.newweather.intelligenttravel.util.TimePickerDialogUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Objects;

import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements TimePickerDialogUtil.TimePickerDialogInterface {

    private static final String TAG = "MainActivity";
    TimePickerDialogUtil mTimePickerDialog = new TimePickerDialogUtil(MainActivity.this);

    private Button ScButton;
    private Button EcButton;
    private Button TimeButton;
    private TextView ScText;
    private TextView EcText;
    SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
    SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScButton = findViewById(R.id.start_city_button);
        EcButton = findViewById(R.id.end_city_button);
        TimeButton = findViewById(R.id.choose_time_button);
        ScText = findViewById(R.id.start_city_text);
        EcText = findViewById(R.id.end_city_text);
        int flag = getIntent().getIntExtra("status",2);
        String sCity, eCity;
        String city = getIntent().getStringExtra("city");

        if(flag==1){
            editor.putString("EcCity",city);
            editor.apply();
        }else if(flag==0) {
            editor.putString("ScCity",city);
            editor.apply();
        }
        if(!pref.getString("ScCity", "").equals("")){
            sCity = pref.getString("ScCity", "");
            ScText.setText(sCity);
        }
        if(!pref.getString("EcCity", "").equals("")){
            eCity = pref.getString("EcCity","");
            EcText.setText(eCity);
        }
        ScButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("data", "Start");
            Fragment fragment = new ChooseAreaFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.area_fragment, fragment);
            transaction.commit();
        });

        EcButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("data", "End");
            Fragment fragment = new ChooseAreaFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.area_fragment, fragment);
            transaction.commit();
        });
        TimeButton.setOnClickListener(v -> mTimePickerDialog.showDateAndTimePickerDialog());
    }


    @Override
    public void positiveListener() {

        TextView TimeText = findViewById(R.id.time_text);
        TextView Datetext = findViewById(R.id.date_text);
        Format f = new DecimalFormat("00");
        String date = mTimePickerDialog.getYear() + "-" + f.format(mTimePickerDialog.getMonth()) +
                "-" + f.format(mTimePickerDialog.getDay());
        String time = f.format(mTimePickerDialog.getHour()) + ":" + f.format(mTimePickerDialog.getMinute());
        Datetext.setText(date);
        TimeText.setText(time);
        editor.putString("date",date);
        editor.putString("time",time);
        editor.apply();
    }

    @Override
    public void negativeListener() {

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
