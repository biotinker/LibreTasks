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
 * Copyright 2010 Omnidroid - http://code.google.com/p/omnidroid 
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

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import libretasks.app.R;
import libretasks.app.view.simple.model.ModelLog;

/**
 * This activity is used to display the logs stored in the db.
 */
public class ActivityLogTabs extends Activity {
  // TODO(acase): Implement sortable by KEYS
  // TODO(acase): Implement filterable by KEYS
  // TODO(acase): Implement user customizable logs (e.g. "don't log TimeTick")
  // TODO(acase): Restore state
  
  // Store state
  private static final String KEY_STATE = "StateActivityLog";
  private static final String KEY_STATE_SELECTED_LOG = "logSelected";
  private SharedPreferences state;

  // The type of logs to pull up
  public static final String KEY_INTENT_LOG_TYPE = "logType";
  public static final int KEY_ALL_LOGS = 1;
  public static final int KEY_EVENT_LOGS = 2;
  public static final int KEY_ACTION_LOGS = 3;
  public static final int KEY_GENERAL_LOGS = 4;
 
  // Menu items
  private static final int MENU_SETTINGS = 0;
  private static final int MENU_CLEAR_LOGS = 1;

  
  // General global variables
  protected ListView listView;
  
  // What type of logs are we viewing
  private int logTypeSelected;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_log_tab);

    // Connect to our db storage
    UIDbHelperStore.init(this);
    
    // Get type of log we should be displaying (default to ALL)
    logTypeSelected = KEY_ALL_LOGS;
    if (getIntent().getExtras() != null) {
      logTypeSelected = getIntent().getExtras().getInt(KEY_INTENT_LOG_TYPE, KEY_ALL_LOGS);
    }

    switch (logTypeSelected) {
    case (KEY_GENERAL_LOGS):
      UtilUI.clearNotification(this, UtilUI.NOTIFICATION_WARN);
      break;
    case (KEY_ACTION_LOGS):
      UtilUI.clearNotification(this, UtilUI.NOTIFICATION_RULE);
      UtilUI.clearNotification(this, UtilUI.NOTIFICATION_ACTION);
      break;
    }
    
    // Update the UI
    updateUI();

    // When a log is selected, bring up the activity to analyze it.
    listView.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view,
          int position, long id) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ActivityDlgLog.class);
        ModelLog log = (ModelLog) parent.getAdapter().getItem(position);
        long logID = log.getDatabaseId();
        int logType = log.getType();
        intent.putExtra(ActivityDlgLog.KEY_LOG_ID, logID);
        intent.putExtra(ActivityDlgLog.KEY_LOG_TYPE, logType);
        startActivity(intent);
      }
    });

  }

  @Override
  protected void onResume() {
    super.onResume();
    updateUI();
  }
  
  @Override
  protected void onPause() {
    super.onPause();

    // Save UI state.
    SharedPreferences.Editor prefsEditor = state.edit();
    prefsEditor.putInt(KEY_STATE_SELECTED_LOG, listView.getCheckedItemPosition());
    prefsEditor.commit();
  }

  /**
   * Update the UI display
   */
  private void updateUI() {
    // Update the Log List since new logs may have been created
    LogAdapter logAdapter = new LogAdapter(this);

    listView = (ListView) findViewById(R.id.activity_logs_listview);
    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    listView.setAdapter(logAdapter);

    // Restore UI control values if possible.
    state = getSharedPreferences(KEY_STATE, Context.MODE_WORLD_READABLE
        | Context.MODE_WORLD_WRITEABLE);
    listView.setItemChecked(state.getInt(KEY_STATE_SELECTED_LOG, -1), true);
  }

  /**
   * Wipes any UI state saves in {@link:state}. Activities which create this activity should call
   * this before launching so we appear as a brand new instance.
   * 
   * @param context
   *          - Context of caller.
   */
  public static void resetUI(Context context) {
    UtilUI.resetSharedPreferences(context, KEY_STATE);
  }

  /** Create a options menu for the main screen */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(Menu.NONE, MENU_SETTINGS, Menu.NONE, getString(R.string.settings_label)).setIcon(
        android.R.drawable.ic_menu_preferences).setAlphabeticShortcut('s');
    menu.add(Menu.NONE, MENU_CLEAR_LOGS, Menu.NONE,
        getString(R.string.clear_logs)).setAlphabeticShortcut('c')
        .setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    return super.onCreateOptionsMenu(menu);
  }

  /** Called when an item of options menu is clicked */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_SETTINGS:
      startActivity(new Intent(this, ActivitySettings.class));
      return true;
    case MENU_CLEAR_LOGS:
      switch (logTypeSelected) {
      case KEY_EVENT_LOGS:
        UIDbHelperStore.instance().db().deleteEventLogs();
        break;
      case KEY_ACTION_LOGS:
        UIDbHelperStore.instance().db().deleteActionLogs();
        break;
      case KEY_GENERAL_LOGS:
        UIDbHelperStore.instance().db().deleteGeneralLogs();
        break;
      case KEY_ALL_LOGS:
        UIDbHelperStore.instance().db().deleteAllLogs();
        break;
      }
    }
    updateUI();
    return super.onOptionsItemSelected(item);
  }

  /**
   * Handles rendering of log items for our ListView.
   * 
   */
  private class LogAdapter extends BaseAdapter {
    private Context context;
    private List<ModelLog> logs;

    public LogAdapter(Context context) {
      this.context = context;

      // Get a list of logs from the database.
      if (logTypeSelected == KEY_ALL_LOGS) {
        logs = UIDbHelperStore.instance().db().getAllLogs();
      } else if (logTypeSelected == KEY_EVENT_LOGS) {
        logs = UIDbHelperStore.instance().db().getEventLogs();
      } else if (logTypeSelected == KEY_ACTION_LOGS) {
        logs = UIDbHelperStore.instance().db().getActionLogs();
      } else if (logTypeSelected == KEY_GENERAL_LOGS) {
        logs = UIDbHelperStore.instance().db().getGeneralLogs();
      }
     
      // Tell our user when empty
      if (logs.isEmpty()) {
        Toast.makeText(context, getString(R.string.no_logs), Toast.LENGTH_LONG).show();
      }
    }

    public int getCount() {
      return logs.size();
    }

    public ModelLog getItem(int position) {
      return logs.get(position);
    }

    public long getItemId(int position) {
      return position;
    }

    /**
     * This function will be called once for every element in the listView control, when it needs to
     * draw itself. It should return a constructed view representing the data in the position
     * specified. Each element in the listView is a Log item, so we display the Log's icon and
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

      // Icon of the log.
      ImageView iv = new ImageView(context);
      iv.setImageResource(logs.get(position).getIconResId());
      iv.setAdjustViewBounds(true);
      iv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT));
      if (listView.getCheckedItemPosition() == position) {
        iv.setBackgroundResource(R.drawable.icon_hilight);
      }

      // Title of the log.
      TextView tv = new TextView(context);
      tv.setText(logs.get(position).getText());
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
