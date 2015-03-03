package com.example.simas.collage;

import android.util.Log;

/**
 * Created by Simas Abramovas on 2015 Mar 02.
 */

// ToDo probably go for a single class with a proper builder and just nevermind the unused fields

public abstract class StreamAttributes {

	public final static String TYPE_AUDIO = "audio";
	public final static String TYPE_VIDEO = "video";

	protected String mCodecName, mCodecLongName;
	protected Double mDuration;

	public String getCodecName() {
		return mCodecName;
	}

	public String getCodecLongName() {
		return mCodecLongName;
	}

	public Double getDuration() {
		return mDuration;
	}

}