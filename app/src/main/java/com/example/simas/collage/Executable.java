package com.example.simas.collage;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Simas Abramovas on 2015 Feb 28.
 */

public abstract class Executable {

	private static final String TAG = "Executable";
	private final Context mContext;
	private final File mExec;// ToDo rename final?
	private final File mAppDir;
	private final String mName;

	/**
	 *
	 * @param ctx
	 * @param name    the name of the asset file
	 */
	protected Executable(Context ctx, String name)
			throws CollageException, IOException {
		mContext = ctx;
		mAppDir = new File(getContext().getApplicationInfo().dataDir);
		mName = name;
		mExec = new File(mAppDir + File.separator + mName);
		if (!mExec.exists()) {
			install();
		}
	}

	protected final String getString(int res) {
		return getContext().getString(res);
	}

	protected final String getString(int res, Object... formatArgs) {
		return getContext().getString(res);
	}

	private void install() throws CollageException, IOException {
		File exec = new File(getPath());

		InputStream is;
		try {
			is = getContext().getAssets().open(getName());
		} catch (FileNotFoundException e) {
			throw new CollageException(getString(R.string.app_data_not_found), getName());
		} // Other IOExceptions will be thrown to the caller

		// Create parent dir if doesn't exist
		if (!getParentDir().exists()) {
			if (!getParentDir().mkdirs()) {
				throw new IOException(getString(R.string.app_dir_creation_failed));
			}
		}

		// Check if there's enough space
		long freeSpace = getParentDir().getFreeSpace()  / 1024 / 1024;  // in Mb
		long estimatedAssetSize = is.available()       / 1024 / 1024;  // in Mb
		if (freeSpace <= 0 && freeSpace <= estimatedAssetSize) {
			throw new CollageException(getString(R.string.app_dir_no_space));
		}

		if (!mExec.createNewFile()) {
			Log.d(TAG, "File existed when it shouldn't have!");
		}
		Utils.copyAsset(getContext(), getName(), getPath());

		if (!exec.setExecutable(true)) {
			throw new IOException("Marking file as executable failed!");
		}
	}

	public final String getName() {
		return mName;
	}

	protected final String getPath() {
		return mExec.getPath();
	}

	protected final File getParentDir() {
		return mAppDir;
	}

	protected final Context getContext() {
		return mContext;
	}

}
