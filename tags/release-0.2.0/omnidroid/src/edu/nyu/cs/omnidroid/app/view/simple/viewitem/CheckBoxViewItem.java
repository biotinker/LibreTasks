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
package edu.nyu.cs.omnidroid.app.view.simple.viewitem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import edu.nyu.cs.omnidroid.app.controller.datatypes.DataType;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniCheckBoxInput;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAttribute;

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
