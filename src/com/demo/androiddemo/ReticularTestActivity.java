package com.demo.androiddemo;

import com.zed3.sipua.common.metadata.AppMetaDatas;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ReticularTestActivity extends BaseActivity {

	private static final String TAG = ReticularTestActivity.class.getSimpleName();

	@Override
	public void onActivityCreate() {
		setContentView(R.layout.activity_reticular);
		AppMetaDatas amds = (AppMetaDatas) getIntent().getSerializableExtra("data");
		Log.i(TAG, "amds: "+amds);
	}

}
