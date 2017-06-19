package com.demo.androiddemo.customview;

import java.io.File;

import com.demo.androiddemo.R;
import com.demo.androiddemo.reader.service.ReadFile;
import com.demo.androiddemo.reader.service.Reader;
import com.demo.androiddemo.reader.service.ReaderImpl;
import com.demo.androiddemo.utils.BookFlip;
import com.demo.androiddemo.utils.Circle;
import com.demo.androiddemo.utils.ImageUtils;
import com.demo.androiddemo.utils.PathUtils;
import com.demo.androiddemo.utils.Utils;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
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
	private Context mContext;
	private Paint mPaint,mBgPaint;
	private ColorMatrixColorFilter mColorMatrixFilter;
	
	Matrix mMatrix = new Matrix();
	float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };
	
	private float touchX ;
	private float touchY ;
	private float cornerX;
	private float cornerY;
	private Bitmap first,second,firstTmp,secondTmp,back,backTmp;
	private Canvas firstCanvas,secondCanvas,backCanvas;
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
		mContext = context;
		mDisplay = Utils.getDisplayMetrics(context);
		mState = State.PREPARED;
		initPaint();
		
		initShadows();
		
		initDemoImg();
		
		initAnimators();
		
		initColorFilter();
	}

	private void initColorFilter() {
		ColorMatrix cm = new ColorMatrix();
		float array[] = { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0,
				0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0 };
		cm.set(array);
		mColorMatrixFilter = new ColorMatrixColorFilter(cm);
		mBgPaint.setColorFilter(mColorMatrixFilter);
		
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(Color.RED);
		
		mBgPaint = new Paint(mPaint);
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
		ReadFile mCurrentReadFile = new ReadFile();
		mCurrentReadFile.setBookPath(Environment.getExternalStorageDirectory()+File.separator+"test.txt");
		mCurrentReadFile.setLastIndex(1000);
		int den = mDisplay.densityDpi;
		com.demo.androiddemo.reader.service.Style readStyle = new com.demo.androiddemo.reader.service.Style();
		int statusHeight = Utils.getStatusBarHeight(mContext);
		firstTmp =Bitmap.createBitmap(mDisplay.widthPixels, mDisplay.heightPixels-statusHeight, Config.ARGB_8888); 
		secondTmp =Bitmap.createBitmap(mDisplay.widthPixels, mDisplay.heightPixels-statusHeight, Config.ARGB_8888); 
		backTmp =Bitmap.createBitmap(mDisplay.widthPixels, mDisplay.heightPixels-statusHeight, Config.ARGB_8888); 
		firstCanvas = new Canvas(firstTmp);
		secondCanvas = new Canvas(secondTmp);
		backCanvas = new Canvas(backTmp);
		Bitmap bg = Bitmap.createBitmap(mDisplay.widthPixels, mDisplay.heightPixels-statusHeight, Config.ARGB_8888);
		Canvas bgCanvas = new Canvas(bg);
		bgCanvas.drawColor(Color.YELLOW);
		readStyle.setBg(bg);
		readStyle.setTextSize(18);
		readStyle.setRawSpace(0.5f);
		mReader = new ReaderImpl(mDisplay.widthPixels, mDisplay.heightPixels-statusHeight, readStyle);
		/*reader.load(new Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				first = reader.getCurrentPage();
				second = reader.getNextPage();
				postInvalidate();
				return false;
			}
		});*/
		///storage/emulated/0/test.txt
//		mReader.openBook("resources/test.txt", 0);
		String filePath = Environment.getExternalStorageDirectory()+File.separator+"read_tmp.txt";
		mReader.openBook(filePath, 0);
		mReader.draw(firstCanvas);
		/*mReader.pageDown();
		mReader.draw(secondCanvas);*/
//		first = BitmapFactory.decodeResource(getResources(), R.drawable.tmp);
//		second = BitmapFactory.decodeResource(getResources(), R.drawable.test2);
		updateBitmaps();
//		first = ImageUtils.compressImage( firstTmp, mDisplay.widthPixels, mDisplay.heightPixels);
//		second = ImageUtils.compressImage( secondTmp,mDisplay.widthPixels, mDisplay.heightPixels);
	}
	
	private void updateBitmaps(){
		this.first = firstTmp;
		this.second = secondTmp;
	}
	
	private Bitmap makeBgBitmap(Bitmap bitmap){
//		Bitmap b = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
//		Canvas canvas = new Canvas(b);
		 Matrix matrix = new Matrix();
//       matrix.setRotate(90,bm.getWidth()/2,bm.getHeight());
//       matrix.setTranslate(20,20);
       //镜子效果：
       matrix.setScale(-1,1);//翻转
//       matrix.postTranslate(bitmap.getWidth(),0);
//       canvas.drawBitmap(bitmap,matrix,paint);
       return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}
	private void drawBgBitmap(Bitmap bitmap,Canvas canvas,BookFlip flip){
		float mCornerX = flip.cornerX;
		float mCornerY = flip.cornerY;
		float mBezierControl1X = flip.c1_x;
		float mBezierControl1Y = flip.c1_y;
		float mBezierControl2X = flip.c2_x;
		float mBezierControl2Y = flip.c2_y;
		float dis = (float) Math.hypot(mCornerX - mBezierControl1X,
				mBezierControl2Y - mCornerY);
		float f8 = (mCornerX - mBezierControl1X) / dis;
		float f9 = (mBezierControl2Y - mCornerY) / dis;
		mMatrixArray[0] = 1 - 2 * f9 * f9;
		mMatrixArray[1] = 2 * f8 * f9;
		mMatrixArray[3] = mMatrixArray[1];
		mMatrixArray[4] = 1 - 2 * f8 * f8;
		mMatrix.reset();
		mMatrix.setValues(mMatrixArray);
		mMatrix.preTranslate(-mBezierControl1X, -mBezierControl1Y);
		mMatrix.postTranslate(mBezierControl1X, mBezierControl1Y);
		canvas.drawBitmap(bitmap, mMatrix, mBgPaint);
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
		drawBgBitmap(first,canvas,flip);
//		canvas.drawBitmap(bg,srcR,dstR, mBgPaint);
//		canvas.drawColor(Color.WHITE);
		canvas.restore();
		
		drawShadow(canvas,flip);
	}
	private int[] colors = new int[]{0x333333, 0xB0333333};
	private int[] colors2 = new int[]{0xFF111111, 0x111111};
	private int[] colors3 = new int[]{0x80888888, 0x888888};
	private void drawShadow(Canvas canvas, BookFlip flip) {
		int shadowWidth = 50;
		drawBackShadow(canvas, flip, shadowWidth);
		if(true){
			return ;
		}
		if(mCornerPos==CornerPos.RB){
			double angle1 = Math.toDegrees(Math.atan((Math.abs(flip.touchY-flip.s1_y)/Math.abs(flip.touchX-flip.s1_x))));
			double angle2 = Math.toDegrees(Math.atan((Math.abs(flip.touchY-flip.s2_y)/Math.abs(flip.touchX-flip.s2_x))));
			double angle = 180-(180-angle1+angle2)/2-angle1;
			
			double d1 = (float) shadowWidth * 1.414 * Math.cos(angle);
			double d2 = (float) shadowWidth * 1.414 * Math.sin(angle);
			
			
//			float topX = (float) (flip.touchX-shadowWidth*Math.cos(angle));
//			float topY = (float) (flip.touchY-shadowWidth*Math.sin(angle));
			float topX = (float) (flip.touchX-d1);
			float topY = (float) (flip.touchY-d2);
			
			Path path = new Path();
			path.moveTo(topX, topY);
			path.lineTo(flip.touchX, flip.touchY);
			path.lineTo(flip.cornerX, flip.cornerY);
			path.lineTo(topX, flip.cornerY);
//			path.lineTo(flip.a1_x, flip.a1_y);
			path.close();
			canvas.save();
			canvas.clipPath(flip.paths[0],Op.XOR);
			canvas.clipPath(path, Op.INTERSECT);
			float rotate_x1 = flip.s1_x;
			float rotate_y1 = flip.s1_y;
			float rotate_x2 = flip.touchX;
			float rotate_y2 = flip.touchY;
			float rotateAngle = (float) Math.toDegrees(Math.atan((Math.abs(rotate_y1-rotate_y2)/Math.abs(rotate_x1-rotate_x2))));
			canvas.rotate(rotateAngle, rotate_x1, rotate_y1);
			mPaint.setColor(Color.YELLOW);
			int left = 0;
			int top = 0;
			int right = 0;
			int bottom = 0;
			double length = Math.sqrt((rotate_x1-rotate_x2)*(rotate_x1-rotate_x2)+(rotate_y1-rotate_y2)*(rotate_y1-rotate_y2));
			//阴影显示区的范围暂时写的大一些，防止有些空白未显示阴影
			if(mCornerPos==CornerPos.RB){
				 left = (int) (rotate_x1-length-500);
				 top = (int) rotate_y1-100;
				 right = (int) (rotate_x1+500);
				 bottom = (int)rotate_y1+shadowWidth;
			}
			Rect bounds = new Rect(left, top, right, bottom);
			mFrontLeftShadow.setBounds(bounds);
			mFrontLeftShadow.draw(canvas);
			canvas.restore();
		}
	}

	private void drawBackShadow(Canvas canvas, BookFlip flip, int shadowWidth) {
		float x1 = flip.a1_x;
		float y1 = flip.a1_y;
		float x2 = flip.a2_x;
		float y2 = flip.a2_y;
		float angle = (float) Math.toDegrees(Math.atan((Math.abs(y1-y2)/Math.abs(x1-x2))));
		double length = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		//话下一页画面上的背景阴影
		canvas.save();
		//计算下一页画面的显示区域
		canvas.clipPath(flip.paths[0]);
		canvas.clipPath(flip.paths[1], Op.DIFFERENCE);
		//旋转画布
		if(mCornerPos==CornerPos.LB||mCornerPos==CornerPos.RT){
			canvas.rotate(angle,x1,y1);
		}else{
			canvas.rotate(-angle, x1, y1);
		}
		int left = 0;
		int top = 0;
		int right = 0;
		int bottom = 0;
		//阴影显示区的范围暂时写的大一些，防止有些空白未显示阴影
		if(mCornerPos==CornerPos.RB){
			 left = (int) x1-500;
			 top = (int) y1-100;
			 right = (int) (x1+length+500);
			 bottom = (int) y1+shadowWidth;
			 mBackShadow.setOrientation(Orientation.TOP_BOTTOM);
		}else if(mCornerPos==CornerPos.LB){
			 left = (int) (x1-length-500);
			 top = (int) y1-100;
			 right = (int) (x1+500);
			 bottom = (int) y1+shadowWidth;
			 mBackShadow.setOrientation(Orientation.TOP_BOTTOM);
		}else if(mCornerPos==CornerPos.LT){
			 left = (int) (x1-length-500);
			 top = (int) y1-shadowWidth;
			 right = (int) (x1+500);
			 bottom = (int) y1+100;
			 mBackShadow.setOrientation(Orientation.BOTTOM_TOP);
		}else if(mCornerPos == CornerPos.RT){
			 left = (int) x1-500;
			 top = (int) y1-shadowWidth;
			 right = (int) (x1+length+500);
			 bottom = (int) y1+100;
			 mBackShadow.setOrientation(Orientation.BOTTOM_TOP);
		}
		Rect bounds = new Rect(left, top, right, bottom);
		mBackShadow.setBounds(bounds);
		mBackShadow.draw(canvas);
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
			boolean result = initCorner();
			if(!result){
				return super.onTouchEvent(event);
			}
			mReader.draw(firstCanvas);
			if(touchX>mDisplay.widthPixels/2){
				mReader.turnNext();
			}else{
				mReader.turnPre();
			}
			mReader.draw(secondCanvas);
			updateBitmaps();
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
//			mReader.pageDown();
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
		/*	mReader.pageUp();
			mReader.draw(firstCanvas);
			mReader.pageDown();
			mReader.draw(secondCanvas);
			updateBitmaps();*/
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
		return result;
	}
	private int rangeWidth = 300;//手指落点有效区间宽度
	private ReaderImpl mReader;
	private boolean initCorner() {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		if(touchX>=width-rangeWidth&&(touchY>=height-rangeWidth)){
			cornerX = width;
			cornerY = height;
			mCornerPos = CornerPos.RB;
			return true;
		}else if(touchX>=width-rangeWidth&&(touchY<=rangeWidth)){
			cornerX = width;
			cornerY = 0;
			mCornerPos = CornerPos.RT;
			return true;
		}else if((touchX<=rangeWidth)&&(touchY<=rangeWidth)){
			cornerX = 0;
			cornerY = 0;
			mCornerPos = CornerPos.LT;
			return true;
		}else if((touchX<=rangeWidth)&&(touchY>=rangeWidth)){
			cornerX = 0;
			cornerY = height;
			mCornerPos = CornerPos.LB;
			return true;
		}
		return false;
		
	}

	@Override
	protected void initWidthAndHeight() {
		// TODO Auto-generated method stub

	}

}
