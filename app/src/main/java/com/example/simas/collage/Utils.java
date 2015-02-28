package com.example.simas.collage;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Simas Abramovas on 2015 Feb 28.
 */

public class Utils {

	private static final String TAG = "Utils";
	private static final int IO_BUFFER_SIZE = 32768;

	public static void copyBytes(InputStream is, OutputStream os) throws IOException {
		byte[] buffer = new byte[IO_BUFFER_SIZE];
		int count;
		while ((count = is.read(buffer)) != -1) {
			os.write(buffer, 0, count);
		}
	}

	public static void copyBytes(InputStream is, String destinationFile) throws IOException {
		File destination = new File(destinationFile);
		OutputStream os = new FileOutputStream(destination);
		copyBytes(is, os);
	}

	/**
	 * Will trim the file if it already exists
	 * @param bytes                  Byte array to be written to given file
	 * @param destionationFile    absolute path to destination file
	 */
	public static void copyBytes(byte[] bytes, File destionationFile) throws IOException {
		InputStream is = new ByteArrayInputStream(bytes);
		OutputStream os = new FileOutputStream(destionationFile);
		copyBytes(is, os);
	}

	/**
	 * Will use {@code destinationPath/assetFileName} as the output file
	 * @param ctx                Current application's context, used for asset fetching
	 * @param assetName      Asset name
	 * @param destination    Absolute path to the output directory
	 */
	public static void copyAsset(Context ctx, String assetName, String destination)
			throws IOException {
		AssetManager assetManager = ctx.getAssets();
		InputStream is = assetManager.open(assetName);
		copyBytes(is, destination + "/" + assetName);
	}

}
