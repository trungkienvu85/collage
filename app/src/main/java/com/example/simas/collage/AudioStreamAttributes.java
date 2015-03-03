package com.example.simas.collage;

import android.util.Log;

/**
 * Created by Simas Abramovas on 2015 Mar 02.
 */

// ToDo language attribute

public class AudioStreamAttributes extends StreamAttributes {

	private Integer mChannelCount, mSampleRate;

	private AudioStreamAttributes() { /* Prevent default construction */ }

	public Integer getChannelCount() {
		return mChannelCount;
	}
	public Integer getSampleRate() {
		return mSampleRate;
	}

	public static class Builder {

		private AudioStreamAttributes mAsa = new AudioStreamAttributes();

		public Builder setCodecName(String name) {
			mAsa.mCodecName = name;
			return this;
		}

		public Builder setCodecLongName(String longName) {
			mAsa.mCodecLongName = longName;
			return this;
		}

		public Builder setDuration(Double duration) {
			mAsa.mDuration = duration;
			return this;
		}

		protected Builder setChannelCount(Integer channelCount) {
			mAsa.mChannelCount = channelCount;
			return this;
		}

		protected Builder setSampleRate(Integer sampleRate) {
			mAsa.mSampleRate = sampleRate;
			return this;
		}

		public AudioStreamAttributes build() {
			return mAsa;
		}

	}

}
