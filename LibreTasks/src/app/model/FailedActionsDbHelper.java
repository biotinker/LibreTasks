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

import static libretasks.app.model.CursorHelper.*;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import libretasks.app.R;
import libretasks.app.controller.Action;
import libretasks.app.controller.actions.CallPhoneAction;
import libretasks.app.controller.actions.SendGmailAction;
import libretasks.app.controller.actions.SendSmsAction;
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
import libretasks.app.model.db.FailedActionParameterDbAdapter;
import libretasks.app.model.db.FailedActionsDbAdapter;
import libretasks.app.model.db.RegisteredActionDbAdapter;
import libretasks.app.model.db.RegisteredAppDbAdapter;
import libretasks.app.model.db.RuleActionDbAdapter;
import libretasks.app.model.db.RuleDbAdapter;
import libretasks.app.view.simple.UtilUI;

/**
 * This class serves as a database access layer for Failed Actions framework.
 * 
 */
public class FailedActionsDbHelper {
  private static final String TAG = FailedActionsDbHelper.class.getSimpleName();

  private DbHelper dbHelper;
  private SQLiteDatabase database;
  private FailedActionsDbAdapter failedActionsDbAdapter;
  private FailedActionParameterDbAdapter failedActionParameterDbAdapter;
  private RegisteredActionDbAdapter registeredActionDbAdapter;
  private RegisteredAppDbAdapter registeredAppDbAdapter;
  private RuleDbAdapter ruleDbAdapter;
  private RuleActionDbAdapter ruleActionDbAdapter;
  
  private Context context;
  
  // Action info constants
  private final int KEY_APP_NAME = 0;
  private final int KEY_ACTION_NAME = 1;

  public FailedActionsDbHelper(Context context) {
    this.context = context;
    dbHelper = new DbHelper(context);
    database = dbHelper.getWritableDatabase();
    failedActionsDbAdapter = new FailedActionsDbAdapter(database);
    failedActionParameterDbAdapter = new FailedActionParameterDbAdapter(database);
    registeredActionDbAdapter = new RegisteredActionDbAdapter(database);
    registeredAppDbAdapter = new RegisteredAppDbAdapter(database);
    ruleDbAdapter = new RuleDbAdapter(database);
    ruleActionDbAdapter = new RuleActionDbAdapter(database);
  }

  /**
   * Close this database helper object. Attempting to use this object after this call will cause an
   * {@link IllegalStateException} being raised.
   */
  public void close() {
    Log.i(TAG, "closing database.");
    database.close();
    dbHelper.close();
  }

  /**
   * This method gives an ArrayList of actions to be executed for a given rule. Populates the action
   * parameter fields, which may require retrieving them from the event
   * 
   * @param failureType
   *          type of failure, use ResulProcessor RESULT_FAIULRE_types;
   * @return ArrayList of queued actions, matching failureType 
   * 
   * @throws IllegalStateException
   *           when this object is already closed
   */
  public ArrayList<Action> getActions(int failureType) {
    if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }

    ArrayList<Action> actions = new ArrayList<Action>();
    
    Action action;
    String[] actionInfo;
    HashMap<String, String> actionParams;
    for (Long failedActionId : getFailedActionIds(failureType)) {
      actionInfo = getRegisteredActionInfo(failedActionId);
      if (actionInfo == null) {
        throw new IllegalArgumentException(
            "Cannot find ActionId, ApplicationName or ActionName for: " + failedActionId);
      }
      
      actionParams = getParameters(failedActionId);
      
      Cursor cursor = failedActionsDbAdapter.fetch(failedActionId);
      cursor = ruleDbAdapter.fetch(getLongFromCursor(cursor, FailedActionsDbAdapter.KEY_RULEID));
      try {
        action = getAction(actionInfo[KEY_APP_NAME], actionInfo[KEY_ACTION_NAME], actionParams);
        action.setRuleName(getStringFromCursor(cursor, RuleDbAdapter.KEY_RULENAME));
        action.setNotification(getBooleanFromCursor(cursor, RuleDbAdapter.KEY_NOTIFICATION));
        action.setDatabaseId(failedActionId);
        action.setActionType(Action.FAILED_ACTION);
        actions.add(action); 
      } catch (OmnidroidException e) {
        Logger.w(TAG, e.toString(), e);
        Logger.w(TAG, e.getLocalizedMessage());
      }
      cursor.close();
    }
    
