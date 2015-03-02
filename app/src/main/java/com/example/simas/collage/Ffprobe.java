package com.example.simas.collage;

import android.content.Context;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simas Abramovas on 2015 Feb 28.
 */

public class Ffprobe extends Executable {

	private static final String TAG = "FFprobe";
	private static final String NAME = "ffprobe";

	/**
	 * @param ctx
	 */
	public Ffprobe(Context ctx) throws IOException, CollageException {
		super(ctx, NAME);
	}

	public Attributes getVideoData() {
		preventWorkOnUiThread();

		// ToDo invoke exec call
			// $ ./ffprobe -v quiet -print_format json -show_format -show_streams -i 1.mp4

		// ToDo parse JSON

		//

		// Create streams attributes
		VideoStreamAttributes videoAttributes = new VideoStreamAttributes.Builder()
				.setCodecName("h264")
				.setCodecLongName("H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10")
				.setCodecType(StreamAttributes.TYPE_VIDEO)
				.setSize(1024, 768)
				.setAspectRatio("16:9")
				.build();

		AudioStreamAttributes audioAttributes = new AudioStreamAttributes.Builder()
				.setCodecName("aac")
				.setCodecLongName("AAC (Advanced Audio Coding)")
				.setCodecType(StreamAttributes.TYPE_AUDIO)
				.setChannelCount(1)
				.build();

		// Create file attributes

		Attributes attributes = new Attributes();
		attributes.length = 100.0;
		attributes.streams.add(videoAttributes);
		attributes.streams.add(audioAttributes);

		return new Attributes();
	}


	// Attribute class for files
	public static class Attributes {

		public double length;
		public List<StreamAttributes> streams = new ArrayList<>();

		private Attributes() { /* Prevent default construction */ }

	}



}
