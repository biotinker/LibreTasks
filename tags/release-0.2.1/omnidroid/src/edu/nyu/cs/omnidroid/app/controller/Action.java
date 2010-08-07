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
package edu.nyu.cs.omnidroid.app.controller;

import android.content.Intent;

/**
 * Action provides a general wrapper for the intent to be fired. Classes that extend Action
 * should provide the intent to be fired and execution method used to fire the intent.
 */
public abstract class Action {

  /** Execution Methods */
  public static final String BY_ACTIVITY = "action.execution.ByActivity";
  public static final String BY_SERVICE = "action.execution.ByService";
  public static final String BY_BROADCAST = "action.execution.ByBroadcast";
  
  /** Keyword */
  public static final String NOTIFICATION = "notification";
  public static final String DATABASE_ID = "DatabaseId";
  public static final String ACTION_TYPE = "ActionType";
  
  /** Action name to be used in the intent */
  private final String actionName;
  protected String ruleName;

  /** Shows whether notification service is on or off for this Action*/
  protected boolean showNotification;
  
  /** The execution method used to fire an intent */
  private final String executionMethod;
  
  /** Action type constants  */
  public static final String RULE_ACTION = "RuleAction";
  public static final String FAILED_ACTION = "FailedAction";
  
  /** 
   * type of action, indicates whether action is retrieved 
   * from RuleActions or FailedActions table.
   */
  protected String actionType = null;
  
  /**
   * this is used in conjunction with actionType to provide access 
   * to the database entry where this action is stored.
   * */
  protected long databaseId;
  /**
   * Create a new Action.
   * 
   * @param actionName
   *          Action name to be used in the intent
   * @param executionMethod
   *          The execution method used to fire an intent (activity, service or broadcast)
   */
  public Action(String actionName, String executionMethod) {
    this.actionName = actionName;
    this.executionMethod = executionMethod;
    this.showNotification = true;
  }

  /**
   * Returns an intent that can be fired to perform this action.
   * 
   * @return Intent to be fired
   */
  public abstract Intent getIntent();

  /**
   * Returns the execution method used to fire an intent.
   * 
   * @return Execution Method for the intent
   */
  public final String getExecutionMethod() {
    return executionMethod;
  }

  /**
   * Returns the action name used in the intent.
   * 
   * @return Action name used in the intents
   */
  public String getActionName() {
    return actionName;
  }

  /**
   * @return Description of this type of action, usually used for display to user
   */
  abstract public String getDescription();

  public String getRuleName() {
    return ruleName;
  }

  public abstract String getAppName();

  public String getParameters() {
    if (getIntent().getExtras() != null) {
      return getIntent().getExtras().toString();
    } else {
      return "";
    }
  }

  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;    
  }
  public void setDatabaseId(long databaseId) {
    this.databaseId = databaseId;
  }

  public long getDatabaseId() {
    return databaseId;
  }
  public void setActionType(String actionType) {
    if (actionType.equals(RULE_ACTION) || actionType.equals(FAILED_ACTION)) {
      this.actionType = actionType;
    } else {
      throw new IllegalArgumentException ();
    }
  }
  public void setNotification(Boolean notification) {
    this.showNotification = notification;
  }
  
  public boolean showsNotification() {
    return this.showNotification;
  }
  
}
