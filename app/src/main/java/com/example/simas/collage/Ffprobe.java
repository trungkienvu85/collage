package com.example.simas.collage;

import android.content.Context;
import android.util.Log;

import com.example.simas.collage.SmartProcessBuilder.SmartProcessBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simas Abramovas on 2015 Feb 28.
 */

// ToDo IOException: Text file busy
	//03-03 19:12:21.935  28564-28698/com.example.simas.collage E/MainActivity﹕ Error!
	//		java.io.IOException: Error running exec(). Command: [/data/data/com.example.simas.collage/ffprobe, -v quiet -print_format json, -show_format, -show_entries format=duration,size,format_name,format_long_name,filename,nb_streams, -show_streams, -show_entries stream=codec_name,codec_long_name,codec_type,sample_rate,channels,duration,display_aspect_ratio,width,height, -i /storage/emulated/0/Download/tmps/1.mp4] Working Directory: null Environment: [ANDROID_ROOT=/system, EMULATED_STORAGE_SOURCE=/mnt/shell/emulated, LOOP_MOUNTPOINT=/mnt/obb, LD_PRELOAD=libsigchain.so, ANDROID_BOOTLOGO=1, EMULATED_STORAGE_TARGET=/storage/emulated, EXTERNAL_STORAGE=/storage/emulated/legacy, SYSTEMSERVERCLASSPATH=/system/framework/services.jar:/system/framework/ethernet-service.jar:/system/framework/wifi-service.jar, ANDROID_SOCKET_zygote=11, PATH=/sbin:/vendor/bin:/system/sbin:/system/bin:/system/xbin, ANDROID_DATA=/data, ANDROID_ASSETS=/system/app, ASEC_MOUNTPOINT=/mnt/asec, BOOTCLASSPATH=/system/framework/core-libart.jar:/system/framework/conscrypt.jar:/system/framework/okhttp.jar:/system/framework/core-junit.jar:/system/framework/bouncycastle.jar:/system/framework/ext.jar:/system/framework/framework.jar:/system/framework/telephony-common.jar:/system/framework/voip-common.jar:/system/framework/ims-common.jar:/system/framework/mms-common.jar:/system/framework/android.policy.jar:/system/framework/apache-xml.jar, ANDROID_PROPERTY_WORKSPACE=9,0, ANDROID_STORAGE=/storage]
	//		at java.lang.ProcessManager.exec(ProcessManager.java:211)
	//		at java.lang.ProcessBuilder.start(ProcessBuilder.java:195)
	//		at com.example.simas.collage.Ffprobe.getAttributesForVideo(Ffprobe.java:62)
	//		at com.example.simas.collage.MainActivity$1.run(MainActivity.java:76)
	//		at android.os.Handler.handleCallback(Handler.java:739)
	//		at android.os.Handler.dispatchMessage(Handler.java:95)
	//		at android.os.Looper.loop(Looper.java:135)
	//		at android.os.HandlerThread.run(HandlerThread.java:61)
	//		Caused by: java.io.IOException: Text file busy
	//		at java.lang.ProcessManager.exec(Native Method)
	//		at java.lang.ProcessManager.exec(ProcessManager.java:209)
	//		            at java.lang.ProcessBuilder.start(ProcessBuilder.java:195)
	//		            at com.example.simas.collage.Ffprobe.getAttributesForVideo(Ffprobe.java:62)
	//		            at com.example.simas.collage.MainActivity$1.run(MainActivity.java:76)
	//		            at android.os.Handler.handleCallback(Handler.java:739)
	//		            at android.os.Handler.dispatchMessage(Handler.java:95)
	//		            at android.os.Looper.loop(Looper.java:135)
	//		            at android.os.HandlerThread.run(HandlerThread.java:61)

public class Ffprobe extends Executable {

	private static final String TAG = "FFprobe";
	private static final String NAME = "ffprobe";

	/**
	 * @param ctx
	 */
	public Ffprobe(Context ctx) throws IOException, CollageException {
		super(ctx, NAME);
	}

