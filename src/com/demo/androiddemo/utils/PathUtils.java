package com.demo.androiddemo.utils;

import android.graphics.Path;

public class PathUtils {

	private PathUtils() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 画出给定两个小球之间的Path路径，实现黏连体效果
	 * @param c1x 圆C1的圆心点x坐标
	 * @param c1y 圆C1的圆心点Y坐标
	 * @param radius1 圆C1的圆半径
	 * @param c2x 圆C2的圆心点x坐标
	 * @param c2y 圆C2的圆心点Y坐标
	 * @param radius2 圆C2的圆半径
	 * @param maxLength 两个圆之间的最长距离
	 * @return
	 */
	public static Path makePathBetweenCircles(float c1x,float c1y,float radius1,float c2x,float c2y,float radius2,float maxLength){
		Path path = new Path();
		//圆C1的圆心坐标
		float c1_x = c1x;
		float c1_y = c1y;
		//小球的圆心坐标
		float c_x = c2x;
		float c_y = c2y;
		//根据两圆的圆心，计算夹角
		float disx = c1_x-c_x;
		float disy = c1_y-c_y;
		//圆心距离
		float dis = (float) Math.sqrt(disx*disx+disy*disy);
		if(dis>=maxLength){
			return path;
		}
		float sin = Math.abs(disy)/dis;
		float cos = Math.abs(disx)/dis;
		//两个贝塞尔曲线的控制点，是两个圆心的连线中点
		float control_x =(c1_x+c_x)/2;
		float control_y = (c1_y+c_y)/2;
		float c1_start_x=0;
		float c1_start_y=0;
		float c1_end_x=0;
		float c1_end_y=0;
		float c2_start_x=0;
		float c2_start_y=0;
		float c2_end_x=0;
		float c2_end_y=0;
		if(disx<=0&&disy>=0){//C2在C1的右上方包括右方和上方
		//底部大圆的左右连点，左边点作为Path的起点
		 c1_start_x = c1_x-radius1*sin;
		 c1_start_y = c1_y-radius1*cos;
		 c1_end_x = c1_x+radius1*sin;
		 c1_end_y = c1_y+radius1*cos;
		
		 c2_start_x = c_x-radius2*sin;
		 c2_start_y = c_y-radius2*cos;
		 c2_end_x = c_x+radius2*sin;
		 c2_end_y = c_y+radius2*cos;
		}else if(disx>=0&&disy>=0){//C2在C1的左上方
			c1_start_x = c1_x-radius1*sin;
			 c1_start_y = c1_y+radius1*cos;
			 c1_end_x = c1_x+radius1*sin;
			 c1_end_y = c1_y-radius1*cos;
			
			 c2_start_x = c_x-radius2*sin;
			 c2_start_y = c_y+radius2*cos;
			 c2_end_x = c_x+radius2*sin;
			 c2_end_y = c_y-radius2*cos;
		}else if(disx>=0&&disy<=0){//C2在C1的左下方
			 c1_start_x = c1_x-radius1*sin;
			 c1_start_y = c1_y-radius1*cos;
			 c1_end_x = c1_x+radius1*sin;
			 c1_end_y = c1_y+radius1*cos;
			
			 c2_start_x = c_x-radius2*sin;
			 c2_start_y = c_y-radius2*cos;
			 c2_end_x = c_x+radius2*sin;
			 c2_end_y = c_y+radius2*cos;
		}else if(disx<=0&&disy<=0){//C2在C1的右下方
			c1_start_x = c1_x-radius1*sin;
			 c1_start_y = c1_y+radius1*cos;
			 c1_end_x = c1_x+radius1*sin;
			 c1_end_y = c1_y-radius1*cos;
			
			 c2_start_x = c_x-radius2*sin;
			 c2_start_y = c_y+radius2*cos;
			 c2_end_x = c_x+radius2*sin;
			 c2_end_y = c_y-radius2*cos;
		}
		 
		path.moveTo(c1_start_x, c1_start_y);
		path.quadTo(control_x, control_y, c2_start_x, c2_start_y);
		path.lineTo(c2_end_x, c2_end_y);
		path.quadTo(control_x, control_y, c1_end_x, c1_end_y);
		path.close();
		return path;
	
	}

}
