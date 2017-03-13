package com.demo.androiddemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		for (int i = 0; i < 5; i++) {
			MyTaskRunnable runnable = new MyTaskRunnable("TaskRunable "+i);
			ServiceThread.getHandler().post(runnable);
		}
	}
	
	class MyTaskRunnable implements Runnable{
		private String info;
		 public MyTaskRunnable(String info) {
			this.info = info;
		}

		@Override
		public void run() {
			Log.i("MyTaskRunnable", "info:"+info+", sleep");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("MyTaskRunnable", "info:"+info+", up");
		}
	}
}
