package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Overview is the main UI Launcher for the OmniDroid Application. It presents
 * all the current OmniHandlers as well as a way to add/delete/edit them.
 * 
 */
public class Overview extends Activity implements OnClickListener {

	// Context Menu Options (Android menus require int, so no enums)
	private static final int MENU_EDIT = 0;
	private static final int MENU_DELETE = 1;

	// Standard Menu options (Android menus require int, so no enums)
	private static final int MENU_ADD = 2;
	private static final int MENU_SETTINGS = 3;
	private static final int MENU_HELP = 4;
	private static final int MENU_TESTS = 5;
	private static final int MENU_ABOUT = 6;

    // Return value for our subactivities
	private static final int RESULT_ADD = 1;
	private static final int RESULT_SETTING = 2;
	private static final int RESULT_ADD_SUCCESS = 1;

	// User Config Parser
	private static UGParser ug;

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Debug.startMethodTracing("OmniDroid");
		// Create our Activity
		super.onCreate(savedInstanceState);

		// Get a list of our current OmniHandlers
		ug = new UGParser(getApplicationContext());
		ArrayList<View> rowList = new ArrayList<View>();
		ArrayList<HashMap<String, String>> userConfigRecords = ug.readRecords();
		Iterator<HashMap<String, String>> i = userConfigRecords.iterator();

		// Add current OmniHandlers to our list
		while (i.hasNext()) {
			HashMap<String, String> HM1 = i.next();

			// Build our button
			Button button = new Button(this);
			button.setText(HM1.get(UGParser.KEY_InstanceName));
			button.setCursorVisible(true);
			button.setTag(HM1.get((String) UGParser.KEY_InstanceName));
			//button.setOnClickListener(this);

			// Build our checkbox
			Log.i(this.getLocalClassName().toString(), "Adding a checkbox");
			CheckBox checkbox = new CheckBox(this);
			checkbox.setGravity(Gravity.CENTER);
			checkbox.setClickable(true);
			checkbox.setTag(HM1.get((String) UGParser.KEY_InstanceName));
			checkbox.setOnClickListener(this);
			checkbox.setEnabled(true);
			if (HM1.get(UGParser.KEY_EnableInstance).equalsIgnoreCase("True")) {
				Log.d(this.getLocalClassName(), "Enabled=true");
				checkbox.setChecked(true);
			} else {
				Log.d(this.getLocalClassName(), "Enabled=false");
				checkbox.setChecked(false);
			}

			// Add a context menu for the row
			registerForContextMenu(button);
			
			// Build our table row
			TableRow row = new TableRow(this);
			row.addView(button);
			row.addView(checkbox);
			rowList.add(row);
		}

		// Build our OmniHandler display table
		TableLayout table_layout = new TableLayout(this);
		table_layout.setColumnStretchable(0, true);
		for (View rows : rowList) {
			rows.setPadding(2, 2, 2, 2);
			table_layout.addView(rows);
		}

		// Add our table to a scrollpane
		ScrollView scrollPane = new ScrollView(this);
		scrollPane.addView(table_layout);
		setContentView(scrollPane);

	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	public void onDestroy() {
		super.onDestroy();
		//Debug.stopMethodTracing();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
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
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		switch (item.getItemId()) {
		case MENU_EDIT:
			editHandler(info.id);
			return true;
		case MENU_DELETE:
			deleteHandler(info.id);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RESULT_ADD:
			switch (resultCode) { 
			case RESULT_ADD_SUCCESS:
				// TODO(acase): Repopulate our list if needed
				break;
			}
          break;
		case RESULT_SETTING:
          // TODO(acase): Repopulate our list if needed
          break;
		}
     }

