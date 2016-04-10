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
 * 
 * Uses JTwitter Library http://www.winterwell.com/software/jtwitter.php
 *******************************************************************************/
package libretasks.app.controller.external.actions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException.*;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import libretasks.app.R;
import libretasks.app.controller.ResultProcessor;
import libretasks.app.controller.actions.UpdateTwitterStatusAction;
import libretasks.app.model.db.DbHelper;
import libretasks.app.model.db.RegisteredAppDbAdapter;

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
  
  private Intent intent;
 
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
    this.intent = intent;
    
    extractUserCredentials();
    message = intent.getStringExtra(UpdateTwitterStatusAction.PARAM_MESSAGE);
    update();
  }

  /**
   * Update Twitter Status
   * 
   */
  private void update() {
    
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
    String time = dateFormat.format(calendar.getTime());
    message += " " + time;
    try {   
      Twitter twitter = new Twitter(account.accountName, account.credential);
      //TODO : To set the source to "Omnidroid" we first have to register the app with Twitter.
      //       http://twitter.com/apps/new (service was down when I tried)
      twitter.setSource(getString(R.string.libretasks));
      twitter.setStatus(message);
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS, 
          getString(R.string.twitter_updated));
    } catch(E401 e){  
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_IRRECOVERABLE,
          getString(R.string.twitter_failed_authentication_error));
    } catch (E403 e) {
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_IRRECOVERABLE,
          getString(R.string.twitter_failed));
    } catch (E404 e) {
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_IRRECOVERABLE,
          getString(R.string.twitter_failed));
    } catch (E50X e) {
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_UNKNOWN,
          getString(R.string.twitter_failed));
    } catch (RateLimit e) {
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_UNKNOWN,
          getString(R.string.twitter_failed));
    } catch (Timeout e) {
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_UNKNOWN,
          getString(R.string.twitter_failed));
    }    
  }

}
