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
import android.widget.EditText;
import edu.nyu.cs.omnidroid.app.controller.datatypes.DataType;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniText;
import edu.nyu.cs.omnidroid.app.view.simple.UtilUI;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAttribute;

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
