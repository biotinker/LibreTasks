package edu.nyu.cs.omnidroid.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class ActionThrowerActions extends ListActivity {
    private static final String TAG = "EventListActivity";
        
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, EVENTS));
        getListView().setTextFilterEnabled(true);

        final Intent intent = getIntent();

        // Do some setup based on the action being performed.

        final String action = intent.getAction();
        if (Intent.ACTION_CALL.equals(action)) {
            // The new entry was created, so assume all will end well and
        	Log.i(TAG, "Found ACTION_CALL");
        } else {
        	Log.i(TAG, "Unknown ACTION");
        }
        // Set the layout for this activity.  You can find it in res/layout/
        //setContentView(R.layout.event_list_activity);
        
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, EVENTS));
        getListView().setTextFilterEnabled(true);
        Log.i(TAG, "Success");

    }

    // TODO: Pull this from the Package Manager
    // TODO: Filter by only apps that contain actions
    static final String[] EVENTS = new String[] {
        "Email Received", "Email was Deleted", "Email was Moved" };

}