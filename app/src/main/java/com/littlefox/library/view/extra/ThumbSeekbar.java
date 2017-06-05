package com.littlefox.library.view.extra;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;


public class ThumbSeekbar extends SeekBar
{
	private Drawable mThumb;

	public ThumbSeekbar(Context context)
	{
		super(context);
	}

	public ThumbSeekbar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public ThumbSeekbar(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void setThumb(Drawable thumb)
	{
		super.setThumb(thumb);
		mThumb = thumb;
	}
	
	public void setThumb(int width, int height , int thumb)
	{
		Bitmap bitmap=BitmapFactory.decodeResource(getResources(), thumb);
        Bitmap resizeThumb=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(resizeThumb);
        canvas.drawBitmap(bitmap,new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),
                new Rect(0,0,resizeThumb.getWidth(),resizeThumb.getHeight()),null);
        Drawable drawable = new BitmapDrawable(getResources(),resizeThumb);
        setThumb(drawable);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if (event.getX() < mThumb.getBounds().left || event.getX() > mThumb.getBounds().right || event.getY() > mThumb.getBounds().bottom || event.getY() < mThumb.getBounds().top)
			{
				return false;
			}
		}
		return super.onTouchEvent(event);
	}

}
