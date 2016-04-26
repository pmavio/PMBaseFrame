package com.strongit.framedemo;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.pmavio.pmbaseframe.global.BaseApplication;
import com.strongit.framedemo.bean.*;
import com.strongit.framedemo.global.GlobalConfigs;
import com.squareup.leakcanary.LeakCanary;

import cn.sharesdk.framework.ShareSDK;

/**
 * 作者：Mavio
 * 日期：2016/3/1.
 */
public class TTApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        // 内存泄露检测
        LeakCanary.install(this);

        initShareSDK();

//        initLeanCloud();

//        initORM();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        //多包打包，解决方法数超65535问题，同时需要在build.gradle中配置
//        MultiDex.install(this);
    }

    private void initShareSDK(){
        ShareSDK.initSDK(this);
    }

    /**
     * LeanCloud初始化，包括数据统计分析、即时聊天
     */
    private void initLeanCloud(){
        AVOSCloud.initialize(this, "0Wh5GBAck9qXEWaqrVng4Qz0-gzGzoHsz", "xN4AAYfN9TGLVIUT12S6B071");

        AVAnalytics.enableCrashReport(this, true);
    }

    /**
     * ActiveAndroid ORM 初始化
     */
    private void initORM(){
        //在此添加所有需要写入数据库的类
        Configuration.Builder builder = new Configuration.Builder(this)
                .setDatabaseName(GlobalConfigs.DBNAME)
                .setDatabaseVersion(GlobalConfigs.DBVERSION)
                .addModelClasses(User.class);

        Configuration dbConfiguration = builder.create();
        ActiveAndroid.initialize(dbConfiguration);

    }

}
