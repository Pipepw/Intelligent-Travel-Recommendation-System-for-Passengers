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
gm
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
        Flight time_flight = intent.getSerializableExtra("key");
        Flight fare_flight = flight;
        List<Segments> time_train = null;
        List<Segments> fare_train = null;

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
        Flight flight=new Flight();
        flight.setChoose_flag(1);
        flight.setOrder_flag(1);
        flight.setStartStation("杭州站");
        flight.setEndStation("太原站");
        flight.setStartTime("4:00");
        flight.setEndTime("8:00");
        flight.setFare("4000元");

        //出发，到达城市
        //计算总耗时，总费用
        String zonghaoshitext = "12hour";//最短时间
        String zongfeiyongtext = "500yuan";
        String zonghaoshi2text="19hour";//最少费用
        String zongfeiyong2text="400yuan";

        chufadi.setText("杭州");
        mudidi.setText("太原");
        zonghaoshi.setText(zonghaoshitext);
        zongfeiyong.setText(zongfeiyongtext);
        //最短时间路线
        luxianf11Layout.removeAllViews();
        if(time_flight.getOrder_flag()==0&&time_flight.getChoose_flag()==1){
            View view=LayoutInflater.from(this).inflate(R.layout.luxian_item,luxianf11Layout,false);
            TextView chufazhan=(TextView)view.findViewById(R.id.chufazhan);
            TextView mudizhan=(TextView)view.findViewById(R.id.mudizhan);
            TextView starttime=(TextView)view.findViewById(R.id.starttime);
            TextView endtime=(TextView)view.findViewById(R.id.endtime);
            TextView feiyong=(TextView)view.findViewById(R.id.feiyong);
            chufazhan.setText(flight.getStartStation());
            mudizhan.setText(flight.getEndStation());
            starttime.setText(flight.getEndTime());
            endtime.setText(flight.getEndTime());
            feiyong.setText(flight.getFare());
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
            chufazhan.setText(flight.getStartStation());
            mudizhan.setText(flight.getEndStation());
            starttime.setText(flight.getEndTime());
            endtime.setText(flight.getEndTime());
            feiyong.setText(flight.getFare());
            luxianf12Layout.addView(view);
        }

        //最少费用路线
        chufadi2.setText("杭州");
        mudidi2.setText("太原");
        zonghaoshi2.setText(zonghaoshi2text);
        zongfeiyong2.setText(zongfeiyong2text);
        //最短时间路线
        luxianf21Layout.removeAllViews();
        if(fare_flight.getOrder_flag()==0&&fare_flight.getChoose_flag()==1){
            View view=LayoutInflater.from(this).inflate(R.layout.luxian_item,luxianf21Layout,false);
            TextView chufazhan=(TextView)view.findViewById(R.id.chufazhan);
            TextView mudizhan=(TextView)view.findViewById(R.id.mudizhan);
            TextView starttime=(TextView)view.findViewById(R.id.starttime);
            TextView endtime=(TextView)view.findViewById(R.id.endtime);
            TextView feiyong=(TextView)view.findViewById(R.id.feiyong);
            chufazhan.setText(flight.getStartStation());
            mudizhan.setText(flight.getEndStation());
            starttime.setText(flight.getEndTime());
            endtime.setText(flight.getEndTime());
            feiyong.setText(flight.getFare());
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
            chufazhan.setText(flight.getStartStation());
            mudizhan.setText(flight.getEndStation());
            starttime.setText(flight.getEndTime());
            endtime.setText(flight.getEndTime());
            feiyong.setText(flight.getFare());
            luxianf22Layout.addView(view);
        }
    }

}
