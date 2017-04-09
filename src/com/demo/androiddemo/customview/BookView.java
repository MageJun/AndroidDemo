package com.demo.androiddemo.customview;

import com.demo.androiddemo.R;
import com.demo.androiddemo.utils.Circle;
import com.demo.androiddemo.utils.ImageUtils;
import com.demo.androiddemo.utils.PathUtils;
import com.demo.androiddemo.utils.Utils;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.support.v4.animation.AnimatorUpdateListenerCompat;
import android.support.v4.animation.ValueAnimatorCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class BookView extends BaseView {

	public BookView(Context context) {
		super(context);
		init(context);
	}

	public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public BookView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private Paint mPaint;
	private float touchX ;
	private float touchY ;
	private float cornerX;
	private float cornerY;
	private Bitmap first,second;
	private DisplayMetrics mDisplay;//获取屏幕宽高，用来将视图默认设置为全屏
	private ValueAnimator mAnimator1;
	private ValueAnimator mAnimator2;
	private State mState;
	private float mDistanceX;
	private float mDistanceY;
	private  enum State{
		PREPARED,ANIMATORING;
	}
	private void init(Context context) {
		mDisplay = Utils.getDisplayMetrics(context);
		mState = State.PREPARED;
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(Color.RED);
		
		first = BitmapFactory.decodeResource(getResources(), R.drawable.tmp);
		second = BitmapFactory.decodeResource(getResources(), R.drawable.test2);
		first = ImageUtils.compressImage( first, mDisplay.widthPixels, mDisplay.heightPixels);
		second = ImageUtils.compressImage( second,mDisplay.widthPixels, mDisplay.heightPixels);
		
		mAnimator1 = new ValueAnimator();
		mAnimator1.setFloatValues(new float[]{0f,1f});
		mAnimator1.setDuration(100);
		mAnimator1.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator arg0) {
				float fraction = arg0.getAnimatedFraction();
				touchX = touchX+mDistanceX*fraction;
				touchY = touchY+mDistanceY*fraction;
				if(fraction==1){
					Bitmap tmp = null;
					tmp = first;
					first = second;
					second = tmp;
					mState = State.PREPARED;
					reset();
				}
				invalidate();
			}
		});
		mAnimator2 = new ValueAnimator();
		mAnimator2.setFloatValues(new float[]{0f,1f});
		mAnimator2.setDuration(100);
		mAnimator2.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator arg0) {
				float fraction = arg0.getAnimatedFraction();
				touchX = touchX+mDistanceX*fraction;
				touchY = touchY+mDistanceY*fraction;
				if(fraction==1){
					mState = State.PREPARED;
					reset();
				}
				invalidate();
			}
		});
	}
	
	protected void reset() {
		touchX = 0;
		touchY = 0;
		cornerX = 0;
		cornerY = 0;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//图片要绘制的范围
		Rect srcR = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
		//图片要绘制到屏幕上得范围
		Rect dstR = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
		Path[] paths = PathUtils.make(touchX, touchY, cornerX, cornerY);
		//画当前页要显示的内容
		canvas.save();
		canvas.clipPath(paths[0], Op.XOR);
//		canvas.drawColor(Color.YELLOW);
		canvas.drawBitmap(first,srcR,dstR, mPaint);
		canvas.restore();
//		canvas.drawPath(paths[0], mPaint);
//		canvas.drawPath(paths[1], mPaint);
		//画下一页要显示的内容
		canvas.save();
		canvas.clipPath(paths[0]);
		canvas.clipPath(paths[1], Op.DIFFERENCE);
//		canvas.drawColor(Color.GRAY);
		canvas.drawBitmap(second,srcR,dstR, mPaint);
		canvas.restore();
		//画当前页翻角的背景内容
		canvas.save();
		canvas.clipPath(paths[0]);
		canvas.clipPath(paths[1], Op.INTERSECT);
		canvas.drawColor(Color.WHITE);
		canvas.restore();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mState==State.ANIMATORING){
			return super.onTouchEvent(event);
		}
		touchX = event.getRawX();
		touchY = event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			initCorner();
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
			startAnimator();
			break;
		}
		invalidate();
		return true;
	}
	
	private void startAnimator() {
		mState = State.ANIMATORING;
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		float mid_width = width/2;
		float  mid_height = height/2;
		if(Math.abs((touchX-cornerX))>=mid_width||Math.abs((touchY-cornerY))>=mid_height){
			mDistanceX =calcDistanceX(true);
			mDistanceY = calcDistanceY(true);
			mAnimator1.start();
		}else{
			mDistanceX = calcDistanceX(false);
			mDistanceY = calcDistanceY(false);
			mAnimator2.start();
		}
	}
	private float calcDistanceX(boolean isAdd){
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		float result = 0;
		if(!isAdd){
			return cornerX-touchX;
		}
		if(cornerX==0&&cornerY==0){
			result=width*2-touchX;
		}else if(cornerX==width&&cornerY==0){
			result = -width-touchX;
		}else if(cornerX==0&&cornerY==height){
			result = width*2-touchX;
		}else if(cornerX==width&&cornerY==height){
			result = -width-touchX;
		}
		return result;
	}
	private float calcDistanceY(boolean isAdd){
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		float result = 0;
		if(!isAdd){
			return cornerY-touchY;
		}
		if(cornerX==0&&cornerY==0){
			result=height*2-touchY;
		}else if(cornerX==width&&cornerY==0){
			result = height*2-touchY;
		}else if(cornerX==0&&cornerY==height){
			result = -width-touchY;
		}else if(cornerX==width&&cornerY==height){
			result = -width-touchY;
		}
		return result;
	}

	private void initCorner() {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		if(touchX>=width/2&&(touchY>=height/2&&touchY<=height)){
			cornerX = width;
			cornerY = height;
		}else if(touchX>=width/2&&(touchY>=0&&touchY<=height/2)){
			cornerX = width;
			cornerY = 0;
		}else if((touchX<=width/2&&touchX>=0)&&(touchY>=0&&touchY<=height/2)){
			cornerX = 0;
			cornerY = 0;
		}else if((touchX<=width/2&&touchX>=0)&&(touchY>=height/2&&touchY<=height)){
			cornerX = 0;
			cornerY = height;
		}else{
			cornerX = width;
			cornerY = height;
		}
		
	}

	@Override
	protected void initWidthAndHeight() {
		// TODO Auto-generated method stub

	}

}
