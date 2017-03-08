package com.demo.androiddemo.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;

public class LoadingCircleView extends BaseView {
	
	/**
	 * ����С��Ľ�����
	 * ˮƽ�����ϣ������һ����С��С��˳ʱ������С��������ұ���һ��С�����¸�����С��
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
		int center_x = width/2;//��ͼ���ĵ�x����
		int center_y = height/2;//��ͼ���ĵ�y����
		
		double big_circle_r = getBigCircleRadius(width,height);
		
	}
	
	
	//��ȡ�������������ڴ�Բ�εİ뾶
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
