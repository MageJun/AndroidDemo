package com.demo.androiddemo.customview;

import java.util.ArrayList;

import com.demo.androiddemo.utils.PathUtils;
import com.demo.androiddemo.utils.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class StickView extends BaseView {
	/**
	 * 黏连体Demo视图
	 * @param context
	 */

	public StickView(Context context) {
		super(context);
		init(context);
	}

	public StickView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public StickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	private DisplayMetrics mDisplay;//获取屏幕宽高，用来将视图默认设置为全屏
	private Paint mPaint;
	private Boll mBoll;//屏幕中默认存在的小球
	private float mBollRadius = 50;//屏幕中默认存在的小球的半径
	private Boll mTouchBoll;//触屏生成的小球
	private float mTouchBollRadius = mBollRadius/2;//触屏生成的小球的半径
	private float mMaxLength = 300;//两个小球圆心距离的临界值
	private void init(Context context){
		mDisplay = Utils.getDisplayMetrics(context);
		mPaint  = new Paint();
		mPaint.setStrokeWidth(2);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.GREEN);
		mPaint.setStyle(Style.FILL);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//将视图背景设置为透明
		canvas.drawColor(Color.TRANSPARENT);
		//画默认小球
		drawBoll(canvas);
		drawTouchBoll(canvas);
		drawPath(canvas);
	}
	
	private void drawPath(Canvas canvas) {
		if(mBoll!=null&&mTouchBoll!=null){
			Path path = PathUtils.makePathBetweenCircles(mBoll.x, mBoll.y, mBoll.radius, mTouchBoll.x, mTouchBoll.y, mTouchBoll.radius, mMaxLength);
			canvas.drawPath(path, mPaint);
		}
	}

	private void drawTouchBoll(Canvas canvas) {
		if(mTouchBoll==null){
			return ;
		}
		canvas.drawCircle(mTouchBoll.x, mTouchBoll.y, mTouchBoll.radius, mPaint);
	}
	private boolean isFirst = true;//标志第一次加载视图
	private void drawBoll(Canvas canvas) {
		if(isFirst){
			//第一次加载小球时，创建默认小球，赋值
			isFirst = false;
			mBoll = new Boll();
			int width = getMeasuredWidth();
			int height = getMeasuredHeight();
			mBoll.radius = mBollRadius;
			mBoll.x = width/2;
			mBoll.y = height/2;
		}
		if(mBoll!=null)
			canvas.drawCircle(mBoll.x, mBoll.y, mBoll.radius, mPaint);
	}

	private class Boll{
		float x;
		float y;
		float radius;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//如果默认小球和生成的小球都已经消失，说明当前是触屏之后，拉伸长度已超过临界值，并且此时的状态是UP状态。
		//此时不进行处理，回复默认值
		if(mBoll==null&&mTouchBoll==null){
			return super.onTouchEvent(event);
		}
		float touchX = event.getX();
		float touchY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//判断第一次触屏时，触电是否在默认小球内，如果不在，返回默认值，不进行拦截处理
			if(!verificationRange(touchX,touchY)){
				return super.onTouchEvent(event);
			}
			//生成小球
			if(mTouchBoll==null){
				mTouchBoll = new Boll();
				mTouchBoll.x = touchX;
				mTouchBoll.y = touchY;
				mTouchBoll.radius = mTouchBollRadius;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			mTouchBoll.x = touchX;
			mTouchBoll.y = touchY;
			//判断两个小球间的距离
			verificationDistance();
			if(mBoll!=null)
				updataBollRadius(touchX,touchY,mBoll.x,mBoll.y);
			break;
		case MotionEvent.ACTION_UP:
			//恢复初始值（如果默认小球存在的话）
			reset();
			break;
		}
		invalidate();
		return true;
	}
	/**
	 * 重置
	 * 将生成的小球消除
	 * 如果默认小球没有被消除，恢复到初始状态
	 */
	private void reset() {
		mTouchBoll = null;
		if(mBoll!=null){
			mBoll.radius = mBollRadius;
		}
	}
	/**
	 * 根据两个小球的距离，更新两个小球的半径
	 */
	private void updataBollRadius(float srcX,float srcY,float dstX,float dstY) {
		if(mBoll!=null&&mTouchBoll!=null){
			float len = Utils.calcStraightLen(srcX, srcY, dstX, dstY);
			float ratio = len/mMaxLength;
			mBoll.radius=mBollRadius-mBollRadius*ratio;
			mTouchBoll.radius=mTouchBollRadius+mTouchBollRadius*ratio;
			if(mBoll.radius<=0){
				mBoll = null;
			}
		}
	}
	/**
	 * 判断触点范围，是否在默认小球范围之内
	 */
	private boolean verificationRange(float touchX,float touchY){
		if(mBoll!=null){
			float len = Utils.calcStraightLen( mBoll.x, mBoll.y, touchX, touchY);
			if(len>=mBoll.radius){
				return false;
			}
			return true;
		}
		return false;
	}
	/**
	 * 判断两小球间的距离，如果距离大于临界值，将默认小球消除
	 */
	private void verificationDistance(){
		if(mTouchBoll!=null&&mBoll!=null){
			float len = Utils.calcStraightLen( mBoll.x, mBoll.y, mTouchBoll.x, mTouchBoll.y);
			if(len>=mMaxLength){
				mBoll = null;
			}
		}
	}
	@Override
	protected void initWidthAndHeight() {
		int width = mDisplay.widthPixels;
		int height = mDisplay.heightPixels;
		mWidth = width;
		mHeight = height;
	}

}
