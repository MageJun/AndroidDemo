package com.demo.androiddemo.customview;

import java.util.ArrayList;

import com.demo.androiddemo.utils.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;

public class MathDemoView extends BaseView {

	private int  COMPLEXITY = 12;
	private LinePath[] lineRifts,mLinePath ;
	private Paint mPaint;
	private int SEGMENT = 66;

	public MathDemoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stubs
		init();
	}

	public MathDemoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public MathDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	private void init() {
		mLinePath = new LinePath[COMPLEXITY];
		lineRifts = new LinePath[COMPLEXITY];
		circleWidth = new int[COMPLEXITY];
		circleRifts = new Path[COMPLEXITY];
		pathArray = new ArrayList<Path>();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(2);
	}
	@Override
	protected void initWidthAndHeight() {
		mWidth = 200;
		mHeight = 200;
	}
	private int padding = 100;
	private boolean hasCircleRifts = true;
	private int[] circleWidth;
	private Path[] circleRifts;
	private ArrayList<Path> pathArray;
	@Override
	protected void onDraw(Canvas canvas) {
		int height = getMeasuredHeight();
		int width = getMeasuredWidth();
		canvas.drawColor(Color.YELLOW);
		Rect r = new Rect();
		//返回视图在整个屏幕中可是区域的坐标范围
		this.getGlobalVisibleRect(r);
		r.offset(-padding, -padding);
//		buildBaselines(mLinePath, r);
		buildBrokenLines(r);
		buildBrokenAreas(r);
		Path path =new Path();
		for (int i = 0; i < pathArray.size(); i++) {
			Path lp = pathArray.get(i);
			canvas.drawPath(lp, mPaint);
		}
//		for (int i = 0; i < lineRifts.length; i++) {
//			LinePath lp = lineRifts[i];
//			canvas.drawPath(lp, mPaint);
//			canvas.drawCircle(lp.getEndX(), lp.getEndY(), 5, mPaint);
//		}
		
	}
	
	 /**
     * Build warped-lines according to the baselines, like the DiscretePathEffect.
     */
    private void buildBrokenLines(Rect r) {
        LinePath[] baseLines = new LinePath[COMPLEXITY];
        buildBaselines(baseLines, r);
        PathMeasure pmTemp = new PathMeasure();
        for (int i = 0; i < COMPLEXITY; i++) {
            lineRifts[i] = new LinePath();
            lineRifts[i].moveTo(0, 0);
            lineRifts[i].setEndPoint(baseLines[i].getEndPoint());

            pmTemp.setPath(baseLines[i], false);
            float length = pmTemp.getLength();
            final int THRESHOLD = SEGMENT + SEGMENT / 2;

            if (length > Utils.dp2px(THRESHOLD)) {
                lineRifts[i].setStraight(false);
                // First, line to the point at SEGMENT of baseline;
                // Second, line to the random-point at (SEGMENT+SEGMENT/2) of baseline;
                // So when we set the start-draw-length to SEGMENT and the paint style is "FILL",
                // we can make the line become visible faster(exactly, the triangle)
                float[] pos = new float[2];
                pmTemp.getPosTan(Utils.dp2px(SEGMENT), pos, null);
                lineRifts[i].lineTo(pos[0], pos[1]);

                lineRifts[i].points.add(new Point((int)pos[0], (int)pos[1]));

                int xRandom, yRandom;
                int step = Utils.dp2px(THRESHOLD);
                do{
                    pmTemp.getPosTan(step, pos, null);
                    // !!!
                    // Here determine the stroke width of lineRifts
                    xRandom = (int) (pos[0] + Utils.nextInt(-Utils.dp2px(3),Utils.dp2px(2)));
                    yRandom = (int) (pos[1] + Utils.nextInt(-Utils.dp2px(2),Utils.dp2px(3)));
                    lineRifts[i].lineTo(xRandom, yRandom);
                    lineRifts[i].points.add(new Point(xRandom, yRandom));
                    step += Utils.dp2px(SEGMENT);
                } while (step < length);
                lineRifts[i].lineToEnd();
            } else {
                // Too short, it's still a beeline, so we must warp it later {@warpStraightLines()},
                // to make sure it is visible in "FILL" mode.
                lineRifts[i] = baseLines[i];
                lineRifts[i].setStraight(true);
            }
            lineRifts[i].points.add(lineRifts[i].getEndPoint());
        }
    }
	
	 private void buildBaselines(LinePath[] baseLines,Rect r){
	        for(int i = 0; i < COMPLEXITY; i++){
	            baseLines[i] = new LinePath();
	            baseLines[i].moveTo(0,0);
	        }
	        buildFirstLine(baseLines[0], r);

	        // First angle
	        int angle = (int)(Math.toDegrees(Math.atan((float)(-baseLines[0].getEndY()) / baseLines[0].getEndX())));

	        // The four diagonal angle base
	        int[] angleBase = new int[4];
	        angleBase[0] = (int)(Math.toDegrees(Math.atan((float)(-r.top) / (r.right))));
	        angleBase[1] = (int)(Math.toDegrees(Math.atan((float)(-r.top) / (-r.left))));
	        angleBase[2] = (int)(Math.toDegrees(Math.atan((float)(r.bottom) / (-r.left))));
	        angleBase[3] = (int)(Math.toDegrees(Math.atan((float)(r.bottom) / (r.right))));

	        if(baseLines[0].getEndX() < 0) // 2-quadrant,3-quadrant
	            angle += 180;
	        else if(baseLines[0].getEndX() > 0 && baseLines[0].getEndY() > 0) // 4-quadrant
	            angle += 360;

	        // Random angle range
	        int range = 360 / COMPLEXITY / 3;
	        int angleRandom;

	        for(int i = 1; i<COMPLEXITY; i++) {
	            angle = angle + 360 / COMPLEXITY;
	            if (angle >= 360)
	                angle -= 360;

	            angleRandom = angle + Utils.nextInt(-range, range);
	            if (angleRandom >= 360)
	                angleRandom -= 360;
	            else if (angleRandom < 0)
	                angleRandom += 360;

	            baseLines[i].obtainEndPoint(angleRandom,angleBase,r);
	            baseLines[i].lineToEnd();
	        }
	    }

	    /**
	     * Line to the the farthest boundary, in case appear a super big piece.
	     */
	    private void buildFirstLine(LinePath path, Rect r){
	        int[] range=new int[]{-r.left,-r.top,r.right,r.bottom};
	        int max = -1;
	        int maxId = 0;
	        for(int i = 0; i < 4; i++) {
	            if(range[i] > max) {
	                max = range[i];
	                maxId = i;
	            }
	        }
	        switch (maxId){
	            case 0:
	                path.setEndPoint(r.left, Utils.nextInt(r.height()) + r.top);
	                break;
	            case 1:
	                path.setEndPoint(Utils.nextInt(r.width()) + r.left, r.top);
	                break;
	            case 2:
	                path.setEndPoint(r.right, Utils.nextInt(r.height()) + r.top);
	                break;
	            case 3:
	                path.setEndPoint(Utils.nextInt(r.width()) + r.left, r.bottom);
	                break;
	        }
	        path.lineToEnd();
	    }
	    
	    private void buildBrokenAreas(Rect r){
	        final int SEGMENT_LESS = SEGMENT * 7 / 9;
	        final int START_LENGTH = (int)(SEGMENT * 1.1);

	        // The Circle-Rifts is just some isosceles triangles,
	        // "linkLen" is the length of oblique side
	        float linkLen = 0;
	        int repeat = 0;

	        PathMeasure pmNow = new PathMeasure();
	        PathMeasure pmPre = new PathMeasure();

	        for(int i = 0; i < COMPLEXITY; i++) {

	            lineRifts[i].setStartLength(Utils.dp2px(START_LENGTH));

	            if (repeat > 0) {
	                repeat--;
	            } else {
	                linkLen = Utils.nextInt(Utils.dp2px(SEGMENT_LESS),Utils.dp2px(SEGMENT));
	                repeat = Utils.nextInt(3);
	            }

	            int iPre = (i - 1) < 0 ? COMPLEXITY - 1 : i - 1;
	            pmNow.setPath(lineRifts[i],false);
	            pmPre.setPath(lineRifts[iPre], false);

	            if (hasCircleRifts  && pmNow.getLength() > linkLen && pmPre.getLength() > linkLen) {

	                float[] pointNow = new float[2];
	                float[] pointPre = new float[2];	
	                circleWidth[i] = Utils.nextInt(Utils.dp2px(1)) + 1;
	                circleRifts[i] = new Path();
	                pmNow.getPosTan(linkLen, pointNow, null);
	                circleRifts[i].moveTo(pointNow[0], pointNow[1]);
	                pmPre.getPosTan(linkLen, pointPre, null);
	                circleRifts[i].lineTo(pointPre[0], pointPre[1]);

	                // The area outside Circle-Rifts
	                Path pathArea = new Path();
	                pmPre.getSegment(linkLen, pmPre.getLength(), pathArea, true);
	                pathArea.rLineTo(0, 0); // KITKAT(API 19) and earlier need it
	                drawBorder(pathArea,lineRifts[iPre].getEndPoint(),
	                        lineRifts[i].points.get(lineRifts[i].points.size() - 1),r);
	                for (int j =  lineRifts[i].points.size() - 2; j >= 0; j--)
	                    pathArea.lineTo(lineRifts[i].points.get(j).x, lineRifts[i].points.get(j).y);
	                pathArea.lineTo(pointNow[0], pointNow[1]);
	                pathArea.lineTo(pointPre[0], pointPre[1]);
	                pathArea.close();
	                pathArray.add(pathArea);

	                // The area inside Circle-Rifts, it's a isosceles triangles
	                pathArea = new Path();
	                pathArea.moveTo(0,0);
	                pathArea.lineTo(pointPre[0],pointPre[1]);
	                pathArea.lineTo(pointNow[0],pointNow[1]);
	                pathArea.close();
	                pathArray.add(pathArea);
	            }
	            else{
	                // Too short, there is no Circle-Rifts
	                Path pathArea = new Path(lineRifts[iPre]);
	                drawBorder(pathArea, lineRifts[iPre].getEndPoint(), lineRifts[i].points.get( lineRifts[i].points.size()-1),r);
	                for (int j = lineRifts[i].points.size() - 2; j >= 0; j--)
	                    pathArea.lineTo(lineRifts[i].points.get(j).x,  lineRifts[i].points.get(j).y);
	                pathArea.close();
	                pathArray.add(pathArea);
	            }
	        }
	    }

	    public void drawBorder(Path path,Point pointStart,Point pointEnd,Rect r){
	        if(pointStart.x == r.right) {
	            if(pointEnd.x == r.right)
	                path.lineTo(pointEnd.x, pointEnd.y);
	            else if(pointEnd.y == r.top) {
	                path.lineTo(r.right, r.top);
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }
	            else if(pointEnd.x == r.left){
	                path.lineTo(r.right, r.top);
	                path.lineTo(r.left, r.top);
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }else if(pointEnd.y == r.bottom){
	                path.lineTo(r.right, r.top);
	                path.lineTo(r.left, r.top);
	                path.lineTo(r.left, r.bottom);
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }
	        }
	        else if(pointStart.y == r.top) {
	            if(pointEnd.y == r.top){
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }else if(pointEnd.x == r.left){
	                path.lineTo(r.left,r.top);
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }else if(pointEnd.y == r.bottom){
	                path.lineTo(r.left,r.top);
	                path.lineTo(r.left,r.bottom);
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }else if(pointEnd.x == r.right){
	                path.lineTo(r.left,r.top);
	                path.lineTo(r.left,r.bottom);
	                path.lineTo(r.right,r.bottom);
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }
	        }
	        else if(pointStart.x == r.left) {
	            if(pointEnd.x == r.left){
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }else if(pointEnd.y == r.bottom){
	                path.lineTo(r.left,r.bottom);
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }else if(pointEnd.x == r.right){
	                path.lineTo(r.left,r.bottom);
	                path.lineTo(r.right,r.bottom);
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }else if(pointEnd.y == r.top){
	                path.lineTo(r.left,r.bottom);
	                path.lineTo(r.right,r.bottom);
	                path.lineTo(r.right,r.top);
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }
	        }
	        else if(pointStart.y == r.bottom) {
	            if(pointEnd.y == r.bottom) {
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }
	            else if(pointEnd.x == r.right){
	                path.lineTo(r.right,r.bottom);
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }else if(pointEnd.y == r.top){
	                path.lineTo(r.right,r.bottom);
	                path.lineTo(r.right,r.top);
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }else if(pointEnd.x == r.left){
	                path.lineTo(r.right,r.bottom);
	                path.lineTo(r.right,r.top);
	                path.lineTo(r.left,r.top);
	                path.lineTo(pointEnd.x, pointEnd.y);
	            }
	        }
	    }

}
