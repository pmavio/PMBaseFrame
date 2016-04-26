package com.strongit.framedemo.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pmavio.pmbaseframe.test.BuildConfig;
import com.pmavio.pmbaseframe.test.R;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 作者：Mavio
 * 日期：2016/3/18.
 */
public class ShareUtils {
    private static boolean IS_INITIALIZED = false;
    private static final String SIZE_URL = "http://www.strongit.com.cn";

    public static void share(Context context, ShareObject obj){
        if(!IS_INITIALIZED) {
            ShareSDK.initSDK(context);
            IS_INITIALIZED = true;
        }

        if(obj == null && !BuildConfig.DEBUG) return;

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(obj.title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(obj.url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(obj.text);

        if(obj.image != null){
            if(obj.image.startsWith("http")){
                oks.setImageUrl(obj.image);
            }else {
                File imageFile = new File(obj.image);
                if (imageFile.exists()) {
                    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                    oks.setImagePath(obj.image);//确保SDcard下面存在此张图片
                    Glide.with(context).load(obj.image).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            //TODO
                        }
                    });
                }
            }
        }

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(obj.url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(obj.comment);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(SIZE_URL);

        //TODO
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });

        // 启动分享GUI
        oks.show(context);
    }

    public static class ShareObject {
        public String title;
        public String text;
        public String url;
        public String comment;
        public String image;

    }
}
