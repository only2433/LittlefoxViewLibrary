package com.littlefox.library.view.media;

public interface ProgressiveMediaListener
{
	void onError(int messageType);
	void onSetSeekBarMaxProgress(int progress);

	/**
	 * 플레이를 처음 시작 할 때 호출
	 */
	void onPlayStart();
	
	/**
	 * 홈이나 화면을 껏다가 다시 진입시에 호출
	 */
	void onPlayResume();
	void onPlayComplete();
	void onSeekComplete(int progress);
	void onDownloadStart();
	void onDownloadProgress(int progress);
	void onDownloadComplete(int progress);
	void onFullDownloadComplete();
}
