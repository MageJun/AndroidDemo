package com.demo.androiddemo.customview;

import android.content.Context;
import android.util.AttributeSet;

public class LoadingCircleView extends BaseView {
	
	/**
	 * 六个小球的进度条
	 * 水平方向上，左边有一个最小的小球，顺时针增大小球体积，右边有一个小球，上下各两个小球
	 * @param context
	 */
	
	private double radius_0 = 0.5;
	private double radius_1 = radius_0+0.5;
	private double radius_2 = radius_1+0.5;
	private double radius_3 = radius_2+0.5;
	private double radius_4 =radius_3+0.5;
	private double radius_5 = radius_4+0.5;

	public LoadingCircleView(Context context) {
		super(context);
	}

	public LoadingCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
}
