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
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import libretasks.app.controller.datatypes.DataType;
import libretasks.app.controller.datatypes.OmniCheckBoxInput;
import libretasks.app.view.simple.model.ModelAttribute;

/**
 * {@link ViewItem} implementation for {@link OmniCheckBoxInput}
 * 
 * Note: Refactored from FactoryActions (former class name @ r791), but not tested because there no
 * existing actions with this datatype as a parameter.
 */
public class CheckBoxViewItem extends AbstractViewItem {
  private final CheckBox checkBox;

  /**
   * Class Constructor.
   * 
   * @param id
   *          the id used to uniquely identify this object.
   * @param dataTypeDbID
   *          the database id for {@link OmniCheckBoxInput}
   * @param activity
   *          the activity where this view item is to be built on
   */
  public CheckBoxViewItem(int id, long dataTypeDbID, Activity activity) {
    super(id, dataTypeDbID);

    checkBox = new CheckBox(activity);
    checkBox.setId(id);
  }

  /**
   * {@inheritDoc}
   */
  public View buildUI(DataType initData) {
    if (initData != null) {
      checkBox.setChecked(Boolean.parseBoolean(initData.getValue()));
    }

    return(checkBox);
  }

  /**
   * {@inheritDoc}
   */
  public DataType getData() throws Exception {
    return new OmniCheckBoxInput(checkBox.isChecked());
  }

  /**
   * {@inheritDoc}
   */
  public void insertAttribute(ModelAttribute attribute) {
    // Do nothing. This class does not support attributes.
  }

  /**
   * {@inheritDoc}
   */
  public void loadState(Bundle bundle) {
    String key = String.valueOf(ID);

    if (bundle.containsKey(key)) {
      checkBox.setChecked(bundle.getBoolean(key, true));
    }
  }

  /**
   * {@inheritDoc}
   */
  public void saveState(Bundle bundle) {
    bundle.putBoolean(String.valueOf(ID), checkBox.isChecked());
  }
}
