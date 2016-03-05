package com.managed.revenuetracker.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.managed.revenuetracker.MainActivity;
import com.managed.revenuetracker.R;
import com.managed.revenuetracker.db.RevenueDAO;
import com.managed.revenuetracker.to.Revenue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CustomRevDialogFragment extends DialogFragment {

	// UI references
	private EditText revPlatformEtxt;
	private EditText revAmtEtxt;
	private EditText revDateEtxt;
	private LinearLayout submitLayout;

	private Revenue revenue;

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.ENGLISH);
	
	RevenueDAO revenueDAO;
	
	public static final String ARG_ITEM_ID = "rev_dialog_fragment";

	public interface RevDialogFragmentListener {
		void onFinishDialog();
	}

	public CustomRevDialogFragment() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		revenueDAO = new RevenueDAO(getActivity());

		Bundle bundle = this.getArguments();
		revenue = bundle.getParcelable("selectedRevenue");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View customDialogView = inflater.inflate(R.layout.fragment_add_rev,
				null);
		builder.setView(customDialogView);

		revPlatformEtxt = (EditText) customDialogView.findViewById(R.id.etxt_platform);
		revAmtEtxt = (EditText) customDialogView
				.findViewById(R.id.etxt_amt);
		revDateEtxt = (EditText) customDialogView.findViewById(R.id.etxt_date);
		submitLayout = (LinearLayout) customDialogView
				.findViewById(R.id.layout_submit);
		submitLayout.setVisibility(View.GONE);

		setValue();

		builder.setTitle(R.string.update);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.update,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {
							revenue.setDate(formatter.parse(revDateEtxt.getText().toString()));
						} catch (ParseException e) {
							Toast.makeText(getActivity(),
									"Invalid date format!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						revenue.setPlatform(revPlatformEtxt.getText().toString());
						revenue.setAmt(Double.parseDouble(revAmtEtxt
								.getText().toString()));
						long result = revenueDAO.update(revenue);
						if (result > 0) {
							MainActivity activity = (MainActivity) getActivity();
							activity.onFinishDialog();
						} else {
							Toast.makeText(getActivity(),
									"Unable to update revenue",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = builder.create();

		return alertDialog;
	}

	private void setValue() {
		if (revenue != null) {
			revPlatformEtxt.setText(revenue.getPlatform());
			revAmtEtxt.setText(revenue.getAmt() + "");
			revDateEtxt.setText(formatter.format(revenue.getDate()));
		}
	}
}
