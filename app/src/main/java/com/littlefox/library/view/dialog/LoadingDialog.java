package com.littlefox.library.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.littlefox.view.library.R;


public class LoadingDialog extends Dialog
{
	private static final int[] FRAME_ANIMATION_LIST = {R.drawable.spinner, R.drawable.spinner_02, R.drawable.spinner_03, R.drawable.spinner_04, R.drawable.spinner_05, R.drawable.spinner_06};
	private static final int DURATION_FRAME = 50;
	public static final int TYPE_ROTATION 	= 0;
	public static final int TYPE_FRAME		= 1;
	private static final int DIALOG_SIZE = 150;
	private ProgressBar _LoadingImage;
	private Context mContext;
	public LoadingDialog(Context context, int width, int height)
	{
		super(context, R.style.TransparentProgressDialog);
		mContext = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams winParams = getWindow().getAttributes();
		winParams.gravity = Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(winParams);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		
		setContentView(R.layout.loading_dialog);
		_LoadingImage = (ProgressBar)findViewById(R.id.loading_image);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
		_LoadingImage.setLayoutParams(params);
	}
	@Override
	public void show()
	{
		super.show();
		
	}
	
	

}
