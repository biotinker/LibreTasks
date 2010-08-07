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

import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniArea;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniCheckBoxInput;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniPasswordInput;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniPhoneNumber;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniText;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniTimePeriod;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniUserAccount;
import edu.nyu.cs.omnidroid.app.model.DataTypeIDLookup;
import edu.nyu.cs.omnidroid.app.view.simple.UIDbHelperStore;
import android.app.Activity;

/**
 * Singleton factory for {@link ViewItem} instances.
 */
public class ViewItemFactory {
  private static final ViewItemFactory INSTANCE = new ViewItemFactory();

  public final long PHONE_NUMBER_DATATYPE_DB_ID;
  public final long TEXT_DATATYPE_DB_ID;
  public final long AREA_DATATYPE_DB_ID;
  public final long PASSWORD_INPUT_DATATYPE_DB_ID;
  public final long CHECK_BOX_DATATYPE_DB_ID;
  public final long USER_ACCOUNT_DATATYPE_DB_ID;
  public final long DATE_DATATYPE_DB_ID;
  public final long TIME_PERIOD_DATATYPE_DB_ID;

  private ViewItemFactory() {
    DataTypeIDLookup lookup = UIDbHelperStore.instance().getDatatypeLookup();

    PHONE_NUMBER_DATATYPE_DB_ID = lookup.getDataTypeID(OmniPhoneNumber.DB_NAME);
    TEXT_DATATYPE_DB_ID = lookup.getDataTypeID(OmniText.DB_NAME);
    AREA_DATATYPE_DB_ID = lookup.getDataTypeID(OmniArea.DB_NAME);
    PASSWORD_INPUT_DATATYPE_DB_ID = lookup.getDataTypeID(OmniPasswordInput.DB_NAME);
    CHECK_BOX_DATATYPE_DB_ID = lookup.getDataTypeID(OmniCheckBoxInput.DB_NAME);
    USER_ACCOUNT_DATATYPE_DB_ID = lookup.getDataTypeID(OmniUserAccount.DB_NAME);
    DATE_DATATYPE_DB_ID = lookup.getDataTypeID(OmniDate.DB_NAME);
    TIME_PERIOD_DATATYPE_DB_ID = lookup.getDataTypeID(OmniTimePeriod.DB_NAME);
  }

  /**
   * Get an instance of {@link ViewItemFactory}.
   * 
   * @return an instance of {@link ViewItemFactory}
   */
  public static ViewItemFactory instance() {
    return INSTANCE;
  }

  /**
   * Create an instance of {@link ViewItem}
   * 
   * @param itemID
   *          id for {@link ViewItem}
   * @param dataTypeID
   *          database id for the datatype
   * @param activity
   *          {@link Activity} instance where the {@link ViewItem} will be attached to
   * @return an instance of {@link ViewItem}
   */
  public ViewItem create(int itemID, long dataTypeID, Activity activity) {
    ViewItem viewItem = null;

    if (dataTypeID == PHONE_NUMBER_DATATYPE_DB_ID) {
      viewItem = new PhoneNumberViewItem(itemID, dataTypeID, activity);
    } else if (dataTypeID == TEXT_DATATYPE_DB_ID) {
      viewItem = new TextViewItem(itemID, dataTypeID, activity);
    } else if (dataTypeID == AREA_DATATYPE_DB_ID) {
      viewItem = new AreaViewItem(itemID, dataTypeID, activity);
    } else if (dataTypeID == PASSWORD_INPUT_DATATYPE_DB_ID) {
      viewItem = new PasswordInputViewItem(itemID, dataTypeID, activity);
    } else if (dataTypeID == USER_ACCOUNT_DATATYPE_DB_ID) {
      viewItem = new UserAccountViewItem(itemID, dataTypeID, activity);
    } else if (dataTypeID == DATE_DATATYPE_DB_ID) {
      viewItem = new DateViewItem(itemID, dataTypeID, activity);
    } else if (dataTypeID == TIME_PERIOD_DATATYPE_DB_ID) {
      viewItem = new TimePeriodViewItem(itemID, dataTypeID, activity);
    } else if (dataTypeID == CHECK_BOX_DATATYPE_DB_ID) {
      viewItem = new CheckBoxViewItem(itemID, dataTypeID, activity);
    } else {
      throw new IllegalArgumentException("Unknown Datatype ID: " + dataTypeID);
    }

    return viewItem;
  }
}
