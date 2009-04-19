package edu.nyu.cs.omnidroid.ui;

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
 * Presents a list of possible actions that the selected <code>EventCatcher</code> could have
 * performed that we want to hook an OmniHandler onto.
 * 
 * @author acase
 */
public class EventCatcherActions extends ListActivity {
  private static AGParser ag;
  private String appName = null;

  // Standard Menu options (Android menus require int, so no enums)
  private static final int MENU_HELP = 0;

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
      appName = extras.getString(AGParser.KEY_APPLICATION);
    } else {
      // TODO (acase): Throw exception
    }

    

    // Getting the Events from AppConfig
    ArrayList<HashMap<String, String>> eventList = ag.readEvents(appName);
    Iterator<HashMap<String, String>> i1 = eventList.iterator();
    
    ArrayList<String> values = new ArrayList<String>();
    while (i1.hasNext()) {
      HashMap<String, String> HM1 = i1.next();
      //Toast.makeText(getBaseContext(), HM1.toString(), 5).show();
      // TODO (acase): We need a better way then accessing a hashmap
      values.addAll(HM1.values());
      //values.add(HM1.get("SMS_Received"));
      //values.add(HM1.get("SMS_Sent"));

    }
    if (values == null)
    {
      // TODO (acase): Throw exception
    }
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, values);
    setListAdapter(arrayAdapter);
    getListView().setTextFilterEnabled(true);

    Log.i(this.getLocalClassName(), "onCreate exit");
  }

  /*
   * (non-Javadoc)
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
   */
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), Filters.class);
    TextView tv = (TextView) v;
    i.putExtra(AGParser.KEY_APPLICATION, appName);
    i.putExtra(UGParser.KEY_EventName, tv.getText());
    startActivity(i);
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