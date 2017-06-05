package com.littlefox.library.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.littlefox.view.library.R;

/**
 * Loading Dialog 의 Material Style
 * @author 정재현
 *
 */
public class MaterialLoadingDialog extends Dialog
{
	private int mDialogColor;
	private int mDialogSize = 0;
	private ProgressWheel _ProgressWheel;
	public MaterialLoadingDialog(Context context, int size)
	{
		super(context, R.style.TransparentProgressDialog);
		mDialogColor = context.getResources().getColor(R.color.material_dialog_default_color);
		mDialogSize = size;
		init(context);
	}
	
	public MaterialLoadingDialog(Context context, int size, int color)
	{
		super(context, R.style.TransparentProgressDialog);
		mDialogColor = color;
		mDialogSize = size;
		init(context);
	}
	
	private void init(Context context)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams winParams = getWindow().getAttributes();
		winParams.gravity = Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(winParams);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		setContentView(R.layout.material_progress_dialog);
		_ProgressWheel = (ProgressWheel)findViewById(R.id.progress_wheel);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDialogSize, mDialogSize);
		_ProgressWheel.setLayoutParams(params);
		_ProgressWheel.setBarColor(mDialogColor);
		
	}

}
