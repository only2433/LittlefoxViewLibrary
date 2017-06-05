package com.littlefox.library.view.extra;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class InterceptTouchImageView extends ImageView
{
	
	public InterceptTouchImageView(Context context)
	{
		super(context);
	}

	public InterceptTouchImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public InterceptTouchImageView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return true;
	}
	
	

}
