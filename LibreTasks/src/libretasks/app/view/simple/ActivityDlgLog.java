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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import libretasks.app.R;
import libretasks.app.model.UIDbHelper;
import libretasks.app.view.simple.model.ModelLog;

/**
 * This dialog shows a the details of a specific log.
 */
public class ActivityDlgLog extends Activity {
  private static final String KEY_STATE = "StateDlgLog";
  private static final String KEY_PREF_ID = "selectedLogId";
  private static final String KEY_PREF_TYPE = "selectedLogType";

  protected static final String KEY_LOG_ID = "_ID";
  protected static final String KEY_LOG_TYPE = "_TYPE";
  
  private SharedPreferences state;
  private int type;
  private long id;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Find what Log we want to analyze
    getIntentData(getIntent());

    // Link up controls from the xml layout resource file.
    initializeUI();

    // Restore UI state if possible.
    state = getSharedPreferences(KEY_STATE, Context.MODE_WORLD_READABLE
        | Context.MODE_WORLD_WRITEABLE);
    id = state.getLong(KEY_PREF_ID, -1);
    type = state.getInt(KEY_PREF_TYPE, -1);
  }

  @Override
  protected void onPause() {
    super.onPause();

    // Save UI state.
    SharedPreferences.Editor prefsEditor = state.edit();
    prefsEditor.putLong(KEY_PREF_ID, id);
    prefsEditor.putInt(KEY_PREF_TYPE, type);
    prefsEditor.commit();
  }

  private void initializeUI() {
    setContentView(R.layout.activity_dlg_log);
    TextView logInfo = (TextView) findViewById(R.id.activity_dlg_log_info);
    UIDbHelper dbHelper = new UIDbHelper(this);
    ModelLog log = dbHelper.getLog(type, id);
    setTitle(log.getTypeString());
    logInfo.setText(log.getLog(this).toString());
    
    logInfo.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        finish();
      }
    });


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

  private void getIntentData(Intent i) {
    // intent data passed to us.
    Bundle extras = i.getExtras();

    if (extras != null) {
      id = extras.getLong(KEY_LOG_ID);
      type = extras.getInt(KEY_LOG_TYPE);
    }
  }
}
