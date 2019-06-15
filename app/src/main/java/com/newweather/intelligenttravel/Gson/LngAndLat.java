package com.newweather.intelligenttravel.Gson;

//用Gson解析多层嵌套Json的方法：
//将子类声明在里面
public class LngAndLat {
    public static result result;
    public class result{
//        将result和location声明出来
        public location location;
        public class location{
            public String lng;
            public String lat;
        }
    }
}