	public Attributes getAttributesForVideo(File video) throws IOException, InterruptedException,
			CollageException {
		preventWorkOnUiThread();

		if (!video.exists()) throw new IOException("The input file doesn't exist!");

//		Invoke executable call
//		./ffprobe -v quiet -print_format json -show_format -show_streams -show_entries format=duration,size,format_name,format_long_name,filename,nb_streams -show_entries stream=codec_name,codec_long_name,codec_type,sample_rate,channels,duration,display_aspect_ratio,width,height 1.mp4

		List<String> args = new ArrayList<>();
		// Executable
		args.add(getPath());
		// Specify the input file
		args.add(String.format("-i %s", video.getPath()));
		// Print file information quietly in JSON
		args.add("-v quiet -print_format json");
		// Show specific information about the file
		args.add(String.format("-show_format -show_entries format=%s,%s,%s,%s,%s,%s",
				getString(R.string.format_duration), getString(R.string.format_size),
				getString(R.string.format_name), getString(R.string.format_long_name),
				getString(R.string.format_filename), getString(R.string.format_stream_count)));
		// Show all the streams but only specific information
		args.add(String.format("-show_streams -show_entries stream=%s,%s,%s,%s,%s,%s,%s,%s,%s",
				getString(R.string.stream_name), getString(R.string.stream_long_name),
				getString(R.string.stream_type), getString(R.string.stream_sample_rate),
				getString(R.string.stream_channels), getString(R.string.stream_duration),
				getString(R.string.stream_aspect_ratio), getString(R.string.stream_width),
				getString(R.string.stream_height)));

		// Build and start the process
		SmartProcessBuilder smb = new SmartProcessBuilder(args);
		Process p = smb.start();
		p.waitFor();

		// Read executable output
		Log.d(TAG, "Returned: " + p.exitValue());

		// Get output as JSON string
		String json = getProcessOutput(p);
		if (json == null) throw new CollageException(getString(R.string.ffprobe_fail));

		// Parse JSON
		List<JSONObject> streams = new ArrayList<>();
		JSONObject format;
		try {
			JSONObject obj = new JSONObject(json);
			// Fetch streams
			JSONArray streamArr = obj.getJSONArray("streams");
			int streamCount = streamArr.length();
			for (int i=0; i<streamCount; ++i) {
				streams.add(streamArr.getJSONObject(i));
			}
			// Fetch format
			format = obj.getJSONObject("format");
		} catch (JSONException e) {
			e.printStackTrace(); // ToDo nusprest kur mest CollageException kur IOException,
									// T.y. kur recoverable, o kur ne
			return null;
		}

		// Parse format (container)
		String fileName, formatName, formatLongName;
		Integer streamCount;
		Long size;
		Double duration;

		fileName = getString(format, getString(R.string.format_filename));
		size = getLong(format, getString(R.string.format_size));
		duration = getDouble(format, getString(R.string.format_duration));
		formatName = getString(format, getString(R.string.format_name));
		formatLongName = getString(format, getString(R.string.format_long_name));
		streamCount = getInt(format, getString(R.string.format_stream_count));

		if (streamCount == null || streamCount != streams.size()) {
			throw new IOException(String.format("Failed reading the JSON! Stream count" +
					" doesn't match: format: %d, streams: %d", streamCount, streams.size()));
		}

		Attributes attributes = new Attributes();
		attributes.fileName = fileName;
		attributes.size = size;
		attributes.duration = duration;
		attributes.name = formatName;
		attributes.longName = formatLongName;

		// Parse streams
		for (JSONObject stream : streams) {
			switch (getString(stream, getString(R.string.stream_type))) {
				case StreamAttributes.TYPE_AUDIO:
					AudioStreamAttributes asa = new AudioStreamAttributes.Builder()
							.setCodecName(getString(stream, getString(R.string.stream_name)))
							.setCodecLongName(getString(stream, getString(R.string.stream_long_name)))
							.setChannelCount(getInt(stream, getString(R.string.stream_channels)))
							.setDuration(getDouble(stream, getString(R.string.stream_duration)))
							.setSampleRate(getInt(stream, getString(R.string.stream_sample_rate)))
							.build();
					attributes.addStreamAttributes(asa); // ToDo required fields?
					break;
				case StreamAttributes.TYPE_VIDEO:
					VideoStreamAttributes vsa = new VideoStreamAttributes.Builder()
							.setCodecName(getString(stream, getString(R.string.stream_name)))
							.setCodecLongName(getString(stream, getString(R.string.stream_long_name)))
							.setAspectRatio(getString(stream, getString(R.string.stream_aspect_ratio)))
							.setSize(getInt(stream, getString(R.string.stream_width)),
									getInt(stream, getString(R.string.stream_height)))
							.setDuration(getDouble(stream, getString(R.string.stream_duration)))
							.build();
					// Don't add the stream if any of the required fields not found
					if (vsa.getHeight() != null && vsa.getWidth() != null &&
							vsa.getCodecName() != null) {
						attributes.addStreamAttributes(vsa);
					}
					break;
				default:
					break;
			}
		}

		return attributes;
	}

	private String getString(JSONObject container, String key) {
		try {
			return container.getString(key);
		} catch (JSONException e) {
			Log.w(TAG, "Error fetching a String!", e);
			return null;
		}
	}

	private Integer getInt(JSONObject container, String key) {
		try {
			return container.getInt(key);
		} catch (JSONException e) {
			Log.w(TAG, "Error fetching an int!", e);
			return null;
		}
	}

	private Double getDouble(JSONObject container, String key) {
		try {
			return container.getDouble(key);
		} catch (JSONException e) {
			Log.w(TAG, "Error fetching an int!", e);
			return null;
		}
	}

	private Long getLong(JSONObject container, String key) {
		try {
			return container.getLong(key);
		} catch (JSONException e) {
			Log.w(TAG, "Error fetching an int!", e);
			return null;
		}
	}

	// Attribute class for files
	public static class Attributes {

		public String fileName, longName, name;
		public Long size;
		public Double duration;
		public List<StreamAttributes> streams = new ArrayList<>();

		private Attributes() { /* Prevent default construction */ }

		public void addStreamAttributes(StreamAttributes attributes) {
			streams.add(attributes);
		}

	}

}
