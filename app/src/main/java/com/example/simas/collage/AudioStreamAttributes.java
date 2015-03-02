package com.example.simas.collage;

import android.util.Log;

/**
 * Created by Simas Abramovas on 2015 Mar 02.
 */
public class AudioStreamAttributes extends StreamAttributes {

	private int mChannelCount;

	private AudioStreamAttributes() { /* Prevent default construction */ }

	public int getChannelCount() {
		return mChannelCount;
	}

	public static class Builder {

		private AudioStreamAttributes mAsa = new AudioStreamAttributes();

		public Builder setCodecType(int type) {
			mAsa.mCodecType = type;
			return this;
		}

		public Builder setCodecName(String name) {
			mAsa.mCodecName = name;
			return this;
		}

		public Builder setCodecLongName(String longName) {
			mAsa.mCodecLongName = longName;
			return this;
		}

		protected Builder setChannelCount(int channelCount) {
			mAsa.mChannelCount = channelCount;
			return this;
		}

		public AudioStreamAttributes build() {
			return mAsa;
		}

	}

}
