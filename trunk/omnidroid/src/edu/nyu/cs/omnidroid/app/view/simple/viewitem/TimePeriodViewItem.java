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

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.LinearLayout.LayoutParams;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.datatypes.DataType;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniArea;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniTimePeriod;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAttribute;

/**
 * {@link ViewItem} implementation for {@link OmniTimePeriod}
 */
public class TimePeriodViewItem extends AbstractViewItem {
  
  private final static String START_TIME = "StartTime";
  private final static String END_TIME = "EndTime";
  
  private final static int START_TIME_VIEW_ID = 0;
  private final static int END_TIME_VIEW_ID = 1;
  
  private final Activity mActivity;
  private final TimePicker startTimePicker;
  private final TimePicker endTimePicker;

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
  public TimePeriodViewItem(int id, long dataTypeDbID, Activity activity) {
    super(id, dataTypeDbID);
    mActivity = activity;

    startTimePicker = new TimePicker(activity);
    startTimePicker.setId(START_TIME_VIEW_ID);
    startTimePicker.setIs24HourView(false); // TODO (renctan): use locale info to decide on/off

    endTimePicker = new TimePicker(activity);
    endTimePicker.setId(END_TIME_VIEW_ID);
    endTimePicker.setIs24HourView(false); // TODO (renctan): use locale info to decide on/off
  }

  /**
   * Create insert this object's {@link View} objects into {@code viewGroup}
   * 
   * @param initData
   *          data to be used for initializing the values in the {@link View} objects. Pass null for
   *          no data. Note that this should be an instance of {@link OmniTimePeriod}
   * @return the {@link View} object representing the underlying {@link OmniTimePeriod} object
   */
  public View buildUI(DataType initData) {
    if (initData != null) {
      OmniTimePeriod timePeriod = (OmniTimePeriod) initData;
      startTimePicker.setCurrentHour(timePeriod.getStartHour());
      startTimePicker.setCurrentMinute(timePeriod.getStartMinute());

      endTimePicker.setCurrentHour(timePeriod.getEndHour());
      endTimePicker.setCurrentMinute(timePeriod.getEndMinute());
    }

    LinearLayout layout = new LinearLayout(mActivity);
    layout.setId(ID);
    layout.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
        LayoutParams.FILL_PARENT));
    layout.setOrientation(LinearLayout.VERTICAL);

    TextView tvInstructionsStart = new TextView(mActivity);
    tvInstructionsStart.setText(mActivity.getString(R.string.pick_start_time));
    layout.addView(tvInstructionsStart);

    layout.addView(startTimePicker);

    TextView tvInstructionsEnd = new TextView(mActivity);
    tvInstructionsEnd.setText(mActivity.getString(R.string.pick_end_time));
    layout.addView(tvInstructionsEnd);

    layout.addView(endTimePicker);

    return layout;
  }

  /**
   * {@inheritDoc}
   */
  public DataType getData() throws Exception {
    // Set the year, month and day arbitrarily since we don't care about them
    return new OmniTimePeriod(OmniTimePeriod.buildTimeString(1, 1, 1, startTimePicker
        .getCurrentHour(), startTimePicker.getCurrentMinute(), 0), OmniTimePeriod.buildTimeString(
        1, 1, 1, endTimePicker.getCurrentHour(), endTimePicker.getCurrentMinute(), 0));
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
    String key = ID + START_TIME;

    if (bundle.containsKey(key)) {
      Date date = OmniTimePeriod.getDate(bundle.getString(key));
      startTimePicker.setCurrentHour(date.getHours());
      startTimePicker.setCurrentMinute(date.getMinutes());
    }
    
    key = ID + END_TIME;

    if (bundle.containsKey(key)) {
      Date date = OmniTimePeriod.getDate(bundle.getString(key));
      endTimePicker.setCurrentHour(date.getHours());
      endTimePicker.setCurrentMinute(date.getMinutes());
    }
  }

  /**
   * {@inheritDoc}
   */
  public void saveState(Bundle bundle) {
    bundle.putString(ID + START_TIME, OmniTimePeriod.buildTimeString(0, 0, 0,
        startTimePicker.getCurrentHour(), startTimePicker.getCurrentMinute(), 0));
    bundle.putString(ID + END_TIME, OmniTimePeriod.buildTimeString(0, 0, 0,
        endTimePicker.getCurrentHour(), endTimePicker.getCurrentMinute(), 0));
  }
}
