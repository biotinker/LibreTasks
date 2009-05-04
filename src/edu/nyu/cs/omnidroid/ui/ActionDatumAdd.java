package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.StringMap;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Activity used to present a list of filter types available to apply to this OmniHandler.
 * FilterTypes present the type of data that an OmniHandler can filter by. For example a
 * PhoneNumber, or an EmailAddress.
 * 
 * @author acase
 * 
 */
public class ActionDatumAdd extends Activity implements OnItemClickListener, OnClickListener {
  // Standard Menu options (Android menus require int)
  private static final int MENU_HELP = 0;
  
  // User Selected Data
  private String eventApp;
  private String eventName;
  private String filterType;
  private String filterData;
  private String throwerApp;
  private String throwerName;
  private String throwerData1;
  private String throwerData2;
  private int throwerData1Type;
  private int throwerData2Type;
  private String dataName;
  private int dataSelection;

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.action_datum_add);

    // Get intent data passed to us
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    eventApp = extras.getString(AGParser.KEY_APPLICATION);
    eventName = extras.getString(UGParser.KEY_EVENT_TYPE);
    filterType = extras.getString(UGParser.KEY_FILTER_TYPE);
    filterData = extras.getString(UGParser.KEY_FILTER_DATA);
    throwerApp = extras.getString(UGParser.KEY_ACTION_APP);
    throwerName = extras.getString(UGParser.KEY_ACTION_TYPE);
    throwerData1Type = extras.getInt(ActionData.KEY_DATA1_TYPE);
    throwerData2Type = extras.getInt(ActionData.KEY_DATA2_TYPE);
    throwerData1 = extras.getString(UGParser.KEY_ACTION_DATA1);
    throwerData2 = extras.getString(UGParser.KEY_ACTION_DATA2);
    dataSelection = (int) extras.getLong(ActionData.KEY_DATA_ID);
    dataName = extras.getString(ActionData.KEY_DATA_NAME);

    // Activate the "Done" button
    Button done = (Button) findViewById(R.id.thrower_datum_add_done);
    done.setOnClickListener(this);

    // Getting the list of data available from the eventApp's content map
    AGParser ag = new AGParser(getApplicationContext());
    ArrayList<StringMap> contentMap = ag.readContentMap(eventApp);

    // Filter down to things that match the data we want
    ArrayList<StringMap> filteredList = new ArrayList<StringMap>();
    for (StringMap s : contentMap) {
      if (s.toString().equals(dataName)) {
        filteredList.add(s);
      }
    }

    // Create a list of available selections
    ArrayAdapter<StringMap> arrayAdapter = new ArrayAdapter<StringMap>(this,
        android.R.layout.simple_list_item_1, filteredList);
    ListView lv = (ListView) findViewById(R.id.simple_list_view_01);
    lv.setOnItemClickListener(this);
    lv.setAdapter(arrayAdapter);
    lv.setTextFilterEnabled(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_HELP, 0, R.string.help).setIcon(android.R.drawable.ic_menu_help);
    return true;
  }

  /*
   * (non-Javadoc)
   * 
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

  /**
   * Call our Help dialog
   */
  private void help() {
    Builder help = new AlertDialog.Builder(this);
    // TODO(acase): Move to some kind of resource
    String help_msg = "Select the type of filter you wish to apply.";
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
   * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView,
   * android.view.View, int, long)
   */
  public void onItemClick(AdapterView<?> lv, View v, int position, long id) {
    StringMap sm = (StringMap) lv.getAdapter().getItem(position);
    Intent i = new Intent();
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EVENT_TYPE, eventName);
    i.putExtra(UGParser.KEY_FILTER_TYPE, filterType);
    i.putExtra(UGParser.KEY_FILTER_DATA, filterData);
    i.putExtra(UGParser.KEY_ACTION_APP, throwerApp);
    i.putExtra(UGParser.KEY_ACTION_TYPE, throwerName);
    if (dataSelection == 0) {
      i.putExtra(ActionData.KEY_DATA1_TYPE, ActionData.DATA_TYPE_SELECT);
      i.putExtra(UGParser.KEY_ACTION_DATA1, sm.getValue());
      if (throwerData2 != null) {
        i.putExtra(ActionData.KEY_DATA2_TYPE, throwerData2Type);
        i.putExtra(UGParser.KEY_ACTION_DATA2, throwerData2);
      }
    } else if (dataSelection == 1) {
      i.putExtra(ActionData.KEY_DATA2_TYPE, ActionData.DATA_TYPE_SELECT);
      i.putExtra(UGParser.KEY_ACTION_DATA2, sm.getValue());
      if (throwerData1 != null) {
        i.putExtra(ActionData.KEY_DATA1_TYPE, throwerData1Type);
        i.putExtra(UGParser.KEY_ACTION_DATA1, throwerData1);
      }
    } else {
      Toast.makeText(this, "Invalid selection", Toast.LENGTH_SHORT).show();
    }
    setResult(Constants.RESULT_SUCCESS, i);
    finish();
  }

  /**
   * Done button is clicked. Pull data from interface and report it back to caller.
   */
  private void done() {
    // Data to store
    EditText manualEntry = (EditText) findViewById(R.id.thrower_datum_add_manual);
    String throwerData = manualEntry.getText().toString();
    
    /* Save on the Actions save() function
    Uri uri = null;

    // Store the data in the ContentProvider
    String data = manualEntry.getText().toString();
    if (data.length() > 0) {
      ContentValues values = new ContentValues();
      // values.put("i_name", iname);
      values.put("a_data", data);
      uri = getContentResolver().insert(Uri.parse(CP.CONTENT_URI.toString()), values);
      Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(getBaseContext(), "Must enter some data, or select from the list",
          Toast.LENGTH_LONG).show();
    }
    */
    
    // Send back the result to the caller
    Intent i = new Intent();
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EVENT_TYPE, eventName);
    i.putExtra(UGParser.KEY_FILTER_TYPE, filterType);
    i.putExtra(UGParser.KEY_FILTER_DATA, filterData);
    i.putExtra(UGParser.KEY_ACTION_APP, throwerApp);
    i.putExtra(UGParser.KEY_ACTION_TYPE, throwerName);
    // Determine which field were storing data for and provide the appropriate data
    if (dataSelection == 0) {
      i.putExtra(ActionData.KEY_DATA1_TYPE, ActionData.DATA_TYPE_MANUAL);
      i.putExtra(UGParser.KEY_ACTION_DATA1, throwerData);
      if (throwerData2 != null) {
        i.putExtra(ActionData.KEY_DATA2_TYPE, throwerData2Type);
        i.putExtra(UGParser.KEY_ACTION_DATA2, throwerData2);
      }
    } else if (dataSelection == 1) {
      i.putExtra(ActionData.KEY_DATA2_TYPE, ActionData.DATA_TYPE_MANUAL);
      i.putExtra(UGParser.KEY_ACTION_DATA2, throwerData);
      if (throwerData1 != null) {
        i.putExtra(ActionData.KEY_DATA1_TYPE, throwerData1Type);
        i.putExtra(UGParser.KEY_ACTION_DATA1, throwerData1);
      }
    } else {
      Toast.makeText(this, "Invalid selection", Toast.LENGTH_SHORT).show();
    }
    setResult(Constants.RESULT_SUCCESS, i);
    finish();
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.thrower_datum_add_done:
      done();
      break;
    }
  }
}