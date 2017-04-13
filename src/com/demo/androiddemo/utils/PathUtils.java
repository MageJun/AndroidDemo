package com.demo.androiddemo.utils;

import com.demo.androiddemo.utils.Circle.ThreeBessel;

import android.graphics.Path;

public class PathUtils {

	private PathUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 画出给定两个小球之间的Path路径，实现黏连体效果
	 * 
	 * @param c1x
	 *            圆C1的圆心点x坐标
	 * @param c1y
	 *            圆C1的圆心点Y坐标
	 * @param radius1
	 *            圆C1的圆半径
	 * @param c2x
	 *            圆C2的圆心点x坐标
	 * @param c2y
	 *            圆C2的圆心点Y坐标
	 * @param radius2
	 *            圆C2的圆半径
	 * @param maxLength
	 *            两个圆之间的最长距离
	 * @return
	 */
	public static Path makePathBetweenCircles(float c1x, float c1y, float radius1, float c2x, float c2y, float radius2,
			float maxLength) {
		Path path = new Path();
		// 圆C1的圆心坐标
		float c1_x = c1x;
		float c1_y = c1y;
		// 小球的圆心坐标
		float c_x = c2x;
		float c_y = c2y;
		// 根据两圆的圆心，计算夹角
		float disx = c1_x - c_x;
		float disy = c1_y - c_y;
		// 圆心距离
		float dis = (float) Math.sqrt(disx * disx + disy * disy);
		if (dis >= maxLength) {
			return path;
		}
		float sin = Math.abs(disy) / dis;
		float cos = Math.abs(disx) / dis;
		// 两个贝塞尔曲线的控制点，是两个圆心的连线中点
		float control_x = (c1_x + c_x) / 2;
		float control_y = (c1_y + c_y) / 2;
		float c1_start_x = 0;
		float c1_start_y = 0;
		float c1_end_x = 0;
		float c1_end_y = 0;
		float c2_start_x = 0;
		float c2_start_y = 0;
		float c2_end_x = 0;
		float c2_end_y = 0;
		if (disx <= 0 && disy >= 0) {// C2在C1的右上方包括右方和上方
			// 底部大圆的左右连点，左边点作为Path的起点
			c1_start_x = c1_x - radius1 * sin;
			c1_start_y = c1_y - radius1 * cos;
			c1_end_x = c1_x + radius1 * sin;
			c1_end_y = c1_y + radius1 * cos;

			c2_start_x = c_x - radius2 * sin;
			c2_start_y = c_y - radius2 * cos;
			c2_end_x = c_x + radius2 * sin;
			c2_end_y = c_y + radius2 * cos;
		} else if (disx >= 0 && disy >= 0) {// C2在C1的左上方
			c1_start_x = c1_x - radius1 * sin;
			c1_start_y = c1_y + radius1 * cos;
			c1_end_x = c1_x + radius1 * sin;
			c1_end_y = c1_y - radius1 * cos;

			c2_start_x = c_x - radius2 * sin;
			c2_start_y = c_y + radius2 * cos;
			c2_end_x = c_x + radius2 * sin;
			c2_end_y = c_y - radius2 * cos;
		} else if (disx >= 0 && disy <= 0) {// C2在C1的左下方
			c1_start_x = c1_x - radius1 * sin;
			c1_start_y = c1_y - radius1 * cos;
			c1_end_x = c1_x + radius1 * sin;
			c1_end_y = c1_y + radius1 * cos;

			c2_start_x = c_x - radius2 * sin;
			c2_start_y = c_y - radius2 * cos;
			c2_end_x = c_x + radius2 * sin;
			c2_end_y = c_y + radius2 * cos;
		} else if (disx <= 0 && disy <= 0) {// C2在C1的右下方
			c1_start_x = c1_x - radius1 * sin;
			c1_start_y = c1_y + radius1 * cos;
			c1_end_x = c1_x + radius1 * sin;
			c1_end_y = c1_y - radius1 * cos;

			c2_start_x = c_x - radius2 * sin;
			c2_start_y = c_y + radius2 * cos;
			c2_end_x = c_x + radius2 * sin;
			c2_end_y = c_y - radius2 * cos;
		}

		path.moveTo(c1_start_x, c1_start_y);
		path.quadTo(control_x, control_y, c2_start_x, c2_start_y);
		path.lineTo(c2_end_x, c2_end_y);
		path.quadTo(control_x, control_y, c1_end_x, c1_end_y);
		path.close();
		return path;

	}

