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
import edu.nyu.cs.omnidroid.app.controller.datatypes.DataType;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniPhoneNumber;
import edu.nyu.cs.omnidroid.app.view.simple.viewitem.ViewItem;

/**
 * {@link ViewItem} implementation for {@link OmniPhoneNumber}
 */
public class PhoneNumberViewItem extends TextViewItem {
  /**
   * Class Constructor.
   * 
   * @param id
   *          the id used to uniquely identify this object.
   * @param dataTypeDbID
   *          the database id for {@link OmniPhoneNumber}
   * @param activity
   *          the activity where this view item is to be built on
   */
  public PhoneNumberViewItem(int id, long dataTypeDbID, Activity activity) {
    super(id, dataTypeDbID, activity);
  }

  /**
   * {@inheritDoc}
   */
  public DataType getData() throws Exception {
    return new OmniPhoneNumber(editText.getText().toString());
  }
}
