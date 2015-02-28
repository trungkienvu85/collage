package com.example.simas.collage;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import java.io.File;
import java.io.IOException;

/**
 * Created by Simas Abramovas on 2015 Feb 28.
 */

public abstract class Executable {

	private final Context mContext;
	private final String mExecPath; // ToDo rename final?
	private final File mAppDir;
	private final String mName;

	/**
	 *
	 * @param ctx
	 * @param name    the name of the asset file
	 */
	protected Executable(Context ctx, String name) throws IOException {
		mContext = ctx;
		mAppDir = new File(getContext().getApplicationInfo().dataDir);
		mName = name;
		mExecPath = mAppDir + File.separator + mName;
		File exec = new File(getPath());
		if (!exec.exists()) {
			install();
		}
	}

	private void install() throws IOException {
		File exec = new File(getPath());

		AssetFileDescriptor afd = getContext().getAssets().openFd(getName());

		// Create parent dir if doesn't exist
		if (!getParentDir().exists()) {
			if (!getParentDir().mkdirs()) {
				throw new IllegalStateException("Data directory must exist, or be creatable!");
			}
		}

		// Check if there's enough space
		long freeSpace = getParentDir().getFreeSpace() / 1024 / 1024;	// in Mb
		long assetSize = afd.getLength() / 1024 / 1024;			        // in Mb
		if (assetSize <= 0 && freeSpace <= 0 && freeSpace <= assetSize) {
			throw new IllegalStateException("Data directory doesn't have enough space!");
		}

		if (!exec.createNewFile()) {
			throw new IOException("Executable file creation failed!");
		}
		Utils.copyAsset(getContext(), getName(), getPath());

		if (!exec.setExecutable(true)) {
			throw new IOException("Marking file as executing failed!");
		}
	}

	public final String getName() {
		return mName;
	}

	protected final String getPath() {
		return mExecPath;
	}

	protected final File getParentDir() {
		return mAppDir;
	}

	protected final Context getContext() {
		return mContext;
	}

}
