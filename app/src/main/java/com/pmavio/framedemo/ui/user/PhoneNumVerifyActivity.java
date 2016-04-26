package com.strongit.framedemo.ui.user;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.pmavio.pmbaseframe.utils.CheatSheet;
import com.pmavio.pmbaseframe.test.R;
import com.strongit.framedemo.ui.MBaseActivity;
import com.pmavio.pmbaseframe.title.annotations.Title;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 作者：Mavio
 * 日期：2016/3/4.
 */
@Title(value = "注册", layoutRes = R.layout.act_phonenumverify)
public class PhoneNumVerifyActivity extends MBaseActivity {
    public static final String CODE_MODE = "PhoneNumVerifyMode";
    public static final int MODE_REGISTER = 1;
    public static final int MODE_FINDPASS = 2;

    private static final int MAX_VERIFY_COUNT = 120;
    int count = 0;


    @Bind(R.id.et_phonenum)
    EditText etPhonenum;
    @Bind(R.id.til_phonenum)
    TextInputLayout tilPhonenum;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.til_password)
    TextInputLayout tilPassword;
    @Bind(R.id.et_verify)
    EditText etVerify;
    @Bind(R.id.til_verify)
    TextInputLayout tilVerify;
    @Bind(R.id.bt_getverifycode)
    Button btGetverifycode;
    @Bind(R.id.bt_ensure)
    Button btEnsure;

    @CheatSheet.HardQuestion(code = CODE_MODE)
    int mode;

    //TODO
    String verifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initWidgets();
    }

//    @Override
//    public void onGetCheatSheet(CheatSheet sheet) {
//
//    }
//
//    @Subscribe(sticky = true)
//    public void onGetEventMessage(CheatSheet sheet){
//        sheet.getWith(this);
//        if(sheet.checkCode(CODE_MODE)){
//            int mode = sheet.get();
//            onModeChange(mode);
//        }
//    }

    protected void onModeChange(int mode){
        if(this.mode == mode){
            return ;
        }
        if(mode == MODE_REGISTER){
            mToolbar.setTitle("注册");
            etPhonenum.setEnabled(true);
            btEnsure.setText("注册");
        }else if(mode == MODE_FINDPASS){
            mToolbar.setTitle("找回密码");
            etPhonenum.setText("18657818123");//TODO
            etPhonenum.setEnabled(false);
            tilPassword.setHint("新密码");
            btEnsure.setText("找回密码");
        }
    }


    protected void initWidgets(){
        etPhonenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tilPhonenum.setError("");
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tilPassword.setError("");
            }
        });
        etVerify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tilVerify.setError("");
            }
        });
    }

    @OnClick(R.id.bt_getverifycode)
    public void onClickVerify(){
        //TODO
        String username = etPhonenum.getText().toString();
        if(username.length() != 11){
            tilPhonenum.setError("请输入正确的手机号");
            etPhonenum.requestFocus();
            return;
        }

        Random rd = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<6; i++){
            sb.append(rd.nextInt(10));
        }
        verifyCode = sb.toString();
        Snackbar.make(btGetverifycode, "验证码是"+verifyCode, Snackbar.LENGTH_LONG).show();

        countVerifyTime();
    }

    @OnClick(R.id.bt_ensure)
    public void onClickEnsure(){
        String username = etPhonenum.getText().toString();
        if(username.length() != 11){
            tilPhonenum.setError("请输入正确的手机号");
            etPhonenum.requestFocus();
            return;
        }

        String password = etPassword.getText().toString();
        if(password.length() < 6 || password.length() > 20){
            tilPassword.setError("密码长度应在6-20位之间");
            etPassword.requestFocus();
            return;
        }

        String verify = etVerify.getText().toString();
        if(verify.length() != 6 || !verify.equals(verifyCode)){
            tilVerify.setError("验证码不正确");
            etVerify.requestFocus();
            return;
        }

        //TODO
        Snackbar.make(btEnsure, "正在操作...", Snackbar.LENGTH_SHORT).show();
    }

    protected void updateVerifyButton(){
        runOnUiThread(new Action0() {
            @Override
            public void call() {
                if (count == 0) {
                    btGetverifycode.setEnabled(true);
                    btGetverifycode.setText("获取验证码");
                } else {
                    btGetverifycode.setText(count + "s");
                }
            }
        });
    }

    protected void countVerifyTime(){
        btGetverifycode.setEnabled(false);
        Schedulers.newThread().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                count = MAX_VERIFY_COUNT;
                while (count > 0){
                    if(count < 0) break;
                    updateVerifyButton();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                    count --;
                }
            }
        });
    }
}
