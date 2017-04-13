package com.demo.androiddemo.customview;

import com.demo.androiddemo.R;
import com.demo.androiddemo.utils.BookFlip;
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
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
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
	private CornerPos mCornerPos;
	private float mDistanceX;
	private float mDistanceY;
	private GradientDrawable mFrontLeftShadow;
	private GradientDrawable mFrontRightShadow;
	private GradientDrawable mBackShadow;
	private  enum State{
		PREPARED,ANIMATORING;
	}
	
	private enum CornerPos{
		/**
		 * 左上角
		 */
		LT,
		/**
		 * 右上角
		 */
		RT,
		/**
		 * 左下角
		 */
		LB,
		/**
		 * 右下角
		 */
		RB;
	}
	private void init(Context context) {
		mDisplay = Utils.getDisplayMetrics(context);
		mState = State.PREPARED;
		initPaint();
		
		initShadows();
		
		initDemoImg();
		
		initAnimators();
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(Color.RED);
	}

	private void initAnimators() {
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

	private void initDemoImg() {
		first = BitmapFactory.decodeResource(getResources(), R.drawable.tmp);
		second = BitmapFactory.decodeResource(getResources(), R.drawable.test2);
		first = ImageUtils.compressImage( first, mDisplay.widthPixels, mDisplay.heightPixels);
		second = ImageUtils.compressImage( second,mDisplay.widthPixels, mDisplay.heightPixels);
	}

	private void initShadows() {
		mFrontLeftShadow = new GradientDrawable();
		mFrontLeftShadow.setColors(colors);
		mFrontLeftShadow.setOrientation(Orientation.TOP_BOTTOM);
		mFrontRightShadow = new GradientDrawable();
		mFrontRightShadow.setColors(colors);
		mFrontRightShadow.setOrientation(Orientation.TOP_BOTTOM);
		mBackShadow = new GradientDrawable();
		mBackShadow.setColors(colors2);
		mBackShadow.setOrientation(Orientation.TOP_BOTTOM);
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
		BookFlip flip = PathUtils.make(touchX, touchY, cornerX, cornerY);
		Path[] paths = flip.paths;
		//画当前页要显示的内容
		canvas.save();
		canvas.clipPath(paths[0], Op.XOR);
		canvas.drawBitmap(first,srcR,dstR, mPaint);
		canvas.restore();
		//画下一页要显示的内容
		canvas.save();
		canvas.clipPath(paths[0]);
		canvas.clipPath(paths[1], Op.DIFFERENCE);
		canvas.drawBitmap(second,srcR,dstR, mPaint);
		canvas.restore();
		//画当前页翻角的背景内容
		canvas.save();
		canvas.clipPath(paths[0]);
		canvas.clipPath(paths[1], Op.INTERSECT);
		canvas.drawColor(Color.WHITE);
		canvas.restore();
		
		drawShadow(canvas,flip);
	}
	private int[] colors = new int[]{0x333333, 0xB0333333};
	private int[] colors2 = new int[]{0xFF111111, 0x10111111};
	private void drawShadow(Canvas canvas, BookFlip flip) {
		if(mCornerPos==CornerPos.RB){
			float x1 = flip.a1_x;
			float y1 = flip.a1_y;
			float x2 = flip.a2_x;
			float y2 = flip.a2_y;
			float angle = (float) Math.toDegrees(Math.atan((Math.abs(y1-y2)/Math.abs(x1-x2))));
			double length = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
			//话下一页画面上的背景阴影
			mPaint.setColor(Color.RED);
			canvas.drawLine(0, 0, x1, y1, mPaint);
			mPaint.setColor(Color.BLUE);
			canvas.drawLine(0, 0, x2, y2, mPaint);
			canvas.save();
			canvas.clipPath(flip.paths[0]);
			canvas.clipPath(flip.paths[1], Op.DIFFERENCE);
//			canvas.drawLine(0, 0, flip.e1_x, flip.e1_y, mPaint);
			canvas.rotate(-angle, x1, y1);
			int left = (int) x1-500;
			int top = (int) y1;
			int right = (int) (x1+length+500);
//			mPaint.setColor(Color.BLUE);
//			canvas.drawLine(left, top, right, top, mPaint);
			int bottom = (int) y1+25;
			Rect bounds = new Rect(left, top, right, bottom);
			mBackShadow.setBounds(bounds);
//			mBackShadow.setColors(colors);
			mBackShadow.draw(canvas);
//			canvas.drawRect(bounds, mPaint);
			canvas.restore();
		}
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
		if(mCornerPos==CornerPos.LT){
			result=height*2-touchY;
		}else if(mCornerPos==CornerPos.RT){
			result = height*2-touchY;
		}else if(mCornerPos==CornerPos.LB){
			result = -width-touchY;
		}else if(mCornerPos==CornerPos.RB){
			result = -width-touchY;
		}
/*		if(cornerX==0&&cornerY==0){
			result=height*2-touchY;
		}else if(cornerX==width&&cornerY==0){
			result = height*2-touchY;
		}else if(cornerX==0&&cornerY==height){
			result = -width-touchY;
		}else if(cornerX==width&&cornerY==height){
			result = -width-touchY;
		}
*/		return result;
	}

	private void initCorner() {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		if(touchX>=width/2&&(touchY>=height/2&&touchY<=height)){
			cornerX = width;
			cornerY = height;
			mCornerPos = CornerPos.RB;
		}else if(touchX>=width/2&&(touchY>=0&&touchY<=height/2)){
			cornerX = width;
			cornerY = 0;
			mCornerPos = CornerPos.RT;
		}else if((touchX<=width/2&&touchX>=0)&&(touchY>=0&&touchY<=height/2)){
			cornerX = 0;
			cornerY = 0;
			mCornerPos = CornerPos.LT;
		}else if((touchX<=width/2&&touchX>=0)&&(touchY>=height/2&&touchY<=height)){
			cornerX = 0;
			cornerY = height;
			mCornerPos = CornerPos.LB;
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
