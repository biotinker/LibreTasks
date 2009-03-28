package edu.nyu.cs.omnidroid.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ActionThrower extends ListActivity {
    private static final String TAG = "EventListActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("<onCreate>", "Test");
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, APPLICATIONS));
        getListView().setTextFilterEnabled(true);
        Log.i("<onCreate>", "Test");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.i(TAG, "Enter");
        Log.i(TAG, "listview: " + l);
        Log.i(TAG, "view: " + v);
        Log.i(TAG, "position: " + position);
        Log.i(TAG, "id: " + id);
        
        //Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
        //String action = getIntent().getAction();
        // Launch activity to view/edit the currently selected item
        // TODO: Build URI dynamically
        startActivity(new Intent(Intent.ACTION_CALL));
        Log.i(TAG, "Exit");
    }

    // TODO: Pull this from the Package Manager
    // TODO: Filter by only apps that contain actions
    static final String[] APPLICATIONS = new String[] {
        "Email", "Messaging", "Dialer", "Weather" };

}