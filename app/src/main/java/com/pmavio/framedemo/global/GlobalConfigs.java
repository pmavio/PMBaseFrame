package com.strongit.framedemo.global;

import com.pmavio.pmbaseframe.global.BaseGlobalConfigs;
import com.pmavio.pmbaseframe.test.BuildConfig;

/**
 * 作者：Mavio
 * 日期：2016/3/1.
 */
public class GlobalConfigs extends BaseGlobalConfigs {
    static {
        D = BuildConfig.DEBUG;
        DBNAME = "testmoudle.realm";
        DBVERSION = 1;
    }
}
