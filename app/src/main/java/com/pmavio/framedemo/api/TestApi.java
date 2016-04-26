package com.strongit.framedemo.api;

import com.strongit.framedemo.bean.BaseResponse;
import com.strongit.framedemo.global.GlobalConfigs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：Mavio
 * 日期：2016/3/1.
 */
public interface TestApi{

    static String BASE_URL_RELEASE = "http://app.archshanghai.com/";
    static String BASE_URL_PRODUCT = "http://appcoding.archshanghai.com/";
    public static String BASE_URL = GlobalConfigs.D ? BASE_URL_RELEASE : BASE_URL_PRODUCT;


    @GET("iData/iForCustomer/interUserInfo.aspx?flag=UserInfo")
    Call<BaseResponse> getUserInfoById(@Query("uidx") String id);

}