	/**
	 * @param l
	 *            of the menu item
	 */
	private void deleteHandler(long l) {
		// TODO (acase): Present delete confirmation dialog
		Object data = getIntent().getData();
		
		Toast.makeText(this.getBaseContext(),
				"Delete OmniHandler Selected", 5).show();
		// TODO (acase): Delete from UGParser
		// TODO (acase): Delete from CP

		// Button selected = (Button) view;
		// ug.deleteRecord(selected.getText());
	}

	/**
	 * @param l
	 *            of the menu item
	 */
	private void editHandler(long l) {
		// TODO (acase): Call next activity
		Toast.makeText(this.getBaseContext(), "Edit OmniHandler Selected", 5)
				.show();
	}

	/**
	 * Creates the options menu items
	 * 
	 * @param menu
	 *            - the options menu to create
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ADD, 0, R.string.add_omnihandler).setIcon(
				android.R.drawable.ic_menu_add);
		menu.add(0, MENU_SETTINGS, 0, R.string.settings).setIcon(
				android.R.drawable.ic_menu_preferences);
		menu.add(0, MENU_HELP, 0, R.string.help).setIcon(
				android.R.drawable.ic_menu_help);
		menu.add(0, MENU_ABOUT, 0, R.string.about).setIcon(
				android.R.drawable.ic_menu_info_details);
		menu.add(0, MENU_TESTS, 0, R.string.tests).setIcon(
				android.R.drawable.ic_menu_manage);
		return true;
	}

	/**
	 * Handles menu item selections
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADD:
			AddOmniHandler();
			return true;
		case MENU_SETTINGS:
			Settings();
			return true;
		case MENU_HELP:
			Help();
			return true;
		case MENU_ABOUT:
			About();
			return true;
		case MENU_TESTS:
			RunTests();
			return true;
		}
		return false;
	}

	/**
	 * Presents the TestApp activity to run any available tests.
	 */
	private void RunTests() {
		Intent i = new Intent();
		i.setClass(this.getApplicationContext(),
				edu.nyu.cs.omnidroid.tests.TestApp.class);
		startActivity(i);
	}

	/**
	 * Add a new OmniHandler to OmniDroid
	 */
	private void AddOmniHandler() {
		Intent i = new Intent();
		i.setClass(this.getApplicationContext(),
				edu.nyu.cs.omnidroid.ui.EventCatcher.class);
		startActivityForResult(i, RESULT_ADD);
	}

	/**
	 * Call our Settings Activity
	 */
	private void Settings() {
		Intent i = new Intent();
		i.setClass(this.getApplicationContext(),
				edu.nyu.cs.omnidroid.ui.Settings.class);
		startActivityForResult(i, RESULT_SETTING);
	}

	/**
	 * Call our Help dialog
	 */
	private void Help() {
		// TODO (acase): Create a help dialog for this activity
	}

	/**
	 * Call our About dialog
	 */
	private void About() {
		// TODO (acase): Create an about dialog for our program
	}

	/*
	 * (non-Javadoc) Add OmniHandler to OmniDroid if appropriate
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
	  Log.d(this.getLocalClassName(), "Classname of v=" + v.getClass().getName());
	  //if (v.getClass().getName() == CheckBox.class.getName()) {
			// Handle "Enable" button clicked
			String instanceName = (String) v.getTag();
			CheckBox cb = (CheckBox) v;
			HashMap<String, String> HM = ug.readRecord(instanceName);
			if (cb.isChecked()) {
				Toast.makeText(this.getBaseContext(), "Enabling " + instanceName,
						5).show();
				HM.put(UGParser.KEY_EnableInstance, "true");
			} else {
				Toast.makeText(this.getBaseContext(), "Disabling " + instanceName,
						5).show();
				HM.put(UGParser.KEY_EnableInstance, "false");
			}
			ug.updateRecord(HM);

			// Restart the service
			Intent i = new Intent();
			i.setAction("OmniRestart");
			sendBroadcast(i);
		//} else if (v.getClass().getName() == Button.class.getName()) {
			// TODO (acase): Handle "Edit" button clicked
		//}
	}
}
