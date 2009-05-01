package edu.nyu.cs.omnidroid.tests;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
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
public class ThrowerDatumAdd extends Activity implements OnItemClickListener {
  // Standard Menu options (Android menus require int)
  private static final int MENU_HELP = 0;

  // User Selected Data
  private String appName;
  private String eventName;
  private String filterType;
  private String filterData;
  private String throwerApp;
  private String throwerName;
  private String throwerData1;

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.thrower_datum_add);

    // See what application we want to handle events for from the
    // intent data passed to us.
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    appName = extras.getString(AGParser.KEY_APPLICATION);
    eventName = extras.getString(UGParser.KEY_EventName);
    // TODO(acase): Allow more than one filter
    filterType = extras.getString(UGParser.KEY_FilterType);
    filterData = extras.getString(UGParser.KEY_FilterData);
    throwerApp = extras.getString(UGParser.KEY_ActionApp);
    throwerName = extras.getString(UGParser.KEY_ActionName);
    throwerData1 = extras.getString(UGParser.KEY_ActionData);

    // Getting the list of data available from AppConfig ContentMap
    AGParser ag = new AGParser(getApplicationContext());
    ArrayList<StringMap> contentMap = ag.readContentMap(appName);
    ArrayAdapter<StringMap> arrayAdapter = new ArrayAdapter<StringMap>(this,
        android.R.layout.simple_list_item_1, contentMap);
    ListView lv = (ListView) findViewById(R.id.simple_list_view_01);
    //lv.setOnItemSelectedListener(this);
    lv.setOnItemClickListener(this);
    lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    lv.setAdapter(arrayAdapter);
    lv.setTextFilterEnabled(true);
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

  
	public void onItemClick(AdapterView<?> lv, View v, int position, long id) {
    StringMap sm = (StringMap) lv.getAdapter().getItem(position);
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), SaveDialog.class);
    i.putExtra(AGParser.KEY_APPLICATION, appName);
    i.putExtra(UGParser.KEY_EventName, eventName);
    i.putExtra(UGParser.KEY_FilterType, sm.getKey());
    // TODO(acase): Allow more than one filter
    i.putExtra(UGParser.KEY_FilterType, filterType);
    i.putExtra(UGParser.KEY_FilterData, filterData);
    i.putExtra(UGParser.KEY_ActionApp, throwerApp);
    i.putExtra(UGParser.KEY_ActionName, throwerName);
    if (throwerData1 == null) {
      i.putExtra(UGParser.KEY_ActionData, sm.getKey());
    } else {
      i.putExtra(UGParser.KEY_ActionData, throwerData1);
      i.putExtra(UGParser.KEY_ActionData2, sm.getKey());
    }
    setResult(Constants.RESULT_SUCCESS, i);
    finish();
	}
}