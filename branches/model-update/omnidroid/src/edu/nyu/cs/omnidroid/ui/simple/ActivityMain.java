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
package edu.nyu.cs.omnidroid.ui.simple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.nyu.cs.omnidroid.R;

/**
 * This is the main entry point of the application.
 */
public class ActivityMain extends Activity {

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Initialize database interface.
    DbInterfaceUI.init(this);
    
    // Link up click handlers with their buttons.
    Button btnCreateRule = (Button) findViewById(R.id.activity_dummyui_btnCreateRule);
    btnCreateRule.setOnClickListener(listenerBtnClickCreateRule);

    Button btnViewRules = (Button) findViewById(R.id.activity_dummyui_btnViewRules);
    btnViewRules.setOnClickListener(listenerBtnClickViewRules);

    Button btnHelp = (Button) findViewById(R.id.activity_dummyui_btnHelp);
    btnHelp.setOnClickListener(listenerBtnClickHelp);
  }

  /**
   * Launch the create-a-new-rule activity.
   */
  private OnClickListener listenerBtnClickCreateRule = new OnClickListener() {
    public void onClick(View v) {
      // Wipe UI state for the activity.
      SharedPreferences state = v.getContext().getSharedPreferences(
          ActivityChooseRootEvent.KEY_STATE,
          Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
      state.edit().clear().commit();

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
      UtilUI.showAlert(v.getContext(), "Sorry!", "Viewing saved rules is not yet implemented!");
    }
  };

  /**
   * Help info.
   */
  private OnClickListener listenerBtnClickHelp = new OnClickListener() {
    public void onClick(View v) {
      UtilUI.showAlert(v.getContext(), "Sorry!", "Help is not yet available!");
    }
  };
}