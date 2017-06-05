package com.littlefox.library.view.object;

/**
 * 디스플레이에서 최초에 가져오는 WIDTH , HEIGHT 정보
 * @author 정재현
 *
 */
public class DisPlayMetricsObject
{
	public float widthPixel = 0f;
	public float heightPixel = 0f;
	
	public DisPlayMetricsObject(float width , float height)
	{
		this.widthPixel = width;
		this.heightPixel = height;
	}
}