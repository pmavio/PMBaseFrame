package com.pmavio.pmbaseframe.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.pmavio.pmbaseframe.utils.CheatSheet;
import com.pmavio.pmbaseframe.global.BaseApplication;
import com.pmavio.pmbaseframe.global.BaseGlobalConfigs;
import com.pmavio.pmbaseframe.utils.CircleTransform;
import com.pmavio.pmbaseframe.utils.Log;
import com.pmavio.pmbaseframe.utils.SPUtils;
import com.pmavio.pmbaseframe.utils.SystemMemoryUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * 作者：Mavio
 * 日期：2016/2/29.
 */
public abstract class BaseActivity extends AppCompatActivity implements CheatSheet.AnswerSheet{
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 获得返回结果后finish自身
     */
    public static final int REQUEST_RETURN = 99;

    /**
     * 获得返回结果后刷新自身
     */
    public static final int REQUEST_REFRESH = 98;

    /**
     * 图片显示/下载工具Glide设置
     */
    protected static Glide glide;

    /**
     * 图片显示/下载
     */
    public RequestManager imager;

    protected EventBus eventBus;

    protected LayoutInflater mInflater;

    public Handler mHandler;

    /**
     * 是否有修改内容
     */
    protected boolean hasModified = false;

    protected BaseActivity act;

    protected BaseApplication app;

    protected FragmentManager fmManager;

    protected SPUtils sp;

    /**
     * 屏幕宽度
     */
    public int screenWidth = 0;
    /**
     * 屏幕高度
     */
    public int screenHeight = 0;

    /**
     * 0-未登录
     * 1-已登录
     */
    protected int lastLoginStatus = 0;

    protected Toolbar mToolbar;

    protected CircleTransform circleTransform;

    @Override
    protected void onCreate(Bundle arg0) {
        act = (BaseActivity) this;
        app = (BaseApplication) getApplication();
        eventBus = EventBus.getDefault();
        super.onCreate(arg0);
        getWindowWH();
        mHandler = new Handler();
        if(glide == null) {
            glide = Glide.get(act);
        }
        imager = Glide.with(act);
        fmManager = getSupportFragmentManager();

        mInflater = LayoutInflater.from(act);
        eventBus.register(this);
        sp = new SPUtils(act);
        sp.getValues(act);
        circleTransform = new CircleTransform(act);
        beforeSetContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);//取消注释
    }

    protected void beforeSetContent(){}

    /**
     * 获取屏幕的高和宽
     */
    private void getWindowWH() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(BaseGlobalConfigs.D)
            Log.d(TAG, "onResume 当前可用内存为" + SystemMemoryUtil.getAvailMemory(act));
    }

    /**
     * 获得网络连接状况
     *
     * @return
     */
    protected boolean isNetConnected() {
        int state = ((BaseApplication)app).getNetworkState();
        return state == TelephonyManager.DATA_CONNECTED;
    }

    protected void killMe() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 提示框架当前activity是否有内容被改变
     * @return
     */
    protected boolean hasModified() {
        return hasModified;
    }

    protected void setModified(){
        hasModified = true;
    }

    /**
     * 标题自动隐藏的开关
     * @return
     */
    protected boolean isTitleAutoHide(){
        return false;
    }

    protected boolean isEnableTouchEffect(){
        return false;
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch(ev.getAction() & MotionEvent.ACTION_MASK){
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_POINTER_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    public void startActivity(Class<? extends Activity> actClass) {
        startActivityForResult(actClass, -1);
    }

    public void startActivityForResult(Class<? extends Activity> actClass, int requestCode){
        Intent intent = new Intent(this, actClass);
        super.startActivityForResult(intent, requestCode);
    }

    public void startFragment(Class<? extends Fragment> fmClass){
        startFragmentForResult(fmClass, -1);
    }

    public void startFragmentForResult(Class<? extends Fragment> fmClass, int requestCode){
        Intent intent = new Intent(this, SingleFragmentActivity.class);
        intent.putExtra(SingleFragmentActivity.DATA_CLASSNAME, fmClass);
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * 检测是否在wifi环境下
     * @return
     */
    public boolean isWifiConnected() {
        if (this != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断字符串是否为空或空字符串
     * @param str
     * @return
     */
    public boolean isEmpty(String str){
        return TextUtils.isEmpty(str);
    }

    /**
     * 以指定比例设置控件的尺寸（以宽为基准）
     * @param v
     * @param rateW
     * @param rateH
     */
    public static void setViewSizeRate(final View v, final int rateW, final int rateH){
        if(v == null) return;
        if(v.getWidth() > 0){
            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.height = (int) (((float)v.getWidth()) / rateW * rateH);
            v.setLayoutParams(params);
            return;
        }

        final ViewTreeObserver vto = v.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            boolean isDone = false;

            @Override
            public void onGlobalLayout() {
                if (isDone) return;
                ViewGroup.LayoutParams params = v.getLayoutParams();
                params.height = (int) (((float) v.getWidth()) / rateW * rateH);
                v.setLayoutParams(params);

                try {

                    vto.removeGlobalOnLayoutListener(this);
                } catch (Exception e) {
//					try {
//						vto.removeOnGlobalLayoutListener(this);
//					}catch(Exception e1){
//						isDone = true;
//					}
                } finally {
                    isDone = true;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_RETURN:
                onResultResult(resultCode, data);
            case REQUEST_REFRESH:
                OnResultRefresh(resultCode, data);
                break;
            default:
                break;
        }
    }

    protected void onResultResult(int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            setResult(RESULT_OK, data);
            finish();
        }
    }

    protected void OnResultRefresh(int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            reload();
        }
    }
    /**
     * 重新加载页面数据
     */
    public void reload(){

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
        Toast toast = new Toast(act);
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
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
                Log.d(TAG, msg);
            }
        });
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
