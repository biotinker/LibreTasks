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
import android.widget.TimePicker;
import libretasks.app.controller.datatypes.DataType;
import libretasks.app.controller.datatypes.OmniArea;
import libretasks.app.controller.datatypes.OmniDate;
import libretasks.app.controller.datatypes.OmniTimePeriod;
import libretasks.app.view.simple.model.ModelAttribute;
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
