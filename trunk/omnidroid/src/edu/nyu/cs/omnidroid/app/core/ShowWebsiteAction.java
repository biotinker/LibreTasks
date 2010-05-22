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

import java.util.Map;

import edu.nyu.cs.omnidroid.app.util.ExceptionMessageMap;
import edu.nyu.cs.omnidroid.app.util.OmnidroidException;
import edu.nyu.cs.omnidroid.app.util.ShowWebsiteActivity;
import android.content.Intent;

/**
 * Simple action that shows a website
 *
 */
public class ShowWebsiteAction extends OmniAction {

  public static final String ACTION_NAME = "Show Web Site";
  public static final String PARAM_WEB_URL = "WEB_URL";
  
  private String url = null;

  public ShowWebsiteAction(Map<String, String> parameters) throws OmnidroidException {
    super(ShowWebsiteActivity.class.getName(), Action.BY_ACTIVITY);
    url = parameters.get(PARAM_WEB_URL);
    if (url == null) {
      throw new OmnidroidException(120002, ExceptionMessageMap.getMessage(new Integer(120002)
      .toString()));
    }
  }

  @Override
  public Intent getIntent() {
    Intent intent = new Intent();
    intent.setClassName(OMNIDROID_PACKAGE_NAME, ShowWebsiteActivity.class.getName());
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(PARAM_WEB_URL, url);
    return intent;
  }
}
