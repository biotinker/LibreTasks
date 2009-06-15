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
package edu.nyu.cs.omnidroid.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;
import edu.nyu.cs.omnidroid.R;

/**
 * Display and allow a user to change the settings/preferences for OmniDroid
 * 
 */
public class Settings extends Activity {

  /**
   * Build our activity
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Create a base layout to present our settings
    setContentView(R.layout.settings);

    final ToggleButton omniabled_toggle = (ToggleButton) findViewById(R.id.settings_omniabled);
    omniabled_toggle.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        if (omniabled_toggle.isChecked()) {
          // FIXME(acase): Enable OmniDroid
        } else {
          // FIXME(acase): Disable OmniDroid
        }
      }
    });

    final ToggleButton on_boot_toggle = (ToggleButton) findViewById(R.id.settings_onboot);
    on_boot_toggle.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        if (on_boot_toggle.isChecked()) {
          // FIXME(acase): Set OmniDroid to start on boot
        } else {
          // FIXME(acase): Turn off OmniDroid on boot
        }
      }
    });

  }

}