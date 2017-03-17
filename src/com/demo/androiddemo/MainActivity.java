package com.demo.androiddemo;

import com.demo.androiddemo.customview.LoadingArrawView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends BaseActivity {

	private LoadingArrawView mLoadingArraw;

	public void onClick(View v){
		switch (v.getId()) {
		case R.id.start:
			mLoadingArraw.startLoading();
			break;
		case R.id.stop:
			mLoadingArraw.loadingComplete();
			break;
		case R.id.lightning:
			Intent intent = new Intent(this,ReticularTestActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onActivityCreate() {
		setContentView(R.layout.activity_main);
		mLoadingArraw = (LoadingArrawView) findViewById(R.id.loadingArrawView1);
	}
}
