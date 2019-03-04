package com.littlefox.library.view.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.littlefox.library.common.CommonUtils;
import com.littlefox.library.system.common.FileUtils;
import com.littlefox.logmonitor.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Timer;

public class ProgressiveMediaPlayer extends SurfaceView implements SurfaceHolder.Callback
{
	public static final int STATUS_START 	= 0x00;
	public static final int STATUS_RESUME 	= 0x01;
	public static final int STATUS_PAUSE 	= 0x02;
	public static final int STATUS_STOP 	= 0x03;
	
	private static final int MESSAGE_PLAY_VIDEO = 0;
	private Context mContext;
	private SurfaceHolder mSurfaceHolder;
	private MediaPlayer mMediaPlayer;
	
	private int mMaxSeekProgress 			= 0;
	private int mCurrentDownloadProgress 	= 0;

	private String mDownloadUrl ="";
	private String mSavePath ="";
	private MediaDownloadAsync mMediaDownloadAsync;
	private ProgressiveMediaListener mProgressiveMediaListener;
	private Timer mPlayTimer = null;
	
	private boolean isPlayAvailable = false;
	private boolean isDownloadComplete = false;
	
	private long mPreiviewPlayTime 	= -1;
	private float mCurrentDownloadTime = -1;
	private long mMaxPlayTime		= -1;

	
	private int mCurrentMediaPlayerStatus = -1;
	private int mCurrentSeekTime 			= -1;
	
