package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
public class Filters extends Activity implements OnClickListener {

  // Intent data passed along
  private String appName = null;
  private String eventName = null;
  private ArrayList<StringMap> filter = new ArrayList<StringMap>();
  private String fType;
  private String fData;

  // Context Menu Options (Android menus require int, so no enums)
  private static final int MENU_EDIT = 0;
  private static final int MENU_DELETE = 1;

  // Standard Menu options (Android menus require int, so no enums)
  private static final int MENU_ADD = 2;
  private static final int MENU_HELP = 3;

  // Debugging
  private static final String TAG = "Filters";

  // List Data
  private ListView list;

  // Activity results
  private static final int ADD_RESULT = 1;
  private static final int RESULT_ADD_SUCCESS = 1;

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.filters);
    list = (ListView) findViewById(R.id.filterlist);

    // Present "Next" button
    Button next = (Button) findViewById(R.id.next);
    next.setOnClickListener(this);

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
    // Populate a list of active filters
    if ((fType != null) && (fData != null)) {
      filter.add(new StringMap(fType,fData));
      //filterType[filterType.length] = fType;
      //filterData[filterData.length] = fData;
    }

    // TODO(acase): If editing, then pull from UGParser
    // ArrayList<String> filterData = ug.readLines(UGParser.KEY_FilterData);

    // Display a list of active filters
    ArrayAdapter<StringMap> listadpt = new ArrayAdapter<StringMap>(this,
        android.R.layout.simple_list_item_1, filter);
    list.setAdapter(listadpt);
    list.setTextFilterEnabled(true);
    registerForContextMenu(list);

  }

  /*
   * (non-Javadoc) If an item is selected, go to it's edit page.
   * 
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int,
   * long)
   */
  protected void onListItemClick(ListView l, View v, int position, long id) {
    StringMap sm = (StringMap) l.getAdapter().getItem(position);
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), FiltersAdd.class);
    i.putExtra(AGParser.KEY_APPLICATION, appName);
    i.putExtra(UGParser.KEY_EventName, eventName);
    i.putExtra(UGParser.KEY_FilterType, sm.getKey());
    i.putExtra(UGParser.KEY_FilterData, sm.getValue());
    startActivity(i);
    finish();
  }

  /**
   * Creates the options menu items
   * 
   * @param menu
   *          - the options menu to create
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    // super.onCreateOptionsMenu(menu);
    menu.add(0, MENU_ADD, 0, R.string.add_filter).setIcon(android.R.drawable.ic_menu_add);
    menu.add(0, MENU_HELP, 0, R.string.help).setIcon(android.R.drawable.ic_menu_help);
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
   * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View,
   * android.view.ContextMenu.ContextMenuInfo)
   */
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
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

    Uri data = getIntent().getData();
    switch (item.getItemId()) {
    case MENU_EDIT:
      editFilter(data, info.id);
      return true;
    case MENU_DELETE:
      deleteFilter(data, info.id);
      return true;
    default:
      return super.onContextItemSelected(item);
    }
  }

  /**
   * TODO(acase): Allow deletion of already set filters
   * 
   * @param data
   * @param l
   *          - the item id selected
   * @return void
   */
  private void deleteFilter(Uri data, long l) {
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
  private void editFilter(Uri data, long l) {
  }

  /**
   * Add a new filter to this OmniHandler
   * 
   * @return void
   */
  private void Add_Filter() {
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), FiltersAdd.class);
    i.putExtra(AGParser.KEY_APPLICATION, appName);
    i.putExtra(UGParser.KEY_EventName, eventName);
    startActivity(i);
    //TODO(acase): Destroy if necessary
  }

  /**
   * Call our Help dialog
   * 
   * @return void
   */
  private void Help() {
    // TODO (acase): Create a help dialog for this activity
  }

  /* (non-Javadoc)
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  public void onClick(View v) {
      // Handle "Next" button clicked by passing to the next UI page
      Intent i = new Intent();
      i.setClass(this.getApplicationContext(), ActionThrower.class);
      i.putExtra(AGParser.KEY_APPLICATION, appName);
      i.putExtra(UGParser.KEY_EventName, eventName);
      // TODO(acase): Allow more than one filter
      if ((fType != null) && (fData != null)) {
        i.putExtra(UGParser.KEY_FilterType, fType);
        i.putExtra(UGParser.KEY_FilterData, fData);
      }
  	startActivityForResult(i, ADD_RESULT);
  }

  /*
   * (non-Javadoc)
   * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
   */
  protected void onActivityResult (int requestCode, int resultCode, Intent data) {
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
}