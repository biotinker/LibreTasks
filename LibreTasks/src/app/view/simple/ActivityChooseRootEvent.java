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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import libretasks.app.R;
import libretasks.app.view.simple.model.ModelEvent;

/**
 * This activity gives users a chance to pick a root event for a new rule. After a user chooses a
 * root event, we move them to {@link ActivityChooseFilters} to continue building their rule.
 */
public class ActivityChooseRootEvent extends Activity {
  // Request codes for creating new activities
  private static final int REQUEST_ACTIVITY_CHOOSE_FILTERS_ACTIONS = 0;

  // Result code for successfully creating a rule
  public static final int RESULT_RULE_CREATED = 1;

  private ListView listView;
  private AdapterEvents adapterEvents;

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

    listView.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // Store the rule in the global RuleBuilder.
        RuleBuilder.instance().resetForNewRuleEditing(adapterEvents.getItem(position));

        // Wipe UI state for the new activity.
        ActivityChooseFiltersAndActions.resetUI(v.getContext());

        /*
         * Move them along to the ActivityChooseFilters activity where they can start adding some
         * filters.
         */
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ActivityChooseFiltersAndActions.class);
        startActivityForResult(intent, REQUEST_ACTIVITY_CHOOSE_FILTERS_ACTIONS);
      }
    });
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_ACTIVITY_CHOOSE_FILTERS_ACTIONS
        && resultCode == ActivityChooseFiltersAndActions.RESULT_RULE_SAVED) {
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
      String text = events.get(position).getDescriptionShort();

      int numOfRules = UIDbHelperStore.instance().db().getRuleCount(
          events.get(position).getDatabaseId());
      if (numOfRules == 1) {
        text += getString(R.string.one_rule);
      } else if (numOfRules > 1) {
        text += getString(R.string.n_rules, numOfRules);
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
