package com.littlefox.library.view.listener;

import android.view.View;

public abstract class OnClickIndexListener implements View.OnClickListener
{
	protected int index;
	
	public OnClickIndexListener(int index)
	{
		this.index = index;
	}
}
