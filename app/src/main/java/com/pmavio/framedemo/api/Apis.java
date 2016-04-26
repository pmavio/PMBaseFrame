package com.strongit.framedemo.api;

import com.strongit.framedemo.api.converter.FastjsonConverterFactory;

import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * 作者：Mavio
 * 日期：2016/3/1.
 */
public class Apis {

    public static String Gobale_BASE_URL_RELEASE="http://app.archshanghai.com/";
    public static String Gobale_BASE_URL_ForTest= "http://appcoding.archshanghai.com/";



    public static TestApi getTestApi(){
        return new Retrofit.Builder()
                .baseUrl(TestApi.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(FastjsonConverterFactory.create())
                .build()
                .create(TestApi.class);
    }
}
