package com.demo.androiddemo.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.ViewGroup.LayoutParams;

public class LoadingArrawView extends BaseView {

	private Paint mPaint;
	private Path mPathArraw_1,mPathArraw_2;
	
	private int mWidth = 100;
	private int mHeight =100;
	
	private int mStrokeWidth = 2;

	public LoadingArrawView(Context context) {
		super(context);
		init();
	}

	public LoadingArrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(mStrokeWidth);
		mPathArraw_1 = new Path();
		mPathArraw_2 = new Path();
	}
	
	private int mMoveTime = 0;
	private int mSpeed = 100;
	@Override
	protected void onDraw(Canvas canvas) {
		mPathArraw_1.reset();
		mPathArraw_2.reset();
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		
		int padingX = getPaddingLeft()+getPaddingRight();
		int padingY = getPaddingTop()+getPaddingBottom();
		//先画出外围的圆
		float center_x = width/2;
		float center_y = height/2;
		float radius = Math.min((width-padingX-mStrokeWidth*2), (height-padingY-mStrokeWidth*2))/2;
		canvas.drawCircle(center_x, center_y, radius, mPaint);
		
		//画出箭头1
		//先画出竖线，确认初始开始时的起点和终点坐标
		if (mMoveTime <= radius*2 ) {
			float arrawLength = radius;
			float arraw1_start_x = center_x;
			float arraw1_start_y = center_y - arrawLength / 2 + mMoveTime;
			float arraw1_dst_x = center_x;
			float arraw1_dst_y = arraw1_start_y + arrawLength;

			mPathArraw_1.moveTo(arraw1_start_x, arraw1_start_y);
			mPathArraw_1.lineTo(arraw1_dst_x, arraw1_dst_y);

			// 画出两边的斜线
			// 设定两边斜线和中间直线的角度是45度，直角边长度等于中间直线长度的1/4，求斜边终点坐标位置
			Path path = new Path();
			float arraw1_lr_length = arrawLength / 4;
			// 左边斜边起点坐标
			float arraw1_left_x = arraw1_dst_x - arraw1_lr_length;
			float arraw1_left_y = arraw1_dst_y - arraw1_lr_length;
			// 右边斜边起点坐标
			float arraw1_right_x = arraw1_dst_x + arraw1_lr_length;
			float arraw1_right_y = arraw1_dst_y - arraw1_lr_length;

			path.moveTo(arraw1_left_x, arraw1_left_y);
			path.lineTo(arraw1_dst_x, arraw1_dst_y);
			path.lineTo(arraw1_right_x, arraw1_right_y);
			mPathArraw_1.addPath(path);
			
			
			//画出第二个箭头，开始的时候是隐藏的
			float arraw2_start_x = center_x;
			float arraw2_start_y = center_y - 5*arrawLength  + mMoveTime;
			float arraw2_dst_x = center_x;
			float arraw2_dst_y = arraw2_start_y + arrawLength;

			mPathArraw_2.moveTo(arraw2_start_x, arraw2_start_y);
			mPathArraw_2.lineTo(arraw2_dst_x, arraw2_dst_y);

			// 画出两边的斜线
			// 设定两边斜线和中间直线的角度是45度，直角边长度等于中间直线长度的1/4，求斜边终点坐标位置
			Path path2 = new Path();
			float arraw2_lr_length = arrawLength / 4;
			// 左边斜边起点坐标
			float arraw2_left_x = arraw2_dst_x - arraw2_lr_length;
			float arraw2_left_y = arraw2_dst_y - arraw2_lr_length;
			// 右边斜边起点坐标
			float arraw2_right_x = arraw2_dst_x + arraw2_lr_length;
			float arraw2_right_y = arraw2_dst_y - arraw2_lr_length;

			path2.moveTo(arraw2_left_x, arraw2_left_y);
			path2.lineTo(arraw2_dst_x, arraw2_dst_y);
			path2.lineTo(arraw2_right_x, arraw2_right_y);
			
			mPathArraw_2.addPath(path2);
		}
		
		canvas.drawPath(mPathArraw_1, mPaint);
		canvas.drawPath(mPathArraw_2, mPaint);
		postInvalidateDelayed(mSpeed);
		mMoveTime+=radius/5;
		if(mMoveTime>=radius*2)
			mMoveTime=0;
	}
	
	private float validateBound(float length){
		
		float result = 0;
		return result;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		LayoutParams lp = getLayoutParams();
		
		if(lp.width==LayoutParams.WRAP_CONTENT){
			if(widthMode==MeasureSpec.AT_MOST||widthMode==MeasureSpec.EXACTLY){
				widthSize = mWidth;
			}
		}
		
		if(lp.height==LayoutParams.WRAP_CONTENT){
			if(heightMode==MeasureSpec.AT_MOST||widthMode==MeasureSpec.EXACTLY){
				heightSize = mHeight;
			}
		}
		
		setMeasuredDimension(widthSize, heightSize);
	}
	
	
}
