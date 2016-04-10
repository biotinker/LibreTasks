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
 * Copyright 2009, 2010 Omnidroid - http://code.google.com/p/omnidroid 
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
package libretasks.app.view.simple.factoryui;

import android.app.Activity;
import android.widget.TextView;
import libretasks.app.R;
import libretasks.app.controller.datatypes.DataType;
import libretasks.app.controller.datatypes.OmniArea;
import libretasks.app.controller.datatypes.OmniDate;
import libretasks.app.controller.datatypes.OmniPhoneNumber;
import libretasks.app.controller.datatypes.OmniText;
import libretasks.app.controller.datatypes.OmniTimePeriod;
import libretasks.app.view.simple.UIDbHelperStore;
import libretasks.app.view.simple.model.ModelFilter;
import libretasks.app.view.simple.model.ModelRuleFilter;
import libretasks.app.view.simple.viewitem.ViewItemFactory;
import libretasks.app.view.simple.viewitem.ViewItemGroup;

/**
 * Static factory class for setting up a dynamic UI for every filter type.
 */
public class RuleFilterViewFactory {

  private RuleFilterViewFactory() {
  }

  /**
   * Build a UI based on a given filter. Eventually this should be changed to use some information
   * from the database about filter attribute types to generate the required UI elements, instead of
   * a large hard-coded if statement.
   * 
   * @param modelFilter
   *          The model filter we want to try to fill.
   * @param initData
   *          collection of data for pre-populating the UI
   * @param activity
   *          the activity where the UI is going to be built on
   * @return {@link ViewItemGroup} instance that contains the underlying UI elements
   */
  public static ViewItemGroup buildUIForFilter(final ModelFilter modelFilter,
      final DataType initData, Activity activity) {

    ViewItemGroup viewItemGroup = new ViewItemGroup(activity);
    ViewItemFactory viewItemFactory = ViewItemFactory.instance();
    int uiID = 0;
    long filterDbID = modelFilter.getDatabaseId();

    if (filterDbID == AllFilterID.PHONENUMBER_EQUALS) {
      TextView tvInstructions = new TextView(activity);
      tvInstructions.setText(R.string.phone_num_eq_filter_inst);
      viewItemGroup.addView(tvInstructions);

      viewItemGroup.addViewItem(viewItemFactory.create(uiID,
          viewItemFactory.PHONE_NUMBER_DATATYPE_DB_ID, activity), initData);
    } else if (filterDbID == AllFilterID.PHONENUMBER_NOT_EQUALS) {
        TextView tvInstructions = new TextView(activity);
        tvInstructions.setText(R.string.phone_num_not_eq_filter_inst);
        viewItemGroup.addView(tvInstructions);

        viewItemGroup.addViewItem(viewItemFactory.create(uiID,
            viewItemFactory.PHONE_NUMBER_DATATYPE_DB_ID, activity), initData);
    } else if (filterDbID == AllFilterID.TEXT_EQUALS) {
      TextView tvInstructions = new TextView(activity);
      tvInstructions.setText(R.string.text_eq_filter_inst);
      viewItemGroup.addView(tvInstructions);

      viewItemGroup.addViewItem(viewItemFactory.create(uiID, viewItemFactory.TEXT_DATATYPE_DB_ID,
          activity), initData);
    } else if (filterDbID == AllFilterID.TEXT_CONTAINS) {
      TextView tvInstructions = new TextView(activity);
      tvInstructions.setText(R.string.text_cont_filter_inst);
      viewItemGroup.addView(tvInstructions);

      viewItemGroup.addViewItem(viewItemFactory.create(uiID, viewItemFactory.TEXT_DATATYPE_DB_ID,
          activity), initData);
    } else if (filterDbID == AllFilterID.AREA_AWAY || filterDbID == AllFilterID.AREA_NEAR) {
      viewItemGroup.addViewItem(viewItemFactory.create(uiID, viewItemFactory.AREA_DATATYPE_DB_ID,
          activity), initData);
    } else if (filterDbID == AllFilterID.DATE_BEFORE_EVERYDAY
        || filterDbID == AllFilterID.DATE_IS_EVERYDAY
        || filterDbID == AllFilterID.DATE_IS_NOT_EVERYDAY
        || filterDbID == AllFilterID.DATE_AFTER_EVERYDAY) {
      viewItemGroup.addViewItem(viewItemFactory.create(uiID, viewItemFactory.DATE_DATATYPE_DB_ID,
          activity), initData);
    } else if (filterDbID == AllFilterID.DATE_DURING_EVERYDAY
        || filterDbID == AllFilterID.DATE_EXCEPT_EVERYDAY
        // The filters below are not currently used?
        || filterDbID == AllFilterID.TIMEPERIOD_DURING_EVERYDAY
        || filterDbID == AllFilterID.TIMEPERIOD_EXCEPT_EVERYDAY) {
      viewItemGroup.addViewItem(viewItemFactory.create(uiID,
          viewItemFactory.TIME_PERIOD_DATATYPE_DB_ID, activity), initData);
    } else {
      throw new IllegalArgumentException("Unknown filter ID: " + filterDbID);
    }

    return viewItemGroup;
  }

