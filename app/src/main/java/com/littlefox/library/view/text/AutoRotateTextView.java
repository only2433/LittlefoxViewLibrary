package com.littlefox.library.view.text;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.Dimension;

import com.littlefox.library.system.handler.WeakReferenceHandler;
import com.littlefox.library.system.handler.callback.MessageHandlerCallback;
import com.littlefox.view.library.R;

import java.util.ArrayList;

public class AutoRotateTextView extends TextSwitcher implements ViewSwitcher.ViewFactory, MessageHandlerCallback
{
    private static final int MESSAGE_ROTATE = 100;
    private static final long DURATION_DEFAULT = 1000;
    private static final int DEFAULT_TEXT_SIZE = 20;
    private ArrayList<String> mTextList;
    private int mCurrentIndex = 0;
    private long mCurrentDelay = DURATION_DEFAULT;

    private WeakReferenceHandler mMainHandler;

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
        mMainHandler = new WeakReferenceHandler(this);
    }

    private void startRotating()
    {
        if (mTextList != null && mTextList.size() > 0)
        {
            mMainHandler.sendEmptyMessage(MESSAGE_ROTATE);
        }
    }

    private void stopRotating()
    {
        mMainHandler.removeCallbacksAndMessages(null);
    }

    private void adjustTextViewSize(TextView textView)
    {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams);
    }

    private void adjustTextViewSize(TextView textView, int width, int height)
    {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams);
    }

    public void setTextList(ArrayList<String> list)
    {
        mTextList = list;
    }

    public void setDelay(long delay)
    {
        mCurrentDelay = delay;
    }

    public void setTextSize(int viewIndex, int size)
    {
        View view = getChildAt(viewIndex);
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setTextSize(Dimension.SP , size);
        }
    }

    public void setTextSize(float textSize, int width, int height)
    {
        for(int i = 0; i < getChildCount(); i++)
        {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setTextSize(Dimension.SP ,textSize);
                adjustTextViewSize(textView, width, height);
            }
        }

    }

    public void setTextSize(float textSize, int width, int height, int gravity)
    {
        for(int i = 0; i < getChildCount(); i++)
        {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setGravity(gravity);
                textView.setTextSize(Dimension.SP ,textSize);
                adjustTextViewSize(textView, width, height);
            }
        }
    }

    public void start()
    {
        startRotating();
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

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
        textView.setGravity(Gravity.CENTER_VERTICAL);
        adjustTextViewSize(textView);
        return textView;
    }

    @Override
    public void handlerMessage(Message msg)
    {
        switch (msg.what)
        {
            case MESSAGE_ROTATE:
                setText(mTextList.get(mCurrentIndex));
                mCurrentIndex = (mCurrentIndex + 1) % mTextList.size();
                mMainHandler.sendEmptyMessageDelayed(MESSAGE_ROTATE, mCurrentDelay);
                break;
        }
    }
}
