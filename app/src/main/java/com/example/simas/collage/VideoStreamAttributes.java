package com.example.simas.collage;

/**
 * Created by Simas Abramovas on 2015 Mar 02.
 */
public class VideoStreamAttributes extends StreamAttributes {

	private int mWidth;
	private int mHeight;
	private String mAspectRatio;

	private VideoStreamAttributes() { /* Prevent default construction */ }

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	public String getAspectRatio() {
		return mAspectRatio;
	}

	public static class Builder {

		private VideoStreamAttributes mVsa = new VideoStreamAttributes();

		// ToDo No need for type since there's 2 separate classes. Or are there other types?
			// ’v’ for video, ’a’ for audio, ’s’ for subtitle, ’d’ for data, and ’t’ for attachments
		// ToDo maybe 2 separate classes is an overkill? What about subtitle stream?
		public Builder setCodecType(int type) {
			mVsa.mCodecType = type;
			return this;
		}

		public Builder setCodecName(String name) {
			mVsa.mCodecName = name;
			return this;
		}

		public Builder setCodecLongName(String longName) {
			mVsa.mCodecLongName = longName;
			return this;
		}

		protected Builder setSize(int width, int height) {
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
