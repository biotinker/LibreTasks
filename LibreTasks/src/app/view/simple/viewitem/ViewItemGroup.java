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
package libretasks.app.view.simple.viewitem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import libretasks.app.controller.datatypes.DataType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Container class for holding sets of {@link ViewItem} objects.
 */
public class ViewItemGroup {
  public static final String INTENT_EXTRA_SOURCE_ID = "source ID";

  private final Map<Integer, ViewItem> items;
  private final LinearLayout layout;

  /**
   * Create a new {@link ViewItemGroup} instance
   * 
   * @param activity
   *          the {@link Activity} instance using this object
   */
  public ViewItemGroup(Activity activity) {
    layout = new LinearLayout(activity);
    layout.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
        LayoutParams.FILL_PARENT));

    layout.setOrientation(LinearLayout.VERTICAL);

    items = new HashMap<Integer, ViewItem>();
  }

  /**
   * Add an item and append its UI representation to this container
   * 
   * @param item
   *          the item to be added. Items are mapped based on their ID.
   * @param initData
   *          data to be used for initializing the values when creating a view from {@code item}.
   *          Pass null for no data.
   * @throws IllegalStateException
   *           if this container already contains a {@link ViewItem} instance with the same ID as
   *           {@code item}
   */
  public void addViewItem(ViewItem item, DataType initData) throws IllegalStateException {
    int key = item.getID();

    if (items.containsKey(key)) {
      throw new IllegalStateException("ViewItem with ID = " + key + " already exists!");
    } else {
      items.put(key, item);
      layout.addView(item.buildUI(initData));
    }
  }

  /**
   * Add a view to the UI representation of this container. The view element added will not be be
   * managed by this container. This means that {@link View} objects added with this method cannot
   * be extracted with the {@code get} method.
   * 
   * @param view
   *          the {@link View} instance to be added
   */
  public void addView(View view) {
    layout.addView(view);
  }

  /**
   * Get an item in the container specified by id
   * 
   * @param id
   *          the ID of the item to be extracted
   * @return an instance of the item if it exists in this container, null otherwise
   */
  public ViewItem get(int id) {
    return items.get(id);
  }

  /**
   * Get all the items in this container.
   * 
   * @return all the items in this container
   */
  public Collection<ViewItem> getItems() {
    return items.values();
  }

  /**
   * Save the state of all the items in this container to the bundle.
   * 
   * @param bundle
   *          {@link Bundle} object where the state will be saved
   */
  public void saveState(Bundle bundle) {
    for (ViewItem item : getItems()) {
      item.saveState(bundle);
    }
  }

  /**
   * Load the state of the items in this container
   * 
   * @param bundle
   *          {@link Bundle} object where the state will be extracted
   * @throws Exception
   */
  public void loadState(Bundle bundle) throws Exception {
    if (bundle != null && !bundle.isEmpty()) {
      for (ViewItem item : getItems()) {
        item.loadState(bundle);
      }
    }
  }

  /**
   * Get the UI representation (layout instance) beneath this container.
   * 
   * @return the layout instance
   */
  public LinearLayout getLayout() {
    return layout;
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (data != null) {
      int sourceID = data.getIntExtra(INTENT_EXTRA_SOURCE_ID, -1);

      if (sourceID != -1) {
        items.get(sourceID).onActivityResult(requestCode, resultCode, data);
      }
    }
  }
}
