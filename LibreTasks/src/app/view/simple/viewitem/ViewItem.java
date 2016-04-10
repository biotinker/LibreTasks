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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import libretasks.app.controller.datatypes.DataType;
import libretasks.app.view.simple.model.ModelAttribute;

/**
 * Interface for {@link DataType} adapters to {@link View} objects.
 */
public interface ViewItem {
  /**
   * Get the ID of this object
   * 
   * @return the ID of this object
   */
  public int getID();

  /**
   * Create the UI for this object.
   * 
   * @param initData
   *          data to be used for initializing the values in the {@link View} objects. Pass null for
   *          no data.
   * @return the {@link View} object representing the underlying {@link DataType}
   */
  public View buildUI(DataType initData);

  /**
   * Get the data underlying the controls
   * 
   * @return the data
   * @throws Exception when data is invalid
   */
  public DataType getData() throws Exception;

  /**
   * Get the database ID of the underlying datatype represented by this object
   * 
   * @return the database ID
   */
  public long getDataTypeDbID();

  /**
   * Save the state of this object into the {@code bundle}
   * 
   * @param bundle
   *          the {@link Bundle} object where the data will be saved
   */
  public void saveState(Bundle bundle);

  /**
   * Load the state of this object from the {@code bundle}
   * 
   * @param bundle
   *          the {@link Bundle} object where the data will be extracted
   * @throws Exception
   */
  public void loadState(Bundle bundle) throws Exception;

  /**
   * Insert an attribute parameter tag directly into the underlying {@link View}
   */
  public void insertAttribute(ModelAttribute attribute);

  /**
   * Callback method for this object when an activity finishes
   * 
   * @see {@link Activity#onActivityResult}
   */
  public void onActivityResult(int requestCode, int resultCode, Intent data);
}
