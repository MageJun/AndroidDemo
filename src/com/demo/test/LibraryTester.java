package com.demo.test;

import java.util.Iterator;
import java.util.Map;

import com.demo.androiddemo.DemoApplication;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.test.AndroidTestCase;
import android.text.TextUtils;
import android.util.Log;

public class LibraryTester extends AndroidTestCase {/*
	private static final String TAG = LibraryTester.class.getSimpleName();
	private Context mTestContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mTestContext = DemoApplication.getAppContext();
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	
	public void testLoadAllMetaData(){
		Map<String,AppMetaDatas> apps = MetaDataManager.getInstance(mContext).getMetaDataCollections();
		Iterator<String> iterator = apps.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			Log.i(TAG, "PackageName: "+key);
			AppMetaDatas amds = apps.get(key);
			Iterator<MetaData> iteratorAmd = amds.getMetaDatas().iterator();
			while(iteratorAmd.hasNext()){
				MetaData md = iteratorAmd.next();
				Log.i(TAG, "MetaData:"+md);
			}
		}
	}
	
	public void testGetMetaDataByKey(){
		AppMetaDatas amds = MetaDataManager.getInstance(mContext).getAppMetaDatas(MetaDataKey.KEY_HOME_UI);
		Log.i(TAG, "AppMetaDatas: "+amds);
	}
	
	public void testGetMetaData(){
		ComponentName mHomeComponetName = null;
		MetaData md = MetaDataManager.getInstance(mContext).getMetaData(MetaDataKey.KEY_HOME_UI);
		if(md!=null){
			String packageName = md.getPackageName();
			String value = md.getValue();
			Log.i(TAG, "loadHomeComponentName() packageName "+packageName+", value = "+value);
			if(!TextUtils.isEmpty(packageName)&&!TextUtils.isEmpty(value)){
				mHomeComponetName = new ComponentName(packageName, value);
			}
			Log.i(TAG, "mHomeComponetName: "+mHomeComponetName);
		}
	}
	
	public void testPath(){
		Path path = new Path();
		path.moveTo(0, 0);
		path.lineTo(50, 50);
		
		PathMeasure pm = new PathMeasure();
		pm.setPath(path, false);
		
		float[] pos =new float[2];
		pm.getPosTan(10, pos, null);
		Log.i(TAG, "pox[0]="+pos[0]+",pos[1]="+pos[1]);
	}
	
	
*/}
