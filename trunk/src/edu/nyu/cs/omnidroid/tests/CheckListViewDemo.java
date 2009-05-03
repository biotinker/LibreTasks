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
package edu.nyu.cs.omnidroid.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.UGParser;

public class CheckListViewDemo extends ListActivity {
  // Context Menu Options (Android menus require int, so no enums)
  private static final int MENU_EDIT = 0;
  private static final int MENU_DELETE = 1;

  TextView selection;
  UGParser ug;
  ArrayList<String> items = new ArrayList<String>();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Get a list of our current OmniHandlers
    ug = new UGParser(getApplicationContext());
    // ArrayList<View> rowList = new ArrayList<View>();
    ArrayList<HashMap<String, String>> userConfigRecords = ug.readRecords();
    Iterator<HashMap<String, String>> i = userConfigRecords.iterator();

    // Add current OmniHandlers to our list
    while (i.hasNext()) {
      HashMap<String, String> HM1 = i.next();
      items.add(HM1.get((String) UGParser.KEY_INSTANCE_NAME));
    }

    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
    setListAdapter(arrayAdapter);
    selection = (TextView) findViewById(R.id.checklist_selection);
    registerForContextMenu(getListView());
    setContentView(R.layout.test_checklistview);
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View,
   * android.view.ContextMenu.ContextMenuInfo)
   */
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.add(0, MENU_EDIT, 0, R.string.edit);
    menu.add(0, MENU_DELETE, 0, R.string.del);
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
   */
  public boolean onContextItemSelected(MenuItem item) {
    Toast.makeText(this.getBaseContext(), "ContextItemSelected", Toast.LENGTH_SHORT).show();
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
   *          of the menu item
   */
  private void deleteHandler(MenuItem item) {
    Toast.makeText(this.getBaseContext(), "Edit OmniHandler Selected", Toast.LENGTH_SHORT).show();
  }

  /**
   * @param item
   *          of the menu item
   */
  private void editHandler(MenuItem item) {
    Toast.makeText(this.getBaseContext(), "Edit OmniHandler Selected", Toast.LENGTH_SHORT).show();
  }
  /*
   * (non-Javadoc)
   * 
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int,
   * long)
   */
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Toast.makeText(this.getBaseContext(), "ListItemClick", Toast.LENGTH_SHORT).show();
  }

}
