package com.littlefox.library.view.extra;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollSendScrollView extends ScrollView
{

	// true if we can scroll (not locked)
    // false if we cannot scroll (locked)
    private boolean mScrollable = true;
    
	public interface OnScrollCheckListener
	{
		void onCatch(boolean isCatch);
	}
	private OnScrollCheckListener mOnScrollCheckListener;
	
	private int mCatchScrollValue = -1;
	public ScrollSendScrollView(Context context)
	{
		super(context);
	}
	
	public ScrollSendScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public ScrollSendScrollView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}
	
    public void setScrollingEnabled(boolean enabled) {
        mScrollable = enabled;
    }

    public boolean isScrollable() {
        return mScrollable;
    }
	
	public void setCatchScrollValue(int value)
	{
		mCatchScrollValue = value;
	}
	
	public void setOnScrollCheckListener(OnScrollCheckListener onScrollCheckListener)
	{
		mOnScrollCheckListener = onScrollCheckListener;
	}

	@Override
	protected void onScrollChanged(int currentHorizontal, int currentVertical, int previousHorizontal, int previousVertical)
	{
		super.onScrollChanged(currentHorizontal, currentVertical, previousHorizontal, previousVertical);
				
		if(mOnScrollCheckListener != null && mCatchScrollValue != -1)
		{
			if(currentVertical > mCatchScrollValue)
			{
				mOnScrollCheckListener.onCatch(true);
			}
			else if(currentVertical <= mCatchScrollValue)
			{
				mOnScrollCheckListener.onCatch(false);
			}
		}
		
		
	}
	




    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                if (mScrollable) return super.onTouchEvent(ev);
                // only continue to handle the touch event if scrolling enabled
                return mScrollable; // mScrollable is always false at this point
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if 
        // we are not scrollable
        if (!mScrollable) return false;
        else return super.onInterceptTouchEvent(ev);
    }

	
	

}
