package com.managed.revenuetracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.managed.revenuetracker.fragment.CustomRevDialogFragment.RevDialogFragmentListener;
import com.managed.revenuetracker.fragment.RevAddFragment;
import com.managed.revenuetracker.fragment.RevListFragment;

public class MainActivity extends FragmentActivity implements
		RevDialogFragmentListener {

	private Fragment contentFragment;
	private RevListFragment revenueListFragment;
	private RevAddFragment revenueAddFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentManager fragmentManager = getSupportFragmentManager();

		/*
		 * This is called when orientation is changed.
		 */
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("content")) {
				String content = savedInstanceState.getString("content");
				if (content.equals(RevAddFragment.ARG_ITEM_ID)) {
					if (fragmentManager
							.findFragmentByTag(RevAddFragment.ARG_ITEM_ID) != null) {
						setFragmentTitle(R.string.add_rev);
						contentFragment = fragmentManager
								.findFragmentByTag(RevAddFragment.ARG_ITEM_ID);
					}
				}
			}
			if (fragmentManager.findFragmentByTag(RevListFragment.ARG_ITEM_ID) != null) {
				revenueListFragment = (RevListFragment) fragmentManager
						.findFragmentByTag(RevListFragment.ARG_ITEM_ID);
				contentFragment = revenueListFragment;
			}
		} else {
			revenueListFragment = new RevListFragment();
			setFragmentTitle(R.string.app_name);
			switchContent(revenueListFragment, RevListFragment.ARG_ITEM_ID);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (contentFragment instanceof RevAddFragment) {
			outState.putString("content", RevAddFragment.ARG_ITEM_ID);
		} else {
			outState.putString("content", RevListFragment.ARG_ITEM_ID);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			setFragmentTitle(R.string.add_rev);
			revenueAddFragment = new RevAddFragment();
			switchContent(revenueAddFragment, RevAddFragment.ARG_ITEM_ID);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * We consider RevListFragment as the home fragment and it is not added to
	 * the back stack.
	 */
	public void switchContent(Fragment fragment, String tag) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		while (fragmentManager.popBackStackImmediate())
			;

		if (fragment != null) {
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.replace(R.id.content_frame, fragment, tag);
			// Only RevAddFragment is added to the back stack.
			if (!(fragment instanceof RevListFragment)) {
				transaction.addToBackStack(tag);
			}
			transaction.commit();
			contentFragment = fragment;
		}
	}

	protected void setFragmentTitle(int resourseId) {
		setTitle(resourseId);
		getActionBar().setTitle(resourseId);

	}

	/*
	 * We call super.onBackPressed(); when the stack entry count is > 0. if it
	 * is instanceof RevListFragment or if the stack entry count is == 0, then
	 * we prompt the user whether to quit the app or not by displaying dialog.
	 * In other words, from RevListFragment on back press it quits the app.
	 */
	@Override
	public void onBackPressed() {
		FragmentManager fm = getSupportFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			super.onBackPressed();
		} else if (contentFragment instanceof RevListFragment
				|| fm.getBackStackEntryCount() == 0) {
            //Shows an alert dialog on quit
			onShowQuitDialog();
		}
	}

	public void onShowQuitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);

		builder.setMessage("Do You Want To Quit?");
		builder.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});
		builder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

	@Override
	public void onFinishDialog() {
		if (revenueListFragment != null) {
			revenueListFragment.updateView();
		}
	}
}
