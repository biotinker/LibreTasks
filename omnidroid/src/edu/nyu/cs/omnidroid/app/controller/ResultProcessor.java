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
package edu.nyu.cs.omnidroid.app.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.model.FailedActionsDbHelper;
import edu.nyu.cs.omnidroid.app.view.simple.UtilUI;

/**
 * This class processes results after action is executed. 
 * In case of failure it stores action to be executed when resources are available. 
 */
public class ResultProcessor {
  
  /** This result code is used when action was successfully executed*/
  public static final int RESULT_SUCCESS = 0;
  /** if action is processes with this type of failure
   *  will be executed when service becomes available */
  public static final int RESULT_FAILURE_SERVICE = 1;
  /** if action is processes with this type of failure
   *  will be executed when internet becomes available */
  public static final int RESULT_FAILURE_INTERNET = 2;
  /** if action is processes with this type of failure
   *  will be executed in a minute */
  public static final int RESULT_FAILURE_UNKNOWN = 3;
  /** this type of failure is typically used when username/password is incorrect,
   * actions with this type of failure can't be recovered, therefore they aren't queued*/
  public static final int RESULT_FAILURE_IRRECOVERABLE = 4;
  
  public static final String TAG = ResultProcessor.class.getSimpleName();

  /**
   * @param context
   *        context in which database will be initialized
   * @param intent
   *        intent of an action to be processes
   * @param result
   *        integer identifying success or cause of failure
   */
  public static void process(Context context, Intent intent, int result, String message) {
    FailedActionsDbHelper failedActionsDbHelper = new FailedActionsDbHelper(context);
    String actionType = intent.getStringExtra(Action.ACTION_TYPE);
    long databaseId = intent.getLongExtra(Action.DATABASE_ID, -1L);
    boolean showNotification = intent.getBooleanExtra(Action.NOTIFICATION, true);
    //TODO add support for 3rd party actions 
       
    switch (result) {
    case RESULT_SUCCESS :
      if (actionType.equals(Action.RULE_ACTION)) {
        //TODO move action log here
      } else if (actionType.equals(Action.FAILED_ACTION)) {
        failedActionsDbHelper.delete(new Long(databaseId));
      }
      notifyResult(context, showNotification, message);
      break;
    case RESULT_FAILURE_SERVICE:
    case RESULT_FAILURE_INTERNET:
    case RESULT_FAILURE_UNKNOWN:
      if (actionType.equals(Action.RULE_ACTION)) {
        failedActionsDbHelper.insert(intent, result, message);
      } else if (actionType.equals(Action.FAILED_ACTION)) {
        failedActionsDbHelper.update(intent, result, message);
      } 
      notifyResult(context, false, message);
      break;
    case RESULT_FAILURE_IRRECOVERABLE:
      if (actionType.equals(Action.FAILED_ACTION)) {
        failedActionsDbHelper.delete(databaseId);
      }
      notifyResult(context, showNotification, message);
      break;    
    default :
      failedActionsDbHelper.close();
      throw new IllegalArgumentException();
    }
    failedActionsDbHelper.close();
  }
  
  private static void notifyResult(Context context, boolean showNotification, String message){
    if (message != null) {
      if (showNotification) {
        UtilUI.showNotification(context, UtilUI.NOTIFICATION_RULE, 
            context.getString(R.string.omnidroid), message);
      } else {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
      }
    }
  }
  
}