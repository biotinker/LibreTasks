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
package libretasks.app.controller.bkgservice;

import libretasks.app.R;
import libretasks.app.controller.OmnidroidManager;
import libretasks.app.view.simple.UtilUI;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * This broadcast receiver detect intents including System Boot, OmniStart and OmniRestart to
 * complete necessary operations
 */
public class Starter extends BroadcastReceiver {

  public void onReceive(Context context, Intent intent) {

    if (android.content.Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
      // Start the background monitoring service on boot
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
      if (sharedPreferences
          .getBoolean(context.getString(R.string.pref_key_libretasks_enabled), true)) {
        OmnidroidManager.enable(context, true);
      }
      UtilUI.loadNotifications(context);
    } else if ("OmniStart".equals(intent.getAction())) {
      // Start the background monitoring service by request
      OmnidroidManager.enable(context, true);
    } else if ("OmniRestart".equals(intent.getAction())) {
      /*
       * Restart the background monitoring service on request (maybe we loaded new data and need a
       * restart)
       */
      OmnidroidManager.enable(context, false);
      OmnidroidManager.enable(context, true);
    } else if ("OmniStop".equals(intent.getAction())) {
      // Stop the background monitoring services by request
      OmnidroidManager.enable(context, false);
    }
  }

}
