package com.littlefox.library.view.text;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;



public class TextDrawView extends View {

	Context mContext;
	private String mTime 		= "";
	private int mFontSize 		= 15;
	private int mColor 			= Color.rgb(255,255,255);
	private Paint mPaint	 	= null;
	private float  mLocationX 	= 0;
    
	
	public TextDrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mPaint = new Paint();
		
	}
	
	public void setText(String str) {
		mTime = str;		
		this.invalidate();
	}
	
	public String getText() {
		return mTime;	
		
	}
	
	public void setFont(int nSize) {
		mFontSize = nSize;		
	}
	
	public void setColor(int nColor) {
		mColor = nColor;		
	}
	
	protected void onDraw (Canvas canvas) {
		super.onDraw(canvas);
		
		mPaint.setAntiAlias(true);
		
		mPaint.setTextSize(mFontSize);
		mPaint.setColor(mColor);
		mPaint.setTextAlign(Paint.Align.CENTER);
		
		canvas.drawText(mTime, mLocationX, mFontSize, mPaint);	 

	}
	
	@Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        mLocationX = w * 0.5f;  // remember the center of the screen
    }
}
