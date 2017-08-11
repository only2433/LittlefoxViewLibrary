package com.littlefox.library.view.listener;

import android.view.View;

/**
 * 인덱스를 전달 하기 위한 클릭 리스너
 * by 정재현
 */
public abstract class OnClickIndexListener implements View.OnClickListener
{
	protected int index;
	
	public OnClickIndexListener(int index)
	{
		this.index = index;
	}
}
