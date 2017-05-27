package com.demo.androiddemo.reader.service;


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
	private int bgColor;
	/**
	 * 翻页效果
	 */
	private int flipStyle;

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

	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	public int getFlipStyle() {
		return flipStyle;
	}

	public void setFlipStyle(int flipStyle) {
		this.flipStyle = flipStyle;
	}
	
}