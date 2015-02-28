package com.example.simas.collage;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simas Abramovas on 2015 Feb 28.
 */

// ToDo async class or not? i.e. where should synchronizing should be done at? here or at the app level?
public class FFmpeg {

	private static final String TAG = "FFmpeg";
	private static final String EXEC_FILE_NAME = "ffmpeg";

	private final Context mContext;
	private String mExecPath;

	public FFmpeg(Context ctx) throws IOException {
		mContext = ctx;
		installIfNotPresent();
	}

	private void installIfNotPresent() throws IOException {
		File dataDir = new File(getContext().getApplicationInfo().dataDir);
		mExecPath = dataDir + File.separator + EXEC_FILE_NAME;
		File exec = new File(mExecPath);

		// First check if exec already exists
		if (exec.exists()) {
			Log.i(TAG, "FFmpeg already installed in: " + exec.getPath());
			return;
		}

		AssetFileDescriptor afd = getContext().getAssets().openFd(EXEC_FILE_NAME);

		// Create parent dir if doesn't exist
		if (!dataDir.exists()) {
			Log.e(TAG, "Data directory doesn't exist!");
			if (!dataDir.mkdirs()) {
				throw new IllegalStateException("Data directory must exist, or be creatable!");
			}
		}

		// Check if there's enough space
		long freeSpace = dataDir.getFreeSpace() / 1024 / 1024;	// in Mb
		long assetSize = afd.getLength() / 1024 / 1024;			// in Mb
		if (assetSize <= 0 && freeSpace <= 0 && freeSpace <= assetSize) {
			throw new IllegalStateException("Data directory doesn't have enough space!");
			// ToDo throw an app-wide exception and catch in constructor/main
		}

		if (!exec.createNewFile()) {
			throw new IOException("Executable file creation failed!");
		}

		// ToDo rename+change to from asset
		// ToDo error checking? installExecFromAsset turetu throws IOException
		Utils.copyAsset(getContext(), "ffmpeg", mExecPath);

		if (!exec.setExecutable(true)) {
			throw new IOException("Marking file as executing failed!");
		}
	}

	/**
	 *
	 * @param output     output file
	 * @param sources    full path filenames to source videos that will be merged
	 * @return
	 * @throws IOException
	 */
	public int merge(File output, String ...sources) throws IOException, InterruptedException {
		// Prepare a temporary file containing source video list
		File tmpFile = File.createTempFile("collage", null); // empty collage.tmp is created
		if (sources.length < 2) {
			throw new IllegalStateException("Must provide at least 2 videos to merge!");
		}
		String sourceList = "";
		for (String source : sources) {
			sourceList += String.format("file '%s'\n", source);
		}
		Utils.copyBytes(sourceList.getBytes(), tmpFile.getPath()); // ToDo copyBytes() ima File o ne string kad butu taip pat kaip ir merge()

		List<String> args = new ArrayList<>();
		// Executable
		args.add(mExecPath);
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

	private Context getContext() {
		return mContext;
	}

}

