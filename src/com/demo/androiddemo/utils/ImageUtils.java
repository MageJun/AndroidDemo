package com.demo.androiddemo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.util.DisplayMetrics;
import android.view.WindowId;
import android.view.WindowManager;

public class ImageUtils {

	public ImageUtils() {
	}
	/**
	 * 图片切块,按需要的数量，平均切割成矩形
	 * @param bitmap
	 * @param count
	 * @return
	 */
	public static List<PieceImage> createPieceImage(Bitmap bitmap,int count){
		List<PieceImage> bitmaps = new ArrayList<PieceImage>();
		int bWidth = bitmap.getWidth();
		int bHeight = bitmap.getHeight();
		int widthNum = (int) Math.sqrt(count);
		int heightNum = count/widthNum;
		int averageHeight = bHeight/heightNum;
		int averageWidth = bWidth/widthNum;
		for (int i = 0; i < heightNum; i++) {
			for(int j = 0;j<widthNum;j++){
				int y = i*averageHeight;
				int x = j*averageWidth;
				Bitmap bm = Bitmap.createBitmap(bitmap,x, y, averageWidth, averageHeight);
				PieceImage image = new PieceImage();
				image.setBitmap(bm);
				image.setX(x);
				image.setY(y);
				bitmaps.add(image);
			}
		}
		return bitmaps;
	}
	
	
	/**
	 * 图片切块,按需要的数量，以中心点为中心，切割成不规则图形
	 * @param srcBitmap
	 * @param count
	 * @param interval
	 * @return
	 */
	public static List<PieceImage> createPieceImageShape(Bitmap srcBitmap,int count,float interval){
		List<PieceImage> images = new ArrayList<PieceImage>();
		int s_width = srcBitmap.getWidth();
		int s_height = srcBitmap.getHeight();
		Bitmap tmpBitmap = Bitmap.createBitmap(s_width, s_height, Config.ARGB_8888);
		Canvas tmpCanvas = new Canvas(tmpBitmap);
		Point point = new Point();
		point.setX(0);
		point.setY(0);
		
		
		return images;
	}
	
	public static Bitmap compressImage(Bitmap bitmap,int reqWidth,int reqHeight){
		
		Options op = new Options();
		op.inJustDecodeBounds = true;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bos);
		byte[] sr = bos.toByteArray();
		try {
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BitmapFactory.decodeByteArray(sr, 0, sr.length, op);
		int realWidth = op.outWidth;
		int realHeight = op.outHeight;
		int sampleSize = 1;
		while(realHeight>reqHeight||realWidth>reqWidth){
			realHeight = realHeight/sampleSize;
			realWidth = realWidth/sampleSize;
			sampleSize+=2;
		}
		op.inSampleSize = sampleSize;
		op.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(sr, 0, sr.length, op);
	}

}
