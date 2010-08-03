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
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAttribute;

/**
 * {@link ViewItem} implementation for {@link OmniPasswordInput}
 */
public class PasswordInputViewItem extends TextViewItem {

  /**
   * Class Constructor.
   * 
   * @param id
   *          the id used to uniquely identify this object.
   * @param dataTypeDbID
   *          the database id for {@link OmniPasswordInput}
   * @param activity
   *          the activity where this view item is to be built on
   */
  public PasswordInputViewItem(int id, long dataTypeDbID, Activity activity) {
    super(id, dataTypeDbID, activity);

    // Make the text input password-type (Transforms characters into * after input)
    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    editText.setRawInputType(InputType.TYPE_CLASS_TEXT
        | InputType.TYPE_TEXT_VARIATION_PASSWORD);
  }
  
  /**
   * {@inheritDoc}
   */
  public void insertAttribute(ModelAttribute attribute) {
    // Do nothing. This class does not support attributes.
  }
}
