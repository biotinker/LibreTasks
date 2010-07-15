/*******************************************************************************
 * Copyright 2009 Omnidroid - http://code.google.com/p/omnidroid 
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
package edu.nyu.cs.omnidroid.app.view.simple;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelEvent;

/**
 * This activity gives users a chance to pick a root event for a new rule. After a user chooses a
 * root event, we move them to {@link ActivityChooseFilters} to continue building their rule.
 */
public class ActivityChooseRootEvent extends Activity {
  private static final String KEY_STATE = "StateActivityChooseRootEvent";
  private static final String KEY_STATE_SELECTED_EVENT = "selectedEventItem";

  // Request codes for creating new activities
  private static final int REQUEST_ACTIVITY_CHOOSE_FILTERS_ACTIONS = 0;

  // Result code for successfully creating a rule 
  public static final int RESULT_RULE_CREATED=1;
  
  private ListView listView;
  private AdapterEvents adapterEvents;
  private SharedPreferences state;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choose_root_event);

    adapterEvents = new AdapterEvents(this);

    listView = (ListView) findViewById(R.id.activity_chooserootevent_listview);
    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    listView.setAdapter(adapterEvents);

    Button btnCreateRule = (Button) findViewById(R.id.activity_chooserootevent_btnOk);
    btnCreateRule.setOnClickListener(listenerBtnClickCreateRule);

    LinearLayout llBottomButtons = (LinearLayout) findViewById(
        R.id.activity_chooserootevent_llBottomButtons);
    llBottomButtons.setBackgroundColor(getResources().getColor(R.color.layout_button_panel));

    // Restore UI control values if possible.
    state = getSharedPreferences(ActivityChooseRootEvent.KEY_STATE, Context.MODE_WORLD_READABLE
        | Context.MODE_WORLD_WRITEABLE);
    listView.setItemChecked(state.getInt(KEY_STATE_SELECTED_EVENT, -1), true);
  }

  protected void onPause() {
    super.onPause();

    // Save UI state.
    SharedPreferences.Editor prefsEditor = state.edit();
    prefsEditor.putInt(KEY_STATE_SELECTED_EVENT, listView.getCheckedItemPosition());
    prefsEditor.commit();
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

  private OnClickListener listenerBtnClickCreateRule = new OnClickListener() {
    public void onClick(View v) {
      int selectedEventPosition = listView.getCheckedItemPosition();
      if (selectedEventPosition > -1 && selectedEventPosition < adapterEvents.getCount()) {
        // The user has chosen a valid root event, store it
        // in the global RuleBuilder.
        RuleBuilder.instance().resetForNewRuleEditing(adapterEvents.getItem(selectedEventPosition));

        // Wipe UI state for the new activity.
        ActivityChooseFiltersAndActions.resetUI(v.getContext());

        // Move them along to the ActivityChooseFilters activity where
        // they can start adding some filters.
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ActivityChooseFiltersAndActions.class);
        startActivityForResult(intent, REQUEST_ACTIVITY_CHOOSE_FILTERS_ACTIONS);
      } else {
        UtilUI.showAlert(v.getContext(), getString(R.string.sorry),
            getString(R.string.choose_root_event_inst));
      }
    }
  };
  
  @Override
  public void onActivityResult(int requestCode,int resultCode,Intent data) {
    if (requestCode == REQUEST_ACTIVITY_CHOOSE_FILTERS_ACTIONS && 
        resultCode == ActivityChooseFiltersAndActions.RESULT_RULE_SAVED) {
      setResult(RESULT_RULE_CREATED);
      finish();
    }  
  }

  /**
   * Handles rendering of individual event items for our ListView.
   */
  private class AdapterEvents extends BaseAdapter {
    private Context context;
    private ArrayList<ModelEvent> events;

    public AdapterEvents(Context c) {
      context = c;

      // Get a list of all possible events from the database.
      events = UIDbHelperStore.instance().db().getAllEvents();
    }

    public int getCount() {
      return events.size();
    }

    public ModelEvent getItem(int position) {
      return events.get(position);
    }

    public long getItemId(int position) {
      return position;
    }

    /**
     * This function will be called once for every element in the listView control, when it needs to
     * draw itself. It should return a constructed view representing the data in the position
     * specified. Each element in the listView is an Event item, so we display the Event's icon and
     * title.
     * 
     * TODO: Use convertView when possible instead of always creating new views.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
      LinearLayout ll = new LinearLayout(context);
      ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
      ll.setMinimumHeight(50);
      ll.setOrientation(LinearLayout.HORIZONTAL);
      ll.setGravity(Gravity.CENTER_VERTICAL);

      // Icon of the event.
      ImageView iv = new ImageView(context);
      iv.setImageResource(events.get(position).getIconResId());
      iv.setAdjustViewBounds(true);
      iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      if (listView.getCheckedItemPosition() == position) {
        iv.setBackgroundResource(R.drawable.icon_hilight);
      }

      // Title of the event.
      TextView tv = new TextView(context);
      String text=events.get(position).getDescriptionShort();
      int numOfRules=UIDbHelperStore.instance().db().getRuleCount(events.get(position).getDatabaseId());
      if (numOfRules == 1) {
        text += " (1 rule)";
      } else if (numOfRules > 1) {
        text += " (" + numOfRules + " rules)";
      }
      tv.setText(text);
      tv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
      tv.setGravity(Gravity.CENTER_VERTICAL);
      tv.setPadding(10, 0, 0, 0);
      tv.setTextSize(14.0f);
      tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
      tv.setTextColor(context.getResources().getColor(R.color.list_element_text));
      tv.setMinHeight(46);

      ll.addView(iv);
      ll.addView(tv);
      return ll;
    }
  }
}