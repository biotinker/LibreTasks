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
package edu.nyu.cs.omnidroid.app.view.simple;

import java.util.ArrayList;

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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.model.UIDbHelper;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelLog;

/**
 * This activity is used to display the logs stored in the db.
 */
public class ActivityLogs extends Activity {
  // TODO(acase): Implement sortable by KEYS
  // TODO(acase): Implement filterable by KEYS
  // TODO(acase): Implement user customizable logs (e.g. "don't log TimeTick")
  // TODO(acase): Restore state
  
  // Store state
  private static final String KEY_STATE = "StateActivityLog";
  private static final String KEY_STATE_SELECTED_LOG = "logSelected";
  private SharedPreferences state;

  // Log view options
  private static String KEY_ALL_LOGS;
  private static String KEY_EVENT_LOGS;
  private static String KEY_ACTION_LOGS;
  private static String KEY_GENERAL_LOGS;
 
  // Menu items
  private static final int MENU_CLEAR_ALL_LOGS = 1;
  private static final int MENU_CLEAR_EVENT_LOGS = 2;
  private static final int MENU_CLEAR_ACTION_LOGS = 3;
  private static final int MENU_CLEAR_GENERAL_LOGS = 4;

  // General global variables
  protected ListView listView;
  private String logsSelected;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_logs);

    // TODO(acase): Consider possibility of using a TabActivity instead.
    // Spinner (static initialization)
    KEY_ALL_LOGS = getString(R.string.all_logs);
    KEY_EVENT_LOGS = getString(R.string.event_logs);
    KEY_ACTION_LOGS = getString(R.string.action_logs);
    KEY_GENERAL_LOGS = getString(R.string.general_logs);
    logsSelected = KEY_ACTION_LOGS;
    String[] items = new String[] { KEY_ALL_LOGS, KEY_EVENT_LOGS, KEY_ACTION_LOGS, KEY_GENERAL_LOGS };
    Spinner spinner = (Spinner) findViewById(R.id.activity_logs_spinner);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, items);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(new LogTypeItemSelectedListener());

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
    // Update the Log List
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
    menu.add(Menu.NONE, MENU_CLEAR_EVENT_LOGS, Menu.NONE,
        getString(R.string.clear) + getString(R.string.event_logs)).setAlphabeticShortcut('e')
        .setIcon(R.drawable.icon_event_unknown);
    menu.add(Menu.NONE, MENU_CLEAR_ACTION_LOGS, Menu.NONE,
        getString(R.string.clear) + getString(R.string.action_logs)).setAlphabeticShortcut('a')
        .setIcon(R.drawable.icon_action_unknown);
    menu.add(Menu.NONE, MENU_CLEAR_GENERAL_LOGS, Menu.NONE,
        getString(R.string.clear) + getString(R.string.general_logs)).setAlphabeticShortcut('g')
        .setIcon(R.drawable.icon_general_log);
    menu.add(Menu.NONE, MENU_CLEAR_ALL_LOGS, Menu.NONE,
        getString(R.string.clear) + getString(R.string.all_logs)).setAlphabeticShortcut('c')
        .setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    return super.onCreateOptionsMenu(menu);
  }

  /** Called when an item of options menu is clicked */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    UIDbHelper dbHelper = new UIDbHelper(this);
    switch (item.getItemId()) {
    case MENU_CLEAR_EVENT_LOGS:
      dbHelper.deleteEventLogs();
      updateUI();
      return true;
    case MENU_CLEAR_ACTION_LOGS:
      dbHelper = new UIDbHelper(this);
      dbHelper.deleteActionLogs();
      updateUI();
      return true;
    case MENU_CLEAR_GENERAL_LOGS:
      dbHelper = new UIDbHelper(this);
      dbHelper.deleteGeneralLogs();
      updateUI();
      return true;
    case MENU_CLEAR_ALL_LOGS:
      dbHelper = new UIDbHelper(this);
      dbHelper.deleteAllLogs();
      updateUI();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Selects the type of logs to view. 
   */
  public class LogTypeItemSelectedListener implements OnItemSelectedListener {
    public void onItemSelected(AdapterView<?> parent,
        View view, int pos, long id) {
      logsSelected = parent.getItemAtPosition(pos).toString();
      updateUI();
    }

    public void onNothingSelected(AdapterView<?> parent) {
      // Do nothing.
    }
  }
  
  /**
   * Handles rendering of log items for our ListView.
   * 
   */
  private class LogAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ModelLog> logs;

    public LogAdapter(Context context) {
      this.context = context;

      // Get a list of logs from the database.
      if (logsSelected == KEY_ALL_LOGS) {
        logs = (ArrayList<ModelLog>) UIDbHelperStore.instance().db().getAllLogs();
      } else if (logsSelected == KEY_EVENT_LOGS) {
        logs = (ArrayList<ModelLog>) UIDbHelperStore.instance().db().getEventLogs();
      } else if (logsSelected == KEY_ACTION_LOGS) {
        logs = (ArrayList<ModelLog>) UIDbHelperStore.instance().db().getActionLogs();
      } else if (logsSelected == KEY_GENERAL_LOGS) {
        logs = (ArrayList<ModelLog>) UIDbHelperStore.instance().db().getGeneralLogs();
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
      return (ModelLog) logs.get(position);
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