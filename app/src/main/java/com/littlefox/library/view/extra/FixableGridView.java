package com.littlefox.library.view.extra;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

public class FixableGridView extends GridView
{
	private boolean isExpand = false;

	public FixableGridView(Context context)
	{
		super(context);
	}

	public FixableGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public FixableGridView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	public boolean isExpanded()
	{
		return isExpand;
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// HACK! TAKE THAT ANDROID!
		if (isExpanded())
		{
			// Calculate entire height by providing a very large height hint.
			// View.MEASURED_SIZE_MASK represents the largest height possible.
			int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);

			ViewGroup.LayoutParams params = getLayoutParams();
			params.height = getMeasuredHeight();
		}
		else
		{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	public void setExpanded(boolean expanded)
	{
		this.isExpand = expanded;
	}

}
