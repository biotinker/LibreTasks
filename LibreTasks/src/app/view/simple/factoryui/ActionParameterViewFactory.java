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
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;
import libretasks.app.R;
import libretasks.app.controller.actions.SendGmailAction;
import libretasks.app.controller.actions.SendSmsAction;
import libretasks.app.controller.actions.ShowWebsiteAction;
import libretasks.app.controller.datatypes.DataType;
import libretasks.app.controller.datatypes.OmniPasswordInput;
import libretasks.app.controller.datatypes.OmniText;
import libretasks.app.controller.util.DataTypeValidationException;
import libretasks.app.view.simple.model.ModelAction;
import libretasks.app.view.simple.model.ModelApplication;
import libretasks.app.view.simple.model.ModelParameter;
import libretasks.app.view.simple.model.ModelRuleAction;
import libretasks.app.view.simple.viewitem.ViewItem;
import libretasks.app.view.simple.viewitem.ViewItemFactory;
import libretasks.app.view.simple.viewitem.ViewItemGroup;

import java.util.ArrayList;

/**
 * Static factory class for setting up a dynamic UI for every action type.
 */
public class ActionParameterViewFactory {
  private static class LoginViewID {
    public static final int USERNAME = 0;
    public static final int PASSWORD = 1;
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
    
    if (initData == null) {
      initData = generateInitialData(activity.getApplicationContext(), modelAction);
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

    ViewItemFactory viewItemFactory = ViewItemFactory.instance();
    ViewItem usernameItem = viewItemFactory.create(LoginViewID.USERNAME,
        viewItemFactory.TEXT_DATATYPE_DB_ID, activity);
    viewItems.addViewItem(usernameItem, new OmniText(modelApp.getUsername()));

    TextView passwordText = new TextView(activity);
    passwordText.setText(R.string.password);
    viewItems.addView(passwordText);

    ViewItem passwordItem = viewItemFactory.create(LoginViewID.PASSWORD,
        viewItemFactory.PASSWORD_INPUT_DATATYPE_DB_ID, activity);
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
  
  /** generates initial data from signatures. */
  private static ArrayList<DataType> generateInitialData(Context context, ModelAction modelAction) {
    SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context);
    ArrayList<DataType> initData = null;
    if (modelAction.getTypeName().equals(SendSmsAction.ACTION_NAME) && sharedPreferences
        .getBoolean(context.getString(R.string.pref_key_sms_signature), true)) {
      initData = new ArrayList<DataType> ();
      initData.add(null); //phone number
      initData.add(new OmniText (sharedPreferences.
          getString(context.getString(R.string.pref_key_sms_signature_settings), "")));
    } else if (modelAction.getTypeName().equals(SendGmailAction.ACTION_NAME) 
        && sharedPreferences.getBoolean(context.
        getString(R.string.pref_key_gmail_signature), true)) {
      initData = new ArrayList<DataType> ();
      initData.add(null); // user account
      initData.add(null); //email To
      initData.add(null); //subject
      initData.add(new OmniText (sharedPreferences.
          getString(context.getString(R.string.pref_key_gmail_signature_settings), "")));
    } else if (modelAction.getTypeName().equals(ShowWebsiteAction.ACTION_NAME) ) {
      initData = new ArrayList <DataType> ();
      initData.add(new OmniText("http://"));
    }
    return initData;
  }
}
