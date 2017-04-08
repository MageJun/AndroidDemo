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
}
