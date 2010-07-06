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

import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.datatypes.DataType;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniUserAccount;
import edu.nyu.cs.omnidroid.app.view.simple.ActivityDlgActionInput;
import edu.nyu.cs.omnidroid.app.view.simple.ActivityDlgApplicationLoginInput;
import edu.nyu.cs.omnidroid.app.view.simple.RuleBuilder;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAttribute;
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
        mActivity.startActivityForResult(intent, ActivityDlgActionInput.SETUP_ACCOUNT_REQUEST);
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
}
