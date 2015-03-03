package com.example.simas.collage;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	/**
	 *
	 * @param is
	 * @param destinationFile    must already exist
	 * @throws IOException
	 */
	public static void copyBytes(InputStream is, File destinationFile) throws IOException {
		OutputStream os = new FileOutputStream(destinationFile);
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
	 * @param assetName          Asset name
	 * @param destinationDir     Absolute path to the output directory
	 */
	public static void copyAsset(Context ctx, String assetName, String destinationDir)
			throws IOException {
		AssetManager assetManager = ctx.getAssets();
		InputStream is = assetManager.open(assetName);
		File destinationFile = new File(destinationDir + File.separator + assetName);
		if (!destinationFile.exists()) {
			if (!destinationFile.mkdirs() || !destinationFile.createNewFile()) {
				throw new IOException("The destination file doesn't exist and couldn't be" +
						"created! " + destinationFile.getPath());
			}
		}
		copyBytes(is, destinationFile);
	}

	/**
	 * Blocking method that will read InputStream to a string and return it
	 * @param is    InputStream from which data will be read
	 * @return null or a String containing the data that was read
	 * @throws IOException
	 */
	public static String readStream(InputStream is) throws IOException {
		BufferedReader reader;
		String output = "", line;
			reader = new BufferedReader(new InputStreamReader(is));
			while ((line = reader.readLine()) != null) {
				output += line + '\n';
			}

		return TextUtils.isEmpty(output) ? null : output;
	}

}