	public static final float CONSTANT_FORCIRCLE = 0.552284749831f;

	public static Path makeCirclePath(float x, float y, int radius) {
		Path path = new Path();
		float start_x = x;
		float start_y = y - radius;
		path.moveTo(start_x, start_y);
		float c1_x = x + CONSTANT_FORCIRCLE * radius;
		float c1_y = y - radius;
		float c2_x = x + radius;
		float c2_y = y - CONSTANT_FORCIRCLE * radius;
		float e1_x = x + radius;
		float e1_y = y;
		path.cubicTo(c1_x, c1_y, c2_x, c2_y, e1_x, e1_y);

		float c3_x = x + radius;
		float c3_y = y + CONSTANT_FORCIRCLE * radius;
		float c4_x = x + CONSTANT_FORCIRCLE * radius;
		float c4_y = y + radius;
		float e2_x = x;
		float e2_y = y + radius;
		path.cubicTo(c3_x, c3_y, c4_x, c4_y, e2_x, e2_y);

		float c5_x = x - CONSTANT_FORCIRCLE * radius;
		float c5_y = y + radius;
		float c6_x = x - radius;
		float c6_y = y + CONSTANT_FORCIRCLE * radius;
		float e3_x = x - radius;
		float e3_y = y;
		path.cubicTo(c5_x, c5_y, c6_x, c6_y, e3_x, e3_y);

		float c7_x = x - radius;
		float c7_y = y - CONSTANT_FORCIRCLE * radius;
		float c8_x = x - CONSTANT_FORCIRCLE * radius;
		float c8_y = y - radius;
		float e4_x = x;
		float e4_y = y - radius;
		path.cubicTo(c7_x, c7_y, c8_x, c8_y, e4_x, e4_y);

		return path;
	}

	public static Circle makeCircle(float x, float y, float radius) {
		Circle circle = new Circle();
		circle.x = x;
		circle.y = y;
		circle.raidus =radius;
		float start1_x = x;
		float start1_y = y - radius;
		float c1_x = x + CONSTANT_FORCIRCLE * radius;
		float c1_y = y - radius;
		float c2_x = x + radius;
		float c2_y = y - CONSTANT_FORCIRCLE * radius;
		float e1_x = x + radius;
		float e1_y = y;
		ThreeBessel first = circle.getFirstBessel();
		first.setStart_x(start1_x);
		first.setStart_y(start1_y);
		first.setContro1_x(c1_x);
		first.setContro1_y(c1_y);
		first.setContro2_x(c2_x);
		first.setContro2_y(c2_y);
		first.setEnd_x(e1_x);
		first.setEnd_y(e1_y);

		float c3_x = x + radius;
		float c3_y = y + CONSTANT_FORCIRCLE * radius;
		float c4_x = x + CONSTANT_FORCIRCLE * radius;
		float c4_y = y + radius;
		float e2_x = x;
		float e2_y = y + radius;
		ThreeBessel sec = circle.getSedBessel();
		sec.setStart_x(e1_x);
		sec.setStart_y(e1_y);
		sec.setContro1_x(c3_x);
		sec.setContro1_y(c3_y);
		sec.setContro2_x(c4_x);
		sec.setContro2_y(c4_y);
		sec.setEnd_x(e2_x);
		sec.setEnd_y(e2_y);

		float c5_x = x - CONSTANT_FORCIRCLE * radius;
		float c5_y = y + radius;
		float c6_x = x - radius;
		float c6_y = y + CONSTANT_FORCIRCLE * radius;
		float e3_x = x - radius;
		float e3_y = y;
		ThreeBessel tird = circle.getThirdBessel();
		tird.setStart_x(e2_x);
		tird.setStart_y(e2_y);
		tird.setContro1_x(c5_x);
		tird.setContro1_y(c5_y);
		tird.setContro2_x(c6_x);
		tird.setContro2_y(c6_y);
		tird.setEnd_x(e3_x);
		tird.setEnd_y(e3_y);

		float c7_x = x - radius;
		float c7_y = y - CONSTANT_FORCIRCLE * radius;
		float c8_x = x - CONSTANT_FORCIRCLE * radius;
		float c8_y = y - radius;
		float e4_x = x;
		float e4_y = y - radius;
		ThreeBessel fourth = circle.getFourBessel();
		fourth.setStart_x(e3_x);
		fourth.setStart_y(e3_y);
		fourth.setContro1_x(c7_x);
		fourth.setContro1_y(c7_y);
		fourth.setContro2_x(c8_x);
		fourth.setContro2_y(c8_y);
		fourth.setEnd_x(e4_x);
		fourth.setEnd_y(e4_y);

		return circle;
	}

