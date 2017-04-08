package com.demo.androiddemo.utils;


public class Circle {

	public Circle() {
		initBessels();
	}

	private ThreeBessel firstBessel ;
	private ThreeBessel sedBessel;
	private ThreeBessel thirdBessel;
	private ThreeBessel fourBessel;
	public float raidus;
	public float x;
	public float y;

	private void initBessels() {
		firstBessel = new ThreeBessel();
		sedBessel = new ThreeBessel();
		thirdBessel = new ThreeBessel();
		fourBessel = new ThreeBessel();
	}

	public ThreeBessel getFirstBessel() {
		return firstBessel;
	}

	public ThreeBessel getSedBessel() {
		return sedBessel;
	}

	public ThreeBessel getThirdBessel() {
		return thirdBessel;
	}

	public ThreeBessel getFourBessel() {
		return fourBessel;
	}
	// 三阶贝塞尔
	public class ThreeBessel {
		public float start_x;// 起点X坐标
		public float start_y;// 起点Y坐标
		public float end_x;// 终点X坐标
		public float end_y;// 终点Y坐标
		public float contro1_x;// 第一控制点X坐标
		public float contro1_y;// 第一控制点Y坐标
		public float contro2_x;// 第二控制点X坐标
		public float contro2_y;// 第二控制点Y坐标
		public float getStart_x() {
			return start_x;
		}
		public void setStart_x(float start_x) {
			this.start_x = start_x;
		}
		public float getStart_y() {
			return start_y;
		}
		public void setStart_y(float start_y) {
			this.start_y = start_y;
		}
		public float getEnd_x() {
			return end_x;
		}
		public void setEnd_x(float end_x) {
			this.end_x = end_x;
		}
		public float getEnd_y() {
			return end_y;
		}
		public void setEnd_y(float end_y) {
			this.end_y = end_y;
		}
		public float getContro1_x() {
			return contro1_x;
		}
		public void setContro1_x(float contro1_x) {
			this.contro1_x = contro1_x;
		}
		public float getContro1_y() {
			return contro1_y;
		}
		public void setContro1_y(float contro1_y) {
			this.contro1_y = contro1_y;
		}
		public float getContro2_x() {
			return contro2_x;
		}
		public void setContro2_x(float contro2_x) {
			this.contro2_x = contro2_x;
		}
		public float getContro2_y() {
			return contro2_y;
		}
		public void setContro2_y(float contro2_y) {
			this.contro2_y = contro2_y;
		}
		
	}
	

}
