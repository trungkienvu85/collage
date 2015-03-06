package com.example.simas.collage;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Simas Abramovas on 2015 Mar 06.
 */

// ToDo kazkaip pratestint parceli
// ToDo move cursor to the end on first EditText focus
// ToDo Hide keyboard when other group expanded

public class MenuAdapter extends BaseExpandableListAdapter implements Parcelable, View.OnFocusChangeListener {

	public Context mContext;
	private List<String> mGroups = new ArrayList<>();
	private List<List<Integer>> mColorSets = new ArrayList<>();
	private Map<Integer, Integer> mGroupColors = new HashMap<>();
	public Integer lastExpandedGroup;
	private Random mRandom = new Random(System.currentTimeMillis());

	/* Parcelable */

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(mColorSets);
		dest.writeStringList(mGroups);
		dest.writeMap(mGroupColors);
		dest.writeSerializable(lastExpandedGroup);
	}

	public static final Parcelable.Creator<MenuAdapter> CREATOR = new Parcelable
			.Creator<MenuAdapter>() {
		public MenuAdapter createFromParcel(Parcel in) {
			return new MenuAdapter(in);
		}

		public MenuAdapter[] newArray(int size) {
			throw new UnsupportedOperationException();
		}
	};

	/**
	 * Constructor for re-creating the list (saved instance)
	 */
	public MenuAdapter(Parcel parcel) {
		parcel.readList(mColorSets, List.class.getClassLoader());
		parcel.readStringList(mGroups);
		parcel.readMap(mGroupColors, Map.class.getClassLoader());
		lastExpandedGroup = (Integer) parcel.readSerializable(); // ToDo r u sure
	}

	/* Place cursor on the end when EditText selected */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			EditText et = (EditText) v;
			et.setSelection(et.getText().length());
		}
	}


	private static class GroupViewHolder {
		TextView name;
	}

	private static class ChildViewHolder {
		TextView from_min;
		TextView from_sec;
		TextView duration_min;
		TextView duration_sec;
		TextView to_min;
		TextView to_sec;
	}

	/**
	 * Default constructor, creating an empty adapter
	 */
	public MenuAdapter(Context context) {
		mContext = context;
		createColorSets();
	}

	private void createColorSets() {
		int[] colors = new int[] { R.color.indigo, R.color.light_green, R.color.orange,
				R.color.blue_grey, R.color.blue, R.color.deep_purple,
				R.color.teal, R.color.lime, R.color.amber };

		for (int i=0; i<3; ++i) {
			List<Integer> set = new ArrayList<>();
			for (int j=0; j<3; ++j) set.add(getContext().getResources().getColor(colors[i*3 + j]));
			mColorSets.add(set);
		}
	}

	public Context getContext() {
		return mContext;
	}

	public void changeGroups(List<String> groups) {
		mGroups = groups;
		notifyDataSetChanged();
	}

	public void changeGroups(String... groups) {
		mGroups = Arrays.asList(groups);
		notifyDataSetChanged();
	}

	@Override
	public int getGroupCount() {
		return (mGroups == null) ? 0 : mGroups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// Every group (effect/stream) has 1 child
		return 1;
	}

	@Override
	public String getGroup(int groupPosition) {
		return mGroups.get(groupPosition);
	}

	private int getGroupColor(int groupPosition) {
		Integer groupColor = mGroupColors.get(groupPosition);
		// Set the color if not present
		if (groupColor == null) {
			int targetSet = groupPosition % 3;
			groupColor = mColorSets.get(targetSet).get(mRandom.nextInt(3));
			mGroupColors.put(groupPosition, groupColor);
		}

		return groupColor;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
	                         ViewGroup parent) {
		GroupViewHolder holder;
		if (convertView == null) {
			// Inflate
			convertView = View.inflate(getContext(), R.layout.elv_group, null);
			// Save the ViewHolder for re-use
			holder = new GroupViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.text_view);
			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}

		holder.name.setText(getGroup(groupPosition));
		holder.name.setBackgroundColor(getGroupColor(groupPosition));

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
	                         View convertView, ViewGroup parent) {
		ChildViewHolder holder;
		if (convertView == null) {
			// Inflate
			convertView = View.inflate(getContext(), R.layout.elv_child, null);
			// Save the ViewHolder for re-use
			holder = new ChildViewHolder();
			holder.from_min = (TextView) convertView.findViewById(R.id.start_time_min);
			holder.from_min.setSelectAllOnFocus(true);
			holder.from_sec = (TextView) convertView.findViewById(R.id.start_time_sec);
			holder.from_sec.setSelectAllOnFocus(true);
			holder.duration_min = (TextView) convertView.findViewById(R.id.duration_min);
			holder.duration_min.setSelectAllOnFocus(true);
			holder.duration_sec = (TextView) convertView.findViewById(R.id.duration_sec);
			holder.duration_sec.setSelectAllOnFocus(true);
			holder.to_min = (TextView) convertView.findViewById(R.id.end_time_min);
			holder.to_min.setSelectAllOnFocus(true);
			holder.to_sec = (TextView) convertView.findViewById(R.id.end_time_sec);
			holder.to_sec.setSelectAllOnFocus(true);
			convertView.setTag(holder);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}

//		holder.from.setText("00:00");
//		holder.duration.setText(getGroup(groupPosition));
//		holder.to.setText(getGroup(groupPosition));
		convertView.setBackgroundColor(getGroupColor(groupPosition));

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public int getChildTypeCount() {
		return 2;
	}

	@Override
	public int getChildType(int groupPosition, int childPosition) {
		// Stream = 0, effect = 1
//			if () {
//				return 0;
//			} else {
//				return 1;
//			}
		return 0;
	}
}