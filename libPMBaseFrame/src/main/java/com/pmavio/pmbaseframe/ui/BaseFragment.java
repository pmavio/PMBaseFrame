package com.pmavio.pmbaseframe.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.pmavio.pmbaseframe.utils.CheatSheet;
import com.pmavio.pmbaseframe.utils.CircleTransform;
import com.pmavio.pmbaseframe.utils.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * 作者：Mavio
 * 日期：2016/3/1.
 */
public class BaseFragment extends Fragment implements CheatSheet.AnswerSheet{
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 图片显示/下载
     */
    public RequestManager imager;

    protected LayoutInflater mInflater;

    public Handler mHandler;

    protected BaseFragment mFragment;

    protected CircleTransform circleTransform;

    protected Activity act;

    protected EventBus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mFragment = this;
        super.onCreate(savedInstanceState);
        act = getActivity();
        imager = Glide.with(this);
        mInflater = getLayoutInflater(savedInstanceState);
        mHandler = new Handler();
        circleTransform = new CircleTransform(act);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    /**
     * 使用rx提供的方法在主线程执行任务
     * @param task
     */
    protected void runOnUiThread(Action0 task){
        AndroidSchedulers.mainThread().createWorker().schedule(task);
    }

    protected void runOnUiThread(Action0 task, long delay){
        AndroidSchedulers.mainThread().createWorker().schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Toast显示消息
     * @param v
     */
    public void toast(View v){
        if(v == null || v.getParent() != null) return;
        Toast toast = new Toast(mFragment.getActivity());
        toast.setView(v);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Toast显示消息
     * @param msg
     */
    public void toast(final String msg){
        if(isEmpty(msg))return;
        runOnUiThread(new Action0() {
            public void call() {
                Toast.makeText(mFragment.getActivity(), msg, Toast.LENGTH_SHORT).show();
                Log.d(TAG, msg);
            }
        });
    }

    /**
     * 判断字符串是否为空或空字符串
     * @param str
     * @return
     */
    public boolean isEmpty(String str){
        return TextUtils.isEmpty(str);
    }

    @Subscribe
    public void onGetCheatSheet(CheatSheet sheet){
        sheet.getWith(this);
    }

    @Subscribe(sticky = true)
    public void onGetCheatSheetSticky(CheatSheet sheet){
        sheet.getWith(this);
    }
}
