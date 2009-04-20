package edu.nyu.cs.omnidroid.tests;

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

/**
 * Activity used to present a list of filters to apply to this OmniHandler. Filters allow the user
 * to only apply this OmniHandler if the filter data matches the data in provided by the Event
 * that was caught.
 * 
 * @author acase
 * 
 */
public class FiltersAddData extends ListActivity {

  // Standard Menu options (Android menus require int, so no enums)
  private static final int MENU_HELP = 0;

  /**
   * Creates the activity
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    AGParser ag = new AGParser(getApplicationContext());
    super.onCreate(savedInstanceState);

    // Getting the Events from AppConfig
    ArrayList<String> appNames;
    appNames = ag.readLines(AGParser.KEY_APPLICATION);

    // Populate our layout with applications
    setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, appNames));
    getListView().setTextFilterEnabled(true);

    Log.i(this.getLocalClassName(), "onCreate exit");
  }

  /*
   * (non-Javadoc)
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
   */
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    TextView tv = (TextView) v;
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), Filters.class);
    i.putExtra(AGParser.KEY_APPLICATION, tv.getText());
    // For each filter pass it to the next page
    // TODO (acase):
    //i.putExtra(UGParser.KEY_Filter, tv.getText());
    startActivity(i);
    // Pass this data back to Filters
    finish();
  }

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