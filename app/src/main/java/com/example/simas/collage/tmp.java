/* MAINACTIVITY */

//package com.example.simas.videocollage;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.ActionBar;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.support.v4.widget.DrawerLayout;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//// ToDo nauja klase kuri bendraus su low level metodais (ffmpeg/c)
//// ToDo visas error checkinimas vyksta kuo daugiau javoje
//// ToDo Galbut reiks ffmpeg mini metodu kurie aprasys video kuris paduotas
//// ToDo low-lvl metodai turetu grazint informatyvius error kodus. Naudot intus arba java objectus
//// ToDo C gali skaityt Javos klases, bet ne atvirksciai. Tad klaidu structai/klases turetu but
//// ToDo sukurtos javoje. Lengviau butu grazint tiesiog intus, taciau galbut galima ir grazint
//// ToDo throwables :DD
//// ToDo kad kuo labiau sumazint bendravima ir siuntinejima tarp c ir javos, naudot paprastus int
//
//// ToDo writing real-time games https://www.youtube.com/watch?v=U4Bk5rmIpic
//
//// ToDo for now just test merging
//
//public class MainActivity extends ActionBarActivity
//		implements NavigationDrawerFragment.NavigationDrawerCallbacks {
//
//	// Load the C libraries, including own scripts
////	static {
////		System.loadLibrary("swresample");
////		System.loadLibrary("avcodec");
////		System.loadLibrary("avutil");
////		System.loadLibrary("avformat");
////		System.loadLibrary("swscale");
////		System.loadLibrary("ffmpegcollage");
////	}
////
////	// C method prototypes
////	private static native void cMain();
////	private static native boolean mergeVideos(MainActivity ma, String filename1, String filename2);
//
//////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	/**
//	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
//	 */
//	private NavigationDrawerFragment mNavigationDrawerFragment;
//
//	/**
//	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
//	 */
//	private CharSequence mTitle;
//
//	private static final String TAG = "MainActivity";
//	private String TMP_FILE;
//	private static final String TMP_DIR = Environment
//			.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/tmps";
//	private String mExecPath;
//
//	static final int REQUEST_VIDEO_CAPTURE = 1;
//	private void dispatchTakeVideoIntent() {
//		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//		if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
//			startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//		}
//	}
//
//	private void installFfmpeg() {
//		// ToDo check if enough space in destination
//		File ffmpegExec = new File(getCacheDir(), "ffmpeg"); // ToDo cacheDir
//		ffmpegExec.delete();
//		mExecPath = ffmpegExec.getPath();
//		Log.i(TAG, "FFmpeg install path: " + mExecPath);
//
//		if (!ffmpegExec.exists()) {
//			try {
//				if (!ffmpegExec.createNewFile()) {
//					Log.e(TAG, "Failed to create new file! " + mExecPath);
//				}
//			} catch (IOException e) {
//				Log.e(TAG, "Failed to create new file!", e);
//			}
//			Utils.installBinaryFromRaw(this, R.raw.ffmpeg, ffmpegExec);
//		} else {
//			Log.i(TAG, "FFmpeg already installed.");
//		}
//
//		if (!ffmpegExec.setExecutable(true)) {
//			Log.e(TAG, "Failed setting file as executable! File: " + mExecPath);
//		}
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		initFiles();
//		installFfmpeg();
//
//		setContentView(R.layout.activity_main);
//
//		mNavigationDrawerFragment = (NavigationDrawerFragment)
//				getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
//		mTitle = getTitle();
//
//		// Set up the drawer.
//		mNavigationDrawerFragment.setUp(
//				R.id.navigation_drawer,
//				(DrawerLayout) findViewById(R.id.drawer_layout));
//
//
//		final String outputPath = TMP_DIR + "/output.mp4";
//		// Remove old output file if exists
//		new File(outputPath).delete();
//
//		// Save file containing all source video paths
//		String sources = saveSources(TMP_DIR, "1.mp4", "2.mp4", "4.mp4", "5.mp4", "6.mp4",
//				"7.mp4", "8.mp4");
//
//		try {
//			String cmd = String.format("%s -f concat -i %s -c copy %s",
//					mExecPath, sources, outputPath);
//			Process p = Runtime.getRuntime().exec(cmd); // ToDo ProcessBuilder instead?
//			p.waitFor();
//
//			String error = getError(p);
//			if (error == null) {
//				Log.e(TAG, "Success!");
//			} else {
//				Log.e(TAG, "Fail! " + error);
//			}
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void initFiles() {
//		// Temporary directory
//		File tmpDir = new File(TMP_DIR);
//		tmpDir.delete();
//		tmpDir.mkdirs();
//
//		// Temporary file
//		TMP_FILE = getCacheDir() + "/" + "collage.tmp"; // ToDo cacheDir ?
//		File tmpFile = new File(TMP_FILE);
//		tmpFile.delete();
//		try {
//			tmpFile.createNewFile();
//		} catch (IOException e) {
//			Log.e(TAG, "Temporary file creation failed!", e);
//		}
//
//		// Video files
//		Utils.copyBytes(this, "1.mp4", TMP_DIR);
//		Utils.copyBytes(this, "2.mp4", TMP_DIR);
//		Utils.copyBytes(this, "4.mp4", TMP_DIR);
//		Utils.copyBytes(this, "5.mp4", TMP_DIR);
//		Utils.copyBytes(this, "6.mp4", TMP_DIR);
//		Utils.copyBytes(this, "7.mp4", TMP_DIR);
//		Utils.copyBytes(this, "8.mp4", TMP_DIR);
//	}
//
//
////	private String saveSources(String... sourceVideoPaths) {
////		String sources = "";
////		for (String sourceVideo : sourceVideoPaths) {
////			sources += "file '" + sourceVideo + "'\n";
////		}
////		Utils.copyBytes(sources.getBytes(), TMP_FILE);
////
////		return TMP_FILE;
////	}
//
//	/**
//	 * ToDo
//	 * @param sourceVideoNames videos names to be added
//	 * @return absolute path to the file containing a list of videos
//	 */
//	private String saveSources(String dir, String... sourceVideoNames) {
//		String sources = "";
//		for (String sourceVideo : sourceVideoNames) {
//			sources += "file '" + dir + "/" + sourceVideo + "'\n";
//		}
//		Utils.copyBytes(sources.getBytes(), TMP_FILE);
//
//		return TMP_FILE;
//	}
//
//	private String getError(Process process) {
//		String error = null;
//		int exitValue = process.exitValue();
//		if (exitValue != 0) {
//			try {
//				error = "Exit value: " + exitValue + "\n";
//				int read;
//				while ((read = process.getErrorStream().read()) != -1) {
//					error += (char) read;
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return error;
//	}
//
//
//	private void saveFrameToPath(Bitmap bitmap, String path) {
//		int BUFFER_SIZE = 1024 * 8;
//		try {
//			Log.e(TAG, "Save to " + path);
//			File file = new File(path);
//			file.createNewFile();
//			FileOutputStream fos = new FileOutputStream(file);
//			final BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE);
//			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//			bos.flush();
//			bos.close();
//			fos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void onNavigationDrawerItemSelected(int position) {
//		// update the main content by replacing fragments
//		FragmentManager fragmentManager = getSupportFragmentManager();
//		fragmentManager.beginTransaction()
//				.replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//				.commit();
//	}
//
//	public void onSectionAttached(int number) {
//		switch (number) {
//			case 1:
//				mTitle = getString(R.string.title_section1);
//				break;
//			case 2:
//				mTitle = getString(R.string.title_section2);
//				break;
//			case 3:
//				mTitle = getString(R.string.title_section3);
//				break;
//		}
//	}
//
//	public void restoreActionBar() {
//		ActionBar actionBar = getSupportActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//		actionBar.setDisplayShowTitleEnabled(true);
//		actionBar.setTitle(mTitle);
//	}
//
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		if (!mNavigationDrawerFragment.isDrawerOpen()) {
//			// Only show items in the action bar relevant to this screen
//			// if the drawer is not showing. Otherwise, let the drawer
//			// decide what to show in the action bar.
//			getMenuInflater().inflate(R.menu.main, menu);
//			restoreActionBar();
//			return true;
//		}
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//
//		//noinspection SimplifiableIfStatement
//		if (id == R.id.action_settings) {
//			return true;
//		}
//
//		return super.onOptionsItemSelected(item);
//	}
//
//	/**
//	 * A placeholder fragment containing a simple view.
//	 */
//	public static class PlaceholderFragment extends Fragment {
//		/**
//		 * The fragment argument representing the section number for this
//		 * fragment.
//		 */
//		private static final String ARG_SECTION_NUMBER = "section_number";
//
//		/**
//		 * Returns a new instance of this fragment for the given section
//		 * number.
//		 */
//		public static PlaceholderFragment newInstance(int sectionNumber) {
//			PlaceholderFragment fragment = new PlaceholderFragment();
//			Bundle args = new Bundle();
//			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//			fragment.setArguments(args);
//			return fragment;
//		}
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//		                         Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//			return rootView;
//		}
//
//		@Override
//		public void onAttach(Activity activity) {
//			super.onAttach(activity);
//			((MainActivity) activity).onSectionAttached(
//					getArguments().getInt(ARG_SECTION_NUMBER));
//		}
//	}
//
//}



/* MAINACTIVITY */