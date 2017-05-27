package com.demo.androiddemo.reader.service;


/**
 * @author lwang
 * @version 1.0
 * @created 11-5月-2017 11:03:09
 */
public class ReadFile {

	private String bookPath;
	/**
	 * 封面图片
	 */
	private String coverPicturePath;
	/**
	 * 书名
	 */
	private String bookName;
	/**
	 * 之前阅读的位置
	 */
	private long lastIndex;
	/**
	 * 图书大小
	 */
	private long bookSize;

	public ReadFile(){

	}
	
	

	public String getBookPath() {
		return bookPath;
	}



	public void setBookPath(String bookPath) {
		this.bookPath = bookPath;
	}



	public String getCoverPicturePath() {
		return coverPicturePath;
	}



	public void setCoverPicturePath(String coverPicturePath) {
		this.coverPicturePath = coverPicturePath;
	}



	public String getBookName() {
		return bookName;
	}



	public void setBookName(String bookName) {
		this.bookName = bookName;
	}



	public long getLastIndex() {
		return lastIndex;
	}



	public void setLastIndex(long lastIndex) {
		this.lastIndex = lastIndex;
	}



	public long getBookSize() {
		return bookSize;
	}



	public void setBookSize(long bookSize) {
		this.bookSize = bookSize;
	}



	public void finalize() throws Throwable {

	}

}