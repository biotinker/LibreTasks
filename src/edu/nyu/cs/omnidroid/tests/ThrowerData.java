package edu.nyu.cs.omnidroid.tests;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.StringMap;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Activity used to present a list of filters to apply to this OmniHandler. Filters allow the user
 * to only apply this OmniHandler if the filter data matches the data in provided by the Event that
 * was caught.
 * 
 * @author acase
 * 
 */
public class ThrowerData extends Activity implements OnClickListener {

  // Intent data passed along
  private String eventApp;
  private String eventName;
  private String filterType;
  private String filterData;
  private String throwerApp;
  private String throwerName;
  private String throwerData1;
  private String throwerData2;

  // Data Storage
  private static final int MAX_NUM_DATA = 2;
  private ListView dataListView;
  private ArrayList<String> dataList = new ArrayList<String>();

  // Menu Options of the Context variety (Android menus require int)
  private static final int MENU_EDIT = 0;
  private static final int MENU_DELETE = 1;
  // Menu options of the Standard variety (Android menus require int)
  private static final int MENU_ADD = 2;
  private static final int MENU_HELP = 3;

  // Debugging
  private static final String TAG = "ThrowerData";

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.thrower_data);
    dataListView = (ListView) findViewById(R.id.data_list);

    // Present "Done" button
    Button done = (Button) findViewById(R.id.thrower_data_done_button);
    done.setOnClickListener(this);

    // Get the data passed to us
    getIntentData(getIntent());

    // Present an updated list of data
    updateList();
  }

  private void updateList() {
    // Clear the List
    dataList.clear();

    // Populate the list
    if (throwerData1 != null) {
      dataList.add(throwerData1);
    }
    if (throwerData2 != null) {
      dataList.add(throwerData2);
    }

    // TODO(acase): If editing, then pull from UGParser
    // ArrayList<String> filterData = ug.readLines(UGParser.KEY_FilterData);

    // Display a list of active filters
    ArrayAdapter<String> listadpt = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, dataList);
    dataListView.setAdapter(listadpt);
    dataListView.setTextFilterEnabled(true);
    registerForContextMenu(dataListView);
  }

  /**
   * Get the intent data passed to us.
   * 
   * Side effects - stores the data in class variables
   * 
   * @param i
   *          - intent that started this activity
   */
  private void getIntentData(Intent i) {
    Bundle extras = i.getExtras();
    if (extras != null) {
      eventApp = extras.getString(AGParser.KEY_APPLICATION);
      eventName = extras.getString(UGParser.KEY_EventName);
      // TODO(acase): Allow more than one filter
      filterType = extras.getString(UGParser.KEY_FilterType);
      filterData = extras.getString(UGParser.KEY_FilterData);
      throwerApp = extras.getString(UGParser.KEY_ActionApp);
      throwerName = extras.getString(UGParser.KEY_ActionName);
      throwerData1 = extras.getString(UGParser.KEY_ActionData);
      throwerData2 = extras.getString(UGParser.KEY_ActionData2);
    }
  }

  /*
   * (non-Javadoc) If an item is selected, go to it's edit page.
   * 
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int,
   * long)
   */
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Uri datum = getIntent().getData();
    editDatum(datum, id);
  }

  /**
   * Creates the options menu items
   * 
   * @param menu
   *          - the options menu to create
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    // TODO(acase): Allow more than MAX_NUM_DATA
    if (dataList.size() < MAX_NUM_DATA) {
      // Don't allow more than MAX_NUM_DATA per thrower for now
      menu.add(0, MENU_ADD, 0, R.string.add_data).setIcon(android.R.drawable.ic_menu_add);
    }

    menu.add(0, MENU_HELP, 0, R.string.help).setIcon(android.R.drawable.ic_menu_help);

    return true;
  }

  /**
   * Handles menu item selections
   */
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_ADD:
      addDatum();
      return true;
    case MENU_HELP:
      help();
      return true;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View,
   * android.view.ContextMenu.ContextMenuInfo)
   */
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    // menu.add(0, MENU_EDIT, 0, R.string.edit);
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

    Uri data = getIntent().getData();
    switch (item.getItemId()) {
    case MENU_EDIT:
      editDatum(data, info.id);
      return true;
    case MENU_DELETE:
      deleteDatum(data, info.id);
      return true;
    default:
      return super.onContextItemSelected(item);
    }
  }

  /**
   * FIXME(acase): Allow deletion of already set filters
   * 
   * @param data
   * @param l
   *          - the item id selected
   * @return void
   */
  private void deleteDatum(Uri datum, long l) {
    // getAdapter().getItem(item.)
    // item.getItemId(
  }

  /**
   * TODO(acase): Allow edit of already set filters
   * 
   * @param data
   * @param l
   *          - the item id selected
   * @return void
   */
  private void editDatum(Uri datum, long l) {
  }

  /**
   * Add a new filter to this OmniHandler
   * 
   * @return void
   */
  private void addDatum() {
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), ThrowerDatumAdd.class);
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EventName, eventName);
    // TODO(acase): Allow more than one filter
    i.putExtra(UGParser.KEY_FilterType, filterType);
    i.putExtra(UGParser.KEY_FilterData, filterData);
    i.putExtra(UGParser.KEY_ActionApp, throwerApp);
    i.putExtra(UGParser.KEY_ActionName, throwerName);
    if (throwerData1 != null) {
      i.putExtra(UGParser.KEY_ActionData, throwerData1);
    }
    startActivityForResult(i, Constants.RESULT_ADD_DATUM);
  }

  /**
   * Call our Help dialog
   * 
   * @return void
   */
  private void help() {
    Builder help = new AlertDialog.Builder(this);
    // TODO(acase): Move to some kind of resource
    String help_msg = "TODO";
    help.setTitle(R.string.help);
    help.setIcon(android.R.drawable.ic_menu_help);
    help.setMessage(Html.fromHtml(help_msg));
    help.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
      }
    });
    help.show();
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  public void onClick(View v) {
    // Go back to our start page
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), ThrowerAction.class);
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EventName, eventName);
    if ((filterType != null) && (filterData != null)) {
      i.putExtra(UGParser.KEY_FilterType, filterType);
      i.putExtra(UGParser.KEY_FilterData, filterData);
    }
    i.putExtra(UGParser.KEY_ActionApp, throwerApp);
    i.putExtra(UGParser.KEY_ActionName, throwerName);
    if (throwerData1 != null) {
      i.putExtra(UGParser.KEY_ActionData, throwerData1);
    }
    if (throwerData2 != null) {
      i.putExtra(UGParser.KEY_ActionData2, throwerData2);
    }
    setResult(Constants.RESULT_SUCCESS, i);
    finish();
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
   */
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
    case Constants.RESULT_ADD_DATUM:
      switch (resultCode) {
      case Constants.RESULT_SUCCESS:
        getIntentData(data);
        updateList();
        break;
      }
      break;
    }
  }
}