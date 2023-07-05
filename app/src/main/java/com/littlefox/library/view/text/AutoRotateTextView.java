package com.littlefox.library.view.text;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.littlefox.view.library.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AutoRotateTextView extends TextSwitcher implements ViewSwitcher.ViewFactory
{
    private static final long DURATION_DEFAULT = 1000;
    private static final float DEFAULT_TEXT_SIZE = 20.0f;
    private ArrayList<String> mTextList;
    private int mCurrentIndex = 0;
    private long mCurrentDelay = DURATION_DEFAULT;
    private float mCurrentTextSize = DEFAULT_TEXT_SIZE;
    private Animation mInAnimation;
    private Animation mOutAnimation;

    public AutoRotateTextView(Context context)
    {
        super(context);
        init();
    }

    public AutoRotateTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        setFactory(this);
        mInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.push_right_in);
        mOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.push_left_out);
        setInAnimation(mInAnimation);
        setOutAnimation(mOutAnimation);
    }

    private void startRotating()
    {
        if (mTextList != null && mTextList.size() > 0)
        {
            setText(mTextList.get(mCurrentIndex));
            mCurrentIndex = (mCurrentIndex + 1) % mTextList.size();
            postDelayed(rotateRunnable, mCurrentDelay);
        }
    }

    private void stopRotating()
    {
        removeCallbacks(rotateRunnable);
    }

    public void setTextList(ArrayList<String> list)
    {
        mTextList = list;
    }

    public void setDelay(long delay)
    {
        mCurrentDelay = delay;
    }

    public void setFontSize(float size)
    {
        mCurrentTextSize = size;
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        startRotating();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        stopRotating();
    }

    @Override
    public View makeView()
    {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textView.setTextSize(mCurrentTextSize);
        return textView;
    }

    private Runnable rotateRunnable = new Runnable() {
        @Override
        public void run() {
            setText(mTextList.get(mCurrentIndex));
            mCurrentIndex = (mCurrentIndex + 1) % mTextList.size();
            postDelayed(this, mCurrentDelay);
        }
    };
}
