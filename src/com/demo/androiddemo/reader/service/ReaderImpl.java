package com.demo.androiddemo.reader.service;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author lwang
 * @version 1.0
 * @created 11-5月-2017 11:03:09
 */
public class ReaderImpl extends Reader {
	
	public ReaderImpl(){
		
	}

	public ReaderImpl(ReadFile mCurrentReadFile,Style mReadeStyle,int mMaxWidth, int mMaxHeight) {
		super(mCurrentReadFile,mReadeStyle,mMaxWidth, mMaxHeight);
	}



	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * 返回当前页
	 */
	public Bitmap getCurrentPage(){
		return mToShowBitmaps.get(1);
	}

	/**
	 * 返回下一页
	 */
	public Bitmap getNextPage(){
		return mToShowBitmaps.get(2);
	}

	/**
	 * 返回上一页
	 */
	public Bitmap getPreviousPage(){
		return mToShowBitmaps.get(0);
	}

	/**
	 * 翻到下一页
	 */
	public Bitmap turnToNextPage(){
		return null;
	}

	/**
	 * 翻到上一页
	 */
	public Bitmap turnToPreviousPage(){
		return null;
	}

}