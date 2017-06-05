package com.littlefox.library.view.media;

public class ProgressiveMediaInformation
{
	
	public static final int MEGA_BYTE 	= 1024 * 1024 * 1;
	public static final int PLAY_AVAILABLE_BYTE = MEGA_BYTE * 1;
	public static final int SECOND		= 1000;
	
	public static final int ERROR_MESSAGE_MEDIAPLAYER_ERROR = 0;
	public static final int ERROR_MESSAGE_NETWORK			= 1;
	public static final int ERROR_MESSAGE_DOWNLOAD			= 2;
	public static final int ERROR_MESSAGE_FILE_ERROR		= 3;
	
	public static final int MESSAGE_MEDIA_PLAY_START		= 0;
	
	public static String BASE_FILE_PATH = "";
	public static final String FILE_INFORMATION = "file_information.txt";
	
	public static final String PARAMS_CURRENT_PLAY_POSITION = "current_play_position";

}
