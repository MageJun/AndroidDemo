package com.demo.androiddemo.customview;

import android.content.Context;
import android.util.AttributeSet;

public class MatrixDemoView extends BaseView {
	
	/**
	 * Matrix
	 * Android中Matrix是个3*3的矩阵，矩阵的内容：
	 * cosθ,-sinθ,translateX，
	 * sinθ,cosθ,translateY,
	 * 0,0,scale
	 * 九个值分别代表的含义：
	 * translateX：X方向平移数
	 * translateY：Y方向平移数
	 * scale：表示缩放，1表示不缩放，2表示缩放1/2倍
	 * cosθ,-sinθ
	 * sinθ,cosθ:四个表示旋转θ角度，旋转方向按照顺时针方向旋转
	 * 
	 * 
	 * @param context
	 */
	

	public MatrixDemoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	

	public MatrixDemoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}


	public MatrixDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initWidthAndHeight() {
		// TODO Auto-generated method stub

	}

}
