package com.demo.androiddemo.customview;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;

public class LoadingCircleView extends BaseView {
	
	/**
	 * ����С��Ľ�����
	 * ˮƽ�����ϣ������һ����С��С��˳ʱ������С��������ұ���һ��С�����¸�����С��
	 * @param context
	 */
	
	private float radius_0 = 0.5f;
	private float radius_1 = radius_0+0.5f;
	private float radius_2 = radius_1+0.5f;
	private float radius_3 = radius_2+0.5f;
	private float radius_4 =radius_3+0.5f;
	private float radius_5 = radius_4+0.5f;
	
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
//		super.onDraw(canvas);
		drawCircle1(canvas, radius_5);
	}
	
	private void drawCircle1(Canvas canvas,float radius){
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		float center_x = width/2;//��ͼ���ĵ�x����
		float center_y = height/2;//��ͼ���ĵ�y����
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.YELLOW);
		canvas.drawRect(0, 0, width, height, paint);
		paint.setColor(Color.BLUE);
		paint.setStyle(Style.STROKE);
		float big_circle_r = getBigCircleRadius(width/2,height/2);
		canvas.drawCircle(center_x, center_y, big_circle_r, paint);
		drawCircleSun(canvas, big_circle_r,big_circle_r*0.15f , center_x, center_y, 180,paint);
		drawCircleSun(canvas, big_circle_r,big_circle_r*0.2f , center_x, center_y, 270,paint);
//		drawCircleSun(canvas, big_circle_r,big_circle_r*0.25f , center_x, center_y, 300,paint);
		drawCircleSun(canvas, big_circle_r,big_circle_r*0.3f , center_x, center_y, 0,paint);
		drawCircleSun(canvas, big_circle_r,big_circle_r*0.35f , center_x, center_y, 90,paint);
//		drawCircleSun(canvas, big_circle_r,big_circle_r*0.4f , center_x, center_y, 120,paint);
	}
	
	
	//��ȡ�������������ڴ�Բ�εİ뾶
	private float getBigCircleRadius(int width,int height) {
		int p_left = getPaddingLeft();
		int p_top = getPaddingTop();
		int p_right = getPaddingRight();
		int p_bottom = getPaddingBottom();
		int totalX = (width-p_left-p_right);
		int totalY = (height-p_top-p_bottom);
		return Math.min(totalX, totalY)/2;
	}
	
	private void drawCircleSun(Canvas canvas,float radius,float targetRaidus,float center_x,float center_y,double angle,Paint paint){
		float targetX = (float) (center_x+radius*Math.cos(angle));
		float targetY = (float) (center_y+radius*Math.sin(angle));
		paint.setColor(Color.RED);
		canvas.drawCircle(targetX, targetY, targetRaidus, paint);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mode_w = MeasureSpec.getMode(widthMeasureSpec);
		int size_w = MeasureSpec.getSize(widthMeasureSpec);

		int mode_h = MeasureSpec.getMode(heightMeasureSpec);
		int size_h = MeasureSpec.getSize(heightMeasureSpec);
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
//		setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, mode_w), 
//				MeasureSpec.makeMeasureSpec(height, mode_h));
		setMeasuredDimension(width, height);
	}
	
}
