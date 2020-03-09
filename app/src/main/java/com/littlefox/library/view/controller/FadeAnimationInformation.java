package com.littlefox.library.view.controller;

import android.view.View;
import android.view.animation.Animation;

public class FadeAnimationInformation
{
	private View view 					= null;
	private Animation fadeInAnimation 	= null;
	private Animation fadeOutAnimation 	= null;
	private int delay					= -1;
	private boolean isAnimationing		= false;
	private boolean isAutoFadeOut		= false;

	/**
	 * Controller에 넣을 뷰의 정보
	 * @param view 해당의 뷰
	 */
	public FadeAnimationInformation(View view)
	{
		this.view 			= view;
		isAutoFadeOut		= false;
	}
	
	/**
	 * Controller에 넣을 뷰의 정보
	 * @param view 해당의 뷰 
	 * @param fadeInAnimation 나타날때의 애니메이션
	 * @param fadeOutAnimation 들어갈때의 애니메이션
	 */
	public FadeAnimationInformation(View view , Animation fadeInAnimation , Animation fadeOutAnimation)
	{
		this.view 				= view;
		this.fadeInAnimation 	= fadeInAnimation;
		this.fadeOutAnimation 	= fadeOutAnimation;
		isAutoFadeOut		= false;
	}
	
	/**
	 * Controller에 넣을 뷰의 정보
	 * @param view 해당의 뷰 
	 * @param fadeInAnimation 나타날때의 애니메이션
	 * @param fadeOutAnimation 들어갈때의 애니메이션
	 * @param delay 나타난 후 다시 들어갈때 사이의 Term
	 */
	public FadeAnimationInformation(View view , Animation fadeInAnimation , Animation fadeOutAnimation , int delay)
	{
		this.view 				= view;
		this.fadeInAnimation 	= fadeInAnimation;
		this.fadeOutAnimation 	= fadeOutAnimation;
		this.delay				= delay;
	}

	public View getView()
	{
		return view;
	}

	public Animation getFadeInAnimation()
	{
		return fadeInAnimation;
	}

	public Animation getFadeOutAnimation()
	{
		return fadeOutAnimation;
	}

	public void setFadeInAnimation(Animation fadeInAnimation)
	{
		this.fadeInAnimation = fadeInAnimation;
	}

	public void setFadeOutAnimation(Animation fadeOutAnimation)
	{
		this.fadeOutAnimation = fadeOutAnimation;
	}


	public int getDelay()
	{
		return delay;
	}

	public boolean isAnimationing()
	{
		return isAnimationing;
	}

	public void setAnimationing(boolean isAnimationing)
	{
		this.isAnimationing = isAnimationing;
	}
	
	public boolean isAutoFadeOut()
	{
		return isAutoFadeOut;
	}

	public void setAutoFadeOut(boolean isFadeOut)
	{
		this.isAutoFadeOut = isFadeOut;
	}

	public void clearAnimation()
	{
		view.clearAnimation();
	}
	
	
}