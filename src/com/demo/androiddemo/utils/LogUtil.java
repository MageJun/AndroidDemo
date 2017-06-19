package com.demo.androiddemo.utils;

import android.util.Log;

public class LogUtil {
	
	private LogUtil(){}
	
	private static boolean isDebug = true;
	
	public static void enableDebug(){
		isDebug = true;
	}
	public static void disableDebug(){
		isDebug = false;
	}
	
	public static void i(String tag,String msg){
		if(isDebug){
			Log.i(tag, msg);
		}
	}
	public static void d(String tag,String msg){
		if(isDebug){
			Log.d(tag, msg);
		}
	}
	public static void e(String tag,String msg){
		if(isDebug){
			Log.e(tag, msg);
		}
	}

}
