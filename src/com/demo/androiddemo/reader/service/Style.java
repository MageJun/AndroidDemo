package com.demo.androiddemo.reader.service;

import android.graphics.Bitmap;

/**
 * @author lwang
 * @version 1.0
 * @created 11-5月-2017 11:03:09
 */
public class Style {

	/**
	 * 文字大小
	 */
	private int textSize;
	/**
	 * 背景颜色
	 */
	private Bitmap bg;
	/**
	 * 翻页效果
	 */
	private int flipStyle;
	
	/**
	 * 行间距
	 */
	private float rawSpace;

	public Style(){

	}

	public void finalize() throws Throwable {

	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public Bitmap getBg() {
		return bg;
	}

	public void setBg(Bitmap bg) {
		this.bg = bg;
	}

	public int getFlipStyle() {
		return flipStyle;
	}

	public void setFlipStyle(int flipStyle) {
		this.flipStyle = flipStyle;
	}

	public float getRawSpace() {
		return rawSpace;
	}

	public void setRawSpace(float rawSpace) {
		this.rawSpace = rawSpace;
		if(rawSpace<0){
			this.rawSpace = 0;
		}
	}
	
	
}