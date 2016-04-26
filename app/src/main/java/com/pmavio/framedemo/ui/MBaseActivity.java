package com.strongit.framedemo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVAnalytics;
import com.pmavio.pmbaseframe.test.R;
import com.strongit.framedemo.api.Apis;

import com.strongit.framedemo.api.*;
import com.strongit.framedemo.global.SPKeys;
import com.pmavio.pmbaseframe.title.annotations.Title;
import com.pmavio.pmbaseframe.ui.BaseActivity;


/**
 * 作者：Mavio
 * 日期：2016/3/1.
 */
public abstract class MBaseActivity extends BaseActivity implements SPKeys {

    protected static final int TOOLBAR_ID = 123;

    protected TestApi api;

    protected View contentView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initTitleAnnotation();
        api = Apis.getTestApi();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void initTitleAnnotation(){
        Title titleAnno = getClass().getAnnotation(Title.class);
        if(titleAnno == null) return;

        String titleStr = titleAnno.value();
        int layoutRes = titleAnno.layoutRes();

        contentView = mInflater.inflate(layoutRes, null);
        if(contentView == null) return;
        setContentView(contentView);

        if(contentView instanceof ViewGroup){
            findToolbar((ViewGroup) contentView);
        }

        if(mToolbar != null){
            mToolbar.setTitle(titleStr);
            mToolbar.setSubtitle(null);
            mToolbar.setTitleTextColor(Color.WHITE);
            mToolbar.setBackgroundResource(R.color.app_primary_color);
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);

            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    protected void findToolbar(ViewGroup vg){
        if(vg != null)
            for (int i=0, size=vg.getChildCount(); i<size; i++){
                View v = vg.getChildAt(i);
                if(v instanceof Toolbar){
                    mToolbar = (Toolbar) v;
                    return;
                }else if (v instanceof ViewGroup){
                    findToolbar((ViewGroup) v);
                    if(mToolbar != null) return;
                }
            }
    }
}
