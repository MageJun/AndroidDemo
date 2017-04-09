package com.demo.androiddemo;

import com.demo.androiddemo.customview.LoadingArrawView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends BaseActivity {

	protected static final String TAG = MainActivity.class.getSimpleName();
	private LoadingArrawView mLoadingArraw;
	Callback callback = new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	public void onClick(View v){
		switch (v.getId()) {
		case R.id.start:
//			mLoadingArraw.startLoading();
			InnerThread.getHandler().post(testRunnable);
			break;
		case R.id.stop:
//			mLoadingArraw.loadingComplete();
			InnerThread.getHandler().removeCallbacks(testRunnable);
			break;
		case R.id.second:
			Intent intent = new Intent(this,ReticularTestActivity.class);
//			AppMetaDatas amds = MetaDataManager.getInstance(this).getAppMetaDatas(MetaDataKey.KEY_HOME_UI);
//			Log.i(TAG, "amds: "+amds);
//			intent.putExtra("data", amds);
			startActivity(intent);
			break;
		case R.id.third:
			Intent third = new Intent(this,ThirdActivity.class);
			startActivity(third);
			break;
		}
	}
	
	private Runnable testRunnable = new Runnable() {
		
		@Override
		public void run() {
			Log.i(TAG, "testRunnable run() ");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i(TAG, "testRunnable run() exit");
		}
	};

	@Override
	public void onActivityCreate() {
		setContentView(R.layout.activity_main);
		mLoadingArraw = (LoadingArrawView) findViewById(R.id.loadingArrawView1);
	}
	
}

 class InnerThread extends HandlerThread{

	private InnerThread(String name) {
		super(name);
	}
	static InnerThread sInstance;
	static Handler sHandler;
	
	static InnerThread getInstance(){
		if(sInstance==null){
			sInstance = new InnerThread("InnerThread");
		}
		return sInstance;
	}
	   private static void ensureThreadLocked() {
	        if (sInstance == null) {
	            sInstance = getInstance();
	            sInstance.start();
	            sHandler = new Handler(sInstance.getLooper());
	        }
	    }
	public static Handler getHandler(){
		synchronized (InnerThread.class) {
			ensureThreadLocked();
			return sHandler;
		}
	}
	
}
