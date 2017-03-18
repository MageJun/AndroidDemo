package com.demo.androiddemo.utils;

import android.graphics.Bitmap;

public class PieceImage {
	
	private Bitmap bitmap;
	private int x;
	private int y;
	private Point endPonit;

	public PieceImage() {
		// TODO Auto-generated constructor stub
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Point getEndPonit() {
		return endPonit;
	}

	public void setEndPonit(Point endPonit) {
		this.endPonit = endPonit;
	}
	
	
}
