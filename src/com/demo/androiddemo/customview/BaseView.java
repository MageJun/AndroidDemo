package com.demo.androiddemo.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;

public abstract class BaseView extends View {
	
	protected int mWidth = 100;
	protected int mHeight = 100;

	public BaseView(Context context) {
		super(context);
		initview();
	}

	public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initview();
	}

	public BaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initview();
	}
	
	private void initview(){
		
	}
	
	protected abstract void initWidthAndHeight();
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		initWidthAndHeight();
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		LayoutParams lp = getLayoutParams();
		
		if(lp.width==LayoutParams.WRAP_CONTENT){
			if(widthMode==MeasureSpec.AT_MOST||widthMode==MeasureSpec.EXACTLY){
				widthSize = mWidth;
			}
		}
		
		if(lp.height==LayoutParams.WRAP_CONTENT){
			if(heightMode==MeasureSpec.AT_MOST||widthMode==MeasureSpec.EXACTLY){
				heightSize = mHeight;
			}
		}
		setMeasuredDimension(widthSize, heightSize);
	}

}
