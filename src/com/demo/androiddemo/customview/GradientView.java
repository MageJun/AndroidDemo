package com.demo.androiddemo.customview;

import com.demo.androiddemo.utils.Utils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class GradientView extends BaseView {

	public GradientView(Context context) {
		super(context);
		init(context);
	}

	public GradientView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public GradientView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	private Paint mPaint;
	private DisplayMetrics mDisplay;
	private GradientDrawable mGradient;
	private void init(Context context) {
		mDisplay = Utils.getDisplayMetrics(context);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(Color.RED);
		
		mGradient = new GradientDrawable();
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onDraw(Canvas canvas) {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
//		canvas.save();
		Rect r = new Rect(width/2-100, height/2-100,width/2+100, height/2+100);
		canvas.save();
		canvas.clipRect(r);
		Rect innerR = new Rect(r.left+30, r.top+30, r.right-30, r.bottom-30);
		canvas.drawColor(Color.YELLOW);
//		canvas.rotate(45,width/2,height/2);
//		canvas.drawRect(innerR, mPaint);
		
//		float  angle = (float) Math.toDegrees(Math.atan2(Math.abs(r.top-r.bottom),(Math.abs(r.right-r.left))));
		float  angle = (float) Math.toDegrees(Math.atan2((r.top-r.bottom),(r.right-r.left)));
		canvas.rotate(angle,r.left,r.bottom);
//		Rect bounds = new Rect(r.left+50-25, r.left-50, r.right+50, r.left);
		int bottom = (int) Math.sqrt(r.left*r.left+r.bottom*r.bottom);
		Rect bounds = new Rect(0, bottom-25, 50, bottom);
		
//		mGradient.setBounds(r);
////		mGradient.setBounds(width/2-100, height/2-100,width/2+100, height/2+100);
//		mGradient.setColors(new int[]{0x333333, 0xB0333333});
//		mGradient.setBounds(bounds);
//		mGradient.setOrientation(Orientation.BL_TR);
//		mGradient.draw(canvas);
		canvas.drawRect(bounds, mPaint);
		canvas.restore();
		
//		canvas.restore();
	}
	@Override
	protected void initWidthAndHeight() {
		// TODO Auto-generated method stub

	}

}
