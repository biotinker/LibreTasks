package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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
 * Presents a list of possible actions that the selected <code>ActionThrower</code> could throw as
 * it's action to perform for that OmniHandler.
 * 
 * @author acase
 */
public class ActionThrowerActions extends ListActivity {
  private static AGParser ag;
  private String eventApp = null;
  private String eventName = null;
  private String filterType = null;
  private String filterData = null;
  private String throwerApp = null;

  // Standard Menu options (Android menus require int, so no enums)
  private static final int MENU_HELP = 0;

  // Activity results
  private static final int ADD_RESULT = 1;
  private static final int RESULT_ADD_SUCCESS = 1;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    // Android Boilerplate
    super.onCreate(savedInstanceState);

    // Initialize our AGParser
    ag = new AGParser(getApplicationContext());

    // See what application we want to handle events for from the
    // intent data passed to us.
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    if (extras != null) {
      eventApp = extras.getString(AGParser.KEY_APPLICATION);
      eventName = extras.getString(UGParser.KEY_EventName);
      // TODO(acase): Allow more than one filter
      filterType = extras.getString(UGParser.KEY_FilterType);
      filterData = extras.getString(UGParser.KEY_FilterData);
      throwerApp = extras.getString(UGParser.KEY_ActionApp);
    }

    // Getting the Actions from AppConfig
    ArrayList<HashMap<String, String>> eventList = ag.readActions(throwerApp);
    Iterator<HashMap<String, String>> i1 = eventList.iterator();
    ArrayList<StringMap> stringvalues = new ArrayList<StringMap>();
    while (i1.hasNext()) {
      HashMap<String, String> HM1 = i1.next();
      HM1.toString();
      String splits[] = HM1.toString().split(",");
      for (int cnt = 0; cnt < splits.length; cnt++) {
        String pairs[] = splits[cnt].split("=");
        String key = pairs[0].replaceFirst("\\{", "");
        String entry = pairs[1].replaceFirst("\\}", "");
        stringvalues.add(new StringMap(key, entry));
      }
    }

    // Build our list of Actions
    ArrayAdapter<StringMap> arrayAdapter = new ArrayAdapter<StringMap>(this,
        android.R.layout.simple_list_item_1, stringvalues);
    setListAdapter(arrayAdapter);
    getListView().setTextFilterEnabled(true);
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
    i.setClass(this.getApplicationContext(), ActionThrowerData.class);
    //i.setClass(this.getApplicationContext(), ActionThrowerData2.class);
    //i.setClass(this.getApplicationContext(), ActionThrowerDataURI.class);
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EventName, eventName);
    if ((filterType != null) && (filterData != null)) {
      i.putExtra(UGParser.KEY_FilterType, filterType);
      i.putExtra(UGParser.KEY_FilterData, filterData);
    }
    i.putExtra(UGParser.KEY_ActionApp, throwerApp);
    i.putExtra(UGParser.KEY_ActionName, sm.getKey());
    startActivityForResult(i, ADD_RESULT);
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
   */
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
    case ADD_RESULT:
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