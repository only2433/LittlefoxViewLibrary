package com.littlefox.library.view.listener;

import android.os.SystemClock;
import android.view.View;

import com.littlefox.library.common.CommonUtils;

public abstract class OnSingleClickListner implements View.OnClickListener
{
    private long mLastClickTime = 0L;

    public abstract void onSingleClick(View v);

    @Override
    public void onClick(View v)
    {
        if(SystemClock.elapsedRealtime() - mLastClickTime < CommonUtils.HALF_SECOND)
        {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        onSingleClick(v);
    }
}
