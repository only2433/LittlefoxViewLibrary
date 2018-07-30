package com.littlefox.library.view.text;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 한 텍스트에 두개의 다른 색이나 다른 크기의 Text를 보여주기 위한 Custom Text
 * @author 정재현
 *
 */
public class SeparateTextView extends TextView
{
	private String mFirstString = "";
	private String mSecondString = "";
	private Context mContext;
	private  SpannableStringBuilder mSpannableStringBuilder;
	public SeparateTextView(Context context)
	{
		super(context);
		init(context);
	}
	
	public SeparateTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}
	
	public SeparateTextView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init(context);
	}
	
	private void init(Context context)
	{
		mContext = context;
		mFirstString = "";
		mSecondString = "";
	}
	
	/**
	 * 바꿀 텍스트를 설정
	 * @param firstText 처음 텍스트 
	 * @param secondText 다른 Style을 적용할 텍스트
	 * @return
	 */
	public SeparateTextView setSeparateText(String firstText , String secondText)
	{
		mFirstString = firstText;
		mSecondString = secondText;
		mSpannableStringBuilder = new SpannableStringBuilder(mFirstString + mSecondString);
		return this;
	}
	
	/**
	 * 텍스트에 각각 다른 컬러를 적용
	 * @param firstColor 1번 컬러
	 * @param secondColor 2번 컬러
	 * @return 만약 텍스트를 세팅하지 않았다면 NullPointerException 을 리턴
	 */
	public SeparateTextView setSeparateColor(int firstColor, int secondColor)
	{
	
		if(mFirstString.equals("") || mSecondString.equals(""))
		{
			throw new NullPointerException();
		}
		mSpannableStringBuilder.setSpan(new ForegroundColorSpan(firstColor) , 0, mFirstString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mSpannableStringBuilder.setSpan(new ForegroundColorSpan(secondColor), mFirstString.length(),  mFirstString.length()+mSecondString.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * 텍스트에 각각 다른 사이즈를 적용
	 * @param firstTextSize 1번 사이즈
	 * @param secondTextSize 2번 사이즈
	 * @return 만약 텍스트를 세팅하지 않았다면 NullPointerException 을 리턴
	 */
	public SeparateTextView setSeparateTextSize(int firstTextSize , int secondTextSize)
	{
		if(mFirstString.equals("") || mSecondString.equals(""))
		{
			throw new NullPointerException();
		}
		mSpannableStringBuilder.setSpan(new AbsoluteSizeSpan(firstTextSize),  0, mFirstString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mSpannableStringBuilder.setSpan(new AbsoluteSizeSpan(secondTextSize), mFirstString.length()  , mFirstString.length()+mSecondString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	/**
	 * 텍스트를 각각 원하는 스타일 폰트로 변경
	 * @param firstFontStyle 첫번째 텍스트의 폰트 스타일
	 * @param secondFontStyle 두번째 텍스트의 폰트 스타일
	 * @return
	 */
	public SeparateTextView setSeparateTextStyle(int firstFontStyle, int secondFontStyle)
	{
		if(mFirstString.equals("") || mSecondString.equals(""))
		{
			throw new NullPointerException();
		}

		mSpannableStringBuilder.setSpan(new StyleSpan(firstFontStyle),  0, mFirstString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		mSpannableStringBuilder.setSpan(new StyleSpan(secondFontStyle), mFirstString.length()  , mFirstString.length()+mSecondString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return this;
	}

	/**
	 * 텍스트 설정을 다 한다음 호출
	 */
	public void showView()
	{
		this.setText(mSpannableStringBuilder);
		
	}
}
