package com.newweather.intelligenttravel;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;

import android.util.Log;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.newweather.intelligenttravel.Entity.Flight;
import com.newweather.intelligenttravel.Entity.TrueSubway;
import com.newweather.intelligenttravel.Entity.TrueTrain;
import com.newweather.intelligenttravel.util.FlightUtil;
import com.newweather.intelligenttravel.util.PlanUtil;
import com.newweather.intelligenttravel.util.SomeUtil;
import com.newweather.intelligenttravel.util.TimePickerDialogUtil;

import com.newweather.intelligenttravel.util.AnotherGet;
import com.newweather.intelligenttravel.util.Utility;
import com.newweather.intelligenttravel.util.WaitDialog;


import java.security.PublicKey;
import java.text.DecimalFormat;
import java.text.Format;

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

        QueryButton.setOnClickListener(v-> {
            WaitDialog.showProgressDialog(MainActivity.this);
            String StartCity = pref.getString("ScCity", "");
            String EndCity = pref.getString("EcCity", "");
            String Date = pref.getString("date", "");   //格式为：xxxx-xx-xx  如：2019-01-23
            String Time = pref.getString("time", "");   //格式为: xx;xx     如：04:45
            if(StartCity.equals("")||EndCity.equals("")||Date.equals("")||Time.equals("")){
                Toast.makeText(MainActivity.this,"Please Choose All!!!",Toast.LENGTH_SHORT).show();
            }else{
                PlanUtil planUtil = new PlanUtil();
                Log.d(TAG, "onCreate:  kkk it has start");
                final int[] i = {0};

                myHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 1) {
                            super.handleMessage(msg);
                            i[0]++;
                            Log.d(TAG, "handleMessage:  kkk main i = " + i[0]);
                            Log.d(TAG, "onCreate: kkk flight choose_flag? + " + planUtil.getFare_flight().getChoose_flag());
//                            Log.d(TAG, "onCreate: kkk flight order_flag? + " + planUtil.getTime_flight().getChoose_flag());
//                            Log.d(TAG, "onCreate: kkk train choose_flag? + " + planUtil.getFare_train().get(0).getChoose_flag());
//                            Log.d(TAG, "onCreate: kkk train order_flag? + " + planUtil.getTime_train().get(0).getChoose_flag());
                            WaitDialog.dismiss();
                        } else if (msg.what == 2) {
                            super.handleMessage(msg);
                            String time = AnotherGet.GetSubwayTime();//获取trueSubway实例
                            // totalduration即公共交通总时间
                            Log.d(TAG, "handleMessage: kkk sub " + time);
                        }
                    }
                };
                planUtil.GetPlan(myHandler,StartCity,EndCity,Date,Time);
//                AnotherGet.GetSubway(myHandler,"杭州","萧山国际机场","杭州站");
                //参数实例
//            AnotherGet.getTrain(myHandler, "杭州", "北京", "2019-6-13", "8:00");
//            AnotherGet.getSubway(myHandler, "杭州", "杭州", "萧山国际机场", "杭州站");
            }
//120.443341,30.240638 xiao 120.189606,30.249207 hang
        });
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
}
