package com.demo.androiddemo.reader.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.demo.androiddemo.DemoApplication;
import com.demo.androiddemo.utils.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler.Callback;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

/**
 * @author lwang
 * @version 1.0
 * @created 11-5月-2017 11:03:09
 */
public abstract class Reader {

	/**
	 * 正在操作的图书
	 */
	private ReadFile mCurrentReadFile = new ReadFile();
	/**
	 * 阅读Style，包含字体大小、背景色和翻页效果
	 */
	private Style mReadStyle = new Style();
	/**
	 * 保存由文字生成的用来显示的图片
	 */
	protected List<Bitmap> mToShowBitmaps = new ArrayList<Bitmap>();
	/**
	 * 当前的阅读位置
	 */
	private long mCurrentIndex;
	private int mCacheChars = 1000;//预加载的字符数
	
	private int mMaxWidth;
	private int mMaxHeight;
	private InputStreamReader mReader;
	private BufferedReader mBufferReader;
	private TextPaint mPaint;
	
	public Reader(){
		
	}

	public Reader(ReadFile mCurrentReadFile,Style mReadStyle ,int mMaxWidth,int mMaxHeight){
		this.mCurrentReadFile = mCurrentReadFile;
		this.mReadStyle = mReadStyle;
		this.mPaint = new TextPaint();
		this.mCurrentIndex = mCurrentReadFile.getLastIndex();
		initTextPaint(mPaint);
		this.mMaxWidth = mMaxWidth;
		this.mMaxHeight = mMaxHeight;
	}
	private int maxTextCounts;
	private int mTextHeight;
	private int mTextWidth;
	private void initTextPaint(TextPaint paint) {
		paint.setTextSize(calcTextSize(mReadStyle.getTextSize()));
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
	}
	private float calcTextSize(int textSize){
		DisplayMetrics mDisplay = Utils.getDisplayMetrics(DemoApplication.getAppContext());
		//字体大小设置为15sp，转换为像素是：15*scaledDensity+0.5f
		return textSize*mDisplay.scaledDensity+0.5f;
	}

	/**
	 * 返回当前页
	 */
	public abstract Bitmap getCurrentPage();

	/**
	 * 返回上一页
	 */
	public abstract Bitmap getPreviousPage();

	/**
	 * 返回下一页
	 */
	public abstract Bitmap getNextPage();

	/**
	 * 翻到上一页
	 */
	public Bitmap turnToPreviousPage(){
		if(mToShowBitmaps.size()==0){
			return null;
		}
		return null;
	}

	/**
	 * 翻到下一页
	 */
	public Bitmap turnToNextPage(){
		return null;
	}
	
