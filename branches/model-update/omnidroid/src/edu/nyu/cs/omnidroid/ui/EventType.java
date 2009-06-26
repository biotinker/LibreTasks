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
import java.util.HashMap;
import java.util.Set;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.StringMap;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Presents a list of possible actions that the selected <code>EventCatcher</code> could have
 * performed that we want to hook an OmniHandler onto.
 *
 */
public class EventType extends ListActivity {
  // Menu options of the Standard variety (Android menus require int)
  private static final int MENU_HELP = 0;

  // Data provided by intents
  private String eventApp;

  // Initialize our AGParser
  AGParser ag = new AGParser(this);

  /**
   * Sets up the list of events, getting the application out of the intent, and the actual events
   * out of the AGParser.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Get intent data
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    eventApp = extras.getString(AGParser.KEY_APPLICATION);

    // Getting the Events from AppConfig
    ArrayList<StringMap> stringvalues = transformEventsList(ag.readEvents(eventApp));

    // Populate our list
    ArrayAdapter<StringMap> arrayAdapter = new ArrayAdapter<StringMap>(this,
        android.R.layout.simple_list_item_1, stringvalues);
    setListAdapter(arrayAdapter);
    getListView().setTextFilterEnabled(true);
  }

  /**
   * Converts the list of events from AGParser's return value to the form needed for an
   * ArrayAdapter.
   *
   * @param eventList AGParser form of event list, where each HashMap has a single entry with the
   *                  key being the event, and the value being the human-readable event.
   * @return ArrayAdapter form of event list, where each StringMap has the same key and value
   *         semantics as the eventList
   */
  /* Visible for testing. */
  static ArrayList<StringMap> transformEventsList(ArrayList<HashMap<String, String>> eventList) {
    ArrayList<StringMap> newList = new ArrayList<StringMap>(eventList.size());
    for (HashMap<String, String> map : eventList) {
      Set<String> keys = map.keySet();
      for (String key : keys) {
        newList.add(new StringMap(key, map.get(key)));
      }
    }
    return newList;
  }

  /*
   * (non-Javadoc)
   *
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int,
   * long)
   */
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    StringMap sm = (StringMap) l.getAdapter().getItem(position);
    String eventName = sm.getKey();
    Intent i = createIntent(eventName);
    startActivityForResult(i, Constants.RESULT_ADD_OMNIHANDLER);
  }

  /**
   * Creates an intent with two extras: this instance's eventApp and the given eventName.
   * @param eventName to populate the EventName value of one of the Intent's extra
   * @return the new Intent
   */
  /* Visible for testing. */
  Intent createIntent(String eventName) {
    Intent i = new Intent();
    // See if filters can be applied, if not skip the filters page
    if (ag.readFilters(eventApp, eventName).size() > 0) {
      i.setClass(this.getApplicationContext(), Filters.class);
    } else {
      i.setClass(this.getApplicationContext(), Actions.class);
    }
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EVENT_TYPE, eventName);
    return i;
  }

  /*
   * (non-Javadoc)
   *
   * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (Constants.RESULT_ADD_OMNIHANDLER == requestCode && Constants.RESULT_SUCCESS == resultCode) {
      setResult(resultCode, data);
      finish();
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_HELP, 0, R.string.help).setIcon(android.R.drawable.ic_menu_help);
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_HELP:
      help();
      return true;
    }
    return false;
  }

  /**
   * Call our help dialog
   */
  private void help() {
    Builder help = new AlertDialog.Builder(this);
    String help_msg = this.getResources().getString(R.string.help_event_intro) + "\n<br/>"
        + this.getResources().getString(R.string.help_event_type);
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