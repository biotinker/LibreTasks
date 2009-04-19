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
import android.widget.TextView;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Activity used to present a list of filters to apply to this OmniHandler. Filters allow the user
 * to only apply this OmniHandler if the filter data matches the data in provided by the Event
 * that was caught.
 * 
 * @author acase
 * 
 */
public class Filters extends ListActivity {
  private String appName;
  private String eventName;

  // Standard Menu options (Android menus require int, so no enums)
  private static final int MENU_HELP = 0;

  /**
   * Creates the activity
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initialize our AGParser
    AGParser ag = new AGParser(getApplicationContext());

    // See what application we want to handle events for from the
    // intent data passed to us.
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    if (extras != null) {
      appName = extras.getString(AGParser.KEY_APPLICATION);
      eventName = extras.getString(UGParser.KEY_EventName);
    }

    // Get a list of active filters
    ArrayList<String> filtersType = ag.readLines(UGParser.KEY_FilterType);
    ArrayList<String> filtersData = ag.readLines(UGParser.KEY_FilterData);

    // Display possible actions
    // TODO (acase): Display currently set filters
    // TODO (acase): Present "Next" button
    setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filtersType));
    getListView().setTextFilterEnabled(true);
  }

  /*
   * (non-Javadoc)
   * If an item is selected, go to it's edit page.
   * 
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
   */
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), FiltersAdd.class);
    i.putExtra(AGParser.KEY_APPLICATION, appName);
    i.putExtra(UGParser.KEY_EventName, eventName);
    // For each filter pass it to the next page
    // TODO (acase): Put each filter into the extras
    //i.putExtra(UGParser.KEY_Filter, tv.getText());
    startActivity(i);
  }

  // TODO: If 

  /**
   * Creates the options menu items
   * 
   * @param menu
   *            - the options menu to create
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_HELP, 0, R.string.help).setIcon(
        android.R.drawable.ic_menu_help);
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