package com.demo.androiddemo.reader.service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.List;
import java.util.Vector;

import com.demo.androiddemo.DemoApplication;
import com.demo.androiddemo.utils.LogUtil;
import com.demo.androiddemo.utils.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;

/**
 * @author lwang
 * @version 1.0
 * @created 11-5月-2017 11:03:09
 */
public class ReaderImpl implements IReader {
	
	private static final String TAG = ReaderImpl.class.getSimpleName();
	
	//页面的宽高
	private int mWidth,mHeight;
	//页面显示Style，包含文字大小、背景图片、行间距
	private Style mReadStyle;
	//文件开始位置
	private int mBeginIndex;
	//当前页开始位置
	private int mCurPageStartIndex;
	//当前页结束位置
	private int mCurPageEndIndex;
	//文件内存映射对象，对象将文件内容映射到了内存中
	private MappedByteBuffer mBookBuffer;
	//页面允许的最大行数
	private int maxLines;
	//保存一个页面显示的文字
	private List<String> mLines = new Vector<String>();
	//图书的字节数
	private long mBookLength;
	
	//编码格式
	private String mCharset = "GBK";
	
	private TextPaint mTextPaint;
	
	private Paint mPaint;
	//行间距
	private float mRawSpacing;
	
	public ReaderImpl(){
		
	}
	
	public ReaderImpl(int width,int height,Style readStyle){
		this.mWidth = width;
		this.mHeight = height;
		this.mReadStyle = readStyle;
		mTextPaint = new TextPaint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(calcTextSize(readStyle.getTextSize()));
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		
		//计算出行间距
		float rawSpace = mReadStyle.getRawSpace();
		float textSize = calcTextSize(readStyle.getTextSize());
		mRawSpacing = textSize*rawSpace;
		//计算允许的最大行数
		maxLines =  height/(int)(textSize+mRawSpacing);
		
	}
	
	private float calcTextSize(int textSize){
		DisplayMetrics mDisplay = Utils.getDisplayMetrics(DemoApplication.getAppContext());
		//字体大小设置为15sp，转换为像素是：15*scaledDensity+0.5f
		return textSize*mDisplay.scaledDensity+0.5f;
	}
	
	@SuppressWarnings("resource")
	@Override
	public void openBook(String bookFilePath, int beginIndex) {
		File file = new File(bookFilePath);
		mBookLength = file.length();
		try {
			LogUtil.i(TAG, "[openBook] book open ,path = "+bookFilePath);
			mBookBuffer = new RandomAccessFile(file, "r").getChannel().map(MapMode.READ_ONLY, 0, mBookLength);
			LogUtil.i(TAG, "[openBook] book open successed,length = "+mBookLength);
		} catch (IOException e) {
			LogUtil.i(TAG, "[openBook] book open failed,length = "+mBookLength);
			e.printStackTrace();
		}
		
		LogUtil.i(TAG, "[openBook] beginIndex = "+beginIndex);
		if(beginIndex>0){
			mCurPageStartIndex = beginIndex;
			mCurPageEndIndex = beginIndex;
		}
	}

