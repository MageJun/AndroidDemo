package com.demo.androiddemo.reader.service;

import java.util.List;

import android.graphics.Canvas;

public interface IReader {
	
	/**
	 * 打开书籍
	 * @param bookFilePath
	 * @param beginIndex
	 */
	void openBook(String bookFilePath,int beginIndex);
	
	/**
	 * 向下翻页
	 * @return
	 */
	List<String> pageDown();
	
	/**
	 * 向上翻页
	 * @return
	 */
	List<String> pageUp();
	
	/**
	 * 将文字内容画到指定的画板上
	 * @param canvas
	 */
	void draw(Canvas canvas);

}
