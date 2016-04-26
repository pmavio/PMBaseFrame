package com.strongit.framedemo.ui;

import android.os.Bundle;

import com.avos.avoscloud.AVAnalytics;
import com.strongit.framedemo.api.Apis;
import com.strongit.framedemo.api.TestApi;
import com.strongit.framedemo.global.SPKeys;
import com.pmavio.pmbaseframe.ui.BaseFragment;

/**
 * 作者：Mavio
 * 日期：2016/3/1.
 */
public class MBaseFragment extends BaseFragment implements SPKeys{

    protected TestApi api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Apis.getTestApi();
    }

    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd(TAG);
    }

    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart(TAG);
    }
}