	@Override
	public List<String> pageDown() {
		LogUtil.i(TAG, "[pageDown] mCurPageEndIndex ="+mCurPageEndIndex+",mBookLength="+mBookLength);
		if(mCurPageEndIndex>mBookLength-1){
			return null;
		}
		Vector<String> lines = new Vector<String>();
		while(lines.size()<maxLines&&mCurPageEndIndex<mBookLength){
			byte[] datas = readNextLine(mCurPageEndIndex);
			mCurPageEndIndex+=datas.length;
			try {
				String lineStr = new String(datas, mCharset);
				LogUtil.i(TAG,"[lineStr] = "+lineStr);
				String lineEndStr = "";
				if(lineStr.indexOf("\r\n")!=-1){
					lineEndStr = "\r\n";
					lineStr = lineStr.replaceAll("\r\n", "");
				}else if(lineStr.indexOf("\n")!=-1){
					lineEndStr = "\n";
					lineStr = lineStr.replaceAll("\n", "");
				}
				if(lineStr.length()==0){
					lines.add(lineStr);
				}
				while(lineStr.length()>0){
					int count = mTextPaint.breakText(lineStr, true, mWidth, null);
					lines.add(lineStr.substring(0, count));
					lineStr = lineStr.substring(count);
					if(lines.size()>=maxLines){
						break;
					}
				}
				
				if(lineStr.length()>0){
					LogUtil.i(TAG, "[more str] lineStr = "+lineStr+", lineEndStr = "+lineEndStr);
					 mCurPageEndIndex-=(lineStr+lineEndStr).getBytes(mCharset).length;
				}
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		LogUtil.i(TAG, "[pageDown] lines="+lines.toString());
		return lines;
	}
	private int maxPageByte = 1024*50;//一个页面最大读取50kb
	/**
	 * 读取指定位置的下一行
	 * @param startIndex
	 * @return
	 */
	private byte[] readNextLine(int startIndex){
		LogUtil.i(TAG, "[readNextLine] startIndex="+startIndex);
		int i = startIndex;
		byte b1,b2;
		if("UTF-16LE".equals(mCharset)){
			while(i<mBookLength-1&&(i-startIndex<maxPageByte)){
				b1 = mBookBuffer.get(i++);
				b2 = mBookBuffer.get(i++);
				if(b1==0x0a&&b2==0x00){
					break;
				}
			}
		}else if("UTF-16BE".equals(mCharset)){
			
			while(i<mBookLength-1&&(i-startIndex<maxPageByte)){
				b1 = mBookBuffer.get(i++);
				b2 = mBookBuffer.get(i++);
				if(b1==0x00&&b2==0x0a){
					break;
				}
			}
		}else{
			while(i<mBookLength&&(i-startIndex<maxPageByte)){
				b1 = mBookBuffer.get(i++);
				if(b1==0x0a){
					break;
				}
			}
		}
		int length = i-startIndex;
		byte[] dst = new byte[length];
		for(i = 0;i<length;i++){
			dst[i] = mBookBuffer.get(startIndex+i);
		}
		LogUtil.i(TAG, "[readNextLine] endIndex="+(startIndex+i-1));
		return dst;
	}
	
	/**
	 * 读取指定位置的上一行,因为是从当前页的开始位置往后读取的，所以读取到的文字无法显示
	 * 这个方法调用的主要作用，是为了修改上一页的开始位置
	 * @param endIndex 上一页的最后一个位置
	 * @return
	 */
	private byte[] readPreLine(int endIndex){
		LogUtil.i(TAG, "[readPreLine] endIndex="+endIndex);
		int i = 0;
		byte b1,b2;
		if("UTF-16LE".equals(mCharset)){
			i = endIndex-2;
			while(i>0&&(endIndex-i<maxPageByte)){
				b1 = mBookBuffer.get(i);
				b2 = mBookBuffer.get(i+1);
				if(b1==0x0a&&b2==0x00&&i!=endIndex-2){
					i+=2;
					break;
				}
				i--;
			}
		}else if("UTF-16BE".equals(mCharset)){
			i = endIndex-2;
			while(i>0&&(endIndex-i<maxPageByte)){
				b1 = mBookBuffer.get(i);
				b2 = mBookBuffer.get(i+1);
				if(b1==0x00&&b2==0x0a&&i!=endIndex-2){
					i+=2;
					break;
				}
				i--;
			}
		}else{
			i = endIndex-1;
			while(i>0&&(endIndex-i<maxPageByte)){
				b1 = mBookBuffer.get(i);
				if(b1==0x0a&&i!=endIndex-1){
					i++;
					break;
				}
				i--;
			}
		}
		if(i<0){
			i = 0;
		}
		int length = endIndex-i;
		byte[] dst = new byte[length];
		for(int j = 0;j<length;j++){
			dst[j] = mBookBuffer.get(i+j);
		}
		LogUtil.i(TAG, "[readPreLine] startIndex="+i);
		return dst;
	}

	@Override
	public List<String> pageUp() {
		LogUtil.i(TAG, "[pageUp] mCurPageStartIndex="+mCurPageStartIndex);
		if(mCurPageStartIndex<0){
			mCurPageStartIndex = 0;
		}
		Vector<String> lines = new Vector<String>();
		while(lines.size()<maxLines&&mCurPageStartIndex>0){
			Vector<String> tmp = new Vector<String>();
			byte[] data = readPreLine(mCurPageStartIndex);
			mCurPageStartIndex-=data.length;
			try {
				String lineStr = new String(data, mCharset);
				lineStr = lineStr.replaceAll("\r\n", "");
				lineStr = lineStr.replaceAll("\n", "");
				
				if(TextUtils.isEmpty(lineStr)){
					tmp.add(lineStr);
				}
				while(lineStr.length()>0){
					int count = mTextPaint.breakText(lineStr, true, mWidth, null);
					tmp.add(lineStr.substring(0, count));
					lineStr = lineStr.substring(count);
				}
				lines.addAll(0,tmp);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(lines.size()>maxLines){
			try {
				mCurPageStartIndex+=lines.get(0).getBytes(mCharset).length;
				lines.remove(0);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mCurPageEndIndex = mCurPageStartIndex;//上一页的开始位置，相当于上上一页的结束位置，这个时候就可以直接调用pageDown，获取上一页的正常内容了
		LogUtil.i(TAG, "[pageUp] mCurPageEndIndex="+mCurPageEndIndex);
		return lines;
	}

	@Override
	public void draw(Canvas canvas) {
		LogUtil.i(TAG, "[draw] mLines="+mLines.toString());
		canvas.drawBitmap(mReadStyle.getBg(), 0, 0, mPaint);
		
		if(mLines.size()==0){
			mLines = pageDown();
		}
		
		if(mLines.size()>0){
			int y = 0;
			for(int i = 0;i<mLines.size();i++){
				y+=(int)(calcTextSize(mReadStyle.getTextSize())+mRawSpacing);
				canvas.drawText(mLines.get(i), 0, y, mTextPaint);
			}
		}
	}
	private Canvas backCanvas;
	public void setBack(Canvas canvas){
		this.backCanvas = canvas;
	}
	
	public void turnNext(){
		LogUtil.i(TAG, "[turnNext] mCurPageEndIndex="+mCurPageEndIndex);
		//翻页的时候，将当前页的最后位置，作为下一页的开始位置
		mCurPageStartIndex = mCurPageEndIndex;
		mLines.clear();
		mLines = pageDown();
	}
	
	public void turnPre(){
		LogUtil.i(TAG, "[turnPre] mCurPageEndIndex="+mCurPageEndIndex);
		pageUp();
		mLines.clear();
		mLines = pageDown();
	}
	
}