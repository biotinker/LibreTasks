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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import libretasks.app.R;
import libretasks.app.view.simple.model.ModelAction;
import libretasks.app.view.simple.model.ModelApplication;
import libretasks.app.view.simple.model.ModelAttribute;
import libretasks.app.view.simple.model.ModelEvent;
import libretasks.app.view.simple.model.ModelItem;
import libretasks.app.view.simple.model.ModelRuleAction;
import libretasks.app.view.simple.model.ModelRuleFilter;
import java.util.List;

/**
 * This activity is used to add multiple filters or actions to a new rule.
 */
public class ActivityChooseFiltersAndActions extends Activity {
  // Log tag
  private static final String TAG = ActivityChooseFiltersAndActions.class.getSimpleName();

  // State storage
  private static final String KEY_STATE = "StateActivityChooseFilters";
  private static final String KEY_PREF = "selectedRuleItem";
  private SharedPreferences state;

  /** Request codes for Activity creation */
  /** Return code for filter building activities. */
  public static final int REQUEST_ADD_FILTER = 0;
  /** Return code for action building activities. */
  public static final int REQUEST_ADD_ACTION = 1;
  /** Return code for filter editing activities. */
  public static final int REQUEST_EDIT_FILTER = 2;
  /** Return code for action editing activities. */
  public static final int REQUEST_EDIT_ACTION = 3;
  /** Return code for rule name editing activity. */
  public static final int REQUEST_SET_RULE_NAME = 4;
  /** Return code for rule building activity. */
  public static final int REQUEST_ADD_FILTERS_AND_ACTIONS = 5;

  /** Result codes for this activity */
  /** Result code for saving a rule successfully. */
  public static final int RESULT_RULE_SAVED = 1;

  // Context Menu Options
  private static final int MENU_HELP = 0;

  // Context Menu Options
  private static final int MENU_EDIT = 0;
  private static final int MENU_DELETE = 1;

