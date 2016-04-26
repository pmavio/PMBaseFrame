package com.strongit.framedemo.ui.user;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pmavio.pmbaseframe.test.R;
import com.strongit.framedemo.bean.User;
import com.strongit.framedemo.ui.MBaseActivity;
import com.pmavio.pmbaseframe.title.annotations.Title;
import com.pmavio.pmbaseframe.utils.CheatSheet;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;


/**
 * 作者：Mavio
 * 日期：2016/3/3.
 */
@Title(value = "登录", layoutRes = R.layout.act_login)
public class LoginActivity extends MBaseActivity {


    @Bind(R.id.et_username1)
    EditText et_username;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.bt_login)
    Button bt_login;
    @Bind(R.id.tv_findpass)
    TextView tv_findpass;
    @Bind(R.id.tv_register)
    TextView tv_register;
    @Bind(R.id.til_username)
    TextInputLayout til_username;
    @Bind(R.id.til_password)
    TextInputLayout til_password;
    @Bind(R.id.iv_login_qq)
    ImageView ivLoginQq;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_login_wx)
    ImageView ivLoginWx;
    @Bind(R.id.iv_login_wb)
    ImageView ivLoginWb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        initWidgets();
    }

    protected void initWidgets() {

        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                til_username.setError("");
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                til_password.setError("");
            }
        });
    }

    @OnClick(R.id.tv_findpass)
    public void onClickFindpass() {
        eventBus.postSticky(new CheatSheet(PhoneNumVerifyActivity.CODE_MODE, PhoneNumVerifyActivity.MODE_FINDPASS) {
        });
        startActivity(PhoneNumVerifyActivity.class);
    }

    @OnClick(R.id.tv_register)
    public void onClickRegister() {
        eventBus.postSticky(new CheatSheet(PhoneNumVerifyActivity.CODE_MODE, PhoneNumVerifyActivity.MODE_REGISTER) {
        });
        startActivity(PhoneNumVerifyActivity.class);
    }

    @OnClick(R.id.bt_login)
    public void onClickLogin() {
        String username = et_username.getText().toString();
        if (username.length() != 11) {
            til_username.setError("请输入正确的手机号");
            et_username.requestFocus();
            return;
        }

        String password = et_password.getText().toString();
        if (password.length() < 6 || password.length() > 20) {
            til_password.setError("密码长度应在6-20位之间");
            et_password.requestFocus();
            return;
        }

        //TODO 登录
//        Snackbar.make(bt_login, "正在登录...", Snackbar.LENGTH_SHORT).show();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        CheatSheet sheet = new CheatSheet("test1", 123, true, 0.5f) {
        };
        sheet.put("test11", 12345);
        sheet.put("test12", false);
        sheet.put("test13", 0.5);
        eventBus.postSticky(sheet);

        eventBus.postSticky(new CheatSheet("HomeFragment", user) {
        });


//        api.login(username, password).enqueue(new BaseCalback<ArchResponse>(act) {
//            @Override
//            public void onResponse(ArchResponse response) {
//                User user = response.getResult(User.class);
        User.setLoginUser(user);
//        startActivity(MainActivity.class);
//        finish();
//            }
//        });
    }

    @OnClick({R.id.iv_login_qq, R.id.iv_login_wx, R.id.iv_login_wb})
    public void onClick(View view) {
        String platformName = null;
        switch (view.getId()) {
            case R.id.iv_login_qq:
                platformName = QQ.NAME;
                break;
            case R.id.iv_login_wx:
//                platformName = Wechat.NAME;
//                break;
                toast("微信尚未拿到开发者账号以及登录权限");
                return;
            case R.id.iv_login_wb:
                platformName = SinaWeibo.NAME;
                break;
        }

        Platform platform = ShareSDK.getPlatform(act, platformName);
        platform.removeAccount();
//        platform.SSOSetting(true); //设置为true表示关闭sso
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                toast(platform.getName() + "登录成功");
                if (hashMap == null) return;
//                Set<Map.Entry<String, Object>> sets = hashMap.entrySet();
//                StringBuffer sb = new StringBuffer();
//                for (Map.Entry e : sets) {
//                    sb.append(e.toString()).append("\n");
//                }
//                System.out.print(sb.toString());
                User user = new User();
                if (platform.getName().equals(QQ.NAME)) {
                    user.setNickname((String)hashMap.get("nickname"));
                    user.setHeadImg((String) hashMap.get("figureurl_qq_2"));
                } else if (platform.getName().equals(Wechat.NAME)) {

                } else if (platform.getName().equals(SinaWeibo.NAME)) {
                    user.setNickname((String)hashMap.get("name"));
                    user.setHeadImg((String) hashMap.get("avatar_large"));
                }
                User.setLoginUser(user);
//                startActivity(MainActivity.class);
//                finish();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                toast(platform.getName() + "登录失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                toast(platform.getName() + "登录取消");
            }
        });
//        platform.authorize();
        platform.showUser(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.deleteCache();
    }
}
