package com.example.simas.collage;

/**
 * Created by Simas Abramovas on 2015 Mar 02.
 */
public class CollageException extends Exception {

	private String mMessage;
	private String mExtra;

	public CollageException(String message) {
		mMessage = message;
	}

	/**
	 *
	 * @param message
	 * @param extraInfo    extra information that won't be shown to the user,
	 *                        only written in the log
	 */
	public CollageException(String message, String extraInfo) {
		mMessage = message;
		mExtra = extraInfo;
	}

	@Override
	public String getMessage() {
		return mMessage;
	}

	public String getExtra() {
		return (mExtra == null) ? "unspecified" : mExtra;
	}

}
