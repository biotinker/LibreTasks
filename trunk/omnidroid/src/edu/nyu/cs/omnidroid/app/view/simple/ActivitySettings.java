/*******************************************************************************
 * Copyright 2009 Omnidroid - http://code.google.com/p/omnidroid 
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

import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.model.db.RuleDbAdapter;
import edu.nyu.cs.omnidroid.app.controller.OmnidroidManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * This activity shows all the settings/preferences
 */
public class ActivitySettings extends PreferenceActivity {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.layout.activity_settings);
    
    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(
        new OnSharedPreferenceChangeListener() {
      public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
         if (key.equals(getString(R.string.pref_key_omnidroid_enabled))) {
          //TODO consider Alert Dialog, 
          //you have this many rules anabled, are you sure ...
          OmnidroidManager.enable(getApplicationContext(), sharedPreferences.getBoolean(key, true));
        } else if (key.equals(getString(R.string.pref_key_notification))) {
          setNotification(sharedPreferences.getBoolean(key, true));          
        }                        
      }
    });
  }
  
  private void setNotification(boolean defaultNotificationIsOn) {
    //TODO consider setting notifications on/off for existing rules;
    RuleDbAdapter.setDefaultNotificationValue(defaultNotificationIsOn);
  }
}
