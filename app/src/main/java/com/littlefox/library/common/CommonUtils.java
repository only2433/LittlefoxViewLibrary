package com.littlefox.library.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.littlefox.library.view.object.DisPlayMetricsObject;



public class CommonUtils
{
	/** 해당 변수는 static 변수도 어느순간 값을 회수되기 때문에 값을 가지고 있어야 하기 때문 */
	private static DisplayMetrics sDisPlayMetrics = null;
	private static float sDisplayFactor = 0.0f;
	private static final String PARAMS_DISPLAY_METRICS		= "display_metrics";
	public static final int TYPE_PARAMS_BOOLEAN = 0;
	public static final int TYPE_PARAMS_INTEGER = 1;
	public static final int TYPE_PARAMS_STRING	= 2;

	public static final int SECOND = 1000;
	public static final int HALF_SECOND = 500;
	
	
	public static void initDisplayMetrics(Context context)
	{
		if(sDisPlayMetrics == null)
		{
			sDisPlayMetrics = new DisplayMetrics();
		}
		
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(sDisPlayMetrics);
		DisPlayMetricsObject object = new DisPlayMetricsObject(sDisPlayMetrics.widthPixels, sDisPlayMetrics.heightPixels);
		CommonUtils.setPreferenceObject(context, PARAMS_DISPLAY_METRICS, object);
		sDisplayFactor = 0.0f;
	}
	
	public static int getPixel(Context context, int value)
	{
		try
		{
			if(sDisplayFactor == 0.0f)
			{
				sDisplayFactor = (float)sDisPlayMetrics.widthPixels / 1920.0f;
			}
		}catch(NullPointerException e)
		{
			DisPlayMetricsObject object = (DisPlayMetricsObject)CommonUtils.getPreferenceObject(context, PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
			sDisplayFactor = object.widthPixel / 1920.0f;
		}
		
		return (int)(value * sDisplayFactor);
	}
	
	/**
	 * 저장한 프리퍼런스를 불러온다.
	 * @param context 
	 * @param key  해당 값의 키값
	 * @param type 데이터의 타입
	 * @return
	 */
	public static Object getSharedPreference(Context context, String key, int type)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

		switch (type)
		{
		case TYPE_PARAMS_BOOLEAN:
			return pref.getBoolean(key, false);
		case TYPE_PARAMS_INTEGER:
			return pref.getInt(key, -1);
		case TYPE_PARAMS_STRING:
			return pref.getString(key, "");
		}

		return pref.getBoolean(key, false);
	}
	
	/**
	 * 해당 프리퍼런스를 저장한다.
	 * @param context
	 * @param key 해당 값의 키값
	 * @param object 저장할 데이터
	 */
	public static void setSharedPreference(Context context,String key, Object object)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		
		if(object instanceof Boolean)
		{
			editor.putBoolean(key, (Boolean) object);
		}
		else if(object instanceof Integer)
		{
			editor.putInt(key, (Integer) object);
		}
		else if(object instanceof String)
		{
			editor.putString(key, (String) object);

		}
		
		editor.commit();

	}
	
	/**
	 * 현재 모델이 타블릿인지 아닌지 확인
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context)
	{
		int xlargeBit = 4; // Configuration.SCREENLAYOUT_SIZE_XLARGE;
		Configuration config = context.getResources().getConfiguration();
		return (config.screenLayout & xlargeBit) == xlargeBit;
	}
	
    /**
     * Drawable 을 Bitmap 형태로 변경
     * @param drawable
     * @return
     */
	public static Bitmap drawableToBitmap(Drawable drawable)
	{
		if (drawable instanceof BitmapDrawable)
		{
			return ((BitmapDrawable) drawable).getBitmap();
		}
		
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
    /**
     * 오브젝트 클래스를 불러오는 프리퍼런스
     * @param context
     * @param key 키값
     * @param className 클래스 네임
     * @return
     */
    public static Object getPreferenceObject(Context context , String key, Class className)
    {
    	 Object result = null;
    	 SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
    	 String loadObjectString = pref.getString(key, "");
    	 
    	 if(loadObjectString.equals("") == false)
    	 {
    		 result = new Gson().fromJson(loadObjectString, className);
    	 }
    	
    	 return result;
    }
    
    /**
     * 오브젝트 클래스를 저장하는 프리퍼런스
     * @param context
     * @param key 키값
     * @param object 저장할 오브젝트
     */
    public static void setPreferenceObject(Context context , String key, Object object)
    {
    	String saveObjectString = "";
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        
        if(object != null)
        {
        	saveObjectString = new Gson().toJson(object);
        }
        
        editor.putString(key, saveObjectString);
        editor.commit();
    }
}