    return actions;
  }
  
  private HashMap<String, String> getParameters (Long failedActionId) {
    
    HashMap<String, String> parameters = new HashMap<String, String>();
    Cursor cursor = failedActionParameterDbAdapter.fetchAll(failedActionId, null, null); 
    while (cursor.moveToNext()) {
      parameters.put(getStringFromCursor(cursor, FailedActionParameterDbAdapter.KEY_ACTIONPARAMETERNAME),
          getStringFromCursor(cursor, FailedActionParameterDbAdapter.KEY_FAILEDACTIONPARAMETERDATA));
    }
    cursor.close();
    return parameters;
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
   * This method returns an ArrayList of action ids which are to be executed for a given failed
   * 
   * @param failedId
   *          The failed Id
   * @return ArrayList of Action Ids for actions to be executed
   * @throws IllegalStateException
   *           when this object is already closed
   */
  private ArrayList<Long> getFailedActionIds(int failureType) {
    if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }

    Cursor cursor = failedActionsDbAdapter.fetchAll(null, null, failureType);
    ArrayList<Long> ruleActionIds = new ArrayList<Long>();
    while (cursor.moveToNext()) {
      ruleActionIds.add(getLongFromCursor(cursor, FailedActionsDbAdapter.KEY_FAILEDACTIONID));
    }
    cursor.close();
    return ruleActionIds;
  }

  /**
   * This method gives the registered action name and its application name for a failed action
   * 
   * @param failedActionId
   *          The failed Action's Id
   * @return String array {application name, action name}. Null if cannot find action name,
   *         application name or action id
   * @throws IllegalStateException
   *           when this object is already closed
   */
  private String[] getRegisteredActionInfo(Long failedActionId) {
    if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }

    Long actionId;
    Long appId;
    String actionName;
    String appName;

    Cursor cursor = failedActionsDbAdapter.fetch(failedActionId);
    if (cursor.moveToFirst()) {
      actionId = getLongFromCursor(cursor, FailedActionsDbAdapter.KEY_ACTIONID);
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
  public boolean delete(long failedActionId) {
    return failedActionParameterDbAdapter.delete(failedActionId) && 
        failedActionsDbAdapter.delete(failedActionId);
  }
  
  public long insert (Intent intent, int failureType, String message ) {
 
    long ruleActionId = intent.getLongExtra(Action.DATABASE_ID, -1);
    Logger.w(TAG, "ruleActionId aris" + ruleActionId);
    Cursor cursor = ruleActionDbAdapter.fetch(ruleActionId);
    long ruleId = getLongFromCursor(cursor, RuleActionDbAdapter.KEY_RULEID);
    long actionId = getLongFromCursor(cursor, RuleActionDbAdapter.KEY_ACTIONID);
    long failedActionId = failedActionsDbAdapter.insert(ruleId, actionId, failureType, message);
    
    Logger.w(TAG, "inserting action into database, failure type "+ failureType);
    
    Bundle params = intent.getExtras();
    
    for (String paramName : params.keySet()) {
      if (!paramName.equals(Action.DATABASE_ID) && !paramName.equals(Action.ACTION_TYPE) &&
          !paramName.equals(Action.NOTIFICATION)) {
        failedActionParameterDbAdapter.insert(failedActionId, paramName, 
            params.getString(paramName));
      }
    }
    return failedActionId;
  }

  public boolean deleteAll () {
    return failedActionParameterDbAdapter.deleteAll() &&
        failedActionsDbAdapter.deleteAll();
  }
  /**
   * updates failureType with new result;
   * 
   * @param intent
   *           intent of the action to be updated
   * @param result
   *           new cause of failure
   * @param result 
   * @param message 
   */
  public void update(Intent intent, int result, String message)  {
      failedActionsDbAdapter.update(intent.getLongExtra(Action.DATABASE_ID, -1), 
          null, null, result, message);
  }
  /**
   * 
   */
  public void deleteOldActions() {
    if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }

    Cursor cursor = failedActionsDbAdapter.fetchOldActions();
    while (cursor.moveToNext()) {
      UtilUI.showNotification(context, UtilUI.NOTIFICATION_RULE, context.getString(R.string.libretasks), getStringFromCursor(cursor, FailedActionsDbAdapter.KEY_MESSAGE));
      failedActionsDbAdapter.delete(getLongFromCursor(cursor, 
          FailedActionsDbAdapter.KEY_FAILEDACTIONID));
    }
    cursor.close();
  }
}
