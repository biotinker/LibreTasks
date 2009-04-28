package edu.nyu.cs.omnidroid.tests;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.core.CP;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * This class will present an interface for the user to input data that they want the
 * <code>ActionThrowerActions</code> to access.
 * 
 * @author acase
 */
public class ActionThrowerData extends Activity implements OnClickListener {
  // Intent Data
  private EditText appData;
  private String eventApp;
  private String eventName;
  private String filterType;
  private String filterData;
  private String throwerApp;
  private String throwerName;
  private String instanceName;

  // Activity results
  private static final int RESULT_ADD_SUCCESS = 1;

  // Standard Menu options (Android menus require int, so no enums)
  private static final int MENU_HELP = 0;

  /**
   * Ask the user for data about the OmniHandler
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.action_thrower_data);

    // Get data passed to us
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    eventApp = extras.getString(AGParser.KEY_APPLICATION);
    eventName = extras.getString(UGParser.KEY_EventName);
    // TODO(acase): Allow more than one filter
    filterType = extras.getString(UGParser.KEY_FilterType);
    filterData = extras.getString(UGParser.KEY_FilterData);
    throwerApp = extras.getString(UGParser.KEY_ActionApp);
    throwerName = extras.getString(UGParser.KEY_ActionName);

    // Setup our UI
    Button save = (Button) findViewById(R.id.save);
    appData = (EditText) findViewById(R.id.data);

    // Listen for the save button click
    save.setOnClickListener(this);
  }

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
        instanceName = v.getText().toString();
        save();
      }
    });
    save_dialog.create();
    save_dialog.show();
  }

  // private void save(String iName) {
  private void save() {
    // Get data from our user
    String aData = appData.getText().toString();

    // Add OmniHandler to OmniDroid
    if (instanceName.length() < 1) {
      Toast.makeText(getBaseContext(), R.string.missing_name, Toast.LENGTH_SHORT).show();
    } else if (aData.length() < 1) {
      Toast.makeText(getBaseContext(), R.string.missing_data, Toast.LENGTH_SHORT).show();
    } else {
      // Add OmniHandler to the CP
      ContentValues values = new ContentValues();
      values.put("i_name", instanceName);
      values.put("a_data", aData);
      Uri uri = getContentResolver().insert(CP.CONTENT_URI, values);

      // Add OmniHandler to the UGConfig
      UGParser ug = new UGParser(getApplicationContext());
      HashMap<String, String> HM = new HashMap<String, String>();
      HM.put(UGParser.KEY_InstanceName, instanceName);
      HM.put(UGParser.KEY_EventName, eventName);
      HM.put(UGParser.KEY_EventApp, eventApp);
      HM.put(UGParser.KEY_ActionName, throwerName);
      HM.put(UGParser.KEY_ActionApp, throwerApp);
      HM.put(UGParser.KEY_EnableInstance, "True");
      HM.put(UGParser.KEY_ActionData, uri.toString());
      if ((filterType != null) && (filterData != null)) {
        HM.put(UGParser.KEY_FilterType, filterType);
        HM.put(UGParser.KEY_FilterData, filterData);
      } else {
        HM.put(UGParser.KEY_FilterType, "");
        HM.put(UGParser.KEY_FilterData, "");
      }
      ug.writeRecord(HM);

      // Added by Pradeep to restart the service to register new
      // IntentFilter
      Intent i = new Intent();
      i.setAction("OmniRestart");
      sendBroadcast(i);

      // Go back to our start page
      i = new Intent();
      setResult(RESULT_ADD_SUCCESS, i);
      finish();
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
    // FIXME(acase): Move to some kind of resource
    String help_msg = "Select data to pass to the application responding to the event.";
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