package com.newweather.intelligenttravel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.newweather.intelligenttravel.db.City;
import com.newweather.intelligenttravel.db.Province;
import com.newweather.intelligenttravel.util.HttpUtil;
import com.newweather.intelligenttravel.util.Utility;
import com.newweather.intelligenttravel.util.WaitDialog;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provincesList;
    private List<City> cityList;
    private Province selectedProvince;
    private int currentLevel;

    //    获取布局的实例
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.
                simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    //    用来获取点击位置
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (currentLevel == LEVEL_PROVINCE) {
                selectedProvince = provincesList.get(position);
//                id和code都是同一个值，那么为什么要将其分成两个呢？，是因为他们的用处不同吧，
//                id是在这一级用的，而code是用于下一级的，比如provinceId是在查询city的时候用的，
//                而provinceCode是在city查询province的时候用的
                cityList = LitePal.where("provinceId = ?",String.valueOf(
                        selectedProvince.getId())).find(City.class);
                if(cityList.size()==1){
                    Bundle bundle = ChooseAreaFragment.this.getArguments();
                    String status = null;
                    if (bundle != null) {
                        status = bundle.getString("data");
                    }
                    String city = selectedProvince.getProvinceName();
                    Toast.makeText(ChooseAreaFragment.this.getActivity(), "You clicked a city", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(ChooseAreaFragment.this.getActivity(), MainActivity.class);
                    if (status.equals("Start")) {
                        intent.putExtra("status", 0);
                    } else if (status.equals("End")) {
                        intent.putExtra("status", 1);
                    }
                    intent.putExtra("city", city);
                    ChooseAreaFragment.this.startActivity(intent);
                    ChooseAreaFragment.this.getActivity().onBackPressed();
                }
                else{
                    ChooseAreaFragment.this.queryCities();
                }
            } else if (currentLevel == LEVEL_CITY) {
                Bundle bundle = ChooseAreaFragment.this.getArguments();
                String status = null;
                if (bundle != null) {
                    status = bundle.getString("data");
                }
                String city = cityList.get(position).getCityName();
                Toast.makeText(ChooseAreaFragment.this.getActivity(), "You clicked a city", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(ChooseAreaFragment.this.getActivity(), MainActivity.class);
                if (status.equals("Start")) {
                    intent.putExtra("status", 0);
                } else if (status.equals("End")) {
                    intent.putExtra("status", 1);
                }
                intent.putExtra("city", city);
                ChooseAreaFragment.this.startActivity(intent);
                ChooseAreaFragment.this.getActivity().onBackPressed();
            }
        });
        backButton.setOnClickListener(v -> {
            if (currentLevel == LEVEL_CITY) {
                ChooseAreaFragment.this.queryProvinces();
            }
        });
        queryProvinces();
    }
    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provincesList = LitePal.findAll(Province.class);
        if(provincesList.size() > 0){

            dataList.clear();
            for(Province province : provincesList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }
    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = LitePal.where("provinceId = ?",String.valueOf(
                selectedProvince.getId())).find(City.class);
        if(cityList.size() > 0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();

            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }

    //为了从服务器中获取数据
    private void queryFromServer(String address, final String type){
        WaitDialog.showProgressDialog(getActivity());

//        Callback是用来回调的
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            //            回调获得服务器中的数据，new Callback()只是声明了一个接口对象，对接口方法的实现在下面
//            为什么不能进入回调中
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WaitDialog.dismiss();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() -> {
                    WaitDialog.dismiss();
                    Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


}
