package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
  // Menu Options of the Context variety (Android menus require int)
  private static final int MENU_EDIT = 0;
  private static final int MENU_DELETE = 1;
  // Menu options of the Standar variety (Android menus require int)
  private static final int MENU_HELP = 3;

  // Intent data passed along
  private String eventApp;
  private String eventName;

  // Filters data
  private ArrayList<StringMap> filterList = new ArrayList<StringMap>();
  private ListView filterListView;
  private String fType;
  private String fData;

  // Maximum number of filters allowed
  // TODO(acase): Allow more than one filter in the future
  private static final int MAX_NUM_FILTERS = 1;

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.filters);
    filterListView = (ListView) findViewById(R.id.filters_list);

    // Activate the "Done" button
    Button done = (Button) findViewById(R.id.filters_done);
    done.setOnClickListener(this);
    // Activate the "Add" button
    Button add = (Button) findViewById(R.id.filters_add);
    add.setOnClickListener(this);

    // Get data passed to this activity
    getIntentData(getIntent());

    // Update our UI
    update();
  }

  /**
   * Update the UI
   */
  private void update() {
    // Populate a list of active filters
    if ((fType != null) && (fData != null)) {
      filterList.add(new StringMap(fType, fData));
    }

    // TODO(acase): If editing, then pull from UGParser
    // ArrayList<String> filterData = ug.readLines(UGParser.KEY_FilterData);

    // Display a list of active filters
    ArrayAdapter<StringMap> listadpt = new ArrayAdapter<StringMap>(this,
        android.R.layout.simple_list_item_1, filterList);
    filterListView.setAdapter(listadpt);
    filterListView.setTextFilterEnabled(true);
    registerForContextMenu(filterListView);

    // Disable/Enable the "Add Filter" button depending on if it's been max'ed out
    Button add = (Button) findViewById(R.id.filters_add);
    if (filterList.size() < MAX_NUM_FILTERS) {
      add.setEnabled(true);
    } else {
      add.setEnabled(false);
    }

  }

  /**
   * Store the intent data passed in
   * @param i
   */
  private void getIntentData(Intent i) {
    // intent data passed to us.
    Bundle extras = i.getExtras();
    if (extras != null) {
      eventApp = extras.getString(AGParser.KEY_APPLICATION);
      eventName = extras.getString(UGParser.KEY_EVENT_TYPE);
      fType = extras.getString(UGParser.KEY_FILTER_TYPE);
      fData = extras.getString(UGParser.KEY_FILTER_DATA);
    }

    // If there aren't any filters that we can apply ignore the filter page
    AGParser ag = new AGParser(this);
    ArrayList<String> filters = ag.readFilters(eventApp, eventName);
    if (filters.size() == 0) {
      onClick(findViewById(R.id.filters_done));
    }
  }

  /*
   * (non-Javadoc) If an item is selected, go to it's edit page.
   * 
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int,
   * long)
   */
  protected void onListItemClick(ListView l, View v, int position, long id) {
    editFilter(id);
  }

  /*
   * (non-Javadoc)
   * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_HELP, 0, R.string.help).setIcon(android.R.drawable.ic_menu_help);
    return true;
  }

  /*
   * (non-Javadoc)
   * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
   */
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
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
    menu.add(0, MENU_DELETE, 0, R.string.del);
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
   */
  public boolean onContextItemSelected(MenuItem item) {
    AdapterView.AdapterContextMenuInfo info;

    switch (item.getItemId()) {
    case MENU_EDIT:
      info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
      editFilter(info.id);
      return true;
    case MENU_DELETE:
      info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
      deleteFilter(info.id);
      return true;
    default:
      return super.onContextItemSelected(item);
    }
  }

  /**
   * FIXME(acase): Allow deletion of already set filters
   * @param l
   *          - the item id selected
   */
  private void deleteFilter(long l) {
  }

  /**
   * TODO(acase): Allow edit of already set filters
   * @param l
   *          - the item id selected
   */
  private void editFilter(long l) {
  }

  /**
   * Add a new filter to this OmniHandler
   */
  private void addFilter() {
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), FiltersAddType.class);
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EVENT_TYPE, eventName);
    startActivityForResult(i, Constants.RESULT_ADD_FILTER);
  }

  /**
   * Present the next UI page to the user
   */
  private void nextPage() {
    // Handle "Next" button clicked by passing to the next UI page
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), Actions.class);
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EVENT_TYPE, eventName);
    // TODO(acase): Allow more than one filter
    if ((fType != null) && (fData != null)) {
      i.putExtra(UGParser.KEY_FILTER_TYPE, fType);
      i.putExtra(UGParser.KEY_FILTER_DATA, fData);
    }
    startActivityForResult(i, Constants.RESULT_ADD_OMNIHANDER);
  }

  /**
   * Call our Help dialog
   * 
   * @return void
   */
  private void help() {
    Builder help = new AlertDialog.Builder(this);
    // TODO(acase): Move to some kind of resource
    String help_msg = "Use the 'Menu' button followed by 'Add Filter' to add filters that "
        + "limit the events caught by this OmniHandler to a subset of events.\n"
        + "When done adding filters, select 'Next' to proceed.";
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
    switch (v.getId()) {
    case R.id.filters_add:
      addFilter();
      break;
    case R.id.filters_done:
      nextPage();
      break;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
   */
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
    case Constants.RESULT_ADD_OMNIHANDER:
      switch (resultCode) {
      case Constants.RESULT_SUCCESS:
        setResult(resultCode, data);
        finish();
        break;
      }
      break;
    case Constants.RESULT_ADD_FILTER:
      switch (resultCode) {
      case Constants.RESULT_SUCCESS:
        getIntentData(data);
        update();
        break;
      }
      break;
    }
  }
}