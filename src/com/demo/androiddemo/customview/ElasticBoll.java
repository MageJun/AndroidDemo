package com.demo.androiddemo.customview;

import com.demo.androiddemo.utils.Circle;
import com.demo.androiddemo.utils.PathUtils;
import com.demo.androiddemo.utils.Circle.ThreeBessel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;

public class ElasticBoll extends BaseView {
	/**
	 * 弹性小球
	 * 
	 * @param context
	 */

	public ElasticBoll(Context context) {
		super(context);
		init(context);
	}

	public ElasticBoll(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public ElasticBoll(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private Paint mPaint;
	private float startX = 100;
	private float startY = 100;
	private float radius = 60;
	private Circle mBoll;

	private void init(Context context) {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(Color.RED);
		mBoll = initBoll();
	}

	private Circle initBoll() {
		return PathUtils.makeCircle(startX, startY, radius);
	}

	private int step = 0;

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		updataBoll();
		Path path = PathUtils.makePathByCircle(mBoll);
		canvas.drawPath(path, mPaint);

	}

	private float segment = radius * 3 / 2;

	private void updataBoll() {
		float seg = segment / 10;
		float RANGE_X = getMeasuredWidth()-100;
		ThreeBessel first = mBoll.getFirstBessel();
		ThreeBessel sec = mBoll.getSedBessel();
		ThreeBessel third = mBoll.getThirdBessel();
		ThreeBessel four = mBoll.getFourBessel();
		float end1_x = first.getEnd_x();
		float end3_x = third.getEnd_x();
		float x = mBoll.x;
		if (first.end_x < RANGE_X) {
			if ((end1_x - x) < segment) {// 右边开始拉伸
				end1_x += seg;
				first.end_x += seg;
				first.contro2_x += seg;
				sec.contro1_x += seg;
			} else if ((x - end3_x) < segment) {// 左边开始拉伸
				mBoll.x += seg;
				first.start_x += seg;
				first.contro1_x += seg;
				first.contro2_x += seg;
				first.end_x += seg;

				sec.contro1_x += seg;
				sec.contro2_x += seg;
				sec.end_x += seg;

				third.contro1_x += seg;
				four.contro2_x += seg;
				four.end_x += seg;
			} else {
				mBoll.x += seg;
				first.start_x += seg;
				first.contro1_x += seg;
				first.contro2_x += seg;
				first.end_x += seg;

				sec.contro1_x += seg;
				sec.contro2_x += seg;
				sec.end_x += seg;

				third.contro1_x += seg;
				third.contro2_x += seg;
				third.end_x += seg;

				four.contro1_x += seg;
				four.contro2_x += seg;
				four.end_x += seg;
			}
		} else if (first.end_x >= RANGE_X) {
			if ((first.end_x - mBoll.x) > mBoll.raidus*2/3) {
				mBoll.x += seg;
				first.start_x += seg;
				first.contro1_x += seg;
				// first.contro2_x+=seg;

				sec.contro2_x += seg;
				sec.end_x += seg;

				third.contro1_x += seg;
				third.contro2_x += seg;
				third.end_x += seg;

				four.contro1_x += seg;
				four.contro2_x += seg;
				four.end_x += seg;
			}else if((mBoll.x - third.end_x) > mBoll.raidus*2/3){
				
				third.contro2_x += seg;
				third.end_x += seg;
				four.contro1_x += seg;
			}else{
				mBoll = PathUtils.makeCircle(first.end_x-radius, mBoll.y,radius);
				return ;
			}
		}
		invalidate();
		// }
	}

	@Override
	protected void initWidthAndHeight() {
		// TODO Auto-generated method stub

	}

}
