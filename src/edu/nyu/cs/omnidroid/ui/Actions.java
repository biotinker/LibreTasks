package edu.nyu.cs.omnidroid.ui;

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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
public class Actions extends Activity implements OnClickListener {
  // Menu Options of the Context variety (Android menus require int)
  //TODO: private static final int MENU_EDIT = 0;
  private static final int MENU_DELETE = 1;
  // Menu options of the Standard variety (Android menus require int)
  private static final int MENU_HELP = 3;

  // Intent data passed along
  private String eventApp;
  private String eventName;
  private String filterType;
  private String filterData;
  private String throwerApp;
  private String throwerAction;
  private String throwerData1;
  private String throwerData2;

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
    setContentView(R.layout.actions);
    throwerListView = (ListView) findViewById(R.id.throwers_list);

    // Present the save button
    Button save = (Button) findViewById(R.id.throwers_save);
    save.setOnClickListener(this);
    Button add = (Button) findViewById(R.id.throwers_add);
    add.setOnClickListener(this);

    // See what application we want to handle events for from the
    getIntentData(getIntent());

    // Present an updated list of filters we have applied
    update();
  }

  private void update() {
    // Clear the list so that it's empty, then it can be filled
    throwerList.clear();
    
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

    // TODO(acase): Allow more than MAX_NUM_DATA
    // Disable the add button if we've reached the maximum number of thrower apps
    Button add = (Button) findViewById(R.id.throwers_add);
    if (throwerList.size() < MAX_NUM_THROWERS) {
      add.setEnabled(true);
    } else {
      add.setEnabled(false);
    }

    // Disable the save button if haven't added any throwers yet
    Button save = (Button) findViewById(R.id.throwers_save);
    if (throwerList.size() > 0 ) {
      save.setEnabled(true);
    } else {
      save.setEnabled(false);
    }
  }

  /**
   * Get the data passed in and store it in our class variables
   * @param i - intent containing data to fill our User Config with.
   */
  private void getIntentData(Intent i) {
    // intent data passed to us.
    Bundle extras = i.getExtras();
    if (extras != null) {
      eventApp = extras.getString(AGParser.KEY_APPLICATION);
      eventName = extras.getString(UGParser.KEY_EVENT_TYPE);
      filterType = extras.getString(UGParser.KEY_FILTER_TYPE);
      filterData = extras.getString(UGParser.KEY_FILTER_DATA);
      throwerApp = extras.getString(UGParser.KEY_ACTION_APP);
      throwerAction = extras.getString(UGParser.KEY_ACTION_TYPE);
      throwerData1 = extras.getString(UGParser.KEY_ACTION_DATA1);
      throwerData2 = extras.getString(UGParser.KEY_ACTION_DATA2);
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
    switch(item.getItemId()) {
    case MENU_DELETE:
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        deleteThrower(info.id);
        return true;
    }
    return super.onContextItemSelected(item);
  }

  /**
   * Delete the thrower from this omnihandler
   * 
   * @param id
   *          - the item id selected
   */
  private void deleteThrower(long id) {
    // Remove item from the list
    throwerList.remove((int)id);

    // Clear it from our intents
    // TODO(acase): this will need to be changed once we support more than one thrower
    throwerApp = null;
    throwerAction = null;
    throwerData1 = null;
    throwerData2 = null;

    // Update our UI
    update();
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
    i.setClass(this.getApplicationContext(), ActionAdd.class);
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EVENT_TYPE, eventName);
    i.putExtra(UGParser.KEY_FILTER_TYPE, filterType);
    i.putExtra(UGParser.KEY_FILTER_DATA, filterData);
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
    switch (v.getId()) {
    case R.id.throwers_add:
      addThrower();
      break;
    case R.id.throwers_save:
      saveDialog();
      break;
    }
  }

  private void saveDialog() {
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
        EditText v = (EditText) textEntryView.findViewById(R.id.save_dialog_name);
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
    HM.put(UGParser.KEY_INSTANCE_NAME, omniHandlerName);
    HM.put(UGParser.KEY_EVENT_TYPE, eventName);
    HM.put(UGParser.KEY_EVENT_APP, eventApp);
    HM.put(UGParser.KEY_ACTION_APP, throwerApp);
    HM.put(UGParser.KEY_ACTION_TYPE, throwerAction);
    HM.put(UGParser.KEY_ENABLE_INSTANCE, "True");
    if (uri1 != null) HM.put(UGParser.KEY_ACTION_DATA1, uri1.toString());
    if (uri2 != null) HM.put(UGParser.KEY_ACTION_DATA2, uri2.toString());
    if ((filterType != null) && (filterData != null)) {
      HM.put(UGParser.KEY_FILTER_TYPE, filterType);
      HM.put(UGParser.KEY_FILTER_DATA, filterData);
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
        update();
        break;
      }
      break;
    }
  }
}