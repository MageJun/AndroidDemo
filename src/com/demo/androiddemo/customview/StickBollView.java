package com.demo.androiddemo.customview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		mValueAnimator.setDuration(4000);
		mValueAnimator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				if(mProductingBoll!=null){
					mBolls.add(mProductingBoll);
				}
				
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				if(mBottomBoll.radius>BOTTOM_BOLL_RADIUS/10)
					mBottomBoll.radius-=0.1;
				mProductingBoll.y--;
				invalidate();
			}
			
		});
		mValueAnimator2 = new ValueAnimator();
		mValueAnimator2.setFloatValues(new float[]{0f,1f});
		mValueAnimator2.setDuration(1000);
		mValueAnimator2.addUpdateListener(mAnimatorListener);
		
	}
	private AnimatorUpdateListener mAnimatorListener = new AnimatorUpdateListener(){
		
		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			int size = mBolls.size();
			float value = (float) animation.getAnimatedValue();
			float fraction = animation.getAnimatedFraction();
			int num = (int) (value*100);
			Log.i(TAG, "onAnimationUpdate value = "+value+", fraction = "+fraction+",num="+num);
			if(size>0){
				Iterator<Boll> iterator = mBolls.iterator();
				while(iterator.hasNext()){
					Boll boll = iterator.next();
					boll.y--;
					if(value==1){
						Log.i(TAG, "onAnimationUpdate x++");
						boll.radius++;
						/*boolean result = Utils.nextBoolean();
						if(result){
							boll.x+=1;
						}else{
							boll.x-=1;
						}*/
//						boll.x += Utils.nextFloat(-1f, 1f)*10;
						boll.x+=2;
//						boll.x=Utils.nextFloat(-1,1)*boll.x+boll.x;
					}
					if(boll.x<-boll.radius){
						boll.x = boll.radius;
					}else if(boll.x>getMeasuredWidth()+boll.radius){
						boll.x = getMeasuredHeight()-boll.radius;
					}
					
					if(boll.y<-boll.radius){
						iterator.remove();
					}
				}
			}
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
		drawProductingBoll(canvas);
		drawBolls(canvas);
		Path path = makeStickBoll();
		canvas.drawPath(path, mPaint);
		if(!mValueAnimator2.isStarted()){
			mValueAnimator2.start();
		}
		if(!mValueAnimator.isStarted()){
			mProductingBoll = makeBoll();
			mBottomBoll.radius = BOTTOM_BOLL_RADIUS;
			mValueAnimator.start();
		}
	}

	private void drawProductingBoll(Canvas canvas) {
		if(mProductingBoll!=null){
			LinearGradient lg = makeLinearShader(mProductingBoll);
			mPaint.setShader(lg);
			canvas.drawCircle(mProductingBoll.x, mProductingBoll.y, mProductingBoll.radius, mPaint);
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
		boll.x = mDisplay.widthPixels/2;
		boll.y = mDisplay.heightPixels;
		boll.radius = mBollRadius;
		return boll;
	};

	private Path makeStickBoll() {
		Path path = new Path();
		int size = mBolls.size();
		if(mProductingBoll==null){
			return path;
		}
		Boll boll = mProductingBoll;
		//底部大圆的圆心坐标
		float cb_x = /*mDisplay.widthPixels/2*/mBottomBoll.x;
		float cb_y = /*mDisplay.heightPixels*/mBottomBoll.y;
		//小球的圆心坐标
		float c_x = boll.x;
		float c_y = boll.y;
		//根据两圆的圆心，计算夹角
		float disx = Math.abs(cb_x-c_x);
		float disy = Math.abs(cb_y-c_y);
		//圆心距离
		float dis = (float) Math.sqrt(disx*disx+disy*disy);
		float sin = disy/dis;
		float cos = disx/dis;
		//两个贝塞尔曲线的控制点，是两个圆心的连线中点
		float control_x =(cb_x+c_x)/2;
		float control_y = (cb_y+c_y)/2;
		//底部大圆的左右连点，左边点作为Path的起点
		float cb_left_x = cb_x-mBottomBoll.radius*sin;
		float cb_left_y = cb_y-mBottomBoll.radius*cos;
		float cb_right_x = cb_x+mBottomBoll.radius*sin;
		float cb_right_y = cb_y+mBottomBoll.radius*cos;
		path.moveTo(cb_left_x, cb_left_y);
		//小球的左右连点
		float c_r = boll.radius;
		float c_left_x = c_x-c_r*sin;
		float c_left_y = c_y-c_r*cos;
		float c_right_x = c_x+c_r*sin;
		float c_right_y = c_y+c_r*cos;
		path.quadTo(control_x, control_y, c_left_x, c_left_y);
		path.lineTo(c_right_x, c_right_y);
		path.quadTo(control_x, control_y, cb_right_x, cb_right_y);
		path.close();
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
	}

}
