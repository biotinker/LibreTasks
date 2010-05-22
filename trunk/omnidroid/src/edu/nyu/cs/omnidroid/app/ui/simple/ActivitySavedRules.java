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
package edu.nyu.cs.omnidroid.app.ui.simple;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.ui.Constants;
import edu.nyu.cs.omnidroid.app.ui.simple.model.Rule;

/**
 * This activity shows all rules currently in the system, and their on/off status.
 */
public class ActivitySavedRules extends Activity {

  private static final String KEY_STATE = "StateActivitySavedRules";

  private ListView listView;
  private AdapterRules adapterRules;
  private SharedPreferences state;
  

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Link up controls from the xml layout resource file.
    initializeUI();

    // Restore UI state.
    state = getSharedPreferences(ActivitySavedRules.KEY_STATE, Context.MODE_WORLD_READABLE
        | Context.MODE_WORLD_WRITEABLE);
    listView.setItemChecked(state.getInt("selectedRule", -1), true);
  }

  @Override
  protected void onPause() {
    super.onPause();

    // Save UI state.
    SharedPreferences.Editor prefsEditor = state.edit();
    prefsEditor.putInt("selectedRule", listView.getCheckedItemPosition());
    prefsEditor.commit();
  }

  private void initializeUI() {
    setContentView(R.layout.activity_saved_rules);
    setTitle("Saved Rules");

    adapterRules = new AdapterRules(this);

    listView = (ListView) findViewById(R.id.activity_saved_rules_listview);
    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    listView.setAdapter(adapterRules);

    Button btnOk = (Button) findViewById(R.id.activity_saved_rules_btnViewRule);
    btnOk.setOnClickListener(listenerBtnClickViewRule);
    Button btnInfo = (Button) findViewById(R.id.activity_saved_rules_btnToggleOnOff);
    btnInfo.setOnClickListener(listenerBtnClickToggleOnOff);
    Button btnCancel = (Button) findViewById(R.id.activity_saved_rules_btnDeleteRule);
    btnCancel.setOnClickListener(listenerBtnClickDeleteRule);
    
    LinearLayout llBottomButtons = (LinearLayout) findViewById(
      R.id.activity_saved_rules_llBottomButtons);
    llBottomButtons.setBackgroundColor(getResources().getColor(R.color.layout_button_panel));
  } 
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
    case Constants.ACTIVITY_RESULT_ADD_FILTERS_AND_ACTIONS:
      // The user returned from the rule building activity. Refresh the list in case they created
      // a new rule, and select the rule currently loaded into RuleBuilder.
      adapterRules.reloadData();
      adapterRules.notifyDataSetChanged();
      adapterRules.setCheckedItemByRuleId(RuleBuilder.instance().getRule().getDatabaseId());
      break;
    }
  }

  /**
   * Wipes any UI state saves in {@link:state}. Activities which create this activity should
   * call this before launching so we appear as a brand new instance.
   * @param context  Context of caller.
   */
  public static void resetUI(Context context) {
    UtilUI.resetSharedPreferences(context, KEY_STATE);
  }
  
  private View.OnClickListener listenerBtnClickViewRule = new View.OnClickListener() {
    public void onClick(View v) {
      showActivityViewRule(listView.getCheckedItemPosition());
    }
  };

  private View.OnClickListener listenerBtnClickToggleOnOff = new View.OnClickListener() {
    public void onClick(View v) {
      toggleRuleOnOff(listView.getCheckedItemPosition());
    }
  };

  private View.OnClickListener listenerBtnClickDeleteRule = new View.OnClickListener() {
    public void onClick(View v) {
      deleteRule(listView.getCheckedItemPosition());
    }
  };

  private void showActivityViewRule(int selectedItemPosition) {
    if (selectedItemPosition < 0) {
      UtilUI.showAlert(this, "Sorry!", "Please select a rule from the list to view!");
      return;
    }

    // The user wants to view and possibly edit an existing rule. We can send them to
    // the ActivityChooseFiltersAndActions activity which can handle rendering and
    // editing a saved rule or a new rule.
    try {
      Rule rule = UIDbHelperStore.instance().db().loadRule(
          ((Rule) adapterRules.getItem(selectedItemPosition)).getDatabaseId());
      RuleBuilder.instance().resetForEditing(rule);
    } catch (Exception ex) {
      // Error loading full rule from database.
      UtilUI.showAlert(this, "Sorry!", "There was an error loading your rule:\n" + ex.toString());
      return;
    }

    // Wipe UI state for the new activity.
    ActivityChooseFiltersAndActions.resetUI(this);
    
    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), ActivityChooseFiltersAndActions.class);
    startActivityForResult(intent, Constants.ACTIVITY_RESULT_ADD_FILTERS_AND_ACTIONS);
  }

  private void toggleRuleOnOff(int selectedItemPosition) {
    if (selectedItemPosition < 0) {
      UtilUI.showAlert(this, "Sorry!", "Please select a rule from the list to turn on/off!");
      return;
    }

    adapterRules.toggleRuleOnOff(selectedItemPosition);
  }

  private void deleteRule(final int selectedItemPosition) {
    if (selectedItemPosition < 0) {
      UtilUI.showAlert(this, "Sorry!", "Please select a rule from the list to delete!");
      return;
    }

    // Ask the user if they're sure they want to delete the rule.
    new AlertDialog.Builder(this)
      .setIcon(android.R.drawable.ic_dialog_alert)
      .setTitle("Are you sure you want to delete this rule?")
      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          adapterRules.deleteRule(selectedItemPosition);
        }
      })
      .setNegativeButton("No", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        }
      })
      .show();
  }

  /**
   * Here we display all rules saved in the database.
   */
  private class AdapterRules extends BaseAdapter {
    private Context context;
    private ArrayList<Rule> rules;

    public AdapterRules(Context context) {
      this.context = context;

      // Fetch all available rules.
      reloadData();
    }
    
    public void reloadData() {
      rules = UIDbHelperStore.instance().db().getRules();
    }

    public void toggleRuleOnOff(int position) {
      Rule rule = rules.get(position);
      
      // Update from memory representation
      rule.setIsEnabled(!rule.getIsEnabled());
      
      // Update the enabled flag in the database
      UIDbHelperStore.instance().db().setRuleEnabled(rule.getDatabaseId(), rule.getIsEnabled());
      
      notifyDataSetChanged();
    }

    public void deleteRule(int position) {
      Rule rule = rules.get(position);
      
      // Delete from the database.
      UIDbHelperStore.instance().db().deleteRule(rule.getDatabaseId());

      // Delete from our memory representation.
      rules.remove(position);
      notifyDataSetChanged();

      // Uncheck whatever item was selected in the list.
      UtilUI.uncheckListViewSingleChoice(listView);
    }
    
    public int getCount() {
      return rules.size();
    }

    public Object getItem(int position) {
      return rules.get(position);
    }

    public long getItemId(int position) {
      return position;
    }
    
    @SuppressWarnings("unused")
    public long getCheckedRuleId() {
      if (listView.getCheckedItemPosition() > -1) {
        return rules.get(listView.getCheckedItemPosition()).getDatabaseId();
      }
      return -1;
    }
    
    public void setCheckedItemByRuleId(long ruleId) {
      UtilUI.uncheckListViewSingleChoice(listView);
      if (ruleId != -1) {
        for (int i = 0; i < rules.size(); i++) {
          if (rules.get(i).getDatabaseId() == ruleId) {
            listView.setItemChecked(i, true);
            return;
          }
        }
      }
    }

    /**
     * Render a single Rule element in the list of all rules.
     * TODO: (markww) Use convertView for efficiency.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

      // The rule we're rendering.
      Rule rule = rules.get(position);

      LinearLayout ll = new LinearLayout(context);
      ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
      ll.setMinimumHeight(50);
      ll.setOrientation(LinearLayout.HORIZONTAL);
      ll.setGravity(Gravity.CENTER_VERTICAL);

      // Choose a color for the title text of the rule. If it's on, make the text
      // white, if the rule is off, dull it out, but override both with yellow if
      // the user has selected this rule in the UI.
      int fontColor;
      if (listView.getCheckedItemPosition() == position) {
        fontColor = context.getResources().getColor(
            R.color.list_element_text_selected_rule_disabled);
        if (rule.getIsEnabled()) {
          fontColor = context.getResources().getColor(R.color.list_element_text_selected_rule);
        }
      } else {
        fontColor = context.getResources().getColor(R.color.list_element_text_disabled);
        if (rule.getIsEnabled()) {
          fontColor = context.getResources().getColor(R.color.list_element_text);
        }
      }

      TextView tv = new TextView(context);
      tv.setText(rules.get(position).getName());
      tv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
      tv.setGravity(Gravity.CENTER_VERTICAL);
      tv.setPadding(10, 0, 0, 0);
      tv.setTextSize(14.0f);
      tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
      tv.setTextColor(fontColor);
      tv.setMinHeight(46);

      ll.addView(tv);

      return ll;
    }
  }//AdapterRules
  
}