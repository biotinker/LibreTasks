package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
 * Activity used to present a list of Applications which are registered in Omnidroid configuration.
 * The user can then select an application whose actions can be thrown using our Omnihandler.
 * 
 * @author acase
 * 
 */
public class ActionThrower extends ListActivity {
  private static AGParser ag;
  private String appName;
  private String eventName;
  private String filterType;
  private String filterData;

  // Standard Menu options (Android menus require int, so no enums)
  private static final int MENU_HELP = 0;

  // Activity results
  private static final int ADD_RESULT = 1;
  private static final int RESULT_ADD_SUCCESS = 1;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(this.getLocalClassName(), "onCreate");
    super.onCreate(savedInstanceState);

    // Initialize our AGParser
    ag = new AGParser(getApplicationContext());

    // See what application we want to handle events for from the
    // intent data passed to us.
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    if (extras != null) {
      appName = extras.getString(AGParser.KEY_APPLICATION);
      eventName = extras.getString(UGParser.KEY_EventName);
      // TODO(acase): Allow more than one filter
      filterType = extras.getString(UGParser.KEY_FilterType);
      filterData = extras.getString(UGParser.KEY_FilterData);
    }

    // Getting the Events from AppConfig
    ArrayList<String> pkgNames;
    pkgNames = ag.readLines(AGParser.KEY_APPLICATION);

    /*
     * // TODO(acase): Filter by only apps that contain actions for (pkgNames::location) { if
     * (ag.readActions(pkgName).size < 1) { pkgNames.remove(location); } }
     */

    // Display possible actions
    setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pkgNames));
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
    TextView tv = (TextView) v;
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), ActionThrowerActions.class);
    i.putExtra(AGParser.KEY_APPLICATION, appName);
    i.putExtra(UGParser.KEY_EventName, eventName);
    if ((filterType != null) && (filterData != null)) {
      i.putExtra(UGParser.KEY_FilterType, filterType);
      i.putExtra(UGParser.KEY_FilterData, filterData);
    }
    i.putExtra(UGParser.KEY_ActionApp, tv.getText());
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
    Builder help = new AlertDialog.Builder(this);
    // TODO(acase): Move to some kind of resource
    String help_msg = "Select the application which will be used to respond for the given event.";
    help.setTitle(R.string.help);
    help.setIcon(android.R.drawable.ic_menu_help);
    help.setMessage(Html.fromHtml(help_msg));
    help.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
      }
    });
    help.show();
  }

}