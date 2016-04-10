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
package libretasks.app.model;

import static libretasks.app.model.CursorHelper.getLongFromCursor;
import static libretasks.app.model.CursorHelper.getStringFromCursor;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import libretasks.app.controller.Action;
import libretasks.app.controller.Event;
import libretasks.app.controller.actions.CallPhoneAction;
import libretasks.app.controller.actions.SendGmailAction;
import libretasks.app.controller.actions.SendSmsAction;
import libretasks.app.controller.actions.PauseMediaAction;
import libretasks.app.controller.actions.PlayMediaAction;
import libretasks.app.controller.actions.SetPhoneLoudAction;
import libretasks.app.controller.actions.SetPhoneSilentAction;
import libretasks.app.controller.actions.SetPhoneVibrateAction;
import libretasks.app.controller.actions.SetScreenBrightnessAction;
import libretasks.app.controller.actions.ShowAlertAction;
import libretasks.app.controller.actions.ShowNotificationAction;
import libretasks.app.controller.actions.ShowWebsiteAction;
import libretasks.app.controller.actions.TurnOffWifiAction;
import libretasks.app.controller.actions.TurnOnWifiAction;
import libretasks.app.controller.actions.UpdateTwitterStatusAction;
import libretasks.app.controller.util.ExceptionMessageMap;
import libretasks.app.controller.util.Logger;
import libretasks.app.controller.util.OmnidroidException;
import libretasks.app.model.db.DbHelper;
import libretasks.app.model.db.RegisteredActionDbAdapter;
import libretasks.app.model.db.RegisteredActionParameterDbAdapter;
import libretasks.app.model.db.RegisteredAppDbAdapter;
import libretasks.app.model.db.RuleActionDbAdapter;
import libretasks.app.model.db.RuleActionParameterDbAdapter;

/**
 * This class serves as a database access layer for Omnidroid's Actions framework.
 * 
 */
public class CoreActionsDbHelper {
  private static final String TAG = CoreActionsDbHelper.class.getSimpleName();

  private DbHelper dbHelper;
  private SQLiteDatabase database;
  private RuleActionDbAdapter ruleActionDbAdpater;
  private RuleActionParameterDbAdapter ruleActionParameterDbAdapter;
  private RegisteredActionDbAdapter registeredActionDbAdapter;
  private RegisteredActionParameterDbAdapter registeredActionParameterDbAdapter;
  private RegisteredAppDbAdapter registeredAppDbAdapter;

  // Action info constants
  private final int APP_NAME = 0;
  private final int ACTION_NAME = 1;

  public CoreActionsDbHelper(Context context) {
    dbHelper = new DbHelper(context);
    database = dbHelper.getReadableDatabase();

    // Initialize db adapters
    ruleActionDbAdpater = new RuleActionDbAdapter(database);
    ruleActionParameterDbAdapter = new RuleActionParameterDbAdapter(database);
    registeredActionDbAdapter = new RegisteredActionDbAdapter(database);
    registeredActionParameterDbAdapter = new RegisteredActionParameterDbAdapter(database);
    registeredAppDbAdapter = new RegisteredAppDbAdapter(database);
  }

  /**
   * Close this database helper object. Attempting to use this object after this call will cause an
   * {@link IllegalStateException} being raised.
   */
  public void close() {
    Log.i(TAG, "closing database.");
    database.close();
    
    // Not necessary, but also close all omnidroidDbHelper databases just in case.
    dbHelper.close();
  }

