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

package edu.nyu.cs.omnidroid.app.controller;

import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.bkgservice.BCReceiver;
import edu.nyu.cs.omnidroid.app.controller.external.attributes.EventMonitoringService;
import edu.nyu.cs.omnidroid.app.controller.util.Logger;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

/**
 * this class serves provides functionality disable/enable omnidroid *
 */
public class OmnidroidManager {
  private static final String TAG = OmnidroidManager.class.getSimpleName();

  /** 
   * @param context  context in which the action needs to be performed.
   * @param enable <br> true if intended to enable;
   *               <br> false if intended to disable
   */
  public static void enable(Context context, boolean enable) {
    ComponentName componentName = new ComponentName(context.getPackageName(),
        BCReceiver.class.getName());
    if (enable) {
      Logger.w(TAG, "Starting Omnidroid.");
      // Start service monitors and set app to enabled state
      context.getPackageManager().setComponentEnabledSetting(componentName, 
          PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
      EventMonitoringService.startService(context);
    } else {
      Logger.w(TAG, "Stopping Omnidroid.");
      // Stop service monitors and set app to disabled state
      context.getPackageManager().setComponentEnabledSetting(componentName,
          PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
      EventMonitoringService.stopService(context);    
    }
    
    // Set Preference to enabled status in case we disable/enable Omnidroid on the code side.
    // If status is changed from the code side, 
    //then this ensures the status in ActivitySettings is updated as well.
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(context.getString(R.string.pref_key_omnidroid_enabled), enable);
    editor.commit();
  }
}
