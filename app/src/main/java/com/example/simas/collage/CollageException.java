package com.example.simas.collage;

/**
 * Created by Simas Abramovas on 2015 Mar 02.
 */

public class CollageException extends Exception {

	private String mMessage;
	private String mExtra;

	/**
	 * Default CollageException
	 * @param message    message that will be shown to the user
	 */
	public CollageException(String message) {
		mMessage = message;
	}

	/**
	 * CollageException with a extra information that will be written to the log
	 * @param message    message that will be shown to the user
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

	/**
	 * Returns the extra information (if set)
	 * @return returns the extra information. If it's not set, returns "unspecified".
	 */
	public String getExtra() {
		return (mExtra == null) ? "unspecified" : mExtra;
	}

}
