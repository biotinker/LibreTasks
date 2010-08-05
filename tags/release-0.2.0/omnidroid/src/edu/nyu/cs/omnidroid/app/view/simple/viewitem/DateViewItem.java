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
import android.widget.TimePicker;
import edu.nyu.cs.omnidroid.app.controller.datatypes.DataType;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniArea;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniTimePeriod;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAttribute;
import java.util.Date;

/**
 * {@link ViewItem} implementation for {@link OmniDate}
 */
public class DateViewItem extends AbstractViewItem {
  private final TimePicker timePicker;

  /**
   * Class Constructor.
   * 
   * @param id
   *          the id used to uniquely identify this object.
   * @param dataTypeDbID
   *          the database id for {@link OmniArea}
   * @param activity
   *          the activity where this view item is to be built on
   */
  public DateViewItem(int id, long dataTypeDbID, Activity activity) {
    super(id, dataTypeDbID);

    timePicker = new TimePicker(activity);
    timePicker.setId(id);
    timePicker.setIs24HourView(false);
  }

  /**
   * Create insert this object's {@link View} objects into {@code viewGroup}
   * 
   * @param initData
   *          data to be used for initializing the values in the {@link View} objects. Pass null for
   *          no data. Note that this should be an instance of {@link OmniDate}
   * @return the {@link View} object representing the underlying {@link OmniDate} object
   */
  public View buildUI(DataType initData) {
    if (initData != null) {
      Date date = ((OmniDate) initData).getDate();

      timePicker.setCurrentHour(date.getHours());
      timePicker.setCurrentMinute(date.getMinutes());
    }

    return timePicker;
  }

  /**
   * {@inheritDoc}
   */
  public DataType getData() throws Exception {
    return new OmniDate(OmniTimePeriod.buildTimeString(1, 1, 1, timePicker.getCurrentHour(),
        timePicker.getCurrentMinute(), 0));
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
  public void loadState(Bundle bundle) throws Exception {
    String key = String.valueOf(ID);

    if (bundle.containsKey(key)) {
      Date date = OmniTimePeriod.getDate(bundle.getString(key));
      timePicker.setCurrentHour(date.getHours());
      timePicker.setCurrentMinute(date.getMinutes());
    }
  }

  /**
   * {@inheritDoc}
   */
  public void saveState(Bundle bundle) {
    String date = OmniTimePeriod.buildTimeString(1, 1, 1, timePicker.getCurrentHour(), timePicker
        .getCurrentMinute(), 0);
    bundle.putString(String.valueOf(ID), date);
  }
}