  /**
   * This method initializes an Action object from a given action name and parameters
   * 
   * @param appName
   *          Name of the application
   * @param actionName
   *          Name of the action
   * @param actionParams
   *          Parameters required to initialize the action
   * @return An action object
   * @throws OmnidroidException
   *           if the given action cannot be initialized by this method
   */
  private Action getAction(String appName, String actionName, HashMap<String, String> actionParams)
      throws OmnidroidException {
    // TODO:(Rutvij) Fetch this hard coded data from database.

    if (appName.equals(SendSmsAction.APP_NAME) 
        && actionName.equals(SendSmsAction.ACTION_NAME)) {
      return new SendSmsAction(actionParams);
    } else if (appName.equals(CallPhoneAction.APP_NAME)
        && actionName.equals(CallPhoneAction.ACTION_NAME)) {
      return new CallPhoneAction(actionParams);
    } else if (appName.equals(SendGmailAction.APP_NAME)
        && actionName.equals(SendGmailAction.ACTION_NAME)) {
      return new SendGmailAction(actionParams);
    } else if (appName.equals(ShowAlertAction.APP_NAME)
        && actionName.equals(ShowAlertAction.ACTION_NAME)) {
      return new ShowAlertAction(actionParams);
    } else if (appName.equals(ShowNotificationAction.APP_NAME)
        && actionName.equals(ShowNotificationAction.ACTION_NAME)) {
      return new ShowNotificationAction(actionParams);
    } else if (appName.equals(ShowWebsiteAction.APP_NAME)
        && actionName.equals(ShowWebsiteAction.ACTION_NAME)) {
      return new ShowWebsiteAction(actionParams);
    } else if (appName.equals(SetScreenBrightnessAction.APP_NAME)
        && actionName.equals(SetScreenBrightnessAction.ACTION_NAME)) {
      return new SetScreenBrightnessAction(actionParams);
    } else if (appName.equals(PauseMediaAction.APP_NAME)
        && actionName.equals(PauseMediaAction.ACTION_NAME)) {
      return new PauseMediaAction(actionParams);
    } else if (appName.equals(PlayMediaAction.APP_NAME)
        && actionName.equals(PlayMediaAction.ACTION_NAME)) {
      return new PlayMediaAction(actionParams);
    } else if (appName.equals(SetPhoneLoudAction.APP_NAME)
        && actionName.equals(SetPhoneLoudAction.ACTION_NAME)) {
      return new SetPhoneLoudAction(actionParams);
    } else if (appName.equals(SetPhoneSilentAction.APP_NAME)
        && actionName.equals(SetPhoneSilentAction.ACTION_NAME)) {
      return new SetPhoneSilentAction(actionParams);
    } else if (appName.equals(SetPhoneVibrateAction.APP_NAME)
        && actionName.equals(SetPhoneVibrateAction.ACTION_NAME)) {
      return new SetPhoneVibrateAction(actionParams);
    } else if (appName.equals(TurnOffWifiAction.APP_NAME)
        && actionName.equals(TurnOffWifiAction.ACTION_NAME)) {
      return new TurnOffWifiAction(actionParams);
    } else if (appName.equals(TurnOnWifiAction.APP_NAME)
        && actionName.equals(TurnOnWifiAction.ACTION_NAME)) {
      return new TurnOnWifiAction(actionParams);
    } else if (appName.equals(UpdateTwitterStatusAction.APP_NAME)
        && actionName.equals(UpdateTwitterStatusAction.ACTION_NAME)) {
      return new UpdateTwitterStatusAction(actionParams);
    } else {
    	Log.d(TAG, "doesn't catch AppName is: " + appName + " and actionName is: " + actionName);
      throw new OmnidroidException(120003, ExceptionMessageMap.getMessage(new Integer(120003)
          .toString()));
    }
  }

  /**
   * This method checks the parameter data to see if it already has value or it should extract value
   * from the event. The paramData should contain valid tags like "<Phone Ring Time>" in order to 
   * retrieve related information.
   * 
   * @param paramData
   *          The parameter data
   * @param event
   *          The event whose attributes can be used as parameter data
   * @return The parameter data with actual value that can be used in intent
   * @throws IllegalStateException
   *           when this object is already closed
   */
  public String fillParamWithEventAttrib(String paramData, Event event) {
    if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }
    
    StringBuilder retVal = new StringBuilder();
    
