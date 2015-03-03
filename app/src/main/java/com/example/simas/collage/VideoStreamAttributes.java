package com.example.simas.collage;

/**
 * Created by Simas Abramovas on 2015 Mar 02.
 */
public class VideoStreamAttributes extends StreamAttributes {

	private Integer mWidth, mHeight;
	private String mAspectRatio;

	private VideoStreamAttributes() { /* Prevent default construction */ }

	public Integer getWidth() {
		return mWidth;
	}

	public Integer getHeight() {
		return mHeight;
	}

	public String getAspectRatio() {
		return mAspectRatio;
	}

	public static class Builder {

		private VideoStreamAttributes mVsa = new VideoStreamAttributes();

		public Builder setCodecName(String name) {
			mVsa.mCodecName = name;
			return this;
		}

		public Builder setCodecLongName(String longName) {
			mVsa.mCodecLongName = longName;
			return this;
		}

		public Builder setDuration(Double duration) {
			mVsa.mDuration = duration;
			return this;
		}

		protected Builder setSize(Integer width, Integer height) {
			mVsa.mWidth = width;
			mVsa.mHeight = height;
			return this;
		}

		protected Builder setAspectRatio(String aspectRatio) {
			mVsa.mAspectRatio = aspectRatio;
			return this;
		}

		public VideoStreamAttributes build() {
			return mVsa;
		}

	}

}
