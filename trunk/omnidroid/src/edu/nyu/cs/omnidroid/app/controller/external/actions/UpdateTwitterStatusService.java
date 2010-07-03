/*******************************************************************************
 * Copyright 2009, 2010 OmniDroid - http://code.google.com/p/omnidroid 
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
 * 
 * Uses JTwitter Library http://www.winterwell.com/software/jtwitter.php
 *******************************************************************************/
package edu.nyu.cs.omnidroid.app.controller.external.actions;

import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.actions.UpdateTwitterStatusAction;
import edu.nyu.cs.omnidroid.app.model.db.DbHelper;
import edu.nyu.cs.omnidroid.app.model.db.RegisteredAppDbAdapter;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import winterwell.jtwitter.*;

/**
 * This service can be used to Update Twitter Status
 * {@link UpdateTwitterStatusAction}. 
 */
public class UpdateTwitterStatusService extends Service {

  /**
   * attributes field names
   */
  private RegisteredAppDbAdapter.AccountCredentials account;
  private String message;
 
  /**
   * @return null because client can't bind to this service
   */
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private void extractUserCredentials() {
    DbHelper omniDbHelper = new DbHelper(this);
    SQLiteDatabase database = omniDbHelper.getWritableDatabase();
    RegisteredAppDbAdapter registeredAppDbAdapter = new RegisteredAppDbAdapter(database);
    
    account = registeredAppDbAdapter.getAccountCredentials(DbHelper.AppName.TWITTER, "");
    
    database.close();
    omniDbHelper.close(); 
  }
  
  @Override
  public void onStart(Intent intent, int startId) {
    super.onStart(intent, startId);
    
    extractUserCredentials();
    message = intent.getStringExtra(UpdateTwitterStatusAction.PARAM_MESSAGE);
    update();
  }

  /**
   * Update Twitter Status
   * 
   */
  private void update() {

    try{   
      Twitter twitter = new Twitter(account.accountName, account.credential);
      //TODO : To set the source to "OmniDroid" we first have to register the app with Twitter.
      twitter.setSource(getString(R.string.omnidroid));
      twitter.setStatus(message);
    }catch(TwitterException e){
      //TODO Exception is received when we try to update twitter with the same message. Log the 
      // exception.
    }
  }


}
