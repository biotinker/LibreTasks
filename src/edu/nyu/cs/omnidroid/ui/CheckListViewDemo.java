/***
  Copyright (c) 2008-2009 CommonsWare, LLC
  Copyright (c) 2009 Andrew I. Case
  
  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.UGParser;

public class CheckListViewDemo extends ListActivity implements OnClickListener {
  // Context Menu Options (Android menus require int, so no enums)
  private static final int MENU_EDIT = 0;
  private static final int MENU_DELETE = 1;

  // Standard Menu options (Android menus require int, so no enums)
  private static final int MENU_ADD = 2;
  private static final int MENU_SETTINGS = 3;
  private static final int MENU_HELP = 4;
  private static final int MENU_TESTS = 5;

  TextView selection;
  UGParser ug;
  ArrayList<String> items = new ArrayList<String>();
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Get a list of our current OmniHandlers
    ug = new UGParser(getApplicationContext());
    //ArrayList<View> rowList = new ArrayList<View>();
    ArrayList<HashMap<String, String>> userConfigRecords = ug.readRecords();
    Iterator<HashMap<String, String>> i = userConfigRecords.iterator();

    // Add current OmniHandlers to our list
    while (i.hasNext()) {
      HashMap<String, String> HM1 = i.next();
      items.add(HM1.get((String) UGParser.KEY_InstanceName));
    }
    
    setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, items));
    selection=(TextView)findViewById(R.id.selection);
    registerForContextMenu(getListView());
    setContentView(R.layout.checkboxlistview);
  }


  /*
   * (non-Javadoc)
   * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
   */
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo) {
    menuInfo = new AdapterContextMenuInfo(v, 0, 0);
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.add(0, MENU_EDIT, 0, R.string.edit);
    menu.add(0, MENU_DELETE, 0, R.string.del);
  }

  /*
   * (non-Javadoc)
   * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
   */
  public boolean onContextItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_EDIT:
      editHandler(item);
      return true;
    case MENU_DELETE:
      deleteHandler(item);
      return true;
    default:
      return super.onContextItemSelected(item);
    }
  }
  
  /**
   * @param item
   *            of the menu item
   */
  private void deleteHandler(MenuItem item) {
    // TODO (acase): Present delete confirmation dialog
    ContextMenuInfo cmi = item.getMenuInfo();
    Toast.makeText(this.getBaseContext(),
        "Delete OmniHandler Selected", 5).show();
    // TODO (acase): Delete from UGParser
    // TODO (acase): Delete from CP

    // Button selected = (Button) view;
    // ug.deleteRecord(selected.getText());
    
    AdapterView.AdapterContextMenuInfo info;
    try {
        info = (AdapterView.AdapterContextMenuInfo) cmi;
    } catch (ClassCastException e) {
        Log.e(this.getLocalClassName(), "bad menuInfo", e);
        return;
    }
    long id = getListAdapter().getItemId(info.position);

  }

  /**
   * @param item
   *            of the menu item
   */
  private void editHandler(MenuItem item) {
    // TODO (acase): Call next activity
    Toast.makeText(this.getBaseContext(), "Edit OmniHandler Selected", 5)
        .show();
  }

  /**
   * Creates the options menu items
   * 
   * @param menu
   *            - the options menu to create
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_ADD, 0, R.string.add_omnihandler).setIcon(
        android.R.drawable.ic_menu_add);
    menu.add(0, MENU_SETTINGS, 0, R.string.settings).setIcon(
        android.R.drawable.ic_menu_preferences);
    menu.add(0, MENU_HELP, 0, R.string.help).setIcon(
        android.R.drawable.ic_menu_help);
    menu.add(0, MENU_TESTS, 0, R.string.tests).setIcon(
        android.R.drawable.ic_menu_manage);
    return true;
  }

  /**
   * Handles menu item selections
   */
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_ADD:
      AddOmniHandler();
      return true;
    case MENU_SETTINGS:
      Settings();
      return true;
    case MENU_HELP:
      Help();
      return true;
    case MENU_TESTS:
      Intent i = new Intent();
      i.setClass(this.getApplicationContext(),
          edu.nyu.cs.omnidroid.tests.TestApp.class);
      startActivity(i);
      return true;
    }
    return false;
  }

  /**
   * Add a new OmniHandler to OmniDroid
   */
  private void AddOmniHandler() {
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(),
        edu.nyu.cs.omnidroid.ui.EventCatcher.class);
    startActivity(i);
  }

  /**
   * Call our Settings Activity
   */
  private void Settings() {
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(),
        edu.nyu.cs.omnidroid.ui.Settings.class);
    startActivity(i);
  }

  /**
   * Call our Help dialog
   */
  private void Help() {
    // TODO (acase): Create a help dialog for this activity
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
   */
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Log.i(this.getLocalClassName(), "onListItemClicked");
    Log.d(this.getLocalClassName(), "Classname of v=" + v.getClass().getName());
    selection.setText(items.get(position));
    //if (v.getClass().getName() == CheckBox.class.getName()) {
      // Handle "Enable" button clicked
      String instanceName = (String) v.getTag();
      CheckBox cb = (CheckBox) v;
      HashMap<String, String> HM = ug.readRecord(instanceName);
      if (cb.isChecked()) {
        Toast.makeText(this.getBaseContext(), "Enabling " + instanceName,
            5).show();
        HM.put(UGParser.KEY_EnableInstance, "true");
      } else {
        Toast.makeText(this.getBaseContext(), "Disabling " + instanceName,
            5).show();
        HM.put(UGParser.KEY_EnableInstance, "false");
      }
      ug.updateRecord(HM);

      // Restart the service
      Intent i = new Intent();
      i.setAction("OmniRestart");
      sendBroadcast(i);
    //} else if (v.getClass().getName() == Button.class.getName()) {
      // TODO (acase): Handle "Edit" button clicked
    //}
  }


  /* (non-Javadoc)
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  public void onClick(View v) {
    // TODO Auto-generated method stub
    
  }

}
