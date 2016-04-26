package com.pmavio.pmbaseframe.title;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pmavio.pmbaseframe.global.BaseGlobalConfigs;
import com.pmavio.pmbaseframe.title.annotations.Title;
import com.pmavio.pmbaseframe.title.interfaces.ITitleDelegete;
import com.pmavio.pmbaseframe.ui.BaseActivity;

/**
 * 标题栏统一代理
 * 作者：Mavio
 * 日期：2016/3/3.
 */
public class LinearTitleDelegete implements ITitleDelegete{

    protected BaseActivity act;
    Title titleAnno;

    protected View dealedContentView;
    protected Toolbar titleView;
    protected View contentView;

    String titleStr;

    public LinearTitleDelegete(BaseActivity act){
        this.act = act;
        titleAnno = act.getClass().getAnnotation(Title.class);
    }

    protected void setTitle() {
        if(titleAnno == null){
            dealedContentView = contentView;
            return;
        }

        LinearLayout dealedContentView = new LinearLayout(act);
        dealedContentView.setOrientation(LinearLayout.VERTICAL);
        dealedContentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        dealedContentView.addView(titleView);
        if(contentView != null) {
            if (contentView.getParent() != null) {
                if (BaseGlobalConfigs.D) {
                    // debug模式下什么都不做，等下面代码报错
                } else {
                    ((ViewGroup) contentView.getParent()).removeView(contentView);
                }
            }
            dealedContentView.addView(contentView);
        }
        this.dealedContentView = dealedContentView;
    }

    protected void initTitleView(){
        if(titleAnno != null){
            titleView = new Toolbar(act);
            titleStr = titleAnno.value();
            titleView.setNavigationContentDescription(titleStr);

            titleView.setVisibility(View.VISIBLE);
        }
    }

    protected void initContentView(){
        if(titleAnno != null){
            int layoutRes = titleAnno.layoutRes();
            if(layoutRes != 0)
                contentView = act.getLayoutInflater().inflate(layoutRes, null);
        }
    }

    @Override
    public void setContentView(View contentView) {
        if(titleAnno == null) return;
        this.contentView = contentView;
        initTitleView();
        initContentView();
        setTitle();
    }

    @Override
    public Toolbar getTitleView() {
        return titleView;
    }

    @Override
    public void setTitleText(final String titleText) {
        titleStr = titleText;
        titleView.post(new Runnable() {
            @Override
            public void run() {
                titleView.setNavigationContentDescription(titleStr);
            }
        });
    }
}
