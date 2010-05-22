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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.external.attributes.EventMonitoringService;

/**
 * This is the main entry point of the application. Here the user will see a main menu where they
 * can choose to create a new rule, view existing rules, or see help items.
 */
public class ActivityMain extends Activity {

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    // Initialize singleton instance of UIDbHelperStore, which is
    // our connection to the omnidroid database.
    UIDbHelperStore.init(this);
    
    // Make sure the background monitoring service is running.
    EventMonitoringService.startService(this);

    // Link up click handlers with their buttons.
    Button btnCreateRule = (Button) findViewById(R.id.activity_main_btnCreateRule);
    btnCreateRule.setOnClickListener(listenerBtnClickCreateRule);

    Button btnViewRules = (Button) findViewById(R.id.activity_main_btnViewRules);
    btnViewRules.setOnClickListener(listenerBtnClickViewRules);

    Button btnViewLogs = (Button) findViewById(R.id.activity_main_btnEventLog);
    btnViewLogs.setOnClickListener(listenerBtnClickViewLogs);

    Button btnHelp = (Button) findViewById(R.id.activity_main_btnHelp);
    btnHelp.setOnClickListener(listenerBtnClickHelp);

    Button btnResetDB = (Button) findViewById(R.id.activity_main_btnResetDB);
    btnResetDB.setOnClickListener(listenerBtnClickResetDb);
  }

  /**
   * Launch the create-a-new-rule activity.
   */
  private OnClickListener listenerBtnClickCreateRule = new OnClickListener() {
    public void onClick(View v) {
      ActivityChooseRootEvent.resetUI(v.getContext());

      // User wants to create a new rule, move them to the ActivityChooseRootEvent
      // activity so they can choose the root event.
      Intent intent = new Intent();
      intent.setClass(getApplicationContext(), ActivityChooseRootEvent.class);
      startActivity(intent);
    }
  };

  /**
   * View saved rules.
   */
  private OnClickListener listenerBtnClickViewRules = new OnClickListener() {
    public void onClick(View v) {
      ActivitySavedRules.resetUI(v.getContext());
        
      Intent intent = new Intent();
      intent.setClass(getApplicationContext(), ActivitySavedRules.class);
      startActivity(intent);
    }
  };

  /**
   * View logs.
   */
  private OnClickListener listenerBtnClickViewLogs = new OnClickListener() {
    public void onClick(View v) {
      // TODO: Implement showing logs activity.
      UtilUI.showAlert(v.getContext(), "Sorry!", "Viewing event logs is not yet implemented!");
    }
  };

  /**
   * Help info.
   */
  private OnClickListener listenerBtnClickHelp = new OnClickListener() {
    public void onClick(View v) {
      // TODO: Implement showing help activity.
      UtilUI.showAlert(v.getContext(), "Sorry!", "Help is not yet available!");
    }
  };

  /**
   * Reset the database, all info will be reset, user set rules will be lost
   */
  private OnClickListener listenerBtnClickResetDb = new OnClickListener() {
    public void onClick(View v) {
      UIDbHelperStore.instance().db().resetDB();
    }
  };
}