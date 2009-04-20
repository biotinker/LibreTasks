package edu.nyu.cs.omnidroid.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
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

  /**
   * Creates the activity
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
    String appName = extras.getString(AGParser.KEY_APPLICATION);
    String eventName = extras.getString(UGParser.KEY_EventName);

    // Getting the list of Filters available from AppConfig
    ArrayList<String> filterList = ag.readFilters(appName, eventName);
    Iterator<String> i1 = filterList.iterator();

    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, filterList);
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
    TextView tv = (TextView) v;
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), FiltersAddData.class);
    i.putExtra(AGParser.KEY_APPLICATION, tv.getText());
    // For each filter pass it to the next page
    // TODO (acase): Once UGParser has support for FilterIDs, we can provide multiple filters.
    i.putExtra(UGParser.KEY_FilterType, tv.getText());
    startActivity(i);
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