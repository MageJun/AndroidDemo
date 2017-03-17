package com.demo.androiddemo.customview;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;

public class ReticularPathView extends BaseView {
	
	/**
	 * 实现闪电效果图
	 * @param context
	 */
	public ReticularPathView(Context context) {
		super(context);
		init(context);
	}

	public ReticularPathView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public ReticularPathView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	private Path mPath;
	private Paint mPaint;
	private int mWidth = 200;
	private int mHeight = 200;
	
	private float mPaintWidth = 5;
	private int mLightingColor = 0xFFC9D0FF;
	private int mBgColor = 0xFF24417D;
	
	private float mCurTouch_x;
	private float mCurTouch_y;
	
	private float mSegmentLength = 20;
	private float mDeviation = 25;
	
	private Vibrator mVibrator ;
	
	
	private void init(Context context){
		mPath = new Path();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(mPaintWidth);
		mPaint.setColor(mLightingColor);
		
		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		mPath.reset();
		canvas.drawColor(mBgColor);
		if(mCurTouch_x!=0||mCurTouch_y!=0){
			Random random = new Random();
			float r = random.nextFloat()*2+0.1f;
			if(r==0){
				r = 1;
			}
			DiscretePathEffect pathEffect = new DiscretePathEffect(mSegmentLength*r, mDeviation*r);
			mPaint.setPathEffect(pathEffect);
			float width = getMeasuredWidth();
			float height = getMeasuredHeight();
			Path leftPath = new Path();
			leftPath.moveTo(0, 0);
			leftPath.lineTo(mCurTouch_x, mCurTouch_y);
			leftPath.lineTo(0, height);
			
			Path rightPath = new Path();
			rightPath.moveTo(width, 0);
			rightPath.lineTo(mCurTouch_x, mCurTouch_y);
			rightPath.lineTo(width, height);
			
			mPath.addPath(leftPath);
			mPath.addPath(rightPath);
			canvas.drawPath(mPath, mPaint);
			
		}else{
			super.onDraw(canvas);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			mCurTouch_x = event.getX();
			mCurTouch_y = event.getY();
			mVibrator.vibrate(1000);
			break;
		case MotionEvent.ACTION_UP:
			mCurTouch_x = 0;
			mCurTouch_y = 0;
			if(mVibrator.hasVibrator()){
				mVibrator.cancel();
			}
			break;
		}
		invalidate();
		return true;
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
