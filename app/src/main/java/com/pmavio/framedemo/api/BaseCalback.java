package com.strongit.framedemo.api;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.strongit.framedemo.bean.BaseResponse;
import com.strongit.framedemo.global.GlobalConfigs;
import com.pmavio.pmbaseframe.utils.Log;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：Mavio
 * 日期：2016/3/1.
 */
public abstract class BaseCalback<T> implements Callback<T> {
    private static String TAG = "BaseCalback";

    protected Context mContext;
    protected View snackView;

    private boolean isFinished = false;

    public BaseCalback(Context mContext) {
        this.mContext = mContext;
    }

    public BaseCalback(Context mContext, View snackView) {
        this.mContext = mContext;
        this.snackView = snackView;
    }

    public abstract void onResponse(T response);

    @Override
    public void onResponse(Response<T> response) {
        //TODO
        T body = response.body();
        if(body != null && body instanceof BaseResponse){
            BaseResponse baseResponse = (BaseResponse) body;
            String flag = baseResponse.getFlag();
            if("success".equals(flag)){
                callResponse(body);
            }else{
                onFailure(null);//TODO 判断错误代码，返回错误信息
            }
        }else {
            callResponse(body);
        }
        finish();
    }

    private void callResponse(T response){
        try{
            onResponse(response);
        }catch (Exception e) {
            if(GlobalConfigs.D) {
                dealException(e);
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(TAG, "网络请求过程中发生了一个异常\n", t);
        showMessage(t.getMessage());
        finish();
    }

    private void finish(){
        if(isFinished) return;
        isFinished = true;
        try{
            onFinish();
        } catch (Exception e){
            dealException(e);
        }
    }

    protected void dealException(Exception e){
        if(GlobalConfigs.D) {
            // 测试环境下直接打印异常
            e.printStackTrace();
        }else{
            // 正式环境下上传Bugtags不报错
//            Bugtags.sendException(e);
        }
    }

    protected void showMessage(final String msg){
//        AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
//            @Override
//            public void call() {
//                SnackBar.make(mContext).text(msg).duration(2500).show();
//            }
//        });
        if(snackView != null)
            Snackbar.make(snackView, msg, Snackbar.LENGTH_SHORT).show();
        else
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void onFinish(){}
}
