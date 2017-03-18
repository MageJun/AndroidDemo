package com.demo.androiddemo.customview;

import java.util.List;

import com.demo.androiddemo.R;
import com.demo.androiddemo.utils.ImageUtils;
import com.demo.androiddemo.utils.PieceImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class BrokenGlassView extends BaseView {

	/**
	 * 碎玻璃效果图
	 * @param context
	 */
	public BrokenGlassView(Context context) {
		super(context);
		init();
	}

	public BrokenGlassView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public BrokenGlassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	private Paint mPaint;
	private void init(){
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		reqWidth = dm.widthPixels;
		reqHeight = dm.heightPixels;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tmp);
		Bitmap cBitmap = ImageUtils.compressImage( bitmap, reqWidth, reqHeight);
		//测试Canvas进行剪切之后，如何操作剪切的canvas和剩下的canvas
//		Canvas bitCanvas = new Canvas(cBitmap);
		int cbWidth = cBitmap.getWidth();
		int cbHeight = cBitmap.getHeight();
		Bitmap tmpBitmap = Bitmap.createBitmap(cbWidth, cbHeight, Config.ARGB_8888);
		Canvas tmpCanvas = new Canvas(tmpBitmap);
		Path clipPath = new Path();
		clipPath.moveTo(0, 0);
		clipPath.lineTo(cbWidth, cbHeight);
		clipPath.lineTo(cbWidth, 0);
		clipPath.close();
		tmpCanvas.save();
		tmpCanvas.clipPath(clipPath);
		tmpCanvas.drawBitmap(cBitmap, 0, 0, mPaint);
		Bitmap b1= Bitmap.createBitmap(tmpBitmap);
		canvas.drawBitmap(b1, 0, 0, mPaint);
		tmpCanvas.restore();
		clearCanvas(tmpCanvas);
//		tmpCanvas.drawBitmap(cBitmap, 0, 0, mPaint);
		canvas.drawBitmap(tmpBitmap, 0, cbHeight, mPaint);
//		canvas.drawBitmap(tmpBitmap, 0, cbHeight, mPaint);
		/*List<PieceImage> images = ImageUtils.createBitmap(cBitmap, 9);
		int count = 0;
		int heightMove = 0;
		for(int i = 0;i<3;i++){
			heightMove+=10;
			int widthMove = 0;
			for(int j = 0;j<3;j++){
				PieceImage image = images.get(count);
				canvas.drawBitmap(image.getBitmap(),image.getX()+widthMove, image.getY()+heightMove, mPaint);
				count++;
				widthMove+=10;
			}
		}*/
	}
	
	/**
	 * 清空指定Canvas
	 * @param canvas
	 */
	private void clearCanvas(Canvas canvas) {
		Paint p = new Paint();
		// 清屏
		p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		canvas.drawPaint(p);
	}

	private int m_width = 100;
	private int m_height = 100;
	private int reqWidth;
	private int reqHeight;
	@Override
	protected void initWidthAndHeight() {
		mWidth = m_width;
		mHeight = m_height;
	}

}
