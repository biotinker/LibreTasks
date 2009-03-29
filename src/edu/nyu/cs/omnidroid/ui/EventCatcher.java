package edu.nyu.cs.omnidroid.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EventCatcher extends ListActivity {
    private static final String TAG = "EventCatcher";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
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
        
        //startActivity(new Intent(edu.nyu.cs.omnidroid.util.intents.eventcatcheractions.SELECT));
        //Intent i = new Intent("SELECT");
        //Intent i = new Intent(this., edu.nyu.cs.omnidroid.ui.ActionThrower.class)
        //i.setAction("SELECT");
        // TODO: Turn into a omnidroid specific intent
        startActivity(new Intent(Intent.ACTION_VIEW, getIntent().getData()));


        Log.i(TAG, "Exit");
    }

    // TODO: Pull this from the Package Manager
    // TODO: Filter by only apps that contain actions
    static final String[] APPLICATIONS = new String[] {
        "Email", "Messaging", "Dialer", "Weather" };

}