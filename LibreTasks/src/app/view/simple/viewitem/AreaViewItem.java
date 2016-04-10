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
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import libretasks.app.R;
import libretasks.app.controller.datatypes.DataType;
import libretasks.app.controller.datatypes.OmniArea;
import libretasks.app.controller.util.DataTypeValidationException;
import libretasks.app.view.simple.model.ModelAttribute;

/**
 * {@link ViewItem} implementation for {@link OmniArea}
 */
public class AreaViewItem extends AbstractViewItem {
  private final static String ADDRESS = "Address";
  private final static String DISTANCE = "Distance";

  private final static int ADDRESS_VIEW_ID = 0;
  private final static int DISTANCE_VIEW_ID = 1;

  private final Activity mActivity;
  private final EditText etAddress;
  private final EditText etDistance;

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
  public AreaViewItem(int id, long dataTypeDbID, Activity activity) {
    super(id, dataTypeDbID);
    mActivity = activity;

    etAddress = new EditText(activity);
    etAddress.setId(ADDRESS_VIEW_ID);

    etDistance = new EditText(activity);
    etDistance.setId(DISTANCE_VIEW_ID);
  }

  /**
   * Create insert this object's {@link View} objects into {@code viewGroup}
   * 
   * @param initData
   *          data to be used for initializing the values in the {@link View} objects. Pass null for
   *          no data. Note that this should be an instance of {@link OmniArea}
   * @return the {@link View} object representing the underlying {@link OmniArea} object
   */
  public View buildUI(DataType initData) {
    LinearLayout layout = new LinearLayout(mActivity);
    layout.setId(ID);
    layout.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
        LayoutParams.FILL_PARENT));
    layout.setOrientation(LinearLayout.VERTICAL);

    if (initData != null) {
      OmniArea area = (OmniArea) initData;
      etAddress.setText(area.getUserInput());
      etDistance.setText(Double.toString(area.getProximityDistance()));
    }

    TextView tvAddress = new TextView(mActivity);
    tvAddress.setText(R.string.area_address);

    TextView tvDistance = new TextView(mActivity);
    tvDistance.setText(R.string.area_distance);

    layout.addView(tvAddress);
    layout.addView(etAddress);
    layout.addView(tvDistance);
    layout.addView(etDistance);

    return layout;
  }

  /**
   * {@inheritDoc}
   */
  public DataType getData() throws Exception {
    double distance = 0;

    try {
      distance = Double.parseDouble(etDistance.getText().toString());
    } catch (NumberFormatException ex) {
      throw new DataTypeValidationException(mActivity.getString(R.string.bad_distance_format));
    }

    return OmniArea.getOmniArea(mActivity, etAddress.getText().toString(), distance);
  }

  /**
   * {@inheritDoc}
   */
  public void insertAttribute(ModelAttribute attribute) {
    // TODO: (markww) figure out if OmniArea should allow copy parameter arguments.
  }

  /**
   * {@inheritDoc}
   */
  public void loadState(Bundle bundle) {
    String key = ID + ADDRESS;

    if (bundle.containsKey(key)) {
      etAddress.setText(bundle.getString(key));
    }

    key = ID + DISTANCE;

    if (bundle.containsKey(key)) {
      etDistance.setText(bundle.getString(key));
    }
  }

  /**
   * {@inheritDoc}
   */
  public void saveState(Bundle bundle) {
    bundle.putString(ID + ADDRESS, etAddress.getText().toString());
    bundle.putString(ID + DISTANCE, etDistance.getText().toString());
  }
}
