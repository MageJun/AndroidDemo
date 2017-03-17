package com.demo.androiddemo.customview;

import android.content.Context;
import android.util.AttributeSet;

public class BrokenGlassView extends BaseView {

	/**
	 * 碎玻璃效果图
	 * @param context
	 */
	public BrokenGlassView(Context context) {
		super(context);
		init();
	}

	public BrokenGlassView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public BrokenGlassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init(){
		
	}

	@Override
	protected void initWidthAndHeight() {
		
	}

}
