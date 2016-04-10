/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
 /*******************************************************************************
 * Copyright 2009, 2010 Omnidroid - http://code.google.com/p/omnidroid 
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
package libretasks.app.view.simple;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;
import libretasks.app.R;
import libretasks.app.view.simple.model.Rule;

/**
 * This activity shows all rules currently in the system, and their on/off status.
 */
public class ActivitySavedRules extends ListActivity {
  // Options Menu IDs
  private static final int MENU_SETTINGS = 0;
  private static final int MENU_ADD_RULE = 1;
  private static final int MENU_ENABLE_ALL = 2;
  private static final int MENU_DISABLE_ALL = 3;
  private static final int MENU_HELP = 4;

  // Context Menu Options
  private static final int MENU_EDIT = 0;
  private static final int MENU_DELETE = 1;
  private static final int MENU_TOGGLE = 2;
  private static final int MENU_NOTIFICATION = 3;
  
  // Activity Request codes for activity results
  private static final int REQUEST_ACTIVITY_EDIT_RULE = 0;
  private static final int REQUEST_ACTIVITY_CREATE_RULE = 1;

  // List storage
  private ListView listview;
  RuleListAdapter ruleListAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Put our data together
    ruleListAdapter = new RuleListAdapter(this, R.layout.activity_saved_rules);

    // Get access to the listview
    listview = getListView();

    // Alert user when Omnidroid is disabled
    UIDbHelperStore.init(this);
    SharedPreferences prefs = UIDbHelperStore.instance().db().getSharedPreferences();
    if (!prefs.getBoolean(getString(R.string.pref_key_libretasks_enabled), true)) {
      Toast.makeText(this, R.string.libretasks_is_disabled_alert, Toast.LENGTH_LONG).show();
    }

    // Connect our data to the listview
    setListAdapter(ruleListAdapter);

