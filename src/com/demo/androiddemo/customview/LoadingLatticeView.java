package com.demo.androiddemo.customview;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;

public class LoadingLatticeView extends BaseView {
	
	private int m_width = 100;
	private int m_height = 100;

	public LoadingLatticeView(Context context) {
		super(context);
		init();
	}

	public LoadingLatticeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	private Paint mPaint ;
	private int defPading = 10;
	private int mLatticeCounts =12;
	private int mLatticeLength = 10;
	private int mLatticeWidth = 2;
	
	private int mTargetColor = 0xfff3f3f3;
	private int mDefColor = 0xff000000;
	
	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);//设置抗锯齿
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(mDefColor);
		mPaint.setStrokeWidth(mLatticeWidth);
	}
	private int moveTimes = 0;
	@Override
	protected void onDraw(Canvas canvas) {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int center_x = width/2;
		int center_y = height/2;
		float mCircleRadius = calacRadius();
		for(int i = 0;i<mLatticeCounts;i++){
			double angle=((i+moveTimes)%mLatticeCounts)*(360/mLatticeCounts);
			if(angle>=360){
				angle= angle-360;
			}
			drawLattice(canvas,mCircleRadius,center_x,center_y,angle,i==(moveTimes%mLatticeCounts));
		}
		postInvalidateDelayed(80);
		moveTimes++;
	}
	
	
	private void drawLattice(Canvas canvas,float mCircleRadius, int center_x, int center_y, double angle, boolean b) {
		if(b){
			mPaint.setColor(mTargetColor);
			mPaint.setStrokeWidth(5);
		}else{
			mPaint.setColor(mDefColor);
//			mPaint.setStrokeWidth(mLatticeWidth);
		}
		int tmpLenght = mLatticeLength/2;
		float topRadius = tmpLenght+mCircleRadius;
		float bottomRadius = mCircleRadius-tmpLenght;
		float topX = (float) (center_x+topRadius*Math.cos(angle*3.14/180));
		float topY = (float) (center_y+topRadius*Math.sin(angle*3.14/180));
		
		
		float bottomX = (float) (center_x+bottomRadius*Math.cos(angle*3.14/180));
		float bottomY = (float) (center_y+bottomRadius*Math.sin(angle*3.14/180));
		
		canvas.drawLine(topX, topY, bottomX, bottomY, mPaint);
		
	}


	private float calacRadius() {
		int measureWidth = getMeasuredWidth();
		int measuerHeight = getMeasuredHeight();
		
		int width = (measureWidth-getPaddingLeft()-getPaddingRight()-defPading*2);
		int height = (measuerHeight-getPaddingTop()-getPaddingBottom()-defPading*2);
		
		int min = Math.min(width, height);
		return min/2;
	}
	
	@Override
	protected void initWidthAndHeight() {
		mWidth = m_width;
		mHeight = m_height;
	}

}
