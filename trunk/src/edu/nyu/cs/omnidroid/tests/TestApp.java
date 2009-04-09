package edu.nyu.cs.omnidroid.tests;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Test Application to Verify Configuration File Functionality o *
 */
public class TestApp extends ListActivity {

	static final String[] TESTS = new String[] { "Test User Config",
			"Test App Config Get", "Test CP", "Test Exception Handler" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(this.getLocalClassName(), "onCreate");
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, TESTS));
		getListView().setTextFilterEnabled(true);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Log.i(this.getLocalClassName(), "Enter");
		Log.i(this.getLocalClassName(), "listview: " + l);
		Log.i(this.getLocalClassName(), "view: " + v);
		Log.i(this.getLocalClassName(), "position: " + position);
		Log.i(this.getLocalClassName(), "id: " + id);

		// Intent i = new Intent();
		Intent i = new Intent();

		switch (position) {
		case 0:
			i.setClassName("edu.nyu.cs.omnidroid.tests",
					"edu.nyu.cs.omnidroid.tests.TestUserConfig");
			break;
		case 1:
			i.setClassName("edu.nyu.cs.omnidroid.tests",
					"edu.nyu.cs.omnidroid.tests.TestAppConfig");
			break;
		case 2:
			i.setClassName("edu.nyu.cs.omnidroid.tests",
					"edu.nyu.cs.omnidroid.tests.TestCP");
			break;
		case 3:
			i.setClassName("edu.nyu.cs.omnidroid.tests",
					"edu.nyu.cs.omnidroid.tests.TestExceptionHandler");
			break;
		}
		startActivity(i);
	}
}