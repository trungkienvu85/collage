package com.example.simas.collage.NavDrawer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.example.simas.collage.R;

/**
 * Created by Simas Abramovas on 2015 Mar 07.
 */

// ToDo < API 11 do an EditText, code check and appropriate layouts
// ToDo uzklausa pasiketus lengthui: crop start or end. If first/last stream then no need to ask
// ToDo need hours/milliseconds picker?

public class TimeChanger {

	private static final String TAG = "TimeChangerDialog";
	private NumberPicker mMinPicker;
	private NumberPicker mSecPicker;
	private TextView mTimeContainer;

	private int mInitialMins, mInitialSecs;

	public TimeChanger(Context context, int titleResId, ViewGroup container) {
		View contentView = View.inflate(context, R.layout.time_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle(titleResId)
				.setView(contentView)
				.setPositiveButton("okay", null)
//				.setNegativeButton()
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						updateTime();
					}
				});

		if (Build.VERSION.SDK_INT >= 17) {
			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					updateTime();
				}
			});
		}

		mMinPicker = (NumberPicker) contentView.findViewById(R.id.min);
		mMinPicker.setMaxValue(60);
		mMinPicker.setMinValue(0);
		mSecPicker = (NumberPicker) contentView.findViewById(R.id.sec);
		mSecPicker.setMaxValue(60);
		mSecPicker.setMinValue(0);

		// Save the container
		mTimeContainer = (TextView) container.getChildAt(1);
		// Fetch the min, sec values
		String str = String.valueOf(mTimeContainer.getText());
		if (!TextUtils.isEmpty(str)) {
			String[] time = str.split(":");
			try {
				mInitialMins = Integer.parseInt(time[0]);
				mInitialMins = Integer.parseInt(time[1]);
			} catch (NumberFormatException e) {
				Log.e(TAG, "Failed to parse: '" + str + "'", e);
				mInitialMins = 0;
				mInitialSecs = 0;
			}
		}

		builder.create().show();
	}

	private void updateTime() {
		if (mMinPicker.getValue() != mInitialMins || mSecPicker.getValue() != mInitialSecs) {
			mTimeContainer.setText(String.format("%02d:%02d",
					mMinPicker.getValue(), mSecPicker.getValue()));
		}
	}

}
