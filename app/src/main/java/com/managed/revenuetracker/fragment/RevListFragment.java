package com.managed.revenuetracker.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.managed.revenuetracker.R;
import com.managed.revenuetracker.adapter.RevListAdapter;
import com.managed.revenuetracker.db.RevenueDAO;
import com.managed.revenuetracker.to.Revenue;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.managed.revenuetracker.adapter.RevListAdapter.*;

public class RevListFragment extends Fragment implements OnItemClickListener,
		OnItemLongClickListener {

	public static final String ARG_ITEM_ID = "revenue_list";

	Activity activity;
	ListView revListView;
	ArrayList<Revenue> revenues;

	RevListAdapter revListAdapter;
	RevenueDAO revenueDAO;

	private GetRevTask task;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		revenueDAO = new RevenueDAO(activity);
	}

	//	inflates fragment_rev_list into activity_main
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_rev_list, container,
				false);
		findViewsById(view);

		task = new GetRevTask(activity);
		task.execute((Void) null);

		revListView.setOnItemClickListener(this);
		revListView.setOnItemLongClickListener(this);
		// Revenue e = employeeDAO.getRevenue(1);
		// Log.d("employee e", e.toString());
		return view;
	}

	private void findViewsById(View view) {
		revListView = (ListView) view.findViewById(R.id.list_emp);
	}

	@Override
	public void onResume() {
		getActivity().setTitle(R.string.app_name);
		getActivity().getActionBar().setTitle(R.string.app_name);
		super.onResume();
	}


	@Override
	public void onItemClick(AdapterView<?> list, View arg1, int position,
			long arg3) {
		Revenue employee = (Revenue) list.getItemAtPosition(position);

		if (employee != null) {
			Bundle arguments = new Bundle();
			arguments.putParcelable("selectedRevenue", employee);
			CustomRevDialogFragment customRevDialogFragment = new CustomRevDialogFragment();
			customRevDialogFragment.setArguments(arguments);
			customRevDialogFragment.show(getFragmentManager(),
					CustomRevDialogFragment.ARG_ITEM_ID);
		}
	}


	//	removes an item from the list and db on a long click
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long arg3) {
		Revenue revenue = (Revenue) parent.getItemAtPosition(position);

		// Use AsyncTask to delete from database
		RevenueDAO.delete(revenue);

		// **** this is an issue and I cannot get the actual list view to update correctly after removing db entry
		// RevListAdapter.remove(revenue);

		return true;
	}

	public class GetRevTask extends AsyncTask<Void, Void, ArrayList<Revenue>> {

		private final WeakReference<Activity> activityWeakRef;

		public GetRevTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected ArrayList<Revenue> doInBackground(Void... arg0) {
			ArrayList<Revenue> revenueList = revenueDAO.getRevenues();
			return revenueList;
		}

		@Override
		protected void onPostExecute(ArrayList<Revenue> revList) {
			if (activityWeakRef.get() != null
					&& !activityWeakRef.get().isFinishing()) {
				Log.d("revenues", revList.toString());
				revenues = revList;
				if (revList != null) {
					if (revList.size() != 0) {
						revListAdapter = new RevListAdapter(activity,
								revList);
						revListView.setAdapter(revListAdapter);
					} else {
						Toast.makeText(activity, "No Revenue Records",
								Toast.LENGTH_LONG).show();
					}
				}

			}
		}
	}

	/*
	 * This method is invoked from MainActivity onFinishDialog() method. It is
	 * called from CustomEmpDialogFragment when an employee record is updated.
	 * This is used for communicating between fragments.
	 */
	public void updateView() {
		task = new GetRevTask(activity);
		task.execute((Void) null);
	}
}
