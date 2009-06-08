/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid 
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 *******************************************************************************/
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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.StringMap;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Activity used to present a list of filters to apply to this OmniHandler. Filters allow the user
 * to only apply this OmniHandler if the filter data matches the data in provided by the Event that
 * was caught.
 * 
 */
public class ActionData extends Activity implements OnClickListener, OnItemClickListener {
  // Menu options of the Standard variety (Android menus require int)
  private static final int MENU_HELP = 3;

  public static String KEY_DATA1_TYPE = "Data1Type";
  public static String KEY_DATA2_TYPE = "Data2Type";
  public static int DATA_TYPE_SELECT = 1;
  public static int DATA_TYPE_MANUAL = 2;

  // Intent data passed along
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

  /* Data Storage */
  // List view to display our data
  private ListView dataListView;
  // Storage for the listview
  private ArrayList<String> dataList = new ArrayList<String>();
  // List of data fields needed for the action app
  private ArrayList<String> dataFields;
  private ArrayList<String> dataInputs = new ArrayList<String>();
  int minUriFields;

  // Application Config Parser
  private static AGParser ag;

  // A filler to get data to select the appropriate data
  private static final String EMPTY_DATA = "Set data for: ";

  // Intent data keys
  public static final String KEY_DATA_ID = "dataId";
  public static final String KEY_DATA_NAME = "dataName";

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.action_data);
    dataListView = (ListView) findViewById(R.id.thrower_data_list);

    // Setup the "Done" button
    Button done = (Button) findViewById(R.id.thrower_data_done);
    done.setOnClickListener(this);

    // Get the data passed to us
    getIntentData(getIntent());

    // Get a list of data we'll need for our action application
    ag = new AGParser(this);
    dataFields = ag.readURIFields(throwerApp, throwerName);
    minUriFields = dataFields.size();
    // Get a mapping of data from the app's content provider
    ArrayList<StringMap> contentMap = ag.readContentMap(throwerApp);
    for (int i = 0; i < dataFields.size(); i++) {
      for (StringMap item : contentMap) {
        if (item.getValue().equals(dataFields.get(i))) {
          dataInputs.add(item.getValue());
        }
      }
    }

    // Update our UI
    update();
  }

  /**
   * Update our UI
   */
  private void update() {
    // Clear the List
    dataList.clear();
    int dataCount = 0;

    // Populate the list of data elements
    if (throwerData1 != null) {
      dataCount++;
      dataList.add(throwerData1);
    } else {
      dataList.add(EMPTY_DATA + dataInputs.get(0));
    }
    if (throwerData2 != null) {
      dataCount++;
      dataList.add(throwerData2);
    } else {
      dataList.add(EMPTY_DATA + dataInputs.get(1));
    }

    // Display a list of our data elements
    ArrayAdapter<String> listadpt = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, dataList);
    dataListView.setAdapter(listadpt);
    dataListView.setTextFilterEnabled(true);
    dataListView.setOnItemClickListener(this);

    // Disable/Enable the "Done" button depending on if all our data has been entered
    Button done = (Button) findViewById(R.id.thrower_data_done);
    if (dataCount == minUriFields) {
      done.setEnabled(true);
    } else {
      done.setEnabled(false);
    }
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
      eventName = extras.getString(UGParser.KEY_EVENT_TYPE);
      filterType = extras.getString(UGParser.KEY_FILTER_TYPE);
      filterData = extras.getString(UGParser.KEY_FILTER_DATA);
      throwerApp = extras.getString(UGParser.KEY_ACTION_APP);
      throwerName = extras.getString(UGParser.KEY_ACTION_TYPE);
      throwerData1Type = extras.getInt(KEY_DATA1_TYPE);
      throwerData2Type = extras.getInt(KEY_DATA2_TYPE);
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
    editDatum(id);
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
   * Edit the data item selected
   * 
   * @param id
   *          - the item id selected
   */
  private void editDatum(long id) {
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), ActionDatumAdd.class);
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EVENT_TYPE, eventName);
    i.putExtra(UGParser.KEY_FILTER_TYPE, filterType);
    i.putExtra(UGParser.KEY_FILTER_DATA, filterData);
    i.putExtra(UGParser.KEY_ACTION_APP, throwerApp);
    i.putExtra(UGParser.KEY_ACTION_TYPE, throwerName);
    i.putExtra(KEY_DATA1_TYPE, throwerData1Type);
    i.putExtra(KEY_DATA2_TYPE, throwerData2Type);
    i.putExtra(KEY_DATA_NAME, dataInputs.get((int) id));
    i.putExtra(KEY_DATA_ID, id);
    if (id != 0) {
      i.putExtra(UGParser.KEY_ACTION_DATA1, throwerData1);
    }
    if (id != 1) {
      i.putExtra(UGParser.KEY_ACTION_DATA2, throwerData2);
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
    String help_msg = this.getResources().getString(R.string.help_action_data);
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
    case R.id.thrower_data_done:
      done();
      break;
    }
  }

  /**
   * If we're done entering data, then go back to the thro
   */
  private void done() {
    // Go back to our start page
    Intent i = new Intent();
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EVENT_TYPE, eventName);
    if ((filterType != null) && (filterData != null)) {
      i.putExtra(UGParser.KEY_FILTER_TYPE, filterType);
      i.putExtra(UGParser.KEY_FILTER_DATA, filterData);
    }
    i.putExtra(UGParser.KEY_ACTION_APP, throwerApp);
    i.putExtra(UGParser.KEY_ACTION_TYPE, throwerName);
    i.putExtra(KEY_DATA1_TYPE, throwerData1Type);
    i.putExtra(KEY_DATA2_TYPE, throwerData2Type);
    if (throwerData1 != null) {
      i.putExtra(UGParser.KEY_ACTION_DATA1, throwerData1);
    }
    if (throwerData2 != null) {
      i.putExtra(UGParser.KEY_ACTION_DATA2, throwerData2);
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
        update();
        break;
      }
      break;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView,
   * android.view.View, int, long)
   */
  public void onItemClick(AdapterView<?> lv, View v, int position, long id) {
    editDatum(id);
  }
}