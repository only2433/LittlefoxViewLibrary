package com.littlefox.library.view.layoutmanager;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class LinearLayoutScrollerManager extends LinearLayoutManager
{
    private static float MILLISECONDS_PER_INCH = 100f;
    private boolean isScrollEnabled = true;

	public LinearLayoutScrollerManager(Context context)
	{
		super(context, VERTICAL, false);
	}

    public LinearLayoutScrollerManager(Context context, float millisecondDuration)
	{
		super(context, VERTICAL, false);
        MILLISECONDS_PER_INCH = millisecondDuration;
	}

	public LinearLayoutScrollerManager(Context context, int orientation, boolean reverseLayout)
	{
		super(context, orientation, reverseLayout);
	}

	@Override
	public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position)
	{
		RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext())
        {
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return super.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };
		smoothScroller.setTargetPosition(position);
		startSmoothScroll(smoothScroller);
	}

	private class TopSnappedSmoothScroller extends LinearSmoothScroller
	{
		public TopSnappedSmoothScroller(Context context)
		{
			super(context);

		}

		@Override
		public PointF computeScrollVectorForPosition(int targetPosition)
		{
			return LinearLayoutScrollerManager.this.computeScrollVectorForPosition(targetPosition);
		}

		@Override
		protected int getVerticalSnapPreference()
		{
			return SNAP_TO_START;
		}
	}

	public void setScrollEnabled(boolean flag) {
		this.isScrollEnabled = flag;
	}

	@Override
	public boolean canScrollVertically() {
		//Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
		return isScrollEnabled && super.canScrollVertically();
	}
}
