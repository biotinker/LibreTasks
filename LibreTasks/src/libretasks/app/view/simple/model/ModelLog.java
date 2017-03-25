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
package libretasks.app.view.simple.model;

import android.content.Context;
import libretasks.app.model.CoreActionLogsDbHelper;
import libretasks.app.model.CoreEventLogsDbHelper;
import libretasks.app.model.CoreGeneralLogsDbHelper;
import libretasks.app.model.CoreLogsDbHelper;
import libretasks.app.model.Log;

/**
 * UI representation of an Log.
 */
public class ModelLog extends ModelItem {
  // Logs have a timestamp that we can use for sorting
  Long timestamp = null;
  // Type of log
  int type = -1;

  // Types of ModelLogs that might be viewed
  public static final int TYPE_GENERAL = 1;
  public static final int TYPE_EVENT = 2;
  public static final int TYPE_ACTION = 3;

  public ModelLog(long databaseId, String name, String description, int iconResId,
      long timestamp, int type) {
    super(name, description, iconResId, databaseId);
    if (type == TYPE_GENERAL || type == TYPE_EVENT || type == TYPE_ACTION) {
      this.type = type;
    }
    this.timestamp = (Long) timestamp;
  }

  /**
   * @param context
   *            - application context for the db
   * @return a {@code Log} that corresponds with this ModelLog databaseId.
   */
  public Log getLog(Context context) {
    CoreLogsDbHelper logHelper;
    Log log = null;
    if (type == TYPE_GENERAL) {
      logHelper = new CoreGeneralLogsDbHelper(context);
      log = logHelper.getLogMatchingID(this.databaseId);
      logHelper.close();
    } else if (type == TYPE_EVENT) {
      logHelper = new CoreEventLogsDbHelper(context);
      log = logHelper.getLogMatchingID(this.databaseId);
      logHelper.close();
    } else if (type == TYPE_ACTION) {
      logHelper = new CoreActionLogsDbHelper(context);
      log = logHelper.getLogMatchingID(this.databaseId);
      logHelper.close();
    }
    return log; 
  }

  public int getType() {
    return type;
  }

  /**
   * @return Human readable String representation of the the type of log stored.
   */
  public String getTypeString() {
    if (type == TYPE_GENERAL) {
      return "General Log";
    } else if (type == TYPE_EVENT) {
      return "Event Log";
    } else if (type == TYPE_ACTION) {
      return "Action Log";
    } else {
      return "Unknown Log";
    }
  }

  /**
   * Compare based on timestamp.
   */
  public int compareTo(ModelLog anotherItem) {
    if (anotherItem.timestamp > this.timestamp) {
      return 1;
    } else if (anotherItem.timestamp < this.timestamp) {
      return -1;
    } else {
      return 0;
    }
  }

  /**
   * @return String representation of the {@code Log} stored in this Model.
   */
  public String getText() {
    return description;
  }
}
