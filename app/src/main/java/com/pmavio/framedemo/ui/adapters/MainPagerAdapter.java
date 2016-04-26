package com.strongit.framedemo.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 作者：Mavio
 * 日期：2016/3/11.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fmList;

    public MainPagerAdapter(FragmentManager fm, List<Fragment> fmList) {
        super(fm);
        this.fmList = fmList;
    }

    @Override
    public Fragment getItem(int position) {
        return getCount() > 0 ? fmList.get(position % getCount()) : null;
    }

    @Override
    public int getCount() {
        return fmList == null ? 0 : fmList.size();
    }
}
