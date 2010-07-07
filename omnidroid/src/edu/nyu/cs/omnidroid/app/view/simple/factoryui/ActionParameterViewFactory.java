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
package edu.nyu.cs.omnidroid.app.view.simple.factoryui;

import android.app.Activity;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.datatypes.DataType;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniPasswordInput;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniText;
import edu.nyu.cs.omnidroid.app.controller.util.DataTypeValidationException;
import edu.nyu.cs.omnidroid.app.model.DataTypeIDLookup;
import edu.nyu.cs.omnidroid.app.view.simple.UIDbHelperStore;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelApplication;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelParameter;
import edu.nyu.cs.omnidroid.app.view.simple.model.ModelRuleAction;
import edu.nyu.cs.omnidroid.app.view.simple.viewitem.ViewItem;
import edu.nyu.cs.omnidroid.app.view.simple.viewitem.ViewItemFactory;
import edu.nyu.cs.omnidroid.app.view.simple.viewitem.ViewItemGroup;

import java.util.ArrayList;

/**
 * Static factory class for setting up a dynamic UI for every action type.
 */
public class ActionParameterViewFactory {
  private static final long TEXT_DATATYPE_DB_ID;
  private static final long PASSWORD_INPUT_DATATYPE_DB_ID;

  private static class LoginViewID {
    public static final int USERNAME = 0;
    public static final int PASSWORD = 1;
  }

  static {
    DataTypeIDLookup lookup = UIDbHelperStore.instance().getDatatypeLookup();
    TEXT_DATATYPE_DB_ID = lookup.getDataTypeID(OmniText.DB_NAME);
    PASSWORD_INPUT_DATATYPE_DB_ID = lookup.getDataTypeID(OmniPasswordInput.DB_NAME);
  }

  private ActionParameterViewFactory() {
  }

  /**
   * Build a UI based on the parameters and datatypes of a given action.
   * 
   * @param modelAction
   *          the action
   * @param initData
   *          collection of data for pre-populating the UI
   * @param activity
   *          the activity where the UI is going to be built on
   * @return {@link ViewItemGroup} instance that contains the underlying UI elements
   */
  public static ViewItemGroup buildUIFromAction(ModelAction modelAction,
      ArrayList<DataType> initData, Activity activity) {
    ArrayList<ModelParameter> parameters = modelAction.getParameters();

    if (initData != null && initData.size() != parameters.size()) {
      throw new IllegalArgumentException(
          "Action parameter data array does not match parameter size!");
    }

    ViewItemGroup viewItemGroup = new ViewItemGroup(activity);

    int numParameters = parameters.size();
    for (int i = 0; i < numParameters; i++) {
      ModelParameter parameter = parameters.get(i);

      // Always add a TextView showing the parameter name.
      TextView tv = new TextView(activity);
      tv.setText(parameter.getTypeName() + ":");

      viewItemGroup.addView(tv);

      ViewItem viewItem = ViewItemFactory.instance().create(i, parameter.getDatatype(), activity);

      // Then append the UI elements required for the type.
      viewItemGroup.addViewItem(viewItem, initData != null ? initData.get(i) : null);
    }

    return viewItemGroup;
  }

  /**
   * Build a standard login UI with Username and Password fields
   * 
   * @param modelApp
   *          Application object used to pre-populate the UI
   * @param activity
   *          The activity where the UI will be built in
   * @return {@link ViewItemGroup} instance that contains the underlying UI elements
   */
  public static ViewItemGroup buildLoginUI(ModelApplication modelApp, Activity activity) {
    ViewItemGroup viewItems = new ViewItemGroup(activity);

    TextView usernameText = new TextView(activity);
    usernameText.setText(R.string.username);
    viewItems.addView(usernameText);

    ViewItem usernameItem = ViewItemFactory.instance().create(LoginViewID.USERNAME,
        TEXT_DATATYPE_DB_ID, activity);
    viewItems.addViewItem(usernameItem, new OmniText(modelApp.getUsername()));

    TextView passwordText = new TextView(activity);
    passwordText.setText(R.string.password);
    viewItems.addView(passwordText);

    ViewItem passwordItem = ViewItemFactory.instance().create(LoginViewID.PASSWORD,
        PASSWORD_INPUT_DATATYPE_DB_ID, activity);
    viewItems.addViewItem(passwordItem, new OmniPasswordInput(modelApp.getPassword()));

    return viewItems;
  }

  /**
   * Extract all user supplied data from {@code viewItems} to {@code modelApplication}
   * 
   * @param modelApplication
   *          an existing {@link ModelApplication} instance
   * @param viewItems
   *          {@link ViewItemGroup} instance that contains UI controls for the username and
   *          password. Note that this instance should be constructed from the {@link buildLoginUI}
   *          method.
   * 
   * @return the newly modified {@code modelApplication}
   * @throws DataTypeValidationException
   *           If the username or password fields are empty
   */
  public static ModelApplication buildApplicationFromLoginUI(ModelApplication modelApplication,
      ViewItemGroup viewItems) throws Exception {
    modelApplication.setUsername(viewItems.get(LoginViewID.USERNAME).getData().getValue());
    modelApplication.setPassword(viewItems.get(LoginViewID.PASSWORD).getData().getValue());

    if (modelApplication.getUsername().length() == 0) {
      throw new DataTypeValidationException("Please enter a username");
    } else if (modelApplication.getPassword().length() == 0) {
      throw new DataTypeValidationException("Please enter a password");
    }

    return modelApplication;
  }

  /**
   * Construct a {@link ModelRuleAction} with data extracted from the UI
   * 
   * @param modelAction
   *          the action on which the new rule will be based on
   * @param viewItems
   *          the source of data where the data will be extracted
   * 
   * @return a {@link ModelRuleAction} object
   * @throws Exception
   *           if the data is invalid.
   */
  public static ModelRuleAction buildActionFromUI(ModelAction modelAction, ViewItemGroup viewItems)
      throws Exception {
    ArrayList<DataType> datas = new ArrayList<DataType>();

    for (ViewItem item : viewItems.getItems()) {
      datas.add(item.getData());
    }

    return new ModelRuleAction(-1, modelAction, datas);
  }
}