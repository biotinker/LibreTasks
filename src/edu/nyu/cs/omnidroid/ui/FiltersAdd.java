package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.StringMap;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Activity used to present a list of filter types available to apply to this OmniHandler.
 * FilterTypes present the type of data that an OmniHandler can filter by. For example a
 * PhoneNumber, or an EmailAddress.
 * 
 * @author acase
 * 
 */
public class FiltersAdd extends ListActivity {

  // Standard Menu options (Android menus require int, so no enums)
  private static final int MENU_HELP = 0;

  // Application Configuration
  private AGParser ag;

  // User Selected Data
  String appName = null;
  String eventName = null;

  // Activity results
  //private static final int ADD_RESULT = 1;
  private static final int ADD_FILTER = 2;
  private static final int RESULT_ADD_SUCCESS = 1;

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initialize our AGParser
    ag = new AGParser(getApplicationContext());

    // See what application we want to handle events for from the
    // intent data passed to us.
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    appName = extras.getString(AGParser.KEY_APPLICATION);
    eventName = extras.getString(UGParser.KEY_EventName);

    // Getting the list of Filters available from AppConfig
    // ArrayList<StringMap> contentMap = new ArrayList<StringMap>();
    ArrayList<StringMap> contentMap = ag.readContentMap(appName);
    ArrayAdapter<StringMap> arrayAdapter = new ArrayAdapter<StringMap>(this,
        android.R.layout.simple_list_item_1, contentMap);
    setListAdapter(arrayAdapter);
    getListView().setTextFilterEnabled(true);
    Log.i(this.getLocalClassName(), "onCreate exit");
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int,
   * long)
   */
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    StringMap sm = (StringMap) l.getAdapter().getItem(position);
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), FiltersAddData.class);
    i.putExtra(AGParser.KEY_APPLICATION, appName);
    i.putExtra(UGParser.KEY_EventName, eventName);
    i.putExtra(UGParser.KEY_FilterType, sm.getKey());
    startActivityForResult(i, ADD_FILTER);
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
   */
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
    case ADD_FILTER:
      switch (resultCode) {
      case RESULT_ADD_SUCCESS:
        setResult(resultCode, data);
        finish();
        break;
      }
      break;
    }
  }

  /**
   * Creates the options menu items
   * 
   * @param menu
   *          - the options menu to create
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_HELP, 0, R.string.help).setIcon(android.R.drawable.ic_menu_help);
    return true;
  }

  /**
   * Handles menu item selections
   */
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_HELP:
      Help();
      return true;
    }
    return false;
  }

  /**
   * Call our Help dialog
   */
  private void Help() {
    // TODO (acase): Create a help dialog for this activity
  }

}