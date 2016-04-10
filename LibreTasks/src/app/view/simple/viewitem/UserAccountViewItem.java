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

import libretasks.app.R;
import libretasks.app.controller.datatypes.DataType;
import libretasks.app.controller.datatypes.OmniUserAccount;
import libretasks.app.view.simple.ActivityDlgApplicationLoginInput;
import libretasks.app.view.simple.RuleBuilder;
import libretasks.app.view.simple.model.ModelAttribute;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * {@link ViewItem} implementation for {@link OmniUserAccount}
 */
public class UserAccountViewItem extends AbstractViewItem {
  // Request code for opening the create account screen
  private static final int SETUP_ACCOUNT_REQUEST = 0;

  private final EditText userAcountText;
  private final Button setupAccountButton;
  private final Activity mActivity;

  /**
   * Class Constructor.
   * 
   * @param id
   *          the id used to uniquely identify this object.
   * @param dataTypeDbID
   *          the database id for {@link OmniUserAccount}
   * @param activity
   *          the activity where this view item is to be built on
   */
  public UserAccountViewItem(int id, long dataTypeDbID, Activity activity) {
    super(id, dataTypeDbID);
    mActivity = activity;

    userAcountText = new EditText(activity);
    userAcountText.setEnabled(false);

    setupAccountButton = new Button(activity);
    setupAccountButton.setText(activity.getResources().getString(R.string.setup_account));
    setupAccountButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(mActivity, ActivityDlgApplicationLoginInput.class);
        intent.putExtra(ViewItemGroup.INTENT_EXTRA_SOURCE_ID, ID);
        mActivity.startActivityForResult(intent, SETUP_ACCOUNT_REQUEST);
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public void loadState(Bundle bundle) {
    setUserName();
  }

  /**
   * {@inheritDoc}
   */
  public View buildUI(DataType initData) {
    setUserName();

    LinearLayout layout = new LinearLayout(mActivity);
    layout.setId(ID);
    layout.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
        LayoutParams.FILL_PARENT));
    layout.setOrientation(LinearLayout.VERTICAL);

    layout.addView(userAcountText);
    layout.addView(setupAccountButton);

    return layout;
  }

  /**
   * {@inheritDoc}
   */
  public DataType getData() throws Exception {
    /**
     * Return a dummy {@link OmniUserAccount} since there is only one user account per application
     * (as of the moment) and the chosen user is therefore only dependent on the currently chosen
     * application.
     */
    return new OmniUserAccount("");
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
  public void saveState(Bundle bundle) {
    // Doesn't need to do anything for now since there is only a single account.
  }

  /**
   * Sets the name of the currently selected user on the text field.
   */
  private void setUserName() {
    userAcountText.setText(RuleBuilder.instance().getChosenApplication().getUsername());
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
    case SETUP_ACCOUNT_REQUEST:
      switch (resultCode) {
      case Activity.RESULT_OK:
        /*
         * Since we only have a single user per account, we need to refresh the field with the
         * latest account created by the user. Pass null to {@link loadState} since the bundle is
         * not being used as of now (Because there is only one account, there is no ambiguity which
         * is the current account).
         */
        loadState(null);
        break;
      }

      break;
    }
  }
}
