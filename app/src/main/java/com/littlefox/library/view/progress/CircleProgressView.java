package com.littlefox.library.view.progress;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.littlefox.library.common.CommonUtils;
import com.littlefox.view.library.R;

public class CircleProgressView extends ImageView
{
	private static final float MAX_ARC = 360.0f;
	
	

	
	private int mCurrentPercent	= 0;
	private Paint mPaint;
	private Bitmap mCurrentBitmap = null;
	private Bitmap mCurrentCoverBitmap = null;
	private RectF mProgressRectF = null;
	private Context mContext;
	public CircleProgressView(Context context)
	{
		super(context);
		init(context);
	}

	public CircleProgressView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}
	
	public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init(context);
	}
	

	
	private void init(Context context)
	{
		mContext = context;
		CommonUtils.initDisplayMetrics(mContext);
		mPaint = new Paint();
		mPaint.setColor(getResources().getColor(R.color.percent_color));
		mPaint.setAntiAlias(true);

		if(CommonUtils.isTablet(mContext))
		{
			mCurrentBitmap = CommonUtils.drawableToBitmap(getResources().getDrawable(R.drawable.tablet_thumbnail_download));
			mCurrentBitmap = Bitmap.createScaledBitmap(mCurrentBitmap, CommonUtils.getPixel(mContext,92), CommonUtils.getPixel(mContext,92), true);
		}
		else
		{
			mCurrentBitmap = CommonUtils.drawableToBitmap(getResources().getDrawable(R.drawable.thumbnail_download));
			mCurrentBitmap = Bitmap.createScaledBitmap(mCurrentBitmap, CommonUtils.getPixel(mContext,145), CommonUtils.getPixel(mContext,145), true);

		}
	
		mCurrentCoverBitmap = CommonUtils.drawableToBitmap(getResources().getDrawable(R.drawable.thumbnail_download_s));
		
		if(CommonUtils.isTablet(mContext))
		{
			mCurrentCoverBitmap = Bitmap.createScaledBitmap(mCurrentCoverBitmap, CommonUtils.getPixel(mContext,65), CommonUtils.getPixel(mContext,65), true);
			mProgressRectF = new RectF(CommonUtils.getPixel(mContext,7),CommonUtils.getPixel(mContext,4), CommonUtils.getPixel(mContext,88), CommonUtils.getPixel(mContext,86));
		}
		else
		{
			mCurrentCoverBitmap = Bitmap.createScaledBitmap(mCurrentCoverBitmap, CommonUtils.getPixel(mContext,110), CommonUtils.getPixel(mContext,110), true);
			mProgressRectF = new RectF(CommonUtils.getPixel(mContext,9), CommonUtils.getPixel(mContext,5), CommonUtils.getPixel(mContext,140), CommonUtils.getPixel(mContext,140));
		}
		

		
		invalidate();
	}
	

	
	public void setPercent(int percent)
	{
		mCurrentPercent = percent;
		invalidate();
	}

	/**
	 * 다운로드 퍼센테이지에 따라 화면을 그리는 부분 
	 */
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawBitmap(mCurrentBitmap, 0, 0, null);
		canvas.drawArc(mProgressRectF, -90, mCurrentPercent * (MAX_ARC/100), true, mPaint);

		if(CommonUtils.isTablet(mContext))
		{
			canvas.drawBitmap(mCurrentCoverBitmap, CommonUtils.getPixel(mContext,15), CommonUtils.getPixel(mContext,14), null);
		}
		else
		{
			canvas.drawBitmap(mCurrentCoverBitmap, CommonUtils.getPixel(mContext,19), CommonUtils.getPixel(mContext,18), null);
		}
		
		
	}
	
	
	
}
