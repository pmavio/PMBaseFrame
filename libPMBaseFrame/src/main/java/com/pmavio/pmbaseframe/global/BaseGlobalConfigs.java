package com.pmavio.pmbaseframe.global;

import android.os.Build;

import com.pmavio.pmbaseframe.BuildConfig;

/**
 * 作者：Mavio
 * 日期：2016/2/29.
 */
public class BaseGlobalConfigs {
    public static boolean D = BuildConfig.DEBUG;

    /**
     * 数据库名称
     */
    public static String DBNAME = "pmbaseframe.db";

    /**
     * 数据库版本
     */
    public static int DBVERSION = 1;
}
