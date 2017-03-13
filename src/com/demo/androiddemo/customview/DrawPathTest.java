package com.demo.androiddemo.customview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.os.Build;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;

public class DrawPathTest extends BaseView {

	private Paint mPaint;
	private Path mPath;
	
	private int height = 200;

	public DrawPathTest(Context context) {
		super(context);
		init();
	}

	public DrawPathTest(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		//初始化画笔对象
		mPaint = new Paint();
		mPaint.setStyle(Style.FILL);
		mPaint.setStrokeWidth(2);
		mPaint.setAntiAlias(true);
		//初始化路径对象
		mPath = new Path();
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int height = getMeasuredHeight();
		int width = getMeasuredWidth();
		//画一个中间线
		canvas.drawLine(0, height/2, getMeasuredWidth(), height/2, mPaint);
		//设置Path的起点位置，如果不设置，默认是视图的（0,0）坐标位置
		mPath.moveTo(0, height/2);
		//设置第二个点的坐标，绘图时会从Path最后一个点的坐标开始，连接到这个坐标画直线
		mPath.lineTo(30, height);
		//设置第三个点的坐标，绘图时会从Path最后一个点的坐标开始，连接到这个坐标画直线
		mPath.lineTo(60, 10);
		//设置第四个点的坐标，这个点的坐标的偏移量，是基于最有一个点的坐标开始的
		//方法中，凡是以r开头的，就是说偏移量是相对于上一个点来说的
		mPath.rLineTo(120, height/2);
		//画一个圆，是相对于(0,0)的地点位置画一个圆,CW顺时针方向 ，CCW逆时针方向,使用渐变颜色时有用
//		mPath.addCircle(width/2, height/2, 20, Direction.CCW);
		//添加一个圆弧，oval 椭圆   startAngle 开始角度   扇形的弧度
//		RectF oval = new RectF(width/2-10, height/2-20, width/2+20, height/2+10);
//		mPath.addArc(oval, 0, 60);
		//添加一个椭圆
//		mPath.addOval(oval, Direction.CCW);
		
		//连接到一个圆弧 5.0系统之后才有这个方法,如果最后一个参数设置为true，标识添加这个圆弧时，现将Path
		//moveTo到这个圆弧的起点位置，反之，从Path的最后一个位置点lineTo这个圆弧的起点位置
//		mPath.arcTo(width-50, height/2, width-10, height/2+40, 0, 359, false);
		//Path的最后一个点和起点相连，组成一个封闭区域
		//注：如果最后一个点和起点相连，不能组成一个封闭区域，纳闷这个close就不生效
		mPath.close();
		mPath.setFillType(FillType.EVEN_ODD);
		canvas.drawPath(mPath, mPaint);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		LayoutParams lp = getLayoutParams();
		
		if(lp.height == LayoutParams.WRAP_CONTENT){
			if(MeasureSpec.AT_MOST==heightMode||MeasureSpec.EXACTLY==heightMode){
				heightSize = height;
			}
		}
		
		setMeasuredDimension(widthSize, heightSize);
	}

}
