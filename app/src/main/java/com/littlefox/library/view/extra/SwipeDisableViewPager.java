package com.littlefox.library.view.extra;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.littlefox.commonviewlibrary.R;

public class SwipeDisableViewPager extends ViewPager
{
	private boolean isSwipable = true;
	public SwipeDisableViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeDisableViewPager);
		
		try
		{
			isSwipable = typeArray.getBoolean(R.styleable.SwipeDisableViewPager_swipeable, true);
		}catch(Exception e)
		{
			
		}finally
		{
			typeArray.recycle();
		}
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		return isSwipable == true ? super.onInterceptTouchEvent(event) : false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return isSwipable == true ? super.onTouchEvent(event) : false;
	}
	
	

}
