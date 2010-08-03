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

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TabHost;
import edu.nyu.cs.omnidroid.app.R;

/**
 * This activity is used to display the logs stored in the db.
 */
public class ActivityLogs extends TabActivity {

  // General global variables
  protected ListView listView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_logs);

    Resources res = getResources(); // Resource object to get Drawables
    TabHost tabHost = getTabHost(); // The activity TabHost
    TabHost.TabSpec spec; // Resusable TabSpec for each tab
    Intent intent; // Reusable Intent for each tab


    // Add All Logs tab
    intent = new Intent().setClass(this, ActivityLogTabs.class);
    intent.putExtra(ActivityLogTabs.KEY_INTENT_LOG_TYPE, ActivityLogTabs.KEY_ALL_LOGS);
    spec = tabHost.newTabSpec("allLogs").setIndicator(getString(R.string.All),
        res.getDrawable(R.drawable.icon_log_all_small)).setContent(intent);
    tabHost.addTab(spec);
    // Add Event Logs tab
    intent = new Intent().setClass(this, ActivityLogTabs.class);
    intent.putExtra(ActivityLogTabs.KEY_INTENT_LOG_TYPE, ActivityLogTabs.KEY_EVENT_LOGS);
    spec = tabHost.newTabSpec("eventLogs").setIndicator(getString(R.string.Events),
        res.getDrawable(R.drawable.icon_event_unknown_small)).setContent(intent);
    tabHost.addTab(spec);
    // Add Action Logs tab
    intent = new Intent().setClass(this, ActivityLogTabs.class);
    intent.putExtra(ActivityLogTabs.KEY_INTENT_LOG_TYPE, ActivityLogTabs.KEY_ACTION_LOGS);
    spec = tabHost.newTabSpec("actionLogs").setIndicator(getString(R.string.Actions),
        res.getDrawable(R.drawable.icon_action_unknown_small)).setContent(intent);
    tabHost.addTab(spec);
    // Add General Logs tab
    intent = new Intent().setClass(this, ActivityLogTabs.class);
    intent.putExtra(ActivityLogTabs.KEY_INTENT_LOG_TYPE, ActivityLogTabs.KEY_GENERAL_LOGS);
    spec = tabHost.newTabSpec("generalLogs").setIndicator(getString(R.string.General),
        res.getDrawable(R.drawable.icon_log_general_small)).setContent(intent);
    tabHost.addTab(spec);

    tabHost.setCurrentTab(0);
  }
}