package com.demo.androiddemo.utils;

import android.graphics.Path;

public class BookFlip {
	
	/**
	 * 书籍翻页对象，保存需要用的Path和Point
	 */

	public BookFlip() {
		// TODO Auto-generated constructor stub
	}
	
	public Path[] paths = new Path[2];
	
	public float touchX;
	public float touchY;
	public float cornerX;
	public float cornerY;
	//两个贝塞尔曲线的控制点
	public float c1_x ;
	public float c1_y ;
	
	public float c2_x ;
	public float c2_y ;
	
	//两个贝塞尔曲线的起点
	public float s1_x ;
	public float s1_y ;
	public float s2_x ;
	public float s2_y ;
	//两个贝塞尔曲线的终点
	
	public float e1_x ;
	public float e1_y ;
	
	public float e2_x ;
	public float e2_y ;
	
	//a1是msc1和mce1连线的中点 a2是msc2和mce2连线的中点 
	//msc1这个是s1、c1连线的中点 mce1是c1、e1的连线中点
	//msc2这个是s2、c2连线的中点 mce2是c2、e2的连线中点
	
	public float a1_x ;
	public float a1_y ;
	public float a2_x ;
	public float a2_y ;
	
	

}
