package com.strongit.framedemo.global;

/**
 * 用于存储SharedPreferenced各变量的Key值<br />
 * 命名规范：SP_[参数类型]_[参数作用]<br />
 * 其中参数类型对应如下：String-S、boolean-B、int-I、float-F、long-L、Set<String>-SET
 * 作者：Mavio
 * 日期：2016/3/29.
 */
public interface SPKeys {
    /**
     * 是否是第一次启动应用
     * 第一次启动应用将会显示滑动引导页
     */
    public static final String SP_B_FIRST_LOAD = "isFirstLoad";
}
