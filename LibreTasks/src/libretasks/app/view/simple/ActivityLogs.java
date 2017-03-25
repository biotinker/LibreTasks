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

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TabHost;
import libretasks.app.R;

/**
 * This activity is used to display the logs stored in the db.
 */
public class ActivityLogs extends TabActivity {

  // General global variables
  protected ListView listView;
  
  protected static final String TAB_TAG_ALL_LOG = "allLogs";
  protected static final String TAB_TAG_EVENT_LOG = "eventLog";
  protected static final String TAB_TAG_ACTION_LOG = "actionLog";
  protected static final String TAB_TAG_GENERAL_LOG = "generalLog";
  
  public static final String KEY_TAB_TAG = "keyTabTag";

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
    spec = tabHost.newTabSpec(TAB_TAG_ALL_LOG).setIndicator(getString(R.string.All),
        res.getDrawable(R.drawable.icon_log_all_small)).setContent(intent);
    tabHost.addTab(spec);
    // Add Event Logs tab
    intent = new Intent().setClass(this, ActivityLogTabs.class);
    intent.putExtra(ActivityLogTabs.KEY_INTENT_LOG_TYPE, ActivityLogTabs.KEY_EVENT_LOGS);
    spec = tabHost.newTabSpec(TAB_TAG_EVENT_LOG).setIndicator(getString(R.string.Events),
        res.getDrawable(R.drawable.icon_event_unknown_small)).setContent(intent);
    tabHost.addTab(spec);
    // Add Action Logs tab
    intent = new Intent().setClass(this, ActivityLogTabs.class);
    intent.putExtra(ActivityLogTabs.KEY_INTENT_LOG_TYPE, ActivityLogTabs.KEY_ACTION_LOGS);
    spec = tabHost.newTabSpec(TAB_TAG_ACTION_LOG).setIndicator(getString(R.string.Actions),
        res.getDrawable(R.drawable.icon_action_unknown_small)).setContent(intent);
    tabHost.addTab(spec);
    // Add General Logs tab
    intent = new Intent().setClass(this, ActivityLogTabs.class);
    intent.putExtra(ActivityLogTabs.KEY_INTENT_LOG_TYPE, ActivityLogTabs.KEY_GENERAL_LOGS);
    spec = tabHost.newTabSpec(TAB_TAG_GENERAL_LOG).setIndicator(getString(R.string.General),
        res.getDrawable(R.drawable.icon_log_general_small)).setContent(intent);
    tabHost.addTab(spec);
    
    String tabTag = getIntent().getStringExtra(KEY_TAB_TAG);
    if (tabTag == null) {
      tabHost.setCurrentTabByTag(TAB_TAG_ALL_LOG);
    } else {
      Log.w("LOGS", "tabTag is "+ tabTag);
      tabHost.setCurrentTabByTag(tabTag);
    }
     
  }
}
