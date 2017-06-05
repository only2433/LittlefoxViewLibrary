package com.littlefox.library.view.text.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

public class ColorVerticalCenterSpan extends ReplacementSpan {
	private int backgroundColor = 0;
	private int foregroundColor = 0;

	public ColorVerticalCenterSpan(int foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public ColorVerticalCenterSpan(int foregroundColor, int backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
	}

	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
		// Background
		if (backgroundColor != 0) {
			paint.setColor(backgroundColor);
			canvas.drawRect(new RectF(x, top, x + paint.measureText(text, start, end), bottom), paint);
		}

		// Text
		if (foregroundColor != 0) {
			paint.setColor(foregroundColor);
		}
		int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
		canvas.drawText(text, start, end, x, yPos, paint);
	}

	@Override
	public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
		return Math.round(paint.measureText(text, start, end));
	}
}