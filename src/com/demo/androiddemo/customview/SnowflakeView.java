package com.demo.androiddemo.customview;

import java.util.ArrayList;

import com.demo.androiddemo.utils.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class SnowflakeView extends BaseView {

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
	private final int LINES = 5;//线路个数
	private final int CURVE = 10;//曲线个数
	private DisplayMetrics mDisplay;
	private ArrayList<Path> mPathArray ;
	private Paint mPaint;
	
	private void init(Context context){
		mDisplay = Utils.getDisplayMetrics(context);
		mPathArray = new ArrayList<>();
		mPaint  = new Paint();
		mPaint.setStrokeWidth(2);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Style.STROKE);
		buildPaths();
	}
	private ArrayList<MyPoint> points = new ArrayList<MyPoint>();
	private void buildPaths() {
		float dis = mDisplay.widthPixels/6;
		for(int i = 1;i<6;i++){
			float offeset = dis*i;
			Path path = makePath(offeset, dis);
			mPathArray.add(path);
		}
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		canvas.drawColor(Color.TRANSPARENT);
		canvas.drawColor(Color.WHITE);
		for(int i=0;i<mPathArray.size();i++){
			canvas.drawPath(mPathArray.get(i),mPaint);
		}
		mPaint.setColor(Color.YELLOW);
		for (MyPoint p : points) {
			canvas.drawPoint(p.x, p.y, mPaint);
		}
	}
	private class MyPoint{
		float x;
		float y;
		public MyPoint(float x,float y){
			this.x = x;
			this.y = y;
		}
	}
	@Override
	protected void initWidthAndHeight() {

	}
	
	private Path makePath(float offeset,float segment){
		Path path = new Path();
		float dis = mDisplay.heightPixels/CURVE;
		float start_x = offeset;
		float start_y = 0;
		float c1_x = offeset;
		float c1_y = 0;
		for(int i=0;i<CURVE;i++){
			MyPoint poin = new MyPoint(start_x,start_y);
			points.add(poin);
			if(i==0){
				path.moveTo(start_x, start_y);
				float control_x = getControlPoint(offeset, segment);
				float control_y = i*dis+0.5f*dis;
				
				float end_x = offeset;
				float end_y = i*dis+dis;
				
				path.quadTo(control_x, control_y, end_x, end_y);
				start_x = control_x;
				start_y = control_y;
				c1_x = end_x;
				c1_y = end_y;
			}else if(i>0){
				Path tmpPath = new Path();
//				path.moveTo(start_x, start_y);
				float control_x1 = c1_x;
				float control_y1 = c1_y;
				float random  = Utils.nextFloat(-segment, segment);
				float control_x2 = getControlPoint(offeset, segment);
				float control_y2 = i*dis+0.5f*dis;
//				if(start_x<offeset){
//					control_x2 = offeset-Math.abs(random);
//				}else{
//					control_x2 = offeset+Math.abs(random);
//				}
				float end_x = offeset;
				float end_y = i*dis+dis;
				path.cubicTo(control_x1, control_y1, control_x2, control_y2, end_x, end_y);
				start_x = control_x1;
				start_y = control_y1;
				c1_x = end_x;
				c1_y = end_y;
//				path.addPath(tmpPath);
			}
		}
		return path;
		
	}
	
	private float getControlPoint(float x,float segment){
		float random = Utils.nextFloat(-segment, segment);
		return random+x;
	}

}