	public static Path makePathByCircle(Circle circle) {
		Path path = new Path();
		ThreeBessel first = circle.getFirstBessel();
		ThreeBessel sec = circle.getSedBessel();
		ThreeBessel tird = circle.getThirdBessel();
		ThreeBessel fourth = circle.getFourBessel();
		path.moveTo(first.getStart_x(), first.getStart_y());
		path.cubicTo(first.getContro1_x(), first.getContro1_y(), first.getContro2_x(), first.getContro2_y(),
				first.getEnd_x(), first.getEnd_y());
		path.cubicTo(sec.getContro1_x(), sec.getContro1_y(), sec.getContro2_x(), sec.getContro2_y(),
				sec.getEnd_x(), sec.getEnd_y());
		path.cubicTo(tird.getContro1_x(), tird.getContro1_y(), tird.getContro2_x(), tird.getContro2_y(),
				tird.getEnd_x(), tird.getEnd_y());
		path.cubicTo(fourth.getContro1_x(), fourth.getContro1_y(), fourth.getContro2_x(), fourth.getContro2_y(),
				fourth.getEnd_x(), fourth.getEnd_y());
		return path;
	}
	
	
	/**
	 * 实现翻页效果的Path
	 * @param touchx  手指点击处的x坐标
	 * @param touchy  手指点击处的y坐标
	 * @param cornerX 翻页角的x坐标
	 * @param cornerY 翻页角的y坐标
	 * @return
	 */
	public static BookFlip make(float touchx,float touchy,float cornerX,float cornerY){
		BookFlip flip = new BookFlip();
		Path[] paths = flip.paths;
		Path path0 = paths[0];//整个翻角区域
		Path path1 = paths[1];//显示第一页翻角背景区域
		if(touchx==00&&touchy==0){
			return flip;
		}
		
		float midX = (touchx+cornerX)/2;//t s两点连线中点x坐标
		float midY = (touchy+cornerY)/2;//t s两点连线中点y坐标
		
		float dis_ms_x = (cornerX-midX);//中点同翻页角在X方向上得距离差
		float dis_ms_y = (midY-cornerY);//中点同翻页角在Y方向上得距离差
		
		float dis_mt_x = (cornerX-midX);//中点同手指点击处在X方向上得距离差
		float dis_mt_y = (midY-cornerY);//中点同手指点击处在Y方向上得距离差
		
		//两个控制点，是过t  x两点直线的垂直平分线，同X轴和Y轴的交点，c1是同X轴的交点，c2是同Y轴的交点
		float c1_x = midX-(dis_ms_y*dis_ms_y)/dis_ms_x;
		float c1_y = cornerY;
		
		float c2_x = cornerX;
		float c2_y = midY+(dis_ms_x*dis_ms_x)/dis_ms_y;
		
		//两个贝塞尔曲线的起点，分别是过t m两点线段的垂直平分线，同tc1和tc2的交点，s1是同tc1的交点，是tc1的中点；s2是同tc2的交点，是tc2的中点
		//tc1是t和第一个控制点c1的连线  tc2是t和第二个控制点c2的连线
		
		float s1_x = (touchx+c1_x)/2;
		float s1_y = (touchy+c1_y)/2;
		float s2_x = (touchx+c2_x)/2;
		float s2_y = (touchy+c2_y)/2;
		
		//两个贝塞尔曲线的终点，分别是过t m两点线段的垂直平分线，同X轴和Y轴的交点，e1是同X轴的交点，e2是同Y轴的交点
		//要求得这两个点，需要先确定过tc1和tc2中点的直线，然后利用两点式求得这两个点
		
		float tc1_x = (touchx+c1_x)/2;
		float tc1_y = (touchy+c1_y)/2;
		
		float tc2_x = (touchx+c2_x)/2;
		float tc2_y = (touchy+c2_y)/2;
		
		//直线的两点式 ：(x-x1)/(x1-x2)=(y-y1)/(y1-y2)
		
		float e1_x =(cornerY-tc1_y)/(tc1_y-tc2_y)*(tc1_x-tc2_x)+tc1_x;
		float e1_y = cornerY;
		
		float e2_x =cornerX;
		float e2_y = (cornerX-tc1_x)/(tc1_x-tc2_x)*(tc1_y-tc2_y)+tc1_y;
		
		//计算两个翻角最大范围的两个角。翻角的范围是个三角形，顶点是触点位置，另外两个点，分别是a1和a2
		//这两个点的位置，是平行于e1e2、c1c2，并且在这两条直线中间的直线，记做直线a1a2,同两个贝塞尔曲线的交点位置
		//由a1来说，三角形e1c1s1是第一条贝塞尔曲线起点、控制点和终点连线构成的三角形，直线a1a2过这个三角形
		//a1a2平行于三角形的边e1s1,并且等分另外两边
		//有贝塞尔曲线的实现原理来看，当点移动到起点和控制点连线中间时，此时曲线上的点，
		//必然是起点和控制点连线的中点，到控制点和终点连线的中点的连线的中点
		//假设Msc是起点、控制点连线中点位置，Mce是控制点、终点连线中点位置，当点移动到Msc位置时，曲线上的点的位置是：（Msc+Mce)/2
		//msc1这个是s1、c1连线的中点 mce1是c1、e1的连线中点
		//msc2这个是s2、c2连线的中点 mce2是c2、e2的连线中点
		//a1是msc1和mce1连线的中点 a2是msc2和mce2连线的中点 
		float msc1_x = (s1_x+c1_x)/2;
		float msc1_y = (s1_y+c1_y)/2;
		float mce1_x = (c1_x+e1_x)/2;
		float mce1_y = (c1_y+e1_y)/2;
		
		float msc2_x = (s2_x+c2_x)/2;
		float msc2_y = (s2_y+c2_y)/2;
		float mce2_x = (c2_x+e2_x)/2;
		float mce2_y = (c2_y+e2_y)/2;
		float a1_x = (msc1_x+mce1_x)/2;
		float a1_y = (msc1_y+mce1_y)/2;
		float a2_x = (msc2_x+mce2_x)/2;
		float a2_y = (msc2_y+mce2_y)/2;
		
		path0.moveTo(e2_x, e2_y);
		path0.quadTo(c2_x, c2_y, s2_x, s2_y);
		path0.lineTo(touchx, touchy);
		path0.lineTo(s1_x, s1_y);
		path0.quadTo(c1_x, c1_y, e1_x, e1_y);
		path0.lineTo(cornerX, cornerY);
		path0.close();
		
		path1.moveTo(a1_x, a1_y);
		path1.lineTo(touchx, touchy);
		path1.lineTo(a2_x, a2_y);
		path1.close();
		
		flip.touchX =touchx;
		flip.touchY =touchy;
		flip.cornerX = cornerX;
		flip.cornerY = cornerY;
		flip.s1_x = s1_x;
		flip.s1_y = s1_y;
		flip.e1_x = e1_x;
		flip.e1_y = e1_y;
		flip.s2_x = s2_x;
		flip.s2_y = s2_y;
		flip.e2_x = e2_x; 
		flip.e2_y = e2_y;
		flip.c1_x = c1_x;
		flip.c1_y = c1_y;
		flip.c2_x = c2_x;
		flip.c2_y = c2_y;
		flip.a1_x = a1_x;
		flip.a1_y = a1_y;
		flip.a2_x = a2_x;
		flip.a2_y = a2_y;
		
		return flip;
	}
	
	
	
}
