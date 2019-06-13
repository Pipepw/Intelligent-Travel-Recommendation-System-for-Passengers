package com.newweather.intelligenttravel;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
<<<<<<< HEAD
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
=======

import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.os.Build;
>>>>>>> e9d28b4434e83ca376b0db1f153261b2b934a7c4
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Button;
<<<<<<< HEAD
import android.widget.ListView;
=======
>>>>>>> e9d28b4434e83ca376b0db1f153261b2b934a7c4
import android.widget.TextView;
import android.widget.Toast;

import com.newweather.intelligenttravel.Entity.Train;
import com.newweather.intelligenttravel.Entity.TrueSubway;
import com.newweather.intelligenttravel.Entity.TrueTrain;
import com.newweather.intelligenttravel.Gson.list;
<<<<<<< HEAD
import com.newweather.intelligenttravel.db.City;
import com.newweather.intelligenttravel.db.Province;
import com.newweather.intelligenttravel.util.HttpUtil;
import com.newweather.intelligenttravel.util.Utility;
import com.newweather.intelligenttravel.util.TimePickerDialogUtil;

import org.litepal.LitePal;
=======
import com.newweather.intelligenttravel.util.AnotherGet;
import com.newweather.intelligenttravel.util.HttpUtil;
import com.newweather.intelligenttravel.util.SiteUtil;

import com.newweather.intelligenttravel.util.Utility;
import com.newweather.intelligenttravel.util.TimePickerDialogUtil;
>>>>>>> e9d28b4434e83ca376b0db1f153261b2b934a7c4

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements TimePickerDialogUtil.TimePickerDialogInterface {

    private static final String TAG = "MainActivity";
    TimePickerDialogUtil mTimePickerDialog = new TimePickerDialogUtil(MainActivity.this);

    private Button ScButton;
    private Button EcButton;
    private Button TimeButton;
    private Button QueryButton;
    private TextView ScText;
    private TextView EcText;
    private TextView TimeText;
    private TextView DateText;
    private Handler myHandler;
    private TrueTrain trueTrain=new TrueTrain();
    private TrueSubway trueSubway=new TrueSubway();

    @SuppressLint("HandlerLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScButton = findViewById(R.id.start_city_button);
        EcButton = findViewById(R.id.end_city_button);
        QueryButton = findViewById(R.id.query_button);
        TimeButton = findViewById(R.id.choose_time_button);
        ScText = findViewById(R.id.start_city_text);
        EcText = findViewById(R.id.end_city_text);
        TimeText = findViewById(R.id.time_text);
        DateText = findViewById(R.id.date_text);
        int flag = getIntent().getIntExtra("status",2);
        String sCity, eCity;
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
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
        DateText.setText(pref.getString("date",""));
        TimeText.setText(pref.getString("time",""));
        ScButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("data", "Start");
            Fragment fragment = new ChooseAreaFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.area_fragment, fragment);
            transaction.commit();
            List<Province> provinceList = LitePal.findAll(Province.class);
            List<City> cityList;
            for(Province province : provinceList){
                cityList = LitePal.where("provinceId=?",String.valueOf(
                        province.getId())).find(City.class);
                for(City City : cityList){
                    Log.d(TAG, "onCreate: kkk city = " + City.getCityName());
                }
            }
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

        QueryButton.setOnClickListener(v->{
            String StartCity = pref.getString("ScCity","");
            String EndCity = pref.getString("EcCity","");
            String Date = pref.getString("date","");   //格式为：xxxx-xx-xx  如：2019-01-23
            String Time = pref.getString("time","");   //格式为: xx;xx     如：04:45
<<<<<<< HEAD

=======
            SiteUtil.requestLaL("上海");
>>>>>>> e9d28b4434e83ca376b0db1f153261b2b934a7c4
        });
        myHandler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what==1){
                    super.handleMessage(msg);
                    trueTrain= AnotherGet.gettraim();//获取trueTrain示例
                    Log.d(TAG, "handleMessage: kkk time  "+trueTrain.getDeparturetime());

                }else if(msg.what==2){
                    super.handleMessage(msg);
                    trueSubway=AnotherGet.getsubwayy();//获取trueSubway实例
                    // totalduration即公共交通总时间
                    Log.d(TAG, "handleMessage: kkk sub "+trueSubway.getTotalduration());

                }
            }
        };
        //参数实例
        AnotherGet.getTrain(myHandler,"杭州","北京" ,"2019-6-13","8:00");
        AnotherGet.getSubway( myHandler,"杭州", "杭州", "西溪竞舟苑","杭州汽车北站");
    }


    @Override
    public void positiveListener() {

        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        Format f = new DecimalFormat("00");
        String date = mTimePickerDialog.getYear() + "-" + f.format(mTimePickerDialog.getMonth()) +
                "-" + f.format(mTimePickerDialog.getDay());
        String time = f.format(mTimePickerDialog.getHour()) + ":" + f.format(mTimePickerDialog.getMinute());
        editor.putString("date",date);
        editor.putString("time",time);
        editor.apply();
        DateText.setText(pref.getString("date",""));
        TimeText.setText(pref.getString("time",""));
    }

    @Override
    public void negativeListener() {

    }

<<<<<<< HEAD
    //获取Train
       // getTrain("杭州","西安" ,"2019-12-12");
//        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
//        String trainString=prefs.getString("train",null);
//        Train train= Utility.handleTrainResponse(trainString);
        //使用train示例
//        String  msg=train.getMsg();
//        String status=train.getStatus();
//        String start=train.getResult().getStart();
//        String end=train.getResult().getEnd();
//        msgText.setText(msg);
//        statusText.setText(status);
//        startText.setText(start);
//        endText.setText(end);
            //获取train中list包括起始站，终点站类型，时间
//        for(list mlist :train.getResult().getListList()){
//            departuretimeText.setText(mlist.getDeparturetime());
//            arrivaltimeText.setText(mlist.getArrivaltime());
//            stationText.setText(mlist.getStation());
//            endstationText.setText(mlist.getEndstation());
//            costtimeText.setText(mlist.getCosttime());
//            typenameText.setText(mlist.getTypename());
//        }

    public void getTrain(String startcity,String endcity,String date){
        String trainUrl="https://api.jisuapi.com/train/station2s?appkey=e74dd71c6e53e1c1&start="+startcity+"&end="+endcity+"&date="+date;
        HttpUtil.sendOkHttpRequest(trainUrl, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"huoquxinxishibai",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Train train= Utility.handleTrainResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(train!=null&&"ok".equals(train.getMsg())){
                            //show(train);
                            SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                            editor.putString("train",responseText);
                            //Log.d("editor",responseText);
                            editor.apply();
                        }else{
                            Toast.makeText(MainActivity.this,"获取信息失败",Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
    }
=======
>>>>>>> e9d28b4434e83ca376b0db1f153261b2b934a7c4





}
