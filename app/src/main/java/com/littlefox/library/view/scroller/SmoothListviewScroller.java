package com.littlefox.library.view.scroller;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearSmoothScroller;

public class SmoothListviewScroller extends LinearSmoothScroller {

    private static final float MILLISECONDS_PER_INCH = 50f; // 스크롤 속도 조절

    public SmoothListviewScroller(Context context) {
        super(context);
    }

    @Override
    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
    }
}