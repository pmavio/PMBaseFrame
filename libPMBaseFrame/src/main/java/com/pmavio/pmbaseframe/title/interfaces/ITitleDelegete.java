package com.pmavio.pmbaseframe.title.interfaces;

import android.view.View;

import com.pmavio.pmbaseframe.ui.BaseActivity;

/**
 * 作者：Mavio
 * 日期：2016/3/3.
 */
public interface ITitleDelegete {
    void setContentView(View contentView);
    View getTitleView();
    void setTitleText(String titleText);
}
