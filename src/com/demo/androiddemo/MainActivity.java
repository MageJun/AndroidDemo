package com.demo.androiddemo;

import com.demo.androiddemo.customview.LoadingArrawView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	private LoadingArrawView mLoadingArraw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLoadingArraw = (LoadingArrawView) findViewById(R.id.loadingArrawView1);
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.start:
			mLoadingArraw.startLoading();
			break;
		case R.id.stop:
			mLoadingArraw.stopLoading();
			break;
		}
	}
}
