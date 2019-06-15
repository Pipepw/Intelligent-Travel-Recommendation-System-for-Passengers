package com.newweather.intelligenttravel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.newweather.intelligenttravel.Entity.Flight;
import com.newweather.intelligenttravel.Entity.Segments;

import java.util.List;

public class SecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Flight time_flight;
        Flight fare_flight;
        List<Segments> time_train;
        List<Segments> fare_train;
    }
}
