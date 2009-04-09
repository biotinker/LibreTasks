package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PackageLister extends ListActivity {
	private static final String TAG = "EventCatcher";

	// TODO: Pull this from the Package Manager
	// TODO: Filter by only apps that contain actions
	static final String[] APPLICATIONS = new String[] { "Email", "Messaging",
			"Dialer", "Weather" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.i(this.getLocalClassName(), "onCreate start");
		super.onCreate(savedInstanceState);

		PackageManager pm = this.getPackageManager();
		List<PackageInfo> pkgList = pm.getInstalledPackages(0);
		Iterator<PackageInfo> i = pkgList.iterator();
		List<String> pkgNames = new ArrayList<String>();
		while (i.hasNext()) {
			PackageInfo pkg = i.next();
			pkgNames.add(pkg.packageName);
		}

		Iterator<String> i2 = pkgNames.iterator();
		while (i2.hasNext()) {
			String name = i2.next();
			Log.i(this.getLocalClassName(), name);
			System.out.println(this.getLocalClassName() + name);
		}

		// TODO (acase): Convert to human readable names
		// TODO (acase): Filter based on if it can catch events
		// setListAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, appNames));
		// setListAdapter(new ArrayAdapter<ApplicationInfo>(this,
		// android.R.layout.simple_list_item_1, appList));
		// setListAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, APPLICATIONS));
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, pkgNames));
		getListView().setTextFilterEnabled(true);
		Log.i(this.getLocalClassName(), "onCreate exit");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.i(TAG, "Enter");
		Log.i(TAG, "listview: " + l);
		Log.i(TAG, "view: " + v);
		Log.i(TAG, "position: " + position);
		Log.i(TAG, "id: " + id);

		// Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
		// String action = getIntent().getAction();
		// Launch activity to view/edit the currently selected item
		// TODO: Build URI dynamically

		// startActivity(new
		// Intent(edu.nyu.cs.omnidroid.util.intents.eventcatcheractions.SELECT));
		// Intent i = new Intent("SELECT");
		// Intent i = new Intent(this.,
		// edu.nyu.cs.omnidroid.ui.ActionThrower.class)
		// i.setAction("SELECT");
		// TODO: Turn into a omnidroid specific intent
		startActivity(new Intent(Intent.ACTION_VIEW, getIntent().getData()));

		Log.i(TAG, "Exit");
	}

}