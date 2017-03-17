package com.demo.androiddemo.customview;

import com.demo.androiddemo.R;

import android.R.color;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;

public class LoadingCircleView extends BaseView {
	
	/**
	 * 六个小球的进度条
	 * 水平方向上，左边有一个最小的小球，顺时针增大小球体积，右边有一个小球，上下各两个小球
	 * @param context
	 */
	
	private float radius_0 = 0.5f;
	private float radius_1 = radius_0+0.5f;
	private float radius_2 = radius_1+0.5f;
	private float radius_3 = radius_2+0.5f;
	private float radius_4 =radius_3+0.5f;
	private float radius_5 = radius_4+0.5f;
	
	private int m_width = 100;
	private int m_height = 100;
	
	private final int def_circleColor = Color.GREEN;//默认进度球颜色
	private final float maxLoadSpeed=100f;//最大旋转速度
	
	private int mCircleColor=def_circleColor;
	private float mLoadSpeed=maxLoadSpeed;

	public LoadingCircleView(Context context) {
		super(context);
		init();
	}

	public LoadingCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingCircleView);
		if(tArray!=null){
			int count = tArray.getIndexCount();
			for (int i = 0; i <count; i++) {
				int attrId = tArray.getIndex(i);
				switch (attrId) {
				case R.styleable.LoadingCircleView_circle_color:
					mCircleColor = tArray.getColor(R.styleable.LoadingCircleView_circle_color, def_circleColor);
					break;
				case R.styleable.LoadingCircleView_load_speed:
					float speed = tArray.getFloat(R.styleable.LoadingCircleView_load_speed, 1);
					if(speed<=0||speed>2){
						speed = 1f;
					}
					mLoadSpeed = maxLoadSpeed/speed;
					break;
				}
			}
		}
		tArray.recycle();
		init();
	}
	private Paint mPaint;
	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLUE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawCircle1(canvas, radius_5);
	}
	
	private int circleCounts = 8;//小圆球个数
	private int moveTimes = 0;//移动次数
	private void drawCircle1(Canvas canvas,float radius){
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		float center_x = width/2;//视图中心点x坐标
		float center_y = height/2;//视图中心点y坐标
		//设置画笔参数
		
		float big_circle_r = getBigCircleRadius(width/2,height/2);
		for(int i = 0;i<circleCounts;i++){
			//每次重绘时，画小圆的逻辑：
			//小圆半径不变
			//小圆的角度逐渐变化
			//angle_= 360/circleCounts = 每一个间隔的角度
			//index = (i+rotateCount)%circleCounts = 每个小圆重绘后的index
			//angle_*index = 每个小圆此时对应的角度
			double angle = ((i+moveTimes)%circleCounts)*(360/circleCounts)+180;
			if(angle>=360){
				angle=angle-360;
			}
			drawCircleSun(canvas, big_circle_r,big_circle_r*(0.15f+i*0.025f) , center_x, center_y, angle,mPaint);
		}
		//每重绘一次，相当于每个小圆移动了一次，移动次数+1
		moveTimes++;
		//每隔100ms重绘一次
		postInvalidateDelayed((long) mLoadSpeed);
	}
	
	
	//获取整个进度条所在大圆形的半径
	private float getBigCircleRadius(int width,int height) {
		int p_left = getPaddingLeft();
		int p_top = getPaddingTop();
		int p_right = getPaddingRight();
		int p_bottom = getPaddingBottom();
		int totalX = (width-p_left-p_right);
		int totalY = (height-p_top-p_bottom);
		return Math.min(totalX, totalY)/2;
	}
	
	private void drawCircleSun(Canvas canvas,float radius,float targetRaidus,float center_x,float center_y,double angle,Paint paint){
		float targetX = (float) (center_x+radius*Math.cos(angle*3.14/180));
		float targetY = (float) (center_y+radius*Math.sin(angle*3.14/180));
		paint.setColor(mCircleColor);
		paint.setStyle(Style.FILL);
		canvas.drawCircle(targetX, targetY, targetRaidus, paint);
//		canvas.drawLine(center_x, center_y, targetX, targetY, paint);
	}
	@Override
	protected void initWidthAndHeight() {
		this.mWidth = m_width;
		this.mHeight = m_height;
	}
}
