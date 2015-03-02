package com.example.simas.collage;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simas Abramovas on 2015 Feb 28.
 */

public class Ffmpeg extends Executable {

	private static final String TAG = "FFmpeg";
	private static final String NAME = "ffmpeg";

	/**
	 * @param ctx
	 */
	public Ffmpeg(Context ctx) throws IOException, CollageException {
		super(ctx, NAME);
	}

	/**
	 *
	 * @param output     output file (that already exist)
	 * @param sources    absolute path filenames to source videos that will be merged
	 * @return
	 * @throws IOException
	 */
	public int concat(File output, String... sources) throws IOException, CollageException,
			InterruptedException {
		preventWorkOnUiThread();

		if (!output.exists()) {
			// ToDo rodyt useriui kad output failas neegzistuoja? Sita sutvarkyt kai bus UI,
				// tada bus aiskiau kaip parenkami i/o failai
			if (!output.mkdirs() || !output.createNewFile()) {
				throw new IOException("The output file doesn't exist and couldn't be created!");
			}
		}
		// Prepare a temporary file containing source video list
		File tmpFile = File.createTempFile("collage", null); // empty collage.tmp is created
		if (sources.length < 2) {
			throw new CollageException(getString(R.string.at_least_2_videos));
		}
		String sourceList = "";
		for (String source : sources) {
			sourceList += String.format("file '%s'\n", source);
		}
		Utils.copyBytes(sourceList.getBytes(), tmpFile);

		// ToDo check if files: output, sources[], tmpFile exist
			// otherwise prevent non-existent files to be selected via the UI

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

