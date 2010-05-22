/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid 
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
package edu.nyu.cs.omnidroid.app.core;

import java.util.HashMap;
import android.content.Intent;
import edu.nyu.cs.omnidroid.app.util.ExceptionMessageMap;
import edu.nyu.cs.omnidroid.app.util.OmnidroidException;

/**
 * This action wraps an intent to send to Twitter Update Status service. It contains necessary information for the
 * Twitter update service.
 */
public class UpdateTwitterStatusAction extends Action {

  //  attributes field names
  public static final String ACTION_NAME = "UPDATE TWITTER";
  public static final String APP_NAME = "Twitter";
  public static final String TWITTER_INTENT = "omnidroid.intent.action.TWITTER_UPDATE";

  public static final String PARAM_USERNAME = "Username";
  public static final String PARAM_PASSWORD = "Password";
  public static final String PARAM_MESSAGE = "Message";
  
  // Content of the action
  private String username;
  private String password;
  private String message;

  public UpdateTwitterStatusAction(HashMap<String, String> parameters) throws OmnidroidException {
    super(UpdateTwitterStatusAction.TWITTER_INTENT, Action.BY_SERVICE);
    username = parameters.get(PARAM_USERNAME);
    password = parameters.get(PARAM_PASSWORD);
    message = parameters.get(PARAM_MESSAGE);
    if (username == null || password == null || message == null) {
      throw new OmnidroidException(120002, ExceptionMessageMap.getMessage(new Integer(120002)
          .toString()));
    }
  }

  @Override
  public Intent getIntent() {
    Intent intent = new Intent(getActionName());
    intent.putExtra(PARAM_USERNAME, username);
    intent.putExtra(PARAM_PASSWORD, password);
    intent.putExtra(PARAM_MESSAGE, message);
    return intent;
  }

}
