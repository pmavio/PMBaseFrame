package com.pmavio.pmbaseframe.ui;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.pmavio.pmbaseframe.R;

public class SingleFragmentActivity extends BaseActivity {
	public static final String DATA_CLASSNAME = "classname";

	Fragment fm;
	
	@Override
	protected void onCreate(Bundle arg0) {
		init();
		super.onCreate(arg0);
		setContentView(R.layout.act_singlefragment);
		fmManager.beginTransaction().replace(R.id.fl_single, fm).commit();
	}

	void init(){

		Intent intent = getIntent();
		if(intent == null){
			finish();
			return;
		}

		Serializable o_cname = intent.getSerializableExtra(DATA_CLASSNAME);
		if(o_cname == null){
			finish();
			return;
		}

		if(o_cname instanceof Class){
			Class<?> c_cname = (Class<?>) o_cname;
			if(Fragment.class.isAssignableFrom(c_cname)){
				try {
					fm = (Fragment) c_cname.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
			}else{
				finish();
				return;
			}
		}else{
			finish();
			return;
		}
	}
}
