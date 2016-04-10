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
import android.widget.EditText;
import libretasks.app.controller.datatypes.DataType;
import libretasks.app.controller.datatypes.OmniText;
import libretasks.app.view.simple.UtilUI;
import libretasks.app.view.simple.model.ModelAttribute;

/**
 * {@link ViewItem} implementation for {@link OmniText}
 */
public class TextViewItem extends AbstractViewItem {
  protected final EditText editText;

  /**
   * Class Constructor.
   * 
   * @param id
   *          the id used to uniquely identify this object.
   * @param dataTypeDbID
   *          the database id for {@link OmniText}
   * @param activity
   *          the activity where this view item is to be built on
   */
  public TextViewItem(int id, long dataTypeDbID, Activity activity) {
    super(id, dataTypeDbID);

    editText = new EditText(activity);
    editText.setId(id);
  }

  /**
   * {@inheritDoc}
   */
  public View buildUI(DataType initData) {
    if (initData != null) {
      editText.setText(initData.getValue());
    }

    return(editText);
  }

  /**
   * {@inheritDoc}
   */
  public DataType getData() throws Exception {
    return new OmniText(editText.getText().toString());
  }

  /**
   * {@inheritDoc}
   */
  public void insertAttribute(ModelAttribute attribute) {
    UtilUI.replaceEditText(editText, getAttributeInsertName(attribute));
  }

  /**
   * {@inheritDoc}
   */
  public void loadState(Bundle bundle) {
    String key = String.valueOf(ID);

    if (bundle.containsKey(key)) {
      editText.setText(bundle.getString(key));
    }
  }

  /**
   * {@inheritDoc}
   */
  public void saveState(Bundle bundle) {
    bundle.putString(String.valueOf(ID), editText.getText().toString());
  }
}