	private void openFile() {
		closeFile();
		try {
//			mReader = new InputStreamReader(new FileInputStream(mCurrentReadFile.getBookPath())/*, "gbk"*/);
			InputStream is = DemoApplication.getAppContext().getResources().getAssets().open("test.txt");
			mReader = new InputStreamReader(is,"gbk");
			mBufferReader = new BufferedReader(mReader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/ catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void closeFile(){
		try {
			if (mBufferReader != null) {
				mBufferReader.close();
				mBufferReader = null;
			}
			
			if (mReader != null) {
				mReader.close();
				mReader = null;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private Callback mCallback;
	public void load(Callback callback){
		mCallback = callback;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				openFile();
				createBitmaps();
				mCallback.handleMessage(null);
			}
		}).start();
	}
	private void createBitmaps(){
		TextPaint paint = mPaint;
//		long skip = mCurrentIndex - mCacheChars/2;
		try {
//			mBufferReader.skip(skip);
			char[] chars = new char[mCacheChars];
//			String str = mBufferReader.readLine();
			int result = mBufferReader.read(chars,0,mCacheChars);
			//画当前页
//			int offset = mCacheChars/2;
			int offset = 0;
			TextBitmap curBitmap = createTextBitmap(paint, chars,offset);
			//画下一页
			TextBitmap nextBitmap = createTextBitmap(paint, chars,offset+curBitmap.text.length());
			//画上一页
//			int charCount = curBitmap.text.length();
//			offset = offset-charCount;
//			TextBitmap previousBitmap = createTextBitmap(paint, chars,offset);
			mToShowBitmaps.clear();
			mToShowBitmaps.add(/*previousBitmap.bitmap*/null);
			mToShowBitmaps.add(curBitmap.bitmap);
			mToShowBitmaps.add(nextBitmap.bitmap);
			saveBitmap2File(curBitmap.bitmap,"cur_tmp.jpg");
			saveBitmap2File(nextBitmap.bitmap,"next_tmp.jpg");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private TextBitmap createTextBitmap(TextPaint paint, char[] chars,int offset) {
		Bitmap tmpBitmap = Bitmap.createBitmap(mMaxWidth, mMaxHeight, Config.ARGB_8888);
		Canvas tmpCanvas = new Canvas(tmpBitmap);
		tmpCanvas.drawColor(mReadStyle.getBgColor());
		float x = 0;
		float y = 0;
		StringBuffer sb = new StringBuffer();
		while(y<mMaxHeight&&offset<chars.length){
			int charCount = paint.breakText(chars, offset, chars.length-offset, mMaxWidth, null);
			//判断如果截取的一行文字，最后一个字符是\r，并且下个字符是\n，应该放到下次进行完整处理
			int curEndIndex = offset+charCount-1;
			if(chars[curEndIndex]=='\r'&&chars[curEndIndex+1]=='\n'){
				 charCount--;
			}
			String text = new String(chars, offset, charCount);
			Rect fontBound = new Rect();
			paint.getTextBounds(text, 0, text.length(), fontBound);
			int textHeight = fontBound.height();
			if((textHeight+y)>mMaxHeight){
				break;
			}
			y+=textHeight;
			String[] strs = text.split("\r\n");
			int count = 0;
			if(strs.length>0){
				for (int i = 0; i < strs.length; i++) {
					String tmpStr = strs[i];
					
					if(text.endsWith("\r\n")||i!=strs.length-1){
						count+=tmpStr.length()+2;
						sb.append(tmpStr+"\r\n");
					}else{
						count+=tmpStr.length();
						sb.append(tmpStr);
					}
					tmpCanvas.drawText(tmpStr, x, y, paint);
					y += textHeight;
					if(y>mMaxHeight){
						break;
					}
				}
				charCount = count;
			}else{
				sb.append(text);
				tmpCanvas.drawText(text, x, y, paint);
			}
			offset+=charCount;
			/*tmpCanvas.save();
			tmpCanvas.translate(0, textHeight);
			StaticLayout sl = new StaticLayout(text, 0, text.length(), paint, mMaxWidth, Alignment.ALIGN_NORMAL
					, 1.5f, 0, false);
			sl.draw(tmpCanvas);
			tmpCanvas.restore();*/
		}
		TextBitmap result = new TextBitmap(tmpBitmap, sb.toString());
		return result;
	}
	
	private TextBitmap createTextBitmap2(TextPaint paint, char[] chars,int offset) {
		Bitmap tmpBitmap = Bitmap.createBitmap(mMaxWidth, mMaxHeight, Config.ARGB_8888);
		Canvas tmpCanvas = new Canvas(tmpBitmap);
		tmpCanvas.drawColor(mReadStyle.getBgColor());
		float x = 0;
		float y = 0;
		StringBuffer sb = new StringBuffer();
		while(y<mMaxHeight&&offset<chars.length){
			int charCount = paint.breakText(chars, offset, chars.length-offset, mMaxWidth, null);
			String text = new String(chars, offset, charCount);
			Rect fontBound = new Rect();
			paint.getTextBounds(text, 0, text.length(), fontBound);
			int textHeight = fontBound.height();
			if((textHeight+y)>mMaxHeight){
				break;
			}
			y+=textHeight;
			String[] strs = text.split("\r\n");
			int count = 0;
			if(strs.length>0){
				for (int i = 0; i < strs.length; i++) {
					String tmpStr = strs[i];
					
					if(text.endsWith("\r\n")||i!=strs.length-1){
						count+=tmpStr.length()+4;
						sb.append(tmpStr+"\r\n");
					}else{
						count+=tmpStr.length();
						sb.append(tmpStr);
					}
					tmpCanvas.drawText(tmpStr, x, y, paint);
					y += textHeight;
					if(y>mMaxHeight){
						break;
					}
				}
				charCount = count;
			}else{
				sb.append(text);
				tmpCanvas.drawText(text, x, y, paint);
			}
			offset+=charCount;
		}
		TextBitmap result = new TextBitmap(tmpBitmap, sb.toString());
		return result;
	}
	private void saveBitmap2File(Bitmap tmpBitmap,String fileName) {
		File file = new File(Environment.getExternalStorageDirectory()+File.separator+fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			tmpBitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public TextBitmap createTextBitmap(Paint paint, char[] chars,int offset,int width,int height) {
		Bitmap tmpBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas tmpCanvas = new Canvas(tmpBitmap);
		float x = 0;
		float y = 0;
		StringBuffer sb = new StringBuffer();
		while(y<height&&offset<chars.length){
			int charCount = paint.breakText(chars, offset, chars.length-offset, width, null);
			String text = new String(chars, offset, charCount);
			offset+=charCount;
			Rect fontBound = new Rect();
			paint.getTextBounds(text, 0, text.length(), fontBound);
			float result = paint.measureText(text);
			int textHeight = fontBound.height();
			if((textHeight+y)>height){
				break;
			}
			sb.append(text);
			tmpCanvas.drawText(text, x, y, paint);
			y+=textHeight;
		}
		TextBitmap result = new TextBitmap(tmpBitmap, sb.toString());
		return result;
	}
	
	public static class TextBitmap{
		public TextBitmap(Bitmap bitmap,String text){
			this.bitmap = bitmap;
			this.text = text;
		}
		 Bitmap bitmap;
		 String text;
	}

}