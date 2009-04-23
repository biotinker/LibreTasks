package edu.nyu.cs.omnidroid.tests;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Activity used to present a list of filters to apply to this OmniHandler.
 * Filters allow the user to only apply this OmniHandler if the filter data
 * matches the data in provided by the Event that was caught.
 * 
 * @author acase
 * 
 */
public class Filters extends ListActivity {
	// Intent Data passed along
	private String appName = null;
	private String eventName = null;
	private ArrayList<String> filterType = null;
	private ArrayList<String> filterData = null;

	// Context Menu Options (Android menus require int, so no enums)
	private static final int MENU_EDIT = 0;
	private static final int MENU_DELETE = 1;

	// Standard Menu options (Android menus require int, so no enums)
	private static final int MENU_ADD = 2;
	private static final int MENU_HELP = 3;
	
	// Debugging
	private static final String TAG = "Filters";

	// List Data
	//private ListView list;
	
	/**
	 * Creates the activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String fType;
		String fData;
		
		// See what application we want to handle events for from the
		// intent data passed to us.
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) {
			appName = extras.getString(AGParser.KEY_APPLICATION);
			eventName = extras.getString(UGParser.KEY_EventName);
			fType = extras.getString(UGParser.KEY_FilterType);
			fData = extras.getString(UGParser.KEY_FilterData);
		}

		//if (fType !)
		//filterType.add();
		//filterData.add();

		// Get a list of active filters
		// If editing, then pull from UGParser
		// ArrayList<String> filterData = ug.readLines(UGParser.KEY_FilterData);

		// Display possible actions
		// TODO (acase): Display currently set filters
		// TODO (acase): Present "Next" button
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, filterData));
		getListView().setTextFilterEnabled(true);

		// Add a context menu for the list
		registerForContextMenu(getListView());

	}

	/*
	 * (non-Javadoc) If an item is selected, go to it's edit page.
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String s = (String) l.getAdapter().getItem(position);
		// TODO(acase): Make the edit pages for this
		TextView tv = (TextView) v;
		Intent i = new Intent();
		i.setClass(this.getApplicationContext(), FiltersAdd.class);
		i.putExtra(AGParser.KEY_APPLICATION, appName);
		i.putExtra(UGParser.KEY_EventName, eventName);
		i.putExtra(UGParser.KEY_FilterType, s);
		// i.putExtra(UGParser.KEY_FilterID, filterID);
		// For each filter pass it to the next page
		// TODO (acase): Put each filter into the extras
		startActivity(i);
		finish();
	}

	// TODO: If

	/**
	 * Creates the options menu items
	 * 
	 * @param menu
	 *            - the options menu to create
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ADD, 0, R.string.add_filter).setIcon(
				android.R.drawable.ic_menu_add);
		menu.add(0, MENU_HELP, 0, R.string.help).setIcon(
				android.R.drawable.ic_menu_help);
		return true;
	}

	/**
	 * Handles menu item selections
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADD:
			Add_Filter();
			return true;
		case MENU_HELP:
			Help();
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
	 * android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menuInfo = new AdapterContextMenuInfo(v, 0, 0);
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, MENU_EDIT, 0, R.string.edit);
		menu.add(0, MENU_DELETE, 0, R.string.del);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        try {
             info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            Log.e(TAG, "bad menuInfo", e);
            return false;
        }

		ContextMenuInfo menuInfo = (ContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case MENU_EDIT:
			editFilter(item);
			return true;
		case MENU_DELETE:
	        Uri data = getIntent().getData();
			deleteFilter(data, info.id);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	/**
	 * TODO(acase): Allow deletion of already set filters
	 * @param data 
	 * @param l - the item id selected
	 * @return void
	 */
	private void deleteFilter(Uri data, long l) {
		//getAdapter().getItem(item.)
		//item.getItemId(
	}

	/**
	 * TODO(acase): Allow edit of already set filters
	 *  
	 * @param item - the item selected
	 * @return void
	 */
	private void editFilter(MenuItem item) {
	}

	/**
	 * Add a new filter to this OmniHandler
	 * @return void
	 */
	private void Add_Filter() {
		Intent i = new Intent();
		i.setClass(this.getApplicationContext(), FiltersAdd.class);
		i.putExtra(AGParser.KEY_APPLICATION, appName);
		i.putExtra(UGParser.KEY_EventName, eventName);
		startActivity(i);
	}

	/**
	 * Call our Help dialog
	 * @return void
	 */
	private void Help() {
		// TODO (acase): Create a help dialog for this activity
	}

}