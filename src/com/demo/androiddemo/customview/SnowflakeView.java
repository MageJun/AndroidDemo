package com.demo.androiddemo.customview;

import java.util.ArrayList;

import com.demo.androiddemo.utils.Utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

public class SnowflakeView extends BaseView {

	protected static final String TAG = SnowflakeView.class.getSimpleName();

	public SnowflakeView(Context context) {
		super(context);
		init(context);
	}

	public SnowflakeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public SnowflakeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private final  int  SEGMENT = 30;//弯曲幅度 
	private  float mHeartSize = 10f;//心形的半径
	private final int LINES = 5;//线路个数
	private final int CURVE = 5;//曲线个数
	private DisplayMetrics mDisplay;
	private ArrayList<Path> mPathArray ;
	private Paint mPaint;
	private ValueAnimator mValueAnimator;
	private ArrayList<InnerPath> mInnerPathList;
	private ArrayList<Path> mHeartArray;
	
	private final float CONSTANT_FORCIRCLE = 0.552284749831f;
	
	private void init(Context context){
		mDisplay = Utils.getDisplayMetrics(context);
		mPathArray = new ArrayList<>();
		mInnerPathList = new ArrayList<>();
		mHeartArray = new ArrayList<>();
		mPaint  = new Paint();
		mPaint.setStrokeWidth(2);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Style.STROKE);
		buildPaths();
		buildCircles();
//		buildHeartArray();
		mValueAnimator = new ValueAnimator();
		mValueAnimator.setFloatValues(new float[]{0f,1f});
		mValueAnimator.setDuration(15000);
		mValueAnimator.addUpdateListener(mAnimatorLIstener);
	}
	private void buildHeartArray() {
		for (InnerPath p : mInnerPathList) {
			Path path = buildHeartShape(p.x, p.y, mHeartSize);
			mHeartArray.add(path);
		}
	}

	private void buildCircles() {
		for (Path p : mPathArray) {
			InnerPath path =new InnerPath();
			path.path = p;
			mInnerPathList.add(path);
		}
	}
	private void updataCirclesPos(float value){
		PathMeasure pm = new PathMeasure();
		float[] pos = new float[2];
		for (InnerPath inner : mInnerPathList) {
			pm.setPath(inner.path, false);
			float length = pm.getLength();
			float distance = length*value;
			pm.getPosTan(distance, pos, null);
			inner.x=pos[0];
			inner.y=pos[1];
			Log.i(TAG, "updataCirclesPos() inner.x = "+inner.x+",inner.y= "+inner.y);
		}
	}

	private float step = 0;
	private AnimatorUpdateListener mAnimatorLIstener = new AnimatorUpdateListener() {
		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			float value = (float) animation.getAnimatedValue();
			Log.i(TAG, "onAnimationUpdate() value = "+value);
			updataCirclesPos(value);
			invalidate();
		}
	};
	private void buildPaths() {
		float dis = mDisplay.widthPixels/6;
		for(int i = 1;i<6;i++){
			float offeset = dis*i;
			Path path = makePathCubic(offeset, dis*2);
			mPathArray.add(path);
		}
		
	}
	private boolean isFirst = true;
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		/*mPaint.setColor(Color.BLUE);
		for(int i=0;i<mPathArray.size();i++){
			canvas.drawPath(mPathArray.get(i),mPaint);
		}*/
		mPaint.setColor(Color.RED);
		for (InnerPath inner : mInnerPathList) {
			mPaint.setStyle(Style.FILL);
			drawHeartByBezier(canvas, mPaint, inner.x, inner.y, 10);
		}
		if(/*isFirst&&*/!mValueAnimator.isStarted()){
			isFirst = false;
			mValueAnimator.start();
		}
