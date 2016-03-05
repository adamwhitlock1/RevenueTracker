package com.managed.revenuetracker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.managed.revenuetracker.R;
import com.managed.revenuetracker.to.Revenue;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

//RevListAdapter goes through each db entry and uses the data to populate each list item into the view list_item and repeats this until all entries have been read
public class RevListAdapter extends ArrayAdapter<Revenue> {

	private Context context;
	static List<Revenue> revenues;

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.ENGLISH);

	public RevListAdapter(Context context, List<Revenue> revenues) {
		super(context, R.layout.list_item, revenues);
		this.context = context;
		this.revenues = revenues;
	}

	private class ViewHolder {
		TextView revIdTxt;
		TextView revPlatformTxt;
		TextView revDateTxt;
		TextView revAmtTxt;
	}

	@Override
	public int getCount() {
		return revenues.size();
	}

	@Override
	public Revenue getItem(int position) {
		return revenues.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}


	//	inflates view list_item to populate each value of db entry into list_item sections
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();

			holder.revIdTxt = (TextView) convertView
					.findViewById(R.id.txt_rev_id);
			holder.revPlatformTxt = (TextView) convertView
					.findViewById(R.id.txt_platform);
			holder.revDateTxt = (TextView) convertView
					.findViewById(R.id.txt_date);
			holder.revAmtTxt = (TextView) convertView
					.findViewById(R.id.txt_amt);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Revenue revenue = (Revenue) getItem(position);
		holder.revIdTxt.setText(revenue.getId() + "");
		holder.revPlatformTxt.setText(revenue.getPlatform());
		holder.revAmtTxt.setText(revenue.getAmt() + "");

		holder.revDateTxt.setText(formatter.format(revenue.getDate()));

		return convertView;
	}

	@Override
	public void add(Revenue revenue) {
		revenues.add(revenue);
		notifyDataSetChanged();
		super.add(revenue);
	}


	@Override
	public void remove(Revenue revenue) {
		revenues.remove(revenue);
		notifyDataSetChanged();
		super.remove(revenue);
	}
}