    // Provide click to edit functionality
    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        editRule(position);
        return;
      }
    });

    // Provide context menu functionality
    registerForContextMenu(listview);
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
    case REQUEST_ACTIVITY_EDIT_RULE:
      if (resultCode == ActivityChooseFiltersAndActions.RESULT_RULE_SAVED) {
        // The user returned from the rule building activity. Refresh the list in case they updated
        // a rule.
        ruleListAdapter.notifyDataSetChanged();
      }
      break;
    case REQUEST_ACTIVITY_CREATE_RULE:
      if (resultCode == ActivityChooseRootEvent.RESULT_RULE_CREATED) {
        // The user returned from the rule building activity. Refresh the list in case they created
        // a new rule
        ruleListAdapter.notifyDataSetChanged();
      }
      break;
    }
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

    // TODO(acase): Create ViewRule option and corresponding Activity that doesn't provide "Editing"
    menu.setHeaderTitle(ruleListAdapter.getItem(info.position).getName());
    menu.add(ContextMenu.NONE, MENU_TOGGLE, ContextMenu.NONE, R.string.toggle_rule);
    menu.add(ContextMenu.NONE, MENU_EDIT, ContextMenu.NONE, R.string.edit_rule);
    menu.add(ContextMenu.NONE, MENU_DELETE, ContextMenu.NONE, R.string.delete_rule);
    menu.add(ContextMenu.NONE, MENU_NOTIFICATION, ContextMenu.NONE, 
        getNotificationTitle(info.position));
  }

  private CharSequence getNotificationTitle(int position) {
    if (ruleListAdapter.getItem(position).isNotificationEnabled()) {
      return getString(R.string.disable_notification);
    } else {
      return getString(R.string.enable_notification);
    }
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    switch (item.getItemId()) {
    case MENU_EDIT:
      editRule(info.position);
      return true;
    case MENU_DELETE:
      deleteRule(info.position);
      return true;
    case MENU_TOGGLE:
      ruleListAdapter.toggleRule(info.position);
      return true;
    case MENU_NOTIFICATION:
      ruleListAdapter.setNotification(info.position);
    default:
      return super.onContextItemSelected(item);
    }
  }

  /** Create an options menu */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(Menu.NONE, MENU_SETTINGS, Menu.NONE, getString(R.string.settings_label)).setIcon(
        android.R.drawable.ic_menu_preferences).setAlphabeticShortcut('s');
    menu.add(Menu.NONE, MENU_ADD_RULE, Menu.NONE, getString(R.string.create_rule))
        .setAlphabeticShortcut('a').setIcon(android.R.drawable.ic_menu_add);
    menu.add(Menu.NONE, MENU_ENABLE_ALL, Menu.NONE, getString(R.string.enable_all))
        .setAlphabeticShortcut('e').setIcon(android.R.drawable.checkbox_on_background);
    menu.add(Menu.NONE, MENU_DISABLE_ALL, Menu.NONE, getString(R.string.disable_all))
        .setAlphabeticShortcut('d').setIcon(android.R.drawable.checkbox_off_background);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    boolean allEnabled = ruleListAdapter.allEnabled();
    boolean allDisabled = ruleListAdapter.allDisabled();
    menu.getItem(MENU_ENABLE_ALL).setEnabled(!allEnabled);
    menu.getItem(MENU_DISABLE_ALL).setEnabled(!allDisabled);
    return super.onPrepareOptionsMenu(menu);
  }

  /** Called when an item of options menu is clicked */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_SETTINGS:
      startActivity(new Intent(this, ActivitySettings.class));
      return true;
    case MENU_ADD_RULE:
      Intent intent = new Intent(this, ActivityChooseRootEvent.class);
      startActivityForResult(intent, REQUEST_ACTIVITY_CREATE_RULE);
      return true;
    case MENU_ENABLE_ALL:
      ruleListAdapter.setRulesEnabled(true);
      return true;
    case MENU_DISABLE_ALL:
      ruleListAdapter.setRulesEnabled(false);
      return true;
    case MENU_HELP:
      help();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void help() {
    Builder help = new AlertDialog.Builder(this);
    help.setIcon(android.R.drawable.ic_menu_help);
    help.setTitle(R.string.help);
    help.setMessage(Html.fromHtml(getString(R.string.help_saved_rules)));
    help.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
      }
    });
    help.show();
  }

  private void deleteRule(final int position) {
    // Ask the user if they're sure they want to delete the rule.
    new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(
        getString(R.string.confirm_rule_delete)).setPositiveButton(getString(R.string.yes),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            ruleListAdapter.deleteRule(position);
          }
        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        // Do nothing
      }
    }).show();
  }

  private void editRule(int selectedItemPosition) {
    // The user wants to view and possibly edit an existing rule. We can send them to
    // the ActivityChooseFiltersAndActions activity which can handle rendering and
    // editing a saved rule or a new rule.
    Rule rule = UIDbHelperStore.instance().db().loadRule(
        ((Rule) ruleListAdapter.getItem(selectedItemPosition)).getDatabaseId());
    RuleBuilder.instance().resetForEditing(rule);

    // Wipe UI state for the new activity.
    ActivityChooseFiltersAndActions.resetUI(this);

    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), ActivityChooseFiltersAndActions.class);
    startActivityForResult(intent, REQUEST_ACTIVITY_EDIT_RULE);
  }

  /**
   * Adapter class handles the storage and presentation of our Rules.
   */
  private class RuleListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private int layoutId;
    private ArrayList<Rule> rules;

    public RuleListAdapter(Context context, int layoutId) {
      super();
      this.layoutId = layoutId;
      mInflater = LayoutInflater.from(context);
      rules = UIDbHelperStore.instance().db().getRules();
    }

    @Override
    public void notifyDataSetChanged() {
      rules = UIDbHelperStore.instance().db().getRules();
      super.notifyDataSetChanged();
    }

    private void toggleRule(int position) {
      // Update from memory representation
      Rule rule = rules.get(position);
      rule.setIsEnabled(!rule.getIsEnabled());
      // Update the enabled flag in the database
      UIDbHelperStore.instance().db().setRuleEnabled(rule.getDatabaseId(), rule.getIsEnabled());
      notifyDataSetChanged();
    }

    /**
     * Set all rules to the enable value passed in.
     * 
     * @param enable
     *          boolean to set for all rules
     */
    private void setRulesEnabled(boolean enable) {
      for (int i = 0; i < getCount(); i++) {
        setIsEnabled(i, enable);
        notifyDataSetChanged();
      }
    }

    /**
     * @return true if all rules are disabled, false otherwise
     */
    public boolean allDisabled() {
      for (int i = 0; i < getCount(); i++) {
        if (rules.get(i).getIsEnabled()) {
          return false;
        }
      }
      return true;
    }

    /**
     * @return true if all rules are enabled, false otherwise
     */
    public boolean allEnabled() {
      for (int i = 0; i < getCount(); i++) {
        if (!rules.get(i).getIsEnabled()) {
          return false;
        }
      }
      return true;
    }

    /**
     * Render a single Rule element in the list of all rules.
     * 
     * TODO(acase): Consider using convertView for performance
     * 
     * @return the view for this listview item
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
      // Initialize views
      View item = mInflater.inflate(layoutId, null);
      TextView text = (TextView) item.findViewById(R.id.text1);
      TextView desc = (TextView) item.findViewById(R.id.text2);
      CheckBox checkbox = (CheckBox) item.findViewById(R.id.checkbox);

      // Main text shouldn't be focusable so that the listview item is clickable
      text.setText(rules.get(position).getName());
      text.setFocusable(false);
      text.setClickable(false);

      // Rule description shouldn't be focusable so that the listview item is clickable
      desc.setText(rules.get(position).getDescription());
      desc.setFocusable(false);
      desc.setClickable(false);

      // Enable/disable rule checkbox, connect it to toggling event listener
      checkbox.setChecked(rules.get(position).getIsEnabled());
      checkbox.setFocusable(false);
      checkbox.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
          toggleRule(position);
        }
      });

      return item;
    }

    public void setIsEnabled(int position, boolean enabled) {
      // Update the rule storage
      rules.get(position).setIsEnabled(enabled);
      // Update the enabled flag in the database
      UIDbHelperStore.instance().db().setRuleEnabled(rules.get(position).getDatabaseId(), enabled);
      notifyDataSetChanged();
    }

    public void deleteRule(int position) {
      Rule rule = rules.get(position);

      // Delete from the database.
      UIDbHelperStore.instance().db().deleteRule(rule.getDatabaseId());

      // Delete from our memory representation.
      rules.remove(position);
      notifyDataSetChanged();
    }

    public int getCount() {
      return rules.size();
    }

    /**
     * @return the rule found at position
     */
    public Rule getItem(int position) {
      return rules.get(position);
    }

    /**
     * @return the database row id for the rule at position.
     */
    public long getItemId(int position) {
      return rules.get(position).getDatabaseId();
    }
    
    private void setNotification(int position) {
      Rule rule = ruleListAdapter.getItem(position);
       UIDbHelperStore.instance().db().setNotification(rule.getDatabaseId(), 
           !rule.isNotificationEnabled());
      notifyDataSetChanged();
    }
  }
}
