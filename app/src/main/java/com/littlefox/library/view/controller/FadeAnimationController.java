package com.littlefox.library.view.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;

import java.util.ArrayList;

/**
 * 자동 FadeOut를 하기위한 모듈 Controller
 * @author 정재현
 *
 */
public class FadeAnimationController
{
	private static final int MESSAGE_DELAY_ANIMATION 	= 0;
	private static final int MESSAGE_PROMPT_ANIMATION	= 1;
	
	public static final int TYPE_FADE_IN 	= 0;
	public static final int TYPE_FADE_OUT 	= 1;
	
	private ArrayList<FadeAnimationInformation> controledlist = new ArrayList<FadeAnimationInformation>();
	private Context mContext;
	
	Handler mFadeControllHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case MESSAGE_DELAY_ANIMATION:
				delayAnimation((Integer) msg.obj);
				break;
			case MESSAGE_PROMPT_ANIMATION:
				promptAnimation((Integer) msg.obj, null, msg.arg1, false);
				break;
			}
		}
		
		
		
	};
	
	public FadeAnimationController(Context context)
	{
		mContext = context;
	}
	
	/**
	 * FadeController에 있는 View 의 index 를 리턴
	 * @param viewId 해당 뷰의 Id
	 * @return
	 */
	private int getViewIndexInControledlist(int viewId)
	{
		int result = -1;
		
		for(int i = 0 ; i < controledlist.size(); i++)
		{
			if(controledlist.get(i).getView().getId() == viewId)
			{
				result = i;
				break;
			}
		}
		return result;
	}
	

	/**
	 * 즉시 애니메이션을 실행
	 * @param viewId 해당 뷰의 ID
	 * @param type FADE_IN : 나타나는 애니메이션 , FADE_OUT : 들어가는 애니메이션
	 */
	private void promptAnimation(int viewId, Animation animation, int type, final boolean isAnimationClear)
	{
		final int resultIndex = getViewIndexInControledlist(viewId);
		Animation tempAnimation = null;
		if(animation == null)
		{
			tempAnimation = type == TYPE_FADE_IN ? controledlist.get(resultIndex).getFadeInAnimation() : controledlist.get(resultIndex).getFadeOutAnimation();
		}
		else
		{
			tempAnimation = animation;
		}
		controledlist.get(resultIndex).getView().startAnimation(tempAnimation);
		tempAnimation.setAnimationListener(new Animation.AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation animation)
			{

			}
			
			@Override
			public void onAnimationRepeat(Animation animation)
			{

			}
			
			@Override
			public void onAnimationEnd(Animation animation)
			{

				controledlist.get(resultIndex).setAnimationing(false);

				if(isAnimationClear)
				{
					controledlist.get(resultIndex).clearAnimation();
				}

				controledlist.get(resultIndex).setAutoFadeOut(false);
			}
		});
		
		if(type == TYPE_FADE_IN)
		{
			controledlist.get(resultIndex).getView().setVisibility(View.VISIBLE);
		}
		else
		{
			controledlist.get(resultIndex).getView().setVisibility(View.GONE);
		}
	}
	
	/**
	 * 애니메이션 후 딜레이를 주고 FadeOut 애니메이션을 실행
	 * @param viewId View 의 ID
	 */
	private void delayAnimation(int viewId)
	{
		 
		final int resultIndex = getViewIndexInControledlist(viewId);
		
		if(controledlist.get(resultIndex).getView().getVisibility() == View.GONE)
		{
			Animation animation = controledlist.get(resultIndex).getFadeInAnimation();
			controledlist.get(resultIndex).getView().startAnimation(animation);
			animation.setAnimationListener(new Animation.AnimationListener()
			{
				@Override
				public void onAnimationStart(Animation animation)
				{

				}
				
				@Override
				public void onAnimationRepeat(Animation animation)
				{

				}
				
				@Override
				public void onAnimationEnd(Animation animation)
				{
					controledlist.get(resultIndex).setAnimationing(false);
					controledlist.get(resultIndex).clearAnimation();
				}
			});
			
			controledlist.get(resultIndex).getView().setVisibility(View.VISIBLE);
		}

		Message msg = Message.obtain();
		msg.what = MESSAGE_PROMPT_ANIMATION;
		msg.obj  = viewId;
		msg.arg1 = TYPE_FADE_OUT;
		mFadeControllHandler.sendMessageDelayed(msg, controledlist.get(resultIndex).getDelay());
	}
	
	
	/**
	 * 현재 지금 해당 뷰가 애니메이션 도중인지 아닌지 알수있다.
	 * @param view View
	 * @return TRUE: 애니메이션이 실행중이다. </p> FALSE : 애니메이션을 실행하지 않은 상태이다.
	 */
	public boolean isAnimationing(View view)
	{
		int resultIndex = -1;
		resultIndex = getViewIndexInControledlist(view.getId());
		
		if(resultIndex != -1)
		{
			return controledlist.get(resultIndex).isAnimationing();
		}
		
		return false;
	}
	
	/**
	 * Controller에 FadeAnimationView를 추가한다.
	 * @param fadeAnimationInformation FadeAnimationView 의 정보
	 */
	public void addControlView(FadeAnimationInformation fadeAnimationInformation)
	{
	    int addIndex = getViewIndexInControledlist(fadeAnimationInformation.getView().getId());

	    if(addIndex == -1)
        {
            controledlist.add(fadeAnimationInformation);
        }
        else
        {
            controledlist.set(addIndex,fadeAnimationInformation);
        }
	}
	
	/**
	 * Controller의 FadeAnimationView를 삭제한다.
	 * @param view Controller에서 삭제할 View
	 */
	public void deleteControlView(View view)
	{
		int deleteIndex = getViewIndexInControledlist(view.getId());
		
		if(deleteIndex != -1)
		{
			controledlist.remove(deleteIndex);
		}
	}
	
	/**
	 * Controller를 unRegister 한다.
	 */
	public void unRegisterController(){
		mFadeControllHandler.removeCallbacksAndMessages(null);
		controledlist.clear();
	}
	
	/**
	 * 해당 뷰를 애니메이션 동작을 시킨다.
	 * @param view 애니메이션 동작을 시킬 뷰
	 * @param type FADE_IN : FADE_IN Animation , FADE_OUT : FADE_OUT Animtion
	 */
	public void startAnimation(View view , int type)
	{
		startAnimation(view , type, false);
	}

	public void startAnimation(View view, Animation animation, int type, boolean isAnimationClear)
	{
		int resultIndex = getViewIndexInControledlist(view.getId());
		controledlist.get(resultIndex).setAnimationing(true);
		promptAnimation(view.getId(), animation, type, isAnimationClear);
	}
	
	/**
	 * 해당 뷰를 애니메이션 동작을 시킨다.
	 * @param view 애니메이션 동작을 시킬 뷰
	 * @param type FADE_IN : FADE_IN Animation , FADE_OUT : FADE_OUT Animtion
	 * @param autoFadeOut 자동 FadeOut을 할지 여부를 결정
	 */
	public void startAnimation(View view, int type ,boolean autoFadeOut)
	{
		int resultIndex = getViewIndexInControledlist(view.getId());
		controledlist.get(resultIndex).setAnimationing(true);

		if(autoFadeOut == true)
		{

			controledlist.get(resultIndex).setAutoFadeOut(true);
			Message msg = Message.obtain();
			msg.what = MESSAGE_DELAY_ANIMATION;
			msg.obj = view.getId();
			mFadeControllHandler.sendMessage(msg);
		}
		else
		{
			controledlist.get(resultIndex).setAutoFadeOut(false);
			Message msg = Message.obtain();
			msg.what = MESSAGE_PROMPT_ANIMATION;
			msg.obj = view.getId();
			msg.arg1 = type;
			mFadeControllHandler.sendMessage(msg);
		}
	}
	
	public void promptViewStatus(View view , boolean isVisible)
	{
		int resultIndex = getViewIndexInControledlist(view.getId());
		
		if(isVisible)
		{
			controledlist.get(resultIndex).getView().setVisibility(View.VISIBLE);
		}
		else
		{
			controledlist.get(resultIndex).getView().setVisibility(View.GONE);
		}
		
	}
	
	/**
	 * 애니메이션 동작을 중지 시킨다.
	 */
	public void removeAnimation()
	{
		mFadeControllHandler.removeMessages(MESSAGE_PROMPT_ANIMATION);
	}

}
