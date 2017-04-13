package com.demo.androiddemo.customview;

import com.demo.androiddemo.utils.Utils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class GradientView extends BaseView {

	public GradientView(Context context) {
		super(context);
		init(context);
	}

	public GradientView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public GradientView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	private Paint mPaint;
	private DisplayMetrics mDisplay;
	private GradientDrawable mGradient;
	private void init(Context context) {
		mDisplay = Utils.getDisplayMetrics(context);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);
		mPaint.setStrokeWidth(2);
		mPaint.setColor(Color.RED);
		
		mGradient = new GradientDrawable();
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onDraw(Canvas canvas) {
//		canvas.drawColor(Color.GREEN);
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		Rect r = new Rect(width/2-100, height/2-100,width/2+100, height/2+100);
		canvas.save();
		canvas.clipRect(r);
		canvas.drawColor(Color.GREEN);
		float angle = (float) ((Math.toDegrees(Math.atan2(Math.abs(r.right-r.left), Math.abs((r.top-r.bottom))))));
//		float angle = 10;
		//test
		float x = r.left;
		float  y = r.bottom;
		canvas.rotate(-angle, x, y);
//		int length = (int) Math.sqrt((r.left*r.left+r.bottom*r.bottom));
//		float angle2 = (float) (angle+Math.toDegrees(Math.atan2(r.bottom, r.right)));
		double randians = Math.toRadians(-angle);
		//sin和cos传入的参数是弧度值，不是角度值
		//canvas 绕着指定的点进行旋转，这个点相对于坐标原点的坐标不变（坐标系旋转前和旋转后，这个点的坐标位置不变）
		int newX = (int) (Math.cos(randians)*x-Math.sin(randians)*y);
		int newY = (int) (Math.cos(randians)*y+Math.sin(randians)*x);
		int len = (int) Math.sqrt((newX-x)*(newX-x)+(newY-y)*(newY-y));
		int l = newX+20;
		int t = newY-25;
		int rl = l+50;
		int b = newY;
		Rect bounds = new Rect(l, t, rl, b);
//		canvas.drawRect(bounds, mPaint);
		canvas.drawCircle(newX+10, newY, 100, mPaint);
//		canvas.drawLine(0, 0, newX+10, newY, mPaint);
//		canvas.drawCircle(0, 0, 100, mPaint);
		canvas.restore();
//		mPaint.setColor(Color.BLUE);
		canvas.drawLine(0, 0, newX, newY, mPaint);
		/*Path path1 = new Path();
		path1.moveTo(width/2-100, height/2-100);
		path1.lineTo(width/2+100, height/2-100);
		path1.lineTo(width/2+100, height/2+100);
		path1.lineTo(width/2-100, height/2+100);
		path1.close();
		Path path2 = new Path();
		path2.moveTo(width/2-100, height/2+100);
		path2.lineTo(width/2+100, height/2+100);
		path2.lineTo(width/2+100, height/2-100);
		path2.close();
		canvas.save();
		canvas.clipRect(r);
		canvas.rotate(45,0,height);
		mGradient.setBounds(r);
		mGradient.setColors(new int[]{0x333333, 0xB0333333});
		mGradient.setOrientation(Orientation.BL_TR);
		mGradient.draw(canvas);
		
		canvas.clipPath(path1);
		canvas.drawColor(Color.RED);
		canvas.clipPath(path2, Op.DIFFERENCE);
//		canvas.drawColor(Color.GREEN);
		float sx = width/2-100;
		float sy = height/2+100;
		canvas.rotate(45, sx, sy);
		float dis = (float) Math.sqrt((sx*sx+sy*sy));
		float angle = 90-(float) Math.toDegrees(Math.atan(sx/sy));
//		float angle2 = angle+45;
		int sx_new = (int) (dis*Math.sin(angle));
		int sy_new = (int) (dis*Math.cos(angle));
		
		mGradient.setBounds(sx_new, sy_new-25, sx_new+200, sy_new);
//		mGradient.setColors(new int[]{0x333333, 0xB0333333});
		mGradient.setColor(Color.GREEN);
		mGradient.setOrientation(Orientation.BOTTOM_TOP);
		mGradient.draw(canvas);
		canvas.restore();*/
	}
	@Override
	protected void initWidthAndHeight() {
		// TODO Auto-generated method stub

	}

}
