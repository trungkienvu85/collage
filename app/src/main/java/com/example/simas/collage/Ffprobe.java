package com.example.simas.collage;

import android.content.Context;

import java.io.IOException;

/**
 * Created by Simas Abramovas on 2015 Feb 28.
 */
public class Ffprobe extends Executable {

	private static final String TAG = "FFprobe";
	private static final String NAME = "ffprobe";

	/**
	 * @param ctx
	 */
	public Ffprobe(Context ctx) throws IOException {
		super(ctx, NAME);
	}

	public Size getSize() {
		int width = 0, height = 10;
		return new Size(width, height);
	}

	public final class Size {
		int width, height;

		public Size(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}

}
