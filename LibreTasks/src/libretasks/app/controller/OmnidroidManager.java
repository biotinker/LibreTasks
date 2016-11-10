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

package libretasks.app.controller;

import libretasks.app.R;
import libretasks.app.controller.bkgservice.BCReceiver;
import libretasks.app.controller.external.attributes.EventMonitoringService;
import libretasks.app.controller.util.Logger;
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
   * @param context
   *          context in which the action needs to be performed.
   * @param enable
   * <br>
   *          true if intended to enable; <br>
   *          false if intended to disable
   */
  public static void enable(Context context, boolean enable) {
    ComponentName componentName = new ComponentName(context.getPackageName(), BCReceiver.class
        .getName());
    if (enable) {
      Logger.w(TAG, "Starting LibreTasks.");
      // Start service monitors and set app to enabled state
      context.getPackageManager().setComponentEnabledSetting(componentName,
          PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
      EventMonitoringService.startService(context);
    } else {
      Logger.w(TAG, "Stopping LibreTasks.");
      // Stop service monitors and set app to disabled state
      context.getPackageManager().setComponentEnabledSetting(componentName,
          PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
      EventMonitoringService.stopService(context);
    }

    /*
     * Set Preference to enabled status in case we disable/enable Omnidroid on the code side. If
     * status is changed from the code side, then this ensures the status in ActivitySettings is
     * updated as well.
     */
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(context.getString(R.string.pref_key_libretasks_enabled), enable);
    editor.commit();
  }
}
