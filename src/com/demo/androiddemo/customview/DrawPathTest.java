package com.demo.androiddemo.customview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.os.Build;
import android.graphics.RectF;
import android.graphics.SumPathEffect;
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
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(2);
		mPaint.setAntiAlias(true);
		//初始化路径对象
		mPath = new Path();
		
	}
	private float mPhase = 1;
	@Override
	protected void onDraw(Canvas canvas) {
		mPath.reset();
		int height = getMeasuredHeight();
		int width = getMeasuredWidth();
		//设置Path的起点位置，如果不设置，默认是视图的（0,0）坐标位置
		mPath.moveTo(0, height/2);
		//设置第二个点的坐标，绘图时会从Path最后一个点的坐标开始，连接到这个坐标画直线
		mPath.lineTo(30, height);
		//设置第三个点的坐标，绘图时会从Path最后一个点的坐标开始，连接到这个坐标画直线
		mPath.lineTo(60, 10);
		//设置第四个点的坐标，这个点的坐标的偏移量，是基于最后一个点的坐标开始的
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
//		mPath.close();
		//计算Path的长度
		PathMeasure pm = new PathMeasure(mPath, false);
		float pathLength = pm.getLength();
		//使用DashPathEffect
		//设置虚线边框效果 。 参数：第一个 float[]数组，指定实线和虚线的长度例如{4,2} 指的是画一个4dp的实现，在画一个2dp的空白先，依此循环
		//第二个 pharse 相对于起点位置的偏移量,可以通过改变这个值，来实现动态效果
		//加入第一个实线的长度为50，如果偏移量是10，那么画出来的第一个实现的长度只有40
		//画出来的第一条实线的长度=第一条实线长度-偏移量
		//注：使用这个效果，Paint的必须是Stroke或者StrokeAndFIll类型，如果是FILL类型，没有效果
		//下面这种方式是一种特例，可以实现动态牵引效果
		DashPathEffect dpe = new DashPathEffect(new float[]{pathLength,pathLength}, mPhase*pathLength);
		mPhase-=0.01;
		if(mPhase<0){
			mPhase = 1;
		}
		//使用CornerPathEffect
		//使线和线之间变得平滑，实现累世圆弧和切线的效果，参数就是圆弧的弧度
//		mPaint.setPathEffect(new CornerPathEffect(10));
		//使用DiscretePathEffect 离散Path效果
		//实现的效果，是使Path路径分段打散
		//参数  第一个就是将path路径分段打散时，每一个线段的长度  
		//第二个参数是线段的偏移量
//		mPaint.setPathEffect(new DiscretePathEffect(pathLength/5, 25));
		//使用PathDashPathEffect
		//和DashPathEffect类似，只是不是画实现，而是用指定的Path替换实线
		Path shape = new Path();
//		shape.addCircle(0, 0, 3, Direction.CW);
		shape.addRect(0, 0, 3, 4, Direction.CW);
		PathDashPathEffect ppe =new PathDashPathEffect(shape, 10, 0, PathDashPathEffect.Style.MORPH);
//		SumPathEffect spe = new SumPathEffect(ppe,dpe);
		ComposePathEffect cpe = new ComposePathEffect(ppe, dpe);
		mPaint.setPathEffect(cpe);
//		mPath.setFillType(FillType.EVEN_ODD);
		canvas.drawPath(mPath, mPaint);
		//画一个中间线
		canvas.drawLine(0, height/2, getMeasuredWidth(), height/2, mPaint);
		invalidate();
	}

	@Override
	protected void initWidthAndHeight() {
		mHeight = height;
	}
}
