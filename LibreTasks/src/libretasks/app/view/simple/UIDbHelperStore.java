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
package libretasks.app.view.simple;

import android.content.Context;
import android.util.Log;
import libretasks.app.model.DataFilterIDLookup;
import libretasks.app.model.DataTypeIDLookup;
import libretasks.app.model.UIDbHelper;

/**
 * This singleton was created to hold an instance of the <code>UIDbHelper</code> class which
 * provides the UI package with representations of the core data. It needs to be initialized with a
 * <code>Context</code> instance, thus it is not a collection of static methods. The functionality
 * of this class could be merged directly into UIDbHelper, it would simply act as the singleton
 * instance itself.
 * 
 * TODO: (markww) Discuss merging this directly into UIDbHelper.
 */
public class UIDbHelperStore {
  private static final String TAG = UIDbHelperStore.class.getSimpleName();

  /** The one and only DbInterfaceUI instance. */
  private static UIDbHelperStore instance;

  /** Points to our internal db helper instance. */
  private UIDbHelper dbInstance;

  /** Looks up filters IDs by name. */
  private DataFilterIDLookup filterLookup;

  /** Looks up data type IDs by name. */
  private DataTypeIDLookup datatypeLookup;

  private UIDbHelperStore(Context context) {
    dbInstance = new UIDbHelper(context);
    filterLookup = new DataFilterIDLookup(context);
    datatypeLookup = new DataTypeIDLookup(context);
  }

  public static void init(Context context) {
    if (instance == null) {
      instance = new UIDbHelperStore(context);
    }
  }

  public static UIDbHelperStore instance() {
    // I could add Context as a parameter here, but then every caller needs to pass it in (or null
    // if they don't have a context which will happen like in the case of the UI factory class).
    if (instance == null) {
      throw new IllegalStateException(
          "UIDbHelperStore singleton must be initialized via init() before use!");
    }
    return instance;
  }

  public UIDbHelper db() {
    return dbInstance;
  }

  public DataFilterIDLookup getFilterLookup() {
    return filterLookup;
  }

  public DataTypeIDLookup getDatatypeLookup() {
    return datatypeLookup;
  }

  /**
   * Release all resources held by this helper.
   */
  public void releaseResources() {
    Log.i(TAG, "releasing resources");
    datatypeLookup.close();
    filterLookup.close();
    dbInstance.close();
    instance = null;
  }
}
