package edu.nyu.cs.omnidroid.ui;

import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * This class will present an interface for the user to input data that they want the
 * <code>ActionThrowerActions</code> to access.
 * 
 * @author acase
 */
public class ActionThrowerData extends Activity implements OnClickListener {
  private String eventApp;
  private String eventName;
  private String filterType;
  private String filterData;
  private String throwerApp;
  private String throwerName;
  private EditText appData;
  private EditText instanceName;

  // Standard Menu options (Android menus require int, so no enums)
  private static final int MENU_HELP = 0;

  /**
   *  Ask the user for data about the OmniHandler
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
    instanceName = (EditText) findViewById(R.id.Iname);

    // Listen for the save button click
    save.setOnClickListener(this);
  }

  /*
   * (non-Javadoc)
   * Add OmniHandler to OmniDroid if appropriate
   *  
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  public void onClick(View v) {
    // Get data from our user
    String iName = instanceName.getText().toString();
    String aData = appData.getText().toString();

    // Add OmniHandler to OmniDroid
    if (iName.length() > 0 && aData.length() > 0) {

      // Add OmniHandler to the CP
      ContentValues values = new ContentValues();
      values.put("i_name", iName);
      values.put("a_data", aData);
      Uri uri = getContentResolver().insert(
          Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"), values);

      // Add OmniHandler to the UGConfig
      UGParser ug = new UGParser(getApplicationContext());
      HashMap<String, String> HM = new HashMap<String, String>();
      HM.put(UGParser.KEY_InstanceName, iName);
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

      // Go back to our start page
      Intent i = new Intent();
      i.setClass(this.getApplicationContext(), Overview.class);
      startActivity(i);
      finish();
    } else {
      Toast.makeText(getBaseContext(), "Please enter both an ", Toast.LENGTH_SHORT).show();
    }
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