//		drawCircleByBezier(canvas, mPaint, mDisplay.widthPixels/2, mDisplay.heightPixels/2, 100);
		drawHeartByBezier(canvas, mPaint, mDisplay.widthPixels/2, mDisplay.heightPixels/2, 100);
	}
	@Override
	protected void initWidthAndHeight() {

	}
	//C2 = 2P1 - C1
	//通过二阶贝塞尔，创建连续平滑的曲线。如果要实现连续平滑的曲线，需要通过上面的公式来计算贝塞尔曲线的控制点，起点是上一个贝塞尔曲线的终点
	//C2:当前贝塞尔曲线的控制点
	//P1:上一个贝塞尔曲线的终点
	//C1:上一个贝塞尔曲线的控制点
	private Path makePathQuad(float offeset,float segment){
		Path path = new Path();
		float dis = mDisplay.heightPixels/CURVE;
		float start_x = offeset;
		float start_y = 0;
		float preEnd_x = offeset;
		float preEnd_y = 0;
		float preC_x = offeset;
		float preC_y = 0;
		for(int i=0;i<CURVE;i++){
			if(i==0){
				path.moveTo(start_x, start_y);
				float control_x = getControlPoint(offeset, segment);
				float control_y = i*dis+0.5f*dis;
				
				float end_x = offeset;
				float end_y = i*dis+dis;
				
				path.quadTo(control_x, control_y, end_x, end_y);
				preC_x = control_x;
				preC_y = control_y;
				preEnd_x = end_x;
				preEnd_y = end_y;
			}else if(i>0){
				float control_x = 2*preEnd_x-preC_x;
				float control_y = 2*preEnd_y-preC_y;
				
				float end_x = offeset;
				float end_y = i*dis+dis;
				
				path.quadTo(control_x, control_y, end_x, end_y);
				preC_x = control_x;
				preC_y = control_y;
				preEnd_x = end_x;
				preEnd_y = end_y;
			}
		}
		return path;
		
	}
	//Cc1 = 2P1 - Cp2
	//通过三阶贝塞尔，创建连续平滑的曲线。如果要实现连续平滑的曲线，需要通过上面的公式来计算贝塞尔曲线的控制点，起点是上一个贝塞尔曲线的终点
	//Cc1:当前贝塞尔曲线的第一个控制点
	//P1:上一个贝塞尔曲线的终点
	//Cp2:上一个贝塞尔曲线的第二个控制点
	private Path makePathCubic(float offeset,float segment){
		Path path = new Path();
		float dis = mDisplay.heightPixels/CURVE;
		float start_x = offeset/*mDisplay.heightPixels/2*/;
		float start_y = 0;
		float preEnd_x = offeset;
		float preEnd_y = 0;
		float preC2_x = offeset;
		float preC2_y = 0;
		for(int i=0;i<CURVE;i++){
			if(i==0){
				start_y = -dis;
				path.moveTo(start_x, start_y);
				float control1_x = getControlPoint(offeset, segment);
				float control1_y = -0.5f*dis;
				float control2_x = getControlPoint(offeset, segment);
				float control2_y = 0.5f*dis;
				
				float end_x = offeset;
				float end_y = i*dis+dis;
				path.cubicTo(control1_x, control1_y, control2_x, control2_y, end_x, end_y);
				preC2_x = control2_x;
				preC2_y = control2_y;
				preEnd_x = end_x;
				preEnd_y = end_y;
			}else if(i>0){
				float control1_x = 2*preEnd_x-preC2_x;
				float control1_y =  2*preEnd_y-preC2_y;
				float control2_x = getControlPoint(offeset, segment);
				float control2_y = i*dis+0.5f*dis;
				
				float end_x = offeset;
				float end_y = i*dis+dis;
				path.cubicTo(control1_x, control1_y, control2_x, control2_y, end_x, end_y);
				preC2_x = control2_x;
				preC2_y = control2_y;
				preEnd_x = end_x;
				preEnd_y = end_y;
				
			}
		}
		return path;
		
	}
	
	private float getControlPoint(float x,float segment){
		float random = Utils.nextFloat(-segment, segment);
		return random+x;
	}
	
	private class InnerPath {
		int step = 0;
		Path path;
		float x;
		float y;
	}
	
	/**
	 * 使用三阶贝塞尔曲线画一个圆
	 * 可以通过画四个三阶贝塞尔曲线，来实画一个圆
	 * 四个三阶贝塞尔曲线的控制点，从第一象限顺时针开始，依次是：
	 * x0、y0圆心坐标
	 * r半径
	 * t控制点同半径的常量比
	 * c1(x0+t*r,y0-r)
	 * c2(x0+r,y0-t*r)
	 * 
	 * c3(x0+r,y0+t*r)
	 * c4(x0+t*r,y0+r)
	 * 
	 * c5(x0-t*r,y0+r)
	 * c6(x0-r,y0+t*r)
	 * 
	 * c7(x0-r,y0-t*r)
	 * c8(x0-t*r,y0-r)
	 * 
	 */
	private void drawCircleByBezier(Canvas canvas,Paint paint,float x,float y,int radius){
		Path path = new Path();
		float start_x = x;
		float start_y = y-radius;
		path.moveTo(start_x, start_y);
		float c1_x = x+CONSTANT_FORCIRCLE*radius;
		float c1_y = y-radius;
		float c2_x = x+radius;
		float c2_y = y-CONSTANT_FORCIRCLE*radius;
		float e1_x = x+radius;
		float e1_y = y;
		path.cubicTo(c1_x, c1_y, c2_x, c2_y, e1_x, e1_y);
		
		float c3_x = x+radius;
		float c3_y = y+CONSTANT_FORCIRCLE*radius;
		float c4_x = x+CONSTANT_FORCIRCLE*radius;
		float c4_y = y+radius;
		float e2_x = x;
		float e2_y = y+radius;
		path.cubicTo(c3_x, c3_y, c4_x, c4_y, e2_x, e2_y);
		
		float c5_x = x-CONSTANT_FORCIRCLE*radius;
		float c5_y = y+radius;
		float c6_x = x-radius;
		float c6_y = y+CONSTANT_FORCIRCLE*radius;
		float e3_x = x-radius;
		float e3_y = y;
		path.cubicTo(c5_x, c5_y, c6_x, c6_y, e3_x, e3_y);
		
		float c7_x = x-radius;
		float c7_y = y-CONSTANT_FORCIRCLE*radius;
		float c8_x = x-CONSTANT_FORCIRCLE*radius;
		float c8_y = y-radius;
		float e4_x = x;
		float e4_y = y-radius;
		path.cubicTo(c7_x, c7_y, c8_x, c8_y, e4_x, e4_y);
		
		canvas.drawPath(path, paint);
	}
	
	/**
	 * 画一个心形图案，用贝塞尔图片，方式和画圆一样，只需要平移两个点就行
	 * @param canvas
	 * @param paint
	 * @param x
	 * @param y
	 * @param radius
	 */
	private  float tranLen = 50;
	private void drawHeartByBezier(Canvas canvas,Paint paint,float x,float y,float radius){
		Path path = buildHeartShape(x, y, radius);
		canvas.drawPath(path, paint);
	}

	private Path buildHeartShape(float x, float y, float radius) {
		tranLen = radius*3/4;
		Path path = new Path();
		float start_x = x;
		float start_y = y-radius+tranLen;
		path.moveTo(start_x, start_y);
		float c1_x = x+CONSTANT_FORCIRCLE*radius;
		float c1_y = y-radius;
		float c2_x = x+radius;
		float c2_y = y-CONSTANT_FORCIRCLE*radius;
		float e1_x = x+radius;
		float e1_y = y;
		path.cubicTo(c1_x, c1_y, c2_x, c2_y, e1_x, e1_y);
		
		float c3_x = x+radius;
		float c3_y = y+CONSTANT_FORCIRCLE*radius;
		float c4_x = x+CONSTANT_FORCIRCLE*radius;
		float c4_y = y+radius;
		float e2_x = x;
		float e2_y = y+radius+tranLen*2/5;
		path.cubicTo(c3_x, c3_y, c4_x, c4_y, e2_x, e2_y);
		
		float c5_x = x-CONSTANT_FORCIRCLE*radius;
		float c5_y = y+radius;
		float c6_x = x-radius;
		float c6_y = y+CONSTANT_FORCIRCLE*radius;
		float e3_x = x-radius;
		float e3_y = y;
		path.cubicTo(c5_x, c5_y, c6_x, c6_y, e3_x, e3_y);
		
		float c7_x = x-radius;
		float c7_y = y-CONSTANT_FORCIRCLE*radius;
		float c8_x = x-CONSTANT_FORCIRCLE*radius;
		float c8_y = y-radius;
		float e4_x = x;
		float e4_y = y-radius+tranLen;
		path.cubicTo(c7_x, c7_y, c8_x, c8_y, e4_x, e4_y);
		return path;
	}
}