  private ListView listView;
  private AdapterRule adapterRule;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choose_filters_and_actions);

    // Link our listview with the AdapterRule instance.
    initializeListView();

    // Link up bottom button panel areas.
    initializeButtonPanel();

    // Restore UI state if possible.
    state = getSharedPreferences(ActivityChooseFiltersAndActions.KEY_STATE,
        Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
    listView.setItemChecked(state.getInt(KEY_PREF, 0), true);
  }

  protected void onPause() {
    super.onPause();

    // Save UI state.
    SharedPreferences.Editor prefsEditor = state.edit();
    prefsEditor.putInt(KEY_PREF, listView.getCheckedItemPosition());
    prefsEditor.commit();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
    case REQUEST_ADD_FILTER:
      // Did the user construct a valid filter? If so add it to the rule.
      if (RuleBuilder.instance().getChosenRuleFilter() != null) {
        // Add the filter to the rule builder and the UI tree.
        adapterRule.addItemToParentPosition(listView.getCheckedItemPosition(), RuleBuilder
            .instance().getChosenRuleFilter());
      }
      RuleBuilder.instance().resetFilterPath();
      break;

    case REQUEST_EDIT_FILTER:
      // Did the user modify a valid filter? If so replace it in the rule.
      if (RuleBuilder.instance().getChosenRuleFilter() != null) {
        // Add the filter to the rule builder and the UI tree.
        adapterRule.replaceItem(listView.getCheckedItemPosition(), RuleBuilder.instance()
            .getChosenRuleFilter());
      }
      RuleBuilder.instance().resetFilterPath();
      break;

    case REQUEST_ADD_ACTION:
      // Did the user construct a valid action? If so add it to the rule.
      if (RuleBuilder.instance().getChosenRuleAction() != null) {
        // Add the action to the rule builder and the UI tree.
        adapterRule.addItemToParentPosition(listView.getCheckedItemPosition(), RuleBuilder
            .instance().getChosenRuleAction());
      }
      RuleBuilder.instance().resetActionPath();
      break;

    case REQUEST_EDIT_ACTION:
      // Did the user modify a valid action? If so replace it in the rule.
      if (RuleBuilder.instance().getChosenRuleAction() != null) {
        // Add the action to the rule builder and the UI tree.
        adapterRule.replaceItem(listView.getCheckedItemPosition(), RuleBuilder.instance()
            .getChosenRuleAction());
      }
      RuleBuilder.instance().resetActionPath();
      break;

    case REQUEST_SET_RULE_NAME:
      if (resultCode != Activity.RESULT_CANCELED) {
        // Try to save the rule for the user now.
        // We only get this request code if the user picked
        // a valid rule name.
        saveRule();
      }
      break;
    }
  }

  private void initializeListView() {
    listView = (ListView) findViewById(R.id.activity_choosefiltersandactions_listview);

    adapterRule = new AdapterRule(this, listView);

    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    listView.setAdapter(adapterRule);

    // Set the adapter to render the rule stored in RuleBuilder.
    // It may be a brand new rule or a saved rule being edited.
    adapterRule.setRule(RuleBuilder.instance().getRule());
    registerForContextMenu(listView);
  }

  /** Create an options menu */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(Menu.NONE, MENU_HELP, Menu.NONE, getString(R.string.help)).setIcon(
        android.R.drawable.ic_menu_help).setAlphabeticShortcut('h');
    return super.onCreateOptionsMenu(menu);
  }

  /** Called when an item of options menu is clicked */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
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
    help.setMessage(Html.fromHtml(getString(R.string.help_filtersandactions)));
    help.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
      }
    });
    help.show();
  }

  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.add(ContextMenu.NONE, MENU_EDIT, ContextMenu.NONE, R.string.edit);
    menu.add(ContextMenu.NONE, MENU_DELETE, ContextMenu.NONE, R.string.delete);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    int selectedItemPosition = info.position;
    listView.setItemChecked(selectedItemPosition, true); // Set the chosen item as selected

    switch (item.getItemId()) {
    case MENU_EDIT:
      editItem(selectedItemPosition);
      return true;
    case MENU_DELETE:
      confirmDeleteItem(selectedItemPosition);
      return true;
    default:
      return super.onContextItemSelected(item);
    }
  }

  /**
   * Checks whether the currently chosen event has attributes
   * 
   * @return true if it has at least one attribute
   */
  private boolean hasAttributes() {
    ModelEvent event = RuleBuilder.instance().getChosenEvent();
    List<ModelAttribute> attributes = UIDbHelperStore.instance().db().getAttributesForEvent(event);

    return !attributes.isEmpty();
  }

  private void initializeButtonPanel() {
    Button btnAddFilter = (Button) findViewById(R.id.activity_choosefiltersandactions_btnAddFilter);
    btnAddFilter.setOnClickListener(listenerBtnClickAddFilter);
    btnAddFilter.setEnabled(hasAttributes());

    Button btnAddAction = (Button) findViewById(R.id.activity_choosefiltersandactions_btnAddAction);
    btnAddAction.setOnClickListener(listenerBtnClickAddAction);

    Button btnSaveRule = (Button) findViewById(R.id.activity_choosefiltersandactions_btnSaveRule);
    btnSaveRule.setOnClickListener(listenerBtnClickSaveRule);

    LinearLayout llBottomButtons = (LinearLayout) findViewById(R.id.activity_choosefiltersandactions_llBottomButtons);
    llBottomButtons.setBackgroundColor(getResources().getColor(R.color.layout_button_panel));
  }

  /**
   * Wipes any UI state saves in {@link:state}. Activities which create this activity should call
   * this before launching so we appear as a brand new instance.
   * 
   * @param context
   *          Context of caller.
   */
  public static void resetUI(Context context) {
    UtilUI.resetSharedPreferences(context, KEY_STATE);
  }

  private OnClickListener listenerBtnClickAddFilter = new OnClickListener() {
    public void onClick(View v) {
      ModelItem selectedItem = adapterRule.getItem(listView.getCheckedItemPosition());
      if ((selectedItem instanceof ModelEvent) || (selectedItem instanceof ModelRuleFilter)) {
        // Now we present the user with a list of attributes they can
        // filter on for their chosen root event.
        showDlgAttributes();
      } else {
        UtilUI.showAlert(v.getContext(), getString(R.string.sorry),
            getString(R.string.add_filter_alert));
        return;
      }
    }
  };

  private OnClickListener listenerBtnClickAddAction = new OnClickListener() {
    public void onClick(View v) {
      // For actions, we don't check what node the user has selected since they must always go to
      // the end of the rule tree.
      showDlgApplications();
    }
  };

  private void confirmDeleteItem(final int position) {
    // Ask the user if they're sure they want to delete this item.
    new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(
        R.string.confirm_item_delete).setPositiveButton(getString(R.string.yes),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            deleteItem(position);
          }
        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        // Do nothing
      }
    }).show();
  }

  private void deleteItem(int position) {
    ModelItem item = adapterRule.getItem(position);
    if ((item instanceof ModelRuleFilter) || (item instanceof ModelRuleAction)) {
      adapterRule.removeItem(position);
    } else {
      UtilUI.showAlert(this, getString(R.string.sorry), getString(R.string.select_filter_delete));
    }
  }

  private void editItem(int position) {
    ModelItem item = adapterRule.getItem(position);
    if (item instanceof ModelRuleFilter) {
      editFilter(position, (ModelRuleFilter) item);
    } else if (item instanceof ModelRuleAction) {
      editAction(position, (ModelRuleAction) item);
    } else {
      UtilUI.showAlert(this, getString(R.string.sorry), getString(R.string.select_filter_edit));
    }
  }

  private OnClickListener listenerBtnClickSaveRule = new OnClickListener() {
    public void onClick(View v) {

      // After press save, the rule is set to 'Enabled' as default
      RuleBuilder.instance().getRule().setIsEnabled(true);

      // Ask the user for a new rule name.
      // The result will be stored inside RuleBuilder.
      // When the activity returns, we can continue trying to save the rule.
      Intent intent = new Intent();
      intent.setClass(getApplicationContext(), ActivityDlgRuleName.class);
      startActivityForResult(intent, REQUEST_SET_RULE_NAME);
    }
  };

  /**
   * Starts up the initial activity for adding a new filter.
   */
  private void showDlgAttributes() {
    // Reset the chosen filter data if left over from a previous run.
    RuleBuilder.instance().resetFilterPath();

    // Launch the activity chain for adding a filter.
    // We check if the user completed a
    // filter in onActivityResult.
    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), ActivityDlgAttributes.class);
    startActivityForResult(intent, REQUEST_ADD_FILTER);
  }

  /**
   * Starts up the initial activity for adding a new action.
   */
  private void showDlgApplications() {
    // Reset the chosen action data if left over from a previous run.
    RuleBuilder.instance().resetActionPath();

    // Launch the activity chain for adding an action.
    // We check if the user completed an action in onActivityResult.
    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), ActivityDlgApplications.class);
    startActivityForResult(intent, REQUEST_ADD_ACTION);
  }

  /**
   * Shortcuts directly to {@link ActivityDlgFilterInput} for editing an existing filter.
   */
  private void editFilter(int position, ModelRuleFilter filter) {
    // Set the filter data from the existing rule filter instance.
    RuleBuilder.instance().resetFilterPath();
    RuleBuilder.instance().setChosenModelFilter(filter.getModelFilter());
    RuleBuilder.instance().setChosenRuleFilterDataOld(filter.getData());

    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), ActivityDlgFilterInput.class);
    startActivityForResult(intent, REQUEST_EDIT_FILTER);
  }

  /**
   * Shortcuts directly to {@link ActivityDlgActionInput} for editing an existing action.
   */
  private void editAction(int position, ModelRuleAction ruleAction) {
    // Set the action data from the existing rule action instance.
    ModelAction action = ruleAction.getModelAction();
    RuleBuilder ruleBuilder = RuleBuilder.instance();
    ruleBuilder.resetFilterPath();
    ruleBuilder.setChosenModelAction(ruleAction.getModelAction());
    ruleBuilder.setChosenRuleActionDataOld(ruleAction.getDatas());

    // Set the application in preparation for activities using user accounts
    ModelApplication app = UIDbHelperStore.instance().db().getApplicationFromAction(action);
    ruleBuilder.setChosenApplication(app);

    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), ActivityDlgActionInput.class);
    startActivityForResult(intent, REQUEST_EDIT_ACTION);
  }

  /**
   * Saves the built-up rule inside {@link RuleBuilder} to the database.
   * 
   */
  private void saveRule() {
    Log.d(TAG, "entered saveRule()");
    long newRuleId = 0;
    try {
      newRuleId = UIDbHelperStore.instance().db().saveRule(RuleBuilder.instance().getRule());
    } catch (IllegalStateException e) {
      UtilUI.showAlert(this, getString(R.string.sorry),
          getString(R.string.illegal_state_exception_catch));
      Log.e(TAG, "Save Rule Error: Caught Illegal State Exception when saving", e);
      return;
    } catch (Exception e) {
      UtilUI.showAlert(this, getString(R.string.sorry), getString(R.string.error_saving_rule));
      Log.e(TAG, "Save Rule Error: Caught an error when saving", e);
      return;
    }
    Log.d(TAG, "New rule saved.");
    UtilUI.showAlert(this, getString(R.string.save_rule), getString(R.string.rule_saved));

    // We have to reload the rule from the database now so that the UI representation
    // of it is in-sync with the new element IDs assigned during the save operation.
    RuleBuilder.instance().resetForEditing(UIDbHelperStore.instance().db().loadRule(newRuleId));
    Log.d(TAG, "Save Rule: new rule reloaded from db");
    adapterRule.setRule(RuleBuilder.instance().getRule());
    Log.d(TAG, "Save Rule: new rule set");
    setResult(RESULT_RULE_SAVED);
    finish();
  }
}
