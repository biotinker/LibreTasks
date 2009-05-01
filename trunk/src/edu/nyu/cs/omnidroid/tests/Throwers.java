package edu.nyu.cs.omnidroid.tests;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
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
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.core.CP;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * List current applications that should be called when the event passed to us occurs. Allow the
 * user to add more applications to this list.
 * 
 * @author acase
 * 
 */
public class Throwers extends Activity implements OnClickListener {

  // Intent data passed along
  private String eventApp;
  private String eventName;
  private String filterType;
  private String filterData;
  private String throwerApp;
  private String throwerAction;
  private String throwerData1;
  private String throwerData2;

  // Menu Options of the Context variety (Android menus require int)
  private static final int MENU_EDIT = 0;
  private static final int MENU_DELETE = 1;
  // Menu options of the Standard variety (Android menus require int)
  private static final int MENU_ADD = 2;
  private static final int MENU_HELP = 3;

  // Debugging
  private static final String TAG = "Throwers";

  // List of applications to notify
  private static final int MAX_NUM_THROWERS = 1;
  private ListView throwerListView;  
  private ArrayList<String> throwerList = new ArrayList<String>();

  // Name to save this OmniHandler as
  private String omniHandlerName;

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.throwers);
    throwerListView = (ListView) findViewById(R.id.throwers_list);

    // Present the save button
    Button save = (Button) findViewById(R.id.save);
    save.setOnClickListener(this);

    // See what application we want to handle events for from the
    getIntentData(getIntent());

    // Present an updated list of filters we have applied
    updateList();
  }

  private void updateList() {
    // Populate a list of active filters
    if (throwerApp != null) {
      throwerList.add(throwerApp);
    }

    // Display a list of active filters
    ArrayAdapter<String> listadpt = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, throwerList);
    throwerListView.setAdapter(listadpt);
    throwerListView.setTextFilterEnabled(true);
    registerForContextMenu(throwerListView);
  }

  private void getIntentData(Intent i) {
    // intent data passed to us.
    Bundle extras = i.getExtras();
    if (extras != null) {
      eventApp = extras.getString(AGParser.KEY_APPLICATION);
      eventName = extras.getString(UGParser.KEY_EventName);
      filterType = extras.getString(UGParser.KEY_FilterType);
      filterData = extras.getString(UGParser.KEY_FilterData);
      throwerApp = extras.getString(UGParser.KEY_ActionApp);
      throwerAction = extras.getString(UGParser.KEY_ActionName);
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
    Uri data = getIntent().getData();
    editThrower(data, id);
  }

  /**
   * Creates the options menu items
   * 
   * @param menu
   *          - the options menu to create
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    // TODO(acase): Allow more than MAX_NUM_DATA
    if (throwerList.size() < MAX_NUM_THROWERS) {
      // Don't allow more than MAX_NUM_DATA per thrower for now
      menu.add(0, MENU_ADD, 0, R.string.add_thrower).setIcon(android.R.drawable.ic_menu_add);
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
      addThrower();
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
    // TODO(acase): Edit
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
      editThrower(data, info.id);
      return true;
    case MENU_DELETE:
      deleteThrower(data, info.id);
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
  private void deleteThrower(Uri data, long l) {
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
  private void editThrower(Uri data, long l) {
  }

  /**
   * Add a new filter to this OmniHandler
   * 
   * @return void
   */
  private void addThrower() {
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), ThrowerAdd.class);
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EventName, eventName);
    i.putExtra(UGParser.KEY_FilterType, filterType);
    i.putExtra(UGParser.KEY_FilterData, filterData);
    startActivityForResult(i, Constants.RESULT_ADD_THROWER);
  }

  /**
   * Call our Help dialog
   * 
   * @return void
   */
  private void help() {
    Builder help = new AlertDialog.Builder(this);
    // TODO(acase): Move to some kind of resource
    String help_msg = "Use the 'Menu' button followed by 'Add Thrower' to add throwers that "
        + "the OmniHandler will fire of when the previously selected event occurs.\n"
        + "When done adding throwers, select 'Next' to proceed.";
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
  /*
   * (non-Javadoc) Add OmniHandler to OmniDroid if appropriate
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  public void onClick(View v) {
    LayoutInflater factory = LayoutInflater.from(this);
    final View textEntryView = factory.inflate(R.layout.save_dialog, null);
    Builder save_dialog = new AlertDialog.Builder(this);
    save_dialog.setIcon(android.R.drawable.ic_dialog_alert);
    save_dialog.setTitle(R.string.save_as);
    save_dialog.setView(textEntryView);
    save_dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
      }
    });
    save_dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        EditText v = (EditText) textEntryView.findViewById(R.id.save_name);
        omniHandlerName = v.getText().toString();
        save();
      }
    });
    save_dialog.create();
    save_dialog.show();
  }

  /**
   * Save this OmniHandler.
   * 
   * Side effects:
   *   + Adds data to Content Provider
   *   + Adds data to UGParser 
   */
  private void save() {
    // Add OmniHandler to OmniDroid
    if (omniHandlerName.length() < 1) {
      Toast.makeText(getBaseContext(), R.string.missing_name, Toast.LENGTH_LONG).show();
      return;
    }
    if (throwerApp == null) {
      Toast.makeText(getBaseContext(), R.string.missing_thrower, Toast.LENGTH_LONG).show();
      return;
    }

    ContentValues values;
    Uri uri1 = null;
    Uri uri2 = null;
    
    if (throwerData1 != null) {
      // Add OmniHandler Data1 to the CP
      values = new ContentValues();
      values.put(CP.INSTANCE_NAME, omniHandlerName);
      values.put(CP.ACTION_DATA, throwerData1.toString());
      uri1 = getContentResolver().insert(CP.CONTENT_URI, values);
    }
    
    if (throwerData2 != null) {
      // Add OmniHandler Data1 to the CP
      values = new ContentValues();
      values.put(CP.INSTANCE_NAME, omniHandlerName);
      values.put(CP.ACTION_DATA, throwerData1.toString());
      uri2 = getContentResolver().insert(CP.CONTENT_URI, values);
    }

    // Add OmniHandler to the UGConfig
    UGParser ug = new UGParser(getApplicationContext());
    HashMap<String, String> HM = new HashMap<String, String>();
    HM.put(UGParser.KEY_InstanceName, omniHandlerName);
    HM.put(UGParser.KEY_EventName, eventName);
    HM.put(UGParser.KEY_EventApp, eventApp);
    HM.put(UGParser.KEY_ActionApp, throwerApp);
    HM.put(UGParser.KEY_ActionName, throwerAction);
    HM.put(UGParser.KEY_EnableInstance, "True");
    if (uri1 != null) HM.put(UGParser.KEY_ActionData, uri1.toString());
    if (uri2 != null) HM.put(UGParser.KEY_ActionData2, uri2.toString());
    if ((filterType != null) && (filterData != null)) {
      HM.put(UGParser.KEY_FilterType, filterType);
      HM.put(UGParser.KEY_FilterData, filterData);
    }
    ug.writeRecord(HM);

    // Added by Pradeep to restart the service to register new
    // IntentFilter
    Intent i = new Intent();
    i.setAction("OmniRestart");
    sendBroadcast(i);

    // Go back to our start page
    i = new Intent();
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
    case Constants.RESULT_ADD_THROWER:
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