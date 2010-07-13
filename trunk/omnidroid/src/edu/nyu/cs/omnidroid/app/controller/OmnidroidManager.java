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

import edu.nyu.cs.omnidroid.app.controller.bkgservice.BCReceiver;
import edu.nyu.cs.omnidroid.app.controller.external.attributes.EventMonitoringService;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * this class serves provides functionality disable/anable omnidroid; *
 */
public class OmnidroidManager {
  
  /** 
   * @param context  context in which the action needs to be performed.
   * @param enable <br> true if intended to enable;
   *               <br> false if intended to disable
   */
  public static void enable(Context context, boolean enable) {
    ComponentName componentName = new ComponentName(context.getPackageName(),
        BCReceiver.class.getName());
    if (enable) {
      context.getPackageManager().setComponentEnabledSetting(componentName, 
          PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
      EventMonitoringService.startService(context);
    } else {
      context.getPackageManager().setComponentEnabledSetting(componentName,
          PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
      EventMonitoringService.stopService(context);    
    }
  }
}