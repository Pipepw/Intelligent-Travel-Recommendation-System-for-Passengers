package com.newweather.intelligenttravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.newweather.intelligenttravel.Entity.Flight;
import com.newweather.intelligenttravel.Entity.Segments;
import com.newweather.intelligenttravel.util.SomeUtil;

import org.w3c.dom.Text;

import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private ScrollView secondActivityLayout;

    private TextView chufadi;
    private TextView mudidi;
    private TextView zonghaoshi;
    private TextView zongfeiyong;
    private LinearLayout luxianf11Layout;
    private LinearLayout luxianLayout;
    private LinearLayout luxianf12Layout;

    private TextView chufadi2;
    private TextView mudidi2;
    private TextView zonghaoshi2;
    private TextView zongfeiyong2;
    private LinearLayout luxianf21Layout;
    private LinearLayout luxianLayout2;
    private LinearLayout luxianf22Layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //获取意图
        Intent intent=this.getIntent();
        //根据bundle中的key值获取数据
        //接收对象
        Bundle extras=getIntent().getExtras();
        Flight time_flight = extras.getParcelable("time_flight");
        Flight fare_flight = extras.getParcelable("fare_flight");
        List<Segments> time_train = extras.getParcelableArrayList("time_train");
        List<Segments> fare_train = extras.getParcelableArrayList("fare_train");

        //初始化各控件
        secondActivityLayout=(ScrollView)findViewById(R.id.secondactivity_layout);
        //最短时间控件
        chufadi=(TextView) findViewById(R.id.chufadi);
        mudidi=(TextView)findViewById(R.id.mudidi);
        zonghaoshi=(TextView)findViewById(R.id.zonghaoshi);
        zongfeiyong=(TextView)findViewById(R.id.zongfeiyong);
        luxianf11Layout=(LinearLayout)findViewById(R.id.luxianf11_layout);
        luxianLayout=(LinearLayout)findViewById(R.id.luxian_layout);
        luxianf12Layout=(LinearLayout)findViewById(R.id.luxianf12_layout);
        //最少费用控件
        chufadi2=(TextView) findViewById(R.id.chufadi2);
        mudidi2=(TextView)findViewById(R.id.mudidi2);
        zonghaoshi2=(TextView)findViewById(R.id.zonghaoshi2);
        zongfeiyong2=(TextView)findViewById(R.id.zongfeiyong2);
        luxianf21Layout=(LinearLayout)findViewById(R.id.luxianf21_layout2);
        luxianLayout2=(LinearLayout)findViewById(R.id.luxian_layout2);
        luxianf22Layout=(LinearLayout)findViewById(R.id.luxianf22_layout2);

        //出发，到达城市
        //计算总耗时，总费用
        String flighttime=null;//时间
        String traintime=null;
        String zonghaoshitext=null;
        Float zongfeiyongtext=null;
        String chufaditext=null;
        String mudiditext=null;

        String flighttime2=null;//费用
        String traintime2=null;
        String zonghaoshitext2=null;
        Float zongfeiyongtext2=null;
        String chufaditext2=null;
        String mudiditext2=null;

        //最短时间
        if(time_flight.getChoose_flag()==1&&time_train.get(0).getChoose_flag()==1){
            flighttime = SomeUtil.getTime(time_flight.getEndTime(),time_flight.getStartTime());
            traintime = time_train.get(0).usetime;
            zonghaoshitext = SomeUtil.AddTime(flighttime,traintime);
            zongfeiyongtext = Float.parseFloat(time_flight.getFare())+Float.parseFloat(time_train.get(0).cost);
            if(time_flight.getOrder_flag()==1){
                chufaditext=time_flight.getStartStation();
                mudiditext=time_train.get(time_train.size()-1).railway.arrival_stop.arrival_name;
            }else{
                chufaditext=time_train.get(0).railway.departure_stop.departure_name;
                mudiditext=time_flight.getEndStation();
            }
        }else if(time_flight.getChoose_flag()==1&&time_train.get(0).getChoose_flag()==0){
            chufaditext=time_flight.getStartStation();
            mudiditext=time_flight.getEndStation();
            zonghaoshitext=SomeUtil.getTime(time_flight.getEndTime(),time_flight.getStartTime());
            zongfeiyongtext=Float.valueOf(time_flight.getFare());
        }else if(time_flight.getChoose_flag()==0&&time_train.get(0).getChoose_flag()==1){
            chufaditext=time_train.get(0).railway.departure_stop.departure_name;
            mudiditext=time_train.get(time_train.size()-1).railway.arrival_stop.arrival_name;
            zonghaoshitext=time_train.get(0).usetime;
            zongfeiyongtext=Float.valueOf(time_train.get(0).cost);
        }

        //最少费用
        if(fare_flight.getChoose_flag()==1&&fare_train.get(0).getChoose_flag()==1){
            flighttime2 = SomeUtil.getTime(fare_flight.getEndTime(),fare_flight.getStartTime());
            traintime2 = fare_train.get(0).usetime;
            zonghaoshitext2 = SomeUtil.AddTime(flighttime2,traintime2);
            zongfeiyongtext2 = Float.parseFloat(fare_flight.getFare())+Float.parseFloat(fare_train.get(0).cost);
            if(fare_flight.getOrder_flag()==1){
                chufaditext2=fare_flight.getStartStation();
                mudiditext2=fare_train.get(fare_train.size()-1).railway.arrival_stop.arrival_name;
            }else{
                chufaditext2=fare_train.get(0).railway.departure_stop.departure_name;
                mudiditext2=fare_flight.getEndStation();
            }
        }else if(fare_flight.getChoose_flag()==1&&fare_train.get(0).getChoose_flag()==0){
            chufaditext2=fare_flight.getStartStation();
            mudiditext2=fare_flight.getEndStation();
            zonghaoshitext2=SomeUtil.getTime(fare_flight.getEndTime(),time_flight.getStartTime());
            zongfeiyongtext2=Float.valueOf(fare_flight.getFare());
        }else if(fare_flight.getChoose_flag()==0&&fare_train.get(0).getChoose_flag()==1){
            chufaditext2=fare_train.get(0).railway.departure_stop.departure_name;
            mudiditext2=fare_train.get(fare_train.size()-1).railway.arrival_stop.arrival_name;
            zonghaoshitext2=fare_train.get(0).usetime;
            zongfeiyongtext2=Float.valueOf(fare_train.get(0).cost);
        }

        chufadi.setText(chufaditext);
        mudidi.setText(mudiditext);
        zonghaoshi.setText(zonghaoshitext);
        zongfeiyong.setText(String.valueOf(zongfeiyongtext));
        //最短时间路线
        luxianf11Layout.removeAllViews();
        if(time_flight.getOrder_flag()==0&&time_flight.getChoose_flag()==1){
            View view=LayoutInflater.from(this).inflate(R.layout.luxian_item,luxianf11Layout,false);
            TextView chufazhan=(TextView)view.findViewById(R.id.chufazhan);
            TextView mudizhan=(TextView)view.findViewById(R.id.mudizhan);
            TextView starttime=(TextView)view.findViewById(R.id.starttime);
            TextView endtime=(TextView)view.findViewById(R.id.endtime);
            TextView feiyong=(TextView)view.findViewById(R.id.feiyong);
            chufazhan.setText(time_flight.getStartStation());
            mudizhan.setText(time_flight.getEndStation());
            starttime.setText(time_flight.getEndTime());
            endtime.setText(time_flight.getEndTime());
            feiyong.setText(time_flight.getFare());
            luxianf11Layout.addView(view);

        }
        if(time_train.get(0).getChoose_flag()==1){
            luxianLayout.removeAllViews();
            for(Segments segments:time_train){
                View view= LayoutInflater.from(this).inflate(R.layout.luxian_item,luxianLayout,false);
                TextView chufazhan=(TextView)view.findViewById(R.id.chufazhan);
                TextView mudizhan=(TextView)view.findViewById(R.id.mudizhan);
                TextView starttime=(TextView)view.findViewById(R.id.starttime);
                TextView endtime=(TextView)view.findViewById(R.id.endtime);
                TextView feiyong=(TextView)view.findViewById(R.id.feiyong);
                chufazhan.setText( segments.railway.departure_stop.departure_name);
                mudizhan.setText( segments.railway.arrival_stop.arrival_name);
                starttime.setText(segments.railway.departure_stop.departure_time);
                endtime.setText(segments.railway.arrival_stop.arrival_time);
                feiyong.setText(segments.cost);
                luxianLayout.addView(view);
            }
        }

        luxianf12Layout.removeAllViews();
        if(time_flight.getOrder_flag()==1&&time_flight.getChoose_flag()==1){
            View view=LayoutInflater.from(this).inflate(R.layout.luxian_item,luxianf12Layout,false);
            TextView chufazhan=(TextView)view.findViewById(R.id.chufazhan);
            TextView mudizhan=(TextView)view.findViewById(R.id.mudizhan);
            TextView starttime=(TextView)view.findViewById(R.id.starttime);
            TextView endtime=(TextView)view.findViewById(R.id.endtime);
            TextView feiyong=(TextView)view.findViewById(R.id.feiyong);
            chufazhan.setText(time_flight.getStartStation());
            mudizhan.setText(time_flight.getEndStation());
            starttime.setText(time_flight.getEndTime());
            endtime.setText(time_flight.getEndTime());
            feiyong.setText(time_flight.getFare());
            luxianf12Layout.addView(view);
        }

        //最少费用路线
        chufadi2.setText(chufaditext2);
        mudidi2.setText(mudiditext2);
        zonghaoshi2.setText(zonghaoshitext2);
        zongfeiyong2.setText(String.valueOf(zongfeiyongtext2));

        luxianf21Layout.removeAllViews();
        if(fare_flight.getOrder_flag()==0&&fare_flight.getChoose_flag()==1){
            View view=LayoutInflater.from(this).inflate(R.layout.luxian_item,luxianf21Layout,false);
            TextView chufazhan=(TextView)view.findViewById(R.id.chufazhan);
            TextView mudizhan=(TextView)view.findViewById(R.id.mudizhan);
            TextView starttime=(TextView)view.findViewById(R.id.starttime);
            TextView endtime=(TextView)view.findViewById(R.id.endtime);
            TextView feiyong=(TextView)view.findViewById(R.id.feiyong);
            chufazhan.setText(fare_flight.getStartStation());
            mudizhan.setText(fare_flight.getEndStation());
            starttime.setText(fare_flight.getEndTime());
            endtime.setText(fare_flight.getEndTime());
            feiyong.setText(fare_flight.getFare());
            luxianf21Layout.addView(view);
        }
        if(fare_train.get(0).getChoose_flag()==1){
            luxianLayout2.removeAllViews();
            for(Segments segments:time_train){
                View view= LayoutInflater.from(this).inflate(R.layout.luxian_item,luxianLayout2,false);
                TextView chufazhan=(TextView)view.findViewById(R.id.chufazhan);
                TextView mudizhan=(TextView)view.findViewById(R.id.mudizhan);
                TextView starttime=(TextView)view.findViewById(R.id.starttime);
                TextView endtime=(TextView)view.findViewById(R.id.endtime);
                TextView feiyong=(TextView)view.findViewById(R.id.feiyong);
                chufazhan.setText( segments.railway.departure_stop.departure_name);
                mudizhan.setText( segments.railway.arrival_stop.arrival_name);
                starttime.setText(segments.railway.departure_stop.departure_time);
                endtime.setText(segments.railway.arrival_stop.arrival_time);
                feiyong.setText(segments.cost);
                luxianLayout2.addView(view);
            }
        }

        luxianf22Layout.removeAllViews();
        if(fare_flight.getOrder_flag()==1&&fare_flight.getChoose_flag()==1){
            View view=LayoutInflater.from(this).inflate(R.layout.luxian_item,luxianf22Layout,false);
            TextView chufazhan=(TextView)view.findViewById(R.id.chufazhan);
            TextView mudizhan=(TextView)view.findViewById(R.id.mudizhan);
            TextView starttime=(TextView)view.findViewById(R.id.starttime);
            TextView endtime=(TextView)view.findViewById(R.id.endtime);
            TextView feiyong=(TextView)view.findViewById(R.id.feiyong);
            chufazhan.setText(fare_flight.getStartStation());
            mudizhan.setText(fare_flight.getEndStation());
            starttime.setText(fare_flight.getEndTime());
            endtime.setText(fare_flight.getEndTime());
            feiyong.setText(fare_flight.getFare());
            luxianf22Layout.addView(view);
        }
    }

}
