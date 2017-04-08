package com.demo.androiddemo.customview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.demo.androiddemo.utils.PathUtils;
import com.demo.androiddemo.utils.Utils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.graphics.PorterDuff.Mode;
import android.support.v4.animation.AnimatorUpdateListenerCompat;
import android.support.v4.animation.ValueAnimatorCompat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

public class StickBollView extends BaseView {

	private static final String TAG = StickBollView.class.getSimpleName();
	private static final float MAXDIS = 200;
	public StickBollView(Context context) {
		super(context);
		init(context);
	}

	public StickBollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public StickBollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	private DisplayMetrics mDisplay;
	private Paint mPaint;
	private ValueAnimator mValueAnimator,mValueAnimator2;
	private ArrayList<Boll> mBolls;
	private final float BOTTOM_BOLL_RADIUS = 40;
	private Boll mBottomBoll;
	private void init(Context context){
		mDisplay = Utils.getDisplayMetrics(context);
		mBolls = new ArrayList<Boll>();
		mPaint  = new Paint();
		mPaint.setStrokeWidth(2);
		mPaint.setAntiAlias(true);
		mPaint.setColor(color_2);
		mPaint.setStyle(Style.FILL);
		LinearGradient lg = new LinearGradient(0, 0, BOTTOM_BOLL_RADIUS, BOTTOM_BOLL_RADIUS, new int[]{color_1,color_2,color_3}, null,Shader.TileMode.REPEAT);
		mPaint.setShader(lg);
		mValueAnimator = new ValueAnimator();
		mValueAnimator.setFloatValues(new float[]{0f,1f});
		mValueAnimator.setDuration(1000);
		mValueAnimator.addUpdateListener(mAnimatorListener);
		
	}
	private AnimatorUpdateListener mAnimatorListener = new AnimatorUpdateListener(){
		
		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			int size = mBolls.size();
			float value = (Float) animation.getAnimatedValue();
			float fraction = animation.getAnimatedFraction();
			int num = (int) (value*100);
			Log.i(TAG, "onAnimationUpdate value = "+value+", fraction = "+fraction+",num="+num);
			if(size>0){
				Iterator<Boll> iterator = mBolls.iterator();
				while(iterator.hasNext()){
					Boll boll = iterator.next();
					if(boll.stepCount==100){
						boll.stepCount = 0;
						boll.isAdd=Utils.nextBoolean();
					}
					boll.y-=boll.speed_y;
					float random = Utils.netFloat()*2;
					if(boll.isAdd)
						boll.x+=boll.speed_x;
					else
						boll.x-=boll.speed_x;
					if(boll.x<boll.radius){
						boll.x = boll.radius;
					}else if(boll.x>getMeasuredWidth()-boll.radius){
						boll.x = getMeasuredWidth()-boll.radius;
					}
					boll.stepCount++;
					if(boll.y<-boll.radius){
						iterator.remove();
					}
				}
			}
			if(mBottomBoll.radius>BOTTOM_BOLL_RADIUS/10)
				mBottomBoll.radius-=0.1;
			invalidate();
		}

	};
	
	private float mBollRadius = BOTTOM_BOLL_RADIUS*3/4;//小球的半径
	
	private int color_1 = 0xFFE4EDF4;
	private int color_2 = 0xFF5AA0DB;
	private int color_3 = 0xFFD4E4F1;
	
	private Boll mProductingBoll;
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		drawBottomBoll(canvas);
		drawBolls(canvas);
		Path path = makeStickBoll();
		canvas.drawPath(path, mPaint);
		if(!mValueAnimator.isStarted()){
			Boll boll = makeBoll();
			mBolls.add(boll);
			mBottomBoll.radius = BOTTOM_BOLL_RADIUS;
			mValueAnimator.start();
		}
	}

	private void drawBolls(Canvas canvas) {
		Paint bollPaint = new Paint(mPaint);
		int size = mBolls.size();
		for (int i = 0;i<size;i++) {
			Boll boll = mBolls.get(i);
			if(i<size-1){
				LinearGradient lg = makeLinearShader(boll);
				bollPaint.setShader(lg);
				canvas.drawCircle(boll.x, boll.y, boll.radius, bollPaint);
			}else{
				canvas.drawCircle(boll.x, boll.y, boll.radius, mPaint);
			}
		}
	}

	private LinearGradient makeLinearShader(Boll boll) {
		//45度角线性
		int angle = 45;
		float start_x = (float) (boll.x-boll.radius*Math.cos(angle));
		float start_y = (float) (boll.y-boll.radius*Math.sin(angle));
		float end_x = (float) (boll.x+boll.radius*Math.cos(angle));
		float end_y = (float) (boll.y+boll.radius*Math.sin(angle));
		return new LinearGradient(start_x, start_y, end_x, end_y, new int[]{color_1,color_2,color_3}, null,Shader.TileMode.REPEAT);
	};
	private Boll makeBoll() {
		Boll boll = new Boll();
		boolean isAdd = Utils.nextBoolean();
		boll.isAdd = isAdd;
		float nextX = Utils.nextFloat(0.5f,2f);
		float nextY = Utils.nextFloat(1f,5f);
		boll.speed_x=nextX;
		boll.speed_y = nextY;
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		boll.x = width/2;
		boll.y = height;
		boll.radius = mBollRadius;
		return boll;
	};

	private Path makeStickBoll() {
		Path path = new Path();
		int size = mBolls.size();
		if(size==0){
			return path;
		}
		Boll boll = mBolls.get(size-1);
		//底部大圆的圆心坐标
		float cb_x = mBottomBoll.x;
		float cb_y = mBottomBoll.y;
		//小球的圆心坐标
		float c_x = boll.x;
		float c_y = boll.y;
		path = PathUtils.makePathBetweenCircles(cb_x, cb_y, mBottomBoll.radius, c_x, c_y, boll.radius,MAXDIS);
		return path;
	}

	private void drawBottomBoll(Canvas canvas) {
		if(mBottomBoll==null){
			mBottomBoll = new Boll();
			int width = getMeasuredWidth();
			int height = getMeasuredHeight();
			mBottomBoll.x = width/2;
			mBottomBoll.y=height;
			mBottomBoll.radius = BOTTOM_BOLL_RADIUS;
		}
		float center_x = mBottomBoll.x;
		float center_y = mBottomBoll.y;
		canvas.drawCircle(center_x, center_y, mBottomBoll.radius, mPaint);
	}

	@Override
	protected void initWidthAndHeight() {
		// TODO Auto-generated method stub

	}
	
	private class Boll{
		float x;
		float y;
		float radius;
		boolean isAdd;
		float speed_x;
		float speed_y;
		int stepCount;
	}

}
