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
package edu.nyu.cs.omnidroid.app.controller.external.actions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.Action;
import edu.nyu.cs.omnidroid.app.controller.ResultProcessor;
import edu.nyu.cs.omnidroid.app.controller.actions.ShowNotificationAction;
import edu.nyu.cs.omnidroid.app.controller.actions.UpdateTwitterStatusAction;
import edu.nyu.cs.omnidroid.app.model.db.DbHelper;
import edu.nyu.cs.omnidroid.app.model.db.RegisteredAppDbAdapter;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.widget.Toast;

import winterwell.jtwitter.*;
import winterwell.jtwitter.TwitterException.*;

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
  private boolean notificationIsOn;
  
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
    notificationIsOn = intent.getBooleanExtra(Action.NOTIFICATION, false);
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
      twitter.setSource(getString(R.string.omnidroid));
      twitter.setStatus(message);
      showNotification(getString(R.string.twitter_updated));
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS);
    } catch(E401 e){
      showNotification(getString(R.string.twitter_failed) + getString(R.string.separator_comma)
          + getString(R.string.authentication_error));  
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_IRRECOVERABLE);
    } catch (E403 e) {
      showNotification(getString(R.string.twitter_failed));
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_IRRECOVERABLE);
    } catch (E404 e) {
      showNotification(getString(R.string.twitter_failed));
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_IRRECOVERABLE);
    } catch (E50X e) {
      showNotification(getString(R.string.twitter_failed));
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_UNKNOWN);
    } catch (RateLimit e) {
      showNotification(getString(R.string.twitter_failed));
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_UNKNOWN);
    } catch (Timeout e) {
      showNotification(getString(R.string.twitter_failed));
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_UNKNOWN);
    }    
  }
  
  private  void showNotification(String message) {
    if (notificationIsOn) {
      Intent intent = new Intent();
      intent.setClassName(ShowNotificationAction.OMNIDROID_PACKAGE_NAME, OmniActionService.class.getName());
      intent.putExtra(OmniActionService.OPERATION_TYPE, OmniActionService.SHOW_NOTIFICATION_ACTION);
      intent.putExtra(ShowNotificationAction.PARAM_TITLE, getString(R.string.omnidroid));
      intent.putExtra(ShowNotificationAction.PARAM_ALERT_MESSAGE, message);
      startService(intent);
    } else {
      Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
  }
}
