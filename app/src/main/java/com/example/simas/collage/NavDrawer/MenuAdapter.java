package com.example.simas.collage.NavDrawer;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.example.simas.collage.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Simas Abramovas on 2015 Mar 06.
 */

// ToDo kazkaip pratestint parcel creator. Ar neimanoma, nes bundle pats readina fieldus?
// ToDo start/end/length - buttons
// ToDo show that time is editable (underline?)

public class MenuAdapter extends BaseExpandableListAdapter implements Parcelable, View.OnClickListener {

	private static final String TAG = "MenuAdapter";
	private Context mContext;
	private LayoutInflater mInflater;
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

	private static class GroupViewHolder {
		TextView name;
	}

	private static class ChildViewHolder {
		TextView start;
		TextView duration;
		TextView end;
	}

	/**
	 * Default constructor, creating an empty adapter
	 */
	public MenuAdapter(Context context) {
		initForContext(context);
		createColorSets();
	}

	public void initForContext(Context ctx) {
		mContext = ctx;
		mInflater = LayoutInflater.from(mContext);
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

	private Context getContext() {
		return mContext;
	}

	private LayoutInflater getInflater() {
		return mInflater;
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
			convertView = getInflater().inflate(R.layout.elv_group, parent, false);
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
			convertView = getInflater().inflate(R.layout.elv_child, parent, false);
			// Save the ViewHolder for re-use
			holder = new ChildViewHolder();
			holder.start = (TextView) convertView.findViewById(R.id.start_time);
			convertView.findViewById(R.id.start).setOnClickListener(this);
			holder.duration = (TextView) convertView.findViewById(R.id.duration_time);
			convertView.findViewById(R.id.duration).setOnClickListener(this);
			holder.end = (TextView) convertView.findViewById(R.id.end_time);
			convertView.findViewById(R.id.end).setOnClickListener(this);
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
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.start:
				new TimeChanger(getContext(), R.string.start_changer, (ViewGroup) v);
				break;
			case R.id.duration:
				new TimeChanger(getContext(), R.string.duration_changer, (ViewGroup) v);
				break;
			case R.id.end:
				new TimeChanger(getContext(), R.string.end_changer, (ViewGroup) v);
				break;
			default:
				throw new IllegalArgumentException("Unexpected argument in the click listener!");
		}
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	@Override
	public int getChildTypeCount() {
		return 1;
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