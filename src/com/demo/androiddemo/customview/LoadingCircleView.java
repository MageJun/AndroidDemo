package com.demo.androiddemo.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;

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
	
	private int m_width = 100;
	private int m_height = 100;

	public LoadingCircleView(Context context) {
		super(context);
	}

	public LoadingCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
	private void drawCircle(Canvas canvas,int radius){
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int center_x = width/2;//视图中心点x坐标
		int center_y = height/2;//视图中心点y坐标
		
		double big_circle_r = getBigCircleRadius(width,height);
		
	}
	
	
	//获取整个进度条所在大圆形的半径
	private int getBigCircleRadius(int width,int height) {
		int p_left = getPaddingLeft();
		int p_top = getPaddingTop();
		int p_right = getPaddingRight();
		int p_bottom = getPaddingBottom();
		int totalX = (width-p_left-p_right);
		int totalY = (height-p_top-p_bottom);
		return Math.min(totalX, totalY);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mode_w = MeasureSpec.getMode(widthMeasureSpec);
		int size_w = MeasureSpec.getMode(widthMeasureSpec);

		int mode_h = MeasureSpec.getMode(heightMeasureSpec);
		int size_h = MeasureSpec.getMode(heightMeasureSpec);
		LayoutParams lp = getLayoutParams();
		int width = size_w;
		int height = size_h;
		switch (mode_w) {
		case MeasureSpec.AT_MOST:
		case MeasureSpec.UNSPECIFIED:
			if (lp.width == LayoutParams.WRAP_CONTENT) {
				width = m_width;
			}
			break;
		}
		switch (mode_h) {
		case MeasureSpec.AT_MOST:
		case MeasureSpec.UNSPECIFIED:
			if (lp.height == LayoutParams.WRAP_CONTENT) {
				height = m_height;
			}
			break;
		}
		setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, mode_w), 
				MeasureSpec.makeMeasureSpec(height, mode_h));
	}
	
}
