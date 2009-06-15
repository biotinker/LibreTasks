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
import android.widget.Button;
import android.widget.EditText;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Activity used to present a list of filters to apply to this OmniHandler. Filters allow the user
 * to only apply this OmniHandler if the filter data matches the data in provided by the Event that
 * was caught.
 * 
 */
public class FiltersAddData extends Activity implements OnClickListener {
  // Standard Menu options (Android menus require int)
  private static final int MENU_HELP = 0;

  // User Selected Data
  String eventApp;
  String eventName;
  String filterType;

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.filters_add_data);

    // Get data set so far
    getIntentData();

    // Connect to the "Done" button
    Button done = (Button) findViewById(R.id.filters_add_done);
    done.setOnClickListener(this);
  }

  /**
   * A wrapper for the getIntent() function to store the appropriate data that should be present:
   * eventApp, eventName, and filterType.
   */
  private void getIntentData() {
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    eventApp = extras.getString(AGParser.KEY_APPLICATION);
    eventName = extras.getString(UGParser.KEY_EVENT_TYPE);
    filterType = extras.getString(UGParser.KEY_FILTER_TYPE);
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
   * 
   * @return void
   */
  private void help() {
    Builder help = new AlertDialog.Builder(this);
    String help_msg = this.getResources().getString(R.string.help_filters_add_data);
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
    // Pass the data back to Filters
    // TODO(acase): Use auto-complete text instead
    // AutoCompleteTextView data = (AutoCompleteTextView) findViewById(R.id.filters_add_data);
    EditText data = (EditText) findViewById(R.id.filters_add_data);
    String filterData = data.getText().toString();

    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), Filters.class);
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EVENT_TYPE, eventName);
    i.putExtra(UGParser.KEY_FILTER_TYPE, filterType);
    i.putExtra(UGParser.KEY_FILTER_DATA, filterData);
    setResult(Constants.RESULT_SUCCESS, i);
    finish();
  }

}