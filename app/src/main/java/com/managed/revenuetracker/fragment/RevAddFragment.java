package com.managed.revenuetracker.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.managed.revenuetracker.R;
import com.managed.revenuetracker.db.RevenueDAO;
import com.managed.revenuetracker.to.Revenue;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RevAddFragment extends Fragment implements OnClickListener {

	// UI references
	private EditText revPlatformEtxt;
	private EditText revAmtEtxt;
	private EditText revDateEtxt;
	private Button addButton;
	private Button resetButton;

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.ENGLISH);

	DatePickerDialog datePickerDialog;
	Calendar dateCalendar;

	Revenue revenue = null;
	private RevenueDAO revenueDAO;
	private AddRevTask task;

	public static final String ARG_ITEM_ID = "rev_add_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		revenueDAO = new RevenueDAO(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_rev, container,
				false);

		findViewsById(rootView);

		setListeners();

		//For orientation change. 
		if (savedInstanceState != null) {
			dateCalendar = Calendar.getInstance();
			if (savedInstanceState.getLong("dateCalendar") != 0)
				dateCalendar.setTime(new Date(savedInstanceState
						.getLong("dateCalendar")));
		}

		return rootView;
	}

	private void setListeners() {
		revDateEtxt.setOnClickListener(this);
		Calendar newCalendar = Calendar.getInstance();
		datePickerDialog = new DatePickerDialog(getActivity(),
				new OnDateSetListener() {

					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						dateCalendar = Calendar.getInstance();
						dateCalendar.set(year, monthOfYear, dayOfMonth);
						revDateEtxt.setText(formatter.format(dateCalendar
								.getTime()));
					}

				}, newCalendar.get(Calendar.YEAR),
				newCalendar.get(Calendar.MONTH),
				newCalendar.get(Calendar.DAY_OF_MONTH));

		addButton.setOnClickListener(this);
		resetButton.setOnClickListener(this);
	}

	protected void resetAllFields() {
		revPlatformEtxt.setText("");
		revAmtEtxt.setText("");
		revDateEtxt.setText("");
	}

	private void setRevenue() {
		revenue = new Revenue();
		revenue.setPlatform(revPlatformEtxt.getText().toString());
		revenue.setAmt(Double.parseDouble(revAmtEtxt.getText()
				.toString()));
		if (dateCalendar != null)
			revenue.setDate(dateCalendar.getTime());
	}

	@Override
	public void onResume() {
		getActivity().setTitle(R.string.add_rev);
		getActivity().getActionBar().setTitle(R.string.add_rev);
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (dateCalendar != null)
			outState.putLong("dateCalendar", dateCalendar.getTime().getTime());
	}

	private void findViewsById(View rootView) {
		revPlatformEtxt = (EditText) rootView.findViewById(R.id.etxt_platform);
		revAmtEtxt = (EditText) rootView.findViewById(R.id.etxt_amt);
		revDateEtxt = (EditText) rootView.findViewById(R.id.etxt_date);
		revDateEtxt.setInputType(InputType.TYPE_NULL);

		addButton = (Button) rootView.findViewById(R.id.button_add);
		resetButton = (Button) rootView.findViewById(R.id.button_reset);
	}

	@Override
	public void onClick(View view) {
		if (view == revDateEtxt) {
			datePickerDialog.show();
		} else if (view == addButton) {
			setRevenue();

			task = new AddRevTask(getActivity());
			task.execute((Void) null);
		} else if (view == resetButton) {
			resetAllFields();
		}
	}

	public class AddRevTask extends AsyncTask<Void, Void, Long> {

		private final WeakReference<Activity> activityWeakRef;

		public AddRevTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected Long doInBackground(Void... arg0) {
			long result = revenueDAO.save(revenue);
			return result;
		}

		@Override
		protected void onPostExecute(Long result) {
			if (activityWeakRef.get() != null
					&& !activityWeakRef.get().isFinishing()) {
				if (result != -1)
					Toast.makeText(activityWeakRef.get(), "Revenue Saved",
							Toast.LENGTH_LONG).show();
			}
		}
	}
}