  /**
   * Construct a {@link ModelRuleFilter} with data extracted from the UI
   * 
   * @param modelFilter
   *          the filter on which the new rule will be based on
   * @param viewItems
   *          the source of data where the data will be extracted. This method assumes that only the
   *          first element in the {@code viewItems} contains the required data.
   * @return a {@link ModelRuleFilter} object
   * @throws Exception
   *           if the data is invalid.
   */
  public static ModelRuleFilter buildFilterFromUI(ModelFilter modelFilter, ViewItemGroup viewItems)
      throws Exception {
    return new ModelRuleFilter(-1, modelFilter, viewItems.get(0).getData());
  }

  /**
   * static class to load all filter id for later use.
   */
  private static class AllFilterID {
    public static final long PHONENUMBER_EQUALS = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniPhoneNumber.DB_NAME, OmniPhoneNumber.Filter.EQUALS.toString());
    public static final long PHONENUMBER_NOT_EQUALS = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniPhoneNumber.DB_NAME, OmniPhoneNumber.Filter.NOTEQUALS.toString());     
    public static final long TEXT_EQUALS = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniText.DB_NAME, OmniText.Filter.EQUALS.toString());
    public static final long TEXT_CONTAINS = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniText.DB_NAME, OmniText.Filter.CONTAINS.toString());
    public static final long AREA_AWAY = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniArea.DB_NAME, OmniArea.Filter.AWAY.toString());
    public static final long AREA_NEAR = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniArea.DB_NAME, OmniArea.Filter.NEAR.toString());
    public static final long DATE_IS_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniDate.DB_NAME, OmniDate.Filter.IS_EVERYDAY.toString());
    public static final long DATE_IS_NOT_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniDate.DB_NAME, OmniDate.Filter.IS_NOT_EVERYDAY.toString());
    public static final long DATE_BEFORE = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniDate.DB_NAME, OmniDate.Filter.BEFORE.toString());
    public static final long DATE_AFTER = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniDate.DB_NAME, OmniDate.Filter.AFTER.toString());
    public static final long DATE_AFTER_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniDate.DB_NAME, OmniDate.Filter.AFTER_EVERYDAY.toString());
    public static final long DATE_BEFORE_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniDate.DB_NAME, OmniDate.Filter.BEFORE_EVERYDAY.toString());
    public static final long DATE_DURING = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniDate.DB_NAME, OmniTimePeriod.DB_NAME,
            OmniDate.Filter.DURING.toString());
    public static final long DATE_DURING_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniDate.DB_NAME, OmniTimePeriod.DB_NAME,
            OmniDate.Filter.DURING_EVERYDAY.toString());
    public static final long DATE_EXCEPT = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniDate.DB_NAME, OmniTimePeriod.DB_NAME,
            OmniDate.Filter.EXCEPT.toString());
    public static final long DATE_EXCEPT_EVERYDAY = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniDate.DB_NAME, OmniTimePeriod.DB_NAME,
            OmniDate.Filter.EXCEPT_EVERYDAY.toString());
    public static final long TIMEPERIOD_DURING = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniTimePeriod.DB_NAME, OmniDate.DB_NAME,
            OmniTimePeriod.Filter.DURING.toString());
    public static final long TIMEPERIOD_DURING_EVERYDAY = UIDbHelperStore.instance()
        .getFilterLookup().getDataFilterID(OmniTimePeriod.DB_NAME, OmniDate.DB_NAME,
            OmniTimePeriod.Filter.DURING_EVERYDAY.toString());
    public static final long TIMEPERIOD_EXCEPT = UIDbHelperStore.instance().getFilterLookup()
        .getDataFilterID(OmniTimePeriod.DB_NAME, OmniDate.DB_NAME,
            OmniTimePeriod.Filter.EXCEPT.toString());
    public static final long TIMEPERIOD_EXCEPT_EVERYDAY = UIDbHelperStore.instance()
        .getFilterLookup().getDataFilterID(OmniTimePeriod.DB_NAME, OmniDate.DB_NAME,
            OmniTimePeriod.Filter.EXCEPT_EVERYDAY.toString());
  }
}
