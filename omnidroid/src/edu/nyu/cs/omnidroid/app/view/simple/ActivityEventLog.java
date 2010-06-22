/*******************************************************************************
 * Copyright 2010 OmniDroid - http://code.google.com/p/omnidroid 
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

import android.app.ExpandableListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.app.model.LogEvent;
import edu.nyu.cs.omnidroid.app.model.UIDbHelper;

/**
 * This activity is used to add multiple filters or actions to a new rule.
 */
public class ActivityEventLog extends ExpandableListActivity {
  // TODO(acase): Sortable by KEY_TIMESTAMP
  // TODO(acase): Sortable by KEY_APPNAME
  // TODO(acase): Sortable by KEY_EVENTNAME
  // TODO(acase): Sortable by KEY_EVENTPARAMETERS
  // TODO(acase): Filter by KEY_TIMESTAMP
  // TODO(acase): Filter by KEY_APPNAME
  // TODO(acase): Filter by KEY_EVENTNAME
  // TODO(acase): Filter by KEY_EVENTPARAMETERS
  // TODO(acase): User customizable logs (e.g. "don't log TimeTick")

  // Store state
  private static final String KEY_STATE = "StateActivityEventLog";

  private ExpandableListAdapter logListAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Query for logs
    Cursor cursor = UIDbHelperStore.instance().db().getEventLogsCursor();

    // Set up our adapter
    logListAdapter = new LogListAdapter(this, cursor);
    setListAdapter(logListAdapter);
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

  /**
   * This is a small adapter class that allows for the display of EventLog items in an {@code
   * ExpandableListAdapter}
   */
  public class LogListAdapter extends BaseExpandableListAdapter {
    private static final int LOGS_PER_ITEM = 1;

    private ArrayList<LogEvent> logs;

    LogListAdapter(Context context, Cursor cursor) {
      UIDbHelper dbHelper = new UIDbHelper(context);
      logs = (ArrayList<LogEvent>) dbHelper.getEventLogs();
    }

    public LogEvent getChild(int groupPosition, int childPosition) {
      return logs.get(groupPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
      return logs.get(groupPosition).getID();
    }

    public int getChildrenCount(int groupPosition) {
      return LOGS_PER_ITEM;
    }

    public TextView getParentView() {
      // Layout parameters for the ExpandableListView
      AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
          ViewGroup.LayoutParams.FILL_PARENT, 64);
      TextView textView = new TextView(ActivityEventLog.this);
      textView.setLayoutParams(lp);
      // Center the text vertically
      textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
      // Set the text starting position
      textView.setPadding(36, 0, 0, 0);
      return textView;
    }

    public TextView getChildView() {
      // Layout parameters for the ExpandableListView
      AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
          ViewGroup.LayoutParams.FILL_PARENT, 92);
      TextView textView = new TextView(ActivityEventLog.this);
      textView.setLayoutParams(lp);
      // Center the text vertically
      textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
      // Set the text starting position
      textView.setPadding(36, 0, 0, 0);
      return textView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
        View convertView, ViewGroup parent) {
      TextView textView = getChildView();
      textView.setText(getChild(groupPosition, childPosition).toStringLong());
      return textView;
    }

    public Object getGroup(int groupPosition) {
      return logs.get(groupPosition);
    }

    public int getGroupCount() {
      return logs.size();
    }

    public long getGroupId(int groupPosition) {
      return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
        ViewGroup parent) {
      TextView textView = getParentView();
      textView.setText(getGroup(groupPosition).toString());
      return textView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return false;
    }

    public boolean hasStableIds() {
      return true;
    }
  }
}