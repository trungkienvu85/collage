package com.example.simas.collage;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simas Abramovas on 2015 Feb 28.
 */

// ToDo async class or not? i.e. where should synchronizing be done at? here or at the app level?
	// I'd say here.
// ToDo kur daryt error checkinga? FFmpeg'e ar MainActivity?
	// Solvable errors, e.g. concat failed, should try another method - should be done here
	// If all else fails just throw it back onto the activity for the user to see.

public class Ffmpeg extends Executable {

	private static final String TAG = "FFmpeg";
	private static final String NAME = "ffmpeg";

	/**
	 * @param ctx
	 */
	public Ffmpeg(Context ctx) throws IOException {
		super(ctx, NAME);
	}

	/**
	 *
	 * @param output     output file
	 * @param sources    absolute path filenames to source videos that will be merged
	 * @return
	 * @throws IOException
	 */
	public int concat(File output, String... sources)
			throws IOException, InterruptedException, IllegalStateException {
		// Prepare a temporary file containing source video list
		File tmpFile = File.createTempFile("collage", null); // empty collage.tmp is created
		if (sources.length < 2) {
			throw new IllegalStateException("Must provide at least 2 videos to concat!");
		}
		String sourceList = "";
		for (String source : sources) {
			sourceList += String.format("file '%s'\n", source);
		}
		Utils.copyBytes(sourceList.getBytes(), tmpFile);

		List<String> args = new ArrayList<>();
		// Executable
		args.add(getPath());
		// Concat given files
		args.add(String.format("concat -i %s", tmpFile.getPath()));
		// Copy source codecs
		args.add("-c copy");
		// Output file
		args.add(String.format("-f %s", output.getPath()));

		// Build and start the process
		ProcessBuilder pb = new ProcessBuilder(args);
		Process p = pb.start();
		p.waitFor();

		return p.exitValue();
	}

}

