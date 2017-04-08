package com.demo.androiddemo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.Random;
public class Utils {

    private Utils() {}
    static int screenWidth;
    static int screenHeight;
    private static Random random = new Random();
    private static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;
    private static final Canvas mCanvas = new Canvas();

   public  static int dp2px(int dp) {
        return Math.round(dp * DENSITY);
    }

   public static Bitmap convertViewToBitmap(View view) {
        view.clearFocus();
        Bitmap bitmap = createBitmapSafely(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_4444, 2);
        if (bitmap != null) {
            mCanvas.setBitmap(bitmap);
            mCanvas.translate(-view.getScrollX(), -view.getScrollY());
            view.draw(mCanvas);
            mCanvas.setBitmap(null);
        }
        return bitmap;
    }

   public static Bitmap createBitmapSafely(int width, int height, Bitmap.Config config, int retryCount) {
        while(retryCount-- > 0) {
            try {
                return Bitmap.createBitmap(width, height, config);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                System.gc();
            }
        }
        return null;
    }

   public static int nextInt(int a, int b){
        return Math.min(a,b) + random.nextInt(Math.abs(a - b));
    }

   public static int nextInt(int a){
        return random.nextInt(a);
    }

   public static float nextFloat(float a, float b){
        return Math.min(a,b) + random.nextFloat() * Math.abs(a - b);
    }

   public static float nextFloat(float a){
        return random.nextFloat() * a;
    }
   
   public static float netFloat(){
	   return random.nextFloat();
   }

   public   static boolean nextBoolean(){
        return random.nextBoolean();
    }
   
   public static DisplayMetrics getDisplayMetrics(Context context){
	   DisplayMetrics metric = new DisplayMetrics();  
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getRealMetrics(metric);;
		int disHeight = metric.heightPixels;
		int disWidth = metric.widthPixels;
		return metric;
   }
   
   /**
    * 计算两点间直线距离
    * @param srcX
    * @param srcY
    * @param dstX
    * @param dstY
    * @return
    */
	public static  float calcStraightLen(float srcX, float srcY, float dstX, float dstY) {
		float disx = srcX-dstX;
		float disy = srcY-dstY;
		float len = (float) Math.sqrt(disx*disx+disy*disy);
		return len;
	}
}