	private Handler mPlayerHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case MESSAGE_PLAY_VIDEO:
				startVideo();
				break;
			}
		}
		
	};
	
	public ProgressiveMediaPlayer(Context context)
	{
		super(context);
		mContext = context;
	}
	
	public ProgressiveMediaPlayer(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
	}
	
	public ProgressiveMediaPlayer(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mContext = context;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		Log.i("");
		mSurfaceHolder = holder;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		Log.i("");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		Log.i("");
	}
	
	private void init()
	{
		Log.i("");
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		setupMediaPlayerListener();
	}
	
	private void setupMediaPlayerListener()
	{
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener()
		{
			
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent)
			{
				
			}
		});
		mMediaPlayer.setOnErrorListener(new OnErrorListener()
		{
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra)
			{
				mProgressiveMediaListener.onError(ProgressiveMediaInformation.ERROR_MESSAGE_MEDIAPLAYER_ERROR);
				return false;
			}
		});
		
		mMediaPlayer.setOnPreparedListener(new OnPreparedListener()
		{
			
			@Override
			public void onPrepared(MediaPlayer mp)
			{
				Log.i("mp currentDuration : "+ mp.getCurrentPosition() + ", Duration : "+ mp.getDuration());
				setSurfaceBackground(null);
				
				play();
			}
		});
		
		mMediaPlayer.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener()
		{
			
			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height)
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener()
		{
			
			@Override
			public void onCompletion(MediaPlayer mp)
			{
				Log.i("mMediaPlayer.isPlaying() : "+mMediaPlayer.isPlaying()+", status : "+ getMediaPlayerStatus());
				mProgressiveMediaListener.onPlayComplete();
				
			}
		});
	}
	
	private void setSurfaceBackground(Drawable dw)
	{
		this.setBackgroundDrawable(dw);
	}

	public void release()
	{
		CommonUtils.setSharedPreference(mContext, ProgressiveMediaInformation.PARAMS_CURRENT_PLAY_POSITION, -1);
		if(mMediaDownloadAsync != null)
		{
			mMediaDownloadAsync.setCancel(true);
			mMediaDownloadAsync.cancel(true);
		}
		
		if(mMediaPlayer != null)
		{
			mMediaPlayer.pause();
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
	
	public void reset()
	{
		if(mMediaDownloadAsync != null)
		{
			mMediaDownloadAsync.setCancel(true);
			mMediaDownloadAsync.cancel(true);
		}
		
		if(mMediaPlayer != null && mMediaPlayer.isPlaying())
		{
			mMediaPlayer.pause();
		}
	}
	
	private void startVideo()
	{
		Log.i("getStatus : "+ getMediaPlayerStatus()+", mSavePath : "+mSavePath);
		if(mMediaPlayer != null)
		{
			mMediaPlayer.reset();
		}
		else
		{
			mMediaPlayer = new MediaPlayer();
		}
		
		FileInputStream fis;
		try
		{
			fis = new FileInputStream(mSavePath);
			FileDescriptor fd = fis.getFD();
			mMediaPlayer.setDataSource(fd);
			fis.close();
			mMediaPlayer.setDisplay(mSurfaceHolder);
			mMediaPlayer.prepareAsync();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 프리뷰 시간을 넘지않을때 까지 다운로드를 받기 위한 메소드. 프리뷰 시간이 넘었는지 넘지 않았는 지 체크
	 * @param currentFileSize 현재 다운로드 된 파일 사이즈
	 * @param maxFileSize 총 파일 사이즈
	 * @return TRUE : 프리뷰 시간이 넘지않아서 파일 다운로드 가능 </p> FALSE : 프리뷰 시간이 넘어 파일로드 불가능
	 */
	private boolean isPreviewDownloadAvailable(long currentFileSize, long maxFileSize)
	{
		boolean result = false;
		
		mCurrentDownloadTime = (float)(currentFileSize * mMaxPlayTime)/(float)maxFileSize;
		
		if(mPreiviewPlayTime >= mCurrentDownloadTime)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 홈이나 화면을 껏을때는 이어서 플레이를 하고, 아닐시에는 처음부터 플레이를 한다.
	 */
	private void play()
	{
		if(mMediaPlayer != null && mMediaPlayer.isPlaying() == false)
		{
			int currentDuration = (Integer) CommonUtils.getSharedPreference(mContext, ProgressiveMediaInformation.PARAMS_CURRENT_PLAY_POSITION, CommonUtils.TYPE_PARAMS_INTEGER);
			Log.i("currentDuration : "+currentDuration);
			if(currentDuration > 0)
			{
				mMediaPlayer.seekTo(currentDuration);
				mProgressiveMediaListener.onPlayResume();
			}
			else
			{
				mProgressiveMediaListener.onPlayStart();
			}
			mMediaPlayer.start();
			
		}
	}
	
	/**
	 * 프리뷰 플레이 될 영상이 다운로드가 끝났는 지 아닌지 체크
	 * @return TRUE : 다운로드 컴플리트 , FALSE : 다운로드 진행중
	 */
	public boolean isPreviewDownloadComplete()
	{
		Log.i("mPreiviewPlayTime : "+mPreiviewPlayTime+", mCurrentDownloadTime : "+mCurrentDownloadTime);
		if(mPreiviewPlayTime <= mCurrentDownloadTime)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * URL에 있는 영상을 다운로드 받으면서 영상을 플레이한다. 영상을 다 받앗을 시에는 로컬에 있는 영상을 플레이 한다.
	 * @param url 영상이 있는 URL
	 */
	public void startPlay(String uniqueID, String url, boolean isDownloadComplete)
	{
		this.isDownloadComplete = isDownloadComplete;
		Log.i("uniqueID : "+uniqueID+", url : "+url);
		CommonUtils.setSharedPreference(mContext, ProgressiveMediaInformation.PARAMS_CURRENT_PLAY_POSITION, -1);
		mDownloadUrl = url;
		mSavePath = ProgressiveMediaInformation.BASE_FILE_PATH + uniqueID;
		mMaxSeekProgress = -1;
		
		
		if(mMediaDownloadAsync != null)
		{
			mMediaDownloadAsync.cancel(true);
		}
		
		mMediaDownloadAsync = new MediaDownloadAsync();
		mMediaDownloadAsync.execute();
	}
	
	/**
	 * 특정위치로 미디어 재생위치를 변경한다. 단, 현재 다운로드가 다 안된곳으로 이동시 다운로드 된 이전까지만 미디어 재생위치를 바꿀 수 있다.
	 * @param progress 이동시킬 위치
	 */
	public void seekToMediaPlay(int progress)
	{
		Log.f("isDownloadComplete : "+ isDownloadComplete());
		Log.f("progress : "+ progress +", mCurrentDownloadProgress : "+mCurrentDownloadProgress);
		if(isDownloadComplete() == false && (progress > mCurrentDownloadProgress - ProgressiveMediaInformation.SECOND))
		{
			int mediaCurrentPosition = mMediaPlayer.getDuration() / ProgressiveMediaInformation.SECOND;
			int position = ((mCurrentDownloadProgress - ProgressiveMediaInformation.SECOND) * mediaCurrentPosition) / mMaxSeekProgress;
			
			Log.f("mediaCurrentPosition : "+mediaCurrentPosition+", position : "+position);
			mCurrentSeekTime = position * ProgressiveMediaInformation.SECOND;
			mMediaPlayer.seekTo(mCurrentSeekTime);
			
			mProgressiveMediaListener.onSeekComplete(getMediaPlayProgress());
		}
		else
		{
			int mediaCurrentPosition = mMediaPlayer.getDuration() / ProgressiveMediaInformation.SECOND;
			int position = (progress * mediaCurrentPosition) / mMaxSeekProgress;
			
			Log.f("position : "+position);
			mCurrentSeekTime = position * ProgressiveMediaInformation.SECOND;
			mMediaPlayer.seekTo(mCurrentSeekTime);
			mProgressiveMediaListener.onSeekComplete(getMediaPlayProgress());
		}
		
		CommonUtils.setSharedPreference(mContext, ProgressiveMediaInformation.PARAMS_CURRENT_PLAY_POSITION, mCurrentSeekTime);
	}
	
	/**
	 * 해당 루틴은 플레이가 가능한 상태까지 다운로드가 되어야지만 True를 리턴하는 메소드
	 * @return TRUE : 플레이가 가능하다 </p> FALSE : 플레이가 불가능 한 상태 (다운로드가 플레이가 가능할 정도가 되지 않았다)
	 */
	public boolean isPlayAvailable()
	{
		final int TERM_SECOND = (int)(mMaxSeekProgress * 0.05);
		
		try
		{
			int position =  (int) (mMaxSeekProgress * (mMediaPlayer.getCurrentPosition() / ProgressiveMediaInformation.SECOND)) / (mMediaPlayer.getDuration() / ProgressiveMediaInformation.SECOND);
			if(isDownloadComplete() == false && mCurrentDownloadProgress <= position + TERM_SECOND)
			{
				return false;
			}
		}catch(Exception e)
		{
			
		}
		
		
		return true;
	}
	
	public int getMediaPlayProgress(int seekPosition)
	{
		int position =  (int) (mMaxSeekProgress * (seekPosition / ProgressiveMediaInformation.SECOND)) / (mMediaPlayer.getDuration() / ProgressiveMediaInformation.SECOND);
		
		return position;
	}
	
	public int getMediaPlayProgress()
	{
		int position =  (int) (mMaxSeekProgress * (mMediaPlayer.getCurrentPosition() / ProgressiveMediaInformation.SECOND)) / (mMediaPlayer.getDuration() / ProgressiveMediaInformation.SECOND);
		
		return position;
	}
	
	public int getMediaPlayerStatus()
	{
		return mCurrentMediaPlayerStatus;
	}
	
	public void stop()
	{
		if(mMediaPlayer != null)
		{
			Log.i("");
			mCurrentMediaPlayerStatus = STATUS_STOP;
			CommonUtils.setSharedPreference(mContext, ProgressiveMediaInformation.PARAMS_CURRENT_PLAY_POSITION, mMediaPlayer.getCurrentPosition());
			
			mMediaPlayer.pause();
			mMediaPlayer.stop();
			Log.i("mCurrentPlayPosition : "+CommonUtils.getSharedPreference(mContext, ProgressiveMediaInformation.PARAMS_CURRENT_PLAY_POSITION, CommonUtils.TYPE_PARAMS_INTEGER));
		}
	}
	
	/**
	 * 플레이어를 세팅을 하고 플레이를 시작한다.
	 */
	public void start()
	{
		if(mMediaPlayer != null && mMediaPlayer.isPlaying() == false)
		{
			Log.i("");
			mCurrentMediaPlayerStatus = STATUS_START;
			mPlayerHandler.sendEmptyMessage(MESSAGE_PLAY_VIDEO);
		}
	}
	
	public void pause()
	{
		if(mMediaPlayer != null)
		{
			Log.i("");
			mCurrentMediaPlayerStatus = STATUS_PAUSE;
			mMediaPlayer.pause();
		}
	}
	
	public void resume()
	{
		if(mMediaPlayer != null)
		{
			Log.i("");
			mCurrentMediaPlayerStatus = STATUS_RESUME;
			mMediaPlayer.start();
		}
	}
	
	/**
	 * 프리뷰 플레이할 시간을 세팅한다.
	 * @param previewPlayTime 보여줄 프리뷰 시간
	 * @param maxPlayTime 현재 이 파일의 플레이 총 시간
	 */
	public void setPreviewPlayTime(long previewPlayTime, long maxPlayTime)
	{
		this.mPreiviewPlayTime 	= previewPlayTime;
		this.mMaxPlayTime		= maxPlayTime;	
	}
	
	/**
	 * 모듈을 등록한다. 액티비티 시작 시 등록. 파일 저장할 Path 와 액티비티가 정보를 받을 Listener를 등록한다. 
	 * @param basePath 파일 저장할 Path
	 * @param progressiveMediaListener 액티비티가 정보를 받을 Listener
	 */
	public void register(String basePath, ProgressiveMediaListener progressiveMediaListener)
	{
		ProgressiveMediaInformation.BASE_FILE_PATH = basePath;
		mProgressiveMediaListener = progressiveMediaListener;
		init();
	}
	
	

	
	public String getCurrentFileSavePath()
	{
		return mSavePath;
	}
	
	/**
	 * 저장한 MP4 파일을 전부 삭제한다.
	 */
	public void destroyAllSaveInformation()
	{
		FileUtils.DeleteAllWithSuffix(new File(ProgressiveMediaInformation.BASE_FILE_PATH), "mp4");
	}
	
	public boolean isVideoPlaying()
	{
		if(mMediaPlayer != null)
		{
			return mMediaPlayer.isPlaying();
		}
		
		return false;
	}
	
	
	public int getSeekMaxProgress()
	{
		return mMaxSeekProgress;
	
	}
	public boolean isDownloadComplete()
	{
		return isDownloadComplete;
	}
	
	public int getCurrentPlayerDuration()
	{
		if(mMediaPlayer != null)
		{
			return mMediaPlayer.getCurrentPosition();
		}
		return -1;
	}
	
	public int getMaxPlayerDuration()
	{
		if(mMediaPlayer != null)
		{
			return mMediaPlayer.getDuration();
		}
		return -1;
	}
	

	/**
	 * 프로그래시브 다운로드의 핵심 클래스. AsyncTask로 되어 있으며, 프로그래시브 다운로드를 하며 영상플레이를 한다. 현재 다운로드 진행상황을 리스너로 전달한다.
	 * @author 정재현
	 *
	 */
	private class MediaDownloadAsync extends AsyncTask<Void, String, Long>
	{
		private static final long ERROR_MESSAGE_NETWORK = 10000L;
		private boolean isCancel = false;
		public MediaDownloadAsync(){}
		
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			
			if(mProgressiveMediaListener != null)
			{
				mProgressiveMediaListener.onDownloadStart();
			}
		}

		
		@Override
		protected Long doInBackground(Void... params)
		{
			try
			{
				return startDownloadwithPlay(mDownloadUrl, mSavePath);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return ERROR_MESSAGE_NETWORK;
			}
		}

		@Override
		protected void onPostExecute(Long result)
		{
			super.onPostExecute(result);
			
			if(result == ERROR_MESSAGE_NETWORK)
			{
				mProgressiveMediaListener.onError(ProgressiveMediaInformation.ERROR_MESSAGE_NETWORK);
			}
		}

		@Override
		protected void onCancelled()
		{
			super.onCancelled();
		}

		synchronized private long startDownloadwithPlay(String strUrl, String strPath) throws IOException
		{
			Log.i("strUrl : "+strUrl+", strPath : "+strPath);
			int DOWNLOAD_DONE = 500;
			int DEFAULT_TIMEOUT = 50000;
			long fileSize = 0, remains = 0, lengthOfFile = 0;
			boolean isProgressDownloadStart = false;

			File file = new File(strPath);

			File fileParent = new File(file.getParent());
			
			Log.i("fileParent path : "+ fileParent.getAbsolutePath()+", exist : "+fileParent.exists());
			if(fileParent.exists() == false)
			{
				fileParent.mkdirs();
			}
			
			if (file.exists() == false)
			{
				file.createNewFile();
			}

			RandomAccessFile moutput = new RandomAccessFile(file.getAbsolutePath(), "rw");
			FileChannel fc = moutput.getChannel();
			fileSize = file.length();

			if (fileSize > ProgressiveMediaInformation.PLAY_AVAILABLE_BYTE && isDownloadComplete == false)
			{
				byte[] newSearchData = new byte[4];
				MappedByteBuffer MappByteBuff = fc.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
				MappByteBuff.position((int) (fileSize - newSearchData.length));
				MappByteBuff.get(newSearchData, 0, newSearchData.length);
				
				fileSize = getIntegerByByte(newSearchData);
				
				Log.f("Searched complete Saved File Size : " + fileSize);
				
			}

			URL url = new URL(strUrl);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Range", "bytes=" + String.valueOf(fileSize) + '-');
			conn.connect();
			conn.setConnectTimeout(DEFAULT_TIMEOUT);
			conn.setReadTimeout(DEFAULT_TIMEOUT);
			
			remains = conn.getContentLength();
			lengthOfFile = remains + fileSize;
			Log.f("isDownloadComplete : " +isDownloadComplete+", remains : "+remains+", fileSize : "+fileSize);
			mMaxSeekProgress = (int) (lengthOfFile / 1000);
			mProgressiveMediaListener.onSetSeekBarMaxProgress(mMaxSeekProgress);


			if ((remains <= DOWNLOAD_DONE) || (remains == fileSize))
			{
				Log.i("");
				isDownloadComplete = true;				
			}
			
			if (isDownloadComplete)
			{
				Log.i("");		
				mProgressiveMediaListener.onDownloadComplete(mMaxSeekProgress);
				mPlayerHandler.sendEmptyMessage(MESSAGE_PLAY_VIDEO);
				moutput.close();
				fc.close();
				return -1; // 다운로드 필요 없음
			}
			
			if(mPreiviewPlayTime != -1 && isPreviewDownloadAvailable(fileSize - ProgressiveMediaInformation.PLAY_AVAILABLE_BYTE, lengthOfFile) == false)
			{
				mCurrentDownloadProgress = (int) ((fileSize - ProgressiveMediaInformation.MEGA_BYTE) / ProgressiveMediaInformation.SECOND);
				
				mProgressiveMediaListener.onDownloadComplete(mCurrentDownloadProgress);
				mPlayerHandler.sendEmptyMessage(MESSAGE_PLAY_VIDEO);
				moutput.close();
				fc.close();
				return -1; // 다운로드 필요 없음
			}

			byte data1[] = new byte[1024 * 2];
			Arrays.fill(data1, (byte) '1');
			int dummy = data1.length, dummyRemains = (int) remains;
			moutput.seek(0);
			while (dummyRemains > 0 && fileSize == 0)
			{

				if (dummyRemains <= dummy)
				{
					dummy = dummyRemains;
				}
				moutput.write(data1, 0, dummy);
				dummyRemains = dummyRemains - dummy;
			}

			InputStream input = conn.getInputStream();
			byte data[] = new byte[1024 * 2];
			int count = 0;

			int mode = 0;
			moutput.seek(fileSize);
			while ((count = input.read(data)) != -1)
			{
				if(isCancel == true)
				{
					Log.i("");
					break;
				}
				
				if(mPreiviewPlayTime != -1 && isPreviewDownloadAvailable(fileSize - ProgressiveMediaInformation.PLAY_AVAILABLE_BYTE, lengthOfFile) == false)
				{
					Log.i("Download End!!!");
					mCurrentDownloadProgress = (int) ((fileSize - ProgressiveMediaInformation.MEGA_BYTE) / ProgressiveMediaInformation.SECOND);
					
					mProgressiveMediaListener.onDownloadComplete(mCurrentDownloadProgress);
					break;
				}
				
				moutput.write(data, 0, count);
				fileSize = count + fileSize;
				if (fileSize > ProgressiveMediaInformation.PLAY_AVAILABLE_BYTE && isProgressDownloadStart == false)
				{
					isProgressDownloadStart = true;
					mPlayerHandler.sendEmptyMessage(MESSAGE_PLAY_VIDEO);
				}

				// nCurPosition
				if (fileSize > ProgressiveMediaInformation.MEGA_BYTE && fileSize < lengthOfFile - ProgressiveMediaInformation.MEGA_BYTE)
				{
					mCurrentDownloadProgress = (int) ((fileSize - ProgressiveMediaInformation.MEGA_BYTE) / ProgressiveMediaInformation.SECOND);
					mProgressiveMediaListener.onDownloadProgress(mCurrentDownloadProgress);
				}
				else if (fileSize > lengthOfFile - ProgressiveMediaInformation.MEGA_BYTE)
				{
					mCurrentDownloadProgress = (int) (fileSize / ProgressiveMediaInformation.SECOND);
					mProgressiveMediaListener.onDownloadComplete(mCurrentDownloadProgress);
				}

			}
			Log.i("fileSize : "+fileSize+", lengthOfFile : "+lengthOfFile + ", isDownloadComplete : "+isDownloadComplete);
			
			
			if(fileSize >= lengthOfFile)
			{
				isDownloadComplete = true;
				mProgressiveMediaListener.onFullDownloadComplete();
			}
			else
			{
				byte[] saveSearchData = new byte[4];
				convertByteByInteger((int)fileSize, saveSearchData);
				
				moutput.seek(lengthOfFile - saveSearchData.length);
				moutput.write(saveSearchData, 0, saveSearchData.length);
				
				Log.f("saved download File Size : "+fileSize);
			}
			
			moutput.close();
			input.close();
			fc.close();
			return fileSize;

		}
		
		
		/**
		 * AsyncTask가 바로 종료가 되지않아 액티비티가 종료되도 파일을 받는 현상이 있어서 그 부분을 해결하기위해 선언
		 * @param isCancel TRUE : AsyncTask 종료  </p> FALSE : AsyncTask not 종료
		 */
		public void setCancel(boolean isCancel)
		{
			this.isCancel = isCancel;
		}
		
		/**
		 * Byte 를 Integer 로 변경해서 가져옴
		 * @param source 바이트 배열
		 * @return 변경된 Int 값
		 */
		public int getIntegerByByte(byte[] source)
		{
			int source1 = source[0] & 0xFF;
			int source2 = source[1] & 0xFF;
			int source3 = source[2] & 0xFF;
			int source4 = source[3] & 0xFF;
			
			return ((source1 << 24) + (source2 << 16) + (source3 << 8) + (source4 << 0));
		}

		/**
		 * Integer 를 바이트 배열로 변경해준다.
		 * @param source 값
		 * @param result 결과의 배열
		 */
		public void convertByteByInteger(int source, byte[] result)
		{
			result[0] |= (byte)((source & 0xFF000000)>>24);
			result[1] |= (byte)((source & 0xFF0000)>>16);
			result[2] |= (byte)((source & 0xFF00)>>8);
			result[3] |= (byte)((source & 0xFF));
		}
		
	}
	
	
}