    //the index of current cursor
    int cursor = 0;
    while (cursor < paramData.length()) {
      int openBracketIdx = paramData.indexOf('<', cursor);
      int closeBracketIdx = paramData.indexOf('>', cursor);
      
      //if no valid brancket pair found, append whatever left, and return the string.
      if (openBracketIdx == -1 || closeBracketIdx == -1 || openBracketIdx + 1 >= closeBracketIdx) {
        retVal.append(paramData.substring(cursor));
        break;
      }
      
      //else if the pair is found, substitute "<attr>" with the actual attribute and append that to
      //the retVal
      String attr = paramData.substring(openBracketIdx + 1, closeBracketIdx);
      String param = paramData.substring(openBracketIdx, closeBracketIdx + 1);
      String paramAttr;
      try {
        paramAttr = event.getAttribute(attr);
      } catch (IllegalArgumentException e) {
        paramAttr = param;
      }
      retVal.append(paramData.substring(cursor, openBracketIdx));
      retVal.append(paramAttr);
      
      //update cursor
      cursor = closeBracketIdx + 1;
    }
    Log.d("fillParamWithEventAttrib", paramData + " -> " + retVal.toString());
    return retVal.toString();
  }
  
  /**
   * This method returns an ArrayList of action ids which are to be executed for a given rule
   * 
   * @param ruleId
   *          The rule Id
   * @return ArrayList of Action Ids for actions to be executed
   * @throws IllegalStateException
   *           when this object is already closed
   */
  private ArrayList<Long> getRuleActionIds(Long ruleId) {
    if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }

    Cursor cursor = ruleActionDbAdpater.fetchAll(ruleId, null);
    ArrayList<Long> ruleActionIds = new ArrayList<Long>();
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      ruleActionIds.add(getLongFromCursor(cursor, RuleActionDbAdapter.KEY_RULEACTIONID));
    }
    cursor.close();
    return ruleActionIds;
  }

  /**
   * This method gives the registered action name and its application name for a rule action
   * 
   * @param ruleActionId
   *          The Rule Action's Id
   * @return String array {application name, action name}. Null if cannot find action name,
   *         application name or action id
   * @throws IllegalStateException
   *           when this object is already closed
   */
  private String[] getRegisteredActionInfo(Long ruleActionId) {
    if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }

    Long actionId;
    Long appId;
    String actionName;
    String appName;

    Cursor cursor = ruleActionDbAdpater.fetch(ruleActionId);
    if (cursor.moveToFirst()) {
      actionId = getLongFromCursor(cursor, RuleActionDbAdapter.KEY_ACTIONID);
      cursor.close();
    } else {
      cursor.close();
      return null;
    }

    cursor = registeredActionDbAdapter.fetch(actionId);
    if (cursor.moveToFirst()) {
      actionName = getStringFromCursor(cursor, RegisteredActionDbAdapter.KEY_ACTIONNAME);
      appId = getLongFromCursor(cursor, RegisteredActionDbAdapter.KEY_APPID);
      cursor.close();
    } else {
      cursor.close();
      return null;
    }

    cursor = registeredAppDbAdapter.fetch(appId);
    if (cursor.moveToFirst()) {
      appName = getStringFromCursor(cursor, RegisteredAppDbAdapter.KEY_APPNAME);
      cursor.close();
    } else {
      cursor.close();
      return null;
    }

    return new String[] { appName, actionName };
  }

  /**
   * This method gets Parameter Id, Parameter Data, and Registered Parameter Name for all parameters
   * of a rule action from the database and adds them to two the two maps paramsData and
   * paramsRegisteredParamId.
   * 
   * @param ruleActionId
   *          Rule Action's Id
   * @param event
   *          Event from which data is extracted
   * @param paramsData
   *          An empty map of Rule action parameter Id to Rule action parameter Data
   * @param paramsRegisteredParamId
   *          An empty map of Action parameter Id to Registered action parameter Id
   * @throws IllegalStateException
   *           when this object is already closed
   */
  private void addDataAndRegisteredParamId(Long ruleActionId, Event event,
      HashMap<Long, String> paramsData, HashMap<Long, Long> paramsRegisteredParamId) {
    if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }

    Long paramId;
    String paramData;
    Long paramRegisteredParamId;
    Cursor cursor = ruleActionParameterDbAdapter.fetchAll(ruleActionId, null, null);
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      paramId = getLongFromCursor(cursor, RuleActionParameterDbAdapter.KEY_RULEACTIONPARAMETERID);
      paramData = getStringFromCursor(cursor,
          RuleActionParameterDbAdapter.KEY_RULEACTIONPARAMETERDATA);
      paramRegisteredParamId = getLongFromCursor(cursor,
          RuleActionParameterDbAdapter.KEY_ACTIONPARAMETERID);
      paramsData.put(paramId, fillParamWithEventAttrib(paramData, event));
      paramsRegisteredParamId.put(paramId, paramRegisteredParamId);
    }
    cursor.close();
  }

  /**
   * This method gets a map of all Registered Action Parameter ids and names and returns a map of
   * Ids to Names for lookup.
   * 
   * @return HashMap of Registered action parameter Ids to Registered param names for all registered
   *         action parameters
   * @throws IllegalStateException
   *           when this object is already closed
   */
  private HashMap<Long, String> getRegisteredActionParamNames() {
    if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }

    Long registeredParamId;
    String registeredParamName;
    HashMap<Long, String> registeredParamNames = new HashMap<Long, String>();
    Cursor cursor = registeredActionParameterDbAdapter.fetchAll();
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      registeredParamId = getLongFromCursor(cursor,
          RegisteredActionParameterDbAdapter.KEY_ACTIONPARAMETERID);
      registeredParamName = getStringFromCursor(cursor,
          RegisteredActionParameterDbAdapter.KEY_ACTIONPARAMETERNAME);
      registeredParamNames.put(registeredParamId, registeredParamName);
    }
    cursor.close();
    return registeredParamNames;
  }

  /**
   * This method gives an ArrayList of actions to be executed for a given rule. Populates the action
   * parameter fields, which may require retrieving them from the event
   * 
   * @param ruleId
   *          Id of the rule
   * @param event
   *          Event that triggered this rule
   * @return ArrayList of actions to be executed
   * @throws IllegalArgumentException
   *           if the rule is not enabled or not found in database OR if ActionId or ActionName is
   *           not found in database
   * @throws IllegalStateException
   *           when this object is already closed
   */
  public ArrayList<Action> getActions(long ruleId, String ruleName, Event event) {
    if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }

    ArrayList<Action> actions = new ArrayList<Action>();

    // Get list of Rule Action Ids from database
    ArrayList<Long> ruleActionIds = getRuleActionIds(ruleId);

    // Create an action object for each rule action
    Action action;
    String[] actionInfo;
    String actionName;
    String appName;
    HashMap<Long, String> paramsData; // <ruleActionParamId, ruleActionParamData>
    HashMap<Long, Long> paramsRegisteredParamId; // <ruleActionParamId, registeredActionParamId>
    for (Long ruleActionId : ruleActionIds) {
      // get action info
      actionInfo = getRegisteredActionInfo(ruleActionId);
      if (actionInfo == null) {
        throw new IllegalArgumentException(
            "Cannot find ActionId, ApplicationName or ActionName for: " + ruleActionId);
      }
      appName = actionInfo[APP_NAME];
      actionName = actionInfo[ACTION_NAME];

      // get parameter ids and data for the current action
      paramsData = new HashMap<Long, String>();
      paramsRegisteredParamId = new HashMap<Long, Long>();
      addDataAndRegisteredParamId(ruleActionId, event, paramsData, paramsRegisteredParamId);

      // Get registered action parameter names
      // <registeredActionParamId, registeredActionParamName>
      HashMap<Long, String> registeredParamNames = getRegisteredActionParamNames();

      // Create a map of parameter name to parameter data
      // <registeredActionParamName, ruleActionParamData>
      HashMap<String, String> actionParams = new HashMap<String, String>();
      for (Long parameterId : paramsData.keySet()) {
        actionParams.put(registeredParamNames.get(paramsRegisteredParamId.get(parameterId)),
            paramsData.get(parameterId));
      }

      // create action using action parameters, action name and application name
      try {
        action = getAction(appName, actionName, actionParams);
        action.setRuleName(ruleName);
        action.setDatabaseId(ruleActionId);
        action.setActionType(Action.RULE_ACTION);
        actions.add(action); // add action to actions ArrayList
      } catch (OmnidroidException e) {
        Logger.w(TAG, e.toString(), e);
        Logger.w(TAG, e.getLocalizedMessage());
        Logger.w(TAG, "Action " + actionName + " cannot be initialized");
      }
    }
    return actions;
  }
}
