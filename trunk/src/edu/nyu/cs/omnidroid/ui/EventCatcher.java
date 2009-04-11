package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.nyu.cs.omnidroid.util.AGParser;

public class EventCatcher extends ListActivity {
	private static final String TAG = EventCatcher.class.getSimpleName();
  private static AGParser ag;
  

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  ag = new AGParser(getApplicationContext());
	  
		Log.i(this.getLocalClassName(), "onCreate start");
		super.onCreate(savedInstanceState);

    // TODO: Filter by only apps that contain events
    // TODO: Pull from application config file
		ArrayList<String> pkgNames;
		pkgNames = populateList();
		
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, pkgNames));
		getListView().setTextFilterEnabled(true);

		Log.i(this.getLocalClassName(), "onCreate exit");
	}

	ArrayList<String> populateList() {
	  //Getting the Events from AppConfig
    return ag.readLines(AGParser.KEY_APPLICATION);
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


		// TODO: Turn into a omnidroid specific intent
    Intent i = new Intent();
    i.setClassName("edu.nyu.cs.omnidroid", "edu.nyu.cs.omnidroid.ui.EventCatcherActions");

    //Context context = getBaseContext();
    //ComponentName comp = new ComponentName(context.getPackageName(), EventCatcherActions.class.getName());
    //ComponentName activity = context.startActivity(new Intent().setComponent(comp));
    //ComponentName service = context.startService(new Intent().setComponent(comp));

    //i.setClassName(this.getApplicationContext(), "edu.nyu.cs.omnidroid.ui.EventCatcherActions");
		//TextView tv = (TextView) v;
		//i.putExtra(AGParser.KEY_APPLICATION, tv.getText());
		startActivity(i);
		Log.i(TAG, "Exit");
	}

}