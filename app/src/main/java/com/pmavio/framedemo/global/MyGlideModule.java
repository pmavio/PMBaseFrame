package com.strongit.framedemo.global;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * Glide的配置
 * 作者：Mavio
 * 日期：2016/3/30.
 */
public class MyGlideModule implements GlideModule {
    @Override public void applyOptions(Context context, GlideBuilder builder) {
        //使图片更清晰
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

    }

    @Override public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }
}