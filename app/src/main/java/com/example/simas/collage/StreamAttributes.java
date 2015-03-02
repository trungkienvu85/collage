package com.example.simas.collage;

import android.util.Log;

/**
 * Created by Simas Abramovas on 2015 Mar 02.
 */
// Attributes for streams
public abstract class StreamAttributes {

	public final static int TYPE_VIDEO = 0;
	public final static int TYPE_AUDIO = 1;

	protected int mCodecType;
	protected String mCodecName;
	protected String mCodecLongName;

	public int getCodecType() {
		return mCodecType;
	}

	public String getCodecName() {
		return mCodecName;
	}

	public String getCodecLongName() {
		return mCodecLongName;
	}

}